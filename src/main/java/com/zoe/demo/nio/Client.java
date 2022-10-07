package com.zoe.demo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author zhaoccf
 * @version 1.0
 * @description
 * @date 2022/9/22 15:14
 */
public class Client {
    public static void main(String[] args) throws Exception {
        start();
    }

    public static void start() throws IOException {
        //开启网络通道
        SocketChannel socketChannel = SocketChannel.open();
        //设置非阻塞
        socketChannel.configureBlocking(false);
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 9999);
        //连接服务器
        if (!socketChannel.connect(address)) {
            //重试连接
            while (!socketChannel.finishConnect()) {
                System.out.println("Client：金莲连不上，做点别的事");
            }
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap("你好，金莲，大郎在家吗？".getBytes(StandardCharsets.UTF_8));
        //发送消息
        socketChannel.write(byteBuffer);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
