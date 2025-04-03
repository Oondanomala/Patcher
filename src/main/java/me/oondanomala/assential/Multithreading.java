package me.oondanomala.assential;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Multithreading {
    private static final ExecutorService POOL = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat(Assential.modName + "-%d").build());

    private Multithreading() {
    }

    public static void runAsync(Runnable runnable) {
        POOL.execute(runnable);
    }

    public static ExecutorService getPool() {
        return POOL;
    }
}
