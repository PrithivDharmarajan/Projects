package com.smaat.spark.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.smaat.spark.R;
import com.smaat.spark.entity.inputEntity.LoginRegResetInputEntity;
import com.smaat.spark.entity.outputEntity.UserDetailsEntity;
import com.smaat.spark.main.BaseActivity;
import com.smaat.spark.model.AddressResponse;
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


public class LoginScreen extends BaseActivity implements InterfaceBtnCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    @BindView(R.id.parent_lay)
    ViewGroup mLoginViewGrp;

    @BindView(R.id.email_edt)
    EditText mEmailEdt;

    @BindView(R.id.pwd_edt)
    EditText mPwdEdt;

    private GoogleApiClient mGoogleApiClient;
    private String mAddressStr = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_login_screen);
        ButterKnife.bind(this);
        setupUI(mLoginViewGrp);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initGoogleAPIClient();

    }

    @OnClick({R.id.forgot_pwd_txt, R.id.login_btn, R.id.reg_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forgot_pwd_txt:
                nextScreen(ResetPwdScreen.class, true);
                break;
            case R.id.login_btn:
                validateFields();
                break;
            case R.id.reg_btn:
                nextScreen(RegistrationScreen.class, true);
                break;

        }
    }

    private void validateFields() {
        String emailStr = mEmailEdt.getText().toString().trim();
        String pwdStr = mPwdEdt.getText().toString().trim();

        if (emailStr.isEmpty()) {
            shakeAnimEdt(mEmailEdt, getString(R.string.enter_email_name));

        } else if (pwdStr.isEmpty()) {
            shakeAnimEdt(mPwdEdt, getString(R.string.enter_pwd));
        } else {
            //Login API Call
            LoginRegResetInputEntity loginRegResetInputEntity = new LoginRegResetInputEntity(AppConstants.API_LOGIN, AppConstants.PARAMS_LOGIN, emailStr, pwdStr, "", "", mAddressStr.trim(), AppConstants.SUCCESS_CODE, GlobalMethods.getStringValue(this, AppConstants.DEVICE_ID));
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
    public void onYesClick() {

    }


    @Override
    public void onBackPressed() {
        finish();
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