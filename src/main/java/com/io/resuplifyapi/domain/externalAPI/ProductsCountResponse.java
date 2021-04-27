package com.io.resuplifyapi.domain.externalAPI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductsCountResponse {

    int count;

    public ProductsCountResponse(){}

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}