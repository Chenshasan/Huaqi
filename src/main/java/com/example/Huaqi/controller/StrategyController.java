package com.example.Huaqi.controller;

import com.example.Huaqi.bl.StrategyService;
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
@RequestMapping("/api/strategy")
public class StrategyController {
    @Autowired
    StrategyService strategyService;

    @GetMapping("/cumulativeRate")
    public ResponseVO cumulativeRate(@RequestParam(name = "startMonth", required = true) String startMonth,
                                     @RequestParam(name = "endMonth", required = true) String endMonth){
        return strategyService.cumulativeRate(startMonth, endMonth);
    }

    @GetMapping("/strategyInfo")
    public ResponseVO strategyInfo(@RequestParam(name = "startMonth", required = true) String startMonth,
                                   @RequestParam(name = "endMonth", required = true) String endMonth){
        return strategyService.strategyInfo(startMonth, endMonth);
    }

    @GetMapping("/varianceAndMeanOfTotalRate")
    public ResponseVO varianceAndMeanOfTotalRate(){
        return strategyService.varianceAndMeanOfTotalRate();
    }

    @GetMapping("/strategyOverallInfo")
    public ResponseVO strategyOverallInfo(){
        return strategyService.strategyOverallInfo();
    }

    @GetMapping("/monthRank")
    public ResponseVO monthRank(){
        return strategyService.monthRank();
    }

}
