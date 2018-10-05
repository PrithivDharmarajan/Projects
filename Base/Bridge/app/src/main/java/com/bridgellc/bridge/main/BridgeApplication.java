package com.bridgellc.bridge.main;

import android.app.Application;
import android.content.Context;

import java.lang.Thread.UncaughtExceptionHandler;

public class BridgeApplication extends Application {

    private static boolean activityVisible;

    public static final String TAG = BridgeApplication.class.getSimpleName();

    private static BridgeApplication mInstance;


    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
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

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        Thread.setDefaultUncaughtExceptionHandler(new unCaughtException());

        try {
//			LocationLibrary.initialiseLibrary(getBaseContext(),
//					"com.bridgellc.bridge");
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();

        }

//		DatabaseManager.initialize(getApplicationContext(), new DataBaseHelper(
//				getAppContext()));
//
//		AnalyticsTrackers.initialize(this);
//		AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);

    }

    public static synchronized BridgeApplication getInstance() {
        return mInstance;
    }


    public static Context getAppContext() {
        return BridgeApplication.mInstance;
    }

    class unCaughtException implements UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {

            ex.printStackTrace();

        }

    }

    @Override
    public void registerActivityLifecycleCallbacks(
            ActivityLifecycleCallbacks callback) {

        super.registerActivityLifecycleCallbacks(callback);
    }

    public static Context getContext() {
        return mInstance;
    }

}
