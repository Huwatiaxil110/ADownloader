package com.yzsn.adownlader.common;

import com.yzsn.adownlader.core.ADRequestQueue;
import com.yzsn.adownlader.core.ExecutorCenter;
import com.yzsn.adownlader.error.ADError;
import com.yzsn.adownlader.listener.AnalyticsListener;
import com.yzsn.adownlader.listener.ProgressListener;
import com.yzsn.adownlader.listener.SpeedListener;
import com.yzsn.adownlader.listener.StatuListener;
import com.yzsn.adownlader.util.log.L;

import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okio.Okio;

/**
 * 下载请求类
 * Created by zc on 2018/4/20.
 */

public class ADRequest {
    private String url;
    private String fileDir;
    private String fileName;
    private ArrayList<String> fileList;
    private Object tag;
    private Priority priority;

    private boolean ignoreSSL;
    private String userAgent;
    private OkHttpClient okHttpClient;
    private URLConnection urlConnection;
    private HashMap<String, List<String>> headersMap;
    private CacheControl mCacheControl;

    private AnalyticsListener analyticsListener;
    private StatuListener statuListener;
    private SpeedListener speedListener;
    private ProgressListener progressListener;

    private Executor execute;

    private boolean isCancelled;
    private int sequenceNumber;
    private boolean isRunning;
    private boolean isDelivered;

    private Future future;
    private Call call;

    public ADRequest(Builder builder) {
        this.url = builder.url;
        this.fileDir = builder.fileDir;
        this.fileName = builder.fileName;
        this.fileList = builder.fileList;
        this.tag = builder.tag;
        this.priority = builder.priority;

        this.ignoreSSL = builder.ignoreSSL;
        this.userAgent = builder.userAgent;
        this.okHttpClient = builder.okHttpClient;
        this.urlConnection = builder.urlConnection;
        this.headersMap = builder.headersMap;
        this.mCacheControl = builder.cacheControl;

        this.analyticsListener = builder.analyticsListener;
        this.statuListener = builder.statuListener;
        this.speedListener = builder.speedListener;
        this.progressListener = builder.progressListener;

        this.execute = builder.executor;
    }

    public void start(){
        ADRequestQueue.getInstance().addRequest(this);
    }

    public void cancel(boolean isForceCancel){
        isCancelled = true;
        isRunning = false;
        
        if(call != null){
            call.cancel();
        }
        if(future != null){
            future.cancel(true);
        }
        if(!isDelivered){
            deliverError(new ADError());
        }
    }

    public boolean isCanceled(){
        return isCancelled;
    }

    public boolean isRunning(){
        return isRunning;
    }

    public void destroy() {
        analyticsListener = null;
        statuListener = null;
        speedListener = null;
        progressListener = null;
    }

