package com.fautus.fautusapp.services;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import com.fautus.fautusapp.R;
import com.fautus.fautusapp.main.BaseActivity;
import com.fautus.fautusapp.main.BaseFragment;
import com.fautus.fautusapp.model.EmailValidationResponse;
import com.fautus.fautusapp.model.RetrieveStripeCustomerResponse;
import com.fautus.fautusapp.model.SendEmailResponse;
import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.DialogManager;
import com.fautus.fautusapp.utils.InterfaceBtnCallback;
import com.fautus.fautusapp.utils.NetworkUtil;
import com.fautus.fautusapp.utils.ParseAPIConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.fautus.fautusapp.model.ErrorResponse;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIRequestHandler {

    private static APIRequestHandler sInstance = new APIRequestHandler();

    /*Init retrofit for API call*/
    private Retrofit retrofit = new Retrofit.Builder().baseUrl(AppConstants.PARSE_DOMAIN).
            addConverterFactory(GsonConverterFactory.create()).build();
    private APICommonInterface mServiceInterface = retrofit.create(APICommonInterface.class);


    public static APIRequestHandler getInstance() {
        return sInstance;
    }

    /*Check given mail is valid or not*/
    public void callEmailValidAPI(final String emailStr, final String pwdStr, final boolean isPhotographerBool, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.emailValidAPICall(String.format(AppConstants.API_EMAIL_CHECK, emailStr)).enqueue(new Callback<EmailValidationResponse>() {
            @Override
            public void onResponse(Call<EmailValidationResponse> call, Response<EmailValidationResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().is_valid()) {
                        if (NetworkUtil.isNetworkAvailable(baseActivity)) {
                            parseSignUpUser(emailStr, pwdStr, isPhotographerBool, baseActivity);
                        } else {
                            DialogManager.getInstance().showAlertPopup(baseActivity, baseActivity.getString(R.string.app_name), baseActivity.getString(R.string.no_internet), baseActivity);
                        }
                    } else {
                        DialogManager.getInstance().showAlertPopup(baseActivity, baseActivity.getString(R.string.invalid_email_topic), baseActivity.getString(R.string.invalid_email_msg), new InterfaceBtnCallback() {
                            @Override
                            public void onOkClick() {

                            }
                        });
                    }
                } else {
                    baseActivity.onRequestFailure(new Throwable(response.raw().message()));
                }

            }

            @Override
            public void onFailure(Call<EmailValidationResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(t);

            }
        });

    }

    /*Login API*/
    public void parseLoginUser(@NonNull String mEmail, @NonNull String mPwd, @NonNull final BaseActivity baseActivity) {

        DialogManager.getInstance().showProgress(baseActivity);
        ParseUser.logInInBackground(mEmail, mPwd,
                new LogInCallback() {
                    public void done(@Nullable ParseUser user, ParseException e) {
                        DialogManager.getInstance().hideProgress();
                        if (user != null) {
                            baseActivity.onParseSuccess(user);
                        } else {
                            baseActivity.onParseRequestFailure(e);

                        }
                    }
                });
    }

    /*SignUp API*/
    private void parseSignUpUser(@NonNull String email, @NonNull String pwd, boolean isPhotographerBool, @NonNull final BaseActivity baseActivity) {

        DialogManager.getInstance().showProgress(baseActivity);
        ParseUser signUpUser = new ParseUser();

        signUpUser.put(ParseAPIConstants.username, email);
        signUpUser.put(ParseAPIConstants.email, email);
        signUpUser.put(ParseAPIConstants.password, pwd);
        signUpUser.put(ParseAPIConstants.isPhotographer, isPhotographerBool);

        //TODO: hardcoding this to true until we implement email verification
        signUpUser.put(ParseAPIConstants.emailVerified, true);

        signUpUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                DialogManager.getInstance().hideProgress();
                if (e == null) {
                    baseActivity.onParseRequestSuccess();
                } else {
                    baseActivity.onParseRequestFailure(e);
                }

            }
        });
    }


    /*Forgot Password API*/
    public void parseForgetPassword(String mailIdStr, @NonNull final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        ParseUser.requestPasswordResetInBackground(mailIdStr,
                new RequestPasswordResetCallback() {

                    public void done(@Nullable ParseException e) {
                        DialogManager.getInstance().hideProgress();

                        if (e == null) {
                            baseActivity.onParseRequestSuccess();

                        } else {
                            baseActivity.onParseRequestFailure(e);

                        }
                    }
                });
    }

    /*Save background API*/
    public void paresSaveInBackground(@NonNull ParseObject mParseObj, @NonNull final BaseActivity baseActivity) {

        DialogManager.getInstance().showProgress(baseActivity);
        mParseObj.saveInBackground(new SaveCallback() {

            public void done(@Nullable ParseException e) {
                DialogManager.getInstance().hideProgress();
                if (e == null) {
                    baseActivity.onParseRequestSuccess();
                } else {
                    baseActivity.onParseRequestFailure(e);
                }
            }
        });
    }


    /*Save background API*/
    public void paresSaveInBackground(@NonNull ParseObject mParseObj, @NonNull final BaseFragment baseFragment) {

        DialogManager.getInstance().showProgress(baseFragment.getActivity());
        mParseObj.saveInBackground(new SaveCallback() {

            public void done(@Nullable ParseException e) {
                DialogManager.getInstance().hideProgress();
                if (e == null) {
                    baseFragment.onParseRequestSuccess();
                } else {
                    baseFragment.onParseRequestFailure(e);
                }
            }
        });
    }


    /*Find background API*/
    public void parseFindInBackgroundList(@NonNull ParseQuery<ParseObject> mParseQuery, @NonNull final BaseActivity baseActivity) {

        mParseQuery.findInBackground(new FindCallback<ParseObject>() {

            public void done(@Nullable List<ParseObject> mParseList, @Nullable ParseException e) {

                if (e == null) {
                    baseActivity.onParseSuccess(mParseList);
                } else {
                    baseActivity.onParseRequestFailure(e);
                }
            }
        });
    }

    /*Find background API*/
    public void parseFindInBackgroundList(@NonNull ParseQuery<ParseObject> mParseQuery, @NonNull final BaseFragment baseFragment) {

        DialogManager.getInstance().showProgress(baseFragment.getActivity());
        mParseQuery.findInBackground(new FindCallback<ParseObject>() {

            public void done(@Nullable List<ParseObject> mParseList, @Nullable ParseException e) {

                DialogManager.getInstance().hideProgress();
                if (e == null) {
                    baseFragment.onParseSuccess(mParseList);
                } else {
                    baseFragment.onParseRequestFailure(e);
                }
            }
        });
    }


    /*Find purchased count*/
    public void getPurchasedPhotoCount(ParseObject momentObj, @NonNull final BaseFragment baseFragment) {
        ParseQuery<ParseObject> photoCountQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Photo);
        photoCountQuery.whereEqualTo(ParseAPIConstants.purchased, true);
        photoCountQuery.whereEqualTo(ParseAPIConstants.moment, momentObj);
        photoCountQuery.countInBackground(new CountCallback() {
            @Override
            public void done(int count, ParseException e) {
                if (e == null) {
                    baseFragment.onParseSuccess(count);
                } else {
                    baseFragment.onParseRequestFailure(e);
                }
            }
        });
    }

    /*get all moment photos*/
    public void getAllPhotos(ParseObject momentObj, @NonNull final BaseFragment baseFragment) {
        ParseQuery<ParseObject> photoQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Photo);
        photoQuery.whereEqualTo(ParseAPIConstants.moment, momentObj);
        photoQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    baseFragment.onParseSuccess(objects);
                } else {
                    baseFragment.onParseRequestFailure(e);
                }
            }
        });

    }

