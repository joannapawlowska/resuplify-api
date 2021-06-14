package com.io.resuplifyapi.repository;

import com.io.resuplifyapi.domain.Product;

import java.time.LocalDate;
import java.util.List;

public interface CustomProductRepository {

    List<Product> findAllByResupplyCriteria(int shopId, LocalDate date);
}