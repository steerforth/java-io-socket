package com.steer.socket.netty.inAndoutbound;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
    Logger log = LoggerFactory.getLogger(MyClientHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        //向服务器发送数据
        long o = 111111L;
        ctx.writeAndFlush(o);

//        String o = "abcdabcdabcdabca";
//        ctx.writeAndFlush(Unpooled.copiedBuffer(o, CharsetUtil.UTF_8));

        log.info("通道激活，并向服务器发送数据:{}",o);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long aLong) throws Exception {
        log.info("从服务器{}读到数据:{}",ctx.channel().remoteAddress(),aLong);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable t){
        t.printStackTrace();
        ctx.close();
    }
}
