package com.smaat.virtualtrainer.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.smaat.virtualtrainer.R;
import com.smaat.virtualtrainer.main.BaseActivity;
import com.smaat.virtualtrainer.utils.DialogManager;
import com.smaat.virtualtrainer.utils.DialogMangerTwoBtnCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProVersionScreen extends BaseActivity implements DialogMangerTwoBtnCallback {

    @BindView(R.id.parent_lay)
    ViewGroup mProVerViewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ui_pro_version_screen);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initComponent();
    }

    private void initComponent() {
        ButterKnife.bind(this);
        setupUI(mProVerViewGroup);

        mHeaderLeftBtnLay.setVisibility(View.VISIBLE);
        mHeaderRightBtnLay.setVisibility(View.INVISIBLE);

        mHeaderTxt.setText(getString(R.string.pro_versions));

    }

    @OnClick({R.id.header_left_btn_lay, R.id.silver_lay, R.id.gold_lay, R.id.diamond_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.silver_lay:
                DialogManager.showBasicAlertDialog(this, getString(R.string.future_release), getString(R.string.ok), false, getString(R.string.ok), true, true, ProVersionScreen.this);
                break;

            case R.id.gold_lay:
                DialogManager.showBasicAlertDialog(this, getString(R.string.future_release), getString(R.string.ok), false, getString(R.string.ok), true, true, ProVersionScreen.this);
                break;

            case R.id.diamond_lay:
                DialogManager.showBasicAlertDialog(this, getString(R.string.future_release), getString(R.string.ok), false, getString(R.string.ok), true, true, ProVersionScreen.this);
                break;

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

        finishScreen();
    }
}
