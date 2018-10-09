package com.smaat.spark.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.smaat.spark.database.DatabaseHelper;
import com.smaat.spark.ui.HomeScreen;
import com.smaat.spark.ui.SplashScreen;
import com.smaat.spark.utils.GlobalMethods;

import java.lang.Thread.UncaughtExceptionHandler;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class SparkApplication extends android.app.Application {

    private static boolean activityVisible;
    private static SparkApplication mInstance;

    public static synchronized SparkApplication getInstance() {
        return mInstance;
    }

    public static Context getContext() {
        return mInstance;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
//        MultiDex.install(this);
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
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        new DatabaseHelper(getApplicationContext());
        Thread.setDefaultUncaughtExceptionHandler(new unCaughtException());
    }

    @Override
    public void onTerminate() {
        DatabaseHelper.closeDatabase();
        super.onTerminate();
    }

    class unCaughtException implements UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {

            Log.e("Error", ex.getLocalizedMessage());
            ex.printStackTrace();
            FirebaseCrash.report(ex); // Generate report

            if (activityVisible) {
                Intent intent = new Intent(getContext(), GlobalMethods.isLoggedIn(getContext()) ? HomeScreen.class : SplashScreen.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).finish();
                }
                Runtime.getRuntime().exit(0);
            }
        }
    }

    @Override
    public void registerActivityLifecycleCallbacks(
            ActivityLifecycleCallbacks callback) {
        super.registerActivityLifecycleCallbacks(callback);
    }
}
