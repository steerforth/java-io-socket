package com.steer.socket.netty.dubborpc.provider;

import com.steer.socket.netty.dubborpc.publicinterface.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloServiceImpl implements HelloService {
    Logger log = LoggerFactory.getLogger(HelloServiceImpl.class);
    @Override
    public String hello(String msg) {
        log.info("收到客户端消息:{}",msg);
        if (msg == null){
            return "你好客户端，我已收到你的消息";
        }
        return "你好客户端，我已收到你的消息:["+msg+"]";
    }
}
