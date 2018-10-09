package com.fautus.fautusapp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.fautus.fautusapp.R;
import com.fautus.fautusapp.main.BaseActivity;
import com.fautus.fautusapp.services.APIRequestHandler;
import com.fautus.fautusapp.utils.DialogManager;
import com.fautus.fautusapp.utils.InterfaceBtnCallback;
import com.fautus.fautusapp.utils.NetworkUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * This class implements UI and functions for reset password
 */

public class ForgotPwdScreen extends BaseActivity implements InterfaceBtnCallback {

    /*Variable initialization using bind view*/
    @BindView(R.id.parent_lay)
    ViewGroup mForgotPwdViewGroup;

    @BindView(R.id.email_edt)
    EditText mEmailEdt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_forgot_pwd_screen);
        initView();
    }

    private void initView() {
        ButterKnife.bind(this);

         /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mForgotPwdViewGroup);

        //Keypad Button Action
        mEmailEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 100 || actionId == EditorInfo.IME_ACTION_DONE) {
                    validateFields();
                }
                return true;
            }
        });

    }


    /*View OnClick*/
    @OnClick({R.id.header_left_btn_lay, R.id.request_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.request_lay:
                validateFields();
                break;

        }

    }


    /*Validate the edit text fields and then do API call */
    private void validateFields() {
        /*hiding keypad for user interaction*/
        hideSoftKeyboard(this);

        String emailStr = mEmailEdt.getText().toString().trim();

        if (emailStr.isEmpty()) {
            mEmailEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.pwd_reset), getString(R.string.reset_email_req), ForgotPwdScreen.this);
        } else {
             /*Check internet connection*/
            if (NetworkUtil.isNetworkAvailable(this)) {
                //ForgotPassword API Call
                APIRequestHandler.getInstance().parseForgetPassword(emailStr, this);
            } else {
                /*Alert message will be appeared if there is no internet connection*/
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.app_name), getString(R.string.no_internet), this);

            }

        }
    }


    /*Forgot parse API callback will be resulted*/
    @Override
    public void onParseRequestSuccess() {
        super.onParseRequestSuccess();
        onBackPressed();

    }

    @Override
    public void onParseRequestFailure(@NonNull ParseException e) {
        DialogManager.getInstance().showAlertPopup(ForgotPwdScreen.this, getString(R.string.pwd_reset), e.getMessage(), new InterfaceBtnCallback() {
            @Override
            public void onOkClick() {

            }
        });
    }

    /*Redirect to previous screen*/
    @Override
    public void onBackPressed() {
        previousScreen(LoginScreen.class, true);
    }

    /*Interface default ok click*/
    @Override
    public void onOkClick() {

    }
}
