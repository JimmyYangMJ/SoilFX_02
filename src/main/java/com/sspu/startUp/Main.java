package com.sspu.startUp;

import com.sspu.controller.chart.JavaFXChart;
import com.sspu.controller.port.PortTest;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static com.sspu.controller.chart.JavaFXChart.seriesArray;

public class Main extends Application {

    /**
     * 总体布局
     * 分为
     */
    private BorderPane bp = new BorderPane();

    // Thread javaFXLine ;
    /** X 轴为 时间 */
    CategoryAxis xAxis;
    /** Y 轴为 水势（double） */
    NumberAxis yAxis;

    /** 折线图 视图 */
    LineChart<String, Number> lineChart;

    /**
     * 水势折线图
     */
    public void JavaFXChart(){

        /** 运行 动态折线图 线程 */
        JavaFXChart FXChart = new JavaFXChart ();
        Thread F = new Thread(FXChart,"JavaFX 折线图");
        F.setDaemon(true);  // 设为 守护线程
        F.start(); // 开始添加节点线程
        // javaFXLine = F;     // 线程赋值 javaFXLine（过时）


        /** 基本信息初始化 */
        xAxis = new CategoryAxis();
        yAxis = new NumberAxis(0,15,0.2);
        xAxis.setLabel("时间");
        yAxis.setLabel("水势");

        lineChart = new LineChart(xAxis,yAxis);
        lineChart.setCreateSymbols(false); // 图上没有具体的点
        lineChart.setVerticalGridLinesVisible(false);

        lineChart.autosize();
        lineChart.alternativeColumnFillVisibleProperty();
        //lineChart.setMaxWidth(5);
        /** 一个结点 */
        String node01 = "节点1";
        seriesArray[1].setName(node01);

        lineChart.getData().addAll(seriesArray[1]);
    }//水势折线图

    /**
     * JavaFX 启动 方法
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{

        /** 启动动态折线图线程 */
        JavaFXChart();

        /** 串口设置 */
        FXMLLoader portConnect = new FXMLLoader(getClass().getResource("/fxml/portTest.fxml"));
        /** 实时信息 */
        FXMLLoader RealTimeInfo = new FXMLLoader(getClass().getResource("/fxml/realTimeInfo.fxml"));
        //FXMLLoader Chart = new FXMLLoader(getClass().getResource("/fxml/lineChart.fxml"));
        /** 数据表显示 */
        FXMLLoader dataBaseForm = new FXMLLoader(getClass().getResource("/fxml/dataBaseForm.fxml"));
        /** 检测频率 */
        FXMLLoader portSend = new FXMLLoader(getClass().getResource("/fxml/portSend.fxml"));
        /** 云端通信 NIO */
        FXMLLoader cloudNIO = new FXMLLoader(getClass().getResource("/fxml/cloudNIO.fxml"));

        /** 1. Top：顶部视图容器 */
        HBox topBox = new HBox();

        /** load 串口初始化 视图组件（Top1） */
        HBox portHBox = portConnect.load();

        /** 定时监测 （Top 2） */
        HBox portSendHBox = portSend.load();

        /** 云端通信 （Top3）*/
        HBox cloudNIOBox = cloudNIO.load();

        /** 2. Left：左边视图容器  */
        VBox RealTimeVBox = RealTimeInfo.load();

        /** 3. Right：右边视图容器 */
        VBox dataBaseFormVBox = dataBaseForm.load();
        //AnchorPane ChartAnchorPane = Chart.load();
        /** Top 1，2，3*/
        topBox.getChildren().addAll(portHBox, portSendHBox, cloudNIOBox);

        /** bp 加载 fxml 视图文件 */
        bp.setTop(topBox);
        bp.setLeft(RealTimeVBox);
        bp.setCenter(lineChart);  /** 4. Center ：中间视图容器 */
        bp.setRight(dataBaseFormVBox);

        /** Todo 连接云端 */

        //bp.setCenter(ChartAnchorPane);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(bp, 1500, 1000));
        primaryStage.show();
    }


    /**
     * 总启动 主函数
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /** 主函数终止后操作 */
    @Override
    public void stop() throws Exception {
        super.stop();
        // javaFXLine.stop();      //停止线程，过时方法
        try {
            /** 串口关闭 */
            PortTest.serialPort.close();
            System.out.print("结束");
        }catch (Exception e){
            //log("尚未端口没有打开");
        } //关闭端口
//        ChartThear.stop();
    }

}
