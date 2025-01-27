package com.fimi.app.x8s.controls.aifly;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.fimi.TcpClient;
import com.fimi.android.app.R;
import com.fimi.app.x8s.X8Application;
import com.fimi.app.x8s.controls.X8AiTrackController;
import com.fimi.app.x8s.controls.aifly.confirm.module.X8AiGravitationExcuteConfirmModule;
import com.fimi.app.x8s.interfaces.AbsX8AiController;
import com.fimi.app.x8s.interfaces.IX8AiGravitationExcuteControllerListener;
import com.fimi.app.x8s.interfaces.IX8NextViewListener;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.app.x8s.widget.X8AiTipWithCloseView;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.widget.X8ToastUtil;
import com.fimi.x8sdk.controller.CameraManager;
import com.fimi.x8sdk.controller.FcManager;
import com.fimi.x8sdk.dataparser.AckAiGetGravitationPrameter;
import com.fimi.x8sdk.dataparser.AutoFcSportState;
import com.fimi.x8sdk.modulestate.StateManager;


public class X8AiGravitationExcuteController extends AbsX8AiController implements View.OnClickListener, X8DoubleCustomDialog.onDialogButtonClickListener, X8AiTrackController.OnAiTrackControllerListener {
    private final X8sMainActivity activity;
    private final IX8NextViewListener mIX8NextViewListener;
    protected boolean isNextShow;
    private X8DoubleCustomDialog dialog;
    private ImageView imgBack;
    private ImageView imgNext;
    private AckAiGetGravitationPrameter mAckAiGetGravitationPrameter;
    private CameraManager mCameraManager;
    private double mCurrentLatitude;
    private double mCurrentLongitude;
    private X8AiGravitationExcuteConfirmModule mExcuteConfirmModule;
    private FcManager mFcManager;
    private View mFlagBottom;
    private X8GravitationState mGravitationState;
    private IX8AiGravitationExcuteControllerListener mIX8AiGravitationExcuteControllerListener;
    private ImageView mImSuroundBg;
    private View mNextBlank;
    private View mNextContent;
    private X8AiTipWithCloseView tvTip;
    private int width;

    public X8AiGravitationExcuteController(X8sMainActivity activity, View rootView, X8GravitationState state) {
        super(rootView);
        this.mGravitationState = X8GravitationState.IDLE;
        this.mIX8NextViewListener = new IX8NextViewListener() {
            @Override
            public void onBackClick() {
                closeNextUi(true);
                if (activity.getmMapVideoController().isFullVideo()) {
                    mImSuroundBg.setVisibility(View.VISIBLE);
                } else {
                    mImSuroundBg.setVisibility(View.GONE);
                }
            }

            @Override
            public void onExcuteClick() {
                closeNextUi(false);
                if (activity.getmMapVideoController().isFullVideo()) {
                    mImSuroundBg.setVisibility(View.VISIBLE);
                } else {
                    mImSuroundBg.setVisibility(View.GONE);
                }
                imgNext.setVisibility(View.GONE);
                mGravitationState = X8GravitationState.RUNNING;
                tvTip.setVisibility(View.GONE);
                mIX8AiGravitationExcuteControllerListener.onAiGravitaionRunning();
            }

            @Override
            public void onSaveClick() {
            }
        };
        this.activity = activity;
        this.mGravitationState = state;
    }

    public void setCameraManager(CameraManager cameraManager) {
        this.mCameraManager = cameraManager;
    }

    public void setFcManager(FcManager mFcManager) {
        this.mFcManager = mFcManager;
    }

    public void setListener(IX8AiGravitationExcuteControllerListener listener) {
        this.mIX8AiGravitationExcuteControllerListener = listener;
    }

    @Override
    public void initViews(View rootView) {
    }

    @Override
    public void initActions() {
        if (this.handleView != null) {
            this.imgBack.setOnClickListener(this);
            this.imgNext.setOnClickListener(this);
            this.mNextBlank.setOnClickListener(this);
        }
    }

    @Override
    public void defaultVal() {
    }

    @Override
    public boolean onClickBackKey() {
        return false;
    }

