package com.fautus.fautusapp.main;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.fautus.fautusapp.R;
import com.fautus.fautusapp.ui.HomeScreen;
import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.ParseAPIConstants;
import com.fautus.fautusapp.utils.PreferenceUtil;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import static com.fautus.fautusapp.main.FautusApplication.getContext;

/**
 * This class implements parse push notification functions
 *
 * @author Smaat Apps
 * @since 2017-06-21
 */
public class GCMPushReceiver extends ParsePushBroadcastReceiver {
    private final String TAG = GCMPushReceiver.class.getSimpleName();

    @Override
    protected void onPushReceive(Context context, Intent intent) {

        String messageStr = "", momentIdStr = null;
        try {
            String pushDataStr = intent.getStringExtra(KEY_PUSH_DATA);
            JSONObject json = new JSONObject(pushDataStr);
            if (json.getString(ParseAPIConstants.alert) != null)
                messageStr = json.getString(ParseAPIConstants.alert);

            if (json.getString(ParseAPIConstants.momentId) != null)
                momentIdStr = json.getString(ParseAPIConstants.momentId);

        } catch (JSONException e) {
            Log.e(TAG, "Push message json exception: " + e.getMessage());
        }

        /*Clear old notification*/
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(0);

        if (!FautusApplication.isActivityVisible() && PreferenceUtil.getBoolPreferenceValue(getContext(), AppConstants.LOGIN_STATUS) && !PreferenceUtil.getBoolPreferenceValue(getContext(), AppConstants.USER_IS_CONSUMER)
                && !messageStr.isEmpty() && momentIdStr != null && !momentIdStr.isEmpty())
            sendNotification(context, messageStr, momentIdStr);

    }

    @Override
    protected void onPushDismiss(Context context, Intent intent) {
        super.onPushDismiss(context, intent);
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
    }


    private void sendNotification(Context context, String messageStr, String momentIdStr) {

        Intent intent = new Intent(context, HomeScreen.class);
        intent.putExtra(AppConstants.PUSH_MOMENT_ID, momentIdStr);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        android.support.v4.app.NotificationCompat.Builder notificationBuilder = new android.support.v4.app.NotificationCompat.Builder(
                context)
                .setSmallIcon(R.mipmap.app_push_icon) //Small Icon from drawable
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(messageStr)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setColor(ContextCompat.getColor(context, R.color.sky_blue))
                .setStyle(
                        new android.support.v4.app.NotificationCompat.BigTextStyle().bigText(messageStr))
                .setDefaults(Notification.DEFAULT_ALL)
                .setVibrate(new long[]{0, 100, 200, 300})
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }


}