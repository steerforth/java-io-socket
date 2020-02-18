package com.steer.socket.netty.tcpprotocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    Logger log = LoggerFactory.getLogger(MyClientHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 5; i++) {
           String msg = "今天天气冷，吃火锅";
           byte[] content = msg.getBytes(CharsetUtil.UTF_8);
           int len = content.length;

           MessageProtocol message = new MessageProtocol();
           message.setConcent(content);
           message.setLen(len);
           ctx.writeAndFlush(message);

        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol buf) throws Exception {
//        String msg = new String(bytes, CharsetUtil.UTF_8);
//        log.info("从服务器{}读到数据:{}",ctx.channel().remoteAddress(),msg);
//        log.info("客户端接收消息次数:{}",++count);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable t){
        t.printStackTrace();
        ctx.close();
    }
}
