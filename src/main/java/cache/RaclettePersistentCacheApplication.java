package cache;

import cache.api.NettyServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RaclettePersistentCacheApplication {
  private static final Logger LOG = LogManager.getLogger(RaclettePersistentCacheApplication.class);

  public static void main(String[] args) {
    try {
      new NettyServer().run(8090);
    } catch (Exception e) {
      LOG.warn("Server error", e);
    }
  }
}
