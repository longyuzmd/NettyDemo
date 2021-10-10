package com.atguigu.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class NettyServer {

    public static void main(String[] args) throws InterruptedException {
        // 创建BossGroup和WorkerGroup
        /**
         * 说明：
         *  1、创建两个线程组 bossGroup 和 workerGroup
         *  2、bossGroup 只是处理连接请求 真正的和客户端业务处理，会交给workerGroup完成
         *  3、两个都是无限循环
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 创建服务器端的启动对象 配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();

            //使用链式编程来进行设置
            bootstrap.group(bossGroup,workerGroup) // 设置两个线程组
                    .channel(NioServerSocketChannel.class) // 设置服务器通道实现
                    .option(ChannelOption.SO_BACKLOG,128) // 线程队列得到连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE,true) // 设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 创建一个通道测试对象 匿名对象
                        // 给pipeline 设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 这里获取每个socketChannel的hashcode,作为用户标识
                            System.out.println("用户唯一标识通道channel hashcode="+ch.hashCode());
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    }); // 给我们的workerGroup 的 EventLoop 对应的管道设置处理器

            System.out.println("......服务器 is ready.....");

            // 绑定一个端口并且同步，并生成一个channelFuture
            // 启动服务器（并绑定端口）
            ChannelFuture cf = bootstrap.bind(6668).sync();

            // future-Listener 异步模型 给channelFuture 增加监听器
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if(channelFuture.isSuccess()){
                        System.out.println("监听6668端口成功！");
                    }else{
                        System.out.println("监听6668端口失败！");
                    }
                }
            });

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
