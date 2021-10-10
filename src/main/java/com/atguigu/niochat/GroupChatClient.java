package com.atguigu.niochat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * 客户端：
 *  1、连接服务端
 *  2、发送消息
 *  3、接收服务端消息
 */
public class GroupChatClient {
    // 定义相关属性
    private final String Host = "127.0.0.1";
    private final int PORT = 6667;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    public GroupChatClient(){
        // 初始化操作
        try {
            selector = Selector.open();
            socketChannel = socketChannel.open(new InetSocketAddress(Host,PORT));
            // 设置通道为非阻塞
            socketChannel.configureBlocking(false);
            // socketChannel 注册到 selector
            socketChannel.register(selector, SelectionKey.OP_READ);
            username = socketChannel.getLocalAddress().toString().substring(1);
            System.out.println(username + " is OK .....");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 发送消息
    public void send(String msg){
        // 消息内容
        msg = username + "说：" + msg;
        try {
            socketChannel.write(ByteBuffer.wrap(msg.trim().getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 接收消息
    public void read(){
        // 从通道读取消息 创建buffer
        try {
            int readChannels = selector.select();
            if(readChannels > 0){
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext()){
                    SelectionKey key = it.next();
                    if(key.isReadable()){
                        // 得到相关通道
                        SocketChannel sc =(SocketChannel) key.channel();

                        // 创建通道读入数据
                        ByteBuffer byteBuffer = ByteBuffer.allocate(100);
                        // 读取
                        sc.read(byteBuffer);
                        System.out.println(new String(byteBuffer.array(), 0, byteBuffer.position()));
                    }
                }
                it.remove();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        // 启动客户端
        GroupChatClient chatClient = new GroupChatClient();

        // 启动线程 没3秒读取数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    chatClient.read();
                    try {
                        Thread.currentThread().sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String s = scanner.next();
            chatClient.send(s);
        }
    }
}
