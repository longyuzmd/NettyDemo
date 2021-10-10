package com.atguigu.nio;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * MapperByteBuffer
 * 可以让文件直接再内存（堆外内存）中修改，
 * 操作系统不需要拷贝一次
 */
public class MapperByteBufferTest {
    public static void main(String[] args) throws Exception {
        RandomAccessFile rw = new RandomAccessFile("1.txt", "rw");

        //获取通道
        FileChannel channel = rw.getChannel();

        /**
         * 1 读写模式
         * 2 修改的起始位置
         * 3 映射到内存的大小 5个字节
         * 将文件的多少个字节映射到内存
         * 可以直接修改的范围就是 0~5
         * 不能修改索引是5位置，5是大小
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        mappedByteBuffer.put(0,(byte)'H');
        mappedByteBuffer.put(3,(byte)'9');

        rw.close();
        System.out.println("修改成功！");

    }
}
