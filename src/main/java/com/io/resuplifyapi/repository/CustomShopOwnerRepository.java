package com.io.resuplifyapi.repository;

public interface CustomShopOwnerRepository {

    boolean existsByUsernameAndShopUrl(String username, String url);
}
