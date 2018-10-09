package com.smaat.renterblock.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.smaat.renterblock.fragments.MapFragment;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

import java.lang.Thread.UncaughtExceptionHandler;

import io.fabric.sdk.android.Fabric;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.smaat.renterblock.utils.AppConstants.TWITTER_API_KEY;
import static com.smaat.renterblock.utils.AppConstants.TWITTER_API_SECRET;


public class RBApplication extends android.app.Application {

    private static boolean activityVisible;
    private static RBApplication mInstance;

    public static synchronized RBApplication getInstance() {
        return mInstance;
    }

    public static Context getContext() {
        return mInstance;
    }

    @Override
    protected void attachBaseContext(Context context) {
        MultiDex.install(this);
        super.attachBaseContext(context);
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

        /*init Twitter*/
        TwitterConfig config = new TwitterConfig.Builder(mInstance)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(TWITTER_API_KEY, TWITTER_API_SECRET))
                .debug(true)
                .build();
        Twitter.initialize(config);

        /*init Fabric*/
        final Fabric fabric = new Fabric.Builder(mInstance)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);

        /*init UncaughtException*/
        Thread.setDefaultUncaughtExceptionHandler(new unCaughtException());

    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    /*unCaughtException*/
    private class unCaughtException implements UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            Crashlytics.logException(ex);
            /*Restart application */
            if (activityVisible) {
                AppConstants.MAP_CURRENT_BACK_FRAGMENT = new MapFragment();
                Intent intent = new Intent(mInstance, HomeScreen.class);
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
