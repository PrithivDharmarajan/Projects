package com.smaat.spark.fragment;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import com.smaat.spark.ui.WebScreen;
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
    private Dialog mPictureDialog;

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
        ((HomeScreen) getActivity()).setHeaderLeftClick(false);
        ((HomeScreen) getActivity()).setHeaderText(getString(R.string.settings));
        ((HomeScreen) getActivity()).setHeadRigImgVisible(false, R.mipmap.app_icon);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getActivity().getCurrentFocus() != null && getActivity().getCurrentFocus().getWindowToken() != null) {
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }, 300);
    }

    private void initView() {

        mUserDetailsRes = GlobalMethods.getUserDetailsRes(getActivity());
        setSettingsData();
    }

    private void setSettingsData() {

        mAllowAnonymousImg.setTag(mUserDetailsRes.getAnonymous().equals(AppConstants.SUCCESS_CODE) ? 1 : 0);
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


    @OnClick({R.id.anonymous_img, R.id.push_notify_img, R.id.hide_loc_img, R.id.support_txt, R.id.invite_friends_txt,
            R.id.privacy_policy_txt, R.id.terms_service_txt, R.id.logout_txt, R.id.delete_acc_txt})
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
                AppConstants.SETTINGS_WEB_TITLE = getResources().getString(R.string.support);
                nextScreen(WebScreen.class, false);
                break;
            case R.id.invite_friends_txt:
//                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//                sharingIntent.setType("text/plain");
//                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name) + " - " + getString(R.string.invite_friend));
//                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string
//                        .share_txt) + "\n\n\t" + getString(R.string.username) + " " + mUserDetailsRes.getUsername()
//                        + "\n\n\t" + getString(R.string.android_app) + " " + AppConstants.ANDROID_SPARK_APP
//                        + "\n\n\t" + getString(R.string.ios_app) + " " + AppConstants.IOS_SPARK_APP);
//                startActivity(Intent.createChooser(sharingIntent, getString(R.string.invite_friend)));

                showInviteFriendPopup();
                break;
            case R.id.privacy_policy_txt:
                AppConstants.SETTINGS_WEB_TITLE = getResources().getString(R.string.privacy_policy);
                nextScreen(WebScreen.class, false);
                break;
            case R.id.terms_service_txt:
                AppConstants.SETTINGS_WEB_TITLE = getResources().getString(R.string.terms_service);
                nextScreen(WebScreen.class, false);
                break;
            case R.id.logout_txt:
                DialogManager.getInstance().showOptionPopup(getActivity(), getString(R.string.logout_msg), getString(R.string.yes), getString(R.string.no), new InterfaceTwoBtnCallback() {
                    @Override
                    public void onYesClick() {
                        GlobalMethods.storeUserDetails(getActivity(), null);
                        previousScreen(LoginScreen.class, true);
                    }

                    public void onNoClick() {
                    }
                });
                break;
            case R.id.delete_acc_txt:
                DialogManager.getInstance().showAccDeletePopup(getActivity(), new InterfaceTwoBtnCallback() {
                    @Override
                    public void onYesClick() {


                        DialogManager.getInstance().showOptionPopup(getActivity(), getString(R.string.delete_acc_msg), getString(R.string.yes), getString(R.string.no), new InterfaceTwoBtnCallback() {
                            @Override
                            public void onYesClick() {
                                ChatConnDisInputEntity chatDisConInputEntityRes = new ChatConnDisInputEntity(AppConstants.API_DELETE_ACC, AppConstants.PARAMS_USER_ID, mUserDetailsRes.getUser_id());
                                APIRequestHandler.getInstance().callDisConnectAPI(chatDisConInputEntityRes, SettingsFragment.this);

                            }

                            public void onNoClick() {
                            }
                        });

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
        if (!AppConstants.SETTINGS_ANONYMOUS.equals(mUserDetailsRes.getAnonymous())) {
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

    public Dialog showInviteFriendPopup() {

        if (mPictureDialog != null && mPictureDialog.isShowing()) {
            try {
                mPictureDialog.dismiss();
            } catch (Exception e) {
                Log.e(AppConstants.DIALOG_TAG, e.getMessage());
            }
        }
        mPictureDialog = new Dialog(getActivity());
        mPictureDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mPictureDialog.setContentView(R.layout.popup_invite_friend);
        if (mPictureDialog.getWindow() != null) {
            mPictureDialog.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            mPictureDialog.getWindow().setGravity(Gravity.CENTER);
            mPictureDialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));
        }
        WindowManager.LayoutParams layoutParams = mPictureDialog.getWindow().getAttributes();

        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;

        Button shareViaMailBtn,shareViaMsgBtn,  copyLinkBtn, cancelBtn;

        shareViaMailBtn = (Button) mPictureDialog.findViewById(R.id.share_via_mail_btn);
        shareViaMsgBtn = (Button) mPictureDialog.findViewById(R.id.share_via_msg_btn);
        copyLinkBtn = (Button) mPictureDialog.findViewById(R.id.copy_link_btn);
        cancelBtn = (Button) mPictureDialog.findViewById(R.id.cancel_btn);

        final String shareMsgStr = getString(R.string
                .share_txt) + "\n\n\t" + getResources().getString(R.string.username) + " " + mUserDetailsRes.getUsername()
                + "\n\n\t" + getResources().getString(R.string.android_app) + " " + AppConstants.ANDROID_SPARK_APP
                + "\n\n\t" + getResources().getString(R.string.ios_app) + " " + AppConstants.IOS_SPARK_APP;


        shareViaMailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPictureDialog.dismiss();
                final Intent emailInt = new Intent(Intent.ACTION_SENDTO);
                emailInt.setData(Uri.parse("mailto:" + ""));
                emailInt.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + " - " + getString(R.string.invite_friend));
                emailInt.putExtra(Intent.EXTRA_TEXT, shareMsgStr);
                startActivity(emailInt);
            }
        });
        shareViaMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPictureDialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + ""));
                intent.putExtra("sms_body", shareMsgStr);
                startActivity(intent);
            }
        });
        copyLinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPictureDialog.dismiss();
                ClipboardManager cManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData cData = ClipData.newPlainText("text", shareMsgStr);
                cManager.setPrimaryClip(cData);
                DialogManager.showToast(getActivity(), getResources().getString(R.string.copied));
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPictureDialog.dismiss();
            }
        });

        if (mPictureDialog != null) {
            try {
                mPictureDialog.show();
            } catch (Exception e) {
                Log.e(AppConstants.DIALOG_TAG, e.getMessage());
            }
        }
        return mPictureDialog;
    }
}
