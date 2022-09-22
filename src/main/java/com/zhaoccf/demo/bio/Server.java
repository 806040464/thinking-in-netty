package com.zhaoccf.demo.bio;

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
    public static void main(String[] args) throws Exception {
        start();
    }

    public static void start() throws IOException, InterruptedException {
        System.out.println("服务端启动...");
        System.out.println("初始化端口：9999");
        //创建serverSocket对象
        ServerSocket server = new ServerSocket(9999);

        while (true) {
            //监听客户端
            Socket socket = server.accept();
            //从客户端连接中获取输入流，接收信息
            InputStream is = socket.getInputStream();
            byte[] b = new byte[10];
            is.read(b);
            String ip = socket.getInetAddress().getHostAddress();
            System.out.println(ip + "说：" + new String(b).trim());

            //从客户端连接中获取输出流，写入回复信息
            OutputStream os = socket.getOutputStream();
            os.write("开始结巴".getBytes(StandardCharsets.UTF_8));
            Thread.sleep(10000);
            os.write("结束".getBytes(StandardCharsets.UTF_8));
            socket.close();
        }
    }
}
