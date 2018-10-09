package com.calix.calixgigaspireapp.ui.guest;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.adapter.guest.GuestNetworkAdapter;
import com.calix.calixgigaspireapp.main.BaseActivity;
import com.calix.calixgigaspireapp.output.model.DurationEntity;
import com.calix.calixgigaspireapp.output.model.GuestWifiEntity;
import com.calix.calixgigaspireapp.output.model.GuestWifiResponse;
import com.calix.calixgigaspireapp.services.APIRequestHandler;
import com.calix.calixgigaspireapp.ui.dashboard.Dashboard;
import com.calix.calixgigaspireapp.ui.settings.Settings;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.DialogManager;
import com.calix.calixgigaspireapp.utils.InterfaceBtnCallback;
import com.calix.calixgigaspireapp.utils.NumberUtil;
import com.calix.calixgigaspireapp.utils.PreferenceUtil;


import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuestNetwork extends BaseActivity {


    @BindView(R.id.guest_network_par_lay)
    RelativeLayout mGuestNetworkLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.guest_network_header_bg_lay)
    RelativeLayout mGuestNetworkHeaderBgLay;

    @BindView(R.id.guest_network_recycler_view)
    RecyclerView mGuestNetworkRecyclerView;

    @BindView(R.id.footer_first_img)
    ImageView mFooterFirstImg;

    @BindView(R.id.footer_one_txt)
    TextView mFooterOneTxt;

    @BindView(R.id.footer_second_img)
    ImageView mFooterSecondImg;

    @BindView(R.id.footer_second_txt)
    TextView mFooterSecondTxt;

    @BindView(R.id.footer_third_lay)
    LinearLayout mFooterThirdLay;

    @BindView(R.id.footer_third_view)
    View mFooterThirdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_guest_network);
        initView();
    }

    /*View initialization*/
    private void initView() {
            /*For error track purpose - log with class name*/
        AppConstants.TAG = this.getClass().getSimpleName();

       /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mGuestNetworkLay);

        setHeaderView();
        setFooterView();

        /*API call*/
        guestAPICall();

    }


    private void setHeaderView() {

        /*Header*/
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.guest_network));


         /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mGuestNetworkHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(R.dimen.size45);
                    mGuestNetworkHeaderBgLay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(GuestNetwork.this)));
                    mGuestNetworkHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(GuestNetwork.this), 0, 0);
                }

            });
        }
    }

    /*Set Footer View */
    private void setFooterView() {
        mFooterFirstImg.setImageResource(R.drawable.ic_dashboard);
        mFooterOneTxt.setText(getString(R.string.dashboard));

        mFooterSecondImg.setImageResource(R.drawable.setting_icon);
        mFooterSecondTxt.setText(getString(R.string.settings));
        mFooterThirdLay.setVisibility(View.GONE);
        mFooterThirdView.setVisibility(View.GONE);

    }

    /*View onClick*/
    @OnClick({R.id.header_left_img_lay, R.id.add_guest_network_lay,
            R.id.footer_first_lay, R.id.footer_second_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;
            case R.id.add_guest_network_lay:
                GuestWifiEntity guestWifiEntity = new GuestWifiEntity();
                DurationEntity durationEntity = new DurationEntity();
                durationEntity.setStartTime(System.currentTimeMillis());
                durationEntity.setEndTime(System.currentTimeMillis());
                guestWifiEntity.setDuration(durationEntity);

                AppConstants.GUEST_WIFI_DETAILS = guestWifiEntity;
                nextScreen(SetupGuestNetwork.class);
                break;
            case R.id.footer_first_lay:
                nextScreen(Dashboard.class);
                break;
            case R.id.footer_second_lay:
                nextScreen(Settings.class);
                break;
        }
    }

    /*API calls*/
    private void guestAPICall() {
        sysOut(PreferenceUtil.getAuthorization(this));
        APIRequestHandler.getInstance().guestWifiListAPICall(this);
    }

    /*Set adapter*/
    private void setAdapter(ArrayList<GuestWifiEntity> guestWifiResponses) {
        mGuestNetworkRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mGuestNetworkRecyclerView.setAdapter(new GuestNetworkAdapter(guestWifiResponses, this));
        mGuestNetworkRecyclerView.setNestedScrollingEnabled(false);
    }

    /*API request success and failure*/
    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof GuestWifiResponse) {
            GuestWifiResponse guestWifiResponse = (GuestWifiResponse) resObj;
            setAdapter(guestWifiResponse.getWifis());
        }
    }

    @Override
    public void onRequestFailure(Object resObj, Throwable t) {
        super.onRequestFailure(resObj,t);

        if (t instanceof IOException) {
            DialogManager.getInstance().showNetworkErrorPopup(this,
                    (t instanceof java.net.ConnectException ? getString(R.string.no_internet) : getString(R.string
                            .connect_time_out)), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {
                    guestAPICall();
                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        previousScreen(Dashboard.class);
    }
}
