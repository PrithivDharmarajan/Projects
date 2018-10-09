package com.smaat.ipharma.apiinterface;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.smaat.ipharma.entity.AlarmObject;
import com.smaat.ipharma.entity.CommonResponse;
import com.smaat.ipharma.entity.ForgotResponse;
import com.smaat.ipharma.entity.MapResponse;
import com.smaat.ipharma.entity.NewOrderEntity;
import com.smaat.ipharma.entity.OneTouchResponse;
import com.smaat.ipharma.entity.OrderHistoryCommonResponseEntity;
import com.smaat.ipharma.entity.OtpEntity;
import com.smaat.ipharma.entity.RateResponseEntity;
import com.smaat.ipharma.entity.RecentOrderResponse;
import com.smaat.ipharma.entity.ShowReviewResponse;
import com.smaat.ipharma.entity.WriteReviewEntity;
import com.smaat.ipharma.main.BaseActivity;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.ui.SignupScreen;
import com.smaat.ipharma.utils.AppConstants;
import com.smaat.ipharma.utils.DialogManager;
import com.smaat.ipharma.utils.GlobalMethods;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.Part;

import static com.smaat.ipharma.utils.AppConstants.Choosed_Image;


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



    //Signin API Call
    public void signinAPICall(String email_id,String phoneno, String password,String device_type,
                              String device_token,final BaseActivity baseActivity) {
        DialogManager.showProgress(baseActivity);
        mServiceInterface.signinAPICall(email_id,phoneno,password,device_type,device_token).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                //DialogManager.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    baseActivity.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable throwableMsg) {
                DialogManager.hideProgress();
                baseActivity.onRequestFailure(throwableMsg);
            }
        });
    }


    public void getMyOrderList(String mUserID,final BaseFragment baseActivity) {



        DialogManager.showProgress(baseActivity.getActivity());
        mServiceInterface.getMyOrders(mUserID).enqueue(new Callback<OrderHistoryCommonResponseEntity>() {
            @Override
            public void onResponse(Call<OrderHistoryCommonResponseEntity> call, Response<OrderHistoryCommonResponseEntity> response) {
                DialogManager.hideProgress();

                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    baseActivity.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<OrderHistoryCommonResponseEntity> call, Throwable throwableMsg) {
                DialogManager.hideProgress();
                baseActivity.onRequestFailure(throwableMsg);
            }
        });

    }

    public void updateProfileApi(String m_strUserId,String m_strUserName, String m_strEmailid, String m_strPhoneNo, String m_strPassword, String m_strAddress, String m_strCity, String m_area, String m_strPincode, final BaseFragment baseActivity) {

        DialogManager.showProgress(baseActivity.getContext());
        mServiceInterface.getupdateprofile(m_strUserId,m_strUserName,m_strEmailid,m_strPhoneNo,m_strPassword,m_strAddress,m_strCity,m_area,m_strPincode).enqueue(new Callback<CommonResponse>() {
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
            public void onFailure(Call<CommonResponse> call, Throwable throwableMsg) {
                DialogManager.hideProgress();
                baseActivity.onRequestFailure(throwableMsg);
            }
        });

    }

    //Signup API Call
    public void signupAPICall(String m_strUserName, String m_strEmailid, String m_strPhoneNo, String m_strPassword, String m_strAddress, String m_strCity, String m_area, String m_strPincode, String latitude, String longitude, String devicetype, String device_id, final BaseActivity baseActivity) {

        DialogManager.showProgress(baseActivity);
        mServiceInterface.signupAPICall(m_strUserName,m_strEmailid,m_strPhoneNo,m_strPassword,m_strAddress,m_strCity,m_area,m_strPincode,latitude,longitude,devicetype,device_id).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                //DialogManager.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    baseActivity.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable throwableMsg) {
                DialogManager.hideProgress();
                baseActivity.onRequestFailure(throwableMsg);
            }
        });

    }



    public void otpConfirmation(String userid, String otptext,final  BaseActivity baseActivity) {

        DialogManager.showProgress(baseActivity);
        mServiceInterface.otpConfirmation(userid,otptext).enqueue(new Callback<OtpEntity>() {
            @Override
            public void onResponse(Call<OtpEntity> call, Response<OtpEntity> response) {
                DialogManager.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    baseActivity.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<OtpEntity> call, Throwable throwableMsg) {
                DialogManager.hideProgress();
                baseActivity.onRequestFailure(throwableMsg);
            }
        });

    }



    public void ResendOtp(String userid,final  BaseActivity baseActivity) {

        DialogManager.showProgress(baseActivity);
        mServiceInterface.resendOtp(userid).enqueue(new Callback<OtpEntity>() {
            @Override
            public void onResponse(Call<OtpEntity> call, Response<OtpEntity> response) {
                DialogManager.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    baseActivity.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<OtpEntity> call, Throwable throwableMsg) {
                DialogManager.hideProgress();
                baseActivity.onRequestFailure(throwableMsg);
            }
        });

    }


    public void Forgotpassword(String emailid,final BaseActivity baseActivity) {

        DialogManager.showProgress(baseActivity);
        mServiceInterface.forgotpassword(emailid).enqueue(new Callback<ForgotResponse>() {
            @Override
            public void onResponse(Call<ForgotResponse> call, Response<ForgotResponse> response) {
                DialogManager.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    baseActivity.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<ForgotResponse> call, Throwable throwableMsg) {
                DialogManager.hideProgress();
                baseActivity.onRequestFailure(throwableMsg);
            }
        });

    }



    public void SearchPharmacies(double mLatitude,
                                 double mLongitude, String mSearchKeys, float mDistance, final RelativeLayout layout, final ListView lview,final BaseFragment baseActivity) {

        DialogManager.showProgress(baseActivity.getActivity());
        String userid = "0";
        if(!GlobalMethods.getUserID(baseActivity.getActivity()).equalsIgnoreCase(""))
        {
            userid = GlobalMethods.getUserID(baseActivity.getActivity());
        }
        mServiceInterface.getPharmacyList(userid,""+mLatitude,""+mLongitude,mSearchKeys,mDistance).enqueue(new Callback<MapResponse>() {
            @Override
            public void onResponse(Call<MapResponse> call, Response<MapResponse> response) {
                DialogManager.hideProgress();
                Log.e("saasasasas","saasasas"+response.body());
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    baseActivity.onRequestFailureForList(new Throwable(response.raw().message()),layout,lview);
                }
            }

            @Override
            public void onFailure(Call<MapResponse> call, Throwable throwableMsg) {
                DialogManager.hideProgress();
                baseActivity.onRequestFailureForList(throwableMsg,layout,lview);
            }
        });

    }



    public void GetFavList(String mUserID,String lat,String longitude, final RelativeLayout layout, final ListView lview,final BaseFragment baseActivity) {

        DialogManager.showProgress(baseActivity.getActivity());
        mServiceInterface.getFavList(mUserID,lat,longitude).enqueue(new Callback<MapResponse>() {
            @Override
            public void onResponse(Call<MapResponse> call, Response<MapResponse> response) {
                DialogManager.hideProgress();
                Log.e("saasasasas","saasasas"+response.body());
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    baseActivity.onRequestFailureForList(new Throwable(response.raw().message()),layout,lview);
                }
            }

            @Override
            public void onFailure(Call<MapResponse> call, Throwable throwableMsg) {
                DialogManager.hideProgress();
                baseActivity.onRequestFailureForList(throwableMsg,layout,lview);
            }
        });

    }


    public void GetHistory(String mUserID, final RelativeLayout lRlayout, final ListView listView, final BaseFragment baseActivity) {


        DialogManager.showProgress(baseActivity.getActivity());
        mServiceInterface.getMyOrders(mUserID).enqueue(new Callback<OrderHistoryCommonResponseEntity>() {
            @Override
            public void onResponse(Call<OrderHistoryCommonResponseEntity> call, Response<OrderHistoryCommonResponseEntity> response) {
                DialogManager.hideProgress();
                Log.e("saasasasas","saasasas"+response.body());
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    baseActivity.onRequestFailureForList(new Throwable(response.raw().message()),lRlayout,listView);
                }
            }

            @Override
            public void onFailure(Call<OrderHistoryCommonResponseEntity> call, Throwable throwableMsg) {
                DialogManager.hideProgress();
                baseActivity.onRequestFailureForList(throwableMsg,lRlayout,listView);
            }
        });

    }


    public void CashOnDelivery(String UserID, String OrderID,
                               String PaymentAccept, String PaymentMode,
                               final BaseFragment baseActivity) {


        DialogManager.showProgress(baseActivity.getActivity());
        mServiceInterface.getOrderPaymentUpdate(UserID,OrderID,PaymentAccept,PaymentMode).enqueue(new Callback<OtpEntity>() {
            @Override
            public void onResponse(Call<OtpEntity> call, Response<OtpEntity> response) {
                DialogManager.hideProgress();
                Log.e("saasasasas","saasasas"+response.body());
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    baseActivity.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<OtpEntity> call, Throwable throwableMsg) {
                DialogManager.hideProgress();
                baseActivity.onRequestFailure(throwableMsg);
            }
        });

    }

    public void addFavourite(String mShopID, String mUserID, String mFavStatus,
                             final BaseFragment baseActivity) {

        //DialogManager.showProgress(baseActivity.getActivity());
        mServiceInterface.addFavourite(mShopID, mUserID, mFavStatus).enqueue(new Callback<WriteReviewEntity>() {
            @Override
            public void onResponse(Call<WriteReviewEntity> call, Response<WriteReviewEntity> response) {
                DialogManager.hideProgress();

                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    baseActivity.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<WriteReviewEntity> call, Throwable throwableMsg) {
                //DialogManager.hideProgress();
                baseActivity.onRequestFailure(throwableMsg);
            }
        });

    }


    public void rateShop(String mShopID, String mUserID, String ratingvalue,
                          final BaseFragment baseActivity) {

        DialogManager.showProgress(baseActivity.getActivity());
        mServiceInterface.shopRating(mShopID, mUserID, ratingvalue).enqueue(new Callback<RateResponseEntity>() {
            @Override
            public void onResponse(Call<RateResponseEntity> call, Response<RateResponseEntity> response) {
                DialogManager.hideProgress();
                Log.e("Sucess","Sucess"+response.isSuccessful())   ;
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    baseActivity.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<RateResponseEntity> call, Throwable throwableMsg) {
                DialogManager.hideProgress();
                Log.e("onFailure","onFailure")   ;
                baseActivity.onRequestFailure(throwableMsg);
            }
        });

    }



    public void showReview(final String mShopID, final RelativeLayout lRlayout, final ListView listView,  final BaseFragment baseActivity) {
        Log.e("mShopIDmShopID","mShopIDmShopID"+mShopID);

        DialogManager.showProgress(baseActivity.getActivity());
        mServiceInterface.showReview(mShopID).enqueue(new Callback<ShowReviewResponse>() {
            @Override
            public void onResponse(Call<ShowReviewResponse> call, Response<ShowReviewResponse> response) {
                DialogManager.hideProgress();

                Log.e("Sucess","Sucess"+response.isSuccessful())   ;
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    baseActivity.onRequestFailureForList(new Throwable(response.raw().message()),lRlayout,listView);
                }
            }

            @Override
            public void onFailure(Call<ShowReviewResponse> call, Throwable throwableMsg) {
                DialogManager.hideProgress();
                Log.e("onFailure","onFailure");
                baseActivity.onRequestFailureForList(throwableMsg,lRlayout,listView);
            }
        });
    }

    public void Placeorder(String mShopID, String mUserID, String commaddress,String new_address,String order_note,
                         final BaseFragment baseActivity) {

        DialogManager.showProgress(baseActivity.getActivity());
        MultipartBody.Part body;
        if(AppConstants.FROMHISTORY)
        {
            String mSelectedImgLocalPath = GlobalMethods.storeImage(getBitmapFromURL(AppConstants.BASE_URL2 + "/" + Choosed_Image), "OrderImg" + "-",baseActivity.getContext());
            File file = new File(mSelectedImgLocalPath);
            RequestBody imageBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
            body = MultipartBody.Part.createFormData("PrescriptionFile", file.getName(), imageBody);
        }else{
            String mSelectedImgLocalPath = GlobalMethods.storeImage(HomeScreen.mSelectedImgBitmap, "OrderImg" + "-",baseActivity.getContext());
            File file = new File(mSelectedImgLocalPath);
            RequestBody imageBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
            body = MultipartBody.Part.createFormData("PrescriptionFile", file.getName(), imageBody);
        }


        MultipartBody.Part shpid = MultipartBody.Part.createFormData("ShopID", mShopID);
        MultipartBody.Part userid = MultipartBody.Part.createFormData("UserID", mUserID);
        MultipartBody.Part comm_addr = MultipartBody.Part.createFormData("DeliveryAddress", commaddress);
        MultipartBody.Part new_addr = MultipartBody.Part.createFormData("NewAddress", new_address);
        MultipartBody.Part ord_note = MultipartBody.Part.createFormData("OrderNote", order_note);


        mServiceInterface.newOrder(shpid, userid, comm_addr,new_addr, ord_note, body).enqueue(new Callback<NewOrderEntity>() {
            @Override
            public void onResponse(Call<NewOrderEntity> call, Response<NewOrderEntity> response) {
                DialogManager.hideProgress();
                Log.e("Sucess","Sucess"+response.isSuccessful())   ;
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    baseActivity.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<NewOrderEntity> call, Throwable throwableMsg) {
                DialogManager.hideProgress();
                Log.e("onFailure","onFailure")   ;
                baseActivity.onRequestFailure(throwableMsg);
            }
        });

    }


    public void OnetouchOrder(String mUserID,String mShopid,
                           final BaseFragment baseActivity) {

        DialogManager.showProgress(baseActivity.getActivity());


        Log.e("mUserIDmUserID","mUserIDmUserIDmUserID"+mUserID);
        Log.e("mShopidmShopid","mShopidmShopidmShopid"+mShopid);
        mServiceInterface.onetouch(mUserID,mShopid).enqueue(new Callback<OneTouchResponse>() {
            @Override
            public void onResponse(Call<OneTouchResponse> call, Response<OneTouchResponse> response) {
                DialogManager.hideProgress();
                Log.e("Sucess","Sucess"+response.isSuccessful())   ;
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    baseActivity.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<OneTouchResponse> call, Throwable throwableMsg) {
                DialogManager.hideProgress();
                Log.e("onFailure","onFailure")   ;
                baseActivity.onRequestFailure(throwableMsg);
            }
        });

    }


    public void SendSms(String userid,String ph1,String message,
                              final BaseActivity baseActivity) {

        DialogManager.showProgress(baseActivity);



        mServiceInterface.sendsms(userid,ph1,message).enqueue(new Callback<AlarmObject>() {
            @Override
            public void onResponse(Call<AlarmObject> call, Response<AlarmObject> response) {
                DialogManager.hideProgress();
                Log.e("Sucess","Sucess"+response.isSuccessful())   ;
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    baseActivity.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<AlarmObject> call, Throwable throwableMsg) {
                DialogManager.hideProgress();
                Log.e("onFailure","onFailure")   ;
                baseActivity.onRequestFailure(throwableMsg);
            }
        });

    }


    public void Writereview_api(String reviewcomment,String user_id,String shop_id,String rating,
                        final BaseFragment baseActivity) {

        DialogManager.showProgress(baseActivity.getActivity());

        mServiceInterface.write_review(reviewcomment,user_id,shop_id,rating).enqueue(new Callback<WriteReviewEntity>() {
            @Override
            public void onResponse(Call<WriteReviewEntity> call, Response<WriteReviewEntity> response) {
                DialogManager.hideProgress();
                Log.e("Sucess","Sucess"+response.isSuccessful())   ;
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    baseActivity.onRequestFailure(new Throwable(response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<WriteReviewEntity> call, Throwable throwableMsg) {
                DialogManager.hideProgress();
                Log.e("onFailure","onFailure");
                baseActivity.onRequestFailure(throwableMsg);
            }
        });

    }


    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
