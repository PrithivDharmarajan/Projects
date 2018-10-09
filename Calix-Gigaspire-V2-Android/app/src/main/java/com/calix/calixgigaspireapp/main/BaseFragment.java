package com.calix.calixgigaspireapp.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.utils.DialogManager;
import com.calix.calixgigaspireapp.utils.InterfaceBtnCallback;

import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class BaseFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
        // Init default font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Roboto-Regular.otf").build());

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
                        && getActivity().getCurrentFocus().getWindowToken() != null && mInputMethodManager != null) {
                    mInputMethodManager.hideSoftInputFromWindow(getActivity()
                            .getCurrentFocus().getWindowToken(), 0);
                }
            }
        } catch (Exception e) {
            Log.e(getActivity().getClass().getSimpleName(), e.getMessage());
        }

    }

    /*For trigger manually OnResume*/
    public void onFragmentResume() {
    }

    /*set BackPressed option manually */
    public void onFragmentBackPressed() {
    }


    /*API call back success*/
    public void onRequestSuccess(Object resObj) {

    }

    /*API call back failure*/
    public void onRequestFailure(Object inputModelObj, Throwable t) {
        if (getActivity() != null && t.getMessage() != null && !t.getMessage().isEmpty()) {
            if (t.getCause() instanceof java.net.SocketTimeoutException) {

                DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.connect_time_out), new InterfaceBtnCallback() {
                    @Override
                    public void onPositiveClick() {

                    }
                });
            } else if (!(t instanceof IOException)) {
                DialogManager.getInstance().showAlertPopup(getActivity(), t.getMessage(), new InterfaceBtnCallback() {
                    @Override
                    public void onPositiveClick() {

                    }
                });
            }
        }

    }

//    /*API call back failure*/
//    public void onRequestFailure(Throwable t) {
//        if (getActivity() != null && t != null && t.getMessage() != null && !t.getMessage().isEmpty()) {
//            if (t.getCause() instanceof java.net.SocketTimeoutException) {
//                DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.connect_time_out), new InterfaceBtnCallback() {
//                    @Override
//                    public void onPositiveClick() {
//
//                    }
//                });
//            } else if (!(t instanceof IOException)) {
//                DialogManager.getInstance().showAlertPopup(getActivity(), t.getMessage(), new InterfaceBtnCallback() {
//                    @Override
//                    public void onPositiveClick() {
//
//                    }
//                });
//            }
//        }
//
//    }


}
