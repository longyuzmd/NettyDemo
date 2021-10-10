package com.atguigu.niochat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 服务端的步骤：
 * 1、服务端启动并监听6667
 * 2、服务器接受客户端信息，并实时转发到其他客户端（处理上线和离线）
 */
public class GroupChatServer {
    // 定义属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private final int PORT = 6667;


    // 构造器
    public GroupChatServer(){
        // 初始化操作
        try {
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(PORT));  // 监听这个端口6667
            listenChannel.configureBlocking(false); // 设置成非阻塞
            // 将监听通道注册到selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 监听
    public void listen(){
        try {
            while (true) { //循环监听
                int count = selector.select();
                if (count > 0) {
                    // 有通道发生事件，获取发生事件的通道对应SelectionKey
                    Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                    while (it.hasNext()) {
                        // 拿到对应key,
                        SelectionKey key = it.next();
                        // 判断这个key对应的事件
                        if (key.isAcceptable()) {
                            // 表示是接收事件
                            SocketChannel sc = listenChannel.accept();
                            // socketChannel通道设置成非阻塞状态
                            sc.configureBlocking(false);
                            // 通道需要注册到selector选择器中
                            sc.register(selector, SelectionKey.OP_READ);
                            // 处理上线提醒
                            System.out.println(sc.getRemoteAddress() + "上线了！");
                        }
                        if (key.isReadable()) {
                            // 读数据，并转发到其他客户端，还要处理离线
                            readData(key);
                        }

                        // 避免重复操作 删除当前的key
                        it.remove();
                    }

                } else {
                    // 不做处理 没有实际意义
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

    // 通道事件读事件 读取数据
    public void readData(SelectionKey key){
        // 获取对应key 关联的通道
        SocketChannel channel =(SocketChannel)key.channel();
        // 没有绑定对应的buffer 不能 attachment()获取buffer
        ByteBuffer buffer = ByteBuffer.allocate(100);
        // 将通道数据读到buffer
        try {
            int count = channel.read(buffer);
            if(count > 0){
                // 获取信息
                String msg = new String(buffer.array(),0,buffer.position());
                System.out.println("from 客户端" + msg);
                forwardOtherClient(msg,channel);
            }
        } catch (IOException e) {
            // 当前客户端离线了
            try {
                System.out.println(channel.getRemoteAddress()+"离线了！");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    public void forwardOtherClient(String msg,SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中.......");
        // 获取所有注册到selector 中的通道
        Set<SelectionKey> keys = selector.keys();
        for(SelectionKey key:keys){
            Channel channel = key.channel();
            if(channel instanceof SocketChannel && channel != self){

                SocketChannel socketChannel = (SocketChannel)channel;

                socketChannel.write(ByteBuffer.wrap(msg.trim().getBytes()));
            }
        }
    }

    public static void main(String[] args) {
        // 启动
        GroupChatServer chatServer = new GroupChatServer();
        chatServer.listen();
    }
}
