package com.example.Huaqi.blImpl;

import com.example.Huaqi.bl.ETFService;
import com.example.Huaqi.data.ETFMapper;
import com.example.Huaqi.po.ETFPO;
import com.example.Huaqi.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ETFServiceImpl implements ETFService {

    @Autowired
    ETFMapper etfMapper;

    @Override
    public ResponseVO getAllETF() {
        List<ETFPO> etfpos = etfMapper.getAllETF();
        return ResponseVO.buildSuccess(etfpos);
    }
}
