package com.smaat.renterblock.services;

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

        import okhttp3.MultipartBody;
        import retrofit2.Call;
        import retrofit2.http.Field;
        import retrofit2.http.FormUrlEncoded;
        import retrofit2.http.GET;
        import retrofit2.http.Multipart;
        import retrofit2.http.POST;
        import retrofit2.http.Part;
        import retrofit2.http.Url;

interface APICommonInterface {

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> loginAPICall(@Field("login_type") String loginTypeStr, @Field("email_id") String emailIdStr, @Field("password") String passwordStr,
                                     @Field("type") String typeStr, @Field("facebook_id") String facebookIdStr, @Field("google_id") String GoogleIdStr,
                                     @Field("device_id") String deviceIdStr, @Field("device") String deviceStr);

    @FormUrlEncoded
    @POST("forgotpassword")
    Call<CommonResponse> forgotPwdAPICall(@Field("email_id") String emailIdStr);

    @FormUrlEncoded
    @POST("registration")
    Call<LoginResponse> registerAPICall(@Field("business_name") String business_name,
                                        @Field("first_name") String first_name,
                                        @Field("last_name") String last_name,
                                        @Field("user_name") String user_name,
                                        @Field("email_id") String email_id,
                                        @Field("password") String password,
                                        @Field("zip") String zip,
                                        @Field("phone") String phone,
                                        @Field("address1") String address1,
                                        @Field("address2") String address2,
                                        @Field("city") String city,
                                        @Field("state") String state,
                                        @Field("account_manager") String account_manager,
                                        @Field("home_owner") String homeOwner,
                                        @Field("broker") String broker,
                                        @Field("enhanced_profile") String enhanced_profile,
                                        @Field("standard_profile") String standard_profile,
                                        @Field("login_type") String login_type,
                                        @Field("google_id") String google_id,
                                        @Field("facebook_id") String facebook_id,
                                        @Field("type") String type,
                                        @Field("device_id") String device_id,
                                        @Field("device") String device,
                                        @Field("longitude") String longitude,
                                        @Field("latitude") String latitude);

    @FormUrlEncoded
    @POST("feedback")
    Call<CommonResponse> feedbackAPICall(@Field("user_id") String userIdStr, @Field("message") String messageStr);

    @FormUrlEncoded
    @POST("logout")
    Call<CommonResponse> logoutAPICall(@Field("user_id") String userIdStr);

    @FormUrlEncoded
    @POST("propertylist")
    Call<MapFragmentResponse> propertyListAPICall(@Field("user_id") String userIdStr, @Field("filterObject") String filterStr, @Field("type") String typeStr, @Field("latitude") String latitude, @Field("longitude") String longitude, @Field("distance") String distanceStr, @Field("limit") String limitStr, @Field("start") String StartStr);

    @FormUrlEncoded
    @POST("addtofavorite")
    Call<ContentURLResponse> favouriteAPICall(@Field("user_id") String userIdStr, @Field("property_id") String propertyIdStr);

    @POST
    Call<PlacePredictionResponse> placeSuggestionAPICall(@Url String url);

    @FormUrlEncoded
    @POST("setting")
    Call<SettingResponse> settingAPICall(@Field("user_id") String user_id, @Field("setting") String setting);

    @GET
    Call<ContentURLResponse> contentURLAPICall(@Url String urlStr);

    @FormUrlEncoded
    @POST("reviewproperty/myreview")
    Call<ReviewPropertyResponse> reviewDetailsAPICall(@Field("review_user_id") String user_id);

    @FormUrlEncoded
    @POST
    Call<ContentURLResponse> postAndEditReviewAPICall(@Url String urlStr, @Field("review_user_id") String userIdStr, @Field("property_review_id") String propertyReviewIdStr,
                                                      @Field("property_review_comment_id") String propertyReviewCommentIdStr, @Field("comments") String commentStr,
                                                      @Field("rating") String ratingStr, @Field("property_id") String propertyIdStr);


    @FormUrlEncoded
    @POST("addnewproperty")
    Call<CommonResponse> addOrEditProperty(@Field("user_id") String mUserId,
                                           @Field("property_name") String propertyName,
                                           @Field("address") String address,
                                           @Field("type") String type,
                                           @Field("latitude") String lat,
                                           @Field("longitude") String longt,
                                           @Field("description") String description,
                                           @Field("property_type") String propertyType,
                                           @Field("price_range") String price,
                                           @Field("beds") String beds,
                                           @Field("baths") String baths,
                                           @Field("fee") String fee,
                                           @Field("square_footage") String square,
                                           @Field("build_year") String buildYear,
                                           @Field("lot_size") String lotSize,
                                           @Field("new_construction") String newConstruction,
                                           @Field("foreclosure") String forceClosure,
                                           @Field("open_house") String openHouse,
                                           @Field("reduced_prices") String reducePrice,
                                           @Field("MLS") String mls,
                                           @Field("resale") String reSale,
                                           @Field("property_image1") String propImg1,
                                           @Field("property_image2") String propImg2,
                                           @Field("property_image3") String propImg3,
                                           @Field("property_image4") String propImg4,
                                           @Field("property_image5") String propImg5,
                                           @Field("description1") String desc1,
                                           @Field("description2") String desc2,
                                           @Field("description3") String desc3,
                                           @Field("description4") String desc4,
                                           @Field("description5") String desc5,
                                           @Field("property_id") String propId,
                                           @Field("property_video1") String propVideo1,
                                           @Field("property_video2") String propVideo2,
                                           @Field("property_video3") String propVideo3,
                                           @Field("property_video4") String propVideo4,
                                           @Field("property_video5") String propVideo5);

