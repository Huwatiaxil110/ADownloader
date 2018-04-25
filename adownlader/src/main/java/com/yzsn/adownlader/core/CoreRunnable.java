package com.yzsn.adownlader.core;

import com.yzsn.adownlader.common.ADRequest;
import com.yzsn.adownlader.common.Priority;
import com.yzsn.adownlader.error.ADError;
import com.yzsn.adownlader.util.AssistUtil;
import com.yzsn.adownlader.util.log.L;

import okhttp3.Response;

/**
 * Created by zc on 2018/4/24.
 */

public class CoreRunnable implements Runnable{
    private Priority priority;
    public final int sequence;
    public final ADRequest request;

    public CoreRunnable(ADRequest request) {
        this.priority = request.getPriority();
        this.sequence = request.getSequenceNumber();
        this.request = request;
    }

    public Priority getPriority() {
        return priority;
    }

    @Override
    public void run() {
        L.e(true, "");

        executeRequest();
    }

    private void executeRequest() {
        Response okHttpResponse;
        try {
            okHttpResponse = CoreWorking.performRequest(request);
            if (okHttpResponse == null) {
                deliverError(request, AssistUtil.getErrorForConnection(new ADError()));
                return;
            }
            if (okHttpResponse.code() >= 400) {
                deliverError(request, AssistUtil.getErrorForServerResponse(new ADError(okHttpResponse), request, okHttpResponse.code()));
                return;
            }
            request.downloadCompleted();
        } catch (Exception e) {
            deliverError(request, AssistUtil.getErrorForConnection(new ADError(e)));
        }
    }

    private void deliverError(final ADRequest request, final ADError aError) {
        ExecutorCenter.getInstance().getExecutorSupplier().forUIDownloadTask().execute(new Runnable() {
            public void run() {
                request.deliverError(aError);
                request.finish();
            }
        });
    }
}
