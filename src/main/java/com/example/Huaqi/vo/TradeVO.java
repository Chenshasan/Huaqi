package com.example.Huaqi.vo;

import com.example.Huaqi.po.TradePO;

public class TradeVO {
    Integer tradeId;
    String tradeName;
    String startTime;
    String endTime;
    String tradeType;
    String status;
    Double profit;
    Integer dealNum;

    public TradeVO(TradePO tradePO){
        this.setTradeId(tradePO.getId());
        this.setStartTime(tradePO.getStartTime());
        this.setEndTime(tradePO.getEndTime());
        this.setTradeName(tradePO.getTradeName());
        this.setStatus(tradePO.getStatus());
        this.setTradeType(tradePO.getTradeType());
        this.setDealNum(tradePO.getDealNum());
        this.setProfit(tradePO.getProfit());
    }

    public Integer getTradeId() {
        return tradeId;
    }

    public void setTradeId(Integer tradeId) {
        this.tradeId = tradeId;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Integer getDealNum() {
        return dealNum;
    }

    public void setDealNum(Integer dealNum) {
        this.dealNum = dealNum;
    }

    @Override
    public String toString() {
        return "TradeVO{" +
                "tradeId=" + tradeId +
                ", tradeName='" + tradeName + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", tradeType='" + tradeType + '\'' +
                ", status='" + status + '\'' +
                ", profit=" + profit +
                ", dealNum=" + dealNum +
                '}';
    }
}
