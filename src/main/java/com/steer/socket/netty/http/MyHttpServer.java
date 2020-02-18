package com.steer.socket.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyHttpServer {
    static Logger log = LoggerFactory.getLogger(MyHttpServer.class);

    public static void main(String[] args) {
        int port = 16668;

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new MyHttpServerInitializer());
            ChannelFuture future = bootstrap.bind(port).sync();
            future.addListener((ChannelFuture channelFuture)->{
                if (channelFuture.isSuccess()){
                    log.info("开启端口:{}成功",port);
                }else {
                    log.info("开启端口:{}失败",port);
                }
            });
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }
}
