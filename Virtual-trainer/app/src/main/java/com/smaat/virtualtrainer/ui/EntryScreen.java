package com.smaat.virtualtrainer.ui;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.smaat.virtualtrainer.R;
import com.smaat.virtualtrainer.apiinterface.APIRequestHandler;
import com.smaat.virtualtrainer.main.BaseActivity;
import com.smaat.virtualtrainer.model.JoinStreamingEntity;
import com.smaat.virtualtrainer.utils.AppConstants;
import com.smaat.virtualtrainer.utils.DialogManager;
import com.smaat.virtualtrainer.utils.DialogMangerOkEdtCallback;
import com.smaat.virtualtrainer.utils.DialogMangerTwoBtnCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EntryScreen extends BaseActivity implements DialogMangerOkEdtCallback, DialogMangerTwoBtnCallback {


    @BindView(R.id.parent_lay)
    ViewGroup mEntryViewGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ui_entry_screen);
        initComponent();

    }

    private void initComponent() {
        ButterKnife.bind(this);
        setupUI(mEntryViewGroup);
    }


    @OnClick({R.id.login_img, R.id.register_img, R.id.guest_img})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_img:
                nextScreen(LoginScreen.class, true);
                break;

            case R.id.register_img:
                nextScreen(SignupScreen.class, true);
                break;

            case R.id.guest_img:
                DialogManager.showJoinAsGuestDialog(this, EntryScreen.this);
                break;

        }

    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }

    @Override
    public void onOkEdtClick(final String name, final String meetingId) {

        APIRequestHandler.getInstance().joinAsGuestAPICall(name, meetingId, EntryScreen.this);

    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);
        if (responseObj instanceof JoinStreamingEntity) {
            JoinStreamingEntity joinAsGuestEntityRes = (JoinStreamingEntity) responseObj;
            if (joinAsGuestEntityRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                AppConstants.STREAMING_ID = joinAsGuestEntityRes.getResult().getStream_id();
                AppConstants.STREAMING_NAME = joinAsGuestEntityRes.getResult().getStream_name();
                nextScreen(AddJoinessScreen.class, true);

            } else {
                DialogManager.showBasicAlertDialog(this, joinAsGuestEntityRes.getMessage(), getString(R.string.ok), false, getString(R.string.ok), true, true, this);
            }


        }

    }


    @Override
    public void onYesClick() {

    }

    @Override
    public void onNoClick() {

    }
}
