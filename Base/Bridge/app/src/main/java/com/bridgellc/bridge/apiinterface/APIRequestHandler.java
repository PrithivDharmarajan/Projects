package com.bridgellc.bridge.apiinterface;

import com.bridgellc.bridge.entity.CardResponse;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.main.BaseFragment;
import com.bridgellc.bridge.main.BaseFragmentActivity;
import com.bridgellc.bridge.model.AcceptBiddingResponse;
import com.bridgellc.bridge.model.BankAccDetailsResponse;
import com.bridgellc.bridge.model.BankAddAccountResponse;
import com.bridgellc.bridge.model.BiddingsReqResponse;
import com.bridgellc.bridge.model.ChatResponse;
import com.bridgellc.bridge.model.CommonModelResponse;
import com.bridgellc.bridge.model.CommonPhResponse;
import com.bridgellc.bridge.model.CommonResponse;
import com.bridgellc.bridge.model.FinishServicesResponse;
import com.bridgellc.bridge.model.HomeResponse;
import com.bridgellc.bridge.model.ItemDetailResponse;
import com.bridgellc.bridge.model.ItemEditableResponse;
import com.bridgellc.bridge.model.NegotiateResponse;
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
import com.bridgellc.bridge.ui.upload.UploadEntityClass;
import com.bridgellc.bridge.ui.upload.UploadScreen;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.GlobalMethods;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.mime.TypedFile;


public class APIRequestHandler {


    private static final APIRequestHandler mInstance = new APIRequestHandler();


    RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(AppConstants.BASE_URL)
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .setClient(new OkClient(getOkHttpClient()))
            .build();


    APICommonInterface mInterfaces = restAdapter
            .create(APICommonInterface.class);




    private APIRequestHandler() {

    }

