package com.smaat.ipharma.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Ringtone;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smaat.ipharma.R;
import com.smaat.ipharma.apiinterface.APIRequestHandler;
import com.smaat.ipharma.fragments.PharmacyListFragment;
import com.smaat.ipharma.fragments.SettingFragment;
import com.smaat.ipharma.fragments.ShopdetailFragment;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.wang.avi.AVLoadingIndicatorView;


public class DialogManager {

    static Dialog mDialog;
    static Dialog progress;

    public void showAlertDialog(Context mContext) {

    }

    public static void showToast(Context mContext, String mString) {
        Toast.makeText(mContext, mString, Toast.LENGTH_SHORT).show();
    }



    public static Dialog getDialog(Context mContext, int mLayout) {


        Dialog mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mDialog.setContentView(mLayout);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);

        return mDialog;
    }


    private static Dialog getLoadingDialog(Context mContext) {

        mDialog = getDialog(mContext, R.layout.progress);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        return mDialog;
    }

    public static void showProgress(Context context) {

        progress = getLoadingDialog(context);
        progress.show();
        AVLoadingIndicatorView avi = (AVLoadingIndicatorView)progress.findViewById(R.id.avi);
        avi.show();

    }

    public static void hideProgress() {

        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }

    }
    public static void showLogoutDialog(final Context mContext) {
        mDialog = getDialog(mContext, R.layout.popup_msg_layout);
        ImageView m_btnYes = (ImageView) mDialog.findViewById(R.id.yes_btn);
        ImageView m_btnNo = (ImageView) mDialog.findViewById(R.id.close_button);
        TextView title = (TextView) mDialog.findViewById(R.id.title_text);
        title.setText(mContext.getString(R.string.do_u_want));

        m_btnYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                GlobalMethods.clearAllPreferences(mContext);


            }
        });
        m_btnNo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }


    public static void showMsgPopup(final Context mContext,String title,String msg) {
        mDialog = getDialog(mContext, R.layout.popup_msg_layout);

        ImageView m_btnYes = (ImageView) mDialog.findViewById(R.id.yes_btn);
        ImageView m_btnNo = (ImageView) mDialog.findViewById(R.id.close_button);
        m_btnNo.setVisibility(View.GONE);
        TextView mTitte=(TextView)mDialog.findViewById(R.id.title_text);
        mTitte.setText(msg);

        m_btnYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();


            }
        });

        mDialog.show();
    }

    public static void enableTouch(final FragmentActivity activity) {
        mDialog = DialogManager.getDialog(activity, R.layout.popup_msg_layout);

        ImageView m_btnYes = (ImageView) mDialog.findViewById(R.id.yes_btn);
        ImageView m_btnNo = (ImageView) mDialog.findViewById(R.id.close_button);
        TextView title = (TextView) mDialog.findViewById(R.id.title_text);
        title.setText(activity.getString(R.string.enable_touch));

        m_btnYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                showMsgPopup(activity,"",activity.getString(R.string.enable_touch2));
                AppConstants.enableOneTouch = true;
            }
        });
        m_btnNo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                AppConstants.enableOneTouch = false;

            }
        });

        mDialog.show();
    }




    public static void showSucessPopup(final Context mContext,String title,String msg) {
        mDialog = getDialog(mContext, R.layout.popup_msg_layout);

        ImageView m_btnYes = (ImageView) mDialog.findViewById(R.id.yes_btn);
        ImageView m_btnNo = (ImageView) mDialog.findViewById(R.id.close_button);
        m_btnNo.setVisibility(View.GONE);
        TextView mTitte=(TextView)mDialog.findViewById(R.id.title_text);
        mTitte.setText(msg);

        m_btnYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                ((HomeScreen) mContext).replaceFragment(new PharmacyListFragment());

            }
        });

        m_btnNo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();

            }
        });

        mDialog.show();
    }







}
