package com.example.Huaqi.po;

public class OptionPO {
    private String option_code; //唯一标识符
    private String option_name;//期权名
    private String option_var; // 期权品种
    private String us_code; // 标的物代码
    private String us_name; // 标的物名
    private String exe_type; // 期权类型
    private Double strike_price; // 行权价格
    private String month; // 交割月份
    private String call_put; // 期权类型 认购or认沽
    private String first_tradedate; // 起始交易日期
    private String last_tradedate; // 最后交易日期
    private Double change;
    private Integer amount;
    private Double pre_settle;
    private Double open;
    private Double highest;
    private Double lowest;
    private Double close;
    private Double settlement_price;
    private Integer volume;
    private Integer position;
    private Double delta;
    private Double in_value;
    private Double time_value;
    private Integer valid;
    private String update_time;

    public String getOption_code() {
        return option_code;
    }

    public void setOption_code(String option_code) {
        this.option_code = option_code;
    }

    public String getOption_name() {
        return option_name;
    }

    public void setOption_name(String option_name) {
        this.option_name = option_name;
    }

    public String getOption_var() {
        return option_var;
    }

    public void setOption_var(String option_var) {
        this.option_var = option_var;
    }

    public String getUs_code() {
        return us_code;
    }

    public void setUs_code(String us_code) {
        this.us_code = us_code;
    }

    public String getUs_name() {
        return us_name;
    }

    public void setUs_name(String us_name) {
        this.us_name = us_name;
    }

    public String getExe_type() {
        return exe_type;
    }

    public void setExe_type(String exe_type) {
        this.exe_type = exe_type;
    }

    public Double getStrike_price() {
        return strike_price;
    }

    public void setStrike_price(Double strike_price) {
        this.strike_price = strike_price;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getCall_put() {
        return call_put;
    }

    public void setCall_put(String call_put) {
        this.call_put = call_put;
    }

       public String getFirst_tradedate() {
        return first_tradedate;
    }

    public void setFirst_tradedate(String first_tradedate) {
        this.first_tradedate = first_tradedate;
    }

    public String getLast_tradedate() {
        return last_tradedate;
    }

    public void setLast_tradedate(String last_tradedate) {
        this.last_tradedate = last_tradedate;
    }

    public Double getChange() {
        return change;
    }

    public void setChange(Double change) {
        this.change = change;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getPre_settle() {
        return pre_settle;
    }

    public void setPre_settle(Double pre_settle) {
        this.pre_settle = pre_settle;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getHighest() {
        return highest;
    }

    public void setHighest(Double highest) {
        this.highest = highest;
    }

    public Double getLowest() {
        return lowest;
    }

    public void setLowest(Double lowest) {
        this.lowest = lowest;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public Double getSettlement_price() {
        return settlement_price;
    }

    public void setSettlement_price(Double settlement_price) {
        this.settlement_price = settlement_price;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Double getDelta() {
        return delta;
    }

    public void setDelta(Double delta) {
        this.delta = delta;
    }

    public Double getIn_value() {
        return in_value;
    }

    public void setIn_value(Double in_value) {
        this.in_value = in_value;
    }

    public Double getTime_value() {
        return time_value;
    }

    public void setTime_value(Double time_value) {
        this.time_value = time_value;
    }

    public Integer getValid() {
        return valid;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    @Override
    public String toString() {
        return "OptionPO{" +
                "option_code='" + option_code + '\'' +
                ", option_name='" + option_name + '\'' +
                ", option_var='" + option_var + '\'' +
                ", us_code='" + us_code + '\'' +
                ", us_name='" + us_name + '\'' +
                ", exe_type='" + exe_type + '\'' +
                ", strike_price=" + strike_price +
                ", month='" + month + '\'' +
                ", call_put='" + call_put + '\'' +
                ", first_tradedate='" + first_tradedate + '\'' +
                ", last_tradedate='" + last_tradedate + '\'' +
                ", change=" + change +
                ", amount=" + amount +
                ", pre_settle=" + pre_settle +
                ", open=" + open +
                ", highest=" + highest +
                ", lowest=" + lowest +
                ", close=" + close +
                ", settlement_price=" + settlement_price +
                ", volume=" + volume +
                ", position=" + position +
                ", delta=" + delta +
                ", in_value=" + in_value +
                ", time_value=" + time_value +
                ", valid=" + valid +
                ", update_time='" + update_time + '\'' +
                '}';
    }
}
