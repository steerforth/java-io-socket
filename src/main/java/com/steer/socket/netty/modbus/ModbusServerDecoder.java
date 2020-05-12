package com.steer.socket.netty.modbus;

import com.steer.socket.Util.HexUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ModbusServerDecoder extends ByteToMessageDecoder {
    Logger log = LoggerFactory.getLogger(ModbusServerDecoder.class);
    private static final int OUT_BOUND = 2048;
    private static final int BASE_LEN = 8;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        //调试日志
        int n = byteBuf.readableBytes();
        if (n > 0){
            int readIndex = byteBuf.readerIndex();
            byte[] b = new byte[n];
            byteBuf.readBytes(b);
            log.info("读到数据:{}", HexUtil.bytesToHexString(b));
            //还原读指针
            byteBuf.readerIndex(readIndex);
        }

        //不大于BASSE_LEN,要么都是半包数据，暂不解析；会存储在缓存中
        if (byteBuf.readableBytes() >= BASE_LEN){
            // 防止socket字节流攻击
            // 防止，客户端传来的数据过大
            // 因为，太大的数据，是不合理的
            if (byteBuf.readableBytes() > OUT_BOUND) {
                byteBuf.skipBytes(byteBuf.readableBytes());
            }

            while (byteBuf.readableBytes() >= BASE_LEN){
                int readIndex = byteBuf.readerIndex();
                byte addr = byteBuf.readByte();
                byte func = byteBuf.readByte();
                short registerAddr = byteBuf.readShort();
                short registerLen = byteBuf.readShort();
                //还原读指针
                byteBuf.readerIndex(readIndex);
                byte[] body = new byte[6];
                byteBuf.readBytes(body);
                int[] calCrc = ModbusUtil.calculateCRC(body, 0, body.length);
                short trueCrc = byteBuf.readShort();

                if(check(calCrc,trueCrc)){
                    RequestEntity entity = new RequestEntity(addr,func,registerAddr,registerLen,trueCrc);
                    list.add(entity);
                }else{
                    //校验和出错，把剩余的脏数据读完
                    while (byteBuf.readableBytes() > 0){
                        byteBuf.readByte();
                    }
                    log.info("校验和出错: body:{}  crc:{}",HexUtil.bytesToHexString(body),trueCrc);
                }

            }

        }

    }

    //校验crc16
    private boolean check(int[] calCrc, short trueCrc) {
        byte[] a = new byte[2];
        a[0] = (byte) calCrc[0];
        a[1] = (byte) calCrc[1];
        ByteBuf buf = Unpooled.buffer(2);
        buf.writeBytes(a);
        short s = buf.readShort();
        if (s == trueCrc){
            return true;
        }
        return false;
    }

}
