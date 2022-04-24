package com.lezenford.netty.simple.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.StandardCharsets;

public class Server {
    private final int port;

    public static void main(String[] args) throws InterruptedException {
        new Server(9000).start();
    }

    public Server(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {
        //ThreadPool отвечающий за инициализацию новых подключений
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //ThreadPool обслуживающий всех активных клиентов
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup, workerGroup);
            server.channel(NioServerSocketChannel.class);
            server.childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) {
                    ch.pipeline().addLast(
                            new ChannelInboundHandlerAdapter() {
                                private String message = "";
                                private StringBuffer mesBuf = new StringBuffer();

                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                    System.out.println("channelRead");
                                    ByteBuf m = (ByteBuf) msg;

                                    for (int i = m.readerIndex(); i < m.writerIndex(); i++) {
                                        mesBuf.append(((char) m.getByte(i)));
                                    }
                                    if (mesBuf.indexOf("\n") > 0) {
                                        m = Unpooled.wrappedBuffer((mesBuf.toString()).getBytes(StandardCharsets.UTF_8));
                                            ctx.writeAndFlush(m);
                                        mesBuf.delete(0,mesBuf.length());
                                    }
                                }

                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                                    System.out.println("Cause exception");
                                    cause.printStackTrace();
                                    ctx.close(); // инициируем отключение клиента
                                }
                            }

                    );
                }
            });
            server.option(ChannelOption.SO_BACKLOG, 128);
            server.childOption(ChannelOption.SO_KEEPALIVE, true);// используем серверную версию сокета

            Channel channel = server.bind(port).sync().channel();

            System.out.println("Server started");
            channel.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
