package com.smaat.renterblock.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.GoogleMap;
import com.smaat.renterblock.R;
import com.smaat.renterblock.adapters.PlaceListAdapter;
import com.smaat.renterblock.adapters.PropertyTypeAdapter;
import com.smaat.renterblock.adapters.RecommendedReviewsAdapter;
import com.smaat.renterblock.adapters.SelectPropertyAdapter;
import com.smaat.renterblock.entity.LocalSavedSearchEntity;
import com.smaat.renterblock.entity.PropertyDialogEntity;
import com.smaat.renterblock.entity.PropertyReview;
import com.smaat.renterblock.fragments.TabFilterFragment;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.AddressResponse;
import com.smaat.renterblock.model.PlaceDescription;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class DialogManager {

    /*Init variable*/
    private Dialog mProgressDialog, mAlertDialog, mOptionDialog, mForgotPwdDialog, mSelectPropDialog, mMapLayerDialog,
            mSaveSearchDialog, mSortDialog, mPropertyTypeDialog, mReviewDialog, mImageUploadDialog, mDescriptionDialog, mUpdateUsernameDialog,
            mChangePasswordDialog, mShowImageVideoDialog, mShowImageDialog, mAddAlertDialog, mAlertNameDialog, mCallSelectionDialog, mShowImagePropertyDialog;
    private Toast mToast;
    private boolean isVideoTouchBool = false, isClickBool = false;
    private int stopPosition;

    private ImageView mBestMatchImg, mLatestUpdatesImg, mPriceLowImg, mPriceHighImg, mBathImg, mBedRoomImg, mSqftImg, mRatingImg;


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


    public void showAlertPopup(Context context, String message, final InterfaceBtnCallback dialogAlertInterface) {

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

        TextView msgTxt;
        Button firstBtn;

        /*Init view*/
        msgTxt = mAlertDialog.findViewById(R.id.msg_txt);
        firstBtn = mAlertDialog.findViewById(R.id.first_btn);

        /*Set data*/
        msgTxt.setText(message);
        firstBtn.setText(context.getString(R.string.ok));

        //Check and set button visibility
        firstBtn.setVisibility(View.VISIBLE);

        firstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
                dialogAlertInterface.onPositiveClick();
            }
        });

        alertShowing(mAlertDialog);

    }


    public void showOptionPopup(Context context, String message, String firstBtnName, String secondBtnName,
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
        Button firstBtn, secondBtn;

        /*Init view*/
        msgTxt = mOptionDialog.findViewById(R.id.msg_txt);
        firstBtn = mOptionDialog.findViewById(R.id.first_btn);
        secondBtn = mOptionDialog.findViewById(R.id.second_btn);

        secondBtn.setVisibility(View.VISIBLE);

        /*Set data*/
        msgTxt.setText(message);
        firstBtn.setText(firstBtnName);
        secondBtn.setText(secondBtnName);

        firstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOptionDialog.dismiss();
                dialogAlertInterface.onPositiveClick();
            }
        });
        secondBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOptionDialog.dismiss();
                dialogAlertInterface.onNegativeClick();
            }
        });

        alertShowing(mOptionDialog);

    }


    public void showPopupBlockUser(Context context,String mBlockTxtStr,
                                final InterfaceTwoBtnCallback dialogAlertInterface) {
        alertDismiss(mOptionDialog);
        mOptionDialog = getDialog(context, R.layout.popup_dialog_block_user);

        WindowManager.LayoutParams LayoutParams = new WindowManager.LayoutParams();
        Window window = mOptionDialog.getWindow();

        if (window != null) {
            LayoutParams.copyFrom(window.getAttributes());
            LayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            LayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(LayoutParams);
//            window.setGravity(Gravity.CENTER);
        }

        TextView mBlockTxt;
        RelativeLayout blockLay, reportLay,cancelLay;

        /*Init view*/
        blockLay = mOptionDialog.findViewById(R.id.popup_block_lay);
        reportLay = mOptionDialog.findViewById(R.id.popup_report_lay);
        cancelLay = mOptionDialog.findViewById(R.id.popup_cancel_lay);
        mBlockTxt = mOptionDialog.findViewById(R.id.popup_block_txt);

        mBlockTxt.setText(mBlockTxtStr);

        blockLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOptionDialog.dismiss();
                dialogAlertInterface.onPositiveClick();
            }
        });
        reportLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOptionDialog.dismiss();
                dialogAlertInterface.onNegativeClick();
            }
        });

        cancelLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOptionDialog.dismiss();
            }
        });
        alertShowing(mOptionDialog);

    }

    public void selectPropertyTypePopup(final Context context, final InterfaceEdtWithBtnCallback dialogInterface) {
        List<String> mPropertyTypeArray = new ArrayList<>();
        alertDismiss(mSelectPropDialog);
        mSelectPropDialog = getDialog(context, R.layout.popup_property_type);

        WindowManager.LayoutParams LayoutParams = new WindowManager.LayoutParams();
        Window window = mSelectPropDialog.getWindow();

        if (window != null) {
            LayoutParams.copyFrom(window.getAttributes());
            LayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            LayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(LayoutParams);
            window.setGravity(Gravity.CENTER);
        }

        RecyclerView recyclerView;
        TextView setTxt, cancelTxt;
        RelativeLayout backLay;
        /*Init view*/
        recyclerView = mSelectPropDialog.findViewById(R.id.type_recycler_view);
        cancelTxt = mSelectPropDialog.findViewById(R.id.cancel_txt);
        setTxt = mSelectPropDialog.findViewById(R.id.set_txt);
        backLay = mSelectPropDialog.findViewById(R.id.header_left_first_img_lay);

        String[] proStringsArray = context.getResources().getStringArray(R.array.property_types);
        mPropertyTypeArray = Arrays.asList(proStringsArray);

        /*set*/
        SelectPropertyAdapter mAdapter = new SelectPropertyAdapter(context, mPropertyTypeArray);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mAdapter);


        cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectPropDialog.dismiss();
            }
        });
        setTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectPropDialog.dismiss();
                dialogInterface.onFirstEdtBtnClick(AppConstants.SELECTED_PROPERTY_TYPE);
            }
        });
        backLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectPropDialog.dismiss();
            }
        });

        alertShowing(mSelectPropDialog);
    }

    public void showForgotPwdPopup(final Context context, final InterfaceEdtWithBtnCallback dialogInterface) {

        alertDismiss(mForgotPwdDialog);
        mForgotPwdDialog = getDialog(context, R.layout.popup_forgot_pwd);

        ImageView closeImg;
        final EditText emailEdt;
        Button cancelBtn, sendBtn;

        /*Init view*/
        emailEdt = mForgotPwdDialog.findViewById(R.id.email_edt);
        cancelBtn = mForgotPwdDialog.findViewById(R.id.cancel_btn);
        sendBtn = mForgotPwdDialog.findViewById(R.id.send_btn);


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailStr = emailEdt.getText().toString().trim();

                if (emailStr.isEmpty()) {
                    emailEdt.requestFocus();
                    showAlertPopup(context, context.getString(R.string.please_enter_email), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {

                        }
                    });
                } else if (!EmailUtil.isEmailValid(emailStr)) {
                    emailEdt.requestFocus();
                    showAlertPopup(context, context.getString(R.string.please_enter_valid_email), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {

                        }
                    });
                } else {

                    editTextKeyPadHidden(context, emailEdt);
                    mForgotPwdDialog.dismiss();
                    dialogInterface.onFirstEdtBtnClick(emailStr);
                }

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextKeyPadHidden(context, emailEdt);
                mForgotPwdDialog.dismiss();
            }
        });

        alertShowing(mForgotPwdDialog);


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


    public void showMapLayersDialog(final Context context, final InterfaceBtnCallback dialogBtnInterface) {
        alertDismiss(mMapLayerDialog);

        mMapLayerDialog = getDialog(context, R.layout.popup_layers);

        RelativeLayout mDefaultLay, mSatelliteLay, mHybridLay;
        final ImageView mDefaultImg, mSatelliteImg, mHybridImg;
        TextView mDoneTxt;

        int mapTypeInt = 0;

        mDefaultLay = mMapLayerDialog.findViewById(R.id.default_lay);
        mSatelliteLay = mMapLayerDialog.findViewById(R.id.satelite_lay);
        mHybridLay = mMapLayerDialog.findViewById(R.id.hybrid_lay);

        mDefaultImg = mMapLayerDialog.findViewById(R.id.default_img);
        mSatelliteImg = mMapLayerDialog.findViewById(R.id.satellite_img);
        mHybridImg = mMapLayerDialog.findViewById(R.id.hybrid_img);
        mDoneTxt = mMapLayerDialog.findViewById(R.id.done_txt);

        String mapTypeStr = PreferenceUtil.getStringValue(context, AppConstants.MAP_TYPE).isEmpty() ? GoogleMap.MAP_TYPE_NORMAL + "" : PreferenceUtil.getStringValue(context, AppConstants.MAP_TYPE);

        mapTypeInt = Integer.valueOf(mapTypeStr);
        PreferenceUtil.storeStringValue(context, AppConstants.MAP_TYPE, mapTypeStr);

        mDefaultImg.setImageResource(mapTypeInt == GoogleMap.MAP_TYPE_NORMAL ? R.drawable.radio_enable : R.drawable.radio_disable);
        mSatelliteImg.setImageResource(mapTypeInt == GoogleMap.MAP_TYPE_SATELLITE ? R.drawable.radio_enable : R.drawable.radio_disable);
        mHybridImg.setImageResource(mapTypeInt == GoogleMap.MAP_TYPE_HYBRID ? R.drawable.radio_enable : R.drawable.radio_disable);

        mDefaultLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDefaultImg.setImageResource(R.drawable.radio_enable);
                mSatelliteImg.setImageResource(R.drawable.radio_disable);
                mHybridImg.setImageResource(R.drawable.radio_disable);
                PreferenceUtil.storeStringValue(context, AppConstants.MAP_TYPE, GoogleMap.MAP_TYPE_NORMAL + "");
            }
        });

        mSatelliteLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDefaultImg.setImageResource(R.drawable.radio_disable);
                mSatelliteImg.setImageResource(R.drawable.radio_enable);
                mHybridImg.setImageResource(R.drawable.radio_disable);
                PreferenceUtil.storeStringValue(context, AppConstants.MAP_TYPE, GoogleMap.MAP_TYPE_SATELLITE + "");
            }
        });

        mHybridLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDefaultImg.setImageResource(R.drawable.radio_disable);
                mSatelliteImg.setImageResource(R.drawable.radio_disable);
                mHybridImg.setImageResource(R.drawable.radio_enable);
                PreferenceUtil.storeStringValue(context, AppConstants.MAP_TYPE, GoogleMap.MAP_TYPE_HYBRID + "");

            }
        });

        mDoneTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMapLayerDialog.dismiss();
                dialogBtnInterface.onPositiveClick();
            }
        });

        alertShowing(mMapLayerDialog);

    }

    public Dialog saveSearchDialog(final Context context, String locationStr, final InterfaceEdtWithBtnCallback mCallback) {
        alertDismiss(mSaveSearchDialog);

        mSaveSearchDialog = getDialog(context, R.layout.popup_save_search_dialog);
        final EditText mPlaceNameEdt;
        Button mSaveBtn, mCancelBtn;

        mPlaceNameEdt = mSaveSearchDialog.findViewById(R.id.enter_search_name);
        mSaveBtn = mSaveSearchDialog.findViewById(R.id.save);
        mCancelBtn = mSaveSearchDialog.findViewById(R.id.cancel);
        mPlaceNameEdt.setText(locationStr);

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextKeyPadHidden(context, mPlaceNameEdt);
                if (!mPlaceNameEdt.getText().toString().isEmpty()) {
                    mCallback.onFirstEdtBtnClick(mPlaceNameEdt.getText().toString());
                } else {
                    DialogManager.getInstance().showToast(context, context.getString(R.string.enter_location_save));
                }
            }
        });
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextKeyPadHidden(context, mPlaceNameEdt);
                mSaveSearchDialog.dismiss();
            }
        });

        alertShowing(mSaveSearchDialog);
        return mSaveSearchDialog;
    }

    public void sortingDialog(final Context context, final InterfaceBtnCallback mCallback) {
        alertDismiss(mSortDialog);
        mSortDialog = getDialog(context, R.layout.popup_sort_dialog);

        RelativeLayout mBestMatchLay, mLatestUpdatesLay, mPriceHighToLowLay, mPriceLowToHighLay, mBathLay, mBedRoomsLay, mSqftLay, mRatingLay;
        Button mDoneBtn;

        mBestMatchLay = mSortDialog.findViewById(R.id.best_match_lay);
        mLatestUpdatesLay = mSortDialog.findViewById(R.id.latest_updates_lay);
        mPriceHighToLowLay = mSortDialog.findViewById(R.id.high_to_low_lay);
        mPriceLowToHighLay = mSortDialog.findViewById(R.id.low_to_high_lay);
        mBathLay = mSortDialog.findViewById(R.id.bathroom_lay);
        mBedRoomsLay = mSortDialog.findViewById(R.id.bedrooms_lay);
        mSqftLay = mSortDialog.findViewById(R.id.sqft_lay);
        mRatingLay = mSortDialog.findViewById(R.id.rating_lay);


        mBestMatchImg = mSortDialog.findViewById(R.id.best_img);
        mLatestUpdatesImg = mSortDialog.findViewById(R.id.latest_img);
        mPriceLowImg = mSortDialog.findViewById(R.id.low_to_high_img);
        mPriceHighImg = mSortDialog.findViewById(R.id.high_to_low_img);
        mBathImg = mSortDialog.findViewById(R.id.bath_img);
        mBedRoomImg = mSortDialog.findViewById(R.id.bed_img);
        mSqftImg = mSortDialog.findViewById(R.id.sqft_img);
        mRatingImg = mSortDialog.findViewById(R.id.rating_img);

        mDoneBtn = mSortDialog.findViewById(R.id.done_btn);

        String sortStr = PreferenceUtil.getStringValue(context, AppConstants.PROPERTY_SORT_TYPE);
        if (sortStr.isEmpty()) {
            PreferenceUtil.storeStringValue(context, AppConstants.PROPERTY_SORT_TYPE, AppConstants.SORT_BEST);
            sortStr = PreferenceUtil.getStringValue(context, AppConstants.PROPERTY_SORT_TYPE);
        }


        mBestMatchImg.setImageResource(sortStr.equalsIgnoreCase(AppConstants.SORT_BEST) ?
                R.drawable.radio_enable : R.drawable.radio_disable);

        mLatestUpdatesImg.setImageResource(sortStr.equalsIgnoreCase(AppConstants.SORT_LATEST) ?
                R.drawable.radio_enable : R.drawable.radio_disable);
        mPriceLowImg.setImageResource(sortStr.equalsIgnoreCase(AppConstants.SORT_PRICE_LOW) ?
                R.drawable.radio_enable : R.drawable.radio_disable);
        mPriceHighImg.setImageResource(sortStr.equalsIgnoreCase(AppConstants.SORT_PRICE_HIGH) ?
                R.drawable.radio_enable : R.drawable.radio_disable);
        mBathImg.setImageResource(sortStr.equalsIgnoreCase(AppConstants.SORT_BATH) ?
                R.drawable.radio_enable : R.drawable.radio_disable);
        mBedRoomImg.setImageResource(sortStr.equalsIgnoreCase(AppConstants.SORT_BED) ?
                R.drawable.radio_enable : R.drawable.radio_disable);
        mSqftImg.setImageResource(sortStr.equalsIgnoreCase(AppConstants.SORT_SQRT) ?
                R.drawable.radio_enable : R.drawable.radio_disable);
        mRatingImg.setImageResource(sortStr.equalsIgnoreCase(AppConstants.SORT_RATING) ?
                R.drawable.radio_enable : R.drawable.radio_disable);


        mDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSortDialog.dismiss();
                mCallback.onPositiveClick();
            }
        });

        mBestMatchLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeBackgroundImageInSortDialog(context, view);
            }
        });
        mLatestUpdatesLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeBackgroundImageInSortDialog(context, view);
            }
        });
        mPriceHighToLowLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeBackgroundImageInSortDialog(context, view);
            }
        });
        mPriceLowToHighLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeBackgroundImageInSortDialog(context, view);
            }
        });
        mBathLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeBackgroundImageInSortDialog(context, view);
            }
        });
        mBedRoomsLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeBackgroundImageInSortDialog(context, view);
            }
        });
        mSqftLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeBackgroundImageInSortDialog(context, view);
            }
        });
        mRatingLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeBackgroundImageInSortDialog(context, view);
            }
        });

        alertShowing(mSortDialog);
    }

    private void changeBackgroundImageInSortDialog(Context context, View view) {
        mBestMatchImg.setImageResource(R.drawable.radio_disable);
        mLatestUpdatesImg.setImageResource(R.drawable.radio_disable);
        mPriceLowImg.setImageResource(R.drawable.radio_disable);
        mPriceHighImg.setImageResource(R.drawable.radio_disable);
        mBathImg.setImageResource(R.drawable.radio_disable);
        mBedRoomImg.setImageResource(R.drawable.radio_disable);
        mSqftImg.setImageResource(R.drawable.radio_disable);
        mRatingImg.setImageResource(R.drawable.radio_disable);

        switch (view.getId()) {
            case R.id.best_match_lay:
                mBestMatchImg.setImageResource(R.drawable.radio_enable);
                PreferenceUtil.storeStringValue(context, AppConstants.PROPERTY_SORT_TYPE, AppConstants.SORT_BEST);
                break;
            case R.id.latest_updates_lay:
                mLatestUpdatesImg.setImageResource(R.drawable.radio_enable);
                PreferenceUtil.storeStringValue(context, AppConstants.PROPERTY_SORT_TYPE, AppConstants.SORT_LATEST);
                break;
            case R.id.low_to_high_lay:
                mPriceLowImg.setImageResource(R.drawable.radio_enable);
                PreferenceUtil.storeStringValue(context, AppConstants.PROPERTY_SORT_TYPE, AppConstants.SORT_PRICE_LOW);
                break;
            case R.id.high_to_low_lay:
                mPriceHighImg.setImageResource(R.drawable.radio_enable);
                PreferenceUtil.storeStringValue(context, AppConstants.PROPERTY_SORT_TYPE, AppConstants.SORT_PRICE_HIGH);
                break;
            case R.id.bathroom_lay:
                mBathImg.setImageResource(R.drawable.radio_enable);
                PreferenceUtil.storeStringValue(context, AppConstants.PROPERTY_SORT_TYPE, AppConstants.SORT_BATH);
                break;
            case R.id.bedrooms_lay:
                mBedRoomImg.setImageResource(R.drawable.radio_enable);
                PreferenceUtil.storeStringValue(context, AppConstants.PROPERTY_SORT_TYPE, AppConstants.SORT_BED);
                break;
            case R.id.sqft_lay:
                mSqftImg.setImageResource(R.drawable.radio_enable);
                PreferenceUtil.storeStringValue(context, AppConstants.PROPERTY_SORT_TYPE, AppConstants.SORT_SQRT);
                break;
            case R.id.rating_lay:
                mRatingImg.setImageResource(R.drawable.radio_enable);
                PreferenceUtil.storeStringValue(context, AppConstants.PROPERTY_SORT_TYPE, AppConstants.SORT_RATING);
                break;
        }
    }

    public void propertyTypeDialog(final Context mContext, String savedPropertyType, final InterfaceEdtWithBtnCallback mCallback) {
        alertDismiss(mPropertyTypeDialog);
        mPropertyTypeDialog = getDialog(mContext, R.layout.popup_property_type);
        RecyclerView mRecyclerTypeView;
        TextView mCancelTxt, mSetTxt;
        RelativeLayout mLeftImgLay;
        ArrayList<PropertyDialogEntity> mPropertyDialogList = new ArrayList<>();
        PropertyDialogEntity propertyDialogEntity;
        String[] mPropertyArray = mContext.getResources().getStringArray(R.array.Property_Type);


        mRecyclerTypeView = mPropertyTypeDialog.findViewById(R.id.type_recycler_view);
        mCancelTxt = mPropertyTypeDialog.findViewById(R.id.cancel_txt);
        mSetTxt = mPropertyTypeDialog.findViewById(R.id.set_txt);
        mLeftImgLay = mPropertyTypeDialog.findViewById(R.id.header_left_first_img_lay);


        if (savedPropertyType.contains(mContext.getString(R.string.all_types)) || savedPropertyType.isEmpty()) {
            PropertyDialogEntity mEntity = new PropertyDialogEntity();
            mEntity.setType(mContext.getString(R.string.all_types));
            mEntity.setIs_selected(true);
            mPropertyDialogList.add(mEntity);
            for (String i : mPropertyArray) {
                propertyDialogEntity = new PropertyDialogEntity();
                propertyDialogEntity.setType(i);
                propertyDialogEntity.setIs_selected(false);
                mPropertyDialogList.add(propertyDialogEntity);
            }
        } else {
            propertyDialogEntity = new PropertyDialogEntity();
            propertyDialogEntity.setType(mContext.getString(R.string.all_types));
            propertyDialogEntity.setIs_selected(false);
            mPropertyDialogList.add(propertyDialogEntity);
            propertyDialogEntity = new PropertyDialogEntity();
            if (savedPropertyType.contains(mContext.getString(R.string.single_family))) {
                propertyDialogEntity.setType(mContext.getString(R.string.single_family));
                propertyDialogEntity.setIs_selected(true);
                mPropertyDialogList.add(propertyDialogEntity);
            } else {
                propertyDialogEntity.setType(mContext.getString(R.string.single_family));
                propertyDialogEntity.setIs_selected(false);
                mPropertyDialogList.add(propertyDialogEntity);
            }
            propertyDialogEntity = new PropertyDialogEntity();
            if (savedPropertyType.contains(mContext.getString(R.string.condo))) {
                propertyDialogEntity.setType(mContext.getString(R.string.condo));
                propertyDialogEntity.setIs_selected(true);
                mPropertyDialogList.add(propertyDialogEntity);
            } else {
                propertyDialogEntity.setType(mContext.getString(R.string.condo));
                propertyDialogEntity.setIs_selected(false);
                mPropertyDialogList.add(propertyDialogEntity);
            }
            propertyDialogEntity = new PropertyDialogEntity();
            if (savedPropertyType.contains(mContext.getString(R.string.town_house))) {
                propertyDialogEntity.setType(mContext.getString(R.string.town_house));
                propertyDialogEntity.setIs_selected(true);
                mPropertyDialogList.add(propertyDialogEntity);
            } else {
                propertyDialogEntity.setType(mContext.getString(R.string.town_house));
                propertyDialogEntity.setIs_selected(false);
                mPropertyDialogList.add(propertyDialogEntity);
            }
            propertyDialogEntity = new PropertyDialogEntity();
            if (savedPropertyType.contains(mContext.getString(R.string.coop))) {
                propertyDialogEntity.setType(mContext.getString(R.string.coop));
                propertyDialogEntity.setIs_selected(true);
                mPropertyDialogList.add(propertyDialogEntity);
            } else {
                propertyDialogEntity.setType(mContext.getString(R.string.coop));
                propertyDialogEntity.setIs_selected(false);
                mPropertyDialogList.add(propertyDialogEntity);

            }
            propertyDialogEntity = new PropertyDialogEntity();
            if (savedPropertyType.contains(mContext.getString(R.string.apartment))) {

                propertyDialogEntity.setType(mContext.getString(R.string.apartment));
                propertyDialogEntity.setIs_selected(true);
                mPropertyDialogList.add(propertyDialogEntity);
            } else {
                propertyDialogEntity.setType(mContext.getString(R.string.apartment));
                propertyDialogEntity.setIs_selected(false);
                mPropertyDialogList.add(propertyDialogEntity);

            }
            propertyDialogEntity = new PropertyDialogEntity();
            if (savedPropertyType.contains(mContext.getString(R.string.loft))) {

                propertyDialogEntity.setType(mContext.getString(R.string.loft));
                propertyDialogEntity.setIs_selected(true);
                mPropertyDialogList.add(propertyDialogEntity);
            } else {
                propertyDialogEntity.setType(mContext.getString(R.string.loft));
                propertyDialogEntity.setIs_selected(false);
                mPropertyDialogList.add(propertyDialogEntity);

            }
            propertyDialogEntity = new PropertyDialogEntity();
            if (savedPropertyType.contains(mContext.getString(R.string.tic))) {

                propertyDialogEntity.setType(mContext.getString(R.string.tic));
                propertyDialogEntity.setIs_selected(true);
                mPropertyDialogList.add(propertyDialogEntity);
            } else {
                propertyDialogEntity.setType(mContext.getString(R.string.tic));
                propertyDialogEntity.setIs_selected(false);
                mPropertyDialogList.add(propertyDialogEntity);

            }
            propertyDialogEntity = new PropertyDialogEntity();
            if (savedPropertyType.contains(mContext.getString(R.string.apartment_condo))) {
                propertyDialogEntity.setType(mContext.getString(R.string.apartment_condo));
                propertyDialogEntity.setIs_selected(true);
                mPropertyDialogList.add(propertyDialogEntity);
            } else {
                propertyDialogEntity.setType(mContext.getString(R.string.apartment_condo));
                propertyDialogEntity.setIs_selected(false);
                mPropertyDialogList.add(propertyDialogEntity);
            }
            propertyDialogEntity = new PropertyDialogEntity();
            if (savedPropertyType.contains(mContext.getString(R.string.mobile_manufactured))) {
                propertyDialogEntity.setType(mContext.getString(R.string.mobile_manufactured));
                propertyDialogEntity.setIs_selected(true);
                mPropertyDialogList.add(propertyDialogEntity);
            } else {
                propertyDialogEntity.setType(mContext.getString(R.string.mobile_manufactured));
                propertyDialogEntity.setIs_selected(false);
                mPropertyDialogList.add(propertyDialogEntity);
            }
            propertyDialogEntity = new PropertyDialogEntity();
            if (savedPropertyType.contains(mContext.getString(R.string.farm_ranch))) {
                propertyDialogEntity.setType(mContext.getString(R.string.farm_ranch));
                propertyDialogEntity.setIs_selected(true);
                mPropertyDialogList.add(propertyDialogEntity);
            } else {
                propertyDialogEntity.setType(mContext.getString(R.string.farm_ranch));
                propertyDialogEntity.setIs_selected(false);
                mPropertyDialogList.add(propertyDialogEntity);
            }
            propertyDialogEntity = new PropertyDialogEntity();
            if (savedPropertyType.contains(mContext.getString(R.string.lot_land))) {
                propertyDialogEntity.setType(mContext.getString(R.string.lot_land));
                propertyDialogEntity.setIs_selected(true);
                mPropertyDialogList.add(propertyDialogEntity);
            } else {
                propertyDialogEntity.setType(mContext.getString(R.string.lot_land));
                propertyDialogEntity.setIs_selected(false);
                mPropertyDialogList.add(propertyDialogEntity);
            }
            propertyDialogEntity = new PropertyDialogEntity();
            if (savedPropertyType.contains(mContext.getString(R.string.multi_family))) {
                propertyDialogEntity.setType(mContext.getString(R.string.multi_family));
                propertyDialogEntity.setIs_selected(true);
                mPropertyDialogList.add(propertyDialogEntity);
            } else {
                propertyDialogEntity.setType(mContext.getString(R.string.multi_family));
                propertyDialogEntity.setIs_selected(false);
                mPropertyDialogList.add(propertyDialogEntity);
            }
            propertyDialogEntity = new PropertyDialogEntity();
            if (savedPropertyType.contains(mContext.getString(R.string.income_investment))) {
                propertyDialogEntity.setType(mContext.getString(R.string.income_investment));
                propertyDialogEntity.setIs_selected(true);
                mPropertyDialogList.add(propertyDialogEntity);
            } else {
                propertyDialogEntity.setType(mContext.getString(R.string.income_investment));
                propertyDialogEntity.setIs_selected(false);
                mPropertyDialogList.add(propertyDialogEntity);
            }
            propertyDialogEntity = new PropertyDialogEntity();
            if (savedPropertyType.contains(mContext.getString(R.string.houseboat))) {
                propertyDialogEntity.setType(mContext.getString(R.string.houseboat));
                propertyDialogEntity.setIs_selected(true);
                mPropertyDialogList.add(propertyDialogEntity);
            } else {
                propertyDialogEntity.setType(mContext.getString(R.string.houseboat));
                propertyDialogEntity.setIs_selected(false);
                mPropertyDialogList.add(propertyDialogEntity);
            }
        }

        PropertyTypeAdapter mAdapter = new PropertyTypeAdapter(mContext, mPropertyDialogList);
        GridLayoutManager manager = new GridLayoutManager(mContext, 1);
        mRecyclerTypeView.setLayoutManager(manager);
        mRecyclerTypeView.setNestedScrollingEnabled(false);
        mRecyclerTypeView.setFocusable(false);
        mRecyclerTypeView.setAdapter(mAdapter);

        mLeftImgLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onFirstEdtBtnClick(AppConstants.PROPERTY_TYPE);
                mPropertyTypeDialog.dismiss();
            }
        });
        mCancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onFirstEdtBtnClick(AppConstants.PROPERTY_TYPE);
                mPropertyTypeDialog.dismiss();
            }
        });
        mSetTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onFirstEdtBtnClick(AppConstants.PROPERTY_TYPE);
                mPropertyTypeDialog.dismiss();
            }
        });


        alertShowing(mPropertyTypeDialog);
    }

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


    private void editTextKeyPadHidden(Context context, EditText mEdtView) {
        /*Hiding keypad for user interaction*/
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEdtView.getWindowToken(), 0);
    }

    public Dialog showAllReviewsDialog(Context context, ArrayList<PropertyReview> mPropertyReviewList, BaseFragment mFragment) {

        alertDismiss(mReviewDialog);
        mReviewDialog = getDialog(context, R.layout.dialog_recommended_reviews);


        mReviewDialog = getDialog(context, R.layout.dialog_recommended_reviews);
        WindowManager.LayoutParams LayoutParams = new WindowManager.LayoutParams();
        Window window = mReviewDialog.getWindow();
        if (window != null) {
            LayoutParams.copyFrom(window.getAttributes());
            LayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            LayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(LayoutParams);
            window.setGravity(Gravity.CENTER);
        }
        mReviewDialog.setCancelable(false);
        LinearLayout mClose_Icon = (LinearLayout) mReviewDialog.findViewById(R.id.close_icon);


        RecyclerView mReviewsList = (RecyclerView) mReviewDialog.findViewById(R.id.reviews_list);
        RecommendedReviewsAdapter mReviewsAdapter;
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(context);
        mReviewsList.setLayoutManager(mLinearLayoutManager);
        mReviewsAdapter = new RecommendedReviewsAdapter(mFragment, mPropertyReviewList);
        mReviewsList.setAdapter(mReviewsAdapter);

        mClose_Icon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                alertDismiss(mReviewDialog);
            }
        });
        alertShowing(mReviewDialog);
        return mReviewDialog;
    }

    public void showReviewDialog(Context context, final PropertyReview propertyReview, final int pos, final InterfaceBtnCallback mCallback) {

        alertDismiss(mReviewDialog);
        mReviewDialog = getDialog(context, R.layout.dialog_recommended_review);

        TextView reviewUserName, reviewComment, recommandReviewTime, HeaderTxt;
        RatingBar recommandReviewRating;
        ImageView recommandReviewImage;
        Button mChatIconBtn;
        LinearLayout mBackIcon;

        reviewUserName = (TextView) mReviewDialog.findViewById(R.id.review_user_name);
        reviewComment = (TextView) mReviewDialog.findViewById(R.id.recommand_review_comment);
        recommandReviewTime = (TextView) mReviewDialog.findViewById(R.id.recommand_review_time);
        HeaderTxt = (TextView) mReviewDialog.findViewById(R.id.header_txt);
        recommandReviewRating = (RatingBar) mReviewDialog.findViewById(R.id.recommand_revie_rating);
        mChatIconBtn = (Button) mReviewDialog.findViewById(R.id.chat_icon_adapter);
        mBackIcon = (LinearLayout) mReviewDialog.findViewById(R.id.back_icon);

        if(propertyReview.getReview_user_details().size() > 0) {
            reviewUserName.setText(propertyReview.getReview_user_details().get(0).getUser_name());
            HeaderTxt.setText(propertyReview.getReview_user_details().get(0).getFirst_name() + " Review");
        }
        reviewComment.setText(propertyReview.getComments());
        recommandReviewTime.setText(TimeUtil.timeDiff(propertyReview.getDate_time()));
        float rating = Float.parseFloat(propertyReview.getRating());
        recommandReviewRating.setRating(rating);

        String UserID = PreferenceUtil.getStringValue(context, AppConstants.USER_ID);

        mBackIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDismiss(mReviewDialog);
            }
        });

        if (propertyReview.getReview_user_id().equalsIgnoreCase(UserID)) {
            mChatIconBtn.setVisibility(View.GONE);
        } else {
            mChatIconBtn.setVisibility(View.VISIBLE);
        }

        mChatIconBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDismiss(mReviewDialog);
                mCallback.onPositiveClick();
