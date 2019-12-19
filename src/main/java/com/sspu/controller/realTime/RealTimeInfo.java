package com.sspu.controller.realTime;

import com.sspu.controller.Main;
import com.sspu.controller.port.PortTest;
import com.sspu.pojo.DataAD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * @author ymj
 * @Date： 2019/12/18 16:22
 */
public class RealTimeInfo  implements Initializable{

    @FXML public TextArea textArea;

    @FXML TableView<DataAD> RealTableView; //AD数据表格显示
    @FXML TableColumn node;
    @FXML TableColumn ad;
    @FXML TableColumn ad_base;
    @FXML TableColumn humidity;
    @FXML TableColumn time;

    /** 实时表格数据集合 */
    public static ObservableList<DataAD> RealList;

    public void log(String st){
        textArea.appendText(st+"\n");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textArea.appendText("-----输出提示信息------"+"\n");

        node.setCellValueFactory(new PropertyValueFactory<>("node"));
        ad.setCellValueFactory(new PropertyValueFactory<>("ad"));
        ad_base.setCellValueFactory(new PropertyValueFactory<>("ad_base"));
        humidity.setCellValueFactory(new PropertyValueFactory<>("humidity"));
        time.setCellValueFactory(new PropertyValueFactory<>("time"));
        RealList = FXCollections.observableArrayList();

        Date date = new Date();
        String time = String.format("%tT%n",date);;
        DataAD dataAD = new DataAD(1, 0.0, 0.0,0.0, time);
        RealList.add(dataAD);
        RealTableView.setItems(RealList); // 插入 list数据


        Main.controllers.put(this.getClass().getSimpleName(), this);
    }
}
