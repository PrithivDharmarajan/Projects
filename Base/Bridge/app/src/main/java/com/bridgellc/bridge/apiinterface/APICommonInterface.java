package com.bridgellc.bridge.apiinterface;

import com.bridgellc.bridge.entity.CardResponse;
import com.bridgellc.bridge.model.AcceptBiddingResponse;
import com.bridgellc.bridge.model.BankAccDetailsResponse;
import com.bridgellc.bridge.model.BankAddAccountResponse;
import com.bridgellc.bridge.model.BiddingsReqResponse;
import com.bridgellc.bridge.model.BuyItemResponse;
import com.bridgellc.bridge.model.ChatResponse;
import com.bridgellc.bridge.model.CommonModelResponse;
import com.bridgellc.bridge.model.CommonPhResponse;
import com.bridgellc.bridge.model.CommonResponse;
import com.bridgellc.bridge.model.FinishServicesResponse;
import com.bridgellc.bridge.model.HomeResponse;
import com.bridgellc.bridge.model.ItemDetailResponse;
import com.bridgellc.bridge.model.ItemEditableResponse;
import com.bridgellc.bridge.model.NegotiateResponse;
import com.bridgellc.bridge.model.NotificationCountResponse;
import com.bridgellc.bridge.model.NotificationEntityResponse;
import com.bridgellc.bridge.model.PayForItemResponse;
import com.bridgellc.bridge.model.PaymentHistoryResponse;
import com.bridgellc.bridge.model.PaypalPayResponse;
import com.bridgellc.bridge.model.ProfileListResponse;
import com.bridgellc.bridge.model.ProfileResponse;
import com.bridgellc.bridge.model.ReviewResponce;
import com.bridgellc.bridge.model.SaveStripCardResponse;
import com.bridgellc.bridge.model.SignInResponse;
import com.bridgellc.bridge.model.SignUpResponse;
import com.bridgellc.bridge.model.UniqueCodeModelResponse;
import com.bridgellc.bridge.model.UniversityResponse;
import com.bridgellc.bridge.model.UpdatePhNumResponse;
import com.bridgellc.bridge.model.UploadPictureResponse;
import com.bridgellc.bridge.model.VerifyNumResponse;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;

public interface APICommonInterface {

    @FormUrlEncoded
    @POST("/api_bridge_login")
    void getSignInResponse(@Field("email_id") String email_id,
                           @Field("password") String password,
                           @Field("device_platform") String device_platform,
                           @Field("device_token") String device_token,
                           @Field("logincount") String logincount,
                           Callback<SignInResponse> callback);


