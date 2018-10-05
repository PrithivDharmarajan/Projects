package com.bridgellc.bridgeqr.apiinterface;

import com.bridgellc.bridgeqr.model.CommonResponse;
import com.bridgellc.bridgeqr.model.SignInResponse;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

interface APICommonInterface {

    @FormUrlEncoded
    @POST("/api_bridge_qr_scanner_login")
    void getSignInResponse(@Field("email_id") String email_id,
                           @Field("password") String password,
                           @Field("login_type") String login_type,
                           @Field("social_id") String social_id,
                           Callback<SignInResponse> callback);
    @FormUrlEncoded
    @POST("/api_bridge_check_qrcode")
    void getQRReaderResponse(@Field("user_id") String user_id,
                           @Field("qr_code") String qr_code,
                           Callback<CommonResponse> callback);
}
