package com.steer.socket.nio.zerocopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NewIOClient {

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost",7001));

        String filename = "/home/fangwk/Desktop/qxj-log/catalina.out";
        FileChannel fileChannel = new FileInputStream(filename).getChannel();

        long startAt = System.currentTimeMillis();

        /**
         * 底层使用了零拷贝
         * window下一次最大只能发送8m，需要分段传输
         */
        long count = fileChannel.transferTo(0,fileChannel.size(),socketChannel);



        System.out.println("发送总大小:"+count+",耗时:"+(System.currentTimeMillis()-startAt));

        fileChannel.close();
        socketChannel.close();
    }
}
