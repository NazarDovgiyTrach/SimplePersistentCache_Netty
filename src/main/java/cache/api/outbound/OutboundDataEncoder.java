package cache.api.outbound;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.commons.lang3.SerializationUtils;

public class OutboundDataEncoder extends MessageToByteEncoder<ResponseData> {
  @Override
  protected void encode(ChannelHandlerContext ctx, ResponseData msg, ByteBuf out) throws Exception {
    out.writeBytes(SerializationUtils.serialize(msg));
  }
}
