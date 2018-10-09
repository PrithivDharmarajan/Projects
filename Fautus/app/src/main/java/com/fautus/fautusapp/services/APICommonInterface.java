package com.fautus.fautusapp.services;


import com.fautus.fautusapp.model.EmailValidationResponse;
import com.fautus.fautusapp.model.RetrieveStripeCustomerResponse;
import com.fautus.fautusapp.model.SendEmailResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface APICommonInterface {

    @GET
    Call<EmailValidationResponse> emailValidAPICall(@Url String emailValidURLStr);

    @FormUrlEncoded
    @POST
    Call<SendEmailResponse> emailSendAPICall(@Header("authorization")String headerStr, @Url String sendEmailURLStr, @Field("from") String fromEmailStr, @Field("to") String toEmailStr, @Field("subject") String subjectStr, @Field("text") String msgStr);

    @GET
    Call<RetrieveStripeCustomerResponse> stripCardAPICall(@Header("authorization")String headerStr, @Url String cardUrl);

}
