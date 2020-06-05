package com.sspu.controller;

import com.sspu.service.cloud.NioClient;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 云端通信
 * Top
 * @author ymj
 * @Date： 2020/4/17 20:33
 */
public class CloudView implements Initializable {

    /** 实时同步 */
    @FXML  public RadioButton realSyn;
    /** 关闭同步 */
    @FXML  public RadioButton closeSyn;

    /** 最近更新时间 */
    @FXML Label updateTime;

    /** 当前状态 */
    @FXML public Label status;


    public static NioClient nioClient= new NioClient();
    public static Thread threadNioClient;

    private static Object lock = new Object();

    public CloudView(){
        // NIO 云端通信
        threadNioClient = new Thread(nioClient);
        threadNioClient.setDaemon(true);
        threadNioClient.start();
//        cloudNIO = new Thread(new CloudNIO(), "cloudNIO");
//        cloudNIO.setDaemon(true);
//        cloudNIO.start();
    }

    /** 实时同步 */
    public void realSynFunction() throws InterruptedException {
        threadNioClient = new Thread(nioClient);
        threadNioClient.setDaemon(true);
        threadNioClient.start();
        System.out.println("实时同步");
        status.setText("同步中");

    }
    public void closeSynFunction() {

            System.out.println("关闭同步");
            threadNioClient.interrupt();
            status.setText("离线");

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ToggleGroup group = new ToggleGroup();
        realSyn.setToggleGroup(group);
        closeSyn.setToggleGroup(group);
        closeSyn.setSelected(true);

        Channel.controllers.put(this.getClass().getSimpleName(), this);
    }
}
