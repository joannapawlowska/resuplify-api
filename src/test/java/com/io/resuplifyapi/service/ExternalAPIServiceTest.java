package com.io.resuplifyapi.service;

import com.io.resuplifyapi.domain.UserDto;
import com.io.resuplifyapi.exception.ExternalAPIAuthException;
import com.io.resuplifyapi.exception.ExternalAPICallException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.net.URI;
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
        String url = UUID.randomUUID().toString();
        String token = UUID.randomUUID().toString();

        doThrow(responseException(500, "Internal Server Error"))
                .when(serviceSpy)
                .requestForTotalProductsNumber(url, token);

        Throwable exceptionThrown = assertThrows(ExternalAPICallException.class, () -> serviceSpy.requestForProducts(url, token));
        assertThat(exceptionThrown.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR");
    }

    public WebClientResponseException responseException(int statusCode, String statusText) {
        return new WebClientResponseException(statusCode, statusText, null, null, null);
    }

    @Test
    public void shouldThrowWhenInvalidUrl(){
        ExternalAPIService serviceSpy = spy(service);
        UserDto userDto = new UserDto();

        doThrow(requestException(UUID.randomUUID().toString()))
                .when(serviceSpy)
                .authenticate(userDto);

        Throwable exceptionThrown = assertThrows(ExternalAPIAuthException.class, () -> serviceSpy.authenticateShopOwner(userDto));
        assertThat(exceptionThrown.getMessage()).isEqualTo("Invalid url");
    }

    public WebClientRequestException requestException(String invalidUrl) {
        return new WebClientRequestException(new Throwable(), HttpMethod.POST, URI.create(invalidUrl), HttpHeaders.EMPTY);
    }

    @Test
    public void shouldThrowWhenInvalidUserCredentials(){
        ExternalAPIService serviceSpy = spy(service);
        UserDto userDto = new UserDto();

        doThrow(responseException(401, "Unauthorized"))
                .when(serviceSpy)
                .authenticate(userDto);

        Throwable exceptionThrown = assertThrows(ExternalAPIAuthException.class, () -> serviceSpy.authenticateShopOwner(userDto));
        assertThat(exceptionThrown.getMessage()).isEqualTo("Invalid username or password");
    }

    @Test
    public void shouldThrowWhenExternalApiIsDow(){
        ExternalAPIService serviceSpy = spy(service);
        UserDto userDto = new UserDto();

        doThrow(responseException(500, "Internal Server Error"))
                .when(serviceSpy)
                .authenticate(userDto);

        Throwable exceptionThrown = assertThrows(ExternalAPIAuthException.class, () -> serviceSpy.authenticateShopOwner(userDto));
        assertThat(exceptionThrown.getMessage()).isEqualTo("Could not authorize due to server error");
    }
}