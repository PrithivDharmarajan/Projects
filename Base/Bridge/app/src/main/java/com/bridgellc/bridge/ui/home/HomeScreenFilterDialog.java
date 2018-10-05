package com.bridgellc.bridge.ui.home;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.ui.upload.UploadScreen;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.TypefaceSingleton;

import java.util.Locale;

/**
 * Created by USER on 3/14/2016.
 */
public class HomeScreenFilterDialog {

    private static boolean isFilter = false;

    private static PopupWindow mPopupDialog;

    private static TextView titleTv;
    private static ImageView mHeaderLeftImage, mHeaderRightImage;
    private static Dialog mDialog;
    private static Typeface mLightFont, mRegulartFont;
    private static TextView all, academics, apparel, social, electronics, maintenance, errands, misc, ticket, mAllTxt, mShopTxt, mRequestTxt;

    public static void showFilterDialog(final Context mContext) {
        final Dialog mDialog = DialogManager.getDialog(mContext, R.layout.home_screen_filter);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mDialog.getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);


        mLightFont = TypefaceSingleton.getTypeface().getLightFont(mContext);
        mRegulartFont = TypefaceSingleton.getTypeface().getRegularFont(mContext);
        ViewGroup mViewGroup = (ViewGroup) mDialog.findViewById(R.id.parent_dia_lay);
        setFont(mViewGroup, mLightFont);

        titleTv = (TextView) mDialog.findViewById(R.id.header_txt);

        titleTv.setText(mContext.getResources().getString(R.string.home_c));
        titleTv.setTypeface(mLightFont);

        mHeaderLeftImage = (ImageView) mDialog.findViewById(R.id.header_left_img);
        mHeaderRightImage = (ImageView) mDialog.findViewById(R.id.header_right_img);
        mHeaderLeftImage.setImageResource(R.drawable.home_filter_icon);
        mHeaderRightImage.setImageResource(R.drawable.home_upload_icon);


        final LinearLayout parent_lay = (LinearLayout) mDialog.findViewById(R.id.anim_layout);

        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.filter_anim_in_left);

        parent_lay.setAnimation(anim);
        parent_lay.setVisibility(View.VISIBLE);

//        Drawable d = new ColorDrawable(Color.WHITE);
//        d.setAlpha(200);
//        mDialog.getWindow().setBackgroundDrawable(d);
//
        RelativeLayout bgLay = (RelativeLayout) mDialog.findViewById(R.id.parent_dia_lay);
        LinearLayout bgFilterLay = (LinearLayout) mDialog.findViewById(R.id.parent_lay);


//        Drawable d1 = new ColorDrawable(Color.parseColor("#F7F7F7"));
        Drawable d1 = new ColorDrawable(mContext.getResources().getColor(R.color.screen_bg));
        d1.setAlpha(2000);
        bgLay.setBackground(d1);
//        bgLay.setBackgroundDrawable(d1);
//        bgFilterLay.setBackgroundDrawable(d1);


        RelativeLayout headerLeft = (RelativeLayout) mDialog.findViewById(R.id.header_left_btn_lay);
        RelativeLayout headerRight = (RelativeLayout) mDialog.findViewById(R.id.header_right_btn_lay);

        headerLeft.setVisibility(View.VISIBLE);
        headerRight.setVisibility(View.VISIBLE);

        headerRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDialog.dismiss();
                closeFilterDialog();
//                if (GlobalMethods.getStringValue(mContext, AppConstants.BANK_DETAILS).equalsIgnoreCase(mContext.getString(R.string.one))) {
                Intent mIntent = new Intent(mContext, UploadScreen.class);
                mContext.startActivity(mIntent);
                ((Activity) mContext).overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
//                } else {
//                    AppConstants.BANK_ACC_DET_BACK_SCREEN = AppConstants.SUCCESS_CODE;
//
//                    Intent mIntent = new Intent(mContext, BankAccDetails.class);
//                    mContext.startActivity(mIntent);
//                    ((Activity) mContext).overridePendingTransition(R.anim.slide_in_right,
//                            R.anim.slide_out_left);

