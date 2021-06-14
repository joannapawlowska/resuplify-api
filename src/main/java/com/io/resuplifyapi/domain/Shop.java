package com.io.resuplifyapi.domain;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDefs;
import org.jasypt.hibernate5.type.EncryptedStringType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@TypeDefs({
        @TypeDef(
                name = "encryptedString",
                typeClass = EncryptedStringType.class,
                parameters = {
                        @Parameter(name = "encryptorRegisteredName", value = "stringEncryptor")
                }
        )
})
@Entity
@Table(name="shop")
public class Shop {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Type(type = "encryptedString")
    @Column(name="url")
    private String url;

    @Type(type="encryptedString")
    @Column(name="token")
    private String token;

    @Column(name="token_validity_date")
    private LocalDate tokenValidityDate;

    @OneToMany(mappedBy="shop", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Product> products;

    public Shop(){}

    public Shop(String url, String token, LocalDate tokenValidityDate) {
        this.url = url;
        this.token = token;
        this.tokenValidityDate = tokenValidityDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDate getTokenValidityDate() {
        return tokenValidityDate;
    }

    public void setTokenValidityDate(LocalDate tokenRefreshDate) {
        this.tokenValidityDate = tokenRefreshDate;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product) {
        if(products == null) products = new ArrayList<>();
        products.add(product);}
}