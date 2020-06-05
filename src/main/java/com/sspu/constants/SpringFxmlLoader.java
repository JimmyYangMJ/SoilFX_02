package com.sspu.constants;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;


public class SpringFxmlLoader {

    private static final ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");

    /**
     * url 指向的 FXML 文件受 Spring 管理
     * @param url
     * @return
     */
    public Parent load(String url) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
            loader.setControllerFactory(applicationContext::getBean);
            return loader.load();
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

}