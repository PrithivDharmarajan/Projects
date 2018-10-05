package com.smaat.jolt.apiinterface;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

import com.smaat.jolt.model.AddCreditCardResponse;
import com.smaat.jolt.model.ConversationResponse;
import com.smaat.jolt.model.FAQResponse;
import com.smaat.jolt.model.GlobalResponse;
import com.smaat.jolt.model.JoltPlansResponse;
import com.smaat.jolt.model.MessageDetailsResponse;
import com.smaat.jolt.model.MessageResponse;
import com.smaat.jolt.model.MyHistoryResponse;
import com.smaat.jolt.model.ProfileResponse;
import com.smaat.jolt.model.PromoCouponResponse;
import com.smaat.jolt.model.ShopListResponse;
import com.smaat.jolt.model.SignInResponse;
import com.smaat.jolt.model.TermsResponse;

public interface APICommonInterface {

	@FormUrlEncoded
	@POST("/login")
	public void getRegistration(@Field("EmailAddress") String email,
			@Field("LoginType") String logintype,
			@Field("FullName") String personName,
			@Field("DeviceType") String deviceType,
			@Field("DeviceToken") String deviceToken,
			@Field("ProfilePicture") String ProfilePicture,
			Callback<SignInResponse> callback);

	@FormUrlEncoded
	@POST("/GetShopList")
	public void getShopList(@Field("UserID") String userId,
			@Field("Latitude") String latitude,
			@Field("Longitude") String longtitude,
			@Field("Start") String start, @Field("Limit") String limit,
			Callback<ShopListResponse> callback);

	@FormUrlEncoded
	@POST("/GetMyProfile")
	public void getMyProfile(@Field("UserID") String userId,
			Callback<ProfileResponse> callback);

	@FormUrlEncoded
	@POST("/AddCreditCard")
	public void addUpdateCreditCard(@Field("UserID") String userId,
			@Field("CardNumber") String cardNumber,
			@Field("CardExpiryMonth") String expiryMonth,
			@Field("CardExpiryYear") String expiryYear,
			@Field("CardCVV") String cvv,
			Callback<AddCreditCardResponse> callback);

	@FormUrlEncoded
	@POST("/GetJoltPlans")
	public void getJoltPlans(@Field("UserID") String userId,
			Callback<JoltPlansResponse> callback);

	@FormUrlEncoded
	@POST("/HowWasIt")
	public void setHowWasIt(@Field("UserID") String userId,
			@Field("ShopID") String shopId, @Field("Comments") String comments,
			Callback<GlobalResponse> callback);

	@FormUrlEncoded
	@POST("/ValidateCoupon")
	public void validateCoupon(@Field("UserID") String userId,
			@Field("CouponCode") String couponCode,
			Callback<GlobalResponse> callback);

	@FormUrlEncoded
	@POST("/ValidateCoupon/promo")
	public void validatePromoCoupon(@Field("UserID") String userId,
			@Field("CouponCode") String couponCode,
			Callback<PromoCouponResponse> callback);

	@FormUrlEncoded
	@POST("/GetMessage")
	public void getConversationList(@Field("UserID") String userId,
			Callback<MessageResponse> callback);

	@FormUrlEncoded
	@POST("/Conversation")
	public void sendConversationMessage(@Field("UserID") String userId,
			@Field("MessageText") String messageText,
			@Field("ConversationID") String conversationId,
			Callback<ConversationResponse> callback);

	@FormUrlEncoded
	@POST("/GetMessage/Details")
	public void getMessageDetails(@Field("UserID") String userId,
			@Field("ConversationID") String conversationId,
			Callback<MessageDetailsResponse> callback);

	@FormUrlEncoded
	@POST("/GetMyHistory")
	public void getMyHistory(@Field("UserID") String userId,
			Callback<MyHistoryResponse> callback);

	@FormUrlEncoded
	@POST("/faq")
	public void getFaq(@Field("UserID") String userId,
			Callback<FAQResponse> callback);

	@FormUrlEncoded
	@POST("/terms")
	public void getTerms(@Field("UserID") String userId,
			Callback<TermsResponse> callback);

	@FormUrlEncoded
	@POST("/device")
	public void updateDeviceID(@Field("user_id") String mUserID,
			@Field("device_id") String mDeviceID,
			@Field("device") String mDevice, Callback<TermsResponse> callback);

	@FormUrlEncoded
	@POST("/login/verifyemail")
	public void verifyEmail(@Field("token") String mTokenID,
			Callback<SignInResponse> callback);
}
