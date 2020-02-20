package com.steer.socket.netty.dubborpc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyClient {
    static private Logger log = LoggerFactory.getLogger(NettyClient.class);

    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static NettyClientHandler clientHandler;

    public Object getBean(final Class<?> serviceClass,final String providerName){
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),new Class<?>[]{serviceClass},(proxy, method, args) -> {
            log.info(" method:{} args:{}",method,args);
            if (clientHandler == null){
                initClient("127.0.0.1",8888);
            }
            clientHandler.setParam(providerName+args[0]);
            //阻塞等待结果返回
            return executorService.submit(clientHandler).get();
        });
    }

    private static void initClient(String host,int port){
        clientHandler = new NettyClientHandler();
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(clientHandler);
                    }
                });

        try {
            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.addListener((ChannelFuture f)->{
                if (f.isSuccess()){
                    log.info("连接{}:{}成功",host,port);
                }else {
                    log.info("连接{}:{}失败",host,port);
                }
            });
//            future.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
//            group.shutdownGracefully();
        }
    }
}
