package com.calix.calixgigamanage.ui.dashboard;


import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.adapter.RouterMapAdapter;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.output.model.RouterMapEntity;
import com.calix.calixgigamanage.output.model.RouterMapResponse;
import com.calix.calixgigamanage.services.APIRequestHandler;
import com.calix.calixgigamanage.utils.AppConstants;
import com.calix.calixgigamanage.utils.DialogManager;
import com.calix.calixgigamanage.utils.InterfaceBtnCallback;
import com.calix.calixgigamanage.utils.NumberUtil;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RouterMap extends BaseActivity {

    @BindView(R.id.router_map_par_lay)
    RelativeLayout mRouterMapLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.router_map_header_bg_lay)
    RelativeLayout mRouterMapHeaderBgLay;

    @BindView(R.id.router_recycler_view)
    RecyclerView mRouterRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_router_map);
        initView();
    }


    private void initView() {

        /*For error track purpose - log with class name*/
        AppConstants.TAG = this.getClass().getSimpleName();

        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mRouterMapLay);

        setHeaderView();

        routerMapAPICall();

    }


    private void setHeaderView() {

        /*Header*/
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.router_map));

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mRouterMapHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(R.dimen.size45);
                    mRouterMapHeaderBgLay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(RouterMap.this)));
                    mRouterMapHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(RouterMap.this), 0, 0);
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

    /*API calls*/
    private void routerMapAPICall() {
        APIRequestHandler.getInstance().routerMapAPICall(this);
    }

    /*Set adapter*/
    private void setAdapter(ArrayList<RouterMapEntity> routerEntityArrayList) {
        mRouterRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRouterRecyclerView.setAdapter(new RouterMapAdapter(routerEntityArrayList, this));

    }

    /*API request success and failure*/
    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof RouterMapResponse) {
            RouterMapResponse routerMapResponse = (RouterMapResponse) resObj;
            setAdapter(routerMapResponse.getRouters());
        }
    }

    @Override
    public void onRequestFailure(Object resObj, Throwable t) {
        super.onRequestFailure(resObj, t);
        if (t instanceof IOException) {
            DialogManager.getInstance().showNetworkErrorPopup(this,
                    (t instanceof java.net.ConnectException ? getString(R.string.no_internet) : getString(R.string
                            .connect_time_out)), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {
                            routerMapAPICall();
                        }
                    });
        }
    }


    @Override
    public void onBackPressed() {
        backScreen();
    }
}

