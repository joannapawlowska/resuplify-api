package com.io.resuplifyapi.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="shop")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="url")
    private String url;

    @Column(name="token")
    private String token;

    @Column(name="token_refresh_date")
    private LocalDate tokenRefreshDate;

    @OneToOne(mappedBy="shop", cascade=CascadeType.ALL)
    private ShopOwner shopOwner;

    @OneToMany(mappedBy="shop", cascade=CascadeType.ALL)
    private List<Product> products;

    public Shop(){}

    public Shop(String url, String token, LocalDate tokenRefreshDate) {
        this.url = url;
        this.token = token;
        this.tokenRefreshDate = tokenRefreshDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String shopUrl) {
        this.url = shopUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDate getTokenRefreshDate() {
        return tokenRefreshDate;
    }

    public void setTokenRefreshDate(LocalDate tokenRefreshDate) {
        this.tokenRefreshDate = tokenRefreshDate;
    }

    public ShopOwner getUser() {
        return shopOwner;
    }

    public void setUser(ShopOwner shopOwner) {
        this.shopOwner = shopOwner;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product){
        if(products == null){
            products = new ArrayList<>();
        }
        products.add(product);
    }
}