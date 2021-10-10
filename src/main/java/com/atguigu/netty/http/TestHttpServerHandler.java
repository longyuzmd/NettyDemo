package com.atguigu.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;
import java.nio.charset.Charset;

/**
 * 说明
 * 1.SimpleChannelInboundHandler是ChannelInboundHandlerAdapter
 * 2.HttpObject客户端和服务器端相互通讯的数据被封装成HttpObject
 * */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    // 读取客户端信息
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        // 判断是否是httpRequest 请求
        if(msg instanceof HttpRequest){

            System.out.println("pipeline hashcode"+ctx.pipeline().hashCode()
                    +"TestHttpServerHandler hash="+this.hashCode());

            System.out.println("msg类型="+msg.getClass());
            System.out.println("客户端地址"+ctx.channel().remoteAddress());

            // 处理获取网站图片的请求屏蔽问题
            HttpRequest httpRequest=(HttpRequest)msg;

            URI uri=new URI(httpRequest.uri());
            if("/favicon.ico".equals(uri.getPath())){
                System.out.println("请求了favicon.ico,不做响应");
                return;
            }

            // 回复浏览器
            ByteBuf content = Unpooled.copiedBuffer("hello,我是服务端~", CharsetUtil.UTF_8);

            // 构建HttpResponse 对象
            DefaultHttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);

            // 设置response header 信息
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain"); // 文本类型的内容
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());

            // 返回给客户端
            ctx.writeAndFlush(response);
        }
    }
}
