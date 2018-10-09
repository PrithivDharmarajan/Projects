package com.smaat.spark.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.smaat.spark.R;
import com.smaat.spark.entity.inputEntity.LoginRegResetInputEntity;
import com.smaat.spark.main.BaseActivity;
import com.smaat.spark.model.CommonResponse;
import com.smaat.spark.services.APIRequestHandler;
import com.smaat.spark.utils.AppConstants;
import com.smaat.spark.utils.DialogManager;
import com.smaat.spark.utils.GlobalMethods;
import com.smaat.spark.utils.InterfaceBtnCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResetPwdScreen extends BaseActivity implements InterfaceBtnCallback {


    @BindView(R.id.parent_lay)
    ViewGroup mForgotPwdViewGrp;

    @BindView(R.id.email_edt)
    EditText mEmailEdt;

    @BindView(R.id.edt_img)
    ImageView mEdtImg;

    @BindView(R.id.hint_txt)
    TextView mHintTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_forgot_pwd_screen);
        ButterKnife.bind(this);
        setupUI(mForgotPwdViewGrp);
    }


    @OnClick({R.id.header_left_btn_lay, R.id.hint_txt, R.id.reset_pwd_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.hint_txt:
                if (mHintTxt.getText().toString().trim().equals(getString(R.string.use_username))) {
                    mHintTxt.setText(getString(R.string.use_email));
                    mEmailEdt.setHint(getString(R.string.username));
                    mEdtImg.setImageResource(R.drawable.user_img);
                } else {
                    mHintTxt.setText(getString(R.string.use_username));
                    mEmailEdt.setHint(getString(R.string.email));
                    mEdtImg.setImageResource(R.drawable.email_img);
                }
                break;
            case R.id.reset_pwd_btn:
                validateFields(mEmailEdt.getHint().equals(getString(R.string.email)));
                break;

        }
    }

    private void validateFields(boolean isMailId) {
        String emailStr = mEmailEdt.getText().toString().trim();

        if (emailStr.isEmpty() || (isMailId && (!GlobalMethods.isEmailValid(emailStr)))) {
            shakeAnimEdt(mEmailEdt, isMailId ? getString(R.string.enter_email) : getString(R.string.enter_name));
        } else {
            //Login API Call
            LoginRegResetInputEntity loginRegResetInputEntity = new LoginRegResetInputEntity(AppConstants.API_RESET_PWD, AppConstants.PARAMS_EMAIL_ID, emailStr);
            APIRequestHandler.getInstance().callResetPwdAPI(loginRegResetInputEntity, this);

        }

    }

    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof CommonResponse) {
            CommonResponse resetPasswordRes = (CommonResponse) resObj;
            if (resetPasswordRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                DialogManager.getInstance().showAlertPopup(this, resetPasswordRes.getMessage());
                onBackPressed();

            } else {
                DialogManager.getInstance().showAlertPopup(this, resetPasswordRes.getMessage());
            }
        }
    }

    @Override
    public void onYesClick() {

    }

    @Override
    public void onBackPressed() {
        previousScreen(LoginScreen.class, true);
    }
}
