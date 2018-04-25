package com.yzsn.adownlader.listener;

/**
 * Created by zc on 2018/4/25.
 */

public interface AnalyticsListener {

    void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache);
}
