package com.steer.socket;

import com.steer.socket.client.SocketClient;
import com.steer.socket.server.ServerSocketListener;

/**
 * @Program: java-io-socket
 * @Author: Steerforth
 * @Description: socketServer
 * @Date: 2018-09-26 16:09
 */
public class Main {

    public static void main(String[] args) {
        //开启socket服务端口
        new Thread(new ServerSocketListener()).start();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SocketClient client = new SocketClient();
        client.run();
    }
}
