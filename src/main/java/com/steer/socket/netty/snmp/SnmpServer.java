package com.steer.socket.netty.snmp;

import com.steer.socket.netty.modbus.ModBusServer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SnmpServer {
    private Logger log = LoggerFactory.getLogger(ModBusServer.class);

    private int port;

    public SnmpServer(int port){
        this.port = port;
    }

    //处理客户端请求
    public void run() throws InterruptedException {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioDatagramChannel.class)
                    .handler(new ChannelInitializer<NioDatagramChannel>() {
                        @Override
                        protected void initChannel(NioDatagramChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new SnmpServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BROADCAST,true)//支持广播
                    .option(ChannelOption.SO_RCVBUF,2048*1024)//UDP读缓冲区
                    .option(ChannelOption.SO_SNDBUF,1024*1024)//UDP写缓冲区
                    ;

            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            channelFuture.addListener((cf)->{
                if (cf.isSuccess()){
                    log.info("服务器开启端口:{}成功",port);
                }else{
                    log.warn("服务器开启端口:{}失败",port);
                }
            });
            channelFuture.channel().closeFuture().sync();
        }finally {
            workerGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        new SnmpServer(10001).run();
    }
}
