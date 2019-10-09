package com.lt.rpc.server;

import com.lt.rpc.handler.RpcInvokeHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.util.List;
import java.util.Map;

/**
 * @author lantian
 */
public class NettyServer {

    /**
     * 负责调用方法的handler
     */
    private RpcInvokeHandler rpcInvokeHandler;

    public NettyServer(List serverConfigs, Map interfaceMethods) throws InterruptedException {
        this.rpcInvokeHandler = new RpcInvokeHandler(serverConfigs, interfaceMethods);
    }

    public int init(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChannelInitializer(){
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        ByteBuf delimiter = Unpooled.copiedBuffer("$$".getBytes());
                        // 设置按照分隔符“&&”来切分消息，单条消息限制为 1MB
                        channel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024 * 1024, delimiter));
                        channel.pipeline().addLast(new StringDecoder());
                        channel.pipeline().addLast(rpcInvokeHandler);
                    }
                });
        ChannelFuture sync = b.bind(port).sync();
        System.out.println("启动NettyService，端口为：" + port);
        return port;
    }
}