    @FormUrlEncoded
    @POST("/api_bridge_signup")
    void getSignUpResponse(@Field("first_name") String first_name,
                           @Field("last_name") String last_name,
                           @Field("email_id") String email_id,
                           @Field("phone_number") String phone_number,
                           @Field("password") String password,
                           @Field("university_id") String university_id,
                           @Field("dob") String dob,
                           @Field("gender") String gender,
                           @Field("login_type") String login_type,
                           @Field("device_platform") String device_platform,
                           @Field("device_token") String device_token,
                           @Field("social_id") String social_id,
                           @Field("payment_mode") String payment_mode,
                           Callback<SignUpResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_update_profile")
    void getUpdateProfileResponse(@Field("user_id") String user_id,
                                  @Field("first_name") String first_name,
                                  @Field("last_name") String last_name,
                                  @Field("email_id") String email_id,
                                  @Field("edu_email_id") String edu_email_id,
                                  @Field("phone_number") String phone_number,
                                  @Field("password") String password,
                                  @Field("university_id") String university_id,
                                  @Field("dob") String dob,
                                  @Field("gender") String gender,
                                  @Field("login_type") String login_type,
                                  @Field("social_id") String social_id,
                                  @Field("payment_mode") String payment_mode,
                                  Callback<SignUpResponse> callback);


    @FormUrlEncoded
    @POST("/api_bridge_get_university")
    void getUniversityResponse(@Field("user_id") String user_id,
                               Callback<UniversityResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_clear_payment_history")
    void getPaymentClearResponse(@Field("user_id") String user_id,
                                 Callback<CommonModelResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_reset_password")
    void getResetPwdResponse(@Field("first_name") String first_name,@Field("email_id") String email_id,
                             Callback<CommonResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_update_phone_number")
    void getUpdatePhNumResponse(@Field("user_id") String user_id, @Field("phonenumber") String phonenumber,
                                Callback<UpdatePhNumResponse> callback);


    @FormUrlEncoded
    @POST("/api_bridge_update_edu_mail")
    void getUpdateMailResponse(@Field("user_id") String user_id, @Field("email_id") String
            email_id, Callback<CommonResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_notification_count")
    void getNotificationCountResponse(@Field("user_id") String user_id, Callback<NotificationCountResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_upload_item")
    void uploadItemResponse(@Field("user_id") String user_id,
                            @Field("item_mode") String item_mode,
                            @Field("item_type") String item_type,
                            @Field("delivery_type") String delivery_type,
                            @Field("item_category") String item_category,
                            @Field("item_name") String item_name,
                            @Field("item_quantity") String item_quantity,
                            @Field("item_cost") String item_cost,
                            @Field("item_condition") String item_condition,
                            @Field("is_fixed_cost") String is_fixed_cost,
                            @Field("item_description") String item_description,
                            @Field("is_big_bubble") String is_big_bubble,
                            @Field("is_billboard_bubble") String is_billboard_bubble,
                            @Field("billboard_bubble_hours") String billboard_bubble_hours,
                            @Field("file_url") String url,
                            @Field("preview_url") String preview_url,
                            @Field("original_url") String original_url,
                            @Field("file_url_type") String urlType,
                            @Field("picture1") String picture1,
                            @Field("picture2") String picture2,
                            @Field("picture3") String picture3,
                            @Field("event_date_time") String event_date_time,
                            @Field("venue") String venue,
                            @Field("payment_mode") String payment_mode,
                            @Field("phone_number") String phone_number,
                            @Field("website") String website,
                            Callback<CommonResponse> callback);

    @Multipart
    @POST("/api_bridge_upload_pictures")
    void uploadPhotosResponse(@Part("user_id") String user_id, @Part("picture1_data")
            TypedFile picture1,
                              @Part("picture2_data") TypedFile picture2,
                              @Part("picture3_data") TypedFile picture3,
                              Callback<UploadPictureResponse> callback);


    @FormUrlEncoded
    @POST("/api_bridge_get_items")
    void getHomeResponse(@Field("user_id") String user_id, @Field("limit") String limit,
                         @Field("offset") String offset, @Field("device_platform") String device_platform, @Field("device_token") String device_token, @Field("is_from_my_university") String is_from_my_university,
                         Callback<HomeResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_search_items")
    void getSearchResponse(@Field("user_id") String user_id, @Field("limit") String limit,
                           @Field("offset") String offset, @Field("search_key") String
                                   search_key,
                           Callback<HomeResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_get_chat")
    void getChatResponse(@Field("user_id") String user_id, @Field("friend_id") String
            friend_id, @Field("item_id") String item_id,
                         Callback<ChatResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_send_chat")
    void getChatSendResponse(@Field("user_id") String user_id, @Field("friend_id") String
            friend_id, @Field("item_id") String item_id, @Field("chat_message") String chat_message, @Field("payment_id") String payment_id,
                             Callback<CommonResponse> callback);


    @FormUrlEncoded
    @POST("/api_bridge_get_negotiations_for_item")
    void getNegotiationsResponse(@Field("user_id") String user_id, @Field("item_id")
            String item_id, @Field("buyer_id") String buyer_id, @Field("notification") String notification,
                                 Callback<NegotiateResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_negotiate_item")
    void getNegotiateSendResponse(@Field("user_id") String user_id, @Field("to_user_id")
            String friend_id, @Field("item_id") String item_id, @Field("negitation_amount") String
                                          negitation_amount, @Field("quantity") String quantity, @Field("bid_id") String bid_id,
                                  Callback<CommonResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_approve_negotiation")
    void approveNegotiateResponse(@Field("user_id") String user_id, @Field("negotiate_id")
            String negotiate_id, @Field("item_id") String item_id, @Field("negitation_amount") String
                                          approve_amount,
                                  Callback<CommonResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_get_my_profile")
    void getMyProfileResponse(@Field("user_id") String user_id,
                              Callback<ProfileResponse> callback);


    @FormUrlEncoded
    @POST("/api_bridge_get_user_profile")
    void getOtherProfileResponse(@Field("user_id") String user_id, @Field
            ("profile_user_id") String profile_user_id,
                                 Callback<ProfileResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_get_my_selling")
    void getMySellingResponse(@Field("user_id") String user_id,
                              Callback<ProfileListResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_get_my_buying")
    void getMyBuyingResponse(@Field("user_id") String user_id,
                             Callback<ProfileListResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_verify_number")
    void getVerifyNumResponse(@Field("user_id") String user_id, @Field("number") String number, @Field("otp") String otp,
                              Callback<VerifyNumResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_item_editable")
    void getItemEditableResponse(@Field("item_id") String item_id,
                                 Callback<ItemEditableResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_get_my_requests")
    void getMyRequestsResponse(@Field("user_id") String user_id,
                               Callback<ProfileListResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_get_my_biddings")
    void getMyBiddingsResponse(@Field("user_id") String user_id,
                               Callback<ProfileListResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_bid_get_request")
    void getMyBiddingsReqResponse(@Field("user_id") String user_id, @Field("item_id") String item_id,
                                  Callback<BiddingsReqResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_accept_bidding")
    void getAcceptBiddingResponse(@Field("user_id") String user_id, @Field("item_id") String item_id, @Field("bid_amount") String bid_amount, @Field("bid_id") String bid_id, @Field("request_user_id") String request_user_id,
                                  Callback<AcceptBiddingResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_get_my_payment_history")
    void getMyPaymentHistoryResponse(@Field("user_id") String user_id,
                                     Callback<PaymentHistoryResponse> callback);


    @FormUrlEncoded
    @POST("/api_bridge_clear_payment_history")
    void getMyPaymentClearHistoryResponse(@Field("user_id") String user_id,
                                          Callback<CommonResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_notifications")
    void getNotificationResponse(@Field("user_id") String user_id,
                                 Callback<NotificationEntityResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_mark_notification_read")
    void getNotificationReadResponse(@Field("user_id") String user_id, @Field("notification_id") String notification_id,
                                     Callback<CommonResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_delete_push")
    void deleteNotificationResponse(@Field("user_id") String user_id, @Field("notification_id") String notification_id, @Field("delete") String delete,
                                    Callback<CommonModelResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_add_rating")
    void postRating(@Field("user_id") String user_id, @Field("rate_user_id") String
            rate_user_id, @Field("rating") String rating,
                    @Field("comments") String comments,
                    @Field("item_id") String item_id,
                    Callback<CommonResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_view_ratings")
    void getViewRating(@Field("user_id") String user_id,
                       Callback<ReviewResponce> callback);

    @FormUrlEncoded
    @POST("/api_bridge_pay_for_item")
    void payForItemResponse(@Field("user_id") String user_id, @Field("item_id") String item_id, @Field("item_quantity") String item_quantity,
                            Callback<PayForItemResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_buy_item")
    void buyItemResponse(@Field("user_id") String user_id, @Field("item_id") String item_id,
                         @Field("item_quantity") String item_quantity, @Field("negotiate_id") String negotiate_id, @Field("tips") String tips,
                         Callback<BuyItemResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_mark_unsatisfy")
    void getUnsatiesFactoryResponse(@Field("user_id") String user_id, @Field("item_id") String item_id,
                                    @Field("payment_id") String payment_id,
                                    Callback<CommonResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_start_service")
    void getStartedServicesResponse(@Field("user_id") String user_id, @Field("item_id") String item_id,
                                    @Field("buyer_id") String buyer_id,
                                    Callback<CommonResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_get_item_details")
    void getItemDetailsResponse(@Field("user_id") String user_id, @Field("item_id") String item_id,
                                @Field("buyer_id") String buyer_id, @Field("payment_id") String payment_id,
                                @Field("notification_id") String notification_id, Callback<ItemDetailResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_update_item")
    void updateItemResponse(@Field("user_id") String user_id,
                            @Field("item_id") String item_id,
                            @Field("item_mode") String item_mode,
                            @Field("item_type") String item_type,
                            @Field("delivery_type") String delivery_type,
                            @Field("item_category") String item_category,
                            @Field("item_name") String item_name,
                            @Field("item_quantity") String item_quantity,
                            @Field("item_cost") String item_cost,
                            @Field("item_condition") String item_condition,
                            @Field("is_fixed_cost") String is_fixed_cost,
                            @Field("item_description") String item_description,
                            @Field("is_big_bubble") String is_big_bubble,
                            @Field("is_billboard_bubble") String is_billboard_bubble,
                            @Field("billboard_bubble_hours") String billboard_bubble_hours,
                            @Field("file_url") String url,
                            @Field("preview_url") String preview_url,
                            @Field("original_url") String original_url,
                            @Field("file_url_type") String urlType,
                            @Field("picture1") String picture1,
                            @Field("picture2") String picture2,
                            @Field("picture3") String picture3,
                            @Field("event_date_time") String event_date_time,
                            @Field("venue") String venue,
                            @Field("phone_number") String phone_number,
                            @Field("website") String website,
                            Callback<CommonResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_delete_item")
    void getDeleteItemResponse(@Field("user_id") String user_id, @Field("item_id") String item_id,
                               Callback<CommonResponse> callback);


    @FormUrlEncoded
    @POST("/api_bridge_finish_buy")
    void finishBuyResponse(@Field("user_id") String user_id, @Field("item_id") String item_id,
                           Callback<PayForItemResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_finish_service")
    void getFinishServicesBuyResponse(@Field("user_id") String user_id, @Field("item_id") String item_id, @Field("buyer_id") String buyer_id, @Field("payment_mode") String payment_mode,
                                      Callback<FinishServicesResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_finish_sale")
    void finishSaleResponse(@Field("user_id") String user_id, @Field("item_id") String item_id, @Field("unique_code") String unique_code, @Field("payment_mode") String payment_mode,
                            Callback<FinishServicesResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_add_bank_account")
    void getAddBankAccResponse(@Field("user_id") String user_id, @Field("payment_type") String payment_type, @Field("is_primary") String is_primary,
                               @Field("user_name") String user_name, @Field("account_number") String account_number,
                               @Field("routing_number") String routing_number,
                               @Field("bank_name") String bank_name,
                               @Field("cardnumber") String cardnumber, @Field("exp_month") String exp_month,
                               @Field("exp_year") String exp_year, @Field("cvc") String cvc, @Field("card_id") String card_id,
                               @Field("first_name") String first_name,
                               @Field("last_name") String last_name,
                               @Field("paypal_verified_email") String paypal_verified_email, @Field("paypal_id") String paypal_id,
                               Callback<BankAddAccountResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_get_bank_details")
    void getAccDetailsResponse(@Field("user_id") String user_id,
                               Callback<BankAccDetailsResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_card_save")
    void getSaveStripeCardResponse(@Field("user_id") String user_id, @Field("user_name") String user_name, @Field("cardnumber") String cardnumber, @Field("exp_month") String exp_month, @Field("exp_year") String exp_year, @Field("cvc") String cvc,
                                  Callback<SaveStripCardResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_get_card")
    void getStripeCardResponse(@Field("user_id") String user_id,
                               Callback<CardResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_upload_file")
    void getUploadFileSerResponse(@Field("user_id") String user_id,
                                  @Field("buyer_id") String buyer_id,
                                  @Field("item_id") String item_id,
                                  @Field("file_url") String file_url,
                                  @Field("file_url_type") String file_url_type,
                                  @Field("file_status") String file_status,
                                  @Field("payment_id") String payment_id,
                                  @Field("payment_mode") String payment_mode,
                                  Callback<CommonModelResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_approve_preview")
    void getApprovePreviewSerResponse(@Field("user_id") String user_id,
                                      @Field("item_id") String item_id,
                                      Callback<FinishServicesResponse> callback);


    @FormUrlEncoded
    @POST("/api_bridge_bid_for_request")
    void getBidRequestResponse(@Field("user_id") String user_id,
                               @Field("item_id") String item_id, @Field("bid_amount") String bid_amount,
                               @Field("to_user_id") String to_user_id,
                               @Field("quantity") String quantity, @Field("payment_mode") String payment_mode,
                               Callback<CommonModelResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_pay_item_paypal")
    void getPaypalPaymentResponse(@Field("user_id") String user_id,
                                  @Field("item_id") String item_id, @Field("item_quantity") String item_quantity,
                                  @Field("item_cost") String item_cost,
                                  @Field("pay_id") String pay_id, @Field("process_fee") String process_fee, @Field("total_cost") String total_cost,
                                  @Field("is_nagotiation") String is_nagotiation, @Field("negotiate_id") String negotiate_id,
                                  @Field("tips") String tips,
                                  @Field("card_id") String user_name,

                                  Callback<PaypalPayResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_bid_pay_paypal")
    void getBidPaypalPaymentResponse(@Field("user_id") String user_id,
                                     @Field("item_id") String item_id, @Field("bid_amount") String bid_amount,
                                     @Field("bid_id") String bid_id,
                                     @Field("request_user_id") String request_user_id,
                                     @Field("pay_id") String pay_id,
                                     @Field("negotiation_id") String negotiation_id,
                                     @Field("card_id") String card_id,

                                     Callback<PaypalPayResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_finish_buy")
    void getSaleCodeResponse(@Field("user_id") String user_id, @Field("item_id") String item_id, @Field("payment_id") String payment_id,
                             Callback<UniqueCodeModelResponse> callback);


    @FormUrlEncoded
    @POST("/api_bridge_make_partner")
    void getMakePartnerResponse(@Field("user_id") String user_id, @Field("partner_code") String partner_code, @Field("item_type") String item_type,
                                Callback<CommonResponse> callback);


    @FormUrlEncoded
    @POST("/api_bridge_partner_count")
    void getPartnerCountResponse(@Field("user_id") String user_id, @Field("owner_id") String owner_id, @Field("item_id") String item_id,
                                 @Field("website") String website, @Field("phone") String phone,
                                 Callback<CommonPhResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_offer_reject")
    void getOfferRejectResponse(@Field("user_id") String user_id, @Field("bid_id") String bid_id,
                                Callback<CommonPhResponse> callback);

    @FormUrlEncoded
    @POST("/api_bridge_logout")
    void getLogoutResponse(@Field("user_id") String user_id,
                           Callback<CommonResponse> callback);

}
