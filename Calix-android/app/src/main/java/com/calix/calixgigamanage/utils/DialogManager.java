package com.calix.calixgigamanage.utils;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.adapter.AgentListAdapter;
import com.calix.calixgigamanage.output.model.AlexaAppIdEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class DialogManager {

    /*Init variable*/
    private Dialog mProgressDialog, mNetworkErrorDialog, mAlertDialog, mOptionDialog, mLogoutDialog, mDeviceDisconnectOptionDialog, mDeviceDisconnectScheduleDialog, mForgotPwdDialog, mEdtDeviceNameDialog, mEdtDeviceLocDialog, mEdtDeviceNameLocDialog, mDevelopmentDialog, mAgentDialog;
    private Toast mToast;

    private DatePickerDialog mDatePicker;
    private TimePickerDialog mTimePicker;
    private TextView mCurrentTxt;
    private Context mContext;

    /*Init dialog instance*/
    private static final DialogManager sDialogInstance = new DialogManager();


    public static DialogManager getInstance() {
        return sDialogInstance;
    }

    /*Init toast*/
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
        Button sendBtn, cancelBtn;

        /*Init view*/
        emailEdt = mForgotPwdDialog.findViewById(R.id.email_id_edt);
        sendBtn = mForgotPwdDialog.findViewById(R.id.send_btn);
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

        sendBtn.setOnClickListener(new View.OnClickListener() {
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


    public void showEdtDeviceNamePopup(final Context context, String deviceNameStr, final InterfaceEdtBtnCallback dialogAlertInterface) {
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

        /*Init view*/
        deviceNameEdt = mEdtDeviceNameDialog.findViewById(R.id.device_name_edt);
        positiveBtn = mEdtDeviceNameDialog.findViewById(R.id.positive_btn);
        negativeBtn = mEdtDeviceNameDialog.findViewById(R.id.negative_btn);

        /*Set data*/
        deviceNameEdt.setText(deviceNameStr);
        deviceNameEdt.setSelection(deviceNameStr.length());

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

    public void showEdtDeviceLocPopup(final Context context, String deviceLocStr, final InterfaceEdtBtnCallback dialogAlertInterface) {
        alertDismiss(mEdtDeviceLocDialog);
        mEdtDeviceLocDialog = getDialog(context, R.layout.popup_edt_device_loc_alert);

        WindowManager.LayoutParams LayoutParams = new WindowManager.LayoutParams();
        Window window = mEdtDeviceLocDialog.getWindow();

        if (window != null) {
            LayoutParams.copyFrom(window.getAttributes());
            LayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            LayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(LayoutParams);
            window.setGravity(Gravity.CENTER);
        }

        final EditText deviceLocEdt;
        Button positiveBtn, negativeBtn;

        /*Init view*/
        deviceLocEdt = mEdtDeviceLocDialog.findViewById(R.id.device_loc_edt);
        positiveBtn = mEdtDeviceLocDialog.findViewById(R.id.positive_btn);
        negativeBtn = mEdtDeviceLocDialog.findViewById(R.id.negative_btn);

        /*Set data*/
        deviceLocEdt.setText(deviceLocStr);
        deviceLocEdt.setSelection(deviceLocStr.length());

        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextKeyPadHidden(context, deviceLocEdt);
                mEdtDeviceLocDialog.dismiss();
                dialogAlertInterface.onNegativeClick();
            }
        });

        positiveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                editTextKeyPadHidden(context, deviceLocEdt);
                String nameStr = deviceLocEdt.getText().toString().trim();
                if (nameStr.isEmpty()) {
                    deviceLocEdt.requestFocus();
                    deviceLocEdt.setSelection(nameStr.length());
                    showAlertPopup(context, context.getString(R.string.please_enter_device_loc), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {

                        }
                    });
                } else {
                    mEdtDeviceLocDialog.dismiss();
                    dialogAlertInterface.onPositiveClick(nameStr);
                }
            }
        });


        alertShowing(mEdtDeviceLocDialog);

    }

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

    public void showDeviceDisconnectOptionPopup(final Context context, final InterfaceTwoEdtBtnCallback dialogAlertInterface) {

        alertDismiss(mDeviceDisconnectOptionDialog);
        mDeviceDisconnectOptionDialog = getDialog(context, R.layout.popup_device_discconect_option_alert);

        WindowManager.LayoutParams LayoutParams = new WindowManager.LayoutParams();
        Window window = mDeviceDisconnectOptionDialog.getWindow();

        if (window != null) {
            LayoutParams.copyFrom(window.getAttributes());
            LayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            LayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(LayoutParams);
            window.setGravity(Gravity.CENTER);
        }

        Button alertImmediateBtn, alertScheduleBtn, alertCancelBtn;

        /*Init view*/
        alertImmediateBtn = mDeviceDisconnectOptionDialog.findViewById(R.id.alert_immediate_btn);
        alertScheduleBtn = mDeviceDisconnectOptionDialog.findViewById(R.id.alert_schedule_btn);
        alertCancelBtn = mDeviceDisconnectOptionDialog.findViewById(R.id.alert_cancel_btn);

        alertImmediateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeviceDisconnectOptionDialog.dismiss();
                dialogAlertInterface.onNegativeClick();
            }
        });

        alertScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeviceDisconnectOptionDialog.dismiss();
                showDeviceDisconnectSchedulePopup(context, dialogAlertInterface);
            }
        });

        alertCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeviceDisconnectOptionDialog.dismiss();
            }
        });

        alertShowing(mDeviceDisconnectOptionDialog);

    }

    private void showDeviceDisconnectSchedulePopup(final Context context, final InterfaceTwoEdtBtnCallback dialogAlertInterface) {

        alertDismiss(mDeviceDisconnectScheduleDialog);
        mDeviceDisconnectScheduleDialog = getDialog(context, R.layout.popup_device_discconect_schedule_alert);

        WindowManager.LayoutParams LayoutParams = new WindowManager.LayoutParams();
        Window window = mDeviceDisconnectScheduleDialog.getWindow();

        if (window != null) {
            LayoutParams.copyFrom(window.getAttributes());
            LayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            LayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(LayoutParams);
            window.setGravity(Gravity.CENTER);
        }

        final TextView startDateTxt, endDateTxt, startTimeTxt, endTimeTxt;
        Button alertDoneBtn, alertCancelBtn;

        /*Init view*/
        startDateTxt = mDeviceDisconnectScheduleDialog.findViewById(R.id.start_date_txt);
        endDateTxt = mDeviceDisconnectScheduleDialog.findViewById(R.id.end_date_txt);
        startTimeTxt = mDeviceDisconnectScheduleDialog.findViewById(R.id.start_time_txt);
        endTimeTxt = mDeviceDisconnectScheduleDialog.findViewById(R.id.end_time_txt);

        alertDoneBtn = mDeviceDisconnectScheduleDialog.findViewById(R.id.alert_done_btn);
        alertCancelBtn = mDeviceDisconnectScheduleDialog.findViewById(R.id.alert_cancel_btn);

        /*Set data*/
        startDateTxt.setText(DateUtil.getCustomDateAndTimeFormat(System.currentTimeMillis(), AppConstants.CUSTOM_DATE_FORMAT));
        endDateTxt.setText(DateUtil.getCustomDateAndTimeFormat(System.currentTimeMillis(), AppConstants.CUSTOM_DATE_FORMAT));
        startTimeTxt.setText(DateUtil.getCustomDateAndTimeFormat(System.currentTimeMillis(), AppConstants.CUSTOM_12_HRS_TIME_FORMAT));
        endTimeTxt.setText(DateUtil.getCustomDateAndTimeFormat(System.currentTimeMillis(), AppConstants.CUSTOM_12_HRS_TIME_FORMAT));

        startDateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mContext = context;
                mCurrentTxt = startDateTxt;
                showDatePickerDialog(startDateTxt.getText().toString().trim());
            }
        });
        endDateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mContext = context;
                mCurrentTxt = endDateTxt;
                showDatePickerDialog(endDateTxt.getText().toString().trim());
            }
        });
        startTimeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext = context;
                mCurrentTxt = startTimeTxt;
                showTimePickerDialog(startDateTxt.getText().toString().trim() + " " + startTimeTxt.getText().toString().trim());

            }
        });
        endTimeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext = context;
                mCurrentTxt = endTimeTxt;
                showTimePickerDialog(endDateTxt.getText().toString().trim() + " " + endTimeTxt.getText().toString().trim());

            }
        });

        alertDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DateUtil.getMileSecFromDate(startDateTxt.getText().toString().trim() + " " + startTimeTxt.getText().toString().trim(), AppConstants.CUSTOM_DATE_TIME_FORMAT) < System.currentTimeMillis() - 120000) {
                    DialogManager.getInstance().showAlertPopup(context, context.getString(R.string.select_feature_date_time), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {

                        }
                    });
                } else if (DateUtil.getMileSecFromDate(startDateTxt.getText().toString().trim() + " " + startTimeTxt.getText().toString().trim(), AppConstants.CUSTOM_DATE_TIME_FORMAT) >
                        DateUtil.getMileSecFromDate(endDateTxt.getText().toString().trim() + " " + endTimeTxt.getText().toString().trim(), AppConstants.CUSTOM_DATE_TIME_FORMAT)) {
                    DialogManager.getInstance().showAlertPopup(context, context.getString(R.string.select_valid_date_time), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {

                        }
                    });
                } else {
                    mDeviceDisconnectScheduleDialog.dismiss();
                    dialogAlertInterface.onPositiveClick(String.valueOf(DateUtil.getMileSecFromDate(startDateTxt.getText().toString().trim() + " " + startTimeTxt.getText().toString().trim(), AppConstants.CUSTOM_DATE_TIME_FORMAT)), String.valueOf(DateUtil.getMileSecFromDate(endDateTxt.getText().toString().trim() + " " + endTimeTxt.getText().toString().trim(), AppConstants.CUSTOM_DATE_TIME_FORMAT)));
                }
            }
        });

        alertCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeviceDisconnectScheduleDialog.dismiss();
            }
        });

        alertShowing(mDeviceDisconnectScheduleDialog);

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
        LinearLayout devModeChangeLay;

        /*Init view*/
        developmentRadioBtn = mDevelopmentDialog.findViewById(R.id.development_radio_btn);
        stageRadioBtn = mDevelopmentDialog.findViewById(R.id.stage_radio_btn);
        productionRadioBtn = mDevelopmentDialog.findViewById(R.id.production_radio_btn);

        devModeChangeLay = mDevelopmentDialog.findViewById(R.id.dev_mode_change_lay);

        final String selectedUrlStr = PreferenceUtil.getBaseURL(context);
        developmentRadioBtn.setChecked(selectedUrlStr.equalsIgnoreCase(AppConstants.DEVELOPER_BASE_URL));

        productionRadioBtn.setChecked(selectedUrlStr.equalsIgnoreCase(AppConstants.PRODUCTION_BASE_URL));
        stageRadioBtn.setChecked( selectedUrlStr.equalsIgnoreCase(AppConstants.STAGE_BASE_URL));



        developmentRadioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    developmentRadioBtn.setChecked(true);
                    productionRadioBtn.setChecked(false);
                    stageRadioBtn.setChecked(false);
                    if (selectedUrlStr.equalsIgnoreCase(AppConstants.DEVELOPER_BASE_URL)) {
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
                                PreferenceUtil.storeStringValue(context, AppConstants.USER_SELECTED_BASE_URL, AppConstants.DEVELOPER_BASE_URL);
                                mDevelopmentDialog.dismiss();
                                dialogAlertInterface.onPositiveClick();
                            }
                        });
                    }
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
                    if (selectedUrlStr.equalsIgnoreCase(AppConstants.STAGE_BASE_URL)) {
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
                                PreferenceUtil.storeStringValue(context, AppConstants.USER_SELECTED_BASE_URL, AppConstants.STAGE_BASE_URL);
                                mDevelopmentDialog.dismiss();
                                dialogAlertInterface.onPositiveClick();
                            }
                        });
                    }
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

                    if (selectedUrlStr.equalsIgnoreCase(AppConstants.PRODUCTION_BASE_URL)) {
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
                                PreferenceUtil.storeStringValue(context, AppConstants.USER_SELECTED_BASE_URL, AppConstants.PRODUCTION_BASE_URL);
                                mDevelopmentDialog.dismiss();
                                dialogAlertInterface.onPositiveClick();
                            }
                        });
                    }
                }
            }
        });

        devModeChangeLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDevelopmentDialog.dismiss();
                dialogAlertInterface.onNegativeClick();
            }
        });

        mDevelopmentDialog.setCancelable(true);
        mDevelopmentDialog.setCanceledOnTouchOutside(true);
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

    public void showProgress(Context context) {

        try {
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
            if (mProgressDialog != null) {
                mProgressDialog.show();
            }
        } catch (Exception e) {
            Log.e(AppConstants.TAG, e.getMessage());
        }
    }

    public void hideProgress() {
        /*hide progress*/
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            try {
                mProgressDialog.dismiss();
            } catch (Exception e) {
                Log.e(AppConstants.TAG, e.getMessage());
            }
        }
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


    /*Show data picker*/
    private void showDatePickerDialog(String dateStr) {

        String[] dateStrArr = dateStr.split(mContext.getString(R.string.hyphen_sym));

        datePickerDialogDismiss(mDatePicker);

        mDatePicker = new DatePickerDialog(mContext, mDateSetListener,
                Integer.valueOf(dateStrArr[2]), (Integer.valueOf(dateStrArr[0]) - 1), Integer.valueOf(dateStrArr[1]));

        mDatePicker.show();

    }

    /*Date picker listener */
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog
            .OnDateSetListener() {

        /* date picker dialog box*/
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            String dateStr = String.valueOf(monthOfYear + 1) + mContext.getString(R.string.hyphen_sym) + dayOfMonth + mContext.getString(R.string.hyphen_sym) + year;
            Date selectedDate;
            String mDateStr = "";

            try {
                selectedDate = new SimpleDateFormat("MM-dd-yyyy", Locale.US).parse(dateStr);
                mDateStr = new SimpleDateFormat("MM-dd-yyyy", Locale.US).format(selectedDate);

            } catch (Exception e) {
                Log.e(AppConstants.TAG, e.getMessage());
            }


            if (mCurrentTxt != null) {
                mCurrentTxt.setText(mDateStr);
            }
        }


    };

    private void showTimePickerDialog(String timeStr) {

        String timeStrArr[] = DateUtil.getCustomDateFormat(timeStr, AppConstants.CUSTOM_DATE_TIME_FORMAT, AppConstants.CUSTOM_24_HRS_TIME_FORMAT).split(mContext.getString(R.string.colon_sym));

        timePickerDialogDismiss(mTimePicker);

        mTimePicker = new TimePickerDialog(mContext, mTimeSetListener, Integer.valueOf(timeStrArr[0]), Integer.valueOf(timeStrArr[1]), false);

        mTimePicker.show();
    }

    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String timeStr = String.valueOf(hourOfDay) + ":" + minute;

            Date selectedTime;
            String returnStr = "";

            try {
                selectedTime = new SimpleDateFormat("HH:mm", Locale.US).parse(timeStr);
                returnStr = new SimpleDateFormat("hh:mm aa", Locale.US).format(selectedTime);

            } catch (Exception e) {
                Log.e(AppConstants.TAG, e.getMessage());
            }


            if (mCurrentTxt != null) {
                mCurrentTxt.setText(returnStr);
            }
        }

    };

    private void datePickerDialogDismiss(DatePickerDialog dialog) {
        /*To check if the dialog is shown, if the dialog is shown it will be cancelled */
        if (dialog != null && dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                Log.e(AppConstants.TAG, e.getMessage());
            }
        }

    }

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
}
