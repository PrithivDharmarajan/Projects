package com.e2infosystems.activeprotective.services;

import com.e2infosystems.activeprotective.input.model.AddBeltEntity;
import com.e2infosystems.activeprotective.input.model.AddUserEntity;
import com.e2infosystems.activeprotective.input.model.DeleteDeviceEntity;
import com.e2infosystems.activeprotective.input.model.FetchDeviceEntity;
import com.e2infosystems.activeprotective.input.model.LoginEntity;
import com.e2infosystems.activeprotective.input.model.AssignUnAssignBeltEntity;
import com.e2infosystems.activeprotective.output.model.AllUserListResponse;
import com.e2infosystems.activeprotective.output.model.BeltListResponse;
import com.e2infosystems.activeprotective.output.model.CommonResponse;
import com.e2infosystems.activeprotective.output.model.DeleteDeviceResponse;
import com.e2infosystems.activeprotective.output.model.LoginResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface APICommonInterface {

    /*Login API*/
    @POST("v1/user/login")
    Call<LoginResponse> loginAPI(@Body LoginEntity loginEntity);

    /*Belt List API*/
    @FormUrlEncoded
    @POST("devManagement/fetchAllDevice")
    Call<BeltListResponse> beltListAPI(@Header("Authorization") String authorizationStr, @Header("username") String usernameStr, @Field("communityId") String communityIdStr);

    /*Delete device from Belt List API*/
    @POST("devManagement/deleteDevice")
    Call<DeleteDeviceResponse> deleteDeviceFromBeltListAPI(@Header("Authorization") String authorizationStr, @Header("username") String usernameStr, @Body ArrayList<DeleteDeviceEntity> deleteDeviceArrEntityList);

    /*Delete device from Belt List API*/
    @POST("devManagement/addDevice")
    Call<CommonResponse> addDeviceAPI(@Header("Authorization") String authorizationStr, @Header("username") String usernameStr, @Body ArrayList<AddBeltEntity> addDeviceArrEntityList);

    /*Delete device from Belt List API*/
    @POST("devManagement/fetchDevice")
    Call<BeltListResponse> fetchDeviceAPI(@Header("Authorization") String authorizationStr, @Header("username") String usernameStr, @Body ArrayList<FetchDeviceEntity> fetchDeviceEntityArrEntityList);

    /*Assign user belt API*/
    @POST("deviceMap/AssignBelt")
    Call<CommonResponse> assignBeltAPI(@Header("Authorization") String authorizationStr, @Header("username") String usernameStr, @Body AssignUnAssignBeltEntity unAssignBeltEntity);

    /*UnAssign user belt API*/
    @POST("deviceMap/unAssignBelt")
    Call<CommonResponse> unAssignBeltAPI(@Header("Authorization") String authorizationStr, @Header("username") String usernameStr, @Body AssignUnAssignBeltEntity unAssignBeltEntity);

    /*Fetch All User List API*/
    @FormUrlEncoded
    @POST("userDetails/fetchAllUser")
    Call<AllUserListResponse> fetchAllUserListAPI(@Header("Authorization") String authorizationStr, @Header("username") String usernameStr, @Field("communityId") String communityIdStr);

    /*Add userAPI*/
    @POST("userDetails/addUser")
    Call<CommonResponse> addUserAPI(@Header("Authorization") String authorizationStr, @Header("username") String usernameStr, @Body AddUserEntity addUserEntity);

}
