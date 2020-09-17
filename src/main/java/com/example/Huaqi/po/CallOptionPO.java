package com.example.Huaqi.po;

import java.util.Date;

public class CallOptionPO {
    String optioncode;//唯一标识符
    String name;
    double price;
    double execPrice;
    String ETFName;
    double ETFPrice;
    Date time;
    double delta;
    double avg1_2;//买一买二平均值
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getExecPrice() {
        return execPrice;
    }

    public void setExecPrice(double execPrice) {
        this.execPrice = execPrice;
    }

    public String getETFName() {
        return ETFName;
    }

    public void setETFName(String ETFName) {
        this.ETFName = ETFName;
    }

    public double getETFPrice() {
        return ETFPrice;
    }

    public void setETFPrice(double ETFPrice) {
        this.ETFPrice = ETFPrice;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setOptioncode(String optioncode) {
        this.optioncode = optioncode;
    }

    public String getOptioncode() {
        return optioncode;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public double getAvg1_2() {
        return avg1_2;
    }

    public void setAvg1_2(double avg1_2) {
        this.avg1_2 = avg1_2;
    }
}
