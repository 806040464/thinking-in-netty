package com.zhaoccf.demo.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

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
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            InetSocketAddress address = new InetSocketAddress(HOST, PORT);
            if (!socketChannel.connect(address)) {
                while (!socketChannel.finishConnect()) {
                    System.out.println("等待客户端连接期间，doSomethingElse()");
                }
            }
            userName = socketChannel.getLocalAddress().toString();
            System.out.println("Client" + userName + "is ready!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        try {
            if ("bye".equalsIgnoreCase(msg)) {
                socketChannel.close();
                return;
            }
            msg = userName + "说：" + msg;
            ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
            socketChannel.write(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveMsg() {
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int len;
            if ((len = socketChannel.read(byteBuffer)) > 0) {
                String msg = new String(byteBuffer.array()).trim();
                System.out.println(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
