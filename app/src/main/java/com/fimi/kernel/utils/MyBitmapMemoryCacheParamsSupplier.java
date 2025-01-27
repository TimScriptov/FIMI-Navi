package com.fimi.kernel.utils;

import android.app.ActivityManager;
import android.os.Build;

import com.facebook.common.internal.Supplier;
import com.facebook.imagepipeline.cache.MemoryCacheParams;


public class MyBitmapMemoryCacheParamsSupplier implements Supplier<MemoryCacheParams> {
    private static final int MAX_CACHE_ASHM_ENTRIES = 128;
    private static final int MAX_CACHE_ENTRIES = 56;
    private static final int MAX_CACHE_EVICTION_ENTRIES = 5;
    private static final int MAX_CACHE_EVICTION_SIZE = 5;
    private final ActivityManager mActivityManager;

    public MyBitmapMemoryCacheParamsSupplier(ActivityManager activityManager) {
        this.mActivityManager = activityManager;
    }

    @Override
    public MemoryCacheParams get() {
        if (Build.VERSION.SDK_INT >= 21) {
            return new MemoryCacheParams(getMaxCacheSize(), 56, 5, 5, 1);
        }
        return new MemoryCacheParams(getMaxCacheSize(), 128, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    private int getMaxCacheSize() {
        int maxMemory = Math.min(this.mActivityManager.getMemoryClass() * 1048576, Integer.MAX_VALUE);
        if (maxMemory < 33554432) {
            return 4194304;
        }
        if (maxMemory < 67108864) {
            return 6291456;
        }
        return maxMemory / 5;
    }
}
