package com.calix.calixgigamanage.ui.media;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.adapter.MyMediaAdapter;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.utils.NumberUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MyMedia extends BaseActivity {

    @BindView(R.id.my_media_parent_lay)
    ViewGroup mMyMediaViewGroup;

    @BindView(R.id.my_media_header_bg_lay)
    RelativeLayout mMyMediaHeaderBgLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.my_media_recycler_view)
    RecyclerView mMyMediaRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_my_media);
        initView();
    }


    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mMyMediaViewGroup);


        setHeaderView();
        setAdapter();
    }


    private void setAdapter() {
        MyMediaAdapter mediaAdapter = new MyMediaAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mMyMediaRecyclerView.setLayoutManager(linearLayoutManager);
        mMyMediaRecyclerView.setAdapter(mediaAdapter);

    }

    private void setHeaderView() {

        /*set header changes*/
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.my_media));

         /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mMyMediaHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(R.dimen.size45);
                    mMyMediaHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(MyMedia.this)));
                    mMyMediaHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(MyMedia.this), 0, 0);
                }

            });
        }

    }

    /*View onClick*/
    @OnClick({R.id.header_left_img_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;

        }
    }


    @Override
    public void onBackPressed() {
        backScreen();
    }
}