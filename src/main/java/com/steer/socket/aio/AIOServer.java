package com.steer.socket.aio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class AIOServer {
    private Logger log = LoggerFactory.getLogger(AIOServer.class);

    private AsynchronousServerSocketChannel server;

    public void init(String host, int port) throws IOException {
        //ChannelGroup用来管理共享资源
        AsynchronousChannelGroup group = AsynchronousChannelGroup.withCachedThreadPool(Executors.newCachedThreadPool(), 10);
        server = AsynchronousServerSocketChannel.open(group);
        //通过setOption配置Socket
        server.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        server.setOption(StandardSocketOptions.SO_RCVBUF, 16 * 1024);
        //绑定到指定的主机，端口
        server.bind(new InetSocketAddress(host, port));
        log.info("listener {}:{}",host,port);
        //输出provider
        log.info("Channel Provider : {}" ,server.provider());
        //等待连接，并注册CompletionHandler处理内核完成后的操作。
        server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
            final ByteBuffer buffer = ByteBuffer.allocate(1024);

            @Override
            public void completed(AsynchronousSocketChannel result, Object attachment) {
                log.info("waiting for ...");
                buffer.clear();
                try {
                    //把socket中的数据读取到buffer中
                    result.read(buffer).get();
                    buffer.flip();
                    log.info("Echo {} to {}",new String(buffer.array()).trim(), result);

                    //把收到的直接返回给客户端
                    result.write(buffer);
                    buffer.flip();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        //关闭处理完的socket，并重新调用accept等待新的连接
                        result.close();
                        server.accept(null, this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                log.error("Server failed...: {}",exc.getCause());
            }
        });

    }
}
