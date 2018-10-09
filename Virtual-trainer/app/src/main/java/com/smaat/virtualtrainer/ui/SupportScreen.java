package com.smaat.virtualtrainer.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.smaat.virtualtrainer.R;
import com.smaat.virtualtrainer.apiinterface.APIRequestHandler;
import com.smaat.virtualtrainer.main.BaseActivity;
import com.smaat.virtualtrainer.model.ContactSupportEntity;
import com.smaat.virtualtrainer.utils.AppConstants;
import com.smaat.virtualtrainer.utils.DialogManager;
import com.smaat.virtualtrainer.utils.DialogMangerTwoBtnCallback;
import com.smaat.virtualtrainer.utils.GlobalMethods;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SupportScreen extends BaseActivity implements DialogMangerTwoBtnCallback {

    @BindView(R.id.parent_lay)
    ViewGroup mSupportViewGroup;

    @BindView(R.id.mail_id_edt)
    EditText mEmailIdEdt;

    @BindView(R.id.cmd_edt)
    EditText mMsgEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ui_support_screen);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initComponent();
    }

    private void initComponent() {
        ButterKnife.bind(this);
        setupUI(mSupportViewGroup);

        mHeaderLeftBtnLay.setVisibility(View.VISIBLE);
        mHeaderRightBtnLay.setVisibility(View.INVISIBLE);

        mHeaderTxt.setText(getString(R.string.profile));

        mEmailIdEdt.setText(AppConstants.SUPPORT_EMAIL_ID);
        mEmailIdEdt.setFocusable(false);
        mEmailIdEdt.setFocusableInTouchMode(false);
        mEmailIdEdt.setClickable(false);
    }

    @OnClick({R.id.header_left_btn_lay, R.id.submit_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.submit_btn:

                String emailIdStr = mEmailIdEdt.getText().toString().trim();
                String messageStr = mMsgEdt.getText().toString().trim();

                if (emailIdStr.isEmpty() || (!GlobalMethods.isEmailValid(emailIdStr))) {
                    mEmailIdEdt.requestFocus();
                    DialogManager.showBasicAlertDialog(this, getString(R.string.enter_email), getString(R.string.ok), false, getString(R.string.ok), true, true, SupportScreen.this);
                } else if (messageStr.isEmpty()) {
                    mMsgEdt.requestFocus();
                    DialogManager.showBasicAlertDialog(this, getString(R.string.enter_cmd), getString(R.string.ok), false, getString(R.string.ok), true, true, SupportScreen.this);
                } else {

                    APIRequestHandler.getInstance().contactSupportAPICall(emailIdStr, messageStr, this);

                }
                break;

        }
    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);
        if (responseObj instanceof ContactSupportEntity) {
            ContactSupportEntity contactSupportEntityRes = (ContactSupportEntity) responseObj;

            if (contactSupportEntityRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                DialogManager.showBasicAlertDialog(this, contactSupportEntityRes.getMessage(), getString(R.string.ok), false, getString(R.string.ok), true, true, new DialogMangerTwoBtnCallback() {
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
                DialogManager.showBasicAlertDialog(this, contactSupportEntityRes.getMessage(), getString(R.string.ok), false, getString(R.string.ok), true, true, SupportScreen.this);
            }
        }
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
