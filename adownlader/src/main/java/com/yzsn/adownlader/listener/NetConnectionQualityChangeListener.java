package com.yzsn.adownlader.listener;

import com.yzsn.adownlader.common.NetConnectionQuality;

/**
 * Created by zc on 2018/4/25.
 */

public interface NetConnectionQualityChangeListener {
    void onChange(NetConnectionQuality currentConnectionQuality, int currentBandwidth);
}
