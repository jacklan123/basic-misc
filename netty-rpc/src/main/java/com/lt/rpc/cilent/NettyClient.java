package com.lt.rpc.cilent;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.ReferenceCountUtil;

/**
 * @author lantian
 */
public class NettyClient {

    private ChannelHandlerContext ctx;

    private MessageCallback messageCallback;

    public NettyClient(String ip, Integer port) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ByteBuf delimiter = Unpooled.copiedBuffer("$$".getBytes());
                        // 设置按照分隔符“&&”来切分消息，单条消息限制为 1MB
                        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024 * 1024, delimiter));
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new NettyClientHandler());
                    }
                });
            ChannelFuture sync = b.connect(ip, port).sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMessageCallback(MessageCallback callback) {
        this.messageCallback = callback;
    }

    public ChannelHandlerContext getCtx() throws InterruptedException {
        System.out.println("等待连接成功...");
        if (ctx == null) {
            synchronized (this) {
                wait();
            }
        }
        return ctx;
    }




    private class NettyClientHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            try {
                String message = (String) msg;
                if (messageCallback != null) {
                    messageCallback.onMessage(message);
                }
            } finally {
                ReferenceCountUtil.release(msg);
            }
        }
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            NettyClient.this.ctx = ctx;
            System.out.println("连接成功：" + ctx);
            synchronized (NettyClient.this) {
                NettyClient.this.notifyAll();
            }
        }
        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
        }
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
        }
    }


    public interface MessageCallback {
        /**
         * 服务器返回数据之后
         * @param message
         */
        void onMessage(String message);
    }


}