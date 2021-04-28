package com.io.resuplifyapi.service;

import com.io.resuplifyapi.domain.externalAPI.*;
import com.io.resuplifyapi.domain.externalAPI.ProductModel;

import com.io.resuplifyapi.exception.ExternalAPICallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.retry.Repeat;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExternalAPIService {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private BulkRequestBuilder bulkRequestBuilder;

    private List<List<ProductsRequest>> bulkRequestsList;
    private List<ProductModel> productModels;
    private int totalProductsNumber;
    private String url;
    private String token;

    public List<ProductModel> requestForProducts(String url, String token) throws ExternalAPICallException {

        this.url = url;
        this.token = token;

        try {
            requestForTotalProductsNumber();
            prepareBulkRequests();
            performBulkRequests();

        } catch (WebClientResponseException e) {
            throw new ExternalAPICallException(String.valueOf(e.getStatusCode()));
        }

        return productModels;
    }

    protected void requestForTotalProductsNumber() throws WebClientResponseException {

        ProductsCountResponse productsCountResponse = webClientBuilder
                .build()
                .get()
                .uri("https://" + url + "/webapi/rest/products/?page=1&limit=1")
                .headers(h -> h.setBearerAuth(token))
                .retrieve()
                .bodyToMono(ProductsCountResponse.class)
                .block();

        totalProductsNumber = productsCountResponse.getCount();
    }

    private void prepareBulkRequests() {
        bulkRequestsList = bulkRequestBuilder.prepareProductsRequests(totalProductsNumber);
    }

    private void performBulkRequests() throws ExternalAPICallException {
        productModels = bulkRequestsList
                .parallelStream()
                .map(this::requestForProducts)
                .flatMap(Flux::toStream)
                .collect(Collectors.toList());
    }

    private Flux<ProductModel> requestForProducts(List<ProductsRequest> bulkRequest) throws WebClientResponseException {
        return webClientBuilder
                .build()
                .post()
                .uri("https://" + url + "/webapi/rest/bulk")
                .headers(h -> h.setBearerAuth(token))
                .bodyValue(bulkRequest)
                .retrieve()
                .bodyToMono(BulkResponse.class)
                .filter(BulkResponse::hasNoErrors)
                .repeatWhenEmpty(Repeat.times(2))
                .flatMapIterable(BulkResponse::getProductsResponses)
                .flatMapIterable(ProductsResponse::getProductModels);
    }
}