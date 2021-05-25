package com.io.resuplifyapi.service;

import com.io.resuplifyapi.domain.Prediction;
import com.io.resuplifyapi.domain.Product;
import com.io.resuplifyapi.domain.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PredictorServiceTest {

    private PredictorService service;

    @BeforeEach
    public void init() { service = new PredictorService(); }

    @Test
    public void shouldSetNotValidPredictionWhenNotEnoughStocks(){
        Product product = new Product.Builder()
                .withPrediction(new Prediction())
                .withStocks(new Stock())
                .build();

        service.updatePrediction(product);

        assertFalse(product.getPrediction().isValid());
    }

    @Test
    public void shouldSetNotValidPredictionWhenConstantStockLevels(){
        Product product = new Product.Builder()
                .withPrediction(new Prediction())
                .withStocks(Stream.generate((() -> new Stock(10)))
                                    .limit(10)
                                    .toArray(Stock[]::new))
                .build();

        service.updatePrediction(product);

        assertFalse(product.getPrediction().isValid());
    }

    @Test
    public void shouldSetNotValidPredictionWhenOutOfStockDateLaterThanMaxPredictionDate(){
        Product product = new Product.Builder()
                .withPrediction(new Prediction())
                .withWarnLevel(1)
                .withStocks(Stream.iterate(new Stock(LocalDate.now().minusDays(29), 100),
                                            s -> new Stock(s.getDate().plusDays(1), 100))
                                    .limit(29)
                                    .toArray(Stock[]::new))
                .withStocks(new Stock(LocalDate.now(), 99))
                .build();

        service.updatePrediction(product);

        assertFalse(product.getPrediction().isValid());
    }

    @Test
    public void shouldRejectZeroLevelStocksBeforeDeliveryAndSetPredictionParams(){
        Product product = new Product.Builder()
                .withPrediction(new Prediction())
                .withWarnLevel(1)
                .withStocks(new Stock(LocalDate.now().minusDays(5), 0),
                            new Stock(LocalDate.now().minusDays(4), 0),
                            new Stock(LocalDate.now().minusDays(3),5),
                            new Stock(LocalDate.now().minusDays(2),5),
                            new Stock(LocalDate.now().minusDays(1),4),
                            new Stock(LocalDate.now(), 3))
                .build();

        service.updatePrediction(product);

        assertAll(
                () -> assertTrue(product.getPrediction().isValid()),
                () -> assertEquals(product.getPrediction().getSalePerDay(), 0.7),
                () -> assertEquals(product.getPrediction().getWarnLevelDate(), LocalDate.now().plusDays(2))
        );
    }

    @Test
    public void shouldIncludeConstantNotZeroLevelStocksBeforeDeliveryAndSetPredictionParams(){
        Product product = new Product.Builder()
                .withPrediction(new Prediction())
                .withWarnLevel(1)
                .withStocks(new Stock(LocalDate.now().minusDays(5), 1),
                            new Stock(LocalDate.now().minusDays(4), 1),
                            new Stock(LocalDate.now().minusDays(3),5),
                            new Stock(LocalDate.now().minusDays(2),5),
                            new Stock(LocalDate.now().minusDays(1),4),
                            new Stock(LocalDate.now(), 3))
                .build();

        service.updatePrediction(product);

        assertAll(
                () -> assertTrue(product.getPrediction().isValid()),
                () -> assertEquals(product.getPrediction().getSalePerDay(), 0.6),
                () -> assertEquals(product.getPrediction().getWarnLevelDate(), LocalDate.now().plusDays(3))
        );
    }

    @Test
    public void shouldSetPredictionParamsWhenZeroLevelStocksAfterDelivery(){
        Product product = new Product.Builder()
                .withPrediction(new Prediction())
                .withWarnLevel(1)
                .withStocks(new Stock(LocalDate.now().minusDays(3),3),
                            new Stock(LocalDate.now().minusDays(2), 0),
                            new Stock(LocalDate.now().minusDays(1),0),
                            new Stock(LocalDate.now(), 0))
                .build();

        service.updatePrediction(product);

        assertAll(
                () -> assertTrue(product.getPrediction().isValid()),
                () -> assertEquals(product.getPrediction().getSalePerDay(), 0.9),
                () -> assertEquals(product.getPrediction().getWarnLevelDate(), LocalDate.now())
        );
    }

    @Test
    public void shouldSetPredictionParams(){
        Product product = new Product.Builder()
                .withPrediction(new Prediction())
                .withWarnLevel(1)
                .withStocks(new Stock(LocalDate.now().minusDays(3),5),
                            new Stock(LocalDate.now().minusDays(2),5),
                            new Stock(LocalDate.now().minusDays(1),4),
                            new Stock(LocalDate.now(), 3))
                .build();

        service.updatePrediction(product);

        assertAll(
                () -> assertTrue(product.getPrediction().isValid()),
                () -> assertEquals(product.getPrediction().getSalePerDay(), 0.7),
                () -> assertEquals(product.getPrediction().getWarnLevelDate(), LocalDate.now().plusDays(2))
        );
    }

    @Test
    public void shouldSetNotValidPredictionWhenOnlySingleStocksAfterDelivery(){
        Product product = new Product.Builder()
                .withPrediction(new Prediction())
                .withWarnLevel(1)
                .withStocks(new Stock(LocalDate.now().minusDays(3),2),
                            new Stock(LocalDate.now(), 5))
                .build();

        service.updatePrediction(product);

        assertFalse(product.getPrediction().isValid());
    }

    @Test
    public void shouldSetNotValidPredictionWhenMultipleConstantStockLevels(){
        Product product = new Product.Builder()
                .withPrediction(new Prediction())
                .withWarnLevel(1)
                .withStocks(new Stock(LocalDate.now().minusDays(5),1),
                            new Stock(LocalDate.now().minusDays(4),1),
                            new Stock(LocalDate.now().minusDays(3),5),
                            new Stock(LocalDate.now().minusDays(2),5),
                            new Stock(LocalDate.now().minusDays(1),7),
                            new Stock(LocalDate.now(), 7))
                .build();

        service.updatePrediction(product);

        assertFalse(product.getPrediction().isValid());
    }
}