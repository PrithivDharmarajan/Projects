package com.calix.calixgigaspireapp.ui.settings;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
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
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.DialogManager;
import com.calix.calixgigaspireapp.utils.MinMaxFilter;
import com.calix.calixgigaspireapp.utils.NumberUtil;
import com.calix.calixgigaspireapp.utils.PreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SetPinAndResetPin extends BaseActivity {

    @BindView(R.id.set_reset_pin_parent_lay)
    ViewGroup mRegViewGroup;

    @BindView(R.id.set_reset_pin_header_bg_lay)
    RelativeLayout mRegHeaderBgLay;

    @BindView(R.id.set_reset_pin_header_img)
    ImageView mRegHeaderMsgLay;


    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.type_current_pin_lay)
    RelativeLayout mTypeCurrentPinLay;

    @BindView(R.id.type_current_pin_edt)
    EditText mTypeCurrentPinEdt;

    @BindView(R.id.type_current_pin_visible_img)
    ImageView mTypeCurrentPinVisibleImg;

    @BindView(R.id.type_new_pin_edt)
    EditText mTypeNewPinEdt;

    @BindView(R.id.type_new_pin_visible_img)
    ImageView mTypeNewtPinVisibleImg;

    @BindView(R.id.type_confirm_pin_edt)
    EditText mTypeConfirmPinEdt;

    @BindView(R.id.type_confirm_pin_visible_img)
    ImageView mTypeConfirmPinVisibleImg;

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

        mTypeCurrentPinVisibleImg.setTag(1);
        mTypeNewtPinVisibleImg.setTag(1);
        mTypeConfirmPinVisibleImg.setTag(1);

    }

    private void setHeaderView() {

        /*set header changes*/
        String headerStr = getString(PreferenceUtil.getBoolPreferenceValue(this, AppConstants.LOGIN_PIN_PWD_STATUS) ? R.string.reset_pin : R.string.set_pin);
        mRegHeaderMsgLay.setVisibility(IsScreenModePortrait() ? View.VISIBLE : View.GONE);
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(headerStr);

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mRegHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(IsScreenModePortrait() ? R.dimen.size190 : R.dimen.size45);
                    mRegHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(SetPinAndResetPin.this)));
                    mRegHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(SetPinAndResetPin.this), 0, 0);
                    mRegHeaderBgLay.setBackground(IsScreenModePortrait() ? getResources().getDrawable(R.drawable.header_bg) : getResources().getDrawable(R.color.blue));
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
    @OnClick({R.id.header_left_img_lay, R.id.type_current_pin_visible_img,R.id.type_new_pin_visible_img,R.id.type_confirm_pin_visible_img,R.id.submit_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;
            case R.id.type_current_pin_visible_img:
                int currentPinSelectedInt=mTypeCurrentPinEdt.getText().toString().length();
                mTypeCurrentPinEdt.setTransformationMethod(mTypeCurrentPinVisibleImg.getTag().equals(1)?null:new PasswordTransformationMethod());
                mTypeCurrentPinEdt.setSelection(currentPinSelectedInt);
                mTypeCurrentPinVisibleImg.setImageResource(mTypeCurrentPinVisibleImg.getTag().equals(1)?R.drawable.visible:R.drawable.in_visible);
                mTypeCurrentPinVisibleImg.setTag(mTypeCurrentPinVisibleImg.getTag().equals(1)?0:1);
                break;
            case R.id.type_new_pin_visible_img:
                int newPinSelectedInt=mTypeNewPinEdt.getText().toString().length();
                mTypeNewPinEdt.setTransformationMethod(mTypeNewtPinVisibleImg.getTag().equals(1)?null:new PasswordTransformationMethod());
                mTypeNewPinEdt.setSelection(newPinSelectedInt);
                mTypeNewtPinVisibleImg.setImageResource(mTypeNewtPinVisibleImg.getTag().equals(1)?R.drawable.visible:R.drawable.in_visible);
                mTypeNewtPinVisibleImg.setTag(mTypeNewtPinVisibleImg.getTag().equals(1)?0:1);
                break;
            case R.id.type_confirm_pin_visible_img:
                int confirmPinSelectedInt=mTypeConfirmPinEdt.getText().toString().length();
                mTypeConfirmPinEdt.setTransformationMethod(mTypeConfirmPinVisibleImg.getTag().equals(1)?null:new PasswordTransformationMethod());
                mTypeConfirmPinEdt.setSelection(confirmPinSelectedInt);
                mTypeConfirmPinVisibleImg.setImageResource(mTypeConfirmPinVisibleImg.getTag().equals(1)?R.drawable.visible:R.drawable.in_visible);
                mTypeConfirmPinVisibleImg.setTag(mTypeConfirmPinVisibleImg.getTag().equals(1)?0:1);
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