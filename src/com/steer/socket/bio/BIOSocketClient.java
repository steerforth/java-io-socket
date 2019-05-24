package com.steer.socket.bio;

import com.steer.socket.Util.HexUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * @Program: java-io-socket
 * @Author: Steerforth
 * @Description: Socket客户端
 * @Date: 2018-09-26 19:19
 */
public class BIOSocketClient {
    private String host = "192.168.2.171";
    private int port = 23000;

    public void run(){
        Socket socket = null;
        try {
            socket = new Socket(host, port);
            socket.setSoTimeout(3000);
            //向服务端写入数据
            socket.getOutputStream().write(new byte[]{0x01,0x02,0x02,0x00,0x01,0x00,0x00});
            socket.shutdownOutput();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //接收服务端返回的数据
            int len = 0;
            byte[] data = new byte[1024];
            InputStream in = socket.getInputStream();
            while ((len = in.read(data))!= -1) {
                System.out.println("客户端接收到服务端响应："+new String(data,0,len));
            }
//            System.out.println("客户端接收到服务端响应："+HexUtil.bytesToHexString(data));
            socket.shutdownInput();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
