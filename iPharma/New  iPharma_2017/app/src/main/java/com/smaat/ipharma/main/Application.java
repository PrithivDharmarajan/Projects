package com.smaat.ipharma.main;

import android.content.Context;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;

public class Application extends android.app.Application {
    static Context mAppContext;
    private static boolean activityVisible;

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityStoped() {
        activityVisible = false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        logUser();
        Application.mAppContext = getApplicationContext();
        //Thread.setDefaultUncaughtExceptionHandler(mUncaughtExceptionHandler);
        //Thread.setDefaultUncaughtExceptionHandler(new unCaughtException());
    }


    private void logUser() {
        // TODO: Use the current user's information
        // You can call any combination of these three methods
        Crashlytics.setUserIdentifier("12345");
        Crashlytics.setUserEmail("parthasarathy@smaatapps.com");
        Crashlytics.setUserName("Test User");
    }

    public static Context getAppContext() {

        return Application.mAppContext;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    private UncaughtExceptionHandler mUncaughtExceptionHandler = new UncaughtExceptionHandler() {

        @Override
        public void uncaughtException(Thread thread, Throwable exception) {
            exception.printStackTrace();
            // restartApp();
            android.os.Process.killProcess(android.os.Process.myPid());

        }
    };

    private void restartApp() {
        /*PendingIntent mPendingIntent = PendingIntent.getActivity(
                getAppContext(), 192837, new Intent(getAppContext(),
                        SplashScreen.class), PendingIntent.FLAG_ONE_SHOT);

        AlarmManager mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 3000,
                mPendingIntent);

        System.exit(0);*/
    }

    class unCaughtException implements UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {

            try {
                Log.e("Error", ex.getLocalizedMessage());
                Crashlytics.logException(ex);
                try {
                    PrintWriter pw;
                    try {
                        pw = new PrintWriter(
                                new FileWriter(Environment.getExternalStorageDirectory() + "/TirumeniError.log", true));
                        ex.printStackTrace(pw);
                        pw.flush();
                        pw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Crashlytics.logException(e);
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }


            } catch (Exception ex1) {
                ex1.printStackTrace();
            }
            ex.printStackTrace();
//            Intent intent = new Intent(getContext(), SplashScreen.class);
//            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
//            getContext().startActivity(intent);
//            if (getContext() instanceof Activity) {
//                ((Activity) getContext()).finish();
//            }
//
//            Runtime.getRuntime().exit(0);
        }
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

//    public synchronized Tracker getGoogleAnalyticsTracker() {
//        AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
//        return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
//    }

    /***
     * Tracking screen view
     *
     * @param screenName screen name to be displayed on GA dashboard
     */
//    public void trackScreenView(String screenName) {
//        Tracker t = getGoogleAnalyticsTracker();
//
//        // Set screen name.
//        t.setScreenName(screenName);
//
//        // Send a screen view.
//        t.send(new HitBuilders.ScreenViewBuilder().build());
//
//        GoogleAnalytics.getInstance(this).dispatchLocalHits();
//    }

    /***
     * Tracking exception
     *
     * @param e exception to be tracked
     */
//    public void trackException(Exception e) {
//        if (e != null) {
//            Tracker t = getGoogleAnalyticsTracker();
//
//            t.send(new HitBuilders.ExceptionBuilder()
//                    .setDescription(
//                            new StandardExceptionParser(this, null)
//                                    .getDescription(Thread.currentThread()
//                                            .getName(), e)).setFatal(false)
//                    .build());
//        }
//    }

    /***
     * Tracking event
     *
     * @param category event category
     * @param action   action of the event
     * @param label    label
     */
//    public void trackEvent(String category, String action, String label) {
//        Tracker t = getGoogleAnalyticsTracker();
//
//        // Build and send an Event.
//        t.send(new HitBuilders.EventBuilder().setCategory(category)
//                .setAction(action).setLabel(label).build());
//    }
}
