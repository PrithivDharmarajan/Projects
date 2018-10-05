package com.calix.calixgigamanage.ui.parentalcontrol;

import android.app.TimePickerDialog;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.utils.AppConstants;
import com.calix.calixgigamanage.utils.NumberUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ParentalControlBedTime extends BaseActivity {

    @BindView(R.id.pc_bed_time_par_lay)
    RelativeLayout mParentControlBedTime;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.header_left_img)
    ImageView mHeaderLeftImg;

    @BindView(R.id.control_bed_time_header_msg_lay)
    RelativeLayout mControlBedTimeHeaderLay;

    @BindView(R.id.control_bed_time_header_bg_lay)
    RelativeLayout mControlBedTimeHeaderBgLay;

    @BindView(R.id.pc_bed_time_mon_lay)
    RelativeLayout mPCBedTimeMonLay;

    @BindView(R.id.pc_bed_time_tue_lay)
    RelativeLayout mPCBedTimeTueLay;

    @BindView(R.id.pc_bed_time_wed_lay)
    RelativeLayout mPCBedTimeWedLay;

    @BindView(R.id.pc_bed_time_thu_lay)
    RelativeLayout mPCBedTimeThuLay;

    @BindView(R.id.pc_bed_time_fri_lay)
    RelativeLayout mPCBedTimeFriLay;

    @BindView(R.id.pc_bed_time_sat_lay)
    RelativeLayout mPCBedTimeSatLay;

    @BindView(R.id.pc_bed_time_sun_lay)
    RelativeLayout mPCBedTimeSunLay;

    @BindView(R.id.pc_bed_time_mon_txt)
    TextView mPCBedTimeMonTxt;

    @BindView(R.id.pc_bed_time_tue_txt)
    TextView mPCBedTimeTueTxt;

    @BindView(R.id.pc_bed_time_wed_txt)
    TextView mPCBedTimeWedTxt;

    @BindView(R.id.pc_bed_time_thu_txt)
    TextView mPCBedTimeThuTxt;

    @BindView(R.id.pc_bed_time_fri_txt)
    TextView mPCBedTimeFriTxt;

    @BindView(R.id.pc_bed_time_sat_txt)
    TextView mPCBedTimeSatTxt;

    @BindView(R.id.pc_bed_time_sun_txt)
    TextView mPCBedTimeSunTxt;

    @BindView(R.id.bed_time_filter_weeekend_enable_img)
    SwitchCompat mBedTimeFilterWeekendEnableToggle;

    private boolean isChangeMonBool = true , isChangeTueBool = true, isChangeWedBool = true , isChangeThuBool = true, isChangeFriBool = false, isChangeSatBool = false , isChangeSunBool = false;


    private int mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_parental_control_bed_time);
        initView();
    }

    private void initView() {
                    /*For error track purpose - log with class name*/
        AppConstants.TAG = this.getClass().getSimpleName();

       /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mParentControlBedTime);

        setHeaderView();

        mBedTimeFilterWeekendEnableToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean onOffToggleBool) {
                isChangeFriBool = onOffToggleBool;
                isChangeSatBool = onOffToggleBool;
                isChangeSunBool = onOffToggleBool;

                mPCBedTimeFriLay.setBackground(onOffToggleBool ? getResources().getDrawable(R.drawable.round_violet_bg) : getResources().getDrawable(R.drawable.round_white_violet_bg) );
                mPCBedTimeFriTxt.setTextColor(onOffToggleBool ? getResources().getColor(R.color.white) : getResources().getColor(R.color.violet));
                mPCBedTimeSatLay.setBackground(onOffToggleBool ? getResources().getDrawable(R.drawable.round_violet_bg) : getResources().getDrawable(R.drawable.round_white_violet_bg) );
                mPCBedTimeSatTxt.setTextColor(onOffToggleBool ? getResources().getColor(R.color.white) : getResources().getColor(R.color.violet));
                mPCBedTimeSunLay.setBackground(onOffToggleBool ? getResources().getDrawable(R.drawable.round_violet_bg) : getResources().getDrawable(R.drawable.round_white_violet_bg) );
                mPCBedTimeSunTxt.setTextColor(onOffToggleBool ? getResources().getColor(R.color.white) : getResources().getColor(R.color.violet));

            }
        });

    }

    /*View onClick*/
    @OnClick({R.id.header_left_img_lay, R.id.bed_time_enable_time_edit_img, R.id.awake_time_enable_time_edit_img, R.id.pc_bed_time_mon_lay,R.id.pc_bed_time_tue_lay,R.id.pc_bed_time_wed_lay,R.id.pc_bed_time_thu_lay,R.id.pc_bed_time_fri_lay,R.id.pc_bed_time_sat_lay,R.id.pc_bed_time_sun_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;
            case R.id.bed_time_enable_time_edit_img:
            case R.id.awake_time_enable_time_edit_img:
                showTimePickerDialog();
                break;
            case R.id.pc_bed_time_mon_lay:
                if (!isChangeMonBool) {
                    mPCBedTimeMonLay.setBackground( getResources().getDrawable(R.drawable.round_violet_bg));
                    mPCBedTimeMonTxt.setTextColor( getResources().getColor(R.color.white));
                    isChangeMonBool = true;
                }else {
                    mPCBedTimeMonLay.setBackground( getResources().getDrawable(R.drawable.round_white_violet_bg));
                    mPCBedTimeMonTxt.setTextColor( getResources().getColor(R.color.violet));
                    isChangeMonBool = false;
                }
                break;
            case R.id.pc_bed_time_tue_lay:
                if (!isChangeTueBool) {
                    mPCBedTimeTueLay.setBackground( getResources().getDrawable(R.drawable.round_violet_bg));
                    mPCBedTimeTueTxt.setTextColor( getResources().getColor(R.color.white));
                    isChangeTueBool = true;
                }else {
                    mPCBedTimeTueLay.setBackground( getResources().getDrawable(R.drawable.round_white_violet_bg));
                    mPCBedTimeTueTxt.setTextColor( getResources().getColor(R.color.violet));
                    isChangeTueBool = false;
                }
                break;
            case R.id.pc_bed_time_wed_lay:
                if (!isChangeWedBool) {
                    mPCBedTimeWedLay.setBackground( getResources().getDrawable(R.drawable.round_violet_bg));
                    mPCBedTimeWedTxt.setTextColor( getResources().getColor(R.color.white));
                    isChangeWedBool = true;
                }else {
                    mPCBedTimeWedLay.setBackground( getResources().getDrawable(R.drawable.round_white_violet_bg));
                    mPCBedTimeWedTxt.setTextColor( getResources().getColor(R.color.violet));
                    isChangeWedBool = false;
                }
                break;
            case R.id.pc_bed_time_thu_lay:
                if (!isChangeThuBool) {
                    mPCBedTimeThuLay.setBackground( getResources().getDrawable(R.drawable.round_violet_bg));
                    mPCBedTimeThuTxt.setTextColor( getResources().getColor(R.color.white));
                    isChangeThuBool = true;
                }else {
                    mPCBedTimeThuLay.setBackground( getResources().getDrawable(R.drawable.round_white_violet_bg));
                    mPCBedTimeThuTxt.setTextColor( getResources().getColor(R.color.violet));
                    isChangeThuBool = false;
                }
                break;
            case R.id.pc_bed_time_fri_lay:
                if (!isChangeFriBool) {
                    mPCBedTimeFriLay.setBackground( getResources().getDrawable(R.drawable.round_violet_bg));
                    mPCBedTimeFriTxt.setTextColor( getResources().getColor(R.color.white));
                    isChangeFriBool = true;
                }else {
                    mPCBedTimeFriLay.setBackground( getResources().getDrawable(R.drawable.round_white_violet_bg));
                    mPCBedTimeFriTxt.setTextColor( getResources().getColor(R.color.violet));
                    isChangeFriBool = false;
                }
                break;
            case R.id.pc_bed_time_sat_lay:
                if (!isChangeSatBool) {
                    mPCBedTimeSatLay.setBackground( getResources().getDrawable(R.drawable.round_violet_bg));
                    mPCBedTimeSatTxt.setTextColor( getResources().getColor(R.color.white));
                    isChangeSatBool = true;
                }else {
                    mPCBedTimeSatLay.setBackground( getResources().getDrawable(R.drawable.round_white_violet_bg));
                    mPCBedTimeSatTxt.setTextColor( getResources().getColor(R.color.violet));
                    isChangeSatBool = false;
                }
                break;
            case R.id.pc_bed_time_sun_lay:
                if (!isChangeSunBool) {
                    mPCBedTimeSunLay.setBackground( getResources().getDrawable(R.drawable.round_violet_bg));
                    mPCBedTimeSunTxt.setTextColor( getResources().getColor(R.color.white));
                    isChangeSunBool = true;
                }else {
                    mPCBedTimeSunLay.setBackground( getResources().getDrawable(R.drawable.round_white_violet_bg));
                    mPCBedTimeSunTxt.setTextColor( getResources().getColor(R.color.violet));
                    isChangeSunBool = false;
                }
                break;
        }
    }


    private void setHeaderView() {
         /*Header*/
        mControlBedTimeHeaderLay.setVisibility(IsScreenModePortrait() ? View.VISIBLE : View.GONE);
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderLeftImg.setImageResource(R.drawable.back_icon);
        mHeaderTxt.setText(getString(R.string.bed_time_for_samantha));

         /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mControlBedTimeHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt =  getResources().getDimensionPixelSize(IsScreenModePortrait() ? R.dimen.size190 : R.dimen.size45);
                    mControlBedTimeHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(ParentalControlBedTime.this)));
                    mControlBedTimeHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(ParentalControlBedTime.this), 0, 0);
                    mControlBedTimeHeaderBgLay.setBackground(IsScreenModePortrait() ? getResources().getDrawable(R.drawable.header_violet_bg) : getResources().getDrawable(R.color.violet));
                }

            });
        }
    }

    private void showTimePickerDialog() {
        final Calendar cal = Calendar.getInstance();
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);
        new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, false)
                .show();
    }

    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mHour = hourOfDay;
            mMinute = minute;

            String time = String.valueOf(mHour) + ":" +
                    mMinute;

            String mTime_input = "HH:mm";
            SimpleDateFormat timeinputFormat = new SimpleDateFormat(
                    mTime_input, Locale.getDefault());
            Date date1 = null;
            String str1 = null;

            try {
                date1 = timeinputFormat.parse(time);
                str1 = timeinputFormat.format(date1);

                Date dateObj;
                SimpleDateFormat mServerFormat = new SimpleDateFormat("HH:mm", Locale.US);
                SimpleDateFormat mTargetFormat = new SimpleDateFormat("hh:mm aa", Locale.US);
                try {
                    dateObj = mServerFormat.parse(str1);
                    mTargetFormat.setTimeZone(TimeZone.getDefault());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

    };

    /*Screen orientation Changes*/
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setHeaderView();
    }


    /*Check screen orientation*/
    protected boolean IsScreenModePortrait() {
        return this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    @Override
    public void onBackPressed() {
        backScreen();
    }
}
