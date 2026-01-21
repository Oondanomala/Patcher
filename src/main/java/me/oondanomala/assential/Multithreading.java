package me.oondanomala.assential;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public final class Multithreading {
    private static final AtomicInteger threadCounter = new AtomicInteger();
    private static final ExecutorService POOL = Executors.newCachedThreadPool(
        r -> new Thread(r, Assential.modName + "-" + threadCounter.getAndIncrement())
    );

    private Multithreading() {
    }

    public static void runAsync(Runnable runnable) {
        POOL.execute(runnable);
    }

    public static ExecutorService getPool() {
        return POOL;
    }
}
