package com.io.resuplifyapi.repository;

import com.io.resuplifyapi.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer>, CustomProductRepository {
}