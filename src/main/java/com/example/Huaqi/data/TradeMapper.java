package com.example.Huaqi.data;

import com.example.Huaqi.po.TradePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TradeMapper {
    int addTrade(TradePO trade);//TODO:还没测试这个接口
    List<TradePO> getCompletedTrade();
    TradePO getTradeById(@Param("id") Integer tradeId);
    List<Integer> getUncompletedTradeId();

}
