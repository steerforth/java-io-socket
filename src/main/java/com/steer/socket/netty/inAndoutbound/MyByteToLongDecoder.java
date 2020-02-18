package com.steer.socket.netty.inAndoutbound;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * inbound
 */
public class MyByteToLongDecoder extends ByteToMessageDecoder {
    Logger log = LoggerFactory.getLogger(MyByteToLongDecoder.class);

    /**
     * 如果list不为空，交由下一个handler处理
     * @param ctx
     * @param byteBuf
     * @param list
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        log.info("<<<<<入站解码:{}",byteBuf);
        if (byteBuf.readableBytes() >=8){
            long l = byteBuf.readLong();
            log.info("======={}",l);
            list.add(l);
        }
    }
}
