package com.smaat.ipharma.ui;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.smaat.ipharma.R;
import com.smaat.ipharma.apiinterface.APIRequestHandler;
import com.smaat.ipharma.entity.CommonResponse;
import com.smaat.ipharma.entity.GoogleApiEntity;
import com.smaat.ipharma.main.BaseActivity;
import com.smaat.ipharma.utils.AppConstants;
import com.smaat.ipharma.utils.DialogManager;
import com.smaat.ipharma.utils.GPSTracker;
import com.smaat.ipharma.utils.GlobalMethods;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin on 1/5/2017.
 */

public class SignupScreen extends BaseActivity {



    @BindView(R.id.parent_layout)
    LinearLayout m_Mainlayout;
    @BindView(R.id.backbutton)
    ImageView BackButton;

    @BindView(R.id.user_name)
    EditText mUserName;
    @BindView(R.id.email_id)
    EditText mEmailId;
    @BindView(R.id.phone_number)
    EditText mPhoneNumber;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.address)
    EditText mAddress;
    @BindView(R.id.city)
    EditText mCity;
    @BindView(R.id.pincode)
    EditText mPincode;
    @BindView(R.id.use_cur_location)
    TextView UseCurlocation;


    @BindView(R.id.signup_btn_click)
    ImageView sign_up_btn;

    private String m_strUserName = "";
    private String m_strEmailid = "";
    private String m_strPhoneNo = "";
    private String m_strPassword = "";
    private String m_strAddress = "";
    private String m_strCity = "";
    private String m_strPincode = "";
    private GPSTracker gps_tracker;
    double latitude = 0.0;
    double longitude = 0.0;
    private String device_id = "";
    private boolean is_check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_signup_screen);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.bind(this);
        gps_tracker = new GPSTracker(getApplicationContext());
        setupUI(m_Mainlayout);
        if(GlobalMethods.getStringValue(getApplicationContext(),AppConstants.DEVICE_ID).isEmpty())
        {
            GlobalMethods.storeValuetoPreference(this, 1, AppConstants.DEVICE_ID, FirebaseInstanceId.getInstance().getToken());
        }
    }

    @OnClick({R.id.backbutton,R.id.signup_btn,R.id.use_cur_location,R.id.signup_btn_click})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backbutton:
                onBackPressed();
                break;
            case R.id.signup_btn:
                if(gps_tracker.canGetLocation())
                {
                    latitude = gps_tracker.getLatitude();
                    longitude = gps_tracker.getLongitude();
                }
                if(IsConnecttointernet())
                {
                    if (ValidRegister()) {
                        APIRequestHandler.getInstance().signupAPICall(m_strUserName, m_strEmailid, m_strPhoneNo, m_strPassword,
                                m_strAddress, m_strCity, m_strCity, m_strPincode, String.valueOf(latitude), String.valueOf(longitude),
                                getApplicationContext().getString(R.string.android), GlobalMethods.getStringValue(getApplicationContext(),AppConstants.DEVICE_ID),SignupScreen.this);
                    }
                }else{
                    DialogManager.showMsgPopup(SignupScreen.this,"",getString(R.string.no_internet));
                }

                break;
            case R.id.use_cur_location:
                if (!is_check) {
                    if(IsConnecttointernet())
                    {
                        showCurrentLocation();
                        is_check = true;
                    }else{
                        DialogManager.showMsgPopup(SignupScreen.this,"",getString(R.string.no_internet));
                    }

                } else {
                    mCity.setText("");
                    mPincode.setText("");
                    mAddress.setText("");
                    is_check = false;
                }
                break;

            case R.id.signup_btn_click:
                if(gps_tracker.canGetLocation())
                {
                    latitude = gps_tracker.getLatitude();
                    longitude = gps_tracker.getLongitude();
                }
                if (ValidRegister()) {
                    APIRequestHandler.getInstance().signupAPICall(m_strUserName, m_strEmailid, m_strPhoneNo, m_strPassword,
                            m_strAddress, m_strCity, m_strCity, m_strPincode, String.valueOf(latitude), String.valueOf(longitude),
                            getApplicationContext().getString(R.string.android), GlobalMethods.getStringValue(getApplicationContext(),AppConstants.DEVICE_ID),SignupScreen.this);
                }
                break;
            default:
                break;

        }
    }


    private boolean ValidRegister() {

        m_strUserName = mUserName.getText().toString();
        m_strEmailid = mEmailId.getText().toString();
        m_strPassword = mPassword.getText().toString();
        m_strPhoneNo = mPhoneNumber.getText().toString();
        m_strAddress = mAddress.getText().toString();
        m_strCity = mCity.getText().toString();
        m_strPincode = mPincode.getText().toString();
        if(GlobalMethods.getStringValue(getApplicationContext(),AppConstants.DEVICE_ID).isEmpty())
        {
            GlobalMethods.storeValuetoPreference(this, 1, AppConstants.DEVICE_ID, FirebaseInstanceId.getInstance().getToken());
        }

        if (mUserName.getText().toString().trim().isEmpty()
                && mUserName.getText().toString().trim().length() < 1
                && mUserName.getText().toString().trim().equalsIgnoreCase("")) {
            DialogManager.showMsgPopup(this,
                    getString(R.string.registration_failed),
                    getString(R.string.enter_name));
            return false;
        } else if (mEmailId.getText().toString().trim().equalsIgnoreCase("")) {
            DialogManager.showMsgPopup(SignupScreen.this,"",getString(R.string.enter_email_empty));
            return false;
        }else if (!GlobalMethods.isEmailValid(mEmailId.getText().toString()
                .trim())) {
            DialogManager.showMsgPopup(this,
                    getString(R.string.registration_failed),
                    getString(R.string.enter_email));
            return false;
        } else if (m_strPhoneNo.isEmpty()) {
            DialogManager.showMsgPopup(this,
                    getString(R.string.registration_failed),
                    getString(R.string.enter_phone));
            return false;
        }else if (!GlobalMethods.isValidMobile(m_strPhoneNo)) {
            DialogManager.showMsgPopup(this,
                    getString(R.string.registration_failed),
                    getString(R.string.enter_valid_phone));
            return false;
        }

        else if (mPassword.getText().toString().trim().isEmpty()
                && mPassword.getText().toString().trim().length() < 1
                && mPassword.getText().toString().trim().equalsIgnoreCase("")) {
            DialogManager.showMsgPopup(this,
                    getString(R.string.registration_failed),
                    getString(R.string.enter_password));
            return false;
        }else if (mPassword.getText().toString().trim().length() < 8) {
            DialogManager.showMsgPopup(this,
                    getString(R.string.registration_failed),
                    getString(R.string.enter_password_valid));
            return false;
        }else if (mAddress.getText().toString().trim().isEmpty()
                && mAddress.getText().toString().trim().length() < 1
                && mAddress.getText().toString().trim().equalsIgnoreCase("")) {
            DialogManager.showMsgPopup(this,
                    getString(R.string.registration_failed),
                    getString(R.string.enter_address));
            return false;
        }

        else if (mCity.getText().toString().trim().isEmpty()
                && mCity.getText().toString().trim().length() < 1
                && mCity.getText().toString().trim().equalsIgnoreCase("")) {
            DialogManager.showMsgPopup(this,
                    getString(R.string.registration_failed),
                    getString(R.string.enter_city));
            return false;
        }  else if (mPincode.getText().toString().trim().isEmpty()
                && mPincode.getText().toString().trim().length() < 1
                && mPincode.getText().toString().trim().equalsIgnoreCase("")) {
            DialogManager.showMsgPopup(this,
                    getString(R.string.registration_failed),
                    getString(R.string.enter_pincode));
            return false;
        }

        return true;
    }

    private void showCurrentLocation() {
        GPSTracker tracker = new GPSTracker(this);
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
                    String[] addresses = response.getResults().get(0)
                            .getFormatted_address().toString().split(",");
                    if (addresses.length >= 4) {
                        String add_txt = response
                                .getResults()
                                .get(0)
                                .getFormatted_address()
                                .replace(
                                        (addresses[(addresses.length - 3)] + ","),
                                        "")
                                .replace(
                                        (addresses[(addresses.length - 2)] + ","),
                                        "")
                                .replace(addresses[(addresses.length - 1)], "");
                        String city = addresses[(addresses.length - 3)].trim();
                        String pincode = addresses[(addresses.length - 2)]
                                .replaceAll("\\D", "");
                        mAddress.setText(add_txt.trim());
                        mCity.setText(city.trim());
                        mPincode.setText(pincode.trim());
                    }
                } else {
                    DialogManager.showMsgPopup(SignupScreen.this,getString(R.string.app_name),
                            getString(R.string.unable_to_find_location));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        // TODO Auto-generated method stub
        super.onRequestSuccess(responseObj);
        CommonResponse resultObj = (CommonResponse) responseObj;

        if(resultObj.getStatus().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
            GlobalMethods.storeValuetoPreference(SignupScreen.this, GlobalMethods.STRING_PREFERENCE, AppConstants.PASSWORD, m_strPassword);
            GlobalMethods.UpdateUserDetails(SignupScreen.this,
                    resultObj.getResult());
            String communication_address = resultObj.getResult()
                    .getAddress()
                    + ", "
                    + resultObj.getResult().getArea()
                    + ", "
                    + resultObj.getResult().getCity()
                    + ", "
                    + resultObj.getResult().getPincode();

            GlobalMethods.storeValuetoPreference(
                    SignupScreen.this,
                    GlobalMethods.STRING_PREFERENCE,
                    AppConstants.COMMUNICATION_ADDRESS,
                    communication_address);
            launchScreen(OneTimePassword.class);
        }else{
            DialogManager.showMsgPopup(SignupScreen.this,"",resultObj.getMsg());
        }
        DialogManager.hideProgress();
    }


}
