package com.steer.socket.udp;

import com.steer.socket.Util.HexUtil;

import java.io.IOException;
import java.net.*;
import java.util.Date;

public class UdpSocketClient implements Runnable{
    private boolean terminated = false;
    private int port;

    public UdpSocketClient(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        while (!terminated){
            DatagramSocket socket = null;
            try {
//                InetAddress address = Inet4Address.getByName("192.168.2.171");
                socket = new DatagramSocket(port);
                byte[] buf = new byte[1024];
                DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length); // 1024
                System.out.println(new Date()+":开始接收等待UDP消息...");
                //调用udp的服务接收数据
                socket.receive(datagramPacket); //receive是一个阻塞型的方法，没有接收到数据包之前会一直等待。
                System.out.println(new Date()+":接收端接收到的数据："+ HexUtil.bytesToHexString(buf));
                System.out.println("接收端接收到转码的数据："+ new String(buf,0,datagramPacket.getLength(),"UTF-8")); // getLength() 获取数据包存储了几个字节。
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (socket != null){
                    socket.close();
                }
            }

        }
    }

    public void stop(){
        this.terminated = true;
    }
}
