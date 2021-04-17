package com.io.resuplifyapi.service;

import com.io.resuplifyapi.model.Shoper.ShoperProduct;
import com.io.resuplifyapi.repository.ShoperProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoperService {

    @Autowired
    private ShoperProductRepository shoperProductRepository;

    public List<ShoperProduct> getProductsFromShop(String url, String token){
        return shoperProductRepository.getProductsFromShop(url, token);
    }
}