package com.calix.calixgigamanage.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.adapter.NetworkUsageAdapter;
import com.calix.calixgigamanage.main.BaseFragment;
import com.calix.calixgigamanage.output.model.DeviceEntity;
import com.calix.calixgigamanage.output.model.DeviceListResponse;
import com.calix.calixgigamanage.services.APIRequestHandler;
import com.calix.calixgigamanage.utils.AppConstants;
import com.calix.calixgigamanage.utils.DialogManager;
import com.calix.calixgigamanage.utils.InterfaceBtnCallback;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DownloadFragment extends BaseFragment {


    @BindView(R.id.recycler_view)
    RecyclerView mDownloadRecyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_download_upload, container, false);
       /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(rootView);

        /*To focus on current fragment*/
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        initView();
        return rootView;
    }


    private void initView() {
         /*For error track purpose - log with class name*/
        AppConstants.TAG = this.getClass().getSimpleName();

        downloadListAPI();
    }


    /*API calls*/
    private void downloadListAPI() {
        APIRequestHandler.getInstance().deviceListAPICall("", this);
    }

    /*Set device list adapter*/
    private void setUploadListAdapter(ArrayList<DeviceEntity> deviceListResponse) {
        if (getActivity() != null) {
            mDownloadRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mDownloadRecyclerView.setNestedScrollingEnabled(false);
            mDownloadRecyclerView.setAdapter(new NetworkUsageAdapter(deviceListResponse, true, getContext()));
        }
    }

    /*API request success and failure*/
    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof DeviceListResponse) {
            DeviceListResponse deviceListResponse = (DeviceListResponse) resObj;
            setUploadListAdapter(deviceListResponse.getDevices());
        }
    }


    @Override
    public void onRequestFailure(Object resObj, Throwable t) {
        super.onRequestFailure(resObj,t);
        if (t instanceof IOException) {
            DialogManager.getInstance().showNetworkErrorPopup(getActivity(),
                    (t instanceof java.net.ConnectException ? getString(R.string.no_internet) : getString(R.string
                            .connect_time_out)), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {
                            downloadListAPI();
                        }
                    });
        }
    }
}
