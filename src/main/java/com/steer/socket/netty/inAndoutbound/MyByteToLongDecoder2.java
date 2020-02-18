package com.steer.socket.netty.inAndoutbound;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 使用方便，但也有些局限性
 * 并不是所有的Bytebuf操作都被支持，如果调用了一个不被支持的方法，将会抛出一个UnsupportedOpertationException
 * ReplayingDecoder在某些情况下可能稍慢于ByteToMessageDecoder,
 * 例如网络缓慢，并且消息格式浮渣时，消息会被拆成了多个碎片，速度变慢
 */
public class MyByteToLongDecoder2 extends ReplayingDecoder<Void> {
    Logger log = LoggerFactory.getLogger(MyByteToLongDecoder2.class);
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        log.info("~~~~~入站解码:{}",byteBuf);
        //
        long l = byteBuf.readLong();
        log.info("======={}",l);
        list.add(l);
    }
}