    @FormUrlEncoded
    @POST("getmylisting")
    Call<PropertyListingResponse> getMyProperties(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("addnewproperty/delete")
    Call<CommonResponse> propertyDelete(@Field("user_id") String user_id, @Field("property_id") String propIds);

    @FormUrlEncoded
    @POST("filtersearch")
    Call<SettingResponse> filterSearchAPICall(@Field("user_id") String user_id, @Field("filter_type") String filterStr, @Field("filter_object") String filterObjStr);


    @FormUrlEncoded
    @POST("savesearch/view")
    Call<SavedSearchResponse> savedSearchAPICall(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("savesearch/delete")
    Call<SettingResponse> deleteSavedSearchAPICall(@Field("user_id") String user_id, @Field("save_search_id") String searchIds);

    @FormUrlEncoded
    @POST("savesearch/update")
    Call<SettingResponse> updateSavedSearchAPICall(@Field("user_id") String user_id, @Field("save_search_id") String saveSearchId, @Field("filter_object") String filterObjStr, @Field("filter_type") String filterTypeStr, @Field("email_notification") String emailNotifyStr, @Field("inquiry") String inquiryStr);

    @FormUrlEncoded
    @POST("addtofavorite/view")
    Call<FavouriteResponse> getFavouriteListAPICall(@Field("user_id") String userIdStr);

    @FormUrlEncoded
    @POST("postboards/view")
    Call<BoardsResponse> getBoardListAPICall(@Field("user_id") String userIdStr);

    @FormUrlEncoded
    @POST("addtoboards")
    Call<CommonResponse> addBoardAPICall(@Field("user_id") String userIdStr, @Field("property_id") String propertyIdStr, @Field("message") String messageStr);

    @GET
    Call<PlaceBufferResponse> getLatLangApi(@Url String urlStr);

    @GET
    Call<AddressResponse> getUserAddressAPICall(@Url String urlStr);

    /*Property Details*/
    @FormUrlEncoded
    @POST("property/view")
    Call<PropertyDetailsResponse> propertyDetails(@Field("user_id") String user_id, @Field("property_id") String property_id);

    /*Add to msg Board*/
    @FormUrlEncoded
    @POST("addtoboards")
    Call<AddToBoardsResponse> addToBoard(@Field("user_id") String user_id, @Field("property_id") String property_id);

    /*Send Friend Request*/
    @FormUrlEncoded
    @POST("friend/sendfrindrequest")
    Call<FriendsRecentListResponse> sendFriendRequest(@Field("user_id") String user_id, @Field("friend_user_id") String friend_user_id);

    /*Request Info Services*/
    @FormUrlEncoded
    @POST("requestinfo")
    Call<CommonResponse> requestInfoService(@Field("userId") String user_id, @Field("propertyId") String property_Id);

    /*GroupId Service*/
    @FormUrlEncoded
    @POST("group")
    Call<GroupChatSendResponse> groupIdServices(@Field("owner_id") String owner_id, @Field("user_id") String user_Id, @Field("name") String name);

    /*Image and Video Upload API*/
    @Multipart
    @POST("photovideo")
    Call<ImageVideoUploadResponse> photoVideoUpload(@Part MultipartBody.Part user_id, @Part MultipartBody.Part property_id,
                                                    @Part MultipartBody.Part agent_or_broker, @Part MultipartBody.Part file_type,
                                                    @Part MultipartBody.Part description, @Part MultipartBody.Part data);

    @FormUrlEncoded
    @POST("agentfilter")
    Call<FindAgentFilterEntity> findAgentFilterAPICall(@Field("user_id") String user_id, @Field("name") String name, @Field("location") String location, @Field("price_range_min") String price_range_min, @Field("price_range_max") String price_range_max, @Field("property_experties") String property_experties, @Field("limit") String limit, @Field("start") String start, @Field("type") String type, @Field("latitude") String latitude, @Field("longitude") String longitude);


    @FormUrlEncoded
    @POST("alert/get")
    Call<AlertsEntity> alertsAPICall(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("alert/delete")
    Call<AlertsDeleteEntity> alertsDeleteAPICall(@Field("user_id") String user_id, @Field("alert_id") String alert_id);

    @FormUrlEncoded
    @POST("alert")
    Call<CommonResponse> createAlertAPICall(@Field("user_id") String mUserId,
                                            @Field("type") String type,
                                            @Field("alert_id") String alertId,
                                            @Field("alert_name") String alertName,
                                            @Field("alert_object") String alertObj);


    @FormUrlEncoded
    @POST("schedulemeeting")
    Call<CommonResponse> createSchedule(@Field("user_id") String user_id,
                                        @Field("meeting_subject") String meeting_subject,
                                        @Field("description") String description,
                                        @Field("date") String date,
                                        @Field("time") String time,
                                        @Field("venue") String venue,
                                        @Field("invite_user_ids") String invite_user_ids,
                                        @Field("status") String status);
    @FormUrlEncoded
    @POST("updatemeeting")
    Call<CommonResponse> updateSchedule(@Field("user_id")String userId,@Field("meeting_subject") String meeting_subject,
                                        @Field("description") String description,
                                        @Field("date") String date,
                                        @Field("time") String time,
                                        @Field("venue") String venue,
                                        @Field("schedule_id")String scheduleId,
                                        @Field("invite_user_ids") String invite_user_ids,
                                        @Field("status") String status);

    @FormUrlEncoded
    @POST("getschedule")
    Call<ScheduleListResponse> getScheduleList(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("getschedule/delete")
    Call<CommonResponse> deleteSchedule(@Field("user_id") String user_id,
                                        @Field("schedule_id") String schedule_id);

    @FormUrlEncoded
    @POST("savesearch")
    Call<SettingResponse> saveSearchAPICall(@Field("user_id") String user_id, @Field("filter_object") String filterObj, @Field("filter_type") String filterType);

    @FormUrlEncoded
    @POST("reviewuser/view")
    Call<FindAgentDetailReviewEntity> findAgentDetailsReviewAPICall(@Field("review_user_id") String review_user_id);

    @FormUrlEncoded
    @POST("reviewuser")
    Call<AgentReviewEntity> agentReviewAPICall(@Field("user_id") String user_id, @Field("review_user_id") String review_user_id, @Field("comments") String comments, @Field("rating") String rating);

    @FormUrlEncoded
    @POST("contactagentmail")
    Call<AgentReviewEntity> contactAgentMailAPICall(@Field("user_id") String user_id, @Field("agent_id") String agent_id, @Field("message") String message);

    @FormUrlEncoded
    @POST("myprofile")
    Call<UserProfileResponse> getProfileDetails(@Field("user_id") String user_id, @Field("friend_id") String friend_id,
                                                @Field("offset") String offset, @Field("count") String count);

    @FormUrlEncoded
    @POST("username")
    Call<CommonResponse> updateUserProfileName(@Field("user_id") String user_id, @Field("user_name") String user_name);

    @FormUrlEncoded
    @POST("changepassword")
    Call<CommonResponse> changePassword(@Field("user_id") String user_id, @Field("password") String password);

    @Multipart
    @POST("addprofileimage")
    Call<ImageUplaodResponse> uploadProfileImage(@Part MultipartBody.Part userId, @Part MultipartBody.Part filePart);

    @FormUrlEncoded
    @POST("reviewproperty")
    Call<ContentURLResponse> postNewReviewAPICall(@Field("review_user_id") String userId, @Field("property_id") String propertyId, @Field("comments") String commentStr, @Field("rating") String ratingStr);

    @FormUrlEncoded
    @POST("getmyhotleads")
    Call<HotLeadsResponse> hotLeadsAPICall(@Field("userId") String userId);

    @FormUrlEncoded
    @POST("getmyhotleads/update")
    Call<CommonResponse> hotLeadsUpdateAPICall(@Field("user_id") String userId, @Field("property_id") String propertyId);

    @FormUrlEncoded
    @POST("recentlist")
    Call<FriendsRecentListResponse> friendsRecentList(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("friend")
    Call<FriendsResponse> friendsList(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("notification")
    Call<NotificationResponse> notificationAPICall(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("notification/delete")
    Call<CommonResponse> deleteNotification(@Field("notification_id") String notify_id);


    @FormUrlEncoded
    @POST("friend/usersearch")
    Call<FriendsSearchResponse> friendSearchResponse(@Field("user_id") String user_id,
                                                     @Field("search") String search_key);

    @FormUrlEncoded
    @POST("friend/friendsrequest")
    Call<PendingRequestResponse> pendingFriendRequest(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("friend/acceptfrindrequest")
    Call<CommonResponse> acceptedFriendRequest(@Field("user_id") String user_id,
                                               @Field("friend_user_id") String friend_user_id);

    @FormUrlEncoded
    @POST("friend/rejectfrindrequest")
    Call<CommonResponse> rejectedFriendRequest(@Field("user_id") String user_id,
                                               @Field("friend_user_id") String friend_user_id);

    @FormUrlEncoded
    @POST("group")
    Call<CreateGroupChatResponse> getChatID(@Field("owner_id") String owner_id,
                                            @Field("users_id") String users_id, @Field("name") String name);

    @FormUrlEncoded
    @POST("groupchat")
    Call<ChatResponse> getChatMessages(@Field("user_id") String user_id,
                                       @Field("schedule_id") String group_id, @Field("type") String type
            , @Field("group_chat_id") String group_chat_id);

    /*add properties*/
    @Multipart
    @POST("addnewproperty")
    Call<CommonResponse> addProperty(@Part MultipartBody.Part user_id, @Part MultipartBody.Part prop_name,
                                     @Part MultipartBody.Part address, @Part MultipartBody.Part type,
                                     @Part MultipartBody.Part lat, @Part MultipartBody.Part lang,
                                     @Part MultipartBody.Part desc, @Part MultipartBody.Part prop_type,
                                     @Part MultipartBody.Part price_range, @Part MultipartBody.Part bed,
                                     @Part MultipartBody.Part bath, @Part MultipartBody.Part fee,
                                     @Part MultipartBody.Part square_footage,
                                     @Part MultipartBody.Part build_year, @Part MultipartBody.Part lot_size,
                                     @Part MultipartBody.Part new_constr, @Part MultipartBody.Part force_closure,
                                     @Part MultipartBody.Part open_house, @Part MultipartBody.Part reduce_price,
                                     @Part MultipartBody.Part mls, @Part MultipartBody.Part resale,
                                     @Part MultipartBody.Part prop_img1, @Part MultipartBody.Part prop_img2,
                                     @Part MultipartBody.Part prop_img3, @Part MultipartBody.Part prop_img4,
                                     @Part MultipartBody.Part prop_img5, @Part MultipartBody.Part desc1,
                                     @Part MultipartBody.Part desc2, @Part MultipartBody.Part desc3,
                                     @Part MultipartBody.Part desc4, @Part MultipartBody.Part desc5,
                                     @Part MultipartBody.Part prop_id, @Part MultipartBody.Part prop_video1,
                                     @Part MultipartBody.Part prop_video2,
                                     @Part MultipartBody.Part prop_video3, @Part MultipartBody.Part prop_video4,
                                     @Part MultipartBody.Part prop_video5);


    @FormUrlEncoded
    @POST("groupchat/sendmessage")
    Call<CreateGroupChatResponse> sendMessageChat(@Field("user_id") String user_id, @Field("schedule_id") String schedule_id,
                                                  @Field("message") String message, @Field("file_type") String file_type,
                                                  @Field("imagename") String imagename, @Field("type") String type,
                                                  @Field("file_format") String file_format);

    @Multipart
    @POST("groupchat/sendmessage")
    Call<ChatMessageResultEntity> sendMessageChatMultiPart(@Part MultipartBody.Part user_id, @Part MultipartBody.Part schedule_id,
                                                           @Part MultipartBody.Part message, @Part MultipartBody.Part file_type,
                                                           @Part MultipartBody.Part chat_file,
                                                           @Part MultipartBody.Part imagename, @Part MultipartBody.Part type, @Part MultipartBody.Part file_format);

    @FormUrlEncoded
    @POST("postboards/messageview")
    Call<BoardMessageResponse> getBoardMessage(@Field("user_id") String userId, @Field("propertyId") String propertyId);

    @FormUrlEncoded
    @POST("postboards")
    Call<CommonResponse> sendBoardMessage(@Field("user_id") String userId, @Field("property_id") String propertyId,
                                          @Field("message") String msg);

    @FormUrlEncoded
    @POST("friend/block")
    Call<CommonResponse> friendBlock(@Field("user_id") String user_id,@Field("friend_user_id") String friend_user_id);

    @FormUrlEncoded
    @POST("friend/unblock")
    Call<CommonResponse> friendUnBlock(@Field("user_id") String user_id,@Field("friend_user_id") String friend_user_id);


    @FormUrlEncoded
    @POST("updateme/view")
    Call<UpdateViewEntity> updateView(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("updateme")
    Call<CommonResponse> updateMe(@Field("user_id") String user_id,@Field("about")
            String about,@Field("file_type") String file_type,@Field("file_order")
            String file_order,@Field("photo_description") String photo_description, @Field("about_image") String about_image);

    @FormUrlEncoded
    @POST("acceptmeeting")
    Call<CommonResponse> acceptRejectMeeting(@Field("user_id") String user_id, @Field("schedule_id") String schedule_id,
                                             @Field("isonline") String isonline);
        }
