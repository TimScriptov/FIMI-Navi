package com.fimi.libperson.ui.setting;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.kernel.Constants;
import com.fimi.kernel.utils.DataValidatorUtil;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.kernel.utils.LanguageUtil;
import com.fimi.kernel.utils.StatusBarUtil;
import com.fimi.kernel.utils.ToastUtil;
import com.fimi.libperson.BasePersonActivity;
import com.fimi.libperson.ivew.ILibpersonRightApplyView;
import com.fimi.libperson.presenter.LibPersonRightApplyPresenter;
import com.fimi.libperson.widget.TitleView;
import com.fimi.widget.NetworkLoadManage;

/* loaded from: classes.dex */
public class LibPersonRightApplyActivity extends BasePersonActivity implements ILibpersonRightApplyView {
    private LibPersonRightApplyPresenter libPersonRightApplyPresenter;
    private Button libpersonBtnApplySend;
    private EditText libpersonEtEmail;
    private TextView libpersonTvApplyHint;
    private TitleView mTitleView;

    @Override // com.fimi.libperson.BasePersonActivity, com.fimi.kernel.base.BaseActivity
    public void setStatusBarColor() {
        StatusBarUtil.StatusBarLightMode(this);
    }

    @Override // com.fimi.kernel.base.BaseActivity
    protected int getContentViewLayoutID() {
        return R.layout.libperson_activity_right_apply;
    }

    @Override // com.fimi.kernel.base.BaseActivity
    public void initData() {
        initView();
    }

    private void initView() {
        this.libPersonRightApplyPresenter = new LibPersonRightApplyPresenter(this, this);
        this.mTitleView = (TitleView) findViewById(R.id.title_view);
        this.mTitleView.setTvTitle(getResources().getString(R.string.libperson_user_right));
        this.libpersonTvApplyHint = (TextView) findViewById(R.id.libperson_tv_apply_hint);
        FontUtil.changeFontLanTing(getAssets(), this.libpersonTvApplyHint);
        this.libpersonEtEmail = (EditText) findViewById(R.id.libperson_et_email);
        this.libpersonBtnApplySend = (Button) findViewById(R.id.libperson_btn_apply_send);
        sendBtnIsClick(false);
    }

    @Override // com.fimi.kernel.base.BaseActivity
    public void doTrans() {
        this.libpersonEtEmail.addTextChangedListener(new TextWatcher() { // from class: com.fimi.libperson.ui.setting.LibPersonRightApplyActivity.1
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                if (DataValidatorUtil.isEmail(LibPersonRightApplyActivity.this.libpersonEtEmail.getText().toString().trim())) {
                    LibPersonRightApplyActivity.this.sendBtnIsClick(true);
                } else {
                    LibPersonRightApplyActivity.this.sendBtnIsClick(false);
                }
            }
        });
        this.libpersonBtnApplySend.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.libperson.ui.setting.LibPersonRightApplyActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                NetworkLoadManage.show(LibPersonRightApplyActivity.this);
                LibPersonRightApplyActivity.this.libPersonRightApplyPresenter.sendEmail(LibPersonRightApplyActivity.this.libpersonEtEmail.getText().toString(), LanguageUtil.getCurrentLanguage().getInternalCoutry(), Constants.productType.name().toLowerCase());
            }
        });
    }

    @Override
    // com.fimi.libperson.BasePersonActivity, com.fimi.kernel.base.BaseActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
    }

    public void sendBtnIsClick(boolean isClick) {
        if (isClick) {
            this.libpersonBtnApplySend.setEnabled(true);
        } else {
            this.libpersonBtnApplySend.setEnabled(false);
        }
    }

    @Override // com.fimi.libperson.ivew.ILibpersonRightApplyView
    public void sendSuccess(String hint) {
        ToastUtil.showToast(this, hint, 1);
        NetworkLoadManage.dismiss();
    }

    @Override // com.fimi.libperson.ivew.ILibpersonRightApplyView
    public void sendFailure(String hint) {
        ToastUtil.showToast(this, hint, 1);
        NetworkLoadManage.dismiss();
    }
}