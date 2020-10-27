package com.example.Huaqi.po;

/**
 * 买卖类，一次买卖
 */
public class DealPO {
    private int id;
    private String dealType;
    private String dealTime;
    private String status;
    private Double volume;
    private Double transferFee;
    private Double pricePer;
    private Double price;
    private Integer tradeId;
    private String item;
    private String optionName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    public String getDealTime() {
        return dealTime;
    }

    public void setDealTime(String dealTime) {
        this.dealTime = dealTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getTransferFee() {
        return transferFee;
    }

    public void setTransferFee(Double transferFee) {
        this.transferFee = transferFee;
    }

    public Double getPricePer() {
        return pricePer;
    }

    public void setPricePer(Double pricePer) {
        this.pricePer = pricePer;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getTradeId() {
        return tradeId;
    }

    public void setTradeId(Integer tradeId) {
        this.tradeId = tradeId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    @Override
    public String toString() {
        return "DealPO{" +
                "id=" + id +
                ", dealType='" + dealType + '\'' +
                ", dealTime='" + dealTime + '\'' +
                ", status='" + status + '\'' +
                ", volume=" + volume +
                ", transferFee=" + transferFee +
                ", pricePer=" + pricePer +
                ", price=" + price +
                ", tradeId=" + tradeId +
                ", item='" + item + '\'' +
                ", optionName='" + optionName + '\'' +
                '}';
    }
}
