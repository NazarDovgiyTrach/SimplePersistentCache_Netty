import static org.junit.Assert.assertEquals;

import cache.api.RacletteHandler;
import cache.api.inbound.RequestData;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import java.nio.charset.StandardCharsets;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

public class NettyTest {
 /* @Test
  public void test() {
     EmbeddedChannel embeddedChannel =
          new EmbeddedChannel(
                  new ObjectDecoder(ClassResolvers.cacheDisabled(getClass().getClassLoader())),
                  new ObjectEncoder(),
                  new RacletteHandler(dbDir, overwrite));

  RequestData msg = new RequestData();
    msg.setStringValue("It WORKS!!!");

    embeddedChannel.writeInbound(Unpooled.copiedBuffer(SerializationUtils.serialize(msg)));
  Object o = embeddedChannel.readInbound();

    System.out.println(embeddedChannel);
}*/


}
