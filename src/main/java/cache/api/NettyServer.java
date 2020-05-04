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
import org.apache.logging.log4j.util.Strings;

public class NettyServer {
  private static final Logger LOG = LogManager.getLogger(NettyServer.class);
  private static final Properties appProps = new Properties();
  private static String dbDir;
  private static boolean overwriteExisting;

  static {
    initializeProperties();
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
                              new RacletteHandler(dbDir, overwriteExisting));
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
  
  private static void initializeProperties() {
    try {
      appProps.load(
          NettyServer.class.getClassLoader().getResourceAsStream("application.properties"));

      String dbDirProperty = appProps.getProperty("rocksDB.database.path");
      String overwriteExistingProperty = appProps.getProperty("rocksDB.overwrite.existing");

      dbDir = Strings.isBlank(dbDirProperty) ? "rocks-db" : dbDirProperty;
      overwriteExisting =
          Strings.isBlank(overwriteExistingProperty)
              || Boolean.getBoolean(overwriteExistingProperty);

    } catch (IOException e) {
      LOG.error("Error reading application properties.");
    }
  }
}
