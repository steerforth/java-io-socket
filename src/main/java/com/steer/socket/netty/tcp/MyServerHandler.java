package com.steer.socket.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class MyServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private Logger log = LoggerFactory.getLogger(MyServerHandler.class);
    private int count;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String msg = new String(bytes, CharsetUtil.UTF_8);
        log.info("从客户端{}读到数据:{}",ctx.channel().remoteAddress(),msg);

        log.info("服务端接收消息次数:{}",++count);

        //回复数据
        ctx.writeAndFlush(Unpooled.copiedBuffer(UUID.randomUUID().toString(),CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable t){
        t.printStackTrace();
        ctx.close();
    }
}
