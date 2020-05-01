package cache.store;

import cache.exception.KeyExistsException;
import cache.exception.RepoOpException;
import java.util.Objects;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

/**
 * It's implementation of {@link KeyValueRepository} which allows perform basic Create, Read, Delete
 * operations using RocksDB library https://github.com/facebook/rocksdb/tree/master/java.
 */
public class RocksDBRepositoryImpl implements KeyValueRepository<String, Byte[]> {

  private static final Logger LOG = LogManager.getLogger(RocksDBRepositoryImpl.class);

  private final String dbDir;
  private final RocksDB rocksDB;
  private boolean overwriteExisting;

  public RocksDBRepositoryImpl(String dbDir, boolean overwriteExisting) {
    this.overwriteExisting = overwriteExisting;
    this.dbDir = dbDir;
    this.rocksDB = RocksDBHandler.getInstance().getRocksDB(dbDir);
  }

  @Override
  public void save(String key, Byte[] value) {
    try {
      if (!overwriteExisting && rocksDB.keyMayExist(key.getBytes(), null)) {
        throw new KeyExistsException(
            String.format(
                "Entry with the key: %s already exists, please choose another key or enable 'overwriteExisting' mode ",
                key));
      }
      rocksDB.put(key.getBytes(), ArrayUtils.toPrimitive(value));
      LOG.info("Entry with key:{} successfully saved to RocksDB", key);
    } catch (RocksDBException e) {
      throw new RepoOpException("Error saving entry in RocksDB, error:", e);
    }
  }

  @Override
  public Byte[] find(String key) {
    Byte[] result = null;
    try {
      byte[] bytes = rocksDB.get(key.getBytes());
      if (Objects.nonNull((bytes))) {
        LOG.info("Entry with key: {} found", key);
        result = ArrayUtils.toObject(bytes);
      }
    } catch (RocksDBException e) {
      throw new RepoOpException(
          String.format("Error retrieving the entry in RocksDB from key: %s, error", key), e);
    }
    return result;
  }

  @Override
  public void delete(String key) {
    try {
      rocksDB.delete(key.getBytes());
      LOG.info("Entry with key: {} deleted from RocksDB", key);
    } catch (RocksDBException e) {
      throw new RepoOpException("Error deleting entry in RocksDB, error", e);
    }
  }

  public String getDbDir() {
    return dbDir;
  }
}
