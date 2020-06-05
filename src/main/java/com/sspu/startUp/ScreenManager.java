package com.sspu.startUp;

import com.sspu.constants.SpringFxmlLoader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.ibatis.io.ResolverUtil;

import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 *@Description TODO
 *@Author QuZhangJing
 *@Date 13:01  2018-08-07
 *@Version 1.0
 */
@Configuration
public class ScreenManager implements Observer  {

    private static SpringFxmlLoader loader = new SpringFxmlLoader();

    private Stage stage;

    /**
     * 总体布局
     * 分为
     */
    private BorderPane bp = new BorderPane();

    public void setPrimaryStage(Stage primaryStage) {
        this.stage = primaryStage;
    }

    public void showStage() throws IOException {
        stage.setTitle("土壤水势实时监测系统");

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
        /** 1. Top：顶部视图容器 */
        HBox topBox = new HBox();
        /** Top 1，2，3*/
        topBox.getChildren().addAll(portHBox, portSendHBox, cloudNIOBox);

        /** bp 加载 fxml 视图文件 */
        bp.setTop(topBox);

        //bp.setLeft(RealTimeVBox);
        bp.setLeft(loader.load("/fxml/realTimeInfo.fxml")); // Spring 管理

        bp.setCenter(Main.lineChart);  /** 4. Center ：中间视图容器 */

        //bp.setRight(dataBaseFormVBox);
        bp.setRight(loader.load("/fxml/dataBaseForm.fxml")); // Spring 管理


        stage.setScene(new Scene(bp, 1500, 1050));
        stage.setResizable(true);
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
