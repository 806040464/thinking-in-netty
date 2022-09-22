package com.zhaoccf.demo.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
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
            //从客户端连接中获取输出流并发送消息
            OutputStream os = socket.getOutputStream();
            System.out.println("请输入：");
            Scanner scanner = new Scanner(System.in);
            String msg = scanner.nextLine();
            os.write(msg.getBytes(StandardCharsets.UTF_8));

            //从客户端连接中获取输入流，读取回复消息
            System.out.println("开始读回复消息");
            InputStream is = socket.getInputStream();
            byte[] b = new byte[20];
            is.read(b);
            System.out.println("那头说：" + new String(b).trim());
            Thread.sleep(10000);
            while (is.read(b) > 0) {
                System.out.println("那头又说：" + new String(b).trim());
            }
            socket.close();
        }
    }
}
