package com.atguigu.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel03 {

    // 文件的拷贝功能实现
    public static void main(String[] args) throws Exception {
        File file = new File("hello.txt");

        FileInputStream fis = new FileInputStream(file);

        FileChannel channel = fis.getChannel();

        // 创建目标文件
        File destFile = new File("hello1.txt");

        FileOutputStream fos = new FileOutputStream(destFile);

        FileChannel channelOut = fos.getChannel();

//        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
//        channel.read(byteBuffer);
//        byteBuffer.flip();
//        channelOut.write(byteBuffer);

        ByteBuffer byteBuffer = ByteBuffer.allocate(5);
        while(true){
            // 复位操作
            byteBuffer.clear();
            // 循环读取数据
            int read = channel.read(byteBuffer);
            if(read == -1){
                break;
            }
            // 反转,指针位置position = 0
            byteBuffer.flip();
            channelOut.write(byteBuffer);
        }

        channel.close();
        channelOut.close();

    }
}
