package com.io.resuplifyapi.model;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name="product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne(fetch = FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.MERGE,CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="shop_id")
    private Shop shop;

    @OneToMany(mappedBy = "product", cascade=CascadeType.ALL)
    private List<Stock> stocks;

    public Product() {}

    public Product(String name, int shoperId) {
        this.name = name;
        this.shoperId = shoperId;
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
}