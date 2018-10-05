package com.smaat.jolt.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smaat.jolt.R;
import com.smaat.jolt.entity.DrinksDetailsEntity;
import com.smaat.jolt.entity.ShopDetailsEntity;
import com.smaat.jolt.entity.ShopOpenTimeEntity;
import com.smaat.jolt.sqlite.DatabaseUtil;
import com.smaat.jolt.ui.BaseFragment;
import com.smaat.jolt.ui.HomeScreen;
import com.smaat.jolt.util.AppConstants;
import com.smaat.jolt.util.CustomMapFragment;
import com.smaat.jolt.util.DialogManager;
import com.smaat.jolt.util.GlobalMethods;
import com.squareup.picasso.Picasso;

public class AvailableDrinks extends BaseFragment implements OnClickListener {

	private ViewPager pager, drinksPager, drinkSize;
	private GoogleMap mGoogleMap;
	private double mCurrentLatitude, mCurrentLongitude;
	private FrameLayout mMapLay;
	boolean ismapClicked = false;
	private static View rootview;
	private Button mButtonArray[] = new Button[9], mLetsDo, mSelectOne,
			mSelectTwo, mSelectThree, mSelectFour, mSelectFive, mSelectSix,
			mSelectSeven, mSelectEight, mSelectNine;

	private RelativeLayout leftArrow, rightArrow, imgmap, imgdetails,
			favourite, mChooseDrinkLeft, mChooseDrinkRight, mDrinkSizeLeft,
			mDrinkSizeRight, mMapShopDetail;

	private ImageView mCurrentLocation, mSlidepointerOne, mSlidepointerTwo,
			mSlidepointerThree, mFavouriteSelectImg, btnList, mapShopDetails;

	private int mCurrentPos = 0, mCurrentDrinkPos = 0,
			mCurrentDrinkSizePos = 0, oldSelectorM = 0, mDrinksCount = 0,
			mSizeLength;

	private TextView drinkslist, drinkslist1, drinkslist2, small, medium,
			special, mChooseDrinkTxt, mChooseDrinkSizeTxt, mHowmanyTxt,
			shopopen, shopname, distance, shopstreet, mMapShopName,
			mMapDistance, mMapShopStreet, atmosphere;

	private Animation mFromRight, mFromLeft;

	public static ShopDetailsEntity shopDetails;
	private String mUserType, mChooseDrink, mChooseDrinkSize, mCurrentDay,
			mCurrentTime, mShopOpenTime = "";
	private Bundle mBundle;
	private long mCurrentTimeMillies, mShopStartTimeMillies,
			mShopEndTimeMillies;
	SimpleDateFormat timeFormat = new SimpleDateFormat(
			"EEE MMM dd kk:mm:ss yyyy", Locale.ENGLISH);
	private boolean mIsChangedDay;
	private ShopOpenTimeEntity mShopOpenTimeEntity;
	private DatabaseUtil db = new DatabaseUtil();
	private ScrollView main_layout;
	private String[] drinkSizeList, drinksList;
	private CustomDrinksSizeAdapter drinksizeadapter;

