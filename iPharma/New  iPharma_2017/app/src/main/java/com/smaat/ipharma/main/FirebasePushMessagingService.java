package com.smaat.ipharma.main;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.smaat.ipharma.R;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.utils.AppConstants;
import com.smaat.ipharma.utils.GlobalMethods;

import org.json.JSONObject;

import java.util.Map;
import java.util.Random;


public class FirebasePushMessagingService extends FirebaseMessagingService {
    private static final String TAG = "GCMIntentService";
    private JSONObject json;
    private int numMessages = 0;
    @SuppressWarnings("unused")
    private static String mMessage = "", mTypeId = "", mPurpose = "";

    public void onMessageReceived(RemoteMessage remoteMessage) {


        String from = remoteMessage.getFrom();

        System.out.println("Messahe:" + from);
        System.out.println("MessaheWhole:" + remoteMessage);
        Map data = remoteMessage.getData();
        mMessage = (String) data.get("msg");
        mTypeId = (String) data.get("type");
        mPurpose = (String) data.get("purpose");

        if((Boolean) GlobalMethods.getValueFromPreference(this,GlobalMethods.BOOLEAN_PREFERENCE, AppConstants.PUSH_NOTIFICATION))
        {
            if (mMessage != null && !mMessage.equalsIgnoreCase("")) {

                Notify(mMessage,mTypeId);
            }
        }

    }

    private void Notify(String notificationMessage,String mTypeId) {


        Intent intent = new Intent(this, HomeScreen.class);
        intent.putExtra("mTypeId",mTypeId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        final int not_nu = generateRandom();
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                not_nu, intent, PendingIntent.FLAG_ONE_SHOT);





        //Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        android.support.v4.app.NotificationCompat.Builder notificationBuilder = new android.support.v4.app.NotificationCompat.Builder(
                this)
                .setSmallIcon(R.drawable.notify_icon)
                .setContentTitle(this.getString(R.string.app_name))
                .setContentText(notificationMessage)
                .setAutoCancel(true)
                .setLocalOnly(true)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setColor(ContextCompat.getColor(this,R.color.greycolor))
                .setStyle(
                        new android.support.v4.app.NotificationCompat.BigTextStyle().bigText(notificationMessage))
                .setContentIntent(pendingIntent);


        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        //notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
        //notificationBuilder.setVibrate(new long[]{0, 100, 200, 300});

        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(not_nu, notificationBuilder.build());
    }


    /*protected void displayNotification(String mMessage) {
        Log.i("Start", "notification");

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
   *//* Invoking the default notification service *//*
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        mBuilder.setContentTitle(getString(R.string.app_name));
        mBuilder.setContentText(mMessage);
        mBuilder.setTicker("New message alert from Ipharma!");
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setColor(getResources().getColor(R.color.greycolor));
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setVibrate(new long[]{0, 100, 200, 300});
        mBuilder.setAutoCancel(true);
   *//* Increase notification number every time a new notification arrives *//*
//		mBuilder.setNumber(++numMessages);

   *//* Add Big View Specific Configuration *//*
//		NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

//		String[] events = new String[6];
//		events[0] = new String("This is first line....");
//		events[1] = new String("This is second line...");
//		events[2] = new String("This is third line...");
//		events[3] = new String("This is 4th line...");
//		events[4] = new String("This is 5th line...");
//		events[5] = new String("This is 6th line...");

        // Sets a title for the Inbox style big view
//		inboxStyle.setBigContentTitle(getString(R.string.app_name));
//		inboxStyle.addLine(mMessage);

        // Moves events into the big view
//		for (int i=0; i < events.length; i++) {
//			inboxStyle.addLine(events[i]);
//		}



        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(mMessage));

   *//* Creates an explicit intent for an Activity in your app *//*
        Intent resultIntent = new Intent(this, HomeScreen.class);
        resultIntent.putExtra("mTypeId", mTypeId);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(HomeScreen.class);

   *//* Adds the Intent that starts the Activity to the top of the stack *//*
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

   *//* notificationID allows you to update the notification later on. *//*
        mNotificationManager.notify(9999, mBuilder.build());
    }*/

    public static int generateRandom() {
        Random random = new Random();
        return random.nextInt(9999 - 1000) + 1000;
    }
}
