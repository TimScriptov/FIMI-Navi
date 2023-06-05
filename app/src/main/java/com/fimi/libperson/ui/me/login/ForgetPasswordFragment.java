package com.fimi.libperson.ui.me.login;

import android.content.Context;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.kernel.base.BaseFragment;
import com.fimi.kernel.utils.AbAppUtil;
import com.fimi.kernel.utils.DataValidatorUtil;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.libperson.ivew.IForgetPasswordView;
import com.fimi.libperson.presenter.ForgetPasswordPresenter;

/* loaded from: classes.dex */
public class ForgetPasswordFragment extends BaseFragment implements IForgetPasswordView {
    Button mBtnSendEmail;
    Button mBtnVerfication;
    EditText mEtFpEmailAccount;
    EditText mEtInputVerficationCode;
    EditText mEtNewPassword;
    EditText mEtNewPasswordAgain;
    ImageView mIvNewPasswordAgainUnified;
    ImageView mIvNewPasswordUnified;
    ImageView mIvShowPassword;
    ImageView mIvShowPasswordAgain;
    TextView mTvEmailaddress;
    TextView mTvFpHint;
    TextView mTvFpVerficationHint;
    TextView mTvTitleSubNmae;
    View mVNpDivider;
    View mVNpDividerAgain;
    View mViewDivide;
    private boolean isShowPassword;
    private boolean isShowPasswordAgain;
    private String mEmailAddressStr;
    private ForgetPasswordPresenter mForgetPasswordPresenter;
    private OnResetPasswordListerner mOnResetPasswordListerner;
    private State mState = State.EMAIL;

