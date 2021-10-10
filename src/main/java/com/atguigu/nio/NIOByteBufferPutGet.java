package com.atguigu.nio;

import java.nio.ByteBuffer;

/**
 * buffer put对应基本类型，get基本类型需要一致
 */
public class NIOByteBufferPutGet {

    public static void main(String[] args) {
        ByteBuffer bf = ByteBuffer.allocate(64);

        bf.putInt(10);
        bf.putChar('s');

        System.out.println(bf.getInt());
        System.out.println(bf.getChar());
    }
}
