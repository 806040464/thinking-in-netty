package com.zoe.demo.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author zhaoccf
 * @version 1.0
 * @description
 * @date 2022/9/22 14:40
 */
public class Server {
    public static void main(String[] args) throws IOException {
        start();
    }

    public static void start() throws IOException {
        System.out.println("服务端启动");
        System.out.println("服务端初始化端口：9999");
        ServerSocket serverSocket = new ServerSocket(9999);
        while (true) {
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();//阻塞
            byte[] bytes = new byte[20];
            int read = inputStream.read(bytes);
            if (read > -1) {
                String hostAddress = socket.getInetAddress().getHostAddress();
                System.out.println(hostAddress + "说：" + new String(bytes).trim());
            }
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write("没钱".getBytes(StandardCharsets.UTF_8));
            socket.close();
        }
    }
}
