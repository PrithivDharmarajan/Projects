package com.bridgellc.bridge.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.apiinterface.APIRequestHandler;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.model.FinishServicesResponse;
import com.bridgellc.bridge.ui.home.HomeScreenActivity;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;

import java.util.Locale;

public class InspectModeScreen extends BaseActivity implements View.OnClickListener {

    private int numberCount = 0;
    private ImageView mHeaderRightImg, slidePointer1, slidePointer2, slidePointer3,
            slidePointer4;
    private Button oneButton, twoButton, threeButton, fourButton, fiveButton,
            sixButton, sevenButton, eightButton,
            nineButton, zeroButton;


    private String friendId = "1", itemId = "1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inspect_mode_screen);
        if (getIntent().hasExtra("FriendId")) {
            friendId = getIntent().getStringExtra("FriendId");
        }
        if (getIntent().hasExtra("ItemId")) {
            itemId = getIntent().getStringExtra("ItemId");
        }
//        if (getIntent().hasExtra("UserName")) {
//            UserName = getIntent().getStringExtra("UserName");
//        }
        initComponents();
    }

    private void initComponents() {
        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);

        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);

        mHeaderTxt.setText(getString(R.string.inspect_mode).toUpperCase(Locale.ENGLISH));
        mHeadeLeftBtnLay.setVisibility(View.VISIBLE);

        mHeadeRightBtnLay = (RelativeLayout) findViewById(R.id.header_right_btn_lay);

        mHeaderRightImg = (ImageView) findViewById(R.id.header_right_img);
        mHeaderRightImg.setImageResource(R.drawable.refresh_icon);

        mHeadeRightBtnLay.setVisibility(View.VISIBLE);
        mHeaderRightImg.setVisibility(View.VISIBLE);


        oneButton = (Button) findViewById(R.id.one);
        twoButton = (Button) findViewById(R.id.two);
        threeButton = (Button) findViewById(R.id.three);
        fourButton = (Button) findViewById(R.id.four);
        fiveButton = (Button) findViewById(R.id.five);
        sixButton = (Button) findViewById(R.id.six);
        sevenButton = (Button) findViewById(R.id.seven);
        eightButton = (Button) findViewById(R.id.eight);
        nineButton = (Button) findViewById(R.id.nine);
        zeroButton = (Button) findViewById(R.id.zero);


        slidePointer1 = (ImageView) findViewById(R.id.slidepointer_one);
        slidePointer2 = (ImageView) findViewById(R.id.slidepointer_two);
        slidePointer3 = (ImageView) findViewById(R.id.slidepointer_three);
        slidePointer4 = (ImageView) findViewById(R.id.slidepointer_four);


    }

    @Override
    public void onBackPressed() {
        previousScreen(ProductDetailsScreen.class, true);
    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);

        FinishServicesResponse commonResponse = (FinishServicesResponse) responseObj;


        if (commonResponse.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
            DialogManager.showBasicAlertDialog(InspectModeScreen.this, commonResponse.getMessage(), new DialogMangerOkCallback() {

                @Override
                public void onOkClick() {
//                    nextScreen(RateBuySellScreen.class, true);

                    RateBuySellScreen.mHeader = getString(R.string.ratebuyer);
//                    Intent intent = new Intent(InspectModeScreen.this, RateBuySellScreen.class);
//                    intent.putExtra("FriendId", friendId);
//                    intent.putExtra("ItemId", itemId);
//                    intent.putExtra("UserName", UserName);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.slide_in_right,
//                            R.anim.slide_out_left);
                    nextScreen(RateBuySellScreen.class, true);
//                    finish();

                }
            });
        } else if (commonResponse.getResponse_code().equalsIgnoreCase(getString(R.string.two))) {
            AppConstants.BANK_ACC_DET_BACK_SCREEN = getString(R.string.five);
            nextScreen(PaymentHomeScreen.class, false);
        } else if (commonResponse.getResponse_code().equalsIgnoreCase(getString(R.string.three))) {
            DialogManager.showBasicAlertDialog(this,
                    commonResponse.getMessage(), new DialogMangerOkCallback() {
                        @Override
                        public void onOkClick() {
                            previousScreen(HomeScreenActivity.class, true);
                        }
                    });
        } else {
            DialogManager.showBasicAlertDialog(InspectModeScreen.this, getString(R.string.enter_inspect_mode_c), new DialogMangerOkCallback
                    () {

                @Override
                public void onOkClick() {


                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.header_right_btn_lay:
                shopCode = "";
                numberCount = 0;
                slidePointer1
                        .setImageResource(R.drawable.keypad_selection_on);
                slidePointer2
                        .setImageResource(R.drawable.keypad_selection_on);
                slidePointer3
                        .setImageResource(R.drawable.keypad_selection_on);
                slidePointer4
                        .setImageResource(R.drawable.keypad_selection_on);
                break;
            case R.id.one:
                codeVerificationButtonClick((String) oneButton.getTag());
                break;
            case R.id.two:
                codeVerificationButtonClick((String) twoButton.getTag());
                break;
            case R.id.three:
                codeVerificationButtonClick((String) threeButton.getTag());
                break;
            case R.id.four:
                codeVerificationButtonClick((String) fourButton.getTag());
                break;
            case R.id.five:
                codeVerificationButtonClick((String) fiveButton.getTag());
                break;
            case R.id.six:
                codeVerificationButtonClick((String) sixButton.getTag());
                break;
            case R.id.seven:
                codeVerificationButtonClick((String) sevenButton.getTag());
                break;
            case R.id.eight:
                codeVerificationButtonClick((String) eightButton.getTag());
                break;
            case R.id.nine:
                codeVerificationButtonClick((String) nineButton.getTag());
                break;
            case R.id.zero:
                codeVerificationButtonClick((String) zeroButton.getTag());
                break;

        }
    }

    String shopCode = "";

    private void codeVerificationButtonClick(String text) {

        numberCount++;

        shopCode = shopCode += text;

        switch (numberCount) {
            case 1:
                slidePointer1.setImageResource(R.drawable.keypad_selection_off);
                break;
            case 2:
                slidePointer2.setImageResource(R.drawable.keypad_selection_off);
                break;
            case 3:
                slidePointer3.setImageResource(R.drawable.keypad_selection_off);
                break;
            case 4:
                slidePointer4.setImageResource(R.drawable.keypad_selection_off);
                break;
            default:
                break;
        }

        // Maximum code number limit is 4;
        if (numberCount == 4) {
            APIRequestHandler.getInstance().finishSaleResponse(itemId, shopCode,
                    InspectModeScreen.this);

            shopCode = "";
            numberCount = 0;
            slidePointer1
                    .setImageResource(R.drawable.keypad_selection_on);
            slidePointer2
                    .setImageResource(R.drawable.keypad_selection_on);
            slidePointer3
                    .setImageResource(R.drawable.keypad_selection_on);
            slidePointer4
                    .setImageResource(R.drawable.keypad_selection_on);

        }


    }


}
