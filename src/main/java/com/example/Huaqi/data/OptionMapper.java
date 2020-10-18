package com.example.Huaqi.data;

import com.example.Huaqi.po.OptionPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OptionMapper {
    int addOption(OptionPO option);
}
