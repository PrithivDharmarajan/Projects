package com.smaat.ipharma.fragment;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.smaat.ipharma.R;
import com.smaat.ipharma.adapter.AddPillTimeReminderAdapter;
import com.smaat.ipharma.database.DatabaseUtil;
import com.smaat.ipharma.db.model.PillTimerResponse;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.util.AppConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class AddNewReminderFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private RelativeLayout mAddTimeLay;
    private Spinner mDurationTypeSpinner, mDurationTimeSpinner;
    private List<String> mDurationTypeList;
    private int mHour, mMinute;
    private String mTime_input = "HH:mm", mTime_output = "hh:mm a", mTime;
    private ArrayList<String> mPillTimeList, mMedicineNameList;
    public static ArrayList<String> mLocTimeList = new ArrayList<>();
    private AddPillTimeReminderAdapter mPillTimeAdapter;
    private RecyclerView mPillReminderTimeList;
    private AutoCompleteTextView mMedicineList;
    ArrayList<String> medicineList;
    private ScrollView mMedLay;
    private TextView mTabNameTxt;
    private LinearLayout mTabLay;
    DatabaseUtil db;
    public static int mReminderID;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.add_new_reminder_screen, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup viewGroup = (ViewGroup) view.findViewById(R.id.parent_lay);
        setupUI(viewGroup);
        setFont(viewGroup, mHelvetica);

        db = new DatabaseUtil();
        initiComponents(view);
        setClickListener();
        setData();


        AppConstants.FRAG = AppConstants.PILL_REM_SCREEEN;
        hideSoftKeyboard(getActivity());
        HomeScreen.mHeaderLeftLay.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                mLocTimeList = new ArrayList<>();
                HomeScreen.onBackMove(getActivity());
            }
        });

    }


    private void initiComponents(View view) {

        mMedLay = (ScrollView) view.findViewById(R.id.med_lay);
        mTabNameTxt = (TextView) view.findViewById(R.id.tab_name_txt);
        mTabLay = (LinearLayout) view.findViewById(R.id.tab_lay);

        mDurationTypeSpinner = (Spinner) view.findViewById(R.id.time_dur_spinner);
        mDurationTimeSpinner = (Spinner) view.findViewById(R.id.time_sub_dur_spinner);
        mAddTimeLay = (RelativeLayout) view.findViewById(R.id.add_time_lay);
        mPillReminderTimeList = (RecyclerView) view.findViewById(R.id.pill_reminder_time_list);

        mMedicineList = (AutoCompleteTextView) view.findViewById(R.id.tab_list);
        mMedicineList.setOnItemClickListener(this);

        mDurationTypeList = Arrays.asList(getResources().getStringArray(R.array.string_duration));


        mMedicineList
                .setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    public boolean onEditorAction(TextView v, int actionId,
                                                  KeyEvent event) {

                        mMedicineList.dismissDropDown();
                        hideSoftKeyboard(getActivity());

                        String mList = mMedicineList.getText().toString().trim();
                        if (mList.length() > 1) {
                            searchMedicineList(mList);
                        }
                        return true;
                    }
                });

        mMedicineList.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String mCityValue = mMedicineList.getText().toString()
                        .trim();
                if (mCityValue.length() > 1) {
                    searchMedicineList(mCityValue);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


//        String m=mTimeDurSpinner.getSelectedItem().toString();
//        String wm=mTimeSubDurSpinner.getSelectedItem().toString();

        mMedicineNameList = new ArrayList<>();
        mMedicineNameList.add("Abacavir Sulfate");
        mMedicineNameList.add("Abatacept Injection");
        mMedicineNameList.add("Abilify");
        mMedicineNameList.add("Abitrexate");
        mMedicineNameList.add("Abraxane");
        mMedicineNameList.add("Absorica");
        mMedicineNameList.add("Abstral");
        mMedicineNameList.add("Accolate");
        mMedicineNameList.add("Aceta-Gesic");
        mMedicineNameList.add("Aspirin");
        mMedicineNameList.add("Acetaminophen");


        HomeScreen.mFooterText.setText(R.string.done);


    }

    private void setClickListener() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, mDurationTypeList);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        mDurationTypeSpinner.setAdapter(adapter);


        //Time Spinner item click
        mDurationTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                setDaysAdapter(position);

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mDurationTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //set Time dialog
        mAddTimeLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        HomeScreen.mBottombar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mTimeList = "";
                for (int i = 0; i < mPillTimeList.size(); i++) {
                    if (mTimeList.isEmpty()) {
                        mTimeList = mPillTimeList.get(i);
                    } else {
                        mTimeList += "-" + mPillTimeList.get(i);
                    }

                }

                PillTimerResponse pillReminderRes = new PillTimerResponse();

                pillReminderRes.setId(mReminderID);
                pillReminderRes.setTablet_name(mTabNameTxt.getText().toString().trim());
                pillReminderRes.setDuration_type(mDurationTypeSpinner.getSelectedItem().toString());
                pillReminderRes.setDuration_time(mDurationTimeSpinner
                        .getSelectedItem().toString());
                pillReminderRes.setPill_timing(mTimeList);

                if (mReminderID == 0) {
                    db.pillReminderInsert(getActivity(), pillReminderRes);
                } else {
                    db.updatePillReminderData(getActivity(), pillReminderRes);
                }

