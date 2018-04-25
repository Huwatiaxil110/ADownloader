package com.yzsn.adownlader.error;

import com.yzsn.adownlader.common.ADConstants;

import okhttp3.Response;

/**
 * Created by zc on 2018/4/25.
 */

public class ADError extends Exception{

    private String errorBody;

    private int errorCode = 0;

    private String errorDetail;

    private Response response;

    public ADError() {
    }

    public ADError(Response response) {
        this.response = response;
    }

    public ADError(String message) {
        super(message);
    }

    public ADError(String message, Response response) {
        super(message);
        this.response = response;
    }

    public ADError(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ADError(String message, Response response, Throwable throwable) {
        super(message, throwable);
        this.response = response;
    }

    public ADError(Response response, Throwable throwable) {
        super(throwable);
        this.response = response;
    }

    public ADError(Throwable throwable) {
        super(throwable);
    }

    public Response getResponse() {
        return response;
    }

    public void setErrorDetail(String errorDetail) {
        this.errorDetail = errorDetail;
    }

    public String getErrorDetail() {
        return this.errorDetail;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setCancellationMessageInError() {
        this.errorDetail = ADConstants.ERROR_REQUEST_CANCELLED;
    }

    public String getErrorBody() {
        return errorBody;
    }

    public void setErrorBody(String errorBody) {
        this.errorBody = errorBody;
    }
}
