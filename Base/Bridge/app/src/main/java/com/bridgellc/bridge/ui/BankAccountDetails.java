package com.bridgellc.bridge.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.apiinterface.APIRequestHandler;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.model.BankAddAccountResponse;
import com.bridgellc.bridge.ui.home.HomeScreenActivity;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.GlobalMethods;

import java.util.Locale;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

/**
 * Created by Karthi on 6/29/2016.
 */
public class BankAccountDetails extends BaseActivity implements DialogMangerOkCallback {

    private RelativeLayout mScanCard;
    //    public static EditText expiration_date;
    final String TAG = getClass().getName();
    private int MY_SCAN_REQUEST_CODE = 100;
    private LinearLayout mCardHeaderImgLay, mDebitDetailsLay, mBankDetailsLay, mPaypalDetailsLay;

    private EditText mUserNameEdt, mEmailIdEdt, mFirstNameEdt, mLastNameEdt;
    private static EditText mCardsNumberEdtTxt, mMmYyEditTxt, mCvvEditTxt;
    private String mUserName, mCardID, mCardNumber, mMonthYear, mCvv, mEmail, mFirstName, mLastName, mPaypalID;
    private String mPaymentType = AppConstants.PAYMENT_TYPE;
    private String mCard = "" ;
    private ImageView mPaypalHeaderImgLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_account_details);

        initView();
    }

    private void initView() {
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

        //Payment imputs Int
//        name_on_card = (EditText) findViewById(R.id.name_on_card);
//        card_number = (EditText) findViewById(R.id.card_number);
//        expiration_date = (EditText) findViewById(R.id.exp_date);
//        security_code = (EditText) findViewById(R.id.security_code);

        mCardHeaderImgLay = (LinearLayout) findViewById(R.id.card_header_img_lay);
        mDebitDetailsLay = (LinearLayout) findViewById(R.id.debit_details_lay);
        mUserNameEdt = (EditText) findViewById(R.id.name_on_card);
        mCardsNumberEdtTxt = (EditText) findViewById(R.id.debit_card_number);
        mCardsNumberEdtTxt.addTextChangedListener(new FourDigitCardFormatWatcher());
        mMmYyEditTxt = (EditText) findViewById(R.id.exp_date);
        mCvvEditTxt = (EditText) findViewById(R.id.security_code);
        mMmYyEditTxt.addTextChangedListener(new MonthYearValidation());


        mHeadeLeftBtnLay.setVisibility(View.VISIBLE);
        mFooterOneLay.setVisibility(View.VISIBLE);
        mFooterTwoLay.setVisibility(View.GONE);
        mFooterThreeLay.setVisibility(View.GONE);

        mFooterOneBtn.setBackgroundColor(getResources().getColor(R.color.green));
        mFooterTwoBtn.setTextColor(getResources().getColor(R.color.white));
        mFooterOneBtn.setVisibility(View.VISIBLE);
        mFooterTwoBtn.setVisibility(View.GONE);

        mFooterOneBtn.setText(getString(R.string.save_debit_card));
        mScanCard = (RelativeLayout) findViewById(R.id.scan_debit_card);
        mHeaderTxt.setText(getString(R.string.add_payment).toUpperCase(Locale.ENGLISH));

        //Paypal Int
        mPaypalDetailsLay = (LinearLayout) findViewById(R.id.paypal_details_lay);
        mFirstNameEdt = (EditText) findViewById(R.id.first_name_edt);
        mLastNameEdt = (EditText) findViewById(R.id.last_name_edt);
        mEmailIdEdt = (EditText) findViewById(R.id.email_edt);
        mPaypalHeaderImgLay = (ImageView) findViewById(R.id.paypal_header_img_lay);

//        mCardsNumberEditTxt.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                String mEdtCard = mCardsNumberEditTxt.getText().toString().trim();
//                if (mEdtCard.contains("XXXX")) {
//                    mCardsNumberEditTxt.setText("");
//                    mCard = "";
//                }
//
//                return false;
//            }
//        });
//


        setData();

//        isLayVisible(2);
    }

    private void isLayVisible(int mIsDebit) {


        if (mIsDebit == 1) {
            mPaymentType = getString(R.string.one);

            mDebitDetailsLay.setVisibility(View.VISIBLE);
            mPaypalDetailsLay.setVisibility(View.GONE);

            mCardHeaderImgLay.setVisibility(View.VISIBLE);
            mPaypalHeaderImgLay.setVisibility(View.GONE);

        } else {
            mPaymentType = getString(R.string.three);


            mDebitDetailsLay.setVisibility(View.GONE);
            mPaypalDetailsLay.setVisibility(View.VISIBLE);

            mCardHeaderImgLay.setVisibility(View.GONE);
            mPaypalHeaderImgLay.setVisibility(View.VISIBLE);


        }
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

        mEmailIdEdt.setText(AppConstants.PAYPAL_EMAIL);
        mFirstNameEdt.setText(AppConstants.PAYPAL_FIRSTNAME);
        mLastNameEdt.setText(AppConstants.PAYPAL_LASTNAME);
        mPaypalID = AppConstants.PAYPAL_ID;


        //App Contact data Clr
        AppConstants.CARD_NAME = "" ;
        AppConstants.EXPIRE_DATE = "" ;
        AppConstants.SECURITY_CODE = "" ;
        AppConstants.CARD_NUMBER = "" ;
        AppConstants.CARD_NUMBER_WITHX = "" ;
        AppConstants.CAED_ID = "" ;

        AppConstants.PAYPAL_EMAIL = "" ;
        AppConstants.PAYPAL_FIRSTNAME = "" ;
        AppConstants.PAYPAL_LASTNAME = "" ;
        AppConstants.PAYPAL_ID = "" ;

        isLayVisible(Integer.valueOf(mPaymentType));

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
        mUserName = mUserNameEdt.getText().toString();
        mCardNumber = mCardsNumberEdtTxt.getText().toString();
        mMonthYear = mMmYyEditTxt.getText().toString();
        mCvv = mCvvEditTxt.getText().toString();
        if (!mCardNumber.contains("XXXX")) {
            mCard = mCardNumber;
        }

        mCard = mCard.replaceAll("-", "");


        //Paypal data
        mEmail = mEmailIdEdt.getText().toString().trim();
        mFirstName = mFirstNameEdt.getText().toString().trim();
        mLastName = mLastNameEdt.getText().toString().trim();

        if (mPaymentType.equalsIgnoreCase(getString(R.string.one))) {

            if (mUserName.equals("")) {
                DialogManager.showBasicAlertDialog(BankAccountDetails.this,
                        getString(R.string.enter_user_name), this);

            } else if (mCard.length() != 16) {
                DialogManager.showBasicAlertDialog(BankAccountDetails.this,
                        getString(R.string.enter_card_number), this);
            } else if (mMonthYear.equals("")) {
                DialogManager.showBasicAlertDialog(BankAccountDetails.this,
                        getString(R.string.enter_month), this);

            } else if (mCvv.equals("") || mCvv.length() < 3) {
                DialogManager.showBasicAlertDialog(BankAccountDetails.this,
                        getString(R.string.enter_cvv), this);
            } else {
                mCardNumber = mCardNumber.replaceAll("-", "");
                String[] month = mMonthYear.split("/");
                //Add Card and Update API
//                if (isCreditCardValid(mCardNumber)) {

//                if (isCreditCardValid(mCard)) {
                APIRequestHandler.getInstance().getAddBankAccResponse(mPaymentType, AppConstants.FAILURE_CODE, mUserName, "", "", "", mCard, month[0], month[1], mCvv, mCardID, "", "", "", "", this);
//                }
            }
        } else {
            if (mFirstName.isEmpty()) {
                mFirstNameEdt.requestFocus();
                DialogManager.showBasicAlertDialog(BankAccountDetails.this, getString(R.string
                        .enter_first_name), this);
            } else if (mLastName.isEmpty()) {

                mLastNameEdt.requestFocus();
                DialogManager.showBasicAlertDialog(BankAccountDetails.this, getString(R.string
                        .enter_last_name), this);

            } else if (mEmail.isEmpty() || !GlobalMethods.isEmailValid(mEmail, false)) {
                mEmailIdEdt.requestFocus();
                DialogManager.showBasicAlertDialog(BankAccountDetails.this, getString(R.string
                        .enter_email_id), this);

            } else {
                APIRequestHandler.getInstance().getAddBankAccResponse(mPaymentType, AppConstants.FAILURE_CODE, "", "", "", "", "", "", "", "", "", mFirstName, mLastName, mEmail, mPaypalID, this);
            }
        }
    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        if (responseObj instanceof BankAddAccountResponse) {
            BankAddAccountResponse mRes = (BankAddAccountResponse) responseObj;
            if (mRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                GlobalMethods.storeValuetoPreference(BankAccountDetails.this,
                        GlobalMethods.STRING_PREFERENCE,
                        AppConstants.PAYMENT_MODE, mPaymentType);
                GlobalMethods.storeValuetoPreference(BankAccountDetails.this,
                        GlobalMethods.STRING_PREFERENCE,
                        AppConstants.PAYMENT_DETAILS, AppConstants.SUCCESS_CODE);
                GlobalMethods.storeValuetoPreference(BankAccountDetails.this, GlobalMethods.STRING_PREFERENCE, AppConstants.BANK_DETAILS, getString(R.string.one));

                DialogManager.showBasicAlertDialog(this,
                        mRes.getResult().getMessage(), new DialogMangerOkCallback() {
                            @Override
                            public void onOkClick() {

                                AppConstants.BANK_FROMLIST_BACK = AppConstants.FAILURE_CODE;
                                if (AppConstants.BANK_ACC_DET_BACK_SCREEN.equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
                                    previousScreen(PaymentCardListScreen.class, true);
                                } else if (AppConstants.BANK_ACC_DET_BACK_SCREEN.equalsIgnoreCase(getString(R.string.app_name))) {
                                    previousScreen(PaymentCardListScreen.class, true);
                                } else if (AppConstants.BANK_ACC_DET_BACK_SCREEN.equalsIgnoreCase(getString(R.string.eight))) {
                                    previousScreen(HomeScreenActivity.class, true);
                                } else if (AppConstants.BANK_ACC_DET_BACK_SCREEN.equalsIgnoreCase(getString(R.string.nine))) {
                                    previousScreen(NotificationScreen.class, true);
                                } else {
                                    finishScreen();
                                }
                            }
                        });
            } else {
                DialogManager.showBasicAlertDialog(this,
                        mRes.getMessage(), this);
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (AppConstants.BANK_FROMLIST_BACK.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
            AppConstants.BANK_FROMLIST_BACK = AppConstants.FAILURE_CODE;
            previousScreen(PaymentCardListScreen.class, true);
        } else {
            previousScreen(PaymentHomeScreen.class, true);
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
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String resultStr;
        if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
            CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

            // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
            resultStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n" ;

            // Do something with the raw number, e.g.:
            // myService.setCardNumber( scanResult.cardNumber );

            if (scanResult.isExpiryValid()) {
                resultStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n" ;
            }

            if (scanResult.cvv != null) {
                // Never log or display a CVV
                resultStr += "CVV has " + scanResult.cvv.length() + " digits.\n" ;
            }

            if (scanResult.postalCode != null) {
                resultStr += "Postal Code: " + scanResult.postalCode + "\n" ;
            }

            if (scanResult.cardholderName != null) {
                resultStr += "Cardholder Name : " + scanResult.cardholderName + "\n" ;
            }

            AppConstants.CARD_NUMBER = scanResult.getFormattedCardNumber().replaceAll("\\s+", "-");
            AppConstants.CARD_NUMBER_WITHX = scanResult.getRedactedCardNumber();
            if (scanResult.isExpiryValid()) {
                String month = scanResult.expiryMonth + "" ;
                String year = scanResult.expiryYear + "" ;
                year = year.substring(2);
                if (month.length() == 1) {
                    month = "0" + scanResult.expiryMonth;
                }
                AppConstants.EXPIRE_DATE = month + "/" + year;
            }
            if (scanResult.cvv != null) {
                AppConstants.SECURITY_CODE = scanResult.cvv;
            }
            AppConstants.CAED_ID = "" ;
            AppConstants.PAYMENT_TYPE = getString(R.string.one);
            AppConstants.CARD_NAME = "" ;
            setData();
        } else {
            resultStr = "Scan was canceled." ;
        }

    }


    public static class FourDigitCardFormatWatcher implements TextWatcher {

        // Change this to what you want... ' ', '-' etc..
        private static final String space = "-" ;

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

    static String mLastInput = "" ;

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
//            } else {

            }
            mLastInput = mMmYyEditTxt.getText().toString();
            return;

        }

    }

    public static boolean isCreditCardValid(String cardNumber) {
        String digitsOnly = cardNumber.replaceAll("-", "");
        int sum = 0;
        int digit = 0;
        int addend = 0;
        boolean timesTwo = false;

        if (digitsOnly.length() < 16) {
            return false;
        }
        for (int i = digitsOnly.length() - 1; i >= 0; i--) {
            digit = Integer.parseInt(digitsOnly.substring(i, i + 1));
            if (timesTwo) {
                addend = digit * 2;
                if (addend > 9) {
                    addend -= 9;
                }
            } else {
                addend = digit;
            }
            sum += addend;
            timesTwo = !timesTwo;
        }

        int modulus = sum % 10;
        return modulus == 0;

    }


    @Override
    public void onOkClick() {

    }

}
