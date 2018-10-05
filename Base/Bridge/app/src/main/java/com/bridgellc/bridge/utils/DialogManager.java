package com.bridgellc.bridge.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bumptech.glide.Glide;

import java.util.Locale;


public class DialogManager {
    //    static Dialog  imageDialog , mBaseTwoDialog;
    static Dialog mDialog, mCommonDialog, mProgressDialog, mAlertDialog;
    static String mode = "";
    static Typeface mRegulart, mLight;

    public static Dialog getDialog(Context mContext, int mLayout) {

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

    public static Dialog showBasicAlertDialog(Context mContext, String mMessage, final
    DialogMangerOkCallback mDialoginterface) {

        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }

        mAlertDialog = getDialog(mContext, R.layout.popup_alret);

        mRegulart = TypefaceSingleton.getTypeface().getRegularFont(mContext);
        mLight = TypefaceSingleton.getTypeface().getLightFont(mContext);

        ViewGroup mViewGroup = (ViewGroup) mAlertDialog.findViewById(R.id.parent_dia_lay);
        setFont(mViewGroup, mLight);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mAlertDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        mAlertDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        RelativeLayout mCloseLay = (RelativeLayout) mAlertDialog.findViewById(R.id.close_img_lay);
        TextView mTitleTxt = (TextView) mAlertDialog.findViewById(R.id.header_txt);
        TextView mMessageTxt = (TextView) mAlertDialog
                .findViewById(R.id.msg_txt);
        Button mOkBtn = (Button) mAlertDialog.findViewById(R.id.footer_btn);
        String mTitle = mContext.getString(R.string.app_name);
        mTitleTxt.setTypeface(mRegulart);
        mTitleTxt.setText(mTitle.toUpperCase(Locale.ENGLISH));
        mMessageTxt.setText(mMessage);
        mOkBtn.setText(mContext.getString(R.string.ok));
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mAlertDialog;

    }

    private static Dialog getLoadingDialog(Context mContext) {

        mDialog = getDialog(mContext, R.layout.progress);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);

        return mDialog;
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


//    public static void showValidationDialog(Context mContext, String mMessage) {
////      showBasicAlertDialog(mContext, mContext.getString(R.string.error_title), mMessage, new DialogMangerOkCallback() {
////          @Override
////          public void onOkClick() {
////
////          }
////      });
//
//        if (mDialog != null && mDialog.isShowing()) {
//            mDialog.dismiss();
//        }
//        mDialog = getDialog(mContext, R.layout.popup_alret);
//
//        mRegulart = TypefaceSingleton.getTypeface().getRegularFont(mContext);
//        mLight = TypefaceSingleton.getTypeface().getLightFont(mContext);
//
//        ViewGroup mViewGroup = (ViewGroup) mDialog.findViewById(R.id.parent_dia_lay);
//        setFont(mViewGroup, mLight);
//
//
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        Window window = mDialog.getWindow();
//        lp.copyFrom(window.getAttributes());
////This makes the dialog take up the full width
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        window.setAttributes(lp);
//
//        mDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
//
//        RelativeLayout mCloseLay = (RelativeLayout) mDialog.findViewById(R.id.close_img_lay);
//        TextView mTitleTxt = (TextView) mDialog.findViewById(R.id.header_txt);
//        TextView mMessageTxt = (TextView) mDialog
//                .findViewById(R.id.msg_txt);
//        Button mOkBtn = (Button) mDialog.findViewById(R.id.footer_btn);
//
//
//        mTitleTxt.setText(mContext.getString(R.string.app_name).toUpperCase(Locale.ENGLISH));
//        mTitleTxt.setTypeface(mRegulart);
//        mMessageTxt.setText(mMessage);
//        mOkBtn.setText(mContext.getString(R.string.ok));
//
//        mOkBtn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mDialog.dismiss();
//
//            }
//        });
//        mCloseLay.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mDialog.dismiss();
//            }
//        });
//        mDialog.show();
//    }


    public static Dialog shownAlertDialogProductPrice(Context mContext, String mTitle, String
            mMessage, String price, final DialogMangerOkCallback mDialoginterface) {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }

        mAlertDialog = getDialog(mContext, R.layout.popup_buy_product_price);

