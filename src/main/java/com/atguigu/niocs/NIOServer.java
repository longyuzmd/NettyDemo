package com.atguigu.niocs;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws IOException {
        // 创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 得到一个Selector对象
        Selector selector = Selector.open();

        // 绑定端口6666 服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        // 设置通道为非阻塞
        serverSocketChannel.configureBlocking(false);

        // 把serverSocketChannel 注册到 selector 关心的事件 OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 循环等待客户端连接
        while(true){
            // 等待一秒，如果没有事件发生，返回
            if(selector.select(1000) == 0){
                // 1s 没有任何通道事件发生
                System.out.println("服务器等待了1s,无连接！");
                continue;
            }

            // 如果有事件发生 获取相关的selectionKey集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            // 通过key 获取通道 迭代器
            Iterator<SelectionKey> it = selectionKeys.iterator();
            while (it.hasNext()){
                // 获取key
                SelectionKey key = it.next();
                // 根据key 对应通道发生的事件
                if(key.isAcceptable()){
                    // 有客户端连接了 生成一个socketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();

                    System.out.println("客户端连接成功,生成一个socketChannel"+socketChannel.hashCode());

                    // 设置通道非阻塞
                    socketChannel.configureBlocking(false);

                    // 将当前的socketChannel 注册到 selector
                    // 关注的事件是Read,将通道数据读到buffer
                    // 关联一个buffer
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                }
                if (key.isReadable()){
                    // 发生了读事件 key 获取 channel
                    SocketChannel channel =(SocketChannel) key.channel();
                    // 获取channel 关联的buffer
                    ByteBuffer buffer =(ByteBuffer) key.attachment();
                    // 将通道数据读到buffer
                    channel.read(buffer);
                    System.out.println("from 客户端 "+ new String(buffer.array(),0,buffer.position()));
                }

                // 手动从集合中移除当前的selectionKey,防止重复操作
                it.remove();

            }
        }
    }
}
