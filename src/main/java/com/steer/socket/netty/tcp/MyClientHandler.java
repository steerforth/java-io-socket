package com.steer.socket.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    Logger log = LoggerFactory.getLogger(MyClientHandler.class);
    private int count;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 30; i++) {
           ByteBuf byteBuf =  Unpooled.copiedBuffer("hello,server"+i+"  ", CharsetUtil.UTF_8);
           ctx.writeAndFlush(byteBuf);
           Thread.sleep(3);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String msg = new String(bytes, CharsetUtil.UTF_8);
        log.info("从服务器{}读到数据:{}",ctx.channel().remoteAddress(),msg);
        log.info("客户端接收消息次数:{}",++count);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable t){
        t.printStackTrace();
        ctx.close();
    }
}
