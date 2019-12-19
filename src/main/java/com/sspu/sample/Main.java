package com.sspu.sample;

import com.sspu.controller.chart.JavaFXChart;
import com.sspu.controller.port.PortTest;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static com.sspu.controller.chart.JavaFXChart.seriesArray;

public class Main extends Application {

    private BorderPane bp = new BorderPane(); //总体布局

    Thread javaFXLine ;
    CategoryAxis xAxis;
    NumberAxis yAxis;
    LineChart<String,Number> lineChart;

    /**
     * 水势折线图
     */
    public void JavaFXChart(){
        JavaFXChart FXChart = new JavaFXChart ();
        Thread F = new Thread(FXChart,"JavaFX 折线图");
        F.start(); //开始添加节点线程
        javaFXLine = F;     //线程赋值

        /** 基本信息初始化 */
        xAxis = new CategoryAxis();
        yAxis = new NumberAxis(0,15,0.2);
        xAxis.setLabel("时间");
        yAxis.setLabel("水势");

        lineChart = new LineChart(xAxis,yAxis);
        lineChart.setCreateSymbols(false);// 没有具体的点
        lineChart.setVerticalGridLinesVisible(false);

        lineChart.autosize();
        lineChart.alternativeColumnFillVisibleProperty();
        //lineChart.setMaxWidth(5);
        String node01 = "节点1";
        seriesArray[1].setName(node01);
        lineChart.getData().addAll(seriesArray[1]);
    }//水势折线图

    @Override
    public void start(Stage primaryStage) throws Exception{

        JavaFXChart();

        FXMLLoader portConnect = new FXMLLoader(getClass().getResource("/fxml/portTest.fxml"));
        FXMLLoader RealTimeInfo = new FXMLLoader(getClass().getResource("/fxml/realTimeInfo.fxml"));
        FXMLLoader Chart = new FXMLLoader(getClass().getResource("/fxml/lineChart.fxml"));
        HBox portHBox = portConnect.load();
        VBox RealTimeVBox = RealTimeInfo.load();
        AnchorPane ChartAnchorPane = Chart.load();
        bp.setTop(portHBox);
        bp.setLeft(RealTimeVBox);
        bp.setCenter(lineChart);
        //bp.setCenter(ChartAnchorPane);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(bp, 1500, 1000));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        javaFXLine.stop();      //停止线程
        try {
            PortTest.serialPort.close();
            System.out.print("结束");
        }catch (Exception e){
            //log("尚未端口没有打开");
        } //关闭端口
//        ChartThear.stop();
    }

}
