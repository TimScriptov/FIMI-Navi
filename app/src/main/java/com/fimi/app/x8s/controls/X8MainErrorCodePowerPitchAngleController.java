package com.fimi.app.x8s.controls;

import android.view.View;

import androidx.annotation.NonNull;

import com.fimi.android.app.R;
import com.fimi.app.x8s.interfaces.AbsX8Controllers;
import com.fimi.x8sdk.entity.X8ErrorCodeInfo;

import java.util.List;


public class X8MainErrorCodePowerPitchAngleController extends AbsX8Controllers {
    private X8ErrorCodeController mX8ErrorCodeController;

    public X8MainErrorCodePowerPitchAngleController(View rootView) {
        super(rootView);
    }

    public X8ErrorCodeController getmX8ErrorCodeController() {
        return this.mX8ErrorCodeController;
    }

    @Override
    public void initViews(@NonNull View rootView) {
        this.handleView = rootView.findViewById(R.id.main_left_tools);
        this.mX8ErrorCodeController = new X8ErrorCodeController(rootView);
        if (this.mX8ErrorCodeController != null) {
            this.mX8ErrorCodeController.initSpeak();
        }
    }

    @Override
    public void initActions() {
    }

    @Override
    public void defaultVal() {
    }

    @Override
    public void openUi() {
    }

    @Override
    public void closeUi() {
    }

    public void onErrorCode(List<X8ErrorCodeInfo> list) {
        if (this.mX8ErrorCodeController != null) {
            this.mX8ErrorCodeController.onErrorCode(list);
        }
    }

    @Override
    public void onDroneConnected(boolean b) {
        if (this.mX8ErrorCodeController != null) {
            this.mX8ErrorCodeController.onDroneConnected(b);
        }
    }

    public void setErrorViewEnableShow(boolean b) {
    }

    @Override
    public boolean onClickBackKey() {
        return false;
    }
}
