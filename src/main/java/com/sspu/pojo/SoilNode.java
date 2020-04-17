package com.sspu.pojo;


import java.util.Date;

/**
 * 结点信息表
 * @author ymj
 * @Date： 2019/11/13 20:10
 */
public class SoilNode {

    /** 结点编号*/
    private int node;
    /** 结点状态 */
    private String state = null;
    /** 监测频率， 单位秒*/
    private int interval_s = 0;
    /** 经度 */
    private double lng;
    /** 纬度 */
    private double lat;
    /** 信息修改时间 */
    private Date last_update;

    public SoilNode() {
        super();
    }

    public SoilNode(int node, String state, int interval_s, double lng, double lat, Date last_update) {
        this.node = node;
        this.state = state;
        this.interval_s = interval_s;
        this.lng = lng;
        this.lat = lat;
        this.last_update = last_update;
    }

    @Override
    public String toString() {
        return "SoilNode{" +
                "node=" + node +
                ", state='" + state + '\'' +
                ", interval_s=" + interval_s +
                ", lng=" + lng +
                ", lat=" + lat +
                ", last_update=" + last_update +
                '}';
    }

    public int getNode() {
        return node;
    }

    public void setNode(int node) {
        this.node = node;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getInterval_s() {
        return interval_s;
    }

    public void setInterval_s(int interval_s) {
        this.interval_s = interval_s;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT")
    public Date getLast_update() {
        return last_update;
    }

    public void setLast_update(Date last_update) {
        this.last_update = last_update;
    }
}
