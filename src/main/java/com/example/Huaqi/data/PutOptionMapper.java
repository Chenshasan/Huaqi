package com.example.Huaqi.data;

import com.example.Huaqi.po.PutOptionPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PutOptionMapper {

    int addPutOption(PutOptionPO putOptionPO);

}
