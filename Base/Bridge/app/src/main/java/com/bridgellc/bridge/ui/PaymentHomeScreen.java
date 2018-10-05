package com.bridgellc.bridge.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.ui.home.HomeScreenActivity;
import com.bridgellc.bridge.utils.AppConstants;

import java.util.Locale;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

/**
 * Created by Karthi on 6/30/2016.
 */
public class PaymentHomeScreen extends BaseActivity {

    private RelativeLayout mScanDebitcard, mManuallyLater;
    private Button mPayment_card, mPaypal;

    final String TAG = getClass().getName();
    private int MY_SCAN_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_selection_screen);

        initView();
    }

    private void initView() {
        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);

        //Header & Footer Int
        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        mHeadeLeftBtnLay.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.add_payment).toUpperCase(Locale.ENGLISH));

        mScanDebitcard = (RelativeLayout) findViewById(R.id.scan_debit_card);
        mManuallyLater = (RelativeLayout) findViewById(R.id.manually_later);
        mPayment_card = (Button) findViewById(R.id.payment_card);
        mPaypal = (Button) findViewById(R.id.paypal_image);

        mPayment_card.setText(getString(R.string.pay_card).toUpperCase(Locale.ENGLISH));

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.scan_debit_card:
                scanCard();
                break;
            case R.id.manually_later:
                AppConstants.PAYMENT_TYPE = getString(R.string.one);
                nextScreen(BankAccountDetails.class, true);
                break;
            case R.id.payment_card:
                AppConstants.PAYMENT_TYPE = getString(R.string.one);
                nextScreen(BankAccountDetails.class, true);
                break;
            case R.id.paypal_image:
                AppConstants.PAYMENT_TYPE = getString(R.string.three);
                nextScreen(BankAccountDetails.class, true);
                break;
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

            AppConstants.PAYMENT_TYPE = getString(R.string.one);
            nextScreen(BankAccountDetails.class, true);
        } else {
            resultStr = "Scan was canceled.";
        }

    }

    @Override
    public void onBackPressed() {
        if (AppConstants.BANK_ACC_DET_BACK_SCREEN.equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
            previousScreen(SettingsScreen.class, true);
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

}
