package com.yzsn.adownlader.core;

import android.os.Process;

import com.androidnetworking.core.PriorityThreadFactory;
import com.yzsn.adownlader.listener.ExecutorSupplier;

import java.util.concurrent.ThreadFactory;

/**
 * Created by zc on 2018/4/24.
 */

public class DefaultExecutorSupplier implements ExecutorSupplier {
    private static final int DEFAULT_MAX_NUM_THREADS = 2 * Runtime.getRuntime().availableProcessors() + 1;
    private ADExecutor downloadTask;
    private ADExecutor immedicateDownloadTask;

    public DefaultExecutorSupplier() {
        ThreadFactory threadFactory = new PriorityThreadFactory(Process.THREAD_PRIORITY_BACKGROUND);
        downloadTask = new ADExecutor(DEFAULT_MAX_NUM_THREADS, threadFactory);
        immedicateDownloadTask = new ADExecutor(2, threadFactory);
    }

    @Override
    public ADExecutor forDownloadTask() {
        return downloadTask;
    }

    @Override
    public ADExecutor forImmediateDownloadTask() {
        return immedicateDownloadTask;
    }

}
