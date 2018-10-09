package com.smaat.virtualtrainer.apiinterface;

import com.smaat.virtualtrainer.model.CommonModelEntity;
import com.smaat.virtualtrainer.model.ContactSupportEntity;
import com.smaat.virtualtrainer.model.CreateStreamingEntity;
import com.smaat.virtualtrainer.model.InviteUserListEntity;
import com.smaat.virtualtrainer.model.JoinStreamingEntity;
import com.smaat.virtualtrainer.model.ResetPwdEntity;
import com.smaat.virtualtrainer.model.SignInEntity;
import com.smaat.virtualtrainer.model.SignUpEntity;
import com.smaat.virtualtrainer.model.StreamingEntity;
import com.smaat.virtualtrainer.model.UpdateProfileEntity;
import com.smaat.virtualtrainer.model.UserAccTypeEntity;
import com.smaat.virtualtrainer.model.UserListEntity;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

interface APICommonInterface {


    @FormUrlEncoded
    @POST("/svt/svt_api/index.php/api_svt_signup")
    Call<SignUpEntity> signupAPICall(@Field("user_name") String user_name,
                                     @Field("email_id") String email_id,
                                     @Field("password") String password,
                                     @Field("login_type") String login_type,
                                     @Field("facebook_id") String facebook_id,
                                     @Field("google_id") String google_id,
                                     @Field("linkedin_id") String linkedin_id,
                                     @Field("device_token") String device_token,
                                     @Field("device_platform") String device_platform);

    @FormUrlEncoded
    @POST("/svt/svt_api/index.php/api_svt_login")
    Call<SignInEntity> signinAPICall(@Field("email_id") String email_id,
                                     @Field("password") String password,
                                     @Field("login_type") String login_type,
                                     @Field("facebook_id") String facebook_id,
                                     @Field("google_id") String google_id,
                                     @Field("linkedin_id") String linkedin_id,
                                     @Field("device_token") String device_token,
                                     @Field("device_platform") String device_platform);

    @FormUrlEncoded
    @POST("/svt/svt_api/index.php/api_guest_join")
    Call<JoinStreamingEntity> joinAsGuestAPICall(@Field("name") String name, @Field("stream_id") String stream_id);

    @FormUrlEncoded
    @POST("/svt/svt_api/index.php/api_forgot_password")
    Call<ResetPwdEntity> resetPwdAPICall(@Field("email_id") String email_id);

    @FormUrlEncoded
    @POST("/svt/svt_api/index.php/api_select_account_type")
    Call<UserAccTypeEntity> userAccTypeAPICall(@Field("svt_user_id") String svt_user_id,
                                               @Field("user_account_type") String user_account_type);

    @FormUrlEncoded
    @POST("/svt/svt_api/index.php/api_create_stream")
    Call<CreateStreamingEntity> createStreamingAPICall(@Field("svt_user_id") String svt_user_id);

    @FormUrlEncoded
    @POST("/svt/svt_api/index.php/api_svt_users")
    Call<UserListEntity> userListAPICall(@Field("svt_user_id") String svt_user_id);

    @FormUrlEncoded
    @POST("svt/svt_api/index.php/api_svt_stream_invite")
    Call<InviteUserListEntity> inviteUserListAPICall(@Field("svt_user_id") String svt_user_id,
                                                     @Field("stream_name") String stream_name,
                                                     @Field("stream_id") String stream_id,
                                                     @Field("invite_user_ids") String invite_user_ids);

    @FormUrlEncoded
    @POST("svt/svt_api/index.php/api_update_profile")
    Call<UpdateProfileEntity> updateProfileAPICall(@Field("svt_user_id") String svt_user_id,
                                                   @Field("user_name") String user_name,
                                                   @Field("password") String password,
                                                   @Field("login_type") String login_type,
                                                   @Field("device_token") String device_token,
                                                   @Field("device_platform") String device_platform);

    @FormUrlEncoded
    @POST("/svt/svt_api/index.php/api_contact_support")
    Call<ContactSupportEntity> contactSupportAPICall(@Field("svt_user_id") String svt_user_id,
                                                     @Field("email_id") String email_id,
                                                     @Field("message") String message);

    @FormUrlEncoded
    @POST("/svt/svt_api/index.php/api_start_stream")
    Call<CommonModelEntity> startStreamingAPICall( @Field("stream_name") String stream_name,
                                                   @Field("svt_user_id") String svt_user_id,
                                                  @Field("stream_id") String stream_id,
                                                  @Field("stream_status") String stream_status);

    @FormUrlEncoded
    @POST("/svt/svt_api/index.php/api_svt_join_stream")
    Call<JoinStreamingEntity> joinStreamingAPICall(@Field("svt_user_id") String svt_user_id,
                                                   @Field("stream_id") String stream_id,
                                                   @Field("join_status") String join_status);

    @FormUrlEncoded
    @POST("/svt/svt_api/index.php/api_svt_home")
    Call<StreamingEntity> streamingAPICall(@Field("svt_user_id") String svt_user_id);
}
