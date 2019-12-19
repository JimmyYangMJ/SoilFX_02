package com.sspu.controller.port;

import com.sspu.controller.AlertBox;
import com.sspu.controller.Main;
import com.sspu.controller.realTime.RealTimeInfo;
import com.sspu.service.handerData.CRC;
import gnu.io.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.*;

/**
 * @author ymj
 * @Date： 2019/12/18 15:11
 */
public class PortTest implements Initializable , SerialPortEventListener {

    private int timeout = 1000;//open端口时的等待时间

    /** RXTX com端口检测*/
    public CommPortIdentifier commPort;
    public static SerialPort serialPort;        //RXTX


    private InputStream inputStream; //输入流
    public OutputStream outputStream = null; //输出流

    /** 数据位 校验位 波特率 停止位 */
    @FXML private ChoiceBox<String> CBdateBit, CBparity, CBbaudRate, CBstopBit;
    /** 选择端口 */
    @FXML private ChoiceBox<String> com_choice;
    /** 向串口发送消息 */
    @FXML TextField TFcomSentText;

    /** 提示信息类 */
    public static RealTimeInfo realTimeInfo;

    /** 是否接收到消息 */
    public static boolean messageState = false;
    /** 消息信息*/
    public static int[] message = null;

    private static LinkedList<String> ports;
    /**
     * 显示PC中开启的端口
     * (1. 识别端口) 按钮
     */
    public void StartComClicked(){
        CommPortIdentifier commPortIdentifier;
        Enumeration en = CommPortIdentifier.getPortIdentifiers();
        System.out.println("下面列出pc中的所有端口:");

        /** 获取 另一个类的实例*/
        realTimeInfo = (RealTimeInfo) Main.controllers.get(RealTimeInfo.class.getSimpleName());
        realTimeInfo.log("下面列出pc中的所有端口:");

        ports = new LinkedList<>();
        while (en.hasMoreElements()) {
            commPortIdentifier = (CommPortIdentifier) en.nextElement();
            if (commPortIdentifier.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                //打印端口名称和该端口所有者
                if(commPortIdentifier.getCurrentOwner() == null){
                    ports.add(commPortIdentifier.getName());
                }
                System.out.println("端口名称：" + commPortIdentifier.getName() + " , " + commPortIdentifier.getCurrentOwner());
                realTimeInfo.log("端口名称：" + commPortIdentifier.getName() + " , " + commPortIdentifier.getCurrentOwner());
            }
        }
        String[] post = ports.toArray(new String[ports.size()]);
        com_choice.getItems().addAll(post);
        if (post.length == 0){
            com_choice.setValue("无port");
        }else{
            com_choice.setValue(post[0]);
        }


    }

    /**
     * 选择接收端口
     * (4. 确定端口)按钮
     */
    public void selectPort() {
        String portName = com_choice.getValue(); //获得选择框的值

        this.commPort = null;
        CommPortIdentifier cpid;
        Enumeration en = CommPortIdentifier.getPortIdentifiers();
        while (en.hasMoreElements()) {
            cpid = (CommPortIdentifier) en.nextElement();
            if (cpid.getPortType() == CommPortIdentifier.PORT_SERIAL && cpid.getName().equals(portName)) {
                this.commPort = cpid;
                break;
            }
        }
        /** 开启端口 */
        openPort();
    }

    /**
     * 开启选择的端口
     */
    public void openPort() {
        if (commPort == null){
            AlertBox.display("端口","尚未选择端口");
            //log(String.format("无法找到名字为'%s'的串口！", commPort.getName()));
        }else {
            System.out.println("端口选择成功，当前端口：<" + commPort.getName() + ">,现在实例化 SerialPort:");
            realTimeInfo.log("端口选择成功，当前端口：<" + commPort.getName() + "> ");
            try {
                serialPort = (SerialPort) commPort.open("已占用", timeout);
                /**
                 * 设置波特率/ 校验位 /数据位 /停止位
                 */
                int baudRate = Integer.parseInt(CBbaudRate.getValue());
                int dateBit = Integer.parseInt(CBdateBit.getValue()); //SerialPort.DATABITS_8

                int parity = 0; //
                if(CBparity.getValue().equals("NONE"))
                    parity = 0;
                int stopBit = Integer.parseInt(CBstopBit.getValue()); //SerialPort.STOPBITS_1

                System.out.println(dateBit + "***" + parity);
                serialPort.setSerialPortParams(baudRate,  //波特率
                        dateBit, 			//数据位
                        stopBit, 			//停止位
                        parity);		//校验位

                System.out.println("实例 SerialPort 成功！");
            } catch (PortInUseException e) {
                /*throw new RuntimeException(String.format("端口'%1$s'正在使用中！",
                        commPort.getName()));*/
                AlertBox.display("端口",String.format("端口'%1$s'正在使用中！",
                        commPort.getName()));
            } catch (UnsupportedCommOperationException e) {
                e.printStackTrace();
            }
        }
    }//打开端口


    /**
     * 检测是否有端口
     */
    private void checkPort() {
        if (commPort == null){
            //throw new RuntimeException("没有选择端口，请使用 " + "selectPort(String portName) 方法选择端口");
            AlertBox.display("端口","没有选择端口");
        }
        if (serialPort == null) {
            //throw new RuntimeException("SerialPort 对象无效！");
            AlertBox.display("选择无效","重新选择");
        }
    }//检测是否有端口

