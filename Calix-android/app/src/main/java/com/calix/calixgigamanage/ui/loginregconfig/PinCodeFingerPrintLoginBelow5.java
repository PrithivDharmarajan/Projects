package com.calix.calixgigamanage.ui.loginregconfig;

import android.content.res.Configuration;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.ui.dashboard.Dashboard;
import com.calix.calixgigamanage.utils.AppConstants;
import com.calix.calixgigamanage.utils.DialogManager;
import com.calix.calixgigamanage.utils.NumberUtil;
import com.calix.calixgigamanage.utils.PreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PinCodeFingerPrintLoginBelow5 extends BaseActivity {

    @BindView(R.id.pin_finger_parent_lay)
    ViewGroup mPinFingerViewGroup;

    @BindView(R.id.pin_finger_header_bg_lay)
    RelativeLayout mPinFingerHeaderBgLay;

    @BindView(R.id.pin_finger_header_msg_lay)
    RelativeLayout mPinFingerHeaderMsgLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.header_left_img_lay)
    RelativeLayout mHeaderLeftImgLay;

    @BindView(R.id.pass_code_edt)
    EditText mPassCodeEdt;

    @BindView(R.id.touch_id_lay)
    LinearLayout mTouchIdLay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ui_pin_fingure_print_login);
        initView();
    }




    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mPinFingerViewGroup);

        /*Keypad button action*/
        mPassCodeEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 100 || actionId == EditorInfo.IME_ACTION_GO) {
                    validateFields();
                }
                return true;
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            mTouchIdLay.setVisibility(fingerprintManager != null && fingerprintManager.isHardwareDetected() && PreferenceUtil.getBoolPreferenceValue(this, AppConstants.TOUCH_ID_ENABLE_STATUS) ? View.VISIBLE : View.GONE);
        } else {
            mTouchIdLay.setVisibility(View.GONE);
        }


        setHeaderView();
    }


    private void setHeaderView() {
        /*set header changes*/
        mPinFingerHeaderMsgLay.setVisibility(IsScreenModePortrait() ? View.VISIBLE : View.GONE);
        mHeaderTxt.setVisibility(IsScreenModePortrait() ? View.INVISIBLE : View.VISIBLE);
        mHeaderLeftImgLay.setVisibility(View.INVISIBLE);
        mHeaderTxt.setText(getString(R.string.welcome));

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mPinFingerHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(IsScreenModePortrait() ? R.dimen.size190 : R.dimen.size45);
                    mPinFingerHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(PinCodeFingerPrintLoginBelow5.this)));
                    mPinFingerHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(PinCodeFingerPrintLoginBelow5.this), 0, 0);
                    mPinFingerHeaderBgLay.setBackground(IsScreenModePortrait() ? getResources().getDrawable(R.drawable.header_violet_bg) : getResources().getDrawable(R.color.violet));
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


    /*validate fields*/
    private void validateFields() {
        hideSoftKeyboard(PinCodeFingerPrintLoginBelow5.this);
        String passCodeStr = mPassCodeEdt.getText().toString().trim();


        if (passCodeStr.isEmpty()) {
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_pin), this);
        } else if (!passCodeStr.equals(PreferenceUtil.getStringValue(this, AppConstants.LOGIN_PIN_PWD))) {
            mPassCodeEdt.setSelection(passCodeStr.length());

            String hintMsg=getString(R.string.incorrect_pin)+"\n"+getString(R.string.incorrect_pin_note);
            DialogManager.getInstance().showAlertPopup(this, hintMsg, this);
        } else {
            Class<?> nextScreenClass = Login.class;

            if (PreferenceUtil.getBoolPreferenceValue(this, AppConstants.LOGIN_STATUS)) {
                nextScreenClass = Dashboard.class;
            }
            DialogManager.getInstance().showToast(PinCodeFingerPrintLoginBelow5.this, getString(R.string.logged_success));
            nextScreen(nextScreenClass);
        }
    }


}