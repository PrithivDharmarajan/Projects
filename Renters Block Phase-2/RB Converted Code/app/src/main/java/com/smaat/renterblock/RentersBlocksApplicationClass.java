package com.smaat.renterblock;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibrary;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.smaat.renterblock.sqlite.DatabaseManager;
import com.smaat.renterblock.sqlite.RentersBlockDatabase;

import java.io.File;

import io.fabric.sdk.android.Fabric;

public class RentersBlocksApplicationClass extends Application {

    private static Context appContext;

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {

        super.onCreate();
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(this, new Crashlytics());

        RentersBlocksApplicationClass.appContext = getApplicationContext();
        Thread.setDefaultUncaughtExceptionHandler(unCaughtExceptionHandler);
        DatabaseManager.initialize(getApplicationContext(),
                new RentersBlockDatabase(getAppContext()));
        try {
            LocationLibrary.initialiseLibrary(getBaseContext(), "com.smaat.rentersblock");
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();

        }

        Thread.setDefaultUncaughtExceptionHandler(unCaughtExceptionHandler);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPoolSize(5)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCacheSize(3200000) // 1.5 Mb
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .enableLogging() // Not necessary in common
                .build();
        ImageLoader.getInstance().init(config);
    }

    public static Context getAppContext() {
        return RentersBlocksApplicationClass.appContext;
    }

    /**
     * Handle the uncaught exception throws from the application
     */
    private Thread.UncaughtExceptionHandler unCaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable exception) {

            /*Fire base Crash report*/
            Crashlytics.logException(exception);
            // TODO remove the following code to restart the application when
            // its crashed.

            exception.printStackTrace();
            Log.e("Exception", exception.getMessage());

            restartApp();

        }
    };

    private void restartApp() {
        // PendingIntent myActivity = PendingIntent.getActivity(getAppContext(),
        // 192837, new Intent(getAppContext(), MapFragmentActivity.class),
        // PendingIntent.FLAG_ONE_SHOT);
        //
        // AlarmManager mgr = (AlarmManager)
        // getSystemService(Context.ALARM_SERVICE);
        // mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000,
        // myActivity);

        System.exit(0);
    }


    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    boolean deleteResult = deleteDir(new File(appDir, s));
                    Log.i("TAG", "**************** Delete Result"
                            + deleteResult + s + " DELETED *******************");
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
