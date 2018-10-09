package com.smaat.renterblock;

import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.smaat.renterblock.friends.ui.FriendChatScreen;
import com.smaat.renterblock.friends.ui.FriendsPendingRequest;
import com.smaat.renterblock.hotleads.ui.LeadsActivity;
import com.smaat.renterblock.scheduling.SchedulingActivity;
import com.smaat.renterblock.ui.CallChatActivity;
import com.smaat.renterblock.ui.ProfileScreen;
import com.smaat.renterblock.ui.PropertyDetailsActivity;
import com.smaat.renterblock.ui.SplashScreen;
import com.smaat.renterblock.util.AppConstants;

public class PushNotificationReceiver extends WakefulBroadcastReceiver {
	private static final String TAG = "GCMIntentService";
	private JSONObject json;
	private static String message = "", type = "", id = "", alert_msg = "",
			notification_id = "", chat_message = "", group_id = "";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.i(TAG, "Received message");

		System.out.println("data:" + intent.getExtras());
		String intentStr = intent.getExtras().getString("message");

		if (intentStr != null && !intentStr.equalsIgnoreCase("")) {

			try {
				json = new JSONObject(intentStr);
				message = json.getString("msg");
				type = json.getString("type");
				if(type.equalsIgnoreCase("friendchat")) {
					group_id = json.getString("group_id");
				}
				id = json.getString("id");
				notification_id = json.getString("notification_user_id");
				chat_message = json.getString("message");
				group_id = json.getString("group_id");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			if (type.equalsIgnoreCase("video")) {
				AppConstants.push_notification_call = "true";
				Intent intent1 = new Intent()
						.setClass(context.getApplicationContext(),
								CallChatActivity.class);
				intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent1.putExtra("msg", message);
				intent1.putExtra("type", type);
				intent1.putExtra("chat_id", id);
				context.startActivity(intent1);
			} else if (type.equalsIgnoreCase("voice")) {
				AppConstants.push_notification_call = "true";
				Intent intent1 = new Intent()
						.setClass(context.getApplicationContext(),
								CallChatActivity.class);
				intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent1.putExtra("msg", message);
				intent1.putExtra("type", type);
				intent1.putExtra("chat_id", id);
				context.startActivity(intent1);
			}
			else {
				//generateNotification(context, message);
			}
		}
	}

	/*private static void generateNotification(Context context, String message) {

		Random r = new Random();
		int notifiId = r.nextInt();

		int icon = R.drawable.app_icon;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, message, when);

		String title = context.getString(R.string.app_name);

		Class<?> target;
		AppConstants.push_notification_call = "true";
		if (type.equalsIgnoreCase("friend")) {
			target = FriendsPendingRequest.class;
		} else if (type.equalsIgnoreCase("alertproperty")
				|| type.equalsIgnoreCase("savedproperty")) {
			target = PropertyDetailsActivity.class;
		} else if (type.equalsIgnoreCase("schedule")) {
			target = SchedulingActivity.class;
		} else if (type.equalsIgnoreCase("request")) {
			target = LeadsActivity.class;
		} else if (type.equalsIgnoreCase("request to agent")) {
			target = ProfileScreen.class;
		} else if (type.equalsIgnoreCase("hotleadchat")) {
			target = FriendChatScreen.class;
			AppConstants.GCM_chat_message = "false";
		} else if (type.equalsIgnoreCase("friendchat")) {
			target = FriendChatScreen.class;
			AppConstants.GCM_chat_message = "true";
		} else {
			target = SplashScreen.class;
		}
		Intent notificationIntent = new Intent(context, target);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);

		notificationIntent.putExtra("ids", id);
		notificationIntent.putExtra("names",
				message.substring(message.lastIndexOf(" ") + 1));
		notificationIntent.putExtra("groupId", group_id);
		notificationIntent.putExtra("type", "group");
		notificationIntent.putExtra("enhanced_profile_ids", "1");
		notificationIntent.putExtra("from_call", "gcm");
		notificationIntent.putExtra("user_id", id);
		notificationIntent.putExtra("CallFrom", "GCM");
		notificationIntent.putExtra("PropertyId", id);
		notificationIntent.putExtra("PropType", "");
		notificationIntent.putExtra("ScheduleID", id);
		PendingIntent intent = PendingIntent.getActivity(context,
				"com.smaat.rentersblock".hashCode(), notificationIntent,
				Intent.FLAG_ACTIVITY_NEW_TASK);
		notification.setLatestEventInfo(context, title, message, intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notificationManager.notify(notifiId, notification);
	}*/

}