package com.smaat.ipharma.fragment;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smaat.ipharma.R;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.ui.TutorialScreen;
import com.smaat.ipharma.util.AppConstants;
import com.smaat.ipharma.util.GlobalMethods;

import java.util.List;

public class MoreFragment extends BaseFragment implements OnClickListener {
	private RelativeLayout mNotification, mShare, mRate, mAbout, mContact,
			mHelp;
	private Button mToggle;
	boolean isClicked, mShareOption;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = inflater.inflate(R.layout.fragment_more, container,
				false);
		Window window = getActivity().getWindow();
		window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setupUI(rootview);
		return rootview;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ViewGroup mViewGroup = (ViewGroup) view
				.findViewById(R.id.parent_layout);
		setupUI(mViewGroup);
		hideSoftKeyboard(getActivity());

		AppConstants.FRAG = AppConstants.MAP_SCREEN;
		initComponents(view);
		HomeScreen.mHeaderLeft.setBackgroundResource(R.drawable.back_butto);

	}

	private void initComponents(View view) {
		mNotification = (RelativeLayout) view
				.findViewById(R.id.notification_layout);
		mNotification.setOnClickListener(this);
		mHelp = (RelativeLayout) view.findViewById(R.id.help_layout);
		mHelp.setOnClickListener(this);
		mShare = (RelativeLayout) view.findViewById(R.id.share_layout);
		mShare.setOnClickListener(this);
		mAbout = (RelativeLayout) view.findViewById(R.id.about_layout);
		mAbout.setOnClickListener(this);
		mContact = (RelativeLayout) view.findViewById(R.id.contact_layout);
		mContact.setOnClickListener(this);
		mRate = (RelativeLayout) view.findViewById(R.id.rate_layout);
		mRate.setOnClickListener(this);
		mToggle = (Button) view.findViewById(R.id.toggle_btn);
		mToggle.setOnClickListener(this);

		HomeScreen.mHeaderLeftLay.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// HomeScreen.mSlideHolder.toggle();

				HomeScreen.onBackMove(getActivity());
			}
		});

	}

	public void showShareDialog() {

		mDialog = new Dialog(getActivity());
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.dialog_share);

		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
		wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

		mDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
						| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		// hideSoftKeyboard(getActivity());
		ImageView mFB, mTwt, mEmail, mSms;
		mFB = (ImageView) mDialog.findViewById(R.id.facebook);
		mTwt = (ImageView) mDialog.findViewById(R.id.twitter);
		mEmail = (ImageView) mDialog.findViewById(R.id.email);
		mSms = (ImageView) mDialog.findViewById(R.id.sms);

		mFB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setShare(v, false, getString(R.string.facebook));
			}
		});
		mTwt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setShare(v, false, getString(R.string.twitter));
			}
		});
		mEmail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// setShare(v, false, getString(R.string.gm));
				Intent email = new Intent(Intent.ACTION_SENDTO);
				email.setData(Uri.parse("mailto:")); // mailto:
				email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.Sub));
				email.putExtra(Intent.EXTRA_TEXT, getString(R.string.MsgTxt));
				getActivity().startActivity(email);

			}
		});
		mSms.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setShare(v, false, getString(R.string.mms));
			}
		});

		mDialog.show();
	}

	private void setShare(View view, boolean mShareBoolean, String mShareStr) {
		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
				getString(R.string.share_text));
		PackageManager pm = view.getContext().getPackageManager();
		List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent,
				0);
		mShareOption = mShareBoolean;
		for (final ResolveInfo app : activityList) {

			if ((app.activityInfo.name).contains(mShareStr)) {
				mShareOption = true;
				final ActivityInfo activity = app.activityInfo;

				final ComponentName name = new ComponentName(
						activity.applicationInfo.packageName, activity.name);
				shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
				shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				shareIntent.setComponent(name);
				view.getContext().startActivity(shareIntent);
				break;
			}
		}

		if (!mShareOption) {

			showToast(getActivity(), getString(R.string.install_app));
		}
	}

	public void showRateDialog() {

		mDialog = new Dialog(getActivity());
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.dialog_rate);

		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
		wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

		mDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
						| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		Button rate = (Button) mDialog.findViewById(R.id.rate_it);
		Button cancel = (Button) mDialog.findViewById(R.id.cancel);
		cancel.setTypeface(HomeScreen.mHelveticaBold);
		rate.setTypeface(HomeScreen.mHelveticaBold);
		TextView mRateText = (TextView) mDialog.findViewById(R.id.rate_text);
		final TextView mRating_avg_txt = (TextView) mDialog
				.findViewById(R.id.rating_avg_txt);
		mRating_avg_txt.setTypeface(HomeScreen.mHelveticaBold);
		mRateText.setTypeface(HomeScreen.mHelveticaNormal);
		final RatingBar mFav_ratingbar = (RatingBar) mDialog
				.findViewById(R.id.fav_ratingbar);
		final Button mRating_icons = (Button) mDialog
				.findViewById(R.id.rating_icons);

		mFav_ratingbar
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						switch ((int) mFav_ratingbar.getRating()) {
						case 1:
							mRating_avg_txt.setText(getString(R.string.poor));
							mRating_icons
									.setBackgroundResource(R.drawable.poor);
							break;
						case 2:
							mRating_avg_txt.setText(getString(R.string.bad));
							mRating_icons.setBackgroundResource(R.drawable.bad);
							break;
						case 3:
							mRating_avg_txt
									.setText(getString(R.string.average));
							mRating_icons
									.setBackgroundResource(R.drawable.average);
							break;
						case 4:
							mRating_avg_txt.setText(getString(R.string.good));
							mRating_icons
									.setBackgroundResource(R.drawable.good);
							break;
						case 5:
							mRating_avg_txt
									.setText(getString(R.string.excellent));
							mRating_icons
									.setBackgroundResource(R.drawable.excellent);
							break;
						}
					}
				});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDialog.dismiss();
			}
		});

		rate.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// float rating_value = mFav_ratingbar.getRating();
				// callPharmacyRatingService(rating_value);
				mDialog.dismiss();
			}
		});

		mDialog.show();
	}

	// @Override
	// public void onDestroy() {
	// // TODO Auto-generated method stub
	// super.onDestroy();
	// getActivity().getSupportFragmentManager().beginTransaction()
	// .remove(this).commit();
	// }

	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.notification_layout:
			// if (isClicked) {
			// mToggle.setBackgroundResource(R.drawable.toggle_on);
			// isClicked = false;
			// } else {
			// mToggle.setBackgroundResource(R.drawable.toggle_off);
			// isClicked = true;
			// }

			break;
		case R.id.help_layout:
			GlobalMethods.storeValuetoPreference(getActivity(),
					GlobalMethods.STRING_PREFERENCE,
					AppConstants.TUTORIAL_SCREEN, AppConstants.TUTORIAL);
			Intent in = new Intent(getActivity(), TutorialScreen.class);
			startActivity(in);
			break;
		case R.id.about_layout:
			GlobalMethods.storeValuetoPreference(getActivity(),
					GlobalMethods.STRING_PREFERENCE, AppConstants.WEB_SCREEN,
					AppConstants.MORE_SCREEN);
			HomeScreen.mHeaderRightLay.setVisibility(View.INVISIBLE);
			HomeScreen.mHeaderText.setText(R.string.about_us);
			HomeScreen.mHeaderLeft.setBackgroundResource(R.drawable.back_butto);
			HomeScreen.mBottombar.setVisibility(View.GONE);
			((HomeScreen) getActivity()).replaceFragment(new AboutFragment(),
					true);
			break;
		case R.id.share_layout:
			showShareDialog();

			break;
		case R.id.rate_layout:
			showRateDialog();

			break;
		case R.id.contact_layout:
			// Displays contact us Screen

			break;
		case R.id.toggle_btn:
			if (isClicked) {
				mToggle.setBackgroundResource(R.drawable.toggle_on);
				isClicked = false;
			} else {
				mToggle.setBackgroundResource(R.drawable.toggle_off);
				isClicked = true;
			}
			break;
		default:
			break;
		}

	}
}
