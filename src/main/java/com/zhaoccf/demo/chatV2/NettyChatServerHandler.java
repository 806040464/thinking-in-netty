package com.zhaoccf.demo.chatV2;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author zhaoccf
 * @version 1.0.0
 * @description
 * @date 2022/10/6 21:16
 */
public class NettyChatServerHandler extends SimpleChannelInboundHandler<String> {
    public static List<Channel> channels = new CopyOnWriteArrayList<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(msg.toString());
        for (Channel channel1 : channels) {
            if (channel != channel1) {
                channel1.writeAndFlush(channel.remoteAddress().toString().substring(1) + "说：" + msg);
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channels.add(channel);
        System.out.println(channel.remoteAddress().toString().substring(1) + "上线了");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channels.remove(channel);
        System.out.println(channel.remoteAddress().toString().substring(1) + "下线了");
    }
}
