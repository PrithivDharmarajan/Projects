package com.smaat.spark.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.smaat.spark.R;
import com.smaat.spark.main.BaseActivity;
import com.smaat.spark.utils.AppConstants;
import com.smaat.spark.utils.DialogManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class WebScreen extends BaseActivity {

    @BindView(R.id.parent_lay)
    ViewGroup mTermsAndCondViewGrp;

    @BindView(R.id.header_left_img)
    ImageView mHeaderLeftImg;

    @BindView(R.id.header_txt)
    public TextView mHeaderTxt;

    @BindView(R.id.web_view)
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_terms_cond_screen);
        ButterKnife.bind(this);
        setupUI(mTermsAndCondViewGrp);
        initView();
    }

    private void initView() {
        mHeaderLeftImg.setImageResource(R.drawable.blue_back_img);
        mHeaderTxt.setText(AppConstants.SETTINGS_WEB_TITLE);
        loadWebPage();
    }

    @OnClick({R.id.header_left_btn_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
        }
    }

    private void loadWebPage() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        DialogManager.showProgress(this);

        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                DialogManager.hideProgress();
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                DialogManager.showToast(WebScreen.this, description);

            }
        });

        String url = AppConstants.SETTINGS_WEB_TERMS;
        if (AppConstants.SETTINGS_WEB_TITLE.equals(getString(R.string.support))) {
            url = AppConstants.SETTINGS_WEB_SUPPORT;
        } else if (AppConstants.SETTINGS_WEB_TITLE.equals(getString(R.string.privacy_policy))) {
            url = AppConstants.SETTINGS_WEB_PRIVACY;
        }
        mWebView.loadUrl(url);
    }


    @Override
    public void onBackPressed() {
        finishScreen();
    }
}
