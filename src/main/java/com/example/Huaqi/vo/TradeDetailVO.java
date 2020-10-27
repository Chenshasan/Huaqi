package com.example.Huaqi.vo;


import java.util.List;

public class TradeDetailVO {
    TradeVO tradeInfo;
    List<DealVO> deals;

    public TradeDetailVO(TradeVO tradeVO, List<DealVO> deals){
        this.tradeInfo = tradeVO;
        this.deals = deals;
    }

    public TradeVO getTradeInfo() {
        return tradeInfo;
    }

    public void setTradeInfo(TradeVO tradeInfo) {
        this.tradeInfo = tradeInfo;
    }

    public List<DealVO> getDeals() {
        return deals;
    }

    public void setDeals(List<DealVO> deals) {
        this.deals = deals;
    }

    @Override
    public String toString() {
        return "TradeDetailVO{" +
                "tradeInfo=" + tradeInfo +
                ", deals=" + deals +
                '}';
    }
}
