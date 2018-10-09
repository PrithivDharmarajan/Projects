package com.fautus.fautusapp.main;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.fautus.fautusapp.R;
import com.fautus.fautusapp.ui.HomeScreen;
import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.PreferenceUtil;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.fautus.fautusapp.main.FautusApplication.getContext;


public class FireBasePushMessagingService extends FirebaseMessagingService {


    public void onMessageReceived(RemoteMessage remoteMessage) {
        String pushDataStr = "";
        pushDataStr = remoteMessage.getData().get("message");

        /*Clear old notification*/
        ((NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE)).cancel(0);
        if (PreferenceUtil.getBoolPreferenceValue(getContext(), AppConstants.LOGIN_STATUS) && pushDataStr != null && !pushDataStr.isEmpty()) {
            sendChatNotification(pushDataStr);
        }
    }

    private void sendChatNotification(String messageStr) {
        Intent intent = new Intent(this, HomeScreen.class);
        intent.putExtra(AppConstants.PUSH_CHAT_STATUS, AppConstants.SUCCESS_CODE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                this)
                .setSmallIcon(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? R.mipmap.app_push_icon : R.mipmap.app_icon) //Small Icon from drawable
                .setContentTitle(this.getString(R.string.app_name))
                .setContentText(messageStr)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setColor(ContextCompat.getColor(this, R.color.sky_blue))
                .setStyle(new android.support.v4.app.NotificationCompat.BigTextStyle().bigText(messageStr))
                .setDefaults(Notification.DEFAULT_ALL)
                .setVibrate(new long[]{0, 100, 200, 300})
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }


}
