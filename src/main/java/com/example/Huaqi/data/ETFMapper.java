package com.example.Huaqi.data;

import com.example.Huaqi.po.ETFPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ETFMapper {
    /**
     * 添加ETF
     * @return
     */
    int addETF(ETFPO etfpo);

    /**
     * 更新ETF
     * @return
     */
    int updateETF(ETFPO etfpo);

    /**
     * 获取所有ETF
     * @return
     */
    List<ETFPO> getAllETF();
}
