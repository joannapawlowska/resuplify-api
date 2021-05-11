package com.io.resuplifyapi.service;

import com.io.resuplifyapi.domain.Product;
import com.io.resuplifyapi.repository.ProductRepository;
import com.io.resuplifyapi.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService{

    @Autowired
    ProductRepository productRepository;

    @Autowired
    PredictorService predictorService;

    @Autowired
    ShopRepository shopRepository;

    public void save(Product product){
        productRepository.save(product);
    }

    public void deleteById(int id){
        productRepository.deleteById(id);
    }

    public Optional<Product> findById(int id){
        return productRepository.findById(id);
    }

    public List<Product> findAllByResupplyCriteria(int shopId, LocalDate date) { return productRepository.findAllBySupplyCriteria(shopId, date); }

    public void updatePrediction(Product product) { predictorService.updatePrediction(product); }
}