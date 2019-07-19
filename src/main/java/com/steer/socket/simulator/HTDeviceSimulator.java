package com.steer.socket.simulator;

import com.steer.socket.Util.HexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 温湿度设备模拟器   03/04
 */
public class HTDeviceSimulator extends Simulator{
    private Logger LOGGER = LoggerFactory.getLogger(HTDeviceSimulator.class);

    @Override
    public void run() {

        try {
            while (true) {
                byte[] data = new byte[8];
                int len = 0;
                while ((len = in.read(data)) == -1) {
                    //查看客户端是否断开
                    socket.sendUrgentData(0xFF);
                }
                LOGGER.info("端口号为[{}]的服务器,收到数据:{}",this.getServerPort(),HexUtil.bytesToHexString(data));
                if ("010300000002C40B".equals(HexUtil.bytesToHexString(data))) {
                    out.write(RESPONSE);
                    out.flush();
                } else {
                    LOGGER.info("端口号为[{}]的服务器,无数据返回",this.getServerPort());
                }
            }

        } catch (IOException e) {
            LOGGER.error("端口号为[{}]的服务器,客户端[{}]连接断开:{}",this.getServerPort(),socket.getRemoteSocketAddress(),e.getMessage());

            try {
                this.in.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            try {
                this.out.close();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            if (this.socket != null){
                try {
                    this.socket.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
        }

    }


    private static byte[] RESPONSE = new byte[]{0x01, 0x03, 0x04, 0x07, (byte) 0xD0, 0x15, 0x7C, (byte) 0xF5, (byte) 0xCF};

}