    @Override
    public void openUi() {
        LayoutInflater inflater = LayoutInflater.from(this.rootView.getContext());
        this.handleView = inflater.inflate(R.layout.x8_ai_gravitation_layout, (ViewGroup) this.rootView, true);
        this.imgNext = this.handleView.findViewById(R.id.img_ai_gravitation_follow_next);
        this.imgBack = this.handleView.findViewById(R.id.img_ai_gravitation_follow_back);
        this.tvTip = this.handleView.findViewById(R.id.v_gravitation_content_tip);
        this.mNextBlank = this.handleView.findViewById(R.id.x8_main_ai_gravitation_next_blank);
        this.mNextContent = this.handleView.findViewById(R.id.x8_main_ai_gravitation_next_content);
        this.mFlagBottom = this.handleView.findViewById(R.id.rl_flag_gravitation_bottom);
        this.mImSuroundBg = this.handleView.findViewById(R.id.img_ai_gravitation_suround_bg);
        this.tvTip.setTipText(this.rootView.getContext().getString(R.string.x8_ai_fly_gravitation_tip4));
        this.imgNext.setEnabled(false);
        this.mExcuteConfirmModule = new X8AiGravitationExcuteConfirmModule();
        if (this.activity.getmMapVideoController().isFullVideo()) {
            this.mImSuroundBg.setVisibility(View.VISIBLE);
        } else {
            this.mImSuroundBg.setVisibility(View.GONE);
        }
        this.activity.getmMapVideoController().getFimiMap().onLocationGravitTrailEvent();
        this.activity.getmX8AiTrackController().setOnAiTrackControllerListener(this);
        this.activity.getmX8AiTrackController().openUi();
        if (this.mGravitationState == X8GravitationState.RUNNING) {
            this.imgNext.setVisibility(View.GONE);
            this.tvTip.setVisibility(View.GONE);
            this.mIX8AiGravitationExcuteControllerListener.onAiGravitaionRunning();
            getRunningParmeter();
        } else if (this.mGravitationState == X8GravitationState.IDLE) {
            this.imgNext.setVisibility(View.VISIBLE);
            this.tvTip.setVisibility(View.VISIBLE);
        }
        initActions();
        super.openUi();
    }

    @Override
    public void closeUi() {
        super.closeUi();
        this.activity.getmX8AiTrackController().closeUi();
        this.activity.getmMapVideoController().getFimiMap().getAiSurroundManager().clearSurroundMarker();
        this.activity.getmMapVideoController().getFimiMap().getAiSurroundManager().resetMapEvent();
    }

