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
    public boolean existsByUsername(String username) {
        return findByUsername(username) != null;
    }

    @Override
    public User findByUsername(String username){

        TypedQuery<User> query = entityManager.createQuery("select u from User u where u.username=:username", User.class);
        query.setParameter("username", username);

        try{
            return query.getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }
}