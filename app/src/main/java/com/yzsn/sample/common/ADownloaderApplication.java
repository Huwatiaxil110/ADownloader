package com.yzsn.sample.common;

import android.app.Application;

import com.yzsn.adownlader.ADownloader;

/**
 * Created by zc on 2018/4/25.
 */

public class ADownloaderApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    private void init(){
        ADownloader.init();
    }
}
