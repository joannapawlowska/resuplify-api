package com.io.resuplifyapi.repository;

import com.io.resuplifyapi.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("dev")
class CustomUserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void shouldFindByUsername(){

        User user = new User();
        user.setUsername("username");
        userRepository.save(user);

        assertAll(
                () -> assertTrue(userRepository.existsByUsername("username")),
                () -> assertFalse(userRepository.existsByUsername("another-username"))
        );
    }
}