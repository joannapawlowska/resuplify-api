package com.io.resuplifyapi.service;

import com.io.resuplifyapi.domain.Shop;
import com.io.resuplifyapi.domain.User;
import com.io.resuplifyapi.domain.UserDto;
import com.io.resuplifyapi.domain.externalAPI.AuthResponse;
import com.io.resuplifyapi.exception.UserAlreadyExistException;
import com.io.resuplifyapi.exception.UserNotFoundException;
import com.io.resuplifyapi.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void save(User user){
        userRepository.save(user);
   }

    public void createNewUserAccount(UserDto userDto, AuthResponse response) throws UserAlreadyExistException {

        if(existByUsername(userDto.getUsername()))
            throw new UserAlreadyExistException(userDto.getUsername(),  userDto.getUrl());

        Shop shop = new Shop(userDto.getUrl(), response.getAccessToken(), LocalDate.now().plusDays(TimeUnit.SECONDS.toDays(response.getExpiresIn())));
        User user = new User(userDto.getUsername(), passwordEncoder.encode(userDto.getPassword()), shop);
        save(user);
    }

    public boolean existByUsername(String username){ return userRepository.existsByUsername(username); }

    public User findByUsername(String username) throws UserNotFoundException {

        if(!existByUsername(username))
            throw new UserNotFoundException(username);

        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        try{
            return findByUsername(s);
        }catch(UserNotFoundException e){
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}