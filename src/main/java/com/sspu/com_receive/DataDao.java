package com.sspu.com_receive;
import com.sspu.SQL.MySql_operate;
import com.sspu.pojo.SoilNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * 数据Dao层--数据库操作
 */
public class DataDao implements Initializable {

    @FXML TableView<SoilNode> RealTableView1;
    @FXML TableColumn node1;
    @FXML TableColumn state;
    @FXML TableColumn interval_s;
    @FXML TableColumn last_update;


    /** 实时表格数据集合 */
    public static ObservableList<SoilNode> RealList;

    private static boolean con1 = false;
    static Connection connection;

    /**
     * 数据库是否连接
     * @throws SQLException
     */
    public static void isDBConnnect() throws SQLException {
        if(con1 == false){
            connection = MySql_operate.JDBC_connect();
//            if(connection.isClosed())
////                log("<---数据库连接失败--->");
//            else
//                LogTextArea.log("<---数据库连接成功-->");
            con1 = true;
        }
    }


    /**
     * 查询数据表共多少数据
     * @return
     */
    public static int DataCounter(String tableName) throws SQLException {
        if(con1 == false){
            connection = MySql_operate.JDBC_connect();
            if(connection.isClosed())
                System.out.println("<---数据库连接失败--->");
            else
                System.out.println("<---数据库连接成功-->");
            con1 = true;
        }
        int count = 0;
        PreparedStatement preparedStatement = null;
        String sql = "select count(*) number from ";
        sql += tableName;
        try {
            preparedStatement = connection.prepareStatement(sql);
            //preparedStatement.setNString(1, tableName );

            ResultSet rs = preparedStatement.executeQuery();

            System.out.println("成功查询数据库");

            if(rs.next()){
                count = rs.getInt("number");
            }
            return count;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 查询具体某一天数据
     * @return ResultSet
     * @throws SQLException
     */
    public static ResultSet ReadData() throws SQLException {

        isDBConnnect(); //判断数据库是否连接

        PreparedStatement preparedStatement = null;
        String sql = "select * from soil_node";

        try {
            preparedStatement = connection.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();

            System.out.println("成功查询数据库");
//
            return rs;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @FXML
    public void getNode()  {
        RealList.clear();

        try {
            ResultSet rs = ReadData();
            SoilNode soilNode = new SoilNode();
            String data;
            while(rs.next()) {
                soilNode = new SoilNode();
                data = rs.getString("node");
                System.out.println(data);
                soilNode.setNode(Integer.parseInt(data));
                data = rs.getString("state");
                System.out.println(data);
                soilNode.setState(data);
                data = rs.getString("interval_s");
                System.out.println(data);
                soilNode.setInterval_s(Integer.parseInt(data));

                soilNode.setLast_update(rs.getDate("last_update"));
                // 显示
                RealList.add(soilNode);

            }
        } catch (Exception e) {
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

