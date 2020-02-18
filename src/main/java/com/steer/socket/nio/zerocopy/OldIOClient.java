package com.steer.socket.nio.zerocopy;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class OldIOClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 7001);

        String filename = "/Users/steer/Desktop/vote.txt";
        InputStream inputStream = new FileInputStream(filename);
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

        byte[] byteArray = new byte[4096];
        long readCount;
        long total = 0;

        long startAt = System.currentTimeMillis();

        while ((readCount = inputStream.read(byteArray))!= -1){
            total += readCount;
            outputStream.write(byteArray);
        }

        System.out.println("发送总字节数: "+total+",耗时: "+(System.currentTimeMillis()-startAt));

        outputStream.close();
        inputStream.close();
        socket.close();
    }
}
