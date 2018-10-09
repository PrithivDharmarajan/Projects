package com.smaat.spark.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.smaat.spark.R;

public class DialogManager {

    private static Dialog mProgressDialog;
    private Dialog mAlertDialog, mOptionDialog, mAccDeleteDialog, mOriginalImgDialog;

    private static final DialogManager sDialogInstance = new DialogManager();

    public static DialogManager getInstance() {
        return sDialogInstance;
    }

    private static Dialog getDialog(Context context, int layout) {
        Dialog mCommonDialog;
        mCommonDialog = new Dialog(context);
        mCommonDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (mCommonDialog.getWindow() != null) {
            mCommonDialog.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            mCommonDialog.setContentView(layout);
            mCommonDialog.getWindow().setGravity(Gravity.TOP);
            mCommonDialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));
        }
        mCommonDialog.setCancelable(false);
        mCommonDialog.setCanceledOnTouchOutside(false);

        return mCommonDialog;
    }

    public static void showToast(Context context, String message) {

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

    }

    //    public Dialog showAlertPopup(Context context, String message) {
//
//        alertDismiss(mAlertDialog);
//
//        mAlertDialog = getDialog(context, R.layout.popup_basic_alert);
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        Window window = mAlertDialog.getWindow();
//
//        if (window != null) {
//            lp.copyFrom(window.getAttributes());
//            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//            window.setAttributes(lp);
//            window.getAttributes().windowAnimations = R.style.PopupTopAnimation;
//        }
//
//        TextView msgTxt;
//        Button firstBtn;
//        ImageView infoImg;
//
//        //Init View
//        msgTxt = (TextView) mAlertDialog.findViewById(R.id.msg_txt);
//        firstBtn = (Button) mAlertDialog.findViewById(R.id.first_btn);
//        infoImg = (ImageView) mAlertDialog.findViewById(R.id.info_img);
//
//        //Set Data
//        msgTxt.setText(message);
//        firstBtn.setText(context.getString(R.string.ok));
//
//        //Check and Set Button Visibility
//        firstBtn.setVisibility(View.VISIBLE);
//
//        firstBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAlertDialog.dismiss();
//            }
//        });
//        infoImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAlertDialog.dismiss();
//            }
//        });
//
//        alertShowing(mAlertDialog);
//        return mAlertDialog;
//
//    }
    public void showAlertPopup(Context context, String message) {
        showToast(context, message);
    }

    public Dialog showOptionPopup(Context context, String message, String firstBtnName, String secondBtnName,
                                  final InterfaceTwoBtnCallback dialogAlertInterface) {
        alertDismiss(mOptionDialog);
        mOptionDialog = getDialog(context, R.layout.popup_basic_alert);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mOptionDialog.getWindow();

        if (window != null) {
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            window.getAttributes().windowAnimations = R.style.PopupTopAnimation;
        }

        TextView msgTxt;
        Button firstBtn, secondBtn;
        ImageView infoImg;
        View dividerView;
        //Init View
        msgTxt = (TextView) mOptionDialog.findViewById(R.id.msg_txt);
        firstBtn = (Button) mOptionDialog.findViewById(R.id.first_btn);
        secondBtn = (Button) mOptionDialog.findViewById(R.id.second_btn);
        infoImg = (ImageView) mOptionDialog.findViewById(R.id.info_img);
        dividerView = mOptionDialog.findViewById(R.id.divider_view);

        secondBtn.setVisibility(View.VISIBLE);
        dividerView.setVisibility(View.VISIBLE);


        //Set Data
        msgTxt.setText(message);
        firstBtn.setText(firstBtnName);
        secondBtn.setText(secondBtnName);
        infoImg.setImageResource(R.drawable.pop_up_caution_img);

        firstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOptionDialog.dismiss();
                dialogAlertInterface.onYesClick();
            }
        });
        secondBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOptionDialog.dismiss();
                dialogAlertInterface.onNoClick();
            }
        });
        infoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOptionDialog.dismiss();
                dialogAlertInterface.onNoClick();
            }
        });

        alertShowing(mOptionDialog);
        return mOptionDialog;

    }


    public Dialog showAccDeletePopup(Context context, final InterfaceTwoBtnCallback dialogAlertInterface) {
        alertDismiss(mAccDeleteDialog);
        mAccDeleteDialog = new Dialog(context);
        mAccDeleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mAccDeleteDialog.setContentView(R.layout.popup_acc_delete_alert);
        if (mAccDeleteDialog.getWindow() != null) {
            mAccDeleteDialog.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            mAccDeleteDialog.getWindow().setGravity(Gravity.BOTTOM);
            mAccDeleteDialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams layoutParams = mAccDeleteDialog.getWindow().getAttributes();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mAccDeleteDialog.getWindow().getAttributes().windowAnimations = R.style.PopupBottomAnimation;
        }


        Button deleteAccBtn, cancelBtn;

        deleteAccBtn = (Button) mAccDeleteDialog.findViewById(R.id.delete_acc_btn);
        cancelBtn = (Button) mAccDeleteDialog.findViewById(R.id.cancel_btn);

        deleteAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAccDeleteDialog.dismiss();
                dialogAlertInterface.onYesClick();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAccDeleteDialog.dismiss();
                dialogAlertInterface.onNoClick();
            }
        });

        alertShowing(mAccDeleteDialog);
        return mAccDeleteDialog;
    }

    public Dialog showOriginalImgPopup(Context context, final String imgUrl) {

        alertDismiss(mOriginalImgDialog);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View layoutView = inflater.inflate(R.layout.popup_image_viewer, null, false);

        mOriginalImgDialog = new Dialog(context,
                android.R.style.Theme_Translucent_NoTitleBar);
        mOriginalImgDialog = getDialog(context, R.layout.popup_image_viewer);
        mOriginalImgDialog.setContentView(layoutView);


        if (mOriginalImgDialog.getWindow() != null) {
            mOriginalImgDialog.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            mOriginalImgDialog.getWindow().setGravity(Gravity.CENTER);
            mOriginalImgDialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams layoutParams = mOriginalImgDialog.getWindow().getAttributes();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        }

        ImageView originalImg, closeImg;

        originalImg = (ImageView) mOriginalImgDialog
                .findViewById(R.id.original_img);
        closeImg = (ImageView) mOriginalImgDialog
                .findViewById(R.id.close_img);

        if (imgUrl.isEmpty() || imgUrl.equals(AppConstants.FAILURE_CODE)) {

            originalImg.setImageResource(R.drawable.default_user_img);
        } else {
            try {
                Glide.with(context)
                        .load(imgUrl).asBitmap().into(originalImg);
            } catch (Exception e) {
                Log.e(AppConstants.TAG, e.toString());
                originalImg.setImageResource(R.drawable.default_user_img);
            }
        }


        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOriginalImgDialog.dismiss();
            }
        });

        alertShowing(mOriginalImgDialog);
        return mOriginalImgDialog;
    }

    public static void showProgress(Context context) {

        hideProgress();
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

        try {
            if (mProgressDialog != null) {
                mProgressDialog.show();
            }
        } catch (Exception e) {
            Log.e(AppConstants.DIALOG_TAG, e.getMessage());
        }
    }

    public static void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            try {
                mProgressDialog.dismiss();
            } catch (Exception e) {
                Log.e(AppConstants.DIALOG_TAG, e.getMessage());
            }
        }
    }

    private void alertDismiss(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                Log.e(AppConstants.DIALOG_TAG, e.getMessage());
            }
        }
    }

    private void alertShowing(Dialog dialog) {
        if (dialog != null) {
            try {
                dialog.show();
            } catch (Exception e) {
                Log.e(AppConstants.DIALOG_TAG, e.getMessage());
            }
        }
    }
}
