package cache;

import cache.api.NettyServer;

public class RaclettePersistentCacheApplication {
  public static void main(String[] args) {
    try {
      new NettyServer().run(8089);
    } catch (Exception e) {
      System.out.println("Sraka");
    }
  }
}
