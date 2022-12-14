package com.zoe.demo.chat;

import java.util.Scanner;

/**
 * @author zhaoccf
 * @version 1.0
 * @description
 * @date 2022/9/22 17:59
 */
public class ChatTest2 {
    public static void main(String[] args) {
        Client chatClient = new Client();

        new Thread(()->{
            while (true) {
                chatClient.receiveMsg();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        },"monitor-server").start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String msg = scanner.nextLine();
            chatClient.sendMsg(msg);
        }
    }
}
