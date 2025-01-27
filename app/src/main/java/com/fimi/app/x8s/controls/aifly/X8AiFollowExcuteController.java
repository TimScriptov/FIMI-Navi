package com.fimi.app.x8s.controls.aifly;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fimi.TcpClient;
import com.fimi.android.app.R;
import com.fimi.app.x8s.controls.X8AiTrackController;
import com.fimi.app.x8s.enums.X8AiFollowState;
import com.fimi.app.x8s.interfaces.AbsX8AiController;
import com.fimi.app.x8s.interfaces.IX8AiFollowExcuteListener;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.app.x8s.widget.X8AiFollowModeItemView;
import com.fimi.app.x8s.widget.X8AiTipWithCloseView;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8FollowSpeedContainerView;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.percent.PercentRelativeLayout;
import com.fimi.x8sdk.cmdsenum.X8Task;
import com.fimi.x8sdk.dataparser.AckAiFollowGetSpeed;
import com.fimi.x8sdk.dataparser.AckGetAiFollowMode;


public class X8AiFollowExcuteController extends AbsX8AiController implements View.OnClickListener, X8AiTrackController.OnAiTrackControllerListener, X8DoubleCustomDialog.onDialogButtonClickListener, X8AiFollowModeItemView.OnModeSelectListner, X8FollowSpeedContainerView.OnSendSpeedListener {
    private final X8sMainActivity activity;
    private X8DoubleCustomDialog dialog;
    private View flagSmall;
    private int goHeight;
    private int goWidth;
    private ImageView imgBack;
    private ImageView imgGo;
    private ImageView imgSmall;
    private boolean isGetMode;
    private boolean isGetSpeed;
    private boolean isShowGo;
    private boolean isSwitchMode;
    private boolean isTou;
    private IX8AiFollowExcuteListener listener;
    private X8AiTipWithCloseView mTipBgView;
    private X8AiFollowState mX8AiFollowState;
    private int timeSend;
    private TextView tvTitle;
    private int type;
    private X8AiFollowModeItemView vModeImtes;
    private X8FollowSpeedContainerView vSpeedContainer;

    public X8AiFollowExcuteController(X8sMainActivity activity, View rootView, X8AiFollowState state, int type) {
        super(rootView);
        this.isShowGo = false;
        this.timeSend = 0;
        this.mX8AiFollowState = state;
        this.type = type;
        this.activity = activity;
    }

    public void setX8AiFollowExcuteListener(IX8AiFollowExcuteListener listener) {
        this.listener = listener;
    }

    @Override
    public void initViews(View rootView) {
    }

    @Override
    public void initActions() {
        if (this.handleView != null) {
            this.imgGo.setOnClickListener(this);
            this.imgBack.setOnClickListener(this);
        }
    }

    public void initViewStubViews(@NonNull View view) {
        this.imgGo = view.findViewById(R.id.img_ai_follow_go);
        this.imgBack = view.findViewById(R.id.img_ai_follow_back);
        this.tvTitle = view.findViewById(R.id.tv_title);
        this.vModeImtes = view.findViewById(R.id.v_mode_item);
        this.vSpeedContainer = view.findViewById(R.id.v_lock_mode_speed);
        this.mTipBgView = view.findViewById(R.id.v_content_tip);
        this.mTipBgView.setVisibility(View.VISIBLE);
        if (this.type == 0) {
            this.mTipBgView.setTipText(getString(R.string.x8_ai_fly_follow_normal_tip));
        } else if (this.type == 1) {
            this.mTipBgView.setTipText(getString(R.string.x8_ai_fly_follow_parallel_tip));
        } else if (this.type == 2) {
            this.mTipBgView.setTipText(getString(R.string.x8_ai_fly_follow_lockup_tip));
        }
        this.vModeImtes.setListener(this);
        this.vSpeedContainer.setOnSendSpeedListener(this);
    }

    public void setTitle() {
        String s = "";
        int res = 0;
        switch (this.type) {
            case 0:
                s = getString(R.string.x8_ai_fly_follow_normal);
                res = R.drawable.x8_img_ai_follow_normal1_small;
                break;
            case 1:
                s = getString(R.string.x8_ai_fly_follow_parallel);
                res = R.drawable.x8_img_ai_follow_parallel1_small;
                break;
            case 2:
                s = getString(R.string.x8_ai_fly_follow_lockup);
                res = R.drawable.x8_img_ai_follow_lockup1_small;
                break;
        }
        this.vModeImtes.findIndexByMode(this.type);
        this.tvTitle.setText(s);
        this.imgSmall.setBackgroundResource(res);
    }

