package com.smaat.renterblock.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.UserDetailsEntity;
import com.smaat.renterblock.main.BaseActivity;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.PreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateAccountScreen extends BaseActivity {

    @BindView(R.id.account_parent_lay)
    ViewGroup mAccountViewGroup;

    @BindView(R.id.buy_txt)
    TextView mBuyTxt;

    @BindView(R.id.rent_txt)
    TextView mRentTxt;

    @BindView(R.id.sell_txt)
    TextView mSellTxt;

    @BindView(R.id.agent_txt)
    TextView mAgentTxt;

    @BindView(R.id.broker_txt)
    TextView mBrokerTxt;

    @BindView(R.id.skip_btn)
    Button mSkip;

    private UserDetailsEntity mUserDetailsRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_create_account_screem);

        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);
        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mAccountViewGroup);
        /*View Init*/
        initView();
    }

    private void initView() {

        mUserDetailsRes = PreferenceUtil.getUserDetailsRes(this);
        View selectedView = mBuyTxt;

        if (mUserDetailsRes.getType().equalsIgnoreCase(AppConstants.SELLER)) {
            selectedView = mSellTxt;
        } else if (mUserDetailsRes.getType().equalsIgnoreCase(AppConstants.RENTER)) {
            selectedView = mRentTxt;
        } else if (mUserDetailsRes.getType().equalsIgnoreCase(AppConstants.AGENT)) {
            selectedView = mAgentTxt;
        } else if (mUserDetailsRes.getType().equalsIgnoreCase(AppConstants.BROKER)) {
            selectedView = mBrokerTxt;
        }
        setBackGroundColor(selectedView);
    }


    /*View OnClick*/
    @OnClick({R.id.buy_txt, R.id.rent_txt, R.id.sell_txt, R.id.agent_txt, R.id.broker_txt, R.id.skip_btn})
    public void onClick(View v) {
        setBackGroundColor(v);
        switch (v.getId()) {
            case R.id.buy_txt:
                mUserDetailsRes.setType(AppConstants.BUYER);
                directToSignUpScreen();
                break;
            case R.id.rent_txt:
                mUserDetailsRes.setType(AppConstants.RENTER);
                directToSignUpScreen();
                break;
            case R.id.sell_txt:
                mUserDetailsRes.setType(AppConstants.SELLER);
                directToSignUpScreen();
                break;
            case R.id.agent_txt:
                mUserDetailsRes.setType(AppConstants.AGENT);
                directToSignUpScreen();
                break;
            case R.id.broker_txt:
                mUserDetailsRes.setType(AppConstants.BROKER);
                directToSignUpScreen();
                break;
            case R.id.skip_btn:
                nextScreen(HomeScreen.class, true);
                break;
        }
    }

    private void directToSignUpScreen() {
        PreferenceUtil.storeUserDetails(this, mUserDetailsRes);
        PreferenceUtil.storeBoolPreferenceValue(this, AppConstants.LOGIN_STATUS, false);
        nextScreen(RegisterScreen.class, true);
    }


    private void setBackGroundColor(View v) {
        /*Disable Color*/
        mBuyTxt.setBackgroundResource(R.color.transperent);
        mRentTxt.setBackgroundResource(R.color.transperent);
        mSellTxt.setBackgroundResource(R.color.transperent);
        mAgentTxt.setBackgroundResource(R.color.transperent);
        mBrokerTxt.setBackgroundResource(R.color.transperent);

        /*Selected Color*/
        v.setBackgroundResource(R.color.selection_color_ash);
    }
}
