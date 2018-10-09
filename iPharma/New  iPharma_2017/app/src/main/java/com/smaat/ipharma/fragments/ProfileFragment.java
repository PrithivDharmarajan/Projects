package com.smaat.ipharma.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.ipharma.R;
import com.smaat.ipharma.apiinterface.APIRequestHandler;
import com.smaat.ipharma.entity.CommonResponse;
import com.smaat.ipharma.entity.GoogleApiEntity;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.ui.OneTimePassword;
import com.smaat.ipharma.ui.SignupScreen;
import com.smaat.ipharma.utils.AppConstants;
import com.smaat.ipharma.utils.DialogManager;
import com.smaat.ipharma.utils.GPSTracker;
import com.smaat.ipharma.utils.GlobalMethods;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin on 1/24/2017.
 */

public class ProfileFragment extends BaseFragment {
    View m_profileRootView;


    @BindView(R.id.parent_layout)
    LinearLayout m_linParent;

    @BindView(R.id.user_name_txt)
    EditText m_textUserName;
    @BindView(R.id.email_text)
    EditText m_textEmail;
    @BindView(R.id.password_text)
    EditText m_textPassword;
    @BindView(R.id.mob_text)
    EditText m_textMob;
    @BindView(R.id.address_text)
    EditText m_textAddress;

    /*@BindView(R.id.area_text)
    EditText m_textArea;*/
    @BindView(R.id.city_text)
    EditText m_textCity;
    @BindView(R.id.pincode_text)
    EditText m_textPincode;
    int disabeleFlag=0;
   /*@BindView(R.id.save_imge)
    ImageView m_ImgsSave;*/
//

    double latitude = 0.0;
    double longitude = 0.0;
    private boolean is_check = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        m_profileRootView = inflater.inflate(R.layout.ui_profile_fragment, container, false);
        ButterKnife.bind(this, m_profileRootView);

