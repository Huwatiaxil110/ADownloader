package com.yzsn.adownlader.core;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.yzsn.adownlader.common.ADConstants;
import com.yzsn.adownlader.common.Progress;
import com.yzsn.adownlader.listener.ProgressListener;

/**
 * Created by zc on 2018/4/25.
 */

public class ProgressHandler extends Handler {

    private final ProgressListener mDownloadProgressListener;

    public ProgressHandler(ProgressListener downloadProgressListener) {
        super(Looper.getMainLooper());
        mDownloadProgressListener = downloadProgressListener;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case ADConstants.UPDATE:
                if (mDownloadProgressListener != null) {
                    final Progress progress = (Progress) msg.obj;
                    mDownloadProgressListener.onProgress(progress.currentBytes, progress.totalBytes);
                }
                break;
            default:
                super.handleMessage(msg);
                break;
        }
    }
}
