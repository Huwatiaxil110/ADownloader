package com.yzsn.adownlader.listener;

import com.yzsn.adownlader.core.ADExecutor;

/**
 * Created by zc on 2018/4/24.
 */

public interface ExecutorSupplier {

    ADExecutor forDownloadTask();

    ADExecutor forImmediateDownloadTask();

}
