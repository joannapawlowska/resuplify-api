package com.io.resuplifyapi.controllers;

import com.io.resuplifyapi.model.Shoper.ShoperProduct;
import com.io.resuplifyapi.service.ShoperService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/shoper")
public class ShoperController {

    @Autowired
    ShoperService shoperService;

    @GetMapping("/products")
    public List<ShoperProduct> getShoperProducts(){
        String url = "";
        String token = "";
        return shoperService.getProductsFromShop(url, token);
    }
}