package com.steer.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOServer implements Runnable{
    //通道管理器
    private Selector selector;

    public void initServer(int port) throws IOException {
        //获取一个ServerSocket通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //设置通道为非阻塞
        serverSocketChannel.configureBlocking(false);
        //监听端口
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        this.selector = Selector.open();
        //将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_ACCEPT事件,注册该事件后
        //当该事件到达时，selector.select()会返回，如果该事件没到达selector.select()会一直阻塞
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    @Override
    public void run() {
        System.out.println("服务端轮询启动成功！");
        //轮询访问selector
        while (true){
            //当注册事件到达时，方法返回；否则，该方法会一直阻塞
            try {
                int result = this.selector.select(1000);
                if (result == 0){
                    continue;
                }
                //有事件发生的key
                Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    //删除已选的key，防止重复处理
                    iterator.remove();

                    handler(key);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void handler(SelectionKey key) throws IOException {
        // 客户端请求连接事件
        if (key.isAcceptable()) {
            handlerAccept(key);
            // 获得了可读的事件
        } else if (key.isReadable()) {
            handlerRead(key);
        }
    }

    private void handlerAccept(SelectionKey key) throws IOException {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel channel = server.accept();
        channel.configureBlocking(false);
        System.out.println("收到了来自IP:"+channel.socket().getInetAddress().getHostAddress()+"的连接请求");
        channel.write(ByteBuffer.wrap(new String("您已连接成功！").getBytes("utf-8")));
        channel.register(this.selector,SelectionKey.OP_READ,ByteBuffer.allocate(100));
    }


    /**
     * 处理客户端发来的信息 事件
     * @param key
     */
    private void handlerRead(SelectionKey key) throws IOException {
        //设置或改变监听的事件
        //key.interestOps(SelectionKey.OP_WRITE);
        //
        SocketChannel channel = (SocketChannel) key.channel();
        //创建读取的缓冲区
//        ByteBuffer buffer = ByteBuffer.allocate(100);
        //attachment 与之关联的共享数据
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        buffer.rewind();
        int size = channel.read(buffer);
        if (size <= 0){
            key.cancel();
            channel.close();
            System.out.println("服务端关闭了通道："+channel.socket().toString());
        }else{
            byte[] data = buffer.array();
            String msg = new String(data).trim();
            System.out.println("服务器收到信息："+msg);
            //回传消息给客户端
            channel.write(ByteBuffer.wrap(("服务端已读消息内容:"+msg).getBytes("utf-8")));
        }

    }


    public static void main(String[] args) throws IOException {
        NIOServer server = new NIOServer();
        server.initServer(20000);
        new Thread(server).start();
    }


}
