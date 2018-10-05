package com.bridgellc.bridge.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.apiinterface.APIRequestHandler;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.model.CommonResponse;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;

import java.util.Locale;

/**
 * Created by admin on 7/4/2016.
 */
public class PartnerScreen extends BaseActivity implements View.OnClickListener, DialogMangerOkCallback {

    private EditText mPartnerCodeEdt;
    private LinearLayout mPartLay;
    private RelativeLayout mGoodsTickLay, mServiceTickLay, mInfoPartLay;
    private String mPartnerCode, mItemType = AppConstants.FAILURE_CODE;
    private ImageView mHeaderRightImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_partner_screen);
        initComponents();
    }

    private void initComponents() {

        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);

        //Header & Footer Init
        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        mHeadeRightBtnLay = (RelativeLayout) findViewById(R.id.header_right_btn_lay);
        mFooterOneLay = (RelativeLayout) findViewById(R.id.footer_one_lay);
        mFooterTwoLay = (RelativeLayout) findViewById(R.id.footer_two_lay);
        mFooterThreeLay = (RelativeLayout) findViewById(R.id.footer_three_lay);
        mFooterOneBtn = (Button) findViewById(R.id.footer_one_btn);
        mFooterTwoBtn = (Button) findViewById(R.id.footer_two_btn);


        mPartnerCodeEdt = (EditText) findViewById(R.id.partner_code_edt);
        mGoodsTickLay = (RelativeLayout) findViewById(R.id.goods_tick_lay);
        mServiceTickLay = (RelativeLayout) findViewById(R.id.service_tick_lay);
        mInfoPartLay = (RelativeLayout) findViewById(R.id.info_part_lay);

        mHeadeRightBtnLay = (RelativeLayout) findViewById(R.id.header_right_btn_lay);
        mHeaderRightImg = (ImageView) findViewById(R.id.header_right_img);
        mHeadeRightBtnLay.setVisibility(View.VISIBLE);
        mHeaderRightImg.setImageResource(R.drawable.partner_inf);

        mPartLay = (LinearLayout) findViewById(R.id.part_lay);

        mGoodsTickLay.setTag(getString(R.string.off));
        mServiceTickLay.setTag(getString(R.string.off));

        //SetData
        mHeadeLeftBtnLay.setVisibility(View.VISIBLE);
        mFooterOneLay.setVisibility(View.VISIBLE);
        mFooterTwoLay.setVisibility(View.VISIBLE);
        mFooterThreeLay.setVisibility(View.GONE);

        mFooterOneLay.setBackgroundColor(getResources().getColor(R.color.blue_btn_bg));
        mFooterTwoLay.setBackgroundColor(getResources().getColor(R.color.green));
//        mFooterTwoBtn.setTextColor(getResources().getColor(R.color.blue_btn_bg));

        mHeaderTxt.setText(getString(R.string.become_par).toUpperCase(Locale.ENGLISH));
        mFooterOneBtn.setText(getString(R.string.submit));
        mFooterTwoBtn.setText(getString(R.string.cancel));

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) PartnerScreen.this
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 300);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;

            case R.id.goods_img:
                mItemType = getString(R.string.one);
                if (mGoodsTickLay.getTag().toString().equalsIgnoreCase(getString(R.string.off))) {
                    mGoodsTickLay.setTag(getString(R.string.on));
                    mServiceTickLay.setTag(getString(R.string.off));

                    mGoodsTickLay.setVisibility(View.VISIBLE);
                    mServiceTickLay.setVisibility(View.INVISIBLE);

                }
                break;
            case R.id.service_img:
                mItemType = getString(R.string.two);
                if (mServiceTickLay.getTag().toString().equalsIgnoreCase(getString(R.string.off))) {
                    mGoodsTickLay.setTag(getString(R.string.off));
                    mServiceTickLay.setTag(getString(R.string.on));

                    mGoodsTickLay.setVisibility(View.INVISIBLE);
                    mServiceTickLay.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.partner_email_txt:
                final Intent emailInt = new Intent(Intent.ACTION_SENDTO);
                emailInt.setData(Uri.parse("mailto:" + AppConstants.PARTNER_EMAIL_ID));
                emailInt.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.become_par));
                emailInt.putExtra(Intent.EXTRA_TEXT, getString(R.string.i_would_like));
                startActivity(emailInt);
                break;

            case R.id.footer_one_btn:
                mPartnerCode = mPartnerCodeEdt.getText().toString().trim();
                if (mPartnerCode.length() > 0 && !mItemType.equalsIgnoreCase(AppConstants.FAILURE_CODE)) {

                    APIRequestHandler.getInstance().getMakePartnerResponse(mPartnerCode, mItemType, this);
                } else {
                    String mAlertMsg = getString(R.string.enter_partner_code);
                    if (mItemType.equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
                        mAlertMsg = getString(R.string.select_goods);
                    }
                    DialogManager.showBasicAlertDialog(this, mAlertMsg, this);

                }
                break;
            case R.id.part_right_btn_lay:
                mPartLay.setFocusable(true);
                mPartLay.setClickable(true);
                mPartLay.setFocusableInTouchMode(true);
                mInfoPartLay.setVisibility(View.GONE);
                break;

            case R.id.footer_two_btn:
                onBackPressed();
                break;

            case R.id.header_right_btn_lay:
                mPartLay.setFocusable(false);
                mPartLay.setClickable(false);
                mPartLay.setFocusableInTouchMode(false);
                mInfoPartLay.setVisibility(View.VISIBLE);
                break;
        }

    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);
        if (responseObj instanceof CommonResponse) {
            CommonResponse mPartnerRes = (CommonResponse) responseObj;
            if (mPartnerRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                DialogManager.showBasicAlertDialog(this, mPartnerRes.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {
                        onBackPressed();
                    }
                });

            } else {
                DialogManager.showBasicAlertDialog(this, mPartnerRes.getMessage(), this);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        previousScreen(DashboardScreen.class, true);
    }

    @Override
    public void onOkClick() {

    }
}
