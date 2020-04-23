//package com.sspu.service.cloud;
//
//import java.io.IOException;
//import java.net.InetSocketAddress;
//import java.nio.ByteBuffer;
//import java.nio.channels.SelectionKey;
//import java.nio.channels.Selector;
//import java.nio.channels.SocketChannel;
//import java.util.Iterator;
//import java.util.Set;
//import java.util.UUID;
//
///**
// * NIO
// * Client 客户端
// * @author ymj
// * @Date： 2020/4/17 20:35
// */
//public class CloudNIO implements Runnable{
//
//    @Override
//    public void run() {
//
//        System.out.println("尝试云端连接");
//        //String host = "192.168.179.182";
//        String host = "127.0.0.1";
//        int port = 8001;
//
//        Selector selector = null;
//        SocketChannel socketChannel = null;
//
//        try {
//            selector = Selector.open();
//            socketChannel = SocketChannel.open();
//            socketChannel.configureBlocking(false); // 非阻塞
//
//            // 如果直接连接成功，则注册到多路复用器上，发送请求消息，读应答
//            if (socketChannel.connect(new InetSocketAddress(host, port)))
//            {
//                socketChannel.register(selector, SelectionKey.OP_READ);
//                /** 开始写数据 */
//                doWrite(socketChannel);
//            }
//            else
//            {
//                socketChannel.register(selector, SelectionKey.OP_CONNECT);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.exit(1);
//        }
//
//        while (true) {
//            try
//            {
//                selector.select(1000);
//                Set<SelectionKey> selectedKeys = selector.selectedKeys();
//                Iterator<SelectionKey> it = selectedKeys.iterator();
//                SelectionKey key = null;
//                while (it.hasNext())
//                {
//                    key = it.next();
//                    it.remove();
//                    try
//                    {
//                        //处理每一个channel
//                        handleInput(selector, key);
//                    }
//                    catch (Exception e) {
//                        if (key != null) {
//                            key.cancel();
//                            if (key.channel() != null)
//                                key.channel().close();
//                        }
//                    }
//                }
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * 写数据
//     * @param sc
//     * @throws IOException
//     */
//    public static void doWrite(SocketChannel sc) throws IOException {
//        byte[] str = UUID.randomUUID().toString().getBytes();
//        ByteBuffer writeBuffer = ByteBuffer.allocate(str.length);
//        writeBuffer.put(str);
//        writeBuffer.flip();
//        sc.write(writeBuffer);
//    }
//
//    /**
//     * 读数据
//     * @param selector
//     * @param key
//     * @throws Exception
//     */
//    public static void handleInput(Selector selector, SelectionKey key) throws Exception {
//
//        if (key.isValid()) {
//            // 判断是否连接成功
//            SocketChannel sc = (SocketChannel) key.channel();
//            if (key.isConnectable()) {
//                if (sc.finishConnect()) {
//                    sc.register(selector, SelectionKey.OP_READ);
//                }
//            }
//            if (key.isReadable()) {
//                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
//                int readBytes = sc.read(readBuffer);
//                if (readBytes > 0) {
//                    readBuffer.flip();
//                    byte[] bytes = new byte[readBuffer.remaining()];
//                    readBuffer.get(bytes);
//                    String body = new String(bytes, "UTF-8");
//                    System.out.println("Server said : " + body);
//                } else if (readBytes < 0) {
//                    // 对端链路关闭
//                    key.cancel();
//                    sc.close();
//                } else
//                    ; // 读到0字节，忽略
//            }
//            Thread.sleep(3000);
//            doWrite(sc);
//        }
//    }
//}
