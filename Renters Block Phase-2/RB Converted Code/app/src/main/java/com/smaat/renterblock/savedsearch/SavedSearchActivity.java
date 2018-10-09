package com.smaat.renterblock.savedsearch;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.renterblock.MapFragmentActivity;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.SavedSearchResult;
import com.smaat.renterblock.model.LoginResponse;
import com.smaat.renterblock.model.SavedSearchResponse;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.ui.SlideHolder;
import com.smaat.renterblock.ui.SplashScreen;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.DialogMangerCallback;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TypefaceSingleton;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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

public class SavedSearchActivity extends BaseActivity implements OnClickListener, DialogMangerCallback {
	/**
	 * Saved Search Declaration
	 */
	private LinearLayout mSavedSearchList;
	private RelativeLayout mEditDeletView;
	private boolean isShown = false;
	static SavedSearchResponseEntity search_ent;
	private ArrayList<SavedSearchResult> mResult;
	private static Context context;
	private static String Filter_name;
	private static String Saved_id, Email_notif, Inquiry, mType;
	private String mOkClick = "";
	private Button mEditBtn;
	private SavedSearchResponseEntity mSavedSearch;
	private SavedSearchResponseEntityTemp mSavedserTemp;
	private ArrayList<Button> mCheckBtnList = new ArrayList<Button>();
	private ArrayList<String> mSavedSearchIDS = new ArrayList<String>();
	private String mSavedID = "";
	/**
	 * Slide Menu Declaration
	 */
	private SlideHolder slide_holder;
	private LinearLayout mBuyRentView, mAgentBrokerView, mSellerView;
	private String selectedType;
	private String mSplitedAmountMin, mSplitedAmountMax;

	private TextView mHeader_txt;

