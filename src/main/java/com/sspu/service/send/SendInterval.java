package com.sspu.service.send;

import com.sspu.controller.port.PortListener;

import java.io.IOException;

/**
 * 定时 向 下位机 发送 数据回传 命令
 * 类型： 守护线程
 * @author ymj
 * @Date： 2019/12/25 0:20
 */
public class SendInterval implements Runnable{

    /** 定时 间隔 ： S*/
    public static int interval = 1;

    private static Object lock = new Object();

    private static volatile boolean pause = false;

    private static volatile boolean stop = false;
    /**
     * 调用该方法实现线程的暂停
     */
    public static void pauseThread(){
        pause = true;
    }

    /**
    调用该方法实现恢复线程的运行
     */
    public static void resumeThread(){

        pause =false;
        synchronized (lock){
            lock.notify();
        }
    }

    /**
     调用该方法实现恢复线程的运行
     */
    public static void stopThread(){
        stop =true;
    }

    /**
     * 这个方法只能在run 方法中实现，不然会阻塞主线程，导致页面无响应
     */
    public static void onPause() {
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 定时 向 下位机 发送 数据回传命令
     */
    @Override
    public void run() {
        while (!stop) {
            while (pause){
                onPause();
            }
            try {
                Thread.sleep(interval*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String message = "09 02 21 12 15 01 00 6f 04";

            try {
                PortListener.outputStream.write(stringTransByte(message));
                PortListener.outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("向下位机 发送命令完成");
        }
    }

    /** 16 进制字符串数组 转为 字节数组 */
    public static byte[] stringTransByte(String message){

        String hexString = message;
        hexString = hexString.replaceAll(" ", "");
        int len = hexString.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节
            bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character
                    .digit(hexString.charAt(i+1), 16));
        }
        return bytes;
    }
}
