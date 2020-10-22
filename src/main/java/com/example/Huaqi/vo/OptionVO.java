package com.example.Huaqi.vo;

import java.util.Date;

public class OptionVO implements Comparable<OptionVO> {
    String optioncode; //唯一标识符 option_code
    String name;//期权名 option_name
    String option_var; // 期权品种
    String us_code; // 标的物代码
    String us_name; // 标的物名
    String exe_type; // 期权类型
    String month; // 交割月份
    String call_put; // 期权类型 认购or认沽
    String etf_price; //ETF价格
    String expiredate; // 剩余存续期
    String first_tradedate; // 起始交易日期
    String last_tradedate; // 最后交易日期

    double price;//期权价格
    double execPrice;//执行价格
    String ETFName;
    double ETFPrice;//ETF价格
    Date time;
    double delta;//delta值
    double avg1_2;//买一买二平均值
    double timeprice;//时间价值
    int num;
    int ETFNum;
    //买一到买十的价格，卖一到卖十的价格
    double ask1;
    double ask2;
    double ask3;
    double ask4;
    double ask5;
    double ask6;
    double ask7;
    double ask8;
    double ask9;
    double ask10;
    double bid1;
    double bid2;
    double bid3;
    double bid4;
    double bid5;
    double bid6;
    double bid7;
    double bid8;
    double bid9;
    double bid10;

    public double getAsk1() {
        return ask1;
    }

    public double getAsk2() {
        return ask2;
    }

    public double getAsk3() {
        return ask3;
    }

    public double getAsk4() {
        return ask4;
    }

    public double getAsk5() {
        return ask5;
    }

    public double getAsk6() {
        return ask6;
    }

    public double getAsk7() {
        return ask7;
    }

    public double getAsk8() {
        return ask8;
    }

    public double getAsk9() {
        return ask9;
    }

    public double getAsk10() {
        return ask10;
    }

    public double getBid1() {
        return bid1;
    }

    public double getBid2() {
        return bid2;
    }

    public double getBid3() {
        return bid3;
    }

    public double getBid4() {
        return bid4;
    }

    public double getBid5() {
        return bid5;
    }

    public double getBid6() {
        return bid6;
    }

    public double getBid7() {
        return bid7;
    }

    public double getBid8() {
        return bid8;
    }

    public double getBid9() {
        return bid9;
    }

    public double getBid10() {
        return bid10;
    }

    public void setAsk1(double ask1) {
        this.ask1 = ask1;
    }

    public void setAsk2(double ask2) {
        this.ask2 = ask2;
    }

    public void setAsk3(double ask3) {
        this.ask3 = ask3;
    }

    public void setAsk4(double ask4) {
        this.ask4 = ask4;
    }

    public void setAsk5(double ask5) {
        this.ask5 = ask5;
    }

    public void setAsk6(double ask6) {
        this.ask6 = ask6;
    }

    public void setAsk7(double ask7) {
        this.ask7 = ask7;
    }

    public void setAsk8(double ask8) {
        this.ask8 = ask8;
    }

    public void setAsk9(double ask9) {
        this.ask9 = ask9;
    }

    public void setAsk10(double ask10) {
        this.ask10 = ask10;
    }

    public void setBid1(double bid1) {
        this.bid1 = bid1;
    }

    public void setBid2(double bid2) {
        this.bid2 = bid2;
    }

    public void setBid3(double bid3) {
        this.bid3 = bid3;
    }

    public void setBid4(double bid4) {
        this.bid4 = bid4;
    }

    public void setBid5(double bid5) {
        this.bid5 = bid5;
    }

    public void setBid6(double bid6) {
        this.bid6 = bid6;
    }

    public void setBid7(double bid7) {
        this.bid7 = bid7;
    }

    public void setBid8(double bid8) {
        this.bid8 = bid8;
    }

    public void setBid9(double bid9) {
        this.bid9 = bid9;
    }

    public void setBid10(double bid10) {
        this.bid10 = bid10;
    }

    public int getETFNum() {
        return ETFNum;
    }

    public void setETFNum(int ETFNum) {
        this.ETFNum = ETFNum;
    }


    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

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

    public String getOptioncode() {
        return optioncode;
    }

    public void setOptioncode(String optioncode) {
        this.optioncode = optioncode;
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

    public double getTimeprice() {
        return timeprice;
    }

    public void setTimeprice(double timeprice) {
        this.timeprice = timeprice;
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

    public String getEtf_price() {
        return etf_price;
    }

    public void setEtf_price(String etf_price) {
        this.etf_price = etf_price;
    }

    public String getExpiredate() {
        return expiredate;
    }

    public void setExpiredate(String expiredate) {
        this.expiredate = expiredate;
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

    //把List按时间价值的升序排列，能顺序向上取到
    @Override
    public int compareTo(OptionVO optionVO) {

        if (this.getTimeprice() - optionVO.getTimeprice() >= 0) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public String toString() {
        return "optionCode" + this.optioncode + "price" + this.price + "execPrice" + this.execPrice + "ETFPrice" + this.ETFPrice + "delta" + this.delta + "avg1_2" + this.avg1_2 + "num" + this.num + "timePrice" + this.timeprice;
    }
}
