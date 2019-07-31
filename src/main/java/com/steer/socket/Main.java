package com.steer.socket;

import com.steer.socket.udp.UdpSocketServer;
import com.steer.socket.udp.UpdSocketSender;

import java.io.IOException;

/**
 * @Program: java-io-socket
 * @Author: Steerforth
 * @Description: socketServer
 * @Date: 2018-09-26 16:09
 */
public class Main {

    public static void main(String[] args) throws IOException {
        //-------1.开启BIO socket服务端口-------
//        new Thread(new BIOServerSocketListener(10001)).start();
//
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        BIOSocketClient client = new BIOSocketClient();
//        client.run();


        //---------2.开启udp socket服务端口 47808--------
        new Thread(new UdpSocketServer(11111)).start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        UpdSocketSender sender = new UpdSocketSender(11111,"192.168.2.177");
        sender.sendMsg("你号,dfdf");

        //-------3.开启NIO socket服务端口-------
//        NIOServer server = new NIOServer();
//        server.initServer(20000);
//        new Thread(server).start();
//
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        NIOClient client = new NIOClient();
//        client.initClient("192.168.2.171",20000);
//        new Thread(client).start();

        //--------4.开启NIO netty服务----------
//        new Thread(()->{
//            NettyServer server = new NettyServer();
//            server.init(8080);
//        }).start();
//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        new Thread(()->{
//            NettyClient client = new NettyClient();
//            client.init("127.0.0.1",8080);
//        }).start();
    }
}
