package com.sspu.service.cloud;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

import static com.sspu.service.cloud.NioClient.cin;
import static com.sspu.service.cloud.NioClient.doWrite;

/**
 * 发送数据 给服务端
 * @author ymj
 * @Date： 2020/4/21 21:14
 */
public class NioWrite implements Runnable{

    public static Scanner cin = new Scanner(System.in);

    private static SocketChannel socketChannel = null;

    public volatile boolean lock = true;
    public NioWrite(SocketChannel socketChannel){
        this.socketChannel = socketChannel;

    }
    /** 发送数据数据线程*/
    @Override
    public void run() {
        while (lock) {

            if (socketChannel.isConnected() == false){
                continue;
            }
            try {
                String message = cin.nextLine();
                doWrite(socketChannel, message);
            } catch (IOException e) {
                e.printStackTrace();
            }catch (IndexOutOfBoundsException e){

            }


        }
    }
}
