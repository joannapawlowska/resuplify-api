package com.io.resuplifyapi.repository;

import com.io.resuplifyapi.domain.Prediction;
import com.io.resuplifyapi.domain.Product;
import com.io.resuplifyapi.domain.Shop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("dev")
class CustomProductRepositoryTest {


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShopRepository shopRepository;

    @BeforeEach
    void init(){

        Shop shop1 = new Shop();
        shop1.setId(1);
        Shop shop2 = new Shop();
        shop2.setId(2);

        shop1.setProducts(Arrays.asList(
                new Product.Builder()
                        .withShop(shop1)
                        .withPrediction(new Prediction(true, LocalDate.now().minusDays(1)))
                        .build(),
                new Product.Builder()
                        .withShop(shop1)
                        .withPrediction(new Prediction(false, LocalDate.now().minusDays(1)))
                        .build(),
                new Product.Builder()
                        .withShop(shop1)
                        .withPrediction(new Prediction(true, LocalDate.now()))
                        .build(),
                new Product.Builder()
                        .withShop(shop1)
                        .withPrediction(new Prediction(true, LocalDate.now().minusDays(5)))
                        .build()
        ));

        shop2.addProduct(
                new Product.Builder()
                        .withShop(shop2)
                        .withPrediction(new Prediction(true, LocalDate.now().minusDays(1)))
                        .build()
        );

        shopRepository.saveAll(List.of(shop1, shop2));
    }


    @Test
    public void shouldFindByShopIdAndPredictionActiveAndWarnLevelDateBeforeGivenDate(){

        assertEquals(productRepository.findAllBySupplyCriteria(1, LocalDate.now()).size(), 2);
    }
}