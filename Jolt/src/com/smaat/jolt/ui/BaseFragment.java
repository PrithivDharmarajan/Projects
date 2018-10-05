package com.smaat.jolt.ui;

import java.net.SocketTimeoutException;

import retrofit.RetrofitError;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.smaat.jolt.R;
import com.smaat.jolt.util.DialogManager;

public class BaseFragment extends Fragment {
	public static Dialog mDialog;

	public void setupUI(View view) {

		if (!(view instanceof EditText)) {

			view.setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					hideSoftKeyboard(getActivity());
					return false;
				}

			});
		}
		if (view instanceof ViewGroup) {

			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

				View innerView = ((ViewGroup) view).getChildAt(i);

				setupUI(innerView);
			}
		}
	}


	public void hideSoftKeyboard(Context fragment) {
		try {
			if (fragment != null) {
				getActivity()
						.getWindow()
						.setSoftInputMode(
								WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				InputMethodManager imm = (InputMethodManager) fragment
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void onBackPressed() {

	}

	public void onRequestSuccess(Object responseObj) {

	}

	public void onRequestFailure(RetrofitError errorCode) {

		if (errorCode.getCause() instanceof SocketTimeoutException) {
			DialogManager.showToast(getActivity(),
					getString(R.string.connection_timeout));
		} else {
			DialogManager.showToast(getActivity(),
					getString(R.string.no_internet));
		}

	}

}
