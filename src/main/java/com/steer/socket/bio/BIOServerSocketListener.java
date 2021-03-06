package com.steer.socket.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Program: java-io-socket
 * @Author: Steerforth
 * @Description:
 * @Date: 2018-09-26 16:13
 */
public class BIOServerSocketListener implements Runnable{

    private ServerSocket serverSocket;
    int port;

    public BIOServerSocketListener(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("开启端口号为:"+port+"的socket服务");
            while(true){
                System.out.println("服务端接收等待中...");

                Socket socket = serverSocket.accept();
                Thread acquirer = new Thread(new BIOSocketAcquirer(socket));
//	                t.setDaemon(true);
                acquirer.start();
                while(acquirer.isAlive()){

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket!=null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                System.out.println("close serverSocket failed !");
            }
        }
    }
}
