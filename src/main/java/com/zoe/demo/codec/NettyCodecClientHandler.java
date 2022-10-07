package com.zoe.demo.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author zhaoccf
 * @version 1.0.0
 * @description
 * @date 2022/10/6 21:27
 */
public class NettyCodecClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        UserProto.User user = UserProto.User.newBuilder().setId(1).setName("zhaoccf").setPassword("123456").build();
        ctx.writeAndFlush(user);
    }
}
