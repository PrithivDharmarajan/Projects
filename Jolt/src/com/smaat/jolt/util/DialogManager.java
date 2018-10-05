package com.smaat.jolt.util;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.smaat.jolt.R;
import com.smaat.jolt.ui.HomeScreen;
import com.smaat.jolt.ui.SignInScreen;

public class DialogManager {

	private static Dialog mDialog;
	private static Button mCloseBtn, mSignInBtn;
	// public static ProgressDialog progress;

	public static Dialog progress;

	public static void showCustomAlertDialog(final Context mContext,
			String message, String btnTxt) {

		mDialog = new Dialog(mContext);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		mDialog.setContentView(R.layout.alert_popup);
		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
		wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		wmlp.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

		TextView mDialogText = (TextView) mDialog.findViewById(R.id.alert_text);
		mDialogText.setText(message);
		Button mDialogbutton = (Button) mDialog.findViewById(R.id.ok);
		mDialogText.setTypeface(HomeScreen.helveticaNeueBold);
		mDialogbutton.setTypeface(HomeScreen.helveticaNeueBold);
		mDialogbutton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				mDialog.dismiss();
				// dialoginterface.onOkclick();

			}
		});
		mDialog.show();

	}

	public static void onQuitPressed() {

		int pid = android.os.Process.myPid();
		android.os.Process.killProcess(pid);
	}

	public static void showLogoutDialog(final Context mContext, String message) {

		mDialog = new Dialog(mContext);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.alert_popup);
		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
		wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		wmlp.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

		Button noBtn = (Button) mDialog.findViewById(R.id.no);
		Button yesBtn = (Button) mDialog.findViewById(R.id.yes);

		TextView mDialogText = (TextView) mDialog.findViewById(R.id.alert_text);
		mDialogText.setText(message);

		noBtn.setTypeface(HomeScreen.helveticaNeueBold);
		yesBtn.setTypeface(HomeScreen.helveticaNeueBold);
		mDialogText.setTypeface(HomeScreen.helveticaNeueRegular);

		noBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				mDialog.dismiss();

			}
		});
		yesBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				mDialog.dismiss();
				GlobalMethods.storeValuetoPreference(mContext,
						GlobalMethods.STRING_PREFERENCE, AppConstants.USER_ID,
						"0");
				GlobalMethods.storeValuetoPreference(mContext,
						GlobalMethods.BOOLEAN_PREFERENCE,
						AppConstants.ISLOGGEDIN, false);
				GlobalMethods.storeValuetoPreference(mContext,
						GlobalMethods.STRING_PREFERENCE, AppConstants.USER_ID,
						"0");

				Session session = Session.getActiveSession();
				if (session != null) {
					session.close();
					session.closeAndClearTokenInformation();
					Session.setActiveSession(null);
				}
				Intent mIntent = new Intent(mContext, SignInScreen.class);
				mContext.startActivity(mIntent);
				((Activity) mContext).finish();
				((Activity) mContext).overridePendingTransition(
						R.anim.slide_out_right, R.anim.slide_in_left);

			}
		});

		mDialog.show();

	}

	public static void showToast(Context context, String message) {
		Typeface helveticaNeueMedium = TypefaceSingleton.getTypeface()
				.getHelveticaNeueMedium(context);
		Toast mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
		TextView mToastTxt = (TextView) mToast.getView().findViewById(
				android.R.id.message);
		mToastTxt.setTypeface(helveticaNeueMedium);
		// mToastTxt.setTextSize(15);
		mToast.show();
	}

	public static void showApiAlertDialog(final Context mContext,
			final DialogMangerCallback mDialogInterface, String message) {

		mDialog = new Dialog(mContext);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mDialog.setContentView(R.layout.alert_popup);
		mDialog.setCancelable(false);
		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
		wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		wmlp.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

		TextView mDialogText = (TextView) mDialog.findViewById(R.id.alert_text);
		mDialogText.setText(message);
		Button mDialogbutton = (Button) mDialog.findViewById(R.id.ok);
		mDialogText.setTypeface(HomeScreen.helveticaNeueBold);
		mDialogbutton.setTypeface(HomeScreen.helveticaNeueBold);
		mDialogbutton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				mDialog.dismiss();
				mDialogInterface.onOkclick();

			}
		});
		mDialog.show();

	}

	public static void showCustomAlertNotIntegratedDialog(
			final Context mContext, String message, String btnTxt) {

		mDialog = new Dialog(mContext);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		mDialog.setContentView(R.layout.alert_integrated);
		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
		wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		wmlp.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		TextView mDialogText = (TextView) mDialog.findViewById(R.id.alert_text);
		mDialogText.setText(message);
		Button mDialogbutton = (Button) mDialog.findViewById(R.id.ok);
		mDialogText.setTypeface(HomeScreen.helveticaNeueLight);
		mDialogbutton.setTypeface(HomeScreen.helveticaNeueRegular);
		mDialogbutton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				mDialog.dismiss();
				// dialoginterface.onOkclick();

			}
		});
		mDialog.show();

	}

	public static void showCustomAlertSignInDialog(final Context mContext,
			String message, String btnTxt) {

		mDialog = new Dialog(mContext);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.alert_signin);
		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
		wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		wmlp.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		TextView mAlertTxt = (TextView) mDialog.findViewById(R.id.alert_text);
		mAlertTxt.setText(message);
		mAlertTxt.setTypeface(HomeScreen.helveticaNeueLight);
		mCloseBtn = (Button) mDialog.findViewById(R.id.close);
		mCloseBtn.setTypeface(HomeScreen.helveticaNeueRegular);
		mSignInBtn = (Button) mDialog.findViewById(R.id.sign_in);
		mSignInBtn.setTypeface(HomeScreen.helveticaNeueRegular);
		mSignInBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent mIntent = new Intent(mContext, SignInScreen.class);
				mContext.startActivity(mIntent);
				((Activity) mContext).finish();
			}
		});
		mCloseBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				mDialog.dismiss();
				// dialoginterface.onOkclick();

			}
		});
		mDialog.show();

	}

	// private static ProgressDialog getProgressDialog(Context context) {
	//
	// ProgressDialog mProgressDialog = ProgressDialog.show(context, null,
	// null);
	// mProgressDialog.setContentView(R.layout.progress);
	// GifMovieView loadingAnim = (GifMovieView) mProgressDialog
	// .findViewById(R.id.loadingAnim);
	//
	// loadingAnim.setMovieResource(R.drawable.loading_animation);
	// loadingAnim.setPaused(false);
	//
	// mProgressDialog.setCancelable(false);
	//
	// mProgressDialog.setCanceledOnTouchOutside(false);
	//
	// return mProgressDialog;
	// }

	private static int _index = 1;
	private static MyHandler handler;
	private static Timer _timer;

	private static Dialog getLoadingDialog(Context context) {

		Dialog mDialog = new Dialog(context);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.progress);
		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
		wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		wmlp.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

		ImageView _imagView = (ImageView) mDialog.findViewById(R.id.imageView1);

		handler = new MyHandler(context, _imagView);
		_index = 1;
		_timer = new Timer();
		TickClass tick = new TickClass(context);
		_timer.schedule(tick, 100, 100);

		mDialog.setCancelable(false);

		mDialog.setCanceledOnTouchOutside(false);

		return mDialog;
	}

	public static void showProgress(Context context) {

		progress = getLoadingDialog(context);

		progress.show();

	}

	public static void hideProgress(Context context) {

		if (_timer != null) {
			_timer.cancel();
		}
		if (progress != null && progress.isShowing()) {
			progress.dismiss();
		}

	}

	public static ProgressDialog getToast(Context context) {
		return null;

	}

	static class TickClass extends TimerTask {
		TickClass(Context context) {

		}

		@Override
		public void run() {
			if (_index == 45) {
				_index = 1;
			}
			handler.sendEmptyMessage(_index);
			_index++;
		}
	}

	static class MyHandler extends Handler {
		Context mContext;
		ImageView mImageView;

		MyHandler(Context context, ImageView imageView) {

			mContext = context;
			mImageView = imageView;
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			try {
				Bitmap bmp = BitmapFactory.decodeStream(mContext.getAssets()
						.open("animation/jolt-loading-" + _index + ".png"));
				mImageView.setImageBitmap(bmp);

				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.v("Exception in Handler ", e.getMessage());
			}
		}
	}

}
