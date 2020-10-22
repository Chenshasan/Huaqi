package com.example.Huaqi.data;

import com.example.Huaqi.po.OptionPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OptionMapper {
    int addOption(OptionPO option);

    List<OptionPO> getOptionByETF(@Param("code") String code);

    int updateOption(OptionPO option);


}
