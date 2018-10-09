package com.smaat.renterblock.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.smaat.renterblock.R;
import com.smaat.renterblock.friends.ui.FriendChatScreen;
import com.smaat.renterblock.friends.ui.FriendsPendingRequest;
import com.smaat.renterblock.hotleads.ui.LeadsActivity;
import com.smaat.renterblock.scheduling.SchedulingActivity;
import com.smaat.renterblock.ui.CallChatActivity;
import com.smaat.renterblock.ui.ProfileScreen;
import com.smaat.renterblock.ui.PropertyDetailsActivity;
import com.smaat.renterblock.ui.SplashScreen;
import com.smaat.renterblock.util.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;


public class FireBasePushMessagingService extends FirebaseMessagingService {
    private JSONObject json;
    private static String message = "", type = "", alert_msg = "",
            chat_message = "", group_id = "", notification_id;

    public void onMessageReceived(RemoteMessage remoteMessage) {
        String intentStr = "";
        intentStr = remoteMessage.getData().get("message");
//        generateNotification(getApplicationContext(), "Test");
//		Toast.makeText(this,"Test",Toast.LENGTH_LONG).show();
        if (intentStr != null && !intentStr.equalsIgnoreCase("")) {

            try {
                json = new JSONObject(intentStr);
                message = json.getString("msg");
                type = json.getString("type");
                if (type.equalsIgnoreCase("friendchat")) {
                    group_id = json.getString("group_id");
                }
//				if (json.getString("id") != null) {
//					id = json.getString("id");
//				}else{
//					id= (String) GlobalMethods.getValueFromPreference(
//							this, GlobalMethods.STRING_PREFERENCE,
//							AppConstants.pref_userId);
//				}
//				notification_id = json.getString("notification_user_id");
                chat_message = json.getString("msg");
                group_id = json.getString("group_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (type.equalsIgnoreCase("video")) {
                AppConstants.push_notification_call = "true";
                Intent intent1 = new Intent()
                        .setClass(this,
                                CallChatActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("msg", message);
                intent1.putExtra("type", type);
                intent1.putExtra("chat_id", "");
                startActivity(intent1);
            } else if (type.equalsIgnoreCase("voice")) {
                AppConstants.push_notification_call = "true";
                Intent intent1 = new Intent()
                        .setClass(this,
                                CallChatActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("msg", message);
                intent1.putExtra("type", type);
                intent1.putExtra("chat_id", "");
                startActivity(intent1);
            } else {
                generateNotification(this, message);

            }

        }
    }

    private static void generateNotification(Context context, String message) {

        int notifiId = generateRandom();

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

        notificationIntent.putExtra("ids", "");
        notificationIntent.putExtra("names",
                message.substring(message.lastIndexOf(" ") + 1));
        notificationIntent.putExtra("groupId", group_id);
        notificationIntent.putExtra("type", "group");
        notificationIntent.putExtra("enhanced_profile_ids", "1");
        notificationIntent.putExtra("from_call", "gcm");
        notificationIntent.putExtra("user_id", "");
        notificationIntent.putExtra("CallFrom", "GCM");
        notificationIntent.putExtra("PropertyId", "");
        notificationIntent.putExtra("PropType", "");
        notificationIntent.putExtra("ScheduleID", "");
//		PendingIntent intent = PendingIntent.getActivity(context,
//				"com.smaat.rentersblock".hashCode(), notificationIntent,
//				Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent intent = PendingIntent.getActivity(context,
                0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//		notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(notifiId, notification);
    }

    public static int generateRandom() {
        Random random = new Random();
        return random.nextInt(9999 - 1000) + 1000;
    }

//	private void sendChatNotification(String messageStr) {
//		Intent intent = new Intent(this, HomeScreen.class);
//		intent.putExtra(AppConstants.PUSH_CHAT_STATUS, AppConstants.SUCCESS_CODE);
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//		PendingIntent pendingIntent = PendingIntent.getActivity(this,
//				0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//		Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
//				this)
//				.setSmallIcon(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? R.mipmap.app_push_icon : R.mipmap.app_icon) //Small Icon from drawable
//				.setContentTitle(this.getString(R.string.app_name))
//				.setContentText(messageStr)
//				.setAutoCancel(true)
//				.setSound(defaultSoundUri)
//				.setColor(ContextCompat.getColor(this, R.color.sky_blue))
//				.setStyle(new android.support.v4.app.NotificationCompat.BigTextStyle().bigText(messageStr))
//				.setDefaults(Notification.DEFAULT_ALL)
//				.setVibrate(new long[]{0, 100, 200, 300})
//				.setPriority(Notification.PRIORITY_HIGH)
//				.setContentIntent(pendingIntent);
//
//		NotificationManager notificationManager = (NotificationManager) this
//				.getSystemService(Context.NOTIFICATION_SERVICE);
//
//		notificationManager.notify(0, notificationBuilder.build());
//	}
}
