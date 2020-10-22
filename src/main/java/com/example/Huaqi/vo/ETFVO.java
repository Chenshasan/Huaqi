package com.example.Huaqi.vo;

import java.util.Date;

public class ETFVO {
    String etf_code;
    String etf_name;
    Double rt_last;
    Double rt_chg;
    Double rt_pcg_chg;
    Double rt_open;
    Double rt_high;
    Double rt_low;
    Double rt_vol;
    Double rt_amt;
    Double option_vol;
    Double option_op;
    Integer valid;
    String update_time;

    public String getEtf_code() {
        return etf_code;
    }

    public void setEtf_code(String etf_code) {
        this.etf_code = etf_code;
    }

    public String getEtf_name() {
        return etf_name;
    }

    public void setEtf_name(String etf_name) {
        this.etf_name = etf_name;
    }

    public Double getRt_last() {
        return rt_last;
    }

    public void setRt_last(Double rt_last) {
        this.rt_last = rt_last;
    }

    public Double getRt_chg() {
        return rt_chg;
    }

    public void setRt_chg(Double rt_chg) {
        this.rt_chg = rt_chg;
    }

    public Double getRt_pcg_chg() {
        return rt_pcg_chg;
    }

    public void setRt_pcg_chg(Double rt_pcg_chg) {
        this.rt_pcg_chg = rt_pcg_chg;
    }

    public Double getRt_open() {
        return rt_open;
    }

    public void setRt_open(Double rt_open) {
        this.rt_open = rt_open;
    }

    public Double getRt_high() {
        return rt_high;
    }

    public void setRt_high(Double rt_high) {
        this.rt_high = rt_high;
    }

    public Double getRt_low() {
        return rt_low;
    }

    public void setRt_low(Double rt_low) {
        this.rt_low = rt_low;
    }

    public Double getRt_vol() {
        return rt_vol;
    }

    public void setRt_vol(Double rt_vol) {
        this.rt_vol = rt_vol;
    }

    public Double getRt_amt() {
        return rt_amt;
    }

    public void setRt_amt(Double rt_amt) {
        this.rt_amt = rt_amt;
    }

    public Double getOption_vol() {
        return option_vol;
    }

    public void setOption_vol(Double option_vol) {
        this.option_vol = option_vol;
    }

    public Double getOption_op() {
        return option_op;
    }

    public void setOption_op(Double option_op) {
        this.option_op = option_op;
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
}
