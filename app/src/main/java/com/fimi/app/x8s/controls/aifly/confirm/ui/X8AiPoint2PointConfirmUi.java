package com.fimi.app.x8s.controls.aifly.confirm.ui;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.config.X8AiConfig;
import com.fimi.app.x8s.controls.X8MainAiFlyController;
import com.fimi.app.x8s.tools.ImageUtils;


public class X8AiPoint2PointConfirmUi implements View.OnClickListener {
    private final View contentView;
    private View btnOk;
    private CheckBox cbTip;
    private ImageView imgFlag;
    private View imgReturn;
    private X8MainAiFlyController listener;
    private X8MainAiFlyController mX8MainAiFlyController;

    public X8AiPoint2PointConfirmUi(Activity activity, View parent) {
        this.contentView = activity.getLayoutInflater().inflate(R.layout.x8_ai_point_to_point_confirm_layout, (ViewGroup) parent, true);
        initViews(this.contentView);
        initActions();
    }

    public void setX8MainAiFlyController(X8MainAiFlyController mX8MainAiFlyController) {
        this.mX8MainAiFlyController = mX8MainAiFlyController;
    }

    public void initViews(View rootView) {
        this.imgReturn = rootView.findViewById(R.id.img_ai_follow_return);
        this.btnOk = rootView.findViewById(R.id.btn_ai_follow_confirm_ok);
        this.cbTip = rootView.findViewById(R.id.cb_ai_follow_confirm_ok);
        this.imgFlag = rootView.findViewById(R.id.img_device_to_point_flag);
        this.imgFlag.setImageBitmap(ImageUtils.getBitmapByPath(rootView.getContext(), R.drawable.x8_img_device_to_point_flag));
    }

    public void initActions() {
        this.imgReturn.setOnClickListener(this);
        this.btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_ai_follow_return) {
            this.mX8MainAiFlyController.onCloseConfirmUi();
        } else if (id == R.id.btn_ai_follow_confirm_ok) {
            X8AiConfig.getInstance().setAiP2PCourse(!this.cbTip.isChecked());
            this.mX8MainAiFlyController.onPoint2PointConfirmOkClick();
        }
    }

    public void setFcHeart(boolean isInSky, boolean isLowPower) {
        this.btnOk.setEnabled(isInSky && isLowPower);
    }
}
