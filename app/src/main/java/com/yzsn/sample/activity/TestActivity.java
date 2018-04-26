package com.yzsn.sample.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.yzsn.adownlader.ADownloader;
import com.yzsn.adownlader.common.ADRequest;
import com.yzsn.adownlader.error.ADError;
import com.yzsn.adownlader.listener.AnalyticsListener;
import com.yzsn.adownlader.listener.ProgressListener;
import com.yzsn.adownlader.listener.SpeedListener;
import com.yzsn.adownlader.listener.StatuListener;
import com.yzsn.adownlader.util.log.L;
import com.yzsn.sample.R;

/**
 * Created by zc on 2018/4/25.
 */

public class TestActivity extends Activity{
    private static final String TAG = TestActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

    }

    public void startDownload(View view){
        String url = "http://sw.bos.baidu.com/sw-search-sp/software/b4a14b3037808/kugou_8.2.4.20449_setup.exe";
        String fileDir = this.getApplicationContext().getExternalCacheDir().getAbsolutePath();
        String fileName = "kugou_setup.exe";

        ADownloader.prepareDownLoad(url, fileDir, fileName)
                .setTag(TAG)
                .setProgressListener(new TestProgressListener())
                .setAnalyticsListener(new TestAnalyticsListener())
                .setStatuListener(new TestStatuListener())
                .setSpeedListener(new TestSpeedListener())
                .build()
                .start();
    }

    public void cancelDownload(View view){
        ADownloader.cancel(TAG);
    }

    class TestSpeedListener implements SpeedListener{

    }

    class TestStatuListener implements StatuListener{
        @Override
        public void onStart() {
            L.e(true, "");
        }

        @Override
        public void onComplete() {
            L.e(true, "");
        }

        @Override
        public void onError(ADError error) {
            L.e(true, "");
        }
    }

    class TestAnalyticsListener implements AnalyticsListener{
        @Override
        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
            L.e("耗时time = " + timeTakenInMillis + "; 发送字节size = " + bytesSent);
            L.e("收到字节size = " + bytesReceived + "; 是否取自缓存isFromCache = " + isFromCache);
        }
    }

    class TestProgressListener implements ProgressListener{
        @Override
        public void onProgress(long bytesDownloaded, long totalBytes) {
            L.e("已下载字节大小size = " + bytesDownloaded + "; 总大小size = " + totalBytes);
        }
    }
}
