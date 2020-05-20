package com.steer.socket.netty;

import com.steer.socket.Util.HexUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

public class ZeroCopyTest {

    /**
     * 平常合并做法
     * 会多分配空间
     */
    @Test
    public void test1(){
        ByteBuf header = Unpooled.buffer(2);
        header.writeByte(0x03);
        ByteBuf body = Unpooled.buffer(4);
        body.writeByte(0x15);

        ByteBuf allBuf = Unpooled.buffer(header.readableBytes()+body.readableBytes());
        allBuf.writeBytes(header);
        allBuf.writeBytes(body);
    }

    /**
     * zero copy
     * CompositeByteBuf不需要copy,把多个ByteBuf逻辑上聚合起来
     */
    @Test
    public void test10(){
        ByteBuf header = Unpooled.buffer(2);
        header.writeByte(0x03);
        ByteBuf body = Unpooled.buffer(4);
        body.writeByte(0x15);
        //默认初始化16大小，劲量不要扩容
        CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer(16);
        compositeByteBuf.addComponents(true,header,body);

        byte[] res = new byte[2];
        compositeByteBuf.readBytes(res);
        System.out.println(HexUtil.bytesToHexString(res));
    }


    @Test
    public void test2(){
        byte[] b = new byte[]{0x01,0x03,0x05,0x07};
        ByteBuf byteBuf = Unpooled.buffer(4);
        byteBuf.writeBytes(b);
    }

    /**
     * zero copy
     */
    @Test
    public void test20(){
        byte[] b = new byte[]{0x01,0x03,0x05,0x07};
        ByteBuf byteBuf = Unpooled.wrappedBuffer(b);
    }


    @Test
    public void testFileZeroCopy(){
        File src = new File("/home/fangwk/Desktop/qxj-log/catalina.out");
        File dest = new File("/home/fangwk/Desktop/test.out");
        long t1 = System.currentTimeMillis();
        copy(src,dest);
        System.out.println("耗时:"+(System.currentTimeMillis()-t1));
    }

    @Test
    public void testFile(){
        File src = new File("/home/fangwk/Desktop/qxj-log/catalina.out");
        File dest = new File("/home/fangwk/Desktop/test.out");
        long t1 = System.currentTimeMillis();
        oldCopy(src,dest);
        System.out.println("耗时:"+(System.currentTimeMillis()-t1));
    }

    private void copy(File src, File dest) {
        try (
                FileChannel in = FileChannel.open(src.toPath(), StandardOpenOption.READ);
                FileChannel out = FileChannel.open(dest.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE)
        ) {
            in.transferTo(0, in.size(), out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void oldCopy(File src, File dst) {
        try (
                BufferedInputStream in = new BufferedInputStream(new FileInputStream(src));
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(dst))
        ) {
            byte[] data = new byte[1024];
            int len = 0;
            while ((len = in.read(data)) > 0) {
                out.write(data, 0, len);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void testMmap() throws IOException {
        File file = new File("/home/fangwk/Desktop/test.out");

        RandomAccessFile raf = new RandomAccessFile (file, "rw");

        FileChannel channel = raf.getChannel();

        MappedByteBuffer buff = channel.map(FileChannel.MapMode.READ_WRITE,0,file.length());

        buff.put(new String("你好压").getBytes());
//        buff.put(new String("你好压33").getBytes(),(int)file.length(),new String("你好压33").getBytes().length);

        channel.close();
        raf.close();
    }
}
