package com.zhaoccf.demo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

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

    public static void start() throws IOException, InterruptedException {
        SocketChannel channel = null;
        ByteBuffer buffer = null;
        try {
            channel = SocketChannel.open();
            channel.configureBlocking(false);
            InetSocketAddress address = new InetSocketAddress("127.0.0.1", 9999);
            if (!channel.connect(address)) {
                while (!channel.finishConnect()) {
                    System.out.println("等待客户端连接期间，doSomethingElse()");
                }
            }
            String msg = "你好，金莲，大郎在家吗？";
            buffer = ByteBuffer.wrap(msg.getBytes());
            channel.write(buffer);
            Thread.sleep(10000);
        } finally {
            channel.close();
            buffer.clear();
        }
    }
}
