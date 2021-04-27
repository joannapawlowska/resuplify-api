package com.io.resuplifyapi.controller;

import com.io.resuplifyapi.service.ScheduledService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shoper")
public class ShoperController {

    @Autowired
    ScheduledService scheduledService;

    @GetMapping("/update")
    public void updateEachShopInventoryBalances(){
       scheduledService.updateEachShopInventoryBalances();
    }
}