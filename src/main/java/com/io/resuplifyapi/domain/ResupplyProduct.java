package com.io.resuplifyapi.domain;

public class ResupplyProduct {

    private int shoperId;
    private String name;
    private int stockLevel;
    private int demand;

    public ResupplyProduct(){}

    public ResupplyProduct(int shoperId, String name, int stockLevel, int demand) {
        this.shoperId = shoperId;
        this.name = name;
        this.stockLevel = stockLevel;
        this.demand = demand;
    }

    public int getShoperId() {
        return shoperId;
    }

    public void setShoperId(int shoperId) {
        this.shoperId = shoperId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(int stock) {
        this.stockLevel = stock;
    }

    public int getDemand() {
        return demand;
    }

    public void setDemand(int demand) {
        this.demand = demand;
    }
}