	String beds_val = "";
	String baths_val = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saved_search);
		ViewGroup root = (ViewGroup) findViewById(R.id.parent_layout);
		Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(this);
		setFont(root, mTypeface);
		setupUI(root);
		UserID = GlobalMethods.getUserID(this);
		mNotification_bell = (ImageView) findViewById(R.id.notification_bell);
		context = SavedSearchActivity.this;
		initComponents();
		setGoogleAnalytics(SavedSearchActivity.this);
		if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
			moveToLogin();
		} else {
			getsavedsearch();
		}

	}

	private void initComponents() {

		/**
		 * Saved Search Initialization
		 */
		mSavedSearchList = (LinearLayout) findViewById(R.id.saved_search_list);
		mEditDeletView = (RelativeLayout) findViewById(R.id.edit_delete_view);
		mEditBtn = (Button) findViewById(R.id.edit_btn);

		/**
		 * Slide menu Initialization
		 */

		slideUserNameComponents();
		agentSlidemenuComponents();
		sellSlidemenuComponents();
		buySlidemenuComponents();
		mHeader_txt = (TextView) findViewById(R.id.header_txt);
		mHeader_txt.setTypeface(helvetica_bold);

		mBuyRentView = (LinearLayout) findViewById(R.id.buyer_renter_view);
		mSellerView = (LinearLayout) findViewById(R.id.seller_view);
		mAgentBrokerView = (LinearLayout) findViewById(R.id.agent_broker_view);
		selectedType = (String) GlobalMethods.getValueFromPreference(this, GlobalMethods.STRING_PREFERENCE,
				AppConstants.pref_type);
		if (selectedType.equalsIgnoreCase(AppConstants.BUYER) || selectedType.equalsIgnoreCase(AppConstants.RENTER)) {
			mBuyRentView.setVisibility(View.VISIBLE);
			setBuyBackground(R.id.buy_saved_searches);
			AppConstants.view_id = R.id.buy_saved_searches;
		} else if (selectedType.equalsIgnoreCase(AppConstants.SELLER)) {
			mSellerView.setVisibility(View.VISIBLE);
			setSellBackground(R.id.sell_saved_searches);
			AppConstants.view_id = R.id.sell_saved_searches;
		} else if (selectedType.equalsIgnoreCase(AppConstants.AGENT)
				|| selectedType.equalsIgnoreCase(AppConstants.BROKER)) {
			mAgentBrokerView.setVisibility(View.VISIBLE);
			setAgentBackground(R.id.agent_saved_searches);
			AppConstants.view_id = R.id.agent_saved_searches;
		} else {
			mBuyRentView.setVisibility(View.VISIBLE);
			setBuyBackground(R.id.buy_saved_searches);
			AppConstants.view_id = R.id.buy_saved_searches;
		}

		slide_holder = (SlideHolder) findViewById(R.id.slideHolder);

		slide_holder.setAllowInterceptTouch(false);
	}

	private void getsavedsearch() {

		String Url = AppConstants.BASE_URL + "savesearch/view";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);

		aq().transformer(t).progress(DialogManager.getProgressDialog(this)).ajax(Url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {

						if (json != null) {

							SavedSearchResponse mResponse = new Gson().fromJson(json.toString(),
									SavedSearchResponse.class);

							if (mResponse.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
								mResult = mResponse.getResult();

								AppConstants.IS_EDIT = "false";
								mEditDeletView.setVisibility(View.GONE);
								mEditBtn.setBackgroundResource(R.drawable.edit_button);
								isShown = false;
								setSavedSearchAdapter(mResult);

							}

						} else {
							statusErrorCode(status);
						}

					}
				});

	}

	private void setSavedSearchAdapter(final ArrayList<SavedSearchResult> SavedSearchResultList) {
		mSavedSearchList.removeAllViews();
		if (SavedSearchResultList != null && SavedSearchResultList.size() > 0) {
			for (int i = 0; i < SavedSearchResultList.size(); i++) {
				mSavedSearch = new SavedSearchResponseEntity();

				LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View convertView = inflater.inflate(R.layout.adapter_saved_search, null);

				Typeface mTypefaceBold = TypefaceSingleton.getInstance().getHelveticaBold(this);
				Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(this);

				TextView mNameid = (TextView) convertView.findViewById(R.id.name_txt);
				mNameid.setTypeface(mTypefaceBold);
				TextView mOtherinfo = (TextView) convertView.findViewById(R.id.other_info_txt);
				mOtherinfo.setTypeface(mTypeface);
				RelativeLayout mSavedSearchLay = (RelativeLayout) convertView.findViewById(R.id.saved_search_lay);
				LinearLayout mCheckLay = (LinearLayout) convertView.findViewById(R.id.alerts_check_lay);
				final Button mCheckBtn = (Button) convertView.findViewById(R.id.check_btn);
				mCheckBtnList.add(mCheckBtn);

				if (AppConstants.IS_EDIT.equals("true")) {
					mCheckLay.setVisibility(View.VISIBLE);
					mSavedSearchLay.setClickable(true);
				} else {
					mCheckLay.setVisibility(View.GONE);
					mSavedSearchLay.setClickable(false);
				}

				String jsonStr = SavedSearchResultList.get(i).getFilter_object();
				Gson gson = new Gson();
				try {
					mSavedSearch = gson.fromJson(jsonStr, SavedSearchResponseEntity.class);
				} catch (Exception e) {
					e.printStackTrace();
				}

				mNameid.setText(mSavedSearch.getFilter_name());
				String beds = "";
				String baths = "";
				if (mSavedSearch.getBeds() != null && !mSavedSearch.getBeds().equals("")) {
					beds = mSavedSearch.getBeds();
				} else {
					beds = "Any";
				}
				if (mSavedSearch.getBaths() != null && !mSavedSearch.getBaths().equals("")) {
					baths = mSavedSearch.getBaths();
				} else {
					baths = "Any";
				}
				if (mSavedSearch.getProperty_type().equalsIgnoreCase("rent")) {
					mType = "Rent";
					if (mSavedSearch.getPrice_range_min().equalsIgnoreCase("")
							|| mSavedSearch.getPrice_range_min() == null) {
						mSplitedAmountMin = "$100";
					} else {
						if (mSavedSearch.getPrice_range_min().toString().equalsIgnoreCase("No Min")) {
							mSplitedAmountMin = mSavedSearch.getPrice_range_min();
						} else {
							mSplitedAmountMin = "$" + mSavedSearch.getPrice_range_min();
						}
					}
					if (mSavedSearch.getPrice_range_max().equalsIgnoreCase("")
							|| mSavedSearch.getPrice_range_max() == null) {
						mSplitedAmountMax = "$10,000";
					} else {
						if (mSavedSearch.getPrice_range_max().toString().equalsIgnoreCase("No Max")) {
							mSplitedAmountMax = mSavedSearch.getPrice_range_max();
						} else {
							mSplitedAmountMax = "$" + mSavedSearch.getPrice_range_max();
						}
					}
				} else {
					mType = "Sale";
					if (mSavedSearch.getPrice_range_min().equalsIgnoreCase("")
							|| mSavedSearch.getPrice_range_min() == null) {
						mSplitedAmountMin = "$10,000";
					} else {
						if (mSavedSearch.getPrice_range_min().toString().equalsIgnoreCase("No Min")) {
							mSplitedAmountMin = mSavedSearch.getPrice_range_min();
						} else {
							mSplitedAmountMin = "$" + mSavedSearch.getPrice_range_min();
						}
					}
					if (mSavedSearch.getPrice_range_max().equalsIgnoreCase("")
							|| mSavedSearch.getPrice_range_max() == null) {
						mSplitedAmountMax = "$40,00,000";
					} else {
						if (mSavedSearch.getPrice_range_max().toString().equalsIgnoreCase("No Max")) {
							mSplitedAmountMax = mSavedSearch.getPrice_range_max();
						} else {
							mSplitedAmountMax = "$" + mSavedSearch.getPrice_range_max();
						}

					}
				}

				mSavedSearchLay.setTag(i);
				mSavedSearchLay.setOnClickListener(new OnClickListener() {

					private boolean isFirst = true;

					@Override
					public void onClick(View v) {
						int pos = Integer.parseInt(String.valueOf(v.getTag()));
						if (!isShown) {
							mSavedserTemp = new SavedSearchResponseEntityTemp();
							String jsonStr = SavedSearchResultList.get(pos).getFilter_object();
							// AppConstants.saved_search_json = jsonStr;
							Gson gson = new Gson();
							try {

								mSavedSearch = gson.fromJson(jsonStr, SavedSearchResponseEntity.class);
								mSavedserTemp.setBaths(mSavedSearch.getBaths());
								mSavedserTemp.setBeds(mSavedSearch.getBeds());
								mSavedserTemp.setDays_on_RB(mSavedSearch.getDays_on_RB());
								if (mSavedSearch.getFore_closure().equalsIgnoreCase("")) {
									mSavedserTemp.setFore_closure("0");
								} else {
									mSavedserTemp.setFore_closure(mSavedSearch.getFore_closure());
								}
								mSavedserTemp.setKeywords(mSavedSearch.getKeywords());
								mSavedserTemp.setLot_size(mSavedSearch.getLot_size());
								mSavedserTemp.setMLS(mSavedSearch.getMLS());
								if (mSavedSearch.getNew_construction().equalsIgnoreCase("")) {
									mSavedserTemp.setNew_construction("0");
								} else {
									mSavedserTemp.setNew_construction(mSavedSearch.getNew_construction());
								}

								if (mSavedSearch.getNo_fee().equalsIgnoreCase("")) {
									mSavedserTemp.setNo_fee("0");
								} else {
									mSavedserTemp.setNo_fee(mSavedSearch.getNo_fee());
								}

								if (mSavedSearch.getOpen_house().equalsIgnoreCase("")) {
									mSavedserTemp.setOpen_house("0");
								} else {
									mSavedserTemp.setOpen_house(mSavedSearch.getOpen_house());
								}

								mSavedserTemp.setPrice_range_max(mSavedSearch.getPrice_range_max());
								mSavedserTemp.setPrice_range_min(mSavedSearch.getPrice_range_min());
								mSavedserTemp.setProperty_type("");
								if (mSavedSearch.getReduced_prices().equalsIgnoreCase("")) {
									mSavedserTemp.setReduced_prices("0");
								} else {
									mSavedserTemp.setReduced_prices(mSavedSearch.getReduced_prices());
								}
								if (mSavedSearch.getResale().equalsIgnoreCase("")) {
									mSavedserTemp.setResale("0");
								} else {
									mSavedserTemp.setResale(mSavedSearch.getResale());
								}
								mSavedserTemp.setSold_within(mSavedSearch.getSold_within());
								mSavedserTemp.setSquare_footage_max(mSavedSearch.getSquare_footage_max());
								mSavedserTemp.setSquare_footage_min(mSavedSearch.getSquare_footage_min());
								mSavedserTemp.setYear_build_max(mSavedSearch.getYear_build_max());
								mSavedserTemp.setYear_build_min(mSavedSearch.getYear_build_min());
								Gson gsons = new Gson();
								AppConstants.saved_search_json = gsons.toJson(mSavedserTemp);

							} catch (Exception e) {
								e.printStackTrace();
							}
							AppConstants.saved_search_Latitude = mSavedSearch.getLatitude().toString();
							AppConstants.saved_search_Longitude = mSavedSearch.getLongitude().toString();

							launchActivity(MapFragmentActivity.class);

						} else {
							mSavedID = SavedSearchResultList.get(pos).getSave_search_id();
							if (isFirst) {
								isFirst = false;
								mCheckBtn.setBackgroundResource(R.drawable.tick_on);
								mSavedSearchIDS.add(mSavedID);
							} else {
								isFirst = true;
								mCheckBtn.setBackgroundResource(R.drawable.tick_off);
								mSavedSearchIDS.remove(mSavedID);
							}
							String jsonStr = SavedSearchResultList.get(pos).getFilter_object();
							Gson gson = new Gson();
							try {
								mSavedSearch = gson.fromJson(jsonStr, SavedSearchResponseEntity.class);
							} catch (Exception e) {
								e.printStackTrace();
							}

							String filtername = mSavedSearch.getFilter_name();
							String savesearchid = SavedSearchResultList.get(pos).getSave_search_id();
							String email_notification = SavedSearchResultList.get(pos).getEmail_notification();
							String inquiry = SavedSearchResultList.get(pos).getInquiry();
							String type = SavedSearchResultList.get(pos).getFilter_type();
							SavedSearchActivity.showEditview(filtername, savesearchid, mSavedSearch, email_notification,
									inquiry, type);
							GlobalMethods.storeValuetoPreference(SavedSearchActivity.this,
									GlobalMethods.STRING_PREFERENCE, AppConstants.pref_Save_Search_id, savesearchid);
						}

					}
				});

				if (beds.equals("Any")) {
					beds_val = "Any Beds ";
				} else {
					beds_val = beds + "+ bd ";
				}

				if (baths.equals("Any")) {
					baths_val = "Any Baths";
				} else {
					baths_val = baths + "+ ba";
				}

				mOtherinfo.setText(mType + ": " + mSplitedAmountMin + " - " + mSplitedAmountMax + "\n" + beds_val + "| "
						+ baths_val);

				mSavedSearchList.addView(convertView);
			}
		}
	}

	// private void viewAmountMin(String AmountMin) {
	// System.out.println(NumberFormat.getIntegerInstance().format(
	// Integer.parseInt(AmountMin)));
	// String amount = NumberFormat.getIntegerInstance().format(
	// Integer.parseInt(AmountMin));
	// mSplitedAmountMin = amount.substring(0, amount.lastIndexOf(","));
	// }
	//
	// private void viewAmountMax(String AmountMax) {
	// System.out.println(NumberFormat.getIntegerInstance().format(
	// Integer.parseInt(AmountMax)));
	// String amount = NumberFormat.getIntegerInstance().format(
	// Integer.parseInt(AmountMax));
	// mSplitedAmountMax = amount.substring(0, amount.lastIndexOf(","));
	// }

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.menu_icon:
			slide_holder.toggle();
			break;
		case R.id.edit_button:
			if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
				moveToLogin();
			} else {
				if (mResult.size() == 0) {
					DialogManager.showCustomAlertDialog(this, this, "No search results are available!");
				} else {
					if (!isShown) {
						AppConstants.IS_EDIT = "true";
						mEditDeletView.setVisibility(View.VISIBLE);
						mSavedSearchIDS.clear();
						mEditBtn.setBackgroundResource(R.drawable.close_icon);
						setSavedSearchAdapter(mResult);
						isShown = true;
					} else {
						AppConstants.IS_EDIT = "false";
						mEditDeletView.setVisibility(View.GONE);
						mSavedSearchIDS.clear();
						mEditBtn.setBackgroundResource(R.drawable.edit_button);
						setSavedSearchAdapter(mResult);
						isShown = false;
					}
				}
			}
			break;
		case R.id.edit_text:
			if (mSavedID.equals("") || mSavedID == null || mSavedSearchIDS.size() == 0) {
				DialogManager.showCustomAlertDialog(this, this, "Please select one item to edit.");
			} else if (mSavedSearchIDS.size() > 1) {
				DialogManager.showCustomAlertDialog(this, this, "Please select one item to edit.");
			} else {
				mSavedSearchIDS.clear();
				Intent intent = new Intent(SavedSearchActivity.this, SavedSearchEditView.class);
				intent.putExtra("filter_name", Filter_name);
				intent.putExtra("save_search_id", Saved_id);
				intent.putExtra("Search_object", search_ent);
				intent.putExtra("email_notification", Email_notif);
				intent.putExtra("inquiry", Inquiry);
				intent.putExtra("filter_type", mType);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}

			break;
		case R.id.txtDelete:

			String searchid = (String) GlobalMethods.getValueFromPreference(context, GlobalMethods.STRING_PREFERENCE,
					AppConstants.pref_Save_Search_id);
			if (mSavedID.equals("") || mSavedID == null || mSavedSearchIDS.size() == 0) {
				DialogManager.showCustomAlertDialog(this, this, "Please select one item to delete.");
			} else {
				CallDeleteService(searchid);
			}

			break;
		}
	}

	public static void showEditview(String filter_name, String save_search, SavedSearchResponseEntity mSavedSearchs,
			String email_notification, String inquiry, String type) {
		search_ent = new SavedSearchResponseEntity();
		Filter_name = filter_name;
		Saved_id = save_search;
		search_ent = mSavedSearchs;
		Email_notif = email_notification;
		Inquiry = inquiry;
		mType = type;
	}

	private void CallDeleteService(String searchid) {

		if (mSavedSearchIDS.size() != 0 && mSavedSearchIDS.size() > 1) {
			searchid = mSavedSearchIDS.toString().replace("[", "").replace("]", "");
		}
		String Url = AppConstants.BASE_URL + "savesearch/delete";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		params.put("save_search_id", searchid);

		aq().transformer(t).progress(DialogManager.getProgressDialog(this)).ajax(Url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {

						if (json != null) {
							String newjsonn = json.toString().replace("\\", "");
							System.out.println("Result:::" + newjsonn);
							LoginResponse mResponse = new Gson().fromJson(json.toString(), LoginResponse.class);
							if (mResponse.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
								mOkClick = "CallService";
								mSavedSearchIDS.clear();
								DialogManager.showCustomAlertDialog(SavedSearchActivity.this, SavedSearchActivity.this,
										mResponse.msg);
							}
						} else {
							statusErrorCode(status);
						}
					}

				});

	}

	public static void getLatLng(String lat, String lon, String filtername) {

		Intent intent = new Intent(context, MapFragmentActivity.class);

		intent.putExtra("latitude", lat);
		intent.putExtra("longitude", lon);

		context.startActivity(intent);
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

	@Override
	public void onItemclick(String SelctedItem, int pos) {

	}

	@Override
	public void onOkclick() {
		if (mOkClick.equalsIgnoreCase("CallService")) {
			getsavedsearch();
			mOkClick = "";
		} else {
			/**
			 * Close Dialog
			 */
		}

	}
}