    public ADError parseNetworkError(ADError error) {
        try {
            if (error.getResponse() != null && error.getResponse().body() != null
                    && error.getResponse().body().source() != null) {
                error.setErrorBody(Okio.buffer(error.getResponse().body().source()).readUtf8());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return error;
    }

    public void downloadCompleted() {
        L.e(true, "");

        isDelivered = true;
        if (statuListener != null) {
            if (!isCancelled) {
                if (execute != null) {
                    execute.execute(new Runnable() {
                        @Override
                        public void run() {
                            if (statuListener != null) {
                                statuListener.onComplete();
                            }
                            finish();
                        }
                    });
                } else {
                    ExecutorCenter.getInstance().getExecutorSupplier().forUIDownloadTask().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (statuListener != null) {
                                statuListener.onComplete();
                            }
                            finish();
                        }
                    });
                }
            } else {
                deliverError(new ADError());
                finish();
            }
        } else {
            finish();
        }
    }

    public void deliverError(ADError error){
        if (!isDelivered) {
            if (isCancelled) {
                error.setCancellationMessageInError();
                error.setErrorCode(0);
            }
            statuListener.onError(error);
        }
        isDelivered = true;
    }

    public void finish() {
        destroy();
        ADRequestQueue.getInstance().finish(this);
    }

    public String getUrl() {
        return url;
    }

    public String getFileDir() {
        return fileDir;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isIgnoreSSL() {
        return ignoreSSL;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public CacheControl getCacheControl() {
        return mCacheControl;
    }

    public Headers getHeaders() {
        Headers.Builder builder = new Headers.Builder();
        try {
            if (headersMap != null) {
                Set<Map.Entry<String, List<String>>> entries = headersMap.entrySet();
                for (Map.Entry<String, List<String>> entry : entries) {
                    String name = entry.getKey();
                    List<String> list = entry.getValue();
                    if (list != null) {
                        for (String value : list) {
                            builder.add(name, value);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.build();
    }

    public Object getTag() {
        return tag;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setSequenceNumber(int sequenceNumber){
        this.sequenceNumber = sequenceNumber;
    }
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setFuture(Future future) {
        this.future = future;
    }

    public AnalyticsListener getAnalyticsListener() {
        return analyticsListener;
    }

    public StatuListener getStatuListener() {
        return statuListener;
    }

    public SpeedListener getSpeedListener() {
        return speedListener;
    }

    public ProgressListener getProgressListener() {
        return progressListener;
    }

    public void setCall(Call call) {
        this.call = call;
    }

    public Call getCall() {
        return call;
    }

    public static class Builder{
        private String url;
        private String fileDir;
        private String fileName;
        private ArrayList<String> fileList;
        private Object tag;
        private Priority priority = Priority.MEDIUM;

        private boolean ignoreSSL;
        private String userAgent;
        private OkHttpClient okHttpClient;
        private URLConnection urlConnection;
        private HashMap<String, List<String>> headersMap;
        private CacheControl cacheControl;

        private AnalyticsListener analyticsListener;
        private StatuListener statuListener;
        private SpeedListener speedListener;
        private ProgressListener progressListener;

        private Executor executor;

        public Builder(String url, String fileDir, String fileName) {
            this.url = url;
            this.fileDir = fileDir;
            this.fileName = fileName;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setFileDir(String fileDir) {
            this.fileDir = fileDir;
            return this;
        }

        public Builder setFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder setFileList(ArrayList<String> fileList) {
            this.fileList = fileList;
            return this;
        }
        public Builder addFileName(String fileName){
            if(fileList == null){
                fileList = new ArrayList<>();
            }
            fileList.add(fileName);
            return this;
        }

        public Builder setTag(Object tag) {
            this.tag = tag;
            return this;
        }

        public Builder setPriority(Priority priority) {
            this.priority = priority;
            return this;
        }

        public Builder setIgnoreSSL(boolean ignoreSSL) {
            this.ignoreSSL = ignoreSSL;
            return this;
        }

        public Builder setUserAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public Builder setOkHttpClient(OkHttpClient okHttpClient) {
            this.okHttpClient = okHttpClient;
            return this;
        }

        public Builder setUrlConnection(URLConnection urlConnection) {
            this.urlConnection = urlConnection;
            return this;
        }

        public Builder setHeadersMap(HashMap<String, List<String>> headersMap) {
            this.headersMap = headersMap;
            return this;
        }

        public Builder addHeader(String key, String value){
            List<String> list = headersMap.get(key);
            if (list == null) {
                list = new ArrayList<>();
                headersMap.put(key, list);
            }
            if (!list.contains(value)) {
                list.add(value);
            }
            return this;
        }

        public Builder addHeaders(Map<String, String> headerMap) {
            if (headerMap != null) {
                for (HashMap.Entry<String, String> entry : headerMap.entrySet()) {
                    addHeader(entry.getKey(), entry.getValue());
                }
            }
            return this;
        }

        public Builder setCacheControl(CacheControl mCacheControl) {
            this.cacheControl = mCacheControl;
            return this;
        }

        public Builder setAnalyticsListener(AnalyticsListener analyticsListener) {
            this.analyticsListener = analyticsListener;
            return this;
        }

        public Builder setStatuListener(StatuListener statuListener) {
            this.statuListener = statuListener;
            return this;
        }

        public Builder setSpeedListener(SpeedListener speedListener) {
            this.speedListener = speedListener;
            return this;
        }

        public Builder setProgressListener(ProgressListener progressListener) {
            this.progressListener = progressListener;
            return this;
        }

        public Builder setExecutor(Executor mExecutor) {
            this.executor = mExecutor;
            return this;
        }

        public ADRequest build(){
            return new ADRequest(this);
        }
    }
}
