package com.io.resuplifyapi.domain.externalAPI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BulkResponse {

    @JsonProperty("items")
    private List<ProductsResponse> responses;

    boolean errors;

    public BulkResponse(){}

    public List<ProductsResponse> getProductsResponses() {
        return responses;
    }

    public boolean isErrors() {
        return errors;
    }

    public void setErrors(boolean errors) {
        this.errors = errors;
    }

    public boolean hasNoErrors(){
        return !errors;
    }
}