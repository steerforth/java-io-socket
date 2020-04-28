package com.steer.socket.netty.dubborpc.provider;

import com.steer.socket.netty.dubborpc.framework.URL;
import com.steer.socket.netty.dubborpc.netty.NettyServer;
import com.steer.socket.netty.dubborpc.publicinterface.HelloService;
import com.steer.socket.netty.dubborpc.register.LocalRegister;
import com.steer.socket.netty.dubborpc.register.RemoteRegister;

public class ServerBootstrap {
    public static void main(String[] args) {
        //本地注册
        LocalRegister.regist(HelloService.class.getName(),HelloServiceImpl.class);


        //远程注册
        URL url = new URL("127.0.0.1",8888);
        RemoteRegister.regist(HelloService.class.getName(),url);

        //启动server
        NettyServer.startServer(url.getHostName(),url.getPort());
    }
}
