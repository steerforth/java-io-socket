package com.steer.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class NIOServer {
    //通道管理器
    private Selector selector;

    public void initServer(int port) throws IOException {
        //获取一个ServerSocket通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //设置通道为非阻塞
        serverSocketChannel.configureBlocking(false);
        //坚挺端口
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        this.selector = Selector.open();
        //将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_ACCEPT事件,注册该事件后
        //当该事件到达时，selector.select()会返回，如果该事件没到达selector.select()会一直阻塞
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void listen() throws IOException{
        System.out.println("服务端启动成功");
        //轮询访问selector
        while (true){
            //当注册事件到达时，方法返回；否则，该方法会一直阻塞
            selector.select();
            Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                //删除已选的key，防止重复处理
                iterator.remove();
                //客户端请求的连接事件
                if (key.isAcceptable()){
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel channel = server.accept();
                    channel.configureBlocking(false);

                    channel.write(ByteBuffer.wrap(new String("像客户端发送一条信息").getBytes("utf-8")));
                    channel.register(this.selector,SelectionKey.OP_READ);
                    //获取可读事件
                }else if(key.isReadable()){
                    read(key);
                }
            }
        }
    }

    /**
     * 处理客户端发来的信息 事件
     * @param key
     */
    private void read(SelectionKey key) throws IOException {
        //
        SocketChannel channel = (SocketChannel) key.channel();
        //创建读取的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(10);
        channel.read(buffer);

        byte[] data = buffer.array();
        String msg = new String(data).trim();
        System.out.println("服务器收到信息："+msg);
        //回传消息给客户端
        channel.write(ByteBuffer.wrap(("已收到你发送的消息内容:"+msg).getBytes("utf-8")));
    }


    public static void main(String[] args) throws IOException {
        NIOServer server = new NIOServer();
        server.initServer(20000);
        server.listen();
    }
}
