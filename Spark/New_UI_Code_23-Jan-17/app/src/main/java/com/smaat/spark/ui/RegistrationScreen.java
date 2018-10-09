package com.smaat.spark.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.smaat.spark.R;
import com.smaat.spark.entity.inputEntity.LoginRegResetInputEntity;
import com.smaat.spark.entity.inputEntity.NotificationInputEntity;
import com.smaat.spark.entity.outputEntity.UserDetailsEntity;
import com.smaat.spark.main.BaseActivity;
import com.smaat.spark.model.AddressResponse;
import com.smaat.spark.model.CommonResponse;
import com.smaat.spark.model.UserDetailsResponse;
import com.smaat.spark.services.APIRequestHandler;
import com.smaat.spark.utils.AppConstants;
import com.smaat.spark.utils.DialogManager;
import com.smaat.spark.utils.GlobalMethods;
import com.smaat.spark.utils.InterfaceBtnCallback;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RegistrationScreen extends BaseActivity implements InterfaceBtnCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.parent_lay)
    ViewGroup mRegViewGrp;

    @BindView(R.id.email_edt)
    EditText mEmailEdt;

    @BindView(R.id.user_name_edt)
    EditText mUserNameEdt;

    @BindView(R.id.pwd_edt)
    EditText mPwdEdt;

    @BindView(R.id.confirm_pwd_edt)
    EditText mConfirmPwdEdt;

    private GoogleApiClient mGoogleApiClient;
    private String mAddressStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_registration_screen);
        ButterKnife.bind(this);
        setupUI(mRegViewGrp);
    }


    @Override
    protected void onResume() {
        super.onResume();
        initGoogleAPIClient();
        initView();
    }

    private void initView() {

        mUserNameEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    String mUserNameStr = mUserNameEdt.getText().toString().trim();
                    if (mUserNameStr.isEmpty()) {
                        shakeAnimEdt(mUserNameEdt, getString(R.string.enter_name));
                    } else {
                        NotificationInputEntity userName = new NotificationInputEntity(AppConstants.API_CHECK_USER_NAME, AppConstants.PARAMS_CHECK_USER_NAME, mUserNameStr);
                        APIRequestHandler.getInstance().callUserNameAPI(userName, RegistrationScreen.this);
                        mPwdEdt.requestFocus();

                    }
                }
                return true;
            }
        });
    }

    @OnClick({R.id.header_left_btn_lay, R.id.create_acc_btn, R.id.term_cond_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.create_acc_btn:
                validateFields();
                break;
            case R.id.term_cond_lay:
                AppConstants.SETTINGS_WEB_TITLE = getResources().getString(R.string.terms_service);
                nextScreen(WebScreen.class, false);
                break;
        }

    }

    private void validateFields() {
        String emailStr = mEmailEdt.getText().toString().trim();
        String userNameStr = mUserNameEdt.getText().toString().trim();
        String pwdStr = mPwdEdt.getText().toString().trim();
        String confirmPwdStr = mConfirmPwdEdt.getText().toString().trim();

        if (emailStr.isEmpty() || (!GlobalMethods.isEmailValid(emailStr))) {
            shakeAnimEdt(mEmailEdt, getString(R.string.enter_email));
        } else if (userNameStr.isEmpty()) {
            shakeAnimEdt(mUserNameEdt, getString(R.string.enter_name));
        } else if (pwdStr.isEmpty()) {
            shakeAnimEdt(mPwdEdt, getString(R.string.enter_pwd));
        } else if (pwdStr.length() < 8) {
            shakeAnimEdt(mPwdEdt, getString(R.string.pwd_length_valid));
        } else if (confirmPwdStr.isEmpty()) {
            shakeAnimEdt(mConfirmPwdEdt, getString(R.string.enter_confirm_pwd));
        } else if (!pwdStr.equals(confirmPwdStr)) {
            shakeAnimEdt(mConfirmPwdEdt, getString(R.string.enter_diff_pwd_confirm));
        } else {
            //Register User API Call
            LoginRegResetInputEntity loginRegResetInputEntity = new LoginRegResetInputEntity(AppConstants.API_REGISTRATION, AppConstants.PARAMS_REGISTRATION,
                    emailStr, pwdStr, userNameStr, "", "", mAddressStr.trim(), AppConstants.SUCCESS_CODE, GlobalMethods.getStringValue(this, AppConstants.DEVICE_ID));
            APIRequestHandler.getInstance().callSignInAPI(loginRegResetInputEntity, this);
        }


    }

    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof UserDetailsResponse) {

            UserDetailsResponse userRes = (UserDetailsResponse) resObj;
            if (userRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                ArrayList<UserDetailsEntity> userDetails = userRes.getResult();
                userDetails.get(0).setUsername(mUserNameEdt.getText().toString().trim());
                userDetails.get(0).setEmail_id(mEmailEdt.getText().toString().trim());
                userDetails.get(0).setPassword(mPwdEdt.getText().toString().trim());
                GlobalMethods.storeUserDetails(this, userDetails.get(0));
                nextScreen(HomeScreen.class, true);

            } else {
                DialogManager.getInstance().showAlertPopup(this, userRes.getMessage());
            }
        }
        if (resObj instanceof AddressResponse) {
            AddressResponse userAddressRes = (AddressResponse) resObj;

            String addStrArr[] = userAddressRes.getResults().get(0).getFormatted_address().split(",");
            String cityStr = addStrArr[addStrArr.length - 1], areaStr = addStrArr[addStrArr.length - 2];
            if (addStrArr.length > 4) {
                cityStr = addStrArr[addStrArr.length - 3];
                areaStr = addStrArr[addStrArr.length - 4];
            } else if (addStrArr.length > 3) {
                cityStr = addStrArr[addStrArr.length - 2];
                areaStr = addStrArr[addStrArr.length - 3];
            }

            mAddressStr = areaStr + ", " + cityStr;
        }

        if (resObj instanceof CommonResponse) {
            CommonResponse userRes = (CommonResponse) resObj;
            if (userRes.getResponse_code().equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
                shakeAnimEdt(mUserNameEdt, userRes.getMessage());

            }
        }
    }

    @Override
    public void onRequestFailure(Throwable t) {
        super.onRequestFailure(t);

    }

    private void initGoogleAPIClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();

    }

    private String getCurrentLatLong() {

        String latLongStr = "";

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            DialogManager.showToast(this, getString(R.string.go_settings_per));

        } else {
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                latLongStr = mLastLocation.getLatitude() + "," + mLastLocation.getLongitude();
            }
        }
        return latLongStr;
    }


    @Override
    public void onBackPressed() {
        previousScreen(LoginScreen.class, true);
    }

    @Override
    public void onYesClick() {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        String addressURLStr = String.format(AppConstants.GET_ADDRESS_URL, getCurrentLatLong());
        APIRequestHandler.getInstance().callGetUserAddressAPI(addressURLStr, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        DialogManager.showToast(this, connectionResult.getErrorMessage());
    }
}
