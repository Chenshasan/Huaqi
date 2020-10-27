package com.example.Huaqi.bl;

import com.example.Huaqi.vo.ResponseVO;

public interface TradeService {
    /**
     * 获取已完成交易
     * @return
     */
    ResponseVO completedTrade();

    /**
     * 根据tradeId获取交易详情
     * @param tradeId
     * @return
     */
    ResponseVO detail(Integer tradeId);

    /**
     * 获取我的持仓
     * @return
     */
    ResponseVO myPosition();
}
