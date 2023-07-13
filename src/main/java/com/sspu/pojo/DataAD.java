package com.sspu.pojo;

import org.springframework.stereotype.Component;

/**
 * AD 数据
 * 表格显示
 * @author ymj
 * @Date： 2019/12/18 20:25
 */

public class DataAD {

    private int node;
    private double ad;
    private double ad_base;
    private double humidity;
    private String time;

    public DataAD(int node, double ad, double ad_base, double humidity, String time) {
        this.node = node;
        this.ad = ad;
        this.ad_base = ad_base;
        this.humidity = humidity;
        this.time = time;
    }
    public DataAD() {

    }

    public int getNode() {
        return node;
    }

    public void setNode(int node) {
        this.node = node;
    }

    public double getAd() {
        return ad;
    }

    public void setAd(double ad) {
        this.ad = ad;
    }

    public double getAd_base() {
        return ad_base;
    }

    public void setAd_base(double ad_base) {
        this.ad_base = ad_base;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "DataAD{" +
                "node=" + node +
                ", ad=" + ad +
                ", ad_base=" + ad_base +
                ", humidity=" + humidity +
                ", time='" + time + '\'' +
                '}';
    }
}
