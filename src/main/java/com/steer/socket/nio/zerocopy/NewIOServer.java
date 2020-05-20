package com.steer.socket.nio.zerocopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NewIOServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(7001));
        serverSocket.setReuseAddress(true);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        while (true){
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(true);

            long count = 0;
            long total = 0;
            while ( (count =socketChannel.read(byteBuffer)) != -1){
                total += count;
                byteBuffer.rewind();
            }

            System.out.println("收到数据大小:"+total);
        }
    }
}
