package com.bridgellc.bridgeqr.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridgeqr.R;
import com.bridgellc.bridgeqr.main.BaseActivity;
import com.bridgellc.bridgeqr.utils.AppConstants;

import java.util.Locale;


public class QRSuccScreen extends BaseActivity implements View.OnClickListener {

    TextView mStatusTxt;
    ImageView mStatusImg;
    Button mCloseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_success_screen);

        initComponents();

    }

    private void initComponents() {
        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);

        //Header and Footer Init
        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        mHeadeRightBtnLay = (RelativeLayout) findViewById(R.id.header_right_btn_lay);
        mHeadeLeftImg = (ImageView) findViewById(R.id.header_left_img);
        mHeadeRightImg = (ImageView) findViewById(R.id.header_right_img);
        mCloseBtn = (Button) findViewById(R.id.footer_btn);

        //Screen Init
        mStatusTxt = (TextView) findViewById(R.id.status_txt);
        mStatusImg = (ImageView) findViewById(R.id.status_img);

        //SetData
        mHeadeLeftBtnLay.setVisibility(View.VISIBLE);
        mHeadeRightBtnLay.setVisibility(View.INVISIBLE);
        mHeadeLeftImg.setImageResource(R.drawable.back_img);
        mHeaderTxt.setText(getString(R.string.qr_code).toUpperCase(Locale.ENGLISH));
        mStatusTxt.setText(AppConstants.QR_STATUS_TXT);
        mCloseBtn.setText(getString(R.string.close));

        if (AppConstants.QR_STATUS.equalsIgnoreCase(AppConstants.SUCCESS_CODE))
            mStatusImg.setImageResource(R.drawable.qr_scan_succ);
        else
            mStatusImg.setImageResource(R.drawable.qr_scan_unsucc);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
            case R.id.footer_btn:
                onBackPressed();
                break;
            case R.id.status_img:
                if (AppConstants.QR_STATUS.equalsIgnoreCase(AppConstants.SUCCESS_CODE))
                    nextScreen(HomeScreen.class, true);
                else
                    previousScreen(HomeScreen.class, true);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        previousScreen(HomeScreen.class, true);
    }
}
