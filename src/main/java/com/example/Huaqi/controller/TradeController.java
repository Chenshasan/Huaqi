package com.example.Huaqi.controller;

import com.example.Huaqi.bl.TradeService;
import com.example.Huaqi.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author syc
 * @date 2020/10/25
 */
@RestController()
@RequestMapping("/api/trade")
public class TradeController {
    @Autowired
    TradeService tradeService;

    @GetMapping("/completedTrade")
    public ResponseVO completedTrade(){
        return tradeService.completedTrade();
    }

    @GetMapping("/detail")
    public ResponseVO detail(@RequestParam(name = "id", required = true) Integer tradeId){
        return tradeService.detail(tradeId);
    }

    @GetMapping("/myPosition")
    public ResponseVO myPosition(){
        return tradeService.myPosition();
    }
}
