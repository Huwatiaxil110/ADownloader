package com.yzsn.adownlader.core;

import android.net.TrafficStats;
import android.text.TextUtils;

import com.yzsn.adownlader.common.ADConstants;
import com.yzsn.adownlader.common.ADRequest;
import com.yzsn.adownlader.common.NetConnectionManager;
import com.yzsn.adownlader.error.ADError;
import com.yzsn.adownlader.util.AssistUtil;
import com.yzsn.adownlader.util.SSLUtil;
import com.yzsn.adownlader.util.log.L;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zc on 2018/4/25.
 */

public class CoreWorking {
    public static OkHttpClient mOkHttpClient = getClient();
    public static String sUserAgent = null;

    public static OkHttpClient getClient() {
        if (mOkHttpClient == null) {
            return getDefaultClient();
        }
        return mOkHttpClient;
    }

    public static OkHttpClient getDefaultClient() {
        return new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    public static Response performRequest(final ADRequest request) throws ADError {
        Request okHttpRequest;
        Response okHttpResponse;
        try {
            Request.Builder builder = new Request.Builder().url(request.getUrl());
            addHeadersToRequestBuilder(builder, request);
            builder = builder.get();
            if (request.getCacheControl() != null) {
                builder.cacheControl(request.getCacheControl());
            }
            okHttpRequest = builder.build();

            OkHttpClient okHttpClient;
            OkHttpClient.Builder okHttpClientBuilder;
            if (request.getOkHttpClient() != null) {
                okHttpClientBuilder = request.getOkHttpClient().newBuilder().cache(mOkHttpClient.cache())
                        .addNetworkInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Response originalResponse = chain.proceed(chain.request());
                                return originalResponse.newBuilder()
                                        .body(new ResponseProgressBody(originalResponse.body(), request.getProgressListener()))
                                        .build();
                            }
                        });
            } else {
                okHttpClientBuilder = mOkHttpClient.newBuilder()
                        .addNetworkInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Response originalResponse = chain.proceed(chain.request());
                                return originalResponse.newBuilder()
                                        .body(new ResponseProgressBody(originalResponse.body(), request.getProgressListener()))
                                        .build();
                            }
                        });
            }

            if(request.isIgnoreSSL()){  //不理会证书问题
                okHttpClientBuilder.sslSocketFactory(SSLUtil.createSSLSocketFactory()).hostnameVerifier(new SSLUtil.TrustAllHostnameVerifier());
            }
            okHttpClient = okHttpClientBuilder.build();

            request.setCall(okHttpClient.newCall(okHttpRequest));
            final long startTime = System.currentTimeMillis();
            final long startBytes = TrafficStats.getTotalRxBytes();
            okHttpResponse = request.getCall().execute();

            //解析文件名
            if(TextUtils.isEmpty(request.getFileName()) && request.getParseFileNameListener() != null){
                String contentDisposition = okHttpResponse.header("Content-Disposition");
                String fileName = request.getParseFileNameListener().getFileNameFromHeader(contentDisposition);
                if(!TextUtils.isEmpty(fileName)){
                    request.setFileName(fileName);
                }else{
                    throw new ADError(new NullPointerException("filename not set and no filename from response"));
                }
            }

            AssistUtil.saveFile(okHttpResponse, request.getFileDir(), request.getFileName());

            final long timeTaken = System.currentTimeMillis() - startTime;
            if (okHttpResponse.cacheResponse() == null) {
                final long finalBytes = TrafficStats.getTotalRxBytes();
                final long diffBytes;
                if (startBytes == TrafficStats.UNSUPPORTED || finalBytes == TrafficStats.UNSUPPORTED) {
                    diffBytes = okHttpResponse.body().contentLength();
                } else {
                    diffBytes = finalBytes - startBytes;
                }
                NetConnectionManager.getInstance().updateBandwidth(diffBytes, timeTaken);
                AssistUtil.sendAnalytics(request.getAnalyticsListener(), timeTaken, -1, okHttpResponse.body().contentLength(), false);
            } else if (request.getAnalyticsListener() != null) {
                AssistUtil.sendAnalytics(request.getAnalyticsListener(), timeTaken, -1, 0, true);
            }
        } catch (IOException ioe) {
            L.e(true, "");
            try {
                File destinationFile = new File(request.getFileDir() + File.separator + request.getFileName());
                if (destinationFile.exists()) {
                    destinationFile.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            throw new ADError(ioe);
        }
        return okHttpResponse;
    }

    public static void addHeadersToRequestBuilder(Request.Builder builder, ADRequest request) {
        if (request.getUserAgent() != null) {
            builder.addHeader(ADConstants.USER_AGENT, request.getUserAgent());
        } else if (sUserAgent != null) {
            request.setUserAgent(sUserAgent);
            builder.addHeader(ADConstants.USER_AGENT, sUserAgent);
        }
        Headers requestHeaders = request.getHeaders();
        if (requestHeaders != null) {
            builder.headers(requestHeaders);
            if (request.getUserAgent() != null && !requestHeaders.names().contains(ADConstants.USER_AGENT)) {
                builder.addHeader(ADConstants.USER_AGENT, request.getUserAgent());
            }
        }
    }

}
