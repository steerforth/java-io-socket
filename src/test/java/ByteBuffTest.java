import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

public class ByteBuffTest {
    private static Logger LOGGER = LoggerFactory.getLogger(ByteBuffTest.class);

    @Test
    public void test(){
        ByteBuffer buffer =  ByteBuffer.allocate(8);

        LOGGER.info("position:{},limit:{},capacity:{}\n",buffer.position(),buffer.limit(),buffer.capacity());
        buffer.put(new byte[]{0x00,0x01,0x04});
        LOGGER.info("{}",buffer.array());
        LOGGER.info("position:{},limit:{},capacity:{}\n",buffer.position(),buffer.limit(),buffer.capacity());

        LOGGER.info("after flip,切换为读取模式");
        //limit会变为当前position大小，然后position设为0，
        buffer.flip();
        LOGGER.info("position:{},limit:{},capacity:{}\n",buffer.position(),buffer.limit(),buffer.capacity());
        //相对读，position会+1；使用绝对读，buffer.get(buffer的index)，不会改变position大小
        buffer.get();
        buffer.get();
        LOGGER.info("{}",buffer.array());
        LOGGER.info("position:{},limit:{},capacity:{}\n",buffer.position(),buffer.limit(),buffer.capacity());
    }

    @Test
    public void testMark(){
        ByteBuffer buffer =  ByteBuffer.allocate(8);
        //相对写，postion+添加的size；使用绝对写，buffer.put(int index,byte b)不会改变position
        buffer.put(new byte[]{0x00,0x01,0x04});
        //会标记当前的position
        buffer.mark();
        LOGGER.info("mark: position:{},limit:{},capacity:{}\n",buffer.position(),buffer.limit(),buffer.capacity());
        buffer.put((byte) 5);
        LOGGER.info("put: position:{},limit:{},capacity:{}\n",buffer.position(),buffer.limit(),buffer.capacity());
        //对buff进行重置，恢复到mark时的状态
        buffer.reset();
        LOGGER.info("reset: position:{},limit:{},capacity:{}\n",buffer.position(),buffer.limit(),buffer.capacity());
        buffer.flip();
        LOGGER.info("flip: position:{},limit:{},capacity:{}\n",buffer.position(),buffer.limit(),buffer.capacity());
        buffer.get();
        buffer.mark();
        LOGGER.info("get and mark: position:{},limit:{},capacity:{}\n",buffer.position(),buffer.limit(),buffer.capacity());
        buffer.get();
        LOGGER.info("get: position:{},limit:{},capacity:{}\n",buffer.position(),buffer.limit(),buffer.capacity());
        buffer.reset();
        LOGGER.info("reset: position:{},limit:{},capacity:{}\n",buffer.position(),buffer.limit(),buffer.capacity());
    }

    @Test
    public void testRewind(){
        ByteBuffer buffer = ByteBuffer.allocate(6);
        // position: 0, limit: 6, capacity: 6
        LOGGER.info("position:{},limit:{},capacity:{}\n",buffer.position(),buffer.limit(),buffer.capacity());

        buffer.put((byte) 1);
        buffer.put((byte) 2);
        buffer.put((byte) 3);
        buffer.mark();
        // position: 3, limit: 6, capacity: 6
        LOGGER.info("put and mark: position:{},limit:{},capacity:{}\n",buffer.position(),buffer.limit(),buffer.capacity());
        // 调用rewind()方法之后，postion会置为0，mark会置为-1,即之前的mark方法失效，再次调用reset方法会报错
        buffer.rewind();
        // position: 0, limit: 6, capacity: 6
        LOGGER.info("rewind: position:{},limit:{},capacity:{}\n",buffer.position(),buffer.limit(),buffer.capacity());
        //会报错
        //        buffer.reset();
    }

    @Test
    public void testCompact(){
        ByteBuffer buffer = ByteBuffer.allocate(6);
        buffer.put((byte) 1);
        buffer.put((byte) 2);
        buffer.put((byte) 3);
        buffer.put((byte) 4);
        buffer.put((byte) 5);
        buffer.put((byte) 6); // 初始化一个写满的buffer

        buffer.flip();
        // position: 0, limit: 6, capacity: 6  -- 切换为读取模式

        buffer.get();
        buffer.get();
        // position: 2, limit: 6, capacity: 6  -- 读取两个字节后，还剩余四个字节

        buffer.compact();
        //将剩余的字节往前移
        // position: 4, limit: 6, capacity: 6  -- 进行压缩之后将从第五个字节开始

        buffer.put((byte) 7);
        // position: 5, limit: 6, capacity: 6  -- 写入一个字节数据的状态
        LOGGER.info("{}",buffer.array());
    }

}
