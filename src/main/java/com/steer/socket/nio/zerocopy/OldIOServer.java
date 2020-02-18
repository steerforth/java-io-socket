package com.steer.socket.nio.zerocopy;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 传统IO服务器
 */
public class OldIOServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(7001);
        while (true){
            Socket socket = serverSocket.accept();
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            byte[] byteArray = new byte[4096];
            long count;
            long total = 0;
            while ((count =dataInputStream.read(byteArray,0,byteArray.length)) != -1){
                total += count;
            }

            System.out.println(socket.getRemoteSocketAddress()+"接收到文件大小: "+total);
        }
    }
}
