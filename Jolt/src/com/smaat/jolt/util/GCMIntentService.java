package com.smaat.jolt.util;

import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.smaat.jolt.R;
import com.smaat.jolt.ui.SplashScreen;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";
	private JSONObject json;
	private static String message = "";
	String type = "";

	public GCMIntentService() {
		super(AppConstants.SENDER_ID);
	}

	/**
	 * Method called on device registered
	 **/
	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);

		Log.d("NAME", "Registerd");

		GlobalMethods.storeValuetoPreference(context,
				GlobalMethods.STRING_PREFERENCE, AppConstants.DEVICE_ID,
				registrationId);

		GlobalMethods.storeValuetoPreference(context,
				GlobalMethods.BOOLEAN_PREFERENCE, AppConstants.ISREGISTERED,
				false);

		GlobalMethods.storeValuetoPreference(context,
				GlobalMethods.BOOLEAN_PREFERENCE,
				AppConstants.ISDEVICEIDCHANGED, AppConstants.DEVICE_ID
						.equalsIgnoreCase(registrationId) ? false : true);

	}

	/**
	 * Method called on device un registred
	 * */
	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");

	}

	/**
	 * Method called on Receiving a new message
	 * */
	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.i(TAG, "Received message");

		System.out.println("data:" + intent.getExtras());
		String intentStr = intent.getExtras().getString(getString(R.string.message));
		try {
			json = new JSONObject(intentStr);
			message = json.getString(getString(R.string.msg));
			type = json.getString(getString(R.string.type));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		generateNotification(context, message);
	}

	/**
	 * Method called on receiving a deleted message
	 * */
	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");

	}

	/**
	 * Method called on Error
	 * */
	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);

	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);

		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	@SuppressWarnings("deprecation")
	private static void generateNotification(Context context, String message) {

		Random r = new Random();
		int notifiId = r.nextInt();

		int icon = R.drawable.jolt_app_icon;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, message, when);

		String title = context.getString(R.string.app_name);

		Class<?> target;

		target = SplashScreen.class;
		Intent notificationIntent = new Intent(context, target);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);

	
		// notificationIntent.putExtra("names",
		// message.substring(message.lastIndexOf(" ") + 1));
		// notificationIntent.putExtra("groupId", group_id);
		// notificationIntent.putExtra("type", "group");
		// notificationIntent.putExtra("enhanced_profile_ids", "1");
		// notificationIntent.putExtra("from_call", "gcm");
		//
		// notificationIntent.putExtra("CallFrom", "GCM");
		// notificationIntent.putExtra("PropertyId", id);
		// notificationIntent.putExtra("PropType", "");
		// notificationIntent.putExtra("ScheduleID", id);
		PendingIntent intent = PendingIntent.getActivity(context,
				AppConstants.com_smaat_jolt.hashCode(), notificationIntent,
				Intent.FLAG_ACTIVITY_NEW_TASK);
		notification.setLatestEventInfo(context, title, message, intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notificationManager.notify(notifiId, notification);
	}
}
