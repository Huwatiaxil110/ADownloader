package com.yzsn.adownlader.util.log;

/**
 * 控制打印纸日开关的工具
 * Created by zc.
 */

public class LAdapter {
    private String tag;
    private boolean isDebug;
    private boolean isNeedOtherInfo;

    public LAdapter(String tag, boolean isDebug, boolean isNeedOtherInfo) {
        this.tag = tag;
        this.isDebug = isDebug;
        this.isNeedOtherInfo = isNeedOtherInfo;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public boolean isNeedOtherInfo() {
        return isNeedOtherInfo;
    }

    public String getTag() {
        if (tag == null) {
            return "";
        }
        return tag;
    }
}
