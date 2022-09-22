package com.zhaoccf.demo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @author zhaoccf
 * @version 1.0
 * @description
 * @date 2022/9/22 15:14
 */
public class Server {
    public static void main(String[] args) throws IOException {
        start();
    }

    public static void start() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        Selector selector = Selector.open();
        System.out.println("服务器端启动...");
        System.out.println("初始化端口：9999");
        serverSocketChannel.bind(new InetSocketAddress(9999));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            if (selector.select(2000) == 0) {
                System.out.println("Server：非阻塞获取客户端连接，doSomethingElse()");
                continue;
            }
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {
                    System.out.println("OP_ACCEPT");
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
                    channel.read(byteBuffer);
                    System.out.println("客户端发来数据：" + new String(byteBuffer.array()));
                    // TODO: 2022/9/22 为何关闭后不断循环发送数据，怀疑客户端关闭后key移除后重新获取selectKey，不断走服务端连接注册OP_READ逻辑
                }
                iterator.remove();
            }
        }
    }
}
