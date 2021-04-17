package com.io.resuplifyapi.repository;

import com.io.resuplifyapi.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}