	@Override
	public void onResume() {

		super.onResume();
		initComponents(rootview);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootview != null) {

		} else {
			try {
				rootview = inflater.inflate(R.layout.drinks_available_fragment,
						container, false);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		mBundle = new Bundle();
		return rootview;

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

	}

	public void setViewData() {

		mMapLay.setVisibility(View.GONE);
		pager.setVisibility(View.VISIBLE);

		try {
			getCurrentDateTime();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		mShopOpenTimeEntity = shopDetails.getShopOpenTime();

		if (db.isFavorite(shopDetails.getShopId())) {
			mFavouriteSelectImg
					.setImageResource(R.drawable.favouriteheartselected);
		} else {
			mFavouriteSelectImg
					.setImageResource(R.drawable.favouriteheartunselected);
		}
		try {
			getShopOpenTime(mShopOpenTimeEntity, mCurrentDay);

			shopopen.setText(mShopOpenTime.toUpperCase(Locale.US));
		} catch (ParseException e1) {

			e1.printStackTrace();
		}
		shopname.setText(shopDetails.getShopName());
		String distanceValue = GlobalMethods.getDistanceString(
				shopDetails.getDistance(), getActivity());
		distance.setText(distanceValue);
		shopstreet.setText(shopDetails.getShopStreet());

		atmosphere.setText(shopDetails.getAtmosphere());

		mMapShopName.setText(shopDetails.getShopName());
		mMapDistance.setText(distanceValue);
		mMapShopStreet.setText(shopDetails.getShopStreet());

		ArrayList<DrinksDetailsEntity> drinksListData = shopDetails
				.getDrinksDetails();

		final HashMap<String, String[]> tempDrinkList = new HashMap<String, String[]>();

		drinksList = new String[drinksListData.size()];
		for (int i = 0; i < drinksListData.size(); i++) {

			tempDrinkList.put(drinksListData.get(i).getDrinkName(),
					drinksListData.get(i).getDrinkSize().split(","));

			drinksList[i] = drinksListData.get(i).getDrinkName();
		}

		final int mDrinksLength = drinksList.length;
		// By default first position.
		drinkSizeList = tempDrinkList.get(drinksList[0]);
		mSizeLength = drinkSizeList.length;

		if (mDrinksLength > 0) {
			CustomPagerAdapter adapter = new CustomPagerAdapter(getActivity(),
					shopDetails);

			CustomDrinksAdapter drinksadapter = new CustomDrinksAdapter(
					getActivity(), drinksList);
			drinksizeadapter = new CustomDrinksSizeAdapter(getActivity(),
					drinkSizeList);
			drinkSize.setAdapter(drinksizeadapter);
			pager.setAdapter(adapter);
			drinksPager.setAdapter(drinksadapter);
		}

		mChooseDrinkRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCurrentDrinkPos = drinksPager.getCurrentItem();
				drinksPager.setCurrentItem(++mCurrentDrinkPos);
			}

		});

		mChooseDrinkLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCurrentDrinkPos = drinksPager.getCurrentItem();
				drinksPager.setCurrentItem(--mCurrentDrinkPos);

			}
		});

		drinksPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int pos) {
				drinkSizeList = tempDrinkList.get(drinksList[pos]);
				mSizeLength = drinkSizeList.length;

				drinksizeadapter = new CustomDrinksSizeAdapter(getActivity(),
						drinkSizeList);

				drinkSize.setAdapter(drinksizeadapter);

			}

			@Override
			public void onPageScrolled(int mCurrentDrinkPos, float arg1,
					int arg2) {

				mChooseDrink = drinksList[mCurrentDrinkPos];
				if (mCurrentDrinkPos == 0) {
					mChooseDrinkLeft.setVisibility(View.INVISIBLE);
					mChooseDrinkRight.setVisibility(View.VISIBLE);

				} else if (mCurrentDrinkPos == mDrinksLength - 1) {
					mChooseDrinkLeft.setVisibility(View.VISIBLE);
					mChooseDrinkRight.setVisibility(View.INVISIBLE);
				} else {
					mChooseDrinkLeft.setVisibility(View.VISIBLE);
					mChooseDrinkRight.setVisibility(View.VISIBLE);
				}

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		mDrinkSizeRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCurrentDrinkSizePos = drinkSize.getCurrentItem();
				drinkSize.setCurrentItem(++mCurrentDrinkSizePos);
			}

		});
		mDrinkSizeLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCurrentDrinkSizePos = drinkSize.getCurrentItem();
				drinkSize.setCurrentItem(--mCurrentDrinkSizePos);
			}

		});
		drinkSize.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
			}

			@Override
			public void onPageScrolled(int mCurrentDrinkSizePos, float arg1,
					int arg2) {
				mChooseDrinkSize = drinkSizeList[mCurrentDrinkSizePos];
				if (mCurrentDrinkSizePos == 0) {
					mDrinkSizeLeft.setVisibility(View.INVISIBLE);
					mDrinkSizeRight.setVisibility(View.VISIBLE);
				} else if (mCurrentDrinkSizePos == mSizeLength - 1) {
					mDrinkSizeLeft.setVisibility(View.VISIBLE);
					mDrinkSizeRight.setVisibility(View.INVISIBLE);
				} else {
					mDrinkSizeLeft.setVisibility(View.VISIBLE);
					mDrinkSizeRight.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {

			}

			@Override
			public void onPageScrolled(int mCurrentPos, float arg1, int arg2) {
				if (mCurrentPos == 0) {
					mSlidepointerOne
							.setImageResource(R.drawable.nextpageselect);
					mSlidepointerTwo
							.setImageResource(R.drawable.nextpageunselect);
					mSlidepointerThree
							.setImageResource(R.drawable.nextpageunselect);
					leftArrow.setVisibility(View.INVISIBLE);
					rightArrow.setVisibility(View.VISIBLE);
				} else if (mCurrentPos == 1) {
					mSlidepointerOne
							.setImageResource(R.drawable.nextpageunselect);
					mSlidepointerTwo
							.setImageResource(R.drawable.nextpageselect);
					mSlidepointerThree
							.setImageResource(R.drawable.nextpageunselect);
					leftArrow.setVisibility(View.VISIBLE);
					rightArrow.setVisibility(View.VISIBLE);
				} else {
					mSlidepointerOne
							.setImageResource(R.drawable.nextpageunselect);
					mSlidepointerTwo
							.setImageResource(R.drawable.nextpageunselect);
					mSlidepointerThree
							.setImageResource(R.drawable.nextpageselect);

					leftArrow.setVisibility(View.VISIBLE);
					rightArrow.setVisibility(View.INVISIBLE);
				}

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		double lat = Double.parseDouble(shopDetails.getShopLattitude());
		double longitude = Double.parseDouble(shopDetails.getShopLongitude());

		// create marker
		MarkerOptions marker = new MarkerOptions().position(new LatLng(lat,
				longitude));

		// Changing marker icon
		marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));

		if (mGoogleMap != null) {
			mGoogleMap.addMarker(marker);
			mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,
					longitude)));
			mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(18));
		} else {
			initializeMap();

			try {
				// adding marker
				mGoogleMap.addMarker(marker);
				mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(
						lat, longitude)));
				mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(18));

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	private void initComponents(View view) {

		main_layout = (ScrollView) view.findViewById(R.id.main_layout);
		shopopen = (TextView) view.findViewById(R.id.shopopen);
		shopopen.setTypeface(HomeScreen.helveticaNeueBold);
		shopname = (TextView) view.findViewById(R.id.shopname);
		shopname.setTypeface(HomeScreen.helveticaNeueBold);
		distance = (TextView) view.findViewById(R.id.distance);
		distance.setTypeface(HomeScreen.helveticaNeueMedium);
		shopstreet = (TextView) view.findViewById(R.id.shopstreet);
		atmosphere = (TextView) view.findViewById(R.id.atmosphere);

		atmosphere.setTypeface(HomeScreen.helveticaNeueBold);
		shopstreet.setTypeface(HomeScreen.helveticaNeueLight);

		mMapShopDetail = (RelativeLayout) view
				.findViewById(R.id.map_shop_detail);
		mMapShopDetail.setVisibility(View.GONE);
		mMapShopName = (TextView) view.findViewById(R.id.map_shopname);
		mMapShopName.setTypeface(HomeScreen.helveticaNeueBold);
		mMapDistance = (TextView) view.findViewById(R.id.map_distance);
		mMapDistance.setTypeface(HomeScreen.helveticaNeueMedium);
		mMapShopStreet = (TextView) view.findViewById(R.id.map_shopstreet);
		mMapShopStreet.setTypeface(HomeScreen.helveticaNeueLight);

		mChooseDrinkTxt = (TextView) view.findViewById(R.id.choosedrink);
		mChooseDrinkTxt.setTypeface(HomeScreen.helveticaNeueBold);

		mChooseDrinkSizeTxt = (TextView) view
				.findViewById(R.id.choosedrink_size);
		mChooseDrinkSizeTxt.setTypeface(HomeScreen.helveticaNeueBold);

		mHowmanyTxt = (TextView) view.findViewById(R.id.howmany);
		mHowmanyTxt.setTypeface(HomeScreen.helveticaNeueBold);

		mUserType = (String) GlobalMethods.getValueFromPreference(
				getActivity(), GlobalMethods.STRING_PREFERENCE,
				AppConstants.LOGIN_TYPE);

		pager = (ViewPager) view.findViewById(R.id.view_pager);
		drinksPager = (ViewPager) view.findViewById(R.id.choosedrinkpager);
		drinkSize = (ViewPager) view.findViewById(R.id.drinkssize);

		mChooseDrinkLeft = (RelativeLayout) view
				.findViewById(R.id.choosedrink_left);
		mChooseDrinkLeft.setVisibility(View.INVISIBLE);
		mChooseDrinkRight = (RelativeLayout) view
				.findViewById(R.id.choosedrink_right);
		mDrinkSizeLeft = (RelativeLayout) view
				.findViewById(R.id.drinkssize_left);
		mDrinkSizeLeft.setVisibility(View.INVISIBLE);
		mDrinkSizeRight = (RelativeLayout) view
				.findViewById(R.id.drinkssize_right);

		leftArrow = (RelativeLayout) view.findViewById(R.id.leftArrow);
		leftArrow.setVisibility(View.INVISIBLE);
		rightArrow = (RelativeLayout) view.findViewById(R.id.rightArrow);
		mSlidepointerOne = (ImageView) view.findViewById(R.id.slidepointer_one);
		mSlidepointerTwo = (ImageView) view.findViewById(R.id.slidepointer_two);
		mSlidepointerThree = (ImageView) view
				.findViewById(R.id.slidepointer_three);

		imgmap = (RelativeLayout) view.findViewById(R.id.imgMap);
		mFavouriteSelectImg = (ImageView) view
				.findViewById(R.id.favouriteselect_img);

		imgdetails = (RelativeLayout) view.findViewById(R.id.imgDetails);

		mapShopDetails = (ImageView) view.findViewById(R.id.imgListDetails);
		favourite = (RelativeLayout) view.findViewById(R.id.favouriteselect);

		try {
			initializeMap();

		} catch (Exception e) {
			e.printStackTrace();
		}
		mCurrentLocation = (ImageView) getActivity().findViewById(
				R.id.drinkscurrentLocation);
		mMapLay = (FrameLayout) view.findViewById(R.id.map_frame_lay);
		btnList = (ImageView) view.findViewById(R.id.available_drinks_detail);
		mLetsDo = (Button) view.findViewById(R.id.letsdo);
		mSelectOne = (Button) view.findViewById(R.id.one);
		mSelectTwo = (Button) view.findViewById(R.id.two);
		mSelectThree = (Button) view.findViewById(R.id.three);
		mSelectFour = (Button) view.findViewById(R.id.four);
		mSelectFive = (Button) view.findViewById(R.id.five);
		mSelectSix = (Button) view.findViewById(R.id.six);
		mSelectSeven = (Button) view.findViewById(R.id.seven);
		mSelectEight = (Button) view.findViewById(R.id.eight);
		mSelectNine = (Button) view.findViewById(R.id.nine);
		mButtonArray[0] = mSelectOne;
		mButtonArray[1] = mSelectTwo;
		mButtonArray[2] = mSelectThree;
		mButtonArray[3] = mSelectFour;
		mButtonArray[4] = mSelectFive;
		mButtonArray[5] = mSelectSix;
		mButtonArray[6] = mSelectSeven;
		mButtonArray[7] = mSelectEight;
		mButtonArray[8] = mSelectNine;
		for (int n = 0; n < 9; n++) {

			mButtonArray[n].setBackgroundResource(R.drawable.cup_unselected);
			mDrinksCount = 0;
			mButtonArray[n].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (v.getId()) {
					case R.id.one:
						select(0);
						break;
					case R.id.two:
						select(1);
						break;
					case R.id.three:
						select(2);
						break;
					case R.id.four:
						select(3);
						break;
					case R.id.five:
						select(4);
						break;
					case R.id.six:
						select(5);
						break;
					case R.id.seven:
						select(6);
						break;
					case R.id.eight:
						select(7);
						break;
					case R.id.nine:
						select(8);
						break;

					}

				}

			});
		}
		mFromRight = AnimationUtils.loadAnimation(getActivity(),
				R.anim.anim_in_right);
		mFromLeft = AnimationUtils.loadAnimation(getActivity(),
				R.anim.anim_in_left);
		setClickListener();

		setViewData();

	}

	private void select(int m) {

		if (oldSelectorM == m) {
			mButtonArray[m].setBackgroundResource(R.drawable.cup_unselected);
			mDrinksCount = oldSelectorM--;
			oldSelectorM += 2;
		} else {

			oldSelectorM = m;
			mDrinksCount = oldSelectorM;
			mDrinksCount++;
			for (int i = 0; i <= m; i++) {
				mButtonArray[i].setBackgroundResource(R.drawable.cup_selected);
			}
			for (int j = m + 1; j < 9; j++) {
				mButtonArray[j]
						.setBackgroundResource(R.drawable.cup_unselected);
			}
		}
	}

	private void initializeMap() {

		try {
			if (mGoogleMap == null) {

				mGoogleMap = ((CustomMapFragment) getChildFragmentManager()
						.findFragmentById(R.id.map_fragment_view)).getMap();

				((CustomMapFragment) getChildFragmentManager()
						.findFragmentById(R.id.map_fragment_view))
						.setListener(new CustomMapFragment.OnTouchListener() {
							@Override
							public void onTouch() {
								hideSoftKeyboard(getActivity());
								main_layout
										.requestDisallowInterceptTouchEvent(true);
							}
						});

				if (mGoogleMap == null) {
					DialogManager.showToast(getActivity(),
							getString(R.string.sorry_unable_create_maps));
				} else {
					mGoogleMap.setMyLocationEnabled(true);
					mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
					mGoogleMap.getUiSettings().setCompassEnabled(true);
					mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);
					mGoogleMap.getUiSettings()
							.setMyLocationButtonEnabled(false);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void getCurrentLocation() {
		try {
			mCurrentLatitude = ((HomeScreen) getActivity()).location.lastLat;
			mCurrentLongitude = ((HomeScreen) getActivity()).location.lastLong;

			mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(
					mCurrentLatitude, mCurrentLongitude)));
			mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	class CustomPagerAdapter extends PagerAdapter {

		Context mContext;
		LayoutInflater mLayoutInflater;

		ArrayList<String> imageList = new ArrayList<String>();
		ShopDetailsEntity mList;

		public CustomPagerAdapter(Context context, ShopDetailsEntity mList) {
			mContext = context;
			imageList.add(mList.getShopImage1());
			imageList.add(mList.getShopImage2());
			imageList.add(mList.getShopImage3());
			mLayoutInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.mList = mList;
		}

		@Override
		public int getCount() {
			// only we have maximum 3 images
			return imageList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((LinearLayout) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View itemView = mLayoutInflater.inflate(R.layout.pager_item,
					container, false);

			ImageView imageView = (ImageView) itemView
					.findViewById(R.id.imageView);

			Picasso.with(getActivity())
					.load(AppConstants.BASE_TIMTHUMB + imageList.get(position)
							+ "&q=100&zc=0&w=" + HomeScreen.screenWidth)
					.into(imageView);

			container.addView(itemView);

			return itemView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((LinearLayout) object);
		}
	}

	class CustomDrinksAdapter extends PagerAdapter {

		Context mContext;
		LayoutInflater mLayoutInflater;
		String[] drinks;
		int i = 1;

		public CustomDrinksAdapter(Context context, String[] drinks) {
			mContext = context;
			this.drinks = drinks;
			mLayoutInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return drinks.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			// Declare Variables

			View itemView = mLayoutInflater.inflate(R.layout.drinks_pager_item,
					container, false);

			// Locate the TextViews in viewpager_item.xml
			drinkslist = (TextView) itemView.findViewById(R.id.drink1);
			drinkslist.setTypeface(HomeScreen.helveticaNeueLight);
			drinkslist1 = (TextView) itemView.findViewById(R.id.drink2);
			drinkslist1.setTypeface(HomeScreen.helveticaNeueBold);
			drinkslist2 = (TextView) itemView.findViewById(R.id.drink3);
			drinkslist2.setTypeface(HomeScreen.helveticaNeueLight);

			// Capture position and set to the TextViews

			if (position == 0) {
				drinkslist.setText("");
				drinkslist1.setText("( " + drinks[position] + " )");
				if (drinks.length > position + 1) {
					drinkslist2.setText(drinks[position + 1]);
				} else {
					drinkslist2.setText("");
				}

			} else if (drinks.length - 1 == position) {
				drinkslist.setText(drinks[position - 1]);
				drinkslist1.setText("( " + drinks[position] + " )");
				drinkslist2.setText("");

			} else {

				if (position == i) {
					drinkslist.setText(drinks[position - 1]);
					drinkslist1.setText("( " + drinks[position] + " )");
					drinkslist2.setText(drinks[position + 1]);
					i++;
				} else {
					drinkslist.setText(drinks[position - 1]);
					drinkslist1.setText("( " + drinks[position] + " )");
					drinkslist2.setText(drinks[position + 1]);
				}

			}

			((ViewPager) container).addView(itemView);

			return itemView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

	class CustomDrinksSizeAdapter extends PagerAdapter {

		Context mContext;
		LayoutInflater mLayoutInflater;
		String[] drinksSize;
		int i = 1;

		public CustomDrinksSizeAdapter(Context context, String[] drinksSize) {
			mContext = context;
			this.drinksSize = drinksSize;
			mLayoutInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return drinksSize.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			// Declare Variables

			View itemView = mLayoutInflater.inflate(
					R.layout.drinks_size_pageritem, container, false);

			small = (TextView) itemView.findViewById(R.id.smallsize);
			small.setTypeface(HomeScreen.helveticaNeueLight);
			medium = (TextView) itemView.findViewById(R.id.mediumsize);
			medium.setTypeface(HomeScreen.helveticaNeueBold);
			special = (TextView) itemView.findViewById(R.id.specialmilk);
			special.setTypeface(HomeScreen.helveticaNeueLight);
			if (position == 0) {
				small.setText("");
				medium.setText("( " + drinksSize[position] + " )");
				if (drinksSize.length > position + 1) {
					special.setText(drinksSize[position + 1]);
				} else {
					special.setText("");
				}

			} else if (drinksSize.length - 1 == position) {
				small.setText(drinksSize[position - 1]);
				medium.setText("( " + drinksSize[position] + " )");
				special.setText("");
			} else {
				if (position == i) {
					small.setText(drinksSize[position - 1]);
					medium.setText("( " + drinksSize[position] + " )");
					special.setText(drinksSize[position + 1]);
					i++;
				} else {
					small.setText(drinksSize[position - 1]);
					medium.setText("( " + drinksSize[position] + " )");
					special.setText(drinksSize[position + 1]);
				}
			}

			((ViewPager) container).addView(itemView);

			return itemView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

	public void setClickListener() {

		btnList.setOnClickListener(this);
		mLetsDo.setOnClickListener(this);
		mCurrentLocation.setOnClickListener(this);
		imgdetails.setOnClickListener(this);
		imgmap.setOnClickListener(this);
		leftArrow.setOnClickListener(this);
		rightArrow.setOnClickListener(this);
		favourite.setOnClickListener(this);
		mChooseDrinkRight.setOnClickListener(this);
		mapShopDetails.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.available_drinks_detail:
			mMapShopDetail.startAnimation(mFromLeft);
			mMapShopDetail.setVisibility(View.GONE);
			mMapLay.setVisibility(View.GONE);
			pager.setVisibility(View.VISIBLE);
			mMapLay.startAnimation(mFromLeft);
			pager.startAnimation(mFromRight);
			break;
		case R.id.imgMap:
			mMapShopDetail.startAnimation(mFromRight);
			mMapShopDetail.setVisibility(View.VISIBLE);
			mMapLay.setVisibility(View.VISIBLE);
			pager.setVisibility(View.GONE);
			mMapLay.startAnimation(mFromRight);
			pager.startAnimation(mFromLeft);

			break;
		case R.id.letsdo:

			if (!mUserType.equalsIgnoreCase(AppConstants.LOGIN_TYPE_GUEST)) {
				if (mDrinksCount > 0) {
					String cardNumberValue = ((String) GlobalMethods
							.getValueFromPreference(getActivity(),
									GlobalMethods.STRING_PREFERENCE,
									AppConstants.CARD_NUMBER)).trim();

					if (cardNumberValue.isEmpty()) {

						DialogManager.showToast(getActivity(),
								getString(R.string.add_card_alert));

						HomeScreen.mHeaderLeft
								.setTag(HomeScreen.SECONDARY_SCREEN);
						HomeScreen.moveBackFragment = new AvailableDrinks();
						((HomeScreen) getActivity()).updateDisplayFragment(
								new AddCreditCard(), true);
					}

					else {

						// int cupCount;
						// try {
						// cupCount = Integer.parseInt(((String) GlobalMethods
						// .getValueFromPreference(getActivity(),
						// GlobalMethods.STRING_PREFERENCE,
						// AppConstants.AVAIL_DRINKS)).trim());
						// } catch (Exception e) {
						// cupCount = 0;
						// }
						HomeScreen.mHeaderLeft
								.setTag(HomeScreen.SECONDARY_SCREEN);
						HomeScreen.moveBackFragment = new AvailableDrinks();
						mBundle.putString(AppConstants.POSITION,
								String.valueOf(mDrinksCount));
						mBundle.putString(AppConstants.CHOOSE_DRINKS,
								String.valueOf(mChooseDrink));
						mBundle.putString(AppConstants.CHOOSE_DRINK_SIZE,
								String.valueOf(mChooseDrinkSize));
						((HomeScreen) getActivity()).mFragment = new ShopLetsDoThis();
						((HomeScreen) getActivity()).mFragment
								.setArguments(mBundle);
						((HomeScreen) getActivity()).replaceFragment(
								((HomeScreen) getActivity()).mFragment, true);
					}
				} else {
					DialogManager.showToast(getActivity(),
							getString(R.string.please_select_cup));
				}
			} else {
				DialogManager.showCustomAlertSignInDialog(getActivity(),
						getString(R.string.sign_in_to_continue), null);
			}

			break;

		case R.id.drinkscurrentLocation:
			getCurrentLocation();
			break;

		case R.id.favouriteselect:

			if (db.isFavorite(shopDetails.getShopId())) {
				mFavouriteSelectImg
						.setImageResource(R.drawable.favouriteheartunselected);

				String temp = String.format(
						getString(R.string.removed_favorite),
						shopDetails.getShopName());
				DialogManager.showToast(getActivity(), temp);

				db.makeUnfavorite(shopDetails.getShopId());

			} else {
				mFavouriteSelectImg
						.setImageResource(R.drawable.favouriteheartselected);
				String temp = String.format(getString(R.string.added_favorite),
						shopDetails.getShopName());
				DialogManager.showToast(getActivity(), temp);
				db.makeFavorite(shopDetails.getShopId());
			}
			break;
		case R.id.rightArrow:
			mCurrentPos = pager.getCurrentItem();
			pager.setCurrentItem(++mCurrentPos);
			break;

		case R.id.leftArrow:
			mCurrentPos = pager.getCurrentItem();
			pager.setCurrentItem(--mCurrentPos);

			break;

		case R.id.imgDetails:
			HomeScreen.mHeaderLeft.setTag(HomeScreen.SECONDARY_SCREEN);
			HomeScreen.moveBackFragment = new AvailableDrinks();
			((HomeScreen) getActivity()).mFragment = new ShopDetailsFragment();
			((HomeScreen) getActivity()).replaceFragment(
					((HomeScreen) getActivity()).mFragment, true);
			break;

		case R.id.imgListDetails:

			HomeScreen.mHeaderLeft.setTag(HomeScreen.SECONDARY_SCREEN);
			HomeScreen.moveBackFragment = new AvailableDrinks();
			((HomeScreen) getActivity()).mFragment = new ShopDetailsFragment();
			((HomeScreen) getActivity()).replaceFragment(
					((HomeScreen) getActivity()).mFragment, true);
			break;
		}

	}

	@SuppressLint("SimpleDateFormat")
	private void getCurrentDateTime() throws ParseException {
		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		calendar.add(Calendar.DATE, 1);
		String strDate = timeFormat.format(today);

		Date newDate = timeFormat.parse(strDate);
		SimpleDateFormat dayFormat = new SimpleDateFormat(
				getString(R.string.EEE));
		SimpleDateFormat timeFormat = new SimpleDateFormat(
				getString(R.string.h_mm_a));

		mCurrentDay = dayFormat.format(newDate);
		mCurrentTime = timeFormat.format(newDate);
		try {
			Date mDateStart = timeFormat.parse(mCurrentTime);
			mCurrentTimeMillies = mDateStart.getTime();

		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	private void getShopOpenTime(ShopOpenTimeEntity mShopOpenTimeEntity,
			String mCurrentDay) throws ParseException {
		if (mCurrentDay.equalsIgnoreCase(getString(R.string.Mon))) {
			if (mShopOpenTimeEntity.getMon().equalsIgnoreCase(
					getString(R.string.closed))) {
				changeDay(mCurrentDay);
			} else {
				shopOpenTimeCalculation(
						mShopOpenTimeEntity.getMon().toString(), mCurrentDay);
			}
		} else if (mCurrentDay.equalsIgnoreCase(getString(R.string.Tue))) {
			if (mShopOpenTimeEntity.getTue().equalsIgnoreCase(
					getString(R.string.closed))) {
				changeDay(mCurrentDay);
			} else {
				shopOpenTimeCalculation(
						mShopOpenTimeEntity.getTue().toString(), mCurrentDay);
			}
		} else if (mCurrentDay.equalsIgnoreCase(getString(R.string.Wed))) {
			if (mShopOpenTimeEntity.getWed().equalsIgnoreCase(
					getString(R.string.closed))) {
				changeDay(mCurrentDay);
			} else {
				shopOpenTimeCalculation(
						mShopOpenTimeEntity.getWed().toString(), mCurrentDay);
			}
		} else if (mCurrentDay.equalsIgnoreCase(getString(R.string.Thu))) {
			if (mShopOpenTimeEntity.getThu().equalsIgnoreCase(
					getString(R.string.closed))) {
				changeDay(mCurrentDay);
			} else {
				shopOpenTimeCalculation(
						mShopOpenTimeEntity.getThu().toString(), mCurrentDay);
			}
		} else if (mCurrentDay.equalsIgnoreCase(getString(R.string.Fri))) {
			if (mShopOpenTimeEntity.getFri().equalsIgnoreCase(
					getString(R.string.closed))) {
				changeDay(mCurrentDay);
			} else {
				shopOpenTimeCalculation(
						mShopOpenTimeEntity.getFri().toString(), mCurrentDay);
			}
		} else if (mCurrentDay.equalsIgnoreCase(getString(R.string.Sat))) {
			if (mShopOpenTimeEntity.getSat().equalsIgnoreCase(
					getString(R.string.closed))) {
				changeDay(mCurrentDay);
			} else {
				shopOpenTimeCalculation(
						mShopOpenTimeEntity.getSat().toString(), mCurrentDay);
			}
		} else if (mCurrentDay.equalsIgnoreCase(getString(R.string.Sun))) {
			if (mShopOpenTimeEntity.getSun().equalsIgnoreCase(
					getString(R.string.closed))) {
				changeDay(mCurrentDay);
			} else {
				shopOpenTimeCalculation(
						mShopOpenTimeEntity.getSun().toString(), mCurrentDay);
			}
		}

	}

	@SuppressLint("SimpleDateFormat")
	private void shopOpenTimeCalculation(String mResponseStr, String mCurrentDay)
			throws ParseException {

		String[] mStrings = mResponseStr.split("-");
		String mStartTime = mStrings[0].trim();
		String mEndTime = mStrings[1].trim();

		if (mIsChangedDay) {
			mIsChangedDay = false;
			mShopOpenTime = getString(R.string.Open_at) + " " + mCurrentDay
					+ " " + mStartTime;
		} else {

			SimpleDateFormat mFormat = new SimpleDateFormat(
					getString(R.string.h_mm_a));
			try {
				Date mDateStart = mFormat.parse(mStartTime);
				mShopStartTimeMillies = mDateStart.getTime();
				Date mDateEnd = mFormat.parse(mEndTime);
				mShopEndTimeMillies = mDateEnd.getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if (mCurrentTimeMillies < mShopStartTimeMillies) {
				long mins = mShopStartTimeMillies - mCurrentTimeMillies;
				if (mins <= 3540000) {
					mins = mins / 60000;
					mShopOpenTime = getString(R.string.Open_at) + " "
							+ String.valueOf(mins) + getString(R.string.mins);
				} else {
					mShopOpenTime = getString(R.string.Open_at) + " "
							+ mStartTime;
				}

			} else if (mCurrentTimeMillies > mShopStartTimeMillies
					&& mCurrentTimeMillies < mShopEndTimeMillies) {
				mShopOpenTime = getString(R.string.open_now);

			} else if (mCurrentTimeMillies > mShopEndTimeMillies) {
				changeDay(mCurrentDay);
			}

		}

	}

	private void changeDay(String mCurrentDay) throws ParseException {
		mIsChangedDay = true;
		if (mCurrentDay.equalsIgnoreCase(getString(R.string.Mon))) {
			mCurrentDay = getString(R.string.Tue);
		} else if (mCurrentDay.equalsIgnoreCase(getString(R.string.Tue))) {
			mCurrentDay = getString(R.string.Wed);
		} else if (mCurrentDay.equalsIgnoreCase(getString(R.string.Wed))) {
			mCurrentDay = getString(R.string.Thu);
		} else if (mCurrentDay.equalsIgnoreCase(getString(R.string.Thu))) {
			mCurrentDay = getString(R.string.Fri);
		} else if (mCurrentDay.equalsIgnoreCase(getString(R.string.Fri))) {
			mCurrentDay = getString(R.string.Sat);
		} else if (mCurrentDay.equalsIgnoreCase(getString(R.string.Sat))) {
			mCurrentDay = getString(R.string.Sun);
		} else if (mCurrentDay.equalsIgnoreCase(getString(R.string.Sun))) {
			mCurrentDay = getString(R.string.Mon);
		}
		if (mShopOpenTimeEntity != null) {
			getShopOpenTime(mShopOpenTimeEntity, mCurrentDay);
		}
	}
}
