package com.smaat.jolt.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.Transformer;
import com.google.gson.Gson;
import com.smaat.jolt.R;
import com.smaat.jolt.util.DialogManager;
import com.smaat.jolt.util.DialogMangerCallback;

public class BaseActivity extends FragmentActivity implements DialogMangerCallback {

	private static Activity _activity;
	private AQuery aq;
	public static Dialog mDialog;
	private DisplayMetrics dm;
	public static String mUserID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_activity = this;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

	}

	public static byte[] convertFileToByteArray(File f) {
		byte[] byteArray = null;
		try {
			@SuppressWarnings("resource")
			InputStream inputStream = new FileInputStream(f);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024 * 8];
			int bytesRead = 0;

			while ((bytesRead = inputStream.read(b)) != -1) {
				bos.write(b, 0, bytesRead);
			}

			byteArray = bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return byteArray;
	}


	public void updateDateTimeValue(String string, Calendar calendar_fragment,
			int id) {

	}

	public AQuery aq() {
		if (aq == null) {
			aq = new AQuery(_activity);
		}
		return aq;
	}

	public void setupUI(View view) {

		// Set up touch listener for non-text box views to hide keyboard.
		if (!(view instanceof EditText)) {

			view.setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					hideSoftKeyboard(_activity);
					return false;
				}

			});
		}

		// If a layout container, iterate over children and seed recursion.
		if (view instanceof ViewGroup) {

			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

				View innerView = ((ViewGroup) view).getChildAt(i);

				setupUI(innerView);
			}
		}
	}

	public void launchActivity(Class<?> clazz) {
		Intent intent = new Intent(_activity, clazz);
		// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
		// | Intent.FLAG_ACTIVITY_CLEAR_TASK
		// | Intent.FLAG_ACTIVITY_NO_HISTORY);
		_activity.startActivity(intent);
		_activity.finish();
		_activity.overridePendingTransition(R.anim.slide_in_right,
				R.anim.slide_out_left);
	}

	public void statusErrorCode(AjaxStatus status) {
		if (status.getCode() == 500) {
			DialogManager.showApiAlertDialog(_activity, BaseActivity.this,
					getString(R.string.server_error));
		} else if (status.getCode() == 404) {
			DialogManager.showApiAlertDialog(_activity, BaseActivity.this,
					getString(R.string.resource_not_found));
		} else {
			DialogManager.showApiAlertDialog(_activity, BaseActivity.this,
					getString(R.string.server_error));
		}
	}

	public static class GsonTransformer implements Transformer {

		public <T> T transform(String url, Class<T> type, String encoding,
				byte[] data, AjaxStatus status) {
			Gson g = new Gson();
			return g.fromJson(new String(data), type);
		}
	}

	public static void hideSoftKeyboard(Activity activity) {
		try {
			if (activity != null && !activity.isFinishing()) {
				InputMethodManager inputMethodManager = (InputMethodManager) activity
						.getSystemService(Activity.INPUT_METHOD_SERVICE);

				if (activity.getCurrentFocus() != null
						&& activity.getCurrentFocus().getWindowToken() != null) {
					inputMethodManager.hideSoftInputFromWindow(activity
							.getCurrentFocus().getWindowToken(), 0);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Sets the font on all TextViews in the ViewGroup. Searches recursively for
	 * all inner ViewGroups as well. Just add a check for any other views you
	 * want to set as well (EditText, etc.)
	 */
	public static void setFont(ViewGroup group, Typeface font) {
		int count = group.getChildCount();
		View v;
		for (int i = 0; i < count; i++) {
			v = group.getChildAt(i);
			if (v instanceof TextView || v instanceof Button
					|| v instanceof EditText/* etc. */)
				((TextView) v).setTypeface(font);
			else if (v instanceof ViewGroup)
				setFont((ViewGroup) v, font);
		}
	}

	public void onRequestSuccess(Object responseObj) {

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (aq != null) {
			aq.clear();
		}
		if (mDialog != null && mDialog.isShowing()) {

			mDialog.dismiss();
		}
	}

	public void backPreviousScreen(Class<?> clazz) {
		Intent login = new Intent(_activity, clazz);
		_activity.startActivity(login);
		_activity.overridePendingTransition(R.anim.slide_in_right,
				R.anim.slide_out_right);

		_activity.finish();
	}

	public void moveToNextScreen(Class<?> clazz) {
		Intent login = new Intent(_activity, clazz);
		_activity.startActivity(login);
		_activity.overridePendingTransition(R.anim.slide_in_left,
				R.anim.slide_out_left);

		_activity.finish();
	}

	@Override
	public void onOkclick() {
		/**
		 * Close Dialog
		 */

	}

}
