package com.steer.socket.netty.dubborpc.customer;

import com.steer.socket.netty.dubborpc.netty.NettyClient;
import com.steer.socket.netty.dubborpc.publicinterface.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientBootstrap {
    static Logger log = LoggerFactory.getLogger(ClientBootstrap.class);

    //定义协议头
    public static final String providerName = "dubbo://";

    public static void main(String[] args) {

        NettyClient client = new NettyClient();
        HelloService helloService = (HelloService) client.getBean(HelloService.class,providerName);

        for (int i = 0; i < 10; i++) {
            String res = helloService.hello("hello dubbo "+i);
            log.info("====结果:{}===",res);
        }
    }
}
