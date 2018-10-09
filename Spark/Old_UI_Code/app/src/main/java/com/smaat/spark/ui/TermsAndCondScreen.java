package com.smaat.spark.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.smaat.spark.R;
import com.smaat.spark.main.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TermsAndCondScreen extends BaseActivity {

    @BindView(R.id.parent_lay)
    ViewGroup mTermsAndCondViewGrp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_terms_cond_screen);
        ButterKnife.bind(this);
        setupUI(mTermsAndCondViewGrp);
    }

    @OnClick({R.id.header_left_btn_lay, R.id.ok_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
            case R.id.ok_btn:
                onBackPressed();
                break;

        }

    }

    @Override
    public void onBackPressed() {
       finishScreen();
    }
}
