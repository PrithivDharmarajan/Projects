package com.calix.calixgigaspireapp.ui.settings;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.main.BaseActivity;
import com.calix.calixgigaspireapp.output.model.CommonResponse;
import com.calix.calixgigaspireapp.services.APIRequestHandler;
import com.calix.calixgigaspireapp.utils.DialogManager;
import com.calix.calixgigaspireapp.utils.InterfaceBtnCallback;
import com.calix.calixgigaspireapp.utils.NumberUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ChangeLoginPwd extends BaseActivity {

    @BindView(R.id.change_login_pwd_parent_lay)
    ViewGroup mChangeLoginPwdViewGroup;

    @BindView(R.id.change_login_pwd_header_bg_lay)
    RelativeLayout mChangeLoginPwdHeaderBgLay;

    @BindView(R.id.change_login_pwd_header_img)
    ImageView mChangeLoginPwdHeaderImg;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.old_pwd_edt)
    EditText mOldPwdEdt;

    @BindView(R.id.old_pwd_visible_img)
    ImageView mOldPwdVisibleImg;

    @BindView(R.id.new_pwd_edt)
    EditText mNewPwdEdt;

    @BindView(R.id.new_pwd_visible_img)
    ImageView mNewPwdVisibleImg;

    @BindView(R.id.confirm_pwd_edt)
    EditText mConfirmPwdEdt;

    @BindView(R.id.confirm_pwd_visible_img)
    ImageView mConfirmPwdVisibleImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_change_login_pwd);
        initView();
    }


    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mChangeLoginPwdViewGroup);

        /*Keypad button action*/
        mConfirmPwdEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 100 || actionId == EditorInfo.IME_ACTION_DONE) {
                    validateFields();
                }
                return true;
            }
        });


        setHeaderView();

        mOldPwdVisibleImg.setTag(1);
        mNewPwdVisibleImg.setTag(1);
        mConfirmPwdVisibleImg.setTag(1);
    }

    private void setHeaderView() {

        /*set header changes*/
        String headerStr = getString(R.string.change_pwd);
        mChangeLoginPwdHeaderImg.setVisibility(IsScreenModePortrait() ? View.VISIBLE : View.GONE);
        mHeaderTxt.setVisibility( View.VISIBLE);
        mHeaderTxt.setText(headerStr);

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mChangeLoginPwdHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(IsScreenModePortrait() ? R.dimen.size190 : R.dimen.size45);
                    mChangeLoginPwdHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(ChangeLoginPwd.this)));
                    mChangeLoginPwdHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(ChangeLoginPwd.this), 0, 0);
                    mChangeLoginPwdHeaderBgLay.setBackground(IsScreenModePortrait() ? getResources().getDrawable(R.drawable.header_bg) : getResources().getDrawable(R.color.blue));
                }

            });
        }

    }

    /*Screen orientation Changes*/
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setHeaderView();
    }

    /*View onClick*/
    @OnClick({R.id.header_left_img_lay,R.id.old_pwd_visible_img,R.id.new_pwd_visible_img,R.id.confirm_pwd_visible_img, R.id.submit_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;
            case R.id.old_pwd_visible_img:
                int currentPinSelectedInt=mOldPwdEdt.getText().toString().length();
                mOldPwdEdt.setTransformationMethod(mOldPwdVisibleImg.getTag().equals(1)?null:new PasswordTransformationMethod());
                mOldPwdEdt.setSelection(currentPinSelectedInt);
                mOldPwdVisibleImg.setImageResource(mOldPwdVisibleImg.getTag().equals(1)?R.drawable.visible:R.drawable.in_visible);
                mOldPwdVisibleImg.setTag(mOldPwdVisibleImg.getTag().equals(1)?0:1);
                break;
            case R.id.new_pwd_visible_img:
                int confirmPinSelectedInt=mNewPwdEdt.getText().toString().length();
                mNewPwdEdt.setTransformationMethod(mNewPwdVisibleImg.getTag().equals(1)?null:new PasswordTransformationMethod());
                mNewPwdEdt.setSelection(confirmPinSelectedInt);
                mNewPwdVisibleImg.setImageResource(mNewPwdVisibleImg.getTag().equals(1)?R.drawable.visible:R.drawable.in_visible);
                mNewPwdVisibleImg.setTag(mNewPwdVisibleImg.getTag().equals(1)?0:1);
                break;
            case R.id.confirm_pwd_visible_img:
                int newPinSelectedInt=mConfirmPwdEdt.getText().toString().length();
                mConfirmPwdEdt.setTransformationMethod(mConfirmPwdVisibleImg.getTag().equals(1)?null:new PasswordTransformationMethod());
                mConfirmPwdEdt.setSelection(newPinSelectedInt);
                mConfirmPwdVisibleImg.setImageResource(mConfirmPwdVisibleImg.getTag().equals(1)?R.drawable.visible:R.drawable.in_visible);
                mConfirmPwdVisibleImg.setTag(mConfirmPwdVisibleImg.getTag().equals(1)?0:1);
                break;

            case R.id.submit_btn:
                validateFields();
                break;
        }
    }

    /*validate fields*/
    private void validateFields() {
        hideSoftKeyboard(this);
        String oldPwdStr = mOldPwdEdt.getText().toString().trim();
        String newPwdStr = mNewPwdEdt.getText().toString().trim();
        String confirmPwdStr = mConfirmPwdEdt.getText().toString().trim();

//        if (typeCurrentPinStr.isEmpty()) {
//            mOldPwdEdt.requestFocus();
//            DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_pin), this);
//        } else
        if (newPwdStr.isEmpty()) {
            mNewPwdEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.enter_pwd), this);
        } else if (newPwdStr.length() < 8) {
            mNewPwdEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.pass_contains_eight_char), this);
        } else if (!newPwdStr.equals(confirmPwdStr)) {
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.pwd_does_not_match), this);
        } else {
            updatePwdAPICall();
        }
    }

    /*Update Pwd API call*/
    private void updatePwdAPICall() {
        APIRequestHandler.getInstance().updatePwdAPICall(mNewPwdEdt.getText().toString().trim(), this);
    }


    /*API request success and failure*/
    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof CommonResponse) {
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.pwd_update_success), new InterfaceBtnCallback() {
                @Override
                public void onPositiveClick() {
                    onBackPressed();
                }
            });
        }
    }

    @Override
    public void onRequestFailure(final Object resObj, Throwable t) {
        super.onRequestFailure(resObj, t);
        if (t instanceof IOException) {
            DialogManager.getInstance().showNetworkErrorPopup(this,
                    (t instanceof java.net.ConnectException ? getString(R.string.no_internet) : getString(R.string
                            .connect_time_out)), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {
                            if (resObj instanceof CommonResponse) {
                                updatePwdAPICall();
                            }
                        }
                    });
        }
    }


    @Override
    public void onBackPressed() {
        backScreen();
    }
}