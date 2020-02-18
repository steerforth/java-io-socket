package com.steer.socket.netty.inAndoutbound;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyServerHandler extends SimpleChannelInboundHandler<Long> {
    private Logger log = LoggerFactory.getLogger(MyServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long aLong) throws Exception {
        log.info("从客户端{}读到数据:{}",ctx.channel().remoteAddress(),aLong);

        long data = 1234567L;
        ctx.writeAndFlush(data);
        log.info("服务端向客户端写数据:{}",data);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable t){
        t.printStackTrace();
        ctx.close();
    }
}
