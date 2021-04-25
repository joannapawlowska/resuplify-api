package com.io.resuplifyapi.service;

import com.io.resuplifyapi.model.Shop;
import com.io.resuplifyapi.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ShopService {

    @Autowired
    ShopRepository shopRepository;

    public Optional<Shop> findById(int id){
        return shopRepository.findById(id);
    }

    public List<Shop> findAll(){ return shopRepository.findAll(); }

    public void save(Shop shop){
        shopRepository.save(shop);
    }

    public void deleteById(int shopId){
        shopRepository.deleteById(shopId);
    }
}