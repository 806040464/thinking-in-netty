package com.zoe.demo.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author zhaoccf
 * @version 1.0.0
 * @description
 * @date 2022/10/6 21:28
 */
public class NettyCodecServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        UserProto.User user = (UserProto.User) msg;
        System.out.println("客户端 msg:" + user.getId() + "," + user.getName() + "," + user.getPassword());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
