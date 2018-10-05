package com.calix.calixgigamanage.services;

import com.calix.calixgigamanage.input.model.LoginRegistrationInputModel;
import com.calix.calixgigamanage.output.model.AddIOTDeviceResponse;
import com.calix.calixgigamanage.output.model.AlertResponse;
import com.calix.calixgigamanage.output.model.AlexaAppIdResponse;
import com.calix.calixgigamanage.output.model.AlexaRefreshTokenResponse;
import com.calix.calixgigamanage.output.model.CalixAgentResponse;
import com.calix.calixgigamanage.output.model.ChartDetailsResponse;
import com.calix.calixgigamanage.output.model.ChartFilterResponse;
import com.calix.calixgigamanage.output.model.CommonModuleResponse;
import com.calix.calixgigamanage.output.model.CommonResponse;
import com.calix.calixgigamanage.output.model.DashboardResponse;
import com.calix.calixgigamanage.output.model.DeviceFilterListResponse;
import com.calix.calixgigamanage.output.model.DeviceListResponse;
import com.calix.calixgigamanage.output.model.DeviceRenameResponse;
import com.calix.calixgigamanage.output.model.EncryptionTypeResponse;
import com.calix.calixgigamanage.output.model.FilterDeviceListResponse;
import com.calix.calixgigamanage.output.model.GuestWifiEntity;
import com.calix.calixgigamanage.output.model.GuestWifiResponse;
import com.calix.calixgigamanage.output.model.IOTDeviceConfigResponse;
import com.calix.calixgigamanage.output.model.InstallAlexaResponse;
import com.calix.calixgigamanage.output.model.LocationResponse;
import com.calix.calixgigamanage.output.model.LoginResponse;
import com.calix.calixgigamanage.output.model.RegistrationResponse;
import com.calix.calixgigamanage.output.model.RouterAgentListResponse;
import com.calix.calixgigamanage.output.model.RouterMapResponse;
import com.calix.calixgigamanage.output.model.RouterSetupResponse;
import com.calix.calixgigamanage.output.model.SpeedTestResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface APICommonInterface {

    /*Registration API*/
    @PUT("map/v1/mobile/account/add")
    Call<RegistrationResponse> registrationAPI(@Body LoginRegistrationInputModel registrationInputModel);

    /*Login API*/
    @POST("map/v1/mobile/account/login")
    Call<LoginResponse> loginAPI(@Body LoginRegistrationInputModel loginInputModel);

    /*Reset API*/
    @FormUrlEncoded
    @POST("map/v1/mobile/account/reset")
    Call<CommonResponse> restPasswordAPI(@Field("email") String emailStr);

    /*EncryptionType API*/
    @GET("map/v1/mobile/router/wifi/encryptionType")
    Call<EncryptionTypeResponse> encryptionTypeAPI(@Header("Authorization") String authorizationStr);

    /*Router Setup API*/
    @FormUrlEncoded
    @POST("map/v1/mobile/router/factoryinfo/send")
    Call<RouterSetupResponse> routerSetupAPI(@Header("Authorization") String authorizationStr, @Field("macAddress") String macAddressStr, @Field("serialNumber") String serialNumberStr);

    /*Router Setup API*/
    @FormUrlEncoded
    @POST("map/v1/mobile/router/update")
    Call<CommonResponse> routerUpdateAPI(@Header("Authorization") String authorizationStr, @Field("routerId") String routerIdStr, @Field("routerName") String routerNameStr, @Field("ssid") String ssidStr, @Field("password") String passwordStr, @Field("encryptionType") String encryptionTypeStr);

    /*Router Map API*/
    @GET("map/v1/mobile/router/map")
    Call<RouterMapResponse> routerMapAPI(@Header("Authorization") String authorizationStr);

    /*Dashboard API*/
    @GET("map/v1/mobile/dashboard")
    Call<DashboardResponse> dashboardAPI(@Header("Authorization") String authorizationStr);

    /*Alert API*/
    @GET("map/v1/mobile/notification")
    Call<AlertResponse> alertAPI(@Header("Authorization") String authorizationStr);

    /*Alert API*/
    @FormUrlEncoded
    @POST("map/v1/mobile/notification/read")
    Call<CommonResponse> notificationReadAPI(@Header("Authorization") String authorizationStr, @Field("notifId") String notifIdStr);

    /*Location API*/
    @GET("map/v1/mobile/iot/location/list")
    Call<LocationResponse> locationAPI(@Header("Authorization") String authorizationStr);

    /*Device List API*/
    @GET
    Call<DeviceListResponse> deviceListAPI(@Header("Authorization") String authorizationStr, @Url String urlStr);

    /*Device Filter List API*/
    @GET("map/v1/mobile/device/filter")
    Call<DeviceFilterListResponse> deviceFilterListAPI(@Header("Authorization") String authorizationStr);

    /*Device List By Filter API*/
    @GET
    Call<FilterDeviceListResponse> deviceListByFilterAPI(@Header("Authorization") String authorizationStr, @Url String urlStr);

    /*Disconnect the DeviceAPI*/
    @FormUrlEncoded
    @POST("map/v1/mobile/device/disconnect")
    Call<CommonResponse> deviceDisconnectAPI(@Header("Authorization") String authorizationStr, @Field("deviceId") String deviceIdStr);

    /*Disconnect the DeviceAPI*/
    @FormUrlEncoded
    @POST("map/v1/mobile/device/disconnect")
    Call<CommonResponse> deviceDisconnectAPI(@Header("Authorization") String authorizationStr, @Field("deviceId") String deviceIdStr, @Field("startTime") String startTimeStr, @Field("endTime") String endTimeStr);

    /*Disconnect the DeviceAPI*/
    @FormUrlEncoded
    @POST("map/v1/mobile/device/connect")
    Call<CommonResponse> deviceConnectAPI(@Header("Authorization") String authorizationStr, @Field("deviceId") String deviceIdStr);

    /*Rename the Device Name*/
    @FormUrlEncoded
    @POST("map/v1/mobile/device/rename")
    Call<DeviceRenameResponse> deviceRenameAPI(@Header("Authorization") String authorizationStr, @Field("deviceId") String deviceIdStr, @Field("name") String nameStr, @Field("locationId") String locationIdStr);

    /*IOT Device List API*/
    @GET("map/v1/mobile/iot/list?")
    Call<DeviceListResponse> iotDeviceListAPI(@Header("Authorization") String authorizationStr, @Query("routerId") String routerIdStr);

    /*Device List API*/
    @GET
    Call<IOTDeviceConfigResponse> iotDeviceConfigAPI(@Header("Authorization") String authorizationStr, @Url String urlStr);

    /*Guest List API*/
    @GET("map/v1/mobile/router/guest")
    Call<GuestWifiResponse> guestWifiListAPI(@Header("Authorization") String authorizationStr);

    /*Add Guest List API*/
    @PUT("map/v1/mobile/router/guest/add")
    Call<GuestWifiEntity> addGuestNetworkAPI(@Header("Authorization") String authorizationStr, @Body GuestWifiEntity guestWifiEntityInputModel);

    /*Update Guest List API*/
    @POST("map/v1/mobile/router/guest/update")
    Call<GuestWifiEntity> updateGuestNetworkAPI(@Header("Authorization") String authorizationStr, @Body GuestWifiEntity guestWifiEntityInputModel);

    /*Delete Guest List API*/
    @DELETE("map/v1/mobile/router/guest/delete?")
    Call<CommonResponse> deleteGuestNetworkAPI(@Header("Authorization") String authorizationStr, @Query("eventId") String eventId);

    /*Dashboard API*/
    @GET("map/v1/mobile/iot/device/type")
    Call<AddIOTDeviceResponse> addIOTDeviceAPI(@Header("Authorization") String authorizationStr);

    /*Chart API*/
    @GET("map/v1/mobile/device/usage/filter")
    Call<ChartFilterResponse> deviceChartFilterAPI(@Header("Authorization") String authorizationStr);

    /*Chart Details API*/
    @GET
    Call<ChartDetailsResponse> deviceChartDetailsAPI(@Header("Authorization") String authorizationStr, @Url String urlStr);

    /*Alexa Application Id API*/
    @GET("map/v1/mobile/application/id")
    Call<AlexaAppIdResponse> alexaAppIdAPI(@Header("Authorization") String authorizationStr);

    /*Update Alexa Application Id API*/
    @FormUrlEncoded
    @POST("map/v1/mobile/application/token/update")
    Call<CommonResponse> updateAlexaAppIdAPI(@Header("Authorization") String authorizationStr, @Field("appId") String appIdStr, @Field("token") String tokenStr, @Field("clientId") String clientIdStr, @Field("routerId") String routerIdStr);

    /*Set Alexa Application Id API*/
    @FormUrlEncoded
    @POST
    Call<InstallAlexaResponse> installAlexaAppIdAPI(@Header("Authorization") String authorizationStr, @Url String urlStr, @Field("appName") String appNameStr, @Field("userId") String userIdStr, @Field("routerId") String routerIdStr);

    /*Set Alexa Application Id API*/
    @FormUrlEncoded
    @POST
    Call<CommonResponse> sendContactAPI(@Header("Authorization") String authorizationStr, @Url String urlStr, @Field("guestName") String guestNameStr, @Field("userId") String userIdStr, @Field("routerId") String routerIdStr);

    /*Agent List API*/
    @GET("map/v1/mobile/application/list")
    Call<RouterAgentListResponse> routerAgentListAPI(@Header("Authorization") String authorizationStr);

    /*Install Agent API*/
    @FormUrlEncoded
    @POST("map/v1/mobile/application/install")
    Call<CommonModuleResponse> installRouterAgentAPI(@Header("Authorization") String authorizationStr, @Field("appId") String appIdStr, @Field("routerId") String routerIdStr);

    /*Uninstall Agent API*/
    @FormUrlEncoded
    @POST("map/v1/mobile/application/uninstall")
    Call<CommonResponse> uninstallRouterAgentAPI(@Header("Authorization") String authorizationStr, @Field("appId") String appIdStr, @Field("routerId") String routerIdStr);

    /*Update Alexa Application Id API*/
    @FormUrlEncoded
    @POST("map/v1/mobile/account/password/update")
    Call<CommonResponse> updatePwdAPI(@Header("Authorization") String authorizationStr, @Field("token") String tokenStr, @Field("newpassword") String newPwdStr);

    /*Update Alexa Application Id API*/
    @FormUrlEncoded
    @POST
    Call<AlexaRefreshTokenResponse> alexaRefreshTokenAPI(@Url String urlStr, @Field("grant_type") String tokenStr, @Field("client_id") String client_id
            , @Field("client_secret") String client_secretStr, @Field("redirect_uri") String redirect_uri
            , @Field("code_verifier") String code_verifier, @Field("code") String code);

    /*Start Speed Test API*/
    @FormUrlEncoded
    @POST("map/v1/mobile/speedtest/start")
    Call<CommonResponse> startSpeedTestAPI(@Header("Authorization") String authorizationStr, @Field("routerId") String routerIdStr);

    /*Get Speed Test API*/
    @GET("map/v1/mobile/speedtest/get?")
    Call<SpeedTestResponse> getSpeedTestAPI(@Header("Authorization") String authorizationStr, @Query("routerId") String routerIdStr);

    /*Agent List API*/
    @GET("map/v1/mobile/application/token")
    Call<CalixAgentResponse> calixAgentTokenAPI(@Header("Authorization") String authorizationStr, @Query("appId") String appIdStr, @Query("routerId") String routerIdStr);

    /*Remove Router API*/
    @POST("map/v1/mobile/router/remove")
    Call<CommonModuleResponse> removeRouterAPI(@Header("Authorization") String authorizationStr);

}
