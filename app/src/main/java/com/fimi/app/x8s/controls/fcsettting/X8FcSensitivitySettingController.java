package com.fimi.app.x8s.controls.fcsettting;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.interfaces.AbsX8MenuBoxControllers;
import com.fimi.app.x8s.interfaces.IX8CalibrationListener;
import com.fimi.app.x8s.widget.X8CustomSeekBar;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.percent.PercentLinearLayout;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.dataparser.AckGetSensitivity;
import com.fimi.x8sdk.entity.X8AppSettingLog;
import com.fimi.x8sdk.modulestate.StateManager;


public class X8FcSensitivitySettingController extends AbsX8MenuBoxControllers implements View.OnClickListener, X8CustomSeekBar.onSeekValueSetListener {
    private final int DEFAULT_VALUE;
    private ImageButton btnReset;
    private PercentLinearLayout content_layout;
    private int curAttitudeSens;
    private int curBrakeSens;
    private int curYawSens;
    private int curYawTrip;
    private FcCtrlManager fcCtrlManager;
    private ImageView imgReturn;
    private boolean isRequested;
    private IX8CalibrationListener listener;
    private Context mContext;
    private X8DoubleCustomDialog resetDialog;
    private X8CustomSeekBar sbAttitudeSens;
    private X8CustomSeekBar sbBrakeSens;
    private X8CustomSeekBar sbYawSens;
    private X8CustomSeekBar sbYawTrip;

    public X8FcSensitivitySettingController(View rootView) {
        super(rootView);
        this.DEFAULT_VALUE = 50;
        this.isRequested = false;
    }

    @Override
    public void initViews(View rootView) {
        LayoutInflater inflater = LayoutInflater.from(rootView.getContext());
        this.contentView = inflater.inflate(R.layout.x8_main_fc_sensitivity_setting, (ViewGroup) rootView, true);
        this.content_layout = this.contentView.findViewById(R.id.content_layout);
        this.imgReturn = this.contentView.findViewById(R.id.img_return);
        this.btnReset = this.contentView.findViewById(R.id.btn_reset);
        this.btnReset.setClickable(false);
        this.btnReset.setOnClickListener(this);
        this.sbAttitudeSens = this.contentView.findViewById(R.id.sb_attitude_sens);
        this.sbBrakeSens = this.contentView.findViewById(R.id.sb_brake_sens);
        this.sbYawTrip = this.contentView.findViewById(R.id.sb_yaw_trip);
        this.sbYawSens = this.contentView.findViewById(R.id.sb_yaw_sens);
        Resources resources = this.contentView.getContext().getResources();
        this.sbAttitudeSens.initData(resources.getString(R.string.x8_fc_sensitivity_attitude_sens), 10, 100);
        this.sbBrakeSens.initData(resources.getString(R.string.x8_fc_sensitivity_brake_sens), 10, 100);
        this.sbYawTrip.initData(resources.getString(R.string.x8_fc_sensitivity_yaw_trip), 10, 100);
        this.sbYawSens.initData(resources.getString(R.string.x8_fc_sensitivity_yaw_sens), 10, 100);
        this.sbAttitudeSens.setOnSeekChangedListener(this);
        this.sbBrakeSens.setOnSeekChangedListener(this);
        this.sbYawTrip.setOnSeekChangedListener(this);
        this.sbYawSens.setOnSeekChangedListener(this);
        this.mContext = this.contentView.getContext();
        initActions();
    }

    @Override
    public void initActions() {
        if (this.contentView != null) {
            this.imgReturn.setOnClickListener(this);
        }
    }

    @Override
    public void onDroneConnected(boolean b) {
        if (this.isShow && b && !this.isRequested) {
            requestDefaultValue();
            this.isRequested = true;
        }
        updateViewEnable(b, this.content_layout);
        this.btnReset.setAlpha(b ? 1.0f : 0.6f);
        this.btnReset.setClickable(b);
    }

