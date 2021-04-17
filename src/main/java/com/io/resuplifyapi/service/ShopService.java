package com.io.resuplifyapi.service;

import com.io.resuplifyapi.model.Shop;
import com.io.resuplifyapi.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShopService {

    @Autowired
    ShopRepository shopRepository;

    public Shop findById(int id){
        Optional<Shop> result = shopRepository.findById(id);
        Shop shop = null;
        if(result.isPresent()){
            shop = result.get();
        }
        return shop;
    }
}