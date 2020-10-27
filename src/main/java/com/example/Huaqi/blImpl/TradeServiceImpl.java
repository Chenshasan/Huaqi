package com.example.Huaqi.blImpl;

import com.example.Huaqi.bl.TradeService;
import com.example.Huaqi.data.DealMapper;
import com.example.Huaqi.data.TradeMapper;
import com.example.Huaqi.po.DealPO;
import com.example.Huaqi.po.TradePO;
import com.example.Huaqi.vo.DealVO;
import com.example.Huaqi.vo.ResponseVO;
import com.example.Huaqi.vo.TradeDetailVO;
import com.example.Huaqi.vo.TradeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TradeServiceImpl implements TradeService {
    @Autowired
    TradeMapper tradeMapper;
    @Autowired
    DealMapper dealMapper;

    @Override
    public ResponseVO completedTrade() {
        List<TradeVO> tradeVOS = new ArrayList<>();
        List<TradePO> tradePOS = tradeMapper.getCompletedTrade();
        for(int i=0;i<tradePOS.size();i++){
            tradeVOS.add(new TradeVO(tradePOS.get(i)));
        }
        return ResponseVO.buildSuccess(tradeVOS);
    }

    @Override
    public ResponseVO detail(Integer tradeId) {
        TradeVO tradeVO = new TradeVO(tradeMapper.getTradeById(tradeId));
        List<DealVO> dealVOS = new ArrayList<>();
        List<DealPO> dealPOS = dealMapper.getDealsByTradeId(tradeId);
        for(int i=0;i<dealPOS.size();i++){
            dealVOS.add(new DealVO(dealPOS.get(i)));
        }
        TradeDetailVO tradeDetailVO = new TradeDetailVO(tradeVO,dealVOS);
        return ResponseVO.buildSuccess(tradeDetailVO);
    }

    @Override
    public ResponseVO myPosition() {
        List<Integer> tradeIds = tradeMapper.getUncompletedTradeId();
        List<DealPO> dealPOS = new ArrayList<>();
        for(int i=0;i<tradeIds.size();i++){
            dealPOS.addAll(dealMapper.getDealsByTradeId(tradeIds.get(i)));
        }
        return ResponseVO.buildSuccess(dealPOS);
    }
}
