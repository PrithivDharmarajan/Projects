package com.smaat.virtualtrainer.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smaat.virtualtrainer.R;

import static com.smaat.virtualtrainer.R.id.submit_btn;

public class DialogManager {

    private static Dialog mProgressDialog, mAlertDialog, mTwoAlertDialog;

    private static Dialog getDialog(Context context, int layout) {
        Dialog mCommonDialog;
        mCommonDialog = new Dialog(context);
        mCommonDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mCommonDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mCommonDialog.setContentView(layout);
        mCommonDialog.getWindow().setGravity(Gravity.CENTER);
        mCommonDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        mCommonDialog.setCancelable(false);
        mCommonDialog.setCanceledOnTouchOutside(false);

        return mCommonDialog;
    }

    public static void showToastMessage(Context context, String message) {


        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();


    }

    public static Dialog showBasicAlertDialog(Context context, String message, String firstBtnName, boolean firstBtnVisible, String secondBtnName, boolean secondBtnVisible, boolean closeBtnVisible, final DialogMangerTwoBtnCallback dialogInterface) {

        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            try {
                mAlertDialog.dismiss();
            } catch (Exception e) {
                Log.e("showBasicAlertDialog", e.getMessage());
            }
        }
        mAlertDialog = getDialog(context, R.layout.dialog_basic_alert);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mAlertDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        mAlertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogBoxAnimation;

        ImageView closeImg;
        TextView msgTxt;
        Button firstBtn, secondBtn;

        //Init View
        closeImg = (ImageView) mAlertDialog.findViewById(R.id.close_img);
        msgTxt = (TextView) mAlertDialog.findViewById(R.id.msg_txt);
        firstBtn = (Button) mAlertDialog.findViewById(R.id.first_btn);
        secondBtn = (Button) mAlertDialog.findViewById(R.id.second_btn);

        //Set Data
        msgTxt.setText(message);
        firstBtn.setText(firstBtnName);
        secondBtn.setText(secondBtnName);

        //Check and Set Button Visibility
        firstBtn.setVisibility(firstBtnVisible ? View.VISIBLE : View.GONE);
        secondBtn.setVisibility(secondBtnVisible ? View.VISIBLE : View.GONE);
        closeImg.setVisibility(closeBtnVisible ? View.VISIBLE : View.GONE);


        firstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
                dialogInterface.onNoClick();
            }
        });
        secondBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
                dialogInterface.onYesClick();
            }
        });
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
                dialogInterface.onNoClick();
            }
        });

        try {
            mAlertDialog.show();
        } catch (Exception e) {
            Log.e("showBasicAlertDialog", e.getMessage());
        }
        return mAlertDialog;

    }

    public static Dialog showJoinAsGuestDialog(final Context context, final DialogMangerOkEdtCallback dialogInterface) {

        if (mTwoAlertDialog != null && mTwoAlertDialog.isShowing()) {
            try {
                mTwoAlertDialog.dismiss();
            } catch (Exception e) {
                Log.e("showBasicAlertDialog", e.getMessage());
            }
        }
        mTwoAlertDialog = getDialog(context, R.layout.dialog_join_guest);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mTwoAlertDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        mTwoAlertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogBoxAnimation;

        ImageView closeImg;
        final EditText nameEdt, meetingIdEdt;
        final Button joinAsGustBtn;

        //Init View
        closeImg = (ImageView) mTwoAlertDialog.findViewById(R.id.close_img);
        nameEdt = (EditText) mTwoAlertDialog.findViewById(R.id.name_edt);
        meetingIdEdt = (EditText) mTwoAlertDialog.findViewById(R.id.meeting_id_edt);
        joinAsGustBtn = (Button) mTwoAlertDialog.findViewById(R.id.join_as_btn);


        joinAsGustBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nameStr = nameEdt.getText().toString().trim();
                String meetingIdStr = meetingIdEdt.getText().toString().trim();


                if (nameStr.isEmpty()) {
                    showBasicAlertDialog(context, context.getString(R.string.enter_name), context.getString(R.string.ok), false, context.getString(R.string.ok), true, true, new DialogMangerTwoBtnCallback() {
                        @Override
                        public void onYesClick() {

                        }

                        @Override
                        public void onNoClick() {

                        }
                    });


                } else if (meetingIdStr.isEmpty()) {
                    showBasicAlertDialog(context, context.getString(R.string.enter_meeting_id), context.getString(R.string.ok), false, context.getString(R.string.ok), true, true, new DialogMangerTwoBtnCallback() {
                        @Override
                        public void onYesClick() {

                        }

                        @Override
                        public void onNoClick() {

                        }
                    });


                } else {
                    EditTxtBoxHide(context, nameEdt);
                    EditTxtBoxHide(context, meetingIdEdt);
                    mTwoAlertDialog.dismiss();
                    dialogInterface.onOkEdtClick(nameStr, meetingIdStr);
                }
            }
        });

        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTxtBoxHide(context, nameEdt);
                EditTxtBoxHide(context, meetingIdEdt);
                mTwoAlertDialog.dismiss();
            }
        });

        try {
            mTwoAlertDialog.show();
        } catch (Exception e) {
            Log.e("showBasicAlertDialog", e.getMessage());
        }
        return mTwoAlertDialog;

    }

    public static Dialog showResetPwdDialog(final Context context, final DialogMangerOkEdtCallback dialogInterface) {

        if (mTwoAlertDialog != null && mTwoAlertDialog.isShowing()) {
            mTwoAlertDialog.dismiss();
            try {
                mTwoAlertDialog.dismiss();
            } catch (Exception e) {
                Log.e("showBasicAlertDialog", e.getMessage());
            }
        }
        mTwoAlertDialog = getDialog(context, R.layout.dialog_forgot_password);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mTwoAlertDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        mTwoAlertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogBoxAnimation;

        ImageView closeImg;
        final EditText emailEdt;
        Button submitBtn;

        //Init View
        closeImg = (ImageView) mTwoAlertDialog.findViewById(R.id.close_img);
        emailEdt = (EditText) mTwoAlertDialog.findViewById(R.id.email_id_edt);
        submitBtn = (Button) mTwoAlertDialog.findViewById(submit_btn);


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailStr = emailEdt.getText().toString().trim();


                if (emailStr.isEmpty() || (!GlobalMethods.isEmailValid(emailStr))) {
                    showBasicAlertDialog(context, context.getString(R.string.enter_email), context.getString(R.string.ok), false, context.getString(R.string.ok), true, true, new DialogMangerTwoBtnCallback() {
                        @Override
                        public void onYesClick() {

                        }

                        @Override
                        public void onNoClick() {

                        }
                    });


                } else {
                    EditTxtBoxHide(context, emailEdt);
                    mTwoAlertDialog.dismiss();
                    dialogInterface.onOkEdtClick(emailStr, "");
                }
            }
        });

        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTxtBoxHide(context, emailEdt);
                mTwoAlertDialog.dismiss();
            }
        });
        try {
            mTwoAlertDialog.show();
        } catch (Exception e) {
            Log.e("showBasicAlertDialog", e.getMessage());
        }
        return mTwoAlertDialog;

    }

    public static Dialog showJoinStreamingDialog(final Context context, final DialogMangerOkEdtCallback dialogInterface) {

        if (mTwoAlertDialog != null && mTwoAlertDialog.isShowing()) {
            try {
                mTwoAlertDialog.dismiss();
            } catch (Exception e) {
                Log.e("showBasicAlertDialog", e.getMessage());
            }
        }
        mTwoAlertDialog = getDialog(context, R.layout.dialog_join_streaming);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mTwoAlertDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        mTwoAlertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogBoxAnimation;

        ImageView closeImg;
        final EditText meetingIdEdt;
        Button submitBtn;

        //Init View
        closeImg = (ImageView) mTwoAlertDialog.findViewById(R.id.close_img);
        meetingIdEdt = (EditText) mTwoAlertDialog.findViewById(R.id.meeting_id_edt);
        submitBtn = (Button) mTwoAlertDialog.findViewById(submit_btn);


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String meetingIdStr = meetingIdEdt.getText().toString().trim();


                if (meetingIdStr.isEmpty()) {
                    showBasicAlertDialog(context, context.getString(R.string.enter_meeting_id), context.getString(R.string.ok), false, context.getString(R.string.ok), true, true, new DialogMangerTwoBtnCallback() {
                        @Override
                        public void onYesClick() {

                        }

                        @Override
                        public void onNoClick() {

                        }
                    });


                } else {
                    EditTxtBoxHide(context, meetingIdEdt);
                    mTwoAlertDialog.dismiss();
                    dialogInterface.onOkEdtClick("", meetingIdStr);
                }
            }
        });

        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTxtBoxHide(context, meetingIdEdt);
                mTwoAlertDialog.dismiss();
            }
        });

        try {
            mTwoAlertDialog.show();
        } catch (Exception e) {
            Log.e("showBasicAlertDialog", e.getMessage());
        }
        return mTwoAlertDialog;

    }


    private static void EditTxtBoxHide(Context context, EditText mEdtView) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEdtView.getWindowToken(), 0);
    }

    private static Dialog getLoadingDialog(Context mContext) {

        Dialog mDialog = getDialog(mContext, R.layout.dialog_progress);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);

        return mDialog;
    }

    public static void showProgress(Context context) {

        hideProgress();
        mProgressDialog = getLoadingDialog(context);
        Window window = mProgressDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        try {
            mProgressDialog.show();
        } catch (Exception e) {
            Log.e("showBasicAlertDialog", e.getMessage());
        }

    }

    public static void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            try {
                mProgressDialog.dismiss();
            } catch (Exception e) {
                Log.e("showBasicAlertDialog", e.getMessage());
            }
        }
    }

}
