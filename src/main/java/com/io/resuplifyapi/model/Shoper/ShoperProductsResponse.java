package com.io.resuplifyapi.model.Shoper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = ShoperProductsResponseDeserializer.class)
public class ShoperProductsResponse {

    List<ShoperProduct> products;

    public ShoperProductsResponse(){}

    public ShoperProductsResponse(List<ShoperProduct> products) {
        this.products = products;
    }

    public List<ShoperProduct> getShoperProducts() {
        return products;
    }
}