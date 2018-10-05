package com.bridgellc.bridge.ui.upload;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.main.BaseFragmentActivity;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.GlobalMethods;
import com.bridgellc.bridge.utils.TypefaceSingleton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class UploadItemTicketDateFragment extends Fragment implements DialogMangerOkCallback {

    private View view;
    private Button mNextBtn;
    private EditText mInputBox;
    private RelativeLayout mInputBoxLay;
    private TextView mTitleTxt;
    private SimpleDateFormat mNewLocalFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm aa", Locale.US);
    SimpleDateFormat mDefaultFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm aa", Locale.US);
    SimpleDateFormat mTargetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.upload_ticket_date, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (UploadScreen.mEntity.event_date_time != null && UploadScreen.mEntity.event_date_time.length() > 0) {
//            mInputBox.setText(UploadScreen.mEntity.event_date_time);
//            mInputBox.setText(GlobalMethods.getCustomDateFormate(UploadScreen.mEntity.event_date_time, mNewLocalFormat));
            mInputBox.setText(GlobalMethods.gettwoDateFormate(UploadScreen.mEntity.event_date_time, mTargetFormat, mDefaultFormat));
//
        }
    }

    private void initComponents() {
        mNextBtn = (Button) view.findViewById(R.id.footer_btn);
        mInputBox = (EditText) view.findViewById(R.id.input_box);
        mInputBoxLay = (RelativeLayout) view.findViewById(R.id.inpt_box_lay);
        mTitleTxt = (TextView) view.findViewById(R.id.title_tv);

        mNextBtn.setText(getString(R.string.next));

        Typeface mLightFont = TypefaceSingleton.getTypeface().getLightFont(getActivity());
        mNextBtn.setTypeface(mLightFont);
        mInputBox.setTypeface(mLightFont);
        mTitleTxt.setTypeface(mLightFont);


        mInputBox.setFocusable(false);
        mInputBox.setFocusableInTouchMode(false);
        mInputBox.setClickable(false);
//        mInputBoxLay.setOnClickListener((View.OnClickListener) getActivity());
//
//        TextView titleTv = (TextView) view.findViewById(R.id.title_tv);
//        if (UploadScreen.mEntity.isSelling) {
//            if (UploadScreen.mEntity.isGood) {
//                titleTv.setText(getString(R.string.how_much_for_item));
//            } else {
//                titleTv.setText(getString(R.string.how_much_for_service));
//            }
//        } else {
//            if (UploadScreen.mEntity.isGood) {
//                titleTv.setText(getString(R.string.how_much_pay_item));
//            } else {
//                titleTv.setText(getString(R.string.how_much_pay_service));
//            }
//        }
        mInputBox.setText("");
        mInputBoxLay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mInputBox.getText().toString().trim().length() > 0) {
//                    UploadScreen.mEntity.event_date_time = mInputBox.getText().toString().trim();


//                    Date dateobj;
//                    SimpleDateFormat mServerFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
//                    SimpleDateFormat mTargetFormat = mSerFormat;
//                    String ruturnDateFormate = "";
//                    try {
//
//                        mServerFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
//                        dateobj = mServerFormat.parse(mInputBox.getText().toString().trim());
//                        mTargetFormat.setTimeZone(TimeZone.getDefault());
//                        ruturnDateFormate = mTargetFormat.format(dateobj);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }

                    UploadScreen.mEntity.event_date_time = GlobalMethods.gettwoDateFormate(mInputBox.getText().toString().trim(), mDefaultFormat, mTargetFormat);
//                    Date dateobj;
//                    //create a new Date object using the UTC timezone
//
////        SimpleDateFormat mServerFormat = new SimpleDateFormat("yyyy-mm-DD HH:mm:ss");
//
//                    String ruturnDateFormate = "";
//                    try {
//
////                        mServerFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
//                        dateobj = mServerFormat.parse(mInputBox.getText().toString().trim());
//                        mTargetFormat.setTimeZone(TimeZone.getDefault());
//                        ruturnDateFormate = mTargetFormat.format(dateobj);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }


//                    UploadScreen.mEntity.event_date_time = ruturnDateFormate;
//
//                    System.out.println("DateFor--" + ruturnDateFormate);
//                    ((BaseFragmentActivity) getActivity()).addFragment(new UploadItemTicketVenueFragment(), 6);
                    ((BaseFragmentActivity) getActivity()).addFragment(new UploadItemTicketVenueFragment(), 5);

                } else {
                    DialogManager.showBasicAlertDialog(getActivity(), getString(R.string.pick_date), UploadItemTicketDateFragment.this);

                }
            }
        });

    }

    private int mDate, mMonth, mYear;
    private String mDateInput = "MM-dd-yyyy", mStartDate;

    private void showDatePickerDialog() {
        final Calendar cal = Calendar.getInstance();
        mDate = cal.get(Calendar.DAY_OF_MONTH);
        mMonth = cal.get(Calendar.MONTH);
        mYear = cal.get(Calendar.YEAR);

        DatePickerDialog dpd = new DatePickerDialog(getActivity(), mDateSetListener,
                mYear, mMonth, mDate);
        dpd.getDatePicker().setMinDate(cal.getTimeInMillis());
        dpd.show();
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDate = dayOfMonth;
            final Calendar cal = Calendar.getInstance();
            if (mYear <= cal.get(Calendar.YEAR) && mMonth <= cal.get(Calendar.MONTH) && mDate <=
                    cal.get(Calendar.DAY_OF_MONTH)) {
                Toast.makeText(getActivity(), "Select feature date", Toast.LENGTH_LONG).show();
            } else {

                String date = new StringBuilder().append(mMonth + 1).append("-")
                        .append(mDate).append("-").append(mYear).toString();


//                String date = new StringBuilder().append(mYear).append("-")
//                        .append(mMonth + 1).append("-").append(mDate).toString();

                SimpleDateFormat dateinputFormat = new SimpleDateFormat(mDateInput,
                        Locale.US);
                Date date1 = null;
                String str1 = null;

                try {
                    date1 = dateinputFormat.parse(date);
                    str1 = dateinputFormat.format(date1);
                    mStartDate = str1;

                    showTimePickerDialog();
                } catch (Exception e) {
                }

            }
        }

    };

    private int mHour, mMinute, mSec;
    private String mTime_input = "HH:mm", mTime_output = "hh:mm a", mTime;

    //    hh:mm aa"
    private void showTimePickerDialog() {
        final Calendar cal = Calendar.getInstance();
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);
        mSec = cal.get(Calendar.SECOND);
        new TimePickerDialog(getActivity(), mTimeSetListener, mHour, mMinute, false)
                .show();
    }

    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mHour = hourOfDay;
            mMinute = minute;
//            String time = new StringBuilder().append(mHour).append(":")
//                    .append(mMinute).append(":").append(mSec).toString();

            String time = new StringBuilder().append(mHour).append(":")
                    .append(mMinute).toString();

            SimpleDateFormat timeinputFormat = new SimpleDateFormat(
                    mTime_input, Locale.getDefault());
            Date date1 = null;
            String str1 = null;

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
                mInputBox.setText(mStartDate + " " + mTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

    };

    @Override
    public void onOkClick() {

    }

}

