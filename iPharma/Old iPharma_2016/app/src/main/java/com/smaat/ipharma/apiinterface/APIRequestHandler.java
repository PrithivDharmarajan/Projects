package com.smaat.ipharma.apiinterface;

import com.smaat.ipharma.entity.AdsEntity;
import com.smaat.ipharma.entity.ChatCommonEntity;
import com.smaat.ipharma.entity.FavouriteCommonResponse;
import com.smaat.ipharma.entity.ForgotResponse;
import com.smaat.ipharma.entity.NewOrderEntity;
import com.smaat.ipharma.entity.OrderHistoryCommonResponseEntity;
import com.smaat.ipharma.entity.OtpEntity;
import com.smaat.ipharma.entity.RateResponseEntity;
import com.smaat.ipharma.entity.ShowReviewResponse;
import com.smaat.ipharma.entity.TermsConditionsEntity;
import com.smaat.ipharma.entity.WriteReviewEntity;
import com.smaat.ipharma.main.BaseActivity;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.model.MapResponse;
import com.smaat.ipharma.model.UpdateResponse;
import com.smaat.ipharma.util.AppConstants;
import com.smaat.ipharma.util.DialogManager;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class APIRequestHandler {

	RestAdapter mRestAdapter = new RestAdapter.Builder().setEndpoint(
			AppConstants.Base_Url).build();
	APICommonInterface mInterface = mRestAdapter.create(APICommonInterface.class);
	private static final APIRequestHandler mInstance = new APIRequestHandler();

	public static APIRequestHandler getInstance() {
		return mInstance;
	}

	private APIRequestHandler() {

	}

	public void getSearchPharmacies(String mUserID, String mLatitude,
			String mLongitude, String mSearchKeys, float mDistance,
			final BaseFragment mFragment) {

//		 DialogManager.showProgress(mFragment.getActivity());
		mInterface.getSearchPharmacies(mUserID, mLatitude, mLongitude,
				mSearchKeys, mDistance, new Callback<MapResponse>() {

					@Override
					public void success(MapResponse mMapResponse, Response arg1) {
						DialogManager.hideProgress(mFragment.getActivity());

						if (mMapResponse != null) {
							Object obj = mMapResponse;
							mFragment.onRequestSuccess(obj);
						}

					}

					@Override
					public void failure(RetrofitError retrofitError) {
						DialogManager.hideProgress(mFragment.getActivity());
						mFragment.onRequestFailure(retrofitError);
					}
				});
	}

	public void getSearchPharmacies(String mUserID, String mLatitude,
			String mLongitude, String mSearchKeys, float mDistance,
			boolean mTemp, final BaseFragment mFragment) {

//		DialogManager.showProgress(mFragment.getActivity());
		mInterface.getSearchPharmacies(mUserID, mLatitude, mLongitude,
				mSearchKeys, mDistance, new Callback<MapResponse>() {

					@Override
					public void success(MapResponse mMapResponse, Response arg1) {
						DialogManager.hideProgress(mFragment.getActivity());

						if (mMapResponse != null) {
							Object obj = mMapResponse;
							mFragment.onRequestSuccess(obj);
						}

					}

					@Override
					public void failure(RetrofitError retrofitError) {
						DialogManager.hideProgress(mFragment.getActivity());
						mFragment.onRequestFailure(retrofitError);
					}
				});
	}

	public void getupdateprofile(String mUserID, String mUserName,
			String mEmailId, String mPhoneTxt, String mAddressTxt,
			String mCityTxt, String mAreaTxt, String mPinCode,
			String mPassword, final BaseFragment mFragment) {

		DialogManager.showProgress(mFragment.getActivity());
		mInterface.getupdateprofile(mUserID, mUserName, mEmailId, mPhoneTxt,
				mAddressTxt, mCityTxt, mAreaTxt, mPinCode, mPassword,
				new Callback<UpdateResponse>() {

					@Override
					public void success(UpdateResponse mUpdateResponse,
							Response arg1) {

						if (mUpdateResponse != null) {
							Object obj = mUpdateResponse;
							mFragment.onRequestSuccess(obj);
						}

					}

					@Override
					public void failure(RetrofitError retrofitError) {
						mFragment.onRequestFailure(retrofitError);
					}
				});
	}

	public void getAds(String latitude, String longitude,
			final BaseFragment mFragment) {

		mInterface.getAds(latitude, longitude, new Callback<AdsEntity>() {

			@Override
			public void success(AdsEntity mAdsResponse, Response arg1) {
				DialogManager.hideProgress(mFragment.getActivity());

				if (mAdsResponse != null) {
					Object obj = mAdsResponse;
					mFragment.onRequestSuccess(obj);
				}

			}

			@Override
			public void failure(RetrofitError retrofitError) {
				DialogManager.hideProgress(mFragment.getActivity());
				mFragment.onRequestFailure(retrofitError);
			}
		});
	}

	public void getFavouriteShops(String mUserID, String mLatitude,
			String mLongitude, final BaseFragment mFragment) {

		DialogManager.showProgress(mFragment.getActivity());
		mInterface.getFavouriteShops(mUserID, mLatitude, mLongitude,
				new Callback<FavouriteCommonResponse>() {

					@Override
					public void success(
							FavouriteCommonResponse mFavouriteShopsResponse,
							Response arg1) {
						DialogManager.hideProgress(mFragment.getActivity());

						if (mFavouriteShopsResponse != null) {
							Object obj = mFavouriteShopsResponse;
							mFragment.onRequestSuccess(obj);
						}

					}

					@Override
					public void failure(RetrofitError retrofitError) {
						DialogManager.hideProgress(mFragment.getActivity());
						mFragment.onRequestFailure(retrofitError);
					}
				});
	}

	public void writeReview(String mReview, String mUserID, String mShopID,
			final BaseFragment mFragment) {

		DialogManager.showProgress(mFragment.getActivity());
		mInterface.writeReview(mReview, mUserID, mShopID,
				new Callback<WriteReviewEntity>() {

					@Override
					public void success(WriteReviewEntity mWriteReviewResponse,
							Response arg1) {
						DialogManager.hideProgress(mFragment.getActivity());

						if (mWriteReviewResponse != null) {
							Object obj = mWriteReviewResponse;
							mFragment.onRequestSuccess(obj);
						}

					}

					@Override
					public void failure(RetrofitError retrofitError) {
						DialogManager.hideProgress(mFragment.getActivity());
						mFragment.onRequestFailure(retrofitError);
					}
				});
	}

	public void showReview(String mShopID, final BaseFragment mFragment) {

		DialogManager.showProgress(mFragment.getActivity());
		mInterface.showReview(mShopID, new Callback<ShowReviewResponse>() {

			@Override
			public void success(ShowReviewResponse mShowReviewResponse,
					Response arg1) {
				DialogManager.hideProgress(mFragment.getActivity());

				if (mShowReviewResponse != null) {
					Object obj = mShowReviewResponse;
					mFragment.onRequestSuccess(obj);
				}

			}

			@Override
			public void failure(RetrofitError retrofitError) {
				DialogManager.hideProgress(mFragment.getActivity());
				mFragment.onRequestFailure(retrofitError);
			}
		});
	}

	public void addFavourite(String mShopID, String mUserID, String mFavStatus,
			final BaseFragment mFragment) {

		DialogManager.showProgress(mFragment.getActivity());
		mInterface.addFavourite(mShopID, mUserID, mFavStatus,
				new Callback<WriteReviewEntity>() {

					@Override
					public void success(WriteReviewEntity mWriteReviewResponse,
							Response arg1) {
						DialogManager.hideProgress(mFragment.getActivity());

						if (mWriteReviewResponse != null) {
							Object obj = mWriteReviewResponse;
							mFragment.onRequestSuccess(obj);
						}

					}

					@Override
					public void failure(RetrofitError retrofitError) {
						DialogManager.hideProgress(mFragment.getActivity());
						mFragment.onRequestFailure(retrofitError);
					}
				});
	}

	public void shopRating(String mShopID, String mUserID, String mRating,
			final BaseFragment mFragment) {

		DialogManager.showProgress(mFragment.getActivity());
		mInterface.shopRating(mShopID, mUserID, mRating,
				new Callback<RateResponseEntity>() {

					@Override
					public void success(RateResponseEntity mRatingResponse,
							Response arg1) {
						DialogManager.hideProgress(mFragment.getActivity());

						if (mRatingResponse != null) {
							Object obj = mRatingResponse;
							mFragment.onRequestSuccess(obj);
						}

					}

					@Override
					public void failure(RetrofitError retrofitError) {
						DialogManager.hideProgress(mFragment.getActivity());
						mFragment.onRequestFailure(retrofitError);
					}
				});
	}

	public void newOrder(String mShopID, String mUserID,
			String mDeliveryAddress, String mNewAddress, String mOrderNote,
			TypedFile mPrecImage, final BaseFragment mFragment) {

		DialogManager.showProgress(mFragment.getActivity());
		mInterface.newOrder(mShopID, mUserID, mDeliveryAddress, mNewAddress,
				mOrderNote, mPrecImage, new Callback<NewOrderEntity>() {

					@Override
					public void success(NewOrderEntity mNewOrderResponse,
							Response arg1) {
						DialogManager.hideProgress(mFragment.getActivity());
						if (mNewOrderResponse != null) {
							Object obj = mNewOrderResponse;
							mFragment.onRequestSuccess(obj);
						}

					}

					@Override
					public void failure(RetrofitError retrofitError) {
						// TODO Auto-generated method stub
						DialogManager.hideProgress(mFragment.getActivity());
						mFragment.onRequestFailure(retrofitError);
					}
				});
	}

	public void reOrder(String mShopID, String mUserID,
			String mDeliveryAddress, String mNewAddress, String mPrecID,
			String mOrderNote, final BaseFragment mFragment) {

		DialogManager.showProgress(mFragment.getActivity());
		mInterface.reOrder(mShopID, mUserID, mDeliveryAddress, mNewAddress,
				mPrecID, mOrderNote, new Callback<NewOrderEntity>() {

					@Override
					public void success(NewOrderEntity mReOrderResponse,
							Response arg1) {
						// TODO Auto-generated method stub
						DialogManager.hideProgress(mFragment.getActivity());
						if (mReOrderResponse != null) {
							Object obj = mReOrderResponse;
							mFragment.onRequestSuccess(obj);
						}
					}

					@Override
					public void failure(RetrofitError retrofitError) {
						// TODO Auto-generated method stub
						DialogManager.hideProgress(mFragment.getActivity());
						mFragment.onRequestFailure(retrofitError);
					}
				});
	}

	public void getMyOrders(String UserID, final BaseFragment mFragment) {

		DialogManager.showProgress(mFragment.getActivity());
		mInterface.getMyOrders(UserID,
				new Callback<OrderHistoryCommonResponseEntity>() {

					@Override
					public void success(
							OrderHistoryCommonResponseEntity mOrderHistoryCommonResponse,
							Response arg1) {
						// TODO Auto-generated method stub
						DialogManager.hideProgress(mFragment.getActivity());
						if (mOrderHistoryCommonResponse != null) {
							Object obj = mOrderHistoryCommonResponse;
							mFragment.onRequestSuccess(obj);
						}
					}

					@Override
					public void failure(RetrofitError retrofitError) {
						// TODO Auto-generated method stub
						DialogManager.hideProgress(mFragment.getActivity());
						mFragment.onRequestFailure(retrofitError);
					}
				});
	}

	public void getOffers(String mUserID, String mLatitude, String mLongitude,
			String mSearchKey, float mDistance, final BaseFragment mFragment) {

		DialogManager.showProgress(mFragment.getActivity());
		mInterface.getOffers(mUserID, mLatitude, mLongitude, mSearchKey,
				mDistance, new Callback<MapResponse>() {

					@Override
					public void success(MapResponse mMapResponse, Response arg1) {
						// TODO Auto-generated method stub
						DialogManager.hideProgress(mFragment.getActivity());
						if (mMapResponse != null) {
							Object obj = mMapResponse;
							mFragment.onRequestSuccess(obj);
						}
					}

					@Override
					public void failure(RetrofitError retrofitError) {
						// TODO Auto-generated method stub
						DialogManager.hideProgress(mFragment.getActivity());
						mFragment.onRequestFailure(retrofitError);
					}

				});
	}

	public void getTermsandConditions(String mUserID,
			final BaseActivity mActivity) {

		DialogManager.showProgress(mActivity);
		mInterface.getTermsandConditions(mUserID,
				new Callback<TermsConditionsEntity>() {

					@Override
					public void success(
							TermsConditionsEntity mTermsConditionsResponse,
							Response arg1) {
						// TODO Auto-generated method stub
						DialogManager.hideProgress(mActivity);
						if (mTermsConditionsResponse != null) {
							Object obj = mTermsConditionsResponse;
							mActivity.onRequestSuccess(obj);

						}

					}

					@Override
					public void failure(RetrofitError retrofitError) {
						// TODO Auto-generated method stub
						DialogManager.hideProgress(mActivity);
						mActivity.onRequestFailure(retrofitError);
					}

				});

	}

	// otp confirmation

	public void getOTPconfirmtion(String mUserID, String mOTP,
			final BaseActivity mActivity) {

		DialogManager.showProgress(mActivity);
		mInterface.getOtpconfirmation(mUserID, mOTP, new Callback<OtpEntity>() {

			@Override
			public void success(OtpEntity mOtpResponse, Response arg1) {
				// TODO Auto-generated method stub
				DialogManager.hideProgress(mActivity);
				if (mOtpResponse != null) {
					Object obj = mOtpResponse;
					mActivity.onRequestSuccess(obj);

				}
			}

			@Override
			public void failure(RetrofitError retrofitError) {
				// TODO Auto-generated method stub
				DialogManager.hideProgress(mActivity);
				mActivity.onRequestFailure(retrofitError);
			}

		});

	}

	// Resend otp confirmation

	public void getResendOTPconfirmtion(String mUserID,
			final BaseActivity mActivity) {

		DialogManager.showProgress(mActivity);
		mInterface.getresendotpconfirmation(mUserID, new Callback<OtpEntity>() {

			@Override
			public void success(OtpEntity mOtpResponse, Response arg1) {
				// TODO Auto-generated method stub
				DialogManager.hideProgress(mActivity);
				if (mOtpResponse != null) {
					Object obj = mOtpResponse;
					mActivity.onRequestSuccess(obj);

				}

			}

			@Override
			public void failure(RetrofitError retrofitError) {
				// TODO Auto-generated method stub
				DialogManager.hideProgress(mActivity);
				mActivity.onRequestFailure(retrofitError);
			}

		});

	}

	public void getChathistory(String mUserID, final BaseFragment mFragment) {

		DialogManager.showProgress(mFragment.getActivity());
		mInterface.getChathistory(mUserID, new Callback<ChatCommonEntity>() {

			@Override
			public void success(ChatCommonEntity mChatCommonResponse,
					Response arg1) {
				// TODO Auto-generated method stub
				DialogManager.hideProgress(mFragment.getActivity());
				if (mChatCommonResponse != null) {
					Object obj = mChatCommonResponse;
					mFragment.onRequestSuccess(obj);
				}
			}

			@Override
			public void failure(RetrofitError retrofitError) {
				// TODO Auto-generated method stub
				DialogManager.hideProgress(mFragment.getActivity());
				mFragment.onRequestFailure(retrofitError);
			}
		});
	}

	public void getSendChat(String mUserID, String mMessage,
			final BaseFragment mFragment) {

		DialogManager.showProgress(mFragment.getActivity());
		mInterface.getSendChat(mUserID, mMessage,
				new Callback<ChatCommonEntity>() {

					@Override
					public void success(ChatCommonEntity mChatCommonResponse,
							Response arg1) {
						// TODO Auto-generated method stub
						DialogManager.hideProgress(mFragment.getActivity());
						if (mChatCommonResponse != null) {
							Object obj = mChatCommonResponse;
							mFragment.onRequestSuccess(obj);
						}
					}

					@Override
					public void failure(RetrofitError retrofitError) {
						// TODO Auto-generated method stub
						DialogManager.hideProgress(mFragment.getActivity());
						mFragment.onRequestFailure(retrofitError);

					}
				});
	}

	public void getForgotPasasword(String mUserName,
			final BaseActivity mFragment) {

		DialogManager.showProgress(mFragment);
		mInterface.getForgotPasasword(mUserName,
				new Callback<ForgotResponse>() {

					@Override
					public void success(ForgotResponse mForgotPwdResponse,
							Response arg1) {
						DialogManager.hideProgress(mFragment);
						if (mForgotPwdResponse != null) {
							Object obj = mForgotPwdResponse;
							mFragment.onRequestSuccess(obj);
						} else {
						}
					}

					@Override
					public void failure(RetrofitError retrofitError) {
						DialogManager.hideProgress(mFragment);
						mFragment.onRequestFailure(retrofitError);

					}
				});
	}

	public void getOrderPaymentUpdate(String UserID, String OrderID,
			String PaymentAccept, String PaymentMode,
			final BaseFragment mFragment) {

		DialogManager.showProgress(mFragment.getActivity());
		mInterface.getOrderPaymentUpdate(UserID, OrderID, PaymentAccept,
				PaymentMode, new Callback<OtpEntity>() {

					@Override
					public void success(
							OtpEntity mOrderPaymentUpdateResponse,
							Response arg1) {
						DialogManager.hideProgress(mFragment.getActivity());
						if (mOrderPaymentUpdateResponse != null) {
							Object obj = mOrderPaymentUpdateResponse;
							mFragment.onRequestSuccess(obj);
						} else {
						}
					}

					@Override
					public void failure(RetrofitError retrofitError) {
						DialogManager.hideProgress(mFragment.getActivity());
						mFragment.onRequestFailure(retrofitError);

					}
				});
	}

}
