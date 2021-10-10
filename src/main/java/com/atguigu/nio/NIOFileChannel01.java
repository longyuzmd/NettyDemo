package com.atguigu.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel01 {

    /**
     * 1、创建流，输出流，输出到指定文件
     *
     * 2、通过输出流获取包装再这个流上面的通道channel
     *
     * 3、创建缓冲区buffer
     *
     * 4、将需要输出的内容放入缓冲区中
     *
     * 5、缓冲区的反转操作，将索引指针放入起始位置
     *
     * 6、数据写入到通道中
     *
     * 7、关闭流相关资源
     */
    public static void main(String[] args) throws Exception {
        String str = "hello,尚硅谷！";

        FileOutputStream fos = new FileOutputStream("hello.txt");

        // 实际是FileChannel实现类FileChannelImpl, 流里面内置了channel
        FileChannel channel = fos.getChannel();

        // 创建ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 将字节内容放入buffer
        byteBuffer.put(str.getBytes());

        // 反转，指针位置反转到起始位置
        byteBuffer.flip();

        // 缓冲区数据写入到通道
        channel.write(byteBuffer);

        channel.close();
    }
}
