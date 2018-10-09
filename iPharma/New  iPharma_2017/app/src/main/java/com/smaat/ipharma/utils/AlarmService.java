package com.smaat.ipharma.utils;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;

/**
 * Created by admin on 12/8/2016.
 */

public class AlarmService extends IntentService {
    private NotificationManager alarmNotificationManager;

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        //sendNotification("Wake Up! Wake Up!");
    }


}
