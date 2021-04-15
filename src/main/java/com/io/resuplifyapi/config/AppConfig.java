package com.io.resuplifyapi.config;

import com.io.resuplifyapi.repository.BulkProductsRequestBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Bean
    public WebClient.Builder getWebClientBuilder(){
        return WebClient.builder().codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(20 * 1024 * 1024));
    }

    @Bean
    public BulkProductsRequestBuilder getBulkProductsRequestBuilder(){
        return new BulkProductsRequestBuilder();
    }
}