        mRegulart = TypefaceSingleton.getTypeface().getRegularFont(mContext);
        mLight = TypefaceSingleton.getTypeface().getLightFont(mContext);

        ViewGroup mViewGroup = (ViewGroup) mAlertDialog.findViewById(R.id.parent_dia_lay);
        setFont(mViewGroup, mLight);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mAlertDialog.getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        mAlertDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        RelativeLayout mCloseLay = (RelativeLayout) mAlertDialog.findViewById(R.id.close_img_lay);
        TextView mTitleTxt = (TextView) mAlertDialog.findViewById(R.id.header_txt);
        TextView mMessageTxt = (TextView) mAlertDialog
                .findViewById(R.id.msg_txt);
        final TextView mproduct_cost_Txt = (TextView) mAlertDialog.findViewById(R.id.product_cost_text);
        final TextView mService_cost_Txt = (TextView) mAlertDialog.findViewById(R.id.service_cost_text);
        final TextView mTotal_cost_Txt = (TextView) mAlertDialog.findViewById(R.id.total_cost_text);


        Button mYesBtn = (Button) mAlertDialog.findViewById(R.id.footer_one_btn);
        Button mNoBtn = (Button) mAlertDialog.findViewById(R.id.footer_two_btn);

        RelativeLayout mFooterOneLay = (RelativeLayout) mAlertDialog.findViewById(R.id.footer_one_lay);
        RelativeLayout mFooterTwoLay = (RelativeLayout) mAlertDialog.findViewById(R.id.footer_two_lay);

        mFooterOneLay.setVisibility(View.VISIBLE);
        mFooterTwoLay.setVisibility(View.VISIBLE);

        mFooterOneLay.setBackgroundColor(mContext.getResources().getColor(R.color.blue_btn_bg));
        mFooterTwoLay.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        mNoBtn.setTextColor(mContext.getResources().getColor(R.color.blue_btn_bg));
//        mTitle = mContext.getString(R.string.app_name);
        mTitleTxt.setText(mTitle.toUpperCase(Locale.ENGLISH));
        mTitleTxt.setTypeface(mRegulart);
        mMessageTxt.setText(mMessage);

        String cost = price.replace("$", "").trim();
        double pricecost = Float.parseFloat(cost);
        mproduct_cost_Txt.setText("$" + GlobalMethods.afterTwoPointVal(cost));


        double service_cost = 0.60;
        if (pricecost >= 1 && pricecost < 10) {
            service_cost = 0.60;
        } else if (pricecost >= 10 && pricecost < 133) {
            service_cost = 7.50;
        } else if (pricecost >= 133 && pricecost < 200) {
            service_cost = 10;
        } else if (pricecost >= 200) {
            service_cost = 5.00;
        }
        if (service_cost == 0.60 || service_cost == 10) {
            mService_cost_Txt.setText(mContext.getString(R.string.dollar_sym) + GlobalMethods.afterTwoPointVal(String.valueOf(service_cost)));
//            mTotal_cost_Txt.setText("$" + GlobalMethods.getPriValWithTwoPoint(String.format("%.2f", (pricecost + service_cost))));
            mTotal_cost_Txt.setText(mContext.getString(R.string.dollar_sym) + GlobalMethods.afterTwoPointVal(String.valueOf(pricecost + service_cost)));
        } else {

            double serviceTax = (double) ((pricecost * service_cost) / 100);
//            mService_cost_Txt.setText("$" + serviceTax);
            mService_cost_Txt.setText(mContext.getString(R.string.dollar_sym) + GlobalMethods.afterTwoPointVal(String.valueOf(serviceTax)));
//            mService_cost_Txt.setText("$" + GlobalMethods.getPriValWithTwoPoint(String.format("%.2f", serviceTax)));
//            mTotal_cost_Txt.setText("$" + GlobalMethods.getPriValWithTwoPoint(String.format("%.2f", (pricecost + serviceTax))));
            mTotal_cost_Txt.setText("$" + GlobalMethods.afterTwoPointVal(String.valueOf(pricecost + serviceTax)));
        }


        mYesBtn.setText(mContext.getString(R.string.yes));
        mNoBtn.setText(mContext.getString(R.string.no));
        mYesBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
                mDialoginterface.onOkClick();
            }
        });
        mNoBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
            }
        });
        mCloseLay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
            }
        });
        try {
            mAlertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mAlertDialog;
    }

