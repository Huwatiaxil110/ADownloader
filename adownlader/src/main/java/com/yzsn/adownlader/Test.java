package com.yzsn.adownlader;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;

/**
 * Created by Administrator on 2018/4/20.
 */

public class Test {

    private void hello(String url, String dirPath, String fileName){

        AndroidNetworking.download(url,dirPath,fileName)
                .setTag("downloadTest")
                .setPriority(Priority.MEDIUM)
                .build()
                .setDownloadProgressListener(new DownloadProgressListener() {
                    @Override
                    public void onProgress(long bytesDownloaded, long totalBytes) {
                        // do anything with progress
                    }
                })
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        // do anything after completion
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });

    }
}
