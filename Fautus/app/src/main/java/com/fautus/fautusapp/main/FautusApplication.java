package com.fautus.fautusapp.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.fautus.fautusapp.R;
import com.fautus.fautusapp.ui.HomeScreen;
import com.fautus.fautusapp.ui.SplashScreen;
import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.PreferenceUtil;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.sendbird.android.SendBird;
import com.zendesk.sdk.model.access.AnonymousIdentity;
import com.zendesk.sdk.model.access.Identity;
import com.zendesk.sdk.network.impl.ZendeskConfig;

import java.lang.Thread.UncaughtExceptionHandler;

import io.fabric.sdk.android.Fabric;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class FautusApplication extends android.app.Application {

    private static boolean activityVisible;
    private static FautusApplication mInstance;

    public static synchronized FautusApplication getInstance() {
        return mInstance;
    }

    public static Context getContext() {
        return mInstance;
    }

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

        /*Parse Initialization*/
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.parse_app_id))
                .server(getString(R.string.parse_server_url))
                .build()
        );
        ParseInstallation.getCurrentInstallation().saveInBackground();

        /*SendBird Initialization*/
        SendBird.init(AppConstants.SEND_BIRD_LIVE_APP_ID, mInstance);

        /*Zendesk Configuration*/
        ZendeskConfig.INSTANCE.init(this, AppConstants.ZEN_DESK_URL, AppConstants.ZEN_DESK_APP_ID, AppConstants.ZEN_DESK_CLIENT_ID);
        Identity identity = new AnonymousIdentity.Builder().withNameIdentifier("Generic").build();
        ZendeskConfig.INSTANCE.setIdentity(identity);

        /*Fabric Crashlytics*/
        Fabric.with(this, new Crashlytics());

        Thread.setDefaultUncaughtExceptionHandler(new unCaughtException());

    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private class unCaughtException implements UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {

            /*Fire base Crash report*/
            Crashlytics.logException(ex);

            /*Application restart*/
            if (activityVisible) {
                Intent intent = new Intent(getContext(), PreferenceUtil.getBoolPreferenceValue(getContext(), AppConstants.LOGIN_STATUS) ? HomeScreen.class : SplashScreen.class);
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
