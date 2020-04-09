package com.steer.socket.udp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;

public class UpdSocketSender {
    private Logger log = LoggerFactory.getLogger(UdpSocketServer.class);
    private int port;
    private String host;

    public UpdSocketSender(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public void sendMsg(String content){
        //建立udp的服务
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket();
            //准备数据，把数据封装到数据包中。
            String data = content;
            log.info("发送端发送内容:{}",content);
//            InetAddress a = InetAddress.getLocalHost();
            //创建了一个数据包
            DatagramPacket packet = new DatagramPacket(data.getBytes(), data.getBytes().length, InetAddress.getByName(host) , this.port);
            //调用udp的服务发送数据包
            datagramSocket.send(packet);
            //关闭资源 ---实际上就是释放占用的端口号
            datagramSocket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
