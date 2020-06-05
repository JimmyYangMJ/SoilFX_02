package com.mybatis;

import com.sspu.pojo.SoilNode;
import com.sspu.service.impl.SoilNodeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ymj
 * @Date： 2020/4/28 15:05
 */

@Component("Testas")
public class Test {

    @Autowired
    SoilNodeServiceImpl soilNodeService;

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");

        Test uc = (Test)applicationContext.getBean("Testas");
        List<SoilNode> list = uc.soilNodeService.selectSoilNodeSet();
        System.out.println(list.size());

    }
}
