package com.smaat.jolt.fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smaat.jolt.R;
import com.smaat.jolt.ui.BaseFragment;
import com.smaat.jolt.ui.HomeScreen;
import com.smaat.jolt.ui.SignInScreen;
import com.smaat.jolt.ui.TutorialScreen;
import com.smaat.jolt.util.AppConstants;
import com.smaat.jolt.util.DialogManager;
import com.smaat.jolt.util.GlobalMethods;

public class SettingsFragment extends BaseFragment implements OnClickListener {

	public static ImageView mToggle;
	private TextView howWorks, txtLogout, txtSignIn, mShowDistance, mEmailUs,
			mFaq, mRateus, mTerms, mVersion, mVersionNum;
	private RelativeLayout emailus, faq_layout, termsCondition;

	private String distanceUnit;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = inflater.inflate(R.layout.settings, container, false);
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

		initComponents(view);

	}

	private void initComponents(View view) {

		mShowDistance = (TextView) view.findViewById(R.id.showdistance);
		howWorks = (TextView) view.findViewById(R.id.working);
		mEmailUs = (TextView) view.findViewById(R.id.email_us);
		mFaq = (TextView) view.findViewById(R.id.faq);
		mRateus = (TextView) view.findViewById(R.id.rateus);
		mTerms = (TextView) view.findViewById(R.id.terms);
		mVersion = (TextView) view.findViewById(R.id.version);
		mVersionNum = (TextView) view.findViewById(R.id.version_num);
		emailus = (RelativeLayout) view.findViewById(R.id.emailus_layout);
		mToggle = (ImageView) view.findViewById(R.id.toggle_btn);
		mToggle.setImageResource(R.drawable.miles_visible);
		txtLogout = (TextView) view.findViewById(R.id.txtlogout);
		txtSignIn = (TextView) view.findViewById(R.id.txtSignInSettings);
		faq_layout = (RelativeLayout) view.findViewById(R.id.faq_layout);
		termsCondition = (RelativeLayout) view.findViewById(R.id.terms_layout);

		mShowDistance.setTypeface(HomeScreen.helveticaNeueMedium);
		howWorks.setTypeface(HomeScreen.helveticaNeueMedium);
		mEmailUs.setTypeface(HomeScreen.helveticaNeueMedium);
		mFaq.setTypeface(HomeScreen.helveticaNeueMedium);
		mRateus.setTypeface(HomeScreen.helveticaNeueMedium);
		mTerms.setTypeface(HomeScreen.helveticaNeueMedium);
		mVersion.setTypeface(HomeScreen.helveticaNeueMedium);
		txtLogout.setTypeface(HomeScreen.helveticaNeueMedium);
		txtSignIn.setTypeface(HomeScreen.helveticaNeueMedium);
		mVersionNum.setTypeface(HomeScreen.helveticaNeueRegular);
		try {
			mVersionNum
					.setText(getActivity().getPackageManager().getPackageInfo(
							getActivity().getPackageName(), 0).versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mToggle.setOnClickListener(this);
		howWorks.setOnClickListener(this);
		emailus.setOnClickListener(this);
		txtLogout.setOnClickListener(this);
		txtSignIn.setOnClickListener(this);
		faq_layout.setOnClickListener(this);
		termsCondition.setOnClickListener(this);

		mRateus.setOnClickListener(this);
		setData();
	}

	private void setData() {
		distanceUnit = (String) GlobalMethods.getValueFromPreference(
				getActivity(), GlobalMethods.STRING_PREFERENCE,
				AppConstants.DISTANCE_UNIT);

		if (distanceUnit.isEmpty()
				|| distanceUnit.equalsIgnoreCase(getString(R.string.miles))) {
			mToggle.setImageResource(R.drawable.miles_visible);
		} else {
			mToggle.setImageResource(R.drawable.km_visible);
		}

		if (GlobalMethods.getUserID(getActivity()).equalsIgnoreCase(
				AppConstants.DEFAULT_USERID)) {
			txtLogout.setVisibility(View.GONE);
			txtSignIn.setVisibility(View.VISIBLE);
		} else {
			txtSignIn.setVisibility(View.GONE);
			txtLogout.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.working:

			GlobalMethods.storeValuetoPreference(getActivity(),
					GlobalMethods.STRING_PREFERENCE,
					AppConstants.TUTORIAL_SCREEN,
					AppConstants.IS_SETTINGS_SCREEN);
			Intent in = new Intent(getActivity(), TutorialScreen.class);
			startActivity(in);
			break;
		case R.id.emailus_layout:
			String mTxt = "";
			GlobalMethods.SendMail(getActivity(),
					getString(R.string.admin_mail_id),
					getString(R.string.hi_team), mTxt);
			break;
		case R.id.txtlogout:
			DialogManager.showLogoutDialog(getActivity(),
					getString(R.string.logout_alert));
			break;
		case R.id.txtSignInSettings:

			Intent mIntent = new Intent(getActivity(), SignInScreen.class);
			getActivity().startActivity(mIntent);
			((Activity) getActivity()).finish();
			((Activity) getActivity()).overridePendingTransition(
					R.anim.slide_out_right, R.anim.slide_in_left);
			break;
		case R.id.faq_layout:

			HomeScreen.mHeaderLeft.setTag(HomeScreen.SECONDARY_SCREEN);
			HomeScreen.moveBackFragment = new SettingsFragment();
			HomeScreen.mHeaderText.setText(R.string.faq);
			((HomeScreen) getActivity()).updateDisplayFragment(
					new FAQFragment(), true);

			break;

		case R.id.terms_layout:
			HomeScreen.mHeaderLeft.setTag(HomeScreen.SECONDARY_SCREEN);
			HomeScreen.moveBackFragment = new SettingsFragment();
			HomeScreen.mHeaderText.setText(R.string.settins_terms_conditions);
			((HomeScreen) getActivity()).updateDisplayFragment(
					new TermsAndConditions(), true);

			break;

		case R.id.toggle_btn:

			distanceUnit = (String) GlobalMethods.getValueFromPreference(
					getActivity(), GlobalMethods.STRING_PREFERENCE,
					AppConstants.DISTANCE_UNIT);

			if (distanceUnit.isEmpty()
					|| distanceUnit.equalsIgnoreCase(getString(R.string.miles))) {
				mToggle.setImageResource(R.drawable.km_visible);

				GlobalMethods.storeValuetoPreference(getActivity(),
						GlobalMethods.STRING_PREFERENCE,
						AppConstants.DISTANCE_UNIT, getString(R.string.km));

				AppConstants.appDistanceUnit = getString(R.string.km);
			} else {
				mToggle.setImageResource(R.drawable.miles_visible);
				GlobalMethods.storeValuetoPreference(getActivity(),
						GlobalMethods.STRING_PREFERENCE,
						AppConstants.DISTANCE_UNIT, getString(R.string.miles));
				AppConstants.appDistanceUnit = getString(R.string.miles);
			}

			break;

		case R.id.rateus:
			Uri uri = Uri.parse("market://details?id="
					+ getActivity().getPackageName());
			Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
			// To count with Play market backstack, After pressing back button,
			// to taken back to our application, we need to add following flags
			// to intent.
			goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
					| Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
					| Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
			try {
				startActivity(goToMarket);
			} catch (ActivityNotFoundException e) {
				startActivity(new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("http://play.google.com/store/apps/details?id="
								+ getActivity().getPackageName())));
			}
			break;

		}

	}

}
