package com.yzsn.adownlader.util;

import android.content.Context;

import com.yzsn.adownlader.common.ADConstants;
import com.yzsn.adownlader.common.ADRequest;
import com.yzsn.adownlader.core.ExecutorCenter;
import com.yzsn.adownlader.error.ADError;
import com.yzsn.adownlader.listener.AnalyticsListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;

import okhttp3.Cache;
import okhttp3.Response;

/**
 * Created by zc on 2018/4/18.
 */

public class AssistUtil {

    public static File getDiskCacheDir(Context context, String uniqueName) {
        return new File(context.getCacheDir(), uniqueName);
    }

    public static Cache getCache(Context context, int maxCacheSize, String uniqueName) {
        return new Cache(getDiskCacheDir(context, uniqueName), maxCacheSize);
    }

    public static String getMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }


    public static void saveFile(Response response, String dirPath, String fileName) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, fileName);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void sendAnalytics(final AnalyticsListener analyticsListener, 
                  final long timeTakenInMillis, final long bytesSent, final long bytesReceived, final boolean isFromCache) {
        ExecutorCenter.getInstance().getExecutorSupplier().forUIDownloadTask().execute(new Runnable() {
            @Override
            public void run() {
                if (analyticsListener != null) {
                    analyticsListener.onReceived(timeTakenInMillis, bytesSent, bytesReceived, isFromCache);
                }
            }
        });
    }

    public static ADError getErrorForConnection(ADError error) {
        error.setErrorDetail(ADConstants.ERROR_CONNECTION);
        error.setErrorCode(0);
        return error;
    }


    public static ADError getErrorForServerResponse(ADError error, ADRequest request, int code) {
        error = request.parseNetworkError(error);
        error.setErrorCode(code);
        error.setErrorDetail(ADConstants.ERROR_RESPONSE_FROM_SERVER);
        return error;
    }

    public static ADError getErrorForParse(ADError error) {
        error.setErrorCode(0);
        error.setErrorDetail(ADConstants.ERROR_PARSE);
        return error;
    }
}




