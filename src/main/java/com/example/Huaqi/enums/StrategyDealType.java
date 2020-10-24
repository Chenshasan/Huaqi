package com.example.Huaqi.enums;

/**
 * Strategy表/类中的交易类型
 */
public enum StrategyDealType {
    Buy_Put("Buy_Put"), // 购买认沽期权
    Buy_Call("Buy_Call"), //购买认购期权
    Exec_Option("Exec_Option"), //行权
    Sell_Call("Sell_Call"),//卖出认购期权
    Sell_Put("Sell_Put"),//卖出认沽期权
    Sell_50ETF("Sell_50ETF"),//卖出50ETF
    Buy_50ETF("Buy_50ETF"); //买入50ETF

    private final String type;
    StrategyDealType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
