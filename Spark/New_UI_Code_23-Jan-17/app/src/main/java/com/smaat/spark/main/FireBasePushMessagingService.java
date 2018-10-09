package com.smaat.spark.main;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.smaat.spark.R;
import com.smaat.spark.ui.HomeScreen;
import com.smaat.spark.utils.AppConstants;
import com.smaat.spark.utils.GlobalMethods;

import org.json.JSONObject;

import java.util.Map;
import java.util.Random;


public class FireBasePushMessagingService extends FirebaseMessagingService {


    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map data;
        JSONObject json;
        String intentStr = "", messageStr = "", countsStr = "", typeNotificationStr = "";

        data = remoteMessage.getData();
        intentStr = (String) data.get("message");

        if (intentStr != null && !intentStr.equalsIgnoreCase("")) {
            try {
                json = new JSONObject(intentStr);
                messageStr = json.getString("msg");
                countsStr = json.getString("counts");
                typeNotificationStr = json.getString("type_of_notification");
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }

            if (GlobalMethods.isLoggedIn(FireBasePushMessagingService.this)) {
                if (SparkApplication.isActivityVisible() && (typeNotificationStr.equals(AppConstants.SUCCESS_CODE) || typeNotificationStr.equals(getString(R.string.two)) || typeNotificationStr.equals(getString(R.string.three)))) {
                    BaseActivity.getInstance().displayNotifyPopup(intentStr);
                } else if (!SparkApplication.isActivityVisible()) {
                    GlobalMethods.setBadge(this, Integer.valueOf(countsStr));
                    Notify(messageStr, intentStr);
                }

            }
        }
    }


    private void Notify(String notificationMessage, String pushFullMessage) {

        Intent intent = new Intent(this, HomeScreen.class);
        intent.putExtra(AppConstants.NOTIFICATION_DATA, pushFullMessage);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        final int not_nu = generateRandom();
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                not_nu, intent, PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        android.support.v4.app.NotificationCompat.Builder notificationBuilder = new android.support.v4.app.NotificationCompat.Builder(
                this)
                .setSmallIcon(R.mipmap.app_push_icon) //Small Icon from drawable
                .setContentTitle(this.getString(R.string.app_name))
                .setContentText(notificationMessage)
                .setAutoCancel(true)
                .setLocalOnly(true)
                .setSound(defaultSoundUri)
                .setColor(ContextCompat.getColor(this, R.color.sky_blue))
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
