package com.sspu.service.cloud;

import com.sspu.controller.AlertBox;
import com.sspu.controller.Channel;
import com.sspu.controller.CloudView;
import com.sspu.controller.realTime.RealTimeInfo;
import com.sspu.pojo.DataAD;
import javafx.application.Platform;
import jdk.internal.org.objectweb.asm.TypeReference;

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

import static com.sspu.constants.Config.GetValueByKey;

/**
 * NIO 通信入口
 */
public class NioClient implements Runnable{

	public static Scanner cin = new Scanner(System.in);

	public static boolean cloudStatus = false;

	public static int ID = Integer.parseInt(GetValueByKey("jdbcInfo.properties", "client-id"));;
	private static int port = Integer.parseInt(GetValueByKey("jdbcInfo.properties", "server-port"));
	private static String host =  GetValueByKey("jdbcInfo.properties", "server-ip");

	public static SelectionKey key = null;
	public static Selector selector = null;

	/** 提示信息类 （输出到提示框）*/
	public static RealTimeInfo realTimeInfo;

	/** 云端状态显示*/
	public static CloudView cloudView;

	/**
	 * 接收 服务器指令
	 */
	@Override
	public void run() {

		/** 获取 另一个视图(输出信息框)容器的实例*/
		realTimeInfo = (RealTimeInfo) Channel.controllers.get(RealTimeInfo.class.getSimpleName());
		cloudView = (CloudView) Channel.controllers.get(CloudView.class.getSimpleName());

		System.out.println(ID);

		SocketChannel socketChannel = null;
		try {
			selector = Selector.open(); // 开选择器
			socketChannel = SocketChannel.open(); // 开通道
			socketChannel.configureBlocking(false); // 设置非阻塞

			// 如果直接连接成功，则注册到多路复用器上，发送请求消息，读应答
			if (socketChannel.connect(new InetSocketAddress(host, port))) {
				System.out.println("服务器连接成功");
				System.out.println(socketChannel.register(selector, SelectionKey.OP_READ));
				/** 发送 确认 */
				doWrite(socketChannel, "accept");
			} else { // 没有建立连接
				socketChannel.register(selector, SelectionKey.OP_CONNECT); // 选择器和通道 注册绑定
				System.out.println("选择器注册成功， connect 连接");
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		/** 开启 向服务器发送消息线程 */
		NioWrite nioWrite = new NioWrite(socketChannel);
		Thread nioWriteThread = new Thread(nioWrite, "NioWrite");
		nioWriteThread.setDaemon(true);
		nioWriteThread.start();

		cloudStatus = true;

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
						/** 处理每一个channel */
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
						cloudView = (CloudView) Channel.controllers.get(CloudView.class.getSimpleName());
						cloudView.status.setText("离线");
						cloudView.closeSyn.setSelected(true);
					});
					break ;
				}
			}
		}

		/** 提示框 提示 云端连接出错 */
		Platform.runLater(()->{
			cloudView = (CloudView) Channel.controllers.get(CloudView.class.getSimpleName());
			cloudView.status.setText("离线");
			cloudView.closeSyn.setSelected(true);
			AlertBox.display("云端出错","云端连接失败");
		});

		nioWrite.lock = false;
	}

	/**
	 * 写数据
	 * @param sc
	 * @param message 格式：<1,%s,%d,>
	 * @return -1: 通道连接断开， 1；数据已发送， -2：没有要发送的数据
	 * @throws IOException
	 */
	public static int doWrite(SocketChannel sc, String message) throws IOException {

		message = String.format("<1,%s,%d,>", message, ID);

		byte[] str = message.getBytes();
		ByteBuffer writeBuffer = ByteBuffer.allocate(str.length);
		writeBuffer.put(str);
		writeBuffer.flip();
		sc.write(writeBuffer); // 向通道发送数据

		return  1;
	}

	/** 发送水势数据
	 * <[参数个数],[参数类型],[发送端],[AD],[AD_base],[水势值],[时间],>
	 * <6,1,1,1.2,1.5,6.4,2020-06-06 12:12:13,>
	 *     */
	public static int doWrite(SocketChannel sc, DataAD dataAD) throws IOException {

		String message = String.format("<6,1,%d,%.2f,%.2f,%.2f,%s,>",
				ID, dataAD.getAd(), dataAD.getAd_base(), dataAD.getHumidity(), dataAD.getTime());

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
					/** 第一次发送数据，连接请求 */
					doWrite(sc, "request");
					System.out.println("连接成功");
					Platform.runLater(()->{
						cloudView = (CloudView) Channel.controllers.get(CloudView.class.getSimpleName());
						cloudView.status.setText("在线");
						cloudView.realSyn.setSelected(true);
					});
				}
			}

			/**
			 * 读取服务器 数据
			 */
			if (key.isReadable()) {
				ByteBuffer readBuffer = ByteBuffer.allocate(1024);
				int readBytes = sc.read(readBuffer);
				if (readBytes > 0) {
					readBuffer.flip();
					byte[] bytes = new byte[readBuffer.remaining()];
					readBuffer.get(bytes);
					String MessageReceived = new String(bytes, "UTF-8");
					System.out.println(MessageReceived);

					// Todo 数据处理
					if (handleMessage(MessageReceived) == 3){
						/** 分析并执行指令 */
						Execution.handleInfo(MessageReceived);
						/** 返回确认数据 */
						doWrite(sc,"accept");
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
	public static int handleMessage(String message){

		String[] m = message.split(",");

		if(m[1].trim().equals("accept")){
			System.out.println("发送成功");
			return 1;
		}else if(m[1].trim().equals("tick")){
			return 2;
		}else{
			System.out.println("Server said : " + message);
			return 3;
		}


	}

}