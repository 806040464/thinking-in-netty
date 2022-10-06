package com.zhaoccf.demo.codec;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

/**
 * @author zhaoccf
 * @version 1.0.0
 * @description
 * @date 2022/10/6 21:26
 */
public class NettyCodecClient {
    public static void main(String[] args) throws InterruptedException {
        //创建线程组
        NioEventLoopGroup group = new NioEventLoopGroup();
        //创建客户端启动助手
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("encoder", new ProtobufEncoder());
                        ch.pipeline().addLast(new NettyCodecClientHandler());
                    }
                });
        System.out.println("客户端启动完成");
        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9999).sync();
        channelFuture.channel().closeFuture().sync();
    }
}
