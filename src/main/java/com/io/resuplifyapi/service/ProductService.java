package com.io.resuplifyapi.service;

import com.io.resuplifyapi.model.Product;
import com.io.resuplifyapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ProductService{

    @Autowired
    ProductRepository productRepository;

    public void save(Product product){
        productRepository.save(product);
    }

    public void deleteById(int id){
        productRepository.deleteById(id);
    }

    public Optional<Product> findById(int id){
        return productRepository.findById(id);
    }
}