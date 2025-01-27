package com.fimi.album.presenter;

import android.app.Activity;
import android.view.View;

import com.fimi.album.biz.DataManager;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.handler.HandlerManager;
import com.fimi.album.iview.IDateHandler;
import com.fimi.album.ui.MediaActivity;

import java.io.File;
import java.lang.ref.WeakReference;


public class MediaPresenter<T extends Activity> implements IDateHandler {
    private final DataManager<MediaModel> mDataManager = DataManager.obtain();
    private final MediaActivity mMediaActivity;
    private final WeakReference<T> weakReference;

    public MediaPresenter(T activity) {
        this.weakReference = new WeakReference<>(activity);
        this.mMediaActivity = (MediaActivity) this.weakReference.get();
        this.mDataManager.setIdataImpl(this);
    }

    @Override
    public void loadDateComplete(boolean isCamera, boolean isSuccess) {
        HandlerManager.obtain().getHandlerInMainThread().post(new Runnable() {
            @Override
            public void run() {
                MediaPresenter.this.mMediaActivity.getmProgressBar().setVisibility(View.GONE);
            }
        });
        onStartReshAdapter();
    }

    @Override
    public void refreshLoadDataComplete() {
    }

    public void forEachFile(String folderPath) {
        File file = new File(folderPath);
        if (file.exists()) {
            this.mMediaActivity.getmProgressBar().setVisibility(View.VISIBLE);
            this.mDataManager.forEachFolder(file.getAbsolutePath());
        }
    }

    public void reDefaultVaribale() {
        this.mDataManager.reLocalDefaultVaribale();
        this.mDataManager.removeCallBack();
    }

    public void onStartReshAdapter() {
        this.mMediaActivity.getLocalFragment().reshAdapter();
    }

    public void selectBtn(boolean selectState) {
        if (selectState) {
            this.mMediaActivity.getLocalFragment().enterSelectAllMode();
        } else {
            this.mMediaActivity.getLocalFragment().cancalSelectAllMode();
        }
    }

    public void enterSelectMode(boolean state, boolean isNeedPreform) {
        this.mMediaActivity.getLocalFragment().enterSelectMode(state, isNeedPreform);
    }
}
