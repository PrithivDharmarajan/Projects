package com.smaat.ipharma.adapter;

import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.smaat.ipharma.R;
import com.smaat.ipharma.entity.AlarmEntity;
import com.smaat.ipharma.entity.LocalTimeEntitiy;
import com.smaat.ipharma.fragments.PillReminderListFragment;
import com.smaat.ipharma.utils.AppConstants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 2/8/2017.
 */

public class AlarmAdapter extends BaseAdapter {

    private Holder mHolder;
    private LayoutInflater mLayoutInflater;
    Context mContext;
    ArrayList<AlarmEntity> alentity = null;
    ListView lview;

    public AlarmAdapter(FragmentActivity context, ArrayList<AlarmEntity> alarmEntityArrayList, ListView lv) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(this.mContext);
        alentity = alarmEntityArrayList;
        lview = lv;
    }


    @Override
    public int getCount() {
        return alentity.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class Holder {

        @BindView(R.id.reminder_day_image)
        ImageView mReminderDay;


        @BindView(R.id.reminder_day_name)
        TextView mReminderName;

        @BindView(R.id.reminder_time)
        TextView mReminderTime;

        @BindView(R.id.reminder_check)
        CheckBox mChkReminder;

        @BindView(R.id.click_item)
        LinearLayout mClickItem;


        Holder(View view) {
            ButterKnife.bind(this, view);
        }

    }


    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        mHolder = null;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.adapter_reminder_screen, parent, false);
            mHolder = new AlarmAdapter.Holder(view);
            view.setTag(mHolder);

        } else {
            mHolder = (AlarmAdapter.Holder) view.getTag();
        }
        final AlarmEntity mAlarmEntity = alentity.get(position);
        mHolder.mReminderName.setText(mAlarmEntity.getAlarm_string());
        mHolder.mReminderTime.setText(mAlarmEntity.getAlarm_time());
        mHolder.mReminderDay.setImageResource(mAlarmEntity.getAlarm_image());

        Log.e("mAlarmEntity","mAlarmEntitymAlarmEntity"+mAlarmEntity.isChecked());

        if(mAlarmEntity.isChecked())
        {
            mHolder.mChkReminder.setChecked(true);
        }else{
            mHolder.mChkReminder.setChecked(false);
        }


        mHolder.mReminderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView view  = (TextView)v;
                Log.e("viewview","viewviewview"+view.getText().toString());
                String[] hourmin = view.getText().toString().split(" ");
                String[] hourmin2 = hourmin[0].split(":");
                showTimePickerDialog(v,mAlarmEntity,hourmin2,hourmin);
            }
        });



        mHolder.mChkReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    Random random = new Random();
                    int randomNumber = random.nextInt(9999 - 1000) + 1000;
                    mAlarmEntity.setUniqueId(randomNumber);
                    mAlarmEntity.setChecked(true);
                    buttonView.setChecked(true);
                }else{
                    mAlarmEntity.setUniqueId(0);
                    mAlarmEntity.setChecked(false);
                    buttonView.setChecked(false);
                }
                notifyDataSetChanged();
            }
        });
        mHolder.mClickItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox chk =(CheckBox)v.findViewById(R.id.reminder_check);
                if(chk.isChecked())
                {
                    Random random = new Random();
                    int randomNumber = random.nextInt(9999 - 1000) + 1000;
                    mAlarmEntity.setUniqueId(randomNumber);
                    mAlarmEntity.setChecked(false);
                    chk.setChecked(false);
                }else{
                    mAlarmEntity.setUniqueId(0);
                    mAlarmEntity.setChecked(true);
                    chk.setChecked(true);
                }
                notifyDataSetChanged();
            }
        });

        return view;
    }



    private void showTimePickerDialog(final View v,final AlarmEntity alarmEntity,String[] datetime,String[] ampm) {

        final Calendar c = Calendar.getInstance();
        int mHour;
        int mMinute;
        if (AppConstants.EDITLOCALTIMEENTITIY == null) {

            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
        }else{
            mHour = Integer.parseInt(datetime[0]);
            if(ampm[1].equalsIgnoreCase("pm"))
            {
                mHour = Integer.parseInt(datetime[0])+12;
            }

            /*if (mHour > 12) {
                mHour =  mHour+12;
            }*/

            mMinute = Integer.parseInt(datetime[1]);
        }


        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(mContext,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String aMpM = "AM";
                        if (hourOfDay > 11) {
                            aMpM = "PM";
                        }
                        //Make the 24 hour time format to 12 hour time format
                        String mHour = "";
                        String mMin = "";
                        int currentHour;
                        if (hourOfDay > 12) {
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


                        String mTimeStr = mHour + ":" + mMin + " " + aMpM;
                        TextView time = (TextView)v;
                        time.setText(mTimeStr);
                        alarmEntity.setAlarm_time(mTimeStr);
                        alarmEntity.setChecked(true);
                        Random random = new Random();
                        int randomNumber = random.nextInt(9999 - 1000) + 1000;
                        alarmEntity.setUniqueId(randomNumber);
                        notifyDataSetChanged();

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();

    }


}
