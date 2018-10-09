package com.smaat.renterblock.webservice;

import java.util.Map;

import org.json.simple.JSONObject;

import android.app.Activity;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.Transformer;
import com.google.gson.Gson;
import com.smaat.renterblock.model.BoardsResponse;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.model.FavouriteReponse;
import com.smaat.renterblock.model.ForgetPasswordResponse;
import com.smaat.renterblock.model.LoginResponse;
import com.smaat.renterblock.model.MyReviewResponse;
import com.smaat.renterblock.model.PropertyPostedUserDetails;
import com.smaat.renterblock.model.PropertyResponse;
import com.smaat.renterblock.model.RegisterResponse;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.ui.LoginActivity;
import com.smaat.renterblock.ui.MyReviewActivity;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.PlaceRespone;
import com.smaat.renterblock.util.WebserviceCallbackInterface;

public class ServiceRequestHandler {
	private static final ServiceRequestHandler instance = new ServiceRequestHandler();

	public static ServiceRequestHandler getInstance() {
		return instance;
	}

	private ServiceRequestHandler() {

	}

	public static class GsonTransformer implements Transformer {

		public <T> T transform(String url, Class<T> type, String encoding,
				byte[] data, AjaxStatus status) {
			Gson g = new Gson();
			return g.fromJson(new String(data), type);
		}
	}

