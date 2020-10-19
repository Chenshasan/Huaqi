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
    public ResponseVO getOptionByETF(String etfcode){
        return optionService.getOptionByETFCode(etfcode);
    }

    @GetMapping("deltaCurve")
    public ResponseVO getDeltaCurve(String etfcode){
        return optionService.getDeltaCurve(etfcode);
    }


}
