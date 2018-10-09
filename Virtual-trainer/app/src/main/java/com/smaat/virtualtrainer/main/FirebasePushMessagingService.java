package com.smaat.virtualtrainer.main;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.smaat.virtualtrainer.R;
import com.smaat.virtualtrainer.ui.HomeScreen;

import org.json.JSONObject;

import java.util.Random;


public class FirebasePushMessagingService extends FirebaseMessagingService {

    private JSONObject json;
    private static String message = "Test Msg", counts = "";

    public void onMessageReceived(RemoteMessage remoteMessage) {

//        System.out.println("onMessageReceived msg---" + remoteMessage);
//        Map data = remoteMessage.getData();
//        System.out.println("data msg---" + data);
//        String intentStr = (String) data.get("message");
//        System.out.println("intentStr msg---" + intentStr);

        Notify(message, "1");
    }

    private void Notify(String notificationMessage, String pushFullMessage) {

        System.out.println("Notify method call---");
        Intent intent = new Intent(this, HomeScreen.class);
        intent.putExtra("pushNotification", pushFullMessage);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        final int not_nu = generateRandom();
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                not_nu, intent, PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        android.support.v4.app.NotificationCompat.Builder notificationBuilder = new android.support.v4.app.NotificationCompat.Builder(
                this)
                .setSmallIcon(R.drawable.push_notify_img) //Small Icon from drawable
                .setContentTitle(this.getString(R.string.app_name))
                .setContentText(notificationMessage)
                .setAutoCancel(true)
                .setLocalOnly(true)
                .setSound(defaultSoundUri)
                .setColor(this.getResources().getColor(R.color.screen_bg))
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
