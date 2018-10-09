package com.smaat.ipharma.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smaat.ipharma.R;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.utils.AppConstants;
import com.smaat.ipharma.utils.DialogManager;
import com.smaat.ipharma.utils.GlobalMethods;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 2/9/2017.
 */

public class ReminderSettings  extends BaseFragment {


    @BindView(R.id.doctor_number)
    TextView doctor_number;

    @BindView(R.id.guardian_number)
    TextView guardian_number;

    @BindView(R.id.save_btn)
    Button save_btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View m_pill_reminderRootView = inflater.inflate(R.layout.ui_reminder_settings, container, false);
        ButterKnife.bind(this, m_pill_reminderRootView);
        setupUI(m_pill_reminderRootView);
        doctor_number.setText(GlobalMethods.getStringValue(getActivity(), AppConstants.DOCTOR_NUMBER));
        guardian_number.setText(GlobalMethods.getStringValue(getActivity(), AppConstants.GUARDIAN_NUMBER));
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(guardian_number.getText().toString().trim().isEmpty())
                {
                    DialogManager.showMsgPopup(getActivity(),"","Please enter Guardian Number");
                }else if (!GlobalMethods.isValidMobile(guardian_number.getText().toString())) {
                    DialogManager.showMsgPopup(getActivity(),
                            "",
                            getString(R.string.enter_valid_phone));
                }else if(!doctor_number.getText().toString().trim().isEmpty()&&!GlobalMethods.isValidMobile(doctor_number.getText().toString()))
                {
                    DialogManager.showMsgPopup(getActivity(),
                            "",
                            getString(R.string.enter_valid_phone));
                }else{
                        if(!guardian_number.getText().toString().equalsIgnoreCase(""))
                        {
                            GlobalMethods.storeValuetoPreference(getActivity(),GlobalMethods.STRING_PREFERENCE, AppConstants.GUARDIAN_NUMBER,guardian_number.getText().toString());
                        }
                        /*if(!doctor_number.getText().toString().equalsIgnoreCase(""))
                        {*/
                            GlobalMethods.storeValuetoPreference(getActivity(),GlobalMethods.STRING_PREFERENCE, AppConstants.DOCTOR_NUMBER,doctor_number.getText().toString());
                        //}
                        showMsgPopup(getActivity(),"",getString(R.string.set_sav_suc));
                }

                /*if(guardian_number.getText().toString().trim().isEmpty()
                        && guardian_number.getText().toString().trim().length() < 1
                        && guardian_number.getText().toString().trim().equalsIgnoreCase(""))
                {
                    DialogManager.showMsgPopup(getActivity(),"","Enter a Guardian Number");

                }else if(doctor_number.getText().toString().trim().isEmpty()
                        && doctor_number.getText().toString().trim().length() < 1
                        && doctor_number.getText().toString().trim().equalsIgnoreCase(""))
                {
                    DialogManager.showMsgPopup(getActivity(),"","Enter a Doctor Number");

                }else{
                    GlobalMethods.storeValuetoPreference(getActivity(),GlobalMethods.STRING_PREFERENCE, AppConstants.GUARDIAN_NUMBER,guardian_number.getText().toString());
                    GlobalMethods.storeValuetoPreference(getActivity(),GlobalMethods.STRING_PREFERENCE, AppConstants.DOCTOR_NUMBER,doctor_number.getText().toString());
                    DialogManager.showMsgPopup(getActivity(),"",getString(R.string.set_sav_suc));
                }*/




            }
        });

        return m_pill_reminderRootView;

    }

    public static void showMsgPopup(final Context mContext, String title, String msg) {
        mDialog = DialogManager.getDialog(mContext, R.layout.popup_msg_layout);

        ImageView m_btnYes = (ImageView) mDialog.findViewById(R.id.yes_btn);
        ImageView m_btnNo = (ImageView) mDialog.findViewById(R.id.close_button);
        m_btnNo.setVisibility(View.GONE);
        TextView mTitte=(TextView)mDialog.findViewById(R.id.title_text);
        mTitte.setText(msg);

        m_btnYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                ((HomeScreen) mContext).pushFragment(new PillReminderListFragment());

            }
        });

        mDialog.show();
    }


    @Override
    public void onResume() {
        super.onResume();
        ((HomeScreen) getActivity()).setToolbarTitle(getString(R.string.reminder_st),getString(R.string.home));
    }
}
