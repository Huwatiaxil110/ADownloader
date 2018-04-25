package com.yzsn.adownlader.core;

import android.net.TrafficStats;

import com.androidnetworking.error.ANError;
import com.yzsn.adownlader.common.ADConstants;
import com.yzsn.adownlader.common.ADRequest;
import com.yzsn.adownlader.common.NetConnectionManager;
import com.yzsn.adownlader.util.AssistUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.androidnetworking.internal.InternalNetworking.getClient;

/**
 * Created by zc on 2018/4/25.
 */

public class CoreWorking {
    public static OkHttpClient sHttpClient = getClient();
    public static String sUserAgent = null;

    public static OkHttpClient getClient() {
        if (sHttpClient == null) {
            return getDefaultClient();
        }
        return sHttpClient;
    }

    public static OkHttpClient getDefaultClient() {
        return new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    public static Response performRequest(final ADRequest request) throws ANError {
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

            if (request.getOkHttpClient() != null) {
                okHttpClient = request.getOkHttpClient().newBuilder().cache(sHttpClient.cache())
                        .addNetworkInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Response originalResponse = chain.proceed(chain.request());
                                return originalResponse.newBuilder()
                                        .body(new ResponseProgressBody(originalResponse.body(), request.getProgressListener()))
                                        .build();
                            }
                        }).build();
            } else {
                okHttpClient = sHttpClient.newBuilder()
                        .addNetworkInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Response originalResponse = chain.proceed(chain.request());
                                return originalResponse.newBuilder()
                                        .body(new ResponseProgressBody(originalResponse.body(), request.getProgressListener()))
                                        .build();
                            }
                        }).build();
            }
            request.setCall(okHttpClient.newCall(okHttpRequest));
            final long startTime = System.currentTimeMillis();
            final long startBytes = TrafficStats.getTotalRxBytes();
            okHttpResponse = request.getCall().execute();
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
            try {
                File destinationFile = new File(request.getFileDir() + File.separator + request.getFileName());
                if (destinationFile.exists()) {
                    destinationFile.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            throw new ANError(ioe);
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
