package com.smaat.ipharma.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smaat.ipharma.R;
import com.smaat.ipharma.apiinterface.APIRequestHandler;
import com.smaat.ipharma.entity.AlarmObject;
import com.smaat.ipharma.entity.CommonResponse;
import com.smaat.ipharma.main.BaseActivity;
import com.smaat.ipharma.ui.LoginScreen;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by admin on 12/9/2016.
 */

public class NotifySMSReceived extends BaseActivity {
    private static final String LOG_TAG = "SMSReceiver";
    public static final int NOTIFICATION_ID_RECEIVED = 0x1221;
    static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @BindView(R.id.alarm_title)
    TextView mAlarmTitle;
    @BindView(R.id.alarm_time)
    TextView mAlarmTime;

    @BindView(R.id.done_button)
    ImageView mDoneButton;

    Handler handler = null;

    BroadcastReceiver sendBroadcastReceiver = null;

    BroadcastReceiver deliveryBroadcastReceiver = null;
    PendingIntent sentPI,deliveredPI;
    String mPhoneNumber = "";
    Runnable mRunnable;
    Ringtone ringtone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_alarm_view);
        ButterKnife.bind(this);
        if (getIntent().getExtras() != null) {
            String alarmTime = getIntent().getStringExtra("AlarmTime");
            String alarmTitle = getIntent().getStringExtra("AlarmTitle");
            Uri myUri = Uri.parse(getIntent().getStringExtra("AlarmUri"));
            ringtone = RingtoneManager.getRingtone(getApplicationContext(), myUri);
            ringtone.play();
            //ringtone.setStreamType(AudioManager.STREAM_ALARM);
            mAlarmTitle.setText(alarmTitle);
            mAlarmTime.setText(alarmTime);
        }


        handler=new Handler();
        mRunnable=new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Log.e("asasasasasas","asasasasasas");
                if(!GlobalMethods.getStringValue(getApplicationContext(),AppConstants.GUARDIAN_NUMBER).isEmpty())
                {
                    mPhoneNumber =  GlobalMethods.getStringValue(getApplicationContext(),AppConstants.GUARDIAN_NUMBER);
                }

                if(!GlobalMethods.getStringValue(getApplicationContext(),AppConstants.DOCTOR_NUMBER).isEmpty())
                {
                    mPhoneNumber =  GlobalMethods.getStringValue(getApplicationContext(),AppConstants.DOCTOR_NUMBER);
                }

                if(GlobalMethods.isConnectingToInternet(getApplicationContext()))
                {
                    if(!mPhoneNumber.isEmpty())
                    {
                        APIRequestHandler.getInstance().SendSms(GlobalMethods.getUserID(getApplicationContext()),
                                mPhoneNumber,
                                "Medicine-"+getIntent().getStringExtra("AlarmTitle")+":"+GlobalMethods.getStringValue(getApplicationContext(),AppConstants.USER_NAME)+"Not taken this at time",NotifySMSReceived.this);
                    }

                }else{
                    if(!mPhoneNumber.isEmpty())
                    {
                        sendSMS(mPhoneNumber,"Medicine-"+getIntent().getStringExtra("AlarmTitle")+":"+GlobalMethods.getStringValue(getApplicationContext(),
                                AppConstants.USER_NAME)+"Not taken this at time");
                    }

                }
            }
        };
        handler.postDelayed(mRunnable,300000);


        /*handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("asasasasasas","asasasasasas");


            }
        }, 60000);*/

        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(mRunnable);
                if(ringtone!=null) {
                    ringtone.stop();
                }
                onBackPressed();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(mRunnable);
    }

    private void sendSMS(String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        sentPI= PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);


        deliveredPI= PendingIntent.getBroadcast(this, 0,new Intent(DELIVERED),0);
        //---SMS has been sent---
       sendBroadcastReceiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        /*Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();*/
                        //KillActivity();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        /*Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();*/
                        //KillActivity();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        /*Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();*/
                        //KillActivity();

                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        /*Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();*/
                       // KillActivity();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        /*Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();*/
                       // KillActivity();
                        break;
                }
            }
        };
        registerReceiver(sendBroadcastReceiver, new IntentFilter(SENT));
        //---when the SMS has been delivered---
        deliveryBroadcastReceiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        /*Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();*/
                        //KillActivity();
                        break;
                    case Activity.RESULT_CANCELED:
                        /*Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();*/
                        //KillActivity();
                        break;
                }
            }
        };


        registerReceiver(deliveryBroadcastReceiver, new IntentFilter(DELIVERED));
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null,message, sentPI, deliveredPI);


    }

    @Override
    protected void onStop() {
        try {
            if (sentPI != null) {
                unregisterReceiver(sendBroadcastReceiver);
                sentPI = null;
            }

            if (deliveredPI != null) {
                unregisterReceiver(deliveryBroadcastReceiver);
                deliveredPI =null;
            }
        } catch (Exception e) {

        }
        super.onStop();
    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);
        if (responseObj instanceof AlarmObject) {
            AlarmObject msg = (AlarmObject) responseObj;
            if(msg.getMsg().equalsIgnoreCase(AppConstants.SUCCESS))
            {
                //KillActivity();
            }
        }
    }

    public void KillActivity()
    {
        onBackPressed();
        finish();
    }

}