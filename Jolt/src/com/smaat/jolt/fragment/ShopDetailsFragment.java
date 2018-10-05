package com.smaat.jolt.fragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.smaat.jolt.entity.ShopDetailsEntity;
import com.smaat.jolt.entity.ShopOpenTimeEntity;
import com.smaat.jolt.ui.BaseFragment;
import com.smaat.jolt.ui.HomeScreen;
import com.smaat.jolt.util.AppConstants;
import com.smaat.jolt.util.DialogManager;
import com.smaat.jolt.util.GlobalMethods;
import com.squareup.picasso.Picasso;

public class ShopDetailsFragment extends BaseFragment implements
		OnClickListener {
	private ImageView mShopeLogo;
	private Button mDogBtn, mWifiBtn, mGlutenBtn, mRestaurant, mFbShareBtn,
			mTwitterShareBtn, mWhatsAppShareBtn, mEmailShareBtn;
	private TextView mShopName, mShopStreet, mShopCity, mShopSubway, mSubway,
			mShopOpenDay, mShopOpenTime, mBeansDetail, mShopPhNum, mShopMail,
			mShopDistance, mShopClosed, mShopClosedDay;
	private RelativeLayout mShopPhLay, mShopMailLay, mFacilitiesLay;
	private LinkedHashMap<String, String> mLinkedHashMap;
	private String mSun, mMon, mTue, mWed, mThu, mFri, mSat, mPhTemp,
			mMailTemp, mCaptionTxt, mLinkTxt;
	private LinearLayout mOpenLay, mClosedLay, mPhoneMailLay;
	private Uri mPhone, mUri;
	private Intent mShareIntent, mCall, mUrl;
	private View mViewLay;
	private boolean mShare, isFirstTimeDisplay = false,
			isFirstTimeDisplayProgress = false;
	private Session.StatusCallback statusCallback = new SessionStatusCallback();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = inflater.inflate(R.layout.availabledrink_shopinfo,
				container, false);
		setupUI(rootview);
		mLinkedHashMap = new LinkedHashMap<String, String>();
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

		mShopeLogo = (ImageView) view.findViewById(R.id.shope_logo);

		mClosedLay = (LinearLayout) view.findViewById(R.id.closed_lay);
		mClosedLay.setVisibility(View.GONE);
		mDogBtn = (Button) view.findViewById(R.id.dogs_btn);
		mWifiBtn = (Button) view.findViewById(R.id.wifi_btn);
		mGlutenBtn = (Button) view.findViewById(R.id.gluten_btn);
		mRestaurant = (Button) view.findViewById(R.id.restaurant_btn);
		mFbShareBtn = (Button) view.findViewById(R.id.fb_share_btn);
		mTwitterShareBtn = (Button) view.findViewById(R.id.twitter_share_btn);
		mWhatsAppShareBtn = (Button) view.findViewById(R.id.whatsapp_share_btn);
		mEmailShareBtn = (Button) view.findViewById(R.id.email_share_btn);

		mShopName = (TextView) view.findViewById(R.id.shop_name);
		mShopName.setTypeface(HomeScreen.helveticaNeueBold);
		mShopStreet = (TextView) view.findViewById(R.id.shop_street);
		mShopStreet.setTypeface(HomeScreen.helveticaNeueLight);
		mShopCity = (TextView) view.findViewById(R.id.Shop_city);
		mShopCity.setTypeface(HomeScreen.helveticaNeueBold);
		mSubway = (TextView) view.findViewById(R.id.subway);
		mSubway.setTypeface(HomeScreen.helveticaNeueLight);
		mShopSubway = (TextView) view.findViewById(R.id.Shop_subway);
		mShopSubway.setTypeface(HomeScreen.helveticaNeueLight);
		mShopClosed = (TextView) view.findViewById(R.id.closed);
		mShopClosed.setTypeface(HomeScreen.helveticaNeueLight);
		mShopClosedDay = (TextView) view.findViewById(R.id.closed_day);
		mBeansDetail = (TextView) view.findViewById(R.id.beans_detail);
		mBeansDetail.setTypeface(HomeScreen.helveticaNeueLight);
		mShopPhNum = (TextView) view.findViewById(R.id.shop_ph_num);
		mShopPhNum.setTypeface(HomeScreen.helveticaNeueLight);
		mShopMail = (TextView) view.findViewById(R.id.shop_mail);
		mShopMail.setTypeface(HomeScreen.helveticaNeueLight);
		mShopDistance = (TextView) view.findViewById(R.id.shop_distance);
		mShopDistance.setTypeface(HomeScreen.helveticaNeueLight);
		mShopPhLay = (RelativeLayout) view.findViewById(R.id.shop_Ph_lay);
		mShopMailLay = (RelativeLayout) view.findViewById(R.id.shop_mail_lay);
		mFacilitiesLay = (RelativeLayout) view
				.findViewById(R.id.facilities_lay);
		mViewLay = (View) view.findViewById(R.id.view_lay);

		mOpenLay = (LinearLayout) view.findViewById(R.id.open_lay);
		mPhoneMailLay = (LinearLayout) view.findViewById(R.id.Phonemail);

		setClickListener();

		setShopDetails();
	}

	private void setShopDetails() {

		ShopDetailsEntity shopDetails = AvailableDrinks.shopDetails;
		Picasso.with(getActivity())
				.load(AppConstants.BASE_TIMTHUMB
						+ shopDetails.getShopLogoImageUrl()
						+ "&q=200&zc=0&w=200").placeholder(
								getActivity().getResources().getDrawable(
										R.drawable.converstation_jolt_icon))
						.error(getActivity().getResources().getDrawable(
								R.drawable.converstation_jolt_icon)).into(mShopeLogo);

		mShopName.setText(shopDetails.getShopName());
		mShopStreet.setText(shopDetails.getShopStreet());
		mShopCity.setText(shopDetails.getShopCityName());
		mShopSubway.setText(shopDetails.getShopSubway());
		mBeansDetail.setText(" " + shopDetails.getBeans());
		mShopPhNum.setText(shopDetails.getShopContactNumber());
		mShopMail.setText(shopDetails.getShopWebsite());
		String mTemp = shopDetails.getShopFacilities().toLowerCase();

		if (!mTemp.contains(getString(R.string.dog_allowed))
				&& !mTemp.contains(getString(R.string.free_wifi))
				&& !mTemp.contains(getString(R.string.gluten_free))
				&& !mTemp.contains(getString(R.string.restaurant_available))) {
			mFacilitiesLay.setVisibility(View.GONE);
			mViewLay.setVisibility(View.GONE);
		} else {

			mFacilitiesLay.setVisibility(View.VISIBLE);
			mViewLay.setVisibility(View.VISIBLE);
			if (mTemp.contains(getString(R.string.dog_allowed))) {
				mDogBtn.setVisibility(View.VISIBLE);
			} else {
				mDogBtn.setVisibility(View.GONE);
			}
			if (mTemp.contains(getString(R.string.free_wifi))) {
				mWifiBtn.setVisibility(View.VISIBLE);
			} else {
				mWifiBtn.setVisibility(View.GONE);
			}
			if (mTemp.contains(getString(R.string.gluten_free))) {
				mGlutenBtn.setVisibility(View.VISIBLE);
			} else {
				mGlutenBtn.setVisibility(View.GONE);
			}
			if (mTemp.contains(getString(R.string.restaurant_available))) {
				mRestaurant.setVisibility(View.VISIBLE);
			} else {
				mRestaurant.setVisibility(View.GONE);
			}
		}
		mPhTemp = mShopPhNum.getText().toString();
		mMailTemp = mShopMail.getText().toString();

		if (mPhTemp.isEmpty() && mMailTemp.isEmpty()) {
			mPhoneMailLay.setVisibility(View.GONE);
		} else if (mPhTemp.isEmpty()) {
			mShopPhLay.setVisibility(View.GONE);
		} else if (mMailTemp.isEmpty()) {
			mShopMailLay.setVisibility(View.GONE);
		}
		mShopDistance.setText(" "
				+ GlobalMethods.getDistanceString(shopDetails.getDistance(),
						getActivity()));

		ShopOpenTimeEntity response = shopDetails.getShopOpenTime();

		mSun = response.getSun().trim();
		mMon = response.getMon().trim();
		mTue = response.getTue().trim();
		mWed = response.getWed().trim();
		mThu = response.getThu().trim();
		mFri = response.getFri().trim();
		mSat = response.getSat().trim();

		// ArrayList<String> allDays = new ArrayList<String>();

		String mTime[] = { mSun, mMon, mTue, mWed, mThu, mFri, mSat };// time
		String mDay[] = { getString(R.string.Sun), getString(R.string.Mon),
				getString(R.string.Tue), getString(R.string.Wed),
				getString(R.string.Thu), getString(R.string.Fri),
				getString(R.string.Sat) };// day
		for (int i = 0; i < mDay.length; i++) {

			for (int j = i; j < mTime.length - 1; j++) {
				if (mTime[j] == mTime[j + 1]) {
					if (mDay[j + 1].equalsIgnoreCase(getString(R.string.Sat) )) {
						String mDays = mDay[i] + "-" + mDay[j + 1];
						mLinkedHashMap.put(mDays, mTime[j]);
						i = j;
						break;

					}

				} else {
					String mDays;
					if (i == j) {
						mLinkedHashMap.put(mDay[i], mTime[j]);

					} else {
						mDays = mDay[i] + "-" + mDay[j];
						mLinkedHashMap.put(mDays, mTime[j]);
					}
					if (mDay[j + 1].equalsIgnoreCase(getString(R.string.Sat) )) {
						mLinkedHashMap.put(mDay[j + 1], mTime[j + 1]);
						i = j;
						break;

					}
					i = j;
					break;
				}
			}

		}
		String str = "";
		Iterator<String> mIteratorShort = mLinkedHashMap.keySet().iterator();
		while (mIteratorShort.hasNext()) {

			String mkey = (String) mIteratorShort.next();
			String mvalue = (String) mLinkedHashMap.get(mkey);
			if (mvalue.equalsIgnoreCase(getString(R.string.closed))) {
				mClosedLay.setVisibility(View.VISIBLE);
				if (str.equalsIgnoreCase("")) {
					str = mkey;
				} else {
					str = str + "," + mkey;
				}
				mShopClosed.setText(getString(R.string.closed).toUpperCase());
				mShopClosedDay.setText(str);
			} else {
				LayoutInflater mLayoutInflater = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view = mLayoutInflater.inflate(R.layout.shop_open_adapter,
						null);
				mShopOpenDay = (TextView) view.findViewById(R.id.open_day);
				mShopOpenTime = (TextView) view.findViewById(R.id.open_time);
				mShopOpenTime.setText(mvalue);
				mShopOpenDay.setText(mkey);
				mOpenLay.addView(view);
			}
		}

	}

	private void setClickListener() {
		mDogBtn.setOnClickListener(this);
		mWifiBtn.setOnClickListener(this);
		mGlutenBtn.setOnClickListener(this);
		mRestaurant.setOnClickListener(this);
		mFbShareBtn.setOnClickListener(this);
		mTwitterShareBtn.setOnClickListener(this);
		mWhatsAppShareBtn.setOnClickListener(this);
		mEmailShareBtn.setOnClickListener(this);
		mShopPhLay.setOnClickListener(this);
		mShopMailLay.setOnClickListener(this);

	}

	private void setShare(View view, boolean mShareBoolean, String mShareStr) {
		mShareIntent = new Intent(android.content.Intent.ACTION_SEND);
		mShareIntent.setType("text/plain");
		mShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "\n"
				+ getString(R.string.shop_name_txt)
				+ mShopName.getText().toString() + "\n"
				+ getString(R.string.street_txt)
				+ mShopStreet.getText().toString() + "\n"
				+ getString(R.string.subway_txt)
				+ mShopSubway.getText().toString() + "\n"
				+ getString(R.string.website_txt)
				+ mShopMail.getText().toString());
		mShareIntent.putExtra(Intent.EXTRA_SUBJECT,
				getString(R.string.favorite_coffee_shop_txt));
		PackageManager pm = view.getContext().getPackageManager();
		List<ResolveInfo> activityList = pm.queryIntentActivities(mShareIntent,
				0);
		mShare = mShareBoolean;
		for (final ResolveInfo app : activityList) {

			if ((app.activityInfo.name).contains(mShareStr)) {
				mShare = true;
				final ActivityInfo activity = app.activityInfo;

				final ComponentName name = new ComponentName(
						activity.applicationInfo.packageName, activity.name);
				mShareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
				mShareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				mShareIntent.setComponent(name);
				view.getContext().startActivity(mShareIntent);
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
		case R.id.dogs_btn:
			DialogManager.showToast(getActivity(),
					getString(R.string.dog_allowed));
			break;
		case R.id.wifi_btn:
			DialogManager.showToast(getActivity(),
					getString(R.string.wifi_avail));
			break;
		case R.id.gluten_btn:
			DialogManager.showToast(getActivity(),
					getString(R.string.gluten_free));
			break;
		case R.id.restaurant_btn:
			DialogManager.showToast(getActivity(),
					getString(R.string.resta_avail));
			break;
		case R.id.shop_Ph_lay:
			mPhone = Uri.parse(getString(R.string.tel) + mShopPhNum.getText().toString());
			mCall = new Intent(android.content.Intent.ACTION_CALL, mPhone);
			v.getContext().startActivity(mCall);
			break;
		case R.id.shop_mail_lay:
			mUri = Uri.parse(getString(R.string.http) + mShopMail.getText().toString());
			mUrl = new Intent(android.content.Intent.ACTION_VIEW, mUri);
			v.getContext().startActivity(mUrl);
			break;
		case R.id.fb_share_btn:
			mCaptionTxt = getString(R.string.shop_name_txt)
					+ mShopName.getText().toString() + "," + "\n"
					+ getString(R.string.street_txt)
					+ mShopStreet.getText().toString() + "," + "\n"
					+ getString(R.string.subway_txt)
					+ mShopSubway.getText().toString();
			mLinkTxt = mShopMail.getText().toString();

			isFirstTimeDisplay = true;
			isFirstTimeDisplayProgress = true;
			callFbShare();
			break;
		case R.id.twitter_share_btn:
			setShare(v, false, getString(R.string.twitter));
			break;
		case R.id.email_share_btn:
			setShare(v, false, getString(R.string.gm));
			break;
		case R.id.whatsapp_share_btn:
			setShare(v, false, getString(R.string.whatsapp));
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
		parameters.putString(getString(R.string.description), mCaptionTxt);
		if (mLinkTxt.length() <= 0) {
			parameters.putString(getString(R.string.link), getString(R.string.play_store_link));
		} else {
			parameters.putString(getString(R.string.link), mLinkTxt);
		}
		parameters
				.putString(getString(R.string.name),getActivity().getString(R.string.app_name));

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
					final String postId = values.getString(getString(R.string.post_id));
					if (postId != null) {
					} else {
						DialogManager.showToast(getActivity(), getString(R.string.publish_cancelled));
					}
				} else if (error instanceof FacebookOperationCanceledException) {
					DialogManager.showToast(getActivity(), getString(R.string.publish_cancelled));
				} else {
					DialogManager.showToast(getActivity(), getString(R.string.publish_successfully));
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
