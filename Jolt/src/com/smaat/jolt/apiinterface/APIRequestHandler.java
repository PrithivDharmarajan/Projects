package com.smaat.jolt.apiinterface;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import com.smaat.jolt.model.AddCreditCardResponse;
import com.smaat.jolt.model.ConversationResponse;
import com.smaat.jolt.model.FAQResponse;
import com.smaat.jolt.model.GlobalResponse;
import com.smaat.jolt.model.JoltPlansResponse;
import com.smaat.jolt.model.MessageDetailsResponse;
import com.smaat.jolt.model.MessageResponse;
import com.smaat.jolt.model.MyHistoryResponse;
import com.smaat.jolt.model.ProfileResponse;
import com.smaat.jolt.model.PromoCouponResponse;
import com.smaat.jolt.model.ShopListResponse;
import com.smaat.jolt.model.TermsResponse;
import com.smaat.jolt.ui.BaseFragment;
import com.smaat.jolt.util.AppConstants;
import com.smaat.jolt.util.DialogManager;

public class APIRequestHandler {

	RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(
			AppConstants.BASE_URL).build();

	APICommonInterface interfaces = restAdapter
			.create(APICommonInterface.class);

	private static final APIRequestHandler instance = new APIRequestHandler();

	public static APIRequestHandler getInstance() {
		return instance;
	}

	private APIRequestHandler() {

	}

	public void getShopList(String userId, String latitude, String longtitude,
			String start, String limit, final BaseFragment fragment) {

		DialogManager.showProgress(fragment.getActivity());
		interfaces.getShopList(userId, latitude, longtitude, start, limit,
				new Callback<ShopListResponse>() {

					@Override
					public void success(

					ShopListResponse shopListResponse, Response arg1) {
						DialogManager.hideProgress(fragment.getActivity());

						if (shopListResponse != null) {
							Object obj = shopListResponse;
							fragment.onRequestSuccess(obj);
						}

					}

					@Override
					public void failure(RetrofitError retrofitError) {
						DialogManager.hideProgress(fragment.getActivity());
						fragment.onRequestFailure(retrofitError);
					}
				});
	}

	public void getJoltPlans(String userId, final BaseFragment fragment) {

		DialogManager.showProgress(fragment.getActivity());
		interfaces.getJoltPlans(userId, new Callback<JoltPlansResponse>() {

			@Override
			public void success(

			JoltPlansResponse joltPlansResponse, Response arg1) {
				DialogManager.hideProgress(fragment.getActivity());

				Object obj = joltPlansResponse;
				fragment.onRequestSuccess(obj);

			}

			@Override
			public void failure(RetrofitError retrofitError) {
				DialogManager.hideProgress(fragment.getActivity());
				fragment.onRequestFailure(retrofitError);
			}
		});
	}

	public void getMyProfile(String userId, final BaseFragment fragment) {

		DialogManager.showProgress(fragment.getActivity());
		interfaces.getMyProfile(userId, new Callback<ProfileResponse>() {

			@Override
			public void success(

			ProfileResponse response, Response arg1) {
				DialogManager.hideProgress(fragment.getActivity());

				Object obj = response;
				fragment.onRequestSuccess(obj);

			}

			@Override
			public void failure(RetrofitError retrofitError) {
				DialogManager.hideProgress(fragment.getActivity());
				fragment.onRequestFailure(retrofitError);
			}
		});
	}

	public void addUpdateCreditCard(String userId, String cardNumber,
			String expiryMonth, String expiryYear, String cvv,
			final BaseFragment fragment) {

		DialogManager.showProgress(fragment.getActivity());
		interfaces.addUpdateCreditCard(userId, cardNumber, expiryMonth,
				expiryYear, cvv, new Callback<AddCreditCardResponse>() {

					@Override
					public void success(

					AddCreditCardResponse creditCardResponse, Response arg1) {
						DialogManager.hideProgress(fragment.getActivity());

						Object obj = creditCardResponse;
						fragment.onRequestSuccess(obj);

					}

					@Override
					public void failure(RetrofitError retrofitError) {
						DialogManager.hideProgress(fragment.getActivity());
						fragment.onRequestFailure(retrofitError);
					}
				});
	}

	public void validateCouponCode(String userId, String couponCode,
			final BaseFragment fragment) {

		DialogManager.showProgress(fragment.getActivity());
		interfaces.validateCoupon(userId, couponCode,
				new Callback<GlobalResponse>() {

					@Override
					public void success(

					GlobalResponse globalResponse, Response arg1) {

						DialogManager.hideProgress(fragment.getActivity());
						Object obj = globalResponse;
						fragment.onRequestSuccess(obj);

					}

					@Override
					public void failure(RetrofitError retrofitError) {
						DialogManager.hideProgress(fragment.getActivity());
						fragment.onRequestFailure(retrofitError);
					}
				});
	}

