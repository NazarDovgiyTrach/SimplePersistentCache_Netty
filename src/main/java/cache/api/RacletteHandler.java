package cache.api;

import cache.api.inbound.RequestData;
import cache.api.outbound.ResponseData;
import cache.store.KeyValueRepository;
import cache.store.RocksDBRepositoryImpl;
import cache.util.ActionResolver;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class RacletteHandler extends ChannelInboundHandlerAdapter {

  private final String dbDir;
  private final boolean overwriteExisting;

  public RacletteHandler(String dbDir, boolean overwriteExisting) {
    this.dbDir = dbDir;
    this.overwriteExisting = overwriteExisting;
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object data) {
    KeyValueRepository<String, Byte[]> rocksDBRepository =
        new RocksDBRepositoryImpl(dbDir, overwriteExisting);

    RequestData requestData = (RequestData) data;
    ResponseData responseData = ActionResolver.resolve(requestData, rocksDBRepository);

    ChannelFuture future = ctx.writeAndFlush(responseData);
    future.addListener(ChannelFutureListener.CLOSE);
  }
}
