package com.steer.socket.netty.modbus;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModbusServerHandler extends ChannelInboundHandlerAdapter {
    private Logger log = LoggerFactory.getLogger(ModbusServerHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("服务端ModbusServerHandler通道就绪:{}",ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RequestEntity) {
            RequestEntity req = (RequestEntity) msg;
            log.info("收到客户端[{}]消息: 请求地址:{} 功能码:{} 寄存器地址:{} 寄存器长度:{}",ctx.channel().remoteAddress(),req.getAddr(),req.getFunc(),req.getRegisterAddr(),req.getRegisterLen());

            ResponseEntiy res = new ResponseEntiy();
            res.setAddr(req.getAddr());
            res.setFunc(req.getFunc());
            res.setLen((byte) 4);
            //数据
            res.setBody(new byte[]{0x01,0x2E,0x02,0x63});
            res.calucateCrc();
            ctx.channel().writeAndFlush(res);

//            //处理返回
//            ByteBuf buf = Unpooled.buffer(6);
//            buf.writeBytes(new byte[]{0x01,0x04,0x04,0x01,0x2E,0x02,0x63,(byte)0xDB,0x38});
//            ctx.channel().writeAndFlush(buf);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
        //这里不做处理！！！  ByteToMessageDecoder调用一次  这里也会被调用一次;处理粘包时会被多次调用
        //返回给客户端的数据，告诉我已经读到你的数据了
        log.info("=====read complete end=====");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        log.error("遇到异常，关闭context");
    }


}
