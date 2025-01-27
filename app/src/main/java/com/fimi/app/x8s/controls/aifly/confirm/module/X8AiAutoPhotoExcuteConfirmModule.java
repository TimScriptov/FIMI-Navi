package com.fimi.app.x8s.controls.aifly.confirm.module;

import android.app.Activity;
import android.view.View;

import com.fimi.app.x8s.controls.aifly.X8AiAutoPhototExcuteController;
import com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiAutoPhotoExcuteConfirmUi;
import com.fimi.app.x8s.interfaces.IX8AiAutoPhototExcuteListener;
import com.fimi.app.x8s.interfaces.IX8NextViewListener;
import com.fimi.x8sdk.controller.FcManager;


public class X8AiAutoPhotoExcuteConfirmModule extends X8BaseModule {
    private X8AiAutoPhotoExcuteConfirmUi mUi;

    public void init(Activity activity, View rootView, int type, int angle) {
        this.mUi = new X8AiAutoPhotoExcuteConfirmUi(activity, rootView, type, angle);
    }

    @Override
    public void setFcHeart(boolean isInSky, boolean isLowPower) {
        this.mUi.setFcHeart(isInSky, isLowPower);
    }

    public void setListener(IX8NextViewListener listener, FcManager fcManager, X8AiAutoPhototExcuteController mX8AiAutoPhototExcuteController) {
        this.mUi.setListener(listener, fcManager, mX8AiAutoPhototExcuteController);
    }

    public void setValue() {
        this.mUi.setValue();
    }

    public void startAiAutoPhoto(IX8AiAutoPhototExcuteListener excuteListener) {
        this.mUi.setAiAutoPhotoValueCmd(excuteListener);
    }
}
