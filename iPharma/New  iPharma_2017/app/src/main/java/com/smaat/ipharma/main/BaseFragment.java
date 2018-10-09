package com.smaat.ipharma.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;


import com.androidquery.AQuery;
import com.smaat.ipharma.R;
import com.smaat.ipharma.utils.DialogManager;

import java.io.IOException;
import java.net.ConnectException;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class BaseFragment extends Fragment {
    public static Dialog mDialog;
    public static Activity mActivity;
    private AQuery aq;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mActivity = getActivity();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Ubuntu-R.ttf").build());
    }

    @Override
    public void onResume() {
        super.onResume();

        /*if( this instanceof ContactUsFragment) {
            ((HomeScreen) getActivity()).setToolbarTitle(getActivity().getString(R.string.txt_contact));
            ((HomeScreen) getActivity()).changeBackButtonImage(2, 0, "");
        } else if(this instanceof FindABarberFragment){
            ((HomeScreen) getActivity()).setToolbarTitle(getActivity().getString(R.string.txt_find_barber));
            ((HomeScreen) getActivity()).changeBackButtonImage(1,R.drawable.filter_image,"FindBarber");
        }
        else if(this instanceof ShopDetailsFragment){
            if(AppConstants.SELECTED_SHOPLIST_RESPONSE_ENTITY.getVerified().equalsIgnoreCase("1"))
            {
                ((HomeScreen) getActivity()).changeBackButtonImage(2, 0, "ShopDetail");
            }else{
                ((HomeScreen) getActivity()).changeBackButtonImage(2, 0, "ShopDetail2");
            }

            if (AppConstants.SELECTED_FAVOURITE_ENTITY != null) {
                ((HomeScreen) getActivity()).setToolbarTitle(AppConstants.SELECTED_FAVOURITE_ENTITY.getShop_name());
            }
        }*/
    }

    public void setupUI(View view) {

        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new OnTouchListener() {

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

    public void onBackPressed() {

    }

    public void onRequestSuccess(Object responseObj) {

    }

    public Bitmap ResizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable",  mActivity.getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }


    public void onRequestFailure(Throwable t) {

        try {
            System.out.println("errorCode.getCause() Msg--------" + t.getMessage());
            System.out.println("errorCode.getCause() --------" + t.getCause().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (t instanceof IOException ||t.getCause() instanceof ConnectException || t.getCause() instanceof java.net.UnknownHostException
                || t.getMessage() == null) {

            DialogManager.showMsgPopup(getActivity(),"",getString(R.string.no_internet));

        } else if (t.getCause() instanceof java.net.SocketTimeoutException) {


            DialogManager.showMsgPopup(getActivity(),"",getString(R.string.connect_time_out));

        } else {
            DialogManager.showMsgPopup(getActivity(),"",t.getMessage());

        }


    }


    public void onRequestFailureForList(Throwable t, RelativeLayout rlayout, ListView lView) {

        try {
            System.out.println("errorCode.getCause() Msg--------" + t.getMessage());
            System.out.println("errorCode.getCause() --------" + t.getCause().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (t instanceof IOException ||t.getCause() instanceof ConnectException || t.getCause() instanceof java.net.UnknownHostException
                || t.getMessage() == null) {

            //DialogManager.showMsgPopup(getActivity(),"",getString(R.string.no_internet));
            if(rlayout!=null)
            {
                rlayout.setVisibility(View.VISIBLE);
                lView.setVisibility(View.GONE);
            }

        } else if (t.getCause() instanceof java.net.SocketTimeoutException) {

            if(rlayout!=null)
            {
                rlayout.setVisibility(View.VISIBLE);
                lView.setVisibility(View.GONE);
            }
            //DialogManager.showMsgPopup(getActivity(),"",getString(R.string.connect_time_out));

        } else {
            if(rlayout!=null)
            {
                rlayout.setVisibility(View.VISIBLE);
                lView.setVisibility(View.GONE);
            }
            //DialogManager.showMsgPopup(getActivity(),"",t.getMessage());

        }


    }
}
