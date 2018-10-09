package com.calix.calixgigaspireapp.ui.parentalcontrol;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.adapter.parentcontrol.ParentConInsightsAdapter;
import com.calix.calixgigaspireapp.main.BaseActivity;
import com.calix.calixgigaspireapp.output.model.InsightsEntity;
import com.calix.calixgigaspireapp.ui.dashboard.Alert;
import com.calix.calixgigaspireapp.ui.dashboard.Dashboard;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.DateUtil;
import com.calix.calixgigaspireapp.utils.DialogManager;
import com.calix.calixgigaspireapp.utils.InterfaceBtnCallback;
import com.calix.calixgigaspireapp.utils.NumberUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ParentControlInsightsCategory extends BaseActivity {

    @BindView(R.id.control_user_insights_lay)
    ViewGroup mParentControlLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.pc_header_bg_lay)
    RelativeLayout mPCHeaderBgLay;

    @BindView(R.id.start_txt)
    TextView mStartDateTxt;

    @BindView(R.id.end_txt)
    TextView mEndDateTxt;

    @BindView(R.id.user_insights_recycler_view)
    RecyclerView mUserInsidesRecyclerView;

    @BindView(R.id.internet_pause_img)
    ImageView mPausePlayImg;

    @BindView(R.id.notification_count_lay)
    RelativeLayout mNotificationCountLay;

    @BindView(R.id.header_right_img_lay)
    RelativeLayout mNotificationLay;

    @BindView(R.id.notification_count_txt)
    TextView mNotificationCountTxt;

    @BindView(R.id.notification_count_temp_txt)
    TextView mNotificationCountTempTxt;

    private boolean isStartDateBool = false;
    private int mDate, mMonth, mYear;
    private DatePickerDialog mDatePicker;

    private boolean mIsChangePlayPauseBtn = false;
    private boolean mPauseAllBool = false;


    private ArrayList<InsightsEntity> mInsidesListResponse = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_pc_insights_category);

        initView();
    }

    /*View initialization*/
    private void initView() {
        /*For error track purpose - log with class name*/
        AppConstants.TAG = this.getClass().getSimpleName();

        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mParentControlLay);
        mNotificationLay.setVisibility(View.VISIBLE);

        /*Notification function*/
        if (AppConstants.ALERT_COUNT > 0) {
            mNotificationCountLay.setVisibility(View.VISIBLE);
            mNotificationCountTxt.setText(String.valueOf(AppConstants.ALERT_COUNT));
            mNotificationCountTempTxt.setText(String.valueOf(AppConstants.ALERT_COUNT));
        } else {
            mNotificationCountLay.setVisibility(View.GONE);
        }

        /*header lay*/
        setHeaderView();

        /*UserInsights List Api*/
        //insightsListAPI();
        setAdapter(mInsidesListResponse);
    }

    private void setHeaderView() {
        /*Header*/
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.insights));

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mPCHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(R.dimen.size45);
                    mPCHeaderBgLay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(ParentControlInsightsCategory.this)));
                    mPCHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(ParentControlInsightsCategory.this), 0, 0);
                }
            });
        }
    }

    /*  Show data picker*/
    private void showDatePickerDialog(String dateStr) {

        String[] dateStrArr = dateStr.split(getString(R.string.hyphen_sym));

        mYear = Integer.valueOf(dateStrArr[2]);
        mMonth = (Integer.valueOf(dateStrArr[0]) - 1);
        mDate = Integer.valueOf(dateStrArr[1]);

        datePickerDialogDismiss(mDatePicker);

        mDatePicker = new DatePickerDialog(this, mDateSetListener,
                mYear, mMonth, mDate);

//        if ((isStartDateBool && AppConstants.GUEST_WIFI_DETAILS.getEventId().isEmpty()) || (isStartDateBool && AppConstants.GUEST_WIFI_DETAILS.isIndefinite())) {
//            mDatePicker.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
//        }
//
//        if (!isStartDateBool) {
//            mDatePicker.getDatePicker().setMinDate(DateUtil.getMileSecFromDate(mStartDateTxt.getText().toString().trim(), AppConstants.CUSTOM_DATE_FORMAT));
//        }

        mDatePicker.show();

    }

    /*Date picker listener */
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        /* date picker dialog box*/
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            mYear = year;
            mMonth = monthOfYear;
            mDate = dayOfMonth;

            String dateStr = String.valueOf(mMonth + 1) + getString(R.string.hyphen_sym) + mDate + getString(R.string.hyphen_sym) + mYear;
            Date selectedDate;
            String mDateStr = "";

            try {
                selectedDate = new SimpleDateFormat("MM-dd-yyyy", Locale.US).parse(dateStr);
                mDateStr = new SimpleDateFormat("MM-dd-yyyy", Locale.US).format(selectedDate);

            } catch (Exception e) {
                Log.e(AppConstants.TAG, e.getMessage());
            }


            if (isStartDateBool) {
                mStartDateTxt.setText(mDateStr);
            } else {
                if (DateUtil.getMileSecFromDate(mStartDateTxt.getText().toString().trim(), AppConstants.CUSTOM_DATE_FORMAT) > DateUtil.getMileSecFromDate(mDateStr, AppConstants.CUSTOM_DATE_FORMAT)) {
                    DialogManager.getInstance().showAlertPopup(ParentControlInsightsCategory.this, getString(R.string.select_feature_date), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {

                        }
                    });
                } else {
                    mEndDateTxt.setText(mDateStr);
                }
            }
        }


    };

    /*View onClick*/
    @OnClick({R.id.header_left_img, R.id.pc_dashboard_lay, R.id.internet_pause_img, R.id.start_txt, R.id.end_txt, R.id.header_right_img_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img:
                onBackPressed();
                break;
            case R.id.pc_dashboard_lay:
                nextScreen(Dashboard.class);
                break;
            case R.id.internet_pause_img:
                if (!mIsChangePlayPauseBtn) {
                    mPauseAllBool = true;
                    mPausePlayImg.setImageResource(R.drawable.play_icon);
                    mIsChangePlayPauseBtn = true;
                } else {
                    mPauseAllBool = true;
                    mPausePlayImg.setImageResource(R.drawable.pause_icon);
                    mIsChangePlayPauseBtn = false;
                }
                break;
            case R.id.start_txt:
                isStartDateBool = true;
                showDatePickerDialog(mStartDateTxt.getText().toString().trim());
                break;

            case R.id.end_txt:
                isStartDateBool = false;
                showDatePickerDialog(mEndDateTxt.getText().toString().trim());
                break;

            case R.id.header_right_img_lay:
                nextScreen(Alert.class);
        }
    }

    /*API call*/
    private void insightsListAPI() {
        //APIRequestHandler.getInstance().insightsAPICall(this);
    }

    private void setAdapter(ArrayList<InsightsEntity> userInsightsList) {
        mUserInsidesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mUserInsidesRecyclerView.setAdapter(new ParentConInsightsAdapter(userInsightsList, this));
        mUserInsidesRecyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void onBackPressed() {
        previousScreen(ParentControlInsights.class);
    }
}
