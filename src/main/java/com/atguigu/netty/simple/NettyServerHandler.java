package com.atguigu.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 自定义Handler 需要继承 适配器 规范
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     *
     * @param ctx 上下文对象 含有 管道
     * @param msg 客户端发送的数据
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("server ctx="+ctx);
//        // 将msg 转化成ByteBuf
//        // ByteBuf 是netty 提供的，不是NIO ByteBuffer
//        ByteBuf buf = (ByteBuf) msg;
//        System.out.println("客户端发送的消息是："+buf.toString(CharsetUtil.UTF_8));
//        System.out.println("客户端地址："+ctx.channel().remoteAddress());

        // 当服务端处理非常耗时的业务时候，处于阻塞状态
//        Thread.sleep(10*1000);
//        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端我阻塞了~",CharsetUtil.UTF_8));
//        System.out.println("go on.....");

        // 如何解决这种复杂业务阻塞情况  用户程序自定义普通任务 任务队列的方式 taskQueue channel->eventLoop->taskQueue
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端我阻塞了~",CharsetUtil.UTF_8));
            }
        });
        System.out.println("go on.....");

        // 用户程序自定义定时任务
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端我定时阻塞~",CharsetUtil.UTF_8));
            }
        },5, TimeUnit.SECONDS);

    }

    /**
     * 数据读取完毕
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将数据写入到缓冲，并刷新
        // write + flush
        // 发送的数据也是需要编码处理的
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~",CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }


}
