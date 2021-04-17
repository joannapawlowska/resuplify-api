package com.io.resuplifyapi.repository;

import com.io.resuplifyapi.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<Shop, Integer> {
}