package com.steer.socket.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @program: java-io-socket-master
 * @Author: Steerforth
 * @Description: netty服务端
 * @Date: Created At 2019-06-25 22:13
 * @Modified By：
 */
public class NettyServer {
    Logger log = LoggerFactory.getLogger(NettyServer.class);

    public void init(int port){
        //netty 通过ServerBootstrap启动服务端,启动引导类
        ServerBootstrap server = new ServerBootstrap();
        EventLoopGroup parentGroup = new NioEventLoopGroup(1);
        EventLoopGroup childGroup = new NioEventLoopGroup();
        //第1步，定义两个线程组，用来处理客户端通道的accept和读写事件
        /**
         * 1.parentGroup用来处理accept事件，childgroup用来处理通道的读写事件
         * 2.两个都是无限循环
         * 3.parentGroup和childGroup含有的子线程(NioEventLoop)的个数，默认为Cpu核数*2
         */
        //
        //parentGroup获取客户端连接，连接接收到之后再将连接转发给childgroup去处理
        server.group(parentGroup,childGroup);
        //用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度
        //用来初始化服务端可连接队列
        //服务端处理客户端连接请求是按顺序处理的，所以同一时间只能处理一个客户端连接，多个客户端来的时候，服务端将不能处理的客户端连接请求放在队列中等待处理，backlog参数指定了队列的大小
        server.option(ChannelOption.SO_BACKLOG,128);
        //第2步，绑定服务端通道实现
        server.channel(NioServerSocketChannel.class);

        //第3步绑定handler，处理读写事件，ChannelInitializer是给通道初始化
        server.childHandler(new ChannelInitializer<SocketChannel>() {
            //pipeline本质是一个双向链表，出栈入栈
            @Override
            protected void initChannel(SocketChannel socketChannel) {
                log.info("客户端 socketchannel hashcode:{}",socketChannel.hashCode());
                //解码器，接收的数据进行解码，一定要加在SimpleServerHandler 的上面
                //maxFrameLength表示这一贞最大的大小
                //delimiter表示分隔符，我们需要先将分割符写入到ByteBuf中，然后当做参数传入；
                //需要注意的是，netty并没有提供一个DelimiterBasedFrameDecoder对应的编码器实现(笔者没有找到)，因此在发送端需要自行编码添加分隔符，如 \r \n分隔符
//                socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()[0]));
//                socketChannel.pipeline().addLast(new SimplePreServerHandler());
                //把传过来的数据 转换成byteBuf
                socketChannel.pipeline().addLast(new SimpleServerHandler());
            }
        });


        try {
            //第4步绑定8080端口
            ChannelFuture future = server.bind(port).sync();

            //future-listener机制
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()){
                        log.info("服务器监听端口:{}成功",port);
                    }else{
                        log.warn("服务器监听端口:{}失败",port);
                    }
                }
            });
            //当通道关闭了，就继续往下走
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
//            try {
//                parentGroup.shutdownGracefully();
//                childGroup.shutdownGracefully();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }

    public static void main(String[] args) {
        NettyServer server = new NettyServer();
        server.init(8080);
    }
}
