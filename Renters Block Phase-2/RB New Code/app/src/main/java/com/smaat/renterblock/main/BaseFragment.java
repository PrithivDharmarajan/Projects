package com.smaat.renterblock.main;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.smaat.renterblock.R;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.InterfaceTwoBtnCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static android.content.Context.INPUT_METHOD_SERVICE;

/*
* */
public class BaseFragment extends Fragment implements InterfaceTwoBtnCallback {

    private List<String> mAppPermissionsStrArrList;
    private InterfaceTwoBtnCallback mPermissionCallback = null;
    private int mAskPermissionCountInt = 0;
    protected Animation mAnimInTop, mAnimOutBottom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        // Init default font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Helvetica-Neue-Light.otf").build());

         /*Animation*/
        mAnimInTop = AnimationUtils.loadAnimation(getActivity(),
                R.anim.anim_in_top);
        mAnimOutBottom = AnimationUtils.loadAnimation(getActivity(),
                R.anim.anim_in_bottom);


    }


    protected void setupUI(View view) {

        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard();
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

    protected void hideSoftKeyboard() {
        try {
            if (getActivity() != null && !getActivity().isFinishing()) {
                InputMethodManager mInputMethodManager = (InputMethodManager) getActivity()
                        .getSystemService(INPUT_METHOD_SERVICE);

                if (getActivity().getCurrentFocus() != null
                        && getActivity().getCurrentFocus().getWindowToken() != null) {
                    mInputMethodManager.hideSoftInputFromWindow(getActivity()
                            .getCurrentFocus().getWindowToken(), 0);

                }
            }
        } catch (Exception e) {
            Log.e(getActivity().getClass().getSimpleName(), e.getMessage());
        }

    }

    public void sysOut(String msg) {
        try {
            System.out.println(msg);
        } catch (Exception e) {
            Log.e(AppConstants.TAG, msg);
        }
    }

    /*For trigger manually OnResume*/
    public void onFragmentResume() {
    }

    /*set BackPressed option manually */
    public void onFragmentBackPressed() {

    }


    public void nextScreen(Class<?> clazz, boolean finish) {
        Intent mIntent = new Intent(getActivity(), clazz);

        getActivity().startActivity(mIntent);
        getActivity().overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
        if (finish)
            getActivity().finish();

    }

    protected void previousScreen(Class<?> clazz, boolean finish) {
        Intent mIntent = new Intent(getActivity(), clazz);

        getActivity().startActivity(mIntent);
        getActivity().overridePendingTransition(R.anim.slide_out_right,
                R.anim.slide_in_left);
        if (finish)
            getActivity().finish();
    }

    protected void finishScreen() {
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.slide_out_right,
                R.anim.slide_in_left);

    }

    /*Ask permission for device access*/
    public boolean askAccessPermission(List<String> permissionStrList, int askPermissionCountInt, InterfaceTwoBtnCallback permissionCallback) {
        mAppPermissionsStrArrList = new ArrayList<>();
        mAppPermissionsStrArrList.addAll(permissionStrList);
        mAskPermissionCountInt = askPermissionCountInt;
        mPermissionCallback = permissionCallback;

        if (!mAppPermissionsStrArrList.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), mAppPermissionsStrArrList.toArray(new String[mAppPermissionsStrArrList.size()]), 200);
            return false;
        }

        return true;
    }

    /*Permission call back*/
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    /*Permission call back*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 200: {
                Map<String, Integer> perms = new HashMap<>();
                if (grantResults.length > 0) {
                    boolean isGrantAllPermissionBool = true;
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);

                    for (int j = 0; j < mAppPermissionsStrArrList.size(); j++) {
                        if (perms.get(mAppPermissionsStrArrList.get(j)) == PackageManager.PERMISSION_GRANTED) {
                            if (j == mAppPermissionsStrArrList.size() - 1) {
                                if (isGrantAllPermissionBool)
                                    mPermissionCallback.onPositiveClick();
                                else if (mAskPermissionCountInt == 2)
                                    mPermissionCallback.onNegativeClick();
                                else
                                    askAccessPermission(mAppPermissionsStrArrList, mAskPermissionCountInt + 1, mPermissionCallback);
                            }
                        } else {
                            isGrantAllPermissionBool = false;
                            //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
                            if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), mAppPermissionsStrArrList.get(j))) {
                                if (perms.get(mAppPermissionsStrArrList.get(j)) == PackageManager.PERMISSION_DENIED) {
                                    DialogManager.getInstance().showOptionPopup(getActivity(), getActivity().getString(R.string.go_settings_per), getString(R.string.yes), getString(R.string.no), new InterfaceTwoBtnCallback() {
                                        @Override
                                        public void onPositiveClick() {
                                            Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getActivity().getPackageName()));
                                            myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                                            myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivityForResult(myAppSettings, 168);
                                        }

                                        @Override
                                        public void onNegativeClick() {
                                            mPermissionCallback.onNegativeClick();
                                        }
                                    });
                                    break;

                                } else if (j == mAppPermissionsStrArrList.size() - 1) {
                                    if (mAskPermissionCountInt == 2)
                                        mPermissionCallback.onNegativeClick();
                                    else
                                        askAccessPermission(mAppPermissionsStrArrList, mAskPermissionCountInt + 1, mPermissionCallback);
                                }

                            } else {
                                if (j == mAppPermissionsStrArrList.size() - 1) {
                                    if (mAskPermissionCountInt == 2)
                                        mPermissionCallback.onNegativeClick();
                                    else
                                        askAccessPermission(mAppPermissionsStrArrList, mAskPermissionCountInt + 1, mPermissionCallback);
                                }
                            }
                        }
                    }

                } else if (mPermissionCallback != null) {
                    mPermissionCallback.onNegativeClick();
                }
            }
        }
    }


    /*API call back success*/
    public void onRequestSuccess(Object resObj) {

    }

    /*API call back failure*/
    public void onRequestFailure(Throwable t) {

        if (getActivity() != null) {
            if (t instanceof IOException || t.getCause() instanceof ConnectException || t.getCause() instanceof java.net.UnknownHostException
                    || t.getMessage() == null) {
                DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.no_internet), this);
            } else if (t.getCause() instanceof java.net.SocketTimeoutException) {
                DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.connect_time_out), this);
            } else if (t.getMessage() != null && !t.getMessage().isEmpty()) {
                DialogManager.getInstance().showAlertPopup(getActivity(), t.getMessage(), this);
            }
        }
    }


    protected void baseFragmentAlertDismiss(Dialog dialog) {
        /*To check if the dialog is shown, if the dialog is shown it will be cancelled */
        if (dialog != null && dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                Log.e(AppConstants.TAG, e.getMessage());
            }
        }
    }

    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }


    /*Interface default ok click*/
    @Override
    public void onPositiveClick() {

    }

    @Override
    public void onNegativeClick() {

    }
}
