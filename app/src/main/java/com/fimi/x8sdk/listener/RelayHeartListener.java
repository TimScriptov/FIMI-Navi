package com.fimi.x8sdk.listener;

import com.fimi.x8sdk.dataparser.AutoRelayHeart;

/* loaded from: classes2.dex */
public interface RelayHeartListener {
    void cameraStatusListener(boolean z);

    void onRelayHeart(AutoRelayHeart autoRelayHeart);
}
