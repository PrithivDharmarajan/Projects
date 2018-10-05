package com.calix.calixgigamanage.main;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.ui.dashboard.Dashboard;
import com.calix.calixgigamanage.ui.loginregconfig.Login;
import com.calix.calixgigamanage.ui.loginregconfig.PinCodeFingerPrintLogin;
import com.calix.calixgigamanage.ui.loginregconfig.PinCodeFingerPrintLoginBelow5;
import com.calix.calixgigamanage.utils.AppConstants;
import com.calix.calixgigamanage.utils.PreferenceUtil;
import com.crashlytics.android.Crashlytics;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class CalixApplication extends android.app.Application {

    private static boolean activityVisible;
    private static CalixApplication mInstance;

    public static synchronized CalixApplication getInstance() {
        return mInstance;
    }

    public static Context getContext() {
        return mInstance;
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityStopped() {
        activityVisible = false;
    }

    public static void activityFinished() {
        activityVisible = false;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);
        mInstance = this;
        /*Production Mode*/
//        if (!PreferenceUtil.getUserId(this).isEmpty()) {
//            pushMsg();
//        }

        /*init UncaughtException*/
        Thread.setDefaultUncaughtExceptionHandler(new unCaughtException());

    }

    public void pushMsg() {


//        final Pubnub pubNub = new Pubnub("pub-c-a722a681-b7ca-45e4-9c93-03e029106f62", "sub-c-cd20662c-5883-11e8-a697-1afc57e8b539");
//
//        Callback callback = new Callback() {
//            public void successCallback(String channel, Object response) {
//                System.out.println(response.toString());
//            }
//
//            public void errorCallback(String channel, PubnubError error) {
//                System.out.println(error.toString());
//            }
//        };
//        try {
//            pubNub.subscribe(PreferenceUtil.getUserId(this), new Callback() {
//
//                        @Override
//                        public void connectCallback(String channel, Object message) {
//
//                        }
//
//                        @Override
//                        public void disconnectCallback(String channel, Object message) {
//                        }
//
//                        public void reconnectCallback(String channel, Object message) {
//                        }
//
//                        @Override
//                        public void successCallback(String channel, final Object message) {
//
//                            try {
//                                JSONObject json = new JSONObject(message.toString());
//                                if (json.has("messageType") && json.getString("messageType").equalsIgnoreCase("guest")) {
//                                    if (json.has("notification")) {
//                                        sendNotification(mInstance, json.getString("notification"));
//                                    }
//                                }
//                            } catch (JSONException | JsonParseException e) {
//                                new Handler(Looper.getMainLooper()).post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        sendNotification(mInstance, message.toString());
//                                    }
//                                });
//                            }
//                        }
//
//                        @Override
//                        public void errorCallback(String channel, PubnubError error) {
//                            System.out.println("SUBSCRIBE : ERROR on channel " + channel
//                                    + " : " + error.toString());
//                        }
//                    }
//            );
//        } catch (PubnubException e) {
//            System.out.println(e.toString());
//        }
//
//        pubNub.time(callback);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private void sendNotification(Context context, String messageStr) {

        if (PreferenceUtil.getBoolPreferenceValue(mInstance, AppConstants.LOGIN_STATUS)) {
            Intent intent = new Intent(this, Dashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            String channelId = getString(R.string.default_notification_channel_id);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.mipmap.push_launcher) //Small Icon from drawable
                            .setContentTitle(context.getString(R.string.app_name))
                            .setColor(ContextCompat.getColor(this, R.color.violet))
                            .setContentText(messageStr)
                            .setAutoCancel(true)
                            .setWhen(System.currentTimeMillis())
                            .setSound(defaultSoundUri)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(messageStr))
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setVibrate(new long[]{0, 100, 200, 300})
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setContentIntent(pendingIntent);

            AppConstants.BASE_URL = PreferenceUtil.getBaseURL(mInstance);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        context.getString(R.string.app_name),
                        NotificationManager.IMPORTANCE_DEFAULT);
                if (notificationManager != null)
                    notificationManager.createNotificationChannel(channel);
            }

            if (notificationManager != null)
                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }
    }

    @Override
    public void registerActivityLifecycleCallbacks(
            ActivityLifecycleCallbacks callback) {
        super.registerActivityLifecycleCallbacks(callback);
    }

    /*unCaughtException*/
    private class unCaughtException implements UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            Crashlytics.logException(ex);

            /*Restart application*/
            if (activityVisible) {
                Class<?> nextScreenClass = Login.class;

                if (PreferenceUtil.getBoolPreferenceValue(mInstance, AppConstants.PASS_CODE_ENABLE_STATUS)) {
                    nextScreenClass = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PinCodeFingerPrintLogin.class : PinCodeFingerPrintLoginBelow5.class;
                } else if (PreferenceUtil.getBoolPreferenceValue(mInstance, AppConstants.LOGIN_STATUS)) {
                    nextScreenClass = Dashboard.class;
                }

                /*for back screen process*/
                AppConstants.PREVIOUS_SCREEN = new ArrayList<>();
                AppConstants.PREVIOUS_SCREEN.add(nextScreenClass.getCanonicalName());
                AppConstants.BASE_URL = PreferenceUtil.getBaseURL(mInstance);

                Intent intent = new Intent(mInstance, nextScreenClass);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);

                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).finish();
                }

                Runtime.getRuntime().exit(0);
            }
        }
    }
}