    @Override // com.fimi.kernel.base.BaseFragment, android.support.v4.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mOnResetPasswordListerner = (OnResetPasswordListerner) context;
    }

    @Override // com.fimi.kernel.base.BaseFragment
    public int getLayoutId() {
        return R.layout.fragment_forget_email;
    }

    @Override // com.fimi.kernel.base.BaseFragment
    protected void initData(View view) {
        this.mEtFpEmailAccount = (EditText) view.findViewById(R.id.et_fp_email_account);
        this.mEtNewPassword = (EditText) view.findViewById(R.id.et_new_password);
        this.mEtNewPasswordAgain = (EditText) view.findViewById(R.id.et_new_password_again);
        this.mViewDivide = view.findViewById(R.id.v_divide);
        this.mIvNewPasswordUnified = (ImageView) view.findViewById(R.id.iv_new_password_unified);
        this.mIvNewPasswordAgainUnified = (ImageView) view.findViewById(R.id.iv_new_password_again_unified);
        this.mEtInputVerficationCode = (EditText) view.findViewById(R.id.et_input_verfication_code);
        this.mTvFpHint = (TextView) view.findViewById(R.id.tv_fp_hint);
        this.mTvFpVerficationHint = (TextView) view.findViewById(R.id.tv_fp_verfication_hint);
        this.mBtnSendEmail = (Button) view.findViewById(R.id.btn_send_email);
        this.mBtnVerfication = (Button) view.findViewById(R.id.btn_verfication);
        this.mTvEmailaddress = (TextView) view.findViewById(R.id.tv_emailaddress);
        this.mTvTitleSubNmae = (TextView) view.findViewById(R.id.tv_title_sub_name);
        this.mVNpDivider = view.findViewById(R.id.v_np_divider);
        this.mVNpDividerAgain = view.findViewById(R.id.v_np_again_divider);
        this.mIvShowPassword = (ImageView) view.findViewById(R.id.iv_show_password);
        this.mIvShowPasswordAgain = (ImageView) view.findViewById(R.id.iv_show_password_again);
        FontUtil.changeFontLanTing(getActivity().getAssets(), this.mTvEmailaddress, this.mTvFpHint, this.mEtFpEmailAccount, this.mEtNewPasswordAgain, this.mEtNewPassword, this.mEtInputVerficationCode, this.mBtnVerfication, this.mTvTitleSubNmae);
        this.mIvNewPasswordUnified.setVisibility(8);
        this.mIvNewPasswordAgainUnified.setVisibility(8);
        this.mTvEmailaddress.setVisibility(4);
        this.mEtNewPassword.setVisibility(4);
        this.mEtNewPasswordAgain.setVisibility(4);
        this.mViewDivide.setVisibility(4);
        this.mEtInputVerficationCode.setVisibility(4);
        this.mEtFpEmailAccount.addTextChangedListener(new EditTextWatcher(this.mEtFpEmailAccount));
        this.mEtInputVerficationCode.addTextChangedListener(new EditTextWatcher(this.mEtInputVerficationCode));
        this.mEtNewPassword.addTextChangedListener(new EditTextWatcher(this.mEtNewPassword));
        this.mEtNewPasswordAgain.addTextChangedListener(new EditTextWatcher(this.mEtNewPasswordAgain));
        this.mForgetPasswordPresenter = new ForgetPasswordPresenter(this, getActivity());
        showState();
    }

    @Override // com.fimi.kernel.base.BaseFragment
    protected void doTrans() {
        OnClickListerner();
    }

    private void OnClickListerner() {
        this.mBtnSendEmail.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.libperson.ui.me.login.ForgetPasswordFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                AbAppUtil.closeSoftInput(ForgetPasswordFragment.this.mContext);
                ForgetPasswordFragment.this.mBtnSendEmail.setEnabled(false);
                if (ForgetPasswordFragment.this.mState == State.EMAIL) {
                    ForgetPasswordFragment.this.mEmailAddressStr = ForgetPasswordFragment.this.mEtFpEmailAccount.getText().toString();
                    ForgetPasswordFragment.this.mForgetPasswordPresenter.sendEmail(ForgetPasswordFragment.this.mEmailAddressStr);
                }
            }
        });
        this.mBtnVerfication.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.libperson.ui.me.login.ForgetPasswordFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                AbAppUtil.closeSoftInput(ForgetPasswordFragment.this.mContext);
                ForgetPasswordFragment.this.mBtnVerfication.setEnabled(false);
                if (ForgetPasswordFragment.this.mState == State.VERIFICATION_CODE) {
                    ForgetPasswordFragment.this.mForgetPasswordPresenter.inputVerficationCode(ForgetPasswordFragment.this.mEmailAddressStr, ForgetPasswordFragment.this.mEtInputVerficationCode.getText().toString());
                } else if (ForgetPasswordFragment.this.mState == State.NEW_PASSWORD) {
                    ForgetPasswordFragment.this.mForgetPasswordPresenter.inputPassword(ForgetPasswordFragment.this.mEmailAddressStr, ForgetPasswordFragment.this.mEtInputVerficationCode.getText().toString(), ForgetPasswordFragment.this.mEtNewPassword.getText().toString(), ForgetPasswordFragment.this.mEtNewPasswordAgain.getText().toString());
                }
            }
        });
        this.mIvShowPassword.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.libperson.ui.me.login.ForgetPasswordFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (ForgetPasswordFragment.this.isShowPassword) {
                    ForgetPasswordFragment.this.isShowPassword = false;
                    ForgetPasswordFragment.this.mEtNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ForgetPasswordFragment.this.mIvShowPassword.setImageResource(R.drawable.iv_login_email_password);
                } else {
                    ForgetPasswordFragment.this.isShowPassword = true;
                    ForgetPasswordFragment.this.mEtNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ForgetPasswordFragment.this.mIvShowPassword.setImageResource(R.drawable.iv_login_email_password_show);
                }
                ForgetPasswordFragment.this.mEtNewPassword.requestFocus();
                ForgetPasswordFragment.this.mEtNewPassword.setSelection(ForgetPasswordFragment.this.mEtNewPassword.getText().length());
            }
        });
        this.mIvShowPasswordAgain.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.libperson.ui.me.login.ForgetPasswordFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (ForgetPasswordFragment.this.isShowPasswordAgain) {
                    ForgetPasswordFragment.this.isShowPasswordAgain = false;
                    ForgetPasswordFragment.this.mEtNewPasswordAgain.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ForgetPasswordFragment.this.mIvShowPasswordAgain.setImageResource(R.drawable.iv_login_email_password);
                } else {
                    ForgetPasswordFragment.this.isShowPasswordAgain = true;
                    ForgetPasswordFragment.this.mEtNewPasswordAgain.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ForgetPasswordFragment.this.mIvShowPasswordAgain.setImageResource(R.drawable.iv_login_email_password_show);
                }
                ForgetPasswordFragment.this.mEtNewPasswordAgain.requestFocus();
                ForgetPasswordFragment.this.mEtNewPasswordAgain.setSelection(ForgetPasswordFragment.this.mEtNewPasswordAgain.getText().length());
            }
        });
    }

    @Override // com.fimi.kernel.base.BaseFragment
    protected void initMVP() {
    }

    @Override // com.fimi.libperson.ivew.IForgetPasswordView
    public void sendEmail(boolean isSuccess, String error) {
        this.mBtnSendEmail.setEnabled(true);
        if (isSuccess) {
            this.mState = State.VERIFICATION_CODE;
            this.mTvFpHint.setText(getEmailVerficationSpannableString());
            showState();
        } else if (error != null) {
            this.mTvFpHint.setText(error);
            this.mTvFpHint.setTextColor(getResources().getColor(R.color.forget_password_error_hint));
        }
    }

    @Override // com.fimi.libperson.ivew.IForgetPasswordView
    public void inputVerfication(boolean isSuccess, String error) {
        this.mBtnVerfication.setEnabled(true);
        if (isSuccess) {
            this.mState = State.NEW_PASSWORD;
            showState();
            showClickState(false);
            this.mTvFpVerficationHint.setText(R.string.login_input_password_hint);
            this.mTvFpVerficationHint.setTextColor(getResources().getColor(R.color.forget_password_hint));
        } else if (error != null) {
            this.mTvFpVerficationHint.setText(error);
            this.mTvFpVerficationHint.setTextColor(getResources().getColor(R.color.forget_password_error_hint));
        }
    }

    @Override // com.fimi.libperson.ivew.IForgetPasswordView
    public void resetPassword(boolean isSuccess, String error) {
        this.mBtnVerfication.setEnabled(true);
        if (isSuccess) {
            if (this.mOnResetPasswordListerner != null) {
                this.mEtFpEmailAccount.setText((CharSequence) null);
                this.mEtInputVerficationCode.setText((CharSequence) null);
                this.mEtNewPassword.setText((CharSequence) null);
                this.mEtNewPasswordAgain.setText((CharSequence) null);
                this.mState = State.EMAIL;
                showState();
                this.mOnResetPasswordListerner.resetSuccess();
            }
        } else if (error != null) {
            this.mTvFpVerficationHint.setText(error);
            this.mTvFpVerficationHint.setTextColor(getResources().getColor(R.color.forget_password_error_hint));
        }
    }

    private void showState() {
        if (this.mState == State.EMAIL) {
            this.mEtFpEmailAccount.setVisibility(0);
            this.mEtInputVerficationCode.setVisibility(4);
            this.mTvEmailaddress.setVisibility(8);
            this.mTvEmailaddress.setText("");
            this.mEtNewPassword.setVisibility(4);
            this.mEtNewPasswordAgain.setVisibility(4);
            this.mViewDivide.setVisibility(4);
            this.mBtnSendEmail.setVisibility(0);
            this.mBtnVerfication.setVisibility(8);
            this.mIvNewPasswordUnified.setVisibility(8);
            this.mIvNewPasswordAgainUnified.setVisibility(8);
            this.mTvFpHint.setTextColor(getResources().getColor(R.color.forget_password_hint));
            this.mTvFpHint.setText(getSpannableString());
            this.mTvFpVerficationHint.setVisibility(8);
            this.mTvFpHint.setVisibility(0);
            setIvShowPassword(false);
            if (DataValidatorUtil.isEmail(this.mEtFpEmailAccount.getText().toString().trim())) {
                showClickState(true);
            } else {
                showClickState(false);
            }
        } else if (this.mState == State.VERIFICATION_CODE) {
            this.mEtFpEmailAccount.setVisibility(4);
            this.mEtInputVerficationCode.setVisibility(0);
            this.mTvEmailaddress.setVisibility(0);
            this.mTvEmailaddress.setText(this.mEmailAddressStr);
            this.mEtNewPassword.setVisibility(4);
            this.mViewDivide.setVisibility(0);
            this.mEtNewPasswordAgain.setVisibility(4);
            this.mBtnVerfication.setText(R.string.login_ensure);
            this.mBtnSendEmail.setVisibility(8);
            this.mBtnVerfication.setVisibility(0);
            this.mIvNewPasswordUnified.setVisibility(8);
            this.mIvNewPasswordAgainUnified.setVisibility(8);
            this.mTvFpVerficationHint.setText(getEmailVerficationSpannableString());
            this.mTvFpVerficationHint.setVisibility(0);
            this.mTvFpHint.setVisibility(8);
            setIvShowPassword(false);
            if (this.mEtInputVerficationCode.getText().length() == 6) {
                showBtnVerficationClickState(true);
            } else {
                showBtnVerficationClickState(false);
            }
        } else if (this.mState == State.NEW_PASSWORD) {
            this.mEtFpEmailAccount.setVisibility(4);
            this.mEtInputVerficationCode.setVisibility(4);
            this.mTvEmailaddress.setVisibility(8);
            this.mEtNewPassword.setVisibility(0);
            this.mViewDivide.setVisibility(0);
            this.mEtNewPasswordAgain.setVisibility(0);
            this.mBtnVerfication.setText(R.string.login_reset_password);
            this.mBtnSendEmail.setVisibility(8);
            this.mBtnVerfication.setVisibility(0);
            showClickState(false);
            this.mTvFpVerficationHint.setVisibility(0);
            this.mTvFpHint.setVisibility(8);
            setIvShowPassword(true);
            if (this.mEtNewPasswordAgain.getText().toString().trim().equals(this.mEtNewPassword.getText().toString().trim()) && this.mEtNewPasswordAgain.getText().toString().length() >= 8) {
                showBtnVerficationClickState(true);
            } else {
                showBtnVerficationClickState(false);
            }
        }
    }

    public void setIvShowPassword(boolean isShowPassword) {
        this.mIvShowPassword.setVisibility(isShowPassword ? 0 : 8);
        this.mIvShowPasswordAgain.setVisibility(isShowPassword ? 0 : 8);
        this.mVNpDivider.setVisibility(isShowPassword ? 0 : 8);
        this.mVNpDividerAgain.setVisibility(isShowPassword ? 0 : 8);
    }

    public void setEmailAddress(String emailAddress) {
        if (this.mEtFpEmailAccount != null) {
            this.mEtFpEmailAccount.setText(emailAddress);
        }
    }

    public State getState() {
        return this.mState;
    }

    public void setState(State state) {
        this.mState = state;
    }

    public void setBack() {
        if (this.mState == State.EMAIL) {
            this.mEtFpEmailAccount.setText((CharSequence) null);
        } else if (this.mState == State.VERIFICATION_CODE) {
            this.mState = State.EMAIL;
            this.mEtInputVerficationCode.setText((CharSequence) null);
            this.mTvEmailaddress.setText((CharSequence) null);
            showState();
        } else if (this.mState == State.NEW_PASSWORD) {
            this.mState = State.VERIFICATION_CODE;
            this.mEtNewPassword.setText((CharSequence) null);
            this.mEtNewPasswordAgain.setText((CharSequence) null);
            showState();
        }
    }

    public void showClickState(boolean isClick) {
        if (isClick) {
            this.mBtnSendEmail.setEnabled(true);
        } else {
            this.mBtnSendEmail.setEnabled(false);
        }
    }

    public void showBtnVerficationClickState(boolean isClick) {
        if (isClick) {
            this.mBtnVerfication.setEnabled(true);
        } else {
            this.mBtnVerfication.setEnabled(false);
        }
    }

    public SpannableString getSpannableString() {
        String str1 = this.mContext.getString(R.string.login_send_email_hint1);
        String str2 = this.mContext.getString(R.string.login_send_email_hint2);
        String str3 = this.mContext.getString(R.string.login_send_email_hint3);
        SpannableString spannableString = new SpannableString(str1 + str2 + str3);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.register_agreement)), 0, str1.length(), 33);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.register_agreement)), str1.length() + str2.length(), str1.length() + str2.length() + str3.length(), 33);
        spannableString.setSpan(new ClickableSpan() { // from class: com.fimi.libperson.ui.me.login.ForgetPasswordFragment.5
            @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ForgetPasswordFragment.this.getResources().getColor(R.color.register_agreement_click));
                ds.setUnderlineText(false);
            }

            @Override // android.text.style.ClickableSpan
            public void onClick(View widget) {
            }
        }, str1.length(), str1.length() + str2.length(), 33);
        return spannableString;
    }

    public SpannableString getEmailVerficationSpannableString() {
        String str1 = this.mContext.getString(R.string.login_email_send_hint1);
        String str2 = this.mContext.getString(R.string.login_send_email_hint2);
        SpannableString spannableString = new SpannableString(str1 + str2);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.register_agreement)), 0, str1.length(), 33);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.register_agreement)), str1.length() + str2.length(), str1.length() + str2.length(), 33);
        spannableString.setSpan(new ClickableSpan() { // from class: com.fimi.libperson.ui.me.login.ForgetPasswordFragment.6
            @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ForgetPasswordFragment.this.getResources().getColor(R.color.register_agreement_click));
                ds.setUnderlineText(false);
            }

            @Override // android.text.style.ClickableSpan
            public void onClick(View widget) {
            }
        }, str1.length(), str1.length() + str2.length(), 33);
        return spannableString;
    }

    /* loaded from: classes.dex */
    public enum State {
        EMAIL,
        VERIFICATION_CODE,
        NEW_PASSWORD
    }

    /* loaded from: classes.dex */
    interface OnResetPasswordListerner {
        void resetSuccess();
    }

    /* loaded from: classes.dex */
    class EditTextWatcher implements TextWatcher {
        private EditText mEditText;

        public EditTextWatcher(EditText editText) {
            this.mEditText = editText;
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                if (R.id.et_fp_email_account == this.mEditText.getId()) {
                    if (DataValidatorUtil.isEmail(this.mEditText.getText().toString().trim())) {
                        ForgetPasswordFragment.this.showClickState(true);
                    } else {
                        ForgetPasswordFragment.this.showClickState(false);
                    }
                    ForgetPasswordFragment.this.mTvFpHint.setText(ForgetPasswordFragment.this.getSpannableString());
                } else if (R.id.et_input_verfication_code == this.mEditText.getId()) {
                    if (s.length() == 6) {
                        ForgetPasswordFragment.this.showBtnVerficationClickState(true);
                    } else {
                        ForgetPasswordFragment.this.showBtnVerficationClickState(false);
                    }
                    ForgetPasswordFragment.this.mTvFpVerficationHint.setText(ForgetPasswordFragment.this.getEmailVerficationSpannableString());
                } else if (R.id.et_new_password == this.mEditText.getId()) {
                    if (ForgetPasswordFragment.this.mEtNewPassword.getText().toString().trim().equals(ForgetPasswordFragment.this.mEtNewPasswordAgain.getText().toString().trim()) && s.length() >= 8) {
                        ForgetPasswordFragment.this.mIvNewPasswordUnified.setVisibility(0);
                        ForgetPasswordFragment.this.mIvNewPasswordAgainUnified.setVisibility(0);
                        ForgetPasswordFragment.this.setIvShowPassword(false);
                        ForgetPasswordFragment.this.showBtnVerficationClickState(true);
                    } else {
                        ForgetPasswordFragment.this.mIvNewPasswordUnified.setVisibility(8);
                        ForgetPasswordFragment.this.mIvNewPasswordAgainUnified.setVisibility(8);
                        ForgetPasswordFragment.this.setIvShowPassword(true);
                        ForgetPasswordFragment.this.showBtnVerficationClickState(false);
                    }
                    ForgetPasswordFragment.this.mTvFpVerficationHint.setText(R.string.login_input_password_hint);
                    ForgetPasswordFragment.this.mTvFpHint.setTextColor(ForgetPasswordFragment.this.getResources().getColor(R.color.forget_password_hint));
                } else if (R.id.et_new_password_again == this.mEditText.getId()) {
                    if (ForgetPasswordFragment.this.mEtNewPasswordAgain.getText().toString().trim().equals(ForgetPasswordFragment.this.mEtNewPassword.getText().toString().trim()) && s.length() >= 8) {
                        ForgetPasswordFragment.this.mIvNewPasswordUnified.setVisibility(0);
                        ForgetPasswordFragment.this.mIvNewPasswordAgainUnified.setVisibility(0);
                        ForgetPasswordFragment.this.setIvShowPassword(false);
                        ForgetPasswordFragment.this.showBtnVerficationClickState(true);
                    } else {
                        ForgetPasswordFragment.this.mIvNewPasswordUnified.setVisibility(8);
                        ForgetPasswordFragment.this.mIvNewPasswordAgainUnified.setVisibility(8);
                        ForgetPasswordFragment.this.setIvShowPassword(true);
                        ForgetPasswordFragment.this.showBtnVerficationClickState(false);
                    }
                    ForgetPasswordFragment.this.mTvFpVerficationHint.setText(R.string.login_input_password_hint);
                    ForgetPasswordFragment.this.mTvFpHint.setTextColor(ForgetPasswordFragment.this.getResources().getColor(R.color.forget_password_hint));
                }
            } else if (R.id.et_fp_email_account == this.mEditText.getId()) {
                ForgetPasswordFragment.this.mTvFpHint.setText(ForgetPasswordFragment.this.getSpannableString());
            } else if (R.id.et_input_verfication_code == this.mEditText.getId()) {
                ForgetPasswordFragment.this.showBtnVerficationClickState(false);
                ForgetPasswordFragment.this.mTvFpVerficationHint.setText(ForgetPasswordFragment.this.getEmailVerficationSpannableString());
            } else if (R.id.et_new_password == this.mEditText.getId()) {
                ForgetPasswordFragment.this.showBtnVerficationClickState(false);
                ForgetPasswordFragment.this.mTvFpVerficationHint.setText(R.string.login_input_password_hint);
                ForgetPasswordFragment.this.mTvFpVerficationHint.setTextColor(ForgetPasswordFragment.this.getResources().getColor(R.color.forget_password_hint));
            } else if (R.id.et_new_password_again == this.mEditText.getId()) {
                ForgetPasswordFragment.this.showBtnVerficationClickState(false);
                ForgetPasswordFragment.this.mTvFpVerficationHint.setText(R.string.login_input_password_hint);
                ForgetPasswordFragment.this.mTvFpVerficationHint.setTextColor(ForgetPasswordFragment.this.getResources().getColor(R.color.forget_password_hint));
            }
        }
    }
}
