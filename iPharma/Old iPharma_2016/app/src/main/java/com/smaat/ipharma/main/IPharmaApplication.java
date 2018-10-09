package com.smaat.ipharma.main;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.smaat.ipharma.ui.SplashScreen;

import java.io.File;

public class IPharmaApplication extends Application {
    private static Context appContext;
    private static boolean activityVisible;

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }

    @Override
    public void onCreate() {

        super.onCreate();

        IPharmaApplication.appContext = getApplicationContext();

//		FirebaseMessaging.setAndroidContext(this);

        Thread.setDefaultUncaughtExceptionHandler(unCaughtExceptionHandler);

    }
    public static boolean isActivityVisible() {

        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityStoped() {
        activityVisible = false;
    }


    public static Context getAppContext() {
        return IPharmaApplication.appContext;
    }

    /**
     * Handle the uncaught exception throws from the application
     */
    private Thread.UncaughtExceptionHandler unCaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {

        public void uncaughtException(Thread thread, Throwable exception) {
            exception.printStackTrace();
//			restartApp();
        }
    };

    private void restartApp() {
        PendingIntent myActivity = PendingIntent.getActivity(getAppContext(),
                192837, new Intent(getAppContext(), SplashScreen.class),
                PendingIntent.FLAG_ONE_SHOT);

        AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, myActivity);

        System.exit(0);
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    @SuppressWarnings("unused")
                    boolean deleteResult = deleteDir(new File(appDir, s));
                }
            }
        }

    }

    private boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }
}
