package com.fautus.fautusapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.fautus.fautusapp.R;
import com.fautus.fautusapp.main.BaseFragment;
import com.fautus.fautusapp.ui.HomeScreen;
import com.fautus.fautusapp.utils.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TermsCondFragment extends BaseFragment {


    @BindView(R.id.web_view)
    WebView mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_terms_cond_screen, container, false);
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);
        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(rootView);
        /*For focus current fragment*/
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        return rootView;
    }

    /*Fragment manual onResume*/
    @Override
    public void onFragmentResume() {
        super.onFragmentResume();
        /*set header text and header right img*/
        ((HomeScreen) getActivity()).setDrawerAction(false);
        ((HomeScreen) getActivity()).setHeadLeftImg(R.drawable.menu_cls_img);
        ((HomeScreen) getActivity()).setHeaderText(AppConstants.TERMS_COND_SCREEN.equalsIgnoreCase(AppConstants.SUCCESS_CODE) ? getString(R.string.terms_service) : getString(R.string.privacy_policy));
        ((HomeScreen) getActivity()).setHeadRightImgVisible(false, R.mipmap.app_icon);
        /*Local file Url*/
        mWebView.loadUrl(AppConstants.TERMS_COND_SCREEN.equalsIgnoreCase(AppConstants.SUCCESS_CODE) ? "file:///android_asset/web/term_cond.html" : "file:///android_asset/web/privacy_policy.html");
    }

}
