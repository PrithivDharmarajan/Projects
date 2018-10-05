package com.calix.calixgigamanage.ui.parentalcontrol;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.adapter.ParentalControlAdapter;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.output.model.CommonResponse;
import com.calix.calixgigamanage.output.model.DeviceEntity;
import com.calix.calixgigamanage.output.model.DeviceListResponse;
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


public class ParentalControl extends BaseActivity {

    @BindView(R.id.parental_control_par_lay)
    RelativeLayout mParentControlLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.pc_header_bg_lay)
    RelativeLayout mPCHeaderBgLay;

    @BindView(R.id.parent_control_recycler_view)
    RecyclerView mParentControlRecyclerView;

    @BindView(R.id.play_pause_img)
    ImageView mPlayPauseImg;

    @BindView(R.id.connect_internet_txt)
    TextView mConnectInternetTxt;


    private boolean mIsChangePlayPauseBtn = false;
    private int mPauseAllInt = 0;
    private boolean mPauseAllBool = false;

    private ArrayList<DeviceEntity> mDeviceListResponse = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_parental_control);
        initView();
    }

    /*View initialization*/
    private void initView() {
        /*For error track purpose - log with class name*/
        AppConstants.TAG = this.getClass().getSimpleName();

        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mParentControlLay);

        mPlayPauseImg.setTag(1);

        setHeaderView();
        deviceListAPI();
        /* setAdapter();*/
    }


    private void setHeaderView() {

        /*Header*/
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.parental_control));

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mPCHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(R.dimen.size45);
                    mPCHeaderBgLay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(ParentalControl.this)));
                    mPCHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(ParentalControl.this), 0, 0);
                }

            });
        }
    }


    /*View onClick*/
    @OnClick({R.id.header_left_img_lay, R.id.play_pause_img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;
            case R.id.play_pause_img:

                if (!mIsChangePlayPauseBtn) {
                    mPauseAllBool = true;
                    mConnectInternetTxt.setText(getString(R.string.connect_internet));
                    mPlayPauseImg.setImageResource(R.drawable.play_img);
                    mIsChangePlayPauseBtn = true;
                    for (mPauseAllInt = 0; mPauseAllInt < mDeviceListResponse.size(); mPauseAllInt++) {
                        APIRequestHandler.getInstance().deviceDisconnectAPICall(mDeviceListResponse.get(mPauseAllInt).getDeviceId(), ParentalControl.this);
                    }
                } else {
                    mPauseAllBool = true;
                    mConnectInternetTxt.setText(getString(R.string.disconnect_internet));
                    mPlayPauseImg.setImageResource(R.drawable.pause_img);
                    mIsChangePlayPauseBtn = false;
                    for (mPauseAllInt = 0; mPauseAllInt < mDeviceListResponse.size(); mPauseAllInt++) {
                        APIRequestHandler.getInstance().deviceConnectAPICall(mDeviceListResponse.get(mPauseAllInt).getDeviceId(), this);
                    }
                }

                break;
        }
    }

    /*API calls*/
    private void deviceListAPI() {
        APIRequestHandler.getInstance().deviceListAPICall("", this);
    }

    /*Populate the values*/
    private void setData(DeviceListResponse dashboardResponse) {
     /*   mDeviceCountTxt.setText(String.valueOf(dashboardResponse.getDevices().size()));
        mDeviceHeaderCountTxt.setText(String.valueOf(dashboardResponse.getDevices().size()));*/

        setAdapter(dashboardResponse.getDevices());
    }

    /*Set adapter*/
    private void setAdapter(ArrayList<DeviceEntity> deviceListResponse) {
        mDeviceListResponse = new ArrayList<>();
        mDeviceListResponse = deviceListResponse;

        mParentControlRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mParentControlRecyclerView.setAdapter(new ParentalControlAdapter(deviceListResponse,
                mPauseAllBool, this));
        mParentControlRecyclerView.setNestedScrollingEnabled(false);

    }

    /*API request success and failure*/
    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof DeviceListResponse) {
            DeviceListResponse dashboardResponse = (DeviceListResponse) resObj;
            setData(dashboardResponse);
        } else if (resObj instanceof CommonResponse) {
            if (!mPauseAllBool || (mPauseAllInt != 0 && (mPauseAllInt == mDeviceListResponse.size()))) {
                mPauseAllBool = false;
                deviceListAPI();
            }
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
                            deviceListAPI();
                        }
                    });
        }
    }

    /* private void setAdapter() {
         ParentalControlAdapter parentalControlAdapter = new ParentalControlAdapter(this);
         LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
         mParentCotnrolRecyclerView.setLayoutManager(mLayoutManager);
         mParentCotnrolRecyclerView.setItemAnimator(new DefaultItemAnimator());
         mParentCotnrolRecyclerView.setNestedScrollingEnabled(false);
         mParentCotnrolRecyclerView.setAdapter(parentalControlAdapter);

     }
 */
    @Override
    public void onBackPressed() {
        backScreen();
    }
}
