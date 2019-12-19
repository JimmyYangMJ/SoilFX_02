package com.sspu.controller.chart;

import com.sspu.controller.port.PortTest;
import com.sspu.pojo.DataAD;
import javafx.scene.chart.XYChart;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.sspu.controller.realTime.RealTimeInfo.RealList;

/**
 * @author ymj
 * @Date： 2019/12/18 18:28
 */
public class JavaFXChart implements Runnable {


    private int i = 0;
    /** 多条折线图(0,1,2) 三条 */
    public static XYChart.Series[] seriesArray = {new XYChart.Series<String,Number>(),
            new XYChart.Series<String,Number>(),new XYChart.Series<String,Number>()};

    private String series = "";

    private String firstMassage = null ;

    private int node = 1;

    private int SHOW_NUMBER = 15; //一张图显示 SHOW_NUMBER 个节点

    @Override
    public void run() {

        //seriesArray[1].getData().add(new XYChart.Data( " ", 50));//第一个点 初始化
        while(true){
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e) {

            }
            if(PortTest.messageState == false){
                continue;
            }
            Date date = new Date();
            SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss");
            String time = ft.format(date);
            /** 绑定数据 */
            DataAD dataAD = new DataAD();
            //node = PortTest.message[3];
            double ad = Double.parseDouble(String.valueOf(PortTest.message[6]) + "."+ String.valueOf(PortTest.message[7]));
            double ad_base = Double.parseDouble(String.valueOf(PortTest.message[8]) + "."+ String.valueOf(PortTest.message[9]));
            dataAD.setNode(node);
            dataAD.setAd(ad);
            dataAD.setAd_base(ad_base);
            dataAD.setHumidity(0);
            dataAD.setTime(time);
            ////////

            /** 向表格添加数据 */
            RealList.add(dataAD);

            series = "series" + PortTest.message[3];  //节点值
            System.out.println(series);
            System.out.println("最近一次接收 AD:" + ad + " 基准电压:" + ad_base);

            try {
                seriesArray[node].getData().add(new XYChart.Data(time, ad-10));//实时接收
            }catch (Exception e){

            }

            if(i == SHOW_NUMBER){
                seriesArray[node].getData().remove(0);
                System.out.println("节点信息"+  seriesArray[node].getNode());
            }
            if(i!= SHOW_NUMBER)i++;

            PortTest.messageState = false; //
        }
    }

}
