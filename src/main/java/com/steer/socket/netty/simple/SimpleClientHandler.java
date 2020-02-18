package com.steer.socket.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @program: java-io-socket-master
 * @Author: Steerforth
 * @Description:
 * @Date: Created At 2019-06-25 22:53
 * @Modified By：
 */
public class SimpleClientHandler extends ChannelInboundHandlerAdapter {
    Logger log = LoggerFactory.getLogger(SimpleClientHandler.class);

    /**
     * 通道就绪
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端通道就绪:{}",ctx);

        ctx.channel().writeAndFlush(Unpooled.copiedBuffer("你好，我是客户端",CharsetUtil.UTF_8));
    }


    /**
     * 有读取时间时触发
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            String value = ((ByteBuf) msg).toString(CharsetUtil.UTF_8);
            log.info("服务器端地址:{},返回的数据:{}",ctx.channel().remoteAddress(),value);
        }

//        AttributeKey<String> key = AttributeKey.valueOf("ServerData");
//        ctx.channel().attr(key).set("客户端处理完毕");

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        log.error("遇到异常，关闭context");
    }
}
