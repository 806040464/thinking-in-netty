package com.zhaoccf.demo.chat;

import java.util.Scanner;

/**
 * @author zhaoccf
 * @version 1.0
 * @description
 * @date 2022/9/22 17:59
 */
public class ChatTest {
    public static void main(String[] args) {
        Client client = new Client();
        new Thread(() -> {
            while (true) {
                try {
                    client.receiveMsg();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String msg = scanner.nextLine();
            client.sendMsg(msg);
        }
    }
}
