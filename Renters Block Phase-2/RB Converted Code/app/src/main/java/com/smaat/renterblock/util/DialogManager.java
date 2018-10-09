package com.smaat.renterblock.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.smaat.renterblock.MapFragmentActivity;
import com.smaat.renterblock.R;
import com.smaat.renterblock.ui.SplashScreen;

public class DialogManager {

	static Dialog alertOptionDialog;
	private static Dialog mDialog;
	private static TextView mDialogText;
	private static Button mDialogbutton, mOkbtn, mCancelbtn;

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

	public static Dialog showDialogs(final Context context, String message,
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
									((Activity) context)
											.overridePendingTransition(
													animFrom, animTo);
									((Activity) context).finish();
								}
							}
						});

		alertOptionDialog = builder.show();

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

		mDialogText = (TextView) mDialog.findViewById(R.id.alert_text);
		mDialogText.setText(message);
		mDialogText.setMovementMethod(new ScrollingMovementMethod());
		mDialogbutton = (Button) mDialog.findViewById(R.id.ok);

		mDialogbutton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				mDialog.dismiss();
				dialoginterface.onOkclick();

			}
		});

		mDialog.show();
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

		alertOptionDialog.show();
		return alertOptionDialog;
	}

	public static Dialog showPopUpRequestFraud(final Context context,final OptionDialogInterfaceListener dialogMangerCallback,
											   final String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(true);
		builder.setTitle(R.string.app_name)
				.setMessage(message)
				.setPositiveButton(
						context.getResources().getString(R.string.yes_text),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								alertOptionDialog.cancel();
								dialogMangerCallback.okClick();
							}
						})
				.setNegativeButton(context.getString(R.string.no_text), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						alertOptionDialog.cancel();
//						alertOptionDialog.dismiss();
					}
				});
        alertOptionDialog = builder.show();

		alertOptionDialog.show();
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

	public static void showToast(Context context, String message) {

		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	public static Dialog showDialogAlert(final Activity mContext,
			String alertMessage, String btnText, String btnText1,
			final Class<?> move, final int animFrom, final int animTo,
			final Class<?> target_Class, final Class<?> reset_class,
			final DialogMangerCallback dialoginterface) {

		mDialog = new Dialog(mContext);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.alert_screen_multiple_btns);
		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
		wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		wmlp.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

		mDialogText = (TextView) mDialog.findViewById(R.id.alert_text);
		mDialogText.setText(alertMessage);
		mDialogText.setMovementMethod(new ScrollingMovementMethod());
		mOkbtn = (Button) mDialog.findViewById(R.id.ok);
		mCancelbtn = (Button) mDialog.findViewById(R.id.cancel);

		mOkbtn.setText(btnText1);
		mCancelbtn.setText(btnText);

		mOkbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (move == null) {
					alertOptionDialog.cancel();
				} else {
					if (target_Class != null) {
						SplashScreen.mTargetClass = target_Class;
					}
					Intent cacheIntent = new Intent(mContext, move);
					mContext.startActivity(cacheIntent);
					mContext.overridePendingTransition(animFrom, animTo);
				}
			}
		});

		mCancelbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SplashScreen.mTargetClass = reset_class;
				if (AppConstants.From_Map_Fragment) {
					MapFragmentActivity.callEnhancedtoStandarAPI();
					AppConstants.From_Map_Fragment = false;
				}
				mDialog.dismiss();
			}
		});

		mDialog.show();
		return mDialog;
	}

	public static ProgressDialog getProgressDialog(Context activity) {

		ProgressDialog mProgressDialog = ProgressDialog.show(activity, null,
				null);
		mProgressDialog.setContentView(R.layout.custom_dialog);
		mProgressDialog.show();
		return mProgressDialog;
	}


}
