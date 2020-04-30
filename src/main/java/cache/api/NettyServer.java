package cache.api;

import cache.api.inbound.InboundDataDecoder;
import cache.api.outbound.OutboundDataEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
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
      ServerBootstrap b = new ServerBootstrap();
      b.group(bossGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          .childHandler(
              new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                  ch.pipeline()
                      .addLast(
                          new InboundDataDecoder(),
                          new OutboundDataEncoder(),
                          new RacletteHandler(
                              appProps.getProperty("rocksDB.database.path"),
                              Boolean.getBoolean(
                                  appProps.getProperty("rocksDB.overwrite.existing"))));
                }
              })
          .option(ChannelOption.SO_BACKLOG, 128)
          .childOption(ChannelOption.SO_KEEPALIVE, true);

      ChannelFuture f = b.bind(port).sync();
      f.channel().closeFuture().sync();
    } finally {
      workerGroup.shutdownGracefully();
      bossGroup.shutdownGracefully();
    }
  }
}
