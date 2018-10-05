package com.bridgellc.bridge.ui.upload;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.ui.home.HomeScreenActivity;
import com.bridgellc.bridge.utils.AppConstants;

import java.util.Locale;


public class UploadPublishScreen extends BaseActivity {

    private RelativeLayout mHeaderLay;
    private Button mPublishBtn;
    public static boolean isTicket = false;
    private LinearLayout mAppLinkLay;
    private TextView mAppLinkTxt, mTitleTxt;
    public static String mheaderTxt = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_publish_item);
        initComponents();
    }

    private void initComponents() {
        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);

        mHeaderLay = (RelativeLayout) findViewById(R.id.header_lay);
        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mTitleTxt = (TextView) findViewById(R.id.title_tv);
        mAppLinkTxt = (TextView) findViewById(R.id.app_link_txt);

        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        mAppLinkLay = (LinearLayout) findViewById(R.id.app_link_lay);

        mPublishBtn = (Button) findViewById(R.id.footer_btn);

        mHeaderTxt.setText(mheaderTxt.toUpperCase(Locale.ENGLISH));
        mHeadeLeftBtnLay.setVisibility(View.INVISIBLE);
        mHeaderLay.setVisibility(View.VISIBLE);
        mPublishBtn.setText(getString(R.string.done));
        mTitleTxt.setText(getString(R.string.publish_success));


        mAppLinkTxt.setText(getString(R.string.app_link));
        Paint mBlue = new Paint(Color.BLUE);
        mAppLinkTxt.setPaintFlags(mBlue.UNDERLINE_TEXT_FLAG);


        mAppLinkLay.setVisibility(isTicket ? View.VISIBLE : View.GONE);
        mAppLinkTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri;
                Intent url;
                uri = Uri.parse(getString(R.string.http) + AppConstants.BASE_SCANNER_APP);
                url = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(url);
            }
        });

        mPublishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousScreen(HomeScreenActivity.class, true);
//                finishScreen();
            }
        });


    }

    @Override
    public void onBackPressed() {

    }
}
