package com.io.resuplifyapi.model.Shoper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = ShoperProductDeserializer.class)
public class ShoperProduct {

    private int id;
    private String name;
    private int stock;
    private boolean active;
    private int warnLevel;

    public ShoperProduct() {}

    public ShoperProduct(int id, String name, int stock, boolean active, int warnLevel) {
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.active = active;
        this.warnLevel = warnLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getWarnLevel() {
        return warnLevel;
    }

    public void setWarnLevel(int warnLevel) {
        this.warnLevel = warnLevel;
    }
}