package cache.api.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LoggingHandler;

public class NettyClient {
  public static void main(String[] args) throws Exception {
    String host = "localhost";
    int port = 8090;
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    try {
      Bootstrap b = new Bootstrap();
      b.group(workerGroup);
      b.channel(NioSocketChannel.class);
      b.option(ChannelOption.SO_KEEPALIVE, true);
      b.handler(
          new ChannelInitializer<SocketChannel>() {

            @Override
            public void initChannel(SocketChannel ch) throws Exception {
              ch.pipeline()
                  .addFirst(new LoggingHandler())
                  .addLast(
                      new ObjectEncoder(),
                      new ObjectDecoder(
                          Integer.MAX_VALUE,
                          ClassResolvers.softCachingConcurrentResolver(
                              getClass().getClassLoader())),
                      new ClientHandler());
            }
          });

      ChannelFuture f = b.connect(host, port).sync();

      f.channel().closeFuture().sync();
    } finally {
      workerGroup.shutdownGracefully();
    }
  }
}
