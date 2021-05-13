package com.io.resuplifyapi.repository;

import com.io.resuplifyapi.domain.Shop;
import com.io.resuplifyapi.domain.ShopOwner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@ActiveProfiles("dev")
class CustomShopOwnerRepositoryTest {

    @Autowired
    ShopOwnerRepository shopOwnerRepository;

    @Test
    public void shouldFindByUsernameAndShopUrl(){

        ShopOwner shopOwner = createShopOwner("username", "shop.com");
        shopOwnerRepository.save(shopOwner);

        assertAll(
                () -> assertTrue(shopOwnerRepository.existsByUsernameAndShopUrl("username", "shop.com")),
                () -> assertFalse(shopOwnerRepository.existsByUsernameAndShopUrl("username", "another-shop.com")),
                () -> assertFalse(shopOwnerRepository.existsByUsernameAndShopUrl("another-username", "shop.com")),
                () -> assertFalse(shopOwnerRepository.existsByUsernameAndShopUrl("another-username", "another-shop.com"))
        );
    }

    private ShopOwner createShopOwner(String username, String url){

        ShopOwner shopOwner = new ShopOwner(username, UUID.randomUUID().toString());
        Shop shop = new Shop(url, UUID.randomUUID().toString(), LocalDate.now());

        shopOwner.setShop(shop);
        shop.setShopOwner(shopOwner);

        return shopOwner;
    }
}