//    public static Dialog showBasicBtnAlertDialog(Context mContext, String mTitle, String mMessage,
//                                            final DialogMangerOkCallback mDialoginterface) {
//        if (mAlertDialog != null && mAlertDialog.isShowing()) {
//            mAlertDialog.dismiss();
//        }
//
//        mAlertDialog = getDialog(mContext, R.layout.popup_alert_with_bg);
//
//        mRegulart = TypefaceSingleton.getTypeface().getRegularFont(mContext);
//        mLight = TypefaceSingleton.getTypeface().getLightFont(mContext);
//
//        ViewGroup mViewGroup = (ViewGroup) mAlertDialog.findViewById(R.id.parent_dia_lay);
//        setFont(mViewGroup, mLight);
//
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        Window window = mAlertDialog.getWindow();
//        lp.copyFrom(window.getAttributes());
////This makes the dialog take up the full width
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        window.setAttributes(lp);
//        mAlertDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
//        RelativeLayout mCloseLay = (RelativeLayout) mAlertDialog.findViewById(R.id.close_img_lay);
//        TextView mTitleTxt = (TextView) mAlertDialog.findViewById(R.id.header_txt);
//        TextView mMessageTxt = (TextView) mAlertDialog
//                .findViewById(R.id.msg_txt);
//        Button mYesBtn = (Button) mAlertDialog.findViewById(R.id.footer_one_btn);
//        Button mNoBtn = (Button) mAlertDialog.findViewById(R.id.footer_two_btn);
//
//        RelativeLayout mFooterOneLay = (RelativeLayout) mAlertDialog.findViewById(R.id.footer_one_lay);
//        RelativeLayout mFooterTwoLay = (RelativeLayout) mAlertDialog.findViewById(R.id.footer_two_lay);
//
//        mFooterOneLay.setVisibility(View.VISIBLE);
//        mFooterTwoLay.setVisibility(View.VISIBLE);
//
//        mYesBtn.setBackgroundColor(mContext.getResources().getColor(R.color.blue_btn_bg));
//        mNoBtn.setBackgroundColor(mContext.getResources().getColor(R.color.white));
//        mNoBtn.setTextColor(mContext.getResources().getColor(R.color.blue_btn_bg));
//        mTitle = mContext.getString(R.string.app_name);
//        mTitleTxt.setText(mTitle.toUpperCase(Locale.ENGLISH));
//        mTitleTxt.setTypeface(mRegulart);
//        mMessageTxt.setText(mMessage);
//        mYesBtn.setText(mContext.getString(R.string.yes));
//        mNoBtn.setText(mContext.getString(R.string.no));
//        mYesBtn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mAlertDialog.dismiss();
//                mDialoginterface.onOkClick();
//            }
//        });
//        mNoBtn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mAlertDialog.dismiss();
//            }
//        });
//        mCloseLay.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mAlertDialog.dismiss();
//            }
//        });
//
//        try {
//            mAlertDialog.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return  mAlertDialog;
//    }

    public static void showEditBtnAlertDialog(final Context mContext, String mTitle, String mMessage, String mBtnNameOne, String mBtnNameTwo, final DialogManagerTwoBtnCallback mDialoginterface) {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
        mAlertDialog = getDialog(mContext, R.layout.popup_alert_bid);

        mRegulart = TypefaceSingleton.getTypeface().getRegularFont(mContext);
        mLight = TypefaceSingleton.getTypeface().getLightFont(mContext);


        ViewGroup mViewGroup = (ViewGroup) mAlertDialog.findViewById(R.id.popup_parent_lay);
        setFont(mViewGroup, mLight);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mAlertDialog.getWindow();
        mAlertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        lp.copyFrom(window.getAttributes());


        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        mAlertDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        RelativeLayout mCloseLay = (RelativeLayout) mAlertDialog.findViewById(R.id.close_img_lay);

        TextView mTitleTxt = (TextView) mAlertDialog.findViewById(R.id.header_txt);
        TextView mMsgTxt = (TextView) mAlertDialog.findViewById(R.id.top_txt);
        final TextView mDollTxt = (TextView) mAlertDialog.findViewById(R.id.doll_sym_txt);
        final EditText mBidEdt = (EditText) mAlertDialog.findViewById(R.id.bid_edt);

        mBidEdt.requestFocus();
        Button mYesBtn = (Button) mAlertDialog.findViewById(R.id.footer_one_btn);
        Button mNoBtn = (Button) mAlertDialog.findViewById(R.id.footer_two_btn);

        RelativeLayout mFooterOneLay = (RelativeLayout) mAlertDialog.findViewById(R.id.footer_one_lay);
        RelativeLayout mFooterTwoLay = (RelativeLayout) mAlertDialog.findViewById(R.id.footer_two_lay);

        mFooterOneLay.setVisibility(View.VISIBLE);
        mFooterTwoLay.setVisibility(View.VISIBLE);

        mFooterOneLay.setBackgroundColor(mContext.getResources().getColor(R.color.blue_btn_bg));
        mFooterTwoLay.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        mNoBtn.setTextColor(mContext.getResources().getColor(R.color.blue_btn_bg));

        mTitleTxt.setText(mTitle.toUpperCase(Locale.ENGLISH));
        mTitleTxt.setTypeface(mRegulart);
        mMsgTxt.setText(mMessage);
        mYesBtn.setText(mBtnNameOne);
        mNoBtn.setText(mBtnNameTwo);
        mYesBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String mBidd = mBidEdt.getText().toString();
                if (mBidd.length() > 0 && editTxtValidation(mBidEdt)) {
                    EditTextHide(mContext, mBidEdt);
                    mAlertDialog.dismiss();
                    mDialoginterface.onBtnOkClick(mBidd);
                } else {
                    showBasicAlertDialog(mContext, mContext.getString(R.string.enter_bidd_amt), new DialogMangerOkCallback() {
                        @Override
                        public void onOkClick() {

                        }
                    });
                }
            }
        });
        mNoBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditTextHide(mContext, mBidEdt);
                mAlertDialog.dismiss();
                mDialoginterface.onBtnCancelClick(AppConstants.FAILURE_CODE);
            }
        });
        mCloseLay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditTextHide(mContext, mBidEdt);
                mAlertDialog.dismiss();
                mDialoginterface.onBtnCancelClick(AppConstants.FAILURE_CODE);

            }
        });
        try {
            mAlertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        InputFilter filter = new InputFilter() {
            final int maxDigitsBeforeDecimalPoint = 10;
            final int maxDigitsAfterDecimalPoint = 2;

            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                StringBuilder builder = new StringBuilder(dest);
                builder.replace(dstart, dend, source
                        .subSequence(start, end).toString());
                if (!builder.toString().matches(
                        "(([1-9]{1})([0-9]{0," + (maxDigitsBeforeDecimalPoint - 1) + "})?)?(\\.[0-9]{0," + maxDigitsAfterDecimalPoint + "})?"

                )) {
                    if (source.length() == 0)
                        return dest.subSequence(dstart, dend);
                    return "";
                }

                return null;

            }
        };

        mBidEdt.setFilters(new InputFilter[]{filter});
        mBidEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String mt = mBidEdt.getText().toString().trim();
                if (mt.length() > 0) {
                    mDollTxt.setVisibility(View.VISIBLE);
                } else {
                    mDollTxt.setVisibility(View.GONE);
                }

            }
        });
    }


    public static Dialog showBaseTwoBtnDialog(Context mContext, String mTitTxt, String mMsgTxt, String
            mButtonOneTxt, String mButtonTwoTxt, final DialogManagerTwoBtnCallback mDialoginterface) {

        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
        mAlertDialog = getDialog(mContext, R.layout.dialog_signout_alert);
        mAlertDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;

        mRegulart = TypefaceSingleton.getTypeface().getRegularFont(mContext);
        mLight = TypefaceSingleton.getTypeface().getLightFont(mContext);

        ViewGroup mViewGroup = (ViewGroup) mAlertDialog.findViewById(R.id.popup_parent_lay);
        setFont(mViewGroup, mLight);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mAlertDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        RelativeLayout mCloseLay = (RelativeLayout) mAlertDialog.findViewById(R.id.close_img_lay);
        TextView mTitleTxt = (TextView) mAlertDialog.findViewById(R.id.header_txt);
        TextView mMessageTxt = (TextView) mAlertDialog
                .findViewById(R.id.msg_txt);

        Button mOkBtn = (Button) mAlertDialog.findViewById(R.id.footer_one_btn);
        Button mCancelBtn = (Button) mAlertDialog.findViewById(R.id.footer_two_btn);

        RelativeLayout mFooterOneLay = (RelativeLayout) mAlertDialog.findViewById(R.id.footer_one_lay);
        RelativeLayout mFooterTwoLay = (RelativeLayout) mAlertDialog.findViewById(R.id.footer_two_lay);

        mFooterOneLay.setVisibility(View.VISIBLE);
        mFooterTwoLay.setVisibility(View.VISIBLE);

        mFooterOneLay.setBackgroundColor(mContext.getResources().getColor(R.color.blue_btn_bg));
        mFooterTwoLay.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        mCancelBtn.setTextColor(mContext.getResources().getColor(R.color.blue_btn_bg));


        mTitleTxt.setText(mTitTxt);
        mMessageTxt.setText(mMsgTxt);
        mOkBtn.setText(mButtonOneTxt);
        mCancelBtn.setText(mButtonTwoTxt);


        mTitleTxt.setTypeface(mRegulart);
        mMessageTxt.setTypeface(mLight);
        mOkBtn.setTypeface(mLight);
        mCancelBtn.setTypeface(mLight);


        mOkBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
                mDialoginterface.onBtnOkClick("");

            }
        });
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlertDialog.dismiss();
                mDialoginterface.onBtnCancelClick("");
            }
        });
        mCloseLay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
                mDialoginterface.onBtnCancelClick("");
            }
        });
        try {
            mAlertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mAlertDialog;
    }

