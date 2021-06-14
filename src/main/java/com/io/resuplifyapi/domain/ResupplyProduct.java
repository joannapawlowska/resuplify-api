package com.io.resuplifyapi.domain;

public class ResupplyProduct {

    private int id;
    private String name;
    private int stockLevel;
    private int demand;

    public ResupplyProduct(){}

    public ResupplyProduct(int id, String name, int stockLevel, int demand) {
        this.id = id;
        this.name = name;
        this.stockLevel = stockLevel;
        this.demand = demand;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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