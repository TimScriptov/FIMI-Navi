package com.fimi.app.x8s.controls.aifly.confirm.ui;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.config.X8AiConfig;
import com.fimi.app.x8s.controls.X8MainAiFlyController;
import com.fimi.app.x8s.tools.ImageUtils;


public class X8AiAutoPhotoConfirmUi implements View.OnClickListener {
    private final View contentView;
    private View btnOk;
    private CheckBox cbTip;
    private ImageView imgFlag;
    private View imgReturn;
    private X8MainAiFlyController listener;
    private X8MainAiFlyController mX8MainAiFlyController;
    private int menuIndex;
    private ScrollView svTips;
    private TextView tvContentTip1;
    private View tvPhoto1;
    private View tvPhoto2;
    private TextView tvTitle;
    private View vConfirm;
    private View vItem1;
    private View vItem2;
    private View vItemSelect;

    public X8AiAutoPhotoConfirmUi(Activity activity, View parent) {
        this.contentView = activity.getLayoutInflater().inflate(R.layout.x8_ai_auto_photo_confirm_layout, (ViewGroup) parent, true);
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
        this.svTips = rootView.findViewById(R.id.sv_ai_items);
        this.vItemSelect = rootView.findViewById(R.id.ll_ai_autophoto_item);
        this.vConfirm = rootView.findViewById(R.id.rl_ai_autophoto_confirm);
        this.tvTitle = rootView.findViewById(R.id.tv_ai_follow_title);
        this.tvContentTip1 = rootView.findViewById(R.id.tv_ai_follow_confirm_title1);
        this.vItem1 = rootView.findViewById(R.id.rl_ai_photo1);
        this.vItem2 = rootView.findViewById(R.id.rl_ai_photo2);
        this.tvPhoto1 = rootView.findViewById(R.id.tv_ai_photo1);
        this.tvPhoto2 = rootView.findViewById(R.id.tv_ai_photo2);
        this.vItemSelect.setVisibility(View.VISIBLE);
        this.vConfirm.setVisibility(View.GONE);
        this.tvTitle.setText(this.contentView.getContext().getString(R.string.x8_ai_fly_auto_photo));
        this.imgFlag = rootView.findViewById(R.id.img_auto_photo_flag);
    }

    public void initActions() {
        this.imgReturn.setOnClickListener(this);
        this.btnOk.setOnClickListener(this);
        this.vItem1.setOnClickListener(this);
        this.vItem2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_ai_follow_return) {
            if (this.menuIndex == 0) {
                this.mX8MainAiFlyController.onCloseConfirmUi();
                return;
            }
            this.menuIndex = 0;
            this.svTips.fullScroll(33);
            this.vItemSelect.setVisibility(View.VISIBLE);
            this.vConfirm.setVisibility(View.GONE);
            this.tvTitle.setText(this.contentView.getContext().getString(R.string.x8_ai_fly_auto_photo));
        } else if (id == R.id.btn_ai_follow_confirm_ok) {
            if (this.menuIndex == 1) {
                X8AiConfig.getInstance().setAiAutoPhotoCustomCourse(!this.cbTip.isChecked());
            } else if (this.menuIndex == 2) {
                X8AiConfig.getInstance().setAiAutoPhotoVerticalCourse(!this.cbTip.isChecked());
            }
            this.mX8MainAiFlyController.onAutoPhotoConfirmOk(this.menuIndex - 1);
        } else if (id == R.id.rl_ai_photo1) {
            this.menuIndex = 1;
            if (!X8AiConfig.getInstance().isAiAutoPhotoCustomCourse()) {
                this.mX8MainAiFlyController.onAutoPhotoConfirmOk(this.menuIndex - 1);
                return;
            }
            String title = this.contentView.getContext().getString(R.string.x8_ai_auto_photo_title);
            String content = this.contentView.getContext().getString(R.string.x8_ai_auto_photo_tip1);
            int res = R.drawable.x8_img_auto_photo_h_flag;
            onSelectItem(title, content, res);
        } else if (id == R.id.rl_ai_photo2) {
            this.menuIndex = 2;
            if (!X8AiConfig.getInstance().isAiAutoPhotoVerticalCourse()) {
                this.mX8MainAiFlyController.onAutoPhotoConfirmOk(this.menuIndex - 1);
                return;
            }
            String title2 = this.contentView.getContext().getString(R.string.x8_ai_auto_photo_vertical_title);
            String content2 = this.contentView.getContext().getString(R.string.x8_ai_auto_photo_vertical_tip1);
            int res2 = R.drawable.x8_img_auto_photo_vertical_flag;
            onSelectItem(title2, content2, res2);
        }
    }

    public void onSelectItem(String title, String content, int res) {
        this.vItemSelect.setVisibility(View.GONE);
        this.vConfirm.setVisibility(View.VISIBLE);
        this.tvTitle.setText(title);
        this.tvContentTip1.setText(content);
        this.imgFlag.setImageBitmap(ImageUtils.getBitmapByPath(this.contentView.getContext(), res));
    }

    public void setFcHeart(boolean isInSky, boolean isLowPower) {
        this.btnOk.setEnabled(isInSky && isLowPower);
    }
}
