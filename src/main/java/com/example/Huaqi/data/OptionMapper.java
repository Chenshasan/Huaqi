package com.example.Huaqi.data;

import com.example.Huaqi.po.OptionPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OptionMapper {

    /**
     * 获取某个月到期的期权List
     * @param strike_month 到期月份，格式类似"2020-10"
     * @return
     */
    List<OptionPO> getMonthOption(@Param("month") String strike_month);
    /**
     * 根据optionCode获取期权当前数据
     * @param optionCode
     * @return
     */
    OptionPO currentOption(@Param("code") String optionCode);

    int addOption(OptionPO option);

    List<OptionPO> getOptionByETF(@Param("code") String code);

    List<OptionPO> getCallOptionByETF(@Param("code") String code);

    List<OptionPO> getPutOptionByETF(@Param("code") String code);

    List<String> getTradeDate(@Param("code") String code);

    int updateOption(OptionPO option);


}