    private void requestDefaultValue() {
        if (this.fcCtrlManager != null) {
            this.fcCtrlManager.getSensitivity(new UiCallBackListener<AckGetSensitivity>() {
                @Override
                public void onComplete(CmdResult cmdResult, AckGetSensitivity sensitivity) {
                    if (cmdResult.isSuccess()) {
                        X8FcSensitivitySettingController.this.sbAttitudeSens.setProgress(sensitivity.getRollPercent());
                        X8FcSensitivitySettingController.this.sbYawSens.setProgress(sensitivity.getYawPercent());
                    }
                }
            });
            this.fcCtrlManager.getBrakeSens(new UiCallBackListener<AckGetSensitivity>() {
                @Override
                public void onComplete(CmdResult cmdResult, AckGetSensitivity sensitivity) {
                    if (cmdResult.isSuccess()) {
                        X8FcSensitivitySettingController.this.sbBrakeSens.setProgress(sensitivity.getRollPercent());
                    }
                }
            });
            this.fcCtrlManager.getYawTrip(new UiCallBackListener<AckGetSensitivity>() {
                @Override
                public void onComplete(CmdResult cmdResult, AckGetSensitivity sensitivity) {
                    if (cmdResult.isSuccess()) {
                        X8FcSensitivitySettingController.this.sbYawTrip.setProgress(sensitivity.getYawPercent());
                    }
                }
            });
        }
    }

    @Override
    public void defaultVal() {
        updateViewEnable(false, this.content_layout);
        this.btnReset.setClickable(false);
        this.btnReset.setAlpha(0.6f);
    }

    @Override
    public void showItem() {
        this.isShow = true;
        this.contentView.setVisibility(View.VISIBLE);
        getDroneState();
        onDroneConnected(this.isConect);
    }

    @Override
    public void closeItem() {
        this.isShow = false;
        this.contentView.setVisibility(View.GONE);
        defaultVal();
    }

    public void setFcCtrlManager(FcCtrlManager fcCtrlManager) {
        this.fcCtrlManager = fcCtrlManager;
    }

