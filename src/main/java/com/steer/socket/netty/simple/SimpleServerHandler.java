package com.steer.socket.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @program: java-io-socket-master
 * @Author: Steerforth
 * @Description: 处理客户端返回数据
 * @Date: Created At 2019-06-25 22:38
 * @Modified By：
 */
public class SimpleServerHandler extends ChannelInboundHandlerAdapter {
    Logger log = LoggerFactory.getLogger(SimpleServerHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("服务端SimpleServerHandler通道就绪:{}",ctx);
    }
    /**
     * 一个pipeline可以管理多个ChannelHandlerContext，是一个双向链表的数据结构
     * 一个ChannelHandlerContext对应一个ChannelHandler处理器
     * ChannelHandlerContext保存channel相关的所有上下文信息，含有管道pipeline,通道channel,地址
     * 读取客户端通道的数据
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //可以在这里面写一套类似SpringMVC的框架
        //让SimpleServerHandler不跟任何业务有关，可以封装一套框架
        if (msg instanceof ByteBuf) {
            log.info("客户端地址:{},信息:{}", ctx.channel().remoteAddress(), ((ByteBuf) msg).toString(CharsetUtil.UTF_8));
        }

        //耗时，异步执行->提交该channel对应的NIOEventLoop的taskQueue中
        ctx.channel().eventLoop().execute(()->{
            log.info("我是耗时任务");
        });

        //scheduledTaskQueue中
        ctx.channel().eventLoop().schedule(()->{
            log.info("我是耗时任务");
        },2,TimeUnit.SECONDS);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
        //业务逻辑代码处理框架。。。

        //返回给客户端的数据，告诉我已经读到你的数据了
        String result = "hello client ";
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(result.getBytes());


        ctx.channel().writeAndFlush(buf);
//        ctx.channel().writeAndFlush(Unpooled.copiedBuffer("hello client",CharsetUtil.UTF_8));

//        ByteBuf buf2 = Unpooled.buffer();
//        buf2.writeBytes("\r\n".getBytes());
//        ctx.channel().writeAndFlush(buf2);
        log.info("=====server handler end=====");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        log.error("遇到异常，关闭context");
    }

}
