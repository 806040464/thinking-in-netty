package com.zhaoccf.demo.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author zhaoccf
 * @version 1.0
 * @description
 * @date 2022/9/22 17:14
 */
public class Client {
    private final String HOST = "127.0.0.1";
    private final int PORT = 9999;
    private String userName;
    private SocketChannel socketChannel;

    public Client() {
        try {
            //开启网络通道
            this.socketChannel = SocketChannel.open();
            InetSocketAddress address = new InetSocketAddress(HOST, PORT);
            //设置非阻塞
            this.socketChannel.configureBlocking(false);
            //连接服务器
            if (!socketChannel.connect(address)) {
                //重试连接
                while (!socketChannel.finishConnect()) {
                    System.out.println("Client：还没连接上服务器，干点别的");
                }
            }
            this.userName = socketChannel.getLocalAddress().toString();
            System.out.println("Client" + userName + "is Ready!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 向服务器发送消息
     *
     * @param msg
     */
    public void sendMsg(String msg) {
        try {
            if ("bye".equalsIgnoreCase(msg)) {
                socketChannel.close();
            }
            msg = userName + "说：" + msg;
            ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8));
            socketChannel.write(byteBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从服务端接收消息
     */
    public void receiveMsg() {
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int read = socketChannel.read(byteBuffer);
            if (read > 0) {
                String msg = new String(byteBuffer.array()).trim();
                System.out.println(msg);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
