package com.smaat.spark.main;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smaat.spark.R;
import com.smaat.spark.entity.inputEntity.ChatSendReceiveInputEntity;
import com.smaat.spark.model.CommonResponse;
import com.smaat.spark.services.APICommonInterface;
import com.smaat.spark.utils.AppConstants;
import com.smaat.spark.utils.DialogManager;
import com.smaat.spark.utils.GlobalMethods;

import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class BaseActivity extends AppCompatActivity {


    protected Activity mActivity;
    protected Dialog mBaseDialog;
    protected Animation mShakeAnimation;
    protected Vibrator mVibrator;
    public Dialog mFriendAlertDialog;
    protected ArrayList<String> listPermissionsNeeded = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Default Init
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mActivity = this;

        mShakeAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.anim_shake);
        mVibrator = (Vibrator) mActivity.getSystemService(Context.VIBRATOR_SERVICE);

        //Init default font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Montserrat-Regular.otf").build());
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private static final BaseActivity sBaseActivityInstance = new BaseActivity();

    public static BaseActivity getInstance() {
        return sBaseActivityInstance;
    }

    protected void setupUI(View view) {

        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(mActivity);
                    return false;
                }
            });
        }
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View mInnerView = ((ViewGroup) view).getChildAt(i);
                setupUI(mInnerView);
            }
        }
    }

    protected void hideSoftKeyboard(Activity mActivity) {
        try {
            if (mActivity != null && !mActivity.isFinishing()) {
                InputMethodManager mInputMethodManager = (InputMethodManager) mActivity
                        .getSystemService(INPUT_METHOD_SERVICE);

                if (mActivity.getCurrentFocus() != null
                        && mActivity.getCurrentFocus().getWindowToken() != null) {
                    mInputMethodManager.hideSoftInputFromWindow(mActivity
                            .getCurrentFocus().getWindowToken(), 0);

                }
            }
        } catch (Exception e) {
            Log.d(AppConstants.TAG, e.toString());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        SparkApplication.activityResumed();
        mActivity.sendBroadcast(new Intent("com.google.android.intent.action.GTALK_HEARTBEAT"));
        mActivity.sendBroadcast(new Intent("com.google.android.intent.action.MCS_HEARTBEAT"));

    }

    @Override
    protected void onPause() {
        super.onPause();

        SparkApplication.activityStopped();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    public void sysOut(String msg) {
        System.out.println(msg);
        Log.d(getString(R.string.app_name), msg);
    }

    protected SparkApplication app() {
        return ((SparkApplication) mActivity.getApplication());
    }


    public void nextScreen(Class<?> clazz, boolean finish) {
        Intent mIntent = new Intent(getApplicationContext(), clazz);

        mActivity.startActivity(mIntent);
        mActivity.overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
        if (finish)
            mActivity.finish();

    }

    protected void previousScreen(Class<?> clazz, boolean finish) {
        Intent mIntent = new Intent(getApplicationContext(), clazz);

        mActivity.startActivity(mIntent);
        mActivity.overridePendingTransition(R.anim.slide_out_right,
                R.anim.slide_in_left);
        if (finish)
            mActivity.finish();
    }

    protected void finishScreen() {
        mActivity.finish();
        mActivity.overridePendingTransition(R.anim.slide_out_right,
                R.anim.slide_in_left);

    }

    public void onRequestSuccess(Object resObj) {
    }

    public void onRequestFailure(Throwable t) {
        try {
            sysOut("errorCode.getCause() Msg--------" + t.getMessage());
            sysOut("errorCode.getCause() --------" + t.getCause().toString());
        } catch (Exception e) {
            Log.d(AppConstants.TAG, e.toString());
        }
        if (t instanceof IOException || t.getCause() instanceof ConnectException || t.getCause() instanceof java.net.UnknownHostException
                || t.getMessage() == null) {

            DialogManager.getInstance().showAlertPopup(mActivity, getString(R.string.no_internet));
        } else if (t.getCause() instanceof java.net.SocketTimeoutException) {

            DialogManager.getInstance().showAlertPopup(mActivity, getString(R.string.connect_time_out));
        } else {
            DialogManager.getInstance().showAlertPopup(mActivity, t.getMessage());

        }
    }

    protected void shakeAnimEdt(EditText editTextBox, String errorStr) {
        editTextBox.startAnimation(mShakeAnimation);
        editTextBox.requestFocus();
        editTextBox.setError(errorStr);
        mVibrator.vibrate(100);
    }

    protected void baseAlertDismiss() {
        if (mBaseDialog != null && mBaseDialog.isShowing()) {
            try {
                mBaseDialog.dismiss();
            } catch (Exception e) {
                Log.e(AppConstants.DIALOG_TAG, e.getMessage());
            }
        }
    }


    protected void displayNotifyPopup(final String pushFullMsg) {


        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                JSONObject json;
                String messageStr = "", notificationFromStr = "", typeNotificationStr = "";

                try {
                    json = new JSONObject(pushFullMsg);
                    messageStr = json.getString("msg");
                    notificationFromStr = json.getString("notification_from");


                    typeNotificationStr = json.getString("type_of_notification");

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mActivity, e.toString(), Toast.LENGTH_LONG).show();
                }
                if (mFriendAlertDialog != null && mFriendAlertDialog.isShowing()) {
                    try {
                        mFriendAlertDialog.dismiss();
                    } catch (Exception e) {
                        Log.e(AppConstants.DIALOG_TAG, e.getMessage());
                    }
                }

                mFriendAlertDialog = new Dialog(mActivity);
                mFriendAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                if (mFriendAlertDialog.getWindow() != null) {
                    mFriendAlertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    mFriendAlertDialog.setContentView(typeNotificationStr.equals(AppConstants.SUCCESS_CODE) ? R.layout.popup_friend_req_alert : R.layout.popup_basic_alert);
                    mFriendAlertDialog.getWindow().setGravity(Gravity.TOP);
                    mFriendAlertDialog.getWindow().setBackgroundDrawable(
                            new ColorDrawable(Color.TRANSPARENT));
                }


                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = mFriendAlertDialog.getWindow();

                if (window != null) {
                    lp.copyFrom(window.getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    window.setAttributes(lp);
                    window.getAttributes().windowAnimations = R.style.PopupTopAnimation;
                }

                if (typeNotificationStr.equals(AppConstants.SUCCESS_CODE)) {


                    mFriendAlertDialog.setCancelable(false);
                    mFriendAlertDialog.setCanceledOnTouchOutside(false);

                    TextView msgTxt;
                    Button firstBtn, secondBtn, thirdBtn;
                    ImageView infoImg;
                    //Init View
                    msgTxt = (TextView) mFriendAlertDialog.findViewById(R.id.msg_txt);
                    firstBtn = (Button) mFriendAlertDialog.findViewById(R.id.first_btn);
                    secondBtn = (Button) mFriendAlertDialog.findViewById(R.id.second_btn);
                    thirdBtn = (Button) mFriendAlertDialog.findViewById(R.id.third_btn);
                    infoImg = (ImageView) mFriendAlertDialog.findViewById(R.id.info_img);

                    secondBtn.setVisibility(View.VISIBLE);


                    //Set Data
                    msgTxt.setText(messageStr);


                    final String senderIdStr = notificationFromStr;

                    firstBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mFriendAlertDialog.dismiss();
                            callAddFriendAPI(mActivity, senderIdStr, true);
                        }
                    });
                    secondBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callAddFriendAPI(mActivity, senderIdStr, false);
                            mFriendAlertDialog.dismiss();
                        }
                    });
                    thirdBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mFriendAlertDialog.dismiss();
                        }
                    });
                    infoImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mFriendAlertDialog.dismiss();
                        }
                    });
                } else {

                    mFriendAlertDialog.setCancelable(true);
                    mFriendAlertDialog.setCanceledOnTouchOutside(true);

                    LinearLayout popUpParentLay;
                    TextView msgTxt;
                    ImageView infoImg;

                    //Init View
                    popUpParentLay = (LinearLayout) mFriendAlertDialog.findViewById(R.id.popup_parent_lay);
                    msgTxt = (TextView) mFriendAlertDialog.findViewById(R.id.msg_txt);
                    infoImg = (ImageView) mFriendAlertDialog.findViewById(R.id.info_img);

                    //Set Data
                    msgTxt.setText(messageStr);

                    popUpParentLay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mFriendAlertDialog.dismiss();
                        }
                    });
                    infoImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mFriendAlertDialog.dismiss();
                        }
                    });
                }

                if (mFriendAlertDialog != null) {
                    try {
                        mFriendAlertDialog.show();
                    } catch (Exception e) {
                        Log.e(AppConstants.DIALOG_TAG, e.getMessage());
                    }
                }
            }
        });

    }


    public void callAddFriendAPI(final Context context, String senderId, boolean isAddAPIBool) {
        ChatSendReceiveInputEntity friendListInputEntityRes = new ChatSendReceiveInputEntity(AppConstants.API_ACCEPT_REQUEST, AppConstants.PARAMS_ACCEPT_REQUEST, GlobalMethods.getUserID(context), senderId, isAddAPIBool ? AppConstants.SUCCESS_CODE : context.getString(R.string.two));
        DialogManager.showProgress(context);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).build();
        APICommonInterface serviceInterface = retrofit.create(APICommonInterface.class);

        serviceInterface.addFriendAPICall(friendListInputEntityRes).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                DialogManager.hideProgress();
                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                        DialogManager.getInstance().showAlertPopup(context, response.body().getMessage());
                    } else {
                        DialogManager.getInstance().showAlertPopup(context, response.body().getMessage());
                    }

                } else {
                    onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                DialogManager.hideProgress();
                onRequestFailure(t);
            }
        });

    }

    //    private PermissionCallback mPermissionCallback = null;
