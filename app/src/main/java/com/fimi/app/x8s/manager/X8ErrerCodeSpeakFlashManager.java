package com.fimi.app.x8s.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.fimi.app.x8s.controls.X8ErrorCodeController;
import com.fimi.app.x8s.entity.X8ShowErrorCodeTask;
import com.fimi.app.x8s.enums.X8ErrorCodeEnum;
import com.fimi.x8sdk.entity.ErrorCodeBean;


public class X8ErrerCodeSpeakFlashManager {
    private final Context context;
    private final X8ErrorCodeController errorCodeController;
    private final X8ShowErrorCodeTask mediumTask;
    private final X8ShowErrorCodeTask seriousTask;
    private boolean isSpeek;
    private boolean isStart;
    private long speekId;
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                nextRunBySpeekEnd();
            }
        }
    };

    public X8ErrerCodeSpeakFlashManager(Context context, X8ErrorCodeController errorCodeController) {
        this.context = context;
        this.errorCodeController = errorCodeController;
        this.seriousTask = new X8ShowErrorCodeTask(context, errorCodeController, X8ErrorCodeEnum.serious, this);
        this.mediumTask = new X8ShowErrorCodeTask(context, errorCodeController, X8ErrorCodeEnum.medium, this);
    }

    public void setStart(boolean start) {
        this.isStart = start;
    }

    public void speekText(String text, long speekId) {
        this.isSpeek = true;
        this.speekId = speekId;
    }

    public boolean isSpeek() {
        return this.isSpeek && this.speekId != 0;
    }

    public void nextRun() {
        if (this.errorCodeController.hasErrorCode()) {
            if (this.errorCodeController.hasSeriousCode()) {
                this.seriousTask.nextRun();
            }
            if (this.errorCodeController.hasMediumCode()) {
                this.mediumTask.nextRun();
                return;
            }
            return;
        }
        this.isStart = false;
    }

    public void speakFinish() {
        this.isSpeek = false;
        this.mHandler.sendEmptyMessage(1);
    }

    public void disconnect() {
        this.seriousTask.disconnect();
        this.mediumTask.disconnect();
        this.isStart = false;
        this.speekId = 0L;
        this.isSpeek = false;
    }

    public void start() {
        if (!this.isStart) {
            this.isStart = true;
            nextRun();
        }
    }

    public void clear() {
        if (this.seriousTask.isShow()) {
            this.seriousTask.nextRun();
        }
        if (this.mediumTask.isShow()) {
            this.mediumTask.nextRun();
        }
    }

    public void nextRun(X8ErrorCodeEnum type) {
        if (X8ErrorCodeEnum.serious == type) {
            if (this.seriousTask.getSpeekId() == 0) {
                this.seriousTask.nextRun();
            }
            if (this.errorCodeController.hasMediumCode2() && this.mediumTask.getState() == 0) {
                this.mediumTask.nextRun();
            }
        } else if (X8ErrorCodeEnum.medium == type) {
            if (this.mediumTask.getSpeekId() == 0) {
                this.mediumTask.nextRun();
            }
            if (this.errorCodeController.hasSeriousCode2() && this.seriousTask.getState() == 0) {
                this.seriousTask.nextRun();
            }
        }
    }

    public void nextRunBySpeekEnd() {
        if (this.seriousTask.getSpeekId() != 0) {
            this.speekId = 0L;
            this.seriousTask.setSpeekId(0L);
            if (!this.seriousTask.isShow()) {
                nextRun(X8ErrorCodeEnum.serious);
            }
        } else if (this.mediumTask.getSpeekId() != 0) {
            this.speekId = 0L;
            this.mediumTask.setSpeekId(0L);
            if (!this.mediumTask.isShow()) {
                nextRun(X8ErrorCodeEnum.medium);
            }
        }
    }

    public void runEnd(X8ErrorCodeEnum type) {
        this.errorCodeController.runEnd(type);
        if (!this.errorCodeController.hasErrorCode()) {
            this.isStart = false;
        }
    }

    public void removeMediumMap(ErrorCodeBean.ActionBean actionBean) {
        this.mediumTask.remove(actionBean);
    }

    public void removeSeriousMap(ErrorCodeBean.ActionBean actionBean) {
        this.seriousTask.remove(actionBean);
    }
}
