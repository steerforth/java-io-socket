package com.steer.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOClient {
    //通道管理器
    private Selector selector;

    public void initClient(String ip,int port) throws IOException {
        // 获得一个Socket通道
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        // 获得一个通道管理器
        this.selector = Selector.open();
        // 客户端连接服务器,其实方法执行并没有实现连接，需要在listen（）方法中调
        //用channel.finishConnect();才能完成连接
        channel.connect(new InetSocketAddress(ip,port));

        channel.register(selector, SelectionKey.OP_CONNECT);
    }

    /**
     * 采用轮询的方式监听selector上是否有需要处理的事件,如果有，则进行处理
     */
    public void listen() throws IOException {
        //轮训访问selector
        while (true){
            selector.select();
            Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                // 删除已选的key,以防重复处理
                iterator.remove();
                //连接事件发生
                if (key.isConnectable()){
                    SocketChannel channel = (SocketChannel) key.channel();
                    //如果正在连接，则完成连接
                    if (channel.isConnectionPending()){
                        channel.finishConnect();
                    }

                    channel.configureBlocking(false);

                    channel.write(ByteBuffer.wrap(new String("向服务器发送消息：连接成功").getBytes("utf-8")));

                    channel.register(this.selector,SelectionKey.OP_READ);
                }else if(key.isReadable()){
                    read(key);
                }
            }
        }

    }

    private void read(SelectionKey key) throws IOException {
        //
        SocketChannel channel = (SocketChannel) key.channel();
        //创建读取的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(10);
        channel.read(buffer);

        byte[] data = buffer.array();
        String msg = new String(data).trim();
        System.out.println("客户端收到信息："+msg);
        //回传消息给客户端
//        channel.write(ByteBuffer.wrap(("已收到你发送的消息内容:"+msg).getBytes("utf-8")));
    }

    public static void main(String[] args) throws IOException {
        NIOClient client = new NIOClient();
        client.initClient("localhost",20000);
        client.listen();
    }
}
