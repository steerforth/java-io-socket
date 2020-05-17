package com.steer.socket.netty.snmp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class SnmpClient {
    private Logger log = LoggerFactory.getLogger(SnmpServer.class);

    private int port;

    private Channel channel;

    private NioEventLoopGroup workerGroup;

    public SnmpClient(int port){
        this.port = port;
    }

    public void start() throws InterruptedException {
        workerGroup = new NioEventLoopGroup();
        try {

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioDatagramChannel.class)
                    .handler(new ChannelInitializer<NioDatagramChannel>() {
                        @Override
                        protected void initChannel(NioDatagramChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new SnmpClientHandler());
                        }
                    })
                    .option(ChannelOption.SO_BROADCAST,true)//支持广播
                    .option(ChannelOption.SO_RCVBUF,2048*1024)//UDP读缓冲区
                    .option(ChannelOption.SO_SNDBUF,1024*1024)//UDP写缓冲区
            ;

            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            channelFuture.addListener((cf)->{
                if (cf.isSuccess()){
                    log.info("客户端开启端口:{}成功",port);
                }else{
                    log.warn("客户端开启端口:{}失败",port);
                }
            });
            this.channel = channelFuture.channel();
//            channelFuture.channel().closeFuture().sync();
        }finally {
//            workerGroup.shutdownGracefully();
        }

    }

    public Channel getChannel() {
        return channel;
    }

    public void close(){
        workerGroup.shutdownGracefully();
    }

    public static void main(String[] args) throws InterruptedException {
        SnmpClient client = new SnmpClient(11111);
        client.start();

        String host = "127.0.0.1";
        Integer port = 10001;

        ByteBuf res = Unpooled.buffer(3).writeBytes(new byte[]{0x01,0x01,0x01});
        client.getChannel().writeAndFlush(new DatagramPacket(res,new InetSocketAddress(host,port)));

        Thread.sleep(5000);

        client.close();
    }
}
