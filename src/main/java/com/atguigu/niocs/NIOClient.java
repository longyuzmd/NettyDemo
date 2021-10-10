package com.atguigu.niocs;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {
    public static void main(String[] args) throws IOException {
        // 获取一个通道
        SocketChannel socketChannel = SocketChannel.open();
        // 设置非阻塞
        socketChannel.configureBlocking(false);
        // 提供连接服务器的ip和端口port 服务器地址的封装
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        // 连接服务器
        if(!socketChannel.connect(inetSocketAddress)){
            while (!socketChannel.finishConnect()){
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其他工作！");
            }
        }

        // 连接成功就发送数据
        String str = "Hello,尚硅谷!";
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());

        // 发送数据，将buffer 数据写入 channel
        socketChannel.write(buffer);
        System.in.read();
    }
}
