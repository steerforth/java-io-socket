package com.steer.socket.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class WebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private Logger log = LoggerFactory.getLogger(WebSocketFrameHandler.class);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception{
        //id表示唯一的一个值  LongText是唯一的，shortText不是唯一的
        log.info("handler added:{}   {}",ctx.channel().id().asLongText(),ctx.channel().id().asShortText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception{
        //id表示唯一的一个值  LongText是唯一的，shortText不是唯一的
        log.info("handler removed:{}   {}",ctx.channel().id().asLongText(),ctx.channel().id().asShortText());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        log.info("服务器收到:{}消息:{}",ctx.channel().remoteAddress(),msg
        .text());
        //回复消息
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间:"+LocalDateTime.now()+" "+msg
        .text()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("{}",cause.getMessage());
        ctx.close();
    }
}