    /**
     * 开始读取数据（开始监听端口）
     * (5. 开始接收端口数据)按钮
     */
    public void startRead() {
        checkPort(); //检测是否有端口
        try {
            inputStream = new BufferedInputStream(serialPort.getInputStream());
        } catch (Exception e) {
            throw new RuntimeException("获取端口的InputStream出错：" + e.getMessage());
        }//打开输入流

        try {
            outputStream = serialPort.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }//打开输出流
        try {
            serialPort.addEventListener(this);
        } catch (TooManyListenersException e) {
            throw new RuntimeException(e.getMessage());
        }
        serialPort.notifyOnDataAvailable(true);
        System.out.println(String.format("开始监听来自 '%1$s'端口 的数据--------------", commPort.getName()));
        realTimeInfo.log(String.format("开始监听 <%1$s>端口数据", commPort.getName()));

    }//开始读取数据

    /**
     * 数据接收的监听处理函数（接收数据）
     * @param arg0
     */
    @Override
    public void serialEvent(SerialPortEvent arg0) {//接收串口信息
        switch (arg0.getEventType()) {
            case SerialPortEvent.BI:/*Break interrupt,通讯中断*/
            case SerialPortEvent.OE:/*Overrun error，溢位错误*/
            case SerialPortEvent.FE:/*Framing error，传帧错误*/
            case SerialPortEvent.PE:/*Parity error，校验错误*/
            case SerialPortEvent.CD:/*Carrier detect，载波检测*/
            case SerialPortEvent.CTS:/*Clear to send，清除发送*/
            case SerialPortEvent.DSR:/*Data set ready，数据设备就绪*/
            case SerialPortEvent.RI:/*Ring indicator，响铃指示*/
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:/*Output buffer is empty，输出缓冲区清空*/
                break;
            case SerialPortEvent.DATA_AVAILABLE:/*Data available at the serial port，端口有可用数据。读到缓冲数组，输出到终端*/
                byte[] readBuffer = new byte[1024];
                String readStr = "";
                try {
                    while (inputStream.available() > 0) {     //\r\n
                        inputStream.read(readBuffer);
                        readStr += new String(readBuffer).trim();
                    }
                    /**
                     * s2为接收到数据 格式： 16进制+CRC校验码
                     *t1存放CRC校验前的数据，t2为CRC校验后的数据
                     */
                    String t1="";
                    byte[] receiveByte = Arrays.copyOf(readBuffer, readBuffer[0]);
                    t1= CRC.bytes_String16(receiveByte);
                    String a =t1.substring(20,22);//发送端CRC前两位校验码
                    String b =t1.substring(22,24);//发送端CRC后两位校验码
                    readBuffer= CRC.AddCRC(receiveByte);//进行CRC校验
                    String t2="";

                    t2= CRC.bytes_String16(receiveByte);;//去除字符串末尾的0
                    System.out.print("*" + t2);
                    String c=t2.substring(20,22);//接收端CRC校验前两位校验码
                    String d=t2.substring(22,24);//接收端CRC校验后两位校验码
                    if (a.equals(c)&&b.equals(d))//将发送端与接受端校验码进行比对
                    {   System.out.println("数据传输正确");
                        System.out.println("CRC:"+a+" "+b);
                        /*从字节数组中读取crc校验码*/
                        System.out.println("CRC:"+Integer.toHexString(0xff & receiveByte[10])+" "+Integer.toHexString(0xff & receiveByte[11]));
                    }
                    else {
                        System.out.println("数据传输错误");
                        System.out.println("CRC:"+a+" "+b);
                        /*从字节数组中读取crc校验码*/
                        System.out.println("CRC:"+Integer.toHexString(0xff & receiveByte[10])+" "+Integer.toHexString(0xff & receiveByte[11]));
                        break;//数据舍弃
                    }

                    /**********************/
                    int messageLength = (int)readBuffer[0];
                    message = new int[messageLength];
                    System.out.println("接收数据---长度--" + messageLength);
                    for (int i = 0; i < messageLength; i++) {
                        int temp = (int)(readBuffer[i] & 0xff);
                        System.out.print( temp + "/");
                        message[i] = temp;
                    }
                    System.out.println("接收>>>");

                    realTimeInfo.log("接收到端口返回数据" +"(长度为" + message.length + ")" );
                    realTimeInfo.log(Arrays.toString(message));
                    messageState = true; // 标记接收到数据
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }//数据接收的监听处理函数

    /** 数据校验*/


    /**
     * 向串口发送消息
     */
    public void ComSent() {
        String message = TFcomSentText.getText();
        sendDataToSeriaPort(message);
    }

    /**
     * 给串行端口发送数据
     * @since 2019-4-28 上午12:05:00
     */
    public void sendDataToSeriaPort(String message) {
        try {
            outputStream.write(message.getBytes());
            outputStream.flush();
            realTimeInfo.log("已经发送消息：" + message);
            System.out.println("向端口发送数据成功");
        }catch (IOException e) {
            System.out.println("向端口发送数据出错");
        }catch(NullPointerException e){
            AlertBox.display("端口","端口尚未打开");
        }

    }


    /** 初始化 */
    @Override
    public void initialize(URL location, ResourceBundle resources) {


        CBdateBit.getItems().addAll("5", "6","7","8");
        CBdateBit.setValue("8");

        CBparity.getItems().addAll("NONE", "ODD","EVEN");
        CBparity.setValue("NONE");


        CBbaudRate.getItems().addAll("2400", "4800","9600","115200","14400","19200");
        CBbaudRate.setValue("115200");


        CBstopBit.getItems().addAll("1", "2","1.5");
        CBstopBit.setValue("1");


    }

    /**
     * 关闭端口
     */
    public void CloseCom() {
        try {
            serialPort.close();
            realTimeInfo.log("端口<" + commPort.getName()+">已经关闭");
        }catch (Exception e){
            AlertBox.display("端口","端口尚未打开");
        }
    }
}
