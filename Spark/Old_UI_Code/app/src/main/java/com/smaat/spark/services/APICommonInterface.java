package com.smaat.spark.services;


import com.smaat.spark.entity.inputEntity.ChatConnDisInputEntity;
import com.smaat.spark.entity.inputEntity.ChatSendReceiveInputEntity;
import com.smaat.spark.entity.inputEntity.LoginRegResetInputEntity;
import com.smaat.spark.entity.inputEntity.NotificationInputEntity;
import com.smaat.spark.entity.inputEntity.SettingsInputEntity;
import com.smaat.spark.model.AddressResponse;
import com.smaat.spark.model.BackgroundResponse;
import com.smaat.spark.model.ChatConnectResponse;
import com.smaat.spark.model.ChatReceiveResponse;
import com.smaat.spark.model.ChatSendResponse;
import com.smaat.spark.model.CommonModel;
import com.smaat.spark.model.CommonResponse;
import com.smaat.spark.model.DiscoverResponse;
import com.smaat.spark.model.FriendsListResponse;
import com.smaat.spark.model.ImageResponse;
import com.smaat.spark.model.NotificationResponse;
import com.smaat.spark.model.TrendsResponse;
import com.smaat.spark.model.UserDetailsResponse;
import com.smaat.spark.model.VimeoVideoImgResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface APICommonInterface {

    @POST("spark/index.php/api_interface")
    Call<UserDetailsResponse> signinAPICall(@Body LoginRegResetInputEntity LoginRegResetInputEntity);

    @POST("spark/index.php/api_interface")
    Call<CommonResponse> resetPwdAPICall(@Body LoginRegResetInputEntity LoginRegResetInputEntity);

    @POST("spark/index.php/api_interface")
    Call<ChatConnectResponse> connectAPICall(@Body ChatConnDisInputEntity chatConnDisInputEntity);

    @POST("spark/index.php/api_interface")
    Call<ChatSendResponse> sendAPICall(@Body ChatSendReceiveInputEntity chatSendInputEntity);

    @POST("spark/index.php/api_interface")
    Call<ChatReceiveResponse> receiveAPICall(@Body ChatSendReceiveInputEntity chatReceiveInputEntity);

    @POST("spark/index.php/api_interface")
    Call<ChatReceiveResponse> callMsgAPICall(@Body ChatSendReceiveInputEntity chatReceiveInputEntity);

    @POST("spark/index.php/api_interface")
    Call<CommonResponse> disConnectAPICall(@Body ChatConnDisInputEntity chatConnDisInputEntity);

    @POST("spark/index.php/api_interface")
    Call<TrendsResponse> trendsAPICall(@Body ChatConnDisInputEntity trendsInputEntityRes);

    @POST("spark/index.php/api_interface")
    Call<DiscoverResponse> discoverAPICall(@Body ChatConnDisInputEntity trendsInputEntityRes);

    @POST("spark/index.php/api_interface")
    Call<FriendsListResponse> friendsAPICall(@Body ChatConnDisInputEntity friendsInputEntityRes);

    @POST("spark/index.php/api_interface")
    Call<CommonResponse> addFriendAPICall(@Body ChatSendReceiveInputEntity chatReceiveInputEntity);

    @POST("spark/index.php/api_interface")
    Call<CommonResponse> inviteContactsFriendAPICall(@Body LoginRegResetInputEntity chatReceiveInputEntity);

    @GET
    Call<AddressResponse> getUserAddressAPICall(@Url String urlStr);

    @POST("spark/index.php/api_interface")
    Call<CommonResponse> updateUserNameAPICall(@Body LoginRegResetInputEntity updateInputEntity);

    @POST("spark/index.php/api_interface")
    Call<NotificationResponse> getNotificationAPICall(@Body ChatConnDisInputEntity notificationInputEntity);

    @POST("spark/index.php/api_interface")
    Call<CommonModel> getReadNotificationAPICall(@Body NotificationInputEntity notificationInputEntity);

    @POST("spark/index.php/api_interface")
    Call<BackgroundResponse> getBackgroundAPICall(@Body ChatConnDisInputEntity backgroundInputEntity);

    @Multipart
    @POST("spark/index.php/api_image_upload")
    Call<ImageResponse> profileImageUploadAPICall(@Part MultipartBody.Part mode, @Part MultipartBody.Part add_shop_logo, @Part MultipartBody.Part filePart);

    @POST("spark/index.php/api_interface")
    Call<UserDetailsResponse> settingsAPICall(@Body SettingsInputEntity settingsInputEntity);

    @GET
    Call<VimeoVideoImgResponse> getVimeoImageAPICall(@Url String urlStr);

    @POST("spark/index.php/api_interface")
    Call<CommonResponse> deleteMsgAPICall(@Body NotificationInputEntity settingsInputEntity);
}
