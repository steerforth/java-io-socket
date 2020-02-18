package com.steer.socket.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {
    private Logger log = LoggerFactory.getLogger(GroupChatServerHandler.class);

    // 定义一个channel组，管理所有的channel
    // GlobalEventExecutor.INSTANCE是全局的事件执行器，是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 表示连接一单建立，第一个被执行
     * @param var1
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext var1) throws Exception{
        super.handlerAdded(var1);
        //会将channelGroup中所有的channel遍历，并发送消息
        channelGroup.writeAndFlush(sdf.format(new Date())+" [客户端]"+var1.channel().remoteAddress()+" 加入聊天室\n");
        log.debug("当前用户数:{}",channelGroup.size());
        channelGroup.add(var1.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext var1) throws Exception{
        super.handlerAdded(var1);
        //会将channelGroup中所有的channel遍历，并发送消息
        channelGroup.writeAndFlush(sdf.format(new Date())+" [客户端]"+var1.channel().remoteAddress()+" 离开聊天室\n");
        log.debug("当前用户数:{}",channelGroup.size());
        channelGroup.remove(var1.channel());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info(ctx.channel().remoteAddress()+"上线了！");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info(ctx.channel().remoteAddress()+"下线了！");
    }

    /**
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.forEach(ch->{
            if (channel != ch){
                ch.writeAndFlush(sdf.format(new Date())+" [客户]"+channel.remoteAddress()+" 发送了消息:"+msg+"\n");
            }else{
                //回显自己发送的消息
                ch.writeAndFlush(sdf.format(new Date())+" [自己] 发送了消息:"+msg+"\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventType = null;
            switch (event.state()){
                case READER_IDLE:
                    eventType = "读空闲";
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";
                    break;
                case ALL_IDLE:
                    eventType = "读写空闲";
                    break;
            }
            log.debug("{}{}，服务器做相应处理:",ctx.channel().remoteAddress(),eventType);
        }
    }
}
