package com.yzsn.adownlader.core;

import com.yzsn.adownlader.common.ADConstants;
import com.yzsn.adownlader.common.Progress;
import com.yzsn.adownlader.listener.ProgressListener;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by zc on 2018/4/25.
 */

public class ResponseProgressBody extends ResponseBody {
    private ResponseBody responseBody;
    private BufferedSource bufferedSource;
    private ProgressHandler progressHandler;

    public ResponseProgressBody(ResponseBody responseBody, ProgressListener progressListener) {
        this.responseBody = responseBody;
        if(progressListener != null){
            this.progressHandler = new ProgressHandler(progressListener);
        }
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if(bufferedSource == null){
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }

        return bufferedSource;
    }

    private Source source(Source source) {

        return new ForwardingSource(source) {

            long totalBytesRead;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += ((bytesRead != -1) ? bytesRead : 0);
                if (progressHandler != null) {
                    progressHandler.obtainMessage(ADConstants.UPDATE, new Progress(totalBytesRead, responseBody.contentLength())).sendToTarget();
                }
                return bytesRead;
            }
        };
    }
}
