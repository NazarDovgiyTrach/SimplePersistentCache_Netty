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