//
//    public boolean isPermission(PermissionCallback permissionCallback, List<String> camera) {
//        mPermissionCallback = permissionCallback;
//        listPermissionsNeeded = new ArrayList<>();
//        listPermissionsNeeded.addAll(camera);
//
//        if (!listPermissionsNeeded.isEmpty()) {
//            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), permsRequestCode);
//            return false;
//        }
//
//        return true;
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//
//        switch (requestCode) {
//            case 200: {
//
//                Map<String, Integer> perms = new HashMap<>();
//
//
//                for (int i = 0; i < listPermissionsNeeded.size(); i++) {
//                    perms.put(listPermissionsNeeded.get(i), PackageManager.PERMISSION_GRANTED);
//                }
//                if (grantResults.length > 0) {
//                    for (int i = 0; i < permissions.length; i++)
//                        perms.put(permissions[i], grantResults[i]);
//                    for (int j = 0; j < listPermissionsNeeded.size(); j++) {
//                        if (perms.get(listPermissionsNeeded.get(j)) == PackageManager.PERMISSION_GRANTED) {
//
//
//                            if (j == listPermissionsNeeded.size() - 1) {
//                                if (mPermissionCallback != null) {
//                                    mPermissionCallback.permissionOkClick();
//                                }
//                            }
//
//
//                        } else {
//                            //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                            if (ActivityCompat.shouldShowRequestPermissionRationale(this, listPermissionsNeeded.get(j))) {
//
////                                DialogManager.showAlertDialogWithCallback(this, mAlertcallback,
////                                        getString(R.string.permission_req));
//
//                            }
//                            //permission is denied (and never ask again is  checked)
//                            //shouldShowRequestPermissionRationale will return false
//                            else {
//                                if (perms.get(listPermissionsNeeded.get(j)) == PackageManager.PERMISSION_DENIED) {
//                                    DialogManager.showConfirmDialog(this, mConfirmCallback, getString(R.string.app_name),
//                                            getString(R.string.do_you_want_move_settings));
//                                    break;
//
//                                } else {
//                                    if (j == listPermissionsNeeded.size() - 1) {
//                                        if (mPermissionCallback != null) {
//                                            mPermissionCallback.permissionOkClick();
//                                        }
//                                    }
//
//                                }
//                            }
//                        }
//                    }
//
//
//                }
//            }
//        }
//    }
//
//    DialogMangerCallback mAlertcallback = new DialogMangerCallback() {
//        @Override
//        public void onOkClick() {
//
//            isPermission(mPermissionCallback, listPermissionsNeeded);
//
//        }
//    };
//    DialogMangerCallback mConfirmCallback = new DialogMangerCallback() {
//        @Override
//        public void onOkClick() {
//            Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
//            myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
//            myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivityForResult(myAppSettings, 168);
//
//
//        }
//    };
    private boolean checkAndRequestPermissions(ArrayList<String> permissionArrStrList) {
        int readStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int locCoarsePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int findLocPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);


        List<String> listPermissionsNeeded = new ArrayList<>();

        if (readStoragePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (locCoarsePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (findLocPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 200);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 200: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);

                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        //Next screen
//                        nextScreenCheck();
                    } else {
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                            DialogManager.getInstance().showAlertPopup(this, getString(R.string.storage_req));
//                            checkAndRequestPermissions();

                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            //proceed with logic by disabling the related features or quit the app.
                            DialogManager.getInstance().showAlertPopup(this, getString(R.string.go_settings_per));

                            Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                            myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                            myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivityForResult(myAppSettings, 168);

                        }
                    }
                }
            }
        }

    }

}
