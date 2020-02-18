package com.steer.socket.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class MyHttpServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //HttpServerCodec是netty提供的http编解码器
        socketChannel.pipeline().addLast("MyHttpServerCodec",new HttpServerCodec());
        socketChannel.pipeline().addLast("MyHttpServerHandler",new MyHttpServerHandler());
    }
}
