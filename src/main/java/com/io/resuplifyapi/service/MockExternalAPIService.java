package com.io.resuplifyapi.service;

import com.io.resuplifyapi.domain.UserDto;
import com.io.resuplifyapi.domain.externalAPI.AuthResponse;
import com.io.resuplifyapi.exception.InvalidUrlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class MockExternalAPIService extends ExternalAPIService{

    @Autowired
    protected WebClient.Builder webClientBuilder;

    @Override
    protected AuthResponse authenticate(UserDto userDto) throws WebClientRequestException, WebClientResponseException, InvalidUrlException {

        return webClientBuilder
                .build()
                .get()
                .uri("https://48032270-3579-4a4f-843d-05b139e8eaf9.mock.pstmn.io/mock/external/api/success")
                .retrieve()
                .bodyToMono(AuthResponse.class)
                .switchIfEmpty(Mono.error(new InvalidUrlException()))
                .block();

    }
}