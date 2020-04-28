package com.steer.socket.netty.dubborpc.register;

import java.util.HashMap;
import java.util.Map;

public class LocalRegister {
    private static Map<String,Class> map = new HashMap<>();

    public static void regist(String interfaceName,Class implClz){
        map.put(interfaceName,implClz);
    }

    public static Class get(String interfaceName){
        return map.get(interfaceName);
    }
}