    @Override
    public void openUi() {
        this.isShow = true;
        LayoutInflater inflater = LayoutInflater.from(this.rootView.getContext());
        this.handleView = inflater.inflate(R.layout.x8_ai_follow_excute_layout, (ViewGroup) this.rootView, true);
        this.imgSmall = this.handleView.findViewById(R.id.img_ai_flag_small);
        this.flagSmall = this.handleView.findViewById(R.id.rl_flag_small);
        this.flagSmall.setOnClickListener(this);
        initViewStubViews(this.handleView);
        initActions();
        if (this.mX8AiFollowState == X8AiFollowState.IDLE) {
            this.vModeImtes.setVisibility(View.GONE);
            this.vSpeedContainer.setVisibility(View.GONE);
            this.vModeImtes.findIndexByMode(this.type);
            setTitle();
            this.mX8AiFollowState = X8AiFollowState.OEPNVIEW;
            this.isGetMode = true;
        } else if (this.mX8AiFollowState == X8AiFollowState.RUNNING) {
            this.listener.onAiFollowRunning();
            this.isShowGo = true;
        }
        this.activity.getmX8AiTrackController().setOnAiTrackControllerListener(this);
        this.activity.getmX8AiTrackController().openUi();
        if (this.mX8AiFollowState == X8AiFollowState.RUNNING) {
            this.activity.getmX8AiTrackController().setTou(true);
        }
        super.openUi();
    }

    @Override
    public void closeUi() {
        super.closeUi();
        this.activity.getmX8AiTrackController().closeUi();
        this.mX8AiFollowState = X8AiFollowState.IDLE;
    }

    @Override
    public void defaultVal() {
    }

