package com.smaat.ipharma.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.ipharma.R;
import com.smaat.ipharma.apiinterface.APIRequestHandler;
import com.smaat.ipharma.entity.GoogleApiEntity;
import com.smaat.ipharma.entity.Register;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.model.UpdateResponse;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.util.AppConstants;
import com.smaat.ipharma.util.DialogManager;
import com.smaat.ipharma.util.DialogMangerCallback;
import com.smaat.ipharma.util.GPSTracker;
import com.smaat.ipharma.util.GlobalMethods;

import org.json.JSONObject;

import retrofit.RetrofitError;

public class ProfileFragment extends BaseFragment implements OnClickListener,
        DialogMangerCallback {
    TextView mHeader;
    private LinearLayout mBottom;
    @SuppressWarnings("unused")
    private EditText mName, mEmail, mPhone, mPass, mLocation, mPincode,
            mAddress, mCity, mArea;
    private Button mRegister, mlocationCheck;
    private RelativeLayout mCityLayout, mAreaLayout, mHeader_lay;
    private boolean isCheck = false;
    Register mProfile;
    String lat_txt = "";
    String long_txt = "";
    private double latitude, longitude;
    String mUserId, mUserName, mEmailId, mPhoneTxt, mAddressTxt, mCityTxt,
            mAreaTxt, mPinCode, mTempPass, mPassword;
    protected Object mRegId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.activity_registration,
                container, false);
        setupUI(rootview);
        Window window = getActivity().getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        return rootview;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup mViewGroup = (ViewGroup) view
                .findViewById(R.id.parent_layout);
        setupUI(mViewGroup);
        hideSoftKeyboard(getActivity());

        AppConstants.FRAG = AppConstants.MAP_SCREEN;
        initComponents(view);
    }

    private void initComponents(View view) {

        mHeader = (TextView) view.findViewById(R.id.header_text);
        mBottom = (LinearLayout) view.findViewById(R.id.bottom_layout);
        mHeader.setVisibility(View.GONE);
        mBottom.setVisibility(View.GONE);
        mName = (EditText) view.findViewById(R.id.name);
        mEmail = (EditText) view.findViewById(R.id.email);
        mPhone = (EditText) view.findViewById(R.id.phone);
        mPass = (EditText) view.findViewById(R.id.pass);
        mHeader_lay = (RelativeLayout) view.findViewById(R.id.header_lay);
        mHeader_lay.setVisibility(View.GONE);

        mLocation = (EditText) view.findViewById(R.id.current_location);
        mCity = (EditText) view.findViewById(R.id.city);
        mArea = (EditText) view.findViewById(R.id.area);
        mPincode = (EditText) view.findViewById(R.id.pincoe);
        mAddress = (EditText) view.findViewById(R.id.address);
        mRegister = (Button) view.findViewById(R.id.register);
        mRegister.setText(R.string.save);
        mRegister.setOnClickListener(this);
        mAreaLayout = (RelativeLayout) view.findViewById(R.id.area_layout);
        mAreaLayout.setOnClickListener(this);
        mCityLayout = (RelativeLayout) view.findViewById(R.id.city_layout);
        mCityLayout.setOnClickListener(this);
        mlocationCheck = (Button) view.findViewById(R.id.location_check);
        mlocationCheck.setOnClickListener(this);
        HomeScreen.mHeaderLeftLay.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                HomeScreen.onBackMove(getActivity());
            }
        });
        setData();
    }

    public void setData() {
        mName.setText((String) GlobalMethods.getValueFromPreference(
                getActivity(), GlobalMethods.STRING_PREFERENCE,
                AppConstants.USER_NAME));
        mEmail.setText((String) GlobalMethods.getValueFromPreference(
                getActivity(), GlobalMethods.STRING_PREFERENCE,
                AppConstants.USER_EMAIL_ID));
        mPhone.setText((String) GlobalMethods.getValueFromPreference(
                getActivity(), GlobalMethods.STRING_PREFERENCE,
                AppConstants.USER_PHONE_NUM));
        mCity.setText((String) GlobalMethods.getValueFromPreference(
                getActivity(), GlobalMethods.STRING_PREFERENCE,
                AppConstants.USER_CITY));
        mArea.setText((String) GlobalMethods.getValueFromPreference(
                getActivity(), GlobalMethods.STRING_PREFERENCE,
                AppConstants.USER_AREA));
        mPincode.setText((String) GlobalMethods.getValueFromPreference(
                getActivity(), GlobalMethods.STRING_PREFERENCE,
                AppConstants.USER_AREA_PINCODE));
        mAddress.setText((String) GlobalMethods.getValueFromPreference(
                getActivity(), GlobalMethods.STRING_PREFERENCE,
                AppConstants.USER_ADDRESS));
    }

    private boolean ValidRegister() {
        if (mName.getText().toString().trim().isEmpty()
                && mName.getText().toString().trim().length() < 1
                && mName.getText().toString().trim().equalsIgnoreCase("")) {
            DialogManager.showCustomAlertDialog(getActivity(), this,
                    getString(R.string.enter_name));
            return false;
        } else if (!GlobalMethods.isEmailValid(mEmail.getText().toString()
                .trim())) {
            DialogManager.showCustomAlertDialog(getActivity(), this,
                    getString(R.string.enter_email));
            return false;
        } else if (mPhone.getText().toString().trim().isEmpty()
                && mPhone.getText().toString().trim().length() < 1
                && mPhone.getText().toString().trim().equalsIgnoreCase("")) {
            DialogManager.showCustomAlertDialog(getActivity(), this,
                    getString(R.string.enter_phone));
            return false;
        } else if (mCity.getText().toString().trim().isEmpty()
                && mCity.getText().toString().trim().length() < 1
                && mCity.getText().toString().trim().equalsIgnoreCase("")) {
            DialogManager.showCustomAlertDialog(getActivity(), this,
                    getString(R.string.enter_city));
            return false;
        } else if (mArea.getText().toString().trim().isEmpty()
                && mArea.getText().toString().trim().length() < 1
                && mArea.getText().toString().trim().equalsIgnoreCase("")) {
            DialogManager.showCustomAlertDialog(getActivity(), this,
                    getString(R.string.enter_area));
            return false;
        } else if (mPincode.getText().toString().trim().isEmpty()
                && mPincode.getText().toString().trim().length() < 1
                && mPincode.getText().toString().trim().equalsIgnoreCase("")) {
            DialogManager.showCustomAlertDialog(getActivity(), this,
                    getString(R.string.enter_pincode));
            return false;
        } else if (mAddress.getText().toString().trim().isEmpty()
                && mAddress.getText().toString().trim().length() < 1
                && mAddress.getText().toString().trim().equalsIgnoreCase("")) {
            DialogManager.showCustomAlertDialog(getActivity(), this,
                    getString(R.string.enter_address));
            return false;
        }

        return true;
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
    }

