package com.fautus.fautusapp.utils;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fautus.fautusapp.R;
import com.fautus.fautusapp.fragment.TermsCondFragment;
import com.fautus.fautusapp.ui.HomeScreen;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Date;
import java.util.List;

import static com.fautus.fautusapp.R.id.cancel_txt;
import static com.fautus.fautusapp.R.id.close_txt;
import static com.fautus.fautusapp.R.id.continue_lay;
import static com.fautus.fautusapp.R.id.hint_txt;


public class DialogManager {

    /*Init variable*/
    private Dialog mProgressDialog, mBaseAlertDialog, mOptionAlertDialog, mBusinessAlertDialog, mBaseInfoAlertDialog,
            mPhotoShootReqAlertDialog, mPhotographerProgressAlertDialog, mPhotoSessionAlertDialog, mConsumerSessionCodeAlertDialog,
            mShowPhotographerConnectAlertDialog, mPictureAlertDialog, mLegalAlertDialog, mMomentAmtAlertDialog, mLowRatingAlertDialog,
            mEmilInviteAlertDialog, mPaymentTermsAlertDialog;
    private Toast mToast;
    private boolean mIsDialogCalledBool = false;

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
            Log.e(com.fautus.fautusapp.utils.AppConstants.TAG, e.getMessage());
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


