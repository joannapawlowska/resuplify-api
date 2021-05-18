package com.io.resuplifyapi.service;

import com.io.resuplifyapi.domain.Product;
import com.io.resuplifyapi.domain.Stock;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.math3.util.Precision;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class PredictorService {

    private final int MIN_STOCK_NUMBER_TO_PREDICT = 2;
    private final int MAX_DAYS_AHEAD_WHEN_PREDICTION_VALID = 30;
    private Product product;
    private List<Stock> stocks;
    private List<List<Stock>> stocksSubLists;
    List<List<Integer>> timeSeries;

    public void updatePrediction(Product product) {

        if (canBePredicted(product)) {
            recalculatePrediction(product);
        } else {
            product.getPrediction().setValid(false);
        }
    }

    private boolean canBePredicted(Product product) {
        List<Stock> stocks = product.getStocks();
        return enoughStocks(stocks) && !constantStockLevels(stocks);
    }

    private boolean enoughStocks(List<Stock> stocks){
        return stocks.size() >= MIN_STOCK_NUMBER_TO_PREDICT;
    }

    private boolean constantStockLevels(List<Stock> stocks) {
        int stockValue = stocks.get(0).getLevel();
        return stocks.stream()
                .map(Stock::getLevel)
                .allMatch(stock -> stock.equals(stockValue));
    }

    private void recalculatePrediction(Product product) {
        setGlobalVariables(product);
        sortStocksByDate();
        splitStocksIntoSubListsIfDeliveryOccurred();
        removeNotValidSubLists();
        convertStockSubListsIntoTimeSeries();
        calculatePredictionParams();
    }

    private void setGlobalVariables(Product product) {
        this.product = product;
        this.stocks = product.getStocks();
        this.stocksSubLists = new ArrayList<>();
        this.timeSeries = new ArrayList<>();
    }

    private void sortStocksByDate() {
        stocks = stocks.stream()
                .sorted(Comparator.comparing(Stock::getDate))
                .collect(Collectors.toList());
    }

    private void splitStocksIntoSubListsIfDeliveryOccurred() {

        List<Stock> subList = new ArrayList<>();
        Stock stockDayBefore = stocks.get(0);

        for (Stock currentStock : stocks) {

            if (currentStock.getLevel() > stockDayBefore.getLevel()) {
                stocksSubLists.add(subList);
                subList = new ArrayList<>();
            }
            subList.add(currentStock);
            stockDayBefore = currentStock;
        }

        if(!stocksSubLists.contains(subList)) stocksSubLists.add(subList);

    }

    private void removeNotValidSubLists(){
        stocksSubLists = stocksSubLists.stream()
                        .filter(sublist -> sublist.size() > 1)
                        .filter(Predicate.not(sublist -> sublist.stream().allMatch(stock -> stock.getLevel() == 0)))
                        .collect(Collectors.toList());

        if(stocksSubLists.size() == 1 && constantStockLevels(stocksSubLists.get(0)))
            stocksSubLists.clear();
    }

    private void convertStockSubListsIntoTimeSeries() {

        List<Integer> timeSubSeries;

        for (List<Stock> sublist : stocksSubLists) {
            timeSubSeries = sublist.stream()
                    .map(Stock::getLevel)
                    .collect(Collectors.toList());

            timeSeries.add(timeSubSeries);
        }
    }

    private void calculatePredictionParams(){

        if(!timeSeries.isEmpty()){
            calculateSalePerDay();
            calculateWarnLevelDate();
        }
        else{
            product.getPrediction().setValid(false);
        }
    }

    private void calculateSalePerDay() {
        double salePerDay = (-1) * AveragedLinearRegression.getSlope(timeSeries);
        product.getPrediction().setSalePerDay(salePerDay);
    }

    private void calculateWarnLevelDate() {

        int availableUnits = getAvailableStockLevel();
        int numberOfDays = getNumberOfDaysBeforeWarnLevelReached(availableUnits, product.getPrediction().getSalePerDay());

        if(numberOfDays > MAX_DAYS_AHEAD_WHEN_PREDICTION_VALID){
            product.getPrediction().setValid(false);
        }
        else {
            setPredictionParams(availableUnits, numberOfDays);
        }
    }

    private int getAvailableStockLevel(){
        int stockLevel = stocks.get(stocks.size() - 1).getLevel();
        int warnLevel = product.getWarnLevel();
        return Math.max(stockLevel - warnLevel, 0);
    }

    private int getNumberOfDaysBeforeWarnLevelReached(int availableUnits, double salePerDay){
        return (int) Math.floor(availableUnits / salePerDay);
    }

    private void setPredictionParams(int availableUnits, int numberOfDays){

        LocalDate date;

        if (availableUnits > 0){
            date = LocalDate.now().plusDays(numberOfDays);
        }
        else{
            date = LocalDate.now();
        }

        product.getPrediction().setWarnLevelDate(date);
        product.getPrediction().setValid(true);
    }

    //Performs linear regressions for each time series and then averages
    //the slope from the determined lines using a weighted moving average
    private static class AveragedLinearRegression {

        private final SimpleRegression regression;
        private final List<List<Integer>> timeSeries;
        private final Map<List<Integer>, Double> regressionLineSlopeByTimeSubSeries;
        private double averagedSlope;

        private AveragedLinearRegression(List<List<Integer>> theTimeSeries){
            timeSeries = theTimeSeries;
            regression = new SimpleRegression();
            regressionLineSlopeByTimeSubSeries = new HashMap<>();
        }

        public static double getSlope(List<List<Integer>> timeSeries){
            var var = new AveragedLinearRegression(timeSeries);
            var.calculateSlopesForEachTimeSeries();
            var.averageLineSlopes();
            return var.averagedSlope;
        }

        private void calculateSlopesForEachTimeSeries() {

            double slope;

            for (List<Integer> timeSubSeries : timeSeries) {

                if(isConstant(timeSubSeries)) slope = 0;
                else slope = calculateSlope(timeSubSeries);

                regressionLineSlopeByTimeSubSeries.put(timeSubSeries, slope);
            }
        }

        private boolean isConstant(List<Integer> timeSubSeries) {
            int value = timeSubSeries.get(0);
            return timeSubSeries.stream()
                    .allMatch(i -> i == value);
        }

        private double calculateSlope(List<Integer> timeSeries){

            regression.clear();
            int independentVariable = 1;

            for (Integer data : timeSeries) {

                int x = independentVariable++;
                int y = data;
                regression.addData(x, y);
            }
            return regression.getSlope();
        }

        private void averageLineSlopes() {

            double numerator = 0;
            int denominator = 0;
            int weight = 1;

            for (List<Integer> timeSubSeries : timeSeries) {

                double slope = regressionLineSlopeByTimeSubSeries.get(timeSubSeries);
                int timeSubSeriesSize = timeSubSeries.size();

                while (timeSubSeriesSize-- != 0) {
                    numerator += weight * slope;
                    denominator += weight++;
                }
            }
            averagedSlope = Precision.round(numerator / denominator, 3);
        }
    }
}