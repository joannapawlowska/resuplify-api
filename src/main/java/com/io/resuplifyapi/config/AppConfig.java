package com.io.resuplifyapi.config;

import com.io.resuplifyapi.domain.externalAPI.BulkRequestBuilder;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.io.resuplifyapi.repository")
public class AppConfig {

    @Bean
    public WebClient.Builder getWebClientBuilder(){ return WebClient.builder().codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(20 * 1024 * 1024)); }

    @Bean
    public BulkRequestBuilder getBulkRequestBuilder(){
        return new BulkRequestBuilder();
    }

    @Bean
    public SimpleRegression getSimpleRegression() { return new SimpleRegression(); }
}
