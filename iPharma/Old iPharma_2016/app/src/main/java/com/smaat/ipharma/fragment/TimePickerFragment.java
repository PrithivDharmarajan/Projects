package com.smaat.ipharma.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements
		TimePickerDialog.OnTimeSetListener {

	int inputHour, inputMin;
	String inputTime, inputField;

	@SuppressWarnings("deprecation")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		inputTime = getArguments().getString("inputtime");
		inputField = getArguments().getString("inputfield");

		// Use the current date as the default time in the picker
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		if (!inputTime.equals("")) {

			Date date = null;
			SimpleDateFormat mSDF = new SimpleDateFormat("HH:mm",
					Locale.ENGLISH);
			try {
				date = mSDF.parse(inputTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			hour = date.getHours();
			minute = date.getMinutes();
		}

		// Create a new instance of DatePickerDialog and return it
		// return new TimePickerDialog(getActivity(), this, hour, minute,
		// !DateFormat.is24HourFormat(getActivity()));
		return new TimePickerDialog(getActivity(), this, hour, minute, false);
	}

	// @Override
	// public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	//
	//
	// }

	/*
	 * The activity that creates an instance of this dialog fragment must
	 * implement this interface in order to receive event callbacks. Each method
	 * passes the DialogFragment in case the host needs to query it.
	 */
	public interface NoticeDialogListener {
		public void onTimeSelected(DialogFragment dialog, String selectedTime,
				String inputField);
	}

	// Use this instance of the interface to deliver action events
	NoticeDialogListener mListener;

	public void onTimeSet(TimePicker arg0, int hourOfDay, int minute) {
		Calendar mCalendar = Calendar.getInstance();

		mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		mCalendar.set(Calendar.MINUTE, minute);
		SimpleDateFormat mSDF = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
		String selectedTime = mSDF.format(mCalendar.getTime());

		NoticeDialogListener activity = (NoticeDialogListener) getActivity();
		activity.onTimeSelected(TimePickerFragment.this, selectedTime,
				inputField);
	}

}
