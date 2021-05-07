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

    @Column(name="shoper_id")
    private int shoperId;

    @Column(name="warn_level")
    private int warnLevel;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="prediction_id")
    private Prediction prediction;

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

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public void addStock(Stock stock){
        stocks.add(stock);
    }

    public static class Builder {

        private int warnLevel;
        private Prediction prediction;
        private List<Stock> stocks;

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

        public Product build(){
            Product p = new Product();
            p.setWarnLevel(warnLevel);
            p.setPrediction(prediction);
            p.setStocks(stocks);
            return p;
        }
    }
}