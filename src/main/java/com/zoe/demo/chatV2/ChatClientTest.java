package com.zoe.demo.chatV2;

/**
 * @author zhaoccf
 * @version 1.0.0
 * @description
 * @date 2022/10/6 21:10
 */
public class ChatClientTest {
    public static void main(String[] args) throws InterruptedException {
        ChatNettyClient chatNettyClient = new ChatNettyClient("127.0.0.1", 9999);
        chatNettyClient.run();
    }
}