//    /*Check given mail is valid or not*/
//    public void callSendEmailAPI(final String emailStr, final String subStr, final String msgStr, final BaseFragment baseFragment) {
//        DialogManager.getInstance().showProgress(baseFragment.getActivity());
//        mServiceInterface.emailSendAPICall( Uri.encode(AppConstants.MAIL_GUN_MAIL_URL), AppConstants.MAIL_GUN_FROM, emailStr, subStr, msgStr).enqueue(new Callback<SendEmailResponse>() {
//            @Override
//            public void onResponse(Call<SendEmailResponse> call, Response<SendEmailResponse> response) {
//                DialogManager.getInstance().hideProgress();
//                if (response.isSuccessful() && response.body() != null) {
//                    baseFragment.onRequestSuccess(response.body());
//                } else {
//                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<SendEmailResponse> call, Throwable t) {
//                DialogManager.getInstance().hideProgress();
//                baseFragment.onRequestFailure(t);
//
//            }
//        });
//    }

    /*Check given mail is valid or not*/
    public void callSendEmailAPI(final String emailStr, final String subStr, final String msgStr, final BaseFragment baseFragment) {
        DialogManager.getInstance().showProgress(baseFragment.getActivity());


        String clientIdAndSecret = "api" + ":" + AppConstants.MAIL_GUN_KEY;
        String authorizationHeader = AppConstants.BASIC + " " + Base64.encodeToString(clientIdAndSecret.getBytes(), Base64.NO_WRAP);


        mServiceInterface.emailSendAPICall(authorizationHeader, AppConstants.MAIL_GUN_MAIL_URL, AppConstants.MAIL_GUN_FROM, emailStr, subStr, msgStr).enqueue(new Callback<SendEmailResponse>() {
            @Override
            public void onResponse(Call<SendEmailResponse> call, Response<SendEmailResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }

            }

            @Override
            public void onFailure(Call<SendEmailResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseFragment.onRequestFailure(t);

            }
        });
    }

    /*Check given mail is valid or not*/
    public void stripCardAPICall(final String emailStr, final BaseFragment baseFragment) {
        DialogManager.getInstance().showProgress(baseFragment.getActivity());
        String headerStr = AppConstants.BEARER + " " + AppConstants.STRIP_SECRET_KEY;
        String urlStr = String.format(AppConstants.API_STRIP_CARD, emailStr);
        mServiceInterface.stripCardAPICall(headerStr, urlStr).enqueue(new Callback<RetrieveStripeCustomerResponse>() {
            @Override
            public void onResponse(Call<RetrieveStripeCustomerResponse> call, Response<RetrieveStripeCustomerResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                }else if(response.errorBody()!=null){
                    Gson gson = new GsonBuilder().create();
                    ErrorResponse mError = new ErrorResponse();
                    try {
                        mError = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                        baseFragment.onRequestFailure(new Throwable(mError.getError().getMessage()));

                    } catch (IOException e) {
                        Log.e(AppConstants.TAG,e.getMessage());
                        baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                    }

                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<RetrieveStripeCustomerResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseFragment.onRequestFailure(t);

            }
        });
    }

}
