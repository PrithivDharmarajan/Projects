package com.smaat.virtualtrainer.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.smaat.virtualtrainer.R;
import com.smaat.virtualtrainer.apiinterface.APIRequestHandler;
import com.smaat.virtualtrainer.main.BaseActivity;
import com.smaat.virtualtrainer.model.UserAccTypeEntity;
import com.smaat.virtualtrainer.utils.AppConstants;
import com.smaat.virtualtrainer.utils.DialogManager;
import com.smaat.virtualtrainer.utils.DialogMangerTwoBtnCallback;
import com.smaat.virtualtrainer.utils.GlobalMethods;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UserTypeScreen extends BaseActivity implements DialogMangerTwoBtnCallback {


    @BindView(R.id.parent_lay)
    ViewGroup mUsertypeViewGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ui_user_type_screen);
        initComponent();

    }


    private void initComponent() {

        ButterKnife.bind(this);
        setupUI(mUsertypeViewGroup);

    }

    @OnClick({R.id.free_user_lay, R.id.pro_user_lay})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.free_user_lay:
                APIRequestHandler.getInstance().userAccTypeAPICall(AppConstants.SUCCESS_CODE, this);
                break;

            case R.id.pro_user_lay:
                APIRequestHandler.getInstance().userAccTypeAPICall(getString(R.string.two), this);
                break;

        }

    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);
        if (responseObj instanceof UserAccTypeEntity) {
            UserAccTypeEntity userAccTypeEntityRes = (UserAccTypeEntity) responseObj;

            if (userAccTypeEntityRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                GlobalMethods.storeStringValue(this, AppConstants.USER_STATUS, AppConstants.SUCCESS_CODE);
                nextScreen(HomeScreen.class, true);
            } else {
                DialogManager.showBasicAlertDialog(this, userAccTypeEntityRes.getMessage(), getString(R.string.ok), false, getString(R.string.ok), true, true, UserTypeScreen.this);
            }
        }
    }

    @Override
    public void onYesClick() {

    }

    @Override
    public void onNoClick() {

    }

    @Override
    public void onBackPressed() {
        //Backbutton action is no need for this screen
    }
}
