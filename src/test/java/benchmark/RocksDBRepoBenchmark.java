package benchmark;

import cache.store.RocksDBRepositoryImpl;
import java.io.File;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@State(Scope.Benchmark)
@Measurement(iterations = 2)
@Warmup(iterations = 2)
@Fork(value = 1, warmups = 1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
public class RocksDBRepoBenchmark {
  private File dbDir;
  private String entry;

  public static void main(String[] args) throws Exception {
    org.openjdk.jmh.Main.main(args);
  }

  @Setup
  public void setup() {
    dbDir = FileUtils.getTempDirectory();
    entry = "Some text information";
  }

  // @Threads(6)
  @Benchmark
  public void testSave() {
    RocksDBRepositoryImpl rocksDBRepository = new RocksDBRepositoryImpl(dbDir.toString(), true);
    rocksDBRepository.save("Key1", ArrayUtils.toObject(entry.getBytes()));
  }
}
