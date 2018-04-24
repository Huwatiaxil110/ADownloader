package com.yzsn.adownlader.common;

import com.yzsn.adownlader.listener.ProcessListener;
import com.yzsn.adownlader.listener.SpeedListener;
import com.yzsn.adownlader.listener.StatuListener;

import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Future;

import okhttp3.OkHttpClient;

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
    private HashMap<String, String> headersMap;

    private StatuListener statuListener;
    private SpeedListener speedListener;
    private ProcessListener processListener;

    private boolean isCancelled;
    private int sequenceNumber;
    private boolean isRunning;

    private Future future;

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

        this.statuListener = builder.statuListener;
        this.speedListener = builder.speedListener;
        this.processListener = builder.processListener;
    }

    public void startDownload(){

    }

    public void cancel(boolean isForceCancel){
        // TODO: 2018/4/24
    }

    public boolean isCanceled(){
        // TODO: 2018/4/24
        return isCancelled;
    }

    public boolean isRunning(){
        // TODO: 2018/4/24
        return isRunning;
    }

    public void destroy() {
        statuListener = null;
        speedListener = null;
        processListener = null;
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

    public static class Builder{
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
        private HashMap<String, String> headersMap;

        private StatuListener statuListener;
        private SpeedListener speedListener;
        private ProcessListener processListener;

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

        public Builder setHeadersMap(HashMap<String, String> headersMap) {
            this.headersMap = (HashMap<String, String>) headersMap.clone();
            return this;
        }
        public Builder addHeader(String key, String value){
            if(headersMap == null){
                headersMap = new HashMap<>();
            }
            headersMap.put(key, value);
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

        public Builder setProcessListener(ProcessListener processListener) {
            this.processListener = processListener;
            return this;
        }

        public ADRequest build(){
            return new ADRequest(this);
        }
    }
}
