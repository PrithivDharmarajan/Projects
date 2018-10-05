package com.bridgellc.bridge.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.apiinterface.APIRequestHandler;
import com.bridgellc.bridge.entity.CardResponse;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.model.PaypalPayResponse;
import com.bridgellc.bridge.model.SaveStripCardResponse;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.GlobalMethods;

import java.util.Locale;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;


public class AddPayCard extends BaseActivity implements DialogMangerOkCallback {

    private EditText mUserNameEdt;
    @SuppressLint("StaticFieldLeak")
    private static EditText mCardsNumberEdtTxt, mMmYyEditTxt, mCvvEditTxt;
    private String mCardID;
    private String mPaymentType = AppConstants.PAYMENT_TYPE;
    private String mCard = "";
    static String mLastInput = "";
    private Dialog mPayAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_credit_card_details);
        initComponents();
    }

    private void initComponents() {
        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);

        //Header & Footer Int
        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        mFooterOneLay = (RelativeLayout) findViewById(R.id.footer_one_lay);
        mFooterTwoLay = (RelativeLayout) findViewById(R.id.footer_two_lay);
        mFooterThreeLay = (RelativeLayout) findViewById(R.id.footer_three_lay);
        mFooterOneBtn = (Button) findViewById(R.id.footer_one_btn);
        mFooterTwoBtn = (Button) findViewById(R.id.footer_two_btn);


        TextView mScanDebitCardTxt = (TextView) findViewById(R.id.scan_debit_card_txt);
        mUserNameEdt = (EditText) findViewById(R.id.name_on_card);
        mCardsNumberEdtTxt = (EditText) findViewById(R.id.debit_card_number);
        mCardsNumberEdtTxt.addTextChangedListener(new FourDigitCardFormatWatcher());
        mMmYyEditTxt = (EditText) findViewById(R.id.exp_date);
        mCvvEditTxt = (EditText) findViewById(R.id.security_code);
        mMmYyEditTxt.addTextChangedListener(new MonthYearValidation());

//        mCardsNumberEdtTxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mCardsNumberEdtTxt.getText().toString().trim().contains("XXXX")) {
//                    mCardsNumberEdtTxt.setText("");
//                }
//            }
//        });

        mCardsNumberEdtTxt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mCardsNumberEdtTxt.getText().toString().trim().contains("XXXX")) {
                    mCardsNumberEdtTxt.setText("");
                }
                return false;
            }
        });

        mHeadeLeftBtnLay.setVisibility(View.VISIBLE);
        mFooterOneLay.setVisibility(View.VISIBLE);
        mFooterTwoLay.setVisibility(View.GONE);
        mFooterThreeLay.setVisibility(View.GONE);

        mFooterOneBtn.setBackgroundColor(getResources().getColor(R.color.green));
        mFooterTwoBtn.setTextColor(getResources().getColor(R.color.white));
        mFooterOneBtn.setVisibility(View.VISIBLE);
        mFooterTwoBtn.setVisibility(View.GONE);

        mFooterOneBtn.setText(getString(R.string.save_card));
        mScanDebitCardTxt.setText(getString(R.string.scan_card));

        mHeaderTxt.setText(getString(R.string.pay_c).toUpperCase(Locale.ENGLISH));


        APIRequestHandler.getInstance().getStripeCardResponse(this);

