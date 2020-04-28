package com.steer.socket.netty.dubborpc.netty;

import com.steer.socket.netty.dubborpc.framework.Invocation;
import com.steer.socket.netty.dubborpc.provider.HelloServiceImpl;
import com.steer.socket.netty.dubborpc.publicinterface.HelloService;
import com.steer.socket.netty.dubborpc.register.LocalRegister;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    Logger log = LoggerFactory.getLogger(NettyServerHandler.class);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Invocation invocation = (Invocation) msg;
        //获取客户端发送的消息，并调用服务
        log.info("========={}",msg);

        //反射
        Class clz = LocalRegister.get(invocation.getInterfaceName());
        Object o = clz.newInstance();
        Method method = clz.getMethod(invocation.getMethodName(), invocation.getParamTypes());
        Object result = method.invoke(o,invocation.getParams());

        ctx.writeAndFlush(result);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

}
