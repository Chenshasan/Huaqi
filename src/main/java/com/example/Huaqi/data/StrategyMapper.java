package com.example.Huaqi.data;

import com.example.Huaqi.po.StrategyPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface StrategyMapper {
    /**
     * 添加买卖到数据库
     * @param strategy
     * @return
     */
    int addStrategy(StrategyPO strategy);

    List<StrategyPO> getStrategiesByTradeId(@Param("tradeId") Integer tradeId);
}
