package com.smaat.renterblock.main;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.smaat.renterblock.R;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.PreferenceUtil;

import org.json.JSONObject;

import java.util.Map;

import static com.smaat.renterblock.main.RBApplication.getContext;

/**
 * Created by sys on 1/23/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        String pushDataStr = "";

        if (remoteMessage.getData() != null) {
            Map data = remoteMessage.getData();
            pushDataStr = (String) data.get("message");
        }

        // Check if message contains a data payload.

        if (!RBApplication.isActivityVisible() &&
                PreferenceUtil.getBoolPreferenceValue(getContext(), AppConstants.LOGIN_STATUS) &&
                pushDataStr != null && !pushDataStr.isEmpty()) {


            if (remoteMessage.getData() != null && remoteMessage.getData().size() > 0) {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());


                try {
                    JSONObject json = new JSONObject(pushDataStr);
                    AppConstants.PUSH_TYPE = json.getString("type");

                    AppConstants.PUSH_MSG = pushDataStr;
                    AppConstants.NOTIFICATION_STATUS = AppConstants.SUCCESS_CODE;
                    sendNotification(json.getString("msg"));
                } catch (Exception e) {
                    AppConstants.NOTIFICATION_STATUS = AppConstants.FAILURE_CODE;
                    e.printStackTrace();
                }


            }

            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            }
        }
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]


    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {

//        Intent intent = new Intent(this, HomeScreen.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            // Sets an ID for the notification, so it can be updated.
//            int notifyID = 1;
//
//            String CHANNEL = "channel";// The id of the channel.
//            CharSequence name = getString(R.string.app_name);// The user-visible name of the channel.
//
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel mChannel = new NotificationChannel(CHANNEL, name, importance);
//
//// Create a notification and set the notification channel.
//            Notification notification = new Notification.Builder(this)
//                    .setContentTitle(getString(R.string.app_name))
//                    .setContentText(messageBody)
//                    .setSmallIcon(R.mipmap.app_icon)
//                    .setChannelId(CHANNEL)
//                    .build();
//
//            NotificationManager mNotificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                mNotificationManager.createNotificationChannel(mChannel);
//            }
//
//            mNotificationManager.notify(notifyID, notification);
//
//
//        } else {
//
//
//            String channelId = "channel";
//            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            NotificationCompat.Builder notificationBuilder =
//                    new NotificationCompat.Builder(this, channelId)
//                            .setSmallIcon(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? R.mipmap.app_icon : R.mipmap.app_icon)
//                            .setContentTitle(getString(R.string.app_name))
//                            .setContentText(messageBody)
//                            .setAutoCancel(true)
//                            .setSound(defaultSoundUri)
//                            .setContentIntent(pendingIntent);
//
//            NotificationManager notificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//        }


        //--------------------------------------

        Intent intent = new Intent(this, HomeScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription("");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ?
                        R.mipmap.app_icon : R.drawable.app_notification_icon)
                .setTicker("Hearty365")
                //     .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody)
                .setContentIntent(pendingIntent)
                .setContentInfo("");

        notificationManager.notify(/*notification id*/1, notificationBuilder.build());


    }
}
