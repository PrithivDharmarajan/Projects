package com.calix.calixgigaspireapp.services;


import android.support.annotation.NonNull;
import android.util.Log;

import com.calix.calixgigaspireapp.input.model.LoginRegistrationInputModel;
import com.calix.calixgigaspireapp.main.BaseActivity;
import com.calix.calixgigaspireapp.main.BaseFragment;
import com.calix.calixgigaspireapp.output.model.AddIOTDeviceResponse;
import com.calix.calixgigaspireapp.output.model.AlertResponse;
import com.calix.calixgigaspireapp.output.model.AlexaAppIdResponse;
import com.calix.calixgigaspireapp.output.model.AlexaRefreshTokenResponse;
import com.calix.calixgigaspireapp.output.model.CalixAgentResponse;
import com.calix.calixgigaspireapp.output.model.ChartDetailsResponse;
import com.calix.calixgigaspireapp.output.model.ChartFilterResponse;
import com.calix.calixgigaspireapp.output.model.CommonModuleResponse;
import com.calix.calixgigaspireapp.output.model.CommonResponse;
import com.calix.calixgigaspireapp.output.model.DashboardResponse;
import com.calix.calixgigaspireapp.output.model.DeviceFilterListResponse;
import com.calix.calixgigaspireapp.output.model.DeviceListResponse;
import com.calix.calixgigaspireapp.output.model.DeviceRenameResponse;
import com.calix.calixgigaspireapp.output.model.EncryptionTypeResponse;
import com.calix.calixgigaspireapp.output.model.ErrorResponse;
import com.calix.calixgigaspireapp.output.model.FilterDeviceListResponse;
import com.calix.calixgigaspireapp.output.model.GuestWifiEntity;
import com.calix.calixgigaspireapp.output.model.GuestWifiResponse;
import com.calix.calixgigaspireapp.output.model.IOTDeviceConfigResponse;
import com.calix.calixgigaspireapp.output.model.InstallAlexaResponse;
import com.calix.calixgigaspireapp.output.model.LoginResponse;
import com.calix.calixgigaspireapp.output.model.RegistrationResponse;
import com.calix.calixgigaspireapp.output.model.RouterAgentListResponse;
import com.calix.calixgigaspireapp.output.model.RouterMapResponse;
import com.calix.calixgigaspireapp.output.model.RouterSetupResponse;
import com.calix.calixgigaspireapp.output.model.SpeedTestResponse;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.DialogManager;
import com.calix.calixgigaspireapp.utils.PreferenceUtil;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIRequestHandler {

    private static APIRequestHandler sInstance = new APIRequestHandler();

    /*Init retrofit for API call*/
    private APICommonInterface mServiceInterface = serviceInterface();

    public static APIRequestHandler getInstance() {
        return sInstance;
    }

    private APICommonInterface serviceInterface() {
        return new Retrofit.Builder().baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(APICommonInterface.class);
    }

    public void refreshBaseUrl() {
        mServiceInterface = serviceInterface();
    }

    /*Login API*/
    public void loginAPICall(LoginRegistrationInputModel loginInputModel, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.loginAPI(loginInputModel).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new LoginResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new LoginResponse(), t);
            }
        });
    }


    /*Registration API*/
    public void registrationAPICall(LoginRegistrationInputModel registrationInputModel, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.registrationAPI(registrationInputModel).enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(@NonNull Call<RegistrationResponse> call, @NonNull Response<RegistrationResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new RegistrationResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegistrationResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new RegistrationResponse(), t);
            }
        });
    }


    /*Reset Password API*/
    public void resetAPICall(String emailStr, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.restPasswordAPI(emailStr).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful()) {
                    CommonResponse commonResponse = new CommonResponse();
                    commonResponse.setErrorType(AppConstants.SUCCESS_CODE);
                    baseActivity.onRequestSuccess(commonResponse);
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new CommonResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new CommonResponse(), t);
            }
        });
    }

    /*Router Setup API*/
    public void routerSetupAPICall(String macAddressStr, String serialNumberStr, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.routerSetupAPI(PreferenceUtil.getAuthorization(baseActivity), macAddressStr, serialNumberStr).enqueue(new Callback<RouterSetupResponse>() {
            @Override
            public void onResponse(@NonNull Call<RouterSetupResponse> call, @NonNull Response<RouterSetupResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }

                    baseActivity.onRequestFailure(new RouterSetupResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<RouterSetupResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new RouterSetupResponse(), t);
            }
        });
    }


    /*EncryptionType API*/
    public void encryptionTypeAPICall(final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.encryptionTypeAPI(PreferenceUtil.getAuthorization(baseActivity)).enqueue(new Callback<EncryptionTypeResponse>() {
            @Override
            public void onResponse(@NonNull Call<EncryptionTypeResponse> call, @NonNull Response<EncryptionTypeResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new EncryptionTypeResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<EncryptionTypeResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new EncryptionTypeResponse(), t);
            }
        });
    }

    /*Router Map API*/
    public void routerMapAPICall(final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.routerMapAPI(PreferenceUtil.getAuthorization(baseActivity)).enqueue(new Callback<RouterMapResponse>() {
            @Override
            public void onResponse(@NonNull Call<RouterMapResponse> call, @NonNull Response<RouterMapResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new RouterMapResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<RouterMapResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new RouterMapResponse(), t);
            }
        });
    }


    /*Update Router Details API*/
    public void routerUpdateAPICall(String routerIdStr, String routerNameStr, String ssidStr, String passwordStr, String encryptionTypeStr, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.routerUpdateAPI(PreferenceUtil.getAuthorization(baseActivity), routerIdStr, routerNameStr, ssidStr, passwordStr, encryptionTypeStr).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful()) {
                    CommonResponse commonResponse = new CommonResponse();
                    commonResponse.setErrorType(AppConstants.SUCCESS_CODE);
                    baseActivity.onRequestSuccess(commonResponse);
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new CommonResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new CommonResponse(), t);
            }
        });
    }

    /*Update Router Name API*/
    public void routerNameUpdateAPICall(String routerIdStr, String routerNameStr, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.routerNameUpdateAPI(PreferenceUtil.getAuthorization(baseActivity), routerIdStr, routerNameStr).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful()) {
                    CommonResponse commonResponse = new CommonResponse();
                    commonResponse.setErrorType(AppConstants.SUCCESS_CODE);
                    baseActivity.onRequestSuccess(commonResponse);
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new CommonResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new CommonResponse(), t);
            }
        });
    }

    /*Device List API*/
    public void deviceListAPICall(final String urlStr, final BaseFragment baseFragment) {
        DialogManager.getInstance().showProgress(baseFragment.getActivity());

        mServiceInterface.deviceNetworkListAPI(PreferenceUtil.getAuthorization(baseFragment.getActivity()), String.format((PreferenceUtil.getBaseURL(baseFragment.getActivity()) + AppConstants.API_DEVICE_LIST), urlStr)).enqueue(new Callback<DeviceListResponse>() {
            @Override
            public void onResponse(@NonNull Call<DeviceListResponse> call, @NonNull Response<DeviceListResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                    Log.d(urlStr, "URL");
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseFragment.onRequestFailure(new DeviceListResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<DeviceListResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseFragment.onRequestFailure(new DeviceListResponse(), t);
            }
        });
    }


    /*Notification*/
    public void notificationReadAPICall(String notificationIdStr, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.notificationReadAPI(PreferenceUtil.getAuthorization(baseActivity), notificationIdStr).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful()) {
                    CommonResponse commonResponse = new CommonResponse();
                    commonResponse.setErrorType(AppConstants.SUCCESS_CODE);
                    baseActivity.onRequestSuccess(commonResponse);
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new CommonResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new CommonResponse(), t);
            }
        });
    }

    /*IOT Device List API*/
    public void iotDeviceListAPICall(String routerIdStr,final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.iotDeviceListAPI(PreferenceUtil.getAuthorization(baseActivity),routerIdStr).enqueue(new Callback<DeviceListResponse>() {
            @Override
            public void onResponse(@NonNull Call<DeviceListResponse> call, @NonNull Response<DeviceListResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new DeviceListResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<DeviceListResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new DeviceListResponse(), t);
            }
        });
    }

    /*Add IOT Device API*/
    public void addIOTDeviceAPICall(final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.addIOTDeviceAPI(PreferenceUtil.getAuthorization(baseActivity)).enqueue(new Callback<AddIOTDeviceResponse>() {
            @Override
            public void onResponse(@NonNull Call<AddIOTDeviceResponse> call, @NonNull Response<AddIOTDeviceResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new AddIOTDeviceResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddIOTDeviceResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new AddIOTDeviceResponse(), t);
            }
        });
    }

    /*IOT Config Device API*/
    public void iotDeviceConfigAPICall(String urlStr, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);

        mServiceInterface.iotDeviceConfigAPI(PreferenceUtil.getAuthorization(baseActivity), String.format(PreferenceUtil.getBaseURL(baseActivity) + AppConstants.API_IOT_DEVICE_CONFIG, urlStr)).enqueue(new Callback<IOTDeviceConfigResponse>() {
            @Override
            public void onResponse(@NonNull Call<IOTDeviceConfigResponse> call, @NonNull Response<IOTDeviceConfigResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new IOTDeviceConfigResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<IOTDeviceConfigResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new IOTDeviceConfigResponse(), t);
            }
        });
    }

    /*Dashboard API*/
    public void dashboardTypeAPICall(final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.dashboardAPI(PreferenceUtil.getAuthorization(baseActivity)).enqueue(new Callback<DashboardResponse>() {
            @Override
            public void onResponse(@NonNull Call<DashboardResponse> call, @NonNull Response<DashboardResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new DashboardResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<DashboardResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new DashboardResponse(), t);
            }
        });
    }

    /*Device List API*/
    public void deviceListAPICall(final String deviceType, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.deviceListAPI(PreferenceUtil.getAuthorization(baseActivity), deviceType).enqueue(new Callback<DeviceListResponse>() {
            @Override
            public void onResponse(@NonNull Call<DeviceListResponse> call, @NonNull Response<DeviceListResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new DeviceListResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<DeviceListResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new DeviceListResponse(), t);
            }
        });
    }

    /*Connect the Device API*/
    public void deviceConnectAPICall(String deviceIdStr, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.deviceConnectAPI(PreferenceUtil.getAuthorization(baseActivity), deviceIdStr).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.code() == 200) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new CommonResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new CommonResponse(), t);
            }
        });
    }

    /*Disconnect the Device API*/
    public void deviceDisconnectAPICall(String deviceIdStr, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.deviceDisconnectAPI(PreferenceUtil.getAuthorization(baseActivity), deviceIdStr).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.code() == 200) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new CommonResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new CommonResponse(), t);
            }
        });
    }

    /*Device Chart Details*/
    public void deviceChartDetailsAPICall(String deviceIdStr, String filterStr, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.deviceChartDetailsAPI(PreferenceUtil.getAuthorization(baseActivity), String.format((PreferenceUtil.getBaseURL(baseActivity) + AppConstants.API_DEVICE_USAGE), deviceIdStr, filterStr)).enqueue(new Callback<ChartDetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<ChartDetailsResponse> call, @NonNull Response<ChartDetailsResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new ChartDetailsResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChartDetailsResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new ChartDetailsResponse(), t);
            }
        });
    }

    /*Device Chart Filter*/
    public void deviceChartFilterAPICall(final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.deviceChartFilterAPI(PreferenceUtil.getAuthorization(baseActivity)).enqueue(new Callback<ChartFilterResponse>() {
            @Override
            public void onResponse(@NonNull Call<ChartFilterResponse> call, @NonNull Response<ChartFilterResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new ChartDetailsResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChartFilterResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new ChartDetailsResponse(), t);
            }
        });
    }

    /*Rename the Device Name API*/
    public void deviceRenameAPICalll(String deviceIdStr, String nameStr, String locationIdStr, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.deviceRenameAPI(PreferenceUtil.getAuthorization(baseActivity), deviceIdStr, nameStr, locationIdStr).enqueue(new Callback<DeviceRenameResponse>() {
            @Override
            public void onResponse(@NonNull Call<DeviceRenameResponse> call, @NonNull Response<DeviceRenameResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.code() == 200) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new DeviceRenameResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<DeviceRenameResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new DeviceRenameResponse(), t);
            }
        });
    }

    /*alert API*/
    public void alertAPICall(final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.alertAPI(PreferenceUtil.getAuthorization(baseActivity)).enqueue(new Callback<AlertResponse>() {
            @Override
            public void onResponse(@NonNull Call<AlertResponse> call, @NonNull Response<AlertResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }

                    baseActivity.onRequestFailure(new AlertResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<AlertResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new AlertResponse(), t);
            }
        });
    }

    /*Guest Wifi List API*/
    public void guestWifiListAPICall(final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.guestWifiListAPI(PreferenceUtil.getAuthorization(baseActivity)).enqueue(new Callback<GuestWifiResponse>() {
            @Override
            public void onResponse(@NonNull Call<GuestWifiResponse> call, @NonNull Response<GuestWifiResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new GuestWifiResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<GuestWifiResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new GuestWifiResponse(), t);
            }
        });
    }

    /*Add Guest Wifi API*/
    public void addGuestNetworkAPICall(GuestWifiEntity guestWifiEntityInputModel, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.addGuestNetworkAPI(PreferenceUtil.getAuthorization(baseActivity), guestWifiEntityInputModel).enqueue(new Callback<GuestWifiEntity>() {
            @Override
            public void onResponse(@NonNull Call<GuestWifiEntity> call, @NonNull Response<GuestWifiEntity> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new GuestWifiEntity(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<GuestWifiEntity> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new GuestWifiEntity(), t);
            }
        });
    }

    /*Update Guest Wifi API*/
    public void updateGuestNetworkAPICall(GuestWifiEntity guestWifiEntityInputModel, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.updateGuestNetworkAPI(PreferenceUtil.getAuthorization(baseActivity), guestWifiEntityInputModel).enqueue(new Callback<GuestWifiEntity>() {
            @Override
            public void onResponse(@NonNull Call<GuestWifiEntity> call, @NonNull Response<GuestWifiEntity> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new GuestWifiEntity(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<GuestWifiEntity> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new GuestWifiEntity(), t);
            }
        });
    }


    /*Delete Guest Wifi API*/
    public void deleteGuestNetworkAPICall(String eventId, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.deleteGuestNetworkAPI(PreferenceUtil.getAuthorization(baseActivity), eventId).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new CommonResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new CommonResponse(), t);
            }
        });
    }


    /*Send Guest Alexa App Id*/
    public void sendGuestContactAPICall(String guestNameStr, String userIdStr, String routerIdStr, final BaseActivity baseActivity) {
        mServiceInterface.sendContactAPI(PreferenceUtil.getAuthorization(baseActivity), PreferenceUtil.getBaseURL(baseActivity) + AppConstants.API_GUEST_CONTACTS, guestNameStr, userIdStr, routerIdStr).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    CommonResponse commonResponse = new CommonResponse();
                    commonResponse.setErrorType(AppConstants.SUCCESS_CODE);
                    baseActivity.onRequestSuccess(commonResponse);
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new CommonResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                baseActivity.onRequestFailure(new CommonResponse(), t);
            }
        });
    }

    /*Install Alexa App Id*/
    public void installAlexaAppIdAPICall(String userIdStr, String routerIdStr, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.installAlexaAppIdAPI(PreferenceUtil.getAuthorization(baseActivity), PreferenceUtil.getBaseURL(baseActivity) + AppConstants.API_INSTALL_ALEXA, AppConstants.ALEXA, userIdStr, routerIdStr).enqueue(new Callback<InstallAlexaResponse>() {
            @Override
            public void onResponse(@NonNull Call<InstallAlexaResponse> call, @NonNull Response<InstallAlexaResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new CommonResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<InstallAlexaResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new InstallAlexaResponse(), t);
            }
        });
    }

    /*Router agent list*/
    public void routerAgentListAPICall(final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.routerAgentListAPI(PreferenceUtil.getAuthorization(baseActivity)).enqueue(new Callback<RouterAgentListResponse>() {
            @Override
            public void onResponse(@NonNull Call<RouterAgentListResponse> call, @NonNull Response<RouterAgentListResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new ChartDetailsResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<RouterAgentListResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new ChartDetailsResponse(), t);
            }
        });
    }

    /*Alexa App Id*/
    public void alexaAppIdsAPICall(final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.alexaAppIdAPI(PreferenceUtil.getAuthorization(baseActivity)).enqueue(new Callback<AlexaAppIdResponse>() {
            @Override
            public void onResponse(@NonNull Call<AlexaAppIdResponse> call, @NonNull Response<AlexaAppIdResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new ChartDetailsResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<AlexaAppIdResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new ChartDetailsResponse(), t);
            }
        });
    }

    /*Start Speed Test*/
    public void calixAgentTokenAPICall(String appIdStr, String routerIdStr, final BaseActivity baseActivity) {
        mServiceInterface.calixAgentTokenAPI(PreferenceUtil.getAuthorization(baseActivity), appIdStr, routerIdStr).enqueue(new Callback<CalixAgentResponse>() {
            @Override
            public void onResponse(@NonNull Call<CalixAgentResponse> call, @NonNull Response<CalixAgentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    DialogManager.getInstance().hideProgress();
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new CalixAgentResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CalixAgentResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new CalixAgentResponse(), t);
            }
        });
    }

    /*Install router agent*/
    public void installRouterAgentAPICall(String appIdStr, String routerIdStr, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.installRouterAgentAPI(PreferenceUtil.getAuthorization(baseActivity), appIdStr, routerIdStr).enqueue(new Callback<CommonModuleResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonModuleResponse> call, @NonNull Response<CommonModuleResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful()) {
                    CommonModuleResponse commonResponse = new CommonModuleResponse();
                    commonResponse.setErrorType(AppConstants.SUCCESS_CODE);
                    baseActivity.onRequestSuccess(commonResponse);
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new CommonModuleResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonModuleResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new CommonModuleResponse(), t);
            }
        });
    }
    /*Uninstall router agent*/
    public void uninstallRouterAgentAPICall(String appIdStr, String routerIdStr, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.uninstallRouterAgentAPI(PreferenceUtil.getAuthorization(baseActivity), appIdStr, routerIdStr).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful()) {
                    CommonResponse commonResponse = new CommonResponse();
                    commonResponse.setErrorType(AppConstants.SUCCESS_CODE);
                    baseActivity.onRequestSuccess(commonResponse);
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new CommonResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new CommonResponse(), t);
            }
        });
    }

    /*Start Speed Test*/
    public void startSpeedTestAPICall(String routerIdStr, final BaseActivity baseActivity) {
        mServiceInterface.startSpeedTestAPI(PreferenceUtil.getAuthorization(baseActivity), routerIdStr).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    CommonResponse commonResponse = new CommonResponse();
                    commonResponse.setErrorType(AppConstants.SUCCESS_CODE);
                    baseActivity.onRequestSuccess(commonResponse);
                } else {
                    DialogManager.getInstance().hideProgress();
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new CommonResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new CommonResponse(), t);
            }
        });
    }

    /*Start Speed Test*/
    public void getSpeedTestAPICall(String routerIdStr, final BaseActivity baseActivity) {
        mServiceInterface.getSpeedTestAPI(PreferenceUtil.getAuthorization(baseActivity), routerIdStr).enqueue(new Callback<SpeedTestResponse>() {
            @Override
            public void onResponse(@NonNull Call<SpeedTestResponse> call, @NonNull Response<SpeedTestResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    DialogManager.getInstance().hideProgress();
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new SpeedTestResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<SpeedTestResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new SpeedTestResponse(), t);
            }
        });
    }

    /*update pwd*/
    public void updatePwdAPICall(String newPwdStr, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.updatePwdAPI(PreferenceUtil.getAuthorization(baseActivity), PreferenceUtil.getStringValue(baseActivity, AppConstants.AUTHORIZATION), newPwdStr).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful()) {
                    CommonResponse commonResponse = new CommonResponse();
                    commonResponse.setErrorType(AppConstants.SUCCESS_CODE);
                    baseActivity.onRequestSuccess(commonResponse);
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new CommonResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new CommonResponse(), t);
            }
        });
    }

    /*Update Alexa App Id*/
    public void updateAlexaAppIdAPICall(String appIdStr, String tokenStr, String clientIdStr, String routerIdStr, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.updateAlexaAppIdAPI(PreferenceUtil.getAuthorization(baseActivity), appIdStr, tokenStr, clientIdStr, routerIdStr).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful()) {
                    CommonResponse commonResponse = new CommonResponse();
                    commonResponse.setErrorType(AppConstants.SUCCESS_CODE);
                    baseActivity.onRequestSuccess(commonResponse);
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new CommonResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new CommonResponse(), t);
            }
        });
    }

    /*get Alexa Refresh Token*/
    public void alexaRefreshTokenAPICall(String client_id, String client_secretStr, String redirect_uri
            , String code_verifier, String code, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.alexaRefreshTokenAPI(AppConstants.ALEXA_BASE_URL, AppConstants.GRANT_TYPE, client_id, client_secretStr, redirect_uri
                , code_verifier, code).enqueue(new Callback<AlexaRefreshTokenResponse>() {
            @Override
            public void onResponse(@NonNull Call<AlexaRefreshTokenResponse> call, @NonNull Response<AlexaRefreshTokenResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new ChartDetailsResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<AlexaRefreshTokenResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new ChartDetailsResponse(), t);
            }
        });
    }

    /*Device List API*/
    public void deviceFilterListAPICall(final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);

        mServiceInterface.deviceFilterListAPI(PreferenceUtil.getAuthorization(baseActivity)).enqueue(new Callback<DeviceFilterListResponse>() {
            @Override
            public void onResponse(@NonNull Call<DeviceFilterListResponse> call, @NonNull Response<DeviceFilterListResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new DeviceFilterListResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<DeviceFilterListResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new DeviceFilterListResponse(), t);
            }
        });
    }

    /*Device List Filter API*/
    public void deviceListByFilterAPICall(String urlStr, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.deviceListByFilterAPI(PreferenceUtil.getAuthorization(baseActivity), String.format(PreferenceUtil.getBaseURL(baseActivity) + AppConstants.API_DEVICE_FILTER_LIST, urlStr)).enqueue(new Callback<FilterDeviceListResponse>() {
            @Override
            public void onResponse(@NonNull Call<FilterDeviceListResponse> call, @NonNull Response<FilterDeviceListResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new FilterDeviceListResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<FilterDeviceListResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new FilterDeviceListResponse(), t);
            }
        });
    }

    /*Install router agent*/
    public void removeRouterAPICall(final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.removeRouterAPI(PreferenceUtil.getAuthorization(baseActivity)).enqueue(new Callback<CommonModuleResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonModuleResponse> call, @NonNull Response<CommonModuleResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful()) {
                    CommonModuleResponse commonResponse = new CommonModuleResponse();
                    commonResponse.setErrorType(AppConstants.SUCCESS_CODE);
                    baseActivity.onRequestSuccess(commonResponse);
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorDesc().isEmpty() ? errorMsgStr : errorBodyRes.getErrorDesc();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new CommonModuleResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonModuleResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new CommonModuleResponse(), t);
            }
        });
    }
}


