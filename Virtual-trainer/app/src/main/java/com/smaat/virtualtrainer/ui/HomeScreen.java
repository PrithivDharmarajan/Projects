package com.smaat.virtualtrainer.ui;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.smaat.virtualtrainer.R;
import com.smaat.virtualtrainer.adapter.InviteStreamingAdapter;
import com.smaat.virtualtrainer.apiinterface.APIRequestHandler;
import com.smaat.virtualtrainer.entity.InviteStreamingEntityRes;
import com.smaat.virtualtrainer.main.BaseActivity;
import com.smaat.virtualtrainer.model.JoinStreamingEntity;
import com.smaat.virtualtrainer.model.StreamingEntity;
import com.smaat.virtualtrainer.utils.AppConstants;
import com.smaat.virtualtrainer.utils.DialogManager;
import com.smaat.virtualtrainer.utils.DialogMangerOkEdtCallback;
import com.smaat.virtualtrainer.utils.DialogMangerTwoBtnCallback;
import com.smaat.virtualtrainer.utils.GlobalMethods;
import com.smaat.virtualtrainer.wowza.JoinVideoStreaming;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tyrantgit.explosionfield.ExplosionField;


public class HomeScreen extends BaseActivity implements DialogMangerTwoBtnCallback {

    @BindView(R.id.parent_lay)
    ViewGroup mHomeViewGroup;

    @BindView(R.id.header_curve_lay)
    RelativeLayout mHeaderCurveLay;

    @BindView(R.id.menu_with_curve_lay)
    RelativeLayout mMenuWithCurveLay;

    @BindView(R.id.menu_out_side_view)
    View mMenuOutSideView;

    @BindView(R.id.invite_list)
    RecyclerView mInviteRecyclerListView;

