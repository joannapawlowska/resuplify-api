package com.io.resuplifyapi.service;

import com.io.resuplifyapi.model.Shoper.*;
import com.io.resuplifyapi.repository.ShoperProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShoperService {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private ShoperProductRepository shoperProductRepository;

    public List<ShoperProduct> getProductsFromShop(String url, String token){
        return shoperProductRepository.getProductsFromShop(url, token);
    }
}
