package com.steer.socket.nio.zerocopy;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

public class Test {
    public static void main(String[] args) {
        Path copy_from = Paths.get("/home/fangwk/Desktop/61.hprof");
        Path copy_to = Paths.get("/home/fangwk/Desktop/s3.txt");

        try (FileChannel fileChannel_from = (FileChannel.open(copy_from,
                EnumSet.of(StandardOpenOption.READ)));
             FileChannel fileChannel_to = (FileChannel
                     .open(copy_to, EnumSet.of(StandardOpenOption.CREATE_NEW,
                             StandardOpenOption.WRITE)))) {
            long t1 = System.currentTimeMillis();
            System.out.println("开始复制:"+fileChannel_from.size()+"字节");
            long size = fileChannel_from.transferTo(0L, fileChannel_from.size(), fileChannel_to);
            System.out.println("使用 FileChannel.transferTo 方法复制文件成功:"+size+"字节"+"  耗时:"+(System.currentTimeMillis()-t1));
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}
