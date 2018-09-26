package com.steer.socket.Util;

/**
 * @Program: java-io-socket
 * @Author: Steerforth
 * @Description:
 * @Date: 2018-09-26 19:40
 */
public class HexUtil {
    public static String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp =Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }
}
