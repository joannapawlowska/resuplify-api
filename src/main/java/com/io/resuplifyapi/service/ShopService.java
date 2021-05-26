package com.io.resuplifyapi.service;

import com.io.resuplifyapi.domain.Product;
import com.io.resuplifyapi.domain.Shop;
import com.io.resuplifyapi.domain.User;
import com.io.resuplifyapi.domain.UserDto;
import com.io.resuplifyapi.domain.externalAPI.AuthResponse;
import com.io.resuplifyapi.domain.externalAPI.ProductModel;
import com.io.resuplifyapi.exception.ExternalAPIAuthException;
import com.io.resuplifyapi.exception.ExternalAPIUnavailableException;
import com.io.resuplifyapi.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class ShopService {

    @Autowired
    ShopRepository shopRepository;

    @Autowired
    InventoryBalanceService inventoryBalanceService;

    @Autowired
    ProductService productService;

    @Autowired
    ExternalAPIService externalAPIService;

    @Autowired
    UserService userService;

    public List<Shop> findAll() {
        return shopRepository.findAll();
    }

    public void save(Shop shop) {
        shopRepository.save(shop);
    }

    public void deleteById(int shopId) {
        shopRepository.deleteById(shopId);
    }

    public void updateProducts(Shop shop, List<ProductModel> models) {
        inventoryBalanceService.updateInventory(models, shop);
    }

    public void updatePredictions(Shop shop) {

        for (Product product : shop.getProducts()) {
            productService.updatePrediction(product);
        }
    }

    public void refreshExternalAPITokenIfRequired(UserDto userDto) throws ExternalAPIAuthException, ExternalAPIUnavailableException {

        User user = userService.findByUsername(userDto.getUsername());
        Shop shop = user.getShop();
        LocalDate date = shop.getTokenValidityDate();

        if (shouldTokenBeRefreshed(date)) {
            refreshToken(shop, userDto);
            save(shop);
        }
    }

    private boolean shouldTokenBeRefreshed(LocalDate tokenRefreshDate) {
        return LocalDate.now().plusDays(7).isAfter(tokenRefreshDate);
    }

    public void refreshToken(Shop shop, UserDto userDto) throws ExternalAPIAuthException, ExternalAPIUnavailableException {

        userDto.setUrl(shop.getUrl());
        AuthResponse response = externalAPIService.authenticateUserAccount(userDto);
        shop.setToken(response.getAccessToken());
        shop.setTokenValidityDate(LocalDate.now().plusDays(TimeUnit.SECONDS.toDays(response.getExpiresIn())));
    }
}