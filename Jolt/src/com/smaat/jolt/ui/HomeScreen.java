package com.smaat.jolt.ui;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.widget.DrawerLayout;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.smaat.jolt.R;
import com.smaat.jolt.apiinterface.APICommonInterface;
import com.smaat.jolt.fragment.AddCreditCard;
import com.smaat.jolt.fragment.AvailableDrinks;
import com.smaat.jolt.fragment.Checkout;
import com.smaat.jolt.fragment.CoffeWaitFragment;
import com.smaat.jolt.fragment.CouponCodeFragment;
import com.smaat.jolt.fragment.GetFreeCoffeeFragment;
import com.smaat.jolt.fragment.HistoryFragment;
import com.smaat.jolt.fragment.JoltPlansFragment;
import com.smaat.jolt.fragment.MapScreenFragment;
import com.smaat.jolt.fragment.MessageUsFragment;
import com.smaat.jolt.fragment.MyPlans;
import com.smaat.jolt.fragment.NewConversationFragment;
import com.smaat.jolt.fragment.ProfileFragment;
import com.smaat.jolt.fragment.SettingsFragment;
import com.smaat.jolt.fragment.ShopDetailsFragment;
import com.smaat.jolt.fragment.ShopLetsDoThis;
import com.smaat.jolt.model.TermsResponse;
import com.smaat.jolt.util.AppConstants;
import com.smaat.jolt.util.DialogManager;
import com.smaat.jolt.util.DialogMangerCallback;
import com.smaat.jolt.util.GlobalMethods;
import com.smaat.jolt.util.TypefaceSingleton;
import com.squareup.picasso.Picasso;

