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
import com.bridgellc.bridge.model.UniqueCodeModelResponse;
import com.bridgellc.bridge.ui.home.HomeScreenActivity;
import com.bridgellc.bridge.utils.AppConstants;

import java.util.Locale;

/**
 * Created by USER on 3/17/2016.
 */
public class BuyerCodeScreen extends BaseActivity implements View.OnClickListener {


    private Button mContBtn;
    private TextView mCodeViewTxt, mCodeText;
    private String mEmail;
    private RelativeLayout mBottLay;
    private ImageView mArrowImage;

    private String mBuyerCode;
    public static String mItemID, mPayID;

    private TextView mBuyerMsgText;

    public static String mBuyerName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyer_code_screen);
        initComponents();
    }

    private void initComponents() {
        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);

        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        mContBtn = (Button) findViewById(R.id.footer_btn);
        mBottLay = (RelativeLayout) findViewById(R.id.bott_lay);

        mCodeText = (TextView) findViewById(R.id.code_txt);

        mCodeViewTxt = (TextView) findViewById(R.id.code_view_txt);

        mBuyerMsgText = (TextView) findViewById(R.id.buyer_msg_txt);

        mContBtn.setText(getString(R.string.cont_chat));
        mHeaderTxt.setText(getString(R.string.code).toUpperCase(Locale.ENGLISH));
        mHeadeLeftBtnLay.setVisibility(View.VISIBLE);

        mCodeText.setText(mBuyerCode);


//        String text = String.format(getString(R.string.code_msg), mBuyerName);

        mBuyerMsgText.setText(getString(R.string.code_msg));

//        mArrowImage = (ImageView) findViewById(R.id.close_img);
//        mArrowImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                HomeScreenFilterDialog.showMenuAlertDialog(BuyerCodeScreen.this);
//            }
//        });

        if (AppConstants.RATING_BTN.equalsIgnoreCase(AppConstants.FAILURE_CODE)) {

            AppConstants.RATING_BTN = getString(R.string.one);
            mBottLay.setVisibility(View.GONE);
            mCodeViewTxt.setVisibility(View.GONE);
        }

        APIRequestHandler.getInstance().getSaleCodeResponse(mItemID, mPayID, this);
    }

    @Override
    public void onBackPressed() {
        if (AppConstants.CODE_SCREEN.equalsIgnoreCase(AppConstants.HOME_SCREEN)) {
            AppConstants.CODE_SCREEN = AppConstants.SUCCESS_CODE;
            previousScreen(HomeScreenActivity.class, true);
        } else {
            finishScreen();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.footer_btn:
//                ChatScreen.isSend = true;
//                nextScreen(ChatScreen.class, true);

                previousScreen(DashboardScreen.class, true);
                break;


        }
    }


    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);
        if (responseObj instanceof UniqueCodeModelResponse) {
            UniqueCodeModelResponse mUniqueCodeRes = (UniqueCodeModelResponse) responseObj;

            if (mUniqueCodeRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                mCodeText.setText(mUniqueCodeRes.getResult().getUnique_code());


            } else {

            }

        }

    }
}
