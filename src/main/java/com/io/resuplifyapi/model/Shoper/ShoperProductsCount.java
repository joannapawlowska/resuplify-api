package com.io.resuplifyapi.model.Shoper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ShoperProductsCount {

    int count;

    public ShoperProductsCount(){}

    public ShoperProductsCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
