package com.steer.socket.netty.snmp;

import com.steer.socket.Util.HexUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SnmpClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    private Logger log = LoggerFactory.getLogger(SnmpClientHandler.class);
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket datagramPacket) throws Exception {
        ByteBuf buf = datagramPacket.copy().content();
        byte[] content = new byte[buf.readableBytes()];
        buf.readBytes(content);
        log.info("客户端收到来自[{}:{}]的消息:{}",datagramPacket.sender().getAddress(),datagramPacket.sender().getPort(), HexUtil.bytesToHexString(content));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }
}
