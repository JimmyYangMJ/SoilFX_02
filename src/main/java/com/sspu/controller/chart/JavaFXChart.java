package com.sspu.controller.chart;

import com.sspu.controller.port.PortTest;
import com.sspu.pojo.DataAD;
import javafx.scene.chart.XYChart;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.sspu.controller.realTime.RealTimeInfo.RealList;

/**
 * 动态折线图线程
 * 类型：守护线程
 * @author ymj
 * @Date： 2019/12/18 18:28
 */
public class JavaFXChart implements Runnable {


    /**  折线图中 已有 数据 个数 */
    private static int i = 0;

    /** 多条折线图数组(0,1,2) 设置容量为三条 */
    public static XYChart.Series[] seriesArray = {new XYChart.Series<String,Number>(),
            new XYChart.Series<String,Number>(),new XYChart.Series<String,Number>()};

    /** 节点编号（帧源） */
    private String series = "";

    private String firstMassage = null ;

    private int node = 1;

    /** 一张图最多显示 SHOW_NUMBER 个节点 */
    private int SHOW_NUMBER = 15;


    @Override
    public void run() {

        // seriesArray[1].getData().add(new XYChart.Data( " ", 50)); //第一个点 初始化

        while(true){
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e) {

            }
            /** 判断串口是否有消息
             * 阻塞状态*/
            if(!PortTest.messageState){
                continue;
            }else {
                Date date = new Date();
                SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss");
                String time = ft.format(date);
                /**
                 * 绑定数据
                 * 在表格显示
               */
                DataAD dataAD = new DataAD();
                //node = PortTest.message[3];
                /** 接收端口数据  2个字节 = 4个16进制
                 *  例如 原始16进制：Ox09 Ox02 == 0902 （16）
                 *  message ：9 2
                 *  转换 9*16*16 + 2 = （10）
                 */
                double ad = Double.parseDouble(String.valueOf(PortTest.message[6]))*16*16;
                ad += Double.parseDouble(String.valueOf(PortTest.message[7]));  // 合并
                ad = Double.parseDouble(String.format("%.4f", ad / 4098 * 3.3));

//              double ad_base = Double.parseDouble(String.valueOf(PortTest.message[8]) + String.valueOf(PortTest.message[9]));
                double ad_base = Double.parseDouble(String.valueOf(PortTest.message[8]))*16*16;
                ad_base += Double.parseDouble(String.valueOf(PortTest.message[9])); // 合并
                ad_base = Double.parseDouble(String.format("%.4f", ad_base / 4098 * 3.3));

                dataAD.setNode(node);
                dataAD.setAd(ad);
                dataAD.setAd_base(ad_base);
                dataAD.setHumidity(0);
                dataAD.setTime(time);

                /** 向表格添加数据 */
                RealList.add(dataAD);

                series = "series" + PortTest.message[3];
                System.out.println("结点" + series);
                System.out.println("最近一次接收 AD:" + ad + " 基准电压:" + ad_base);


                // Todo ad 转换为 水势

                try {
                    /** 折线图显示
                     * X：时间 time
                     * Y：测量电压 ad
                     */
                    seriesArray[node].getData().add(new XYChart.Data(time, ad)); //实时接收
                }catch (Exception e){

                }

                if(i == SHOW_NUMBER){
                    seriesArray[node].getData().remove(0);
                    System.out.println("节点信息"+  seriesArray[node].getNode());
                }
                if(i != SHOW_NUMBER)
                    i++;

                /** 标记接收数据为 false
                 * */
                PortTest.messageState = false;
            }
        }
    }


}
