package com.zhaoccf.demo.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * @author zhaoccf
 * @version 1.0
 * @description
 * @date 2022/9/22 17:13
 */
public class Server {
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public Server() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            selector = Selector.open();

            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(9999));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            printInfo("聊天室启动......");
            printInfo("聊天室初始化端口 9999");
            printInfo("聊天室初始化网络ip " + serverSocketChannel.getLocalAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            while (true) {
                if (selector.select(2000) == 0) {
                    printInfo("Server：非阻塞获取客户端连接，doSomethingElse()");
                    continue;
                }
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    //连接请求事件
                    if (key.isAcceptable()) {
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        //与客户端建立连接
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        printInfo(socketChannel.getRemoteAddress().toString() + "上线了");
                    }
                    if (key.isReadable()) {
                        //读取数据
                        readMsg(key);
                    }
                    //将key删掉，防止重复处理，但是外层还有循环，当客户端下线后还是会重复处理key
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void readMsg(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int len;
        if ((len = channel.read(byteBuffer)) > 0) {
            String msg = new String(byteBuffer.array());
            printInfo(msg);
            broadcast(channel, msg);
        }
    }

    private void broadcast(SocketChannel channel, String msg) throws IOException {
        printInfo("服务器广播了消息");
        for (SelectionKey selectionKey : selector.keys()) {
            Channel targetChannel = selectionKey.channel();
            if (targetChannel instanceof SocketChannel && !targetChannel.equals(channel)) {
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                ((SocketChannel) targetChannel).write(buffer);
            }
        }
    }

    private void printInfo(String str) { //往控制台打印消息
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("[" + sdf.format(new Date()) + "] -> " + str);
    }

}