//	@Override
//	public void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//		getActivity().getSupportFragmentManager().beginTransaction()
//				.remove(this).commit();
//	}

    public void onItemclick(String SelctedItem, int pos) {

    }

    public void onOkclick() {

    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.register:
                if (ValidRegister()) {
                    callRegisterService();
                }

                break;
            case R.id.city_layout:
                // Shows City Dialog
                break;
            case R.id.area_layout:
                // Shows Area Dialog
                break;
            case R.id.location_check:
                if (!isCheck) {
                    mlocationCheck
                            .setBackgroundResource(R.drawable.register_till_fill);
                    showCurrentLocation();
                    isCheck = true;
                } else {
                    mCity.setText("");
                    mArea.setText("");
                    mPincode.setText("");
                    mAddress.setText("");
                    mlocationCheck
                            .setBackgroundResource(R.drawable.regsiter_tick_empty);
                    isCheck = false;
                }
                break;
            default:
                break;
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
        aq().ajax(url, JSONObject.class, this,
                getString(R.string.addresslocation));
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
                        String city = addresses[(addresses.length - 3)];
                        String pincode = addresses[(addresses.length - 2)]
                                .replaceAll("\\D", "");
                        mArea.setText(addresses[(addresses.length - 4)]);
                        mAddress.setText(add_txt);
                        mCity.setText(city);
                        mPincode.setText(pincode);
                    }
                } else {
                    DialogManager.showCustomAlertDialog(getActivity(), this,
                            getString(R.string.unable_to_create_maps));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callRegisterService() {
        lat_txt = "";
        long_txt = "";
        mUserId = ((String) GlobalMethods.getUserID(getActivity()));
        mUserName = mName.getText().toString().trim();
        mEmailId = mEmail.getText().toString().trim();

        mTempPass = mPass.getText().toString().trim();
        if (mTempPass.equalsIgnoreCase("") || mTempPass.isEmpty()) {
            mPassword = ((String) GlobalMethods.getValueFromPreference(
                    getActivity(), GlobalMethods.STRING_PREFERENCE,
                    AppConstants.USER_PASSWORD));
        } else {
            mPassword = mTempPass;

        }
        mPhoneTxt = mPhone.getText().toString().trim();
        mAddressTxt = mAddress.getText().toString().trim();
        mCityTxt = mCity.getText().toString().trim();
        mAreaTxt = mArea.getText().toString().trim();
        mPinCode = mPincode.getText().toString().trim();

        String location_address = mAddressTxt + "," + mAreaTxt + "," + mPinCode;
        if (isCheck) {
            lat_txt = latitude + "";
            long_txt = longitude + "";
            callRegistrationAPI();
        } else if (!isCheck) {
            generateLatLong(location_address);
        }

    }

    private void generateLatLong(String address) {
        String add_ress = address.replace(" ", "%20");
        String url = AppConstants.GOOGLE_MAP_LINK + add_ress + "&sensor=true";
        aq().ajax(url, JSONObject.class, this, getString(R.string.latlocation));
    }

    public void latlocation(String url, JSONObject json, AjaxStatus status) {
        if (json != null) {
            try {
                GoogleApiEntity obj = new Gson().fromJson(json.toString(),
                        GoogleApiEntity.class);
                String latitude = obj.getResults().get(0).getGeometry()
                        .getLocation().getLat();
                String longitude = obj.getResults().get(0).getGeometry()
                        .getLocation().getLat();
                this.latitude = Double.valueOf(latitude);
                this.longitude = Double.valueOf(longitude);
                callRegistrationAPI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void callRegistrationAPI() {

        APIRequestHandler.getInstance().getupdateprofile(mUserId, mUserName,
                mEmailId, mPhoneTxt, mAddressTxt, mCityTxt, mAreaTxt, mPinCode,
                mPassword, this);

    }

    @Override
    public void onRequestSuccess(Object responseObj) {

        if (responseObj instanceof UpdateResponse) {

            DialogManager.hideProgress(getActivity());
            UpdateResponse mUpdateResponse = (UpdateResponse) responseObj;

            if (mUpdateResponse.getStatus().equalsIgnoreCase(
                    AppConstants.SUCCESS_CODE)) {
                String communication_address = mUpdateResponse.getResult()
                        .getAddress()
                        + ", "
                        + mUpdateResponse.getResult().getArea()
                        + ", "
                        + mUpdateResponse.getResult().getCity()
                        + ", "
                        + mUpdateResponse.getResult().getPicode();

                GlobalMethods.storeValuetoPreference(getActivity(),
                        GlobalMethods.STRING_PREFERENCE,
                        AppConstants.communication_address,
                        communication_address);
                GlobalMethods.storeValuetoPreference(getActivity(),
                        GlobalMethods.STRING_PREFERENCE, AppConstants.USER_ID,
                        mUpdateResponse.getResult().getUser_id());
                GlobalMethods.storeValuetoPreference(getActivity(),
                        GlobalMethods.STRING_PREFERENCE,
                        AppConstants.USER_NAME, mUpdateResponse.getResult()
                                .getFull_name());
                GlobalMethods.storeValuetoPreference(getActivity(),
                        GlobalMethods.STRING_PREFERENCE,
                        AppConstants.USER_EMAIL_ID, mUpdateResponse.getResult()
                                .getUser_email());
                GlobalMethods.storeValuetoPreference(getActivity(),
                        GlobalMethods.STRING_PREFERENCE,
                        AppConstants.USER_PASSWORD, mPassword);
                GlobalMethods.storeValuetoPreference(getActivity(),
                        GlobalMethods.STRING_PREFERENCE,
                        AppConstants.USER_PHONE_NUM, mUpdateResponse
                                .getResult().getUser_phone());
                GlobalMethods.storeValuetoPreference(getActivity(),
                        GlobalMethods.STRING_PREFERENCE,
                        AppConstants.USER_CITY, mUpdateResponse.getResult()
                                .getCity());
                GlobalMethods.storeValuetoPreference(getActivity(),
                        GlobalMethods.STRING_PREFERENCE,
                        AppConstants.USER_AREA, mUpdateResponse.getResult()
                                .getArea());
                GlobalMethods.storeValuetoPreference(getActivity(),
                        GlobalMethods.STRING_PREFERENCE,
                        AppConstants.USER_AREA_PINCODE, mUpdateResponse
                                .getResult().getPicode());
                GlobalMethods.storeValuetoPreference(getActivity(),
                        GlobalMethods.STRING_PREFERENCE,
                        AppConstants.USER_ADDRESS, mUpdateResponse.getResult()
                                .getAddress());
                Intent in = new Intent(getActivity(), HomeScreen.class);
                startActivity(in);
            } else {
                DialogManager.showCustomAlertDialog(getActivity(),
                        ProfileFragment.this, mUpdateResponse.getMsg());
            }

        }
        super.onRequestSuccess(responseObj);
    }

    @Override
    public void onRequestFailure(RetrofitError errorCode) {

        super.onRequestFailure(errorCode);
    }

}
