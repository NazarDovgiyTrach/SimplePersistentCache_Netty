package cache.api.client;

import cache.api.inbound.RequestData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.commons.lang3.SerializationUtils;

public class RequestDataEncoder extends MessageToByteEncoder<RequestData> {

  @Override
  protected void encode(ChannelHandlerContext ctx, RequestData data, ByteBuf out) {
    byte[] serialize = SerializationUtils.serialize(data);
    out.writeInt(serialize.length);
    out.writeBytes(serialize);
  }
}
