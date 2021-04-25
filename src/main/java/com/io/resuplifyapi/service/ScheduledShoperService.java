package com.io.resuplifyapi.service;

import com.io.resuplifyapi.model.Product;
import com.io.resuplifyapi.model.Shop;
import com.io.resuplifyapi.model.Shoper.ShoperProduct;
import com.io.resuplifyapi.model.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;


@Service
public class ScheduledShoperService {

    Logger loger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    ShopService shopService;

    @Autowired
    ShoperService shoperService;

    BiPredicate<Product, ShoperProduct> existByShoperId = (product, shoperProduct) -> product.getShoperId() == shoperProduct.getId();
    Comparator<Stock> compareByDate = Comparator.comparing(Stock::getDate);

    public void getProductsForEachShop(){
        shopService.findAll()
                   .forEach(this::getProductsFromShop);
    }

    private void getProductsFromShop(Shop shop){
        List<ShoperProduct> shoperProducts = shoperService.getProductsFromShop(shop.getUrl(), shop.getToken());
        List<Product> products = shop.getProducts();


        shoperProducts.stream()
                .filter(ShoperProduct::isActive)
                .forEach(shoperProduct -> {

                    Optional<Product> optional = products.stream()
                            .filter(product -> existByShoperId.test(product, shoperProduct))
                            .findFirst();

                    if (optional.isEmpty()) {
                        addNewProductToShop(shoperProduct, shop);
                    } else {
                        addNewStockForProduct(shoperProduct, optional.get());
                    }

                });
        shopService.save(shop);
    }


    private void addNewProductToShop(ShoperProduct shoperProduct, Shop shop){
        String name = shoperProduct.getName();
        int nameLength = name.length();
        Product newProduct = new Product(name.substring(0, Math.min(nameLength, 255)), shoperProduct.getId(), shoperProduct.getWarnLevel(), shop);
        newProduct.addStock(new Stock(LocalDate.now(), shoperProduct.getStock(), newProduct));
        shop.addProduct(newProduct);
    }

    private void addNewStockForProduct(ShoperProduct shoperProduct, Product product){

        if (isRequiredAmountOfStocks(product)) {
            updateTheOldestStock(shoperProduct, product);
        } else {
            addNewStock(shoperProduct, product);
        }
    }

    private boolean isRequiredAmountOfStocks(Product product){
        return product.getStocks().size() == 2;
    }

    private void updateTheOldestStock(ShoperProduct shoperProduct, Product product) {
        updateWarnLevel(shoperProduct, product);
        Stock stock = product.getStocks().stream().min(compareByDate).get();
        stock.setDate(LocalDate.now());
        stock.setStock(shoperProduct.getStock());
    }

    private void updateWarnLevel(ShoperProduct shoperProduct, Product product){
        product.setWarnLevel(shoperProduct.getWarnLevel());
    }

    private void addNewStock(ShoperProduct shoperProduct, Product product) {
        updateWarnLevel(shoperProduct, product);
        product.addStock(new Stock(LocalDate.now(), shoperProduct.getStock(), product));
    }
}