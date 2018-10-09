package com.smaat.ipharma.ui;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.smaat.ipharma.R;
import com.smaat.ipharma.adapter.MapListAdapter;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.utils.AppConstants;
import com.smaat.ipharma.utils.DialogManager;
import com.smaat.ipharma.utils.GlobalMethods;


public class AboutFragment extends BaseFragment{
    private WebView mAboutWebView;
    private String url;

    // private String UserID;
    // private ProgressDialog progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_about, container,
                false);
        setupUI(rootview);

        return rootview;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup mViewGroup = (ViewGroup) view
                .findViewById(R.id.parent_layout);
        setupUI(mViewGroup);
        hideSoftKeyboard(getActivity());

        initComponents(view);
        // callAbounUsService();

    }

//	@Override
//	public void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//		getActivity().getSupportFragmentManager().beginTransaction()
//				.remove(this).commit();
//	}

    private void initComponents(View view) {
        DialogManager.showProgress(getActivity());
        mAboutWebView = (WebView) view.findViewById(R.id.about);
        url = AppConstants.DISCLAIMER_LINK;
        mAboutWebView.setWebViewClient(new MyWebViewClient());
        mAboutWebView.getSettings().setJavaScriptEnabled(true);

        mAboutWebView.loadUrl(AppConstants.Url);
        mAboutWebView.requestFocus();
        mAboutWebView.clearCache(true);
        android.webkit.CookieManager.getInstance().removeAllCookie();
        //
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
            DialogManager.hideProgress();

        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {

            DialogManager.hideProgress();
            stopWebview("0", getString(R.string.no_internet), "");
            super.onReceivedError(view, errorCode, description, failingUrl);

        }

    }

    private void stopWebview(String errorCode, String message, String result) {
        // TODO Auto-generated method stub

        mAboutWebView.stopLoading();
        mAboutWebView.setWebChromeClient(null);
        mAboutWebView.setWebViewClient(null);
        mAboutWebView.setVisibility(View.GONE);
        DialogManager.showToast(getActivity(), message);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(AppConstants.Url.contains("about.php"))
        {
           /* if (!GlobalMethods.getStringValue(getActivity(), AppConstants.ShopName_OT).isEmpty()) {
                ((HomeScreen) getActivity()).setToolbarTitle(getString(R.string.txt_terms_and_condition), getString(R.string.one_touch_updated)+" "+
                        GlobalMethods.getStringValue(getActivity(), AppConstants.ShopName_OT));
            } else {
                ((HomeScreen) getActivity()).setToolbarTitle(getString(R.string.txt_terms_and_condition),getString(R.string.one_touch_order));
            }*/
            ((HomeScreen) getActivity()).setToolbarTitle(getString(R.string.txt_terms_and_condition),getString(R.string.home));
           // ((HomeScreen) getActivity()).setToolbarTitle(getActivity().getString(R.string.txt_terms_and_condition),getActivity().getString(R.string.one_touch_order));
        }else{
            ((HomeScreen) getActivity()).setToolbarTitle(getString(R.string.disc),getString(R.string.home));
        }


    }



}
