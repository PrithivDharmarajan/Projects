package com.smaat.renterblock.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.adapter.ListingAdapter;
import com.smaat.renterblock.entity.LocalEntityForPhotosandVideos;
import com.smaat.renterblock.entity.PropertyEntity;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.model.ListingResponse;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TypefaceSingleton;

public class ListingActivity extends BaseActivity implements OnClickListener {

	private GridView mListingGrid;
	private ListingAdapter mListingAdapter;
	private ArrayList<PropertyEntity> mListingResultList;
	private TextView mHeaderText;
	private Button mHeaderLeftIcon, edt_btn;
	private LinearLayout mAddIcon, mEditIcon;
	private Bundle mBundle;
	public static String file_i;
	private LinearLayout mPropListingList;
	private ArrayList<Button> mCheckBtnList = new ArrayList<Button>();
	private boolean isShown = false;
	RelativeLayout edit_del_view;
	static PropertyEntity prop_entity;
	static LocalEntityForPhotosandVideos photo_vid_entity = new LocalEntityForPhotosandVideos();
	public static String file_position;
	private ArrayList<String> mPropIDS = new ArrayList<String>();
	private String mAlert = "Alert", mPropID = "";
	/**
	 * Slide Menu Declaration
	 */
	private SlideHolder slide_holder;
	private LinearLayout mBuyRentView, mAgentBrokerView, mSellerView;
	private String selectedType;
	private int listing_size;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listing);
		ViewGroup root = (ViewGroup) findViewById(R.id.parent_layout);
		Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(this);
		setFont(root, mTypeface);
		setupUI(root);
		UserID = GlobalMethods.getUserID(this);
		mNotification_bell = (ImageView) findViewById(R.id.notification_bell);
		initComponents();
		setGoogleAnalytics(ListingActivity.this);
		if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
			moveToLogin();
		} else {
			getListing();
		}

	}

	private void initComponents() {
		/**
		 * Listing View Initialization
		 */
		mListingGrid = (GridView) findViewById(R.id.listing_grid);
		mHeaderText = (TextView) findViewById(R.id.header_txt);
		mHeaderLeftIcon = (Button) findViewById(R.id.header_left_icon);
		mAddIcon = (LinearLayout) findViewById(R.id.add_icon);
		mEditIcon = (LinearLayout) findViewById(R.id.edit_icon_lay);
		edit_del_view = (RelativeLayout) findViewById(R.id.edit_delete_view);
		edt_btn = (Button) findViewById(R.id.edt_btn);

		if (AppConstants.from_profile_listing.equals("true")) {
			mHeaderLeftIcon.setBackgroundResource(R.drawable.back_arrow_white);
			mAddIcon.setVisibility(View.INVISIBLE);
			mEditIcon.setVisibility(View.INVISIBLE);

		} else {
			mHeaderLeftIcon.setBackgroundResource(R.drawable.slide_button);
			mAddIcon.setVisibility(View.VISIBLE);
			mEditIcon.setVisibility(View.VISIBLE);
		}
		/**
		 * Slide Menu Intialization
		 */
		slideUserNameComponents();
		agentSlidemenuComponents();
		sellSlidemenuComponents();
		buySlidemenuComponents();

		mBuyRentView = (LinearLayout) findViewById(R.id.buyer_renter_view);
		mSellerView = (LinearLayout) findViewById(R.id.seller_view);
		mAgentBrokerView = (LinearLayout) findViewById(R.id.agent_broker_view);
		selectedType = (String) GlobalMethods.getValueFromPreference(this,
				GlobalMethods.STRING_PREFERENCE, AppConstants.pref_type);
		// mEnhancedProfile = (String)
		// GlobalMethods.getValueFromPreference(this,
		// GlobalMethods.STRING_PREFERENCE, AppConstants.ENHANCED_PROFILE);
		if (selectedType.equalsIgnoreCase(AppConstants.BUYER)
				|| selectedType.equalsIgnoreCase(AppConstants.RENTER)) {
			mBuyRentView.setVisibility(View.VISIBLE);
			setBuyBackground(R.id.agent_listings);
			AppConstants.view_id = R.id.agent_listings;
		} else if (selectedType.equalsIgnoreCase(AppConstants.SELLER)) {
			mSellerView.setVisibility(View.VISIBLE);
			setSellBackground(R.id.agent_listings);
			AppConstants.view_id = R.id.agent_listings;
			mHeaderText.setText(getString(R.string.property_header));
		} else if (selectedType.equalsIgnoreCase(AppConstants.AGENT)
				|| selectedType.equalsIgnoreCase(AppConstants.BROKER)) {
			mAgentBrokerView.setVisibility(View.VISIBLE);
			setAgentBackground(R.id.agent_listings);
			AppConstants.view_id = R.id.agent_listings;
			mHeaderText.setText(getString(R.string.listing_header));
		} else {
			mBuyRentView.setVisibility(View.VISIBLE);
			setBuyBackground(R.id.agent_listings);
			AppConstants.view_id = R.id.agent_listings;
		}

		slide_holder = (SlideHolder) findViewById(R.id.slideHolder);

		slide_holder.setAllowInterceptTouch(false);

		mBundle = getIntent().getExtras();

		if (mBundle != null) {
			UserID = mBundle.getString("ReviewUserID");
		}

		mPropListingList = (LinearLayout) findViewById(R.id.property_listing_list);

		mListingGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String prop_marker = "";
				if (mListingResultList.get(arg2).getRb_block()
						.equalsIgnoreCase("1")) {
					prop_marker = "exclusive";
				} else if (mListingResultList.get(arg2).getOpen_house()
						.equalsIgnoreCase("1")) {
					prop_marker = "openhouse";
				} else if (mListingResultList.get(arg2).getIsfavourite()
						.equalsIgnoreCase("1")) {
					prop_marker = "favourite";
				} else {
					prop_marker = "";
				}
				Intent intent = new Intent(ListingActivity.this,
						PropertyDetailsActivity.class);
				intent.putExtra("PropertyId", mListingResultList.get(arg2)
						.getProperty_id());
				intent.putExtra("PropType", prop_marker);
				intent.putExtra("CallFrom", "Listing");
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

	}

	@Override
	public void onOkclick() {
		if (mAlert.equalsIgnoreCase("CallApi")) {
			getListing();
			mAlert = "";
		} else {
			/**
			 * Close Dialog
			 */
		}
	}

	private void callDeleteAPI() {
		if (mPropIDS.size() != 0 && mPropIDS.size() > 1) {
			mPropID = mPropIDS.toString().replace("[", "").replace("]", "");
		} else {
			mPropID = prop_entity.getProperty_id();
		}

		String Url = AppConstants.BASE_URL + "addnewproperty/delete";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();

		params.put("user_id", UserID);
		params.put("property_id", mPropID);

		aq().transformer(t)
				.progress(DialogManager.getProgressDialog(this))
				.ajax(Url, params, JSONObject.class,
						new AjaxCallback<JSONObject>() {

							@Override
							public void callback(String url, JSONObject json,
									AjaxStatus status) {

								if (json != null) {
									CommonResponse mResponse = new Gson()
											.fromJson(json.toString(),
													CommonResponse.class);
									if (mResponse.getError_code()
											.equalsIgnoreCase(
													AppConstants.SUCCESS_CODE)) {
										mAlert = "CallApi";
										mPropIDS.clear();
										DialogManager.showCustomAlertDialog(
												ListingActivity.this,
												ListingActivity.this,
												mResponse.getMsg());
									}
								} else {
									statusErrorCode(status);
								}

							}
						});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.menu_icon:
			if (AppConstants.from_profile_listing.equals("true")) {
				AppConstants.from_profile_listing = "false";
				finish();
				overridePendingTransition(R.anim.slide_out_right,
						R.anim.slide_in_left);
			} else {
				slide_holder.toggle();
			}
			break;

		case R.id.add_icon:
			if (listing_size == 3 && mEnhancedProfile.equals("0")) {
				DialogManager.showDialogAlert(ListingActivity.this,
						"Enhance your Profile to Add More listing", "Close",
						"Enhance", ProfileScreen.class, R.anim.slide_in_right,
						R.anim.slide_out_left, ProfileScreen.class,
						ListingActivity.class, this);
			} else {
				if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
					moveToLogin();
				} else {
					Intent intent = new Intent(ListingActivity.this,
							AddListingActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
				}
			}
			break;
		case R.id.edit_icon_lay:
			if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
				moveToLogin();
			} else {
				if (!isShown) {
					mListingGrid.setVisibility(View.GONE);
					mPropListingList.setVisibility(View.VISIBLE);
					edit_del_view.setVisibility(View.VISIBLE);
					edt_btn.setBackgroundResource(R.drawable.close_icon);
					isShown = true;
				} else {
					mListingGrid.setVisibility(View.VISIBLE);
					mPropListingList.setVisibility(View.GONE);
					edit_del_view.setVisibility(View.GONE);
					edt_btn.setBackgroundResource(R.drawable.edit_button);
					isShown = false;
				}
			}
			break;
		case R.id.edit_text:
			if (mPropID.equals("") || mPropID == null || mPropIDS.size() == 0) {
				DialogManager.showCustomAlertDialog(this, this,
						"Select any property from list");
			} else if (mPropIDS.size() > 1) {
				DialogManager.showCustomAlertDialog(this, this,
						"Select only one property from list");
			} else {

				mPropIDS.clear();

				// if (mEnhancedProfile.equals("0")) {
				// Intent editIntent = new Intent(ListingActivity.this,
				// AddSingleListingActivity.class);
				// editIntent.putExtra("property_entity", prop_entity);
				// overridePendingTransition(R.anim.slide_in_right,
				// R.anim.slide_out_left);
				// startActivity(editIntent);
				// } else {
				Intent editIntent = new Intent(ListingActivity.this,
						AddListingActivity.class);
				editIntent.putExtra("property_entity", prop_entity);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
				startActivity(editIntent);
				// }

			}
			break;
		case R.id.txtDelete:
			if (mPropID.equals("") || mPropID == null || mPropIDS.size() == 0) {
				DialogManager.showCustomAlertDialog(this, this,
						"Select any property from list");
			} else {
				callDeleteAPI();
			}

			break;
		}

	}

	private void getListing() {

		String Url = AppConstants.BASE_URL + "getmylisting";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);

		aq().transformer(t)
				.progress(DialogManager.getProgressDialog(this))
				.ajax(Url, params, JSONObject.class,
						new AjaxCallback<JSONObject>() {

							@Override
							public void callback(String url, JSONObject json,
									AjaxStatus status) {

								if (json != null) {
									System.out.println(json);
									ListingResponse mResponse = new Gson()
											.fromJson(json.toString(),
													ListingResponse.class);
									if (mResponse.getError_code()
											.equalsIgnoreCase(
													AppConstants.SUCCESS_CODE)) {
										mListingResultList = mResponse
												.getResult();
										setListingAdapter();
									}
								} else {
									statusErrorCode(status);
								}

							}
						});
	}

	private void setListingAdapter() {
		listing_size = mListingResultList.size();
		setEditListingAdapter(mListingResultList);
		mListingAdapter = new ListingAdapter(this, mListingResultList);
		mListingGrid.setAdapter(mListingAdapter);

	}

	private void setEditListingAdapter(
			final ArrayList<PropertyEntity> mListingResultList) {

		mPropListingList.removeAllViews();

		if (mListingResultList != null && mListingResultList.size() > 0) {
			for (int i = 0; i < mListingResultList.size(); i++) {

				LayoutInflater mLayoutInflater = (LayoutInflater) this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View convertView = mLayoutInflater.inflate(
						R.layout.edit_adapter_listing, null);
				AQuery aq1 = new AQuery(this).recycle(convertView);

				TextView mPropertyAmount = (TextView) convertView
						.findViewById(R.id.property_cost);
				TextView mPropertyDetails = (TextView) convertView
						.findViewById(R.id.property_details);
				TextView mPropertyAddress = (TextView) convertView
						.findViewById(R.id.property_location);
				TextView mDate = (TextView) convertView.findViewById(R.id.time);
				TextView mReviews_count = (TextView) convertView
						.findViewById(R.id.property_reviews);
				RatingBar mRating = (RatingBar) convertView
						.findViewById(R.id.review_ratingbar);
				RelativeLayout mMainLay = (RelativeLayout) convertView
						.findViewById(R.id.main_lay);

				final Button mCheckBtn = (Button) convertView
						.findViewById(R.id.check_btn);
				if (mListingResultList.get(i).getProperty_pics().size() != 0) {
					aq1.id(R.id.image)
							.progress(R.id.loading)
							.image(mListingResultList.get(i).getProperty_pics()
									.get(0).getFile(), true, true, 0,
									R.drawable.default_prop_icon, null, 0, 1.0f);
				}
				mPropertyAmount.setText("$"
						+ mListingResultList.get(i).getPrice_range());

				mPropertyDetails.setText(mListingResultList.get(i).getBeds()
						+ " bed " + mListingResultList.get(i).getBaths()
						+ " bath");
				mPropertyAddress
						.setText(mListingResultList.get(i).getAddress());
				if (mListingResultList.get(i).getProperty_rating() != null) {
					mRating.setRating(Float.valueOf(mListingResultList.get(i)
							.getProperty_rating()));
				} else {
					mRating.setRating(0);
				}
				mReviews_count.setText("( " + mListingResultList.get(i)
						.getReview_count() + " )");
				mCheckBtnList.add(mCheckBtn);
				mMainLay.setTag(i);
				mMainLay.setOnClickListener(new OnClickListener() {

					private boolean isFirst = true;

					@Override
					public void onClick(View v) {
						int pos = Integer.parseInt(String.valueOf(v.getTag()));
						ListingActivity.getValuesFromList(mListingResultList
								.get(pos));
						mPropID = mListingResultList.get(pos).getProperty_id();
						if (isFirst) {
							isFirst = false;
							mCheckBtn.setBackgroundResource(R.drawable.tick_on);
							mPropIDS.add(mPropID);
						} else {
							isFirst = true;
							mCheckBtn
									.setBackgroundResource(R.drawable.tick_off);
							mPropIDS.remove(mPropID);
						}

					}
				});

				mPropListingList.addView(convertView);

			}
		}

	}

	public void onUserClick(View v) {
		onMenuUserNameClick(v);
	}

	public void onbuyMenuClick(View v) {
		if (AppConstants.view_id == v.getId()) {
			slide_holder.toggle();
		} else {
			onMenuBuyClick(v);
			slide_holder.toggle();
		}
	}

	public void onSellerMenuClick(View v) {
		if (AppConstants.view_id == v.getId()) {
			slide_holder.toggle();
		} else {
			onMenuSellerclick(v);
			slide_holder.toggle();
		}
	}

	public void onAgentMenuClick(View v) {
		if (AppConstants.view_id == v.getId()) {
			slide_holder.toggle();
		} else {
			onMenuAgentClick(v);
			slide_holder.toggle();
		}
	}

	public static void getValuesFromList(PropertyEntity propertyEntity) {
		prop_entity = propertyEntity;
	}
}
