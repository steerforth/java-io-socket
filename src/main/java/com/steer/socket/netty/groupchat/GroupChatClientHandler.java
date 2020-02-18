package com.steer.socket.netty.groupchat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroupChatClientHandler extends SimpleChannelInboundHandler<String> {
    private Logger log = LoggerFactory.getLogger(GroupChatClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        log.info("{}",msg);
    }
}
