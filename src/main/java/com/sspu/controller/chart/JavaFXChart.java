package com.sspu.controller.chart;

import com.sspu.controller.port.PortListener;
import javafx.scene.chart.XYChart;

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
            if(!PortListener.messageState){
                continue;
            }else {

                /** 向表格添加数据 */
                RealList.add(PortListener.getDateAD());

                series = "series" + PortListener.getDateAD().getNode();
//                System.out.println("结点" + series);
//                System.out.println("最近一次接收 AD:" + ad + " 基准电压:" + ad_base);

                String time = PortListener.getDateAD().getTime().substring(11, 19);
                try {
                    /** 折线图显示
                     * X：时间 time
                     * Y：测量电压 ad
                     */
                    seriesArray[node].getData().add(
                            new XYChart.Data(time, PortListener.getDateAD().getHumidity())); //实时接收
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
                PortListener.messageState = false;
            }
        }
    }


}
