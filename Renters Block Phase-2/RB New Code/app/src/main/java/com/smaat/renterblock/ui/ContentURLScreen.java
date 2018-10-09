package com.smaat.renterblock.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smaat.renterblock.R;
import com.smaat.renterblock.main.BaseActivity;
import com.smaat.renterblock.model.ContentURLResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This class implements  UI for splash screen
 * Created by Prithiv on 8/19/2017.
 */

public class ContentURLScreen extends BaseActivity {


    /*Variable initialization using bind view*/

    @BindView(R.id.content_parent_lay)
    ViewGroup mContentViewGroup;

    @BindView(R.id.header_left_first_img)
    ImageView mHeaderLeftFirstImg;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.content_txt)
    TextView mContentTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ui_content_screen);

        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);
        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mContentViewGroup);
        /*View Init*/
        initView();
    }


    /*View initialization*/
    private void initView() {
        mHeaderLeftFirstImg.setImageResource(R.drawable.back_icon);
        mHeaderTxt.setText(AppConstants.CONTENT_HEADER);
        APIRequestHandler.getInstance().contentURLDetailsAPICall(AppConstants.CONTENT_URL, this);

    }


    /*View OnClick*/
    @OnClick({R.id.header_left_first_img_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_first_img_lay:
                onBackPressed();
                break;
        }
    }

    /*API request success*/
    public void onRequestSuccess(Object mResponseObj) {
        if (mResponseObj instanceof ContentURLResponse) {
            ContentURLResponse mSettingContentEntity = (ContentURLResponse) mResponseObj;
            if (mSettingContentEntity.getError_code().equals(AppConstants.SUCCESS_CODE))
                mContentTxt.setText(mSettingContentEntity.getResult());
            else
                DialogManager.getInstance().showAlertPopup(this, mSettingContentEntity.getMsg(), this);
        }
    }

    @Override
    public void onBackPressed() {
        finishScreen();
    }
}