    private OkHttpClient getOkHttpClient() {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(50, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(50, TimeUnit.SECONDS);
        return okHttpClient;
    }

    public static APIRequestHandler getInstance() {
        return mInstance;
    }

    public void getSignInResponse(String mEmailId, String mPassword, String mDeviceToken, String
            mLogincount, final
                                  BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getSignInResponse(mEmailId, mPassword, AppConstants.mDEVICEPLATFORM,
                mDeviceToken, mLogincount, new Callback<SignInResponse>() {

                    @Override
                    public void success(SignInResponse mSignInResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mSignInResponse != null) {
                            Object obj = mSignInResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });

    }

    public void getSignUpResponse(String mFirstName, String mLastName, String mEmailId, String
            mPhoneNumber, String mPwd, String mUniversityId, String mDOB, String mGender, String
                                          mLoginType, String mDeviceToken, String mSocialId, String mPaymentMode, final BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getSignUpResponse(mFirstName, mLastName, mEmailId, mPhoneNumber, mPwd,
                mUniversityId, mDOB, mGender, mLoginType, AppConstants.mDEVICEPLATFORM,
                mDeviceToken, mSocialId, mPaymentMode, new Callback<SignUpResponse>() {
                    @Override
                    public void success(SignUpResponse signUpResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (signUpResponse != null) {
                            Object obj = signUpResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }

    public void getUpdateProfileResponse(String mFirstName, String mLastName, String mEmailId,
                                         String mEduEmailId,
                                         String mPhoneNumber, String mPwd, String mUniversityId,
                                         String mDOB, String mGender, String mLoginType, String mSocialId, String mPaymentMode, final
                                         BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getUpdateProfileResponse(GlobalMethods.getUserID(mActivity), mFirstName,
                mLastName, mEmailId, mEduEmailId, mPhoneNumber, mPwd,
                mUniversityId, mDOB, mGender, mLoginType, mSocialId, mPaymentMode, new Callback<SignUpResponse>() {
                    @Override
                    public void success(SignUpResponse signUpResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (signUpResponse != null) {
                            Object obj = signUpResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }

    public void getUniversityResponse(final BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getUniversityResponse(AppConstants.mDEVICEPLATFORM, new
                Callback<UniversityResponse>() {
                    @Override
                    public void success(UniversityResponse universityResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (universityResponse != null) {
                            Object obj = universityResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }

    public void getResetPwdResponse(String mUserName, String mEmailId, final BaseActivity
            mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getResetPwdResponse(mUserName, mEmailId, new Callback<CommonResponse>() {
            @Override
            public void success(CommonResponse resetPwdResponse,
                                Response arg1) {
                DialogManager.hideProgress(mActivity);
                if (resetPwdResponse != null) {
                    Object obj = resetPwdResponse;
                    mActivity.onRequestSuccess(obj);
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                DialogManager.hideProgress(mActivity);
                mActivity.onRequestFailure(retrofitError);
            }
        });
    }

    public void getUpdatePhNumResponse(String mPhonenumber, final BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getUpdatePhNumResponse(GlobalMethods.getUserID(mActivity), mPhonenumber, new
                Callback<UpdatePhNumResponse>() {
                    @Override
                    public void success(UpdatePhNumResponse resetPwdResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (resetPwdResponse != null) {
                            Object obj = resetPwdResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }

    public void getUpdateMailResponse(String mEmailId, final BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getUpdateMailResponse(GlobalMethods.getUserID(mActivity), mEmailId, new
                Callback<CommonResponse>() {
                    @Override
                    public void success(CommonResponse resetPwdResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (resetPwdResponse != null) {
                            Object obj = resetPwdResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }


    public void getHomeResponse(String limit, String offset, final BaseFragmentActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getHomeResponse(GlobalMethods.getUserID(mActivity), limit, offset, AppConstants.mDEVICEPLATFORM, GlobalMethods.getStringValue(mActivity, AppConstants.DEVICE_ID), GlobalMethods.getStringValue(mActivity, AppConstants.UNIVERSITY_MODE),
                new Callback<HomeResponse>() {

                    @Override
                    public void success(HomeResponse mSignInResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mSignInResponse != null) {
                            Object obj = mSignInResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });

    }


    public void getSearchResponse(String limit, String offset, String searchKey, final
    BaseFragmentActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getSearchResponse(GlobalMethods.getUserID(mActivity), limit, offset, searchKey,
                new Callback<HomeResponse>() {

                    @Override
                    public void success(HomeResponse mSignInResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mSignInResponse != null) {
                            Object obj = mSignInResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });

    }


    public String getStringFromBoolean(boolean b) {
        return b ? "1" : "2";
    }


    public void uploadPhotosResponse(UploadEntityClass mUploadEntityClass, final BaseFragment
            mActivity) {
        DialogManager.showProgress(mActivity.getActivity());

        TypedFile photo1 = null, photo2 = null, photo3 = null;
        try {
            if (!mUploadEntityClass.imagePath1.isEmpty() && !mUploadEntityClass.imagePath1
                    .contains("http")) {
                photo1 = new TypedFile("multipart/form-data", new File(
                        mUploadEntityClass.imagePath1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (!mUploadEntityClass.imagePath2.isEmpty() && !mUploadEntityClass.imagePath2
                    .contains("http")) {
                photo2 = new TypedFile("multipart/form-data", new File(
                        mUploadEntityClass.imagePath2));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (!mUploadEntityClass.imagePath3.isEmpty() && !mUploadEntityClass.imagePath3
                    .contains("http")) {
                photo3 = new TypedFile("multipart/form-data", new File(
                        mUploadEntityClass.imagePath3));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        mInterfaces.uploadPhotosResponse(GlobalMethods.getUserID(mActivity.getActivity()),
                photo1, photo2, photo3, new Callback<UploadPictureResponse>() {

                    @Override
                    public void success(UploadPictureResponse commonResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity.getActivity());
                        if (commonResponse != null) {
                            Object obj = commonResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity.getActivity());
                        mActivity.onRequestFailure(retrofitError);
                    }
                });

    }

    public void uploadItemResponse(UploadEntityClass mUploadEntityClass, final
    BaseFragmentActivity mActivity) {
        DialogManager.showProgress(mActivity);

        if (mUploadEntityClass.quantity.length() == 0) {
            mUploadEntityClass.quantity = "1";
        }

        mInterfaces.uploadItemResponse(GlobalMethods.getUserID(mActivity), getStringFromBoolean
                        (mUploadEntityClass.isSelling), getStringFromBoolean(mUploadEntityClass.isGood),
                getStringFromBoolean(mUploadEntityClass.isDeliveryInPerson), mUploadEntityClass
                        .category, mUploadEntityClass.name, mUploadEntityClass.quantity,
                mUploadEntityClass.price, mUploadEntityClass.condition, "", mUploadEntityClass
                        .desc, "", "", "", "", mUploadEntityClass.dropBoxUrlPreview, mUploadEntityClass.dropBoxUrlOrginal, getStringFromBoolean
                        (mUploadEntityClass.isDropBoxUrl), mUploadEntityClass.imagePath1,
                mUploadEntityClass.imagePath2, mUploadEntityClass.imagePath3, mUploadEntityClass.event_date_time, mUploadEntityClass.venue, mUploadEntityClass.payment_mode, mUploadEntityClass.phone_number, mUploadEntityClass.website, new
                        Callback<CommonResponse>() {

                            @Override
                            public void success(CommonResponse commonResponse,
                                                Response arg1) {
                                DialogManager.hideProgress(mActivity);
                                if (commonResponse != null) {
                                    Object obj = commonResponse;
                                    mActivity.onRequestSuccess(obj);
                                }
                            }

                            @Override
                            public void failure(RetrofitError retrofitError) {
                                DialogManager.hideProgress(mActivity);
                                mActivity.onRequestFailure(retrofitError);
                            }
                        });


    }

    public void getChatResponse(String friend_id, String item_id, final BaseActivity mActivity) {
        mInterfaces.getChatResponse(GlobalMethods.getUserID(mActivity), friend_id, item_id,
                new Callback<ChatResponse>() {

                    @Override
                    public void success(ChatResponse mSignInResponse,
                                        Response arg1) {
                        if (mSignInResponse != null) {
                            Object obj = mSignInResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        mActivity.onRequestFailure(retrofitError);
                    }
                });

    }

    public void getChatSendResponse(String friend_id, String item_id, String chat_message, String payment_id, final
    BaseActivity mActivity) {
        mInterfaces.getChatSendResponse(GlobalMethods.getUserID(mActivity), friend_id, item_id,
                chat_message, payment_id,
                new Callback<CommonResponse>() {

                    @Override
                    public void success(CommonResponse mSignInResponse,
                                        Response arg1) {
                        if (mSignInResponse != null) {
                            Object obj = mSignInResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        mActivity.onRequestFailure(retrofitError);
                    }
                });

    }


    public void getNegotiationsResponse(String user_id, String friend_id, String item_id, String notification, final BaseActivity
            mActivity) {
        mInterfaces.getNegotiationsResponse(user_id, item_id, friend_id, notification,
                new Callback<NegotiateResponse>() {

                    @Override
                    public void success(NegotiateResponse mSignInResponse,
                                        Response arg1) {
                        if (mSignInResponse != null) {
                            Object obj = mSignInResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        mActivity.onRequestFailure(retrofitError);
                    }
                });

    }

    public void getNegotiateSendResponse(String friend_id, String item_id, String
            negotiate_amount, String quantity, String bid_id, final BaseActivity mActivity) {
        mInterfaces.getNegotiateSendResponse(GlobalMethods.getUserID(mActivity), friend_id,
                item_id, negotiate_amount, quantity, bid_id,
                new Callback<CommonResponse>() {

                    @Override
                    public void success(CommonResponse mSignInResponse,
                                        Response arg1) {
                        if (mSignInResponse != null) {
                            Object obj = mSignInResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        mActivity.onRequestFailure(retrofitError);
                    }
                });

    }

    public void getBidRequestResponse(String item_id, String
            bid_amount, String to_user_id, String quantity, String paymode, final BaseActivity mActivity) {

        DialogManager.showProgress(mActivity);
        mInterfaces.getBidRequestResponse(GlobalMethods.getUserID(mActivity), item_id, bid_amount, to_user_id, quantity, paymode,
                new Callback<CommonModelResponse>() {

                    @Override
                    public void success(CommonModelResponse mSignInResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mSignInResponse != null) {
                            Object obj = mSignInResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });

    }

    public void approveNegotiateResponse(String friend_id, String item_id, String
            negotiate_amount, final BaseActivity mActivity) {

        DialogManager.showProgress(mActivity);
        mInterfaces.approveNegotiateResponse(GlobalMethods.getUserID(mActivity), friend_id,
                item_id, negotiate_amount,
                new Callback<CommonResponse>() {

                    @Override
                    public void success(CommonResponse mSignInResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mSignInResponse != null) {
                            Object obj = mSignInResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });

    }


    public void getMyProfileResponse(final BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getMyProfileResponse(GlobalMethods.getUserID(mActivity),
                new Callback<ProfileResponse>() {

                    @Override
                    public void success(ProfileResponse mProfileResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mProfileResponse != null) {
                            Object obj = mProfileResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });

    }

    public void getOtherProfileResponse(String mOtherUserId, final BaseFragmentActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getOtherProfileResponse(GlobalMethods.getUserID(mActivity), mOtherUserId,
                new Callback<ProfileResponse>() {

                    @Override
                    public void success(ProfileResponse mOtherProfileResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mOtherProfileResponse != null) {
                            Object obj = mOtherProfileResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });

    }

    public void getMyBiddingsReqResponse(String item_id, final BaseActivity mActivity) {

        mInterfaces.getMyBiddingsReqResponse(GlobalMethods.getUserID(mActivity), item_id,
                new Callback<BiddingsReqResponse>() {

                    @Override
                    public void success(BiddingsReqResponse mBiddingsResponse,
                                        Response arg1) {
                        if (mBiddingsResponse != null) {
                            Object obj = mBiddingsResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        mActivity.onRequestFailure(retrofitError);
                    }
                });

    }

    public void getAcceptBiddingResponse(String item_id, String bid_amount, String bid_id, String request_user_id, final BaseActivity mActivity) {

        mInterfaces.getAcceptBiddingResponse(GlobalMethods.getUserID(mActivity), item_id, bid_amount, bid_id, request_user_id,
                new Callback<AcceptBiddingResponse>() {

                    @Override
                    public void success(AcceptBiddingResponse mBiddingsResponse,
                                        Response arg1) {
                        if (mBiddingsResponse != null) {
                            Object obj = mBiddingsResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        mActivity.onRequestFailure(retrofitError);
                    }
                });

    }


    public void postRating(String rate_user_id, String rating, String comments, String item_id,
                           final
                           BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);

        mInterfaces.postRating(GlobalMethods.getUserID(mActivity), rate_user_id, rating,
                comments, item_id, new Callback<CommonResponse>() {
                    @Override
                    public void success(CommonResponse commonResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (commonResponse != null) {
                            Object obj = commonResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }

    public void getRatings(String userId, final BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);

        mInterfaces.getViewRating(userId, new
                Callback<ReviewResponce>() {

                    @Override
                    public void success(ReviewResponce commonResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (commonResponse != null) {
                            Object obj = commonResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });

    }

//    public void buyItemResponse(String buyer_id, String item_id, String item_quantity, String neg_Id, String tips, final BaseActivity
//            mActivity) {
//        DialogManager.showProgress(mActivity);
//        mInterfaces.buyItemResponse(buyer_id, item_id, item_quantity, neg_Id, tips,
//                new Callback<BuyItemResponse>() {
//
//                    @Override
//                    public void success(BuyItemResponse mSignInResponse,
//                                        Response arg1) {
//                        DialogManager.hideProgress(mActivity);
//                        if (mSignInResponse != null) {
//                            Object obj = mSignInResponse;
//                            mActivity.onRequestSuccess(obj);
//                        }
//                    }
//
//                    @Override
//                    public void failure(RetrofitError retrofitError) {
//                        DialogManager.hideProgress(mActivity);
//                        mActivity.onRequestFailure(retrofitError);
//                    }
//                });
//
//    }


//    public void getPaypalPaymentResponse(String item_id, String item_quantity, String item_cost,
//                                         String pay_id, String process_fee, String total_cost,
//                                         String is_nagotiation, String negotiate_id, String tips, String user_name,
//                                         String cardnumber,
//                                         String exp_month,
//                                         String exp_year,
//                                         String cvc, final BaseActivity
//                                                 mActivity) {
//        DialogManager.showProgress(mActivity);
//        mInterfaces.getPaypalPaymentResponse(GlobalMethods.getUserID(mActivity), item_id, item_quantity, item_cost,
//                pay_id, process_fee, total_cost, is_nagotiation, negotiate_id, tips, user_name,
//                cardnumber, exp_month, exp_year, cvc,
//                new Callback<PaypalPayResponse>() {
//
//                    @Override
//                    public void success(PaypalPayResponse mPaypalResponse,
//                                        Response arg1) {
//                        DialogManager.hideProgress(mActivity);
//                        if (mPaypalResponse != null) {
//                            Object obj = mPaypalResponse;
//                            mActivity.onRequestSuccess(obj);
//                        }
//                    }
//
//                    @Override
//                    public void failure(RetrofitError retrofitError) {
//                        DialogManager.hideProgress(mActivity);
//                        mActivity.onRequestFailure(retrofitError);
//                    }
//                });
//
//    }

    public void getPaypalPaymentResponse(String user_id, String item_id, String item_quantity, String item_cost,
                                         String pay_id, String process_fee, String total_cost,
                                         String is_nagotiation, String negotiate_id, String tips, String card_id,
                                         final BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getPaypalPaymentResponse(user_id, item_id, item_quantity, item_cost,
                pay_id, process_fee, total_cost, is_nagotiation, negotiate_id, tips, card_id,
                new Callback<PaypalPayResponse>() {

                    @Override
                    public void success(PaypalPayResponse mPaypalResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mPaypalResponse != null) {
                            Object obj = mPaypalResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });

    }

    public void getBidPaypalPaymentResponse(String item_id, String bid_amount, String bid_id,
                                            String request_user_id, String pay_id, String neg_id,
                                            String card_id, final BaseActivity
                                                    mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getBidPaypalPaymentResponse(GlobalMethods.getUserID(mActivity), item_id, bid_amount, bid_id,
                request_user_id, pay_id, neg_id, card_id,
                new Callback<PaypalPayResponse>() {

                    @Override
                    public void success(PaypalPayResponse mPaypalResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mPaypalResponse != null) {
                            Object obj = mPaypalResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });

    }

    public void getMySellingResponse(final BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getMySellingResponse(GlobalMethods.getUserID(mActivity),
//        mInterfaces.getMySellingResponse("1",
                new Callback<ProfileListResponse>() {

                    @Override
                    public void success(ProfileListResponse mProfileListResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mProfileListResponse != null) {
                            Object obj = mProfileListResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }


    public void getMyBuyingResponse(final BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getMyBuyingResponse(GlobalMethods.getUserID(mActivity),
//        mInterfaces.getMyBuyingResponse("1",
                new Callback<ProfileListResponse>() {

                    @Override
                    public void success(ProfileListResponse mProfileListResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mProfileListResponse != null) {
                            Object obj = mProfileListResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }


    public void getMyRequestResponse(final BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getMyRequestsResponse(GlobalMethods.getUserID(mActivity),
                new Callback<ProfileListResponse>() {

                    @Override
                    public void success(ProfileListResponse mProfileListResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mProfileListResponse != null) {
                            Object obj = mProfileListResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }

    public void getMyBiddingsResponse(final BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getMyBiddingsResponse(GlobalMethods.getUserID(mActivity),
                new Callback<ProfileListResponse>() {

                    @Override
                    public void success(ProfileListResponse mProfileListResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mProfileListResponse != null) {
                            Object obj = mProfileListResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }

    public void getMyPaymentHistoryResponse(final BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getMyPaymentHistoryResponse(GlobalMethods.getUserID(mActivity),
                new Callback<PaymentHistoryResponse>() {

                    @Override
                    public void success(PaymentHistoryResponse mProfileListResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mProfileListResponse != null) {
                            Object obj = mProfileListResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }


    public void getUnsatiesFactoryResponse(String itemId, String paymentId, final BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getUnsatiesFactoryResponse(GlobalMethods.getUserID(mActivity), itemId,
                paymentId,
                new Callback<CommonResponse>() {

                    @Override
                    public void success(CommonResponse mLogoutResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mLogoutResponse != null) {
                            Object obj = mLogoutResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }

    public void getStartedServicesResponse(String itemId, String buyerId, final BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getStartedServicesResponse(GlobalMethods.getUserID(mActivity), itemId,
                buyerId,
                new Callback<CommonResponse>() {

                    @Override
                    public void success(CommonResponse mStartResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mStartResponse != null) {
                            Object obj = mStartResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }

    public void getApprovePreviewSerResponse(String itemId, final BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getApprovePreviewSerResponse(GlobalMethods.getUserID(mActivity), itemId,
                new Callback<FinishServicesResponse>() {

                    @Override
                    public void success(FinishServicesResponse mApproveResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mApproveResponse != null) {
                            Object obj = mApproveResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }

    public void getVerifyNumResponse(String number, String otp, final BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getVerifyNumResponse(GlobalMethods.getUserID(mActivity), number, otp,
                new Callback<VerifyNumResponse>() {

                    @Override
                    public void success(VerifyNumResponse mResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mResponse != null) {
                            Object obj = mResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }

    public void getItemEditableResponse(String itemID, final BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getItemEditableResponse(itemID,
                new Callback<ItemEditableResponse>() {

                    @Override
                    public void success(ItemEditableResponse mResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mResponse != null) {
                            Object obj = mResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }


    public void getFinishServicesBuyResponse(String buyerId, String itemId, final BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);

        mInterfaces.getFinishServicesBuyResponse(GlobalMethods.getUserID(mActivity), itemId, buyerId
                , GlobalMethods.getStringValue(mActivity, AppConstants.PAYMENT_MODE),
                new Callback<FinishServicesResponse>() {

                    @Override
                    public void success(FinishServicesResponse mStartResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mStartResponse != null) {
                            Object obj = mStartResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }

    public void updateItemResponse(String itemId, UploadEntityClass mUploadEntityClass, final
    BaseFragmentActivity mActivity) {
        DialogManager.showProgress(mActivity);

        if (mUploadEntityClass.quantity.length() == 0) {
            mUploadEntityClass.quantity = "1";
        }
        if (UploadScreen.mEntity.phone_number == null) {
            UploadScreen.mEntity.phone_number = "";

        }
        if (UploadScreen.mEntity.website == null) {
            UploadScreen.mEntity.website = "";

        }

        mInterfaces.updateItemResponse(GlobalMethods.getUserID(mActivity), itemId,
                getStringFromBoolean
                        (mUploadEntityClass.isSelling), getStringFromBoolean(mUploadEntityClass.isGood),
                getStringFromBoolean(mUploadEntityClass.isDeliveryInPerson), mUploadEntityClass
                        .category, mUploadEntityClass.name, mUploadEntityClass.quantity,
                mUploadEntityClass.price, mUploadEntityClass.condition, "", mUploadEntityClass
                        .desc, "", "", "", "", mUploadEntityClass.dropBoxUrlPreview, mUploadEntityClass.dropBoxUrlOrginal, getStringFromBoolean
                        (mUploadEntityClass.isDropBoxUrl), mUploadEntityClass.imagePath1,
                mUploadEntityClass.imagePath2, mUploadEntityClass.imagePath3, mUploadEntityClass.event_date_time, mUploadEntityClass.venue, mUploadEntityClass.phone_number, mUploadEntityClass.website, new
                        Callback<CommonResponse>() {

                            @Override
                            public void success(CommonResponse commonResponse,
                                                Response arg1) {
                                DialogManager.hideProgress(mActivity);
                                if (commonResponse != null) {
                                    Object obj = commonResponse;
                                    mActivity.onRequestSuccess(obj);
                                }
                            }

                            @Override
                            public void failure(RetrofitError retrofitError) {
                                DialogManager.hideProgress(mActivity);
                                mActivity.onRequestFailure(retrofitError);
                            }
                        });


    }

    public void getUploadFileResponse(String mBuyerId, String mItemId, String mFileUrl, String mFileUrlType, String mFileStatus, String mPaymentID, final BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getUploadFileSerResponse(
                GlobalMethods.getUserID(mActivity), mBuyerId, mItemId, mFileUrl, mFileUrlType, mFileStatus, mPaymentID, GlobalMethods.getStringValue(mActivity, AppConstants.PAYMENT_MODE),
                new Callback<CommonModelResponse>() {

                    @Override
                    public void success(CommonModelResponse mStartResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mStartResponse != null) {
                            Object obj = mStartResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }

    public void getDeleteItemResponse(String itemId, final BaseActivity
            mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getDeleteItemResponse(GlobalMethods.getUserID(mActivity), itemId,
                new Callback<CommonResponse>() {

                    @Override
                    public void success(CommonResponse mLogoutResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mLogoutResponse != null) {
                            Object obj = mLogoutResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }

    public void getNotificationResponse(final BaseActivity
                                                mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getNotificationResponse(GlobalMethods.getUserID(mActivity),
                new Callback<NotificationEntityResponse>() {

                    @Override
                    public void success(NotificationEntityResponse mNotificationResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mNotificationResponse != null) {
                            Object obj = mNotificationResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }

    public void getNotificationReadResponse(String mNotifyId, final BaseActivity
            mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getNotificationReadResponse(GlobalMethods.getUserID(mActivity), mNotifyId,
                new Callback<CommonResponse>() {

                    @Override
                    public void success(CommonResponse mNotificationResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mNotificationResponse != null) {
                            Object obj = mNotificationResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }

    public void deleteNotificationResponse(String notification_Id, String delete_Key, final BaseActivity
            mActivity) {
        mInterfaces.deleteNotificationResponse(GlobalMethods.getUserID(mActivity), notification_Id, delete_Key,
                new Callback<CommonModelResponse>() {

                    @Override
                    public void success(CommonModelResponse mNotificationResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mNotificationResponse != null) {
                            Object obj = mNotificationResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }

    public void finishBuyResponse(String itemId, final BaseActivity
            mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.finishBuyResponse(GlobalMethods.getUserID(mActivity), itemId,
                new Callback<PayForItemResponse>() {

                    @Override
                    public void success(PayForItemResponse mLogoutResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mLogoutResponse != null) {
                            Object obj = mLogoutResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }


    public void finishSaleResponse(String itemId, String uniqueCode, final BaseActivity
            mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.finishSaleResponse(GlobalMethods.getUserID(mActivity), itemId, uniqueCode, GlobalMethods.getStringValue(mActivity, AppConstants.PAYMENT_MODE),
                new Callback<FinishServicesResponse>() {

                    @Override
                    public void success(FinishServicesResponse mLogoutResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mLogoutResponse != null) {
                            Object obj = mLogoutResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }

    public void getAddBankAccResponse(String mPaymentType, String mIsPrimary, String mUserName, String mAccountNumber, String mRoutingNumber, String mBankName, String mCardnumberNumber, String mExpMonthNumber, String mExpYear, String mCvcNumber, String mCardID, String mFirstName, String mLastName, String mPaypalmailID, String mPaypalID, final BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getAddBankAccResponse(GlobalMethods.getUserID(mActivity), mPaymentType, mIsPrimary, mUserName, mAccountNumber, mRoutingNumber, mBankName, mCardnumberNumber, mExpMonthNumber, mExpYear, mCvcNumber, mCardID, mFirstName, mLastName, mPaypalmailID, mPaypalID,
                new Callback<BankAddAccountResponse>() {

                    @Override
                    public void success(BankAddAccountResponse AddBankAccResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (AddBankAccResponse != null) {
                            Object obj = AddBankAccResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }


    public void getSaveStripeCardResponse(String user_name, String mCardnumberNumber, String
            mExpMonthNumber, String mExpYear, String mCvcNumber, final BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getSaveStripeCardResponse(GlobalMethods.getUserID(mActivity), user_name,
                mCardnumberNumber, mExpMonthNumber, mExpYear, mCvcNumber,
                new Callback<SaveStripCardResponse>() {

                    @Override
                    public void success(SaveStripCardResponse stripeCardResponsee,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (stripeCardResponsee != null) {
                            Object obj = stripeCardResponsee;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }

    public void getBankAccCardDetailsResponse(final BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getAccDetailsResponse(GlobalMethods.getUserID(mActivity),
                new Callback<BankAccDetailsResponse>() {

                    @Override
                    public void success(BankAccDetailsResponse mBankAccDetailsResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mBankAccDetailsResponse != null) {
                            Object obj = mBankAccDetailsResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }

    public void getStripeCardResponse(final BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getStripeCardResponse(GlobalMethods.getUserID(mActivity),
                new Callback<CardResponse>() {

                    @Override
                    public void success(CardResponse mStripeCardResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mStripeCardResponse != null) {
                            Object obj = mStripeCardResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }

    public void getItemDetailsResponse(String itemID, String buyerID, String paymentID, String notification_id, final BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getItemDetailsResponse(GlobalMethods.getUserID(mActivity), itemID, buyerID, paymentID, notification_id,
                new Callback<ItemDetailResponse>() {

                    @Override
                    public void success(ItemDetailResponse mItemDeResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mItemDeResponse != null) {
                            Object obj = mItemDeResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }

    public void getSaleCodeResponse(String item_id, String pay_id, final BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getSaleCodeResponse(GlobalMethods.getUserID(mActivity), item_id, pay_id,
                new Callback<UniqueCodeModelResponse>() {

                    @Override
                    public void success(UniqueCodeModelResponse mPayResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mPayResponse != null) {
                            Object obj = mPayResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }

    public void getPaymentClearResponse(final BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getPaymentClearResponse(GlobalMethods.getUserID(mActivity),
                new Callback<CommonModelResponse>() {

                    @Override
                    public void success(CommonModelResponse mLogoutResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mLogoutResponse != null) {
                            Object obj = mLogoutResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }

    public void getMakePartnerResponse(String partner_code, String item_type, final BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getMakePartnerResponse(GlobalMethods.getUserID(mActivity), partner_code, item_type,
                new Callback<CommonResponse>() {

                    @Override
                    public void success(CommonResponse mPartnerResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mPartnerResponse != null) {
                            Object obj = mPartnerResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }


    public void getPartnerCountResponse(String owner_id, String item_id, String website, String phnum, final BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getPartnerCountResponse(GlobalMethods.getUserID(mActivity), owner_id, item_id, website, phnum,
                new Callback<CommonPhResponse>() {

                    @Override
                    public void success(CommonPhResponse mLogoutResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mLogoutResponse != null) {
                            Object obj = mLogoutResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }

    public void getOfferRejectResponse(String bid_id, final BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getOfferRejectResponse(GlobalMethods.getUserID(mActivity), bid_id,
                new Callback<CommonPhResponse>() {

                    @Override
                    public void success(CommonPhResponse mOfferRejectResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mOfferRejectResponse != null) {
                            Object obj = mOfferRejectResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }

    public void getLogoutResponse(final BaseActivity mActivity) {
        DialogManager.showProgress(mActivity);
        mInterfaces.getLogoutResponse(GlobalMethods.getUserID(mActivity),
                new Callback<CommonResponse>() {

                    @Override
                    public void success(CommonResponse mLogoutResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mLogoutResponse != null) {
                            Object obj = mLogoutResponse;
                            mActivity.onRequestSuccess(obj);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        mActivity.onRequestFailure(retrofitError);
                    }
                });
    }


}


