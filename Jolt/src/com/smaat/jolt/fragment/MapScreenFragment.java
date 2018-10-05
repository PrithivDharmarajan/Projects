package com.smaat.jolt.fragment;

import java.util.ArrayList;
import java.util.Locale;

import retrofit.RetrofitError;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.smaat.jolt.R;
import com.smaat.jolt.adapter.SearchAdapter;
import com.smaat.jolt.adapter.ShopListAdapter;
import com.smaat.jolt.apiinterface.APIRequestHandler;
import com.smaat.jolt.entity.MarkerIdData;
import com.smaat.jolt.entity.ShopDetailsEntity;
import com.smaat.jolt.model.ShopListResponse;
import com.smaat.jolt.sqlite.DatabaseUtil;
import com.smaat.jolt.ui.BaseActivity;
import com.smaat.jolt.ui.BaseFragment;
import com.smaat.jolt.ui.HomeScreen;
import com.smaat.jolt.util.AppConstants;
import com.smaat.jolt.util.DialogManager;
import com.smaat.jolt.util.GlobalMethods;
import com.squareup.picasso.Picasso;

public class MapScreenFragment extends BaseFragment implements OnClickListener,
		SwipeRefreshLayout.OnRefreshListener {

	GoogleMap mGoogleMap;
	SharedPreferences sharedPreferences;
	int locationCount = 0;
	public static SwipeRefreshLayout mSwipeView;
	public static ListView mMapListView, searchList;
	private static LinearLayout mFooterLay;
	boolean isFirst = false;
	public static RelativeLayout mSerchbylay, mHeaderRight, mSlideMenuHeader,
			mSlideSearchHeader;
	public static ImageView mHeaderMapListView, mFooterImg, mMapListBtn,
			favourite;
	Fragment mFragment;
	private static Button mHeaderRightBtn, mCurrentLocationIcon;
	private double mLatitude, mLongitude;

	OnDragListener onDrag;
	public static EditText mSearchEditTxt;

	private String mUserID, mStart, mLimit;
	private TextView mSearchbyShop, mSearchbyCoffee, mSearch, mFooterTxt,
			mCancelTxt;

	boolean isfavouriteClick = false;

	View rootview;
	private Animation mFromTop, mFromBottom;

	private ShopListAdapter shopListAdapter;

	ArrayList<ShopDetailsEntity> shopList = new ArrayList<ShopDetailsEntity>();
	private static FrameLayout searchSugestionView, map_fragment_main;
	private SearchAdapter searchAdpter;
	private DatabaseUtil db = new DatabaseUtil();

	public MapScreenFragment() {
		// Dummy constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (rootview != null) {

			if (container != null) {
				container.removeView(rootview);
			}

		} else {
			try {
				rootview = inflater.inflate(R.layout.map_activity, container,
						false);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
		mUserID = GlobalMethods.getUserID(getActivity());
		mLatitude = ((HomeScreen) getActivity()).location.lastLat;
		mLongitude = ((HomeScreen) getActivity()).location.lastLong;
		mStart = "";
		mLimit = "";

		if (mLatitude > 90 || mLongitude < -90) {
			mLatitude = 40.739510;
			mLongitude = -74.003029;
		}

		try {
			initializeMap();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String shopData = (String) GlobalMethods.getValueFromPreference(
				getActivity(), GlobalMethods.STRING_PREFERENCE,
				AppConstants.SHOP_LIST_DATA);

		if (shopData == null || shopData.trim().isEmpty()) {
			APIRequestHandler.getInstance().getShopList(mUserID,
					mLatitude + "", mLongitude + "", mStart, mLimit, this);
		} else {
			Gson gson = new Gson();

			shopList.clear();
			ShopListResponse shopResponse = gson.fromJson(shopData,
					ShopListResponse.class);

			updateListWithFavoriteSort(shopResponse.getResult());

			setShopListData(shopList);

		}

	}

	private void updateListWithFavoriteSort(
			ArrayList<ShopDetailsEntity> arrayList) {

		ArrayList<ShopDetailsEntity> tempList = new ArrayList<ShopDetailsEntity>();

		for (ShopDetailsEntity shopDetailsEntity : arrayList) {

			if (db.isFavorite(shopDetailsEntity.getShopId())) {
				shopList.add(shopDetailsEntity);
			} else {
				tempList.add(shopDetailsEntity);
			}
		}

		shopList.addAll(tempList);

	}

	ArrayList<ShopDetailsEntity> searchItemList = new ArrayList<ShopDetailsEntity>();

	private void getMatchedResult(String searchText, String type) {

		searchItemList.clear();

		if (type.equalsIgnoreCase(getString(R.string.search_shop))) {
			for (ShopDetailsEntity shop : shopList) {
				// shop name
				if (shop.getShopName().toLowerCase(Locale.US)
						.contains(searchText.toLowerCase(Locale.US))
						|| shop.getShopStreet().toLowerCase(Locale.US)
								.contains(searchText.toLowerCase(Locale.US))) {
					searchItemList.add(shop);
				}
			}
		} else if (type.equalsIgnoreCase(getString(R.string.search_speciality))) {
			// special
			for (ShopDetailsEntity shop : shopList) {
				if (shop.getSpeciality() != null) {
					if (shop.getSpeciality().toLowerCase(Locale.US)
							.contains(searchText.toLowerCase(Locale.US))) {
						searchItemList.add(shop);
					}
				}
			}
		} else if (type.equalsIgnoreCase(getString(R.string.search_beans))) {
			// beans
			for (ShopDetailsEntity shop : shopList) {

				if (shop.getBeans().toLowerCase(Locale.US)
						.contains(searchText.toLowerCase(Locale.US))) {
					searchItemList.add(shop);
				}
			}
		}

		searchAdpter = new SearchAdapter(getActivity(), R.layout.list_view_row,
				searchItemList);
		searchList.setAdapter(searchAdpter);
		searchAdpter.notifyDataSetChanged();

		if (searchItemList.size() > 0) {
			searchSugestionView.setVisibility(View.VISIBLE);
		} else {
			searchSugestionView.setVisibility(View.INVISIBLE);
		}

	}

	@Override
	public void onRequestSuccess(Object responseObj) {

		if (responseObj instanceof ShopListResponse) {

			DialogManager.hideProgress(getActivity());
			mSwipeView.setRefreshing(false);
			ShopListResponse shopListResponse = (ShopListResponse) responseObj;

			if (shopListResponse.getError_code().equalsIgnoreCase(
					AppConstants.SUCCESS_CODE)) {

				if (shopListResponse.getResult() != null
						&& shopListResponse.getResult().size() > 0) {
					Gson gson = new Gson();

					String temp = gson.toJson(shopListResponse);

					GlobalMethods.storeValuetoPreference(getActivity(),
							GlobalMethods.STRING_PREFERENCE,
							AppConstants.SHOP_LIST_DATA, temp);
					shopList.clear();

					updateListWithFavoriteSort(shopListResponse.getResult());

					setShopListData(shopList);
				}
			}

		}
		super.onRequestSuccess(responseObj);
	}

	@Override
	public void onRequestFailure(RetrofitError errorCode) {

		super.onRequestFailure(errorCode);
		mSwipeView.setRefreshing(false);
	}

	private void setShopListData(ArrayList<ShopDetailsEntity> mList) {
		shopListAdapter = new ShopListAdapter(getActivity(), R.id.shopname,
				mList);
		mMapListView.setAdapter(shopListAdapter);

		setMarker(mList);
	}

	private void initComponents(View view) {

		mSlideMenuHeader = (RelativeLayout) getActivity().findViewById(
				R.id.slide_menu_header);
		mSlideSearchHeader = (RelativeLayout) getActivity().findViewById(
				R.id.slide_search_header);

		mSlideSearchHeader.setVisibility(View.GONE);

		mSearchEditTxt = (EditText) getActivity().findViewById(
				R.id.search_shop_name_edit_txt);
		map_fragment_main = (FrameLayout) getActivity().findViewById(
				R.id.map_fragment_main);

		mCurrentLocationIcon = (Button) view
				.findViewById(R.id.current_location_button);
		mCancelTxt = (TextView) getActivity().findViewById(R.id.cancel_txt);
		mSwipeView = (SwipeRefreshLayout) view
				.findViewById(R.id.map_list_scroll);
		mSwipeView.setOnRefreshListener(this);
		mSwipeView.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		mMapListView = (ListView) view.findViewById(R.id.map_list_view);

		mFooterLay = (LinearLayout) view.findViewById(R.id.footer_lay);
		mSerchbylay = (RelativeLayout) view.findViewById(R.id.serchbylay);
		mSerchbylay.setVisibility(View.GONE);
		mFooterTxt = (TextView) getActivity().findViewById(R.id.footer_txt);
		mFooterTxt.setTypeface(HomeScreen.helveticaNeueBold);
		mFooterImg = (ImageView) getActivity().findViewById(R.id.footer_img);

		mSearchbyShop = (TextView) getActivity().findViewById(R.id.ShopSearch);
		mSearchbyCoffee = (TextView) getActivity().findViewById(
				R.id.CoffeeSearch);
		mSearch = (TextView) getActivity().findViewById(R.id.SearchbyShop);

		mHeaderRight = (RelativeLayout) getActivity().findViewById(
				R.id.header_right);
		mHeaderRightBtn = (Button) getActivity().findViewById(
				R.id.header_right_btn);
		mHeaderRightBtn.setBackgroundResource(R.drawable.header_search_icon);

		mSwipeView.setVisibility(View.VISIBLE);

		searchSugestionView = (FrameLayout) getActivity().findViewById(
				R.id.searchSugestionView);

		searchList = (ListView) getActivity().findViewById(R.id.searchList);

		mFromTop = AnimationUtils.loadAnimation(getActivity(),
				R.anim.anim_in_top);
		mFromBottom = AnimationUtils.loadAnimation(getActivity(),
				R.anim.anim_in_bottom);
		setClickListener();

	}

	private void initializeMap() {
		hideSoftKeyboard(getActivity());

		if (mGoogleMap == null) {
			mGoogleMap = ((SupportMapFragment) getChildFragmentManager()
					.findFragmentById(R.id.map)).getMap();
		}
		if (mGoogleMap == null) {
			DialogManager.showToast(getActivity(),
					getString(R.string.sorry_unable_create_maps));
		} else {
			mGoogleMap.setMyLocationEnabled(true);
			mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
			mGoogleMap.getUiSettings().setCompassEnabled(true);
			mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
			mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);
			mGoogleMap.setInfoWindowAdapter(new InfoWindowAdapter() {

				@Override
				public View getInfoWindow(Marker arg0) {

					int id = 0;
					for (int i = 0; i < mMarkerIDList.size(); i++) {

						if (arg0.getId().equalsIgnoreCase(
								mMarkerIDList.get(i).getMarkerId())) {
							id = Integer.parseInt(mMarkerIDList.get(i)
									.getListId());
							break;
						}
					}

					ShopDetailsEntity shop = shopList.get(id);

					LayoutInflater inflater = (LayoutInflater) getActivity()
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

					View v = inflater.inflate(R.layout.map_popup, null);

					ImageView shopLogo = (ImageView) v
							.findViewById(R.id.shopLogo);
					TextView shopName = (TextView) v
							.findViewById(R.id.shopName);
					TextView shopStreet = (TextView) v
							.findViewById(R.id.shopStreet);
					shopName.setTypeface(HomeScreen.helveticaNeueMedium);
					shopStreet.setTypeface(HomeScreen.helveticaNeueLight);
					shopName.setText(shop.getShopName());
					shopStreet.setText(shop.getShopStreet());

					Picasso.with(getActivity())
							.load(AppConstants.BASE_TIMTHUMB
									+ shop.getShopLogoImageUrl()
									+ "&q=100&zc=0&w=60&h=60")
							.placeholder(
									getActivity().getResources().getDrawable(
											R.drawable.converstation_jolt_icon))
							.error(getActivity().getResources().getDrawable(
									R.drawable.converstation_jolt_icon))
							.into(shopLogo);

					mGoogleMap
							.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

								@Override
								public void onInfoWindowClick(Marker arg0) {
									HomeScreen.mHeaderLeft
											.setTag(HomeScreen.MENU_SCREEN);

									HomeScreen.mHeaderLeftBtn
											.setBackgroundResource(R.drawable.back_btn);
									int id = 0;
									for (int i = 0; i < mMarkerIDList.size(); i++) {

										if (arg0.getId().equalsIgnoreCase(
												mMarkerIDList.get(i)
														.getMarkerId())) {
											id = Integer.parseInt(mMarkerIDList
													.get(i).getListId());
											break;
										}
									}

									AvailableDrinks.shopDetails = shopList
											.get(id);
									((HomeScreen) getActivity()).mFragment = new AvailableDrinks();
									((HomeScreen) getActivity())
											.replaceFragment(
													((HomeScreen) getActivity()).mFragment,
													true);

								}
							});

					return v;
				}

				@Override
				public View getInfoContents(Marker arg0) {

					return null;

				}
			});

			mGoogleMap.setOnMarkerClickListener(new OnMarkerClickListener() {

				@Override
				public boolean onMarkerClick(Marker marker) {

					marker.showInfoWindow();
					return false;
				}
			});

		}

	}

	private void getCurrentLocation(String string) {

		mLatitude = ((HomeScreen) getActivity()).location.lastLat;
		mLongitude = ((HomeScreen) getActivity()).location.lastLong;
		mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(
				mLatitude, mLongitude)));
		mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17));

	}

	private ArrayList<MarkerIdData> mMarkerIDList = new ArrayList<MarkerIdData>();
	private ArrayList<LatLng> mMarkerLatLongList = new ArrayList<LatLng>();

	private void setMarker(ArrayList<ShopDetailsEntity> mList) {

		try {

			if (mGoogleMap != null) {
				mGoogleMap.clear();
				mMarkerIDList.clear();
				mMarkerLatLongList.clear();

				if (mList.size() != 0) {

					for (int i = 0; i < mList.size(); ++i) {

						MarkerOptions markerOptions = new MarkerOptions();
						double lat = Double.parseDouble(mList.get(i)
								.getShopLattitude());
						double lot = Double.parseDouble(mList.get(i)
								.getShopLongitude());

						LatLng newLatLng = new LatLng(lat, lot);
						markerOptions.position(newLatLng);

						markerOptions.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.marker));

						Marker marker = mGoogleMap.addMarker(markerOptions);
						MarkerIdData data = new MarkerIdData();
						data.setMarkerId(marker.getId());
						data.setShopId(mList.get(i).getShopId());
						data.setListId(i + "");
						mMarkerIDList.add(data);
						mMarkerLatLongList.add(newLatLng);

						drawMarker(newLatLng);

					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void drawMarker(LatLng newLatLng1) {

		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(newLatLng1);
		// Changing marker icon
		markerOptions.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.marker));

		try {
			mGoogleMap.addMarker(markerOptions);
			mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(
					mLatitude, mLongitude)));
			mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(11));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setClickListener() {
		mFooterLay.setOnClickListener(this);
		mHeaderRight.setOnClickListener(this);
		mCancelTxt.setOnClickListener(this);
		mCurrentLocationIcon.setOnClickListener(this);
		mSearchbyShop.setOnClickListener(this);
		mSearchbyCoffee.setOnClickListener(this);
		mSearch.setOnClickListener(this);

		searchList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {

						BaseActivity
								.hideSoftKeyboard((HomeScreen) getActivity());
						updateHeader();
						HomeScreen.mHeaderLeft.setTag(HomeScreen.MENU_SCREEN);
						HomeScreen.mHeaderLeftBtn
								.setBackgroundResource(R.drawable.back_btn);

						AvailableDrinks.shopDetails = searchItemList
								.get(position);
						((HomeScreen) getActivity()).mFragment = new AvailableDrinks();

						((HomeScreen) getActivity()).replaceFragment(
								((HomeScreen) getActivity()).mFragment, true);
					}
				});

		mSearchEditTxt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (mSearchEditTxt.getText().toString().equals("")) {
					searchSugestionView.setVisibility(View.INVISIBLE);
				} else {
					getMatchedResult(s.toString().trim(), mSearchEditTxt
							.getHint().toString());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.footer_lay:
			if (mSwipeView.getVisibility() == View.VISIBLE) {
				mSwipeView.setVisibility(View.INVISIBLE);
				map_fragment_main.setVisibility(View.VISIBLE);
				mSwipeView.startAnimation(mFromTop);
				mFooterTxt.setText(R.string.list_txt);
				mFooterImg.setImageResource(R.drawable.maptolist);

			} else {
				mSwipeView.setVisibility(View.VISIBLE);
				mSwipeView.startAnimation(mFromBottom);
				mFooterTxt.setText(R.string.map_txt);
				mFooterImg.setImageResource(R.drawable.map_view_icon);
			}
			break;

		case R.id.current_location_button:
			getCurrentLocation("isNext");
			break;

		case R.id.header_right:
			((HomeScreen) getActivity()).closeDrawer();
			mSlideMenuHeader.setVisibility(View.GONE);
			mSlideSearchHeader.setVisibility(View.VISIBLE);
			mSerchbylay.setVisibility(View.VISIBLE);
			mSearchEditTxt.setVisibility(View.GONE);
			break;
		case R.id.cancel_txt:
			updateHeader();
			mSerchbylay.setVisibility(View.GONE);
			break;
		case R.id.ShopSearch:
			mSearchEditTxt.setHint(getString(R.string.search_speciality));
			mSearchEditTxt.setVisibility(View.VISIBLE);
			mSerchbylay.setVisibility(View.GONE);
			break;
		case R.id.CoffeeSearch:
			mSearchEditTxt.setHint(getString(R.string.search_shop));
			mSearchEditTxt.setVisibility(View.VISIBLE);
			mSerchbylay.setVisibility(View.GONE);
			break;
		case R.id.SearchbyShop:
			mSearchEditTxt.setHint(getString(R.string.search_beans));
			mSearchEditTxt.setVisibility(View.VISIBLE);
			mSerchbylay.setVisibility(View.GONE);
			break;
		}

	}

	public static void updateHeader() {
		mSearchEditTxt.setText("");
		mSlideMenuHeader.setVisibility(View.VISIBLE);
		mSlideSearchHeader.setVisibility(View.GONE);
		searchSugestionView.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onRefresh() {

		APIRequestHandler.getInstance().getShopList(mUserID, mLatitude + "",
				mLongitude + "", mStart, mLimit, this);

	}

}
