package com.example.Huaqi.data;

import com.example.Huaqi.po.DealPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DealMapper {
    /**
     * 添加买卖到数据库
     * @param dealPO
     * @return
     */
    int addDeal(DealPO dealPO);

    List<DealPO> getDealsByTradeId(@Param("tradeId") Integer tradeId);
}
