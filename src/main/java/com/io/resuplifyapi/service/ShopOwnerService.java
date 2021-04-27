package com.io.resuplifyapi.service;

import com.io.resuplifyapi.domain.ShopOwner;
import com.io.resuplifyapi.repository.ShopOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ShopOwnerService {

    @Autowired
    private ShopOwnerRepository shopOwnerRepository;

    public Optional<ShopOwner> findById(int id){
        return shopOwnerRepository.findById(id);
    }

    public List<ShopOwner> findAll(){
        return shopOwnerRepository.findAll();
    }

   public void save(ShopOwner shopOwner){
        shopOwnerRepository.save(shopOwner);
   }
}