package com.steer.socket.simulator;

import com.steer.socket.bio.BIOSocketAcquirer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ModbusServerListener implements Runnable {
    private Logger LOGGER = LoggerFactory.getLogger(ModbusServerListener.class);
    private ServerSocket serverSocket;
    int port;
    private Simulator deviceSimulator;

    public void init(int port,Simulator deviceSimulator){
        this.port = port;
        this.deviceSimulator = deviceSimulator;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            while(true){
                LOGGER.info("端口号为[{}]的服务器,接收等待客户端连接中......",port);
                Socket socket = serverSocket.accept();
                LOGGER.info("端口号为[{}]的服务器,收到来自IP[{}]的连接.",port,socket.getRemoteSocketAddress());
                deviceSimulator.setSocket(socket);
                deviceSimulator.setServerPort(port);
                Thread acquirer = new Thread(deviceSimulator);
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
                LOGGER.error("close serverSocket failed !");
            }
        }
    }
}
