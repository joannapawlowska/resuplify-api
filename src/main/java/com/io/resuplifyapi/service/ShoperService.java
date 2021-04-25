package com.io.resuplifyapi.service;

import com.io.resuplifyapi.model.Shoper.ShoperBulkResponse;
import com.io.resuplifyapi.model.Shoper.ShoperProduct;
import com.io.resuplifyapi.model.Shoper.ShoperProductsCount;
import com.io.resuplifyapi.model.Shoper.ShoperProductsRequest;
import com.io.resuplifyapi.model.Shoper.ShoperBulkRequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoperService{

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    ShoperBulkRequestBuilder shoperBulkRequestBuilder;

    private List<List<ShoperProductsRequest>> bulkRequestsList;
    private List<ShoperProduct> shoperProducts;
    private int totalProductsNumber;
    private String url;
    private String token;

    public List<ShoperProduct> getProductsFromShop(String url, String token) {

        this.url = url;
        this.token = token;

        requestForTotalProductsNumber();
        prepareBulkProductsRequests();
        performBulkProductsRequests();
        return shoperProducts;
    }

    private void requestForTotalProductsNumber() {

        ShoperProductsCount shoperProductsCount = webClientBuilder
                .build()
                .get()
                .uri("https://" + url + "/webapi/rest/products/?page=1&limit=1")
                .headers(h -> h.setBearerAuth(token))
                .retrieve()
                .bodyToMono(ShoperProductsCount.class)
                .block();

        totalProductsNumber = shoperProductsCount.getCount();
    }

    private void prepareBulkProductsRequests() {
        bulkRequestsList = shoperBulkRequestBuilder.prepareProductsRequests(totalProductsNumber);
    }

    private void performBulkProductsRequests() {
        shoperProducts = bulkRequestsList
                .stream()
                .parallel()
                .map(this::getShoperProducts)
                .flatMap(Flux::toStream)
                .collect(Collectors.toList());
    }

    private Flux<ShoperProduct> getShoperProducts(List<ShoperProductsRequest> shoperBulkRequest) {
        return webClientBuilder
                .build()
                .post()
                .uri("https://" + url + "/webapi/rest/bulk")
                .headers(h -> h.setBearerAuth(token))
                .bodyValue(shoperBulkRequest)
                .retrieve()
                .bodyToMono(ShoperBulkResponse.class)
                .flatMapIterable(ShoperBulkResponse::getShoperProducts);
    }
}