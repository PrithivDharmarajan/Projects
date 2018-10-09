package com.calix.calixgigaspireapp.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.main.BaseFragment;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.warkiz.widget.IndicatorSeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimeLimit extends BaseFragment {

    @BindView(R.id.discrete_hours)
    IndicatorSeekBar mSeekBarHour;

    @BindView(R.id.discrete_times)
    IndicatorSeekBar mSeekBarMinutes;

    @BindView(R.id.hours_minutes_txt)
    TextView mHoursTxt;

    @BindView(R.id.minutes_txt)
    TextView mMinutesTxt;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ui_user_time_limit, container, false);

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
        /*Hour SeekBar*/
        indiactorSeekBarHour();
        /*Minute SeekBar*/
        indicatorSeekBarMinutes();
    }

    public void indiactorSeekBarHour() {
        mSeekBarHour.setOnSeekChangeListener(new IndicatorSeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(IndicatorSeekBar seekBar, int progress, float progressFloat, boolean fromUserTouch) {
                mHoursTxt.setText(progress + " hrs");
            }

            @Override
            public void onSectionChanged(IndicatorSeekBar seekBar, int thumbPosOnTick, String textBelowTick, boolean fromUserTouch) {
                //only callback on discrete series SeekBar type.
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar, int thumbPosOnTick) {
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
            }
        });
    }

    public void indicatorSeekBarMinutes() {
        mSeekBarMinutes.setOnSeekChangeListener(new IndicatorSeekBar.OnSeekBarChangeListener() {


            @Override
            public void onProgressChanged(IndicatorSeekBar seekBar, int progress, float progressFloat, boolean fromUserTouch) {
                mMinutesTxt.setText(progress + " min");
            }

            @Override
            public void onSectionChanged(IndicatorSeekBar seekBar, int thumbPosOnTick, String textBelowTick, boolean fromUserTouch) {
                //only callback on discrete series SeekBar type.
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar, int thumbPosOnTick) {
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
            }
        });
    }


}
