package com.calix.calixgigamanage.ui.parentalcontrol;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
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




public class ParentalControlTimeLimit extends BaseActivity {

    @BindView(R.id.control_time_limit_par_lay)
    RelativeLayout mParentControlRewardLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.header_left_img)
    ImageView mHeaderLeftImg;

    @BindView(R.id.control_time_limit_header_bg_lay)
    RelativeLayout mControlRewardHeaderBgLay;

    @BindView(R.id.control_time_limit_header_msg_lay)
    RelativeLayout mControlTimeLimitHeaderLay;

    @BindView(R.id.time_limit_mon_lay)
    RelativeLayout mTimeLimitMonLay;

    @BindView(R.id.time_limit_tue_lay)
    RelativeLayout mTimeLimitTueLay;

    @BindView(R.id.time_limit_wed_lay)
    RelativeLayout mTimeLimitWedLay;

    @BindView(R.id.time_limit_thu_lay)
    RelativeLayout mTimeLimitThuLay;

    @BindView(R.id.time_limit_fri_lay)
    RelativeLayout mTimeLimitFriLay;

    @BindView(R.id.time_limit_sat_lay)
    RelativeLayout mTimeLimitSatLay;

    @BindView(R.id.time_limit_sun_lay)
    RelativeLayout mTimeLimitSunLay;

    @BindView(R.id.time_limit_mon_txt)
    TextView mTimeLimitMonTxt;

    @BindView(R.id.time_limit_tue_txt)
    TextView mTimeLimitTueTxt;

    @BindView(R.id.time_limit_wed_txt)
    TextView mTimeLimitWedTxt;

    @BindView(R.id.time_limit_thu_txt)
    TextView mTimeLimitThuTxt;

    @BindView(R.id.time_limit_fri_txt)
    TextView mTimeLimitFriTxt;

    @BindView(R.id.time_limit_sat_txt)
    TextView mTimeLimitSatTxt;

    @BindView(R.id.time_limit_sun_txt)
    TextView mTimeLimitSunTxt;

    @BindView(R.id.time_limit_filter_weeekend_enable_img)
    SwitchCompat mTimeLimitFilterWeekendToggle;

    private boolean isChangeTrue = true , isChangeMonBool = true , isChangeTueBool = false, isChangeWedBool = false , isChangeThuBool = false, isChangeFriBool = false, isChangeSatBool = true , isChangeSunBool = true;
    private boolean isChangeFalse = false;

    private int mDate, mMonth, mYear;
    private String mDateInput = "MM-dd-yyyy", mStartDate;
    private int mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_parental_control_time_limit);
        initView();
    }

    private void initView() {
                    /*For error track purpose - log with class name*/
        AppConstants.TAG = this.getClass().getSimpleName();

       /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mParentControlRewardLay);

        setHeaderView();

        mTimeLimitFilterWeekendToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean onOffToggleBool) {
                isChangeSatBool = onOffToggleBool;
                isChangeSunBool = onOffToggleBool;

                mTimeLimitSatLay.setBackground(onOffToggleBool ? getResources().getDrawable(R.drawable.round_violet_bg) : getResources().getDrawable(R.drawable.round_white_violet_bg) );
                mTimeLimitSatTxt.setTextColor(onOffToggleBool ? getResources().getColor(R.color.white) : getResources().getColor(R.color.violet));
                mTimeLimitSunLay.setBackground(onOffToggleBool ? getResources().getDrawable(R.drawable.round_violet_bg) : getResources().getDrawable(R.drawable.round_white_violet_bg) );
                mTimeLimitSunTxt.setTextColor(onOffToggleBool ? getResources().getColor(R.color.white) : getResources().getColor(R.color.violet));

            }
        });

    }

    /*View onClick*/
    @OnClick({R.id.header_left_img_lay,R.id.time_limit_calender_img,R.id.time_limit_filter_enable_edit_img,R.id.time_limit_filter_disable_edit_img,R.id.time_limit_mon_lay,R.id.time_limit_tue_lay,R.id.time_limit_wed_lay,R.id.time_limit_thu_lay,R.id.time_limit_fri_lay,R.id.time_limit_sat_lay,R.id.time_limit_sun_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;
            case R.id.time_limit_calender_img:
                showDatePickerDialog();
                break;
            case R.id.time_limit_filter_enable_edit_img:
            case R.id.time_limit_filter_disable_edit_img:
                showTimePickerDialog();
                break;
            case R.id.time_limit_mon_lay:
                if (!isChangeMonBool) {
                    mTimeLimitMonLay.setBackground( getResources().getDrawable(R.drawable.round_violet_bg));
                    mTimeLimitMonTxt.setTextColor( getResources().getColor(R.color.white));
                    isChangeMonBool = true;
                }else {
                    mTimeLimitMonLay.setBackground( getResources().getDrawable(R.drawable.round_white_violet_bg));
                    mTimeLimitMonTxt.setTextColor( getResources().getColor(R.color.violet));
                    isChangeMonBool = false;
                }
                break;
            case R.id.time_limit_tue_lay:
                if (!isChangeTueBool) {
                    mTimeLimitTueLay.setBackground( getResources().getDrawable(R.drawable.round_violet_bg));
                    mTimeLimitTueTxt.setTextColor( getResources().getColor(R.color.white));
                    isChangeTueBool = true;
                }else {
                    mTimeLimitTueLay.setBackground( getResources().getDrawable(R.drawable.round_white_violet_bg));
                    mTimeLimitTueTxt.setTextColor( getResources().getColor(R.color.violet));
                    isChangeTueBool = false;
                }
                break;
            case R.id.time_limit_wed_lay:
                if (!isChangeWedBool) {
                    mTimeLimitWedLay.setBackground( getResources().getDrawable(R.drawable.round_violet_bg));
                    mTimeLimitWedTxt.setTextColor( getResources().getColor(R.color.white));
                    isChangeWedBool = true;
                }else {
                    mTimeLimitWedLay.setBackground( getResources().getDrawable(R.drawable.round_white_violet_bg));
                    mTimeLimitWedTxt.setTextColor( getResources().getColor(R.color.violet));
                    isChangeWedBool = false;
                }
                break;
            case R.id.time_limit_thu_lay:
                if (!isChangeThuBool) {
                    mTimeLimitThuLay.setBackground( getResources().getDrawable(R.drawable.round_violet_bg));
                    mTimeLimitThuTxt.setTextColor( getResources().getColor(R.color.white));
                    isChangeThuBool = true;
                }else {
                    mTimeLimitThuLay.setBackground( getResources().getDrawable(R.drawable.round_white_violet_bg));
                    mTimeLimitThuTxt.setTextColor( getResources().getColor(R.color.violet));
                    isChangeThuBool = false;
                }
                break;
            case R.id.time_limit_fri_lay:
                if (!isChangeFriBool) {
                    mTimeLimitFriLay.setBackground( getResources().getDrawable(R.drawable.round_violet_bg));
                    mTimeLimitFriTxt.setTextColor( getResources().getColor(R.color.white));
                    isChangeFriBool = true;
                }else {
                    mTimeLimitFriLay.setBackground( getResources().getDrawable(R.drawable.round_white_violet_bg));
                    mTimeLimitFriTxt.setTextColor( getResources().getColor(R.color.violet));
                    isChangeFriBool = false;
                }
                break;
            case R.id.time_limit_sat_lay:
                if (!isChangeSatBool) {
                    mTimeLimitSatLay.setBackground( getResources().getDrawable(R.drawable.round_violet_bg));
                    mTimeLimitSatTxt.setTextColor( getResources().getColor(R.color.white));
                    isChangeSatBool = true;
                }else {
                    mTimeLimitSatLay.setBackground( getResources().getDrawable(R.drawable.round_white_violet_bg));
                    mTimeLimitSatTxt.setTextColor( getResources().getColor(R.color.violet));
                    isChangeSatBool = false;
                }
                break;
            case R.id.time_limit_sun_lay:
                if (!isChangeSunBool) {
                    mTimeLimitSunLay.setBackground( getResources().getDrawable(R.drawable.round_violet_bg));
                    mTimeLimitSunTxt.setTextColor( getResources().getColor(R.color.white));
                    isChangeSunBool = true;
                }else {
                    mTimeLimitSunLay.setBackground( getResources().getDrawable(R.drawable.round_white_violet_bg));
                    mTimeLimitSunTxt.setTextColor( getResources().getColor(R.color.violet));
                    isChangeSunBool = false;
                }
                break;

        }
    }

    private void setHeaderView() {
         /*Header*/
        mControlTimeLimitHeaderLay.setVisibility(IsScreenModePortrait() ? View.VISIBLE : View.GONE);
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderLeftImg.setImageResource(R.drawable.back_icon);
        mHeaderTxt.setText(getString(R.string.time_limit_header));

         /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mControlRewardHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt =  getResources().getDimensionPixelSize(IsScreenModePortrait() ? R.dimen.size190 : R.dimen.size45);
                    mControlRewardHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(ParentalControlTimeLimit.this)));
                    mControlRewardHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(ParentalControlTimeLimit.this), 0, 0);
                    mControlRewardHeaderBgLay.setBackground(IsScreenModePortrait() ? getResources().getDrawable(R.drawable.header_violet_bg) : getResources().getDrawable(R.color.violet));
                }

            });
        }
    }

    private void showDatePickerDialog() {
        final Calendar cal = Calendar.getInstance();
        mDate = cal.get(Calendar.DAY_OF_MONTH);
        mMonth = cal.get(Calendar.MONTH);
        mYear = cal.get(Calendar.YEAR);

        DatePickerDialog dpd = new DatePickerDialog(this, mDateSetListener,
                mYear, mMonth, mDate);
        dpd.getDatePicker().setMinDate(cal.getTimeInMillis());
        dpd.show();
    }
    private void showTimePickerDialog() {
        final Calendar cal = Calendar.getInstance();
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);
        new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, false)
                .show();
    }
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDate = dayOfMonth;
            final Calendar cal = Calendar.getInstance();
            String date = String.valueOf(mMonth + 1) + "-" +
                    mDate + "-" + mYear;
            SimpleDateFormat dateInputFormat = new SimpleDateFormat(mDateInput,
                    Locale.US);
            Date date1 = null;
            String str1 = null;

            try {
                date1 = dateInputFormat.parse(date);
                str1 = dateInputFormat.format(date1);
                mStartDate = str1;
            } catch (Exception e) {
                Log.e(AppConstants.TAG, e.toString());
            }
        }

    };

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
