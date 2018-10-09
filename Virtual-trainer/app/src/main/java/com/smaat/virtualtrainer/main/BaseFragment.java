package com.smaat.virtualtrainer.main;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class BaseFragment extends Fragment {

    protected Typeface mHelvetica,mHelveticaLight;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setupUI(View view) {

        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
					 hideSoftKeyboard(getActivity());
                    return false;
                }

            });
        }
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }


    public void setFont(ViewGroup group, Typeface font) {
        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView || v instanceof Button /* etc. */)
                ((TextView) v).setTypeface(font);
            else if (v instanceof ViewGroup)
                setFont((ViewGroup) v, font);
        }
    }


    VirtualTrainerApplication app(){
        return ((VirtualTrainerApplication) getActivity().getApplication()) ;
    }
//    public com.smaat.virtualtrainer.oovoo.ui.fragments.BaseFragment getBackFragment() {
//        return back_fragment;
//    }
//
//    public void setBackFragment(com.smaat.virtualtrainer.oovoo.ui.fragments.BaseFragment back_fragment) {
//        this.back_fragment = back_fragment;
//    }

    public boolean onBackPressed() {
        return false ;
    }
    public void hideSoftKeyboard(Context fragment) {
        try {
            if (fragment != null) {
                getActivity()
                        .getWindow()
                        .setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                InputMethodManager imm = (InputMethodManager) fragment
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void onRequestSuccess(Object responseObj) {

    }


//    public void onRequestFailure(RetrofitError errorCode) {
//
//        if (errorCode.getCause() instanceof ConnectException ||errorCode.getCause() instanceof java.net.UnknownHostException) {
//            DialogManager.showBasicAlertDialog(getActivity(), getString(R.string.app_name), getString(R.string.no_internet), new DialogMangerOkCallback() {
//                @Override
//                public void onOkClick() {
//
//                }
//            });
//        } else if (errorCode.getCause() instanceof java.net.SocketTimeoutException) {
//            DialogManager.showBasicAlertDialog(getActivity(), getString(R.string.app_name),
//                    getString(R.string.connect_time_out), new DialogMangerOkCallback() {
//                        @Override
//                        public void onOkClick() {
//
//                        }
//                    });
//
//        } else if (errorCode.getCause() instanceof retrofit.converter.ConversionException) {
//            DialogManager.showBasicAlertDialog(getActivity(), getString(R.string.app_name),
//                    getString(R.string.serv_con_exce), new DialogMangerOkCallback() {
//                        @Override
//                        public void onOkClick() {
//
//                        }
//                    });
//        } else {
//            DialogManager.showBasicAlertDialog(getActivity(), getString(R.string.app_name),
//                    getString(R.string.serv_not_res), new DialogMangerOkCallback() {
//                        @Override
//                        public void onOkClick() {
//
//                        }
//                    });
//
//        }
//
//    }

}
