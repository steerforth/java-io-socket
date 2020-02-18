package com.steer.socket.netty.tcpprotocol;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyClient {
    private Logger log = LoggerFactory.getLogger(MyClient.class);

    private String host;
    private int port;

    public MyClient(String host,int port){
        this.host = host;
        this.port = port;
    }

    public void run(){
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new MyMessageEncoder());
                            pipeline.addLast("clientHandler",new MyClientHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            channelFuture.addListener((cf)->{
                if (cf.isSuccess()){
                    log.info("客户端连接{}:{}成功",host,port);
                }else{
                    log.warn("客户端连接{}:{}成功",host,port);
                }
            });
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }

    }
    public static void main(String[] args) {
        new MyClient("127.0.0.1",7000).run();
    }
}
