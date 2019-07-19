package com.steer.socket.simulator;

public class Main {
    public static void main(String[] args) {


        for (int port = 10001; port < 10008; port++) {
            ModbusServerListener serverListener = new ModbusServerListener();
            serverListener.init(port,new HTDeviceSimulator());
            new Thread(serverListener).start();
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
