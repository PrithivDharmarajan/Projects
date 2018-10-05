package com.calix.calixgigamanage.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.adapter.ParentalControlFilterAdatper;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.ui.parentalcontrol.ParentalControlUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by AbdulRahim(SmaatApps) on 1/10/2018.
 */

public class ParentalControlFilterFragment extends Fragment {


    @BindView(R.id.pc_filter_platform_recycler_view)
    RecyclerView mFilterPlatformRecyclerView;

    @BindView(R.id.pc_filter_categry_recycler_view)
    RecyclerView mFilterCategryRecyclerView;

    @BindView(R.id.pc_filter_privacy_setting_recycler_view)
    RecyclerView mFilterPrivacyRecyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_parental_control_filter, container, false);
       /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);
        /*Keypad to be hidden when a touch made outside the edit text*/
        //  setupUI(rootView);
        /*To focus on current fragment*/
//        rootView.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });

        initView();
        return rootView;
    }

    private void initView() {
        setAdapter();
    }

    private void setAdapter() {

        ParentalControlFilterAdatper parentalControlPlatfromAdapter = new ParentalControlFilterAdatper(getContext());
        LinearLayoutManager mPlatfromLayoutManager = new LinearLayoutManager(getContext());
        mFilterPlatformRecyclerView.setLayoutManager(mPlatfromLayoutManager);
        mFilterPlatformRecyclerView.setNestedScrollingEnabled(false);
        mFilterPlatformRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mFilterPlatformRecyclerView.setAdapter(parentalControlPlatfromAdapter);

        ParentalControlFilterAdatper parentalControlCategryAdapter = new ParentalControlFilterAdatper(getContext());
        LinearLayoutManager mCategryLayoutManager = new LinearLayoutManager(getContext());

        mFilterCategryRecyclerView.setLayoutManager(mCategryLayoutManager);
        mFilterCategryRecyclerView.setNestedScrollingEnabled(false);
        mFilterCategryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mFilterCategryRecyclerView.setAdapter(parentalControlCategryAdapter);

        ParentalControlFilterAdatper parentalControlPrivacyAdapter = new ParentalControlFilterAdatper(getContext());
        LinearLayoutManager mPrivacyLayoutManager = new LinearLayoutManager(getContext());

        mFilterPrivacyRecyclerView.setLayoutManager(mPrivacyLayoutManager);
        mFilterPrivacyRecyclerView.setNestedScrollingEnabled(false);
        mFilterPrivacyRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mFilterPrivacyRecyclerView.setAdapter(parentalControlPrivacyAdapter);

    }

    /*View onClick*/
    @OnClick({R.id.filter_done_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.filter_done_btn:
                if (getActivity() != null) {
                    ((BaseActivity) getActivity()).previousScreen(ParentalControlUser.class);
                }
                break;

        }
    }
}

