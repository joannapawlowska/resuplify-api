package com.io.resuplifyapi.service;

import com.io.resuplifyapi.domain.Prediction;
import com.io.resuplifyapi.domain.Product;
import com.io.resuplifyapi.domain.Shop;
import com.io.resuplifyapi.domain.externalAPI.ProductModel;
import com.io.resuplifyapi.domain.Stock;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryBalanceService {
    
    private List<Product> products;
    private Shop shop;

    public void updateInventory(List<ProductModel> productModels, Shop shop) {

        this.shop = shop;
        this.products = shop.getProducts();
        productModels.stream()
                .filter(ProductModel::isActive)
                .forEach(this::mapModelToProduct);
    }

    private void mapModelToProduct(final ProductModel model){
        
        Optional<Product> optional = products.stream()
                .filter(product -> model.getId() == product.getShoperId())
                .findFirst();

        if (optional.isEmpty()) {
            addNewProduct(model);
        } else {
            addNewStockForProduct(model, optional.get());
        }
    }

    public void addNewProduct(ProductModel model){

        Product product = new Product(adjustToMaxLength(model.getName()), model.getId(), model.getWarnLevel(), shop);
        Stock stock = new Stock(LocalDate.now(), model.getStock());
        Prediction prediction = new Prediction(false);

        product.setPrediction(prediction);
        product.addStock(stock);
        products.add(product);
    }

    private String adjustToMaxLength(String name){
        int nameLength = name.length();
        return name.substring(0, Math.min(nameLength, 255));
    }

    public void addNewStockForProduct(ProductModel model, Product product){

        int stockLevel = model.getStock();
        int warnLevel = model.getWarnLevel();
        product.setWarnLevel(warnLevel);

        if(hasTodaysStock(product)){
            updateTodaysStock(product, stockLevel);
        }
        else if (hasMaxNumberOfStocks(product)) {
            replaceTheOldestStock(product, stockLevel);
        }
        else {
            addNewStock(product, stockLevel);
        }
    }

    private boolean hasTodaysStock(Product product) { return product.getStocks().stream().anyMatch(s -> s.getDate().equals(LocalDate.now())); }

    private void updateTodaysStock(Product product, int stockLevel){
        Stock stock = product.getStocks().stream()
                .filter(s -> s.getDate().equals(LocalDate.now()))
                .findFirst()
                .get();

        stock.setDate(LocalDate.now());
        stock.setLevel(stockLevel);
    }

    private boolean hasMaxNumberOfStocks(Product product){
        return product.getStocks().size() == 30;
    }

    private void replaceTheOldestStock(Product product, int stockLevel) {

        Stock stock = product.getStocks().stream()
                .min(Comparator.comparing(Stock::getDate))
                .get();

        stock.setDate(LocalDate.now());
        stock.setLevel(stockLevel);
    }

    private void addNewStock(Product product, int stockLevel) {
        product.addStock(new Stock(LocalDate.now(), stockLevel));
    }
}