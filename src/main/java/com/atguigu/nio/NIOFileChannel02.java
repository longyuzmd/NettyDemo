package com.atguigu.nio;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel02 {

    public static void main(String[] args) throws IOException {
        // 创建File
        File file = new File("hello.txt");

        // 创建文件的输入流，读入数据
        FileInputStream fis = new FileInputStream(file);

        // 获取输入流中内置的channel通道
        FileChannel channel = fis.getChannel();

        // 创建ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate((int)file.length());

        // 通道读出到数据放入Buffer
        channel.read(byteBuffer);

        System.out.println(new String(byteBuffer.array()));

    }
}
