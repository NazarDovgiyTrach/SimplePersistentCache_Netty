package cache.api.client;

import cache.api.outbound.ResponseData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import java.util.List;
import org.apache.commons.lang3.SerializationUtils;

public class ResponseDataDecoder extends ReplayingDecoder<ResponseData> {

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    byte[] bytes = new byte[in.readableBytes()];
    in.readBytes(bytes);
    ResponseData data = SerializationUtils.deserialize(bytes);
    out.add(data);
  }
}
