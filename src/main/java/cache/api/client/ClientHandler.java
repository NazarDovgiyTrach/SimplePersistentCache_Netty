package cache.api.client;

import cache.api.inbound.RequestData;
import cache.api.outbound.ResponseData;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientHandler extends ChannelInboundHandlerAdapter {
  private static final Logger LOG = LogManager.getLogger(ClientHandler.class);

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws IOException {

    byte[] bytes = "Roclette".getBytes();
    RequestData data = new RequestData();
    data.setAction("CREATE");
    data.setStringValue("it's roclette key!");
    data.setBinaryValue(bytes);
    ChannelFuture future = ctx.writeAndFlush(data);
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object responseData) throws Exception {
    System.out.println(
        String.format("************%s************", ((ResponseData) responseData).getStatus()));
    ctx.close();
  }
}
