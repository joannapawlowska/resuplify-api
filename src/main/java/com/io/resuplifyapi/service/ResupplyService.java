package com.io.resuplifyapi.service;

import com.io.resuplifyapi.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ResupplyService {

    @Autowired
    private ProductService productService;

    public ResupplyService(ProductService productService){
        this.productService = productService;
    }

    @Transactional
    public List<ResupplyProduct> getProducts(int shopId, LocalDate date){

        List<Product> products = productService.findAllByResupplyCriteria(shopId, date);

        return convertToProductsResponseList(products, date);
    }

    private List<ResupplyProduct> convertToProductsResponseList(List<Product> products, LocalDate date) {

        List<ResupplyProduct> resupplyProducts = new ArrayList<>();

        for(Product p : products){

            int stockLevel = getCurrentStockLevel(p);
            int demand = getDemand(p, date);
            resupplyProducts.add(new ResupplyProduct(p.getExternalId(), p.getName(), stockLevel, demand));
        }

        return resupplyProducts;
    }

    private int getCurrentStockLevel(Product product){
        return product.getStocks().stream().max(Comparator.comparing(Stock::getDate)).get().getLevel();
    }

    private int getDemand(Product p, LocalDate date){

        int amountOfDays = (int) ChronoUnit.DAYS.between(p.getPrediction().getWarnLevelDate(), date);
        double salePerDay = p.getPrediction().getSalePerDay();

        return (int) Math.ceil(salePerDay * amountOfDays);
    }
}