//                    nextScreen(BankAccDetails.class, false);
//                }

            }
        });

        final LinearLayout parent_lay1 = (LinearLayout) mDialog.findViewById(R.id.anim_layout);
        final Animation anim1 = AnimationUtils.loadAnimation(mContext, R.anim.filter_anim_in_left_hide);

        anim1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                RelativeLayout header = (RelativeLayout) mDialog.findViewById(R.id.header_lay);
                header.setVisibility(View.GONE);
                mDialog.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        headerLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                parent_lay1.setAnimation(anim1);
                parent_lay1.setVisibility(View.GONE);


            }
        });

        ImageView closeImage = (ImageView) mDialog.findViewById(R.id.close_img);
        closeImage.setImageResource(R.drawable.close_img);
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HomeScreenActivity.isFilterShown = false;
                parent_lay1.setAnimation(anim1);
                parent_lay1.setVisibility(View.GONE);


            }
        });


//        RadioGroup radioGroup = (RadioGroup) mDialog.findViewById(R.id.parent_lay);
//
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                HomeScreenActivity.filterType = (String) (((RadioButton) mDialog.findViewById(radioGroup.getCheckedRadioButtonId())).getTag());
//            }
//        });
//
//        if (HomeScreenActivity.filterType.length() > 0) {
//            RadioButton radioButton1 = (RadioButton) mDialog.findViewById(R.id.one);
//            RadioButton radioButton2 = (RadioButton) mDialog.findViewById(R.id.two);
//            RadioButton radioButton3 = (RadioButton) mDialog.findViewById(R.id.three);
//
//
//            String tag1 = (String) radioButton1.getTag();
//            String tag2 = (String) radioButton2.getTag();
//            String tag3 = (String) radioButton3.getTag();
//
//            if (tag1.equalsIgnoreCase(HomeScreenActivity.filterType)) {
//                radioButton1.setChecked(true);
//            } else if (tag2.equalsIgnoreCase(HomeScreenActivity.filterType)) {
//                radioButton2.setChecked(true);
//            } else {
//                radioButton3.setChecked(true);
//            }
//
//        }
//
//
//        HomeScreenActivity.filterType = (String) (((RadioButton) mDialog.findViewById(radioGroup.getCheckedRadioButtonId())).getTag());


        mAllTxt = (TextView) mDialog.findViewById(R.id.all_txt);
        mShopTxt = (TextView) mDialog.findViewById(R.id.shop_txt);
        mRequestTxt = (TextView) mDialog.findViewById(R.id.request_txt);



        all = (TextView) mDialog.findViewById(R.id.all);
        academics = (TextView) mDialog.findViewById(R.id.academics);
        apparel = (TextView) mDialog.findViewById(R.id.apparel);
        social = (TextView) mDialog.findViewById(R.id.social);
        electronics = (TextView) mDialog.findViewById(R.id.electronics);
        maintenance = (TextView) mDialog.findViewById(R.id.maintenance);
        errands = (TextView) mDialog.findViewById(R.id.errands);
        misc = (TextView) mDialog.findViewById(R.id.misc);
        ticket = (TextView) mDialog.findViewById(R.id.ticket);





        String tagone = (String) mAllTxt.getTag();
        String tagtwo = (String) mShopTxt.getTag();
        String tagthree = (String) mRequestTxt.getTag();
        if (HomeScreenActivity.filterType.length() > 0) {
            if (tagone.equalsIgnoreCase(HomeScreenActivity.filterType)) {
                filterTextSelect(mContext, 3);
            } else if (tagtwo.equalsIgnoreCase(HomeScreenActivity.filterType)) {
                filterTextSelect(mContext, 1);
            } else if (tagthree.equalsIgnoreCase(HomeScreenActivity.filterType)) {
                filterTextSelect(mContext, 2);
            }

        }


        mAllTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterTextSelect(mContext, 3);

            }
        });

        mShopTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                filterTextSelect(mContext, 1);

            }
        });


        mRequestTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                filterTextSelect(mContext, 2);
            }
        });




        all.setText(mContext.getString(R.string.all).toUpperCase(Locale.ENGLISH));
        academics.setText(mContext.getString(R.string.academics).toUpperCase(Locale.ENGLISH));
        apparel.setText(mContext.getString(R.string.apparel).toUpperCase(Locale.ENGLISH));
        social.setText(mContext.getString(R.string.social).toUpperCase(Locale.ENGLISH));
        electronics.setText(mContext.getString(R.string.electronics).toUpperCase(Locale.ENGLISH));
        maintenance.setText(mContext.getString(R.string.maintenance).toUpperCase(Locale.ENGLISH));
        errands.setText(mContext.getString(R.string.errands).toUpperCase(Locale.ENGLISH));
        misc.setText(mContext.getString(R.string.misc).toUpperCase(Locale.ENGLISH));
        ticket.setText(mContext.getString(R.string.ticket).toUpperCase(Locale.ENGLISH));

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                all.setTextColor(Color.parseColor("#8D8D8D"));
                academics.setTextColor(Color.parseColor("#8D8D8D"));
                apparel.setTextColor(Color.parseColor("#8D8D8D"));
                social.setTextColor(Color.parseColor("#8D8D8D"));
                electronics.setTextColor(Color.parseColor("#8D8D8D"));
                maintenance.setTextColor(Color.parseColor("#8D8D8D"));
                errands.setTextColor(Color.parseColor("#8D8D8D"));
                misc.setTextColor(Color.parseColor("#8D8D8D"));
                ticket.setTextColor(Color.parseColor("#8D8D8D"));

                ((TextView) view).setTextColor(Color.parseColor("#122E41"));
                HomeScreenActivity.filterOption = ((TextView) view).getText().toString().trim();
                ((HomeScreenActivity) mContext).populateData();
                parent_lay1.setAnimation(anim1);
                parent_lay1.setVisibility(View.GONE);
            }
        };


        all.setOnClickListener(onClickListener);
        academics.setOnClickListener(onClickListener);
        apparel.setOnClickListener(onClickListener);
        social.setOnClickListener(onClickListener);
        electronics.setOnClickListener(onClickListener);
        maintenance.setOnClickListener(onClickListener);
        errands.setOnClickListener(onClickListener);
        misc.setOnClickListener(onClickListener);
        ticket.setOnClickListener(onClickListener);


        if (HomeScreenActivity.filterOption.length() > 0) {

            if (HomeScreenActivity.filterOption.equalsIgnoreCase(all.getText().toString().trim())) {
                all.setTextColor(Color.parseColor("#122E41"));
            } else if (HomeScreenActivity.filterOption.equalsIgnoreCase(academics.getText().toString().trim())) {
                academics.setTextColor(Color.parseColor("#122E41"));
            } else if (HomeScreenActivity.filterOption.equalsIgnoreCase(apparel.getText().toString().trim())) {
                apparel.setTextColor(Color.parseColor("#122E41"));
            } else if (HomeScreenActivity.filterOption.equalsIgnoreCase(social.getText().toString().trim())) {
                social.setTextColor(Color.parseColor("#122E41"));
            } else if (HomeScreenActivity.filterOption.equalsIgnoreCase(electronics.getText().toString().trim())) {
                electronics.setTextColor(Color.parseColor("#122E41"));
            } else if (HomeScreenActivity.filterOption.equalsIgnoreCase(maintenance.getText().toString().trim())) {
                maintenance.setTextColor(Color.parseColor("#122E41"));
            } else if (HomeScreenActivity.filterOption.equalsIgnoreCase(errands.getText().toString().trim())) {
                errands.setTextColor(Color.parseColor("#122E41"));
            } else if (HomeScreenActivity.filterOption.equalsIgnoreCase(misc.getText().toString().trim())) {
                misc.setTextColor(Color.parseColor("#122E41"));
            } else if (HomeScreenActivity.filterOption.equalsIgnoreCase(ticket.getText().toString().trim())) {
                ticket.setTextColor(Color.parseColor("#122E41"));
            }
        } else {
            all.setTextColor(Color.parseColor("#122E41"));
        }

        mDialog.show();

        HomeScreenActivity.isFilterShown = true;
    }


    public static void closeFilterDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.cancel();
        }
    }

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

    public static void filterTextSelect(Context mContext, int pos) {
        if (pos == 3) {
            mAllTxt.setBackground(mContext.getResources().getDrawable(R.drawable.green_with_rounded_pg));
            mShopTxt.setBackground(null);
            mRequestTxt.setBackground(null);

            mAllTxt.setTextColor(mContext.getResources().getColor(R.color.white));
            mShopTxt.setTextColor(mContext.getResources().getColor(R.color.blue_gray));
            mRequestTxt.setTextColor(mContext.getResources().getColor(R.color.blue_gray));

//            ticket.setVisibility(View.VISIBLE);

            HomeScreenActivity.filterType = (String) mAllTxt.getTag();

        } else if (pos == 1) {
            mShopTxt.setBackground(mContext.getResources().getDrawable(R.drawable.green_with_rounded_pg));
            mAllTxt.setBackground(null);
            mRequestTxt.setBackground(null);

            mShopTxt.setTextColor(mContext.getResources().getColor(R.color.white));
            mAllTxt.setTextColor(mContext.getResources().getColor(R.color.blue_gray));
            mRequestTxt.setTextColor(mContext.getResources().getColor(R.color.blue_gray));

//            ticket.setVisibility(View.VISIBLE);

            HomeScreenActivity.filterType = (String) mShopTxt.getTag();
        } else {
            mRequestTxt.setBackground(mContext.getResources().getDrawable(R.drawable.green_with_rounded_pg));
            mShopTxt.setBackground(null);
            mAllTxt.setBackground(null);

            mRequestTxt.setTextColor(mContext.getResources().getColor(R.color.white));
            mShopTxt.setTextColor(mContext.getResources().getColor(R.color.blue_gray));
            mAllTxt.setTextColor(mContext.getResources().getColor(R.color.blue_gray));

//            ticket.setVisibility(View.GONE);

            HomeScreenActivity.filterType = (String) mRequestTxt.getTag();
        }

        filterTextSelection(mContext);
    }

    public static void filterTextSelection(Context mContext) {

        if (HomeScreenActivity.filterOption.length() > 0) {

            if (HomeScreenActivity.filterOption.equalsIgnoreCase(all.getText().toString().trim())) {
                all.setTextColor(Color.parseColor("#122E41"));
                HomeScreenActivity.filterOption = all.getText().toString().trim();
            } else if (HomeScreenActivity.filterOption.equalsIgnoreCase(academics.getText().toString().trim())) {
                academics.setTextColor(Color.parseColor("#122E41"));
                HomeScreenActivity.filterOption = academics.getText().toString().trim();
            } else if (HomeScreenActivity.filterOption.equalsIgnoreCase(apparel.getText().toString().trim())) {
                apparel.setTextColor(Color.parseColor("#122E41"));
                HomeScreenActivity.filterOption = apparel.getText().toString().trim();
            } else if (HomeScreenActivity.filterOption.equalsIgnoreCase(social.getText().toString().trim())) {
                social.setTextColor(Color.parseColor("#122E41"));
                HomeScreenActivity.filterOption = social.getText().toString().trim();
            } else if (HomeScreenActivity.filterOption.equalsIgnoreCase(electronics.getText().toString().trim())) {
                electronics.setTextColor(Color.parseColor("#122E41"));
                HomeScreenActivity.filterOption = electronics.getText().toString().trim();
            } else if (HomeScreenActivity.filterOption.equalsIgnoreCase(maintenance.getText().toString().trim())) {
                maintenance.setTextColor(Color.parseColor("#122E41"));
                HomeScreenActivity.filterOption = maintenance.getText().toString().trim();
            } else if (HomeScreenActivity.filterOption.equalsIgnoreCase(errands.getText().toString().trim())) {
                errands.setTextColor(Color.parseColor("#122E41"));
                HomeScreenActivity.filterOption = errands.getText().toString().trim();
            } else if (HomeScreenActivity.filterOption.equalsIgnoreCase(misc.getText().toString().trim())) {
                misc.setTextColor(Color.parseColor("#122E41"));
                HomeScreenActivity.filterOption = misc.getText().toString().trim();
            } else if (HomeScreenActivity.filterOption.equalsIgnoreCase(ticket.getText().toString().trim())) {
                ticket.setTextColor(Color.parseColor("#122E41"));
                HomeScreenActivity.filterOption = ticket.getText().toString().trim();
            }
        } else {
            all.setTextColor(Color.parseColor("#122E41"));
            HomeScreenActivity.filterOption = all.getText().toString().trim();
        }

        ((HomeScreenActivity) mContext).populateData();
    }
}
