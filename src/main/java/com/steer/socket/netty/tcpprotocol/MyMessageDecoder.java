package com.steer.socket.netty.tcpprotocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * ReplayingDecoder自己判断长度够不够？
 */
public class MyMessageDecoder extends ReplayingDecoder<Void> {
    private Logger log = LoggerFactory.getLogger(MyMessageDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        log.info("My Message Decode 被调用");
        //将二进制字节码->MessageProtocol数据包
        int len = byteBuf.readInt();
        byte[] content = new byte[len];
        byteBuf.readBytes(content);

        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(len);
        messageProtocol.setConcent(content);
        list.add(messageProtocol);
    }
}