    @Override
    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == R.id.img_ai_gravitation_follow_next) {
            openNextUi();
        } else if (id == R.id.img_ai_gravitation_follow_back) {
            if (this.mGravitationState == X8GravitationState.RUNNING) {
                showExitDialog();
            } else {
                closeGravitationing();
            }
        } else if (id == R.id.x8_main_ai_gravitation_next_blank) {
            closeNextUi(true);
            this.mImSuroundBg.setVisibility(View.VISIBLE);
        }
    }

    public void showSportState(AutoFcSportState state) {
        if (this.mGravitationState == X8GravitationState.IDLE && this.mCurrentLongitude != state.getLongitude() && this.mCurrentLatitude != state.getLatitude()) {
            this.mCurrentLatitude = state.getLatitude();
            this.mCurrentLongitude = state.getLongitude();
            getIdleParmeter();
        }
        if (this.mGravitationState == X8GravitationState.RUNNING_DISCONNECT) {
            getRunningDisconnectParmeter();
        }
    }

    @Override
    public void onDroneConnected(boolean b) {
        super.onDroneConnected(b);
        if (!b) {
            this.mGravitationState = X8GravitationState.RUNNING_DISCONNECT;
            closeUi();
        }
    }

    private void getRunningParmeter() {
        this.mFcManager.getGravitationPrameter((cmdResult, o) -> {
            if (cmdResult.isSuccess) {
                drawEllipse((AckAiGetGravitationPrameter) o);
            }
        });
    }

    public void drawEllipse(@NonNull AckAiGetGravitationPrameter prameter) {
        this.activity.getmMapVideoController().getFimiMap().getAiSurroundManager().addEllipse(prameter.getStartLat(), prameter.getStartLng(), prameter.getHorizontalDistance(), 90.0f + prameter.getStartNosePoint());
    }

    private void getRunningDisconnectParmeter() {
        this.mFcManager.getGravitationPrameter((cmdResult, o) -> {
            if (cmdResult.isSuccess) {
                mGravitationState = X8GravitationState.RUNNING;
                drawEllipse((AckAiGetGravitationPrameter) o);
            }
        });
    }

    private void getIdleParmeter() {
        this.mFcManager.getGravitationPrameter((cmdResult, o) -> {
            if (cmdResult.isSuccess) {
                mAckAiGetGravitationPrameter = (AckAiGetGravitationPrameter) o;
                drawIdleEllipse();
            }
        });
    }

    public void drawIdleEllipse() {
        this.activity.getmMapVideoController().getFimiMap().getAiSurroundManager().addEllipse(StateManager.getInstance().getX8Drone().getLatitude(), StateManager.getInstance().getX8Drone().getLongitude(), this.mAckAiGetGravitationPrameter.getHorizontalDistance(), 90.0f + StateManager.getInstance().getX8Drone().getDeviceAngle());
    }

    public void closeNextUi(final boolean isImgNextShow) {
        if (this.isNextShow) {
            this.isNextShow = false;
            ObjectAnimator translationRight = ObjectAnimator.ofFloat(this.mNextContent, "translationX", 0.0f, this.width);
            translationRight.setDuration(300L);
            translationRight.start();
            translationRight.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mNextContent.setVisibility(View.GONE);
                    mNextBlank.setVisibility(View.GONE);
                    ((ViewGroup) mNextContent).removeAllViews();
                    imgBack.setVisibility(View.VISIBLE);
                    mFlagBottom.setVisibility(View.VISIBLE);
                    if (isImgNextShow) {
                        imgNext.setVisibility(View.VISIBLE);
                        tvTip.setVisibility(View.VISIBLE);
                        return;
                    }
                    imgNext.setVisibility(View.GONE);
                    tvTip.setVisibility(View.GONE);
                }
            });
        }
    }

    private void openNextUi() {
        this.mNextBlank.setVisibility(View.VISIBLE);
        this.mNextContent.setVisibility(View.VISIBLE);
        this.tvTip.setVisibility(View.GONE);
        this.mFlagBottom.setVisibility(View.GONE);
        this.imgNext.setVisibility(View.GONE);
        this.mImSuroundBg.setVisibility(View.GONE);
        this.mExcuteConfirmModule.init(this.activity, this.mNextContent, this.mCameraManager);
        this.mExcuteConfirmModule.setsetListener(this.mIX8NextViewListener, this.mFcManager);
        if (!this.isNextShow) {
            this.isNextShow = true;
            this.width = X8Application.ANIMATION_WIDTH;
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(this.mNextContent, "translationX", this.width, 0.0f);
            animatorY.setDuration(300L);
            animatorY.start();
        }
    }

    public void showExitDialog() {
        if (this.dialog == null) {
            String t = this.rootView.getContext().getString(R.string.x8_ai_fly_gravitation_dialog_title);
            String m = this.rootView.getContext().getString(R.string.x8_ai_fly_gravitation_dialog_tip);
            this.dialog = new X8DoubleCustomDialog(this.rootView.getContext(), t, m, this);
        }
        this.dialog.show();
    }

    public void switchMapVideo(boolean sightFlag) {
        if (this.handleView != null) {
            this.mImSuroundBg.setVisibility(sightFlag ? View.GONE : View.VISIBLE);
        }
    }

    public void cancleByModeChange(int mode) {
        closeGravitationing();
        if (mode == 1) {
            onTaskComplete(true);
        } else if (this.mIX8AiGravitationExcuteControllerListener != null) {
            this.mIX8AiGravitationExcuteControllerListener.onAiGravitationInterrupt();
        }
    }

    public void onTaskComplete(boolean showText) {
        closeGravitationing();
        if (this.mIX8AiGravitationExcuteControllerListener != null) {
            this.mIX8AiGravitationExcuteControllerListener.onAiGravitationComplete(showText);
        }
    }

    private void closeGravitationing() {
        this.mFcManager.setAiVcClose((cmdResult, o) -> {
            if (cmdResult.isSuccess()) {
                closeUi();
                mGravitationState = X8GravitationState.IDLE;
                if (mIX8AiGravitationExcuteControllerListener != null) {
                    mIX8AiGravitationExcuteControllerListener.onAiGravitationBackClick();
                    return;
                }
                return;
            }
            TcpClient.getIntance().sendLog("setAiVcClose error" + cmdResult.getErrDes());
        });
    }

    @Override
    public void onLeft() {
    }

    @Override
    public void onRight() {
        this.mFcManager.setAiVcClose((cmdResult, o) -> {
            if (cmdResult.isSuccess()) {
                if (mGravitationState == X8GravitationState.RUNNING) {
                    exitGravitation();
                    return;
                }
                return;
            }
            TcpClient.getIntance().sendLog("setAiVcClose error" + cmdResult.getErrDes());
        });
    }

    public void exitGravitation() {
        this.mFcManager.setGravitationExite((cmdResult, o) -> {
            if (!cmdResult.isSuccess) {
                X8ToastUtil.showToast(handleView.getContext(), cmdResult.getErrDes(), 0);
            } else {
                mGravitationState = X8GravitationState.IDLE;
            }
            onTaskComplete(false);
        });
    }

    @Override
    public void onChangeGoLocation(float left, float right, float top, float bottom, int w, int h) {
    }

    @Override
    public void setGoEnabled(boolean b) {
    }

    @Override
    public void onTouchActionDown() {
    }

    @Override
    public void onTouchActionUp() {
        this.imgNext.setEnabled(true);
        setAiVcOpen();
    }

    @Override
    public void onTracking() {
    }

    public void setAiVcOpen() {
        this.mFcManager.setAiVcOpen((cmdResult, o) -> {
            if (cmdResult.isSuccess()) {
            }
        });
    }


    public enum X8GravitationState {
        IDLE,
        RUNNING,
        RUNNING_DISCONNECT
    }
}
