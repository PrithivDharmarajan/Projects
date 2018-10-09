package com.smaat.spark.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.smaat.spark.R;
import com.smaat.spark.adapter.NotificationAdapter;
import com.smaat.spark.entity.inputEntity.ChatConnDisInputEntity;
import com.smaat.spark.entity.inputEntity.NotificationInputEntity;
import com.smaat.spark.entity.outputEntity.UserDetailsEntity;
import com.smaat.spark.main.BaseFragment;
import com.smaat.spark.model.CommonModel;
import com.smaat.spark.model.CommonResponse;
import com.smaat.spark.model.NotificationResponse;
import com.smaat.spark.services.APIRequestHandler;
import com.smaat.spark.ui.HomeScreen;
import com.smaat.spark.utils.AppConstants;
import com.smaat.spark.utils.DialogManager;
import com.smaat.spark.utils.GlobalMethods;
import com.smaat.spark.utils.InterfaceBtnCallback;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NotificationFragment extends BaseFragment implements InterfaceBtnCallback {

    @BindView(R.id.notification_list)
    RecyclerView mNotificationList;


    private ArrayList<UserDetailsEntity> mNotificationRes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_notification_screen, container, false);
        ButterKnife.bind(this, rootView);
        setupUI(rootView);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        ((HomeScreen) getActivity()).setHeaderLeftClick(false);
        ((HomeScreen) getActivity()).setHeaderText(getString(R.string.notifications));
        ((HomeScreen) getActivity()).setHeadRigImgVisible(false, R.mipmap.app_icon);

        callNotificationListAPI();

    }

    private void callNotificationListAPI() {
        ChatConnDisInputEntity notificationInputEntityRes = new ChatConnDisInputEntity(AppConstants.API_NOTIFICATION, AppConstants.PARAMS_USER_ID, GlobalMethods.getUserID(getActivity()));
        APIRequestHandler.getInstance().callNotificationAPI(notificationInputEntityRes, this);
    }

    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);

        if (resObj instanceof NotificationResponse) {
            NotificationResponse addFriendRes = (NotificationResponse) resObj;
            if (addFriendRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                if (addFriendRes.getResult().size() > 0) {
                    mNotificationRes=new ArrayList<>();
                    mNotificationRes = addFriendRes.getResult();
                    String notificationId = "";
                    for (int i = 0; i < mNotificationRes.size(); i++) {
                        notificationId += (i == 0 ? mNotificationRes.get(i).getNotification_id() :
                                "," + mNotificationRes.get(i).getNotification_id());
                    }
                    NotificationInputEntity notificationReadInput = new NotificationInputEntity(AppConstants.API_READ_NOTIFICATION, AppConstants.PARAMS_READ_NOTIFICATION, GlobalMethods.getUserID(getActivity()), notificationId);
                    APIRequestHandler.getInstance().callReadNotificationAPI(notificationReadInput, this);
                }else{
                    mNotificationRes=new ArrayList<>();
                    setNotificationAdapter(mNotificationRes);
                }
            } else {
                DialogManager.getInstance().showAlertPopup(getActivity(), addFriendRes.getMessage());
            }

        } else if (resObj instanceof CommonResponse) {
            CommonResponse addFriendRes = (CommonResponse) resObj;
            if (addFriendRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                DialogManager.getInstance().showAlertPopup(getActivity(), addFriendRes.getMessage());
            } else {
                DialogManager.getInstance().showAlertPopup(getActivity(), addFriendRes.getMessage());
            }

            callNotificationListAPI();

        } else if (resObj instanceof CommonModel) {
            CommonModel readNotificationRes = (CommonModel) resObj;
            if (readNotificationRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                setNotificationAdapter(mNotificationRes);
            } else {
                DialogManager.getInstance().showAlertPopup(getActivity(), readNotificationRes.getMessage());
            }

        }
    }

    @Override
    public void onRequestFailure(Throwable t) {
        super.onRequestFailure(t);
        callNotificationListAPI();
    }

    private void setNotificationAdapter(final ArrayList<UserDetailsEntity> notificationList) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                NotificationAdapter adapter = new NotificationAdapter(getActivity(), NotificationFragment.this, notificationList);
                mNotificationList.setLayoutManager(new LinearLayoutManager(getActivity()));
                mNotificationList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onYesClick() {

    }
}
