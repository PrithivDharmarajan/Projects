package com.calix.calixgigaspireapp.ui.wifi;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.main.BaseActivity;
import com.calix.calixgigaspireapp.output.model.CommonResponse;
import com.calix.calixgigaspireapp.output.model.RouterMapEntity;
import com.calix.calixgigaspireapp.output.model.RouterMapResponse;
import com.calix.calixgigaspireapp.output.model.SpeedEntity;
import com.calix.calixgigaspireapp.output.model.SpeedTestResponse;
import com.calix.calixgigaspireapp.services.APIRequestHandler;
import com.calix.calixgigaspireapp.ui.dashboard.Dashboard;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.DialogManager;
import com.calix.calixgigaspireapp.utils.InterfaceBtnCallback;
import com.calix.calixgigaspireapp.utils.NumberUtil;
import com.github.anastr.speedviewlib.Speedometer;
import com.github.anastr.speedviewlib.components.Indicators.Indicator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class InternetSpeedTest extends BaseActivity {

    @BindView(R.id.internet_speed_test_bg_lay)
    ViewGroup mWifiHeaderLay;

    @BindView(R.id.internet_speed_header_lay)
    RelativeLayout mPCHeaderBgLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.startButton)
    Button startButton;

    @BindView(R.id.speedometer)
    Speedometer speedometer;

  /*  @BindView(R.id.barImageView)
    ImageView barImageView;*/

    static int position = 0;
    static int lastPosition = 0;
    HashSet<String> tempBlackList;

    /*RotateAnimation rotate;*/
    RotateAnimation rotate;

    private String mStartSpeedTestRouterIdStr = "", mGetSpeedTestRouterIdStr = "";
    private int mStartSpeedTestAPIPosInt = 0, mGetSpeedTestAPIPosInt = 0;
    private ArrayList<RouterMapEntity> mSpeedTestArrList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_internet_speed);

        initView();
    }

    /*View initialization*/
    private void initView() {

        tempBlackList = new HashSet<>();

        /*For error track purpose - log with class name*/
        AppConstants.TAG = this.getClass().getSimpleName();

        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mWifiHeaderLay);
        /*speedometer timing*/
        speedometer.setUnitTextColor(View.GONE);
        speedometer.setSpeedTextColor(View.GONE);

        setHeaderView();

        /*Router API Call*/
        routerMapAPICall();
    }


    private void setHeaderView() {

        /*Header*/
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.wifi_speed));
        mHeaderTxt.setVisibility(IsScreenModePortrait() ? View.VISIBLE : View.VISIBLE);

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mPCHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(R.dimen.size45);
                    mPCHeaderBgLay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(InternetSpeedTest.this)));
                    mPCHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(InternetSpeedTest.this), 0, 0);
                }
            });
        }
    }


    /*View onClick*/
    @OnClick({R.id.header_left_img, R.id.startButton, R.id.dashboard_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img:
                onBackPressed();
                break;

            case R.id.startButton:
                if (mSpeedTestArrList.size() > 0) {
                    mStartSpeedTestAPIPosInt = 0;
                    DialogManager.getInstance().showProgress(this);
                    startSpeedTestAPICall(mSpeedTestArrList.get(mStartSpeedTestAPIPosInt).getRouterId());
                    speedometer.speedTo(50, 4000);
                }else{
                    DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_on_board_router), this);
                }
                break;

            case R.id.dashboard_lay:
                nextScreen(Dashboard.class);
                break;
        }
    }

    /*Router API calls*/
    private void routerMapAPICall() {
        APIRequestHandler.getInstance().routerMapAPICall(this);
    }

    /*Start Speed Test (start)API calls*/
    private void startSpeedTestAPICall(String routerIdStr) {
        mStartSpeedTestRouterIdStr = routerIdStr;
        APIRequestHandler.getInstance().startSpeedTestAPICall(routerIdStr, this);
    }

    /*Speed Test Result (get)API calls*/
    private void getSpeedTestAPICall(String routerIdStr) {
        mGetSpeedTestRouterIdStr = routerIdStr;
        APIRequestHandler.getInstance().getSpeedTestAPICall(routerIdStr, this);
    }

    /*API request success and failure*/
    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof RouterMapResponse) {
            RouterMapResponse routerMapResponse = (RouterMapResponse) resObj;
            mSpeedTestArrList = routerMapResponse.getRouters();
            AppConstants.IS_SPEED_STARTED = false;

        } else if (resObj instanceof CommonResponse) {
            if (mSpeedTestArrList.size() - 1 <= mStartSpeedTestAPIPosInt) {
                final Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        handler.removeCallbacks(this);
                        mGetSpeedTestAPIPosInt = 0;
                        getSpeedTestAPICall(mSpeedTestArrList.get(mGetSpeedTestAPIPosInt).getRouterId());
                    }
                };
                handler.postDelayed(runnable, 60000);
            } else {
                mStartSpeedTestAPIPosInt += 1;
                if (mSpeedTestArrList.get(mStartSpeedTestAPIPosInt) != null && !mSpeedTestArrList.get(mStartSpeedTestAPIPosInt).getRouterId().isEmpty()) {
                    startSpeedTestAPICall(mSpeedTestArrList.get(mStartSpeedTestAPIPosInt).getRouterId());
                } else {
                    DialogManager.getInstance().hideProgress();
                }
            }

        } else if (resObj instanceof SpeedTestResponse) {
            AppConstants.IS_SPEED_STARTED = true;
            SpeedTestResponse speedTestResponse = (SpeedTestResponse) resObj;
            SpeedEntity speedTestRes = new SpeedEntity();
            speedTestRes.setUpload(speedTestResponse.getUploadRate());
            speedTestRes.setDownload(speedTestResponse.getDownloadRate());
            speedTestRes.setPing(speedTestResponse.getPing());
            mSpeedTestArrList.get(mGetSpeedTestAPIPosInt).setSpeed(speedTestRes);

            if (mSpeedTestArrList.size() - 1 <= mGetSpeedTestAPIPosInt) {
                speedometer.speedTo(0, 0);
                DialogManager.getInstance().hideProgress();
                AppConstants.SPEED_TEST_RESULT = mSpeedTestArrList;
                nextScreen(WifiSpeedResult.class);
            } else {
                mGetSpeedTestAPIPosInt += 1;
                if (mSpeedTestArrList.get(mGetSpeedTestAPIPosInt) != null && !mSpeedTestArrList.get(mGetSpeedTestAPIPosInt).getRouterId().isEmpty()) {
                    getSpeedTestAPICall(mSpeedTestArrList.get(mGetSpeedTestAPIPosInt).getRouterId());
                } else {
                    DialogManager.getInstance().hideProgress();
                }
            }
        }
    }

    @Override
    public void onRequestFailure(final Object resObj, Throwable t) {
        super.onRequestFailure(resObj, t);
        if (t instanceof IOException) {
            DialogManager.getInstance().showNetworkErrorPopup(this,
                    (t instanceof java.net.ConnectException ? getString(R.string.no_internet) : getString(R.string
                            .connect_time_out)), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {
                            if (resObj instanceof RouterMapResponse) {
                                routerMapAPICall();
                                speedometer.setIndicator(Indicator.Indicators.SpindleIndicator);
                            } else if (resObj instanceof CommonResponse) {
                                DialogManager.getInstance().showProgress(InternetSpeedTest.this);
                                startSpeedTestAPICall(mStartSpeedTestRouterIdStr);

                            } else if (resObj instanceof SpeedTestResponse) {
                                DialogManager.getInstance().showProgress(InternetSpeedTest.this);
                                getSpeedTestAPICall(mGetSpeedTestRouterIdStr);
                            }
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        previousScreen(Dashboard.class);
    }
}


