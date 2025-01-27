package com.fimi.kernel.connect.session;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.DataUiHanler;
import com.fimi.kernel.connect.interfaces.IDataCallBack;
import com.fimi.kernel.connect.interfaces.IPersonalDataCallBack;
import com.fimi.kernel.connect.model.UpdateDateMessage;
import com.fimi.kernel.dataparser.ILinkMessage;
import com.fimi.kernel.dataparser.JsonMessage;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;


public class NoticeManager implements IDataCallBack {
    private static final NoticeManager mNoticeManager = new NoticeManager();
    private final int personMsg = 0;
    private final int outMsg = 1;
    private final int jsonMsg = 3;
    private final int uiCallMsg = 2;
    private final CopyOnWriteArrayList<IDataCallBack> list = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<MediaDataListener> mediaIist = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<JsonListener> jsonListeners = new CopyOnWriteArrayList<>();
    private final DataUiHanler mDataUiHanler = new DataUiHanler();
    @SuppressLint("HandlerLeak")
    private final Handler mHanlder = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    Iterator<IDataCallBack> it = NoticeManager.this.list.iterator();
                    while (it.hasNext()) {
                        IDataCallBack l = it.next();
                        l.onDataCallBack(msg.arg1, msg.arg2, (ILinkMessage) msg.obj);
                    }
                    return;
                case 1:
                    Iterator<IDataCallBack> it2 = NoticeManager.this.list.iterator();
                    while (it2.hasNext()) {
                        IDataCallBack l2 = it2.next();
                        l2.onSendTimeOut(msg.arg1, msg.arg2, (BaseCommand) msg.obj);
                    }
                    return;
                case 2:
                    JsonMessage obj = (JsonMessage) msg.obj;
                    if (obj != null && obj.getUiCallBackListener() != null) {
                        obj.getUiCallBackListener().onComplete(obj.getJsonRt(), Integer.valueOf(obj.getMsg_id()));
                        return;
                    }
                    return;
                case 3:
                    if (!NoticeManager.this.jsonListeners.isEmpty()) {
                        Iterator<JsonListener> it3 = NoticeManager.this.jsonListeners.iterator();
                        while (it3.hasNext()) {
                            JsonListener l3 = it3.next();
                            l3.onProcess(msg.arg1, (JSONObject) msg.obj);
                        }
                        return;
                    }
                    return;
                default:
            }
        }
    };
    VideodDataListener listener;
    UpdateDateListener updateDateListener;
    private MediaDataListener mediaDataListener;

    public static void initInstance() {
        getInstance();
    }

    public static NoticeManager getInstance() {
        return mNoticeManager;
    }

    public void addMediaListener(MediaDataListener listener) {
        if (!this.mediaIist.contains(listener)) {
            this.mediaIist.add(listener);
        }
    }

    public void removeMediaListener(MediaDataListener listener) {
        this.mediaIist.remove(listener);
    }

    public void removeALLMediaListener() {
        this.mediaIist.clear();
    }

    public void add2NoticeList(IDataCallBack callback) {
        this.list.add(callback);
    }

    public void add2NoticeList(IDataCallBack callback, JsonListener listener) {
        this.list.add(callback);
        this.jsonListeners.add(listener);
    }

    public void add2NoticeList(IDataCallBack callback, VideodDataListener listener) {
        this.list.add(callback);
        this.listener = listener;
    }

    public void add2NoticeList(IDataCallBack callback, UpdateDateListener updateDateListener) {
        this.list.add(callback);
        this.updateDateListener = updateDateListener;
    }

    public void removeNoticeList(IDataCallBack callBack) {
        IDataCallBack remove = null;
        Iterator<IDataCallBack> it = this.list.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            IDataCallBack l = it.next();
            if (l != null && l == callBack) {
                remove = l;
                break;
            }
        }
        if (remove != null) {
            this.list.remove(remove);
        }
        if (this.updateDateListener != null) {
            this.updateDateListener = null;
        }
    }

    public void removeFpvListener() {
        if (this.listener != null) {
            this.listener = null;
        }
    }

    public void addJsonListener(JsonListener listener) {
        this.jsonListeners.add(listener);
    }

    public void removeJsonListener(JsonListener listener) {
        JsonListener remove = null;
        if (!this.jsonListeners.isEmpty()) {
            Iterator<JsonListener> it = this.jsonListeners.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                JsonListener ll = it.next();
                if (ll != null && ll == listener) {
                    remove = ll;
                    break;
                }
            }
        }
        if (remove != null) {
            this.jsonListeners.remove(remove);
        }
    }

    public void onMediaDataCallBack(byte[] data) {
        if (this.mediaIist != null && this.mediaIist.size() > 0) {
            Iterator<MediaDataListener> it = this.mediaIist.iterator();
            while (it.hasNext()) {
                MediaDataListener listener = it.next();
                listener.mediaDataCallBack(data);
            }
        }
    }

    public void onJsonDataCallBack(int msgId, JSONObject respJson) {
        this.mHanlder.obtainMessage(3, msgId, msgId, respJson).sendToTarget();
    }

    public void onJsonDataUICallBack(JsonMessage msg) {
        if (msg != null) {
            this.mHanlder.obtainMessage(2, msg.getMsg_id(), msg.getMsg_id(), msg).sendToTarget();
        }
    }

    public void onRawDataCallBack(byte[] bytes) {
        if (this.listener != null) {
            this.listener.onRawdataCallBack(bytes);
        }
    }

    public void onUpdateDateCallBack(UpdateDateMessage updateDateMessage) {
        if (this.updateDateListener != null) {
            this.updateDateListener.onUpdateDateCallBack(updateDateMessage);
        }
    }

    @Override
    public void onDataCallBack(int groupId, int cmdId, ILinkMessage packet) {
        this.mHanlder.obtainMessage(0, groupId, cmdId, packet).sendToTarget();
    }

    @Override
    public void onSendTimeOut(int groupId, int cmdId, BaseCommand bcd) {
        this.mHanlder.obtainMessage(1, groupId, cmdId, bcd).sendToTarget();
    }

    public void onPersonalDataCallBack(int groupId, int cmdId, ILinkMessage msg, IPersonalDataCallBack callBack) {
        Bundle bundle = new Bundle();
        Message message = new Message();
        bundle.putSerializable("target", msg);
        message.setData(bundle);
        message.obj = callBack;
        message.what = 0;
        message.arg1 = groupId;
        message.arg2 = cmdId;
        this.mDataUiHanler.sendMessage(message);
    }

    public void onPersonalSendTimeOut(int groupId, int cmdId, BaseCommand bcd, IPersonalDataCallBack callBack) {
        Bundle bundle = new Bundle();
        Message message = new Message();
        bundle.putSerializable("target", bcd);
        message.setData(bundle);
        message.obj = callBack;
        message.what = 1;
        message.arg1 = groupId;
        message.arg2 = cmdId;
        this.mDataUiHanler.sendMessage(message);
    }
}
