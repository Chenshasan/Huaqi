package com.example.Huaqi.data;

import com.example.Huaqi.po.CallOptionPO;
import com.example.Huaqi.po.PutOptionPO;


import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OptionMapper {

    int addCallOption(CallOptionPO callOptionPO);






}
