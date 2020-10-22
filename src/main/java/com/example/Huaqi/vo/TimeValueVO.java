package com.example.Huaqi.vo;

/**
 * 时间价值曲线
 */
public class TimeValueVO {
    private Double strike_price; // x轴
    private Double timeValue; // y轴

    public TimeValueVO(Double strike_price,Double timeValue){
        this.strike_price = strike_price;
        this.timeValue = timeValue;
    }

    public Double getStrike_price() {
        return strike_price;
    }

    public void setStrike_price(Double strike_price) {
        this.strike_price = strike_price;
    }

    public Double getTimeValue() {
        return timeValue;
    }

    public void setTimeValue(Double timeValue) {
        this.timeValue = timeValue;
    }
}
