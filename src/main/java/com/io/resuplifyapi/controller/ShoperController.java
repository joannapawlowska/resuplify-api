package com.io.resuplifyapi.controller;

import com.io.resuplifyapi.model.Shop;
import com.io.resuplifyapi.model.Shoper.ShoperProduct;
import com.io.resuplifyapi.service.ShopService;
import com.io.resuplifyapi.service.ShoperService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/shoper")
public class ShoperController {

    @Autowired
    ShoperService shoperService;

    @Autowired
    ShopService shopService;

    @GetMapping("/products/shop/{id}")
    public List<ShoperProduct> getShoperProducts(@PathVariable int id){
        Shop shop = shopService.findById(id);
        return shoperService.getProductsFromShop(shop.getUrl(), shop.getToken());
    }
}