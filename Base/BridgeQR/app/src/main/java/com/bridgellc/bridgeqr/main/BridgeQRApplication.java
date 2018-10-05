package com.bridgellc.bridgeqr.main;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.lang.Thread.UncaughtExceptionHandler;

public class BridgeQRApplication extends Application {

    private static boolean activityVisible;

    private static BridgeQRApplication mInstance;


    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
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
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();

        }

    }

    public static synchronized BridgeQRApplication getInstance() {
        return mInstance;
    }


    public static Context getAppContext() {
        return BridgeQRApplication.mInstance;
    }

    class unCaughtException implements UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {

            Log.e("Application", "Exception", ex);

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
