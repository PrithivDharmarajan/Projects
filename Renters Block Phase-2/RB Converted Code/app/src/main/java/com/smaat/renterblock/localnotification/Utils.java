package com.smaat.renterblock.localnotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.smaat.renterblock.R;
import com.smaat.renterblock.scheduling.SchedulingActivity;

public class Utils {

	public static NotificationManager mManager;
	public static String notification_text;

	//@SuppressWarnings("static-access")
	/*public static void generateNotification(Context context, Intent intent) {

		mManager = (NotificationManager) context
				.getSystemService(context.NOTIFICATION_SERVICE);
		Intent intent1 = new Intent(context, SchedulingActivity.class);
		notification_text = intent.getStringExtra("schedule_text");
		Notification notification = new Notification(R.drawable.app_icon,
				notification_text, System.currentTimeMillis());
		intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendingNotificationIntent = PendingIntent.getActivity(
				context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(context, "Renters Block",
				notification_text, pendingNotificationIntent);
		mManager.notify(0, notification);
	}*/
}
