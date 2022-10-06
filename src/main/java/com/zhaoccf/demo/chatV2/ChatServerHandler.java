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
 * @date 2022/10/6 20:38
 */
public class ChatServerHandler extends ChannelInboundHandlerAdapter {
    public static List<Channel> channels = new CopyOnWriteArrayList<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channels.add(channel);
        System.out.println(channel.remoteAddress().toString() + "上线了");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channels.remove(channel);
        System.out.println(channel.remoteAddress().toString() + "下线了");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(msg.toString());
        for (Channel channel1 : channels) {
            if (channel != channel1) {
                channel1.writeAndFlush(channel.remoteAddress().toString() + "说：" + msg.toString());
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
