package com.sspu.controller;

import com.sspu.service.IBinLogService;
import com.sspu.service.ISoilNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

/**
 * @author ymj
 * @Date： 2020/6/10 12:15
 */
@Controller
public class BinLog {


    @Autowired
    public ISoilNodeService soilNodeService;

    @Autowired
    public static BinLog binLog;


    public static void main(String[] args) {

        System.out.println( binLog.soilNodeService.selectSoilNodeSet());
    }
}
