package com.steer.socket.netty.inAndoutbound;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * outbound
 */
public class MyLongToByteEncoder extends MessageToByteEncoder<Long> {
    Logger log = LoggerFactory.getLogger(MyLongToByteEncoder.class);

    /**
     * MessageToByteEncoder的write方法会进行acceptOutboundMessage类型判断
     * 非Long的直接write出去
     * @param ctx
     * @param l
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Long l, ByteBuf byteBuf) throws Exception {
        log.info(">>>>>>>出站编码:{}",l);
        byteBuf.writeLong(l);
    }
}
