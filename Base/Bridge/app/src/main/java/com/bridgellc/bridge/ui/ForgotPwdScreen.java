package com.bridgellc.bridge.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.apiinterface.APIRequestHandler;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.model.CommonResponse;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.GlobalMethods;

import java.util.Locale;

public class ForgotPwdScreen extends BaseActivity implements View.OnClickListener, DialogMangerOkCallback {


    EditText mFirstNameEdt, mEmailEdt;
    String mFirstName, mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword_screen);
        initComponents();
    }

    private void initComponents() {
        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);

        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        Button mResetPwdBtn = (Button) findViewById(R.id.footer_btn);


        mFirstNameEdt = (EditText) findViewById(R.id.first_name_edt);
        mEmailEdt = (EditText) findViewById(R.id.email_edt);

//        mEmailEdt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        mHeaderTxt.setText(getString(R.string.reset_pwd).toUpperCase(Locale.ENGLISH));
        mResetPwdBtn.setText(getString(R.string.reset_pwd));
        mHeadeLeftBtnLay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;

            case R.id.footer_btn:
                validateFields();
                break;
        }
    }

    private void validateFields() {
        mFirstName = mFirstNameEdt.getText().toString().trim();
        mEmail = mEmailEdt.getText().toString().trim();

        if (mEmail.isEmpty() || (!GlobalMethods.isEmailValid(mEmail, false))) {
            DialogManager.showBasicAlertDialog(ForgotPwdScreen.this, getString(R.string.reset_enter_email), this);
        } else {
            APIRequestHandler.getInstance().getResetPwdResponse("", mEmail, this);

        }
    }

    private String vaildEmailPh(String emailPhnum) {

        String isVaild = "";
        if (emailPhnum.isEmpty()) {
            isVaild = getString(R.string.reset_enter_email_ph);

        } else if (emailPhnum.matches("[0-9]+") && emailPhnum.length() > 2) {
            isVaild = getString(R.string.signup_enter_phone);

        } else if (emailPhnum.contains("[a-zA-Z]+") && emailPhnum.length() > 2 || (!GlobalMethods.isEmailValid(mEmail, false))) {

            isVaild = getString(R.string.reset_enter_email);
        } else {
            isVaild = getString(R.string.one);
        }

        return isVaild;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        previousScreen(SignInScreen.class, true);
    }

    @Override
    public void onOkClick() {

    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);

        if (responseObj instanceof CommonResponse) {
            CommonResponse resetpwdres = (CommonResponse) responseObj;

            if (resetpwdres.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                DialogManager.showBasicAlertDialog(ForgotPwdScreen.this, getString(R.string.reset_email_sucess), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {
                        onBackPressed();
                    }
                });
            } else {
                DialogManager.showBasicAlertDialog(this, resetpwdres.getMessage(), this);

            }
        }
    }
}