//    public static void showNegAlertDialog(Context mContext, String mTitle, final DialogMangerOkCallback mDialoginterface) {
//        if (mAlertDialog != null && mAlertDialog.isShowing()) {
//            mAlertDialog.dismiss();
//        }
//
//        mAlertDialog = getDialog(mContext, R.layout.popup_alert_neg);
//
//        mRegulart = TypefaceSingleton.getTypeface().getRegularFont(mContext);
//        mLight = TypefaceSingleton.getTypeface().getLightFont(mContext);
//
//        ViewGroup mViewGroup = (ViewGroup) mAlertDialog.findViewById(R.id.popup_parent_lay);
//        setFont(mViewGroup, mLight);
//
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        Window window = mAlertDialog.getWindow();
//        lp.copyFrom(window.getAttributes());
//
//        //This makes the dialog take up the full width
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        window.setAttributes(lp);
//        mAlertDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
//        RelativeLayout mCloseLay = (RelativeLayout) mAlertDialog.findViewById(R.id.close_img_lay);
//        TextView mTitleTxt = (TextView) mAlertDialog.findViewById(R.id.header_txt);
//        TextView mMessageTxt = (TextView) mAlertDialog
//                .findViewById(R.id.msg_txt);
//        Button mYesBtn = (Button) mAlertDialog.findViewById(R.id.footer_one_btn);
//        Button mNoBtn = (Button) mAlertDialog.findViewById(R.id.footer_two_btn);
//
//        RelativeLayout mFooterOneLay = (RelativeLayout) mAlertDialog.findViewById(R.id.footer_one_lay);
//        RelativeLayout mFooterTwoLay = (RelativeLayout) mAlertDialog.findViewById(R.id.footer_two_lay);
//
//        mFooterOneLay.setVisibility(View.VISIBLE);
//        mFooterTwoLay.setVisibility(View.VISIBLE);
//
//        mYesBtn.setBackgroundColor(mContext.getResources().getColor(R.color.blue_btn_bg));
//        mNoBtn.setBackgroundColor(mContext.getResources().getColor(R.color.white));
//        mNoBtn.setTextColor(mContext.getResources().getColor(R.color.blue_btn_bg));
//        mTitle = mContext.getString(R.string.app_name);
//        mTitleTxt.setText(mTitle.toUpperCase(Locale.ENGLISH));
//        mTitleTxt.setTypeface(mRegulart);
//        mYesBtn.setText(mContext.getString(R.string.yes));
//        mNoBtn.setText(mContext.getString(R.string.no));
//        mMessageTxt.setText(mContext.getString(R.string.do_you_neg));
//        mYesBtn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mAlertDialog.dismiss();
//                mDialoginterface.onOkClick();
//            }
//        });
//        mNoBtn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mAlertDialog.dismiss();
//            }
//        });
//        mCloseLay.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mAlertDialog.dismiss();
//            }
//        });
//        try {
//            mAlertDialog.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void EditTextHide(Context context, EditText mEdtView) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEdtView.getWindowToken(), 0);
    }

    public static void ImageViewer(Context context, final String imgUrl) {

        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.image_viewer, null);

        mAlertDialog = new Dialog(context,
                android.R.style.Theme_Translucent_NoTitleBar);

        mAlertDialog = getDialog(context, R.layout.image_viewer);

        mAlertDialog.setContentView(layout);

        mCommonDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView closeBtn = (ImageView) mAlertDialog
                .findViewById(R.id.closeBtn);
        ImageView imageview = (ImageView) mAlertDialog
                .findViewById(R.id.imageview);
        if (imgUrl != null && !imgUrl.equalsIgnoreCase("")) {
            Glide.with(context)
                    .load(imgUrl)
                    .asBitmap().into(imageview);


        }
        closeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
            }
        });

        try {
            mAlertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    public static void showBasicAlertDialogAutomaticDismiss(Context mContext, String mTitle, String
//            mMessage, final DialogMangerOkCallback mDialoginterface) {
//
//        if (mAlertDialog != null && mAlertDialog.isShowing()) {
//            mAlertDialog.dismiss();
//        }
//
//        mAlertDialog = getDialog(mContext, R.layout.popup_alret);
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        Window window = mAlertDialog.getWindow();
//        lp.copyFrom(window.getAttributes());
////This makes the dialog take up the full width
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        window.setAttributes(lp);
//        mAlertDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
//        RelativeLayout mCloseLay = (RelativeLayout) mAlertDialog.findViewById(R.id.close_img_lay);
//        TextView mTitleTxt = (TextView) mAlertDialog.findViewById(R.id.header_txt);
//        TextView mMessageTxt = (TextView) mAlertDialog
//                .findViewById(R.id.msg_txt);
//        Button mOkBtn = (Button) mAlertDialog.findViewById(R.id.footer_btn);
//
//        mTitleTxt.setText(mTitle.toUpperCase(Locale.ENGLISH));
//        mMessageTxt.setText(mMessage);
//        mOkBtn.setText(mContext.getString(R.string.ok));
//        mOkBtn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mAlertDialog.dismiss();
//                mDialoginterface.onOkClick();
//            }
//        });
//
//
//        final Handler handler = new Handler();
//        final Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                if (mAlertDialog.isShowing()) {
//                    mAlertDialog.dismiss();
//                    handler.postDelayed(this, 10000);
//                    mDialoginterface.onOkClick();
//                }
//            }
//        };
//
//        mAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                handler.removeCallbacks(runnable);
//            }
//        });
//
//        handler.postDelayed(runnable, 10000);
//
//
//        mCloseLay.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mAlertDialog.dismiss();
//            }
//        });
//
//
//        try {
//            mAlertDialog.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void setFont(ViewGroup mViewGroup, Typeface mTypeface) {

        int mCount = mViewGroup.getChildCount();
        View mView;
        for (int i = 0; i < mCount; i++) {
            mView = mViewGroup.getChildAt(i);

            if (mView instanceof TextView || mView instanceof Button
                    || mView instanceof EditText/* etc. */) {
                ((TextView) mView).setTypeface(mTypeface);
            }
            if (mView instanceof ViewGroup) {
                setFont((ViewGroup) mView, mTypeface);
            }
        }

    }

    public static boolean editTxtValidation(EditText editText) {

        boolean editTxtValid;

        String mval = editText.getText().toString().trim();
        if (mval.contains(".")) {
            String mV[] = mval.split("\\.");
            if (mV.length == 2) {
                mval = GlobalMethods.afterTwoPointVal(mval);
            } else {
                mval = mval + ".00";
            }
        } else {
            mval = mval + ".00";
        }
        editTxtValid = (!mval.equalsIgnoreCase(AppConstants.FAILURE_CODE) && !mval.equalsIgnoreCase(GlobalMethods.afterTwoPointVal(AppConstants.FAILURE_CODE)));
        editText.setText(mval);
        editText.setSelection(mval.length());


        return editTxtValid;
    }

}
