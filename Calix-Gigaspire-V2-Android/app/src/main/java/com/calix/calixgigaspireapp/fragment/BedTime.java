package com.calix.calixgigaspireapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.main.BaseFragment;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.DialogManager;
import com.calix.calixgigaspireapp.utils.InterfaceEdtTimeBtnCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BedTime extends BaseFragment {

    @BindView(R.id.start_mon_txt)
    TextView mStartTimeTxt;

    @BindView(R.id.end_mon_txt)
    TextView mEndTimeTxt;

    @BindView(R.id.date_to_txt)
    TextView mToTxt;

    @BindView(R.id.set_bed_time_txt)
    TextView mSetMonBedTimeTxt;

    @BindView(R.id.set_tues_bed_time_txt)
    TextView mSetTuesdayBedTimeTxt;

    @BindView(R.id.start_tues_txt)
    TextView mStartTuesdayTxt;

    @BindView(R.id.end_tues_txt)
    TextView mEndTuesdayTxt;

    @BindView(R.id.to_tues_txt)
    TextView mToTuesdayTxt;

    /*Wednesday set txt*/
    @BindView(R.id.set_wed_bed_time_txt)
    TextView mSetWednesdayBedTimeTxt;

    @BindView(R.id.start_wed_txt)
    TextView mStartWednesTxt;

    @BindView(R.id.end_wed_txt)
    TextView mEndWednesdayTxt;

    @BindView(R.id.wed_to_txt)
    TextView mToWednesdayTxt;

    @BindView(R.id.set_thru_bed_time_txt)
    TextView mSetThursdayBedTimeTxt;

    @BindView(R.id.start_thrus_txt)
    TextView mStartThrusdayTxt;

    @BindView(R.id.end_thrus_txt)
    TextView mEndThrusdayTxt;

    @BindView(R.id.thru_to_txt)
    TextView mToThrusdayTxt;

    @BindView(R.id.set_fri_bed_time_txt)
    TextView mSetFridayBedTimeTxt;

    @BindView(R.id.start_fri_txt)
    TextView mStartFridayTxt;

    @BindView(R.id.end_fri_txt)
    TextView mEndFridayTxt;

    @BindView(R.id.fri_to_txt)
    TextView mToFridayTxt;

    @BindView(R.id.set_satur_bed_time_txt)
    TextView mSetSaturdayBedTimeTxt;

    @BindView(R.id.start_satur_txt)
    TextView mStartSaturTxt;

    @BindView(R.id.end_satur_txt)
    TextView mEndSaturdayTxt;

    @BindView(R.id.satur_to_txt)
    TextView mToSaturdayTxt;

    @BindView(R.id.set_sun_bed_time_txt)
    TextView mSetSundayBedTimeTxt;

    @BindView(R.id.start_sun_txt)
    TextView mStartSundayTxt;

    @BindView(R.id.end_sun_txt)
    TextView mEndSundayTxt;

    @BindView(R.id.sun_to_txt)
    TextView mToSundayTxt;

    @BindView(R.id.view_one)
    View mMoTriangleLay;

    @BindView(R.id.view_two)
    View mMoViewLay;

    @BindView(R.id.mon_img)
    ImageView mMoImg;

    @BindView(R.id.four_vw)
    View mTuTriangleLay;

    @BindView(R.id.three_view)
    View mTuViewLay;

    @BindView(R.id.tues_img)
    ImageView mTuImg;

    @BindView(R.id.fiv_vw)
    View mWeTriangleLay;

    @BindView(R.id.six_vw)
    View mWeViewLay;

    @BindView(R.id.wed_img)
    ImageView mWeImg;

    @BindView(R.id.nine_vw)
    View mThTriangleLay;

    @BindView(R.id.eight_vw)
    View mThViewLay;

    @BindView(R.id.thrus_img)
    ImageView mThImg;

    @BindView(R.id.ten_vw)
    View mFriTriangleLay;

    @BindView(R.id.ele_vw)
    View mFriViewLay;

    @BindView(R.id.fri_img)
    ImageView mFriImg;

    @BindView(R.id.fourteen_vw)
    View mSateTriangleLay;

    @BindView(R.id.thirteen_vw)
    View mSateViewLay;

    @BindView(R.id.sater_vw)
    ImageView mSateImg;

    @BindView(R.id.fifteen_vw)
    View mSunTriangleLay;

    @BindView(R.id.sixteen_vw)
    View mSunViewLay;

    @BindView(R.id.sunday_img)
    ImageView mSunImg;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ui_user_bed_time, container, false);
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(rootView);

        /*To focus on current fragment*/
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        initView();
        return rootView;
    }

    private void initView() {
        /*For error track purpose - log with class name*/
        AppConstants.TAG = this.getClass().getSimpleName();
    }

    /*View onClick*/
    @OnClick({R.id.set_bed_time_txt, R.id.set_tues_bed_time_txt, R.id.set_wed_bed_time_txt, R.id.set_thru_bed_time_txt, R.id.set_fri_bed_time_txt, R.id.set_satur_bed_time_txt, R.id.set_sun_bed_time_txt})
    public void onClick(View v) {
        if (getActivity() != null)
            switch (v.getId()) {
                case R.id.set_bed_time_txt:
                    DialogManager.getInstance().showTimePopup(getActivity(), mStartTimeTxt, "", mEndTimeTxt, "", new InterfaceEdtTimeBtnCallback() {
                        @Override
                        public void onPositiveClick(String startTimeStr, String endTimeStr) {
                            mStartTimeTxt.setText(startTimeStr);
                            mEndTimeTxt.setText(endTimeStr);
                            mSetMonBedTimeTxt.setVisibility(View.GONE);
                            mStartTimeTxt.setVisibility(View.VISIBLE);
                            mEndTimeTxt.setVisibility(View.VISIBLE);
                            mToTxt.setVisibility(View.VISIBLE);

                            mMoTriangleLay.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_triangle_monday));
                            mMoViewLay.setBackgroundColor(getResources().getColor(R.color.sky_light_green));
                            mMoImg.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_round_mon));
                        }

                        @Override
                        public void onNegativeClick() {
                            mMoTriangleLay.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_triangle_common));
                            mMoViewLay.setBackgroundColor(getResources().getColor(R.color.grey));
                            mMoImg.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_round_common));
                        }
                    });
                    break;

                case R.id.set_tues_bed_time_txt:
                    DialogManager.getInstance().showTimePopup(getActivity(), mStartTuesdayTxt, "", mEndTuesdayTxt, "", new InterfaceEdtTimeBtnCallback() {
                        @Override
                        public void onPositiveClick(String startTimeStr, String endTimeStr) {
                            mStartTuesdayTxt.setText(startTimeStr);
                            mEndTuesdayTxt.setText(endTimeStr);
                            mSetTuesdayBedTimeTxt.setVisibility(View.GONE);
                            mStartTuesdayTxt.setVisibility(View.VISIBLE);
                            mEndTuesdayTxt.setVisibility(View.VISIBLE);
                            mToTuesdayTxt.setVisibility(View.VISIBLE);

                            mTuTriangleLay.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_triangle_tues));
                            mTuViewLay.setBackgroundColor(getResources().getColor(R.color.sky_blue));
                            mTuImg.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_round_mon));
                        }

                        @Override
                        public void onNegativeClick() {
                            mTuTriangleLay.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_triangle_common));
                            mTuViewLay.setBackgroundColor(getResources().getColor(R.color.grey));
                            mTuImg.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_round_common));
                        }
                    });
                    break;

                case R.id.set_wed_bed_time_txt:
                    DialogManager.getInstance().showTimePopup(getActivity(), mStartWednesTxt, "", mEndWednesdayTxt, "", new InterfaceEdtTimeBtnCallback() {
                        @Override
                        public void onPositiveClick(String startTimeStr, String endTimeStr) {
                            mStartWednesTxt.setText(startTimeStr);
                            mEndWednesdayTxt.setText(endTimeStr);
                            mSetWednesdayBedTimeTxt.setVisibility(View.GONE);
                            mStartWednesTxt.setVisibility(View.VISIBLE);
                            mEndWednesdayTxt.setVisibility(View.VISIBLE);
                            mToWednesdayTxt.setVisibility(View.VISIBLE);

                            mWeTriangleLay.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_triangle_wed));
                            mWeViewLay.setBackgroundColor(getResources().getColor(R.color.orange));
                            mWeImg.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_round_wed));
                        }

                        @Override
                        public void onNegativeClick() {
                            mWeTriangleLay.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_triangle_common));
                            mWeViewLay.setBackgroundColor(getResources().getColor(R.color.grey));
                            mWeImg.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_round_common));
                        }
                    });
                    break;

                case R.id.set_thru_bed_time_txt:
                    DialogManager.getInstance().showTimePopup(getActivity(), mStartThrusdayTxt, "", mEndThrusdayTxt, "", new InterfaceEdtTimeBtnCallback() {
                        @Override
                        public void onPositiveClick(String startTimeStr, String endTimeStr) {
                            mStartThrusdayTxt.setText(startTimeStr);
                            mEndThrusdayTxt.setText(endTimeStr);
                            mSetThursdayBedTimeTxt.setVisibility(View.GONE);
                            mStartThrusdayTxt.setVisibility(View.VISIBLE);
                            mEndThrusdayTxt.setVisibility(View.VISIBLE);
                            mToThrusdayTxt.setVisibility(View.VISIBLE);

                            mThTriangleLay.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_triangle_thrus));
                            mThViewLay.setBackgroundColor(getResources().getColor(R.color.blue_light));
                            mThImg.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_round_thrus));
                        }

                        @Override
                        public void onNegativeClick() {
                            mThTriangleLay.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_triangle_common));
                            mThViewLay.setBackgroundColor(getResources().getColor(R.color.grey));
                            mThImg.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_round_common));
                        }
                    });
                    break;

                case R.id.set_fri_bed_time_txt:
                    DialogManager.getInstance().showTimePopup(getActivity(), mStartFridayTxt, "", mEndFridayTxt, "", new InterfaceEdtTimeBtnCallback() {
                        @Override
                        public void onPositiveClick(String startTimeStr, String endTimeStr) {
                            mStartFridayTxt.setText(startTimeStr);
                            mEndFridayTxt.setText(endTimeStr);
                            mSetFridayBedTimeTxt.setVisibility(View.GONE);
                            mStartFridayTxt.setVisibility(View.VISIBLE);
                            mEndFridayTxt.setVisibility(View.VISIBLE);
                            mToFridayTxt.setVisibility(View.VISIBLE);

                            mFriTriangleLay.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_triangle_fri));
                            mFriViewLay.setBackgroundColor(getResources().getColor(R.color.sky_light_blue));
                            mFriImg.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_round_fri));
                        }

                        @Override
                        public void onNegativeClick() {
                            mFriTriangleLay.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_triangle_common));
                            mFriViewLay.setBackgroundColor(getResources().getColor(R.color.grey));
                            mFriImg.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_round_common));
                        }
                    });
                    break;

                case R.id.set_satur_bed_time_txt:
                    DialogManager.getInstance().showTimePopup(getActivity(), mStartSaturTxt, "", mEndSaturdayTxt, "", new InterfaceEdtTimeBtnCallback() {
                        @Override
                        public void onPositiveClick(String startTimeStr, String endTimeStr) {
                            mStartSaturTxt.setText(startTimeStr);
                            mEndSaturdayTxt.setText(endTimeStr);
                            mSetSaturdayBedTimeTxt.setVisibility(View.GONE);
                            mStartSaturTxt.setVisibility(View.VISIBLE);
                            mEndSaturdayTxt.setVisibility(View.VISIBLE);
                            mToSaturdayTxt.setVisibility(View.VISIBLE);

                            mSateTriangleLay.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_triangle_sater));
                            mSateViewLay.setBackgroundColor(getResources().getColor(R.color.lavender));
                            mSateImg.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_round_sater));
                        }

                        @Override
                        public void onNegativeClick() {
                            mSateTriangleLay.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_triangle_common));
                            mSateViewLay.setBackgroundColor(getResources().getColor(R.color.grey));
                            mSateImg.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_round_common));
                        }
                    });
                    break;

                case R.id.set_sun_bed_time_txt:
                    DialogManager.getInstance().showTimePopup(getActivity(), mStartSundayTxt, "", mEndSundayTxt, "", new InterfaceEdtTimeBtnCallback() {
                        @Override
                        public void onPositiveClick(String startTimeStr, String endTimeStr) {
                            mStartSundayTxt.setText(startTimeStr);
                            mEndSundayTxt.setText(endTimeStr);
                            mSetSundayBedTimeTxt.setVisibility(View.GONE);
                            mStartSundayTxt.setVisibility(View.VISIBLE);
                            mEndSundayTxt.setVisibility(View.VISIBLE);
                            mToSundayTxt.setVisibility(View.VISIBLE);

                            mSunTriangleLay.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_triangle_sun));
                            mSunViewLay.setBackgroundColor(getResources().getColor(R.color.red_light));
                            mSunImg.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_round_sun));
                        }

                        @Override
                        public void onNegativeClick() {
                            mSunTriangleLay.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_triangle_common));
                            mSunViewLay.setBackgroundColor(getResources().getColor(R.color.grey));
                            mSunImg.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_round_common));
                        }
                    });
                    break;
            }
    }
}