@SuppressWarnings("deprecation")
public class HomeScreen extends BaseActivity implements DialogMangerCallback,
		OnClickListener {
	public Fragment mFragment;
	private AvailableDrinks mAvailableDrinks;
	private MapScreenFragment mMapScreenFragment;
	public static TextView mHeaderText, mBackTxt, mFooterText, mEnterCoupon,
			mPromoCode, mMyHistory, mGetFreeCoffee, mSendGift, mMessageUs,
			mEmailUs;
	public static ImageView mHeaderMapListView, mBackArrow, mHeaderLeftBtn;
	public static LinearLayout mBottombar, mSignIn, mProfileLayout,
			mSlideMenuBar;
	public static RelativeLayout mHeaderLeft, mHeaderRight;

	public static View mEnterCouponView, mMyHistoryView, mGetFreeCoffeeView,
			mSendGiftView, mMessageUsView, mEmailUsView;

	public static Button mHeaderCup, mHeaderRightBtn;
	public static boolean isHeaderLeft = true;

	private ActionBarDrawerToggle mDrawerToggle;

	private DrawerLayout mDrawerLayout;
	private ScrollView slidemenu_view;
	FrameLayout home_screen_second_view;
	public final static int MAIN_SCREEN = 1, MENU_SCREEN = 2,
			SECONDARY_SCREEN = 3;
	public static Fragment moveBackFragment = null;
	private String mUserType, mUserID, mDeviceID, mDevice, mTxt, mMessage,
			mType, mInfo;
	private Bundle mBundle;
	private ImageView user_profile_img;
	public LocationInfo location;
	public static int screenWidth;
	public static Typeface helveticaNeueBold, helveticaNeueMedium,
			helveticaNeueLight, helveticaNeueRegular, kGBlankSpace, corbel;
	private TextView mJoltPlan, mSuggest, mRecommendBarista, mHelp,
			mViewProfile, user_name_txt;
	public static Context mContext;
	private static Dialog mDialog;
	private Uri mUri;
	private Intent mUrl;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screenWidth = size.x;
		mContext = HomeScreen.this;
		ViewGroup mViewGroup = (ViewGroup) findViewById(R.id.parent_layout);
		setupUI(mViewGroup);

		helveticaNeueBold = TypefaceSingleton.getTypeface()
				.getHelveticaNeueBold(this);
		helveticaNeueMedium = TypefaceSingleton.getTypeface()
				.getHelveticaNeueMedium(this);
		helveticaNeueLight = TypefaceSingleton.getTypeface()
				.getHelveticaNeueLight(this);
		helveticaNeueRegular = TypefaceSingleton.getTypeface()
				.getHelveticaNeue(this);
		kGBlankSpace = TypefaceSingleton.getTypeface().getKGBlankSpace(this);
		corbel = TypefaceSingleton.getTypeface().getCorbel(this);

		if (location == null) {
			location = new LocationInfo(this);
		}
		location.refresh(this);
		mAvailableDrinks = new AvailableDrinks();

		mMapScreenFragment = new MapScreenFragment();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.member_drawer_layout);

		slidemenu_view = (ScrollView) findViewById(R.id.member_slidemenu);
		user_name_txt = (TextView) findViewById(R.id.user_name_txt);

		user_profile_img = (ImageView) findViewById(R.id.user_profile_img);

		mEnterCoupon = (TextView) findViewById(R.id.entercoupon);
		mPromoCode = (TextView) findViewById(R.id.promocode);
		mEnterCouponView = (View) findViewById(R.id.entercoupon_view);
		mMyHistory = (TextView) findViewById(R.id.myhistory);
		mMyHistoryView = (View) findViewById(R.id.myhistoryview);
		mGetFreeCoffee = (TextView) findViewById(R.id.getfreecoffe);
		mGetFreeCoffeeView = (View) findViewById(R.id.getfreecoffeview);
		mSendGift = (TextView) findViewById(R.id.sendgift);
		mSendGiftView = (View) findViewById(R.id.sendgiftview);
		mMessageUs = (TextView) findViewById(R.id.messageus);
		mEmailUs = (TextView) findViewById(R.id.txtEmailUs);
		mEmailUsView = (View) findViewById(R.id.viewEmailUs);
		mMessageUsView = (View) findViewById(R.id.viewmessageus);

		mJoltPlan = (TextView) findViewById(R.id.joltplan);
		mSuggest = (TextView) findViewById(R.id.suggest);
		mRecommendBarista = (TextView) findViewById(R.id.recommend_barista);
		mHelp = (TextView) findViewById(R.id.help);
		mViewProfile = (TextView) findViewById(R.id.view_profile);
		// user_name_txt=(TextView) findViewById(R.id.user_name_txt);

		user_name_txt.setTypeface(helveticaNeueMedium);
		mViewProfile.setTypeface(helveticaNeueRegular);
		mJoltPlan.setTypeface(helveticaNeueRegular);
		mEnterCoupon.setTypeface(helveticaNeueRegular);
		mPromoCode.setTypeface(helveticaNeueRegular);
		mMyHistory.setTypeface(helveticaNeueRegular);
		mGetFreeCoffee.setTypeface(helveticaNeueRegular);
		mSuggest.setTypeface(helveticaNeueRegular);
		mRecommendBarista.setTypeface(helveticaNeueRegular);
		mSendGift.setTypeface(helveticaNeueRegular);
		mMessageUs.setTypeface(helveticaNeueRegular);
		mHelp.setTypeface(helveticaNeueRegular);

		mSignIn = (LinearLayout) findViewById(R.id.SignInLayout);
		mProfileLayout = (LinearLayout) findViewById(R.id.profile_lay);

		mUserType = (String) GlobalMethods.getValueFromPreference(this,
				GlobalMethods.STRING_PREFERENCE, AppConstants.LOGIN_TYPE);

		mUserID = (String) GlobalMethods.getValueFromPreference(this,
				GlobalMethods.STRING_PREFERENCE, AppConstants.USER_ID);
		/* Push Notification */

		String deviceId = (String) GlobalMethods.getValueFromPreference(this,
				GlobalMethods.STRING_PREFERENCE, AppConstants.DEVICE_ID);

		boolean isRegistered = (Boolean) GlobalMethods.getValueFromPreference(
				this, GlobalMethods.BOOLEAN_PREFERENCE,
				AppConstants.ISREGISTERED);

		boolean isDeveiceIdChanged = (Boolean) GlobalMethods
				.getValueFromPreference(this, GlobalMethods.BOOLEAN_PREFERENCE,
						AppConstants.ISDEVICEIDCHANGED);

		if (!isRegistered || isDeveiceIdChanged) {
			if (deviceId != null && !deviceId.equals("")) {

			} else {
				GCMRegistrar.register(this, AppConstants.SENDER_ID);
			}
		}
		mDeviceID = (String) GlobalMethods.getValueFromPreference(this,
				GlobalMethods.STRING_PREFERENCE, AppConstants.DEVICE_ID);
		mDevice = getString(R.string.Android);

		if (mUserID.equalsIgnoreCase("0")) {

		} else {
			updateDeviceID();
		}

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.menu_btn, R.string.app_name, R.string.app_name) {

			public void onDrawerClosed(View view) {
				// super.onDrawerClosed(view);
				invalidateOptionsMenu();

				if ((Integer) mHeaderLeft.getTag() == MAIN_SCREEN)
					updateMenuToggleIcon(R.drawable.menu_btn);
			}

			public void onDrawerOpened(View drawerView) {
				// super.onDrawerOpened(drawerView);
				invalidateOptionsMenu();
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		initview();

		if (!GlobalMethods.getUserID(this).equalsIgnoreCase(
				AppConstants.DEFAULT_USERID)) {

			user_name_txt.setText((String) GlobalMethods
					.getValueFromPreference(this,
							GlobalMethods.STRING_PREFERENCE,
							AppConstants.FULL_NAME));
			String profilePic = (String) GlobalMethods.getValueFromPreference(
					this, GlobalMethods.STRING_PREFERENCE,
					AppConstants.PROFILE_PIC);
			Picasso.with(this)
					.load(AppConstants.BASE_TIMTHUMB + profilePic
							+ "&q=100&h=150&w=150&zc=0").into(user_profile_img);
		}
		mViewGroup.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (mDrawerLayout.isDrawerOpen(slidemenu_view)) {
					mDrawerLayout.closeDrawer(slidemenu_view);
					if ((Integer) mHeaderLeft.getTag() == MAIN_SCREEN)
						updateMenuToggleIcon(R.drawable.menu_btn);
				}

				return false;
			}
		});
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			// mBundleString = mBundle.getString("Fragment");
			mMessage = mBundle.getString(getString(R.string.message));
			mType = mBundle.getString(getString(R.string.type));
			mInfo = mBundle.getString(getString(R.string.info));
		}

		mFragment = mMapScreenFragment;

		mHeaderCup.setTypeface(helveticaNeueBold);

		mHeaderRight.setVisibility(View.VISIBLE);

		if (!mUserType.equalsIgnoreCase(AppConstants.LOGIN_TYPE_GUEST)) {
			mHeaderText.setText(R.string.available_drinks_txt);
			mHeaderCup.setVisibility(View.VISIBLE);
			mHeaderCup.setText(countText());
		} else {
			mHeaderText.setText(R.string.sign_in_caps);
			mHeaderCup.setVisibility(View.GONE);
		}

		mHeaderText.setTypeface(helveticaNeueBold);

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.home_screen_second_view, mFragment)
				.addToBackStack(null).commit();

		String distanceUnit = (String) GlobalMethods.getValueFromPreference(
				this, GlobalMethods.STRING_PREFERENCE,
				AppConstants.DISTANCE_UNIT);

		if (distanceUnit.isEmpty()
				|| distanceUnit.equalsIgnoreCase(getString(R.string.miles))) {

			AppConstants.appDistanceUnit = getString(R.string.miles);

		} else {
			AppConstants.appDistanceUnit = getString(R.string.km);
		}

		if (mMessage != null) {
			displayNotifyDialog(mMessage, mType, mInfo);
		}
		showAddCreditCard();

	}

	private void showAddCreditCard() {
		String cardNumberValue = ((String) GlobalMethods
				.getValueFromPreference(this, GlobalMethods.STRING_PREFERENCE,
						AppConstants.CARD_NUMBER)).trim();

		if ( !mUserID.equalsIgnoreCase("0") && cardNumberValue.isEmpty()) {
			DialogManager.showToast(this, getString(R.string.add_card_alert));

			HomeScreen.mHeaderLeft.setTag(HomeScreen.MENU_SCREEN);
			HomeScreen.moveBackFragment = new MapScreenFragment();
			updateMenuToggleIcon(R.drawable.back_btn);
			updateDisplayFragment(new AddCreditCard(), true);
		}
	}

	private void updateDeviceID() {
		RestAdapter mRestAdapter = new RestAdapter.Builder().setEndpoint(
				AppConstants.BASE_URL).build();

		APICommonInterface mApiCommonInterface = mRestAdapter
				.create(APICommonInterface.class);

		mApiCommonInterface.updateDeviceID(mUserID, mDeviceID, mDevice,
				new Callback<TermsResponse>() {

					@Override
					public void success(TermsResponse arg0, Response arg1) {

					}

					@Override
					public void failure(RetrofitError arg0) {

					}
				});

	}

	public void headerClick(View view) {
		if (mHeaderText.getText().toString()
				.equalsIgnoreCase(getString(R.string.available_drinks_txt))) {
			mFragment = new ProfileFragment();
			updateDisplayFragment(mFragment, true);
			closeDrawer();

			mHeaderLeft.setTag(MENU_SCREEN);
			updateMenuToggleIcon(R.drawable.back_btn);
		} else if (mHeaderText.getText().toString()
				.equalsIgnoreCase(getString(R.string.sign_in_caps))) {
			launchActivity(SignInScreen.class);
		}
	}

	private void initview() {

		mHeaderCup = (Button) findViewById(R.id.header_cup);
		mHeaderText = (TextView) findViewById(R.id.header_text);
		mHeaderLeft = (RelativeLayout) findViewById(R.id.header_menu_toggle);
		mHeaderRight = (RelativeLayout) findViewById(R.id.header_right);
		mHeaderLeftBtn = (ImageView) findViewById(R.id.header_menu_toggle_btn);
		mHeaderRightBtn = (Button) findViewById(R.id.header_right_btn);

		mHeaderLeft.setTag(MAIN_SCREEN);

		if (mUserType.equalsIgnoreCase(AppConstants.LOGIN_TYPE_GUEST)) {

			mHeaderText.setText(R.string.sign_in_caps);
			mEnterCoupon.setVisibility(View.GONE);
			mPromoCode.setVisibility(View.GONE);
			mEnterCouponView.setVisibility(View.GONE);
			mMyHistory.setVisibility(View.GONE);
			mMyHistoryView.setVisibility(View.GONE);
			mGetFreeCoffee.setVisibility(View.GONE);
			mGetFreeCoffeeView.setVisibility(View.GONE);
			mSendGift.setVisibility(View.GONE);
			mSendGiftView.setVisibility(View.GONE);
			mMessageUs.setVisibility(View.GONE);
			mMessageUsView.setVisibility(View.GONE);
			mEmailUsView.setVisibility(View.VISIBLE);
			mSignIn.setVisibility(View.VISIBLE);
			mEmailUs.setVisibility(View.VISIBLE);
			mEmailUsView.setVisibility(View.VISIBLE);
			mProfileLayout.setVisibility(View.GONE);
		} else {
			mEnterCoupon.setVisibility(View.GONE);
			mPromoCode.setVisibility(View.VISIBLE);
			mEnterCouponView.setVisibility(View.VISIBLE);
			mMyHistory.setVisibility(View.VISIBLE);
			mMyHistoryView.setVisibility(View.VISIBLE);
			mGetFreeCoffee.setVisibility(View.VISIBLE);
			mGetFreeCoffeeView.setVisibility(View.VISIBLE);
			mSendGift.setVisibility(View.GONE);
			mSendGiftView.setVisibility(View.GONE);
			mMessageUs.setVisibility(View.VISIBLE);
			mSignIn.setVisibility(View.GONE);
			mEmailUsView.setVisibility(View.GONE);
			mEmailUsView.setVisibility(View.GONE);
			mMessageUsView.setVisibility(View.VISIBLE);
			mEmailUs.setVisibility(View.GONE);
			mProfileLayout.setVisibility(View.VISIBLE);
		}

		mHeaderLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				backButtonNavigation(v);
			}
		});

		home_screen_second_view = (FrameLayout) findViewById(R.id.home_screen_second_view);

	}

	public void changeRightIcon() {

		mHeaderRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mFragment instanceof MessageUsFragment) {

					DialogManager.showToast(HomeScreen.this,
							getString(R.string.Messages));

				}

			}
		});

	}

	public void updateMenuToggleIcon(int drawableIcon) {

		mHeaderLeftBtn.setBackgroundResource(drawableIcon);

	}

	@Override
	public void onOkclick() {

	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void replaceFragment(Fragment mFragment, boolean isForwardAnimation) {
		if (mFragment instanceof AvailableDrinks) {

			mFragment = mAvailableDrinks;
		}
		if (mFragment instanceof MapScreenFragment) {
			mFragment = mMapScreenFragment;

			mHeaderRight.setVisibility(View.VISIBLE);

			if (!mUserType.equalsIgnoreCase(AppConstants.LOGIN_TYPE_GUEST)) {
				mHeaderCup.setVisibility(View.VISIBLE);
			} else {
				mHeaderCup.setVisibility(View.GONE);
			}

		} else if ((mFragment instanceof AvailableDrinks)
				|| (mFragment instanceof CoffeWaitFragment)
				|| (mFragment instanceof ShopDetailsFragment)
				|| (mFragment instanceof ShopLetsDoThis)) {
			mHeaderRight.setVisibility(View.INVISIBLE);
			if (!mUserType.equalsIgnoreCase(AppConstants.LOGIN_TYPE_GUEST)) {
				mHeaderCup.setVisibility(View.VISIBLE);
			} else {
				mHeaderCup.setVisibility(View.GONE);
			}
		} else {
			mHeaderRight.setVisibility(View.INVISIBLE);
			mHeaderCup.setVisibility(View.GONE);
		}
		if (mFragment != null && !mFragment.isDetached()) {

			if (isForwardAnimation) {
				showForwardAnimation(mFragment);
			} else {
				showBackWordAnimation(mFragment);
			}

		}

		home_screen_second_view.setVisibility(View.VISIBLE);

	}

	private void showForwardAnimation(Fragment mFragment) {

		getSupportFragmentManager()
				.addOnBackStackChangedListener(getListener());
		getSupportFragmentManager()
				.beginTransaction()
				.setCustomAnimations(R.anim.slide_in_right,
						R.anim.slide_out_left, R.anim.slide_in_left,
						R.anim.slide_out_right)
				.replace(R.id.home_screen_second_view, mFragment)
				.addToBackStack(null).commit();

	}

	private void showBackWordAnimation(Fragment mFragment) {

		getSupportFragmentManager()
				.addOnBackStackChangedListener(getListener());
		getSupportFragmentManager()
				.beginTransaction()
				.setCustomAnimations(R.anim.slide_out_right,
						R.anim.slide_in_left, R.anim.slide_out_left,
						R.anim.slide_in_right)
				.replace(R.id.home_screen_second_view, mFragment)
				.addToBackStack(null).commit();

	}

	private OnBackStackChangedListener getListener() {
		OnBackStackChangedListener result = new OnBackStackChangedListener() {
			public void onBackStackChanged() {
				FragmentManager manager = getSupportFragmentManager();

				if (manager != null) {
					BaseFragment currFrag = (BaseFragment) manager
							.findFragmentById(R.id.home_screen_second_view);

					currFrag.onResume();
				}
			}
		};

		return result;
	}

	public void showHomeScreenView() {

		if (!mUserType.equalsIgnoreCase(AppConstants.LOGIN_TYPE_GUEST)) {
			mHeaderText.setText(R.string.available_drinks_txt);

			mHeaderCup.setText(countText());

			mHeaderRight.setVisibility(View.VISIBLE);
			mHeaderCup.setVisibility(View.VISIBLE);
		} else {
			mHeaderText.setText(R.string.sign_in_caps);
			mHeaderRight.setVisibility(View.VISIBLE);
			mHeaderCup.setVisibility(View.GONE);
		}

		mHeaderLeft.setTag(MAIN_SCREEN);

		try {
			MapScreenFragment.mSwipeView.setVisibility(View.VISIBLE);
		} catch (Exception e) {
			e.printStackTrace();
		}

		mFragment = new MapScreenFragment();
		getSupportFragmentManager()
				.addOnBackStackChangedListener(getListener());
		getSupportFragmentManager()
				.beginTransaction()
				.setCustomAnimations(R.anim.slide_out_right,
						R.anim.slide_in_left, R.anim.slide_out_left,
						R.anim.slide_in_right)
				.replace(R.id.home_screen_second_view, mFragment)
				.addToBackStack(null).commit();

		showAddCreditCard();

	}

	public String countText() {
		int count = 0;
		try {
			count = Integer.parseInt((String) GlobalMethods
					.getValueFromPreference(this,
							GlobalMethods.STRING_PREFERENCE,
							AppConstants.AVAIL_DRINKS));
		} catch (Exception e) {
			count = 0;
		}

		String countText;
		if (count > 99) {
			countText = "U";
		} else {
			countText = count + "";
		}
		return countText;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onActivityResult(int req, int res, Intent intent) {
		super.onActivityResult(req, res, intent);

		if (mFragment instanceof ProfileFragment) {

			((ProfileFragment) mFragment).onActivityResult(req, res, intent);

		}
		if (mFragment instanceof JoltPlansFragment) {

			((JoltPlansFragment) mFragment).onActivityResult(req, res, intent);

		}
		if (mFragment instanceof AvailableDrinks) {

			((AvailableDrinks) mFragment).onActivityResult(req, res, intent);

		}

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.profile_lay:
			mFragment = new ProfileFragment();
			updateDisplayFragment(mFragment, true);
			closeDrawer();

			mHeaderLeft.setTag(MENU_SCREEN);
			updateMenuToggleIcon(R.drawable.back_btn);

			break;
		case R.id.joltplan:
			GlobalMethods.storeValuetoPreference(this,
					GlobalMethods.STRING_PREFERENCE,
					AppConstants.JOLT_PLANS_INCLUDED_DISPLAY_VIEW,
					AppConstants.IS_HOME_SCREEN);
			mFragment = new JoltPlansFragment();
			updateDisplayFragment(mFragment, true);
			closeDrawer();

			mHeaderLeft.setTag(MENU_SCREEN);
			updateMenuToggleIcon(R.drawable.back_btn);

			break;
		case R.id.myhistory:
			mFragment = new HistoryFragment();
			updateDisplayFragment(mFragment, true);
			closeDrawer();

			mHeaderLeft.setTag(MENU_SCREEN);
			updateMenuToggleIcon(R.drawable.back_btn);

			break;
		case R.id.entercoupon:

			mFragment = new CouponCodeFragment();
			GlobalMethods.storeValuetoPreference(this,
					GlobalMethods.STRING_PREFERENCE,
					AppConstants.IS_VALIDATECOUPON_SCREEN,
					AppConstants.IS_COUPONCODE_SCREEN);
			mHeaderText.setText(R.string.coupon_code);
			HomeScreen.moveBackFragment = mFragment;
			updateDisplayFragment(mFragment, true);
			closeDrawer();
			mHeaderLeft.setTag(MENU_SCREEN);
			updateMenuToggleIcon(R.drawable.back_btn);

			break;
		case R.id.promocode:

			mFragment = new CouponCodeFragment();
			GlobalMethods.storeValuetoPreference(this,
					GlobalMethods.STRING_PREFERENCE,
					AppConstants.IS_VALIDATECOUPON_SCREEN,
					AppConstants.IS_PROMOCODE_SCREEN);
			mHeaderText.setText(R.string.promo_code);
			HomeScreen.moveBackFragment = mFragment;
			updateDisplayFragment(mFragment, true);
			closeDrawer();

			mHeaderLeft.setTag(MENU_SCREEN);
			updateMenuToggleIcon(R.drawable.back_btn);

			break;
		case R.id.getfreecoffe:
			GlobalMethods.storeValuetoPreference(this,
					GlobalMethods.STRING_PREFERENCE,
					AppConstants.COFFEE_WAIT_SCREEN,
					AppConstants.IS_HOME_SCREEN);
			mFragment = new GetFreeCoffeeFragment();
			updateDisplayFragment(mFragment, true);
			closeDrawer();

			mHeaderLeft.setTag(MENU_SCREEN);
			updateMenuToggleIcon(R.drawable.back_btn);

			break;
		case R.id.suggest:
			mTxt = getString(R.string.coffee_shop_name) + "\n"
					+ getString(R.string.coffee_shop_address) + "\n"
					+ getString(R.string.contact_information);
			GlobalMethods.SendMail(this, getString(R.string.recommend_mail_id),
					getString(R.string.want_suggest_shop), mTxt);
			break;
		case R.id.recommend_barista:
			mTxt = "\t\t\t" + getString(R.string.barista_msg) + "\n\n"
					+ getString(R.string.barista_name) + "\n"
					+ getString(R.string.coffee_shop_name);
			GlobalMethods.SendMail(this, getString(R.string.recommend_mail_id),
					getString(R.string.want_recommend_barista), mTxt);
			break;
		case R.id.txtEmailUs:
			String mTxt = "";
			GlobalMethods.SendMail(this, getString(R.string.admin_mail_id),
					getString(R.string.hi_team), mTxt);
			break;

		case R.id.sendgift:
			mUri = Uri.parse(getString(R.string.http)
					+ getString(R.string.App_link));
			mUrl = new Intent(android.content.Intent.ACTION_VIEW, mUri);
			v.getContext().startActivity(mUrl);

			break;

		case R.id.messageus:

			mFragment = new MessageUsFragment();
			updateDisplayFragment(mFragment, true);
			closeDrawer();

			mHeaderLeft.setTag(MENU_SCREEN);
			updateMenuToggleIcon(R.drawable.back_btn);

			break;

		case R.id.SignInLayout:

			launchActivity(SignInScreen.class);

		case R.id.help:
			showSettingsScreen();
			break;

		}

	}

	private void showSettingsScreen() {
		mFragment = new SettingsFragment();
		updateDisplayFragment(mFragment, true);
		if (mDrawerLayout.isDrawerOpen(slidemenu_view)) {
			mDrawerLayout.closeDrawer(slidemenu_view);
		}

		mHeaderLeft.setTag(MENU_SCREEN);
		updateMenuToggleIcon(R.drawable.back_btn);
	}

	public void updateDisplayFragment(Fragment fragment,
			boolean isForwardAnimation) {

		if (fragment instanceof JoltPlansFragment) {

			mHeaderText.setText(R.string.choose_your_plan_txt);

		} else if (fragment instanceof ProfileFragment) {
			mHeaderText.setText(R.string.my_account_txt);

		} else if (fragment instanceof HistoryFragment) {

			mHeaderText.setText(R.string.my_history_txt);

		} else if (fragment instanceof CouponCodeFragment) {
			// mHeaderText.setText(R.string.coupon_code);
		} else if (fragment instanceof GetFreeCoffeeFragment) {
			mHeaderText.setText(R.string.get_a_free_coffee_txt);
		} else if (fragment instanceof MessageUsFragment) {
			mHeaderText.setText(R.string.conversations_txt);
		} else if (fragment instanceof NewConversationFragment) {
			mHeaderText.setText(R.string.conversations_txt);
		} else if (fragment instanceof SettingsFragment) {
			mHeaderText.setText(R.string.settings_txt);
		}

		else if (fragment instanceof Checkout) {
			mHeaderText.setText(R.string.checkout_text);

		} else if (fragment instanceof AddCreditCard) {
			mHeaderText.setText(R.string.enter_credit_card_txt);

		} else if (fragment instanceof MyPlans) {
			mHeaderText.setText(R.string.my_plans);

		} else if (fragment instanceof AvailableDrinks) {
			mHeaderText.setText(R.string.available_drinks_txt);
			mHeaderCup.setVisibility(View.VISIBLE);
			mHeaderCup.setText(countText());
		}
		replaceFragment(fragment, isForwardAnimation);

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		JOLTApplication.activityResumed();
	}

	public static void displayNotifyDialog(final String mMessage,
			final String mType, final String mInfo) {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
		mDialog = new Dialog(mContext);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.notify_popup);
		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
		wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		wmlp.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

		TextView mNotifyTxt = (TextView) mDialog.findViewById(R.id.notify_txt);

		if (mType.equalsIgnoreCase("1")) {
			mNotifyTxt.setText(mInfo);
		} else {
			mNotifyTxt.setText(mMessage);
		}

		RelativeLayout mNotifyLay = (RelativeLayout) mDialog
				.findViewById(R.id.notify_lay);
		mNotifyLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
				if (mType.equalsIgnoreCase("1")) {
					((HomeScreen) mContext).mFragment = new MessageUsFragment();
					((HomeScreen) mContext).updateDisplayFragment(
							((HomeScreen) mContext).mFragment, true);
					((HomeScreen) mContext).closeDrawer();
					mHeaderLeft.setTag(MENU_SCREEN);
					((HomeScreen) mContext)
							.updateMenuToggleIcon(R.drawable.back_btn);
				} else {
					GlobalMethods.storeValuetoPreference(mContext,
							GlobalMethods.STRING_PREFERENCE,
							AppConstants.AVAIL_DRINKS, mInfo);
				}
			}
		});
		mDialog.show();

	}

	private void backButtonNavigation(View v) {

		if (mDrawerLayout.isDrawerOpen(slidemenu_view)) {
			mDrawerLayout.closeDrawer(slidemenu_view);
			updateMenuToggleIcon(R.drawable.menu_btn);
		} else if ((Integer) v.getTag() == MAIN_SCREEN) {
			mDrawerLayout.openDrawer(slidemenu_view);
			updateMenuToggleIcon(R.drawable.back_btn);
		}

		if ((Integer) v.getTag() == MENU_SCREEN) {

			v.setTag(MAIN_SCREEN);
			showHomeScreenView();
			updateMenuToggleIcon(R.drawable.menu_btn);
			if (mDrawerLayout.isDrawerOpen(slidemenu_view)) {
				mDrawerLayout.closeDrawer(slidemenu_view);

			}
		} else if ((Integer) v.getTag() == SECONDARY_SCREEN) {

			if (moveBackFragment != null) {
				updateDisplayFragment(moveBackFragment, false);
			}

			mHeaderLeft.setTag(MENU_SCREEN);
		}

	}

	@Override
	public void onBackPressed() {
		if (mHeaderLeft == null
				|| (Integer) mHeaderLeft.getTag() == MAIN_SCREEN) {
			finish();
			JOLTApplication.activityStoped();
		} else {
			backButtonNavigation(mHeaderLeft);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		JOLTApplication.activityStoped();
	}

	public void closeDrawer() {
		if (mDrawerLayout.isDrawerOpen(slidemenu_view)) {
			mDrawerLayout.closeDrawer(slidemenu_view);
		}
	}

}
