package com.io.resuplifyapi.domain.externalAPI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = ProductsResponseDeserializer.class)
public class ProductsResponse {

    private List<ProductModel> productModels;

    public ProductsResponse(){}

    public ProductsResponse(List<ProductModel> productModels) {
        this.productModels = productModels;
    }

    public List<ProductModel> getProductModels() {
        return productModels;
    }
}