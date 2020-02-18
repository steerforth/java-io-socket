package com.steer.socket.netty.tcpprotocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    private Logger log = LoggerFactory.getLogger(MyServerHandler.class);

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol protocol) throws Exception {
        int len = protocol.getLen();
        byte[] content = protocol.getConcent();

        log.info("从客户端{}读到数据: 长度:{},内容:{}",ctx.channel().remoteAddress(),len,new String(content, CharsetUtil.UTF_8));
        log.info("消息包:{}",++count);
        //        //回复数据
//        ctx.writeAndFlush(Unpooled.copiedBuffer(UUID.randomUUID().toString(),CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable t){
        t.printStackTrace();
        ctx.close();
    }
}
