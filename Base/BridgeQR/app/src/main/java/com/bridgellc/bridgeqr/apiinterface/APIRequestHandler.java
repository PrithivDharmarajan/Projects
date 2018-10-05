package com.bridgellc.bridgeqr.apiinterface;


import com.bridgellc.bridgeqr.main.BaseActivity;
import com.bridgellc.bridgeqr.model.CommonResponse;
import com.bridgellc.bridgeqr.model.SignInResponse;
import com.bridgellc.bridgeqr.utils.AppConstants;
import com.bridgellc.bridgeqr.utils.DialogManager;
import com.bridgellc.bridgeqr.utils.GlobalMethods;
import com.squareup.okhttp.OkHttpClient;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

public class APIRequestHandler {


    private static final APIRequestHandler mInstance = new APIRequestHandler();
    private RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(AppConstants.BASE_URL)
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .setClient(new OkClient(new OkHttpClient()))
            .build();
    private APICommonInterface mInterfaces = restAdapter
            .create(APICommonInterface.class);

    private APIRequestHandler() {

    }

    public static APIRequestHandler getInstance() {
        return mInstance;
    }

    public void getSignInResponse(String mEmailId, String mPassword, String mLogInType, String mSocialID, final
    BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getSignInResponse(mEmailId, mPassword,
                mLogInType, mSocialID, new Callback<SignInResponse>() {

                    @Override
                    public void success(SignInResponse mSignInResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mSignInResponse != null) {
                            Object obj = mSignInResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });

    }


    public void getQRReaderResponse(String mQRCode, final
    BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getQRReaderResponse(GlobalMethods.getStringValue(mActivity, AppConstants.USER_ID), mQRCode, new Callback<CommonResponse>() {

            @Override
            public void success(CommonResponse mQRResponse,
                                Response arg1) {
                DialogManager.hideProgress(mActivity);
                if (mQRResponse != null) {
                    Object obj = mQRResponse;
                    mActivity.onRequestSuccess(obj);
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                DialogManager.hideProgress(mActivity);
                mActivity.onRequestFailure(retrofitError);
            }
        });

    }


}


