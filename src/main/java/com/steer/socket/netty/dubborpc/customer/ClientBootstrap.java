package com.steer.socket.netty.dubborpc.customer;

import com.steer.socket.netty.dubborpc.framework.ProxyFactory;
import com.steer.socket.netty.dubborpc.publicinterface.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientBootstrap {
    static Logger log = LoggerFactory.getLogger(ClientBootstrap.class);

    public static void main(String[] args) {
        //具体协议实现, 可以用工厂策略模式  或者SPI机制
        HelloService helloService = (HelloService) ProxyFactory.getProxy(HelloService.class);

        for (int i = 0; i < 10; i++) {
            String res = helloService.hello("hello dubbo "+i);
            log.info("====收到RPC结果:{}===",res);
        }
    }
}
