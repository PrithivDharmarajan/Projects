package com.smaat.renterblock.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smaat.renterblock.R;
import com.smaat.renterblock.adapters.AlertsAdapter;
import com.smaat.renterblock.entity.AlertsDeleteEntity;
import com.smaat.renterblock.entity.AlertsEntity;
import com.smaat.renterblock.entity.AlertsResultEntity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.InterfaceBtnCallback;
import com.smaat.renterblock.utils.InterfaceTwoBtnCallback;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlertsFragment extends BaseFragment {

    ArrayList<AlertsResultEntity> mAlertsResultEntityRes = new ArrayList<>();
    @BindView(R.id.alerts_recycler_view)
    RecyclerView mAlertsRecyclerView;
    AlertsAdapter mAlertsAdapter;
    private boolean isEdit = false;
    @BindView(R.id.edit_delete_lay)
    RelativeLayout mBottomEditDeleteLay;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_alerts_screens, container, false);
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
             /*Slide menu action*/
            ((HomeScreen) getActivity()).setDrawerAction(AppConstants.ALERT_BACK_FRAGMENT instanceof AlertsFragment);

            /*set header first  img*/
            ((HomeScreen) getActivity()).setHeaderLeftFirstImgLayVisible(1);
            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.edit_icon, 1);

            /*set header second img*/
            ((HomeScreen) getActivity()).setHeaderLeftSecondImgLayVisible(R.mipmap.app_icon, 2);
            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.drawable.add_icon, 1, "");

            /*set header third txt*/
            ((HomeScreen) getActivity()).setHeaderLeftThirdTxtLayVisible(getString(R.string.app_name), 0);
            ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.app_name), 0);

            /*set header txt*/
            ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.alerts), 1);
            ((HomeScreen) getActivity()).setHeaderEdt(getActivity().getString(R.string.alerts), 0);

            ((HomeScreen) getActivity()).mHeaderRightSecondImgLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DialogManager.getInstance().addAlertDialog(getActivity(), "");
                }
            });
            mBottomEditDeleteLay.setVisibility(View.GONE);
            isEdit = false;
            AppConstants.CALLED_FROM_ALERT_FRAG = false;

            initView();
        }

    }

    private void initView() {
        APIRequestHandler.getInstance().alertAPICall(AlertsFragment.this);
        if (getActivity() != null) {

            ((HomeScreen) getActivity()).mHeaderRightFirstImgLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getActivity() != null) {

                        if (mAlertsResultEntityRes.size() == 0) {
                            DialogManager.getInstance().showToast(getActivity(), getString(R.string.no_item_alert));
                            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.edit_icon, 2);
                            mBottomEditDeleteLay.setVisibility(View.GONE);
                        } else if (!isEdit) {
                            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.close_icon, 1);
                            isEdit = true;
                            setAdapter(true);
                            mBottomEditDeleteLay.setVisibility(View.VISIBLE);
                        } else {
                            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.edit_icon, 1);
                            isEdit = false;
                            setAdapter(false);
                            mBottomEditDeleteLay.setVisibility(View.GONE);
                        }

                    }
                }
            });
        }

    }

    /*Click Event*/
    @OnClick({R.id.edit_txt, R.id.delete_txt})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_txt:
                if (AppConstants.ALERTS_SELECTED_IDS != null && AppConstants.ALERTS_SELECTED_IDS.size() > 1 || AppConstants.ALERTS_SELECTED_IDS.size() == 0) {
                    DialogManager.getInstance().showAlertPopup(getContext(), getString(R.string.error_msg_select_one_item_edit), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {

                        }
                    });
                } else {
                    AlertsResultEntity editAlertEntity = AppConstants.ALERT_SELECTED_LIST.get(0);
                    DialogManager.getInstance().addAlertDialog(getActivity(), editAlertEntity.getLocation());

                }
                break;
            case R.id.delete_txt:
                if (AppConstants.ALERTS_SELECTED_IDS.isEmpty() && AppConstants.ALERTS_SELECTED_IDS.size() == 0 && AppConstants.ALERTS_SELECTED_IDS == null) {
                    DialogManager.getInstance().showAlertPopup(getContext(), getString(R.string.error_msg_select_one_item_delete), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {

                        }
                    });
                } else {
                    DialogManager.getInstance().showOptionPopup(getActivity(), getString(R.string.alert_delete), getString(R.string.yes), getString(R.string.no), new InterfaceTwoBtnCallback() {
                        @Override
                        public void onNegativeClick() {

                        }

                        @Override
                        public void onPositiveClick() {


                            /*Delete API call*/
                            String selectedIDs = TextUtils.join(",", AppConstants.ALERTS_SELECTED_IDS);
                            APIRequestHandler.getInstance().alertsDeleteAPICall(AlertsFragment.this, selectedIDs);

                        }
                    });


                }
                break;

        }
    }

    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);

        if (resObj instanceof AlertsEntity) {
            AlertsEntity alertsEntityResRes = (AlertsEntity) resObj;
            if (alertsEntityResRes.getError_code().equals(AppConstants.SUCCESS_CODE)) {

                if (mAlertsResultEntityRes == null || mAlertsResultEntityRes.size() == 0) {
                    mAlertsResultEntityRes = new ArrayList<>();

                }

                mAlertsResultEntityRes = alertsEntityResRes.getResult();
                setAdapter(false);


            }

        }
        if (resObj instanceof AlertsDeleteEntity) {
            AlertsDeleteEntity alertsDeleteEntity = (AlertsDeleteEntity) resObj;
            if (alertsDeleteEntity.getError_code().equals(AppConstants.SUCCESS_CODE)) {
                ((HomeScreen) getActivity()).addFragment(new AlertsFragment());
                DialogManager.getInstance().showAlertPopup(getContext(), alertsDeleteEntity.getMsg(), new InterfaceBtnCallback() {
                    @Override
                    public void onPositiveClick() {

                    }
                });
            }
        }
    }

    private void setAdapter(boolean isEdit) {

        if (getActivity() != null) {
            if (mAlertsResultEntityRes.size() == 0) {
//                DialogManager.getInstance().showToast(getActivity(), getString(R.string.no_item_alert));
                ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.edit_icon, 2);
                mBottomEditDeleteLay.setVisibility(View.GONE);
            }
            mAlertsAdapter = new AlertsAdapter(getActivity(), mAlertsResultEntityRes, isEdit);
            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
            mAlertsRecyclerView.setLayoutManager(mLinearLayoutManager);
            mAlertsRecyclerView.setAdapter(mAlertsAdapter);
        }
    }
}