//                        P.adapterChatService(UserID,
//                        propertyReview.getReview_user_details().get(pos).getUser_id(), pos);
                // mPropertyReviewList.get(pos).getReview_user_details()
                // .get(0).getUser_id(), pos);
            }
        });

        alertShowing(mReviewDialog);

    }

    public void showImageUploadPopup(final Context context, String header, String btn1Name, String btn2Name, final InterfaceTwoBtnCallback mCallback) {
        alertDismiss(mImageUploadDialog);
        mImageUploadDialog = getDialog(context, R.layout.popup_photo_selection);
        TextView msgTxt;
        Button firstBtn, secondBtn, cancelBtn;

        /*Init view*/
        msgTxt = mImageUploadDialog.findViewById(R.id.alertTitle);
        firstBtn = mImageUploadDialog.findViewById(R.id.btnName1);
        secondBtn = mImageUploadDialog.findViewById(R.id.btnName2);
        cancelBtn = mImageUploadDialog.findViewById(R.id.cancel);

        /*Set data*/
        msgTxt.setText(header);
        firstBtn.setText(btn1Name);
        secondBtn.setText(btn2Name);

        firstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageUploadDialog.dismiss();
                mCallback.onNegativeClick();
            }
        });
        secondBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageUploadDialog.dismiss();
                mCallback.onPositiveClick();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImageUploadDialog.dismiss();
            }
        });

        alertShowing(mImageUploadDialog);
    }

    public void showDescriptionAlert(Context context, Bitmap image, final InterfaceTwoBtnWithStringCallback mCallback) {
        alertDismiss(mDescriptionDialog);
        mDescriptionDialog = getDialog(context, R.layout.post_photo_video_view);
        TextView post_desc = (TextView) mDescriptionDialog.findViewById(R.id.post_desc);
        LinearLayout cancel = (LinearLayout) mDescriptionDialog.findViewById(R.id.back_icon);
        final EditText mDescription = (EditText) mDescriptionDialog.findViewById(R.id.description);
        ImageView capture_image = (ImageView) mDescriptionDialog.findViewById(R.id.capture_image);


        WindowManager.LayoutParams LayoutParams = new WindowManager.LayoutParams();
        Window window = mDescriptionDialog.getWindow();

        if (window != null) {
            LayoutParams.copyFrom(window.getAttributes());
            LayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            LayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(LayoutParams);
            window.setGravity(Gravity.CENTER);
        }


        capture_image.setImageBitmap(image);

        post_desc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDescriptionDialog.dismiss();
                mCallback.onPositiveStringCallback(mDescription.getText().toString());

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDescriptionDialog.dismiss();
                mCallback.onNegativeCallback();
            }
        });

        alertShowing(mDescriptionDialog);
    }


    public void updateUsernameDialog(Context context,
                                     String usernameStr, final InterfaceEdtWithBtnCallback mCallback) {
        alertDismiss(mUpdateUsernameDialog);

        mUpdateUsernameDialog = getDialog(context, R.layout.popup_update_username);
        final EditText mUserNameEdt;
        TextView mUpdateTxt, mCancelTxt;

        mUserNameEdt = mUpdateUsernameDialog.findViewById(R.id.user_name_edt);
        mUpdateTxt = mUpdateUsernameDialog.findViewById(R.id.update_txt);
        mCancelTxt = mUpdateUsernameDialog.findViewById(R.id.cancel_txt);
        mUserNameEdt.setText(usernameStr);

        mUpdateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUpdateUsernameDialog.dismiss();
                mCallback.onFirstEdtBtnClick(mUserNameEdt.getText().toString());
            }
        });
        mCancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUpdateUsernameDialog.dismiss();
            }
        });

        alertShowing(mUpdateUsernameDialog);
    }

    public void changePasswordDialog(final Context context, final InterfaceTwoStrCallback mCallback) {
        alertDismiss(mChangePasswordDialog);

        mChangePasswordDialog = getDialog(context, R.layout.popup_change_password);
        final EditText mCurrentPasswordEdt, mNewPasswordEdt, mConfirmPasswordEdt;
        Button mUpdateBtn;
        ImageView mCancelImg;

        mCurrentPasswordEdt = mChangePasswordDialog.findViewById(R.id.current_pw_edt);
        mNewPasswordEdt = mChangePasswordDialog.findViewById(R.id.new_pw_edt);
        mConfirmPasswordEdt = mChangePasswordDialog.findViewById(R.id.confirm_pw_edt);
        mCancelImg = mChangePasswordDialog.findViewById(R.id.close_img);
        mUpdateBtn = mChangePasswordDialog.findViewById(R.id.update_btn);


        mUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidate(context, mCurrentPasswordEdt.getText().toString(),
                        mNewPasswordEdt.getText().toString(), mConfirmPasswordEdt.getText().toString())) {
                    mChangePasswordDialog.dismiss();
                    mCallback.onPositiveClick(mCurrentPasswordEdt.getText().toString(), mConfirmPasswordEdt.getText().toString());
                }
            }
        });
        mCancelImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChangePasswordDialog.dismiss();
            }
        });

        alertShowing(mChangePasswordDialog);
    }

    private boolean isValidate(Context context, String mCurrentpw, String mNewpw, String mConfirm_pw) {

        if (mCurrentpw.isEmpty()) {
            DialogManager.getInstance().showAlertPopup(context, context.getString(R.string.enter_current_pwd), new InterfaceBtnCallback() {
                @Override
                public void onPositiveClick() {

                }
            });
            return false;
        } else if (mNewpw.isEmpty()) {
            DialogManager.getInstance().showAlertPopup(context, context.getString(R.string.enter_new_pwd), new InterfaceBtnCallback() {
                @Override
                public void onPositiveClick() {

                }
            });
            return false;
        } else if (mConfirm_pw.isEmpty()) {
            DialogManager.getInstance().showAlertPopup(context, context.getString(R.string.enter_conf_pwd), new InterfaceBtnCallback() {
                @Override
                public void onPositiveClick() {

                }
            });
            return false;
        } else if (!mConfirm_pw.equals(mNewpw)) {
            DialogManager.getInstance().showAlertPopup(context, context.getString(R.string.pwd_does_not_match), new InterfaceBtnCallback() {
                @Override
                public void onPositiveClick() {

                }
            });
            return false;
        }
        return true;
    }

    public void showImageDialog(final Context context, String url) {
        alertDismiss(mShowImageDialog);
        mShowImageDialog = getDialog(context, R.layout.popup_chat_image_view);
        WindowManager.LayoutParams LayoutParams = new WindowManager.LayoutParams();
        Window window = mShowImageDialog.getWindow();
        if (window != null) {
            LayoutParams.copyFrom(window.getAttributes());
            LayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            LayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(LayoutParams);
            window.setGravity(Gravity.CENTER);
        }
        ImageView imageView, closeImage;
        imageView = mShowImageDialog.findViewById(R.id.image);
        closeImage = mShowImageDialog.findViewById(R.id.closeImg);

        if (url.isEmpty()) {
            imageView.setImageResource(R.drawable.default_profile_icon);
        } else {
            try {
                Glide.with(context)
                        .load(url).error(R.drawable.default_profile_icon)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);

            } catch (Exception ex) {
                imageView.setImageResource(R.drawable.default_profile_icon);
            }
        }
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShowImageDialog.dismiss();
            }
        });
        alertShowing(mShowImageDialog);
    }

    public void showImageVideoDialog(final Context context, String file_type, String file, String desc, String time,
                                     String friend_count, String review_count, String photos_count, String profile_name, String user_profile_image) {
        alertDismiss(mShowImageVideoDialog);

        mShowImageVideoDialog = getDialog(context, R.layout.profile_photo_video_full_view);

        WindowManager.LayoutParams LayoutParams = new WindowManager.LayoutParams();
        Window window = mShowImageVideoDialog.getWindow();

        if (window != null) {
            LayoutParams.copyFrom(window.getAttributes());
            LayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            LayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(LayoutParams);
            window.setGravity(Gravity.CENTER);
        }


        TextView mProfileNameTxt, mFriendCountTxt, mReviewCountTxt, mPhotosCountTxt, mDescribeTxt, mTimeTxt;
        final VideoView mVideoView;
        final TouchImageView mImageVIew;
        final ImageView mProfileImg, mPlayImg, mPauseImg, mCloseImg;
        final RelativeLayout mVideoLay;
        final ProgressBar mProgressVideo, mProgressImageBar;


        mProfileImg = mShowImageVideoDialog.findViewById(R.id.profile_img);
        mImageVIew = mShowImageVideoDialog.findViewById(R.id.image_view);
        mPlayImg = mShowImageVideoDialog.findViewById(R.id.video_play_img);
        mPauseImg = mShowImageVideoDialog.findViewById(R.id.video_pause_img);
        mVideoView = mShowImageVideoDialog.findViewById(R.id.video_view);
        mDescribeTxt = mShowImageVideoDialog.findViewById(R.id.desc_txt);
        mTimeTxt = mShowImageVideoDialog.findViewById(R.id.date_time_txt);
        mCloseImg = mShowImageVideoDialog.findViewById(R.id.close_img);
        mProfileNameTxt = mShowImageVideoDialog.findViewById(R.id.profile_name_txt);
        mFriendCountTxt = mShowImageVideoDialog.findViewById(R.id.friends_count_txt);
        mReviewCountTxt = mShowImageVideoDialog.findViewById(R.id.reviews_count_txt);
        mPhotosCountTxt = mShowImageVideoDialog.findViewById(R.id.photos_count_txt);
        mVideoLay = mShowImageVideoDialog.findViewById(R.id.video_lay);
        mProgressVideo = mShowImageVideoDialog.findViewById(R.id.video_progress);
        mProgressImageBar = mShowImageVideoDialog.findViewById(R.id.progress_bar);

        mProfileNameTxt.setText(profile_name);
        mFriendCountTxt.setText(friend_count);
        mReviewCountTxt.setText(review_count);
        mPhotosCountTxt.setText(photos_count);

        mTimeTxt.setText("Latest updated " + DateUtil.timeDiff(time));
        mDescribeTxt.setText(desc);

        if (user_profile_image.isEmpty()) {
            mProfileImg.setImageResource(R.drawable.default_profile_icon);
        } else {
            try {
                Glide.with(context)
                        .load(user_profile_image).error(R.drawable.default_profile_icon)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(mProfileImg);

            } catch (Exception ex) {
                mProfileImg.setImageResource(R.drawable.default_profile_icon);
            }
        }


        mCloseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShowImageVideoDialog.dismiss();
            }
        });

        if (file_type.equals("image")) {
            mImageVIew.setVisibility(View.VISIBLE);


            if (file.isEmpty()) {
                mImageVIew.setImageResource(R.drawable.default_prop_icon);
            } else {
                try {
                    Glide.with(context)
                            .load(file)
                            .asBitmap().override(350, 250).centerCrop()
                            .error(R.drawable.default_prop_icon)
                            .into(mImageVIew);
                } catch (Exception ex) {
                    mImageVIew.setImageResource(R.drawable.default_prop_icon);
                }
            }
        } else {  
            mVideoLay.setVisibility(View.VISIBLE);
            mProgressVideo.setVisibility(View.VISIBLE);
            mImageVIew.setVisibility(View.GONE);
            Uri video = Uri.parse(file);
            mVideoView.setVideoURI(video);

            mVideoView.setOnPreparedListener(new OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    mVideoView.setBackground(null);
                    mVideoView.start();
                    mProgressVideo.setVisibility(View.GONE);
                }
            });
            mVideoView.setOnErrorListener(new OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mVideoView.setVisibility(View.GONE);
                    DialogManager.getInstance().showToast(context, "Sorry! Video Cannot be Played.");
                    mShowImageVideoDialog.dismiss();
                    return true;
                }
            });
            mVideoView.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View arg0, MotionEvent arg1) {
                    isVideoTouchBool = !isVideoTouchBool;
                    mPauseImg.setVisibility(isVideoTouchBool ? View.VISIBLE : View.GONE);
                    return false;
                }
            });
            mPauseImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopPosition = mVideoView.getCurrentPosition();
                    mPauseImg.setVisibility(View.GONE);
                    mPlayImg.setVisibility(View.VISIBLE);
                    mVideoView.pause();

                }
            });
            mPlayImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPauseImg.setVisibility(View.GONE);
                    mPlayImg.setVisibility(View.GONE);
                    mVideoView.seekTo(stopPosition);
                    mVideoView.start();
                }
            });
            mVideoView.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mPlayImg.setVisibility(View.VISIBLE);
                }
            });


        }


        alertShowing(mShowImageVideoDialog);
    }

    public void addAlertDialog(final Context context, final String alertStr) {
        alertDismiss(mAddAlertDialog);
        mAddAlertDialog = getDialog(context, R.layout.frag_location_alert);
        final EditText locationEdt;
        Button cancelBtn, saveBtn;
        final RecyclerView placeNameRecyclerView;

        locationEdt = mAddAlertDialog.findViewById(R.id.enter_search_name);
        placeNameRecyclerView = mAddAlertDialog.findViewById(R.id.alert_location_recycler_view);

        cancelBtn = mAddAlertDialog.findViewById(R.id.cancel_btn);
        saveBtn = mAddAlertDialog.findViewById(R.id.save_btn);


//        WindowManager.LayoutParams LayoutParams = new WindowManager.LayoutParams();
//        Window window = mAddAlertDialog.getWindow();
//
//        if (window != null) {
//            LayoutParams.copyFrom(window.getAttributes());
//            LayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//            LayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
//            window.setAttributes(LayoutParams);
//            window.setGravity(Gravity.CENTER);
//        }

        if (!alertStr.isEmpty()) {
            locationEdt.setText(alertStr);
        }

        locationEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (!s.toString().isEmpty() && !isClickBool) {
                    APIRequestHandler.getInstance().callPlaceSuggestionAPIDialog(s.toString(), new InterfacePlacePredictionCallback() {
                        @Override
                        public void placeList(ArrayList<PlaceDescription> placeDescriptionArrayList) {
                            ArrayList<LocalSavedSearchEntity> suggestionList = new ArrayList<>();
                            if (placeDescriptionArrayList.size() > 0) {
                                for (int i = 0; i < placeDescriptionArrayList.size(); i++) {
                                    LocalSavedSearchEntity entity = new LocalSavedSearchEntity();
                                    entity.setLocation(placeDescriptionArrayList.get(i).getDescription());
                                    entity.setLatitude(placeDescriptionArrayList.get(i).getLatitude());
                                    entity.setLatitude(placeDescriptionArrayList.get(i).getLongitude());
                                    entity.setPlaceID(placeDescriptionArrayList.get(i).getPlace_id());
                                    suggestionList.add(entity);
                                }

                                setAdapter(placeNameRecyclerView, locationEdt, context, suggestionList);


                            }

                        }
                    });
                } else {
                    placeNameRecyclerView.setVisibility(View.GONE);

                }

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler.removeCallbacks(this);
                        isClickBool = false;
                    }
                }, 3000);


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAddAlertDialog.dismiss();

            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String m = locationEdt.getText().toString().trim();
                editTextKeyPadHidden(context, locationEdt);
                AppConstants.CALLED_FROM_ALERT_FRAG = true;
                if (AppConstants.ALERT_SELECTED_LIST != null && AppConstants.ALERT_SELECTED_LIST.size() > 0) {
                    AppConstants.ALERT_ENTITY = AppConstants.ALERT_SELECTED_LIST.get(0);
                    AppConstants.ALERT_ENTITY.setEditbool(true);
                }

                if (!locationEdt.getText().toString().isEmpty()) {

                    String addressURLStr = String.format(AppConstants.GET_DETAILS_ADDRESS_URL, locationEdt.getText().toString());
                    APIRequestHandler.getInstance().callGetUserAddressAPICallback(context, addressURLStr, new InterfaceAPICommonCallback() {
                        @Override
                        public void onRequestSuccess(Object obj) {
                            AddressResponse userAddressRes = (AddressResponse) obj;
                            AppConstants.ALERT_ENTITY.setLocation(locationEdt.getText().toString());
                            AppConstants.ALERT_ENTITY.setLatitude(userAddressRes.getResults().get(0).getGeometry().getLocation().getLat());
                            AppConstants.ALERT_ENTITY.setLongitude(userAddressRes.getResults().get(0).getGeometry().getLocation().getLng());
                            ((HomeScreen) context).addFragment(new TabFilterFragment());
                        }

                        @Override
                        public void onRequestFailure(Throwable r) {

                        }
                    });

                    //LatLng mLoc = AddressUtil.getLocationFromAddress(context, locationEdt.getText().toString());
//                    if (mLoc != null) {
//                        AppConstants.ALERT_ENTITY.setLocation(locationEdt.getText().toString());
//                        AppConstants.ALERT_ENTITY.setLatitude(String.valueOf(mLoc.latitude));
//                        AppConstants.ALERT_ENTITY.setLongitude(String.valueOf(mLoc.longitude));
//
//                        ((HomeScreen) context).addFragment(new TabFilterFragment());
//                    } else {
//                        showToast(context, context.getString(R.string.location_alert_msg));
//                    }

                    mAddAlertDialog.dismiss();
                } else {
                    showToast(context, context.getString(R.string.please_enter_location));
                }


            }
        });


        alertShowing(mAddAlertDialog);

    }

    private void setAdapter(final RecyclerView placeNameRecyclerView, final EditText editText, final Context context,
                            ArrayList<LocalSavedSearchEntity> suggestionList) {

        PlaceListAdapter adapter = new PlaceListAdapter(context, suggestionList, new InterfaceEdtWithBtnCallback() {
            @Override
            public void onFirstEdtBtnClick(String firstEdtStr) {
                isClickBool = true;
                editText.setText(firstEdtStr);
                placeNameRecyclerView.setVisibility(View.GONE);
            }
        });
        placeNameRecyclerView.setVisibility(View.VISIBLE);
        placeNameRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        placeNameRecyclerView.setAdapter(adapter);
    }

    public void alertNameDialog(final Context context, String alertNameStr, final InterfaceEdtWithBtnCallback mCallback) {
        alertDismiss(mAlertNameDialog);
        mAlertNameDialog = getDialog(context, R.layout.popup_alert_name);
        final EditText alertNameEdt;
        Button cancelBtn, saveBtn;

        alertNameEdt = mAlertNameDialog.findViewById(R.id.alert_name_edt);

        cancelBtn = mAlertNameDialog.findViewById(R.id.cancel_btn);
        saveBtn = mAlertNameDialog.findViewById(R.id.save_btn);
        if (!alertNameStr.isEmpty()) {
            alertNameEdt.setText(alertNameStr);
        }
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlertNameDialog.dismiss();

            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAlertNameDialog.dismiss();
                if (!alertNameEdt.getText().toString().isEmpty()) {
                    mCallback.onFirstEdtBtnClick(alertNameEdt.getText().toString().trim());

                } else {
                    showToast(context, context.getString(R.string.alert_msg));
                }
            }
        });
        alertShowing(mAlertNameDialog);
    }

    public void callSelection(final Context context) {
        alertDismiss(mCallSelectionDialog);
        mCallSelectionDialog = getDialog(context, R.layout.popup_call_selection);

        Button mVoiceCallBtn, mVideoCallBtn;

        mVoiceCallBtn = mCallSelectionDialog.findViewById(R.id.voice_call_btn);
        mVideoCallBtn = mCallSelectionDialog.findViewById(R.id.video_call_btn);

        mVoiceCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallSelectionDialog.dismiss();
            }
        });

        mVideoCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallSelectionDialog.dismiss();
            }
        });
        alertShowing(mCallSelectionDialog);
    }

    public void showPropertyImage(final Context context, String imageFile, String address, String amount, String lastUpdate, final InterfaceTwoBtnCallback mCallback) {
        alertDismiss(mShowImagePropertyDialog);
        mShowImagePropertyDialog = getDialog(context, R.layout.popup_property_detail_image_view);

        WindowManager.LayoutParams LayoutParams = new WindowManager.LayoutParams();
        Window window = mShowImagePropertyDialog.getWindow();
        if (window != null) {
            LayoutParams.copyFrom(window.getAttributes());
            LayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            LayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(LayoutParams);
            window.setGravity(Gravity.CENTER);
        }
        Button requestInfoBtn, reviewBtn;
        ImageView closeImg, propertyImg;
        TextView addressTxt, amountTxt, lastUpdateTxt;

        requestInfoBtn = mShowImagePropertyDialog.findViewById(R.id.request_info_details_btn);
        reviewBtn = mShowImagePropertyDialog.findViewById(R.id.review_btn);
        closeImg = mShowImagePropertyDialog.findViewById(R.id.close_img);
        propertyImg = mShowImagePropertyDialog.findViewById(R.id.property_img);
        addressTxt = mShowImagePropertyDialog.findViewById(R.id.address_txt);
        amountTxt = mShowImagePropertyDialog.findViewById(R.id.amount_txt);
        lastUpdateTxt = mShowImagePropertyDialog.findViewById(R.id.last_updated_txt);

        if (!imageFile.isEmpty()) {
            Glide.with(context)
                    .load(imageFile).error(R.drawable.default_prop_icon)
                    .into(propertyImg);

            addressTxt.setText(address);
        }


        String propertyAmtStr = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(amount));
        Integer validate = Integer.parseInt(amount);
        amountTxt.setText(context.getString(R.string.dollar) + (validate <= 2 ? amount : propertyAmtStr) + " " + context.getString(R.string.per_month));

        if (lastUpdate != null) {
            lastUpdateTxt.setText(context.getResources().getString(R.string.last_updated) + " " + TimeUtil.getTimeDifference(lastUpdate));
        }

        requestInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowImagePropertyDialog.dismiss();
                mCallback.onNegativeClick();
            }
        });
        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowImagePropertyDialog.dismiss();
                mCallback.onPositiveClick();
            }
        });
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowImagePropertyDialog.dismiss();
            }
        });


        alertShowing(mShowImagePropertyDialog);

    }
	
	  public void callArcPopUp(final Context context) {
        alertDismiss(mCallSelectionDialog);
        mCallSelectionDialog = getDialog(context, R.layout.pop_arc_menu);

        WindowManager.LayoutParams LayoutParams = new WindowManager.LayoutParams();
        Window window = mCallSelectionDialog.getWindow();

        if (window != null) {
            LayoutParams.copyFrom(window.getAttributes());
            LayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            LayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(LayoutParams);
//            window.setGravity(Gravity.CENTER);
        }

        RelativeLayout mParentLay,mCloseLay;
        ImageView mVideo_chat, mReviews,mPhotoVideo;

        mVideo_chat = mCallSelectionDialog.findViewById(R.id.video_chat);
        mReviews = mCallSelectionDialog.findViewById(R.id.reviews);
        mPhotoVideo = mCallSelectionDialog.findViewById(R.id.photo_video);
        mParentLay = mCallSelectionDialog.findViewById(R.id.popup_arc_parent_lay);
        mCloseLay = mCallSelectionDialog.findViewById(R.id.close_id_lay);

        mCloseLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallSelectionDialog.dismiss();
            }
        });

        mParentLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallSelectionDialog.dismiss();
            }
        });

        mVideo_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallSelectionDialog.dismiss();
            }
        });

        mReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallSelectionDialog.dismiss();
            }
        });
        mPhotoVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallSelectionDialog.dismiss();
            }
        });
        alertShowing(mCallSelectionDialog);
    }

}