//        setData();

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.footer_one_btn:
                validateFields();
                break;
            case R.id.scan_debit_card:
                scanCard();
                break;
        }
    }

    private void validateFields() {


        //Card data
        String mUserName = mUserNameEdt.getText().toString();
        String mCardNumber = mCardsNumberEdtTxt.getText().toString();
        String mMonthYear = mMmYyEditTxt.getText().toString();
        String mCvv = mCvvEditTxt.getText().toString();
        if (!mCardNumber.contains("XXXX")) {
            mCard = mCardNumber;
        }

        mCard = mCard.replaceAll("-", "");


        if (mUserName.equals("")) {

            if(mPayAlert!=null && mPayAlert.isShowing()){
                mPayAlert.dismiss();
            }
            mPayAlert = DialogManager.showBasicAlertDialog(AddPayCard.this,
                    getString(R.string.enter_user_name), this);

        } else if (mCard.length() != 16) {
            if(mPayAlert!=null && mPayAlert.isShowing()){
                mPayAlert.dismiss();
            }
            mPayAlert= DialogManager.showBasicAlertDialog(AddPayCard.this,
                    getString(R.string.enter_card_number), this);
        } else if (mMonthYear.equals("")) {
            if(mPayAlert!=null && mPayAlert.isShowing()){
                mPayAlert.dismiss();
            }
            mPayAlert= DialogManager.showBasicAlertDialog(AddPayCard.this,
                    getString(R.string.enter_month), this);

        } else if (mCvv.equals("") || mCvv.length() < 3) {
            if(mPayAlert!=null && mPayAlert.isShowing()){
                mPayAlert.dismiss();
            }
            mPayAlert= DialogManager.showBasicAlertDialog(AddPayCard.this,
                    getString(R.string.enter_cvv), this);
        } else {
            mCardNumber = mCardNumber.replaceAll("-", "");
            String[] month = mMonthYear.split("/");

            if (!mCardNumber.contains("XXXX")) {

                APIRequestHandler.getInstance().getSaveStripeCardResponse(mUserName, mCard,
                        month[0], month[1], mCvv, this);


            } else {

                System.out.println("AppConstants.PAYPAL_BID_ID---"+AppConstants.PAYPAL_BID_ID);
                System.out.println("AppConstants.PAYPAL_ITEM_ID---"+AppConstants.PAYPAL_ITEM_ID);
                System.out.println("AppConstants.PAYPAL_BUY_ID---"+AppConstants.PAYPAL_BUY_ID);
                System.out.println("AppConstants.PAYPAL_NEG_ID---"+AppConstants.PAYPAL_NEG_ID);
                System.out.println("AppConstants.mCardID---"+mCardID);
                System.out.println("GlobalMethods.getUserID(this)--"+GlobalMethods.getUserID(this));
                System.out.println("AppConstants.PAYPAL_ITEM_ID---"+AppConstants.PAYPAL_ITEM_ID);
                System.out.println("AppConstants.PAYPAL_ITEM_QTY---"+AppConstants.PAYPAL_ITEM_QTY);
                System.out.println("AppConstants.PAYPAL_ITEM_COST---"+AppConstants.PAYPAL_ITEM_COST);
                System.out.println("AppConstants.PAYPAL_SER_FEES---"+AppConstants.PAYPAL_SER_FEES);
                System.out.println("AppConstants.PAYPAL_TOT_COST---"+AppConstants.PAYPAL_TOT_COST);
                System.out.println("AppConstants.PAYPAL_NEG_ID---"+AppConstants.PAYPAL_NEG_ID);
                System.out.println("AppConstants.PAYPAL_TIPS---"+AppConstants.PAYPAL_TIPS);
                System.out.println("AppConstants.AppConstants.PAYPAL_ITEM_TYPE---"+AppConstants.PAYPAL_ITEM_TYPE);



                if (!AppConstants.PAYPAL_BID_ID.equalsIgnoreCase("") && !AppConstants.PAYPAL_BID_ID.equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
                          APIRequestHandler.getInstance().getBidPaypalPaymentResponse(
                            AppConstants.PAYPAL_ITEM_ID, AppConstants.PAYPAL_TOT_COST,
                            AppConstants.PAYPAL_BID_ID, AppConstants.PAYPAL_BUY_ID,
                            "stripe", AppConstants.PAYPAL_NEG_ID, mCardID, this);
                } else {

                    APIRequestHandler.getInstance().getPaypalPaymentResponse(GlobalMethods.getUserID(this), AppConstants.PAYPAL_ITEM_ID, AppConstants.PAYPAL_ITEM_QTY, AppConstants.PAYPAL_ITEM_COST, "stripe",
                            AppConstants.PAYPAL_SER_FEES, AppConstants.PAYPAL_TOT_COST,
                            AppConstants.PAYPAL_NEG, AppConstants.PAYPAL_NEG_ID, AppConstants.PAYPAL_TIPS, mCardID, this);

                }

            }

        }

    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);
        if (responseObj instanceof PaypalPayResponse) {
            final PaypalPayResponse mPaypalResponse = (PaypalPayResponse) responseObj;

            if (mPaypalResponse.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                if(mPayAlert!=null && mPayAlert.isShowing()){
                    mPayAlert.dismiss();
                }
                mPayAlert=  DialogManager.showBasicAlertDialog(AddPayCard.this, mPaypalResponse.getResult().getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {

                        if (AppConstants.PAYPAL_ITEM_TYPE!=null&&AppConstants.PAYPAL_ITEM_TYPE
                                .equalsIgnoreCase(getString(R.string.one))) {

                            //Goods
                            if (AppConstants.PAYPAL_ITEM_DELV_TYPE
                                    .equalsIgnoreCase(getString(R.string.one))) {

                                BuyerCodeScreen.mPayID = mPaypalResponse.getResult().getPayment_id();
                                BuyerCodeScreen.mItemID = AppConstants.PAYPAL_ITEM_ID;

                                AppConstants.CODE_SCREEN = AppConstants.HOME_SCREEN;
                                nextScreen(BuyerCodeScreen.class, true);
                            } else {


                                RateBuySellScreen.mHeader = getString(R.string.rateseller);
                                AppConstants.RATING_ITEM_ID = AppConstants.PAYPAL_ITEM_ID;
                                AppConstants.RATING_USER_ID = AppConstants.PAYPAL_USER_ID;
                                AppConstants.RATING_USER_NAME = AppConstants.PAYPAL_USER_NAME;

                                AppConstants.RATING_RATE = "";
                                AppConstants.RATING_CMD = "";
                                nextScreen(RateBuySellScreen.class, true);


                            }

                        } else {
                            //Services
                            previousScreen(DashboardScreen.class, true);

                        }


//                    } else {
//                        ChatScreen.mPaymentId = mPaypalResponse.getResult().getPayment_id();
//                        AppConstants.CODE_SCREEN = AppConstants.HOME_SCREEN;
//                        AppConstants.CHAT_FRIEND_ID = AppConstants.PAYPAL_USER_ID;
//                        AppConstants.CHAT_ITEM_ID = AppConstants.PAYPAL_ITEM_ID;
//                        nextScreen(ChatScreen.class, true);
                    }


                });

//                }
            } else {
                if(mPayAlert!=null && mPayAlert.isShowing()){
                    mPayAlert.dismiss();
                }
                mPayAlert=  DialogManager.showBasicAlertDialog(this,
                        mPaypalResponse.getMessage(), new DialogMangerOkCallback() {
                            @Override
                            public void onOkClick() {

                            }
                        });
            }
        } else if (responseObj instanceof CardResponse) {

            CardResponse cardResponse = (CardResponse) responseObj;
            if (cardResponse.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                AppConstants.CARD_NAME = cardResponse.getResult().getName();
                AppConstants.EXPIRE_DATE = cardResponse.getResult().getMonth() + "/" + cardResponse
                        .getResult().getYear();
                AppConstants.SECURITY_CODE = cardResponse.getResult().getCvv();
                AppConstants.CARD_NUMBER = "XXXX-XXXX-XXXX-" + cardResponse.getResult()
                        .getCard();
                AppConstants.CAED_ID = cardResponse.getResult().getCard_id();
                setData();

            } else {

            }

        } else if (responseObj instanceof SaveStripCardResponse) {

            SaveStripCardResponse cardStoreResponse = (SaveStripCardResponse) responseObj;
            if (cardStoreResponse.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                mCardID = cardStoreResponse.getResult().getCard_id();
                if (!AppConstants.PAYPAL_BID_ID.equalsIgnoreCase("") && !AppConstants.PAYPAL_BID_ID.equalsIgnoreCase(AppConstants.FAILURE_CODE)) {

                    APIRequestHandler.getInstance().getBidPaypalPaymentResponse(AppConstants.PAYPAL_ITEM_ID, AppConstants.PAYPAL_TOT_COST, AppConstants.PAYPAL_BID_ID, AppConstants.PAYPAL_BUY_ID, "stripe", AppConstants.PAYPAL_NEG_ID, mCardID, this);

                } else {

                    APIRequestHandler.getInstance().getPaypalPaymentResponse(GlobalMethods.getUserID(this), AppConstants.PAYPAL_ITEM_ID, AppConstants.PAYPAL_ITEM_QTY, AppConstants.PAYPAL_ITEM_COST, "stripe", AppConstants.PAYPAL_SER_FEES, AppConstants.PAYPAL_TOT_COST,
                            AppConstants.PAYPAL_NEG, AppConstants.PAYPAL_NEG_ID, AppConstants.PAYPAL_TIPS, mCardID, this);

                }
            } else {
                if(mPayAlert!=null && mPayAlert.isShowing()){
                    mPayAlert.dismiss();
                }
                mPayAlert=  DialogManager.showBasicAlertDialog(this,
                        cardStoreResponse.getMessage(), new DialogMangerOkCallback() {
                            @Override
                            public void onOkClick() {

                            }
                        });
            }

        }
    }


    private void scanCard() {
        Intent scanIntent = new Intent(this, CardIOActivity.class);

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_RESTRICT_POSTAL_CODE_TO_NUMERIC_ONLY, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME, false); // default: false

        // hides the manual entry button
        // if set, developers should provide their own manual entry mechanism in the app
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, false); // default: false

        // matches the theme of your application
        scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, false); // default: false

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, 100);
    }


    private void setData() {


        if (!AppConstants.CARD_NUMBER_WITHX.equalsIgnoreCase("") && AppConstants.CARD_NUMBER_WITHX.contains("XXXX")) {
            String cardnum = AppConstants.CARD_NUMBER_WITHX.replaceAll("-", "");
            mCardsNumberEdtTxt.setText(cardnum.substring(0, 4) + "-" + cardnum.substring(4, 8) + "-" + cardnum.substring(8, 12) + "-" + cardnum.substring(12, 16));
        } else {
            mCardsNumberEdtTxt.setText(AppConstants.CARD_NUMBER);
        }

        mUserNameEdt.setText(AppConstants.CARD_NAME);
        mMmYyEditTxt.setText(AppConstants.EXPIRE_DATE);
        mCvvEditTxt.setText(AppConstants.SECURITY_CODE);
        mCard = AppConstants.CARD_NUMBER;
        mCardID = AppConstants.CAED_ID;


        //App Contact data Clr
        AppConstants.CARD_NAME = "";
        AppConstants.EXPIRE_DATE = "";
        AppConstants.SECURITY_CODE = "";
        AppConstants.CARD_NUMBER = "";
        AppConstants.CARD_NUMBER_WITHX = "";
        AppConstants.CAED_ID = "";


    }


    public static class FourDigitCardFormatWatcher implements TextWatcher {

        // Change this to what you want... ' ', '-' etc..
        private static final String space = "-";

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Remove spacing char
            if (s.length() > 0 && (s.length() % 5) == 0) {
                final char c = s.charAt(s.length() - 1);
                if (space.equalsIgnoreCase(String.valueOf(c))) {
                    s.delete(s.length() - 1, s.length());
                }
            }
            // Insert char where needed.
            if (s.length() > 0 && (s.length() % 5) == 0) {
                char c = s.charAt(s.length() - 1);
                // Only if its a digit where there should be a space we insert a
                // space
                if (Character.isDigit(c)
                        && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
                    s.insert(s.length() - 1, String.valueOf(space));
                }
            }

        }
    }

    public static class MonthYearValidation implements TextWatcher {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String input = s.toString();

            if (s.length() == 2 && !mLastInput.endsWith("/")) {
                int month = Integer.parseInt(input);

                if (month <= 12) {
                    mMmYyEditTxt.setText(mMmYyEditTxt.getText().toString()
                            + "/");
                    mMmYyEditTxt.setSelection(mMmYyEditTxt.getText().toString()
                            .length());
                } else {
                    mMmYyEditTxt.setText(mMmYyEditTxt.getText().toString()
                            .substring(0, s.length() - 1));
                    mMmYyEditTxt.setSelection(mMmYyEditTxt.getText().toString()
                            .length());
                }
            } else if (s.length() == 2 && mLastInput.endsWith("/")) {
                int month = Integer.parseInt(input);
                if (month <= 12) {
                    mMmYyEditTxt.setText(mMmYyEditTxt.getText().toString()
                            .substring(0, 1));
                    mMmYyEditTxt.setSelection(mMmYyEditTxt.getText().toString()
                            .length());
                } else {
                    mMmYyEditTxt.setText("");
                    mMmYyEditTxt.setSelection(mMmYyEditTxt.getText().toString()
                            .length());

                }
            } else if (s.length() == 1) {
                int month = Integer.parseInt(input);
                if (month > 1) {
                    mMmYyEditTxt.setText("0"
                            + mMmYyEditTxt.getText().toString() + "/");
                    mMmYyEditTxt.setSelection(mMmYyEditTxt.getText().toString()
                            .length());
                }

            }
            mLastInput = mMmYyEditTxt.getText().toString();
            return;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String resultStr;
        if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
            CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

            // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
            resultStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";

            // Do something with the raw number, e.g.:
            // myService.setCardNumber( scanResult.cardNumber );

            if (scanResult.isExpiryValid()) {
                resultStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
            }

            if (scanResult.cvv != null) {
                // Never log or display a CVV
                resultStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
            }

            if (scanResult.postalCode != null) {
                resultStr += "Postal Code: " + scanResult.postalCode + "\n";
            }

            if (scanResult.cardholderName != null) {
                resultStr += "Cardholder Name : " + scanResult.cardholderName + "\n";
            }

            AppConstants.CARD_NUMBER = scanResult.getFormattedCardNumber().replaceAll("\\s+", "-");
            AppConstants.CARD_NUMBER_WITHX = scanResult.getRedactedCardNumber();
            if (scanResult.isExpiryValid()) {
                String month = scanResult.expiryMonth + "";
                String year = scanResult.expiryYear + "";
                year = year.substring(2);
                if (month.length() == 1) {
                    month = "0" + scanResult.expiryMonth;
                }
                AppConstants.EXPIRE_DATE = month + "/" + year;
            }
            if (scanResult.cvv != null) {
                AppConstants.SECURITY_CODE = scanResult.cvv;
            }
            AppConstants.CAED_ID = "";
            AppConstants.PAYMENT_TYPE = getString(R.string.one);
            AppConstants.CARD_NAME = "";
            setData();
        } else {
            resultStr = "Scan was canceled.";
        }

    }

    @Override
    public void onBackPressed() {
        previousScreen(PaymentPaypalStripScreen.class, true);
    }

    @Override
    public void onOkClick() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mPayAlert!=null && mPayAlert.isShowing()){
            mPayAlert.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPayAlert!=null && mPayAlert.isShowing()){
            mPayAlert.dismiss();
        }
    }
}

