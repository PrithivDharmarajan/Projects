package com.smaat.jolt.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.smaat.jolt.R;
import com.smaat.jolt.ui.BaseFragment;
import com.smaat.jolt.ui.HomeScreen;
import com.smaat.jolt.util.AppConstants;
import com.smaat.jolt.util.DialogManager;
import com.smaat.jolt.util.GlobalMethods;

public class GetFreeCoffeeFragment extends BaseFragment implements
		OnClickListener {

	private Button mFacebook, mTwitter, mMail, mWhatsapp, mMessage,
			mWillShareLater;
	private TextView couponmessage, couponcode, shareyourcode;
	private boolean mShare, isFirstTimeDisplay = false,
			isFirstTimeDisplayProgress = false;
	private Session.StatusCallback statusCallback = new SessionStatusCallback();
	private LinearLayout mShareLaterLay;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = inflater.inflate(R.layout.get_free_coffee_fragment,
				container, false);
		setupUI(rootview);
		return rootview;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ViewGroup mViewGroup = (ViewGroup) view
				.findViewById(R.id.parent_layout);
		setupUI(mViewGroup);
		GlobalMethods.setFont(mViewGroup, HomeScreen.helveticaNeueMedium);
		hideSoftKeyboard(getActivity());
		initComponents(view);

	}

	private void initComponents(View view) {

		couponmessage = (TextView) view.findViewById(R.id.couponmessage);
		couponcode = (TextView) view.findViewById(R.id.couponcode);
		shareyourcode = (TextView) view.findViewById(R.id.sharetxt);
		mWillShareLater = (Button) view.findViewById(R.id.will_share_later);
		couponmessage.setTypeface(HomeScreen.helveticaNeueMedium);
		couponcode.setTypeface(HomeScreen.helveticaNeueBold);
		shareyourcode.setTypeface(HomeScreen.helveticaNeueBold);
		mWillShareLater.setTypeface(HomeScreen.helveticaNeueMedium);

		mFacebook = (Button) view.findViewById(R.id.facebook);
		mTwitter = (Button) view.findViewById(R.id.twitter);
		mMail = (Button) view.findViewById(R.id.mail);
		mWhatsapp = (Button) view.findViewById(R.id.whatsapp);
		mMessage = (Button) view.findViewById(R.id.message);
		mShareLaterLay = (LinearLayout) view
				.findViewById(R.id.will_share_later_lay);
		if (((String) GlobalMethods.getValueFromPreference(getActivity(),
				GlobalMethods.STRING_PREFERENCE,
				AppConstants.COFFEE_WAIT_SCREEN))
				.equalsIgnoreCase("IS_COFFEE_WAIT_SCREEN")) {
			mShareLaterLay.setVisibility(View.VISIBLE);
		} else {
			mShareLaterLay.setVisibility(View.GONE);
		}

		setClickListener();

		couponcode.setText((String) GlobalMethods.getValueFromPreference(
				getActivity(), GlobalMethods.STRING_PREFERENCE,
				AppConstants.UNIQUE_CODE));

	}

	private void setClickListener() {
		mFacebook.setOnClickListener(this);
		mTwitter.setOnClickListener(this);
		mMail.setOnClickListener(this);
		mWhatsapp.setOnClickListener(this);
		mMessage.setOnClickListener(this);
		mWillShareLater.setOnClickListener(this);
	}

	private void setShare(View view, boolean mShareBoolean, String mShareStr) {
		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "\t\t\t"
				+ getString(R.string.shareMessage) + "\t"
				+ couponcode.getText().toString().trim() + "\n\n"
				+ getActivity().getString(R.string.app_link_txt) + "\t"
				+ getActivity().getString(R.string.App_link));
		PackageManager pm = view.getContext().getPackageManager();
		List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent,
				0);
		mShare = mShareBoolean;
		for (final ResolveInfo app : activityList) {

			if ((app.activityInfo.name).contains(mShareStr)) {
				mShare = true;
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

		if (!mShare) {

			DialogManager.showToast(getActivity(),
					getString(R.string.install_app));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.facebook:
			// setShare(v, false, "facebook"); // com.facebook.katana -(nly Fb)
			isFirstTimeDisplay = true;
			isFirstTimeDisplayProgress = true;
			callFbShare();
			break;
		case R.id.twitter:
			setShare(v, false, getString(R.string.twitter));
			break;
		case R.id.mail:
			String mTxt = "\t\t\t" + getString(R.string.email_shareMessage)
					+ couponcode.getText().toString().trim();
			GlobalMethods.SendMail(getActivity(), "",
					getString(R.string.download_this_app_txt), mTxt);
			break;
		case R.id.whatsapp:
			setShare(v, false, getString(R.string.whatsapp));
			break;
		case R.id.message:
			setShare(v, false, getString(R.string.mms));
			break;
		case R.id.will_share_later:
			Intent i = new Intent(getActivity(), HomeScreen.class);
			startActivity(i);
			((HomeScreen) getActivity()).overridePendingTransition(0, 0);

			// HomeScreen.mHeaderLeft.setTag(HomeScreen.SECONDARY_SCREEN);
			// ((HomeScreen) getActivity()).mFragment = new
			// MapScreenFragment();
			// ((HomeScreen) getActivity()).replaceFragment(
			// ((HomeScreen) getActivity()).mFragment, true);
			break;
		default:
			break;
		}

	}

	public void callFbShare() {
		faceBookLogin();
	}

	private void faceBookLogin() {

		Session session = Session.getActiveSession();

		if (session == null) {
			if (session == null) {
				session = new Session(getActivity());
			}
			Session.setActiveSession(session);
			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
				session.openForRead(new Session.OpenRequest(getActivity())
						.setCallback(statusCallback));
			}
		}
		if (!session.isOpened() && !session.isClosed()) {
			OpenRequest op = new Session.OpenRequest(getActivity());
			op.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);
			op.setCallback(statusCallback);

			List<String> permissions = new ArrayList<String>();
			permissions.add(getString(R.string.email));
			permissions.add(getString(R.string.user_birthday));
			permissions.add(getString(R.string.user_location));

			op.setPermissions(permissions);

			Session.setActiveSession(session);
			session.openForRead(op);
		} else {
			Session.openActiveSession(getActivity(), true, statusCallback);
		}
	}

	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			getUserEmailFromFacebook();
		}
	}

	private void getUserEmailFromFacebook() {
		Session session = Session.getActiveSession();
		if (session.isOpened()) {
			if (isFirstTimeDisplayProgress) {
				DialogManager.showProgress(getActivity());
				isFirstTimeDisplayProgress = false;
			}
			Request.newMeRequest(session, new Request.GraphUserCallback() {

				@Override
				public void onCompleted(final GraphUser user,
						final Response response) {

					if (user != null) {

						if (isFirstTimeDisplay) {
							publishFeedDialog();
							DialogManager.hideProgress(getActivity());
						}

					} else {
						DialogManager.hideProgress(getActivity());
					}
				}
			}).executeAsync();
		}
	}

	private void publishFeedDialog() {
		isFirstTimeDisplay = false;
		Bundle parameters = new Bundle();
		parameters.putString(getString(R.string.description),
				getString(R.string.shareMessage)
						+ couponcode.getText().toString());
		parameters.putString(getString(R.string.link),
				getString(R.string.play_store_link));
		parameters.putString(getString(R.string.name),
				getActivity().getString(R.string.app_name));

		final WebDialog feedDialog = new WebDialog.FeedDialogBuilder(
				getActivity(), Session.getActiveSession(), parameters).build();

		feedDialog.setOnCompleteListener(new OnCompleteListener() {

			@Override
			public void onComplete(Bundle values, FacebookException error) {
				if (feedDialog != null) {
					feedDialog.cancel();
				}
				DialogManager.hideProgress(getActivity());
				if (error == null) {
					final String postId = values
							.getString(getString(R.string.post_id));
					if (postId != null) {
					} else {
						DialogManager.showToast(getActivity(),
								getString(R.string.publish_cancelled));
					}
				} else if (error instanceof FacebookOperationCanceledException) {
					DialogManager.showToast(getActivity(),
							getString(R.string.publish_cancelled));
				} else {
					DialogManager.showToast(getActivity(),
							getString(R.string.publish_successfully));
				}
				Session.getActiveSession().removeCallback(statusCallback);
				Session session = Session.getActiveSession();
				if (session != null) {
					session.close();
				}

			}
		});

		feedDialog.show();

	}
}
