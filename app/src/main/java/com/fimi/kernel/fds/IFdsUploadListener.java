package com.fimi.kernel.fds;

/* loaded from: classes.dex */
public interface IFdsUploadListener {
    void onFailure(Object obj);

    void onProgress(Object obj, long j, long j2);

    void onStop(Object obj);

    void onSuccess(Object obj);
}
