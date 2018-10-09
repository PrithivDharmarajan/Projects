package com.smaat.ipharma.main;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.smaat.ipharma.R;
import com.smaat.ipharma.util.DialogManager;
import com.smaat.ipharma.util.DialogMangerCallback;
import com.smaat.ipharma.util.TypefaceSingleton;

import java.net.ConnectException;

import retrofit.RetrofitError;

public class BaseFragment extends Fragment implements DialogMangerCallback {
    private AQuery aq;
    public static Dialog mDialog;
    protected Typeface mHelvetica, mHelveticaBold, mHelveticaLight, mHighTower;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mHelvetica = TypefaceSingleton.getInstance().getHelvetica(
                getActivity());
        mHelveticaBold = TypefaceSingleton.getInstance().getHelveticaBold(
                getActivity());
        mHelveticaLight = TypefaceSingleton.getInstance().getHelveticaLight(
                getActivity());
        mHighTower = TypefaceSingleton.getInstance().getHighTower(
                getActivity());

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setupUI(View view) {

        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
//					 hideSoftKeyboard(getActivity());
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

    public void showToast(Context fragment, String msg) {
        Toast.makeText(fragment, msg, Toast.LENGTH_SHORT).show();
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


    public AQuery aq() {
        if (aq == null) {
            aq = new AQuery(getActivity());
        }
        return aq;
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


    public void onRequestFailure(RetrofitError errorCode) {


        if (errorCode.getCause() instanceof ConnectException || errorCode.getCause() instanceof java.net.UnknownHostException) {
            //No internet connection and  UnknownHostException
            DialogManager.showCustomAlertDialog(getActivity(), this, getString(R.string.no_network));
        } else if (errorCode.getCause() instanceof java.net.SocketTimeoutException) {
            //SocketTimeoutException
            DialogManager.showCustomAlertDialog(getActivity(), this, getString(R.string.conn_time_out));

        } else if (errorCode.getCause() instanceof retrofit.converter.ConversionException) {
            //ConversionException - Response is different fomat and decl format is different
            DialogManager.showCustomAlertDialog(getActivity(), this, getString(R.string.serv_con_exce));

        } else {
            //Other Exception...
            DialogManager.showCustomAlertDialog(getActivity(), this, getString(R.string.serv_not_res));
        }

    }

    @Override
    public void onItemclick(String SelctedItem, int pos) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onOkclick() {
        // TODO Auto-generated method stub

    }


}
