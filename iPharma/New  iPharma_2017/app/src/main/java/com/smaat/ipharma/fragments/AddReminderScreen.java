package com.smaat.ipharma.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.smaat.ipharma.R;
import com.smaat.ipharma.adapter.AlarmAdapter;
import com.smaat.ipharma.entity.AlarmEntity;
import com.smaat.ipharma.entity.LocalTimeEntitiy;
import com.smaat.ipharma.main.BaseActivity;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.utils.AlarmReceiver;
import com.smaat.ipharma.utils.AppConstants;
import com.smaat.ipharma.utils.DialogManager;
import com.smaat.ipharma.utils.GlobalMethods;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Smaat on 1/31/2017.
 */

public class AddReminderScreen extends BaseFragment {

    @BindView(R.id.parent_layout)
    RelativeLayout m_linParent;


    @BindView(R.id.listview_reminder)
    ListView mListviewReminder;

    @BindView(R.id.pill_title_edt)
    EditText mPillTitledt;

    /*@BindView(R.id.pill_add_btn)
    ImageView mAddImg;*/

    @BindView(R.id.mrg_time_txt)
    TextView mMorningtimeTxt;
    @BindView(R.id.afternoon_time_txt)
    TextView mAfternoontimeTxt;
    @BindView(R.id.eve_time_txt)
    TextView mEveningtimeTxt;
    @BindView(R.id.night_time_txt)
    TextView mNighttimeTxt;
    private String mSessionType = "", mStrmorning = "", mStrafter = "", mStreve = "", mStrnight = "";
    ArrayList<LocalTimeEntitiy> mLocalTimeArrayList = new ArrayList<LocalTimeEntitiy>();
    private LocalTimeEntitiy mLocalTimeEntitiy = new LocalTimeEntitiy();
    ArrayList<String> alarmarray = new ArrayList<>();
    View m_pill_reminderRootView;
    ArrayList<AlarmEntity> alarmlist = new ArrayList<AlarmEntity>();
    long timemillisec = 0;
    AlarmEntity entity;
    Random random;
    int randomNumber;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        m_pill_reminderRootView = inflater.inflate(R.layout.pill_reminder_layout, container, false);
        ButterKnife.bind(this, m_pill_reminderRootView);
        random = new Random();
        setData();
        setupUI(m_linParent);
        return m_pill_reminderRootView;

    }

    private void setData() {
        alarmlist.clear();
        random = new Random();
        randomNumber = random.nextInt(9999 - 1000) + 1000;
        if (AppConstants.EDITLOCALTIMEENTITIY != null) {
            mLocalTimeEntitiy = AppConstants.EDITLOCALTIMEENTITIY;
            entity = new AlarmEntity();
            entity.setAlarm_image(R.drawable.pill_reminder_morning);
            entity.setAlarm_string("Morning");
            if(mLocalTimeEntitiy.getMorningtime()!=null) {
                entity.setAlarm_time(mLocalTimeEntitiy.getMorningtime());
                entity.setChecked(true);
                entity.setUniqueId(randomNumber);
            } else{
                entity.setChecked(false);
                entity.setAlarm_time("06:00 AM");
                entity.setUniqueId(0);
            }

            alarmlist.add(entity);
            randomNumber = random.nextInt(9999 - 1000) + 1000;
            entity = new AlarmEntity();
            entity.setAlarm_image(R.drawable.pill_reminder_afternoon);
            entity.setAlarm_string("Afternoon");

            if(mLocalTimeEntitiy.getAfternoontime()!=null) {
                entity.setChecked(true);
                entity.setAlarm_time(mLocalTimeEntitiy.getAfternoontime());
                entity.setUniqueId(randomNumber);
            }else{
                entity.setChecked(false);
                entity.setAlarm_time("12:00 AM");
                entity.setUniqueId(0);
            }

            alarmlist.add(entity);
            randomNumber = random.nextInt(9999 - 1000) + 1000;
            entity = new AlarmEntity();
            entity.setAlarm_image(R.drawable.pill_reminder_evening);
            entity.setAlarm_string("Evening");

            if(mLocalTimeEntitiy.getEveningtime()!=null){
                entity.setChecked(true);
                entity.setAlarm_time(mLocalTimeEntitiy.getEveningtime());
                entity.setUniqueId(randomNumber);
            } else{
                entity.setChecked(false);
                entity.setAlarm_time("04:00 PM");
                entity.setUniqueId(0);
            }
            alarmlist.add(entity);

            entity = new AlarmEntity();
            entity.setAlarm_image(R.drawable.pill_reminder_night);
            entity.setAlarm_string("Night");
            randomNumber = random.nextInt(9999 - 1000) + 1000;

            if(mLocalTimeEntitiy.getNighttime()!=null){
                entity.setChecked(true);
                entity.setAlarm_time(mLocalTimeEntitiy.getNighttime());
                entity.setUniqueId(randomNumber);
            } else{
                entity.setChecked(false);
                entity.setAlarm_time("09:00 PM");
                entity.setUniqueId(0);
            }

            alarmlist.add(entity);
            mPillTitledt.setText(mLocalTimeEntitiy.getTitle());
            AlarmAdapter adapter = new AlarmAdapter(getActivity(),alarmlist,mListviewReminder);
            mListviewReminder.setAdapter(adapter);
        }else{
            random = new Random();
            randomNumber = random.nextInt(9999 - 1000) + 1000;
            entity = new AlarmEntity();
            entity.setAlarm_image(R.drawable.pill_reminder_morning);
            entity.setAlarm_string("Morning");
            entity.setAlarm_time("06:00 AM");
            entity.setUniqueId(randomNumber);
            entity.setChecked(false);
            alarmlist.add(entity);
            randomNumber = random.nextInt(9999 - 1000) + 1000;
            entity = new AlarmEntity();
            entity.setAlarm_image(R.drawable.pill_reminder_afternoon);
            entity.setAlarm_string("Afternoon");
            entity.setAlarm_time("12:00 PM");
            entity.setChecked(false);
            entity.setUniqueId(randomNumber);
            alarmlist.add(entity);
            randomNumber = random.nextInt(9999 - 1000) + 1000;
            entity = new AlarmEntity();
            entity.setAlarm_image(R.drawable.pill_reminder_evening);
            entity.setAlarm_string("Evening");
            entity.setAlarm_time("04:00 PM");
            entity.setChecked(false);
            entity.setUniqueId(randomNumber);
            alarmlist.add(entity);
            randomNumber = random.nextInt(9999 - 1000) + 1000;
            entity = new AlarmEntity();
            entity.setAlarm_image(R.drawable.pill_reminder_night);
            entity.setAlarm_string("Night");
            entity.setAlarm_time("09:00 PM");
            entity.setChecked(false);
            entity.setUniqueId(randomNumber);
            alarmlist.add(entity);

            AlarmAdapter adapter = new AlarmAdapter(getActivity(),alarmlist,mListviewReminder);
            mListviewReminder.setAdapter(adapter);
            alarmarray.clear();
        }

    }


    public void addreminder()
    {

        addTimevalues();


        /*if (AppConstants.EDITLOCALTIMEENTITIY == null) {

        } else {
            LocalTimeEntitiy localTimeEntitiy = new LocalTimeEntitiy();
            localTimeEntitiy.setTitle(mPillTitledt.getText().toString());
            localTimeEntitiy.setMorningtime(mStrmorning);
            localTimeEntitiy.setAfternoontime(mStrafter);
            localTimeEntitiy.setEveningtime(mStreve);
            localTimeEntitiy.setNighttime(mStrnight);
            localTimeEntitiy.setAlarmuniqueid(UNIQUE_ID);
            mLocalTimeArrayList.add(localTimeEntitiy);
            AppConstants.EDIT_REMINDER_INTERFACE.editReminderCallback(localTimeEntitiy);
            ((HomeScreen) getActivity()).pushFragment(new PillReminderListFragment());


        }*/
    }

    @OnClick({R.id.morning_lay, R.id.afternoon_lay, R.id.evening_lay, R.id.night_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.morning_lay:
                mSessionType = getString(R.string.morning);
                showTimePickerDialog();
                break;
            case R.id.afternoon_lay:
                mSessionType = getString(R.string.afternoon);
                showTimePickerDialog();
                break;
            case R.id.evening_lay:
                mSessionType = getString(R.string.evening);

                showTimePickerDialog();
                break;
            case R.id.night_lay:
                mSessionType = getString(R.string.night);
                showTimePickerDialog();

                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppConstants.EDITLOCALTIMEENTITIY == null) {
            ((HomeScreen) getActivity()).setToolbarTitle(getString(R.string.add_reminder),getString(R.string.save_reminder));
        }else{
            ((HomeScreen) getActivity()).setToolbarTitle(getString(R.string.update_reminder),getString(R.string.update_reminder));
        }


    }



    private void addTimevalues() {
        if (mPillTitledt.getText().toString().trim().isEmpty()) {
            DialogManager.showMsgPopup(getActivity(), getString(R.string.app_name), getString(R.string.reminder_title_empty));
        } else if (!alarmlist.get(0).isChecked()&&
                !alarmlist.get(1).isChecked()&&
                !alarmlist.get(2).isChecked()&&
                !alarmlist.get(3).isChecked()) {
            DialogManager.showMsgPopup(getActivity(), getString(R.string.app_name), getString(R.string.select_any_session));
        } else {

            LocalTimeEntitiy localTimeEntitiy = new LocalTimeEntitiy();
            localTimeEntitiy.setTitle(mPillTitledt.getText().toString());
            if(alarmlist.get(0).isChecked())
            localTimeEntitiy.setMorningtime(alarmlist.get(0).getAlarm_time());
            if(alarmlist.get(1).isChecked())
            localTimeEntitiy.setAfternoontime(alarmlist.get(1).getAlarm_time());
            if(alarmlist.get(2).isChecked())
            localTimeEntitiy.setEveningtime(alarmlist.get(2).getAlarm_time());
            if(alarmlist.get(3).isChecked())
            localTimeEntitiy.setNighttime(alarmlist.get(3).getAlarm_time());

            ArrayList<Integer> iniqarray = new ArrayList<Integer>();
            iniqarray.add(alarmlist.get(0).getUniqueId());
            iniqarray.add(alarmlist.get(1).getUniqueId());
            iniqarray.add(alarmlist.get(2).getUniqueId());
            iniqarray.add(alarmlist.get(3).getUniqueId());
            localTimeEntitiy.setUnique_id(iniqarray);

            mLocalTimeArrayList.add(localTimeEntitiy);


            if (AppConstants.EDITLOCALTIMEENTITIY == null) {
                Random random = new Random();
                setAlarm(mPillTitledt.getText().toString());
                AppConstants.ADD_REMINDER_INTERFACE.createReminderCallback(localTimeEntitiy);
            }else{
                setAlarm(mPillTitledt.getText().toString());
                AppConstants.EDIT_REMINDER_INTERFACE.editReminderCallback(localTimeEntitiy);
            }
            ((HomeScreen) getActivity()).pushFragment(new PillReminderListFragment());
            //finishScreen();

        }


    }

    public interface AddReminderInterface {
        void createReminderCallback(LocalTimeEntitiy mPlansEntity);
    }
    public interface EditReminderInterface {
        void editReminderCallback(LocalTimeEntitiy mPlansEntity);
    }
    private void showTimePickerDialog() {

        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        final int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        /*String aMpM = "AM";
                        if (hourOfDay > 11) {
                            aMpM = "PM";
                        }*/
                        //Make the 24 hour time format to 12 hour time format
                        String mHour = "";
                        String mMin = "";
                        int currentHour;
                        if (hourOfDay > 11) {
                            currentHour = hourOfDay - 12;
                        } else {
                            currentHour = hourOfDay;
                        }

                        if(currentHour  < 10){
                            mHour = "0"+currentHour;

                        }else{
                            mHour = ""+currentHour;
                        }

                        if(minute  < 10){
                            mMin = "0"+minute;

                        }else{
                            mMin = ""+minute;
                        }
                        String mTimeStr = mHour + ":" + mMin;
                        setTime(mTimeStr);


                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();

    }

    private void setTime(String time) {

        switch (mSessionType) {
            case "Morning":
                mStrmorning = time;
                mMorningtimeTxt.setText(time);
                break;
            case "Afternoon":
                mStrafter = time;
                mAfternoontimeTxt.setText(time);
                break;
            case "Evening":
                mStreve = time;
                mEveningtimeTxt.setText(time);
                break;
            case "Night":
                mStrnight = time;
                mNighttimeTxt.setText(time);
                break;

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppConstants.EDITLOCALTIMEENTITIY = null;
    }


    public void setAlarm(String altitle)
    {
        for(int i=0;i<alarmlist.size();i++)
        {
            if(alarmlist.get(i).isChecked())
            {
                if(alarmlist.get(i).getAlarm_time()!=null)
                {

                    if(alarmlist.get(i).getUniqueId()!=0)
                    {
                        String Alarm_Uniqueid = String.valueOf(alarmlist.get(i).getUniqueId());
                        AlarmManager alarmMgr = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                        Intent receiverIntent = new Intent(getActivity(), AlarmReceiver.class);
                        receiverIntent.putExtra("AlarmTime",alarmlist.get(i).getAlarm_time());
                        receiverIntent.putExtra("AlarmTitle",altitle);
                        PendingIntent alarmIntent = PendingIntent.getBroadcast(getActivity(), Integer.parseInt(Alarm_Uniqueid), receiverIntent, 0); //The second parameter is unique to this PendingIntent,
                        //if you want to make more alarms,
                        //make sure to change the 0 to another integer
                        Log.e("getAlarm_time","getAlarm_time"+alarmlist.get(i).getAlarm_time());
                        String alval[] = alarmlist.get(i).getAlarm_time().split(" ");
                        String alarmhours[] =alval[0].split(":");
                        int hour = Integer.parseInt(alarmhours[0]);
                        int minute = Integer.parseInt(alarmhours[1]);

                        Calendar alarmCalendarTime = Calendar.getInstance();
                        alarmCalendarTime.set(Calendar.HOUR_OF_DAY, hour);
                        alarmCalendarTime.set(Calendar.MINUTE, minute);
                        alarmCalendarTime.set(Calendar.SECOND, 0);
                        if(alval[1].equalsIgnoreCase("AM"))
                        {
                            alarmCalendarTime.set(Calendar.AM_PM, Calendar.AM);
                        }else{
                            alarmCalendarTime.set(Calendar.AM_PM, Calendar.PM);
                        }
                        if (alarmCalendarTime.before(Calendar.getInstance())) {
                            alarmCalendarTime.add(Calendar.DAY_OF_MONTH, 1);
                        }
                        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, alarmCalendarTime.getTimeInMillis(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS), alarmIntent);

                    }

                }


            }
        }
    }
}