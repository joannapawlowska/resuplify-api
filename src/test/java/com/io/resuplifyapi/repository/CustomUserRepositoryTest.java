package com.io.resuplifyapi.repository;

import com.io.resuplifyapi.domain.Shop;
import com.io.resuplifyapi.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@ActiveProfiles("dev")
class CustomUserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void shouldFindByUsernameAndShopUrl(){

        User user = createUser("username", "shop.com");
        userRepository.save(user);

        assertAll(
                () -> assertTrue(userRepository.existsByUsernameAndShopUrl("username", "shop.com")),
                () -> assertFalse(userRepository.existsByUsernameAndShopUrl("username", "another-shop.com")),
                () -> assertFalse(userRepository.existsByUsernameAndShopUrl("another-username", "shop.com")),
                () -> assertFalse(userRepository.existsByUsernameAndShopUrl("another-username", "another-shop.com"))
        );
    }

    private User createUser(String username, String url){

        Shop shop = new Shop(url, UUID.randomUUID().toString(), LocalDate.now());
        return new User(username, UUID.randomUUID().toString(), shop);
    }
}