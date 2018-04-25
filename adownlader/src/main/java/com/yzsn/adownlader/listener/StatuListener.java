package com.yzsn.adownlader.listener;

import com.yzsn.adownlader.error.ADError;

/**
 * 监听下载状态 -- 开始，结束，出错
 * Created by zc on 2018/4/24.
 */

public interface StatuListener {

    void onStart();
    void onComplete();
    void onError(ADError error);
}
