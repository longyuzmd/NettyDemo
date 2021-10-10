package com.atguigu.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Scattering: 将数据写入到buffer时，可以采用buffer数组，依次写入
 * Gathering: 从buffer读取数据时，可以采用buffer数组，依次读
 */
public class ScatteringAndGatheringTest {

    public static void main(String[] args) throws IOException {

        // ServerSocketChannel 和 socketChannel TCP
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        //绑定端口到socket,并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        // 创建buffer数据
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        // 等客户端连接
        SocketChannel socketChannel = serverSocketChannel.accept();
        int messageMaxLength = 8; // 客户端最大字节数8
        // 循环读取
        while(true){

            int byteRead = 0;

            while (byteRead < messageMaxLength){
                long l = socketChannel.read(byteBuffers);
                byteRead += l;
                System.out.println("byteRead="+byteRead);
                Arrays.asList(byteBuffers).stream().map(buffer -> "position="+buffer.position()+",limit="+
                        buffer.limit()).forEach(System.out::println);
            }

            // 将所有的buffer进行反转
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.flip());

            // 将数据读出显示到客户端
            long byteWrite = 0;
            while (byteWrite < messageMaxLength){
                long l = socketChannel.write(byteBuffers);
                byteWrite += l;
            }

            // 将所有buffer clear
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.clear());

            System.out.println("byteRead="+byteRead + ",byteWrite="+byteWrite);
        }

    }
}
