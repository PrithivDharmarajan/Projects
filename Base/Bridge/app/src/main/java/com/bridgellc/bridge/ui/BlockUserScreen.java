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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.model.CommonResponse;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogManagerTwoBtnCallback;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;

import java.util.Locale;


public class BlockUserScreen extends BaseActivity implements View.OnClickListener, DialogMangerOkCallback {

    private RelativeLayout mGoodsTickLay, mServiceTickLay, mInfoPartLay;
    private LinearLayout mBotLay;
    private String mItemType = AppConstants.FAILURE_CODE;
    private TextView mGoodsTxt, mServiceTxt, mTopTxt;
    public static String mReportUserTxt = "", mReportItemTxt = "";

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

        mTopTxt = (TextView) findViewById(R.id.top_txt);
        mGoodsTxt = (TextView) findViewById(R.id.goods_txt);
        mServiceTxt = (TextView) findViewById(R.id.service_txt);
        mGoodsTxt.setText(getString(R.string.block_user));
        mServiceTxt.setText(getString(R.string.report));


        mGoodsTickLay = (RelativeLayout) findViewById(R.id.goods_tick_lay);
        mServiceTickLay = (RelativeLayout) findViewById(R.id.service_tick_lay);
        mInfoPartLay = (RelativeLayout) findViewById(R.id.info_part_lay);
        mBotLay = (LinearLayout) findViewById(R.id.bot_lay);
        if (mBotLay != null) {
            mBotLay.setVisibility(View.GONE);
        }

        mHeadeRightBtnLay = (RelativeLayout) findViewById(R.id.header_right_btn_lay);

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

        mTopTxt.setText(getString(R.string.are_you_block));
        mHeaderTxt.setText(getString(R.string.report).toUpperCase(Locale.ENGLISH));
        mFooterOneBtn.setText(getString(R.string.submit));
        mFooterTwoBtn.setText(getString(R.string.cancel));

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) BlockUserScreen.this
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
                if (!mItemType.equalsIgnoreCase(AppConstants.FAILURE_CODE)) {


                    String mAlertMsg = mItemType.equalsIgnoreCase(getString(R.string.one)) ? getString(R.string.are_sure_block) : getString(R.string.are_sure_item);

                    DialogManager.showBaseTwoBtnDialog(this, getString(R.string.app_name), mAlertMsg, getString(R.string.yes), getString(R.string.no), new DialogManagerTwoBtnCallback() {
                        @Override
                        public void onBtnOkClick(String mOkStr) {


                            String sharetxt = mItemType.equalsIgnoreCase(getString(R.string.one)) ?
                                    mReportUserTxt :mReportItemTxt ;

                            sharetxt = "\n\t" + String.format(getString(R.string
                                    .report_user), "") + sharetxt;
                            final Intent emailInt = new Intent(Intent.ACTION_SENDTO);
                            emailInt.setData(Uri.parse("mailto:" + AppConstants.PARTNER_EMAIL_ID));
                            emailInt.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.con_us));
                            emailInt.putExtra(Intent.EXTRA_TEXT, sharetxt);
                            startActivity(emailInt);
                        }

                        @Override
                        public void onBtnCancelClick(String mCancelStr) {

                        }
                    });

                } else {

//                    if (mItemType.equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
//                        mAlertMsg = getString(R.string.select_block);
//                    }
                    DialogManager.showBasicAlertDialog(this, getString(R.string.select_block), this);

                }
                break;
            case R.id.part_right_btn_lay:
                mInfoPartLay.setVisibility(View.GONE);
                break;

            case R.id.footer_two_btn:
                onBackPressed();
                break;

            case R.id.header_right_btn_lay:
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
        finishScreen();
    }

    @Override
    public void onOkClick() {

    }
}
