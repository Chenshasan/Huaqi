package com.example.Huaqi.vo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 折线图VO，Delta和时间价值都用这个
 */
public class PolylinesVO {
    private ArrayList<Double> x_axis;
    private ArrayList<ArrayList<Double>> y_axis;
    private ArrayList<String> date;

    public ArrayList<Double> getX_axis() {
        return x_axis;
    }

    public void setX_axis(ArrayList<Double> x_axis) {
        this.x_axis = x_axis;
    }

    public ArrayList<ArrayList<Double>> getY_axis() {
        return y_axis;
    }

    public void setY_axis(ArrayList<ArrayList<Double>> y_axis) {
        this.y_axis = y_axis;
    }

    public ArrayList<String> getDate() {
        return date;
    }

    public void setDate(ArrayList<String> date) {
        this.date = date;
    }
}
