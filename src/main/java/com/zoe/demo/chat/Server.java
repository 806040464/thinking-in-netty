package com.zoe.demo.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
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
    private static final int PORT = 9999;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public Server() {
        try {
            System.out.println("服务端启动");
            System.out.println("服务端初始化端口：9999");
            //开启socket监听通道
            this.serverSocketChannel = ServerSocketChannel.open();
            //开启选择器
            this.selector = Selector.open();
            //设置非阻塞
            this.serverSocketChannel.configureBlocking(false);
            //绑定端口
            this.serverSocketChannel.bind(new InetSocketAddress(PORT));
            //注册选择器，选择器监听ACCEPT事件
            this.serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        try {
            while (true) {
                if (0 == selector.select(1000)) {
                    System.out.println("没有客户端连接，我去搞点兼职");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    continue;
                }
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                if (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isAcceptable()) {
                        SocketChannel clientSocketChannel = serverSocketChannel.accept();
                        clientSocketChannel.configureBlocking(false);
                        clientSocketChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println(clientSocketChannel.getRemoteAddress().toString() + "上线了");
                    }
                    if (key.isReadable()) {
                        readMsg(key);
                    }
                    //将当前key移除，防止重复处理
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 读取客户端发来的消息并广播
     *
     * @param key
     * @throws IOException
     */
    private void readMsg(SelectionKey key) throws IOException {
        SocketChannel clientSocketChannel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int read = clientSocketChannel.read(byteBuffer);
        if (read > 0) {
            String msg = new String(byteBuffer.array()).trim();
            //打印消息
            printInfo(msg);
            //全员广播消息，除发送客户端外
            broadcast(clientSocketChannel, msg);
        }
    }

    private void broadcast(SocketChannel clientSocketChannel, String msg) throws IOException {
        System.out.println("服务器广播消息");
        for (SelectionKey selectionKey : selector.keys()) {
            SelectableChannel targetChannel = selectionKey.channel();
            if (targetChannel instanceof SocketChannel && targetChannel != clientSocketChannel) {
                SocketChannel destChannel = (SocketChannel) targetChannel;
                ByteBuffer wrap = ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8));
                destChannel.write(wrap);
            }
        }
    }

    private void printInfo(String msg) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("[" + sdf.format(new Date()) + "]->" + msg);
    }
}
