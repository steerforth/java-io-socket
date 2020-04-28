package com.steer.socket.netty.dubborpc.register;

import com.steer.socket.netty.dubborpc.framework.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemoteRegister {
    private static Map<String, List<URL>> map = new HashMap<>();

    public static void regist(String interfaceName, URL url){
        if (map.containsKey(interfaceName)){
            map.get(interfaceName).add(url);
        }else{
            List<URL> list = new ArrayList<>();
            list.add(url);
            map.put(interfaceName,list);
        }
    }

    public static URL get(String interfaceName){
        return map.get(interfaceName).get(0);
    }
}
