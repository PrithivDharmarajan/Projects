package com.smaat.renterblock.services;

import android.content.Context;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smaat.renterblock.entity.AgentReviewEntity;
import com.smaat.renterblock.entity.AlertsDeleteEntity;
import com.smaat.renterblock.entity.AlertsEntity;
import com.smaat.renterblock.entity.ChatMessageResultEntity;
import com.smaat.renterblock.entity.FindAgentDetailReviewEntity;
import com.smaat.renterblock.entity.FindAgentFilterEntity;
import com.smaat.renterblock.entity.GroupChatSendResponse;
import com.smaat.renterblock.entity.MapFragmentResponse;
import com.smaat.renterblock.entity.PlaceBufferResponse;
import com.smaat.renterblock.entity.UpdateViewEntity;
import com.smaat.renterblock.main.BaseActivity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.AddToBoardsResponse;
import com.smaat.renterblock.model.AddressResponse;
import com.smaat.renterblock.model.BoardMessageResponse;
import com.smaat.renterblock.model.BoardsResponse;
import com.smaat.renterblock.model.ChatResponse;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.model.ContentURLResponse;
import com.smaat.renterblock.model.CreateGroupChatResponse;
import com.smaat.renterblock.model.FavouriteResponse;
import com.smaat.renterblock.model.FriendsRecentListResponse;
import com.smaat.renterblock.model.FriendsResponse;
import com.smaat.renterblock.model.FriendsSearchResponse;
import com.smaat.renterblock.model.HotLeadsResponse;
import com.smaat.renterblock.model.ImageUplaodResponse;
import com.smaat.renterblock.model.ImageVideoUploadResponse;
import com.smaat.renterblock.model.LoginResponse;
import com.smaat.renterblock.model.NotificationResponse;
import com.smaat.renterblock.model.PendingRequestResponse;
import com.smaat.renterblock.model.PlacePredictionResponse;
import com.smaat.renterblock.model.PropertyDetailsResponse;
import com.smaat.renterblock.model.PropertyListingResponse;
import com.smaat.renterblock.model.ReviewPropertyResponse;
import com.smaat.renterblock.model.SavedSearchResponse;
import com.smaat.renterblock.model.ScheduleListResponse;
import com.smaat.renterblock.model.SettingResponse;
import com.smaat.renterblock.model.UserProfileResponse;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.ImageUtil;
import com.smaat.renterblock.utils.InterfaceAPICommonCallback;
import com.smaat.renterblock.utils.InterfacePlacePredictionCallback;
import com.smaat.renterblock.utils.PreferenceUtil;

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

    /*Init retrofit for API call*/

    private Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    private Retrofit retrofit = new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).
            addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    private APICommonInterface mServiceInterface = retrofit.create(APICommonInterface.class);


    public static APIRequestHandler getInstance() {
        return sInstance;
    }


    /*Login API call*/
    public void loginAPICall(String loginTypeStr, String emailIdStr, String passwordStr, String typeStr,
                             String facebookIdStr, String GoogleIdStr, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);

        mServiceInterface.loginAPICall(loginTypeStr, emailIdStr, passwordStr, typeStr,
                facebookIdStr, GoogleIdStr, FirebaseInstanceId.getInstance().getToken(),
                AppConstants.DEVICE).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null)
                    baseActivity.onRequestSuccess(response.body());
                else baseActivity.onRequestFailure(new Throwable(response.raw().message()));
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(t);
            }
        });
    }

    /*Forgot Pwd API call*/
    public void forgotPwdAPICall(String emailStr, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.forgotPwdAPICall(emailStr).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null)
                    baseActivity.onRequestSuccess(response.body());
                else baseActivity.onRequestFailure(new Throwable(response.raw().message()));

            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(t);
            }
        });
    }


    public void registerAPICall(String businessNameStr, String firstNameStr, String lastNameStr, String userNameStr,
                                String emailStr, String passwordStr, String zipStr, String phoneNumberStr, String addressOneStr,
                                String addressTwoStr, String cityStr, String stateStr, String accountManagerStr, String homeOwner, String brokerStr,
                                String enhancedProfileStr, String standardProfileStr,
                                String loginTypeStr, String googleIdStr, String facebookIdStr, String typeStr,
                                String longitudeStr, String latitudeStr,
                                final BaseActivity baseActivity) {

        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.registerAPICall(businessNameStr, firstNameStr, lastNameStr, userNameStr, emailStr, passwordStr, zipStr, phoneNumberStr,
                addressOneStr, addressTwoStr, cityStr, stateStr, accountManagerStr, homeOwner, brokerStr, enhancedProfileStr, standardProfileStr,
                loginTypeStr, googleIdStr, facebookIdStr, typeStr, FirebaseInstanceId.getInstance().getToken(), AppConstants.DEVICE, longitudeStr, latitudeStr).
                enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        DialogManager.getInstance().hideProgress();
                        if (response.isSuccessful() && response.body() != null)
                            baseActivity.onRequestSuccess(response.body());
                        else baseActivity.onRequestFailure(new Throwable(response.raw().message()));

                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        DialogManager.getInstance().hideProgress();
                        baseActivity.onRequestFailure(t);
                    }
                });
    }


    /*Feedback API call*/
    public void feedbackAPICall(String cmdMsgStr, final BaseFragment baseFragment) {
        DialogManager.getInstance().showProgress(baseFragment.getActivity());
        mServiceInterface.feedbackAPICall(PreferenceUtil.getUserID(baseFragment.getActivity()),
                cmdMsgStr).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null)
                    baseFragment.onRequestSuccess(response.body());
                else baseFragment.onRequestFailure(new Throwable(response.raw().message()));
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });
    }


    /*Logout API call*/
    public void logoutAPICall(final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.logoutAPICall(PreferenceUtil.getUserID(baseActivity)).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null)
                    baseActivity.onRequestSuccess(response.body());
                else baseActivity.onRequestFailure(new Throwable(response.raw().message()));
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(t);
            }
        });
    }

    /*Property list API Call*/
    public void propertyListAPICall(String filterObjStr, String typeStr, String latitude, String longitude, String distanceStr, String limitStr, String StartStr, final BaseFragment baseFragment) {

        mServiceInterface.propertyListAPICall(PreferenceUtil.getUserID(baseFragment.getActivity()), filterObjStr, typeStr, latitude, longitude, distanceStr, limitStr, StartStr).enqueue(new Callback<MapFragmentResponse>() {
            @Override
            public void onResponse(Call<MapFragmentResponse> call, Response<MapFragmentResponse> response) {

                if (response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<MapFragmentResponse> call, Throwable t) {
                baseFragment.onRequestFailure(t);
            }
        });
    }

    /*Favourite API Call*/
    public void favouriteAPICall(String propertyIdStr, final Context context, final InterfaceAPICommonCallback mCallback) {

        mServiceInterface.favouriteAPICall(PreferenceUtil.getUserID(context), propertyIdStr).enqueue(new Callback<ContentURLResponse>() {
            @Override
            public void onResponse(Call<ContentURLResponse> call, Response<ContentURLResponse> response) {
                if (response.body() != null) {
                    mCallback.onRequestSuccess(response.body());
                } else {
                    mCallback.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<ContentURLResponse> call, Throwable t) {
                mCallback.onRequestFailure(t);
            }
        });
    }

    /*Suggestion about place API Call*/
    public void callPlaceSuggestionAPI(String str, final BaseFragment baseFragment) {
        String URL = AppConstants.PLACE_URL_START + "input=" + str + AppConstants.PLACE_URL_END;
        mServiceInterface.placeSuggestionAPICall(URL).enqueue(new Callback<PlacePredictionResponse>() {
            @Override
            public void onResponse(Call<PlacePredictionResponse> call, Response<PlacePredictionResponse> response) {
                if (response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<PlacePredictionResponse> call, Throwable t) {
                baseFragment.onRequestFailure(t);
            }
        });

    }

    /*Suggestion about place API Call from Dialog Manager*/
    public void callPlaceSuggestionAPIDialog(String str, final InterfacePlacePredictionCallback mCallback) {
        String URL = AppConstants.PLACE_URL_START + "input=" + str + AppConstants.PLACE_URL_END;
        mServiceInterface.placeSuggestionAPICall(URL).enqueue(new Callback<PlacePredictionResponse>() {
            @Override
            public void onResponse(Call<PlacePredictionResponse> call, Response<PlacePredictionResponse> response) {
                if (response.body() != null) {
                    mCallback.placeList(response.body().getPredictions());
                }
            }

            @Override
            public void onFailure(Call<PlacePredictionResponse> call, Throwable t) {
            }
        });

    }

    /* Settings API call*/
    public void settingAPICall(String settingStr, final BaseFragment baseFragment) {
        mServiceInterface.settingAPICall(PreferenceUtil.getUserID(baseFragment.getActivity()), settingStr).enqueue(new Callback<SettingResponse>() {
            @Override
            public void onResponse(Call<SettingResponse> call, Response<SettingResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<SettingResponse> call, Throwable t) {
                baseFragment.onRequestFailure(t);
            }
        });
    }


    /* get content URL API call*/
    public void contentURLDetailsAPICall(final String url, final BaseActivity baseActivity) {
        mServiceInterface.contentURLAPICall(url).enqueue(new Callback<ContentURLResponse>() {
            @Override
            public void onResponse(Call<ContentURLResponse> call, Response<ContentURLResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    baseActivity.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<ContentURLResponse> call, Throwable t) {
                baseActivity.onRequestFailure(t);
            }
        });
    }


    /*get review API call*/
    public void getReviewDetailsAPICall(String propertyIdStr, final BaseFragment baseFragment) {

        DialogManager.getInstance().showProgress(baseFragment.getActivity());
        mServiceInterface.reviewDetailsAPICall(propertyIdStr).enqueue(new Callback<ReviewPropertyResponse>() {
            @Override
            public void onResponse(Call<ReviewPropertyResponse> call, Response<ReviewPropertyResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<ReviewPropertyResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });
    }


    /*Map Properties*/
    public void getMyProperties(String userID, final BaseFragment baseFragment) {
        DialogManager.getInstance().showProgress(baseFragment.getActivity());

        mServiceInterface.getMyProperties(userID).enqueue(new Callback<PropertyListingResponse>() {
            @Override
            public void onResponse(Call<PropertyListingResponse> call, Response<PropertyListingResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());

                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }

            }

            @Override
            public void onFailure(Call<PropertyListingResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseFragment.onRequestFailure(t);


            }
        });
    }


    /*Property delete*/
    public void propertyDelete(final BaseFragment baseFragment, String propIds) {
        DialogManager.getInstance().showProgress(baseFragment.getActivity());

        mServiceInterface.propertyDelete(PreferenceUtil.getUserID(baseFragment.getActivity()), propIds).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());

                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }

            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseFragment.onRequestFailure(t);


            }
        });
    }

    /*Post and Edit API call*/
    public void postAndEditReviewAPICall(String urlStr, String propertyReviewIdStr, String propertyReviewCommentIdStr, String commentStr,
                                         String ratingStr, String propertyIdStr, final BaseFragment baseFragment) {
        DialogManager.getInstance().showProgress(baseFragment.getActivity());
        mServiceInterface.postAndEditReviewAPICall(urlStr, PreferenceUtil.getUserID(baseFragment.getActivity()), propertyReviewIdStr,
                propertyReviewCommentIdStr, commentStr, ratingStr, propertyIdStr).enqueue(new Callback<ContentURLResponse>() {
            @Override
            public void onResponse(Call<ContentURLResponse> call, Response<ContentURLResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null)
                    baseFragment.onRequestSuccess(response.body());
                else baseFragment.onRequestFailure(new Throwable(response.raw().message()));
            }

            @Override
            public void onFailure(Call<ContentURLResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });
    }


    /*filter applied API call*/
    public void filterSearchAPICall(final BaseFragment baseFragment, String filterType, String filterObj) {
        DialogManager.getInstance().showProgress(baseFragment.getActivity());
        mServiceInterface.filterSearchAPICall(PreferenceUtil.getUserID(baseFragment.getActivity()), filterType, filterObj).enqueue(new Callback<SettingResponse>() {
            @Override
            public void onResponse(Call<SettingResponse> call, Response<SettingResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<SettingResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });

    }

    /*Saved searchAPI Call*/
    public void savedSearchAPICall(final BaseFragment baseFragment) {
        DialogManager.getInstance().showProgress(baseFragment.getActivity());
        mServiceInterface.savedSearchAPICall(PreferenceUtil.getUserID(baseFragment.getActivity())).enqueue(new Callback<SavedSearchResponse>() {
            @Override
            public void onResponse(Call<SavedSearchResponse> call, Response<SavedSearchResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<SavedSearchResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });

    }

    /*Delete SavedSearchAPI Call*/
    public void deleteSavedSearchAPICall(final BaseFragment baseFragment, String selectedSavedSearchIds) {
        DialogManager.getInstance().showProgress(baseFragment.getActivity());
        mServiceInterface.deleteSavedSearchAPICall(PreferenceUtil.getUserID(baseFragment.getActivity()), selectedSavedSearchIds).enqueue(new Callback<SettingResponse>() {
            @Override
            public void onResponse(Call<SettingResponse> call, Response<SettingResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<SettingResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });
    }

    /*Update SavedSearchAPI Call*/
    public void updateSavedSearchAPICall(final BaseFragment baseFragment, String savedSearchId, String filterObj, String filterType, String emailStr, String inquiryStr) {
        DialogManager.getInstance().showProgress(baseFragment.getContext());
        mServiceInterface.updateSavedSearchAPICall(PreferenceUtil.getUserID(baseFragment.getActivity()), savedSearchId, filterObj, filterType, emailStr, inquiryStr).enqueue(new Callback<SettingResponse>() {
            @Override
            public void onResponse(Call<SettingResponse> call, Response<SettingResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<SettingResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });
    }

    /*Favorite API Call*/
    public void getFavouriteListAPICall(final BaseFragment baseFragment) {
        DialogManager.getInstance().showProgress(baseFragment.getContext());
        mServiceInterface.getFavouriteListAPICall(PreferenceUtil.getUserID(baseFragment.getActivity())).enqueue(new Callback<FavouriteResponse>() {
            @Override
            public void onResponse(Call<FavouriteResponse> call, Response<FavouriteResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<FavouriteResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });
    }

    /*Board API Call*/
    public void getBoardListAPICall(final BaseFragment baseFragment) {
        DialogManager.getInstance().showProgress(baseFragment.getContext());
        mServiceInterface.getBoardListAPICall(PreferenceUtil.getUserID(baseFragment.getActivity())).enqueue(new Callback<BoardsResponse>() {
            @Override
            public void onResponse(Call<BoardsResponse> call, Response<BoardsResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<BoardsResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });
    }

    /*Add board API Call*/
    public void addBoardAPICall(String propertyStr, Context context, final InterfaceAPICommonCallback adapterAPICallback) {
        mServiceInterface.addBoardAPICall(PreferenceUtil.getUserID(context), propertyStr, "").enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapterAPICallback.onRequestSuccess(response.body());
                } else {
                    adapterAPICallback.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                adapterAPICallback.onRequestFailure(t);
            }
        });
    }


    /*Favorite API Call*/
    public void getLatLangApi(final BaseFragment baseFragment, String url) {
        mServiceInterface.getLatLangApi(url).enqueue(new Callback<PlaceBufferResponse>() {
            @Override
            public void onResponse(Call<PlaceBufferResponse> call, Response<PlaceBufferResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<PlaceBufferResponse> call, Throwable t) {
                baseFragment.onRequestFailure(t);

            }
        });
    }

    /*Property Details*/

    public void propertyDetails(String property_id, final BaseFragment mFragment) {
        DialogManager.getInstance().showProgress(mFragment.getActivity());
        mServiceInterface.propertyDetails(PreferenceUtil.getUserID(mFragment.getActivity()), property_id).enqueue(new Callback<PropertyDetailsResponse>() {
            @Override
            public void onResponse(Call<PropertyDetailsResponse> call, Response<PropertyDetailsResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    mFragment.onRequestSuccess(response.body());
                } else {
                    mFragment.onRequestSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<PropertyDetailsResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                mFragment.onRequestFailure(t);
            }
        });
    }

    /*Add To Msg Board*/
    public void addToMsgBoard(String propertyId, final BaseFragment mFragment) {
        DialogManager.getInstance().showProgress(mFragment.getActivity());
        mServiceInterface.addToBoard(PreferenceUtil.getUserID(mFragment.getActivity()), propertyId).enqueue(new Callback<AddToBoardsResponse>() {
            @Override
            public void onResponse(Call<AddToBoardsResponse> call, Response<AddToBoardsResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    mFragment.onRequestSuccess(response.body());
                } else {
                    mFragment.onRequestSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<AddToBoardsResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                mFragment.onRequestFailure(t);
            }
        });
    }


    /*Request Info Service*/
    public void requestInfoService(String PropertyId, final BaseFragment mFragment) {
        DialogManager.getInstance().showProgress(mFragment.getActivity());
        mServiceInterface.requestInfoService(PreferenceUtil.getUserID(mFragment.getActivity()), PropertyId).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    mFragment.onRequestSuccess(response.body());
                } else {
                    mFragment.onRequestSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                mFragment.onRequestFailure(t);
            }
        });
    }

    /*Group Id Services*/
    public void groupIdServices(String ownerId, String name, final BaseFragment mFragment) {
        DialogManager.getInstance().showProgress(mFragment.getActivity());
        mServiceInterface.groupIdServices(ownerId, PreferenceUtil.getUserID(mFragment.getActivity()), name).enqueue(new Callback<GroupChatSendResponse>() {
            @Override
            public void onResponse(Call<GroupChatSendResponse> call, Response<GroupChatSendResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    mFragment.onRequestSuccess(response.body());
                } else {
                    mFragment.onRequestSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<GroupChatSendResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                mFragment.onRequestFailure(t);
            }
        });
    }

    /*Photo Image Upload*/
    public void ImageVideoUpload(String property_id, String agentOrBroker, String fileType, String description,
                                 String data, final BaseFragment mFragment) {
        DialogManager.getInstance().showProgress(mFragment.getActivity());
        File file = new File(data);
        MultipartBody.Part body;

        if (fileType == "image") {
            RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), file);
            body = MultipartBody.Part.createFormData("data", file.getName(), imageBody);
        } else if (fileType == "video") {
            RequestBody imageBody = RequestBody.create(MediaType.parse("video/*"), file);
            body = MultipartBody.Part.createFormData("data", file.getName(), imageBody);
        } else {
            body = MultipartBody.Part.createFormData("data", "");

        }


        MultipartBody.Part userId = MultipartBody.Part.createFormData("user_id", PreferenceUtil.getUserID(mFragment.getActivity()));

        MultipartBody.Part propertyId = MultipartBody.Part.createFormData("property_id", property_id);

        MultipartBody.Part Description = MultipartBody.Part.createFormData("description", description);

        MultipartBody.Part agent_broker = MultipartBody.Part.createFormData("agent_or_broker", agentOrBroker);

        MultipartBody.Part FileType = MultipartBody.Part.createFormData("file_type", fileType);

        mServiceInterface.photoVideoUpload(userId, propertyId, agent_broker, FileType, Description, body).enqueue(new Callback<ImageVideoUploadResponse>() {
            @Override
            public void onResponse(Call<ImageVideoUploadResponse> call, Response<ImageVideoUploadResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    mFragment.onRequestSuccess(response.body());
                } else {
                    mFragment.onRequestSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<ImageVideoUploadResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                mFragment.onRequestFailure(t);
            }
        });
    }

    /*get user current loc address*/
    public void callGetUserAddressAPI(String urlStr, final BaseFragment baseFragment) {
        DialogManager.getInstance().showProgress(baseFragment.getActivity());
        mServiceInterface.getUserAddressAPICall(urlStr).enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });

    }


    /*get user current loc address*/
    public void callGetUserAddressAPICallback(Context context, String urlStr, final InterfaceAPICommonCallback interfaceAPICommonCallback) {
        DialogManager.getInstance().showProgress(context);
        mServiceInterface.getUserAddressAPICall(urlStr).enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    interfaceAPICommonCallback.onRequestSuccess(response.body());
                } else {
                    interfaceAPICommonCallback.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                interfaceAPICommonCallback.onRequestFailure(t);
            }
        });

    }


    /*Find agent*/
    public void findAnAgentAPICall(String name, String location, String price_range_min, String price_range_max, String property_experties, String limit, String start, String type, String latitude, String longitude, boolean isFirstBool, final BaseFragment baseFragment) {

        if (isFirstBool) {
            DialogManager.getInstance().showProgress(baseFragment.getActivity());
        }
        mServiceInterface.findAgentFilterAPICall(PreferenceUtil.getUserID(baseFragment.getActivity()), name, location, price_range_min, price_range_max, property_experties, limit, start, type, latitude, longitude).enqueue(new Callback<FindAgentFilterEntity>() {
            @Override
            public void onResponse(Call<FindAgentFilterEntity> call, Response<FindAgentFilterEntity> response) {
                DialogManager.getInstance().hideProgress();

                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<FindAgentFilterEntity> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });
    }

    /*Alerts API CAll*/
    public void alertAPICall(final BaseFragment baseFragment) {

        DialogManager.getInstance().showProgress(baseFragment.getActivity());

        mServiceInterface.alertsAPICall(PreferenceUtil.getUserID(baseFragment.getActivity())).enqueue(new Callback<AlertsEntity>() {
            @Override
            public void onResponse(Call<AlertsEntity> call, Response<AlertsEntity> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<AlertsEntity> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });
    }

    /*Alerts Delete Call*/
    public void alertsDeleteAPICall(final BaseFragment baseFragment, String alert_id) {
        DialogManager.getInstance().showProgress(baseFragment.getActivity());

        mServiceInterface.alertsDeleteAPICall(PreferenceUtil.getUserID(baseFragment.getActivity()), alert_id).enqueue(new Callback<AlertsDeleteEntity>() {
            @Override
            public void onResponse(Call<AlertsDeleteEntity> call, Response<AlertsDeleteEntity> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<AlertsDeleteEntity> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });
    }

    /*Alert create API call*/
    public void alertCreateAPICall(final BaseFragment baseFragment, String alertId, String alertName, String typeStr, String alertObject) {

        DialogManager.getInstance().showProgress(baseFragment.getActivity());

        mServiceInterface.createAlertAPICall(PreferenceUtil.getUserID(baseFragment.getActivity()), typeStr, alertId, alertName, alertObject).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });
    }


    /*Create Schedule*/
    public void createScheduleAPICall(final BaseFragment mFragment, String createSubject, String description, String date, String time,
                                      String location, String inviteFriendsId, String status) {
        DialogManager.getInstance().showProgress(mFragment.getActivity());

        mServiceInterface.createSchedule(PreferenceUtil.getUserID(mFragment.getActivity()), createSubject, description, date, time,
                location, inviteFriendsId, status).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    mFragment.onRequestSuccess(response.body());
                } else {
                    mFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                mFragment.onRequestFailure(t);
            }
        });
    }

    /*Update Schedule*/
    public void UpdateScheduleAPICall(final BaseFragment mFragment, String createSubject, String description, String date, String time,
                                      String location, String inviteFriendsId, String status, String scheduleId, String schedulerId) {
        DialogManager.getInstance().showProgress(mFragment.getActivity());
        mServiceInterface.updateSchedule(schedulerId, scheduleId, createSubject, description, date, time,
                location, inviteFriendsId, status).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    mFragment.onRequestSuccess(response.body());
                } else {
                    mFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                mFragment.onRequestFailure(t);
            }
        });
    }

    /*Get Schedule List*/
    public void getScheduleList(final BaseFragment mFragment) {
        DialogManager.getInstance().showProgress(mFragment.getActivity());
        mServiceInterface.getScheduleList(PreferenceUtil.getUserID(mFragment.getActivity())).enqueue(new Callback<ScheduleListResponse>() {
            @Override
            public void onResponse(Call<ScheduleListResponse> call, Response<ScheduleListResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    mFragment.onRequestSuccess(response.body());
                } else {
                    mFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<ScheduleListResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                mFragment.onRequestFailure(t);
            }
        });
    }

    /*Delete Schedule*/
    public void deleteSchedule(final BaseFragment mFragment, String ScheduleId) {
        DialogManager.getInstance().showProgress(mFragment.getActivity());
        mServiceInterface.deleteSchedule(PreferenceUtil.getUserID(mFragment.getActivity()), ScheduleId).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    mFragment.onRequestSuccess(response.body());
                } else {
                    mFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                mFragment.onRequestFailure(t);
            }
        });
    }
    /*To Save search API call*/

    public void saveSearchAPICall(final BaseFragment baseFragment, String filterObj, String filterType) {
        DialogManager.getInstance().showProgress(baseFragment.getContext());
        mServiceInterface.saveSearchAPICall(PreferenceUtil.getUserID(baseFragment.getActivity()), filterObj, filterType).enqueue(new Callback<SettingResponse>() {
            @Override
            public void onResponse(Call<SettingResponse> call, Response<SettingResponse> response) {

                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<SettingResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });

    }


    /*FindAgentReview Result*/
    public void findAgentDetailsAPICall(String review_user_id, final BaseFragment baseFragment) {
        mServiceInterface.findAgentDetailsReviewAPICall(review_user_id).enqueue(new Callback<FindAgentDetailReviewEntity>() {
            @Override
            public void onResponse(Call<FindAgentDetailReviewEntity> call, Response<FindAgentDetailReviewEntity> response) {
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<FindAgentDetailReviewEntity> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });
    }

    /*AgentReviewAPICall*/
    public void agentReviewPostAPICall(String review_user_id, String comments, String rating, final BaseFragment baseFragment) {
        mServiceInterface.agentReviewAPICall(PreferenceUtil.getUserID(baseFragment.getActivity()), review_user_id, comments, rating).enqueue(new Callback<AgentReviewEntity>() {
            @Override
            public void onResponse(Call<AgentReviewEntity> call, Response<AgentReviewEntity> response) {
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<AgentReviewEntity> call, Throwable t) {
                baseFragment.onRequestFailure(t);

            }
        });
    }

    /*ContactAgentMailAPI*/
    public void contactAgentMailAPICall(String agent_id, String message, final BaseFragment baseFragment) {
        mServiceInterface.contactAgentMailAPICall(PreferenceUtil.getUserID(baseFragment.getActivity()), agent_id, message).enqueue(new Callback<AgentReviewEntity>() {
            @Override
            public void onResponse(Call<AgentReviewEntity> call, Response<AgentReviewEntity> response) {
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<AgentReviewEntity> call, Throwable t) {
                baseFragment.onRequestFailure(t);
            }
        });
    }

    /*Alerts Delete Call*/
    public void getProfileDetails(final BaseFragment baseFragment, String friendId, String offset, String count) {
        DialogManager.getInstance().showProgress(baseFragment.getActivity());

        mServiceInterface.getProfileDetails(friendId, PreferenceUtil.getUserID(baseFragment.getActivity()), offset, count).enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    DialogManager.getInstance().hideProgress();
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });
    }

    /*Update Name Call*/
    public void updateUserProfileName(final BaseFragment baseFragment, String user_name) {
        DialogManager.getInstance().showProgress(baseFragment.getActivity());

        mServiceInterface.updateUserProfileName(PreferenceUtil.getUserID(baseFragment.getActivity()), user_name).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });
    }


    /*Post new review*/
    public void postNewReview(final BaseFragment baseFragment, String propertyId, String commentStr, String ratingStr) {
        DialogManager.getInstance().showProgress(baseFragment.getActivity());

        mServiceInterface.postNewReviewAPICall(PreferenceUtil.getUserID(baseFragment.getContext()), propertyId, commentStr, ratingStr).enqueue(new Callback<ContentURLResponse>() {
            @Override
            public void onResponse(Call<ContentURLResponse> call, Response<ContentURLResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<ContentURLResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });
    }

    /*Change Password Call*/
    public void changePassword(final BaseFragment baseFragment, String password) {
        DialogManager.getInstance().showProgress(baseFragment.getActivity());

        mServiceInterface.changePassword(PreferenceUtil.getUserID(baseFragment.getActivity()), password).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });
    }

    /*Image Upload Call*/
    public void uploadProfileImage(final BaseFragment baseFragment, String filepath) {


        DialogManager.getInstance().showProgress(baseFragment.getActivity());
        File file = new File(filepath);
        RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("profile_image", file.getName(), imageBody);

        MultipartBody.Part userId = MultipartBody.Part.createFormData("user_id", PreferenceUtil.getUserID(baseFragment.getActivity()));

        mServiceInterface.uploadProfileImage(userId, body).enqueue(new Callback<ImageUplaodResponse>() {
            @Override
            public void onResponse(Call<ImageUplaodResponse> call, Response<ImageUplaodResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<ImageUplaodResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });
    }

    /*AddProperty Call*/
    public void addProperty(String prop_name, String address, String type,
                            String lat, String lang, String desc, String prop_type, String price_range,
                            String bed, String bath, String fee, String square_foot, String build_year, String lot_size,
                            String new_constru, String force_closure, String open_house, String reduce_price, String mls,
                            String resale, String prop_img1, String prop_img2, String prop_img3, String prop_img4, String prop_img5,
                            String desc1, String desc2, String desc3, String desc4, String desc5, String prop_id,
                            String prop_video1, String prop_video2, String prop_video3, String prop_video4, String prop_video5,
                            final BaseFragment baseFragment) {

        DialogManager.getInstance().showProgress(baseFragment.getActivity());


        MultipartBody.Part user_id = MultipartBody.Part.createFormData("user_id",
                PreferenceUtil.getUserID(baseFragment.getActivity()));

        MultipartBody.Part propName = MultipartBody.Part.createFormData("property_name",
                prop_name);

        MultipartBody.Part addresStr = MultipartBody.Part.createFormData("address",
                address);

        MultipartBody.Part typeStr = MultipartBody.Part.createFormData("type",
                type);

        MultipartBody.Part latStr = MultipartBody.Part.createFormData("latitude",
                lat);
        MultipartBody.Part langStr = MultipartBody.Part.createFormData("longitude",
                lang);
        MultipartBody.Part descStr = MultipartBody.Part.createFormData("description",
                desc);
        MultipartBody.Part propTypeStr = MultipartBody.Part.createFormData("property_type",
                prop_type);
        MultipartBody.Part priceRangeStr = MultipartBody.Part.createFormData("price_range",
                price_range);
        MultipartBody.Part bedStr = MultipartBody.Part.createFormData("beds",
                bed);
        MultipartBody.Part bathStr = MultipartBody.Part.createFormData("baths",
                bath);

        MultipartBody.Part feeStr = MultipartBody.Part.createFormData("fee",
                fee);
        MultipartBody.Part squareStr = MultipartBody.Part.createFormData("square_footage",
                square_foot);
        MultipartBody.Part buildyearStr = MultipartBody.Part.createFormData("build_year",
                build_year);
        MultipartBody.Part lotStr = MultipartBody.Part.createFormData("lot_size",
                lot_size);
        MultipartBody.Part newConStr = MultipartBody.Part.createFormData("new_construction",
                new_constru);
        MultipartBody.Part forceClosureStr = MultipartBody.Part.createFormData("foreclosure",
                force_closure);
        MultipartBody.Part openHouseStr = MultipartBody.Part.createFormData("open_house",
                open_house);
        MultipartBody.Part reducePriceStr = MultipartBody.Part.createFormData("reduced_prices",
                reduce_price);
        MultipartBody.Part mlsStr = MultipartBody.Part.createFormData("MLS",
                mls);
        MultipartBody.Part reSaleStr = MultipartBody.Part.createFormData("resale",
                resale);
        MultipartBody.Part desStr1 = MultipartBody.Part.createFormData("description1",
                desc1);
        MultipartBody.Part desStr2 = MultipartBody.Part.createFormData("description2",
                desc2);
        MultipartBody.Part desStr3 = MultipartBody.Part.createFormData("description3",
                desc3);
        MultipartBody.Part desStr4 = MultipartBody.Part.createFormData("description4",
                desc4);
        MultipartBody.Part desStr5 = MultipartBody.Part.createFormData("description5",
                desc5);
        MultipartBody.Part propIdStr = MultipartBody.Part.createFormData("property_id",
                prop_id);

        MultipartBody.Part img_body1 = null, img_body2 = null, img_body3 = null, img_body4 = null, img_body5 = null,
                vid_body1 = null, vid_body2 = null, vid_body3 = null, vid_body4 = null, vid_body5 = null;
        if (!prop_img1.isEmpty() && !prop_img1.contains("http")) {
            img_body1 = ImageUtil.getMultipartBody("property_image1", "image", prop_img1);
        } else {
            img_body1 = MultipartBody.Part.createFormData("property_image1",
                    prop_img1);
        }
        if (!prop_img2.isEmpty() && !prop_img2.contains("http")) {
            img_body2 = ImageUtil.getMultipartBody("property_image2", "image", prop_img2);
        } else {
            img_body2 = MultipartBody.Part.createFormData("property_image2",
                    prop_img2);
        }
        if (!prop_img3.isEmpty() && !prop_img3.contains("http")) {
            img_body3 = ImageUtil.getMultipartBody("property_image3", "image", prop_img3);
        } else {
            img_body3 = MultipartBody.Part.createFormData("property_image3",
                    prop_img3);
        }


        if (!prop_img4.isEmpty() && !prop_img4.contains("http")) {
            img_body4 = ImageUtil.getMultipartBody("property_image4", "image", prop_img4);
        } else {
            img_body4 = MultipartBody.Part.createFormData("property_image4",
                    prop_img4);
        }
        if (!prop_img5.isEmpty() && !prop_img5.contains("http")) {
            img_body5 = ImageUtil.getMultipartBody("property_image5", "image", prop_img5);
        } else {
            img_body5 = MultipartBody.Part.createFormData("property_image5",
                    prop_img5);
        }

        if (!prop_video1.isEmpty() && !prop_video1.contains("http")) {
            vid_body1 = ImageUtil.getMultipartBody("property_video1", "video", prop_video1);
        } else {
            vid_body1 = MultipartBody.Part.createFormData("property_video1",
                    prop_video1);
        }
        if (!prop_video2.isEmpty() && !prop_video2.contains("http")) {
            vid_body2 = ImageUtil.getMultipartBody("property_video2", "video", prop_video2);
        } else {
            vid_body2 = MultipartBody.Part.createFormData("property_video2",
                    prop_video2);
        }
        if (!prop_video3.isEmpty() && !prop_video3.contains("http")) {
            vid_body3 = ImageUtil.getMultipartBody("property_video3", "video", prop_video3);
        } else {
            vid_body3 = MultipartBody.Part.createFormData("property_video3",
                    prop_video3);
        }
        if (!prop_video4.isEmpty() && !prop_video4.contains("http")) {
            vid_body4 = ImageUtil.getMultipartBody("property_video4", "video", prop_video4);
        } else {
            vid_body4 = MultipartBody.Part.createFormData("property_video4",
                    prop_video4);
        }
        if (!prop_video5.isEmpty() && !prop_video5.contains("http")) {
            vid_body5 = ImageUtil.getMultipartBody("property_video5", "video", prop_video5);
        } else {
            vid_body5 = MultipartBody.Part.createFormData("property_video5",
                    prop_video5);
        }


        mServiceInterface.addProperty(user_id, propName, addresStr, typeStr,
                latStr, langStr, descStr, propTypeStr, priceRangeStr, bedStr,
                bathStr, feeStr, squareStr, buildyearStr, lotStr,
                newConStr, forceClosureStr, openHouseStr,
                reducePriceStr, mlsStr, reSaleStr,
                img_body1, img_body2, img_body3, img_body4,
                img_body5, desStr1, desStr2, desStr3, desStr4, desStr5, propIdStr,
                vid_body1, vid_body2, vid_body3, vid_body4, vid_body5).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });

