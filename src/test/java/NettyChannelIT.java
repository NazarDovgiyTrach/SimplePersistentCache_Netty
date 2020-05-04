import org.junit.Assert;
import org.junit.Test;

import cache.api.RacletteHandler;
import cache.api.inbound.Action;
import cache.api.inbound.RequestData;
import cache.store.RocksDBHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class NettyChannelIT {
  private final String DB_DIR = "test-rocks-db";

  @Test
  public void test() {
    EmbeddedChannel embeddedChannel = new EmbeddedChannel();

    embeddedChannel
        .pipeline()
        .addLast(
            new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)),
            new ObjectEncoder(),
            new RacletteHandler(DB_DIR, true));

    RequestData msg = new RequestData();
    msg.setAction(Action.CREATE);
    msg.setStringValue("key1");
    msg.setBinaryValue("It's roclette".getBytes());

    // Encodes RequestData to ByteBuf
    embeddedChannel.writeOutbound(msg);
    ByteBuf encodedRequestData = embeddedChannel.readOutbound();

    embeddedChannel.writeInbound(encodedRequestData);

    boolean rocletteExists =
        RocksDBHandler.getInstance().getRocksDB(DB_DIR).keyMayExist("key1".getBytes(), null);
    Assert.assertTrue(rocletteExists);
  }
}
