package com.steer.socket.netty.dubborpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context;

    private String result;
    private String param;

    /**
     * 初始化
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.context = ctx;
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //服务器结果返回
        result = (String) msg;
        notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    //被代理对象调用，发送数据给服务器  wait等待被唤醒，返回结果
    @Override
    public synchronized Object call() throws Exception {
        //发送消息
        context.writeAndFlush(param);
        //等待服务器结果返回
        wait();
        //返回结果给调用代理
        return result;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
