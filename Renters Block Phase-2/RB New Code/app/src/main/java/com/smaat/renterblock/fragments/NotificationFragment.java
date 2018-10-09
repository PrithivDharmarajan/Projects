package com.smaat.renterblock.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.smaat.renterblock.R;
import com.smaat.renterblock.adapters.NotificationAdapter;
import com.smaat.renterblock.entity.ChatInputEntity;
import com.smaat.renterblock.entity.NotificationEntity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.model.NotificationResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.PreferenceUtil;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationFragment extends BaseFragment {

    @BindView(R.id.notification_screen_recycler_view)
    RecyclerView mNotificationRecyclerView;

    ArrayList<NotificationEntity> mNotificationEntity = new ArrayList<>();

    NotificationAdapter mNotificationAdapter;

    private LinearLayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_notification_screen, container, false);
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);
        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(rootView);

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
            ((HomeScreen) getActivity()).setDrawerAction(AppConstants.NOTIFICATION_CURRENT_BACK_FRAGMENT instanceof NotificationFragment);

            /*set header first  img*/
            ((HomeScreen) getActivity()).setHeaderLeftFirstImgLayVisible(1);
            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.mipmap.app_icon, 2);

            /*set header second img*/
            ((HomeScreen) getActivity()).setHeaderLeftSecondImgLayVisible(R.mipmap.app_icon, 0);
            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.mipmap.app_icon, 0, "");

            /*set header third txt*/
            ((HomeScreen) getActivity()).setHeaderLeftThirdTxtLayVisible(getString(R.string.app_name), 0);
            ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.app_name), 0);

            /*set header txt*/
            ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.notifications), 1);
            ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.app_name), 0);


        /*set header text and header right img*/
            initView();
        }
    }

    private void initView() {

        APIRequestHandler.getInstance().notificationAPICall(this);

    }


    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof NotificationResponse) {
            NotificationResponse mNotificationRes = (NotificationResponse) resObj;
            if (mNotificationRes.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                if (mNotificationRes.getResult() != null) {
                    mNotificationEntity = mNotificationRes.getResult();
                    setAdapter(mNotificationEntity);
                }
            }
        }

        if (resObj instanceof CommonResponse) {
            CommonResponse mCommonRes = (CommonResponse) resObj;
            if (mCommonRes.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                initView();
            }

        }

    }

    public void setAdapter(ArrayList<NotificationEntity> mNotificationEntity) {


        if (mNotificationEntity.size() > 2) {
            Collections.reverse(mNotificationEntity);
        }
        mNotificationAdapter = new NotificationAdapter(getActivity(), mNotificationEntity, this);
        mLayoutManager = new LinearLayoutManager(getContext());
        mNotificationRecyclerView.setLayoutManager(mLayoutManager);
        mNotificationRecyclerView.setAdapter(mNotificationAdapter);
//        } else {
//            if (getActivity() != null) {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mNotificationAdapter.notifyDataSetChanged();
//                    }
//                });
//            }
//        }
    }

    public void notifyDeleteApi(NotificationEntity notificationEntity) {
         APIRequestHandler.getInstance().deleteNotification(notificationEntity.getNotification_id(), this);

        if (notificationEntity.getType_of_notification().equalsIgnoreCase("chat")
                || notificationEntity.getType_of_notification().equalsIgnoreCase("hotleadchat")) {

            AppConstants.CHAT_INPUT_ENTITY = new ChatInputEntity();
            AppConstants.CHAT_INPUT_ENTITY.setUser_id(PreferenceUtil.getUserID(getActivity()));
            AppConstants.CHAT_INPUT_ENTITY.setFriend_id("");
            AppConstants.CHAT_INPUT_ENTITY.setSchedule_id(notificationEntity.getType_id());
            ((HomeScreen) getActivity()).addFragment(new ChatFragment());


        } else if (notificationEntity.getType_of_notification().equalsIgnoreCase("friend")) {
            ((HomeScreen) getActivity()).addFragment(new FriendPendingRequestFragment());
        } else if (notificationEntity.getType_of_notification().equalsIgnoreCase("request")) {
            AppConstants.HOT_LEADS_BACK_FRAGMENT = new NotificationFragment();
            ((HomeScreen) getActivity()).addFragment(new HotLeadsPropertyFragment());
        } else if (notificationEntity.getType_of_notification().equalsIgnoreCase("Alert")) {
            AppConstants.ALERT_BACK_FRAGMENT = new NotificationFragment();
            ((HomeScreen) getActivity()).addFragment(new AlertsFragment());
        } else if (notificationEntity.getType_of_notification().equalsIgnoreCase("request to agent")) {
            AppConstants.PROFILE_ID = notificationEntity.getType_id();
            AppConstants.PROFILE_CURRENT_BACK_FRAGMENT = new NotificationFragment();
            ((HomeScreen) getActivity()).addFragment(new ProfileFragment());
        } else if (notificationEntity.getType_of_notification().equalsIgnoreCase("schedule")) {
            AppConstants.SCHEDULE_BACK_FRAGMENT = new NotificationFragment();
            ((HomeScreen) getActivity()).addFragment(new SchedulingFragment());
        } else if (notificationEntity.getType_of_notification().equalsIgnoreCase("alertproperty")
                || notificationEntity.getType_of_notification().equalsIgnoreCase("savedproperty")) {

            AppConstants.DETAIL_PROPERTY_ID = notificationEntity.getType_id();
            ((HomeScreen) getActivity()).addFragment(new PropertyDetailsFragment());


        }

    }

}
