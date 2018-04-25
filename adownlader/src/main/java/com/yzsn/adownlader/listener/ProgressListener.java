package com.yzsn.adownlader.listener;

/**
 * 监听下载进度
 * Created by zc on 2018/4/24.
 */

public interface ProgressListener {
    void onProgress(long bytesDownloaded, long totalBytes);
}
