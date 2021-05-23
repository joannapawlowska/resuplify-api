package com.io.resuplifyapi.repository;

import com.io.resuplifyapi.domain.User;

public interface CustomUserRepository {

    boolean existsByUsername(String username);

    User findByUsername(String username);
}