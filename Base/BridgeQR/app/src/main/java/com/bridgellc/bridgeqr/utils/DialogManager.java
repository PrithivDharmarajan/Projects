package com.bridgellc.bridgeqr.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridgeqr.R;

public class DialogManager {

    private static Dialog mCommonDialog, mProgressDialog, mAlertDialog;
    private static Typeface mLightFont, mRegularFont;

    private static Dialog getDialog(Context mContext, int mLayout) {
        if (mCommonDialog != null && mCommonDialog.isShowing()) {
            mCommonDialog.dismiss();
        }
        mCommonDialog = new Dialog(mContext);
        mCommonDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mCommonDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mCommonDialog.setContentView(mLayout);
        mCommonDialog.getWindow().setGravity(Gravity.BOTTOM);
        mCommonDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        mCommonDialog.setCancelable(false);
        mCommonDialog.setCanceledOnTouchOutside(false);

        return mCommonDialog;
    }

    public static Dialog showBasicAlertDialog(Context mContext, String mTitle, String mMessage, final DialogMangerOkCallback mDialoginterface) {

        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
        mAlertDialog = getDialog(mContext, R.layout.popup_alret);

        TextView mTitleTxt, mMessageTxt;
        Button mOkBtn;
        RelativeLayout mCloseLay;

        mLightFont = TypefaceSingleton.getTypeface().getLightFont(mContext);
        mRegularFont = TypefaceSingleton.getTypeface().getRegularFont(mContext);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mAlertDialog.getWindow();

        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        mAlertDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;

        mCloseLay = (RelativeLayout) mAlertDialog.findViewById(R.id.close_img_lay);
        mTitleTxt = (TextView) mAlertDialog.findViewById(R.id.header_txt);
        mMessageTxt = (TextView) mAlertDialog
                .findViewById(R.id.msg_txt);
        mOkBtn = (Button) mAlertDialog.findViewById(R.id.footer_btn);

        //Set Date
        mTitleTxt.setText(mTitle);
        mMessageTxt.setText(mMessage);
        mOkBtn.setText(mContext.getString(R.string.ok));

        //Set Font Style
        mTitleTxt.setTypeface(mRegularFont);
        mMessageTxt.setTypeface(mLightFont);
        mOkBtn.setTypeface(mLightFont);

        mOkBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
                mDialoginterface.onOkClick();
            }
        });
        mCloseLay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
                mDialoginterface.onOkClick();
            }
        });

        try {
            mAlertDialog.show();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        return mAlertDialog;
    }

    private static Dialog getLoadingDialog(Context mContext) {

        Dialog mLoadingDialog = getDialog(mContext, R.layout.progress);
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.setCanceledOnTouchOutside(false);

        return mLoadingDialog;
    }

    public static void showProgress(Context context) {
        hideProgress(context);
        mProgressDialog = getLoadingDialog(context);
        Window window = mProgressDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        mProgressDialog.show();

    }

    public static void hideProgress(Context context) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    public static Dialog showBasicBtnAlertDialog(Context mContext, String mTitle, String mMessage, String mBtnOneTxt, String mBtnTwoTxt, final DialogMangerOkCallback mDialogOkBtn) {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
        mAlertDialog = getDialog(mContext, R.layout.popup_twobtn_alert);


        TextView mTitleTxt, mMessageTxt;
        Button mOneBtn, mTwoBtn;
        RelativeLayout mCloseLay;

        mLightFont = TypefaceSingleton.getTypeface().getLightFont(mContext);
        mRegularFont = TypefaceSingleton.getTypeface().getRegularFont(mContext);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mAlertDialog.getWindow();
        lp.copyFrom(window.getAttributes());

        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        mAlertDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;

        mCloseLay = (RelativeLayout) mAlertDialog.findViewById(R.id.close_img_lay);
        mTitleTxt = (TextView) mAlertDialog.findViewById(R.id.header_txt);
        mMessageTxt = (TextView) mAlertDialog
                .findViewById(R.id.msg_txt);
        mOneBtn = (Button) mAlertDialog.findViewById(R.id.yes_btn);
        mTwoBtn = (Button) mAlertDialog.findViewById(R.id.no_btn);

        //Set Data
        mTitleTxt.setText(mTitle);
        mMessageTxt.setText(mMessage);
        mOneBtn.setText(mBtnOneTxt);
        mTwoBtn.setText(mBtnTwoTxt);

        //Set Font Style
        mTitleTxt.setTypeface(mRegularFont);
        mMessageTxt.setTypeface(mLightFont);
        mOneBtn.setTypeface(mLightFont);
        mTwoBtn.setTypeface(mLightFont);

        mOneBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
                mDialogOkBtn.onYesClick();

            }
        });
        mTwoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlertDialog.dismiss();
                mDialogOkBtn.onCancelClick();
            }
        });
        mCloseLay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
                mDialogOkBtn.onCancelClick();
            }
        });

        try {
            mAlertDialog.show();
        } catch (Exception ignored) {
            ignored.printStackTrace();

        }

        return mAlertDialog;
    }


}
