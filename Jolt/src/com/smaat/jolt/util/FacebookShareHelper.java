package com.smaat.jolt.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;

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

public class FacebookShareHelper {
	private Activity mContext;
	public static FacebookShareHelper FBHELPER;
	
	private Session.StatusCallback statusCallback = new SessionStatusCallback();
	private boolean isFirstTimeDisplay=false;
	private String mCaptionTxt,mLinkTxt;
	public static FacebookShareHelper getFacebookShareHelper() {

		return FBHELPER;

	}

	public void callFbShare(Activity context,boolean mIsFirstTimeDisplay,String CaptionTxt,String LinkTxt) {
		mContext = context;
		isFirstTimeDisplay=mIsFirstTimeDisplay;
		mCaptionTxt=CaptionTxt;
		mLinkTxt=LinkTxt;
		faceBookLogin();
	}

	private void faceBookLogin() {

		Session session = Session.getActiveSession();

		if (session == null) {
			if (session == null) {
				session = new Session(mContext);
			}
			Session.setActiveSession(session);
			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
				session.openForRead(new Session.OpenRequest(mContext)
						.setCallback(statusCallback));
			}
		}
		if (!session.isOpened() && !session.isClosed()) {
			OpenRequest op = new Session.OpenRequest(mContext);
			op.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);
			op.setCallback(statusCallback);

			List<String> permissions = new ArrayList<String>();
			permissions.add(mContext.getString(R.string.email));
			permissions.add(mContext.getString(R.string.user_birthday));
			permissions.add(mContext.getString(R.string.user_location));

			op.setPermissions(permissions);

			Session.setActiveSession(session);
			session.openForRead(op);
		} else {
			Session.openActiveSession(mContext, true, statusCallback);
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
			DialogManager.showProgress(mContext);

			Request.newMeRequest(session, new Request.GraphUserCallback() {

				@Override
				public void onCompleted(final GraphUser user,
						final Response response) {

					if (user != null) {
						DialogManager.hideProgress(mContext);
						if (isFirstTimeDisplay) {
							publishFeedDialog();
						}

					} else {
						DialogManager.hideProgress(mContext);
					}
				}
			}).executeAsync();
		}
	}

	private void publishFeedDialog() {
		isFirstTimeDisplay = false;
		Bundle parameters = new Bundle();
//		if (shareText.length() > 0) {
//			parameters.putString("description", shareText);
//		} else {
//			
//		}
		parameters.putString(mContext.getString(R.string.description),mCaptionTxt);
		parameters.putString(mContext.getString(R.string.link), mLinkTxt);
		parameters.putString(mContext.getString(R.string.name), mContext.getString(R.string.app_name));
		parameters
				.putString(mContext.getString(R.string.caption), mContext.getString(R.string.Coffee_Shop));

		final WebDialog feedDialog = new WebDialog.FeedDialogBuilder(mContext,
				Session.getActiveSession(), parameters).build();

		feedDialog.setOnCompleteListener(new OnCompleteListener() {

			@Override
			public void onComplete(Bundle values, FacebookException error) {

				if (error != null) {
					DialogManager.showToast(mContext, mContext.getString(R.string.publish_successfully));
				}
				if (error == null) {
					final String postId = values.getString(mContext.getString(R.string.post_id));
					if (postId != null) {
					} else {
						DialogManager.showToast(mContext, mContext.getString(R.string.publish_cancelled));
					}
				} else if (error instanceof FacebookOperationCanceledException) {
					DialogManager.showToast(mContext, mContext.getString(R.string.publish_cancelled));
				} else {
					DialogManager.showToast(mContext, mContext.getString(R.string.error_posting_story));
					
				}
				Session.getActiveSession().removeCallback(statusCallback);
				if (feedDialog != null) {
					feedDialog.cancel();
				}

			}
		});

		feedDialog.show();

	}
}
