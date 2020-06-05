package com.sspu.controller.table;


import com.sspu.constants.NodeStatus;
import com.sspu.pojo.SoilNode;
import com.sspu.service.ISoilNodeService;
import com.sspu.service.impl.SoilNodeServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author ymj
 * @Date： 2020/4/28 16:32
 */
@Controller
public class NodeTable implements Initializable {

    //private static final ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
    @FXML
    TableView<SoilNode> RealTableView1;
    @FXML
    TableColumn node1;
    @FXML TableColumn state;
    @FXML TableColumn interval_s;
    @FXML TableColumn last_update;

    /** 实时表格数据集合 */
    public static ObservableList<SoilNode> RealList;

    @Autowired
    ISoilNodeService soilNodeService;
    /**
     * 查询具体结点信息 Lookup 按钮
     * @return ResultSet
     * @throws SQLException
     */
    @FXML
    public void getNode()  {
        RealList.clear();
        //NodeTable nodeTable = (NodeTable) applicationContext.getBean("NodeTable");
        try {
//            List<SoilNode> rs = nodeTable.soilNodeService.selectSoilNodeSet();
            List<SoilNode> rs = soilNodeService.selectSoilNodeSet();
            Iterator<SoilNode> iterator = rs.iterator();
            SoilNode soilNode;
            while(iterator.hasNext()) {
                soilNode = iterator.next();
                String status = "status" + soilNode.getState();
                soilNode.setState(NodeStatus.valueOf(status).getStateMessage());
                RealList.add(soilNode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        node1.setCellValueFactory(new PropertyValueFactory<>("node"));
        state.setCellValueFactory(new PropertyValueFactory<>("state"));
        interval_s.setCellValueFactory(new PropertyValueFactory<>("interval_s"));
        last_update.setCellValueFactory(new PropertyValueFactory<>("last_update"));
        RealList = FXCollections.observableArrayList();
        RealTableView1.setItems(RealList);
    }
}
