package com.calix.calixgigaspireapp.utils;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.adapter.AgentListAdapter;
import com.calix.calixgigaspireapp.adapter.PopUpListAdapter;
import com.calix.calixgigaspireapp.output.model.AlexaAppIdEntity;
import com.calix.calixgigaspireapp.output.model.InsightsEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class DialogManager {

    /*Init variable*/
    private Dialog mProgressDialog, mNetworkErrorDialog, mAlertDialog, mLogoutDialog, mOptionDialog, mForgotPwdDialog, mEdtDeviceNameDialog, mAddIOTAlertDialog, mEdtDeviceNameLocDialog, mIOTReadyToUseAlertDialog, mDevelopmentDialog, mAgentDialog;
    private Toast mToast;
    private boolean isStartTimeBool = false;
    private int mHour, mMinute;
    private TimePickerDialog mTimePicker;
    private TextView startTimeTxt, endTimeTxt;
    /*private String returnStr = "";*/

    /*Init dialog instance*/
    private static final DialogManager sDialogInstance = new DialogManager();


    public static DialogManager getInstance() {
        return sDialogInstance;
    }

    /*Default dialog init method*/
    private Dialog getDialog(Context context, int layout) {

        Dialog mCommonDialog;
        mCommonDialog = new Dialog(context);
        mCommonDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (mCommonDialog.getWindow() != null) {
            mCommonDialog.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            mCommonDialog.setContentView(layout);
            mCommonDialog.getWindow().setGravity(Gravity.CENTER);
            mCommonDialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));
        }
        mCommonDialog.setCancelable(false);
        mCommonDialog.setCanceledOnTouchOutside(false);

        return mCommonDialog;
    }

    /*Show progress popup*/
    public void showProgress(Context context) {

        /*To check if the progressbar is shown, if the progressbar is shown it will be cancelled */
        hideProgress();

        /*Init progress dialog*/
        mProgressDialog = new Dialog(context);
        mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (mProgressDialog.getWindow() != null) {
            mProgressDialog.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            mProgressDialog.setContentView(R.layout.popup_progress);
            mProgressDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            mProgressDialog.getWindow().setGravity(Gravity.CENTER);
            mProgressDialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));
        }
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);

        /*To check if the dialog is null or not. if it'border_with_transparent_bg not a null, the dialog will be shown orelse it will not get appeared*/
        try {
            if (mProgressDialog != null) {
                mProgressDialog.show();
            }
        } catch (Exception e) {
            Log.e(AppConstants.TAG, e.getMessage());
        }
    }

    /*Hide progress*/
    public void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            try {
                mProgressDialog.dismiss();
            } catch (Exception e) {
                Log.e(AppConstants.TAG, e.getMessage());
            }
        }
    }

    /*Show custom toast*/
    public void showToast(Context context, String message) {

        try {
            /*To check if the toast is projected, if projected it will be cancelled */
            if (mToast != null && mToast.getView().isShown()) {
                mToast.cancel();
            }

            mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            TextView toastTxt = mToast.getView().findViewById(
                    android.R.id.message);
            toastTxt.setTypeface(Typeface.SANS_SERIF);
            mToast.show();


        } catch (Exception e) {
            Log.e(AppConstants.TAG, e.toString());
        }
    }

    /*Show the single button Alert popup*/
    public void showAlertPopup(Context context, String messageStr, final InterfaceBtnCallback dialogAlertInterface) {

        alertDismiss(mAlertDialog);
        mAlertDialog = getDialog(context, R.layout.popup_basic_alert);

        WindowManager.LayoutParams LayoutParams = new WindowManager.LayoutParams();
        Window window = mAlertDialog.getWindow();

        if (window != null) {
            LayoutParams.copyFrom(window.getAttributes());
            LayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            LayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(LayoutParams);
            window.setGravity(Gravity.CENTER);
        }

        TextView alertMsgTxt;
        Button alertPositiveBtn;

        /*Init view*/
        alertMsgTxt = mAlertDialog.findViewById(R.id.alert_msg_txt);
        alertPositiveBtn = mAlertDialog.findViewById(R.id.alert_positive_btn);

        /*Set data*/
        alertMsgTxt.setText(messageStr);
        alertPositiveBtn.setText(context.getString(R.string.ok));

        //Check and set button visibility
        alertPositiveBtn.setVisibility(View.VISIBLE);

        alertPositiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
                dialogAlertInterface.onPositiveClick();
            }
        });

        alertShowing(mAlertDialog);

    }


    public void showDevelopmentPopup(final Context context,
                                     final InterfaceTwoBtnCallback dialogAlertInterface) {
        alertDismiss(mDevelopmentDialog);
        mDevelopmentDialog = getDialog(context, R.layout.popup_developer_alert);

        WindowManager.LayoutParams LayoutParams = new WindowManager.LayoutParams();
        Window window = mDevelopmentDialog.getWindow();

        if (window != null) {
            LayoutParams.copyFrom(window.getAttributes());
            LayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            LayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(LayoutParams);
            window.setGravity(Gravity.CENTER);
        }

        final RadioButton developmentRadioBtn, productionRadioBtn, stageRadioBtn;
        Button alertPositiveBtn, alertNegativeBtn;

        /*Init view*/
        developmentRadioBtn = mDevelopmentDialog.findViewById(R.id.development_radio_btn);
        stageRadioBtn = mDevelopmentDialog.findViewById(R.id.stage_radio_btn);
        productionRadioBtn = mDevelopmentDialog.findViewById(R.id.production_radio_btn);

        alertPositiveBtn = mDevelopmentDialog.findViewById(R.id.alert_positive_btn);
        alertNegativeBtn = mDevelopmentDialog.findViewById(R.id.alert_negative_btn);


        final String alreadySelectedUrlStr = PreferenceUtil.getStringValue(context, AppConstants.USER_SELECTED_BASE_URL);
        final String[] selectedUrlStr = {PreferenceUtil.getStringValue(context, AppConstants.USER_SELECTED_BASE_URL)};
        developmentRadioBtn.setChecked(alreadySelectedUrlStr.isEmpty() || alreadySelectedUrlStr.equalsIgnoreCase(AppConstants.DEV_BASE_URL));
        productionRadioBtn.setChecked(alreadySelectedUrlStr.equalsIgnoreCase(AppConstants.PRODUCT_BASE_URL));
        stageRadioBtn.setChecked(alreadySelectedUrlStr.equalsIgnoreCase(AppConstants.STAGE_BASE_URL));

        developmentRadioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    developmentRadioBtn.setChecked(true);
                    productionRadioBtn.setChecked(false);
                    stageRadioBtn.setChecked(false);
                    selectedUrlStr[0] = AppConstants.DEV_BASE_URL;
                }
            }
        });

        stageRadioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    developmentRadioBtn.setChecked(false);
                    productionRadioBtn.setChecked(false);
                    stageRadioBtn.setChecked(true);
                    selectedUrlStr[0] = AppConstants.STAGE_BASE_URL;
                }
            }
        });
        productionRadioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    developmentRadioBtn.setChecked(false);
                    productionRadioBtn.setChecked(true);
                    stageRadioBtn.setChecked(false);
                    selectedUrlStr[0] = AppConstants.PRODUCT_BASE_URL;
                }
            }
        });

        alertPositiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedUrlStr[0].isEmpty() || alreadySelectedUrlStr.equalsIgnoreCase(selectedUrlStr[0])) {
                    mDevelopmentDialog.dismiss();
                    dialogAlertInterface.onNegativeClick();
                } else {
                    showLogoutPopup(context, new InterfaceTwoBtnCallback() {
                        @Override
                        public void onNegativeClick() {
                            mDevelopmentDialog.dismiss();
                            dialogAlertInterface.onNegativeClick();
                        }

                        @Override
                        public void onPositiveClick() {
                            PreferenceUtil.storeStringValue(context, AppConstants.USER_SELECTED_BASE_URL, selectedUrlStr[0]);
                            mDevelopmentDialog.dismiss();
                            dialogAlertInterface.onPositiveClick();
                        }
                    });
                }
            }
        });
        alertNegativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDevelopmentDialog.dismiss();
                dialogAlertInterface.onNegativeClick();
            }
        });

        alertShowing(mDevelopmentDialog);

    }

    public Dialog showAgentListPopup(final Context context, ArrayList<AlexaAppIdEntity> agentListArrList,
                                     final InterfaceEdtBtnCallback dialogAlertInterface) {
        alertDismiss(mAgentDialog);
        mAgentDialog = getDialog(context, R.layout.popup_agent_list_alert);

        WindowManager.LayoutParams LayoutParams = new WindowManager.LayoutParams();
        Window window = mAgentDialog.getWindow();

        if (window != null) {
            LayoutParams.copyFrom(window.getAttributes());
            LayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            LayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(LayoutParams);
            window.setGravity(Gravity.CENTER);
        }

        RecyclerView agentRecyclerView;
        LinearLayout agentParentLay;

        /*Init view*/
        agentRecyclerView = mAgentDialog.findViewById(R.id.agent_recycler_view);
        agentParentLay = mAgentDialog.findViewById(R.id.agent_parent_lay);


        /*Set Adapter*/
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        agentRecyclerView.setLayoutManager(linearLayoutManager);
        agentRecyclerView.setAdapter(new AgentListAdapter(agentListArrList, dialogAlertInterface, context));

        agentParentLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAgentDialog.dismiss();
                dialogAlertInterface.onNegativeClick();
            }
        });

        mAgentDialog.setCancelable(true);
        mAgentDialog.setCanceledOnTouchOutside(true);
        alertShowing(mAgentDialog);
        return mAgentDialog;
    }


    /*Inside pop up*/
    public void showInsideDetailsPopup(Context context, String headerStr, String hoursStr, final InterfaceBtnCallback dialogAlertInterface) {
        ArrayList<InsightsEntity> insightsListResponse = new ArrayList<>();
        alertDismiss(mAlertDialog);
        mAlertDialog = getDialog(context, R.layout.popup_insights_alert);

        WindowManager.LayoutParams LayoutParams = new WindowManager.LayoutParams();
        Window window = mAlertDialog.getWindow();

        if (window != null) {
            LayoutParams.copyFrom(window.getAttributes());
            LayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            LayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(LayoutParams);
            window.setGravity(Gravity.CENTER);
        }

        Button alertNegativeBtn;
        //  String firstNameStr, descStr, expStr, feeStr;

        TextView mHeadingTxt;
        TextView mHoursTxt;
        RecyclerView mInsightsPopUpRecyclerView;

        //TextView mCancelTxt;

        /*Init view*/
        mHeadingTxt = mAlertDialog.findViewById(R.id.insight_details_txt);
        mHoursTxt = mAlertDialog.findViewById(R.id.hour_details_txt);
        alertNegativeBtn = mAlertDialog.findViewById(R.id.inside_cancel_btn);
        mInsightsPopUpRecyclerView = mAlertDialog.findViewById(R.id.inside_recycler_view);

        /*Set data*/
        mHeadingTxt.setText(headerStr);
        mHoursTxt.setText(hoursStr);
        alertNegativeBtn.setText(context.getString(R.string.cancel));

        mInsightsPopUpRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mInsightsPopUpRecyclerView.setNestedScrollingEnabled(false);
        mInsightsPopUpRecyclerView.setAdapter(new PopUpListAdapter(insightsListResponse, context));

        //Check and set button visibility
        alertNegativeBtn.setVisibility(View.VISIBLE);

        alertNegativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
                dialogAlertInterface.onPositiveClick();
            }
        });

        alertShowing(mAlertDialog);
    }


    public void showOptionPopup(Context context, String messageStr, String firstBtnName, String secondBtnName,
                                final InterfaceTwoBtnCallback dialogAlertInterface) {
        alertDismiss(mOptionDialog);
        mOptionDialog = getDialog(context, R.layout.popup_basic_alert);

        WindowManager.LayoutParams LayoutParams = new WindowManager.LayoutParams();
        Window window = mOptionDialog.getWindow();

        if (window != null) {
            LayoutParams.copyFrom(window.getAttributes());
            LayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            LayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(LayoutParams);
            window.setGravity(Gravity.CENTER);
        }

        TextView msgTxt;
        Button positiveBtn, negativeBtn;

        /*Init view*/
        msgTxt = mOptionDialog.findViewById(R.id.alert_msg_txt);
        positiveBtn = mOptionDialog.findViewById(R.id.alert_positive_btn);
        negativeBtn = mOptionDialog.findViewById(R.id.alert_negative_btn);

        negativeBtn.setVisibility(View.VISIBLE);

        /*Set data*/
        msgTxt.setText(messageStr);
        positiveBtn.setText(firstBtnName);
        negativeBtn.setText(secondBtnName);

        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOptionDialog.dismiss();
                dialogAlertInterface.onPositiveClick();
            }
        });
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOptionDialog.dismiss();
                dialogAlertInterface.onNegativeClick();
            }
        });

        alertShowing(mOptionDialog);

    }

    public void showTermsAndConditionpopup(Context context, String messageStr,
                                           final InterfaceTwoBtnCallback dialogAlertInterface) {
        alertDismiss(mOptionDialog);
        mOptionDialog = getDialog(context, R.layout.popup_terms_con_alert);

        WindowManager.LayoutParams LayoutParams = new WindowManager.LayoutParams();
        Window window = mOptionDialog.getWindow();

        if (window != null) {
            LayoutParams.copyFrom(window.getAttributes());
            LayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            LayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(LayoutParams);
            window.setGravity(Gravity.CENTER);
        }

        TextView msgTxt;
        Button positiveBtn, negativeBtn;

        /*Init view*/
        msgTxt = mOptionDialog.findViewById(R.id.alert_msg_txt);

        positiveBtn = mOptionDialog.findViewById(R.id.alert_positive_btn);
        negativeBtn = mOptionDialog.findViewById(R.id.alert_negative_btn);


        /*Set data*/
        msgTxt.setText(messageStr);
        positiveBtn.setText(context.getString(R.string.accept));
        negativeBtn.setText(context.getString(R.string.decline));

        negativeBtn.setVisibility(View.VISIBLE);
        positiveBtn.setVisibility(View.VISIBLE);
        negativeBtn.setBackgroundColor(Color.parseColor("#c4c4c4"));

        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOptionDialog.dismiss();
                dialogAlertInterface.onPositiveClick();
            }
        });

        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOptionDialog.dismiss();
                dialogAlertInterface.onNegativeClick();
            }
        });

        alertShowing(mOptionDialog);

    }

    public void showTimePopup(final Context context, final TextView setStartTime, String startTimeStr, final TextView setEndTime, String endTimeStr, final InterfaceEdtTimeBtnCallback dialogAlertInterface) {
        alertDismiss(mOptionDialog);
        mOptionDialog = getDialog(context, R.layout.popup_time_alert);

        WindowManager.LayoutParams LayoutParams = new WindowManager.LayoutParams();
        Window window = mOptionDialog.getWindow();

        if (window != null) {
            LayoutParams.copyFrom(window.getAttributes());
            LayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            LayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(LayoutParams);
            window.setGravity(Gravity.CENTER);
        }

        Button negativeBtn, positiveBtn;

        /*Init view*/
        startTimeTxt = mOptionDialog.findViewById(R.id.start_time_txt);
        endTimeTxt = mOptionDialog.findViewById(R.id.end_time_txt);

       /* mSetStartTime = mOptionDialog.findViewById(R.id.startt_time_txt);
        mSetEndTime = mOptionDialog.findViewById(R.id.endd_time_txt);*/

        /*setStartTime.setText(returnStr);
        setEndTime.setText(returnStr);*/
        setStartTime.getText().toString().trim();
        setEndTime.getText().toString().trim();


        negativeBtn = mOptionDialog.findViewById(R.id.alert_negativee_btn);
        positiveBtn = mOptionDialog.findViewById(R.id.alert_positivee_btn);


        /*Set data*/
       /* startTimeTxt.setText(startTimeStr);
        endTimeTxt.setText(endTimeStr);*/

        negativeBtn.setText(context.getString(R.string.cancel));
        positiveBtn.setText(context.getString(R.string.ok));

        negativeBtn.setVisibility(View.VISIBLE);
        positiveBtn.setVisibility(View.VISIBLE);


        startTimeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartTimeBool = true;
                showTimePickerDialog(startTimeTxt.getText().toString().trim(), context);
            }
        });

        endTimeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartTimeBool = false;
                showTimePickerDialog(endTimeTxt.getText().toString().trim(), context);
            }
        });
        /*Positive Button Click*/
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOptionDialog.dismiss();
                dialogAlertInterface.onPositiveClick(startTimeTxt.getText().toString().trim(), endTimeTxt.getText().toString().trim());
            }
        });

        /*Negative Button Click*/
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOptionDialog.dismiss();
                dialogAlertInterface.onNegativeClick();
            }
        });
        alertShowing(mOptionDialog);
    }

    private void showTimePickerDialog(String timeStr, Context context) {
        String timeStrArr[] = DateUtil.getCustomDateFormat(timeStr, AppConstants.CUSTOM_12_HRS_TIME_FORMAT, AppConstants.CUSTOM_24_HRS_TIME_FORMAT).split(context.getString(R.string.colon_sym));

        mHour = Integer.valueOf(timeStrArr[0]);
        mMinute = Integer.valueOf(timeStrArr[1]);

        timePickerDialogDismiss(mTimePicker);

        mTimePicker = new TimePickerDialog(context, mTimeSetListener, mHour, mMinute, false);

        mTimePicker.show();
    }

    /*Show Time Picker Dialog*/
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
                startTimeTxt.setText(returnStr);
            } else {
                endTimeTxt.setText(returnStr);
            }
        }
    };

    /*Time Picker Dialog Dismiss*/
    private void timePickerDialogDismiss(TimePickerDialog dialog) {
        /*To check if the dialog is shown, if the dialog is shown it will be cancelled */
        if (dialog != null && dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                Log.e(AppConstants.TAG, e.getMessage());
            }
        }
    }


    public void showForgotPwdPopup(final Context context, String emailIdStr, final InterfaceEdtBtnCallback dialogAlertInterface) {
        alertDismiss(mForgotPwdDialog);
        mForgotPwdDialog = getDialog(context, R.layout.popup_forgot_pwd_alert);

        WindowManager.LayoutParams LayoutParams = new WindowManager.LayoutParams();
        Window window = mForgotPwdDialog.getWindow();

        if (window != null) {
            LayoutParams.copyFrom(window.getAttributes());
            LayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            LayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(LayoutParams);
            window.setGravity(Gravity.CENTER);
        }

        final EditText emailEdt;
        Button submitBtn, cancelBtn;

        /*Init view*/
        emailEdt = mForgotPwdDialog.findViewById(R.id.email_id_edt);
        submitBtn = mForgotPwdDialog.findViewById(R.id.submit_btn);
        cancelBtn = mForgotPwdDialog.findViewById(R.id.cancel_btn);

        /*Set data*/
        emailEdt.setText(emailIdStr);
        emailEdt.setSelection(emailIdStr.length());


        /*Keypad button action*/
        emailEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 100 || actionId == EditorInfo.IME_ACTION_DONE) {
                    editTextKeyPadHidden(context, emailEdt);
                    String emailStr = emailEdt.getText().toString().trim();
                    if (emailStr.isEmpty()) {
                        emailEdt.requestFocus();
                        emailEdt.setSelection(emailStr.length());
                        showAlertPopup(context, context.getString(R.string.enter_email_id), new InterfaceBtnCallback() {
                            @Override
                            public void onPositiveClick() {

                            }
                        });
                    } else if (!PatternMatcherUtil.isEmailValid(emailStr)) {
                        emailEdt.requestFocus();
                        emailEdt.setSelection(emailStr.length());
                        showAlertPopup(context, context.getString(R.string.enter_valid_email_id), new InterfaceBtnCallback() {
                            @Override
                            public void onPositiveClick() {

                            }
                        });
                    } else {
                        mForgotPwdDialog.dismiss();
                        dialogAlertInterface.onPositiveClick(emailStr);
                    }
                }
                return true;
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextKeyPadHidden(context, emailEdt);
                mForgotPwdDialog.dismiss();
                dialogAlertInterface.onNegativeClick();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View v) {
                editTextKeyPadHidden(context, emailEdt);
                String emailStr = emailEdt.getText().toString().trim();
                if (emailStr.isEmpty()) {
                    emailEdt.requestFocus();
                    emailEdt.setSelection(emailStr.length());
                    showAlertPopup(context, context.getString(R.string.enter_email_id), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {

                        }
                    });
                } else if (!PatternMatcherUtil.isEmailValid(emailStr)) {
                    emailEdt.requestFocus();
                    emailEdt.setSelection(emailStr.length());
                    showAlertPopup(context, context.getString(R.string.enter_valid_email_id), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {

                        }
                    });
                } else {
                    mForgotPwdDialog.dismiss();
                    dialogAlertInterface.onPositiveClick(emailStr);
                }
            }
        });


        alertShowing(mForgotPwdDialog);

    }


    /*Show Add IOT Alert popup*/
    public void showAddIOTAlertPopup(Context context, String messageStr, final InterfaceTwoBtnCallback dialogAlertInterface) {

        alertDismiss(mAddIOTAlertDialog);
        mAddIOTAlertDialog = getDialog(context, R.layout.popup_add_iot_alert);

        WindowManager.LayoutParams LayoutParams = new WindowManager.LayoutParams();
        Window window = mAddIOTAlertDialog.getWindow();

        if (window != null) {
            LayoutParams.copyFrom(window.getAttributes());
            LayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            LayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(LayoutParams);
            window.setGravity(Gravity.CENTER);
        }

        TextView alertDeviceNameTxt;
        Button alertPositiveBtn, alertNegativeBtn;

        /*Init view*/
        alertDeviceNameTxt = mAddIOTAlertDialog.findViewById(R.id.alert_device_name_txt);
        alertPositiveBtn = mAddIOTAlertDialog.findViewById(R.id.alert_positive_btn);
        alertNegativeBtn = mAddIOTAlertDialog.findViewById(R.id.alert_negative_btn);

        /*Set data*/
        alertDeviceNameTxt.setText(String.format(context.getString(R.string.add_a_device), messageStr));

        alertPositiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddIOTAlertDialog.dismiss();
                dialogAlertInterface.onPositiveClick();
            }
        });

        alertNegativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddIOTAlertDialog.dismiss();
                dialogAlertInterface.onNegativeClick();
            }
        });

        alertShowing(mAddIOTAlertDialog);

    }

    /*Edit IOt device name and location name*/
    public void showEdtIOTDeviceNameLocPopup(final Context context, String deviceNameStr, String deviceLocStr, final InterfaceTwoEdtBtnCallback dialogAlertInterface) {
        alertDismiss(mEdtDeviceNameLocDialog);
        mEdtDeviceNameLocDialog = getDialog(context, R.layout.popup_edt_device_name_loc_alert);

        WindowManager.LayoutParams LayoutParams = new WindowManager.LayoutParams();
        Window window = mEdtDeviceNameLocDialog.getWindow();

        if (window != null) {
            LayoutParams.copyFrom(window.getAttributes());
            LayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            LayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(LayoutParams);
            window.setGravity(Gravity.CENTER);
        }

        final EditText deviceNameEdt, deviceLocNameEdt;
        Button positiveBtn, negativeBtn;

        /*Init view*/
        deviceNameEdt = mEdtDeviceNameLocDialog.findViewById(R.id.device_name_edt);
        deviceLocNameEdt = mEdtDeviceNameLocDialog.findViewById(R.id.device_loc_name_edt);
        positiveBtn = mEdtDeviceNameLocDialog.findViewById(R.id.positive_btn);
        negativeBtn = mEdtDeviceNameLocDialog.findViewById(R.id.negative_btn);

        /*Set data*/
        deviceNameEdt.setText(deviceNameStr);
        deviceLocNameEdt.setText(deviceLocStr);
        deviceNameEdt.setSelection(deviceNameStr.length());
        deviceLocNameEdt.setSelection(deviceLocStr.length());

        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextKeyPadHidden(context, deviceNameEdt);
                mEdtDeviceNameLocDialog.dismiss();
                dialogAlertInterface.onNegativeClick();
            }
        });

        positiveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                editTextKeyPadHidden(context, deviceNameEdt);
                String nameStr = deviceNameEdt.getText().toString().trim();
                String locStr = deviceLocNameEdt.getText().toString().trim();
                if (nameStr.isEmpty()) {
                    deviceNameEdt.requestFocus();
                    deviceNameEdt.setSelection(nameStr.length());

                    showAlertPopup(context, context.getString(R.string.please_enter_device_name), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {

                        }
                    });
                } else if (locStr.isEmpty()) {
                    deviceLocNameEdt.requestFocus();
                    deviceLocNameEdt.setSelection(locStr.length());
                    showAlertPopup(context, context.getString(R.string.please_enter_device_loc), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {

                        }
                    });
                } else {
                    mEdtDeviceNameLocDialog.dismiss();
                    dialogAlertInterface.onPositiveClick(nameStr, locStr);
                }
            }
        });


        alertShowing(mEdtDeviceNameLocDialog);

    }

    /*Show Add IOT Alert popup*/
    public void showIOTReadyToUseAlertPopup(Context context, final InterfaceBtnCallback dialogAlertInterface) {
        alertDismiss(mIOTReadyToUseAlertDialog);
        mIOTReadyToUseAlertDialog = getDialog(context, R.layout.popup_iot_ready_to_use_alert);

        WindowManager.LayoutParams LayoutParams = new WindowManager.LayoutParams();
        Window window = mIOTReadyToUseAlertDialog.getWindow();

        if (window != null) {
            LayoutParams.copyFrom(window.getAttributes());
            LayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            LayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(LayoutParams);
            window.setGravity(Gravity.CENTER);
        }

        Button alertPositiveBtn;

        /*Init view*/
        alertPositiveBtn = mIOTReadyToUseAlertDialog.findViewById(R.id.alert_positive_btn);

        alertPositiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddIOTAlertDialog.dismiss();
                dialogAlertInterface.onPositiveClick();
            }
        });

        alertShowing(mIOTReadyToUseAlertDialog);

    }

    /*Set Pin IOT device*/
    public void showEdtIOTSetPinPopup(final Context context, String newIOTPinStr, String cnfSetPinIOTStr, final InterfaceTwoEdtBtnCallback dialogAlertInterface) {
        alertDismiss(mEdtDeviceNameLocDialog);
        mEdtDeviceNameLocDialog = getDialog(context, R.layout.popup_edt_set_pin_alert);

        WindowManager.LayoutParams LayoutParams = new WindowManager.LayoutParams();
        Window window = mEdtDeviceNameLocDialog.getWindow();

        if (window != null) {
            LayoutParams.copyFrom(window.getAttributes());
            LayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            LayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(LayoutParams);
            window.setGravity(Gravity.CENTER);
        }

        final EditText setNewPinEdt, cnfPinEdit;
        Button positiveBtn, negativeBtn;

        /*Init view*/
        setNewPinEdt = mEdtDeviceNameLocDialog.findViewById(R.id.new_pin_edt);
        cnfPinEdit = mEdtDeviceNameLocDialog.findViewById(R.id.cnf_pin_edt);
        positiveBtn = mEdtDeviceNameLocDialog.findViewById(R.id.positive_btn);
        negativeBtn = mEdtDeviceNameLocDialog.findViewById(R.id.negative_btn);

        /*Set data*/
        setNewPinEdt.setText(newIOTPinStr);
        cnfPinEdit.setText(cnfSetPinIOTStr);
        setNewPinEdt.setSelection(newIOTPinStr.length());
        cnfPinEdit.setSelection(cnfSetPinIOTStr.length());

        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextKeyPadHidden(context, setNewPinEdt);
                mEdtDeviceNameLocDialog.dismiss();
                dialogAlertInterface.onNegativeClick();
            }
        });

        positiveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                editTextKeyPadHidden(context, setNewPinEdt);
                String newPinStr = setNewPinEdt.getText().toString().trim();
                String cnfPinStr = cnfPinEdit.getText().toString().trim();
                String bothPinStr = cnfPinEdit.getText().toString().trim();

                if (newPinStr.isEmpty()) {
                    setNewPinEdt.requestFocus();
                    setNewPinEdt.setSelection(newPinStr.length());

                    showAlertPopup(context, context.getString(R.string.please_enter_your_pin), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {

                        }
                    });
                } else if (cnfPinStr.isEmpty()) {
                    cnfPinEdit.requestFocus();
                    cnfPinEdit.setSelection(cnfPinStr.length());
                    showAlertPopup(context, context.getString(R.string.please_enter_your_new_pin), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {

                        }
                    });

                } else if (bothPinStr.isEmpty()) {
                    cnfPinEdit.requestFocus();
                    cnfPinEdit.setSelection(cnfPinStr.length());
                    showAlertPopup(context, context.getString(R.string.please_your_cnf_pin), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {

                        }
                    });
                } else {
                    mEdtDeviceNameLocDialog.dismiss();
                    dialogAlertInterface.onPositiveClick(newPinStr, cnfPinStr);
                }
            }
        });


        alertShowing(mEdtDeviceNameLocDialog);

    }


    /*Network error popup*/
    public void showNetworkErrorPopup(Context context, String errorStr, final InterfaceBtnCallback dialogAlertInterface) {

        alertDismiss(mNetworkErrorDialog);

        mNetworkErrorDialog = getDialog(context, R.layout.popup_network_alert);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mNetworkErrorDialog.getWindow();

        if (window != null) {
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            window.setGravity(Gravity.BOTTOM);
            window.getAttributes().windowAnimations = R.style.PopupBottomAnimation;
        }

        Button retryBtn;
        TextView errorMsgTxt;

        //Init View
        retryBtn = mNetworkErrorDialog.findViewById(R.id.retry_btn);
        errorMsgTxt = mNetworkErrorDialog.findViewById(R.id.error_msg_txt);

        /*Set data*/
        errorMsgTxt.setText(errorStr);

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNetworkErrorDialog.dismiss();
                dialogAlertInterface.onPositiveClick();
            }
        });

        alertShowing(mNetworkErrorDialog);
    }

    public void showLogoutPopup(final Context context, final InterfaceTwoBtnCallback dialogAlertInterface) {
        alertDismiss(mLogoutDialog);
        mLogoutDialog = getDialog(context, R.layout.popup_logout_alert);

        WindowManager.LayoutParams LayoutParams = new WindowManager.LayoutParams();
        Window window = mLogoutDialog.getWindow();

        if (window != null) {
            LayoutParams.copyFrom(window.getAttributes());
            LayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            LayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(LayoutParams);
            window.setGravity(Gravity.CENTER);
        }

        Button yesBtn, noBtn;

        /*Init view*/
        yesBtn = mLogoutDialog.findViewById(R.id.yes_btn);
        noBtn = mLogoutDialog.findViewById(R.id.no_btn);


        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLogoutDialog.dismiss();
                dialogAlertInterface.onPositiveClick();
            }
        });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLogoutDialog.dismiss();
                dialogAlertInterface.onNegativeClick();
            }
        });

        alertShowing(mLogoutDialog);

    }


    public void showEdtDeviceNamePopup(final Context context, String pHeader, String sHeader, String editTxtHint, String deviceNameStr, final InterfaceEdtBtnCallback dialogAlertInterface) {
        alertDismiss(mEdtDeviceNameDialog);
        mEdtDeviceNameDialog = getDialog(context, R.layout.popup_edt_device_name_alert);

        WindowManager.LayoutParams LayoutParams = new WindowManager.LayoutParams();
        Window window = mEdtDeviceNameDialog.getWindow();

        if (window != null) {
            LayoutParams.copyFrom(window.getAttributes());
            LayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            LayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(LayoutParams);
            window.setGravity(Gravity.CENTER);
        }

        final EditText deviceNameEdt;
        Button positiveBtn, negativeBtn;
        TextView primaryTV, secondaryTV;

        /*Init view*/
        deviceNameEdt = mEdtDeviceNameDialog.findViewById(R.id.device_name_edt);
        positiveBtn = mEdtDeviceNameDialog.findViewById(R.id.positive_btn);
        negativeBtn = mEdtDeviceNameDialog.findViewById(R.id.negative_btn);
        primaryTV = mEdtDeviceNameDialog.findViewById(R.id.primaryLabel);
        secondaryTV = mEdtDeviceNameDialog.findViewById(R.id.secondaryLabel);

        /*Set data*/
        deviceNameEdt.setText(deviceNameStr);
        deviceNameEdt.setSelection(deviceNameStr.length());
        primaryTV.setText(pHeader);
        secondaryTV.setText(sHeader);
        deviceNameEdt.setHint(editTxtHint);

        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextKeyPadHidden(context, deviceNameEdt);
                mEdtDeviceNameDialog.dismiss();
                dialogAlertInterface.onNegativeClick();
            }
        });

        positiveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                editTextKeyPadHidden(context, deviceNameEdt);
                String nameStr = deviceNameEdt.getText().toString().trim();
                if (nameStr.isEmpty()) {
                    deviceNameEdt.requestFocus();
                    deviceNameEdt.setSelection(nameStr.length());
                    showAlertPopup(context, context.getString(R.string.please_enter_device_name), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {

                        }
                    });
                } else {
                    mEdtDeviceNameDialog.dismiss();
                    dialogAlertInterface.onPositiveClick(nameStr);
                }
            }
        });


        alertShowing(mEdtDeviceNameDialog);

    }

    /*IOT Device Name POPUP*/
    public void showEdtIOTDeviceNamePopup(final Context context, String pHeader, String sHeader, String editTxtHint, String deviceNameStr, final InterfaceEdtBtnCallback dialogAlertInterface) {
        alertDismiss(mEdtDeviceNameDialog);
        mEdtDeviceNameDialog = getDialog(context, R.layout.popup_edt_device_name_alert);

        WindowManager.LayoutParams LayoutParams = new WindowManager.LayoutParams();
        Window window = mEdtDeviceNameDialog.getWindow();

        if (window != null) {
            LayoutParams.copyFrom(window.getAttributes());
            LayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            LayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(LayoutParams);
            window.setGravity(Gravity.CENTER);
        }

        final EditText deviceNameEdt;
        Button positiveBtn, negativeBtn;
        TextView primaryTV, secondaryTV;

        /*Init view*/
        deviceNameEdt = mEdtDeviceNameDialog.findViewById(R.id.device_name_edt);
        positiveBtn = mEdtDeviceNameDialog.findViewById(R.id.positive_btn);
        negativeBtn = mEdtDeviceNameDialog.findViewById(R.id.negative_btn);
        primaryTV = mEdtDeviceNameDialog.findViewById(R.id.primaryLabel);
        secondaryTV = mEdtDeviceNameDialog.findViewById(R.id.secondaryLabel);

        /*Set data*/
        deviceNameEdt.setText(deviceNameStr);
        deviceNameEdt.setSelection(deviceNameStr.length());
        primaryTV.setText(pHeader);
        secondaryTV.setText(sHeader);
        deviceNameEdt.setHint(editTxtHint);

        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextKeyPadHidden(context, deviceNameEdt);
                mEdtDeviceNameDialog.dismiss();
                dialogAlertInterface.onNegativeClick();
            }
        });

        positiveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                editTextKeyPadHidden(context, deviceNameEdt);
                String nameStr = deviceNameEdt.getText().toString().trim();
                if (nameStr.isEmpty()) {
                    deviceNameEdt.requestFocus();
                    deviceNameEdt.setSelection(nameStr.length());
                    showAlertPopup(context, context.getString(R.string.please_enter_device_name), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {

                        }
                    });
                } else {
                    mEdtDeviceNameDialog.dismiss();
                    dialogAlertInterface.onPositiveClick(nameStr);
                }
            }
        });


        alertShowing(mEdtDeviceNameDialog);

    }

    /*IOT Notification POPUP*/
    public void showEdtIOTNamePopup(final Context context, boolean allowNotificationOnOff, boolean notifyMe, final InterfaceEdtBtnCallback dialogIOTAlertInterface) {
        alertDismiss(mEdtDeviceNameDialog);
        mEdtDeviceNameDialog = getDialog(context, R.layout.popup_notification_iot_alert);
        mEdtDeviceNameDialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams LayoutParams = new WindowManager.LayoutParams();
        Window window = mEdtDeviceNameDialog.getWindow();

        if (window != null) {
            LayoutParams.copyFrom(window.getAttributes());
            LayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            LayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(LayoutParams);
            window.setGravity(Gravity.CENTER);
        }

        final TextView notificationOn, notificationOff, notifyMeAlways, notifyMeWhenArmed;
        //Button positiveBtn, negativeBtn;

        /*Init view*/
        notificationOn = mEdtDeviceNameDialog.findViewById(R.id.notification_on_txt);
        notificationOff = mEdtDeviceNameDialog.findViewById(R.id.notification_off_txt);
        notifyMeAlways = mEdtDeviceNameDialog.findViewById(R.id.always_txt);
        notifyMeWhenArmed = mEdtDeviceNameDialog.findViewById(R.id.whenarmed__txt);

//        positiveBtn = mEdtDeviceNameDialog.findViewById(R.id.positive_btn);
//        negativeBtn = mEdtDeviceNameDialog.findViewById(R.id.negative_btn);

        /*Set data*/
        notificationOn.setText("ON");
        notificationOff.setText("OFF");
        notifyMeAlways.setText("Always");
        notifyMeWhenArmed.setText("When armed");

//        negativeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEdtDeviceNameDialog.dismiss();
//                dialogIOTAlertInterface.onNegativeClick();
//            }
//        });
//
//        positiveBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                    mEdtDeviceNameDialog.dismiss();
//                }
//        });


        alertShowing(mEdtDeviceNameDialog);
    }

    private void alertShowing(Dialog dialog) {
        /*To check if the dialog is null or not. if it'border_with_transparent_bg not a null, the dialog will be shown orelse it will not get appeared*/
        if (dialog != null) {
            try {
                dialog.show();
            } catch (Exception e) {
                Log.e(AppConstants.TAG, e.getMessage());
            }
        }
    }

    private void alertDismiss(Dialog dialog) {
        /*To check if the dialog is shown, if the dialog is shown it will be cancelled */
        if (dialog != null && dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                Log.e(AppConstants.TAG, e.getMessage());
            }
        }

    }

    private void editTextKeyPadHidden(Context context, EditText edtView) {
        /*Hiding keypad for user interaction*/
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null)
            inputMethodManager.hideSoftInputFromWindow(edtView.getWindowToken(), 0);
    }


}
