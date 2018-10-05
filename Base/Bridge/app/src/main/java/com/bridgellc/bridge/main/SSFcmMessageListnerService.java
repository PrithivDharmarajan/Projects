package com.bridgellc.bridge.main;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.GlobalMethods;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;
import java.util.Random;


public class SSFcmMessageListnerService extends FirebaseMessagingService {

    private JSONObject json;
    private static String message = "", counts = "";

    public void onMessageReceived(RemoteMessage remoteMessage) {

        Map data = remoteMessage.getData();
        String intentStr = (String) data.get("message");

        if (intentStr != null && !intentStr.equalsIgnoreCase("")) {
            try {
                json = new JSONObject(intentStr);
                message = json.getString("msg");
                counts = json.getString("counts");

                GlobalMethods.setBadge(this, Integer.valueOf(counts));
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }

            if (GlobalMethods.isLoggedIn(SSFcmMessageListnerService.this)) {
                if (BridgeApplication.isActivityVisible()) {
                    AppConstants.TYPE_OF_NOTIFICATION = intentStr;
                    BaseActivity.displayNotifyDialog(message);
                } else {
                    Notify(message, intentStr);
                }
            }
        }

    }

    private void Notify(String notificationMessage, String pushFullMessage) {

        Intent intent = new Intent(this, GCMNotificationActivity.class);
        intent.putExtra("pushNotification", pushFullMessage);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        final int not_nu = generateRandom();
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                not_nu, intent, PendingIntent.FLAG_ONE_SHOT);


        android.support.v4.app.NotificationCompat.Builder notificationBuilder = new android.support.v4.app.NotificationCompat.Builder(
                this)
                .setSmallIcon(R.drawable.app_logo) //Small Icon from drawable
                .setContentTitle(this.getString(R.string.app_name))
                .setContentText(notificationMessage)
                .setAutoCancel(true)
                .setLocalOnly(true)
                .setColor(this.getResources().getColor(R.color.green))
                .setStyle(
                        new android.support.v4.app.NotificationCompat.BigTextStyle().bigText(notificationMessage))
//                .setLargeIcon(
//                        BitmapFactory.decodeResource(this.getResources(),
//                                R.drawable.ic_launcher)) //Big Icon from drawable
                .setContentIntent(pendingIntent);


        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
        notificationBuilder.setVibrate(new long[]{0, 100, 200, 300});

        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(not_nu, notificationBuilder.build());
    }

    public static int generateRandom() {
        Random random = new Random();
        return random.nextInt(9999 - 1000) + 1000;
    }


}
