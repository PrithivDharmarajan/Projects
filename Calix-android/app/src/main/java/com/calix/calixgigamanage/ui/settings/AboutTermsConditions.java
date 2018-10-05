package com.calix.calixgigamanage.ui.settings;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.utils.AppConstants;
import com.calix.calixgigamanage.utils.NumberUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class AboutTermsConditions extends BaseActivity {

    @BindView(R.id.about_terms_parent_lay)
    ViewGroup mAboutTermsViewGroup;

    @BindView(R.id.about_terms_header_bg_lay)
    RelativeLayout mAboutTermsHeaderBgLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.about_terms_txt)
    TextView mAboutTermsTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_about_terms_conditions);
        initView();
    }


    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mAboutTermsViewGroup);

        setHeaderView();
    }


    private void setHeaderView() {

        /*set header changes*/
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(AppConstants.ABOUT_TERMS_HEADER_TEXT);
        mAboutTermsTxt.setText(getString(AppConstants.ABOUT_TERMS_HEADER_TEXT.equalsIgnoreCase(getString(R.string.about))?R.string.about_msg:R.string.terms_msg));

         /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mAboutTermsHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(R.dimen.size45);
                    mAboutTermsHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(AboutTermsConditions.this)));
                    mAboutTermsHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(AboutTermsConditions.this), 0, 0);
                }

            });
        }

    }

    /*View onClick*/
    @OnClick({R.id.header_left_img_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        backScreen();
    }
}