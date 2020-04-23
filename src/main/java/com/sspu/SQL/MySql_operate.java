package com.sspu.SQL;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySql_operate {
    final static String PROPERTIES_NAME = "jdbcInfo.properties";
    static Connection conn = null;

    /**
     * 从配置文件读取数据库相关配置信息
     * @param filePath
     * @param key
     * @return
     */
    public static String GetValueByKey(String filePath, String key)  {
        Properties pps = new Properties();
        try {
            InputStream in = new BufferedInputStream (new FileInputStream(filePath));
            pps.load(in); //所有的K-V对都加载了
            String value = pps.getProperty(key);
            return value;

        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 数据库连接
     * @return Connection
     * @throws SQLException
     */
    public static Connection JDBC_connect() throws SQLException {
        //读取配置文件信息
        //ss(5);
        String driver = GetValueByKey(PROPERTIES_NAME, "driver");
        String url = GetValueByKey(PROPERTIES_NAME, "url");
        String user = GetValueByKey(PROPERTIES_NAME, "user");
        String password = GetValueByKey(PROPERTIES_NAME, "password");
        //构建Java和数据库之间的桥梁介质

        // 1. 注册驱动

        try{
            Class.forName(driver);
            System.out.println("注册驱动成功!");
        }catch(ClassNotFoundException e1){
            System.out.println("注册驱动失败!");
            e1.printStackTrace();
        }

        try {
            //构建Java和数据库之间的桥梁：URL，用户名，密码
        //2. 创建连接
            conn = DriverManager.getConnection(url, user, password);
            //构建数据库执行者
            if(!conn.isClosed()){
                System.out.println("Succeeded connecting to the Database!");
                return conn;
            }
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Sorry,can`t connect the SQL!");
        }
        return conn;
    }

}
