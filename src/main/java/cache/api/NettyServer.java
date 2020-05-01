package cache.api;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LoggingHandler;
import java.io.IOException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NettyServer {
  private static final Logger LOG = LogManager.getLogger(NettyServer.class);
  private static final Properties appProps = new Properties();

  static {
    try {
      appProps.load(
          NettyServer.class.getClassLoader().getResourceAsStream("application.properties"));
    } catch (IOException e) {
      LOG.error("Error reading application properties.");
    }
  }

  public void run(int port) throws Exception {
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap serverBootstrap =
          new ServerBootstrap()
              .group(bossGroup, workerGroup)
              .channel(NioServerSocketChannel.class)
              .childHandler(
                  new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                      ch.pipeline()
                          .addFirst(new LoggingHandler())
                          .addLast(
                              new ObjectDecoder(
                                  Integer.MAX_VALUE,
                                  ClassResolvers.softCachingConcurrentResolver(
                                      getClass().getClassLoader())),
                              new ObjectEncoder(),
                              new RacletteHandler(
                                  appProps.getProperty("rocksDB.database.path"),
                                  Boolean.getBoolean(
                                      appProps.getProperty("rocksDB.overwrite.existing"))));
                    }
                  })
              .option(ChannelOption.SO_BACKLOG, 128)
              .childOption(ChannelOption.SO_KEEPALIVE, true);

      ChannelFuture f = serverBootstrap.bind(port).sync();
      f.channel().closeFuture().sync();
    } finally {
      workerGroup.shutdownGracefully();
      bossGroup.shutdownGracefully();
    }
  }
}
