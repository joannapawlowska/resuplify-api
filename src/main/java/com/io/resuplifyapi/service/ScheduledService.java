package com.io.resuplifyapi.service;

import com.io.resuplifyapi.domain.Shop;
import com.io.resuplifyapi.domain.externalAPI.ProductModel;
import com.io.resuplifyapi.exception.ExternalAPICallException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduledService {

    Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    ShopService shopService;

    @Autowired
    ExternalAPIService externalAPIService;

    @Autowired
    InventoryBalanceService inventoryBalanceService;

    public void updateEachShopInventoryBalances(){

        List<Shop> shops = shopService.findAll();
        for(Shop shop : shops){
            updateInventoryBalance(shop);
        }
    }

    private void updateInventoryBalance(Shop shop){
        try{
            List<ProductModel> productModels = externalAPIService.requestForProducts(shop.getUrl(), shop.getToken());
            shopService.update(shop, productModels);
            shopService.save(shop);
        }catch(ExternalAPICallException e){
            logger.error("Unable to update data for shop with id {} : response {}", shop.getId(), e.getMessage());
        }
    }
}