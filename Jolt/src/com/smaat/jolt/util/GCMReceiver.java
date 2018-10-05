package com.smaat.jolt.util;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.smaat.jolt.R;
import com.smaat.jolt.ui.HomeScreen;
import com.smaat.jolt.ui.JOLTApplication;

public class GCMReceiver extends WakefulBroadcastReceiver {
	private static final String TAG = "GCMIntentService";
	private JSONObject json;
	private static String message = "", type = "", info = "";

	@Override
	public void onReceive(Context context, Intent intent) {
		String regId = intent.getExtras().getString("registration_id");
		if (regId != null && !regId.equals("")) {
			System.out.println("DEVICEID: " + regId);
			GlobalMethods.storeValuetoPreference(context,
					GlobalMethods.STRING_PREFERENCE, AppConstants.DEVICE_ID,
					regId);

			GlobalMethods.storeValuetoPreference(context,
					GlobalMethods.BOOLEAN_PREFERENCE,
					AppConstants.ISREGISTERED, false);

			GlobalMethods.storeValuetoPreference(context,
					GlobalMethods.BOOLEAN_PREFERENCE,
					AppConstants.ISDEVICEIDCHANGED, AppConstants.DEVICE_ID
							.equalsIgnoreCase(regId) ? false : true);
		}
		Log.i(TAG, "Received message");
		message = "";
		String intentStr = intent.getExtras().getString(context.getString(R.string.message));
		if (intentStr != null) {
			try {
				json = new JSONObject(intentStr);
				message = json.getString(context.getString(R.string.msg));
				type = json.getString(context.getString(R.string.type));
				info = json.getString(context.getString(R.string.info));
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		if (message != null && !message.equalsIgnoreCase("")) {
			if (JOLTApplication.isActivityVisible()) {
				HomeScreen.displayNotifyDialog(message, type,info);
			} else {
				generateNotification(context, message);
			}
			message = "";
		}
	}

	@SuppressWarnings("deprecation")
	private static void generateNotification(Context context, String message) {
		int icon = R.drawable.jolt_app_icon;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, message, when);
		String title = context.getString(R.string.app_name);
		Intent notificationIntent = new Intent(context, HomeScreen.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		notificationIntent.putExtra(context.getString(R.string.message), message);
		notificationIntent.putExtra(context.getString(R.string.type), type);
		notificationIntent.putExtra(context.getString(R.string.info), info);

		PendingIntent intent = PendingIntent.getActivity(context,
				AppConstants.com_smaat_jolt.hashCode(), notificationIntent,
				Intent.FLAG_ACTIVITY_NEW_TASK);
		notification.setLatestEventInfo(context, title, message, intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notificationManager.notify(0, notification);
	}

}
