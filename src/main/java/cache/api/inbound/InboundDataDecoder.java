package cache.api.inbound;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import java.util.List;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InboundDataDecoder extends ReplayingDecoder<ByteBuf> {
  private static final Logger LOG = LogManager.getLogger(InboundDataDecoder.class);

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
    byte[] bytes = new byte[in.readInt()];
    in.readBytes(bytes);
    RequestData data = SerializationUtils.deserialize(bytes);
    LOG.info("Inbound data received and decoded.");
    out.add(data);
  }
}
