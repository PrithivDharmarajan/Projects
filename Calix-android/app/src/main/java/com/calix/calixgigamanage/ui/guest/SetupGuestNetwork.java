package com.calix.calixgigamanage.ui.guest;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.output.model.CommonResponse;
import com.calix.calixgigamanage.output.model.DurationEntity;
import com.calix.calixgigamanage.output.model.EncryptionTypeEntity;
import com.calix.calixgigamanage.output.model.EncryptionTypeResponse;
import com.calix.calixgigamanage.output.model.GuestWifiEntity;
import com.calix.calixgigamanage.services.APIRequestHandler;
import com.calix.calixgigamanage.utils.AppConstants;
import com.calix.calixgigamanage.utils.DateUtil;
import com.calix.calixgigamanage.utils.DialogManager;
import com.calix.calixgigamanage.utils.InterfaceBtnCallback;
import com.calix.calixgigamanage.utils.InterfaceTwoBtnCallback;
import com.calix.calixgigamanage.utils.NumberUtil;

import net.glxn.qrgen.android.QRCode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class SetupGuestNetwork extends BaseActivity {

    @BindView(R.id.setup_guest_par_lay)
    RelativeLayout mSetupGuestParLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.setup_guest_header_bg_lay)
    RelativeLayout mSetupGuestHeaderBgLay;

    @BindView(R.id.indefinite_switch_compat)
    SwitchCompat mIndefiniteSwitchCompat;

    @BindView(R.id.setup_guest_indefinite_network_lay)
    CardView mSetupGuestIndefiniteNetworkLay;

    @BindView(R.id.search_name_edit_txt)
    EditText mEventNameEdtTxt;

    @BindView(R.id.guest_network_name_edt)
    EditText mGuestNetworkNameEdt;

    @BindView(R.id.pwd_confirm_pwd_lay)
    LinearLayout mPwdConfirmPwdLay;

    @BindView(R.id.guest_network_pwd_edt)
    EditText mGuestNetworkPwdEdt;

    @BindView(R.id.guest_network_confirm_pwd_edt)
    EditText mGuestNetworkConfirmPwdEdt;


    @BindView(R.id.start_date_txt)
    TextView mStartDateTxt;

    @BindView(R.id.end_date_txt)
    TextView mEndDateTxt;

    @BindView(R.id.start_time_txt)
    TextView mStartTimeTxt;

    @BindView(R.id.end_time_txt)
    TextView mEndTimeTxt;

    @BindView(R.id.delete_btn)
    Button mDeleteBtn;

    @BindView(R.id.auth_type_spinner)
    Spinner mAuthTypeSpinner;

    private LinkedHashMap<String, Integer> mEncryptionTypeHasMap = new LinkedHashMap<>();
    private ArrayList<String> mEncryptionTypStrArrList = new ArrayList<>();
    private int mSelectedTypeInt = 0, mEncryptionTypeInt = 0;
    private boolean isSelectedEncryptionBool = false;

    private int mDate, mMonth, mYear;
    private int mHour, mMinute;
    private DatePickerDialog mDatePicker;
    private TimePickerDialog mTimePicker;

    private boolean isStartDateBool = false;
    private boolean isStartTimeBool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_setup_guest_network);
        initView();
    }

    /*View initialization*/
    private void initView() {
        /*For error track purpose - log with class name*/
        AppConstants.TAG = this.getClass().getSimpleName();

        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mSetupGuestParLay);

        setHeaderView();

        setData();
    }


    private void setHeaderView() {

        /*Header*/
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.setup_guest_wifi));

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mSetupGuestHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(R.dimen.size45);
                    mSetupGuestHeaderBgLay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(SetupGuestNetwork.this)));
                    mSetupGuestHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(SetupGuestNetwork.this), 0, 0);
                }

            });
        }
    }

    private void encryptionTypeAPICall() {
        APIRequestHandler.getInstance().encryptionTypeAPICall(this);
    }

    /*Show data picker*/
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
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog
            .OnDateSetListener() {

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
//                if (DateUtil.getMileSecFromDate(mDateStr, AppConstants.CUSTOM_DATE_FORMAT) > DateUtil.getMileSecFromDate(mEndDateTxt.getText().toString().trim(), AppConstants.CUSTOM_DATE_FORMAT)) {
//                    mEndDateTxt.setText(mDateStr);
//                }
//                if ((DateUtil.getMileSecFromDate(mStartDateTxt.getText().toString().trim(), AppConstants.CUSTOM_DATE_FORMAT).equals(DateUtil.getMileSecFromDate(mEndDateTxt.getText().toString().trim(), AppConstants.CUSTOM_DATE_FORMAT)))
//                        && (DateUtil.getMileSecFromDate(mStartTimeTxt.getText().toString().trim(), AppConstants.CUSTOM_12_HRS_TIME_FORMAT) > DateUtil.getMileSecFromDate(mEndTimeTxt.getText().toString().trim(), AppConstants.CUSTOM_12_HRS_TIME_FORMAT))) {
//                    mEndTimeTxt.setText(mStartTimeTxt.getText().toString().trim());
//                }
            } else {
//                if (DateUtil.getMileSecFromDate(mStartDateTxt.getText().toString().trim(), AppConstants.CUSTOM_DATE_FORMAT) > DateUtil.getMileSecFromDate(mDateStr, AppConstants.CUSTOM_DATE_FORMAT)) {
//                    DialogManager.getInstance().showToast(SetupGuestNetwork.this, getString(R.string.select_feature_date));
//                } else {
                mEndDateTxt.setText(mDateStr);
//                    if ((DateUtil.getMileSecFromDate(mStartDateTxt.getText().toString().trim(), AppConstants.CUSTOM_DATE_FORMAT).equals(DateUtil.getMileSecFromDate(mEndDateTxt.getText().toString().trim(), AppConstants.CUSTOM_DATE_FORMAT)))
//                            && (DateUtil.getMileSecFromDate(mStartTimeTxt.getText().toString().trim(), AppConstants.CUSTOM_12_HRS_TIME_FORMAT) > DateUtil.getMileSecFromDate(mEndTimeTxt.getText().toString().trim(), AppConstants.CUSTOM_12_HRS_TIME_FORMAT))) {
//                        mEndTimeTxt.setText(mStartTimeTxt.getText().toString().trim());
//                    }
//                }
            }
        }


    };

    private void showTimePickerDialog(String timeStr) {

        String timeStrArr[] = DateUtil.getCustomDateFormat(timeStr, AppConstants.CUSTOM_DATE_TIME_FORMAT, AppConstants.CUSTOM_24_HRS_TIME_FORMAT).split(getString(R.string.colon_sym));

        mHour = Integer.valueOf(timeStrArr[0]);
        mMinute = Integer.valueOf(timeStrArr[1]);

        timePickerDialogDismiss(mTimePicker);

        mTimePicker = new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, false);

        mTimePicker.show();
    }

    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mHour = hourOfDay;
            mMinute = minute;
            String timeStr = String.valueOf(mHour) + ":" + mMinute;

            Date selectedTime;
            String returnStr = "";

            try {
                selectedTime = new SimpleDateFormat("HH:mm", Locale.US).parse(timeStr);
                returnStr = new SimpleDateFormat("hh:mm aa", Locale.US).format(selectedTime);

            } catch (Exception e) {
                Log.e(AppConstants.TAG, e.getMessage());
            }


            if (isStartTimeBool) {
                mStartTimeTxt.setText(returnStr);
//                if ((DateUtil.getMileSecFromDate(mStartDateTxt.getText().toString().trim(), AppConstants.CUSTOM_DATE_FORMAT).equals(DateUtil.getMileSecFromDate(mEndDateTxt.getText().toString().trim(), AppConstants.CUSTOM_DATE_FORMAT)))
//                        && (DateUtil.getMileSecFromDate(returnStr, AppConstants.CUSTOM_12_HRS_TIME_FORMAT) > DateUtil.getMileSecFromDate(mEndTimeTxt.getText().toString().trim(), AppConstants.CUSTOM_12_HRS_TIME_FORMAT))) {
//                    mEndTimeTxt.setText(returnStr);
//                }
            } else {
//                if ((DateUtil.getMileSecFromDate(mStartDateTxt.getText().toString().trim(), AppConstants.CUSTOM_DATE_FORMAT).equals(DateUtil.getMileSecFromDate(mEndDateTxt.getText().toString().trim(), AppConstants.CUSTOM_DATE_FORMAT)))
//                        && (DateUtil.getMileSecFromDate(mStartTimeTxt.getText().toString().trim(), AppConstants.CUSTOM_12_HRS_TIME_FORMAT) > DateUtil.getMileSecFromDate(returnStr, AppConstants.CUSTOM_12_HRS_TIME_FORMAT))) {
//                    DialogManager.getInstance().showToast(SetupGuestNetwork.this, getString(R.string.select_feature_time));
//                } else {
                mEndTimeTxt.setText(returnStr);
//                }
            }
        }

    };


    /*View onClick*/
    @OnClick({R.id.header_left_img_lay,  R.id.start_date_txt, R.id.end_date_txt, R.id.start_time_txt, R.id.end_time_txt, R.id.spinner_drop_down_img, R.id.cont_btn, R.id.delete_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;
            case R.id.start_date_txt:
                isStartDateBool = true;
                showDatePickerDialog(mStartDateTxt.getText().toString().trim());
                break;
            case R.id.end_date_txt:
                isStartDateBool = false;
                showDatePickerDialog(mEndDateTxt.getText().toString().trim());
                break;
            case R.id.start_time_txt:
                isStartTimeBool = true;
                showTimePickerDialog(mStartDateTxt.getText().toString().trim() + " " + mStartTimeTxt.getText().toString().trim());

                break;
            case R.id.end_time_txt:
                isStartTimeBool = false;
                showTimePickerDialog(mEndDateTxt.getText().toString().trim() + " " + mEndTimeTxt.getText().toString().trim());
                break;
            case R.id.spinner_drop_down_img:
                mAuthTypeSpinner.performClick();
                break;
            case R.id.cont_btn:
                validateFields();
                break;
            case R.id.delete_btn:
                DialogManager.getInstance().showOptionPopup(this, getString(R.string.delete_msg), getString(R.string.yes), getString(R.string.no), new InterfaceTwoBtnCallback() {
                    @Override
                    public void onPositiveClick() {
                        GuestWifiEntity guestWifiEntityInputModel = new GuestWifiEntity();
                        guestWifiEntityInputModel.setEventId(AppConstants.GUEST_WIFI_DETAILS.getEventId());
                        APIRequestHandler.getInstance().deleteGuestNetworkAPICall(AppConstants.GUEST_WIFI_DETAILS.getEventId(), SetupGuestNetwork.this);
                    }

                    @Override
                    public void onNegativeClick() {

                    }
                });

                break;

        }
    }

    /*View onCheckedChanged*/
    @OnCheckedChanged({R.id.indefinite_switch_compat})
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.indefinite_switch_compat:
                mSetupGuestIndefiniteNetworkLay.setVisibility(isChecked ? View.GONE : View.VISIBLE);
                break;

        }
    }

    /*Set adapter*/
    private void setData() {
        GuestWifiEntity guestWifiDetails = AppConstants.GUEST_WIFI_DETAILS;
        mSelectedTypeInt = guestWifiDetails.getEncryptType();
        boolean isNewGuestBool = guestWifiDetails.getEventId().isEmpty();
        mEventNameEdtTxt.setText(guestWifiDetails.getEventName());
//        mEventNameEdtTxt.setEnabled(isNewGuestBool);

        mIndefiniteSwitchCompat.setChecked(guestWifiDetails.isIndefinite());
        mSetupGuestIndefiniteNetworkLay.setVisibility(guestWifiDetails.isIndefinite() ? View.GONE : View.VISIBLE);

        mStartDateTxt.setText(DateUtil.getCustomDateAndTimeFormat(guestWifiDetails.getDuration().getStartTime(), AppConstants.CUSTOM_DATE_FORMAT));
        mEndDateTxt.setText(DateUtil.getCustomDateAndTimeFormat(guestWifiDetails.getDuration().getEndTime(), AppConstants.CUSTOM_DATE_FORMAT));
        mStartTimeTxt.setText(DateUtil.getCustomDateAndTimeFormat(guestWifiDetails.getDuration().getStartTime(), AppConstants.CUSTOM_12_HRS_TIME_FORMAT));
        mEndTimeTxt.setText(DateUtil.getCustomDateAndTimeFormat(guestWifiDetails.getDuration().getEndTime(), AppConstants.CUSTOM_12_HRS_TIME_FORMAT));

        mGuestNetworkNameEdt.setText(guestWifiDetails.getGuestWifiName());
        mGuestNetworkPwdEdt.setText(guestWifiDetails.getGuestWifiPassword());
        mDeleteBtn.setVisibility(isNewGuestBool ? View.GONE : View.VISIBLE);

        encryptionTypeAPICall();
    }

    /*validate fields*/
    private void validateFields() {
        hideSoftKeyboard(this);
        String eventNameStr = mEventNameEdtTxt.getText().toString().trim();
        String guestNetworkNameStr = mGuestNetworkNameEdt.getText().toString().trim();
        String guestNetworkPwdStr = mGuestNetworkPwdEdt.getText().toString().trim();
        String guestNetworkConfirmPwdStr = mGuestNetworkConfirmPwdEdt.getText().toString().trim();


        sysOut("ss---" + DateUtil.getMileSecFromDate(mStartDateTxt.getText().toString().trim() + " " + mStartTimeTxt.getText().toString().trim(), AppConstants.CUSTOM_DATE_TIME_FORMAT) + " " + (System.currentTimeMillis() - 120000) + " " +
                DateUtil.getMileSecFromDate(mStartDateTxt.getText().toString().trim() + " " + mStartTimeTxt.getText().toString().trim(), AppConstants.CUSTOM_DATE_TIME_FORMAT).compareTo(System.currentTimeMillis() - 120000));
        if (eventNameStr.isEmpty()) {
            mEventNameEdtTxt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.enter_event_name), this);
        } else if (guestNetworkNameStr.isEmpty()) {
            mGuestNetworkNameEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.enter_guest_network_name), this);
        } else if (mPwdConfirmPwdLay.getVisibility() == View.VISIBLE && guestNetworkPwdStr.length() < 8) {
            mGuestNetworkPwdEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.pass_contains_eight_char), this);
        } else if (mPwdConfirmPwdLay.getVisibility() == View.VISIBLE && !guestNetworkPwdStr.equals(guestNetworkConfirmPwdStr)) {
            mGuestNetworkConfirmPwdEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.pwd_does_not_match), this);
        } else if (!mIndefiniteSwitchCompat.isChecked() && AppConstants.GUEST_WIFI_DETAILS.getEventId().isEmpty() && DateUtil.getMileSecFromDate(mStartDateTxt.getText().toString().trim(), AppConstants.CUSTOM_DATE_FORMAT) < DateUtil.getMileSecFromDate(AppConstants.CUSTOM_DATE_FORMAT.format(new Date(System.currentTimeMillis())), AppConstants.CUSTOM_DATE_FORMAT)) {
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.select_feature_date_time), this);
        } else if (!mIndefiniteSwitchCompat.isChecked() && DateUtil.getMileSecFromDate(mEndDateTxt.getText().toString().trim() + " " + mEndTimeTxt.getText().toString().trim(), AppConstants.CUSTOM_DATE_TIME_FORMAT) < System.currentTimeMillis() - 120000) {
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.select_feature_date_time), this);
        } else if (!mIndefiniteSwitchCompat.isChecked() && DateUtil.getMileSecFromDate(mStartDateTxt.getText().toString().trim() + " " + mStartTimeTxt.getText().toString().trim(), AppConstants.CUSTOM_DATE_TIME_FORMAT) >
                DateUtil.getMileSecFromDate(mEndDateTxt.getText().toString().trim() + " " + mEndTimeTxt.getText().toString().trim(), AppConstants.CUSTOM_DATE_TIME_FORMAT)) {
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.select_valid_date_time), this);
        } else {
            /*API calls*/
            GuestWifiEntity guestWifiInputModel = new GuestWifiEntity();
            DurationEntity durationWifiInputModel = new DurationEntity();
            guestNetworkPwdStr = mPwdConfirmPwdLay.getVisibility() == View.VISIBLE ? guestNetworkPwdStr : "";

            guestWifiInputModel.setEventId(AppConstants.GUEST_WIFI_DETAILS.getEventId());
            guestWifiInputModel.setEventName(eventNameStr);
            guestWifiInputModel.setGuestWifiName(guestNetworkNameStr);
            guestWifiInputModel.setGuestWifiPassword(guestNetworkPwdStr);
            guestWifiInputModel.setEncryptionType(String.valueOf(mEncryptionTypeInt));
            guestWifiInputModel.setIndefinite(mIndefiniteSwitchCompat.isChecked());

            durationWifiInputModel.setStartTime(DateUtil.getMileSecFromDate(mStartDateTxt.getText().toString().trim() + " " + mStartTimeTxt.getText().toString().trim(), AppConstants.CUSTOM_DATE_TIME_FORMAT));
            durationWifiInputModel.setEndTime(DateUtil.getMileSecFromDate(mEndDateTxt.getText().toString().trim() + " " + mEndTimeTxt.getText().toString().trim(), AppConstants.CUSTOM_DATE_TIME_FORMAT));
            guestWifiInputModel.setDuration(durationWifiInputModel);

            AppConstants.GUEST_WIFI_DETAILS = guestWifiInputModel;
            if (guestWifiInputModel.getEventId().isEmpty()) {
                APIRequestHandler.getInstance().addGuestNetworkAPICall(guestWifiInputModel, this);
            } else {
                APIRequestHandler.getInstance().updateGuestNetworkAPICall(guestWifiInputModel, this);
            }

        }
    }


    /*API request success and failure*/
    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof GuestWifiEntity) {
            String wifiContentStr = String.format(AppConstants.QR_CODE_GENERATOR, AppConstants.GUEST_WIFI_DETAILS.getGuestWifiName(), AppConstants.GUEST_WIFI_DETAILS.getGuestWifiPassword().isEmpty() ? getString(R.string.none) : getString(R.string.wpa),
                    AppConstants.GUEST_WIFI_DETAILS.getGuestWifiPassword());
            AppConstants.GUEST_WIFI_DETAILS.setQrImage(QRCode.from(wifiContentStr).bitmap());
            DialogManager.getInstance().showToast(getApplicationContext(), getString(AppConstants.GUEST_WIFI_DETAILS.getEventId().isEmpty() ? R.string.guest_network_created : R.string.guest_network_updated));
            nextScreen(SetupGuestContact.class);
        } else if (resObj instanceof CommonResponse) {
            onBackPressed();
        } else if (resObj instanceof EncryptionTypeResponse) {
            EncryptionTypeResponse routerSetupResponse = (EncryptionTypeResponse) resObj;
            ArrayList<EncryptionTypeEntity> encryptionTypeEntity = routerSetupResponse.getTypes();

            mEncryptionTypeHasMap = new LinkedHashMap<>();
            mEncryptionTypStrArrList = new ArrayList<>();

            for (int i = 0; i < encryptionTypeEntity.size(); i++) {
                mEncryptionTypeHasMap.put(encryptionTypeEntity.get(i).getDescription(), encryptionTypeEntity.get(i).getValue());
                mEncryptionTypStrArrList.add(encryptionTypeEntity.get(i).getDescription());

                if (mSelectedTypeInt == encryptionTypeEntity.get(i).getValue()) {
                    isSelectedEncryptionBool = true;
                    mEncryptionTypeInt = encryptionTypeEntity.get(i).getValue();
                }
            }

            if (!isSelectedEncryptionBool && mEncryptionTypStrArrList.size() > 0) {
                mSelectedTypeInt = 0;
                mEncryptionTypeInt = mEncryptionTypeHasMap.get(mEncryptionTypStrArrList.get(0));
            }

            setSpinnerData();
        }
    }

    @Override
    public void onRequestFailure(Object inputModelObj, Throwable t) {
        super.onRequestFailure(inputModelObj, t);
        if (t instanceof IOException) {
            DialogManager.getInstance().showNetworkErrorPopup(this,
                    (t instanceof java.net.ConnectException ? getString(R.string.no_internet) : getString(R.string
                            .connect_time_out)), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {
                            validateFields();
                        }
                    });
        }
    }

    private void setSpinnerData() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(SetupGuestNetwork.this, R.layout.adap_spinner_equ_selected, mEncryptionTypStrArrList);
        adapter.setDropDownViewResource(R.layout.adap_spinner_equ_list);
        mAuthTypeSpinner.setAdapter(adapter);


        //Model Spinner item click
        mAuthTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mEncryptionTypeInt = mEncryptionTypeHasMap.get(mEncryptionTypStrArrList.get(position));
                mPwdConfirmPwdLay.setVisibility(mEncryptionTypStrArrList.get(position).equalsIgnoreCase(getString(R.string.none)) ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (mEncryptionTypStrArrList.size() > 0) {
            mAuthTypeSpinner.setSelection(mSelectedTypeInt, true);
            mPwdConfirmPwdLay.setVisibility(mEncryptionTypStrArrList.get(mSelectedTypeInt).equalsIgnoreCase(getString(R.string.none)) ? View.GONE : View.VISIBLE);
        }
    }


    @Override
    public void onBackPressed() {
        backScreen();
    }
}
