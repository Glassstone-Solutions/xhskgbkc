package net.glassstones.thediarymagazine.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Thompson on 31/05/2016.
 * For The Diary Magazine
 */
public class ExecutorUtils {
    private static final int THREAD_POOL = 5;
    private static ExecutorService executor;

    private ExecutorUtils() {
    }

    public static ExecutorService getExecutor() {
        if (executor == null) {
            synchronized (ExecutorUtils.class) {
                if (executor == null)
                    executor = Executors.newFixedThreadPool(THREAD_POOL);
            }
        }
        return executor;
    }
}
