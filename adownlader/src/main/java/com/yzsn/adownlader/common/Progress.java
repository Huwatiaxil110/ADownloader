package com.yzsn.adownlader.common;

/**
 * Created by zc on 2018/4/25.
 */

public class Progress {
    public long currentBytes;
    public long totalBytes;

    public Progress(long currentBytes, long totalBytes) {
        this.currentBytes = currentBytes;
        this.totalBytes = totalBytes;
    }
}
