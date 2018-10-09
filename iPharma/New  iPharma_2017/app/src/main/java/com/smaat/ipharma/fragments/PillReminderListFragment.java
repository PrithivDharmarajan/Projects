package com.smaat.ipharma.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.smaat.ipharma.R;
import com.smaat.ipharma.adapter.ReminderAdapter;
import com.smaat.ipharma.entity.LocalTimeEntitiy;
import com.smaat.ipharma.main.BaseActivity;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.utils.AlarmReceiver;
import com.smaat.ipharma.utils.AppConstants;
import com.smaat.ipharma.utils.GlobalMethods;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.ALARM_SERVICE;
import static com.smaat.ipharma.utils.GlobalMethods.STRING_PREFERENCE;

/**
 * Created by Smaat on 1/31/2017.
 */

public class PillReminderListFragment extends BaseFragment implements AddReminderScreen.AddReminderInterface,
        AddReminderScreen.EditReminderInterface {


    @BindView(R.id.reminder_list)
    ListView mListView;

    private ReminderAdapter mReminderAdapter;

    private ArrayList<LocalTimeEntitiy> mLocalTimeEntitiyArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.pill_reminder_list_screen,
                container, false);
        ButterKnife.bind(this, rootview);
        AppConstants.ADD_REMINDER_INTERFACE = this;
        AppConstants.EDIT_REMINDER_INTERFACE = this;
        initview();
        return rootview;
    }

    private void initview() {
        /*AlarmManager alarmMgr = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent receiverIntent = new Intent(getActivity(), AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getActivity(), 0, receiverIntent, 0); //The second parameter is unique to this PendingIntent,
        //if you want to make more alarms,
        //make sure to change the 0 to another integer
        int hour = 04;
        int minute = 8;

        Calendar alarmCalendarTime = Calendar.getInstance();
        alarmCalendarTime.set(Calendar.HOUR_OF_DAY, hour);
        alarmCalendarTime.set(Calendar.MINUTE, minute);
        alarmCalendarTime.set(Calendar.SECOND, 0);
        if (alarmCalendarTime.before(Calendar.getInstance())) {
            alarmCalendarTime.add(Calendar.DAY_OF_MONTH, 1);
        }
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, alarmCalendarTime.getTimeInMillis(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS), alarmIntent);

*/

        mLocalTimeEntitiyArrayList = GlobalMethods.getAlarmlist(getActivity());
        if (mLocalTimeEntitiyArrayList != null) {
            mReminderAdapter = new ReminderAdapter(getActivity(),this, mLocalTimeEntitiyArrayList);
            mListView.setAdapter(mReminderAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeScreen) getActivity()).setToolbarTitle(getString(R.string.pill_reminder),getString(R.string.add_reminder));
    }



    @Override
    public void createReminderCallback(LocalTimeEntitiy mLocalTimeEntitiy) {

        ArrayList<LocalTimeEntitiy> mAddlist = new ArrayList<>();


        mAddlist = GlobalMethods.getAlarmlist(getActivity());
        mAddlist.add(mLocalTimeEntitiy);
        Gson gson = new Gson();
        String Alarmdates = gson.toJson(mAddlist);
        GlobalMethods.storeValuetoPreference(getActivity(), STRING_PREFERENCE, AppConstants.ALARM_ARRAY, Alarmdates);

        initview();

    }

    @Override
    public void editReminderCallback(LocalTimeEntitiy mLocalTimeEntitiy) {
        ArrayList<LocalTimeEntitiy> mAddlist = new ArrayList<>();
        deleteReminder(AppConstants.EDITPOS);
        mAddlist = GlobalMethods.getAlarmlist(getActivity());
        mAddlist.add(AppConstants.EDITPOS, mLocalTimeEntitiy);
        Gson gson = new Gson();
        String Alarmdates = gson.toJson(mAddlist);
        GlobalMethods.storeValuetoPreference(getActivity(), STRING_PREFERENCE, AppConstants.ALARM_ARRAY, Alarmdates);
        initview();
    }

    public void deleteReminder(int pos) {

        final LocalTimeEntitiy mLocalTimeEntitiy = mLocalTimeEntitiyArrayList.get(pos);
        ArrayList<String> mTotlatime = new ArrayList<>();
        if(mLocalTimeEntitiy.getMorningtime()!=null)
            mTotlatime.add(mLocalTimeEntitiy.getMorningtime());
        if(mLocalTimeEntitiy.getAfternoontime()!=null)
            mTotlatime.add(mLocalTimeEntitiy.getAfternoontime());
        if(mLocalTimeEntitiy.getEveningtime()!=null)
            mTotlatime.add(mLocalTimeEntitiy.getEveningtime());
        if(mLocalTimeEntitiy.getNighttime()!=null)
            mTotlatime.add(mLocalTimeEntitiy.getNighttime());

        for(int i=0;i<mTotlatime.size();i++)
        {
            String Alarm_Uniqueid = String.valueOf(mLocalTimeEntitiyArrayList.get(pos).getUnique_id().get(i));
            Intent intent = new Intent(getActivity(), AlarmReceiver.class);
            PendingIntent sender = PendingIntent.getBroadcast(getActivity(), Integer.parseInt(Alarm_Uniqueid), intent, 0);
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
            alarmManager.cancel(sender);
        }
        mLocalTimeEntitiyArrayList.remove(pos);
        Gson gson = new Gson();
        String Alarmdates = gson.toJson(mLocalTimeEntitiyArrayList);
        GlobalMethods.storeValuetoPreference(getActivity(), STRING_PREFERENCE, AppConstants.ALARM_ARRAY, Alarmdates);

        mReminderAdapter.notifyDataSetChanged();

    }
}
