package com.sspu.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;

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
    /** 信息修改时间 */
    private String last_update;

    public SoilNode() {
        super();
    }

    public SoilNode(int node, String state, int interval_s, String last_update) {
        this.node = node;
        this.state = state;
        this.interval_s = interval_s;
        this.last_update = last_update;
    }

    @Override
    public String toString() {
        return "SoilNode{" +
                "node=" + node +
                ", state='" + state + '\'' +
                ", interval_s=" + interval_s +
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


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT")
    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }
}
