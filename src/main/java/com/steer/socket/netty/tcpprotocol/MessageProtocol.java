package com.steer.socket.netty.tcpprotocol;

public class MessageProtocol {
    private byte[] concent;
    private int len;

    public byte[] getConcent() {
        return concent;
    }

    public void setConcent(byte[] concent) {
        this.concent = concent;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }
}
