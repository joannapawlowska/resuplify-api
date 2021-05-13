package com.io.resuplifyapi.repository;

import com.io.resuplifyapi.domain.ShopOwner;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class CustomShopOwnerRepositoryImpl implements CustomShopOwnerRepository{

    @Autowired
    EntityManager entityManager;

    @Override
    public boolean existsByUsernameAndShopUrl(String username, String url) {

        TypedQuery<ShopOwner> query = entityManager.createQuery("select so from ShopOwner so join so.shop s where so.username=:username and s.url=:url", ShopOwner.class);
        query.setParameter("username", username);
        query.setParameter("url", url);

        try{
            ShopOwner s = query.getSingleResult();
        }catch(NoResultException e){
            return false;
        }
        return true;
    }
}