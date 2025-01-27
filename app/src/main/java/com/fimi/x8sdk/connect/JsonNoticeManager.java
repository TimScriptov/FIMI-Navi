package com.fimi.x8sdk.connect;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fimi.x8sdk.dataparser.AckCamJsonInfo;
import com.fimi.x8sdk.jsonResult.CameraParamsJson;
import com.fimi.x8sdk.listener.JsonCallBackListener;

import java.util.ArrayList;
import java.util.List;


public class JsonNoticeManager {
    private static final JsonNoticeManager noticeManager = new JsonNoticeManager();
    private final int outTimeMsg = 1;
    private final int normalMsg = 0;
    List<JsonCallBackListener> callBackListeners = new ArrayList();
    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (!JsonNoticeManager.this.callBackListeners.isEmpty()) {
                        for (JsonCallBackListener listener : JsonNoticeManager.this.callBackListeners) {
                            JSONObject rtJson = (JSONObject) msg.obj;
                            switch (rtJson.getIntValue("msg_id")) {
                                case 1:
                                    CameraParamsJson msg9ParamJson2 = JSON.parseObject(rtJson.toString(), CameraParamsJson.class);
                                    if (msg9ParamJson2.getRval() >= 0) {
                                        listener.onSuccess(msg9ParamJson2);
                                        listener.onJSONSuccess(rtJson);
                                        break;
                                    } else {
                                        listener.onFail(msg9ParamJson2.getRval(), msg9ParamJson2.getMsg_id(), msg9ParamJson2.getType());
                                        break;
                                    }
                                case 2:
                                case 9:
                                    CameraParamsJson msg9ParamJson = JSON.parseObject(rtJson.toString(), CameraParamsJson.class);
                                    if (msg9ParamJson.getRval() >= 0) {
                                        listener.onSuccess(msg9ParamJson);
                                        listener.onJSONSuccess(rtJson);
                                        break;
                                    } else {
                                        listener.onFail(msg9ParamJson.getRval(), msg9ParamJson.getMsg_id(), msg9ParamJson.getType());
                                        break;
                                    }
                                case 3:
                                case 257:
                                    if (rtJson.getIntValue("rval") >= 0) {
                                        listener.onJSONSuccess(rtJson);
                                        break;
                                    } else {
                                        AckCamJsonInfo failAck = JSON.parseObject(rtJson.toString(), AckCamJsonInfo.class);
                                        listener.onFail(failAck.getRval(), failAck.getMsg_id(), failAck.getType());
                                        break;
                                    }
                            }
                        }
                        return;
                    }
                    return;
                case 1:
                    if (!JsonNoticeManager.this.callBackListeners.isEmpty()) {
                        for (JsonCallBackListener listener2 : JsonNoticeManager.this.callBackListeners) {
                            listener2.outTime();
                        }
                        return;
                    }
                    return;
                default:
            }
        }
    };

    public static JsonNoticeManager getNoticeManager() {
        return noticeManager;
    }

    public void addListener(JsonCallBackListener listener) {
        this.callBackListeners.add(listener);
    }

    public void removeListener(JsonCallBackListener listener) {
        this.callBackListeners.remove(listener);
    }

    public void removeAll() {
        this.callBackListeners.clear();
    }

    public void onProcess(int msgId, JSONObject ackCamJsonInfo) {
        this.mHandler.obtainMessage(0, ackCamJsonInfo).sendToTarget();
    }

    public void sendOutTime() {
        this.mHandler.obtainMessage(1, -1).sendToTarget();
    }
}
