package com.io.resuplifyapi.repository;

import com.io.resuplifyapi.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Integer> {
}
