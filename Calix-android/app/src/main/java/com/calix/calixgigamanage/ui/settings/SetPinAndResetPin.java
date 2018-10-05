package com.calix.calixgigamanage.ui.settings;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
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
import com.calix.calixgigamanage.utils.AppConstants;
import com.calix.calixgigamanage.utils.DialogManager;
import com.calix.calixgigamanage.utils.NumberUtil;
import com.calix.calixgigamanage.utils.PreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SetPinAndResetPin extends BaseActivity {

    @BindView(R.id.set_reset_pin_parent_lay)
    ViewGroup mRegViewGroup;

    @BindView(R.id.set_reset_pin_header_bg_lay)
    RelativeLayout mRegHeaderBgLay;

    @BindView(R.id.set_reset_pin_header_msg_lay)
    RelativeLayout mRegHeaderMsgLay;

    @BindView(R.id.set_reset_pin_header_txt)
    TextView mSetResetHeaderTxt;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.type_current_pin_lay)
    TextInputLayout mTypeCurrentPinLay;

    @BindView(R.id.type_current_pin_edt)
    EditText mTypeCurrentPinEdt;

    @BindView(R.id.type_new_pin_edt)
    EditText mTypeNewPinEdt;

    @BindView(R.id.type_confirm_pin_edt)
    EditText mTypeConfirmPinEdt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_set_pin_reset_pin);
        initView();
    }


    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mRegViewGroup);

        /*Keypad button action*/
        mTypeConfirmPinEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 100 || actionId == EditorInfo.IME_ACTION_DONE) {
                    validateFields();
                }
                return true;
            }
        });

        mTypeCurrentPinLay.setVisibility(PreferenceUtil.getBoolPreferenceValue(this, AppConstants.LOGIN_PIN_PWD_STATUS) ? View.VISIBLE : View.GONE);

        setHeaderView();
    }

    private void setHeaderView() {

        /*set header changes*/
        String headerStr = getString(PreferenceUtil.getBoolPreferenceValue(this, AppConstants.LOGIN_PIN_PWD_STATUS) ? R.string.reset_pin : R.string.set_pin);
        mRegHeaderMsgLay.setVisibility(IsScreenModePortrait() ? View.VISIBLE : View.GONE);
        mHeaderTxt.setVisibility(IsScreenModePortrait() ? View.INVISIBLE : View.VISIBLE);
        mHeaderTxt.setText(headerStr);
        mSetResetHeaderTxt.setText(headerStr);

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mRegHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(IsScreenModePortrait() ? R.dimen.size190 : R.dimen.size45);
                    mRegHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(SetPinAndResetPin.this)));
                    mRegHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(SetPinAndResetPin.this), 0, 0);
                    mRegHeaderBgLay.setBackground(IsScreenModePortrait() ? getResources().getDrawable(R.drawable.header_violet_bg) : getResources().getDrawable(R.color.violet));
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
    @OnClick({R.id.header_left_img_lay, R.id.submit_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;
            case R.id.submit_btn:
                validateFields();
                break;
        }
    }

    /*validate fields*/
    private void validateFields() {
        hideSoftKeyboard(this);
        boolean alreadyPinBool = PreferenceUtil.getBoolPreferenceValue(this, AppConstants.LOGIN_PIN_PWD_STATUS);
        String typeCurrentPinStr = mTypeCurrentPinEdt.getText().toString().trim();
        String typeNewPinStr = mTypeNewPinEdt.getText().toString().trim();
        String typeConfirmPinStr = mTypeConfirmPinEdt.getText().toString().trim();

        if (alreadyPinBool && typeCurrentPinStr.isEmpty()) {
            mTypeCurrentPinEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_pin), this);
        } else if (alreadyPinBool && !typeCurrentPinStr.equals(PreferenceUtil.getStringValue(this, AppConstants.LOGIN_PIN_PWD))) {
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.incorrect_pin), this);
        } else if (typeNewPinStr.isEmpty()) {
            mTypeNewPinEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_pin), this);
        } else if (typeNewPinStr.length() < 4) {
            mTypeNewPinEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.pin_contains_four_char), this);
        } else if (!typeNewPinStr.equals(typeConfirmPinStr)) {
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.pin_does_not_match), this);
        } else {
            PreferenceUtil.storeBoolPreferenceValue(this, AppConstants.LOGIN_PIN_PWD_STATUS, true);
            PreferenceUtil.storeStringValue(this, AppConstants.LOGIN_PIN_PWD, typeConfirmPinStr);
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        backScreen();
    }
}