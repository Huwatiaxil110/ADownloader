package com.yzsn.adownlader.core;

import com.yzsn.adownlader.listener.ExecutorSupplier;

/**
 * Created by zc on 2018/4/24.
 */
public class ExecutorCore {
    private static ExecutorCore sInstance = null;
    private final ExecutorSupplier mExecutorSupplier;

    private ExecutorCore() {
        this.mExecutorSupplier = new DefaultExecutorSupplier();
    }

    public static ExecutorCore getInstance() {
        if (sInstance == null) {
            synchronized (ExecutorCore.class) {
                if (sInstance == null) {
                    sInstance = new ExecutorCore();
                }
            }
        }
        return sInstance;
    }

    public ExecutorSupplier getExecutorSupplier() {
        return mExecutorSupplier;
    }

    public static void shutDown() {
        if (sInstance != null) {
            sInstance = null;
        }
    }

}
