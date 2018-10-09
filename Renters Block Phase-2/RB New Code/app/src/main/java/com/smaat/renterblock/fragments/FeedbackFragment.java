package com.smaat.renterblock.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.smaat.renterblock.BuildConfig;
import com.smaat.renterblock.R;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedbackFragment extends BaseFragment {


    @BindView(R.id.sub_email_txt)
    TextView mSubEmailTxt;

    @BindView(R.id.message_edt)
    EditText mMessageEdt;

    @BindView(R.id.android_version_txt)
    TextView mAndroidVersionTxt;

    @BindView(R.id.app_version_txt)
    TextView mAppVersionTxt;

    @BindView(R.id.device_txt)
    TextView mDeviceTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_feed_back_screen, container, false);
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
            ((HomeScreen) getActivity()).setDrawerAction(true);

            /*set header first  img*/
            ((HomeScreen) getActivity()).setHeaderLeftFirstImgLayVisible(1);
            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.mipmap.app_icon, 0);

            /*set header second img*/
            ((HomeScreen) getActivity()).setHeaderLeftSecondImgLayVisible(R.mipmap.app_icon, 0);
            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.mipmap.app_icon, 0,"");

            /*set header third txt*/
            ((HomeScreen) getActivity()).setHeaderLeftThirdTxtLayVisible(getString(R.string.app_name), 0);
            ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.send), 1);

            /*set header txt*/
            ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.feedback), 1);
            ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.feedback), 0);

            initView();
        }
    }

    /*InitViews*/
    private void initView() {

        /*set data*/
        mSubEmailTxt.setText(getString(R.string.app_name) + " " + getString(R.string.version) + BuildConfig.VERSION_NAME);
        mAndroidVersionTxt.setText(getString(R.string.android_version_with_colon) + " " + android.os.Build.VERSION.RELEASE);
        mAppVersionTxt.setText(getString(R.string.app_version_with_colon) + " " + getString(R.string.version) + BuildConfig.VERSION_NAME);
        mDeviceTxt.setText(getString(R.string.device_with_colon) + " " + AppConstants.DEVICE);

        /*header right txt click*/
        ((HomeScreen) getActivity()).mHeaderRightThirdTxtLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageStr = mMessageEdt.getText().toString().trim();
                if (messageStr.isEmpty()) {
                    mMessageEdt.requestFocus();
                    DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.please_enter_msg), FeedbackFragment.this);
                } else {
                    APIRequestHandler.getInstance().feedbackAPICall(messageStr, FeedbackFragment.this);
                }
            }
        });

    }

    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (getActivity() != null && resObj instanceof CommonResponse) {
            mMessageEdt.setText("");
            CommonResponse feedbackRes = (CommonResponse) resObj;
            if (!feedbackRes.getMsg().isEmpty())
                DialogManager.getInstance().showAlertPopup(getActivity(), feedbackRes.getMsg(), FeedbackFragment.this);
        }
    }
}
