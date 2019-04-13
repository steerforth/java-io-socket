package com.steer.socket;

import com.steer.socket.client.SocketClient;
import com.steer.socket.client.UdpSocketClient;
import com.steer.socket.server.ServerSocketListener;
import com.steer.socket.server.UpdSocketSender;

/**
 * @Program: java-io-socket
 * @Author: Steerforth
 * @Description: socketServer
 * @Date: 2018-09-26 16:09
 */
public class Main {

    public static void main(String[] args) {
        //-------1.开启socket服务端口-------
//        new Thread(new ServerSocketListener()).start();
//
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        SocketClient client = new SocketClient();
//        client.run();


        //---------2.开启udp socket服务端口 47808--------
        new Thread(new UdpSocketClient(47808)).start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        UpdSocketSender sender = new UpdSocketSender(11111,"192.168.2.255");
        sender.sendMsg("你号,dfdf");
    }
}
