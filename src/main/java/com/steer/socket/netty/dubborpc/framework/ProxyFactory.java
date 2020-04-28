package com.steer.socket.netty.dubborpc.framework;

import com.steer.socket.netty.dubborpc.netty.NettyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProxyFactory {
    static private Logger log = LoggerFactory.getLogger(ProxyFactory.class);

    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static NettyClientHandler clientHandler;

    public static <T> T getProxy(Class serviceClass){
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class[]{serviceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (clientHandler == null){
                    //远程注册去取
//                    URL url = RemoteRegister.get(serviceClass.getName());
                    clientHandler = initClientHandler("127.0.0.1",8888);
                }
                Invocation invocation = new Invocation(serviceClass.getName(),method.getName(),method.getParameterTypes() ,args);
                clientHandler.setInvocation(invocation);
                //阻塞等待结果返回
                return executorService.submit(clientHandler).get();
            }
        });
    }



    public static NettyClientHandler initClientHandler(String host, int port) {
        try {
            clientHandler = new NettyClientHandler();
            NioEventLoopGroup group = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //添加POJO对象解码器 禁止缓存类加载器
                            ch.pipeline().addLast(new ObjectDecoder(1024, ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
                            //设置发送消息编码器
                            ch.pipeline().addLast(new ObjectEncoder());

                            pipeline.addLast(clientHandler);
                        }
                    });
            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.addListener((ChannelFuture f) -> {
                if (f.isSuccess()) {
                    log.info("连接{}:{}成功", host, port);
                } else {
                    log.info("连接{}:{}失败", host, port);
                }
            });
            return clientHandler;
//            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            group.shutdownGracefully();
        }
        return null;
    }

}
