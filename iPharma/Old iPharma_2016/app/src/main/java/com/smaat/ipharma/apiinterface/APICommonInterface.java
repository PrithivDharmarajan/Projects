package com.smaat.ipharma.apiinterface;

import com.smaat.ipharma.entity.AdsEntity;
import com.smaat.ipharma.entity.ChatCommonEntity;
import com.smaat.ipharma.entity.CommonResponse;
import com.smaat.ipharma.entity.FavouriteCommonResponse;
import com.smaat.ipharma.entity.ForgotResponse;
import com.smaat.ipharma.entity.NewOrderEntity;
import com.smaat.ipharma.entity.OrderHistoryCommonResponseEntity;
import com.smaat.ipharma.entity.OtpEntity;
import com.smaat.ipharma.entity.RateResponseEntity;
import com.smaat.ipharma.entity.ShowReviewResponse;
import com.smaat.ipharma.entity.TermsConditionsEntity;
import com.smaat.ipharma.entity.WriteReviewEntity;
import com.smaat.ipharma.model.MapResponse;
import com.smaat.ipharma.model.UpdateResponse;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;

public interface APICommonInterface {
	@FormUrlEncoded
	@POST("/index.php/user/registration")
	public void getRegistration(@Field("FullName") String username,
			@Field("Email") String email, @Field("Phone") String password,
			@Field("Password") String regtype,
			@Field("Address") String profilepic, @Field("City") String city,
			@Field("Area") String area, @Field("Pincode") String pincode,
			@Field("Latitude") String lat_txt,
			@Field("Longitude") String long_txt,
			@Field("DeviceType") String devicetype,
			@Field("DeviceUniqueID") String deviceid,
			Callback<CommonResponse> callback);

	@FormUrlEncoded
	@POST("/index.php/user/login")
	public void getLogin(@Field("Email") String username,
			@Field("Phone") String email, @Field("Password") String password,
			@Field("DeviceType") String devicetype,
			@Field("DeviceUniqueID") String deviceid,
			Callback<CommonResponse> callback);

	@FormUrlEncoded
	@POST("/index.php/pharmacy/searchpharmacy")
	public void getSearchPharmacies(@Field("UserID") String mUserID,
			@Field("Latitude") String mLatitude,
			@Field("Longitude") String mLongitude,
			@Field("SearchText") String mSearchKey,
			@Field("Distance") float mDistance, Callback<MapResponse> callback);

	@FormUrlEncoded
	@POST("/index.php/user/updateprofile")
	public void getupdateprofile(@Field("UserID") String mUserID,
			@Field("FullName") String mUserName,
			@Field("UserEmail") String mEmailId,
			@Field("UserPhone") String mPhoneTxt,
			@Field("Address") String mAddressTxt,
			@Field("City") String mCityTxt, @Field("Area") String mAreaTxt,
			@Field("Pincode") String mPinCode,
			@Field("Password") String mPassword,
			Callback<UpdateResponse> callback);

	@FormUrlEncoded
	@POST("/index.php/pharmacy/getads")
	public void getAds(@Field("Latitude") String mLatitude,
			@Field("Longitude") String mLongitude, Callback<AdsEntity> callback);

	@FormUrlEncoded
	@POST("/index.php/pharmacy/getfavshops")
	public void getFavouriteShops(@Field("UserID") String mUserID,
			@Field("Latitude") String mLatitude,
			@Field("Longitude") String mLongitude,
			Callback<FavouriteCommonResponse> callback);

	@FormUrlEncoded
	@POST("/index.php/pharmacy/submitreview")
	public void writeReview(@Field("ReviewComment") String mReview,
			@Field("UserID") String mUserID, @Field("ShopID") String mShopID,
			Callback<WriteReviewEntity> callback);

	@FormUrlEncoded
	@POST("/index.php/pharmacy/shopreviewlist")
	public void showReview(@Field("ShopID") String mShopID,
			Callback<ShowReviewResponse> callback);

	@FormUrlEncoded
	@POST("/index.php/pharmacy/addremovefav")
	public void addFavourite(@Field("ShopID") String mShopID,
			@Field("UserID") String mUserID,
			@Field("FavStatus") String mFavStatus,
			Callback<WriteReviewEntity> callback);

	@FormUrlEncoded
	@POST("/index.php/pharmacy/submitrating")
	public void shopRating(@Field("ShopID") String mShopID,
			@Field("UserID") String UserID, @Field("Rating") String mRating,
			Callback<RateResponseEntity> callback);

	@Multipart
	@POST("/index.php/pharmacy/neworder")
	public void newOrder(@Part("ShopID") String mShopID,
			@Part("UserID") String mUserID,
			@Part("DeliveryAddress") String mDeliveryAddress,
			@Part("NewAddress") String mNewAddress,
			@Part("OrderNote") String mOrderNote,
			@Part("PrescriptionFile") TypedFile mPrecImage,
			Callback<NewOrderEntity> callback);

	@FormUrlEncoded
	@POST("/index.php/pharmacy/reorder")
	public void reOrder(@Field("ShopID") String mShopID,
			@Field("UserID") String mUserID,
			@Field("DeliveryAddress") String mDeliveryAddress,
			@Field("NewAddress") String mNewAddress,
			@Field("PrescriptionID") String mPrecID,
			@Field("OrderNote") String mOrderNote,
			Callback<NewOrderEntity> callback);

	@FormUrlEncoded
	@POST("/index.php/user/getorderhistory")
	public void getMyOrders(@Field("UserID") String mUserID,
			Callback<OrderHistoryCommonResponseEntity> callback);

	@FormUrlEncoded
	@POST("/index.php/pharmacy/getoffers")
	public void getOffers(@Field("UserID") String mUserID,
			@Field("Latitude") String mLatitude,
			@Field("Longitude") String mLongitude,
			@Field("SearchText") String mSearchKey,
			@Field("Distance") float mDistance, Callback<MapResponse> callback);

	@FormUrlEncoded
	@POST("/index.php/user/getdisclaimer")
	public void getTermsandConditions(@Field("UserID") String mUserID,
			Callback<TermsConditionsEntity> callback);

	@FormUrlEncoded
	@POST("/index.php/user/otpconfirmation")
	public void getOtpconfirmation(@Field("UserID") String mUserID,
			@Field("OTPKey") String mOt, Callback<OtpEntity> callback);

	@FormUrlEncoded
	@POST("/index.php/user/resendotp")
	public void getresendotpconfirmation(@Field("UserID") String mUserID,
			Callback<OtpEntity> callback);

	@FormUrlEncoded
	@POST("/index.php/messages/getmessages")
	public void getChathistory(@Field("UserID") String mUserID,
			Callback<ChatCommonEntity> callback);

	@FormUrlEncoded
	@POST("/index.php/messages/sendmessage")
	public void getSendChat(@Field("UserID") String mUserID,
			@Field("Message") String mMessage,
			Callback<ChatCommonEntity> callback);

	@FormUrlEncoded
	@POST("/index.php/user/forgotpassword")
	public void getForgotPasasword(@Field("UserName") String mUserName,
			Callback<ForgotResponse> callback);

	@FormUrlEncoded
	@POST("/index.php/user/orderpaymentupdate")
	public void getOrderPaymentUpdate(@Field("UserID") String UserID,
			@Field("OrderID") String OrderID,
			@Field("PaymentAccept") String PaymentAccept,
			@Field("PaymentMode") String PaymentMode,
			Callback<OtpEntity> callback);

}
