package com.io.resuplifyapi.service;

import com.io.resuplifyapi.domain.UserDto;
import com.io.resuplifyapi.domain.externalAPI.*;
import com.io.resuplifyapi.exception.ExternalAPIAuthException;
import com.io.resuplifyapi.exception.ExternalAPICallException;
import com.io.resuplifyapi.exception.ExternalAPIUnavailableException;
import com.io.resuplifyapi.exception.InvalidUrlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.retry.Repeat;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Service
public class ExternalAPIService {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private BulkRequestBuilder bulkRequestBuilder;

    private List<ProductModel> productModels;
    private int totalProductsNumber;

    public List<ProductModel> requestForProducts(String url, String token) throws ExternalAPICallException {

        try {
            requestForTotalProductsNumber(url, token);
            performBulkRequestsForProductModels(url, token);

        } catch (WebClientResponseException e) {
            throw new ExternalAPICallException(String.valueOf(e.getStatusCode()));
        }

        return productModels;
    }

    protected void requestForTotalProductsNumber(String url, String token) throws WebClientResponseException {

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

    private void performBulkRequestsForProductModels(String url, String token) throws WebClientResponseException {

        List<List<ProductsRequest>> bulkRequestsList = prepareBulkRequests();

        productModels = bulkRequestsList
                .parallelStream()
                .map(bulkRequest -> requestForProducts(bulkRequest, url, token))
                .flatMap(Flux::toStream)
                .collect(Collectors.toList());
    }

    private List<List<ProductsRequest>> prepareBulkRequests() {
        return bulkRequestBuilder.prepareProductsRequests(totalProductsNumber);
    }

    private Flux<ProductModel> requestForProducts(List<ProductsRequest> bulkRequest, String url, String token) throws WebClientResponseException {
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

    public AuthResponse authenticateUserAccount(UserDto userDto) throws ExternalAPIAuthException, ExternalAPIUnavailableException {

        try{
            return authenticate(userDto);
        }
        catch (WebClientRequestException | InvalidUrlException e){
            throw new ExternalAPIAuthException("Invalid url");

        }catch(WebClientResponseException e){

            if(e.getRawStatusCode() == 401)
                throw new ExternalAPIAuthException("Invalid username or password");

            else if(e.getStatusCode().is5xxServerError())
                throw new ExternalAPIUnavailableException("Could not authorize");

            else
                throw new ExternalAPIAuthException("Invalid url");
        }
    }

    protected AuthResponse authenticate(UserDto userDto) throws WebClientRequestException, WebClientResponseException {

                return webClientBuilder
                        .build()
                        .post()
                        .uri("https://" + userDto.getUrl() + "/webapi/rest/auth")
                        .headers(h -> h.setBasicAuth(userDto.getUsername(), userDto.getPassword()))
                        .retrieve()
                        .bodyToMono(AuthResponse.class)
                        .timeout(Duration.ofSeconds(7))
                        .onErrorMap(TimeoutException.class, e -> new InvalidUrlException())
                        .switchIfEmpty(Mono.error(new InvalidUrlException()))
                        .block();

    }
}