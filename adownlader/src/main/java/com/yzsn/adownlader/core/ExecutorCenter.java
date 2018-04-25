package com.yzsn.adownlader.core;

import com.yzsn.adownlader.listener.ExecutorSupplier;

/**
 * Created by zc on 2018/4/24.
 */
public class ExecutorCenter {
    private static ExecutorCenter sInstance = null;
    private final ExecutorSupplier mExecutorSupplier;

    private ExecutorCenter() {
        this.mExecutorSupplier = new DefaultExecutorSupplier();
    }

    public static ExecutorCenter getInstance() {
        if (sInstance == null) {
            synchronized (ExecutorCenter.class) {
                if (sInstance == null) {
                    sInstance = new ExecutorCenter();
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