	public void validatePromoCouponCode(String userId,String couponCode,
			final BaseFragment fragment) {

		DialogManager.showProgress(fragment.getActivity());
		interfaces.validatePromoCoupon(userId,couponCode,
				new Callback<PromoCouponResponse>() {

					@Override
					public void success(

					PromoCouponResponse promoCoupon, Response arg1) {

						DialogManager.hideProgress(fragment.getActivity());
						Object obj = promoCoupon;
						fragment.onRequestSuccess(obj);

					}

					@Override
					public void failure(RetrofitError retrofitError) {
						DialogManager.hideProgress(fragment.getActivity());
						fragment.onRequestFailure(retrofitError);
					}
				});
	}

	public void setHowWasIt(String userId, String shopId, String comments,
			final BaseFragment fragment) {

		DialogManager.showProgress(fragment.getActivity());
		interfaces.setHowWasIt(userId, shopId, comments,
				new Callback<GlobalResponse>() {

					@Override
					public void success(

					GlobalResponse globalResponse, Response arg1) {

						DialogManager.hideProgress(fragment.getActivity());
						Object obj = globalResponse;
						fragment.onRequestSuccess(obj);

					}

					@Override
					public void failure(RetrofitError retrofitError) {
						DialogManager.hideProgress(fragment.getActivity());
						fragment.onRequestFailure(retrofitError);
					}
				});
	}

	public void getMyHistory(String userId, final BaseFragment fragment) {

		DialogManager.showProgress(fragment.getActivity());
		interfaces.getMyHistory(userId, new Callback<MyHistoryResponse>() {

			@Override
			public void success(

			MyHistoryResponse globalResponse, Response arg1) {

				DialogManager.hideProgress(fragment.getActivity());
				Object obj = globalResponse;
				fragment.onRequestSuccess(obj);

			}

			@Override
			public void failure(RetrofitError retrofitError) {
				DialogManager.hideProgress(fragment.getActivity());
				fragment.onRequestFailure(retrofitError);
			}
		});
	}

	public void getConversationList(String userId, final BaseFragment fragment) {

		DialogManager.showProgress(fragment.getActivity());
		interfaces.getConversationList(userId, new Callback<MessageResponse>() {

			@Override
			public void success(

			MessageResponse response, Response arg1) {

				DialogManager.hideProgress(fragment.getActivity());
				Object obj = response;
				fragment.onRequestSuccess(obj);

			}

			@Override
			public void failure(RetrofitError retrofitError) {
				DialogManager.hideProgress(fragment.getActivity());
				fragment.onRequestFailure(retrofitError);
			}
		});
	}

	public void getMessageDetails(String userId, String conversationId,
			final BaseFragment fragment) {

		DialogManager.showProgress(fragment.getActivity());
		interfaces.getMessageDetails(userId, conversationId,
				new Callback<MessageDetailsResponse>() {

					@Override
					public void success(

					MessageDetailsResponse response, Response arg1) {

						DialogManager.hideProgress(fragment.getActivity());
						Object obj = response;
						fragment.onRequestSuccess(obj);

					}

					@Override
					public void failure(RetrofitError retrofitError) {
						DialogManager.hideProgress(fragment.getActivity());
						fragment.onRequestFailure(retrofitError);
					}
				});
	}

	public void sendConversationMessage(String userId, String conversationId,
			String messageText, final BaseFragment fragment) {
		DialogManager.showProgress(fragment.getActivity());
		interfaces.sendConversationMessage(userId, messageText, conversationId,
				new Callback<ConversationResponse>() {

					@Override
					public void success(

					ConversationResponse response, Response arg1) {

						DialogManager.hideProgress(fragment.getActivity());
						Object obj = response;
						fragment.onRequestSuccess(obj);

					}

					@Override
					public void failure(RetrofitError retrofitError) {
						DialogManager.hideProgress(fragment.getActivity());
						fragment.onRequestFailure(retrofitError);
					}
				});
	}

	public void getFAQMessage(String userId, final BaseFragment fragment) {

		DialogManager.showProgress(fragment.getActivity());
		interfaces.getFaq(userId, new Callback<FAQResponse>() {

			@Override
			public void success(

			FAQResponse response, Response arg1) {
				DialogManager.hideProgress(fragment.getActivity());
				Object obj = response;
				fragment.onRequestSuccess(obj);

			}

			@Override
			public void failure(RetrofitError retrofitError) {
				DialogManager.hideProgress(fragment.getActivity());
				fragment.onRequestFailure(retrofitError);
			}
		});
	}

	public void getTermsAndConditions(String userId, final BaseFragment fragment) {

		DialogManager.showProgress(fragment.getActivity());
		interfaces.getTerms(userId, new Callback<TermsResponse>() {

			@Override
			public void success(

			TermsResponse response, Response arg1) {
				DialogManager.hideProgress(fragment.getActivity());
				Object obj = response;
				fragment.onRequestSuccess(obj);

			}

			@Override
			public void failure(RetrofitError retrofitError) {
				DialogManager.hideProgress(fragment.getActivity());
				fragment.onRequestFailure(retrofitError);
			}
		});
	}

}
