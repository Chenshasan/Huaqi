package com.example.Huaqi.data;

import com.example.Huaqi.po.StrategyPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface StrategyMapper {
    int addStrategy(StrategyPO strategy);
}
