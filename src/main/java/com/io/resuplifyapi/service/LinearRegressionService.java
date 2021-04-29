package com.io.resuplifyapi.service;

import com.io.resuplifyapi.domain.Prediction;
import com.io.resuplifyapi.domain.Product;
import com.io.resuplifyapi.domain.Stock;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class LinearRegressionService {

    private final SimpleRegression regression;
    private final int MIN_STOCK_NUMBER_TO_PREDICT = 3;
    private final int MAX_DAYS_AHEAD_WHEN_PREDICTION_VALID = 60;
    private Product product;
    private Prediction prediction;
    private List<Stock> stocks;
    private List<List<Stock>> stocksSubLists;
    private List<List<Integer>> timeSeries;
    private Map<List<Integer>, Double> regressionLineSlopeByTimeSubSeries;

    @Autowired
    public LinearRegressionService(SimpleRegression regression) {
        this.regression = regression;
    }

    public void performRegression(List<Product> products) {

        for (Product product : products) {
            performRegressionForProduct(product);
        }
    }

    protected void performRegressionForProduct(Product product) {
        setGlobalVariables(product);
        if (isEnoughValidDataToPredict()) {
            performRegression();
        } else {
            prediction.setValid(false);
        }
    }

    private void setGlobalVariables(Product product) {
        this.product = product;
        this.prediction = product.getPrediction();
        this.stocks = product.getStocks();
        this.stocksSubLists = new ArrayList<>();
        this.timeSeries = new ArrayList<>();
        this.regressionLineSlopeByTimeSubSeries = new HashMap<>();
    }

    private boolean isEnoughValidDataToPredict() {
        return stocks.size() >= MIN_STOCK_NUMBER_TO_PREDICT && !isStockConstantOnAllDays();
    }

    private boolean isStockConstantOnAllDays() {
        Predicate<Integer> firstDayStockValue = stock -> stock.equals(stocks.get(0).getStock());
        return stocks.stream()
                .map(Stock::getStock)
                .allMatch(firstDayStockValue);
    }

    private void performRegression() {
        sortStocksByDate();
        splitStocksIntoSubListsIfDeliveryOccurred();
        convertStockSubListsIntoTimeSeries();
        calculateRegressionLineSlopesForEachTimeSeries();
        calculateProductSalePerDayUsingWeightedMovingAverage();
        calculateOutOfStockDate();
    }

    private void sortStocksByDate() {
        stocks = stocks.stream()
                .sorted(Comparator.comparing(Stock::getDate))
                .collect(Collectors.toList());
    }

    private void splitStocksIntoSubListsIfDeliveryOccurred() {

        List<Stock> subList = new ArrayList<>();
        Stock previousStock = stocks.get(0);

        for (Stock stock : stocks) {

            if (stock.getStock() > previousStock.getStock()) {
                stocksSubLists.add(subList);
                subList = new ArrayList<>();
            }
            subList.add(stock);
            previousStock = stock;
        }
    }

    private void convertStockSubListsIntoTimeSeries() {

        List<Integer> timeSubSeries;

        for (List<Stock> sublist : stocksSubLists) {
            timeSubSeries = sublist.stream()
                    .map(Stock::getStock)
                    .collect(Collectors.toList());

            timeSeries.add(timeSubSeries);
        }
    }

    private void calculateRegressionLineSlopesForEachTimeSeries() {

        for (List<Integer> timeSubSeries : timeSeries) {

            regression.clear();
            int independentVariable = 1;

            for (Integer data : timeSubSeries) {

                int x = independentVariable++;
                int y = data;
                regression.addData(x, y);
            }

            regressionLineSlopeByTimeSubSeries.put(timeSubSeries, regression.getSlope());
        }
    }

    private void calculateProductSalePerDayUsingWeightedMovingAverage() {
        double numeratorWMA = 0;
        int denominatorWMA = 0;
        int weight = 1;

        for (List<Integer> timeSubSeries : timeSeries) {

            double slope = regressionLineSlopeByTimeSubSeries.get(timeSubSeries);
            int timeSubSeriesSize = timeSubSeries.size();

            while (timeSubSeriesSize-- != 0) {
                numeratorWMA += weight * slope;
                denominatorWMA += weight++;
            }
        }

        prediction.setSalePerDay(numeratorWMA / denominatorWMA);
    }

    private void calculateOutOfStockDate() {
        if (isSalePerDayZero()) {
            prediction.setValid(false);
        } else {
            calculateDate();
        }
    }

    private boolean isSalePerDayZero() {
        return prediction.getSalePerDay() == 0;
    }

    private void calculateDate() {
        double salePerDay = prediction.getSalePerDay();
        int stock = stocks.get(stocks.size() - 1).getStock();
        int warnLevel = product.getWarnLevel();
        int availableUnits = stock - warnLevel;
        int numberOfDaysUntilOutOfStock = (int) Math.ceil(availableUnits / salePerDay);

        if (numberOfDaysUntilOutOfStock <= MAX_DAYS_AHEAD_WHEN_PREDICTION_VALID) {
            LocalDate outOfStockDate = LocalDate.now().plusDays(numberOfDaysUntilOutOfStock);
            prediction.setOutOfStockDate(outOfStockDate);
        } else {
            prediction.setValid(false);
        }
    }
}