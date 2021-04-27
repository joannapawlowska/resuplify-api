package com.io.resuplifyapi.domain;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="product")
public class Product {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="shoper_id")
    private int shoperId;

    @Column(name="sale_per_day")
    private Double salePerDay;

    @Column(name="out_of_stock_date")
    private LocalTime outOfStockDate;

    @Column(name="warn_level")
    private int warnLevel;

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
    @JoinColumn(name="product_id")
    private List<Stock> stocks;

    public Product() {}

    public Product(String name, int shoperId, int warnLevel) {
        this.name = name;
        this.shoperId = shoperId;
        this.warnLevel = warnLevel;
        this.stocks = new ArrayList<>();
    }

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getShoperId() {
        return shoperId;
    }

    public void setShoperId(int shoperId) {
        this.shoperId = shoperId;
    }

    public Double getSalePerDay() {
        return salePerDay;
    }

    public void setSalePerDay(Double salePerDay) {
        this.salePerDay = salePerDay;
    }

    public LocalTime getOutOfStockDate() {
        return outOfStockDate;
    }

    public void setOutOfStockDate(LocalTime outOfStockDate) {
        this.outOfStockDate = outOfStockDate;
    }

    public int getWarnLevel() {
        return warnLevel;
    }

    public void setWarnLevel(int warnLevel) {
        this.warnLevel = warnLevel;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public void addStock(Stock stock){
        stocks.add(stock);
    }
}