package com.smaat.virtualtrainer.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.smaat.virtualtrainer.R;
import com.smaat.virtualtrainer.apiinterface.APIRequestHandler;
import com.smaat.virtualtrainer.main.BaseActivity;
import com.smaat.virtualtrainer.model.UpdateProfileEntity;
import com.smaat.virtualtrainer.utils.AppConstants;
import com.smaat.virtualtrainer.utils.DialogManager;
import com.smaat.virtualtrainer.utils.DialogMangerTwoBtnCallback;
import com.smaat.virtualtrainer.utils.GlobalMethods;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tyrantgit.explosionfield.ExplosionField;

import static com.smaat.virtualtrainer.utils.GlobalMethods.resetExplosionFields;


public class ProfileScreen extends BaseActivity implements DialogMangerTwoBtnCallback {

    @BindView(R.id.parent_lay)
    ViewGroup mProfileViewGroup;

    @BindView(R.id.name_edt)
    EditText mNameEdt;

    @BindView(R.id.mail_id_edt)
    EditText mMailIdEdt;

    @BindView(R.id.pwd_edt)
    EditText mPwdEdt;

    @BindView(R.id.update_btn)
    Button mUpdateBtn;

    private String mPwdStr;
    private ExplosionField mExplosionField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ui_profile_screen);
        ButterKnife.bind(this);
        mUpdateBtn.setTag(AppConstants.FAILURE_CODE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initComponent();
        setData();
    }


    private void initComponent() {
        setupUI(mProfileViewGroup);

        mExplosionField = ExplosionField.attach2Window(this);

        mHeaderLeftBtnLay.setVisibility(View.VISIBLE);
        mHeaderRightBtnLay.setVisibility(View.VISIBLE);

        mHeaderTxt.setText(getString(R.string.profile));

    }

    @OnClick({R.id.header_left_btn_lay, R.id.header_right_btn_lay, R.id.update_btn, R.id.subscribe_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.header_right_btn_lay:
                if (mUpdateBtn.getTag().equals(AppConstants.FAILURE_CODE)) {
                    mUpdateBtn.setTag(AppConstants.SUCCESS_CODE);
                    mUpdateBtn.setVisibility(View.VISIBLE);
                    setFieldsMode(Integer.valueOf(GlobalMethods.getStringValue(this, AppConstants.LOGIN_TYPE)));
                } else {
                    mUpdateBtn.setTag(AppConstants.FAILURE_CODE);
                    mUpdateBtn.setVisibility(View.GONE);
                    setFieldsMode(0);
                }

                break;
            case R.id.update_btn:
                validateFields();

                break;

            case R.id.subscribe_btn:
                DialogManager.showBasicAlertDialog(this, getString(R.string.future_release), getString(R.string.ok), false, getString(R.string.ok), true, true, ProfileScreen.this);
                break;

        }
    }


    private void validateFields() {
        final String nameStr = mNameEdt.getText().toString().trim();
        mPwdStr = mPwdEdt.getText().toString().trim();

        if (nameStr.isEmpty()) {
            mNameEdt.requestFocus();
            DialogManager.showBasicAlertDialog(this, getString(R.string.enter_email), getString(R.string.ok), false, getString(R.string.ok), true, true, ProfileScreen.this);
        } else if (mPwdStr.isEmpty() && (GlobalMethods.getStringValue(this, AppConstants.LOGIN_TYPE).equalsIgnoreCase
                (AppConstants.SUCCESS_CODE))) {
            mPwdEdt.requestFocus();
            DialogManager.showBasicAlertDialog(this, getString(R.string.enter_pwd), getString(R.string.ok), false, getString(R.string.ok), true, true, ProfileScreen.this);
        } else {
            mExplosionField.explode(mUpdateBtn);
            APIRequestHandler.getInstance().updateProfileAPICall(nameStr, mPwdStr,
                    GlobalMethods.getStringValue(ProfileScreen.this, AppConstants.LOGIN_TYPE), ProfileScreen.this);

        }


    }

    private void setData() {

        mNameEdt.setText(GlobalMethods.getStringValue(this, AppConstants.USER_NAME));
        mMailIdEdt.setText(GlobalMethods.getStringValue(this, AppConstants.EMAIL_ADDRESS));
        mPwdEdt.setText(GlobalMethods.getStringValue(this, AppConstants.PASSWORD));
        setFieldsMode(0);
    }

    private void setFieldsMode(int loginTypeInt) {

        mNameEdt.setFocusable(false);
        mNameEdt.setFocusableInTouchMode(false);
        mNameEdt.setClickable(false);

        mMailIdEdt.setFocusable(false);
        mMailIdEdt.setFocusableInTouchMode(false);
        mMailIdEdt.setClickable(false);

        mPwdEdt.setFocusable(false);
        mPwdEdt.setFocusableInTouchMode(false);
        mPwdEdt.setClickable(false);

        if (loginTypeInt == 1) {
            mNameEdt.setFocusable(true);
            mNameEdt.setFocusableInTouchMode(true);
            mNameEdt.setClickable(true);

            mPwdEdt.setFocusable(true);
            mPwdEdt.setFocusableInTouchMode(true);
            mPwdEdt.setClickable(true);
        } else if (loginTypeInt > 1) {
            mNameEdt.setFocusable(true);
            mNameEdt.setFocusableInTouchMode(true);
            mNameEdt.setClickable(true);
        }

    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);

        if (responseObj instanceof UpdateProfileEntity) {
            final UpdateProfileEntity updateProfileEntityRes = (UpdateProfileEntity) responseObj;

            if (updateProfileEntityRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                GlobalMethods.userDetails(AppConstants.SUCCESS_CODE, updateProfileEntityRes.getResult().getSvt_user_id(), updateProfileEntityRes.getResult().getUser_name(), updateProfileEntityRes.getResult().getEmail_id(), mPwdStr, updateProfileEntityRes.getResult().getLogin_type(), ProfileScreen.this);

                DialogManager.showBasicAlertDialog(ProfileScreen.this, updateProfileEntityRes.getMessage(), getString(R.string.ok), false, getString(R.string.ok), true, true, new DialogMangerTwoBtnCallback() {
                    @Override
                    public void onYesClick() {
                        onBackPressed();
                    }

                    @Override
                    public void onNoClick() {
                        onBackPressed();
                    }
                });

            } else {

                mExplosionField.clear();
                resetExplosionFields(mProfileViewGroup);
                DialogManager.showBasicAlertDialog(this, updateProfileEntityRes.getMessage(), getString(R.string.ok), false, getString(R.string.ok), true, true, this);

            }
        }
    }

    @Override
    public void onRequestFailure(Throwable t) {
        super.onRequestFailure(t);
        mExplosionField.clear();
        resetExplosionFields(mProfileViewGroup);
    }

    @Override
    public void onBackPressed() {
        finishScreen();
    }

    @Override
    public void onYesClick() {

    }

    @Override
    public void onNoClick() {

    }
}