    /*Default dialog init method*/
    public Dialog showAlertPopup(Context context, String headerStr, String messageStr, final InterfaceBtnCallback okBtnInterfaceCallback) {

        alertDismiss(mBaseAlertDialog);

        mBaseAlertDialog = getDialog(context, R.layout.popup_basic_alert);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mBaseAlertDialog.getWindow();

        if (window != null) {
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            window.setGravity(Gravity.CENTER);

        }

        TextView headerTxt, msgTxt, okTxt;

        //Init View
        headerTxt = mBaseAlertDialog.findViewById(R.id.header_txt);
        msgTxt = mBaseAlertDialog.findViewById(R.id.msg_txt);
        okTxt = mBaseAlertDialog.findViewById(R.id.ok_txt);

        //Set Data
        headerTxt.setText(headerStr);
        msgTxt.setText(messageStr);

        okTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBaseAlertDialog.dismiss();
                okBtnInterfaceCallback.onOkClick();
            }
        });

        alertShowing(mBaseAlertDialog);
        return mBaseAlertDialog;
    }


    /*Dialog for double button click*/
    public Dialog showOptionAlertPopup(Context context, String headerStr, String messageStr, String btnOneNameStr, String btnTwoNameStr, final InterfaceTwoBtnCallback twoBtnInterfaceCallback) {

         /*To check if the dialog is shown, if the dialog is shown it will be cancelled */
        alertDismiss(mOptionAlertDialog);

        mOptionAlertDialog = getDialog(context, R.layout.popup_option_alert);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mOptionAlertDialog.getWindow();

        if (window != null) {
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            window.setGravity(Gravity.CENTER);

        }

        TextView headerTxt, msgTxt;
        Button firstBtn, secondBtn;

        //Init View
        headerTxt = mOptionAlertDialog.findViewById(R.id.header_txt);
        msgTxt = mOptionAlertDialog.findViewById(R.id.msg_txt);
        firstBtn = mOptionAlertDialog.findViewById(R.id.first_btn);
        secondBtn = mOptionAlertDialog.findViewById(R.id.second_btn);

        //Set Data
        headerTxt.setText(headerStr);
        msgTxt.setText(messageStr);
        firstBtn.setText(btnOneNameStr);
        secondBtn.setText(btnTwoNameStr);

        firstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOptionAlertDialog.dismiss();
                twoBtnInterfaceCallback.onNoClick();
            }
        });
        secondBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOptionAlertDialog.dismiss();
                twoBtnInterfaceCallback.onYesClick();
            }
        });

        /*To check if the dialog is null or not. if it's not a null, the dialog will be shown orelse it will not get appeared*/
        alertShowing(mOptionAlertDialog);
        return mOptionAlertDialog;
    }


    public Dialog showPhotographerBusinessAlertPopup(final Context context, String businessNameFromDBStr, String webAddressFromDBStr, final InterfaceTwoEdtCallback okEdtInterfaceCallback) {

	/*To check if the dialog is shown, if the dialog is shown it will be cancelled */
        alertDismiss(mBusinessAlertDialog);

        mBusinessAlertDialog = getDialog(context, R.layout.popup_business_address_alert);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mBusinessAlertDialog.getWindow();

        if (window != null) {
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            window.setGravity(Gravity.CENTER);
        }

        final EditText businessNameEdt, webAddressEdt;
        final TextView cancelTxt;
        LinearLayout continueLay;

        //Init View
        businessNameEdt = mBusinessAlertDialog.findViewById(R.id.business_name_edt);
        webAddressEdt = mBusinessAlertDialog.findViewById(R.id.web_address_edt);
        continueLay = mBusinessAlertDialog.findViewById(continue_lay);
        cancelTxt = mBusinessAlertDialog.findViewById(cancel_txt);

        /*set Txt*/
        businessNameEdt.setText(businessNameFromDBStr);
        webAddressEdt.setText(webAddressFromDBStr);

        businessNameEdt.setSelection(businessNameFromDBStr.length());
        webAddressEdt.setSelection(webAddressFromDBStr.length());

        continueLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String businessNameStr, webAddressStr;
                businessNameStr = businessNameEdt.getText().toString().trim();
                webAddressStr = webAddressEdt.getText().toString().trim();
                if (businessNameStr.isEmpty()) {
                    showAlertPopup(context, context.getString(R.string.req_business_topic), context.getString(R.string.req_business_msg), new InterfaceBtnCallback() {
                        @Override
                        public void onOkClick() {

                        }
                    });

                } else if (webAddressStr.isEmpty()) {
                    showAlertPopup(context, context.getString(R.string.req_web_topic), context.getString(R.string.req_web_msg), new InterfaceBtnCallback() {
                        @Override
                        public void onOkClick() {

                        }
                    });
                } else {
                    editTextKeyPadHidden(context, businessNameEdt);
                    editTextKeyPadHidden(context, webAddressEdt);
                    mBusinessAlertDialog.dismiss();
                    okEdtInterfaceCallback.onEdtOneClick(businessNameStr, webAddressStr);
                }

            }
        });

        cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextKeyPadHidden(context, businessNameEdt);
                editTextKeyPadHidden(context, webAddressEdt);
                mBusinessAlertDialog.dismiss();

            }
        });

		/*To check if the dialog is null or not. if it's not a null, the dialog will be shown orelse it will not get appeared*/
        alertShowing(mBusinessAlertDialog);
        return mBusinessAlertDialog;
    }


    public Dialog showBasicInfoAlertPopup(final Context context, String headerTxtStr, String msgTxtStr, String cancelStr, boolean ContinueLayVisibleBool, boolean cancelTxtVisibleBool, final InterfaceTwoBtnCallback okBtnInterfaceCallback) {

	/*To check if the dialog is shown, if the dialog is shown it will be cancelled */
        alertDismiss(mBaseInfoAlertDialog);

        mBaseInfoAlertDialog = getDialog(context, R.layout.popup_basic_info_alert);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mBaseInfoAlertDialog.getWindow();

        if (window != null) {
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            window.setGravity(Gravity.CENTER);
        }

        final TextView headerTxt, msgTxt, cancelTxt;
        LinearLayout continueLay;

        /*Init View*/
        headerTxt = mBaseInfoAlertDialog.findViewById(R.id.header_txt);
        msgTxt = mBaseInfoAlertDialog.findViewById(R.id.msg_txt);
        cancelTxt = mBaseInfoAlertDialog.findViewById(cancel_txt);

        continueLay = mBaseInfoAlertDialog.findViewById(continue_lay);

        /*Set text*/
        headerTxt.setText(headerTxtStr);
        msgTxt.setText(msgTxtStr);
        cancelTxt.setText(cancelStr);
        continueLay.setVisibility(ContinueLayVisibleBool ? View.VISIBLE : View.GONE);
        cancelTxt.setVisibility(cancelTxtVisibleBool ? View.VISIBLE : View.GONE);


        continueLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBaseInfoAlertDialog.dismiss();
                okBtnInterfaceCallback.onYesClick();
            }
        });

        cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBaseInfoAlertDialog.dismiss();
                okBtnInterfaceCallback.onNoClick();

            }
        });

        alertShowing(mBaseInfoAlertDialog);
        return mBaseInfoAlertDialog;
    }

    public Dialog showPhotoShootReqAlertPopup(final Context context, String skillLevelStr, final ParseGeoPoint parseGeoPoint, final ParseObject momentObj, final InterfaceTwoBtnCallback okBtnInterfaceCallback) {

	/*To check if the dialog is shown, if the dialog is shown it will be cancelled */
        alertDismiss(mPhotoShootReqAlertDialog);

        mPhotoShootReqAlertDialog = getDialog(context, R.layout.popup_photo_shoot_req_alert);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mPhotoShootReqAlertDialog.getWindow();

        if (window != null) {
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            window.setGravity(Gravity.CENTER);
        }

        final TextView headerTxt, rejectMomentTxt;
        LinearLayout accPhotoLay;
        String skillRequestLevelStr;
        int markerImg;


        /*Init View*/
        headerTxt = mPhotoShootReqAlertDialog.findViewById(R.id.header_txt);
        rejectMomentTxt = mPhotoShootReqAlertDialog.findViewById(R.id.reject_moment_txt);

        accPhotoLay = mPhotoShootReqAlertDialog.findViewById(R.id.acc_photo_lay);

        skillRequestLevelStr = context.getString(R.string.avid);
        markerImg = R.drawable.avid_img;

        if (!skillLevelStr.equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
            skillRequestLevelStr = skillLevelStr.equalsIgnoreCase(AppConstants.SUCCESS_CODE) ? context.getString(R.string.skill) : context.getString(R.string.pro);
            markerImg = skillLevelStr.equalsIgnoreCase(AppConstants.SUCCESS_CODE) ? R.drawable.skill_img : R.drawable.pro_img;
        }

        /*Set text*/
        headerTxt.setText(String.format(context.getString(R.string.con_req), skillRequestLevelStr));
        MapView mapView;
        final int mapMarkerImg = markerImg;
        MapsInitializer.initialize(context);

        mapView = mPhotoShootReqAlertDialog.findViewById(R.id.map_view);
        mapView.onCreate(mPhotoShootReqAlertDialog.onSaveInstanceState());
        mapView.onResume();// needed to get the map to display immediately
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng photographerLatLng = new LatLng(parseGeoPoint.getLatitude(), parseGeoPoint.getLongitude());
                MarkerOptions markerOption = new MarkerOptions().position(photographerLatLng);

                markerOption.icon(BitmapDescriptorFactory.fromBitmap(ImageUtil.resizeIcon(context, mapMarkerImg, context.getResources().getDimensionPixelSize(R.dimen.size30), context.getResources().getDimensionPixelSize(R.dimen.size30))));
                googleMap.addMarker(markerOption);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(photographerLatLng, 14));


                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                googleMap.getUiSettings().setCompassEnabled(false);
            }
        });


        accPhotoLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsDialogCalledBool = true;
                savePhotographerAcceptRes(context, momentObj.getObjectId(), true, okBtnInterfaceCallback);
            }
        });

        rejectMomentTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsDialogCalledBool = true;
                savePhotographerAcceptRes(context, momentObj.getObjectId(), false, okBtnInterfaceCallback);
            }
        });

        /*To check if the dialog is null or not. if it's not a null, the dialog will be shown orelse it will not get appeared*/
        alertShowing(mPhotoShootReqAlertDialog);
        return mPhotoShootReqAlertDialog;
    }

    private void savePhotographerAcceptRes(final Context context, String momentsObjId, final boolean isAccept, final InterfaceTwoBtnCallback okBtnInterfaceCallback) {

        showProgress(context);
        final ParseQuery<ParseObject> momentQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Moment);
        momentQuery.whereEqualTo(ParseAPIConstants.objectId, momentsObjId);
        momentQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject momentObj, ParseException e) {
                if (e == null && momentObj != null) {
                    final ParseQuery<ParseObject> photographerQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Photographer);
                    photographerQuery.whereEqualTo(ParseAPIConstants.user, ParseUser.getCurrentUser());
                    photographerQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(final ParseObject photographerObj, ParseException e) {

                            if (e == null) {
                                 /*Check internet connection*/
                                if (NetworkUtil.isNetworkAvailable(context)) {

                                    if ((isAccept && momentObj.get(ParseAPIConstants.enabled) != null && !momentObj.getBoolean(ParseAPIConstants.enabled)) || momentObj.get(ParseAPIConstants.accepted) != null) {
                                        hideProgress();
                                        DialogManager.getInstance().showAlertPopup(context, context.getString(R.string.moment_cancelled_topic), context.getString(R.string.moment_cancelled_msg), new InterfaceBtnCallback() {
                                            @Override
                                            public void onOkClick() {
                                                showProgress(context);
                                                saveMomentLog(context, momentObj, false, okBtnInterfaceCallback);
                                            }
                                        });

                                    } else if (isAccept && momentObj.get(ParseAPIConstants.accepted)== null) {
                                        photographerObj.put(ParseAPIConstants.isAvailable, false);
                                        photographerObj.saveEventually();

                                        List<Object> userList = momentObj.getList(ParseAPIConstants.authorizedUsers);
                                        boolean isUserThere = false;

                                        if (userList.size() < 2) {
                                            for (int i = 0; i < userList.size(); i++) {
                                                if (userList.get(i).equals(ParseUser.getCurrentUser())) {
                                                    isUserThere = true;
                                                    break;
                                                }
                                            }
                                            if (!isUserThere) {
                                                userList.add(ParseUser.getCurrentUser());
                                            }
                                        }

                                        momentObj.put(ParseAPIConstants.photographer, photographerObj);
                                        momentObj.put(ParseAPIConstants.accepted, new Date());
                                        momentObj.put(ParseAPIConstants.authorizedUsers, userList);

                                        momentObj.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    saveMomentLog(context, momentObj, true, okBtnInterfaceCallback);
                                                } else {
                                                    hideProgress();
                                                    okBtnInterfaceCallback.onNoClick();
                                                }
                                            }
                                        });
                                    } else {
                                        saveMomentLog(context, momentObj, false, okBtnInterfaceCallback);
                                    }
                                } else {
                                    hideProgress();
                                    okBtnInterfaceCallback.onNoClick();
                                }
                            } else {
                                hideProgress();
                                okBtnInterfaceCallback.onNoClick();
                            }
                        }
                    });
                } else {
                    hideProgress();
                    okBtnInterfaceCallback.onNoClick();
                }

            }
        });


    }

    private void saveMomentLog(final Context context, ParseObject momentObj, final boolean isAccept, final InterfaceTwoBtnCallback okBtnInterfaceCallback) {
        if (NetworkUtil.isNetworkAvailable(context)) {
            ParseQuery<ParseObject> momentLogsQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_MomentLog);
            momentLogsQuery.whereEqualTo(ParseAPIConstants.moment, momentObj);
            momentLogsQuery.whereDoesNotExist(ParseAPIConstants.response);
            momentLogsQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject momentLogObj, ParseException e) {

                    if (e == null && momentLogObj != null) {
                        momentLogObj.put(ParseAPIConstants.response, isAccept ? 1 : 0);
                        momentLogObj.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                hideProgress();
                                if (e == null) {
                                    mPhotoShootReqAlertDialog.dismiss();
                                    if (isAccept) {
                                        okBtnInterfaceCallback.onYesClick();
                                    } else {
                                        okBtnInterfaceCallback.onNoClick();
                                    }
                                } else {
                                    okBtnInterfaceCallback.onNoClick();
                                }
                            }
                        });
                    } else {
                        hideProgress();
                        okBtnInterfaceCallback.onNoClick();
                    }
                }
            });
        } else {
            hideProgress();
            /*Alert message will be appeared if there is no internet connection*/
            showAlertPopup(context, context.getString(R.string.app_name), context.getString(R.string.no_internet), new InterfaceBtnCallback() {
                @Override
                public void onOkClick() {
                    okBtnInterfaceCallback.onNoClick();

                }
            });
        }
    }

    public Dialog showPhotographerProgressPopup(Context context, final InterfaceBtnCallback okBtnInterfaceCallback) {

	/*To check if the dialog is shown, if the dialog is shown it will be cancelled */
        alertDismiss(mPhotographerProgressAlertDialog);

        mPhotographerProgressAlertDialog = getDialog(context, R.layout.popup_progress_search_photographer);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mPhotographerProgressAlertDialog.getWindow();

        if (window != null) {
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            window.setGravity(Gravity.CENTER);
        }

        final TextView cancelTxt;
        final com.fautus.fautusapp.utils.BackgroundFillColor backgroundFillView;
        final Handler handler = new Handler();

        /*Init View*/
        cancelTxt = mPhotographerProgressAlertDialog.findViewById(cancel_txt);
        backgroundFillView = mPhotographerProgressAlertDialog.findViewById(R.id.background_fill_view);

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int countInt = backgroundFillView.getValue();
                if (countInt < 90) {
                    backgroundFillView.setValue(countInt + 1);
                    backgroundFillView.postDelayed(this, 30);
                } else {
                    backgroundFillView.postDelayed(this, 0);
                    backgroundFillView.setValue(10);
                }
            }
        };
        handler.postDelayed(runnable, 0);
        cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotographerProgressAlertDialog.dismiss();
                handler.removeCallbacks(runnable);
                okBtnInterfaceCallback.onOkClick();
            }
        });

        mPhotographerProgressAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });
        /*To check if the dialog is null or not. if it's not a null, the dialog will be shown orelse it will not get appeared*/
        alertShowing(mPhotographerProgressAlertDialog);
        return mPhotographerProgressAlertDialog;
    }


    public Dialog showCreatePhotoSessionPopup(Context context, String codeStr, final InterfaceBtnCallback okBtnInterfaceCallback) {

	/*To check if the dialog is shown, if the dialog is shown it will be cancelled */
        alertDismiss(mPhotoSessionAlertDialog);

        mPhotoSessionAlertDialog = getDialog(context, R.layout.popup_create_session_alert);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mPhotoSessionAlertDialog.getWindow();

        if (window != null) {
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            window.setGravity(Gravity.CENTER);
        }

        final TextView codeTxt, cancelTxt;
        LinearLayout continueLay;

        /*Init View*/
        codeTxt = mPhotoSessionAlertDialog.findViewById(R.id.code_txt);
        cancelTxt = mPhotoSessionAlertDialog.findViewById(cancel_txt);

        continueLay = mPhotoSessionAlertDialog.findViewById(continue_lay);

        /*Set Data*/
        codeTxt.setText(codeStr);

        continueLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoSessionAlertDialog.dismiss();
                okBtnInterfaceCallback.onOkClick();
            }
        });

        cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoSessionAlertDialog.dismiss();
            }
        });

		/*To check if the dialog is null or not. if it's not a null, the dialog will be shown orelse it will not get appeared*/
        alertShowing(mPhotoSessionAlertDialog);
        return mPhotoSessionAlertDialog;
    }

    public Dialog showPhotoSessionCodePopup(final Context context, final InterfaceTwoEdtCallback okEdtInterfaceCallback) {

	/*To check if the dialog is shown, if the dialog is shown it will be cancelled */
        alertDismiss(mConsumerSessionCodeAlertDialog);


        mConsumerSessionCodeAlertDialog = getDialog(context, R.layout.popup_photo_session_code_alert);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mConsumerSessionCodeAlertDialog.getWindow();

        if (window != null) {
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            window.setGravity(Gravity.CENTER);
        }

        TextView closeTxt;
        final EditText sessionCodeEdt;
        LinearLayout continueLay;

        /*Init View*/
        sessionCodeEdt = mConsumerSessionCodeAlertDialog.findViewById(R.id.session_code_edt);
        continueLay = mConsumerSessionCodeAlertDialog.findViewById(continue_lay);
        closeTxt = mConsumerSessionCodeAlertDialog.findViewById(R.id.close_txt);

        continueLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sessionCodeStr;
                sessionCodeStr = sessionCodeEdt.getText().toString().trim();
                if (sessionCodeStr.isEmpty()) {
                    showAlertPopup(context, context.getString(R.string.req_code_topic), context.getString(R.string.req_code_msg), new InterfaceBtnCallback() {
                        @Override
                        public void onOkClick() {

                        }
                    });

                } else {
                    editTextKeyPadHidden(context, sessionCodeEdt);
                    mConsumerSessionCodeAlertDialog.dismiss();
                    okEdtInterfaceCallback.onEdtOneClick(sessionCodeStr, "");
                }

            }
        });

        closeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextKeyPadHidden(context, sessionCodeEdt);
                mConsumerSessionCodeAlertDialog.dismiss();
            }
        });
        /*To check if the dialog is null or not. if it's not a null, the dialog will be shown orelse it will not get appeared*/
        alertShowing(mConsumerSessionCodeAlertDialog);
        return mConsumerSessionCodeAlertDialog;
    }


    public Dialog showPhotographerConnectedAlertPopup(final Context context, ParseObject photographerDetailObj) {

	/*To check if the dialog is shown, if the dialog is shown it will be cancelled */
        alertDismiss(mShowPhotographerConnectAlertDialog);
        mShowPhotographerConnectAlertDialog = getDialog(context, R.layout.popup_photographer_connected_alert);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mShowPhotographerConnectAlertDialog.getWindow();

        if (window != null) {
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            window.setGravity(Gravity.CENTER);
        }

        final TextView photographerNameTxt, languagesTxt, hintTxt;
        final ImageView photographerImg;
        final LinearLayout continueLay;

        /*Init View*/
        photographerNameTxt = mShowPhotographerConnectAlertDialog.findViewById(R.id.photographer_name_txt);
        photographerImg = mShowPhotographerConnectAlertDialog.findViewById(R.id.photographer_img);
        languagesTxt = mShowPhotographerConnectAlertDialog.findViewById(R.id.languages_txt);
        hintTxt = mShowPhotographerConnectAlertDialog.findViewById(hint_txt);
        continueLay = mShowPhotographerConnectAlertDialog.findViewById(continue_lay);

        /*Set text*/
        photographerNameTxt.setText(photographerDetailObj.getString(ParseAPIConstants.fullName));
        languagesTxt.setText(photographerDetailObj.getString(ParseAPIConstants.languages));
        hintTxt.setText(String.format(context.getString(R.string.photo_shoot_msg), photographerDetailObj.getString(ParseAPIConstants.fullName)));

        if (photographerDetailObj.getParseFile(ParseAPIConstants.profilePhoto) != null) {
            photographerDetailObj.getParseFile(ParseAPIConstants.profilePhoto).getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    try {
                                    /*If the Profile picture exists in the DB, this term will make it invisible*/
                        Glide.with(context)
                                .load(data)
                                .into(photographerImg);
                    } catch (Exception ex) {
                        Log.e(this.getClass().getSimpleName(), ex.getMessage());
                    }
                }
            });

        }

        continueLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShowPhotographerConnectAlertDialog.dismiss();
            }
        });

        alertShowing(mShowPhotographerConnectAlertDialog);
        return mShowPhotographerConnectAlertDialog;
    }

    public Dialog showPictureUploadPopup(Context context, final InterfaceTwoBtnCallback interfaceTwoBtnCallback) {

        alertDismiss(mPictureAlertDialog);
        mPictureAlertDialog = getDialog(context, R.layout.popup_photo_selection);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mPictureAlertDialog.getWindow();

        if (window != null) {
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
            window.setGravity(Gravity.CENTER);
        }
        Button cameraBtn, galleryBtn, cancelBtn;

        cameraBtn = mPictureAlertDialog.findViewById(R.id.camera_btn);
        galleryBtn = mPictureAlertDialog.findViewById(R.id.gallery_btn);
        cancelBtn = mPictureAlertDialog.findViewById(R.id.cancel_btn);


        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPictureAlertDialog.dismiss();
                interfaceTwoBtnCallback.onYesClick();
            }
        });
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPictureAlertDialog.dismiss();
                interfaceTwoBtnCallback.onNoClick();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPictureAlertDialog.dismiss();
            }
        });

        alertShowing(mPictureAlertDialog);
        return mPictureAlertDialog;
    }


    public Dialog showLegalPopup(Context context, final InterfaceTwoBtnCallback interfaceTwoBtnCallback) {
        alertDismiss(mLegalAlertDialog);
        mLegalAlertDialog = getDialog(context, R.layout.popup_photo_selection);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mLegalAlertDialog.getWindow();

        if (window != null) {
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
            window.setGravity(Gravity.CENTER);
        }
        TextView headerTxt;
        Button cameraBtn, galleryBtn, cancelBtn;

        headerTxt = mLegalAlertDialog.findViewById(R.id.header_txt);

        cameraBtn = mLegalAlertDialog.findViewById(R.id.camera_btn);
        galleryBtn = mLegalAlertDialog.findViewById(R.id.gallery_btn);
        cancelBtn = mLegalAlertDialog.findViewById(R.id.cancel_btn);


        headerTxt.setText(context.getString(R.string.choose_any));
        cameraBtn.setText(context.getString(R.string.terms_service));
        galleryBtn.setText(context.getString(R.string.privacy_policy));
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLegalAlertDialog.dismiss();
                interfaceTwoBtnCallback.onYesClick();
            }
        });
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLegalAlertDialog.dismiss();
                interfaceTwoBtnCallback.onNoClick();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLegalAlertDialog.dismiss();
            }
        });

        alertShowing(mLegalAlertDialog);
        return mPictureAlertDialog;
    }

    public Dialog showMomentAmtDetailsPopup(Context context, String momentNameValStr, String momentAmtStr, String photosCountStr, String photosCountAmtStr, String serviceFeeStr, String totAmtStr) {
        alertDismiss(mMomentAmtAlertDialog);
        mMomentAmtAlertDialog = getDialog(context, R.layout.popup_moment_pay_details);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mMomentAmtAlertDialog.getWindow();

        if (window != null) {
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            window.setGravity(Gravity.CENTER);
        }
        TextView momentNameTxt, momentAmtTxt, photosCountTxt, photosAmtTxt, serviceFeeAmtTxt, totAmtTxt, closeTxt;

        momentNameTxt = mMomentAmtAlertDialog.findViewById(R.id.moment_name_txt);
        momentAmtTxt = mMomentAmtAlertDialog.findViewById(R.id.moment_amt_txt);
        photosCountTxt = mMomentAmtAlertDialog.findViewById(R.id.photos_count_txt);
        photosAmtTxt = mMomentAmtAlertDialog.findViewById(R.id.photos_amt_txt);
        serviceFeeAmtTxt = mMomentAmtAlertDialog.findViewById(R.id.service_fee_amt_txt);
        totAmtTxt = mMomentAmtAlertDialog.findViewById(R.id.tot_amt_txt);
        closeTxt = mMomentAmtAlertDialog.findViewById(close_txt);

        String momentNameStr = context.getString(R.string.avid);

        if (!momentNameValStr.equalsIgnoreCase(com.fautus.fautusapp.utils.AppConstants.FAILURE_CODE)) {
            momentNameStr = momentNameValStr.equalsIgnoreCase(com.fautus.fautusapp.utils.AppConstants.SUCCESS_CODE) ? context.getString(R.string.skill) : context.getString(R.string.pro);
        }


        momentNameTxt.setText(momentNameStr + " " + context.getString(R.string.moment));
        momentAmtTxt.setText(context.getString(R.string.dollar_sym) + " " + momentAmtStr);
        photosCountTxt.setText(photosCountStr);
        photosAmtTxt.setText(context.getString(R.string.dollar_sym) + " " + photosCountAmtStr);
        serviceFeeAmtTxt.setText(context.getString(R.string.dollar_sym) + " " + serviceFeeStr);
        totAmtTxt.setText(context.getString(R.string.dollar_sym) + " " + totAmtStr);

        closeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMomentAmtAlertDialog.dismiss();
            }
        });

        alertShowing(mMomentAmtAlertDialog);
        return mPictureAlertDialog;
    }

    public Dialog showLowRatingAlertPopup(final Context context, final InterfaceTwoEdtCallback okEdtInterfaceCallback) {

	/*To check if the dialog is shown, if the dialog is shown it will be cancelled */
        alertDismiss(mLowRatingAlertDialog);

        mLowRatingAlertDialog = getDialog(context, R.layout.popup_low_rating_alert);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mLowRatingAlertDialog.getWindow();

        if (window != null) {
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            window.setGravity(Gravity.CENTER);
        }

        final EditText describeIssueEdt;
        final TextView cancelTxt;
        LinearLayout continueLay;

        //Init View
        describeIssueEdt = mLowRatingAlertDialog.findViewById(R.id.describe_issue_edt);
        continueLay = mLowRatingAlertDialog.findViewById(continue_lay);
        cancelTxt = mLowRatingAlertDialog.findViewById(cancel_txt);


        continueLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String describeIssueStr;
                describeIssueStr = describeIssueEdt.getText().toString().trim();
                if (describeIssueStr.isEmpty()) {
                    showAlertPopup(context, context.getString(R.string.req_describe_topic), context.getString(R.string.req_describe_msg), new InterfaceBtnCallback() {
                        @Override
                        public void onOkClick() {

                        }
                    });

                } else {
                    editTextKeyPadHidden(context, describeIssueEdt);
                    mLowRatingAlertDialog.dismiss();
                    okEdtInterfaceCallback.onEdtOneClick(describeIssueStr, "");
                }

            }
        });

        cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextKeyPadHidden(context, describeIssueEdt);
                mLowRatingAlertDialog.dismiss();

            }
        });

		/*To check if the dialog is null or not. if it's not a null, the dialog will be shown orelse it will not get appeared*/
        alertShowing(mLowRatingAlertDialog);
        return mLowRatingAlertDialog;
    }


    public Dialog showEmailInviteAlertPopup(final Context context, final InterfaceTwoEdtCallback okEdtInterfaceCallback) {

	/*To check if the dialog is shown, if the dialog is shown it will be cancelled */
        alertDismiss(mEmilInviteAlertDialog);

        mEmilInviteAlertDialog = getDialog(context, R.layout.popup_email_invite_alert);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mEmilInviteAlertDialog.getWindow();

        if (window != null) {
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            window.setGravity(Gravity.CENTER);
        }

        final EditText emailAddressEdt;
        final TextView cancelTxt;
        LinearLayout continueLay;

        //Init View
        emailAddressEdt = mEmilInviteAlertDialog.findViewById(R.id.email_address_edt);
        continueLay = mEmilInviteAlertDialog.findViewById(continue_lay);
        cancelTxt = mEmilInviteAlertDialog.findViewById(cancel_txt);


        continueLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddressStr;
                emailAddressStr = emailAddressEdt.getText().toString().trim();

                if (emailAddressStr.isEmpty() || !EmailUtil.isEmailValid(emailAddressStr)) {
                    showAlertPopup(context, context.getString(R.string.email_req), context.getString(R.string.invite_mail_req), new InterfaceBtnCallback() {
                        @Override
                        public void onOkClick() {

                        }
                    });

                } else {
                    editTextKeyPadHidden(context, emailAddressEdt);
                    mEmilInviteAlertDialog.dismiss();
                    okEdtInterfaceCallback.onEdtOneClick(emailAddressStr, "");
                }

            }
        });

        cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextKeyPadHidden(context, emailAddressEdt);
                mEmilInviteAlertDialog.dismiss();

            }
        });

		/*To check if the dialog is null or not. if it's not a null, the dialog will be shown orelse it will not get appeared*/
        alertShowing(mEmilInviteAlertDialog);
        return mEmilInviteAlertDialog;
    }


    public Dialog showPaymentTermsAlertPopup(final Context context, final InterfaceTwoBtnCallback okBtnInterfaceCallback) {

	/*To check if the dialog is shown, if the dialog is shown it will be cancelled */
        alertDismiss(mPaymentTermsAlertDialog);

        mPaymentTermsAlertDialog = getDialog(context, R.layout.popup_payment_terms_alert);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mPaymentTermsAlertDialog.getWindow();

        if (window != null) {
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            window.setGravity(Gravity.CENTER);
        }

        final TextView cancelTxt, termAgreeTxt;
        LinearLayout agreeLay;
        Spannable SpannableOne, SpannableTwo, SpannableThree, SpannableFour, SpannableFive;
        //Init View
        termAgreeTxt = mPaymentTermsAlertDialog.findViewById(R.id.term_agree_txt);
        agreeLay = mPaymentTermsAlertDialog.findViewById(R.id.agree_lay);
        cancelTxt = mPaymentTermsAlertDialog.findViewById(cancel_txt);

        SpannableOne = Spannable.Factory.getInstance().newSpannable(context.getResources().getString(R.string.stripe_reg_one));
        SpannableOne.setSpan(new ForegroundColorSpan(Color.BLACK), 0, SpannableOne.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        termAgreeTxt.append(SpannableOne);
        termAgreeTxt.append(" ");

        SpannableTwo = Spannable.Factory.getInstance().newSpannable(context.getResources().getString(R.string.stripe_reg_two));
        SpannableTwo.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View v) {
                showLegalPopup(context, new InterfaceTwoBtnCallback() {
                    @Override
                    public void onYesClick() {
                        mPaymentTermsAlertDialog.dismiss();
                        AppConstants.TERMS_COND_SCREEN = AppConstants.SUCCESS_CODE;
                        ((HomeScreen) context).addFragment(new TermsCondFragment());
                    }

                    @Override
                    public void onNoClick() {
                        mPaymentTermsAlertDialog.dismiss();
                        AppConstants.TERMS_COND_SCREEN = AppConstants.FAILURE_CODE;
                        ((HomeScreen) context).addFragment(new TermsCondFragment());
                    }
                });
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                // this is where you set link color, underline, typeface etc.
                ds.setUnderlineText(false);

            }

        }, 0, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableTwo.setSpan(new ForegroundColorSpan(Color.BLUE), 0, SpannableTwo.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        termAgreeTxt.append(SpannableTwo);
        termAgreeTxt.append(" ");


        SpannableThree = Spannable.Factory.getInstance().newSpannable(context.getResources().getString(R.string.stripe_reg_three));
        SpannableThree.setSpan(new ForegroundColorSpan(Color.BLACK), 0, SpannableThree.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        termAgreeTxt.append(SpannableThree);
        termAgreeTxt.append(" ");

        SpannableFour = Spannable.Factory.getInstance().newSpannable(context.getResources().getString(R.string.stripe_reg_four));
        SpannableFour.setSpan(new ClickableSpan() {
            @Override
            public void onClick(final View c) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.STRIP_LEGAL_WEB_URL));
                context.startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                // this is where you set link color, underline, typeface etc.
                ds.setUnderlineText(false);
            }
        }, 0, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableFour.setSpan(new ForegroundColorSpan(Color.BLUE), 0, SpannableFour.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        termAgreeTxt.append(SpannableFour);


        SpannableFive = Spannable.Factory.getInstance().newSpannable(".");
        SpannableFive.setSpan(new ForegroundColorSpan(Color.BLACK), 0, SpannableFive.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        termAgreeTxt.append(SpannableFive);
        termAgreeTxt.setMovementMethod(LinkMovementMethod.getInstance());

        agreeLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPaymentTermsAlertDialog.dismiss();
                okBtnInterfaceCallback.onYesClick();
            }
        });

        cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPaymentTermsAlertDialog.dismiss();
                okBtnInterfaceCallback.onNoClick();
            }
        });
        /*To check if the dialog is null or not. if it's not a null, the dialog will be shown orelse it will not get appeared*/
        alertShowing(mPaymentTermsAlertDialog);
        return mPaymentTermsAlertDialog;
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

         /*To check if the dialog is null or not. if it's not a null, the dialog will be shown orelse it will not get appeared*/
        try {
            if (mProgressDialog != null) {
                mProgressDialog.show();
            }
        } catch (Exception e) {
            Log.e(com.fautus.fautusapp.utils.AppConstants.TAG, e.getMessage());
        }
    }

    public void hideProgress() {
        /*hide progress*/
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            try {
                mProgressDialog.dismiss();
            } catch (Exception e) {
                Log.e(com.fautus.fautusapp.utils.AppConstants.TAG, e.getMessage());
            }
        }
    }


    private void alertShowing(Dialog dialog) {
         /*To check if the dialog is null or not. if it's not a null, the dialog will be shown orelse it will not get appeared*/
        if (dialog != null) {
            try {
                dialog.show();
            } catch (Exception e) {
                Log.e(com.fautus.fautusapp.utils.AppConstants.TAG, e.getMessage());
            }
        }
    }

    private void alertDismiss(Dialog dialog) {
        /*To check if the dialog is shown, if the dialog is shown it will be cancelled */
        if (dialog != null && dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                Log.e(com.fautus.fautusapp.utils.AppConstants.TAG, e.getMessage());
            }
        }

    }

    private void editTextKeyPadHidden(Context context, EditText mEdtView) {
        /*Hiding keypad for user interaction*/
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEdtView.getWindowToken(), 0);
    }


}
