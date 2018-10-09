package com.smaat.ipharma.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.smaat.ipharma.R;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.ui.AboutFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.utils.AppConstants;
import com.smaat.ipharma.utils.DialogManager;
import com.smaat.ipharma.utils.GlobalMethods;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.smaat.ipharma.utils.AppConstants.Url;

/**
 * Created by vignesh on 11/3/2016.
 */

public class SettingFragment extends BaseFragment {
    View m_settingRootView;


    @BindView(R.id.parent_layout)
    LinearLayout m_linParent;

    @BindView(R.id.terms_and_condition_text)
    TextView m_textTermsAndCondition;
    @BindView(R.id.logout_text)
    TextView m_textLogout;
    @BindView(R.id.push_notification_toggle)
    ToggleButton toggleButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        m_settingRootView = inflater.inflate(R.layout.ui_setting_fragment, container, false);
        ButterKnife.bind(this, m_settingRootView);

        setupUI(m_linParent);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    GlobalMethods.storeValuetoPreference(getActivity(),GlobalMethods.BOOLEAN_PREFERENCE,AppConstants.PUSH_NOTIFICATION,false);
                }else{
                    GlobalMethods.storeValuetoPreference(getActivity(),GlobalMethods.BOOLEAN_PREFERENCE,AppConstants.PUSH_NOTIFICATION,true);
                }
            }
        });
        return m_settingRootView;
    }

    @OnClick({R.id.notification_layout, R.id.terms_and_condition_layout,
            R.id.logout_layout})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.notification_layout:
                break;

            case R.id.logout_layout:
                DialogManager.showLogoutDialog(getActivity());
                break;

            case R.id.terms_and_condition_layout:
                Url = AppConstants.ABOUT_LINK;
                ((HomeScreen) getActivity()).pushFragment(new AboutFragment());
                break;



        }
    }



    @Override
    public void onResume() {
        super.onResume();
        ((HomeScreen) getActivity()).setToolbarTitle(getString(R.string.txt_settings),getString(R.string.home));
    }




}

