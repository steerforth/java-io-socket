package com.steer.socket.udp;

import com.steer.socket.Util.HexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.Date;

public class UdpSocketServer implements Runnable{
    private Logger log = LoggerFactory.getLogger(UdpSocketServer.class);
    private boolean terminated = false;
    private int port;

    public UdpSocketServer(int port) {
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
                DatagramPacket receivePacket = new DatagramPacket(buf, buf.length); // 1024
                log.info("开始接收等待UDP消息...");
                //调用udp的服务接收数据
                socket.receive(receivePacket); //receive是一个阻塞型的方法，没有接收到数据包之前会一直等待。
                log.info("接收端接收到的数据：{}",HexUtil.bytesToHexString(buf));
                log.info("接收端接收到转码的数据：{}",new String(buf,0,receivePacket.getLength(),"UTF-8")); // getLength() 获取数据包存储了几个字节。


                byte[] sendBuf = new byte[]{0x01,0x02,0x04};
                DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length,receivePacket.getAddress(),receivePacket.getPort());
                socket.send(sendPacket);
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
