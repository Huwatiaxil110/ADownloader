package com.yzsn.adownlader.common;

import com.yzsn.adownlader.core.ExecutorCenter;
import com.yzsn.adownlader.listener.NetConnectionQualityChangeListener;

/**
 * Created by zc on 2018/4/25.
 */

public class NetConnectionManager {

    private static final int BYTES_TO_BITS = 8;
    private static final int DEFAULT_SAMPLES_TO_QUALITY_CHANGE = 5;
    private static final int MINIMUM_SAMPLES_TO_DECIDE_QUALITY = 2;
    private static final int DEFAULT_POOR_BANDWIDTH = 150;
    private static final int DEFAULT_MODERATE_BANDWIDTH = 550;
    private static final int DEFAULT_GOOD_BANDWIDTH = 2000;
    private static final long BANDWIDTH_LOWER_BOUND = 10;

    private static NetConnectionManager sInstance;
    private NetConnectionQuality mCurrentNetConnectionQuality = NetConnectionQuality.UNKNOWN;
    private int mCurrentBandwidthForSampling = 0;
    private int mCurrentNumberOfSample = 0;
    private int mCurrentBandwidth = 0;
    private NetConnectionQualityChangeListener mNetConnectionQualityChangeListener;

    public static NetConnectionManager getInstance() {
        if (sInstance == null) {
            synchronized (NetConnectionManager.class) {
                if (sInstance == null) {
                    sInstance = new NetConnectionManager();
                }
            }
        }
        return sInstance;
    }

    public synchronized void updateBandwidth(long bytes, long timeInMs) {
        if (timeInMs == 0 || bytes < 20000 || (bytes) * 1.0 / (timeInMs) *
                BYTES_TO_BITS < BANDWIDTH_LOWER_BOUND) {
            return;
        }
        double bandwidth = (bytes) * 1.0 / (timeInMs) * BYTES_TO_BITS;
        mCurrentBandwidthForSampling = (int) ((mCurrentBandwidthForSampling *
                mCurrentNumberOfSample + bandwidth) / (mCurrentNumberOfSample + 1));
        mCurrentNumberOfSample++;
        if (mCurrentNumberOfSample == DEFAULT_SAMPLES_TO_QUALITY_CHANGE ||
                (mCurrentNetConnectionQuality == NetConnectionQuality.UNKNOWN &&
                        mCurrentNumberOfSample == MINIMUM_SAMPLES_TO_DECIDE_QUALITY)) {
            final NetConnectionQuality lastNetConnectionQuality = mCurrentNetConnectionQuality;
            mCurrentBandwidth = mCurrentBandwidthForSampling;
            if (mCurrentBandwidthForSampling <= 0) {
                mCurrentNetConnectionQuality = NetConnectionQuality.UNKNOWN;
            } else if (mCurrentBandwidthForSampling < DEFAULT_POOR_BANDWIDTH) {
                mCurrentNetConnectionQuality = NetConnectionQuality.POOR;
            } else if (mCurrentBandwidthForSampling < DEFAULT_MODERATE_BANDWIDTH) {
                mCurrentNetConnectionQuality = NetConnectionQuality.MODERATE;
            } else if (mCurrentBandwidthForSampling < DEFAULT_GOOD_BANDWIDTH) {
                mCurrentNetConnectionQuality = NetConnectionQuality.GOOD;
            } else if (mCurrentBandwidthForSampling > DEFAULT_GOOD_BANDWIDTH) {
                mCurrentNetConnectionQuality = NetConnectionQuality.EXCELLENT;
            }
            if (mCurrentNumberOfSample == DEFAULT_SAMPLES_TO_QUALITY_CHANGE) {
                mCurrentBandwidthForSampling = 0;
                mCurrentNumberOfSample = 0;
            }
            if (mCurrentNetConnectionQuality != lastNetConnectionQuality &&
                    mNetConnectionQualityChangeListener != null) {
                ExecutorCenter.getInstance().getExecutorSupplier().forUIDownloadTask()
                        .execute(new Runnable() {
                            @Override
                            public void run() {
                                mNetConnectionQualityChangeListener.onChange(mCurrentNetConnectionQuality, mCurrentBandwidth);
                            }
                        });
            }
        }

    }

    public int getCurrentBandwidth() {
        return mCurrentBandwidth;
    }

    public NetConnectionQuality getCurrentNetConnectionQuality() {
        return mCurrentNetConnectionQuality;
    }

    public void setListener(NetConnectionQualityChangeListener connectionQualityChangeListener) {
        mNetConnectionQualityChangeListener = connectionQualityChangeListener;
    }

    public void removeListener() {
        mNetConnectionQualityChangeListener = null;
    }

    public static void shutDown() {
        if (sInstance != null) {
            sInstance = null;
        }
    }
}
