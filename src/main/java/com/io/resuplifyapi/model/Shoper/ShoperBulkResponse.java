package com.io.resuplifyapi.model.Shoper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ShoperBulkResponse {

    @JsonProperty("items")
    List<ShoperProductsResponse> responses;

    public ShoperBulkResponse(){}

    public ShoperBulkResponse(List<ShoperProductsResponse> responses) {
        this.responses = responses;
    }

    public List<ShoperProductsResponse> getShoperResponses() {
        return responses;
    }

    public List<ShoperProduct> getShoperProducts() {
        return responses.stream()
                .flatMap(response -> response.getShoperProducts().stream())
                .collect(Collectors.toList());
    }
}