package com.steer.socket.netty.modbus;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ResponseEntiy {
    //通信地址
    private byte addr;
    //功能码
    private byte func;
    //数据包长度
    private byte len;
    //数据包
    private byte[] body;
    //校验码
    private short crc;

    public byte getAddr() {
        return addr;
    }

    public void setAddr(byte addr) {
        this.addr = addr;
    }

    public byte getFunc() {
        return func;
    }

    public void setFunc(byte func) {
        this.func = func;
    }

    public byte getLen() {
        return len;
    }

    public void setLen(byte len) {
        this.len = len;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public short getCrc() {
        return crc;
    }

    public void setCrc(short crc) {
        this.crc = crc;
    }


    public void calucateCrc() {
        byte[] data = new byte[3+this.getLen()];
        data[0] = this.getAddr();
        data[1] = this.getFunc();
        data[2] = this.getLen();
        for (int i = 0; i < this.getLen(); i++) {
            data[i + 3] = this.getBody()[i];
        }
        int[] crc = ModbusUtil.calculateCRC(data, 0, data.length);
        //crc转short
        byte[] a = new byte[2];
        a[0] = (byte) crc[0];
        a[1] = (byte) crc[1];
        ByteBuf buf = Unpooled.buffer(2);
        buf.writeBytes(a);
        this.setCrc(buf.readShort());
    }
}
