package com.io.resuplifyapi.repository;

import com.io.resuplifyapi.domain.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PredictionRepository extends JpaRepository<Prediction, Integer> {
}