package com.steer.socket.netty.snmp;

import com.steer.socket.Util.HexUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SnmpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    private Logger log = LoggerFactory.getLogger(SnmpServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket datagramPacket) throws Exception {
        ByteBuf buf = datagramPacket.copy().content();
        byte[] aa = new byte[buf.readableBytes()];
        buf.readBytes(aa);
        log.info("收到来自[{}:{}]的消息:{}",datagramPacket.sender().getAddress(),datagramPacket.sender().getPort(),HexUtil.bytesToHexString(aa));

        ByteBuf res = Unpooled.buffer(3).writeBytes(new byte[]{0x01,0x03,0x09});
        ctx.writeAndFlush(new DatagramPacket(res,datagramPacket.sender()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }
}
