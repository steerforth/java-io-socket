package com.steer.socket.netty.groupchat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class GroupChatServer {
    private Logger log = LoggerFactory.getLogger(GroupChatServer.class);

    private int port;

    public GroupChatServer(int port){
        this.port = port;
    }

    //处理客户端请求
    public void run() throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast("decoder",new StringDecoder());
                            pipeline.addLast("encoder",new StringEncoder());
                            /**
                             * 说明:
                             * 1.IdleStateHandler是netty提供的处理空闲状态的处理器
                             * 2.readerIdleTime:表示多少时间没有读，就会发送一个心跳检测包检测是否连接
                             * 3.writerIdleTime:表示多少时间没有写
                             * 4.allldleTime:表示多少时间没有读写
                             *
                             * 当IdleStateEvent触发后,就会传递给管道的下一个handler去处理
                             * 通过调用下一个handler的userEventTriggered方法去处理IdleStateEvent
                             */
                            pipeline.addLast("idleState",new IdleStateHandler(3,5,7,TimeUnit.SECONDS));
                            pipeline.addLast("serverHandler",new GroupChatServerHandler());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            channelFuture.addListener((cf)->{
                if (cf.isSuccess()){
                    log.info("群聊服务器开启端口:{}成功",port);
                }else{
                    log.warn("群聊服务器开启端口:{}失败",port);
                }
            });
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        new GroupChatServer(7000).run();
    }
}
