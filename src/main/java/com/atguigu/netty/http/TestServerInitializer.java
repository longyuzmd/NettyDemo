package com.atguigu.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class TestServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 先要获取管道
        ChannelPipeline pipeline = ch.pipeline();

        // 现在是一个http请求，需要往管道里面添加netty
        // 提供针对http的编码解码器 HttpServerCodec
        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());

        // 需要提供自己自定义的handler
        pipeline.addLast("MyTestHttpServerHandler",null);
    }
}
