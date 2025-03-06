package me.oondanomala.essential;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Multithreading {
    private static final ExecutorService POOL = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat(Essential.modName + "-%d").build());

    private Multithreading() {
    }

    public static void runAsync(Runnable runnable) {
        POOL.submit(runnable);
    }

    public static ExecutorService getPool() {
        return POOL;
    }
}
