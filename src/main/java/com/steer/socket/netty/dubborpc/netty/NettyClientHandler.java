package com.steer.socket.netty.dubborpc.netty;

import com.steer.socket.netty.dubborpc.framework.Invocation;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context;

    private Object result;
    private Invocation invocation;

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
        result = msg;
        notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    //被代理对象调用，发送数据给服务器  wait等待被唤醒，返回结果
    @Override
    public synchronized Object call() throws Exception {
        //发送消息
        context.writeAndFlush(invocation);
        //等待服务器结果返回
        wait();
        //返回结果给调用代理
        return result;
    }

    public Invocation getInvocation() {
        return invocation;
    }

    public void setInvocation(Invocation invocation) {
        this.invocation = invocation;
    }
}
