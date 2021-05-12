package com.io.resuplifyapi.service;

import com.io.resuplifyapi.domain.Prediction;
import com.io.resuplifyapi.domain.Product;
import com.io.resuplifyapi.domain.ResupplyProduct;
import com.io.resuplifyapi.domain.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ResupplyServiceTest {

    private ProductService productService;
    private ResupplyService resupplyService;

    @BeforeEach
    public void init(){
        productService = mock(ProductService.class);
        resupplyService = new ResupplyService(productService);
    }

    @Test
    public void shouldConvertToResupplyProductsList(){

        doReturn(getData()).when(productService).findAllByResupplyCriteria(anyInt(), any(LocalDate.class));
        List<ResupplyProduct> resupplyProducts = resupplyService.getProducts(new Random().nextInt(), LocalDate.parse("2021-04-21"));

        assertAll(
                () -> assertEquals(resupplyProducts.size(), 2),
                () -> assertEquals(resupplyProducts.get(0).getDemand(), 10),
                () -> assertEquals(resupplyProducts.get(0).getStockLevel(), 10),
                () -> assertEquals(resupplyProducts.get(1).getDemand(), 3),
                () -> assertEquals(resupplyProducts.get(1).getStockLevel(), 4)
        );

        assertEquals(resupplyProducts.size(), 2);
    }


    private List<Product> getData(){
        return Arrays.asList(
                new Product.Builder()
                        .withPrediction(new Prediction(true, LocalDate.parse("2021-04-13"), 1.2))
                        .withStocks(new Stock(LocalDate.parse("2021-04-06"), 11),
                                    new Stock(LocalDate.parse("2021-04-07"), 10))
                        .withWarnLevel(2)
                        .build(),
                new Product.Builder()
                        .withPrediction(new Prediction(true, LocalDate.parse("2021-04-14"), 0.4))
                        .withStocks(new Stock(LocalDate.parse("2021-04-06"), 5),
                                    new Stock(LocalDate.parse("2021-04-07"), 4))
                        .withWarnLevel(1)
                        .build()
        );
    }

}