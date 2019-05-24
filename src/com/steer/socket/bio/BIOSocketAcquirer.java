package com.steer.socket.bio;

import com.steer.socket.Util.HexUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @Program: java-io-socket
 * @Author: Steerforth
 * @Description:
 * @Date: 2018-09-26 17:22
 */
public class BIOSocketAcquirer implements Runnable {
    private Socket socket;
    private InputStream in;
    private OutputStream out;

    public BIOSocketAcquirer(Socket socket) {
        this.socket = socket;
        try {
            this.in = socket.getInputStream();
            this.out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {
            byte[] data = new byte[100];
            int len = 0;
            while((len = in.read(data))!= -1){
//                System.out.println(new String(data,0,len));
            }
            System.out.println("服务端接收到数据:"+HexUtil.bytesToHexString(data));
            this.socket.shutdownInput();

            //response
            out.write("OKZZ".getBytes("UTF-8"));
            out.flush();
            socket.shutdownOutput();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                this.in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                this.out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (this.socket != null){
                try {
                    this.socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public void clearUnreadBytes(InputStream in) {
        long t = System.currentTimeMillis();
        boolean exit = false;
        try{
            while(!exit){ //skip all unread bytes
                int i = in.read();
                if(i==-1 || ((System.currentTimeMillis()-t)> 100)){		//读100ms，有的设备会出现读不完的情况。
                    exit = true;
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
