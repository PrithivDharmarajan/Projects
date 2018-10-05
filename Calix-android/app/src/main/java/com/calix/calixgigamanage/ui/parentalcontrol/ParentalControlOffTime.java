package com.calix.calixgigamanage.ui.parentalcontrol;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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


public class ParentalControlOffTime extends BaseActivity {

    @BindView(R.id.off_time_par_lay)
    RelativeLayout mParentControlOffTime;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.header_left_img)
    ImageView mHeaderLeftImg;

    @BindView(R.id.control_off_time_header_bg_lay)
    RelativeLayout mControlOffTimeHeaderBgLay;

    @BindView(R.id.control_off_time_header_msg_lay)
    RelativeLayout mControlOffTimeHeaderLay;

    @BindView(R.id.off_time_mon_lay)
    RelativeLayout mOffTimeMonLay;

    @BindView(R.id.off_time_tue_lay)
    RelativeLayout mOffTimeTueLay;

    @BindView(R.id.off_time_wed_lay)
    RelativeLayout mOffTimeWedLay;

    @BindView(R.id.off_time_thu_lay)
    RelativeLayout mOffTimeThuLay;

    @BindView(R.id.off_time_fri_lay)
    RelativeLayout mOffTimeFriLay;

    @BindView(R.id.off_time_sat_lay)
    RelativeLayout mOffTimeSatLay;

    @BindView(R.id.off_time_sun_lay)
    RelativeLayout mOffTimeSunLay;

    @BindView(R.id.off_time_mon_txt)
    TextView mOffTimeMonTxt;

    @BindView(R.id.off_time_tue_txt)
    TextView mOffTimeTueTxt;

    @BindView(R.id.off_time_wed_txt)
    TextView mOffTimeWedTxt;

    @BindView(R.id.off_time_thu_txt)
    TextView mOffTimeThuTxt;

    @BindView(R.id.off_time_fri_txt)
    TextView mOffTimeFriTxt;

    @BindView(R.id.off_time_sat_txt)
    TextView mOffTimeSatTxt;

    @BindView(R.id.off_time_sun_txt)
    TextView mOffTimeSunTxt;

    private boolean  isChangeMonBool = true , isChangeTueBool = false, isChangeWedBool = false , isChangeThuBool = false, isChangeFriBool = false, isChangeSatBool = true , isChangeSunBool = true;

    private int mDate, mMonth, mYear;
    private String mDateInput = "MM-dd-yyyy", mStartDate;
    private int mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_parental_control_off_time);
        initView();
    }

    private void initView() {
                    /*For error track purpose - log with class name*/
        AppConstants.TAG = this.getClass().getSimpleName();

       /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mParentControlOffTime);

        setHeaderView();


    }

    /*View onClick*/
    @OnClick({R.id.header_left_img_lay, R.id.off_time_calender_img, R.id.off_time_add_btn,R.id.off_time_cancel_btn, R.id.off_time_filter_enable_edit_img, R.id.off_time_filter_disable_edit_img,R.id.off_time_mon_lay,R.id.off_time_tue_lay,R.id.off_time_wed_lay,R.id.off_time_thu_lay,R.id.off_time_fri_lay,R.id.off_time_sat_lay,R.id.off_time_sun_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;
            case R.id.off_time_calender_img:
                showDatePickerDialog();
                break;
            case R.id.off_time_cancel_btn:
            case R.id.off_time_add_btn:
                onBackPressed();
                break;
            case R.id.off_time_filter_enable_edit_img:
            case R.id.off_time_filter_disable_edit_img:
                showTimePickerDialog();
                break;
            case R.id.off_time_mon_lay:
                if (!isChangeMonBool) {
                    mOffTimeMonLay.setBackground( getResources().getDrawable(R.drawable.round_violet_bg));
                    mOffTimeMonTxt.setTextColor( getResources().getColor(R.color.white));
                    isChangeMonBool = true;
                }else {
                    mOffTimeMonLay.setBackground( getResources().getDrawable(R.drawable.round_white_violet_bg));
                    mOffTimeMonTxt.setTextColor( getResources().getColor(R.color.violet));
                    isChangeMonBool = false;
                }
                break;
            case R.id.off_time_tue_lay:
                if (!isChangeTueBool) {
                    mOffTimeTueLay.setBackground( getResources().getDrawable(R.drawable.round_violet_bg));
                    mOffTimeTueTxt.setTextColor( getResources().getColor(R.color.white));
                    isChangeTueBool = true;
                }else {
                    mOffTimeTueLay.setBackground( getResources().getDrawable(R.drawable.round_white_violet_bg));
                    mOffTimeTueTxt.setTextColor( getResources().getColor(R.color.violet));
                    isChangeTueBool = false;
                }
                break;
            case R.id.off_time_wed_lay:
                if (!isChangeWedBool) {
                    mOffTimeWedLay.setBackground( getResources().getDrawable(R.drawable.round_violet_bg));
                    mOffTimeWedTxt.setTextColor( getResources().getColor(R.color.white));
                    isChangeWedBool = true;
                }else {
                    mOffTimeWedLay.setBackground( getResources().getDrawable(R.drawable.round_white_violet_bg));
                    mOffTimeWedTxt.setTextColor( getResources().getColor(R.color.violet));
                    isChangeWedBool = false;
                }
                break;
            case R.id.off_time_thu_lay:
                if (!isChangeThuBool) {
                    mOffTimeThuLay.setBackground( getResources().getDrawable(R.drawable.round_violet_bg));
                    mOffTimeThuTxt.setTextColor( getResources().getColor(R.color.white));
                    isChangeThuBool = true;
                }else {
                    mOffTimeThuLay.setBackground( getResources().getDrawable(R.drawable.round_white_violet_bg));
                    mOffTimeThuTxt.setTextColor( getResources().getColor(R.color.violet));
                    isChangeThuBool = false;
                }
                break;
            case R.id.off_time_fri_lay:
                if (!isChangeFriBool) {
                    mOffTimeFriLay.setBackground( getResources().getDrawable(R.drawable.round_violet_bg));
                    mOffTimeFriTxt.setTextColor( getResources().getColor(R.color.white));
                    isChangeFriBool = true;
                }else {
                    mOffTimeFriLay.setBackground( getResources().getDrawable(R.drawable.round_white_violet_bg));
                    mOffTimeFriTxt.setTextColor( getResources().getColor(R.color.violet));
                    isChangeFriBool = false;
                }
                break;
            case R.id.off_time_sat_lay:
                if (!isChangeSatBool) {
                    mOffTimeSatLay.setBackground( getResources().getDrawable(R.drawable.round_violet_bg));
                    mOffTimeSatTxt.setTextColor( getResources().getColor(R.color.white));
                    isChangeSatBool = true;
                }else {
                    mOffTimeSatLay.setBackground( getResources().getDrawable(R.drawable.round_white_violet_bg));
                    mOffTimeSatTxt.setTextColor( getResources().getColor(R.color.violet));
                    isChangeSatBool = false;
                }
                break;
            case R.id.off_time_sun_lay:
                if (!isChangeSunBool) {
                    mOffTimeSunLay.setBackground( getResources().getDrawable(R.drawable.round_violet_bg));
                    mOffTimeSunTxt.setTextColor( getResources().getColor(R.color.white));
                    isChangeSunBool = true;
                }else {
                    mOffTimeSunLay.setBackground( getResources().getDrawable(R.drawable.round_white_violet_bg));
                    mOffTimeSunTxt.setTextColor( getResources().getColor(R.color.violet));
                    isChangeSunBool = false;
                }
                break;

        }
    }

    private void setHeaderView() {
         /*Header*/
        mControlOffTimeHeaderLay.setVisibility(IsScreenModePortrait() ? View.VISIBLE : View.GONE);
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderLeftImg.setImageResource(R.drawable.back_icon);
        mHeaderTxt.setText(getString(R.string.off_time_for_samantha));

         /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mControlOffTimeHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt =  getResources().getDimensionPixelSize(IsScreenModePortrait() ? R.dimen.size190 : R.dimen.size45);
                    mControlOffTimeHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(ParentalControlOffTime.this)));
                    mControlOffTimeHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(ParentalControlOffTime.this), 0, 0);
                    mControlOffTimeHeaderBgLay.setBackground(IsScreenModePortrait() ? getResources().getDrawable(R.drawable.header_violet_bg) : getResources().getDrawable(R.color.violet));
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
    private void showTimePickerDialog() {
        final Calendar cal = Calendar.getInstance();
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);
        new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, false)
                .show();
    } private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

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
