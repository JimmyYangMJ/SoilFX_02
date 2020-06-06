package com.sspu.controller.send;

import com.sspu.service.send.SendInterval;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

/**
 * 串口发送信息（向 下位机）
 * 视图：Top
 * @author ymj
 * @Date： 2019/12/24 23:53
 */
public class PortSend {

    @FXML ChoiceBox<String> nodeId;
    @FXML TextField interval;

    /** 定时发送数据线程 */
    public static Thread intervalSend;

    public static SendInterval sendInterval;

    /** 控件：确定
     * service： SendInterval*/
    @FXML
    public void intervalSend(){
        /** 获取 时间间隔 */
        String text = interval.getText();
        int interval = Integer.parseInt(text);
        SendInterval.interval = interval;
        sendInterval = new SendInterval();

        intervalSend  = new Thread(sendInterval, "portSend");
        intervalSend.setDaemon(true);
        intervalSend.start();
//        System.out.println(Thread.currentThread());
        System.out.println("定时发送消息" + interval);
    }

    @FXML
    public void start(){
        System.out.println("start");
        sendInterval.resumeThread();
    }
    @FXML
    public void end(){
        System.out.println("end");
//        System.out.println(intervalSend.isInterrupted());
//        intervalSend.interrupt();
        sendInterval.stopThread();

    }

    @FXML
    public void pause(){
        sendInterval.pauseThread();
        System.out.println("pause");
    }
}