//                db.pillReminderInsert(getActivity(), mTabNameTxt.getText().toString().trim(),
//                        mTimeDurSpinner.getSelectedItem().toString(), mTimeSubDurSpinner
//                                .getSelectedItem().toString(), mTimeList);
            }
        });
    }

    private void setData() {
        if (mReminderID != 0) {
//            PillTimerResponse mDBOldPillTimeRes = new PillTimerResponse();
            PillTimerResponse mDBOldPillTimeRes = db.getPillReminderData(getActivity(), mReminderID);

            for (int pos = 0; pos < mDurationTypeList.size(); pos++) {

                if (mDBOldPillTimeRes.getDuration_type().equalsIgnoreCase(mDurationTypeList.get(pos))) {
                    mDurationTypeSpinner.setSelection(pos);
                    setDaysAdapter(pos);
                    if (pos != 0) {

                        int listSize = (pos == 1 ? 31 : 5);

                        ArrayList<String> mtes = daysList(listSize);

                        for (int durSub = 0; durSub < mtes.size(); durSub++) {
                            if (mDBOldPillTimeRes.getDuration_time().equalsIgnoreCase(mtes.get(durSub))) {
                                mDurationTimeSpinner.setSelection(durSub);
//                                break;
                            }
                        }
                    }
                    break;
                }

            }


            String mPillTime[] = mDBOldPillTimeRes.getPill_timing().split("\\-");
            Collections.addAll(mLocTimeList, mPillTime);
            setAdapter(mLocTimeList);

            hideSoftKeyboard(getActivity());
            mTabLay.setVisibility(View.GONE);
            mTabNameTxt.setText(mDBOldPillTimeRes.getTablet_name());
            mTabNameTxt.setVisibility(View.VISIBLE);
            mMedLay.setVisibility(View.VISIBLE);

        } else {
            mLocTimeList = new ArrayList<>();
        }
    }

    private void setDaysAdapter(int position) {

        int listSize;

        if (position == 0) {
            listSize = 0;
            mDurationTimeSpinner.setEnabled(false);
        } else {
            mDurationTimeSpinner.setEnabled(true);
            listSize = (position == 1 ? 31 : 5);

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, daysList(listSize));
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        mDurationTimeSpinner.setAdapter(adapter);
    }


    private ArrayList<String> daysList(int limitValue) {
        ArrayList<String> mDurationTimeList = new ArrayList<>();
        for (int i = 1; i <= limitValue; i++) {
            mDurationTimeList.add(i + "");
        }
        return mDurationTimeList;

    }


    //    hh:mm aa"
    private void showTimePickerDialog() {
        final Calendar cal = Calendar.getInstance();
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);
//        mSec = cal.get(Calendar.SECOND);
        new TimePickerDialog(getActivity(), mTimeSetListener, mHour, mMinute, false)
                .show();
    }

    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mHour = hourOfDay;
            mMinute = minute;

            String time = String.valueOf(mHour) + ":" +
                    mMinute;
//            String time = new StringBuilder().append(mHour).append(":")
//                    .append(mMinute).toString();

            SimpleDateFormat timeinputFormat = new SimpleDateFormat(
                    mTime_input, Locale.getDefault());
            Date date1;
            String str1;

            try {
                date1 = timeinputFormat.parse(time);
                str1 = timeinputFormat.format(date1);

                Date dateobj;
                SimpleDateFormat mServerFormat = new SimpleDateFormat("HH:mm", Locale.US);
                SimpleDateFormat mTargetFormat = new SimpleDateFormat("hh:mm aa", Locale.US);
                String ruturnDateFormate = "";
                try {

//                    mServerFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    dateobj = mServerFormat.parse(str1);
                    mTargetFormat.setTimeZone(TimeZone.getDefault());
                    ruturnDateFormate = mTargetFormat.format(dateobj);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mTime = ruturnDateFormate;
                mLocTimeList.add(mTime);
                setAdapter(mLocTimeList);
//                mInputBox.setText(mStartDate + " " + mTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

    };


    private void setAdapter(ArrayList<String> mList) {
        mPillReminderTimeList.setVisibility(View.VISIBLE);
        HomeScreen.mBottombar.setVisibility(View.VISIBLE);

        if (mPillTimeAdapter == null) {
            mPillTimeList = new ArrayList<>();
            mPillTimeList.addAll(mList);
            mPillTimeAdapter = new AddPillTimeReminderAdapter(getActivity(), mPillTimeList);
            mPillReminderTimeList.setLayoutManager(new LinearLayoutManager(getActivity()));
            mPillReminderTimeList.setAdapter(mPillTimeAdapter);

        } else {
            mPillTimeList.clear();
            mPillTimeList.addAll(mList);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mPillTimeAdapter.notifyDataSetChanged();
                }
            });

        }

    }

    private void searchMedicineList(String mSearchVal) {

        medicineList = new ArrayList<>();
        if (mMedicineNameList != null && mMedicineNameList.size() > 0) {
            medicineList.clear();
            for (int i = 0; i < mMedicineNameList.size(); i++) {
                String universit = mMedicineNameList.get(i).toLowerCase(Locale.ENGLISH);
                if (universit.contains(mSearchVal.toLowerCase(Locale.ENGLISH))) {

                    medicineList.add(mMedicineNameList.get(i));

                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_list_item_1, android.R.id.text1, medicineList);
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                mMedicineList.setAdapter(adapter);
            }
        }


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        hideSoftKeyboard(getActivity());
        mTabLay.setVisibility(View.GONE);
        mTabNameTxt.setText(mMedicineNameList.get(position));
        mTabNameTxt.setVisibility(View.VISIBLE);
        mMedLay.setVisibility(View.VISIBLE);


    }
}
