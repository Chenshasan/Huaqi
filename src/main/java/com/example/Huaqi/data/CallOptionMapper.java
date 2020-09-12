package com.example.Huaqi.data;

import com.example.Huaqi.po.CallOptionPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CallOptionMapper {

    int addCallOption(CallOptionPO callOptionPO);






}
