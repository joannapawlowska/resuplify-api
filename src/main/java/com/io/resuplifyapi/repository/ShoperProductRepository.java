package com.io.resuplifyapi.repository;

import com.io.resuplifyapi.model.Shoper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ShoperProductRepository {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired BulkProductsRequestBuilder productsRequestBuilder;

    Logger logger = LoggerFactory.getLogger(this.getClass());


    public List<ShoperProduct> getProductsFromShop(String url, String token){

        List<List<ShoperProductsRequest>> bulkRequests;
        int totalProductsNumber = getTotalProductsNumber(url, token);
        bulkRequests = productsRequestBuilder.prepare(totalProductsNumber);
        return getShoperProducts(bulkRequests, url, token);
    }

    private int getTotalProductsNumber(String url, String token){

        ShoperProductsCount shoperProductsCount = webClientBuilder
                .build()
                .get()
                .uri("https://" + url + "/webapi/rest/products/?page=1&limit=1")
                .headers(h -> h.setBearerAuth(token))
                .retrieve()
                .bodyToMono(ShoperProductsCount.class)
                .block();

        return shoperProductsCount.getCount();
    }

    private List<ShoperProduct> getShoperProducts(List<List<ShoperProductsRequest>> bulkRequests, String url, String token) {

        return bulkRequests.stream().map(bulkRequest -> {
            Flux<ShoperProduct> result = webClientBuilder
                    .build()
                    .post()
                    .uri("https://" + url + "/webapi/rest/bulk")
                    .headers(h -> h.setBearerAuth(token))
                    .bodyValue(bulkRequest)
                    .retrieve()
                    .bodyToMono(ShoperBulkResponse.class)
                    .flatMapIterable(ShoperBulkResponse::getShoperProducts);
            logger.info("shoper called");
            return result;
        })
                .flatMap(Flux::toStream)
                .collect(Collectors.toList());
    }
}