    @Override
    public void onClick(@NonNull View v) {
        int id = v.getId();
        if (id == R.id.img_ai_follow_go) {
            onGoClick();
        } else if (id == R.id.img_ai_follow_back) {
            showExitDialog();
        } else if (id == R.id.rl_flag_small) {
            if (this.tvTitle.getVisibility() == View.VISIBLE) {
                this.tvTitle.setVisibility(View.GONE);
            } else {
                this.tvTitle.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onTouchActionDown() {
        this.isTou = false;
        this.imgGo.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onTouchActionUp() {
        this.isTou = true;
        setAiVcNotityFc();
    }

    @Override
    public void onChangeGoLocation(float left, float right, float top, float bottom, int w, int h) {
        int l;
        int t;
        if (this.isShowGo || right - left <= 5.0f) {
            if (left == right && top == bottom && top == right && right == 0.0f) {
                this.imgGo.setVisibility(View.INVISIBLE);
                return;
            }
            return;
        }
        if (this.goWidth == 0) {
            this.goWidth = this.imgGo.getWidth();
            this.goHeight = this.imgGo.getHeight();
        }
        PercentRelativeLayout.LayoutParams lp = new PercentRelativeLayout.LayoutParams(this.imgGo.getLayoutParams());
        boolean inside = false;
        if (this.goWidth <= left) {
            l = (int) (left - this.goWidth);
        } else if (Math.ceil(right) + this.goWidth <= w) {
            l = (int) Math.ceil(right);
        } else {
            l = (int) Math.ceil(left);
            inside = true;
        }
        if (bottom <= this.goHeight) {
            t = 0;
        } else if (Math.ceil(top) + this.goHeight >= h) {
            t = h - this.goHeight;
        } else if (inside) {
            t = (int) (bottom - this.goHeight);
        } else {
            t = (int) ((((bottom - top) / 2.0f) + top) - (this.goHeight / 2.0f));
        }
        lp.setMargins(l, t, 0, 0);
        this.imgGo.setLayoutParams(lp);
        if (this.mX8AiFollowState != X8AiFollowState.IDLE) {
            this.imgGo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setGoEnabled(boolean b) {
    }

    public void setAiVcNotityFc() {
        this.activity.getFcManager().setAiVcNotityFc((cmdResult, o) -> {
            TcpClient.getIntance().sendLog("----->...setAiVcNotityFc..." + cmdResult.getErrDes());
            if (cmdResult.isSuccess()) {
            }
        });
    }

    @Override
    public void onTracking() {
        if (this.isShow && this.isTou && this.isShowGo && this.imgGo.getVisibility() == View.VISIBLE) {
            this.imgGo.setVisibility(View.INVISIBLE);
        }
    }

    public void onGoClick() {
        setModle(this.type);
    }

    public void doFollow() {
        this.activity.getFcManager().setFollowExcute((cmdResult, o) -> {
            if (cmdResult.isSuccess()) {
                listener.onAiFollowRunning();
                mTipBgView.setVisibility(View.GONE);
                activity.getmMapVideoController().resetShow();
                mX8AiFollowState = X8AiFollowState.RUNNING;
                isShowGo = true;
                TcpClient.getIntance().sendLog("..setFollowExcute.. " + isShowGo + cmdResult.getErrDes());
                return;
            }
            TcpClient.getIntance().sendLog("..onGoClick.. " + isShowGo + cmdResult.getErrDes());
        });
    }

    public void setModle(final int mode) {
        this.activity.getFcManager().setAiFollowModle(mode, (cmdResult, o) -> {
            TcpClient.getIntance().sendLog("..setModle.. " + cmdResult.getErrDes() + " mode=" + X8AiFollowExcuteController.this.type);
            if (cmdResult.isSuccess()) {
                if (isSwitchMode) {
                    type = mode;
                    isSwitchMode = false;
                    setTitle();
                    if (mode != 2) {
                        vSpeedContainer.setVisibility(View.GONE);
                        return;
                    }
                    return;
                }
                doFollow();
            }
        });
    }

    public void getFollowMode() {
        this.activity.getFcManager().getAiFollowModle((UiCallBackListener<AckGetAiFollowMode>) (cmdResult, ackGetAiFollowMode) -> {
            if (cmdResult.isSuccess()) {
                isGetMode = true;
                TcpClient.getIntance().sendLog("..getFollowMode.. " + cmdResult.getErrDes() + " mode=" + ackGetAiFollowMode.getMode());
                type = ackGetAiFollowMode.getMode();
                setTitle();
                vModeImtes.findIndexByMode(X8AiFollowExcuteController.this.type);
                return;
            }
            TcpClient.getIntance().sendLog(" ..getFollowMode..  " + cmdResult.getErrDes());
        });
    }

    public void getFollowSpeed() {
        this.activity.getFcManager().getAiFollowSpeed((UiCallBackListener<AckAiFollowGetSpeed>) (cmdResult, o) -> {
            if (cmdResult.isSuccess()) {
                isGetSpeed = true;
                TcpClient.getIntance().sendLog("..getFollowSpeed.. " + cmdResult.getErrDes() + " speed=" + o.getSpeed());
                vSpeedContainer.setSpeed(o.getSpeed());
            }
        });
    }

    public void cancleByModeChange(int mode) {
        onTaskComplete(mode == 1);
        setAiVcCloseByTaskModeChange();
    }

    public void onTaskComplete(boolean b) {
        closeFollow();
        String s = "";
        switch (this.type) {
            case 0:
                s = String.format(getString(R.string.x8_ai_done), getString(R.string.x8_ai_fly_follow_normal));
                break;
            case 1:
                s = String.format(getString(R.string.x8_ai_done), getString(R.string.x8_ai_fly_follow_parallel));
                break;
            case 2:
                s = String.format(getString(R.string.x8_ai_done), getString(R.string.x8_ai_fly_follow_lockup));
                break;
        }
        if (this.listener != null) {
            this.listener.onComplete(s, b);
        }
    }

    private void closeFollow() {
        closeUi();
        if (this.listener != null) {
            this.listener.onAiFollowExcuteBackClick();
        }
    }

    public void onDisconnectTaskComplete() {
        closeFollow();
        String s = "";
        switch (this.type) {
            case 0:
                s = String.format(getString(R.string.x8_ai_done), getString(R.string.x8_ai_fly_follow_normal));
                break;
            case 1:
                s = String.format(getString(R.string.x8_ai_done), getString(R.string.x8_ai_fly_follow_parallel));
                break;
            case 2:
                s = String.format(getString(R.string.x8_ai_done), getString(R.string.x8_ai_fly_follow_lockup));
                break;
        }
        if (this.listener != null) {
            this.listener.onComplete(s, false);
        }
    }

    public void showExitDialog() {
        String t = "";
        String m = getString(R.string.x8_ai_fly_follow_exit_msg);
        switch (this.type) {
            case 0:
                t = getString(R.string.x8_ai_fly_follow_normal);
                break;
            case 1:
                t = getString(R.string.x8_ai_fly_follow_parallel);
                break;
            case 2:
                t = getString(R.string.x8_ai_fly_follow_lockup);
                break;
        }
        this.dialog = new X8DoubleCustomDialog(this.rootView.getContext(), t, m, this);
        this.dialog.show();
    }

    @Override
    public void onLeft() {
    }

    @Override
    public void onRight() {
        sendExiteCmd();
    }

    public void taskExit() {
        onTaskComplete(false);
    }

    public void sendExiteCmd() {
        this.activity.getFcManager().setAiVcClose((cmdResult, o) -> {
            if (cmdResult.isSuccess()) {
                TcpClient.getIntance().sendLog("setAiVcClose success  " + cmdResult.getErrDes() + mX8AiFollowState);
                if (mX8AiFollowState != X8AiFollowState.RUNNING) {
                    taskExit();
                    return;
                }
                return;
            }
            TcpClient.getIntance().sendLog("setAiVcClose error" + cmdResult.getErrDes());
        });
    }

    public void setAiVcCloseByTaskModeChange() {
        this.activity.getFcManager().setAiVcClose((cmdResult, o) -> {
            TcpClient.getIntance().sendLog("setAiVcCloseByTaskModeChange   " + cmdResult.getErrDes());
        });
    }

    public void setFollowExit(int mode) {
        this.activity.getFcManager().setFollowExit((cmdResult, o) -> {
            if (cmdResult.isSuccess()) {
                TcpClient.getIntance().sendLog("退出跟随 success  " + cmdResult.getErrDes());
            } else {
                TcpClient.getIntance().sendLog("退出跟随失败  " + cmdResult.getErrDes());
            }
        });
    }

    @Override
    public void onDroneConnected(boolean b) {
        if (this.isShow) {
            if (!b) {
                onDisconnectTaskComplete();
                return;
            }
            if (this.mX8AiFollowState == X8AiFollowState.RUNNING) {
                if (!this.isGetMode) {
                    getFollowMode();
                } else if (this.vModeImtes.getVisibility() != View.VISIBLE) {
                    this.vModeImtes.setVisibility(View.VISIBLE);
                }
                if (this.isGetSpeed && this.isGetMode && this.type == 2 && this.vSpeedContainer.getVisibility() != View.VISIBLE) {
                    this.vSpeedContainer.setVisibility(View.VISIBLE);
                }
            } else {
                sysAiVcCtrlMode();
            }
            if (!this.isGetSpeed) {
                getFollowSpeed();
            }
        }
    }

    @Override
    public void onModeSelect(int mode) {
        TcpClient.getIntance().sendLog("onModeSelect success  " + mode);
        this.isSwitchMode = true;
        setModle(mode);
    }

    @Override
    public void onSendSpeed(int speed) {
        setFollowSpeed(speed);
    }

    public void setFollowSpeed(final int speed) {
        this.activity.getFcManager().setAiFollowSpeed(speed, (cmdResult, o) -> {
            TcpClient.getIntance().sendLog("..setFollowSpeed.. " + cmdResult.getErrDes() + " speed=" + speed);
            if (cmdResult.isSuccess()) {
            }
        });
    }

    public void switchUnityEvent() {
        if (this.isShow) {
            this.vSpeedContainer.switchUnity();
        }
    }

    @Override
    public boolean onClickBackKey() {
        return false;
    }

    public void sysAiVcCtrlMode() {
        if (this.mX8AiFollowState != X8AiFollowState.RUNNING) {
            if (this.timeSend == 0) {
                this.timeSend = 1;
                this.activity.getFcManager().sysCtrlMode2AiVc((cmdResult, o) -> {
                }, X8Task.VCM_FOLLOW.ordinal());
                return;
            }
            this.timeSend = 0;
        }
    }
}
