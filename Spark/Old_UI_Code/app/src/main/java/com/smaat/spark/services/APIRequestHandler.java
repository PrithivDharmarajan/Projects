package com.smaat.spark.services;


import com.smaat.spark.entity.inputEntity.ChatConnDisInputEntity;
import com.smaat.spark.entity.inputEntity.ChatSendReceiveInputEntity;
import com.smaat.spark.entity.inputEntity.LoginRegResetInputEntity;
import com.smaat.spark.entity.inputEntity.NotificationInputEntity;
import com.smaat.spark.entity.inputEntity.SettingsInputEntity;
import com.smaat.spark.main.BaseActivity;
import com.smaat.spark.main.BaseFragment;
import com.smaat.spark.model.AddressResponse;
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
import com.smaat.spark.utils.AppConstants;
import com.smaat.spark.utils.DialogManager;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIRequestHandler {

    private static APIRequestHandler sInstance = new APIRequestHandler();

    private Retrofit retrofit = new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).
            addConverterFactory(GsonConverterFactory.create()).build();
    private APICommonInterface mServiceInterface = retrofit.create(APICommonInterface.class);


    public static APIRequestHandler getInstance() {
        return sInstance;
    }

    public void callSignInAPI(LoginRegResetInputEntity loginInput, final BaseActivity baseActivity) {
        DialogManager.showProgress(baseActivity);
        mServiceInterface.signinAPICall(loginInput).enqueue(new Callback<UserDetailsResponse>() {
            @Override
            public void onResponse(Call<UserDetailsResponse> call, Response<UserDetailsResponse> response) {
                DialogManager.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    baseActivity.onRequestFailure(new Throwable(response.raw().message()));
                }

            }

            @Override
            public void onFailure(Call<UserDetailsResponse> call, Throwable t) {
                DialogManager.hideProgress();
                baseActivity.onRequestFailure(t);

            }
        });

    }

    public void callResetPwdAPI(LoginRegResetInputEntity loginInput, final BaseActivity baseActivity) {
        DialogManager.showProgress(baseActivity);
        mServiceInterface.resetPwdAPICall(loginInput).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                DialogManager.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    baseActivity.onRequestFailure(new Throwable(response.raw().message()));
                }

            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                DialogManager.hideProgress();
                baseActivity.onRequestFailure(t);

            }
        });

    }

    public void callProfileImageUploadAPI(String imagePath, final BaseFragment baseFragment) {
        DialogManager.showProgress(baseFragment.getActivity());
        File file = new File(imagePath);
        RequestBody imageBody = RequestBody.create(MediaType.parse("image/jpeg/png"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("picture", file.getName(), imageBody);
        MultipartBody.Part api_mode = MultipartBody.Part.createFormData("mode", AppConstants.API_IMAGE_UPLOAD_MODE);
        MultipartBody.Part apiPart = MultipartBody.Part.createFormData("api_name", AppConstants.API_IMAGE_UPLOAD);

        mServiceInterface.profileImageUploadAPICall(api_mode, apiPart, body).enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                DialogManager.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable throwableMsg) {
                DialogManager.hideProgress();
                baseFragment.onRequestFailure(throwableMsg);
            }
        });
    }


    public void callConnectAPI(ChatConnDisInputEntity chatConnDisInputEntity, final BaseFragment baseFragment) {
//        DialogManager.showProgress(baseActivity);
        mServiceInterface.connectAPICall(chatConnDisInputEntity).enqueue(new Callback<ChatConnectResponse>() {
            @Override
            public void onResponse(Call<ChatConnectResponse> call, Response<ChatConnectResponse> response) {
//                DialogManager.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<ChatConnectResponse> call, Throwable t) {
//                DialogManager.hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });

    }

    public void callSendAPI(ChatSendReceiveInputEntity chatConnDisInputEntity, final BaseActivity baseActivity) {
//        DialogManager.showProgress(baseActivity);
        mServiceInterface.sendAPICall(chatConnDisInputEntity).enqueue(new Callback<ChatSendResponse>() {
            @Override
            public void onResponse(Call<ChatSendResponse> call, Response<ChatSendResponse> response) {
//                DialogManager.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    baseActivity.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<ChatSendResponse> call, Throwable t) {
//                DialogManager.hideProgress();
                baseActivity.onRequestFailure(t);
            }
        });

    }

    public void callSendAPI(ChatSendReceiveInputEntity chatConnDisInputEntity, final BaseFragment baseFragment) {
//        DialogManager.showProgress(baseActivity);
        mServiceInterface.sendAPICall(chatConnDisInputEntity).enqueue(new Callback<ChatSendResponse>() {
            @Override
            public void onResponse(Call<ChatSendResponse> call, Response<ChatSendResponse> response) {
//                DialogManager.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<ChatSendResponse> call, Throwable t) {
//                DialogManager.hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });

    }

    public void callReceiveAPI(ChatSendReceiveInputEntity chatConnDisInputEntity, final BaseActivity baseActivity) {
        mServiceInterface.receiveAPICall(chatConnDisInputEntity).enqueue(new Callback<ChatReceiveResponse>() {
            @Override
            public void onResponse(Call<ChatReceiveResponse> call, Response<ChatReceiveResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    baseActivity.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<ChatReceiveResponse> call, Throwable t) {
                baseActivity.onRequestFailure(t);
            }
        });

    }

    public void callReceiveAPI(ChatSendReceiveInputEntity chatConnDisInputEntity, final BaseFragment baseFragment) {
        mServiceInterface.receiveAPICall(chatConnDisInputEntity).enqueue(new Callback<ChatReceiveResponse>() {
            @Override
            public void onResponse(Call<ChatReceiveResponse> call, Response<ChatReceiveResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<ChatReceiveResponse> call, Throwable t) {
                baseFragment.onRequestFailure(t);
            }
        });

    }

    public void callMsgListAPI(ChatSendReceiveInputEntity chatConnDisInputEntity, final BaseFragment baseFragment) {
        mServiceInterface.callMsgAPICall(chatConnDisInputEntity).enqueue(new Callback<ChatReceiveResponse>() {
            @Override
            public void onResponse(Call<ChatReceiveResponse> call, Response<ChatReceiveResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<ChatReceiveResponse> call, Throwable t) {
                baseFragment.onRequestFailure(t);
            }
        });

    }

    public void callDisConnectAPI(ChatConnDisInputEntity chatConnDisInputEntity, final BaseActivity baseActivity) {
        DialogManager.showProgress(baseActivity);
        mServiceInterface.disConnectAPICall(chatConnDisInputEntity).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                DialogManager.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    baseActivity.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                DialogManager.hideProgress();
                baseActivity.onRequestFailure(t);
            }
        });

    }

    public void callDisConnectAPI(ChatConnDisInputEntity chatConnDisInputEntity, final BaseFragment baseFragment) {
        DialogManager.showProgress(baseFragment.getActivity());
        mServiceInterface.disConnectAPICall(chatConnDisInputEntity).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                DialogManager.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                DialogManager.hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });

    }

    public void callTrendsAPI(ChatConnDisInputEntity trendsInputEntityRes, final BaseFragment baseFragment) {
        DialogManager.showProgress(baseFragment.getActivity());
        mServiceInterface.trendsAPICall(trendsInputEntityRes).enqueue(new Callback<TrendsResponse>() {
            @Override
            public void onResponse(Call<TrendsResponse> call, Response<TrendsResponse> response) {
                DialogManager.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<TrendsResponse> call, Throwable t) {
                DialogManager.hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });

    }

    public void callDiscoverAPI(ChatConnDisInputEntity discoverInputEntityRes, final BaseFragment baseFragment) {
        mServiceInterface.discoverAPICall(discoverInputEntityRes).enqueue(new Callback<DiscoverResponse>() {
            @Override
            public void onResponse(Call<DiscoverResponse> call, Response<DiscoverResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<DiscoverResponse> call, Throwable t) {
                baseFragment.onRequestFailure(t);
            }
        });

    }


    public void callFriendsAndUserListAPI(ChatConnDisInputEntity fiendsInputEntityRes, final BaseFragment baseFragment) {
//        DialogManager.showProgress(baseFragment.getActivity());
        mServiceInterface.friendsAPICall(fiendsInputEntityRes).enqueue(new Callback<FriendsListResponse>() {
            @Override
            public void onResponse(Call<FriendsListResponse> call, Response<FriendsListResponse> response) {
//                DialogManager.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<FriendsListResponse> call, Throwable t) {
//                DialogManager.hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });

    }

    public void callAddFriendAPI(ChatSendReceiveInputEntity fiendsInputEntityRes, final BaseFragment baseFragment) {
        DialogManager.showProgress(baseFragment.getActivity());
        mServiceInterface.addFriendAPICall(fiendsInputEntityRes).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                DialogManager.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                DialogManager.hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });

    }

    public void callAddContactsFriendAPI(LoginRegResetInputEntity fiendsInputEntityRes, final BaseFragment baseFragment) {
        DialogManager.showProgress(baseFragment.getActivity());
        mServiceInterface.inviteContactsFriendAPICall(fiendsInputEntityRes).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                DialogManager.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                DialogManager.hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });

    }

    public void callGetUserAddressAPI(String urlStr, final BaseFragment baseFragment) {
        DialogManager.showProgress(baseFragment.getActivity());
        mServiceInterface.getUserAddressAPICall(urlStr).enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                DialogManager.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {
                DialogManager.hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });

    }

    public void callGetUserAddressAPI(String urlStr, final BaseActivity baseActivity) {
        mServiceInterface.getUserAddressAPICall(urlStr).enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {
            }
        });

    }


    public void callUpdateUserNameAPI(LoginRegResetInputEntity loginInput, final BaseFragment baseActivity) {
        DialogManager.showProgress(baseActivity.getActivity());
        mServiceInterface.updateUserNameAPICall(loginInput).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                DialogManager.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    baseActivity.onRequestFailure(new Throwable(response.raw().message()));
                }

            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                DialogManager.hideProgress();
                baseActivity.onRequestFailure(t);

            }
        });

    }

    public void callNotificationAPI(ChatConnDisInputEntity loginInput, final BaseFragment baseActivity) {
        DialogManager.showProgress(baseActivity.getActivity());
        mServiceInterface.getNotificationAPICall(loginInput).enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                DialogManager.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    baseActivity.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                DialogManager.hideProgress();
                baseActivity.onRequestFailure(t);

            }
        });

    }

    public void callReadNotificationAPI(NotificationInputEntity loginInput, final BaseFragment baseActivity) {
        DialogManager.showProgress(baseActivity.getActivity());
        mServiceInterface.getReadNotificationAPICall(loginInput).enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                DialogManager.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    baseActivity.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<CommonModel> call, Throwable t) {
                DialogManager.hideProgress();
                baseActivity.onRequestFailure(t);

            }
        });

    }

    public void callUpdateProfileAPI(LoginRegResetInputEntity loginInput, final BaseFragment baseFragment) {
        DialogManager.showProgress(baseFragment.getActivity());
        mServiceInterface.signinAPICall(loginInput).enqueue(new Callback<UserDetailsResponse>() {
            @Override
            public void onResponse(Call<UserDetailsResponse> call, Response<UserDetailsResponse> response) {
                DialogManager.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }

            }

            @Override
            public void onFailure(Call<UserDetailsResponse> call, Throwable t) {
                DialogManager.hideProgress();
                baseFragment.onRequestFailure(t);

            }
        });

    }

    public void callSettingsAPI(SettingsInputEntity loginInput, final BaseActivity baseActivity) {
        DialogManager.showProgress(baseActivity);
        mServiceInterface.settingsAPICall(loginInput).enqueue(new Callback<UserDetailsResponse>() {
            @Override
            public void onResponse(Call<UserDetailsResponse> call, Response<UserDetailsResponse> response) {
                DialogManager.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    baseActivity.onRequestFailure(new Throwable(response.raw().message()));
                }

            }

            @Override
            public void onFailure(Call<UserDetailsResponse> call, Throwable t) {
                DialogManager.hideProgress();
                baseActivity.onRequestFailure(t);

            }
        });

    }

    public void callDeleteMsgAPI(NotificationInputEntity deleteInput, final BaseFragment baseFragment) {
        DialogManager.showProgress(baseFragment.getActivity());
        mServiceInterface.deleteMsgAPICall(deleteInput).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                DialogManager.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }

            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                DialogManager.hideProgress();
                baseFragment.onRequestFailure(t);

            }
        });

    }
}