//        mServiceInterface.addProperty1(user_id, propName, addresStr, typeStr,
//                latStr, langStr, descStr, propTypeStr, priceRangeStr, bedStr,
//                bathStr, feeStr, squareStr, buildyearStr, lotStr,
//                newConStr, forceClosureStr, openHouseStr,
//                reducePriceStr, mlsStr, reSaleStr,
//                desStr1, desStr2, desStr3, desStr4, desStr5, propIdStr,img_body1).enqueue(new Callback<CommonResponse>() {
//            @Override
//            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
//                DialogManager.getInstance().hideProgress();
//                if (response.isSuccessful() && response.body() != null) {
//                    baseFragment.onRequestSuccess(response.body());
//                } else {
//                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CommonResponse> call, Throwable t) {
//                DialogManager.getInstance().hideProgress();
//                baseFragment.onRequestFailure(t);
//            }
//        });


    }


    /*Hot Leads API call*/
    public void hotLeadsAPICall(final BaseFragment baseFragment) {
        DialogManager.getInstance().showProgress(baseFragment.getActivity());
        mServiceInterface.hotLeadsAPICall(PreferenceUtil.getUserID(baseFragment.getContext())).enqueue(new Callback<HotLeadsResponse>() {
            @Override
            public void onResponse(Call<HotLeadsResponse> call, Response<HotLeadsResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<HotLeadsResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });
    }

    /*Hot Leads update API call*/
    public void hotLeadsUpdateAPICall(Context context, String propertyId, final InterfaceAPICommonCallback adapterCallback) {
        mServiceInterface.hotLeadsUpdateAPICall(PreferenceUtil.getUserID(context), propertyId).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapterCallback.onRequestSuccess(response.body());
                } else {
                    adapterCallback.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                adapterCallback.onRequestFailure(t);
            }
        });
    }


    /*Friends Recent List*/
    public void friendsRecentList(final BaseFragment mFragment) {
        DialogManager.getInstance().showProgress(mFragment.getActivity());

        mServiceInterface.friendsRecentList(PreferenceUtil.getUserID(mFragment.getContext())).enqueue(new Callback<FriendsRecentListResponse>() {
            @Override
            public void onResponse(Call<FriendsRecentListResponse> call, Response<FriendsRecentListResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    mFragment.onRequestSuccess(response.body());
                } else {
                    mFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<FriendsRecentListResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                mFragment.onRequestFailure(t);
            }
        });
    }

    /*Friends List*/
    public void friendsList(final BaseFragment mFragment) {
        DialogManager.getInstance().showProgress(mFragment.getActivity());

        mServiceInterface.friendsList(PreferenceUtil.getUserID(mFragment.getContext())).enqueue(new Callback<FriendsResponse>() {
            @Override
            public void onResponse(Call<FriendsResponse> call, Response<FriendsResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    mFragment.onRequestSuccess(response.body());
                } else {
                    mFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<FriendsResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                mFragment.onRequestFailure(t);
            }
        });

    }

    public void notificationAPICall(final BaseFragment baseFragment) {
        mServiceInterface.notificationAPICall(PreferenceUtil.getUserID(baseFragment.getContext())).enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                baseFragment.onRequestFailure(t);
            }
        });
    }

    public void notificationAPICall2(final BaseActivity baseActivity) {
        mServiceInterface.notificationAPICall(PreferenceUtil.getUserID(baseActivity)).enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    baseActivity.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                //  baseActivity.onRequestFailure(t);
            }
        });
    }


    public void deleteNotification(String notifyId, final BaseFragment baseFragment) {
        DialogManager.getInstance().showProgress(baseFragment.getActivity());
        mServiceInterface.deleteNotification(notifyId).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });
    }


    /*Send Friend Request*/
    public void sendFriendRequest(final BaseFragment baseFragment, String friend_id) {
        DialogManager.getInstance().showProgress(baseFragment.getActivity());
        mServiceInterface.sendFriendRequest(PreferenceUtil.getUserID(baseFragment.getContext()), friend_id).enqueue(new Callback<FriendsRecentListResponse>() {
            @Override
            public void onResponse(Call<FriendsRecentListResponse> call, Response<FriendsRecentListResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<FriendsRecentListResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });
    }

    public void friendSearchList(String search_key, final BaseFragment mFragment) {
        DialogManager.getInstance().showProgress(mFragment.getActivity());

        mServiceInterface.friendSearchResponse(PreferenceUtil.getUserID(mFragment.getContext()), search_key).enqueue(new Callback<FriendsSearchResponse>() {
            @Override
            public void onResponse(Call<FriendsSearchResponse> call, Response<FriendsSearchResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    mFragment.onRequestSuccess(response.body());
                } else {
                    mFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<FriendsSearchResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                mFragment.onRequestFailure(t);
            }
        });
    }

    public void pendingFriendRequest(final BaseFragment mFragment) {
        DialogManager.getInstance().showProgress(mFragment.getActivity());

        mServiceInterface.pendingFriendRequest(PreferenceUtil.getUserID(mFragment.getContext())).enqueue(new Callback<PendingRequestResponse>() {
            @Override
            public void onResponse(Call<PendingRequestResponse> call, Response<PendingRequestResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    mFragment.onRequestSuccess(response.body());
                } else {
                    mFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<PendingRequestResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                mFragment.onRequestFailure(t);
            }
        });
    }

    public void acceptedFriendRequest(String friend_id, final BaseFragment mFragment) {
        DialogManager.getInstance().showProgress(mFragment.getActivity());

        mServiceInterface.acceptedFriendRequest(PreferenceUtil.getUserID(mFragment.getContext()), friend_id).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    mFragment.onRequestSuccess(response.body());
                } else {
                    mFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                mFragment.onRequestFailure(t);
            }
        });
    }

    public void rejectedFriendRequest(String friend_id, final BaseFragment mFragment) {
        DialogManager.getInstance().showProgress(mFragment.getActivity());

        mServiceInterface.rejectedFriendRequest(PreferenceUtil.getUserID(mFragment.getContext()), friend_id).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    mFragment.onRequestSuccess(response.body());
                } else {
                    mFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                mFragment.onRequestFailure(t);
            }
        });
    }

    public void getChatMessages(String userIdStr, String friendIdStr, String typeIsStr, String groupChatId,
                                final BaseFragment mFragment, boolean isShow) {
        if (isShow && mFragment.getActivity() != null) {
            mFragment.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DialogManager.getInstance().showProgress(mFragment.getActivity());

                }
            });


        } else {
            DialogManager.getInstance().hideProgress();
        }

        mServiceInterface.getChatMessages(userIdStr, friendIdStr, typeIsStr, groupChatId).enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                DialogManager.getInstance().hideProgress();

                if (response.isSuccessful() && response.body() != null) {
                    mFragment.onRequestSuccess(response.body());
                } else {
                    mFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                mFragment.onRequestFailure(t);
            }
        });
    }

    public void getChatID(String friendIdStr, String chatNameStr, final BaseFragment baseFragment) {
        DialogManager.getInstance().showProgress(baseFragment.getContext());
        mServiceInterface.getChatID(PreferenceUtil.getUserID(baseFragment.getContext()), friendIdStr, chatNameStr).enqueue(new Callback<CreateGroupChatResponse>() {
            @Override
            public void onResponse(Call<CreateGroupChatResponse> call, Response<CreateGroupChatResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<CreateGroupChatResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });
    }


    public void getChatIDFromSchedule(String SchedulerId, String friendIdStr, String chatNameStr, final BaseFragment baseFragment) {
        DialogManager.getInstance().showProgress(baseFragment.getContext());
        mServiceInterface.getChatID(SchedulerId, friendIdStr, chatNameStr).enqueue(new Callback<CreateGroupChatResponse>() {
            @Override
            public void onResponse(Call<CreateGroupChatResponse> call, Response<CreateGroupChatResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<CreateGroupChatResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });
    }

    public void sendMessageChatMultiPart(String mSchedule_id, String msg, String file_type, String filepath, String fileName, String mType,
                                         String mFile_format, final BaseFragment baseFragment) {

        MultipartBody.Part user_id = MultipartBody.Part.createFormData("user_id",
                PreferenceUtil.getUserID(baseFragment.getContext()));

        MultipartBody.Part schedule_id = MultipartBody.Part.createFormData("schedule_id",
                mSchedule_id);
        MultipartBody.Part message = MultipartBody.Part.createFormData("message",
                msg);
        MultipartBody.Part type = MultipartBody.Part.createFormData("type",
                mType);
        MultipartBody.Part file_format = MultipartBody.Part.createFormData("file_format",
                mFile_format);

        MultipartBody.Part fileType = MultipartBody.Part.createFormData("file_type",
                file_type);
        MultipartBody.Part imagename1 = MultipartBody.Part.createFormData("imagename",
                fileName);

        MultipartBody.Part file_body = null;

        if (!filepath.isEmpty()) {
            switch (file_type) {

                case "image":
                    file_body = ImageUtil.getMultipartBody("chat_file", "image", filepath);
                    break;
                case "video":
                    file_body = ImageUtil.getMultipartBody("chat_file", "video", filepath);
                    break;
                case "audio":
                    file_body = ImageUtil.getMultipartBody("chat_file", "audio", filepath);
                    break;


            }
        }

        mServiceInterface.sendMessageChatMultiPart(user_id, schedule_id, message, fileType, file_body, imagename1, type, file_format).enqueue(new Callback<ChatMessageResultEntity>() {
            @Override
            public void onResponse(Call<ChatMessageResultEntity> call, Response<ChatMessageResultEntity> response) {
                baseFragment.onRequestSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ChatMessageResultEntity> call, Throwable t) {
                baseFragment.onRequestFailure(t);
            }
        });


    }

    public void getBoardMessages(String propertyId, final BaseFragment baseFragment) {
        DialogManager.getInstance().showProgress(baseFragment.getContext());
        mServiceInterface.getBoardMessage(PreferenceUtil.getUserID(baseFragment.getContext()), propertyId).enqueue(new Callback<BoardMessageResponse>() {
            @Override
            public void onResponse(Call<BoardMessageResponse> call, Response<BoardMessageResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<BoardMessageResponse> call, Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseFragment.onRequestFailure(t);
            }
        });
    }

    public void sendBoardMessage(String propertyId, String message, final BaseFragment baseFragment) {


        mServiceInterface.sendBoardMessage(PreferenceUtil.getUserID(baseFragment.getContext()), propertyId, message).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {

                baseFragment.onRequestFailure(t);
            }
        });
    }


    public void friendBlockAPI(String friendId, final BaseFragment baseFragment) {


        mServiceInterface.friendBlock(PreferenceUtil.getUserID(baseFragment.getContext()), friendId).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {

                baseFragment.onRequestFailure(t);
            }
        });
    }

    public void friendUnBlockAPI(String friendId, final BaseFragment baseFragment) {


        mServiceInterface.friendUnBlock(PreferenceUtil.getUserID(baseFragment.getContext()), friendId).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {

                baseFragment.onRequestFailure(t);
            }
        });
    }

    public void updateViewAboutMe(final BaseFragment baseFragment) {

        mServiceInterface.updateView(PreferenceUtil.getUserID(baseFragment.getContext())).enqueue(new Callback<UpdateViewEntity>() {
            @Override
            public void onResponse(Call<UpdateViewEntity> call, Response<UpdateViewEntity> response) {
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<UpdateViewEntity> call, Throwable t) {
                baseFragment.onRequestFailure(t);
            }
        });

    }

    public void updateAboutMe(String about, String file_type, String file_order, String photo_description, String about_image, final BaseFragment baseFragment) {
        mServiceInterface.updateMe(PreferenceUtil.getUserID(baseFragment.getContext()), about, file_type, file_order, photo_description, about_image).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                baseFragment.onRequestFailure(t);
            }
        });

    }

    public void acceptRejectMeeting(String schedule_id, String isonline, final BaseFragment baseFragment) {
        mServiceInterface.acceptRejectMeeting(PreferenceUtil.getUserID(baseFragment.getContext()), schedule_id, isonline).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    baseFragment.onRequestSuccess(response.body());
                } else {
                    baseFragment.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                baseFragment.onRequestFailure(t);
            }
        });
    }
}


