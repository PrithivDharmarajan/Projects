package com.fautus.fautusapp.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fautus.fautusapp.BuildConfig;
import com.fautus.fautusapp.R;
import com.fautus.fautusapp.main.BaseFragment;
import com.fautus.fautusapp.ui.HomeScreen;
import com.fautus.fautusapp.ui.SignUpScreen;
import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.DialogManager;
import com.fautus.fautusapp.utils.InterfaceBtnCallback;
import com.fautus.fautusapp.utils.InterfaceTwoBtnCallback;
import com.fautus.fautusapp.utils.NetworkUtil;
import com.fautus.fautusapp.utils.ParseAPIConstants;
import com.fautus.fautusapp.utils.PreferenceUtil;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SettingsFragment extends BaseFragment implements InterfaceBtnCallback {

    @BindView(R.id.authenticity_strip_lay)
    RelativeLayout mAuthenticityStripLay;

    @BindView(R.id.strip_off_img)
    ImageView mStripOffImg;

    @BindView(R.id.strip_on_img)
    ImageView mStripOnImg;

    @BindView(R.id.version_num_txt)
    TextView mVersionNumTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_settings_screen, container, false);
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);
        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(rootView);
        /*For focus current fragment*/
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        return rootView;
    }


    /*Fragment manual onResume*/
    @Override
    public void onFragmentResume() {
        super.onFragmentResume();
        /*set header text and header right img*/
        ((HomeScreen) getActivity()).setDrawerAction(true);
        ((HomeScreen) getActivity()).setHeaderText(getString(R.string.settings));
        ((HomeScreen) getActivity()).setHeadRightImgVisible(false, R.mipmap.app_icon);
        initView();

    }


    /*Set strip vision image view*/
    private void initView() {

        boolean isStripOnBool = true;

        if (PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.USER_IS_CONSUMER)) {
            isStripOnBool = PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.SETTINGS_STRIP_ON);
        } else {
            mAuthenticityStripLay.setAlpha(0.3f);
        }

        mAuthenticityStripLay.setBackground(isStripOnBool ? ContextCompat.getDrawable(getActivity(), R.drawable.rounded_green_bg) : ContextCompat.getDrawable(getActivity(), R.drawable.rounded_white_bg));
        mStripOnImg.setVisibility(isStripOnBool ? View.VISIBLE : View.GONE);
        mStripOffImg.setVisibility(isStripOnBool ? View.GONE : View.VISIBLE);
        mVersionNumTxt.setText(getString(R.string.version) + " " + BuildConfig.VERSION_NAME);
    }

    /*View OnClick*/
    @OnClick({R.id.authenticity_strip_lay, R.id.logout_txt})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.authenticity_strip_lay:
                /*Set authenticity strip vision*/
                if (PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.USER_IS_CONSUMER)) {
                    PreferenceUtil.storeBoolPreferenceValue(getActivity(), AppConstants.SETTINGS_STRIP_ON, !PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.SETTINGS_STRIP_ON));
                    initView();
                }
                break;
            case R.id.logout_txt:
                /*Logout popup*/
                DialogManager.getInstance().showOptionAlertPopup(getActivity(), getString(R.string.logout), getString(R.string.logout_msg), getString(R.string.no), getString(R.string.yes), new InterfaceTwoBtnCallback() {
                    @Override
                    public void onYesClick() {
                         /*Check internet connection*/
                        if (NetworkUtil.isNetworkAvailable(getActivity())) {
                            DialogManager.getInstance().showProgress(getActivity());
                            /*Logout current parse User*/
                            if (getActivity() != null) {
                                /*Reset device Id to Parse DB*/
                                ParseInstallation parseInstallation = ParseInstallation.getCurrentInstallation();
//                                if (parseInstallation != null) {
//                                    parseInstallation.put(ParseAPIConstants.GCMSenderId, "");
//                                    parseInstallation.saveInBackground();
//                                }

                                if (PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.USER_IS_CONSUMER)) {
                                    ParseUser.logOut();
                                    nextScreen();
                                } else {
                                    final ParseQuery<ParseObject> photographerQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Photographer);

                                    photographerQuery.whereEqualTo(ParseAPIConstants.user, ParseUser.getCurrentUser());
                                    photographerQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                                        @Override
                                        public void done(ParseObject object, ParseException e) {
                                            if (getActivity() != null) {
                                                if (e == null && object != null) {
                                                    ParseUser.logOut();
                                                    object.put(ParseAPIConstants.isAvailable, false);
                                                    object.saveInBackground(new SaveCallback() {
                                                        @Override
                                                        public void done(ParseException e) {
                                                            nextScreen();
                                                        }
                                                    });
                                                } else {
                                                    nextScreen();
                                                }
                                            }
                                        }
                                    });
                                }
                            }

                        } else {
                            /*Alert message will be appeared if there is no internet connection*/
                            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), getString(R.string.no_internet), SettingsFragment.this);
                        }
                    }

                    @Override
                    public void onNoClick() {
                        /*Dialog box second btn click action*/
                    }
                });
                break;
        }
    }


    private void nextScreen() {

        AppConstants.WELCOME_SCREEN_TYPE = AppConstants.FAILURE_CODE;
        PreferenceUtil.storeBoolPreferenceValue(getActivity(), AppConstants.LOGIN_STATUS, false);
        PreferenceUtil.storeBoolPreferenceValue(getActivity(), AppConstants.SETTINGS_STRIP_ON, false);
        PreferenceUtil.storeConsumerChatDetails(getActivity(), null);

        DialogManager.getInstance().hideProgress();
        previousScreen(SignUpScreen.class, true);

    }


    /*Interface default ok button click*/
    @Override
    public void onOkClick() {

    }
}
