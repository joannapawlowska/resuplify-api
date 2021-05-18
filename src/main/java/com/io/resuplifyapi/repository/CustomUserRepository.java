package com.io.resuplifyapi.repository;

public interface CustomUserRepository {

    boolean existsByUsernameAndShopUrl(String username, String url);
}
