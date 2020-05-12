package com.steer.socket.netty.modbus;

import com.steer.socket.Util.HexUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModBusServerEncoder extends MessageToByteEncoder<ResponseEntiy> {
    private Logger log = LoggerFactory.getLogger(ModBusServerEncoder.class);
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ResponseEntiy responseEntiy, ByteBuf byteBuf) throws Exception {
        byteBuf.writeByte(responseEntiy.getAddr());
        byteBuf.writeByte(responseEntiy.getFunc());
        byteBuf.writeByte(responseEntiy.getLen());
        byteBuf.writeBytes(responseEntiy.getBody());
        byteBuf.writeShort(responseEntiy.getCrc());

        byte[] data = new byte[5+responseEntiy.getLen()];
        int readerIndex = byteBuf.readerIndex();
        byteBuf.readBytes(data);
        byteBuf.readerIndex(readerIndex);
        log.info("server 返回数据:{}", HexUtil.bytesToHexString(data));
    }
}
