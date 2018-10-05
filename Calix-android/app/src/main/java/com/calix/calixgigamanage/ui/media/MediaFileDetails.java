package com.calix.calixgigamanage.ui.media;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.adapter.MyMediaFileDetailsAdapter;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.utils.NumberUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MediaFileDetails extends BaseActivity {

    @BindView(R.id.media_details_parent_lay)
    ViewGroup mMediadetailsViewGroup;

    @BindView(R.id.media_details_header_bg_lay)
    RelativeLayout mMediadetailsHeaderBgLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.media_details_recycler_view)
    RecyclerView mMediadetailsRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_media_file_details);
        initView();
    }


    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mMediadetailsViewGroup);


        setHeaderView();
        setAdapter();
    }


    private void setAdapter() {
        MyMediaFileDetailsAdapter mediaAdapter = new MyMediaFileDetailsAdapter(this);
        mMediadetailsRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mMediadetailsRecyclerView.setAdapter(mediaAdapter);

    }

    private void setHeaderView() {

        /*set header changes*/
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText("Videos");

         /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mMediadetailsHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(R.dimen.size45);
                    mMediadetailsHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(MediaFileDetails.this)));
                    mMediadetailsHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(MediaFileDetails.this), 0, 0);
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