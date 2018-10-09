package com.smaat.ipharma.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.NotificationCompat;
import android.util.Log;


import com.smaat.ipharma.R;
import com.smaat.ipharma.ui.HomeScreen;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static android.content.Context.ALARM_SERVICE;


/**
 * Created by admin on 12/8/2016.
 */

public class AlarmReceiver extends WakefulBroadcastReceiver {
    private NotificationManager alarmNotificationManager;
    public Context ctx;
    Uri alarmUri = null;
    long timemillisec = 0;
    @Override
    public void onReceive(final Context context, Intent intent) {
        //this will update the UI with message
        /*AlarmActivity inst = AlarmActivity.instance();
        inst.setAlarmText("Alarm! Wake up! Wake up!");*/
        Log.e("onReceiveonReceive","onReceiveonReceive");
        ctx = context;

        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        }
            if (intent.getExtras() != null) {
                if(!GlobalMethods.getStringValue(ctx,AppConstants.USER_ID).isEmpty())
                {
                    Intent i =new Intent(ctx,NotifySMSReceived.class);
                    i.putExtra("AlarmUri", alarmUri.toString());
                    i.putExtra("AlarmTime",intent.getExtras().getString("AlarmTime"));
                    i.putExtra("AlarmTitle",intent.getExtras().getString("AlarmTitle"));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(i);
                }
            }





        //this will send a notification message
        ComponentName comp = new ComponentName(context.getPackageName(),
                AlarmService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);


    }


    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am
                    .getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo
                        .IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }
    private void sendNotification(Context ctx, String msg, String appid) {
        Log.d("AlarmService", "Preparing to send notification...: " + msg);


        alarmNotificationManager = (NotificationManager) ctx
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
                new Intent(ctx, HomeScreen.class), 0);

        NotificationCompat.Builder alamNotificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(
                ctx).setContentTitle(ctx.getString(R.string.app_name)).setSmallIcon(R.drawable.notification_icon)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);

        Intent intent = new Intent(ctx, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(ctx, Integer.parseInt(appid), intent, 0);
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(sender);


        Random random = new Random();
        int randomNumber = random.nextInt(9999 - 1000) + 1000;
        alamNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationManager.notify(randomNumber, alamNotificationBuilder.build());
        Log.d("AlarmService", "Notification sent.");



        //Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
       /* android.support.v4.app.NotificationCompat.Builder notificationBuilder = new android.support.v4.app.NotificationCompat.Builder(
                ctx)
                .setSmallIcon(R.mipmap.ic_launcher) //Small Icon from drawable
                .setContentTitle(ctx.getString(R.string.app_name))
                .setContentText(msg)
                .setAutoCancel(true)
                .setLocalOnly(true)
                .setSound(alarmUri)
                .setColor(ContextCompat.getColor(ctx,R.color.grey_color))
                .setStyle(
                        new android.support.v4.app.NotificationCompat.BigTextStyle().bigText(msg))
                .setContentIntent(contentIntent);


        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
        notificationBuilder.setVibrate(new long[]{0, 100, 200, 300});
        int randomNumber = random.nextInt(9999 - 1000) + 1000;
        NotificationManager notificationManager = (NotificationManager) ctx
                .getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(randomNumber, notificationBuilder.build());*/

    }
}
