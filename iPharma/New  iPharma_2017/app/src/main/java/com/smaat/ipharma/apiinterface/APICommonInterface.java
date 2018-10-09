package com.smaat.ipharma.apiinterface;




import android.widget.RatingBar;

import com.smaat.ipharma.entity.AlarmObject;
import com.smaat.ipharma.entity.CommonResponse;
import com.smaat.ipharma.entity.ForgotResponse;
import com.smaat.ipharma.entity.LoginInputEntity;
import com.smaat.ipharma.entity.MapResponse;
import com.smaat.ipharma.entity.NewOrderEntity;
import com.smaat.ipharma.entity.OneTouchResponse;
import com.smaat.ipharma.entity.OrderHistoryCommonResponseEntity;
import com.smaat.ipharma.entity.OtpEntity;
import com.smaat.ipharma.entity.RateResponseEntity;
import com.smaat.ipharma.entity.RecentOrderResponse;
import com.smaat.ipharma.entity.ShowReviewResponse;
import com.smaat.ipharma.entity.SignInEntity;
import com.smaat.ipharma.entity.WriteReviewEntity;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

interface APICommonInterface {

    //@FormUrlEncoded/*
    /*@POST("customer/server_interface")
    Call<SignInEntity> signinAPICall(@Body LoginInputEntity ln);*/

    @FormUrlEncoded
    @POST("user/login")
    Call<CommonResponse> signinAPICall(@Field("Email") String email,
                                       @Field("Phone") String phone, @Field("Password") String password,
                                       @Field("DeviceType") String devicetype,
                                       @Field("DeviceUniqueID") String deviceid);

    @FormUrlEncoded
    @POST("user/registration")
    Call<CommonResponse> signupAPICall(@Field("FullName") String username,
                                       @Field("Email") String email, @Field("Phone") String password,
                                       @Field("Password") String regtype,
                                       @Field("Address") String profilepic, @Field("City") String city,
                                       @Field("Area") String area, @Field("Pincode") String pincode,
                                       @Field("Latitude") String lat_txt,
                                       @Field("Longitude") String long_txt,
                                       @Field("DeviceType") String devicetype,
                                       @Field("DeviceUniqueID") String deviceid);


    @FormUrlEncoded
    @POST("user/otpconfirmation")
    Call<OtpEntity> otpConfirmation(@Field("UserID") String userid,
                                    @Field("OTPKey") String otpkey);

    @FormUrlEncoded
    @POST("user/resendotp")
    Call<OtpEntity> resendOtp(@Field("UserID") String userid);


    @FormUrlEncoded
    @POST("user/forgotpassword")
    Call<ForgotResponse> forgotpassword(@Field("UserName") String mEmailid);


    @FormUrlEncoded
    @POST("pharmacy/searchpharmacy")
    Call<MapResponse> getPharmacyList(@Field("UserID") String mUserID,
                                      @Field("Latitude") String mLatitude,
                                      @Field("Longitude") String mLongitude,
                                      @Field("SearchText") String mSearchKey,
                                      @Field("Distance") float mDistance);

    @FormUrlEncoded
    @POST("pharmacy/getfavshops")
    Call<MapResponse> getFavList(@Field("UserID") String mUserID,
                                 @Field("Latitude") String mLatitude,
                                 @Field("Longitude") String mLongitude);


    @FormUrlEncoded
    @POST("pharmacy/addremovefav")
    Call<WriteReviewEntity> addFavourite(@Field("ShopID") String mShopID,
                                         @Field("UserID") String mUserID,
                                         @Field("FavStatus") String mFavStatus);

    @FormUrlEncoded
    @POST("pharmacy/submitrating")
    Call<RateResponseEntity> shopRating(@Field("ShopID") String mShopID,
                                        @Field("UserID") String UserID, @Field("Rating") String mRating);

    @Multipart
    @POST("pharmacy/neworder")
    Call<NewOrderEntity> newOrder(@Part MultipartBody.Part mShopID,
                                  @Part MultipartBody.Part mUserID,
                                  @Part MultipartBody.Part mDeliveryAddress,
                                  @Part MultipartBody.Part mNewAddress,
                                  @Part MultipartBody.Part mOrderNote,
                                  @Part MultipartBody.Part filePart);

    @FormUrlEncoded
    @POST("onetouch")
    Call<OneTouchResponse> onetouch(@Field("UserID") String UserID,
                                    @Field("ShopID") String mShopID);


    @FormUrlEncoded
    @POST("user/getorderhistory")
    Call<OrderHistoryCommonResponseEntity> getMyOrders(@Field("UserID") String mUserID);

    @FormUrlEncoded
    @POST("user/updateprofile")
    Call<CommonResponse> getupdateprofile(@Field("UserID") String mUserID,
                                          @Field("FullName") String mUserName,
                                          @Field("UserEmail") String mEmailId,
                                          @Field("UserPhone") String mPhoneTxt,
                                          @Field("Password") String mPassword,
                                          @Field("Address") String mAddressTxt,
                                          @Field("City") String mCityTxt,
                                          @Field("Area") String mAreaTxt,
                                          @Field("Pincode") String mPinCode);

    @FormUrlEncoded
    @POST("pharmacy/shopreviewlist")
    Call<ShowReviewResponse> showReview(@Field("ShopID") String mShopID);

    @FormUrlEncoded
    @POST("user/smstouser")
    Call<AlarmObject> sendsms(@Field("UserID") String mUserid, @Field("PhoneNumber") String mPhoneNumber, @Field("Message") String mMessage);

    @FormUrlEncoded
    @POST("user/orderpaymentupdate")
    Call<OtpEntity> getOrderPaymentUpdate(@Field("UserID") String UserID,
                                      @Field("OrderID") String OrderID,
                                      @Field("PaymentAccept") String PaymentAccept,
                                      @Field("PaymentMode") String PaymentMode);


    @FormUrlEncoded
    @POST("pharmacy/submitreview")
    Call<WriteReviewEntity> write_review(@Field("ReviewComment") String mReview,
                                   @Field("UserID") String mUserID, @Field("ShopID") String mShopID, @Field("Rating") String mRating);
}
