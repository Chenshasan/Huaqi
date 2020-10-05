package com.example.Huaqi.vo;
import java.util.Date;
public class PutOptionVO implements Comparable<PutOptionVO> {
    String optioncode;//唯一标识符
    String name;
    double price;
    double execPrice;
    String ETFName;
    double ETFPrice;
    Date time;
    double delta;
    double avg1_2;//买一买二平均值
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

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public double getDelta() {
        return delta;
    }

    public double getAvg1_2() {
        return avg1_2;
    }

    public void setAvg1_2(double avg1_2) {
        this.avg1_2 = avg1_2;
    }

    //把List按delta值的升序排列，能顺序向上取到
    @Override
    public int compareTo(PutOptionVO putOptionVO){
        if(this.getDelta()-putOptionVO.getDelta()>=0){
            return 1;
        }else {
            return -1;
        }
    }

    @Override
    public String toString() {
        return "optionCode"+this.optioncode+"price"+this.price+"execPrice"+this.execPrice+"ETFPrice"+this.ETFPrice+"delta"+this.delta+"avg1_2"+this.avg1_2+"num"+this.num;
    }
}
