package com.zhaoccf.demo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
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
        System.out.println("服务端启动");
        System.out.println("服务端初始化端口：9999");
        //开启socket通道监听
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //开启选择器
        Selector selector = Selector.open();
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        //绑定端口
        serverSocketChannel.bind(new InetSocketAddress(9999));
        //注册到选择器，选择器监听ACCEPT事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            if (0 == selector.select(1000)) {
                System.out.println("Server:西门庆没找我，我去找王妈妈做点兼职");
                continue;
            }

            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {
                    SocketChannel clientSocketChannel = serverSocketChannel.accept();
                    clientSocketChannel.configureBlocking(false);
                    clientSocketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
                    System.out.println("客户端发来数据：" + new String(byteBuffer.array()).trim());
                    channel.read(byteBuffer);
                    System.out.println("客户端发来数据：" + new String(byteBuffer.array()).trim());
                }
                //将当前key移除，防止重复处理
                iterator.remove();
            }
        }

    }
}
