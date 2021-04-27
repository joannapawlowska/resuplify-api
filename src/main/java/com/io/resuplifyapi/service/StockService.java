package com.io.resuplifyapi.service;

import com.io.resuplifyapi.domain.Stock;
import com.io.resuplifyapi.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Transactional
    public void save(Stock stock){
        stockRepository.save(stock);
    }
}