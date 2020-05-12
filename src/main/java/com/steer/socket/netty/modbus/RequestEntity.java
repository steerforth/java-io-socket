package com.steer.socket.netty.modbus;

public class RequestEntity {
    //通信地址
    private byte addr;
    //功能码
    private byte func;
    //寄存器起始地址
    private short registerAddr;
    //寄存器长度
    private short registerLen;
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

    public short getRegisterAddr() {
        return registerAddr;
    }

    public void setRegisterAddr(short registerAddr) {
        this.registerAddr = registerAddr;
    }

    public short getRegisterLen() {
        return registerLen;
    }

    public void setRegisterLen(short registerLen) {
        this.registerLen = registerLen;
    }

    public short getCrc() {
        return crc;
    }

    public void setCrc(short crc) {
        this.crc = crc;
    }

    public RequestEntity(byte addr, byte func, short registerAddr, short registerLen, short crc) {
        this.addr = addr;
        this.func = func;
        this.registerAddr = registerAddr;
        this.registerLen = registerLen;
        this.crc = crc;
    }
}
