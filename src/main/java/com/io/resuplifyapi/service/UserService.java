package com.io.resuplifyapi.service;

import com.io.resuplifyapi.domain.Shop;
import com.io.resuplifyapi.domain.User;
import com.io.resuplifyapi.domain.UserDto;
import com.io.resuplifyapi.domain.externalAPI.AuthResponse;
import com.io.resuplifyapi.exception.UserAlreadyExistException;
import com.io.resuplifyapi.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userAccountRepository;

    public Optional<User> findById(int id){
        return userAccountRepository.findById(id);
    }

    public void save(User user){
        userAccountRepository.save(user);
   }

    public void createNewUserAccount(UserDto userDto, AuthResponse response) throws UserAlreadyExistException {

        if(existByUsernameAndShopUrl(userDto.getUsername(), userDto.getUrl()))
            throw new UserAlreadyExistException(userDto.getUsername(),  userDto.getUrl());

        Shop shop = new Shop(userDto.getUrl(), response.getAccessToken(), LocalDate.now());
        User user = new User(userDto.getUsername(), userDto.getPassword(), shop);
        save(user);
    }

    public boolean existByUsernameAndShopUrl(String username, String url){ return userAccountRepository.existsByUsernameAndShopUrl(username, url); }
}