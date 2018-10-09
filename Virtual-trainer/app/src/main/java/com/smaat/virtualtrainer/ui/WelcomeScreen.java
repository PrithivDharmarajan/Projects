package com.smaat.virtualtrainer.ui;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.smaat.virtualtrainer.R;
import com.smaat.virtualtrainer.main.BaseActivity;
import com.smaat.virtualtrainer.utils.AppConstants;
import com.smaat.virtualtrainer.utils.GlobalMethods;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class WelcomeScreen extends BaseActivity {

    @BindView(R.id.parent_lay)
    ViewGroup mWelComeViewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ui_welcome_screen);
        initComponent();

    }


    private void initComponent() {
        ButterKnife.bind(this);
        setupUI(mWelComeViewGroup);
    }


    @OnClick({R.id.cont_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cont_btn:
                GlobalMethods.storeStringValue(this, AppConstants.USER_STATUS, getString(R.string.three));
                nextScreen(EntryScreen.class, true);
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
}