	public void getResultPlaces(String url, AQuery aq, final Activity activity,
			Map<String, Object> params, final Class<?> reDirectActivity,
			final WebserviceCallbackInterface callback) {

		GsonTransformer t = new GsonTransformer();

		aq.transformer(t)

		.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			public void callback(String url, JSONObject profile,
					AjaxStatus status) {

				try {
					PlaceRespone obj = new Gson().fromJson(profile.toString(),
							PlaceRespone.class);

					callback.onRequestSuccess(obj);

				} catch (Exception e) {
					callback.onResponseError(status.getCode() + "");
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
						}
					});

				}

			}
		});

	}

	public void getLogin(String url, AQuery aq, final LoginActivity activity,
			Map<String, Object> params) {

		GsonTransformer t = new GsonTransformer();
		System.out.println("Url:" + url);

		for (Map.Entry<String, Object> entry : params.entrySet()) {
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
		aq.transformer(t)
				.progress(DialogManager.getProgressDialog(activity))
				.ajax(url, params, JSONObject.class,
						new AjaxCallback<JSONObject>() {
							public void callback(String url,
									JSONObject profile, AjaxStatus status) {

								if (profile != null) {

									try {
										Object obj = new Gson().fromJson(
												profile.toString(),
												LoginResponse.class);

										activity.onRequestSuccess(obj);
									} catch (Exception e) {
										e.printStackTrace();
										activity.onRequestFailure(null);
									}

								} else {
									activity.onRequestFailure(status.getCode()
											+ "");
								}

							}

						});
	}

	public void callRegisterService(String url, AQuery aq,
			final BaseActivity activity, Map<String, Object> params) {

		GsonTransformer t = new GsonTransformer();
		System.out.println("Url:" + url);

		// for (Map.Entry<String, Object> entry : params.entrySet()) {
		// System.out.println(entry.getKey() + ":" + entry.getValue());
		// }
		aq.transformer(t)
				.progress(DialogManager.getProgressDialog(activity))
				.ajax(url, params, JSONObject.class,
						new AjaxCallback<JSONObject>() {
							public void callback(String url,
									JSONObject profile, AjaxStatus status) {

								if (profile != null) {

									try {
										Object obj = new Gson().fromJson(
												profile.toString(),
												RegisterResponse.class);

										activity.onRequestSuccess(obj);
									} catch (Exception e) {
										e.printStackTrace();
										activity.onRequestFailure(null);
									}

								} else {
									activity.onRequestFailure(status.getCode()
											+ "");
								}

							}

						});
	}

	public void processForgotPassword(String url, AQuery aq,
			final BaseActivity activity, Map<String, Object> params) {

		GsonTransformer t = new GsonTransformer();
		System.out.println("Url:" + url);

		for (Map.Entry<String, Object> entry : params.entrySet()) {
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
		aq.transformer(t)
				.progress(DialogManager.getProgressDialog(activity))
				.ajax(url, params, JSONObject.class,
						new AjaxCallback<JSONObject>() {
							public void callback(String url,
									JSONObject profile, AjaxStatus status) {

								if (profile != null) {

									try {
										Object obj = new Gson().fromJson(
												profile.toString(),
												ForgetPasswordResponse.class);

										activity.onRequestSuccess(obj);
									} catch (Exception e) {
										e.printStackTrace();

										activity.onRequestFailure(null);
									}

								} else {
									activity.onRequestFailure(status.getCode()
											+ "");
								}

							}

						});
	}

	public void CommonService(String url, AQuery aq,
			final BaseActivity activity, Map<String, Object> params) {

		GsonTransformer t = new GsonTransformer();
		System.out.println("Url:" + url);

		for (Map.Entry<String, Object> entry : params.entrySet()) {
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
		aq.transformer(t)
				.progress(DialogManager.getProgressDialog(activity))
				.ajax(url, params, JSONObject.class,
						new AjaxCallback<JSONObject>() {
							public void callback(String url,
									JSONObject profile, AjaxStatus status) {

								if (profile != null) {

									try {
										Object obj = new Gson().fromJson(
												profile.toString(),
												CommonResponse.class);

										activity.onRequestSuccess(obj);
									} catch (Exception e) {
										activity.onRequestFailure(null);
									}

								} else {
									activity.onRequestFailure(status.getCode()
											+ "");
								}

							}

						});
	}

	public void callPropertyService(String url, AQuery aq,
			final BaseActivity activity, Map<String, Object> params) {

		aq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			public void callback(String url, JSONObject profile,
					AjaxStatus status) {

				if (profile != null) {

					try {
						PropertyResponse obj = new Gson().fromJson(
								profile.toString(), PropertyResponse.class);

						activity.onRequestSuccess(obj);
					} catch (Exception e) {
						e.printStackTrace();
						activity.onRequestFailure(null);
					}

				} else {
					activity.onRequestFailure(status.getCode() + "");
				}

			}

		});
	}

	public void callPropertyDetailService(String url, AQuery aq,
			final BaseActivity activity, Map<String, Object> params) {

		aq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			public void callback(String url, JSONObject profile,
					AjaxStatus status) {

				if (profile != null) {

					try {
						PropertyPostedUserDetails obj = new Gson().fromJson(
								profile.toString(),
								PropertyPostedUserDetails.class);

						activity.onRequestSuccess(obj);
					} catch (Exception e) {
						e.printStackTrace();
						activity.onRequestFailure(null);
					}

				} else {
					activity.onRequestFailure(status.getCode() + "");
				}

			}

		});
	}

	public void callMyReviews(String url, AQuery aq,
			final MyReviewActivity activity, Map<String, Object> params) {

		aq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			public void callback(String url, JSONObject profile,
					AjaxStatus status) {

				if (profile != null) {

					try {
						MyReviewResponse obj = new Gson().fromJson(
								profile.toString(), MyReviewResponse.class);

						activity.onRequestSuccess(obj);
					} catch (Exception e) {
						e.printStackTrace();
						activity.onRequestFailure(null);
					}

				} else {
					activity.onRequestFailure(status.getCode() + "");
				}

			}

		});
	}

	public void getFavouriteService(String url, AQuery aq,
			final BaseActivity activity, Map<String, Object> params) {

		GsonTransformer t = new GsonTransformer();
		System.out.println("Url:" + url);

		// for (Map.Entry<String, Object> entry : params.entrySet()) {
		// System.out.println(entry.getKey() + ":" + entry.getValue());
		// }
		aq.transformer(t)
				.progress(DialogManager.getProgressDialog(activity))
				.ajax(url, params, JSONObject.class,
						new AjaxCallback<JSONObject>() {
							public void callback(String url,
									JSONObject profile, AjaxStatus status) {

								if (profile != null) {

									try {
										FavouriteReponse obj = new Gson()
												.fromJson(profile.toString(),
														FavouriteReponse.class);

										activity.onRequestSuccess(obj);
									} catch (Exception e) {
										e.printStackTrace();
										activity.onRequestFailure(null);
									}

								} else {
									activity.onRequestFailure(status.getCode()
											+ "");
								}

							}

						});
	}

	public void getBoardsService(String url, AQuery aq,
			final BaseActivity activity, Map<String, Object> params) {

		GsonTransformer t = new GsonTransformer();
		System.out.println("Url:" + url);

		// for (Map.Entry<String, Object> entry : params.entrySet()) {
		// System.out.println(entry.getKey() + ":" + entry.getValue());
		// }
		aq.transformer(t)
				.progress(DialogManager.getProgressDialog(activity))
				.ajax(url, params, JSONObject.class,
						new AjaxCallback<JSONObject>() {
							public void callback(String url,
									JSONObject profile, AjaxStatus status) {

								if (profile != null) {

									try {
										BoardsResponse obj = new Gson()
												.fromJson(profile.toString(),
														BoardsResponse.class);

										activity.onRequestSuccess(obj);
									} catch (Exception e) {
										e.printStackTrace();
										activity.onRequestFailure(null);
									}

								} else {
									activity.onRequestFailure(status.getCode()
											+ "");
								}

							}

						});
	}

}
