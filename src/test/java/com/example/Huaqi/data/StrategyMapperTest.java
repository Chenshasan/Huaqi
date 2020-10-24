package com.example.Huaqi.data;

import com.example.Huaqi.enums.StrategyDealType;
import com.example.Huaqi.enums.StrategyStatus;
import com.example.Huaqi.po.StrategyPO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StrategyMapperTest {
    @Autowired
    StrategyMapper strategyMapper;

    @Test
    public void test1(){
        StrategyPO strategyPO = new StrategyPO();
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        strategyPO.setDealType(StrategyDealType.Sell_50ETF.getType());
        strategyPO.setDealTime(ft.format(new Date()));
        strategyPO.setItem("10002423.SH");
        strategyPO.setPrice(500.);
        strategyPO.setPricePer(200.);
        strategyPO.setVolume(4.);
        strategyPO.setTransferFee(300.);
        strategyPO.setStatus(StrategyStatus.Success.getStatus());
        strategyPO.setTradeId(2);
        strategyMapper.addStrategy(strategyPO);
        System.out.println(strategyMapper.getStrategiesByTradeId(2));
    }
}
