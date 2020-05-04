import cache.exception.KeyExistsException;
import cache.store.RocksDBRepositoryImpl;
import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Assert;
import org.junit.Test;
import utils.MultithreadingHelper;

public class RocksDBRepositoryImplTest {
  private static final File DB_DIR = FileUtils.getTempDirectory();
  private static final String ENTRY = "Some text information";

  @Test
  public void testSave() {

    RocksDBRepositoryImpl rocksDBRepository = new RocksDBRepositoryImpl(DB_DIR.toString(), true);
    rocksDBRepository.save("Key1", ArrayUtils.toObject(ENTRY.getBytes()));

    Assert.assertEquals(ENTRY, new String(ArrayUtils.toPrimitive(rocksDBRepository.find("Key1"))));
  }

  @Test(expected = KeyExistsException.class)
  public void testSaveWhenOverwriteExistingModeDisabled() {
    // Disabling overwriteExisting mode
    RocksDBRepositoryImpl rocksDBRepository = new RocksDBRepositoryImpl(DB_DIR.getPath(), false);
    rocksDBRepository.save("Key1", ArrayUtils.toObject(ENTRY.getBytes()));

    // Save new entry with existing key
    rocksDBRepository.save("Key1", ArrayUtils.toObject("New text".getBytes()));

    Assert.assertEquals(
        "The previous entry should not be overwritten, because overwriteExisting mode disabled.",
        ENTRY,
        new String(ArrayUtils.toPrimitive(rocksDBRepository.find("Key1"))));
  }

  @Test
  public void testDelete() {

    RocksDBRepositoryImpl rocksDBRepository = new RocksDBRepositoryImpl(DB_DIR.toString(), true);
    rocksDBRepository.save("Key1", ArrayUtils.toObject(ENTRY.getBytes()));
    rocksDBRepository.delete("Key1");

    Assert.assertNull(rocksDBRepository.find("Key1"));
  }

  // ********************* Multithreading tests *******************************

  @Test
  public void testConcurrentMultithreadedSave() throws InterruptedException {
    List<Runnable> runnables =
        Stream.generate(
                () ->
                    (Runnable)
                        (() -> {
                          RocksDBRepositoryImpl rocksDBRepository =
                              new RocksDBRepositoryImpl(DB_DIR.toString(), true);
                          String key = UUID.randomUUID().toString();
                          String entry = ENTRY + key;
                          rocksDBRepository.save(key, ArrayUtils.toObject(entry.getBytes()));

                          Assert.assertEquals(
                              entry,
                              new String(ArrayUtils.toPrimitive(rocksDBRepository.find(key))));
                        }))
            .limit(100)
            .collect(Collectors.toList());

    MultithreadingHelper.executeConcurrent(runnables);
  }

  @Test
  public void testConcurrentMultithreadedDelete() throws InterruptedException {

    List<Runnable> runnables =
        Stream.generate(
                () ->
                    (Runnable)
                        (() -> {
                          RocksDBRepositoryImpl rocksDBRepository =
                              new RocksDBRepositoryImpl(DB_DIR.toString(), true);
                          String key = UUID.randomUUID().toString();
                          String entry = ENTRY + key;

                          rocksDBRepository.save(key, ArrayUtils.toObject(entry.getBytes()));
                          rocksDBRepository.delete(key);

                          Assert.assertNull(rocksDBRepository.find(key));
                        }))
            .limit(100)
            .collect(Collectors.toList());

    MultithreadingHelper.executeConcurrent(runnables);
  }
}
