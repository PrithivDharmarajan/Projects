package com.smaat.renterblock.util;

import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.Transformer;
import com.google.gson.Gson;

public class ServieRequestHandler {

	private static final ServieRequestHandler instance = new ServieRequestHandler();

	public static ServieRequestHandler getInstance() {
		return instance;
	}

	private ServieRequestHandler() {

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
	
}