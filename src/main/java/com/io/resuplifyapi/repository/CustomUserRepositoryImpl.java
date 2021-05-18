package com.io.resuplifyapi.repository;

import com.io.resuplifyapi.domain.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class CustomUserRepositoryImpl implements CustomUserRepository {

    @Autowired
    EntityManager entityManager;

    @Override
    public boolean existsByUsernameAndShopUrl(String username, String url) {

        TypedQuery<User> query = entityManager.createQuery("select u from User u join u.shop s where u.username=:username and s.url=:url", User.class);
        query.setParameter("username", username);
        query.setParameter("url", url);

        try{
            User u = query.getSingleResult();
        }catch(NoResultException e){
            return false;
        }
        return true;
    }
}