package com.atguigu.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class NIOFileChannel04 {
    public static void main(String[] args) throws Exception {
        FileInputStream fis = new FileInputStream("");
        FileOutputStream fos = new FileOutputStream("");

        // 获取通道
        FileChannel sourceChannel = fis.getChannel();
        FileChannel destChannel = fos.getChannel();

        // 复制
        destChannel.transferFrom(sourceChannel,0,sourceChannel.size());

        fis.close();
        fos.close();
        sourceChannel.close();
        destChannel.close();
    }
}
