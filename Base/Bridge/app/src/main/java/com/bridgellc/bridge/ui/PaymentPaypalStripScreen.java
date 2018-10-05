package com.bridgellc.bridge.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.apiinterface.APIRequestHandler;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.model.PaypalPayResponse;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.GlobalMethods;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Locale;

public class PaymentPaypalStripScreen extends BaseActivity {

    private Dialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_home_screen);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, AppConstants.CONFIG);
        startService(intent);
        initComponents();
    }

    private void initComponents() {
        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);

        //Header & Footer Int
        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        mHeadeLeftBtnLay.setVisibility(View.VISIBLE);

        mHeaderTxt.setText(getString(R.string.pay_c).toUpperCase(Locale.ENGLISH));


    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.payment_card_btn:
                nextScreen(AddPayCard.class, true);
                break;
            case R.id.paypal_btn:
                callPaypalPayment();
                break;
        }
    }


    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);
        if (responseObj instanceof PaypalPayResponse) {
            final PaypalPayResponse mPaypalResponse = (PaypalPayResponse) responseObj;

            if (mPaypalResponse.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                if (mAlertDialog != null && mAlertDialog.isShowing()) {
                    mAlertDialog.dismiss();
                }
                mAlertDialog = DialogManager.showBasicAlertDialog(PaymentPaypalStripScreen.this,
                        mPaypalResponse.getResult().getMessage(), new DialogMangerOkCallback() {
                            @Override
                            public void onOkClick() {

                                //You are buyer
                                if (AppConstants.PAYPAL_ITEM_TYPE != null && AppConstants.PAYPAL_ITEM_TYPE
                                        .equalsIgnoreCase(getString(R.string.one))) {

                                    //Goods
                                    if (AppConstants.PAYPAL_ITEM_DELV_TYPE
                                            .equalsIgnoreCase(getString(R.string.one))) {

                                        BuyerCodeScreen.mPayID = mPaypalResponse.getResult().getPayment_id();
                                        BuyerCodeScreen.mItemID = AppConstants.PAYPAL_ITEM_ID;

                                        AppConstants.CODE_SCREEN = AppConstants.HOME_SCREEN;
                                        nextScreen(BuyerCodeScreen.class, true);
                                    } else {
//                                DialogManager.showBasicAlertDialog(PaymentPaypalStripScreen.this, getString(R
//                                        .string.approve), getString(R.string.electronic_goods_delivery_text), new DialogMangerOkCallback() {
//
//                                    @Override
//                                    public void onOkClick() {


                                        RateBuySellScreen.mHeader = getString(R.string.rateseller);
                                        AppConstants.RATING_ITEM_ID = AppConstants.PAYPAL_ITEM_ID;
                                        AppConstants.RATING_USER_ID = AppConstants.PAYPAL_USER_ID;
                                        AppConstants.RATING_USER_NAME = AppConstants.PAYPAL_USER_NAME;

                                        AppConstants.RATING_RATE = "";
                                        AppConstants.RATING_CMD = "";
                                        nextScreen(RateBuySellScreen.class, true);


//                                    }
//                                });
                                    }
                                } else {
                                    //Services
                                    previousScreen(DashboardScreen.class, true);

                                }

                            }
                        });

            } else {
                if (mAlertDialog != null && mAlertDialog.isShowing()) {
                    mAlertDialog.dismiss();
                }
                mAlertDialog = DialogManager.showBasicAlertDialog(PaymentPaypalStripScreen.this, mPaypalResponse.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {

                    }
                });
            }
        }

    }

    private void callPaypalPayment() {

        PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(AppConstants.PAYPAL_TOT_COST), "USD", GlobalMethods.isGoodsService(this, AppConstants.PAYPAL_ITEM_TYPE) + " : " + AppConstants.PAYPAL_ITEM_NAME,
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent paypalIntent = new Intent(this, PaymentActivity.class);
        paypalIntent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
        startActivityForResult(paypalIntent, AppConstants.REQUEST_PAYPAL_PAYMENT);
    }


    @Override
    public void onBackPressed() {
        finishScreen();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppConstants.REQUEST_PAYPAL_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data
                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {

                        JSONObject jsonObj = new JSONObject(confirm.toJSONObject().toString());
                        String paymentId = jsonObj.getJSONObject("response").getString("id");


                        if (!AppConstants.PAYPAL_BID_ID.equalsIgnoreCase("") && !AppConstants.PAYPAL_BID_ID.equalsIgnoreCase(AppConstants.FAILURE_CODE)) {

                            APIRequestHandler.getInstance().getBidPaypalPaymentResponse(AppConstants.PAYPAL_ITEM_ID, AppConstants.PAYPAL_TOT_COST, AppConstants.PAYPAL_BID_ID, AppConstants.PAYPAL_BUY_ID, paymentId, AppConstants.PAYPAL_NEG_ID, "", this);

                        } else {

                            APIRequestHandler.getInstance().getPaypalPaymentResponse(AppConstants.PAYPAL_BUY_ID, AppConstants.PAYPAL_ITEM_ID, AppConstants.PAYPAL_ITEM_QTY, AppConstants.PAYPAL_ITEM_COST, paymentId, AppConstants.PAYPAL_SER_FEES, AppConstants.PAYPAL_TOT_COST,
                                    AppConstants.PAYPAL_NEG, AppConstants.PAYPAL_NEG_ID, AppConstants.PAYPAL_TIPS, "", this);

                        }


                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment was submitted. Please see the docs.");
            }
        }


    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }
}
