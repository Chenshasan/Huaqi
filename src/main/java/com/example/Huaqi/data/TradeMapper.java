package com.example.Huaqi.data;

import com.example.Huaqi.po.TradePO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TradeMapper {
    int addTrade(TradePO trade);
    //TODO:还没测试这个接口
}
