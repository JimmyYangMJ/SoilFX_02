package com.sspu.service.cloud;

import com.sspu.controller.AlertBox;
import com.sspu.controller.Channel;
import com.sspu.controller.realTime.RealTimeInfo;
import com.sun.javafx.application.PlatformImpl;
import javafx.application.Platform;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import static com.sspu.SQL.MySql_operate.GetValueByKey;

public class NioClient implements Runnable{

	public static Scanner cin = new Scanner(System.in);

	public static int ID = 1;


	public static SelectionKey key = null;
	public static Selector selector = null;

	private static int sendCount = 0;

	/** 提示信息类 （输出到提示框）*/
	public static RealTimeInfo realTimeInfo;

	@Override
	public void run() {

		/** 获取 另一个视图 容器的实例*/
		realTimeInfo = (RealTimeInfo) Channel.controllers.get(RealTimeInfo.class.getSimpleName());

		//String host = "192.168.179.182";
		String host = "127.0.0.1";
//		String host = "47.96.130.110";
		int port = 8001;

		System.out.print("输入客户端编号：" );
//		ID = Integer.parseInt(cin.nextLine());
		ID = Integer.parseInt(GetValueByKey("jdbcInfo.properties", "client-id"));

		System.out.println(ID);

		selector = null;
		SocketChannel socketChannel = null;
		try {
			selector = Selector.open(); // 开选择器
			socketChannel = SocketChannel.open(); // 开通道
			socketChannel.configureBlocking(false); // 设置非阻塞

			// 如果直接连接成功，则注册到多路复用器上，发送请求消息，读应答
			if (socketChannel.connect(new InetSocketAddress(host, port))) {
				System.out.println("服务器连接成功");
				System.out.println("reda 读取");
				System.out.println(socketChannel.register(selector, SelectionKey.OP_READ));
				/** 发送 确认 */
				doWrite(socketChannel, "<accept>");
			} else { // 没有建立连接
				socketChannel.register(selector, SelectionKey.OP_CONNECT); // 选择器和通道 注册绑定
				System.out.println("选择器注册成功， connect 连接");
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		NioWrite nioWrite = new NioWrite(socketChannel);
		Thread nioWriteThread = new Thread(nioWrite, "NioWrite");
		nioWriteThread.setDaemon(true);
		nioWriteThread.start();


		tip:
		while (true) {
			try {
				selector.select(1000);  // 遍历选择器（中的通道）
				Set<SelectionKey> selectedKeys = selector.selectedKeys(); //所有连接上的选择器 key
				Iterator<SelectionKey> it = selectedKeys.iterator();
				key = null;
				while (it.hasNext()) { // 顺序处理 选择器中 有反应的通道
					key = it.next();
					it.remove();
					try {
						// 处理每一个channel
						handleInput(selector, key);
					}catch (ConnectException e) {
						System.out.println("连接异常");
						if (key != null) {
							key.cancel();
							if (key.channel() != null)
								key.channel().close();
						}
						break tip;
					}
				}
			} catch (Exception e) {
//				e.printStackTrace();
				if (e.toString().equals("java.io.IOException: 远程主机强迫关闭了一个现有的连接。")){
					System.out.println("云端出错");
					Platform.runLater(()->{
						realTimeInfo.log("云端出错:");
						AlertBox.display("云端出错","远程主机强迫关闭了一个现有的连接");
					});
					break ;
				}
			}
		}

		Platform.runLater(()->{
			realTimeInfo.log("云端连接出错, 请重新连接:");
		});
		System.out.println("请重新连接");
		nioWrite.lock = false;
	}

	/**
	 * 写数据
	 * @param sc
	 * @param message 格式：[id]：[message]
	 * @return -1: 通道连接断开， 1；数据已发送， -2：没有要发送的数据
	 * @throws IOException
	 */
	public static int doWrite(SocketChannel sc, String message) throws IOException {

		message = Integer.toString(ID) + ":" + message;

		byte[] str = message.getBytes();
		ByteBuffer writeBuffer = ByteBuffer.allocate(str.length);
		writeBuffer.put(str);
		writeBuffer.flip();
		sc.write(writeBuffer); // 向通道发送数据

		return  1;
	}

	/**
	 * 读数据：处理通道
	 * @param selector
	 * @param key
	 * @throws Exception
	 */
	public static void handleInput(Selector selector, SelectionKey key) throws Exception {
		/**  判断是否连接成功*/
		if (key.isValid()) {
			// 获取key 相对应 的 通道Channel
			SocketChannel sc = (SocketChannel) key.channel();
			if (key.isConnectable()) { // 判断 key 和 selector 是否已经注册

				if (sc.finishConnect()) {
					sc.register(selector, SelectionKey.OP_READ); // 可以读数据
					/** 第一次发送数据 */
					doWrite(sc, "<request>");
					System.out.println("连接成功");
				}
			}

			if (key.isReadable()) {
				ByteBuffer readBuffer = ByteBuffer.allocate(1024);
				int readBytes = sc.read(readBuffer);
				if (readBytes > 0) {
					readBuffer.flip();
					byte[] bytes = new byte[readBuffer.remaining()];
					readBuffer.get(bytes);
					String body = new String(bytes, "UTF-8");

					// Todo 数据处理
					if (message(body) == 3){
						/** 返回确认数据 */
						doWrite(sc,"<accept>");
					}

				} else if (readBytes < 0) {
					// 对端链路关闭
					key.cancel();
					sc.close();
				} else
					; // 读到0字节，忽略
			}
			//Thread.sleep(3000);
		}
	}


	/**
	 * 处理接受的数据
	 * @param message
	 * @return 1：发送包的确认, 2 心跳包, 3 数据包
	 */
	public static int message(String message){
		if(message.trim().equals("<accept>")){
			System.out.println("发送成功");
			return 1;
		}else if(message.trim().equals("<tick>")){
			return 2;
		}else{
			System.out.println("Server said 6: " + message);
			return 3;
		}
	}

}