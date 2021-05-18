package com.io.resuplifyapi.repository;

import com.io.resuplifyapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer>, CustomUserRepository {
}