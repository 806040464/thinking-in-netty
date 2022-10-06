package com.zhaoccf.demo.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author zhaoccf
 * @version 1.0.0
 * @description
 * @date 2022/10/6 18:06
 */
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        //创建线程组，处理客户端连接
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        //创建线程组，处理业务操作
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        //创建服务端启动助手配置参数
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                //使用NioServerSocketChannel作为服务器端通道的实现
                .channel(NioServerSocketChannel.class)
                //设置线程队列中等待连接的个数
                .option(ChannelOption.SO_BACKLOG, 128)
                //保持活动连接状态
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                //创建初始化通道
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                        //Pipeline链中添加自定义的handler类
                        channel.pipeline().addLast(new NettyServerHandler());
                    }
                });
        System.out.println("服务端启动中 init port：9999");
        //绑定端口 bind方法是异步的
        //sync方法是同步阻塞的
        ChannelFuture channelFuture = serverBootstrap.bind(9999).sync();
        System.out.println("服务端启动成功");
        //关闭通道，关闭线程组
        channelFuture.channel().closeFuture().sync();
    }
}
