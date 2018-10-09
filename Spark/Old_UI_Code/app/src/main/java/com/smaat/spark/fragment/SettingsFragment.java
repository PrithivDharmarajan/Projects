package com.smaat.spark.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smaat.spark.R;
import com.smaat.spark.entity.inputEntity.ChatConnDisInputEntity;
import com.smaat.spark.entity.outputEntity.UserDetailsEntity;
import com.smaat.spark.main.BaseFragment;
import com.smaat.spark.model.CommonResponse;
import com.smaat.spark.services.APIRequestHandler;
import com.smaat.spark.ui.HomeScreen;
import com.smaat.spark.ui.LoginScreen;
import com.smaat.spark.utils.AppConstants;
import com.smaat.spark.utils.DialogManager;
import com.smaat.spark.utils.GlobalMethods;
import com.smaat.spark.utils.InterfaceBtnCallback;
import com.smaat.spark.utils.InterfaceTwoBtnCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SettingsFragment extends BaseFragment implements InterfaceBtnCallback {


    @BindView(R.id.anonymous_img)
    ImageView mAllowAnonymousImg;

    @BindView(R.id.push_notify_img)
    ImageView mPushNotifyImg;

    @BindView(R.id.hide_loc_img)
    ImageView mHideLocImg;

    @BindView(R.id.version_num_txt)
    TextView mVersionNumTxt;

    private UserDetailsEntity mUserDetailsRes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_settings_screen, container, false);
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }


    @Override
    public void onResume() {
        super.onResume();
        ((HomeScreen) getActivity()).setDrawerAction(false);
        ((HomeScreen) getActivity()).setHeaderText(getString(R.string.settings));
        ((HomeScreen) getActivity()).setHeadRigImgVisible(false, R.mipmap.app_icon);
    }

    private void initView() {

        mUserDetailsRes = GlobalMethods.getUserDetailsRes(getActivity());
        setSettingsData();
    }

    private void setSettingsData() {

        mAllowAnonymousImg.setTag(mUserDetailsRes.getAnonyomous().equals(AppConstants.SUCCESS_CODE) ? 1 : 0);
        mPushNotifyImg.setTag(mUserDetailsRes.getPush_notifications().equals(AppConstants.SUCCESS_CODE) ? 1 : 0);
        mHideLocImg.setTag(mUserDetailsRes.getHide_location().equals(AppConstants.SUCCESS_CODE) ? 1 : 0);

        mAllowAnonymousImg.setImageResource(mAllowAnonymousImg.getTag().equals(1) ? R.drawable.distance_enable_img : R.drawable.distance_disable_img);
        mPushNotifyImg.setImageResource(mPushNotifyImg.getTag().equals(1) ? R.drawable.distance_enable_img : R.drawable.distance_disable_img);
        mHideLocImg.setImageResource(mHideLocImg.getTag().equals(1) ? R.drawable.distance_enable_img : R.drawable.distance_disable_img);

        AppConstants.SETTINGS_ANONYMOUS = mAllowAnonymousImg.getTag().equals(1) ? AppConstants.SUCCESS_CODE : AppConstants.FAILURE_CODE;
        AppConstants.SETTINGS_PUSH = mPushNotifyImg.getTag().equals(1) ? AppConstants.SUCCESS_CODE : AppConstants.FAILURE_CODE;
        AppConstants.SETTINGS_HIDE_LOC = mHideLocImg.getTag().equals(1) ? AppConstants.SUCCESS_CODE : AppConstants.FAILURE_CODE;

        try {
            mVersionNumTxt.setText(getString(R.string.version) + " " + getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(AppConstants.TAG, e.toString());
        }

    }


    @OnClick({R.id.anonymous_img, R.id.push_notify_img, R.id.hide_loc_img, R.id.support_txt, R.id.share_spark_txt,
            R.id.privacy_policy_txt, R.id.terms_service_txt, R.id.licenses_txt, R.id.delete_acc_txt})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.anonymous_img:
                mAllowAnonymousImg.setTag(mAllowAnonymousImg.getTag().equals(1) ? 0 : 1);
                mAllowAnonymousImg.setImageResource(mAllowAnonymousImg.getTag().equals(1) ? R.drawable.distance_enable_img : R.drawable.distance_disable_img);
                AppConstants.SETTINGS_ANONYMOUS = mAllowAnonymousImg.getTag().equals(1) ? AppConstants.SUCCESS_CODE : AppConstants.FAILURE_CODE;
                checkStatus();
                break;
            case R.id.push_notify_img:
                mPushNotifyImg.setTag(mPushNotifyImg.getTag().equals(1) ? 0 : 1);
                mPushNotifyImg.setImageResource(mPushNotifyImg.getTag().equals(1) ? R.drawable.distance_enable_img : R.drawable.distance_disable_img);
                AppConstants.SETTINGS_PUSH = mPushNotifyImg.getTag().equals(1) ? AppConstants.SUCCESS_CODE : AppConstants.FAILURE_CODE;
                checkStatus();
                break;
            case R.id.hide_loc_img:
                mHideLocImg.setTag(mHideLocImg.getTag().equals(1) ? 0 : 1);
                mHideLocImg.setImageResource(mHideLocImg.getTag().equals(1) ? R.drawable.distance_enable_img : R.drawable.distance_disable_img);
                AppConstants.SETTINGS_HIDE_LOC = mHideLocImg.getTag().equals(1) ? AppConstants.SUCCESS_CODE : AppConstants.FAILURE_CODE;
                checkStatus();
                break;
            case R.id.support_txt:
                break;
            case R.id.share_spark_txt:
                UserDetailsEntity userDetailsRes = GlobalMethods.getUserDetailsRes(getActivity());
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name) + " - " + getString(R.string.invite_friend));
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string
                        .share_txt) + "\n\n\t" + getString(R.string.username) + " " + userDetailsRes.getUsername()
                        + "\n\n\t" + getString(R.string.android_app) + " " + AppConstants.ANDROID_SPARK_APP
                        + "\n\n\t" + getString(R.string.ios_app) + " " + AppConstants.IOS_SPARK_APP);
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.invite_friend)));
                break;
            case R.id.privacy_policy_txt:
                break;
            case R.id.terms_service_txt:
                break;
            case R.id.licenses_txt:
                break;
            case R.id.delete_acc_txt:
                DialogManager.getInstance().showAccDeletePopup(getActivity(), new InterfaceTwoBtnCallback() {
                    @Override
                    public void onYesClick() {
                        ChatConnDisInputEntity chatDisConInputEntityRes = new ChatConnDisInputEntity(AppConstants.API_DELETE_ACC, AppConstants.PARAMS_USER_ID, mUserDetailsRes.getUser_id());
                        APIRequestHandler.getInstance().callDisConnectAPI(chatDisConInputEntityRes, SettingsFragment.this);
                    }

                    @Override
                    public void onNoClick() {

                    }
                });
                break;
        }
    }

    private void checkStatus() {
        boolean isChanged = false;
        if (!AppConstants.SETTINGS_ANONYMOUS.equals(mUserDetailsRes.getAnonyomous())) {
            isChanged = true;
        } else if (!AppConstants.SETTINGS_PUSH.equals(mUserDetailsRes.getPush_notifications())) {
            isChanged = true;
        } else if (!AppConstants.SETTINGS_HIDE_LOC.equals(mUserDetailsRes.getHide_location())) {
            isChanged = true;
        }
        AppConstants.SETTINGS_BACK = isChanged ? AppConstants.SUCCESS_CODE : AppConstants.FAILURE_CODE;
    }


    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof CommonResponse) {
            CommonResponse deleteAccRes = (CommonResponse) resObj;
            if (deleteAccRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                GlobalMethods.storeUserDetails(getActivity(), null);
                previousScreen(LoginScreen.class, true);
            } else {
                DialogManager.getInstance().showAlertPopup(getActivity(), deleteAccRes.getMessage());
            }
        }
    }

    @Override
    public void onRequestFailure(Throwable t) {
        super.onRequestFailure(t);
        mUserDetailsRes = GlobalMethods.getUserDetailsRes(getActivity());
        setSettingsData();
    }

    @Override
    public void onYesClick() {

    }
}
