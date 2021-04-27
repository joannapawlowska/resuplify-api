package com.io.resuplifyapi.service;

import com.io.resuplifyapi.domain.Product;
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

    public void updateInventory(List<ProductModel> productModels, List<Product> products) {
        
        this.products = products;
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

        Product product = new Product(adjustToMaxLength(model.getName()), model.getId(), model.getWarnLevel());
        Stock stock = new Stock(LocalDate.now(), model.getStock());

        product.addStock(stock);
        products.add(product);
    }

    private  String adjustToMaxLength(String name){
        int nameLength = name.length();
        return name.substring(0, Math.min(nameLength, 255));
    }

    public void addNewStockForProduct(ProductModel model, Product product){

        int stockLevel = model.getStock();
        int warnLevel = model.getWarnLevel();
        product.setWarnLevel(warnLevel);

        if (hasRequiredNumberOfStocks(product)) {
            updateTheOldestStock(product, stockLevel);
        } else {
            addNewStock(product, stockLevel);
        }
    }

    private boolean hasRequiredNumberOfStocks(Product product){
        return product.getStocks().size() == 30;
    }

    private void updateTheOldestStock(Product product, int stockLevel) {

        Stock stock = product.getStocks().stream()
                .min(Comparator.comparing(Stock::getDate))
                .get();

        stock.setDate(LocalDate.now());
        stock.setStock(stockLevel);
    }

    private void addNewStock(Product product, int stockLevel) {
        product.addStock(new Stock(LocalDate.now(), stockLevel));
    }
}