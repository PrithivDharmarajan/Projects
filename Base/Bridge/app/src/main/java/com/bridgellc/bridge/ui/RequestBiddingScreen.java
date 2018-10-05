package com.bridgellc.bridge.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.adapter.RequestBiddingAdapter;
import com.bridgellc.bridge.apiinterface.APIRequestHandler;
import com.bridgellc.bridge.entity.HomeSingleItemEntity;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.model.AcceptBiddingResponse;
import com.bridgellc.bridge.model.BiddingsReqResponse;
import com.bridgellc.bridge.model.CommonPhResponse;
import com.bridgellc.bridge.model.PaypalPayResponse;
import com.bridgellc.bridge.ui.home.HomeScreenActivity;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.GlobalMethods;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Dell on 3/22/2016.
 */
public class RequestBiddingScreen extends BaseActivity implements View.OnClickListener {

    private ListView mBiddingList;
    private RequestBiddingAdapter mAdapter;
    public static String mItemId;
    private ArrayList<HomeSingleItemEntity> mAdapterRes;
    private static String mItemName = "", mItemID = "", mItemBidAmt = "", mItemBidID = "", mOtherUser = "", mTotalPrice = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_requestbidding_screen);
        mAdapterRes = new ArrayList<HomeSingleItemEntity>();


        mActivity = this;
        initComponents();
    }

    private void initComponents() {
        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);

        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);

        mHeaderTxt.setText(getString(R.string.rec_off).toUpperCase(Locale.ENGLISH));
        mHeadeLeftBtnLay.setVisibility(View.VISIBLE);

        mBiddingList = (ListView) findViewById(R.id.bidding_list);

        APIRequestHandler.getInstance().getMyBiddingsReqResponse(mItemId, this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (AppConstants.FROME_NOTIFICATION.equalsIgnoreCase(getString(R.string.one))) {
            AppConstants.FROME_NOTIFICATION = AppConstants.FAILURE_CODE;
            previousScreen(HomeScreenActivity.class, true);
        } else if (AppConstants.FROME_NOTIFICATION.equalsIgnoreCase(getString(R.string.two))) {
            AppConstants.FROME_NOTIFICATION = AppConstants.FAILURE_CODE;
            previousScreen(NotificationScreen.class, true);
        } else {
            previousScreen(ProfileListScreen.class, true);
        }
    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);
        if (responseObj instanceof BiddingsReqResponse) {
            BiddingsReqResponse biddingsres = (BiddingsReqResponse) responseObj;
            if (biddingsres.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                mAdapterRes = biddingsres.getResult();
                mAdapter = new RequestBiddingAdapter(this, mAdapterRes);
                mBiddingList.setAdapter(mAdapter);
            } else {
                DialogManager.showBasicAlertDialog(this, biddingsres.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {
                        onBackPressed();

                    }
                });
            }
        } else if (responseObj instanceof AcceptBiddingResponse) {
            AcceptBiddingResponse accbidRes = (AcceptBiddingResponse) responseObj;
            if (accbidRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                DialogManager.showBasicAlertDialog(this,
                        getString(R.string.your_off_app), new DialogMangerOkCallback() {
                            @Override
                            public void onOkClick() {
                                onBackPressed();
                            }
                        });
            } else {
                DialogManager.showBasicAlertDialog(this, accbidRes.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {

                    }
                });
            }
        } else if (responseObj instanceof PaypalPayResponse) {
            PaypalPayResponse mPaypalResponse = (PaypalPayResponse) responseObj;

            if (mPaypalResponse.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                DialogManager.showBasicAlertDialog(RequestBiddingScreen.this, mPaypalResponse.getResult().getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {
                        previousScreen(HomeScreenActivity.class, true);

                    }
                });

            } else {
                DialogManager.showBasicAlertDialog(RequestBiddingScreen.this, mPaypalResponse.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {

                    }
                });
            }
        } else if (responseObj instanceof CommonPhResponse) {
            CommonPhResponse mBiddRejectRes = (CommonPhResponse) responseObj;

            if (mBiddRejectRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                DialogManager.showBasicAlertDialog(RequestBiddingScreen.this, mBiddRejectRes.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {
                        previousScreen(HomeScreenActivity.class, true);

                    }
                });

            } else {
                DialogManager.showBasicAlertDialog(RequestBiddingScreen.this, mBiddRejectRes.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {

                    }
                });
            }
        }
    }


    public static void callPaypalPayment(HomeSingleItemEntity mAdapRes, Context mContext) {

        mItemName = mAdapRes.getItem_name();
        mItemID = mAdapRes.getItem_id();
        mItemBidAmt = mAdapRes.getBid_amount();
        mItemBidID = mAdapRes.getBid_id();
        mOtherUser = mAdapRes.getTo_user_id();
//        mTotalPrice = mAdapRes.getTotal_cost();
        Double m = Double.valueOf(mItemBidAmt) * Double.valueOf(mAdapRes.getQuantity());
        mTotalPrice = String.valueOf(m);

        String mTot = "";


        String cost = mTotalPrice.replace("$", "").trim();


        if (mAdapRes.getBid_amount().equalsIgnoreCase(GlobalMethods.afterTwoPointVal(AppConstants.FAILURE_CODE))) {
            APIRequestHandler.getInstance().getBidPaypalPaymentResponse(
                    mItemID, AppConstants.FAILURE_CODE,
                    mItemBidID, mOtherUser,
                    "free", AppConstants.FAILURE_CODE, "", (BaseActivity) mContext);

        } else {
            String mServiceTxt = "", mTotalCostTxt = "";
            double pricecost = Float.parseFloat(cost);
            double service_cost = 0.60;
            if (pricecost >= 1 && pricecost < 10) {
                service_cost = 0.60;
            } else if (pricecost >= 10 && pricecost < 133) {
                service_cost = 7.50;
            } else if (pricecost >= 133 && pricecost < 200) {
                service_cost = 10;
            } else if (pricecost >= 200) {
                service_cost = 5.00;
            }
            if (service_cost == 0.60 || service_cost == 10) {
                mServiceTxt = GlobalMethods.afterTwoPointVal(String.valueOf(service_cost));
                mTotalCostTxt = GlobalMethods.afterTwoPointVal(String.valueOf(pricecost + service_cost));
            } else {

                double serviceTax = (double) ((pricecost * service_cost) / 100);
                mServiceTxt = GlobalMethods.afterTwoPointVal(String.valueOf(serviceTax));
                mTotalCostTxt = GlobalMethods.afterTwoPointVal(String.valueOf(pricecost + serviceTax));
            }

            String mItemPrice = cost;
            String mServicePrice = mServiceTxt;
            mTotalPrice = mTotalCostTxt;
            AppConstants.PAYPAL_BUY_ID = mOtherUser;
            AppConstants.PAYPAL_BID_ID = mItemBidID;
            AppConstants.PAYPAL_ITEM_ID = mItemID;
            AppConstants.PAYPAL_ITEM_QTY = mAdapRes.getQuantity();
            AppConstants.PAYPAL_ITEM_COST = mAdapRes.getBid_amount();
            AppConstants.PAYPAL_SER_FEES = mServicePrice;
            AppConstants.PAYPAL_TOT_COST = mTotalPrice;
            AppConstants.PAYPAL_NEG = AppConstants.FAILURE_CODE;
            AppConstants.PAYPAL_NEG_ID = "";
            AppConstants.PAYPAL_TIPS = AppConstants.FAILURE_CODE;
            AppConstants.PAYPAL_ITEM_DELV_TYPE = mAdapRes.getDelivery_type();
            AppConstants.PAYPAL_USER_ID = mAdapRes.getUser_id();
            AppConstants.PAYPAL_USER_NAME = mAdapRes.getUser_first_name();
            AppConstants.PAYPAL_ITEM_TYPE = mAdapRes.getItem_type();
            AppConstants.PAYPAL_ITEM_NAME = mAdapRes.getItem_name();

            Intent payIntent = new Intent(mActivity, PaymentPaypalStripScreen.class);
            mActivity.startActivity(payIntent);
            mActivity.overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_left);

        }

//        PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(mTot), "USD", mAdapRes.getItem_name(),
//                PayPalPayment.PAYMENT_INTENT_SALE);
//        Intent paypalIntent = new Intent(mActivity, PaymentActivity.class);
//        paypalIntent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
//        mActivity.startActivityForResult(paypalIntent, AppConstants.REQUEST_PAYPAL_PAYMENT);
    }


}
