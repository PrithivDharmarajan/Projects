package com.calix.calixgigamanage.ui.parentalcontrol;

import android.app.DatePickerDialog;
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

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.utils.AppConstants;
import com.calix.calixgigamanage.utils.NumberUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ParentalControlReward extends BaseActivity {

    @BindView(R.id.control_reward_par_lay)
    RelativeLayout mParentControlRewardLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.header_left_img)
    ImageView mHeaderLeftImg;

    @BindView(R.id.control_reward_header_bg_lay)
    RelativeLayout mControlRewardHeaderBgLay;

    @BindView(R.id.control_insight_header_msg_lay)
    RelativeLayout mControlInsightHeaderLay;

    private int mDate, mMonth, mYear;
    private String mDateInput = "MM-dd-yyyy", mStartDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_parental_control_reward);
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


    }

    /*View onClick*/
    @OnClick({R.id.header_left_img_lay, R.id.reward_calender_img, R.id.reward_dont_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
            case R.id.reward_dont_lay:
                onBackPressed();
                break;
            case R.id.reward_calender_img:
                showDatePickerDialog();
                break;

        }
    }

    private void setHeaderView() {
         /*Header*/
        mControlInsightHeaderLay.setVisibility(IsScreenModePortrait() ? View.VISIBLE : View.GONE);
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderLeftImg.setImageResource(R.drawable.back_icon);
        mHeaderTxt.setText(getString(R.string.reward_for_samantha));

         /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mControlRewardHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt =  getResources().getDimensionPixelSize(IsScreenModePortrait() ? R.dimen.size190 : R.dimen.size45);
                    mControlRewardHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(ParentalControlReward.this)));
                    mControlRewardHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(ParentalControlReward.this), 0, 0);
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
