package com.example.Huaqi.controller;

import com.example.Huaqi.bl.ETFService;
import com.example.Huaqi.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:cyz
 */
@RestController
@RequestMapping("/api/etf/")
public class ETFController {
    @Autowired
    ETFService etfService;

    @GetMapping("/getAllETF")
    public ResponseVO getAllETF(){
        return etfService.getAllETF();
    }
}
