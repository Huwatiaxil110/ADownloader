package com.yzsn.adownlader.listener;

import okhttp3.Response;

/**
 * Created by zc on 2018/4/26.
 */

public interface ParseFileNameListener {

    String getFileNameFromHeader(String contentDisposition);
}
