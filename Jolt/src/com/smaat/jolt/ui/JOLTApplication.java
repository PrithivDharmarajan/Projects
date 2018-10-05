package com.smaat.jolt.ui;

import android.app.Application;
import android.content.Context;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibrary;
import com.smaat.jolt.sqlite.DataBaseHelper;
import com.smaat.jolt.sqlite.DatabaseManager;
import com.smaat.jolt.util.AppConstants;

public class JOLTApplication extends Application {

	private static Context instance;
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
		instance = this;

		// Thread.setDefaultUncaughtExceptionHandler(new unCaughtException());

		DatabaseManager.initialize(getApplicationContext(), new DataBaseHelper(
				getAppContext()));
		try {
			LocationLibrary.initialiseLibrary(getBaseContext(),
					AppConstants.com_smaat_jolt);
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();

		}

	}

	@Override
	public void registerActivityLifecycleCallbacks(
			ActivityLifecycleCallbacks callback) {

		super.registerActivityLifecycleCallbacks(callback);
	}

	public static Context getAppContext() {
		return JOLTApplication.instance;
	}

	

	public static Context getContext() {
		return instance;
	}

//	private void restartApp() {
//		// PendingIntent myActivity = PendingIntent.getActivity(getContext(),
//		// 192837, new Intent(getContext(), SplashActivity.class),
//		// PendingIntent.FLAG_ONE_SHOT);
//		//
//		// AlarmManager mgr = (AlarmManager)
//		// getSystemService(Context.ALARM_SERVICE);
//		// mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000,
//		// myActivity);
//		//
//		// System.exit(0);
//	}

}
