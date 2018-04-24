package com.yzsn.adownlader.core;

import com.yzsn.adownlader.common.ADRequest;
import com.yzsn.adownlader.common.Priority;

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
        // TODO: 2018/4/24
    }

}
