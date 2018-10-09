package com.smaat.virtualtrainer.main;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import java.lang.Thread.UncaughtExceptionHandler;

public class VirtualTrainerApplication extends Application {

    private static boolean activityVisible;

    public static final String TAG = VirtualTrainerApplication.class.getSimpleName();

    private static VirtualTrainerApplication mInstance;

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
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

        } catch (UnsupportedOperationException e) {
            e.printStackTrace();

        }

    }

    public static synchronized VirtualTrainerApplication getInstance() {
        return mInstance;
    }


    public static Context getAppContext() {
        return VirtualTrainerApplication.mInstance;
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
