package com.zhaoccf.demo.chatV2;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author zhaoccf
 * @version 1.0.0
 * @description
 * @date 2022/10/6 20:39
 */
public class ChatClientHandler extends ChannelInboundHandlerAdapter {
    public static List<Channel> channels = new CopyOnWriteArrayList<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(msg.toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
