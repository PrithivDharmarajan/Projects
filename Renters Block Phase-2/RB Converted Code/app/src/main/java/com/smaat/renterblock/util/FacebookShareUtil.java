package com.smaat.renterblock.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
/*import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.Builder;
import com.facebook.Session.OpenRequest;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;*/
import com.smaat.renterblock.R;

public class FacebookShareUtil {

	private static String mDialogWidth = "150", mDialogHeight = "150";

	private static final int FB_LOGIN_REQ = 1, FB_SHARE_REQ = 2;
	private static int mReqType = FB_LOGIN_REQ;

	private static String mImageUrl = "";
	private static String mShareText = "";
	private static String mAppName = "";
	private static String mCaption = "";
	private static String mLink = "";

	/*public static void callFacebookLogin(Activity act,
			final FacebookInterface facebookInterface) {

		Session currentSession = Session.getActiveSession();
		if (currentSession == null || currentSession.getState().isClosed()) {
			Session session = new Session.Builder(act).build();
			Session.setActiveSession(session);
			currentSession = session;
		}

		if (currentSession.isOpened()) {
			facebookRequest(act, currentSession, facebookInterface);

		} else if (!currentSession.isOpened()) {

			OpenRequest op = new Session.OpenRequest(act);

			op.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
			op.setCallback(null);

			List<String> permissions = new ArrayList<String>();
			permissions.add("publish_actions");
			permissions.add("email");
			permissions.add("user_mobile_phone ");
			op.setPermissions(permissions);

			Session session = new Builder(act).build();
			Session.setActiveSession(session);
			session.openForPublish(op);
		}
	}

	public static void facebookRequest(final Activity activity,
			final Session session, final FacebookInterface facebookInterface) {

		final Bundle params = new Bundle();
		params.putBoolean("redirect", false);
		params.putString("height", mDialogWidth);
		params.putString("type", "normal");
		params.putString("width", mDialogHeight);
		Request.newMeRequest(session, new Request.GraphUserCallback() {

			@Override
			public void onCompleted(GraphUser user, Response response) {
				if (user != null) {

					if (mReqType == FB_LOGIN_REQ) {
						facebookInterface.onLoginCompleted(user);
					} else {
						publishFeedDialog(activity);
					}

				}
			}

		}).executeAsync();
	}

	public static void onActityResult(final Activity act, int requestCode,
			int resultCode, Intent data,
			final FacebookInterface facebookInterface) {
		if (Session.getActiveSession() != null)
			Session.getActiveSession().onActivityResult(act, requestCode,
					resultCode, data);

		Session currentSession = Session.getActiveSession();
		if (currentSession == null || currentSession.getState().isClosed()) {
			Session session = new Session.Builder(act).build();
			Session.setActiveSession(session);
			currentSession = session;
		}

		if (currentSession.isOpened()) {
			Session.openActiveSession(act, true, new Session.StatusCallback() {

				@Override
				public void call(final Session session, SessionState state,
						Exception exception) {

					if (session.isOpened()) {
						facebookRequest(act, session, facebookInterface);
					}
				}
			});
		}

	}

	public static void publishFeedDialog(final Activity activity) {
		if (Session.getActiveSession().isOpened()) {
			Bundle params = new Bundle();
			if (mAppName.length() > 0) {
				params.putString("name", mAppName);
			}
			if (mCaption.length() > 0) {
				params.putString("caption", mCaption);
			}
			if (mShareText.length() > 0) {
				params.putString("description", mShareText);
			}
			if (mLink.length() > 0) {
				params.putString("link", mLink);
			}
			if (mImageUrl.length() > 0) {
				params.putString("picture", mImageUrl);
			}

			WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(activity,
					Session.getActiveSession(), params)).setOnCompleteListener(
					new OnCompleteListener() {

						@Override
						public void onComplete(Bundle values,
								FacebookException error) {
							if (error == null) {
								final String postId = values
										.getString("post_id");
								if (postId != null) {

									DialogManager.showMessageDialog(activity,
											"Shared via Facebook successfully",
											activity.getString(R.string.ok));
								} else {
									Toast.makeText(
											activity.getApplicationContext(),
											"Publish cancelled",
											Toast.LENGTH_SHORT).show();
								}
							} else if (error instanceof FacebookOperationCanceledException) {
								Toast.makeText(
										activity.getApplicationContext(),
										"Publish cancelled", Toast.LENGTH_SHORT)
										.show();
							} else {
								Toast.makeText(
										activity.getApplicationContext(),
										"Error posting story",
										Toast.LENGTH_SHORT).show();
							}
						}

					}).build();
			feedDialog.show();
		}
	}

	public static void facebookShare(final Activity activity, String imageUrl,
			String shareText, String appName, String caption) {

		mReqType = FB_SHARE_REQ;
		mImageUrl = imageUrl;
		mAppName = appName;
		mShareText = shareText;
		mCaption = caption;

		if (Session.getActiveSession() != null
				&& Session.getActiveSession().isOpened()) {
			publishFeedDialog(activity);

		} else {
			FacebookShareUtil.callFacebookLogin(activity,
					new FacebookInterface() {

						@Override
						public void onLoginCompleted(GraphUser user) {
							publishFeedDialog(activity);
						}
					});
		}
	}*/
}