    public void setCalibrationListener(IX8CalibrationListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.img_return) {
            closeItem();
            if (this.listener != null) {
                this.listener.onCalibrationReturn();
            }
        } else if (i == R.id.btn_reset) {
            if (this.resetDialog == null) {
                this.resetDialog = new X8DoubleCustomDialog(this.mContext, this.mContext.getString(R.string.x8_fc_sensitivity_reset_title), this.mContext.getString(R.string.x8_fc_sensitivity_reset_content), new X8DoubleCustomDialog.onDialogButtonClickListener() {
                    @Override
                    // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
                    public void onLeft() {
                    }

                    @Override
                    // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
                    public void onRight() {
                        X8FcSensitivitySettingController.this.fcCtrlManager.setAttitudeSensitivity(new UiCallBackListener<Object>() {
                            @Override
                            public void onComplete(CmdResult cmdResult, Object respCode) {
                                if (cmdResult.isSuccess()) {
                                    X8FcSensitivitySettingController.this.sbAttitudeSens.setProgress(50);
                                    X8AppSettingLog.setFs(50, 50, 50, 50);
                                }
                            }
                        }, 50, 50);
                        X8FcSensitivitySettingController.this.fcCtrlManager.setBrakeSens(new UiCallBackListener<Object>() {
                            @Override
                            public void onComplete(CmdResult cmdResult, Object resp) {
                                if (cmdResult.isSuccess()) {
                                    X8FcSensitivitySettingController.this.sbBrakeSens.setProgress(50);
                                    X8AppSettingLog.setFb(50, 50, 50, 50);
                                }
                            }
                        }, 50, 50);
                        X8FcSensitivitySettingController.this.fcCtrlManager.setYawTrip(new UiCallBackListener<Object>() {
                            @Override
                            public void onComplete(CmdResult cmdResult, Object resp) {
                                if (cmdResult.isSuccess()) {
                                    X8FcSensitivitySettingController.this.sbYawTrip.setProgress(50);
                                    X8AppSettingLog.setYawTrip(50, 50, 50, 50);
                                }
                            }
                        }, 50);
                        X8FcSensitivitySettingController.this.fcCtrlManager.setYawSensitivity(new UiCallBackListener<Object>() {
                            @Override
                            public void onComplete(CmdResult cmdResult, Object respCode) {
                                if (cmdResult.isSuccess()) {
                                    X8FcSensitivitySettingController.this.sbYawSens.setProgress(50);
                                }
                            }
                        }, 50);
                    }
                });
            }
            this.resetDialog.show();
        }
    }

    @Override
    public void onSeekValueSet(int viewId, final int value) {
        if (viewId == R.id.sb_attitude_sens) {
            this.fcCtrlManager.setAttitudeSensitivity(new UiCallBackListener<Object>() {
                @Override
                public void onComplete(CmdResult cmdResult, Object respCode) {
                    if (cmdResult.isSuccess()) {
                    }
                }
            }, value, value);
            this.fcCtrlManager.getSensitivity(new UiCallBackListener<AckGetSensitivity>() {
                @Override
                public void onComplete(CmdResult cmdResult, AckGetSensitivity sensitivity) {
                    if (cmdResult.isSuccess()) {
                        X8FcSensitivitySettingController.this.sbAttitudeSens.setProgress(sensitivity.getRollPercent());
                        X8FcSensitivitySettingController.this.sbYawSens.setProgress(sensitivity.getYawPercent());
                    }
                }
            });
        } else if (viewId == R.id.sb_brake_sens) {
            this.fcCtrlManager.setBrakeSens(new UiCallBackListener<Object>() {
                @Override
                public void onComplete(CmdResult cmdResult, Object resp) {
                    if (cmdResult.isSuccess()) {
                        StateManager.getInstance().getX8Drone().setFcBrakeSenssity(value);
                    }
                }
            }, value, value);
            this.fcCtrlManager.getBrakeSens(new UiCallBackListener<AckGetSensitivity>() {
                @Override
                public void onComplete(CmdResult cmdResult, AckGetSensitivity sensitivity) {
                    if (cmdResult.isSuccess()) {
                        X8FcSensitivitySettingController.this.sbBrakeSens.setProgress(sensitivity.getRollPercent());
                    }
                }
            });
        } else if (viewId == R.id.sb_yaw_trip) {
            this.fcCtrlManager.setYawTrip(new UiCallBackListener<Object>() {
                @Override
                public void onComplete(CmdResult cmdResult, Object resp) {
                    if (cmdResult.isSuccess()) {
                    }
                }
            }, value);
            this.fcCtrlManager.getYawTrip(new UiCallBackListener<AckGetSensitivity>() {
                @Override
                public void onComplete(CmdResult cmdResult, AckGetSensitivity sensitivity) {
                    if (cmdResult.isSuccess()) {
                        X8FcSensitivitySettingController.this.sbYawTrip.setProgress(sensitivity.getYawPercent());
                    }
                }
            });
        } else if (viewId == R.id.sb_yaw_sens) {
            this.fcCtrlManager.setYawSensitivity(new UiCallBackListener<Object>() {
                @Override
                public void onComplete(CmdResult cmdResult, Object respCode) {
                    if (cmdResult.isSuccess()) {
                        StateManager.getInstance().getX8Drone().setFcYAWSenssity(value);
                    }
                }
            }, value);
            this.fcCtrlManager.getSensitivity(new UiCallBackListener<AckGetSensitivity>() {
                @Override
                public void onComplete(CmdResult cmdResult, AckGetSensitivity sensitivity) {
                    if (cmdResult.isSuccess()) {
                        X8FcSensitivitySettingController.this.sbAttitudeSens.setProgress(sensitivity.getRollPercent());
                        X8FcSensitivitySettingController.this.sbYawSens.setProgress(sensitivity.getYawPercent());
                    }
                }
            });
        }
    }
}