        setupUI(m_linParent);
        initView();
        return m_profileRootView;
    }
    String m_strUserID,m_StrUserName,m_StrEmail,m_StrPassword,m_StrMobl,m_StrAdress,m_strCity,m_StrArea,m_strPincode;

    private void initView() {
        m_strUserID = GlobalMethods.getUserID(getActivity());
        m_StrUserName = (String) GlobalMethods.getValueFromPreference(getActivity(), GlobalMethods.STRING_PREFERENCE, AppConstants.USER_NAME);
        m_StrEmail = (String) GlobalMethods.getValueFromPreference(getActivity(), GlobalMethods.STRING_PREFERENCE, AppConstants.EMAIL_ADDRESS);
        m_StrPassword = (String) GlobalMethods.getValueFromPreference(getActivity(), GlobalMethods.STRING_PREFERENCE, AppConstants.PASSWORD);
        m_StrMobl = (String) GlobalMethods.getValueFromPreference(getActivity(), GlobalMethods.STRING_PREFERENCE, AppConstants.PHONE_NUMBER);
        m_StrAdress = (String) GlobalMethods.getValueFromPreference(getActivity(), GlobalMethods.STRING_PREFERENCE, AppConstants.USER_ADDRESS);
        m_strCity = (String) GlobalMethods.getValueFromPreference(getActivity(), GlobalMethods.STRING_PREFERENCE, AppConstants.CITY);
        m_StrArea = (String) GlobalMethods.getValueFromPreference(getActivity(), GlobalMethods.STRING_PREFERENCE, AppConstants.AREA);
        m_strPincode = (String) GlobalMethods.getValueFromPreference(getActivity(), GlobalMethods.STRING_PREFERENCE, AppConstants.USER_AREA_PINCODE);

        m_textUserName.setText(m_StrUserName);
        m_textEmail.setText(m_StrEmail);
        m_textPassword.setText(m_StrPassword);
        m_textMob.setText(m_StrMobl);
        m_textAddress.setText(m_StrAdress);
        m_textCity.setText(m_strCity);
        m_textPincode.setText(m_strPincode);
        disabeleFlag=0;
        disbleEditing();
    }
    public void UpdateProfile()
    {
        if(disabeleFlag==0){
            if(validateFields()){

                APIRequestHandler.getInstance().updateProfileApi(m_strUserID.trim(),m_StrUserName.trim(), m_StrEmail.trim(), m_StrMobl.trim(), m_StrPassword.trim(),
                        m_StrAdress.trim(), m_strCity.trim(), m_StrArea.trim(), m_strPincode.trim(), ProfileFragment.this);
            }

        }else{
            enableEditing();
        }
    }

   private void disbleEditing(){

       disableEditText(m_textUserName);
       disableEditText(m_textEmail);
       disableEditText(m_textPassword);
       disableEditText(m_textMob);
       disableEditText(m_textAddress);
       //disableEditText(m_textArea);
       disableEditText(m_textCity);
       disableEditText(m_textPincode);
       disabeleFlag=1;
       HomeScreen.right_side_menu_icon.setImageResource(R.drawable.pf_edit);

   }
    private void enableEditing(){
        enableEditText(m_textUserName);
        //enableEditText(m_textEmail);
        enableEditText(m_textPassword);
        enableEditText(m_textMob);
        enableEditText(m_textAddress);
        //enableEditText(m_textArea);
        enableEditText(m_textCity);
        enableEditText(m_textPincode);
        m_textUserName.requestFocus();
        disabeleFlag=0;
        HomeScreen.right_side_menu_icon.setImageResource(R.drawable.pf_save);
    }
    private void disableEditText(EditText editText) {
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setTextColor(getResources().getColor(R.color.greycolor));

    }
    private void enableEditText(EditText editText) {
        editText.setFocusable(true);
        editText.setEnabled(true);
        editText.setCursorVisible(true);
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setTextColor(getResources().getColor(R.color.blackcolor));
    }
    @OnClick({R.id.use_cur_location_layout, R.id.user_img/*,R.id.save_imge*/})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.use_cur_location_layout:
                if(disabeleFlag==0){
                    showCurrentLocation();

                }


                break;

            case R.id.user_img:
                break;
            /*case R.id.save_imge:


                break;*/
        }
    }


    private void showCurrentLocation() {
        GPSTracker tracker = new GPSTracker(getActivity());
        if (tracker.canGetLocation() == false) {
            tracker.showSettingsAlert();
        } else {
            latitude = tracker.getLatitude();
            longitude = tracker.getLongitude();
            callGoogleApiService(latitude, longitude);
        }
    }

    private void callGoogleApiService(double latitude, double longitude) {

        String url = AppConstants.LATLNG_LINK + latitude + "," + longitude
                + "&sensor=true";
        aq().ajax(url, JSONObject.class, this,getString(R.string.addresslocation));
    }

    public void addresslocation(String url, JSONObject json, AjaxStatus status) {
        if (json != null) {
            try {
                GoogleApiEntity obj = new Gson().fromJson(json.toString(),
                        GoogleApiEntity.class);
                onRequest(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void onRequest(GoogleApiEntity response) {
        try {
            if (response != null) {
                if (response.getResults().size() > 0) {
                    Log.e("getResults","getResults"+ response.getResults().get(0).getFormatted_address());
                    String[] addresses = response.getResults().get(0)
                            .getFormatted_address().toString().split(",");
                    if (addresses.length >= 4) {
                        String add_txt = response
                                .getResults()
                                .get(0)
                                .getFormatted_address()
                                .replace((addresses[(addresses.length - 3)] + ","),"").replace((addresses[(addresses.length - 2)] + ","),"").replace(addresses[(addresses.length - 1)], "");
                        add_txt = add_txt.substring(0, add_txt.length()-1);
                        String area = addresses[(addresses.length - 4)].trim();
                        String city = addresses[(addresses.length - 3)].trim();
                        String pincode = addresses[(addresses.length - 2)].replaceAll("\\D", "");
                        m_textAddress.setText(add_txt.trim());
                        m_textCity.setText(city.trim());
                        m_textPincode.setText(pincode.trim());
                        m_StrArea=area;
                        //m_textArea.setText(area);
                    }
                } else {
                    DialogManager.showMsgPopup(getActivity(),getString(R.string.app_name),
                            getString(R.string.unable_to_find_location));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean validateFields() {

        m_StrUserName = m_textUserName.getText().toString().trim();
        m_StrEmail = m_textEmail.getText().toString().trim();
        m_StrPassword = m_textPassword.getText().toString().trim();
        m_StrMobl = m_textMob.getText().toString().trim();
        m_StrAdress = m_textAddress.getText().toString().trim();
        m_strCity = m_textCity.getText().toString().trim();
        m_strPincode = m_textPincode.getText().toString().trim();


        if (m_StrUserName.isEmpty()
                && m_textUserName.getText().toString().trim().length() < 1
                && m_textUserName.getText().toString().trim().equalsIgnoreCase("")) {
            DialogManager.showMsgPopup(getContext(),"", getString(R.string.enter_name));
            return false;
        } else if (!GlobalMethods.isEmailValid(m_textEmail.getText().toString()
                .trim())) {
            DialogManager.showMsgPopup(getContext(),"",getString(R.string.enter_email));


            return false;
        } else if (m_StrMobl.isEmpty()) {
            DialogManager.showMsgPopup(getActivity(),
                    getString(R.string.registration_failed),
                    getString(R.string.enter_phone));
            return false;
        }else if (!GlobalMethods.isValidMobile(m_StrMobl)) {
            DialogManager.showMsgPopup(getActivity(),
                    getString(R.string.registration_failed),
                    getString(R.string.enter_valid_phone));
            return false;
        }

        else if (m_textPassword.getText().toString().trim().isEmpty()
                && m_textPassword.getText().toString().trim().length() < 1
                && m_textPassword.getText().toString().trim().equalsIgnoreCase("")) {
            DialogManager.showMsgPopup(getContext(),"",getString(R.string.enter_password));

            return false;
        }else if (m_textPassword.getText().toString().trim().length() < 8) {
            DialogManager.showMsgPopup(getActivity(),
                    getString(R.string.registration_failed),
                    getString(R.string.enter_password_valid));
            return false;
        }
       else if (m_textAddress.getText().toString().trim().isEmpty()
                && m_textAddress.getText().toString().trim().length() < 1
                && m_textAddress.getText().toString().trim().equalsIgnoreCase("")) {
            DialogManager.showMsgPopup(getContext(),"",getString(R.string.enter_address));


            return false;
        }else if (m_textCity.getText().toString().trim().isEmpty()
                && m_textCity.getText().toString().trim().length() < 1
                && m_textCity.getText().toString().trim().equalsIgnoreCase("")) {

            DialogManager.showMsgPopup(getContext(),"",getString(R.string.enter_city));

            return false;
        }  else if (m_textPincode.getText().toString().trim().isEmpty()
                && m_textPincode.getText().toString().trim().length() < 1
                && m_textPincode.getText().toString().trim().equalsIgnoreCase("")) {

            DialogManager.showMsgPopup(getContext(),"", getString(R.string.enter_pincode));
            return false;
        }

        return true;
    }
    @Override
    public void onRequestSuccess(Object responseObj) {
        // TODO Auto-generated method stub
        super.onRequestSuccess(responseObj);
        CommonResponse resultObj = (CommonResponse) responseObj;
        /*if(resultObj.getStatus().equalsIgnoreCase(AppConstants.SUCCESS_CODE))
        {*/
            disbleEditing();
            GlobalMethods.storeValuetoPreference(getActivity(),GlobalMethods.STRING_PREFERENCE,AppConstants.USER_NAME,m_textUserName.getText().toString());
            GlobalMethods.storeValuetoPreference(getActivity(),GlobalMethods.STRING_PREFERENCE,AppConstants.EMAIL_ADDRESS,m_textEmail.getText().toString());
            GlobalMethods.storeValuetoPreference(getActivity(),GlobalMethods.STRING_PREFERENCE,AppConstants.PASSWORD,m_textPassword.getText().toString());
            GlobalMethods.storeValuetoPreference(getActivity(),GlobalMethods.STRING_PREFERENCE,AppConstants.PHONE_NUMBER,m_textMob.getText().toString());
            GlobalMethods.storeValuetoPreference(getActivity(),GlobalMethods.STRING_PREFERENCE,AppConstants.USER_ADDRESS,m_textAddress.getText().toString());
            GlobalMethods.storeValuetoPreference(getActivity(),GlobalMethods.STRING_PREFERENCE,AppConstants.CITY,m_textCity.getText().toString());
            //GlobalMethods.storeValuetoPreference(getActivity(),GlobalMethods.STRING_PREFERENCE,AppConstants.AREA,m_textArea.getText().toString());
            GlobalMethods.storeValuetoPreference(getActivity(),GlobalMethods.STRING_PREFERENCE,AppConstants.USER_AREA_PINCODE,m_textPincode.getText().toString());
            DialogManager.showMsgPopup(getContext(),"", getString(R.string.profil_updated_successfully));
            ((HomeScreen)getActivity()).setProfileName();
        /*}else{
            DialogManager.showToast(getContext(), resultObj.getMsg());
        }*/

    }
    @Override
    public void onResume() {
        super.onResume();
        ((HomeScreen) getActivity()).setToolbarTitle(getString(R.string.profile),getString(R.string.home));
    }
}