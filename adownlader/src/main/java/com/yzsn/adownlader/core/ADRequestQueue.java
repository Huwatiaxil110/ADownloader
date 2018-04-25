package com.yzsn.adownlader.core;

import com.yzsn.adownlader.common.ADRequest;
import com.yzsn.adownlader.common.Priority;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 请求队列类
 * Created by zc on 2018/4/24.
 */

public class ADRequestQueue {
    private static ADRequestQueue sInstance;
    private AtomicInteger mSequenceGenerator = new AtomicInteger();
    private final Set<ADRequest> mRequests = Collections.newSetFromMap(new ConcurrentHashMap<ADRequest, Boolean>());

    public static ADRequestQueue getInstance() {
        if (sInstance == null) {
            synchronized (ADRequestQueue.class) {
                if (sInstance == null) {
                    sInstance = new ADRequestQueue();
                }
            }
        }
        return sInstance;
    }

    public int getSequenceGenerator() {
        return mSequenceGenerator.incrementAndGet();
    }

    public interface RequestFilter {
        boolean apply(ADRequest request);
    }

    private void cancel(RequestFilter requestFilter, boolean isForceCancel) {
        Iterator<ADRequest> iterator = mRequests.iterator();

        for (; iterator.hasNext(); ) {
            ADRequest request = iterator.next();
            if (requestFilter.apply(request)) {
                request.cancel(isForceCancel);
                if (request.isCanceled()) {
                    request.destroy();
                    iterator.remove();
                }
            }
        }
    }

    public void cancelAll(boolean isForceCancel) {
        Iterator<ADRequest> iterator = mRequests.iterator();

        for (; iterator.hasNext(); ) {
            ADRequest request = iterator.next();
            request.cancel(isForceCancel);
            if (request.isCanceled()) {
                request.destroy();
                iterator.remove();
            }
        }
    }

    public void cancelRequestByTag(final Object tag, boolean isForceCancel){
        if(tag == null){
            return;
        }

        cancel(new RequestFilter() {
            @Override
            public boolean apply(ADRequest request) {
                return isRequestWithTheTag(request, tag);
            }
        }, isForceCancel);
    }

    private boolean isRequestWithTheTag(ADRequest request, Object object){
        if(request.getTag() == null){
            return false;
        }

        if(request.getTag() instanceof String && object instanceof String){
            final String rTag = (String) request.getTag();
            final String oTag = (String) object;
            return rTag.equals(oTag);
        }

        return request.getTag().equals(object);
    }

    public boolean isRequestRunning(Object tag){
        for(ADRequest request : mRequests){
            if(isRequestWithTheTag(request, tag) && request.isRunning()){
                return true;
            }
        }

        return false;
    }

    public ADRequest addRequest(ADRequest request){
        mRequests.add(request);
        request.setSequenceNumber(getSequenceGenerator());

        if(request.getPriority() == Priority.IMMEDIATE){
            request.setFuture(ExecutorCenter.getInstance().getExecutorSupplier().forImmediateDownloadTask().submit(new CoreRunnable(request)));
        }else{
            request.setFuture(ExecutorCenter.getInstance().getExecutorSupplier().forDownloadTask().submit(new CoreRunnable(request)));
        }

        return request;
    }

    public void finish(ADRequest request){
        mRequests.remove(request);
    }
}



































