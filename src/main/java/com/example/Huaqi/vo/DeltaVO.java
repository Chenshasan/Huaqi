package com.example.Huaqi.vo;

import java.util.HashMap;

/**
 * Delta曲线
 */
public class DeltaVO {
    private Double strike_price; // x轴
    private Double delta; // y轴

    public DeltaVO(Double price,Double delta){
        this.strike_price = price;
        this.delta = delta;
    }
    public Double getStrike_price() {
        return strike_price;
    }

    public void setStrike_price(Double strike_price) {
        this.strike_price = strike_price;
    }

    public Double getDelta() {
        return delta;
    }

    public void setDelta(Double delta) {
        this.delta = delta;
    }
}
