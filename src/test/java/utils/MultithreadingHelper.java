package utils;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultithreadingHelper {
    private static final Logger LOG = LoggerFactory.getLogger(MultithreadingHelper.class);

    /** It allows to start all threads at once and monitor process of execution */
    public static void executeConcurrent(final List<? extends Runnable> threads)
            throws InterruptedException {
        final int numThreads = threads.size();
        final List<Throwable> exceptions = Collections.synchronizedList(new ArrayList<>());

        final CountDownLatch allExecutorThreadsReady = new CountDownLatch(numThreads);
        final CountDownLatch afterInitBlocker = new CountDownLatch(1);
        final CountDownLatch allDone = new CountDownLatch(numThreads);

        for (final Runnable runnable : threads) {
            new Thread(
                    () -> {
                        allExecutorThreadsReady.countDown();
                        try {
                            afterInitBlocker.await();
                            LOG.info("Thread: [{}] start", Thread.currentThread().getName());
                            runnable.run();
                        } catch (Exception e) {
                            exceptions.add(e);
                        } finally {
                            LOG.info("Thread: [{}] finished", Thread.currentThread().getName());
                            allDone.countDown();
                        }
                    })
                    .start();
        }
        allExecutorThreadsReady.await();
        LOG.info("Threads ready");
        afterInitBlocker.countDown();
        allDone.await();
        LOG.info("Threads complete");
        assertTrue(exceptions.isEmpty());
    }
}
