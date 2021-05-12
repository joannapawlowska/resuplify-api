package com.io.resuplifyapi.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Column(name="external_id")
    private int externalId;

    @Column(name="warn_level")
    private int warnLevel;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="prediction_id")
    private Prediction prediction;

    @ManyToOne
    @JoinColumn(name="shop_id")
    private Shop shop;

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
    @JoinColumn(name="product_id")
    private List<Stock> stocks;

    public Product() {}

    public Product(String name, int externalId, int warnLevel, Shop shop) {
        this.name = name;
        this.externalId = externalId;
        this.warnLevel = warnLevel;
        this.shop = shop;
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

    public int getExternalId() {
        return externalId;
    }

    public void setExternalId(int shoperId) {
        this.externalId = shoperId;
    }

    public int getWarnLevel() {
        return warnLevel;
    }

    public void setWarnLevel(int warnLevel) {
        this.warnLevel = warnLevel;
    }

    public Prediction getPrediction() {
        return prediction;
    }

    public void setPrediction(Prediction prediction) {
        this.prediction = prediction;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public void addStock(Stock stock){
        if(stocks == null) this.stocks = new ArrayList<>();
        stocks.add(stock);
    }

    public static class Builder {

        private int id;
        private int warnLevel;
        private Prediction prediction;
        private List<Stock> stocks;
        private Shop shop;

        public static Builder builder(){
            return new Builder();
        }

        public Builder withWarnLevel(int warnLevel){
            this.warnLevel = warnLevel;
            return this;
        }

        public Builder withPrediction(Prediction prediction){
            this.prediction = prediction;
            return this;
        }

        public Builder withStocks(Stock ... stocks){
            if(this.stocks == null){
                this.stocks = new ArrayList<>();
            }
            this.stocks.addAll(Arrays.asList(stocks));
            return this;
        }

        public Builder withShop(Shop shop){
            this.shop = shop;
            return this;
        }

        public Product build(){
            Product p = new Product();
            p.setWarnLevel(warnLevel);
            p.setPrediction(prediction);
            p.setStocks(stocks);
            p.setShop(shop);
            return p;
        }
    }
}