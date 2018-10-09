package com.smaat.renterblock.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.smaat.renterblock.R;
import com.smaat.renterblock.adapters.HotLeadsPropertyListAdapter;
import com.smaat.renterblock.entity.HotLeadsEntity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.HotLeadsResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 10/9/2017.
 */

public class HotLeadsPropertyFragment extends BaseFragment {
    @BindView(R.id.hot_lead_recycler_view)
    RecyclerView mHotLeadRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_hotleads, container, false);
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);
        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(rootView);
        /*For focus current fragment*/

        /*For focus current fragment*/
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    default:
                        v.performClick();
                }
                return true;
            }
        });
        return rootView;
    }

    /*Fragment manual onResume*/
    @Override
    public void onFragmentResume() {
        super.onFragmentResume();

          /* If the value of visibleInt is zero,  the view will set gone. Or if the value of visibleInt is one,  the view will set visible. Or else, the view will set gone*/
        if (getActivity() != null) {

            AppConstants.TAG = this.getClass().getSimpleName();

             /*Slide menu action*/
            //  ((HomeScreen) getActivity()).setDrawerAction(true);
            ((HomeScreen) getActivity()).setDrawerAction(AppConstants.HOT_LEADS_BACK_FRAGMENT instanceof HotLeadsPropertyFragment);


            /*set header first  img*/
            ((HomeScreen) getActivity()).setHeaderLeftFirstImgLayVisible(1);
            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.mipmap.app_icon, 2);

            /*set header second img*/
            ((HomeScreen) getActivity()).setHeaderLeftSecondImgLayVisible(R.mipmap.app_icon, 0);
            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.drawable.filter_icon, 0, "");

            /*set header third txt*/
            ((HomeScreen) getActivity()).setHeaderLeftThirdTxtLayVisible(getString(R.string.app_name), 0);
            ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.app_name), 0);

            /*set header txt*/
            ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.hot_leads), 1);
            ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.app_name), 0);


            initView();
        }
    }

    private void initView() {
        APIRequestHandler.getInstance().hotLeadsAPICall(HotLeadsPropertyFragment.this);


    }

    @Override
    public void onRequestSuccess(Object mResponseObj) {
        super.onRequestSuccess(mResponseObj);
        if (getActivity() != null) {
            if (mResponseObj instanceof HotLeadsResponse) {
                HotLeadsResponse hotLeadsResponse = (HotLeadsResponse) mResponseObj;
                if (hotLeadsResponse.getError_code().equals(AppConstants.SUCCESS_CODE)) {
                    ArrayList<HotLeadsEntity> hotLeadsList = hotLeadsResponse.getResult();
                    if (hotLeadsList.size() > 0) {
                    /*set Adapter*/
                        HotLeadsPropertyListAdapter hotLeadsPropertyListAdapter = new HotLeadsPropertyListAdapter(getActivity(), hotLeadsList);
                        mHotLeadRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        mHotLeadRecyclerView.setAdapter(hotLeadsPropertyListAdapter);
                        mHotLeadRecyclerView.setNestedScrollingEnabled(false);

                    } else {
                        // DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.hot_leads_alert), this);
                    }
                }
            }
        }

    }
}
