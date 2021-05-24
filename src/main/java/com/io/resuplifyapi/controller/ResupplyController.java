package com.io.resuplifyapi.controller;

import com.io.resuplifyapi.domain.ResupplyProduct;
import com.io.resuplifyapi.domain.User;
import com.io.resuplifyapi.service.ResupplyService;
import com.io.resuplifyapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
public class ResupplyController {

    @Autowired
    UserService userService;

    @Autowired
    ResupplyService resupplyService;

    @GetMapping(value = "/resupply", produces = "application/json")
    public ResponseEntity<List<ResupplyProduct>> getResupplyProducts(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            HttpServletRequest request){

        Principal principal = request.getUserPrincipal();
        User user = userService.findByUsername(principal.getName());
        List<ResupplyProduct> products = resupplyService.getProducts(user.getShop().getId(), date);

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }
}