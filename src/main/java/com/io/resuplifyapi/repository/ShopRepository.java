package com.io.resuplifyapi.repository;

import com.io.resuplifyapi.domain.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<Shop, Integer> {
}