package com.smaat.renterblock.main;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.smaat.renterblock.R;
import com.smaat.renterblock.fragments.MapFragment;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.PreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static com.smaat.renterblock.main.RBApplication.getContext;


public class FireBasePushMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String pushDataStr = "";
        // pushDataStr = remoteMessage.getNotification().getBody();


        Map data = remoteMessage.getData();
        final String msg = remoteMessage.getFrom();


        pushDataStr = (String) data.get("message");
        System.out.println("Message:" + pushDataStr);

        System.out.println("Message:" + msg);
        System.out.println("Message:" + msg);
        System.out.println("MessageWhole:" + remoteMessage);
        String mMessage = (String) data.get("message");
        String bnb_id = (String) data.get("bnb_id");
        String locationLat = (String) data.get("locationlat");
        String locationLong = (String) data.get("locationLong");
        String distance = (String) data.get("distance");

        if (!RBApplication.isActivityVisible() &&
                PreferenceUtil.getBoolPreferenceValue(getContext(), AppConstants.LOGIN_STATUS) &&
                pushDataStr != null && !pushDataStr.isEmpty()) {

            try {
                JSONObject json = new JSONObject(pushDataStr);
                AppConstants.PUSH_TYPE = json.getString("type");

                AppConstants.PUSH_MSG = pushDataStr;
                AppConstants.NOTIFICATION_STATUS = AppConstants.SUCCESS_CODE;
                sendChatNotification(json.getString("msg"));
            } catch (Exception e) {
                AppConstants.NOTIFICATION_STATUS = AppConstants.FAILURE_CODE;
                e.printStackTrace();
            }
        }
    }


//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        Map data = remoteMessage.getData();
//        Log.d("RemoteMessage",""+data);
//        String data1 = remoteMessage.getNotification().getTitle();
//        Log.d("Body",""+data1);
//
//
//    }

    private void sendChatNotification(String messageStr) {

        AppConstants.LAST_CURRENT_FRAG = new MapFragment();
        Intent intent = new Intent(this, HomeScreen.class);
//        intent.putExtra(AppConstants.PUSH_CHAT_STATUS, AppConstants.SUCCESS_CODE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                this)
                .setSmallIcon(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? R.mipmap.app_icon :  R.drawable.app_notification_icon) //Small Icon from drawable
                .setContentTitle(this.getString(R.string.app_name))
                .setContentText(messageStr)
                .setAutoCancel(true)
                //.setSound(defaultSoundUri)
                .setColor(ContextCompat.getColor(this, R.color.app_blue))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageStr))
                .setDefaults(Notification.DEFAULT_ALL)
                .setVibrate(new long[]{0, 100, 200, 300})
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());


    }


}