    private Handler mHomeHandler, mHomeLogoutHandler;
    private Runnable mHomeRunnable, mHomeLogoutRunnable;
    private InviteStreamingAdapter mInviteStreamingAdapter;
    private ArrayList<InviteStreamingEntityRes> mInviteStreamingISEArrList;
    private ExplosionField mExplosionField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ui_home_screen);
        mHomeHandler = new Handler();
        mHomeLogoutHandler = new Handler();

        mHomeViewGroup.setFocusableInTouchMode(false);
        mHomeViewGroup.setFocusable(false);
        mHomeViewGroup.setClickable(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mHomeHandler != null) {
            mHomeHandler.removeCallbacks(mHomeRunnable);
        }
        if (mHomeLogoutHandler != null) {
            mHomeLogoutHandler.removeCallbacks(mHomeLogoutRunnable);
        }
        initComponent();
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mHomeHandler != null) {
            mHomeHandler.removeCallbacks(mHomeRunnable);
        }
        if (mHomeLogoutHandler != null) {
            mHomeLogoutHandler.removeCallbacks(mHomeLogoutRunnable);
        }
    }


    private void initComponent() {
        ButterKnife.bind(this);
        setupUI(mHomeViewGroup);
        mExplosionField = ExplosionField.attach2Window(this);

        mHeaderLeftBtnLay.setVisibility(View.INVISIBLE);
        mHeaderRightBtnLay.setVisibility(View.INVISIBLE);

        mHeaderCurveLay.setVisibility(View.VISIBLE);
        mMenuWithCurveLay.setVisibility(View.GONE);
        mMenuOutSideView.setVisibility(View.GONE);

        mHeaderTxt.setText(getString(R.string.home));
        animationAction();
        callHomeAPI();
    }


    @OnClick({R.id.start_stream_txt, R.id.join_stream_txt, R.id.profile_lay, R.id.help_lay, R.id.buy_full_version_lay, R.id.terms_condition_lay, R.id.support_lay
            , R.id.logout_lay, R.id.menu_curve_lay, R.id.menu_out_side_view, R.id.header_menu_img})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_stream_txt:
                mHeaderCurveLay.setVisibility(View.VISIBLE);
                mMenuWithCurveLay.setVisibility(View.GONE);
                mMenuOutSideView.setVisibility(View.GONE);
                if (!(mActivity instanceof StartStreamingScreen)) {
                    nextScreen(StartStreamingScreen.class, false);
                }
                break;
            case R.id.join_stream_txt:
                mHeaderCurveLay.setVisibility(View.VISIBLE);
                mMenuWithCurveLay.setVisibility(View.GONE);
                mMenuOutSideView.setVisibility(View.GONE);

                DialogManager.showJoinStreamingDialog(this, new DialogMangerOkEdtCallback() {
                    @Override
                    public void onOkEdtClick(String name, String meetingId) {
                        AppConstants.STREAMING_ID = meetingId;
                        AppConstants.STREAMING_NAME = "test";
                        nextScreen(JoinVideoStreaming.class, false);
//                        APIRequestHandler.getInstance().joinStreamingAPICall(meetingId, AppConstants.SUCCESS_CODE, HomeScreen.this);
                    }
                });

                break;
            case R.id.profile_lay:

                mHeaderCurveLay.setVisibility(View.VISIBLE);
                mMenuWithCurveLay.setVisibility(View.GONE);
                mMenuOutSideView.setVisibility(View.GONE);

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

                        mExplosionField.explode(mHomeViewGroup);
                        mHomeLogoutRunnable = new Runnable() {
                            @Override
                            public void run() {
                                previousScreen(EntryScreen.class, true);
                            }
                        };
                        GlobalMethods.userDetails(getString(R.string.three), AppConstants.FAILURE_CODE, "", "", "",
                                AppConstants.FAILURE_CODE, HomeScreen.this);
                        mHomeViewGroup.setFocusableInTouchMode(false);
                        mHomeViewGroup.setFocusable(false);
                        mHomeViewGroup.setClickable(false);
                        mHomeLogoutHandler.postDelayed(mHomeLogoutRunnable, 900);

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


    private void callHomeAPI() {

        mHomeRunnable = new Runnable() {
            @Override
            public void run() {

                APIRequestHandler.getInstance().streamingAPICall(HomeScreen.this);
                mHomeHandler.postDelayed(mHomeRunnable, 5000);
            }
        };
        mHomeHandler.postDelayed(mHomeRunnable, 0);
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
        if (responseObj instanceof JoinStreamingEntity) {
            JoinStreamingEntity JoinStreamingEntityRes = (JoinStreamingEntity) responseObj;
            if (JoinStreamingEntityRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                AppConstants.STREAMING_ID = JoinStreamingEntityRes.getResult().getStream_id();
                AppConstants.STREAMING_NAME = JoinStreamingEntityRes.getResult().getStream_name();
                nextScreen(AddJoinessScreen.class, true);

            } else {
                DialogManager.showBasicAlertDialog(this, JoinStreamingEntityRes.getMessage(), getString(R.string.ok), false,
                        getString(R.string.ok), true, true, this);
            }
        } else if (responseObj instanceof StreamingEntity) {
            StreamingEntity streamingEntityRes = (StreamingEntity) responseObj;
            if (streamingEntityRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                setInviteStreamingAdapter(streamingEntityRes.getResult().getInvite_stream());
            }
        }
    }

    private void setInviteStreamingAdapter(ArrayList<InviteStreamingEntityRes> mList) {
        if (mInviteStreamingAdapter == null) {
            mInviteStreamingISEArrList = new ArrayList<>();
            mInviteStreamingISEArrList.addAll(mList);
            mInviteStreamingAdapter = new InviteStreamingAdapter(this, mInviteStreamingISEArrList);
            mInviteRecyclerListView.setLayoutManager(new LinearLayoutManager(this));
            mInviteRecyclerListView.setAdapter(mInviteStreamingAdapter);


        } else {
            mInviteStreamingISEArrList.clear();
            mInviteStreamingISEArrList.addAll(mList);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mInviteStreamingAdapter.notifyDataSetChanged();
                }
            });
        }

    }

    @Override
    public void onBackPressed() {

        DialogManager.showBasicAlertDialog(this, getString(R.string.exit_msg), getString(R.string.no), true, getString(R.string.yes), true, false, new DialogMangerTwoBtnCallback() {
            @Override
            public void onYesClick() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask();
                } else {
                    finish();
                }
            }

            @Override
            public void onNoClick() {

            }
        });

    }

    @Override
    public void onYesClick() {

    }

    @Override
    public void onNoClick() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHomeHandler != null) {
            mHomeHandler.removeCallbacks(mHomeRunnable);
        }
        if (mHomeLogoutHandler != null) {
            mHomeLogoutHandler.removeCallbacks(mHomeLogoutRunnable);
        }
    }
}
