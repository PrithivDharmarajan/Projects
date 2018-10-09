package com.smaat.virtualtrainer.apiinterface;


import android.util.Log;

import com.smaat.virtualtrainer.R;
import com.smaat.virtualtrainer.main.BaseActivity;
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
import com.smaat.virtualtrainer.utils.AppConstants;
import com.smaat.virtualtrainer.utils.DialogManager;
import com.smaat.virtualtrainer.utils.GlobalMethods;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIRequestHandler {

    private static final APIRequestHandler sInstance = new APIRequestHandler();
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private APICommonInterface mServiceInterface = retrofit.create(APICommonInterface.class);

    public static APIRequestHandler getInstance() {
        return sInstance;
    }


    //Signup API Call
    public void signupAPICall(String userNameStr, String emailIdStr, String passwordStr, String loginTypeStr,
                              String facebookIdStr, String googleIdStr, String linkedinIdStr,
                              final BaseActivity baseActivity) {

        DialogManager.showProgress(baseActivity);

        Call<SignUpEntity> signUpAPIRes = mServiceInterface.signupAPICall(userNameStr, emailIdStr, passwordStr,
                loginTypeStr, facebookIdStr, googleIdStr, linkedinIdStr,
                GlobalMethods.getStringValue(baseActivity, AppConstants.DEVICE_ID),
                baseActivity.getString(R.string.two));

        signUpAPIRes.enqueue(new Callback<SignUpEntity>() {

            @Override
            public void onResponse(Call<SignUpEntity> call, Response<SignUpEntity> response) {
                DialogManager.hideProgress();
                if (response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                }

            }

            @Override
            public void onFailure(Call<SignUpEntity> call, Throwable throwableMsg) {
                DialogManager.hideProgress();
                baseActivity.onRequestFailure(throwableMsg);

            }
        });
    }

    //Signin API Call
    public void signinAPICall(String emailIdStr, String passwordStr, String loginTypeStr, String facebookIdStr,
                              String googleIdStr, String linkedinIdStr, final BaseActivity baseActivity) {
        DialogManager.showProgress(baseActivity);
        Call<SignInEntity> signInAPIRes = mServiceInterface.signinAPICall(emailIdStr, passwordStr, loginTypeStr,
                facebookIdStr, googleIdStr, linkedinIdStr,
                GlobalMethods.getStringValue(baseActivity, AppConstants.DEVICE_ID), baseActivity.getString(R.string.two));

        signInAPIRes.enqueue(new Callback<SignInEntity>() {

            @Override
            public void onResponse(Call<SignInEntity> call, Response<SignInEntity> response) {
                DialogManager.hideProgress();
                if (response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<SignInEntity> call, Throwable throwableMsg) {
                DialogManager.hideProgress();
                baseActivity.onRequestFailure(throwableMsg);
            }
        });
    }

    //ResetPwd API Call
    public void resetPwdAPICall(String emailIdStr, final BaseActivity baseActivity) {
        DialogManager.showProgress(baseActivity);
        Call<ResetPwdEntity> resetPwdRes = mServiceInterface.resetPwdAPICall(emailIdStr);
        resetPwdRes.enqueue(new Callback<ResetPwdEntity>() {
            @Override
            public void onResponse(Call<ResetPwdEntity> call, Response<ResetPwdEntity> response) {
                DialogManager.hideProgress();
                if (response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<ResetPwdEntity> call, Throwable throwableMsg) {
                DialogManager.hideProgress();
                baseActivity.onRequestFailure(throwableMsg);
            }
        });

    }

    //UserAccType API Call
    public void userAccTypeAPICall(String accTypeStr, final BaseActivity baseActivity) {
        DialogManager.showProgress(baseActivity);
        Log.d("UserID", GlobalMethods.getUserID(baseActivity));
        Call<UserAccTypeEntity> userAccTypeRes = mServiceInterface.userAccTypeAPICall(GlobalMethods.getUserID(baseActivity), accTypeStr);
        userAccTypeRes.enqueue(new Callback<UserAccTypeEntity>() {
            @Override
            public void onResponse(Call<UserAccTypeEntity> call, Response<UserAccTypeEntity> response) {
                DialogManager.hideProgress();
                if (response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<UserAccTypeEntity> call, Throwable throwableMsg) {
                DialogManager.hideProgress();
                baseActivity.onRequestFailure(throwableMsg);
            }
        });

    }

    //CreateStreaming API Call
    public void createStreamingAPICall(final BaseActivity baseActivity) {
        DialogManager.showProgress(baseActivity);
        Log.d("UserID", GlobalMethods.getUserID(baseActivity));
        Call<CreateStreamingEntity> createStreamingRes = mServiceInterface.createStreamingAPICall(GlobalMethods.getUserID(baseActivity));
        createStreamingRes.enqueue(new Callback<CreateStreamingEntity>() {
            @Override
            public void onResponse(Call<CreateStreamingEntity> call, Response<CreateStreamingEntity> response) {
                DialogManager.hideProgress();
                if (response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<CreateStreamingEntity> call, Throwable throwableMsg) {
                DialogManager.hideProgress();
                baseActivity.onRequestFailure(throwableMsg);
            }
        });

    }

    //UserList API Call
    public void userListAPICall(final BaseActivity baseActivity) {
        DialogManager.showProgress(baseActivity);
        Log.d("UserID", GlobalMethods.getUserID(baseActivity));
        Call<UserListEntity> userListRes = mServiceInterface.userListAPICall(GlobalMethods.getUserID(baseActivity));
        userListRes.enqueue(new Callback<UserListEntity>() {
            @Override
            public void onResponse(Call<UserListEntity> call, Response<UserListEntity> response) {
                DialogManager.hideProgress();
                if (response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<UserListEntity> call, Throwable throwableMsg) {
                DialogManager.hideProgress();
                baseActivity.onRequestFailure(throwableMsg);
            }
        });

    }


    //inviteUser API Call
    public void inviteUserListAPICall(String streamNameStr, String streamIdStr, String inviteUserIdsStr, final BaseActivity baseActivity) {
        DialogManager.showProgress(baseActivity);
        Call<InviteUserListEntity> inviteUserListRes = mServiceInterface.inviteUserListAPICall(GlobalMethods.getUserID(baseActivity),
                streamNameStr, streamIdStr, inviteUserIdsStr);
        inviteUserListRes.enqueue(new Callback<InviteUserListEntity>() {
            @Override
            public void onResponse(Call<InviteUserListEntity> call, Response<InviteUserListEntity> response) {
                DialogManager.hideProgress();
                if (response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<InviteUserListEntity> call, Throwable throwableMsg) {
                DialogManager.hideProgress();
                baseActivity.onRequestFailure(throwableMsg);
            }
        });

    }

    //Update Profile API Call
    public void updateProfileAPICall(String userNameStr, String passwordStr, String loginTypeStr,
                                     final BaseActivity baseActivity) {

        DialogManager.showProgress(baseActivity);

        Call<UpdateProfileEntity> updateProfileAPIRes = mServiceInterface.updateProfileAPICall(GlobalMethods.getUserID(baseActivity), userNameStr, passwordStr,
                loginTypeStr,
                GlobalMethods.getStringValue(baseActivity, AppConstants.DEVICE_ID),
                baseActivity.getString(R.string.two));
        updateProfileAPIRes.enqueue(new Callback<UpdateProfileEntity>() {

            @Override
            public void onResponse(Call<UpdateProfileEntity> call, Response<UpdateProfileEntity> response) {
                DialogManager.hideProgress();
                if (response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                }

            }

            @Override
            public void onFailure(Call<UpdateProfileEntity> call, Throwable throwableMsg) {
                DialogManager.hideProgress();
                baseActivity.onRequestFailure(throwableMsg);

            }
        });
    }

    //Contact Support Profile API Call
    public void contactSupportAPICall(String emailIdStr, String msgStr,
                                      final BaseActivity baseActivity) {

        DialogManager.showProgress(baseActivity);

        Call<ContactSupportEntity> contactSupportAPIRes = mServiceInterface.contactSupportAPICall(GlobalMethods.getUserID(baseActivity), emailIdStr, msgStr);
        contactSupportAPIRes.enqueue(new Callback<ContactSupportEntity>() {

            @Override
            public void onResponse(Call<ContactSupportEntity> call, Response<ContactSupportEntity> response) {
                DialogManager.hideProgress();
                if (response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                }

            }

            @Override
            public void onFailure(Call<ContactSupportEntity> call, Throwable throwableMsg) {
                DialogManager.hideProgress();
                baseActivity.onRequestFailure(throwableMsg);

            }
        });
    }

    //Join as Guest API Call
    public void joinAsGuestAPICall(String guestNameStr, String guestIdStr,
                                   final BaseActivity baseActivity) {

        DialogManager.showProgress(baseActivity);

        Call<JoinStreamingEntity> joinAsGuestAPIRes = mServiceInterface.joinAsGuestAPICall(guestNameStr, guestIdStr);
        joinAsGuestAPIRes.enqueue(new Callback<JoinStreamingEntity>() {

            @Override
            public void onResponse(Call<JoinStreamingEntity> call, Response<JoinStreamingEntity> response) {
                DialogManager.hideProgress();
                if (response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                }

            }

            @Override
            public void onFailure(Call<JoinStreamingEntity> call, Throwable throwableMsg) {
                DialogManager.hideProgress();
                baseActivity.onRequestFailure(throwableMsg);

            }
        });
    }

    //Start Streaming API Call
    public void startStreamingAPICall(String streamingNameStr, String streamingIDStr, String streamingStatusStr,
                                      final BaseActivity baseActivity) {

        DialogManager.showProgress(baseActivity);

        Call<CommonModelEntity> startStreamingAPIRes = mServiceInterface.startStreamingAPICall(streamingNameStr,
                GlobalMethods.getUserID(baseActivity), streamingIDStr, streamingStatusStr);
        startStreamingAPIRes.enqueue(new Callback<CommonModelEntity>() {

            @Override
            public void onResponse(Call<CommonModelEntity> call, Response<CommonModelEntity> response) {
                DialogManager.hideProgress();
                if (response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                }

            }

            @Override
            public void onFailure(Call<CommonModelEntity> call, Throwable throwableMsg) {
                DialogManager.hideProgress();
                baseActivity.onRequestFailure(throwableMsg);

            }
        });
    }

    //Join Streaming API Call
    public void joinStreamingAPICall(String streamingIDStr, String streamingStatusStr,
                                     final BaseActivity baseActivity) {

        DialogManager.showProgress(baseActivity);

        Call<JoinStreamingEntity> joinStreamingAPIRes = mServiceInterface.joinStreamingAPICall(GlobalMethods.getUserID(baseActivity), streamingIDStr, streamingStatusStr);
        joinStreamingAPIRes.enqueue(new Callback<JoinStreamingEntity>() {

            @Override
            public void onResponse(Call<JoinStreamingEntity> call, Response<JoinStreamingEntity> response) {
                DialogManager.hideProgress();
                if (response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                }

            }

            @Override
            public void onFailure(Call<JoinStreamingEntity> call, Throwable throwableMsg) {
                DialogManager.hideProgress();
                baseActivity.onRequestFailure(throwableMsg);

            }
        });
    }

    //Streaming API Call
    public void streamingAPICall(final BaseActivity baseActivity) {

        //No need loading sym
        Call<StreamingEntity> joinStreamingAPIRes = mServiceInterface.streamingAPICall("4");
//                GlobalMethods.getUserID(baseActivity));
        joinStreamingAPIRes.enqueue(new Callback<StreamingEntity>() {

            @Override
            public void onResponse(Call<StreamingEntity> call, Response<StreamingEntity> response) {

                if (response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                }

            }

            @Override
            public void onFailure(Call<StreamingEntity> call, Throwable throwableMsg) {

                //no need failure Case and loading sym bz every 5 sec that api is called

            }
        });
    }

}
