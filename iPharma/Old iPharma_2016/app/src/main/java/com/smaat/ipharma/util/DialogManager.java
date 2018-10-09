package com.smaat.ipharma.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smaat.ipharma.R;
import com.smaat.ipharma.ui.LoginActivity;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class DialogManager {

	static Dialog alertOptionDialog;
	private static Dialog mDialog;
	public static Dialog mAnimDialog;
	private static TextView mDialogText, mDialogTitle;
	private static Button mDialogbutton;
	public static Dialog progress;

	public static Dialog showDialog(final Activity context, String message,
			String buttonText, final Class<?> move, final int animFrom,
			final int animTo) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(false);

		builder.setTitle(R.string.app_name)
				.setMessage(message)
				.setPositiveButton(buttonText,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (move == null) {
									alertOptionDialog.cancel();
								} else {
									Intent cacheIntent = new Intent(context,
											move);

									context.startActivity(cacheIntent);
									// Setting screen transition
									context.overridePendingTransition(animFrom,
											animTo);
									context.finish();
								}
							}
						});

		alertOptionDialog = builder.show();

		alertOptionDialog.show();
		return alertOptionDialog;
	}

	public static Dialog showPopUpDialog(final Context context,
			final DialogMangerCallback dialoginterface, final String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(false);

		builder.setTitle(R.string.app_name)
				.setMessage(message)
				.setPositiveButton(
						context.getResources().getString(R.string.ok),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								alertOptionDialog.cancel();
								dialoginterface.onOkclick();
							}
						});

		alertOptionDialog = builder.show();
		if (alertOptionDialog != null && alertOptionDialog.isShowing()) {
			alertOptionDialog.dismiss();
			alertOptionDialog.show();
		} else {
			alertOptionDialog.show();
		}
		return alertOptionDialog;
	}

	public static Dialog callSessionId(final Activity context, String message,
			String buttonText, final Class<?> move, final int animFrom,
			final int animTo) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(false);

		builder.setTitle(R.string.app_name)
				.setMessage(message)
				.setPositiveButton(buttonText,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (move == null) {
									// LoginActivity.getSessionId();
									alertOptionDialog.cancel();
								} else {
									// LoginActivity.getSessionId();
									Intent cacheIntent = new Intent(context,
											move);
									context.startActivity(cacheIntent);
									// Setting screen transition
									context.overridePendingTransition(animFrom,
											animTo);
									context.finish();
								}

							}
						});

		alertOptionDialog = builder.show();

		alertOptionDialog.show();
		return alertOptionDialog;
	}

	public static Dialog showMessageDialog(final Context mContext,
			String message, String btnTxt) {

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

		builder.setTitle(R.string.app_name)
				.setMessage(message)
				.setPositiveButton(btnTxt,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								alertOptionDialog.cancel();

							}
						});

		alertOptionDialog = builder.show();
		alertOptionDialog.show();

		return alertOptionDialog;
	}

	public static Dialog showSuccessDialog(final Context mContext,
			final DialogMangerSucessCallback mDialoginterface, String mMsg) {

		alertOptionDialog = new Dialog(mContext);
		alertOptionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alertOptionDialog.setContentView(R.layout.success_popup);

		alertOptionDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		WindowManager.LayoutParams wmlp = alertOptionDialog.getWindow()
				.getAttributes();
		wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

		alertOptionDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
						| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		TextView mMsgTxt = (TextView) alertOptionDialog
				.findViewById(R.id.msg_txt);
		Button ok = (Button) alertOptionDialog.findViewById(R.id.ok);
		mMsgTxt.setText(mMsg);

		ok.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				alertOptionDialog.dismiss();
				mDialoginterface.onOkclick();
			}
		});

		alertOptionDialog.show();
		return alertOptionDialog;
	}

	public static void showCustomAlertDialog(final Context context,
			final DialogMangerCallback dialoginterface, final String message) {

		mDialog = new Dialog(context);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.alert_popup);
		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
		wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		wmlp.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

		mDialogTitle = (TextView) mDialog.findViewById(R.id.alert_title);
		mDialogText = (TextView) mDialog.findViewById(R.id.message);
		ImageView mErrorIcon = (ImageView) mDialog
				.findViewById(R.id.error_icon);
		mDialogTitle.setVisibility(View.GONE);
		mErrorIcon.setVisibility(View.GONE);
		mDialogText.setText(message);
		mDialogText.setMovementMethod(new ScrollingMovementMethod());
		mDialogbutton = (Button) mDialog.findViewById(R.id.submit);

		mDialogbutton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				mDialog.dismiss();
				dialoginterface.onOkclick();
			}
		});
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
			mDialog.show();
		} else {
			mDialog.show();
		}
	}

	public static void showCustomAlertDialog(final Context context,
			final DialogMangerCallback dialoginterface, final String title,
			final String message) {

		mDialog = new Dialog(context);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.alert_popup);
		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
		wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		wmlp.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

		mDialogTitle = (TextView) mDialog.findViewById(R.id.alert_title);
		mDialogText = (TextView) mDialog.findViewById(R.id.message);
		mDialogTitle.setText(title);
		mDialogText.setText(message);
		mDialogText.setMovementMethod(new ScrollingMovementMethod());
		mDialogbutton = (Button) mDialog.findViewById(R.id.submit);

		mDialogbutton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				mDialog.dismiss();
				dialoginterface.onOkclick();
			}
		});

		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
			mDialog.show();
		} else {
			mDialog.show();
		}
	}

	public static void showToast(Context context, String message) {

		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	public static Dialog showDialogAlert(final Activity mContext,
			String alertMessage, String btnText, String btnText1,
			final Class<?> move, final int animFrom, final int animTo) {

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

		builder.setTitle(R.string.app_name)
				.setMessage(alertMessage)
				.setPositiveButton(btnText,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								// ((Activity) mContext).finish();
								if (move == null) {
									alertOptionDialog.cancel();
								} else {
									Intent cacheIntent = new Intent(mContext,
											move);

									mContext.startActivity(cacheIntent);
									// Setting screen transition
									mContext.overridePendingTransition(
											animFrom, animTo);
									// mContext.finish();
								}

							}
						})
				.setNegativeButton(btnText1,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								alertOptionDialog.cancel();
							}
						});

		alertOptionDialog = builder.show();

		alertOptionDialog.show();
		return alertOptionDialog;
	}

	public static ProgressDialog getProgressDialog(Context activity, int message) {
		ProgressDialog dialog = new ProgressDialog(activity);

		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		dialog.setInverseBackgroundForced(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setTitle(message);

		return dialog;
	}

	public static void showProgress(Context context) {

		progress = getLoadingDialog(context);

		progress.show();

	}

	public static void hideProgress(Context context) {
		if (progress != null && progress.isShowing()) {
			progress.dismiss();
		}

		if (_timer != null) {
			_timer.cancel();
		}


	}

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

	static class TickClass extends TimerTask {
		TickClass(Context context) {

		}

		@Override
		public void run() {
			if (_index == 9) {
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
						.open("animation/loading_image_" + _index + ".png"));
				mImageView.setImageBitmap(bmp);

			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}
	}

	public static void showLogoutDialog(final Context mContext, String message) {

		mDialog = new Dialog(mContext);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.log_out_popup);
		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
		wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		wmlp.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

		Button noBtn = (Button) mDialog.findViewById(R.id.no);
		Button yesBtn = (Button) mDialog.findViewById(R.id.yes);

		mDialogText = (TextView) mDialog.findViewById(R.id.message);
		mDialogText.setText(message);

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
						GlobalMethods.STRING_PREFERENCE,
						AppConstants.IPHARMA_TOTAL_MONEY,
						AppConstants.FAILURE_CODE);
				GlobalMethods.storeValuetoPreference(mContext,
						GlobalMethods.STRING_PREFERENCE, AppConstants.USER_ID,
						AppConstants.FAILURE_CODE);
				GlobalMethods.storeValuetoPreference(mContext,
						GlobalMethods.STRING_PREFERENCE,
						AppConstants.LOGOUT_SCREEN, AppConstants.LOGOUT);
				// deleteCache(mContext);
				Intent mIntent = new Intent(mContext, LoginActivity.class);
				mContext.startActivity(mIntent);
				((Activity) mContext).finish();

			}
		});

		mDialog.show();

	}

	public static void deleteCache(Context mContext) {
		try {
			File dir = mContext.getCacheDir();
			if (dir != null && dir.isDirectory()) {
				deleteDir(dir);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		// The directory is now empty so delete it
		return dir.delete();
	}


}
