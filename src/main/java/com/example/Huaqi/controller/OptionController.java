package com.example.Huaqi.controller;


import com.example.Huaqi.bl.OptionService;
import com.example.Huaqi.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/option")
public class OptionController {

    @Autowired
    OptionService optionService;

    @GetMapping("getOptionByETF")
    public ResponseVO getOptionByETF(String etfcode,String type){
        return optionService.getOptionByETFCode(etfcode,type);
    }

    @GetMapping("deltaCurve")
    public ResponseVO getDeltaCurve(String etfcode){
        return optionService.getDeltaCurve(etfcode);
    }

    @GetMapping("timeValueCurve")
    public ResponseVO getTimeValueCurve(String etfcode){
        return optionService.getTimeValueCurve(etfcode);
    }

    @GetMapping("tradeDate")
    public ResponseVO getOptionTradeDate(String etfcode){
        return optionService.getOptionTradeDate(etfcode);
    }
}
