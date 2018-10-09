package com.smaat.virtualtrainer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smaat.virtualtrainer.R;
import com.smaat.virtualtrainer.apiinterface.APIRequestHandler;
import com.smaat.virtualtrainer.main.BaseActivity;
import com.smaat.virtualtrainer.model.CommonModelEntity;
import com.smaat.virtualtrainer.model.CreateStreamingEntity;
import com.smaat.virtualtrainer.utils.AppConstants;
import com.smaat.virtualtrainer.utils.DialogManager;
import com.smaat.virtualtrainer.utils.DialogMangerTwoBtnCallback;
import com.smaat.virtualtrainer.utils.GlobalMethods;
import com.smaat.virtualtrainer.wowza.StartVideoStreaming;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartStreamingScreen extends BaseActivity implements DialogMangerTwoBtnCallback {


    @BindView(R.id.parent_lay)
    ViewGroup mStartStreamViewGroup;

    @BindView(R.id.header_left_btn_lay)
    RelativeLayout mHeaderLeftBtnLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.header_curve_lay)
    RelativeLayout mHeaderCurveLay;

    @BindView(R.id.menu_with_curve_lay)
    RelativeLayout mMenuWithCurveLay;

    @BindView(R.id.menu_out_side_view)
    View mMenuOutSideView;

    @BindView(R.id.streaming_id_txt)
    TextView mStreamingIdTxt;

    @BindView(R.id.title_edt)
    EditText mTitleEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_start_streaming_screen);

        initComponent();
    }


    private void initComponent() {
        ButterKnife.bind(this);
        setupUI(mStartStreamViewGroup);

        mHeaderLeftBtnLay.setVisibility(View.VISIBLE);

        mHeaderCurveLay.setVisibility(View.VISIBLE);
        mMenuWithCurveLay.setVisibility(View.GONE);
        mMenuOutSideView.setVisibility(View.GONE);

        mHeaderTxt.setText(getString(R.string.home));
        animationAction();

        APIRequestHandler.getInstance().createStreamingAPICall(this);
    }


    @OnClick({R.id.header_left_btn_lay, R.id.add_btn, R.id.start_btn, R.id.share_stream_id_btn, R.id.profile_lay, R.id.help_lay, R.id.buy_full_version_lay, R.id.terms_condition_lay, R.id.support_lay
            , R.id.logout_lay, R.id.menu_curve_lay, R.id.menu_out_side_view, R.id.header_menu_img})
    void onClick(View view) {
        String titleStr = mTitleEdt.getText().toString().trim();
        switch (view.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;

            case R.id.add_btn:

                if (titleStr.isEmpty()) {
                    DialogManager.showBasicAlertDialog(mActivity, getString(R.string.enter_title), getString(R.string.ok), false, getString(R.string.ok), true, true, this);
                } else {

                    AppConstants.STREAMING_ID = mStreamingIdTxt.getText().toString().trim();
                    AppConstants.STREAMING_NAME = titleStr;

                    nextScreen(AddJoinessScreen.class, true);
                }

                break;
            case R.id.start_btn:

                mHeaderCurveLay.setVisibility(View.VISIBLE);
                mMenuWithCurveLay.setVisibility(View.GONE);
                mMenuOutSideView.setVisibility(View.GONE);


                if (titleStr.isEmpty()) {
                    DialogManager.showBasicAlertDialog(mActivity, getString(R.string.enter_title), getString(R.string.ok), false, getString(R.string.ok), true, true, this);
                } else {
                    AppConstants.STREAMING_ID = mStreamingIdTxt.getText().toString().trim();
                    AppConstants.STREAMING_NAME = titleStr;
                    APIRequestHandler.getInstance().startStreamingAPICall(AppConstants.STREAMING_NAME, AppConstants.STREAMING_ID,
                            AppConstants.SUCCESS_CODE, this);
                }
                break;
            case R.id.share_stream_id_btn:

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.streaming_id) + " " + mStreamingIdTxt.getText().toString().trim());
                startActivity(Intent.createChooser(sharingIntent, "Share via"));

                break;
            case R.id.profile_lay:

                mHeaderCurveLay.setVisibility(View.VISIBLE);
                mMenuWithCurveLay.setVisibility(View.GONE);
                mMenuOutSideView.setVisibility(View.GONE);
                if (!(mActivity instanceof ProfileScreen))
                    nextScreen(ProfileScreen.class, false);


                break;
            case R.id.help_lay:

                mHeaderCurveLay.setVisibility(View.VISIBLE);
                mMenuWithCurveLay.setVisibility(View.GONE);
                mMenuOutSideView.setVisibility(View.GONE);
                if (!(mActivity instanceof HelpandTermsScreen)) {
                    mHeaderType = 1;
                    nextScreen(HelpandTermsScreen.class, false);
                }

                break;
            case R.id.buy_full_version_lay:

                mHeaderCurveLay.setVisibility(View.VISIBLE);
                mMenuWithCurveLay.setVisibility(View.GONE);
                mMenuOutSideView.setVisibility(View.GONE);
                if (!(mActivity instanceof ProVersionScreen)) {
                    nextScreen(ProVersionScreen.class, false);
                }

                break;
            case R.id.terms_condition_lay:

                mHeaderCurveLay.setVisibility(View.VISIBLE);
                mMenuWithCurveLay.setVisibility(View.GONE);
                mMenuOutSideView.setVisibility(View.GONE);
                if (!(mActivity instanceof HelpandTermsScreen)) {
                    mHeaderType = 2;
                    nextScreen(HelpandTermsScreen.class, false);
                }

                break;
            case R.id.support_lay:

                mHeaderCurveLay.setVisibility(View.VISIBLE);
                mMenuWithCurveLay.setVisibility(View.GONE);
                mMenuOutSideView.setVisibility(View.GONE);
                if (!(mActivity instanceof SupportScreen)) {
                    nextScreen(SupportScreen.class, false);
                }

                break;
            case R.id.logout_lay:

                mHeaderCurveLay.setVisibility(View.VISIBLE);
                mMenuWithCurveLay.setVisibility(View.GONE);
                mMenuOutSideView.setVisibility(View.GONE);
                DialogManager.showBasicAlertDialog(mActivity, getString(R.string.logout_msg), getString(R.string.no), true, getString(R.string.yes), true, false, new DialogMangerTwoBtnCallback() {
                    @Override
                    public void onYesClick() {
                        GlobalMethods.userDetails(getString(R.string.three), AppConstants.FAILURE_CODE, "", "", "", AppConstants.FAILURE_CODE, StartStreamingScreen.this);
                        previousScreen(EntryScreen.class, true);
                    }

                    @Override
                    public void onNoClick() {

                    }
                });
                break;

            case R.id.menu_out_side_view:
            case R.id.menu_curve_lay:
                mMenuWithCurveLay.setVisibility(View.GONE);
                mMenuOutSideView.setVisibility(View.GONE);
                mMenuWithCurveLay.startAnimation(mExitToTop);
                break;

            case R.id.header_menu_img:
                mMenuWithCurveLay.setVisibility(View.VISIBLE);
                mMenuWithCurveLay.startAnimation(mEnterFromTop);
                break;
        }

    }

    private void animationAction() {
        mEnterFromTop.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mHeaderCurveLay.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mMenuOutSideView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mExitToTop.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mMenuOutSideView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mHeaderCurveLay.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);
        if (responseObj instanceof CreateStreamingEntity) {
            //Create Streaming API Res
            CreateStreamingEntity createStreamingRes = (CreateStreamingEntity) responseObj;
            if (createStreamingRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                mStreamingIdTxt.setText(createStreamingRes.getResult().getStream_id());
            } else {
                DialogManager.showBasicAlertDialog(this, createStreamingRes.getMessage(), getString(R.string.ok), false, getString(R.string.ok), true, true, this);
            }

        } else if (responseObj instanceof CommonModelEntity) {
            //Start Streaming API Res
            CommonModelEntity startStreamingRes = (CommonModelEntity) responseObj;
            if (startStreamingRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                nextScreen(StartVideoStreaming.class, true);
            } else {
                DialogManager.showBasicAlertDialog(this, startStreamingRes.getMessage(), getString(R.string.ok), false, getString(R.string.ok), true, true, this);
            }
        }
    }

    @Override
    public void onBackPressed() {
        mHeaderCurveLay.setVisibility(View.VISIBLE);
        mMenuWithCurveLay.setVisibility(View.GONE);
        finishScreen();
    }

    @Override
    public void onYesClick() {

    }

    @Override
    public void onNoClick() {

    }
}
