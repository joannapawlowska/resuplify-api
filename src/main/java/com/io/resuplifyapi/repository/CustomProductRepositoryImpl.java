package com.io.resuplifyapi.repository;

import com.io.resuplifyapi.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

public class CustomProductRepositoryImpl implements CustomProductRepository{

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Product> findAllByResupplyCriteria(int shopId, LocalDate date) {

        TypedQuery<Product> query = entityManager.createQuery("select p from Product p join p.shop s join p.prediction pn where s.id=:shopId and pn.valid=true and pn.warnLevelDate<:date", Product.class);
        query.setParameter("shopId", shopId);
        query.setParameter("date", date);
        return query.getResultList();
    }
}