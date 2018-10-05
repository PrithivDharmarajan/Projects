package com.calix.calixgigamanage.ui.dashboard;


import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.adapter.SpeedTestAdapter;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.output.model.CommonResponse;
import com.calix.calixgigamanage.output.model.RouterMapEntity;
import com.calix.calixgigamanage.output.model.RouterMapResponse;
import com.calix.calixgigamanage.output.model.SpeedEntity;
import com.calix.calixgigamanage.output.model.SpeedTestResponse;
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

public class SpeedTest extends BaseActivity {

    @BindView(R.id.speed_test_par_lay)
    RelativeLayout mSpeedTestParLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.speed_test_header_bg_lay)
    RelativeLayout mSpeedTestHeaderBgLay;

    @BindView(R.id.speed_test_recycler_view)
    RecyclerView mSpeedTestRecyclerView;

    @BindView(R.id.speed_test_img)
    ImageView mSpeedTestImg;

    private SpeedTestAdapter mSpeedTestAdapter;
    private String mStartSpeedTestRouterIdStr = "", mGetSpeedTestRouterIdStr = "";
    private int mStartSpeedTestAPIPosInt = 0, mGetSpeedTestAPIPosInt = 0;
    private ArrayList<RouterMapEntity> mSpeedTestArrList = new ArrayList<>();
    private boolean mIsInitSpeedTestBool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_speed_test);
        initView();
    }


    private void initView() {

        /*For error track purpose - log with class name*/
        AppConstants.TAG = this.getClass().getSimpleName();

        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mSpeedTestParLay);

        setHeaderView();

        routerMapAPICall();
    }


    private void setHeaderView() {

        /*Header*/
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.speed_test));
        mSpeedTestImg.setImageResource(R.drawable.speed_test_enable);

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mSpeedTestHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(R.dimen.size45);
                    mSpeedTestHeaderBgLay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(SpeedTest.this)));
                    mSpeedTestHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(SpeedTest.this), 0, 0);
                }

            });
        }
    }


    /*View onClick*/
    @OnClick({R.id.header_left_img_lay, R.id.speed_test_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;
            case R.id.speed_test_lay:
                if (mSpeedTestArrList.size() > 0) {
                    mStartSpeedTestAPIPosInt = 0;
                    DialogManager.getInstance().showProgress(SpeedTest.this);
                    mSpeedTestImg.setImageResource(R.drawable.speed_test_disable);
                    mSpeedTestArrList.get(mStartSpeedTestAPIPosInt).getSpeed().setAnimationBool(true);
                    setAdapter(mSpeedTestArrList);
                    startSpeedTestAPICall(mSpeedTestArrList.get(mStartSpeedTestAPIPosInt).getRouterId());
                }
                break;
        }
    }

    /*API calls*/
    private void routerMapAPICall() {
        APIRequestHandler.getInstance().routerMapAPICall(this);
    }

    /*API calls*/
    private void startSpeedTestAPICall(String routerIdStr) {
        mStartSpeedTestRouterIdStr = routerIdStr;
        APIRequestHandler.getInstance().startSpeedTestAPICall(routerIdStr, this);
    }

    /*API calls*/
    private void getSpeedTestAPICall(String routerIdStr) {
        mGetSpeedTestRouterIdStr = routerIdStr;
        APIRequestHandler.getInstance().getSpeedTestAPICall(routerIdStr, this);
    }


    /*Set adapter*/
    private void setAdapter(ArrayList<RouterMapEntity> routerEntityArrayList) {

        if (mSpeedTestAdapter == null) {
            mSpeedTestAdapter = new SpeedTestAdapter(routerEntityArrayList, this);
            mSpeedTestRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mSpeedTestRecyclerView.setAdapter(mSpeedTestAdapter);
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSpeedTestAdapter.notifyDataSetChanged();
                }
            });
        }

    }

    /*API request success and failure*/
    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof RouterMapResponse) {
            AppConstants.IS_SPEED_STARTED = false;
            RouterMapResponse routerMapResponse = (RouterMapResponse) resObj;
            mSpeedTestArrList = new ArrayList<>();
            mSpeedTestArrList = routerMapResponse.getRouters();
            if (mSpeedTestArrList.size() > 0) {
                mIsInitSpeedTestBool = true;
                mGetSpeedTestAPIPosInt = 0;
                getSpeedTestAPICall(mSpeedTestArrList.get(mGetSpeedTestAPIPosInt).getRouterId());
            }
            setAdapter(mSpeedTestArrList);
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

            SpeedTestResponse speedTestResponse = (SpeedTestResponse) resObj;

            if (speedTestResponse.isFinal() || (mIsInitSpeedTestBool && speedTestResponse.isFinal())) {
                AppConstants.IS_SPEED_STARTED = true;
                SpeedEntity speedTestRes = new SpeedEntity();
                speedTestRes.setUpload(Double.valueOf(speedTestResponse.getUploadRate()));
                speedTestRes.setDownload(Double.valueOf(speedTestResponse.getDownloadRate()));
                speedTestRes.setPing(speedTestResponse.getPing());
                speedTestRes.setAnimationBool(false);
                mSpeedTestArrList.get(mGetSpeedTestAPIPosInt).setSpeed(speedTestRes);
                setAdapter(mSpeedTestArrList);

                if (mSpeedTestArrList.size() - 1 <= mGetSpeedTestAPIPosInt) {
                    mIsInitSpeedTestBool=false;
                    DialogManager.getInstance().hideProgress();
                    mSpeedTestImg.setImageResource(R.drawable.speed_test_enable);
                } else {
                    mGetSpeedTestAPIPosInt += 1;
                    if (mSpeedTestArrList.get(mGetSpeedTestAPIPosInt) != null && !mSpeedTestArrList.get(mGetSpeedTestAPIPosInt).getRouterId().isEmpty()) {
                        getSpeedTestAPICall(mSpeedTestArrList.get(mGetSpeedTestAPIPosInt).getRouterId());
                    } else {
                        DialogManager.getInstance().hideProgress();
                        mSpeedTestImg.setImageResource(R.drawable.speed_test_enable);
                    }
                }
            } else if (!mIsInitSpeedTestBool) {
                getSpeedTestAPICall(mSpeedTestArrList.get(mGetSpeedTestAPIPosInt).getRouterId());
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
                            } else if (resObj instanceof CommonResponse) {
                                DialogManager.getInstance().showProgress(SpeedTest.this);
                                startSpeedTestAPICall(mStartSpeedTestRouterIdStr);
                            } else if (resObj instanceof SpeedTestResponse) {
                                DialogManager.getInstance().showProgress(SpeedTest.this);
                                getSpeedTestAPICall(mGetSpeedTestRouterIdStr);
                            }
                        }
                    });
        }
    }


    @Override
    public void onBackPressed() {
        backScreen();
    }
}


