package com.bridgellc.bridge.ui;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.utils.DialogManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

/**
 * Created by USER on 3/17/2016.
 */
public class TermsCondScreen extends BaseActivity {

    private WebView mTemsWebView;
    private Button mOkBtn;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_terms_cond_screen);
        initComponents();
    }

    private void initComponents() {
        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);

        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        mOkBtn = (Button) findViewById(R.id.footer_btn);
        mFooterLay = (LinearLayout) findViewById(R.id.footer_parent_lay);
        mTemsWebView = (WebView) findViewById(R.id.about_webview);

        mHeaderTxt.setText(getString(R.string.terms_cond).toUpperCase(Locale.ENGLISH));
        mOkBtn.setText(getString(R.string.ok));
        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        loadWebView();
//        setWebView();
    }

    private void loadWebView() {

        DialogManager.showProgress(TermsCondScreen.this);
        url = "file:///android_asset/term_cond.html";
        mTemsWebView.setInitialScale(1);
        mTemsWebView.setWebViewClient(new MyWebViewClient());
        mTemsWebView.getSettings().setJavaScriptEnabled(true);
        mTemsWebView.getSettings().setLoadWithOverviewMode(true);
        mTemsWebView.getSettings().setUseWideViewPort(true);
        mTemsWebView.getSettings().setDefaultFixedFontSize(25);
        mTemsWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mTemsWebView.setScrollbarFadingEnabled(false);
        mTemsWebView.loadUrl(url);

        mTemsWebView.clearCache(true);
        android.webkit.CookieManager.getInstance().removeAllCookie();


    }

    private void setWebView() {

        WebSettings webSetting = mTemsWebView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setDisplayZoomControls(true);

        String htmlFilename = "term_cond" + ".html";
        AssetManager mgr = this.getBaseContext().getAssets();
        try {
            InputStream in = mgr.open(htmlFilename, AssetManager.ACCESS_BUFFER);
            String htmlContentInStringFormat = StreamToString(in);
            in.close();
            mTemsWebView.loadDataWithBaseURL(null,
                    "<p style=\"text-align: justify\">"
                            + htmlContentInStringFormat + "</p>", "text/html",
                    "utf-8", null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishScreen();
    }

    public static String StreamToString(InputStream in) throws IOException {
        if (in == null) {
            return "";
        }
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(in,
                    "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
        }
        return writer.toString();
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            DialogManager.hideProgress(TermsCondScreen.this);
            if (url.contains(getString(R.string.apirequestsuccess))) {
                // DialogManager.hideProgress(getActivity());
                Uri uri = Uri.parse(url);
                String errorCode = uri
                        .getQueryParameter(getString(R.string.error_code));
                String message = uri
                        .getQueryParameter(getString(R.string.message));
                String result = uri
                        .getQueryParameter(getString(R.string.result));

                stopWebview(errorCode, message, result);

            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {

            DialogManager.hideProgress(TermsCondScreen.this);
            stopWebview("0", getString(R.string.no_internet), "");
            super.onReceivedError(view, errorCode, description, failingUrl);

        }

    }

    private void stopWebview(String errorCode, String message, String result) {
        // TODO Auto-generated method stub

        mTemsWebView.stopLoading();
        mTemsWebView.setWebChromeClient(null);
        mTemsWebView.setWebViewClient(null);
        mTemsWebView.setVisibility(View.GONE);
        // gshowToast(getActivity(), message);
    }
}
