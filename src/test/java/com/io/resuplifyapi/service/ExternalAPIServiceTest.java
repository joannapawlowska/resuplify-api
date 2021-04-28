package com.io.resuplifyapi.service;

import com.io.resuplifyapi.exception.ExternalAPICallException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

class ExternalAPIServiceTest {

    private ExternalAPIService service;

    @BeforeEach
    public void init() {
        service = new ExternalAPIService();
    }

    @Test
    public void shouldThrowWhenExternalApiIsDown() {
        ExternalAPIService serviceSpy = spy(service);

        doThrow(responseException(500, "Internal Server Error"))
                .when(serviceSpy)
                .requestForTotalProductsNumber();

        Throwable exceptionThrown = assertThrows(ExternalAPICallException.class, () -> serviceSpy.requestForProducts(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString()
        ));

        assertThat(exceptionThrown.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR");
    }

    public WebClientResponseException responseException(int statusCode, String statusText) {
        return new WebClientResponseException(
                statusCode,
                statusText,
                null,
                null,
                null
        );
    }
}