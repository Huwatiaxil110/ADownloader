package com.yzsn.adownlader;

import com.yzsn.adownlader.common.ADRequest;
import com.yzsn.adownlader.core.ADRequestQueue;
import com.yzsn.adownlader.util.log.L;
import com.yzsn.adownlader.util.log.LAdapter;

/**
 * Created by zc on 2018/4/18.
 */

public class ADownloader {

    public static void init(){
        L.initLAdapter(new LAdapter("-->", true, false));
    }

    public static ADRequest.Builder prepareDownLoad(String url, String fileDir, String fileName){
        return new ADRequest.Builder(url, fileDir, fileName);
    }

    public static void cancel(Object tag){
        ADRequestQueue.getInstance().cancelRequestByTag(tag, false);
    }


}
