package com.example.Huaqi.vo;
import java.util.Date;
public class PutOptionVO {
    String name;
    int price;
    int execPrice;
    String ETFName;
    int ETFPrice;
    Date time;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getExecPrice() {
        return execPrice;
    }

    public void setExecPrice(int execPrice) {
        this.execPrice = execPrice;
    }

    public String getETFName() {
        return ETFName;
    }

    public void setETFName(String ETFName) {
        this.ETFName = ETFName;
    }

    public int getETFPrice() {
        return ETFPrice;
    }

    public void setETFPrice(int ETFPrice) {
        this.ETFPrice = ETFPrice;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }


}
