package com.zhaoccf.demo.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author zhaoccf
 * @version 1.0
 * @description
 * @date 2022/9/22 14:40
 */
public class Client {
    public static void main(String[] args) throws Exception {
        start();
    }

    public static void start() throws IOException, InterruptedException {
        while (true) {
            //创建socket对象
            Socket socket = new Socket("127.0.0.1", 9999);
            //获取输出流发送消息
            OutputStream outputStream = socket.getOutputStream();
            Scanner scanner = new Scanner(System.in);
            String msg = scanner.nextLine();
            outputStream.write(msg.getBytes(StandardCharsets.UTF_8));
            //获取输入流接收消息
            InputStream inputStream = socket.getInputStream();//阻塞
            byte[] bytes = new byte[20];
            int read = inputStream.read(bytes);
            if (read > -1) {
                SocketAddress remoteSocketAddress = socket.getRemoteSocketAddress();
                System.out.println("老板" + remoteSocketAddress + "说：" + new String(bytes).trim());
            }
            socket.close();
        }
    }
}
