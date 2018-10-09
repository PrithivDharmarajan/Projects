package com.smaat.renterblock.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.ToggleButton;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.renterblock.MapFragmentActivity;
import com.smaat.renterblock.R;
import com.smaat.renterblock.alerts.AlertsActivity;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.sqlite.DatabaseManager;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.DialogMangerCallback;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TypefaceSingleton;

public class FilterActivity extends BaseActivity implements OnClickListener,
		OnItemSelectedListener, OnCheckedChangeListener, DialogMangerCallback {

	private TextView mForSale, mForRent, mSold, mShowmore, mBedAny, mBedNum0,
			mBedNum1, mBedNum2, mBedNum3, mBedNum4, mBedNum5, mBathAny,
			mBathNum0, mBathNum1, mBathNum2, mBathNum3, mBathNum4, mBathNum5,
			mClearText, mCancel, mApply, mPriceTxt, mPriceTxtSale, mSqauareTxt,
			mYearTxt, mLotTxt, mPropertyTypeTxt, mDaysonrbTxt, mSoldewithTxt;

	private LinearLayout mForSaleLay, mShowLay;
	private int mShow = 1;
	private TableRow mPriceRangeSpinRow, mPriceRangeSpinRowSale,
			mSquareFootageSpinRow, mYearBuiltSpinRow, mLotSizeSpinRow,
			mDaysonRBSpinRow, mSoldwithinSpinRow;
	private Button mPriceRangeBtn, mPriceRangeBtnSale, mSquareFootageBtn,
			mYearBuiltBtn, mLotSizeBtn, mPropertTypeBtn, mDaysonRBBtn,
			mSoldwithinBtn;
	private Dialog mDialog;
	private String mString;
	private int windowWidth;
	private HashMap<String, String> mMap = new LinkedHashMap<String, String>();
	private LinearLayout mKeysAddedView, mBackArrow, mAddIcon;
	private LinearLayout row;
	ArrayList<String> mKeywordList = new ArrayList<String>();
	private EditText mKeywordEdit, mMLSEdit;
	private View mKeyView;
	private RelativeLayout mKeyLay, mMlsLay, mSoldwithinLay, mNoFeeLay,
			mYearbuiltLay, mEmptybackground, mEmptysoldAb, mEmptysoldBe,
			mIncludeestlay;

	private ArrayList<String> mPriceMinList;
	private ArrayList<String> mPriceMaxList;
	// pricelist sale
	private ArrayList<String> mPriceMinListSale;
	private ArrayList<String> mPriceMaxListSale;

	private ArrayList<String> mSquareMinList;
	private ArrayList<String> mSquareMaxList;
	private ArrayList<String> mLotList;
	private ArrayList<String> mDaysonRbList;
	private ArrayList<String> mSoldWithList;
	private ArrayList<String> mYearMinList;
	private ArrayList<String> mYearMaxList;
	private ArrayList<String> mTempListPrice;
	private ArrayList<String> mTempListPriceSale;
	private ArrayList<String> mTempListSquare;
	private ArrayList<String> mTempListYear;
	ArrayList<String> mPropertyTypeList;

	private String mSaleFilterObject = "", mFilterType = "",
			mRentFilterObject = "", mSoldFilterObject = "",
			mFilterObjects = "";
	private Spinner mPriceMinSpin, mPriceMaxSpin, mPriceMinSpinSale,
			mPriceMaxSpinSale, mSquareMinSpin, mSquareMaxSpin, mLotSpin,
			mYearMaxSpin, mYearMinSpin, mDaysonRbSpin, mSoldwithSpin;

	private CheckBox mAllTypes, mSingleFamilyHome, mCondo, mTownhouse, mCoop,
			mApartment, mLoft, mTIC, mApartmentCondo, mMobileManufactured,
			mFarmRanch, mLotLand, mMultiFamily, mIncomeInvestment, mHouseBoat;

	private boolean boolAllTypes = true, boolSingleFamilyHome = true,
			boolCondo = true, boolTownhouse = true, boolCoop = true,
			boolApartment = true, boolLoft = true, boolTIC = true,
			boolApartmentCondo = true, boolMobileManufactured = true,
			boolFarmRanch = true, boolLotLand = true, boolMultiFamily = true,
			boolIncomeInvestment = true, boolHouseBoat = true;

	private ArrayAdapter<String> mPriceMinAdapter;
	private ArrayAdapter<String> mPriceMaxAdapter;
	// for sale
	private ArrayAdapter<String> mPriceMinAdapterSale;
	private ArrayAdapter<String> mPriceMaxAdapterSale;

	private ArrayAdapter<String> mSquareMinAdapter;
	private ArrayAdapter<String> mSquareMaxAdapter;
	private ArrayAdapter<String> mYearMinAdapter;
	private ArrayAdapter<String> mYearMaxAdapter;
	private ArrayAdapter<String> mLotSize;
	private ArrayAdapter<String> mDaysonRbSize;
	private ArrayAdapter<String> mSoldWithinSize;

	private String mPriceMin, mPriceMax, mPriceMinSale, mPriceMaxSale,
			mSquareMin, mSquareMax, mLot, mPropertyType, mDaysonRb,
			mSoldwithins;

	private String mSplitPriceMin = "", mSplitPriceMax = "",
			mSplitPriceMinSale = "", mSplitPriceMaxSale = "",
			mSplitSquareMin = "", mSplitSquareMax = "", mSplitLot = "",
			mSplitDaysrb = "", mYearMin = "", mYearMax = "";

	private ToggleButton mResaleTog, mNewConstuctionTog, mForclousreTog,
			mOpenHousesTog, mReducedPricesTog, mIncludeEstimateTog;

	private Button mNoFeeTog;

	private boolean boolResale = false, boolNewConstrction = false,
			boolForclousre = false, boolOpenHHouses = false,
			boolReducedPrices = false, boolNoFee = false,
			boolIncludeEstimate = false;

	ArrayList<String> price_range = new ArrayList<String>();
	ArrayList<String> prices_range = new ArrayList<String>();

	// for sale
	ArrayList<String> price_range_sale = new ArrayList<String>();
	ArrayList<String> prices_range_sale = new ArrayList<String>();

	private int type = 0;
	private Bundle mBundle;
	private String mCallFrom = "";
	private String mAlertTitile = "", mAlertID = "", mLatitude, mLongitude,
			mLocation, mAlerttxt;
	private String mAlert = "Alert";
	private RelativeLayout mBottomLay;

	private RelativeLayout price_range_lay, price_range_lay_sale;
	private boolean isKey = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_filter_main);
		ViewGroup root = (ViewGroup) findViewById(R.id.parent_layout);
		Typeface mTypefaceBold = TypefaceSingleton.getInstance()
				.getHelveticaBold(this);
		setFont(root, mTypefaceBold);
		setupUI(root);
		mPriceMinList = new ArrayList<String>();
		mPriceMaxList = new ArrayList<String>();
		// for sale
		mPriceMinListSale = new ArrayList<String>();
		mPriceMaxListSale = new ArrayList<String>();

		mSquareMinList = new ArrayList<String>();
		mSquareMaxList = new ArrayList<String>();
		mLotList = new ArrayList<String>();
		mDaysonRbList = new ArrayList<String>();
		mSoldWithList = new ArrayList<String>();
		mYearMinList = new ArrayList<String>();
		mYearMaxList = new ArrayList<String>();
		mTempListPrice = new ArrayList<String>();
		// for sale
		mTempListPriceSale = new ArrayList<String>();
		mTempListSquare = new ArrayList<String>();
		mTempListYear = new ArrayList<String>();
		mPropertyTypeList = new ArrayList<String>();

		UserID = GlobalMethods.getUserID(this);
		initComponents();
	}

	private void initComponents() {

		/**
		 * TextView
		 */
		mForSale = (TextView) findViewById(R.id.for_sale_txt);
		mForRent = (TextView) findViewById(R.id.for_rent_txt);
		mSold = (TextView) findViewById(R.id.sold_txt);
		mShowmore = (TextView) findViewById(R.id.show_more);
		mBedAny = (TextView) findViewById(R.id.bed_any);
		mBedNum0 = (TextView) findViewById(R.id.bed_num0);
		mBedNum1 = (TextView) findViewById(R.id.bed_num1);
		mBedNum2 = (TextView) findViewById(R.id.bed_num2);
		mBedNum3 = (TextView) findViewById(R.id.bed_num3);
		mBedNum4 = (TextView) findViewById(R.id.bed_num4);
		mBedNum5 = (TextView) findViewById(R.id.bed_num5);
		mBathAny = (TextView) findViewById(R.id.baths_any);
		mBathNum0 = (TextView) findViewById(R.id.baths_num0);
		mBathNum1 = (TextView) findViewById(R.id.baths_num1);
		mBathNum2 = (TextView) findViewById(R.id.baths_num2);
		mBathNum3 = (TextView) findViewById(R.id.baths_num3);
		mBathNum4 = (TextView) findViewById(R.id.baths_num4);
		mBathNum5 = (TextView) findViewById(R.id.baths_num5);
		mClearText = (TextView) findViewById(R.id.clear_text);
		mCancel = (TextView) findViewById(R.id.cancel);
		mApply = (TextView) findViewById(R.id.apply);

		price_range_lay = (RelativeLayout) findViewById(R.id.price_range_lay);
		price_range_lay_sale = (RelativeLayout) findViewById(R.id.price_range_lay_sale);

		mBottomLay = (RelativeLayout) findViewById(R.id.bottom_bar);

		mPriceTxt = (TextView) findViewById(R.id.price_range_txt);
		mPriceTxtSale = (TextView) findViewById(R.id.price_range_txt_sale);
		mSqauareTxt = (TextView) findViewById(R.id.square_footage_txt);
		mYearTxt = (TextView) findViewById(R.id.year_built_txt);
		mLotTxt = (TextView) findViewById(R.id.lot_size_txt);
		mDaysonrbTxt = (TextView) findViewById(R.id.days_on_rb_txt);
		mSoldewithTxt = (TextView) findViewById(R.id.sold_within_txt);
		mPropertyTypeTxt = (TextView) findViewById(R.id.property_type_txt);

		/**
		 * Button
		 */
		mPriceRangeBtn = (Button) findViewById(R.id.price_range_btn);
		mPriceRangeBtnSale = (Button) findViewById(R.id.price_range_btn_sale);
		mSquareFootageBtn = (Button) findViewById(R.id.square_footage_btn);
		mYearBuiltBtn = (Button) findViewById(R.id.year_built_btn);
		mLotSizeBtn = (Button) findViewById(R.id.lot_size_btn);
		mDaysonRBBtn = (Button) findViewById(R.id.days_on_rb_btn);
		mSoldwithinBtn = (Button) findViewById(R.id.sold_within_btn);
		mPropertTypeBtn = (Button) findViewById(R.id.property_type_btn);

		/**
		 * Relative Layout
		 */

		mSoldwithinLay = (RelativeLayout) findViewById(R.id.sold_within_size);
		mNoFeeLay = (RelativeLayout) findViewById(R.id.no_fee);
		mYearbuiltLay = (RelativeLayout) findViewById(R.id.year_built);
		mEmptybackground = (RelativeLayout) findViewById(R.id.emp_background);
		mEmptysoldAb = (RelativeLayout) findViewById(R.id.empty_back_sold_above);
		mEmptysoldBe = (RelativeLayout) findViewById(R.id.empty_back_sold_below);
		mIncludeestlay = (RelativeLayout) findViewById(R.id.include_estimate_sold);
		mIncludeestlay.setVisibility(View.GONE);

		/**
		 * Table Row
		 */

		mPriceRangeSpinRow = (TableRow) findViewById(R.id.price_range_spin_row);
		mPriceRangeSpinRowSale = (TableRow) findViewById(R.id.price_range_spin_row_sale);
		mSquareFootageSpinRow = (TableRow) findViewById(R.id.square_footage_spin_row);
		mYearBuiltSpinRow = (TableRow) findViewById(R.id.year_built_spin_row);
		mLotSizeSpinRow = (TableRow) findViewById(R.id.lot_size_spin_row);
		mDaysonRBSpinRow = (TableRow) findViewById(R.id.days_on_rb_spin_row);
		mSoldwithinSpinRow = (TableRow) findViewById(R.id.sold_within_spin_row);

		/**
		 * Linear Layout
		 */
		mForSaleLay = (LinearLayout) findViewById(R.id.filter_for_sale_lay);
		mShowLay = (LinearLayout) findViewById(R.id.show_lay);
		mBackArrow = (LinearLayout) findViewById(R.id.back_arrow);
		mAddIcon = (LinearLayout) findViewById(R.id.add_icon);

		/**
		 * Spinner
		 */
		mPriceMinSpin = (Spinner) findViewById(R.id.price_range_min_spin);
		mPriceMaxSpin = (Spinner) findViewById(R.id.price_range_max_spin);
		// for sale
		mPriceMinSpinSale = (Spinner) findViewById(R.id.price_range_min_spin_sale);
		mPriceMaxSpinSale = (Spinner) findViewById(R.id.price_range_max_spin_sale);

		mSquareMinSpin = (Spinner) findViewById(R.id.square_footage_min_spin);
		mSquareMaxSpin = (Spinner) findViewById(R.id.square_footage_max_spin);
		mLotSpin = (Spinner) findViewById(R.id.lot_size_spin);
		mDaysonRbSpin = (Spinner) findViewById(R.id.days_on_rb_spin);
		mSoldwithSpin = (Spinner) findViewById(R.id.sold_within_spin);
		mYearMinSpin = (Spinner) findViewById(R.id.year_built_min_spin);
		mYearMaxSpin = (Spinner) findViewById(R.id.year_built_max_spin);

		mResaleTog = (ToggleButton) findViewById(R.id.tog_resale);
		mNewConstuctionTog = (ToggleButton) findViewById(R.id.tog_new_construction);
		mForclousreTog = (ToggleButton) findViewById(R.id.tog_fore_closure);
		mOpenHousesTog = (ToggleButton) findViewById(R.id.tog_open_houses);
		mIncludeEstimateTog = (ToggleButton) findViewById(R.id.tog_include_estimates);
		mIncludeEstimateTog.setVisibility(View.GONE);
		mReducedPricesTog = (ToggleButton) findViewById(R.id.tog_reduced_prices);
		mNoFeeTog = (Button) findViewById(R.id.tog_no_fee);

		mPriceMinSpin.setOnItemSelectedListener(this);
		mPriceMaxSpin.setOnItemSelectedListener(this);
		// for sale
		mPriceMinSpinSale.setOnItemSelectedListener(this);
		mPriceMaxSpinSale.setOnItemSelectedListener(this);

		mSquareMinSpin.setOnItemSelectedListener(this);
		mSquareMaxSpin.setOnItemSelectedListener(this);
		mLotSpin.setOnItemSelectedListener(this);
		mDaysonRbSpin.setOnItemSelectedListener(this);
		mSoldwithSpin.setOnItemSelectedListener(this);
		mYearMinSpin.setOnItemSelectedListener(this);
		mYearMaxSpin.setOnItemSelectedListener(this);

		DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();

		windowWidth = (int) displayMetrics.widthPixels;

		mKeyLay = (RelativeLayout) findViewById(R.id.keywords);
		mMlsLay = (RelativeLayout) findViewById(R.id.mls_lay);
		mKeyView = (View) findViewById(R.id.key_view);
		mKeywordEdit = (EditText) findViewById(R.id.keyword_edit);
		mMLSEdit = (EditText) findViewById(R.id.mls_edit);
		mKeysAddedView = (LinearLayout) findViewById(R.id.table_lay);

		mKeyLay.setVisibility(View.GONE);
		mMlsLay.setVisibility(View.GONE);
		mKeyView.setVisibility(View.GONE);
		mKeywordEdit.setVisibility(View.GONE);
		mKeysAddedView.setVisibility(View.GONE);

		mKeywordEdit.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				mString = v.getText().toString();
				if (!mString.trim().equalsIgnoreCase("")) {
					mMap.put(mString, mString);
					setKeyViews(mMap);
				}
				hideSoftKeyboard(FilterActivity.this);
				return true;
			}
		});

		/**
		 * Set setOnClickListener
		 */
		mForSale.setOnClickListener(this);
		mForRent.setOnClickListener(this);
		mSold.setOnClickListener(this);
		mShowmore.setOnClickListener(this);
		mPriceRangeBtn.setOnClickListener(this);
		mPriceRangeBtnSale.setOnClickListener(this);
		mSquareFootageBtn.setOnClickListener(this);
		mYearBuiltBtn.setOnClickListener(this);
		mLotSizeBtn.setOnClickListener(this);
		mDaysonRBBtn.setOnClickListener(this);
		mPropertTypeBtn.setOnClickListener(this);

		mBedAny.setOnClickListener(this);
		mBedNum0.setOnClickListener(this);
		mBedNum1.setOnClickListener(this);
		mBedNum2.setOnClickListener(this);
		mBedNum3.setOnClickListener(this);
		mBedNum4.setOnClickListener(this);
		mBedNum5.setOnClickListener(this);
		mBathAny.setOnClickListener(this);
		mBathNum0.setOnClickListener(this);
		mBathNum1.setOnClickListener(this);
		mBathNum2.setOnClickListener(this);
		mBathNum3.setOnClickListener(this);
		mBathNum4.setOnClickListener(this);
		mBathNum5.setOnClickListener(this);
		mBackArrow.setOnClickListener(this);
		mAddIcon.setOnClickListener(this);
		mClearText.setOnClickListener(this);
		mCancel.setOnClickListener(this);
		mApply.setOnClickListener(this);

		mPriceTxt.setOnClickListener(this);
		mPriceTxtSale.setOnClickListener(this);
		mSqauareTxt.setOnClickListener(this);
		mYearTxt.setOnClickListener(this);
		mLotTxt.setOnClickListener(this);
		mDaysonrbTxt.setOnClickListener(this);
		mSoldewithTxt.setOnClickListener(this);
		mPropertyTypeTxt.setOnClickListener(this);

		mResaleTog.setOnClickListener(this);
		mNewConstuctionTog.setOnClickListener(this);
		mForclousreTog.setOnClickListener(this);
		mOpenHousesTog.setOnClickListener(this);
		mIncludeEstimateTog.setOnClickListener(this);
		mReducedPricesTog.setOnClickListener(this);
		// mNoFeeTog.setOnClickListener(this);

		// mNoFeeTog.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// if (boolNoFee) {
		// boolNoFee = false;
		// mNoFeeTog.setBackgroundResource(R.drawable.toggle_off);
		// } else {
		// boolNoFee = true;
		// mNoFeeTog.setBackgroundResource(R.drawable.toggle_on);
		// }
		// }
		// });

		/**
		 * Set default background
		 */

		// mFilterType = "Sale";
		mShowmore.setVisibility(View.VISIBLE);
		setSpinnerListValues();
		if (AppConstants.type_of_property_filter.equalsIgnoreCase("rent")) {
			rentFilterChanges();
			setHeaderViewBackground(R.id.for_rent_txt);
			price_range_lay.setVisibility(View.VISIBLE);
			price_range_lay_sale.setVisibility(View.GONE);
			if (mPriceTxt.getText().toString()
					.equalsIgnoreCase("$No Min - $No Max")) {
				mPriceTxt.setText("Any");
			}
		} else if (AppConstants.type_of_property_filter
				.equalsIgnoreCase("sale")) {
			saleFilterChanges();
			price_range_lay.setVisibility(View.GONE);
			price_range_lay_sale.setVisibility(View.VISIBLE);
			setHeaderViewBackground(R.id.for_sale_txt);
		} else if (AppConstants.type_of_property_filter
				.equalsIgnoreCase("sold")) {
			soldFilterChanges();
			setHeaderViewBackground(R.id.sold_txt);
		}

		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			mCallFrom = mBundle.getString("FROM");
			mAlertID = mBundle.getString("AlertID");
			mLatitude = mBundle.getString("Latitude");
			mLongitude = mBundle.getString("Longitude");
			mLocation = mBundle.getString("Location");
			mAlerttxt = mBundle.getString("alert_name");

			if (mCallFrom == null) {
				mCallFrom = "";
			}
			if (mAlertID == null) {
				mAlertID = "";
			}
		}

		if (mCallFrom.equalsIgnoreCase("Alerts")) {
			mAddIcon.setVisibility(View.VISIBLE);
			mBottomLay.setVisibility(View.GONE);
		} else {
			mAddIcon.setVisibility(View.INVISIBLE);
			mBottomLay.setVisibility(View.VISIBLE);
		}
	}

	private void setSoldFilterDefaultDetails() {
		if (SplashScreen.mLocaleSoldFilterObjectEntity.getPrice_range_min()
				.equals("")) {
			mPriceMin = "No Min";
		} else {
			mPriceMin = "$"
					+ SplashScreen.mLocaleSoldFilterObjectEntity
							.getPrice_range_min();
		}
		if (SplashScreen.mLocaleSoldFilterObjectEntity.getPrice_range_max()
				.equals("")) {
			mPriceMax = "No Max";
		} else {
			mPriceMax = "$"
					+ SplashScreen.mLocaleSoldFilterObjectEntity
							.getPrice_range_max();
		}

		if (SplashScreen.mLocaleSoldFilterObjectEntity.getSquare_footage_min()
				.equals("")) {
			mSquareMin = "No Min";
		} else {
			mSquareMin = SplashScreen.mLocaleSoldFilterObjectEntity
					.getSquare_footage_min() + "" + "sqft";
		}
		if (SplashScreen.mLocaleSoldFilterObjectEntity.getSquare_footage_max()
				.equals("")) {
			mSquareMax = "No Max";
		} else {
			mSquareMax = SplashScreen.mLocaleSoldFilterObjectEntity
					.getSquare_footage_max() + "" + "sqft";
		}
		if (SplashScreen.mLocaleSoldFilterObjectEntity.getYear_build_min()
				.equals("")) {
			mYearMin = "No Min";
		} else {
			mYearMin = SplashScreen.mLocaleSoldFilterObjectEntity
					.getYear_build_min();
		}

		if (SplashScreen.mLocaleSoldFilterObjectEntity.getYear_build_max()
				.equals("")) {
			mYearMax = "No Min";
		} else {
			mYearMax = SplashScreen.mLocaleSoldFilterObjectEntity
					.getYear_build_max();
		}
		if (SplashScreen.mLocaleSoldFilterObjectEntity.getLot_size().equals("")) {
			mLot = "Any";
		} else {
			mLot = SplashScreen.mLocaleSoldFilterObjectEntity.getLot_size()
					+ "" + " sqft";
		}

		if (SplashScreen.mLocaleSoldFilterObjectEntity.getDays_on_RB().equals(
				"")) {
			mDaysonRb = "Any";
		} else {
			if (SplashScreen.mLocaleSoldFilterObjectEntity.getDays_on_RB()
					.equals("1")) {
				mDaysonRb = SplashScreen.mLocaleSoldFilterObjectEntity
						.getDays_on_RB() + "" + " day";
			} else {
				mDaysonRb = SplashScreen.mLocaleSoldFilterObjectEntity
						.getDays_on_RB() + "" + " days";
			}
		}

		if (SplashScreen.mLocaleSoldFilterObjectEntity.getProperty_type()
				.equals("")) {
			mPropertyType = "All Types";
		} else {
			mPropertyType = SplashScreen.mLocaleSoldFilterObjectEntity
					.getProperty_type();
		}
		if (SplashScreen.mLocaleSoldFilterObjectEntity.getBeds().equals("")) {
			setBedBackground(R.id.bed_any);
			setBedTextColor(R.id.bed_any);
		} else if (SplashScreen.mLocaleSoldFilterObjectEntity.getBeds().equals(
				"0")) {
			setBedBackground(R.id.bed_num0);
			setBedTextColor(R.id.bed_num0);

		} else if (SplashScreen.mLocaleSoldFilterObjectEntity.getBeds().equals(
				"1")) {
			setBedBackground(R.id.bed_num1);
			setBedTextColor(R.id.bed_num1);
		} else if (SplashScreen.mLocaleSoldFilterObjectEntity.getBeds().equals(
				"2")) {
			setBedBackground(R.id.bed_num2);
			setBedTextColor(R.id.bed_num2);
		} else if (SplashScreen.mLocaleSoldFilterObjectEntity.getBeds().equals(
				"3")) {
			setBedBackground(R.id.bed_num3);
			setBedTextColor(R.id.bed_num3);
		} else if (SplashScreen.mLocaleSoldFilterObjectEntity.getBeds().equals(
				"4")) {
			setBedBackground(R.id.bed_num4);
			setBedTextColor(R.id.bed_num4);
		} else if (SplashScreen.mLocaleSoldFilterObjectEntity.getBeds().equals(
				"5")) {
			setBedBackground(R.id.bed_num5);
			setBedTextColor(R.id.bed_num5);
		}
		if (SplashScreen.mLocaleSoldFilterObjectEntity.getBaths().equals("")) {
			setBathBackground(R.id.baths_any);
			setBathTextColor(R.id.baths_any);
		} else if (SplashScreen.mLocaleSoldFilterObjectEntity.getBaths()
				.equals("0")) {
			setBathBackground(R.id.baths_num0);
			setBathTextColor(R.id.baths_num0);

		} else if (SplashScreen.mLocaleSoldFilterObjectEntity.getBaths()
				.equals("1")) {
			setBathBackground(R.id.baths_num1);
			setBathTextColor(R.id.baths_num1);
		} else if (SplashScreen.mLocaleSoldFilterObjectEntity.getBaths()
				.equals("2")) {
			setBathBackground(R.id.baths_num2);
			setBathTextColor(R.id.baths_num2);
		} else if (SplashScreen.mLocaleSoldFilterObjectEntity.getBaths()
				.equals("3")) {
			setBathBackground(R.id.baths_num3);
			setBathTextColor(R.id.baths_num3);
		} else if (SplashScreen.mLocaleSoldFilterObjectEntity.getBaths()
				.equals("4")) {
			setBathBackground(R.id.baths_num4);
			setBathTextColor(R.id.baths_num4);
		} else if (SplashScreen.mLocaleSoldFilterObjectEntity.getBaths()
				.equals("5")) {
			setBathBackground(R.id.baths_num5);
			setBathTextColor(R.id.baths_num5);
		}
		if (SplashScreen.mLocaleSoldFilterObjectEntity.getNo_fee().equals("0")
				|| SplashScreen.mLocaleSoldFilterObjectEntity.getNo_fee()
						.equals("")) {
			boolNoFee = false;
			mNoFeeTog.setBackgroundResource(R.drawable.toggle_off);
		} else {
			boolNoFee = true;
			mNoFeeTog.setBackgroundResource(R.drawable.toggle_on);
		}
		if (SplashScreen.mLocaleSoldFilterObjectEntity.getResale().equals("0")
				|| SplashScreen.mLocaleSoldFilterObjectEntity.getResale()
						.equals("")) {
			boolResale = false;
			mResaleTog.setBackgroundResource(R.drawable.tick_off);
		} else {
			boolResale = true;
			mResaleTog.setBackgroundResource(R.drawable.tick_on);
		}
		if (SplashScreen.mLocaleSoldFilterObjectEntity.getNew_construction()
				.equals("0")
				|| SplashScreen.mLocaleSoldFilterObjectEntity
						.getNew_construction().equals("")) {
			boolNewConstrction = false;
			mNewConstuctionTog.setBackgroundResource(R.drawable.tick_off);
		} else {
			boolNewConstrction = true;
			mNewConstuctionTog.setBackgroundResource(R.drawable.tick_on);
		}
		if (SplashScreen.mLocaleSoldFilterObjectEntity.getFore_closure()
				.equals("0")
				|| SplashScreen.mLocaleSoldFilterObjectEntity.getFore_closure()
						.equals("")) {
			boolForclousre = false;
			mForclousreTog.setBackgroundResource(R.drawable.tick_off);
		} else {
			boolForclousre = true;
			mForclousreTog.setBackgroundResource(R.drawable.tick_on);
		}
		if (SplashScreen.mLocaleSoldFilterObjectEntity.getOpen_house().equals(
				"0")
				|| SplashScreen.mLocaleSoldFilterObjectEntity.getOpen_house()
						.equals("")) {
			boolOpenHHouses = false;
			mOpenHousesTog.setBackgroundResource(R.drawable.tick_off);
		} else {
			boolOpenHHouses = true;
			mOpenHousesTog.setBackgroundResource(R.drawable.tick_on);
		}
		// if (SplashScreen.mLocaleFilterObjectEntity.getInclude_estimate()
		// .equals("0")
		// || SplashScreen.mLocaleFilterObjectEntity.getInclude_estimate()
		// .equals("")) {
		// boolIncludeEstimate = false;
		// mIncludeEstimateTog.setBackgroundResource(R.drawable.tick_off);
		// } else {
		// boolIncludeEstimate = true;
		// mIncludeEstimateTog.setBackgroundResource(R.drawable.tick_on);
		// }
		if (SplashScreen.mLocaleSoldFilterObjectEntity.getReduced_prices()
				.equals("0")
				|| SplashScreen.mLocaleSoldFilterObjectEntity
						.getReduced_prices().equals("")) {
			boolReducedPrices = false;
			mReducedPricesTog.setBackgroundResource(R.drawable.tick_off);
		} else {
			boolReducedPrices = true;
			mReducedPricesTog.setBackgroundResource(R.drawable.tick_on);
		}

		if (SplashScreen.mLocaleSoldFilterObjectEntity.getPrice_range_min()
				.equals("")
				&& SplashScreen.mLocaleSoldFilterObjectEntity
						.getPrice_range_max().equals("")) {
			mPriceTxt.setText("Any");
		} else {
			mPriceTxt.setText(mPriceMin + " - " + mPriceMax);
		}
		if (SplashScreen.mLocaleSoldFilterObjectEntity.getSquare_footage_min()
				.equals("")
				&& SplashScreen.mLocaleSoldFilterObjectEntity
						.getSquare_footage_max().equals("")) {
			mSqauareTxt.setText("Any");
		} else {
			mSqauareTxt.setText(mSquareMin + " - " + mSquareMax);
		}
		if (SplashScreen.mLocaleSoldFilterObjectEntity.getYear_build_min()
				.equals("")
				&& SplashScreen.mLocaleSoldFilterObjectEntity
						.getYear_build_max().equals("")) {
			mYearTxt.setText("Any");
		} else {
			mYearTxt.setText(mYearMax + " - " + mYearMax);
		}
		mLotTxt.setText(mLot);
		mDaysonrbTxt.setText(mDaysonRb);
		mSoldewithTxt.setText(mSoldwithins);
		mPropertyTypeTxt.setText(mPropertyType);
		mMLSEdit.setText(SplashScreen.mLocaleSoldFilterObjectEntity.getMLS());
	}

	private void setRentFilterDefaultDetails() {
		if (SplashScreen.mLocaleRentFilterObjectEntity.getPrice_range_min()
				.equals("")) {
			mPriceMin = "No Min";
			mPriceMinSpin.setSelection(0);
		} else {
			mPriceMin = "$"
					+ SplashScreen.mLocaleRentFilterObjectEntity
							.getPrice_range_min();
			for (int i = 0; i < price_range.size(); i++) {
				String price_txt = price_range.get(i).toString().trim();
				String price_val_db = "$"
						+ SplashScreen.mLocaleRentFilterObjectEntity
								.getPrice_range_min().toString().trim();

				if (price_txt.equalsIgnoreCase(price_val_db)) {
					mPriceMinSpin.setSelection(i);
					break;
				}
			}
		}
		if (SplashScreen.mLocaleRentFilterObjectEntity.getPrice_range_max()
				.equals("")) {
			mPriceMax = "No Max";
			mPriceMaxSpin.setSelection(0);
		} else {
			mPriceMax = "$"
					+ SplashScreen.mLocaleRentFilterObjectEntity
							.getPrice_range_max();
			for (int i = 0; i < prices_range.size(); i++) {
				String price_txt = prices_range.get(i).toString().trim();
				String price_val_db = "$"
						+ SplashScreen.mLocaleRentFilterObjectEntity
								.getPrice_range_max().toString().trim();

				if (price_txt.equalsIgnoreCase(price_val_db)) {
					mPriceMaxSpin.setSelection(i);
					break;
				}
			}
		}

		if (SplashScreen.mLocaleRentFilterObjectEntity.getSquare_footage_min()
				.equals("")) {
			mSquareMin = "No Min";
		} else {
			mSquareMin = SplashScreen.mLocaleRentFilterObjectEntity
					.getSquare_footage_min() + "" + "sqft";
		}
		if (SplashScreen.mLocaleRentFilterObjectEntity.getSquare_footage_max()
				.equals("")) {
			mSquareMax = "No Max";
		} else {
			mSquareMax = SplashScreen.mLocaleRentFilterObjectEntity
					.getSquare_footage_max() + "" + "sqft";
		}
		if (SplashScreen.mLocaleRentFilterObjectEntity.getYear_build_min()
				.equals("")) {
			mYearMin = "No Min";
		} else {
			mYearMin = SplashScreen.mLocaleRentFilterObjectEntity
					.getYear_build_min();
		}

		if (SplashScreen.mLocaleRentFilterObjectEntity.getYear_build_max()
				.equals("")) {
			mYearMax = "No Min";
		} else {
			mYearMax = SplashScreen.mLocaleRentFilterObjectEntity
					.getYear_build_max();
		}
		if (SplashScreen.mLocaleRentFilterObjectEntity.getLot_size().equals("")) {
			mLot = "Any";
		} else {
			mLot = SplashScreen.mLocaleRentFilterObjectEntity.getLot_size()
					+ "" + " sqft";
		}

		if (SplashScreen.mLocaleRentFilterObjectEntity.getDays_on_RB().equals(
				"")) {
			mDaysonRb = "Any";
		} else {
			if (SplashScreen.mLocaleRentFilterObjectEntity.getDays_on_RB()
					.equals("1")) {
				mDaysonRb = SplashScreen.mLocaleRentFilterObjectEntity
						.getDays_on_RB() + "" + " day";
			} else {
				mDaysonRb = SplashScreen.mLocaleRentFilterObjectEntity
						.getDays_on_RB() + "" + " days";
			}
		}

		// if
		// (SplashScreen.mLocaleFilterObjectEntity.getSold_within().equals(""))
		// {
		// mSoldwithins = "Any";
		// } else {
		// mSoldwithins = SplashScreen.mLocaleFilterObjectEntity
		// .getSold_within() + "" + " months";
		// }

		if (SplashScreen.mLocaleRentFilterObjectEntity.getProperty_type()
				.equals("")) {
			mPropertyType = "All Types";
		} else {
			mPropertyType = SplashScreen.mLocaleRentFilterObjectEntity
					.getProperty_type();
		}
		if (SplashScreen.mLocaleRentFilterObjectEntity.getBeds().equals("")) {
			setBedBackground(R.id.bed_any);
			setBedTextColor(R.id.bed_any);
		} else if (SplashScreen.mLocaleRentFilterObjectEntity.getBeds().equals(
				"0")) {
			setBedBackground(R.id.bed_num0);
			setBedTextColor(R.id.bed_num0);

		} else if (SplashScreen.mLocaleRentFilterObjectEntity.getBeds().equals(
				"1")) {
			setBedBackground(R.id.bed_num1);
			setBedTextColor(R.id.bed_num1);
		} else if (SplashScreen.mLocaleRentFilterObjectEntity.getBeds().equals(
				"2")) {
			setBedBackground(R.id.bed_num2);
			setBedTextColor(R.id.bed_num2);
		} else if (SplashScreen.mLocaleRentFilterObjectEntity.getBeds().equals(
				"3")) {
			setBedBackground(R.id.bed_num3);
			setBedTextColor(R.id.bed_num3);
		} else if (SplashScreen.mLocaleRentFilterObjectEntity.getBeds().equals(
				"4")) {
			setBedBackground(R.id.bed_num4);
			setBedTextColor(R.id.bed_num4);
		} else if (SplashScreen.mLocaleRentFilterObjectEntity.getBeds().equals(
				"5")) {
			setBedBackground(R.id.bed_num5);
			setBedTextColor(R.id.bed_num5);
		}
		if (SplashScreen.mLocaleRentFilterObjectEntity.getBaths().equals("")) {
			setBathBackground(R.id.baths_any);
			setBathTextColor(R.id.baths_any);
		} else if (SplashScreen.mLocaleRentFilterObjectEntity.getBaths()
				.equals("0")) {
			setBathBackground(R.id.baths_num0);
			setBathTextColor(R.id.baths_num0);

		} else if (SplashScreen.mLocaleRentFilterObjectEntity.getBaths()
				.equals("1")) {
			setBathBackground(R.id.baths_num1);
			setBathTextColor(R.id.baths_num1);
		} else if (SplashScreen.mLocaleRentFilterObjectEntity.getBaths()
				.equals("2")) {
			setBathBackground(R.id.baths_num2);
			setBathTextColor(R.id.baths_num2);
		} else if (SplashScreen.mLocaleRentFilterObjectEntity.getBaths()
				.equals("3")) {
			setBathBackground(R.id.baths_num3);
			setBathTextColor(R.id.baths_num3);
		} else if (SplashScreen.mLocaleRentFilterObjectEntity.getBaths()
				.equals("4")) {
			setBathBackground(R.id.baths_num4);
			setBathTextColor(R.id.baths_num4);
		} else if (SplashScreen.mLocaleRentFilterObjectEntity.getBaths()
				.equals("5")) {
			setBathBackground(R.id.baths_num5);
			setBathTextColor(R.id.baths_num5);
		}
		if (SplashScreen.mLocaleRentFilterObjectEntity.getNo_fee().equals("0")
				|| SplashScreen.mLocaleRentFilterObjectEntity.getNo_fee()
						.equals("")) {
			boolNoFee = false;
			mNoFeeTog.setBackgroundResource(R.drawable.toggle_off);
		} else {
			boolNoFee = true;
			mNoFeeTog.setBackgroundResource(R.drawable.toggle_on);
		}
		if (SplashScreen.mLocaleRentFilterObjectEntity.getResale().equals("0")
				|| SplashScreen.mLocaleRentFilterObjectEntity.getResale()
						.equals("")) {
			boolResale = false;
			mResaleTog.setBackgroundResource(R.drawable.tick_off);
		} else {
			boolResale = true;
			mResaleTog.setBackgroundResource(R.drawable.tick_on);
		}
		if (SplashScreen.mLocaleRentFilterObjectEntity.getNew_construction()
				.equals("0")
				|| SplashScreen.mLocaleRentFilterObjectEntity
						.getNew_construction().equals("")) {
			boolNewConstrction = false;
			mNewConstuctionTog.setBackgroundResource(R.drawable.tick_off);
		} else {
			boolNewConstrction = true;
			mNewConstuctionTog.setBackgroundResource(R.drawable.tick_on);
		}
		if (SplashScreen.mLocaleRentFilterObjectEntity.getFore_closure()
				.equals("0")
				|| SplashScreen.mLocaleRentFilterObjectEntity.getFore_closure()
						.equals("")) {
			boolForclousre = false;
			mForclousreTog.setBackgroundResource(R.drawable.tick_off);
		} else {
			boolForclousre = true;
			mForclousreTog.setBackgroundResource(R.drawable.tick_on);
		}
		if (SplashScreen.mLocaleRentFilterObjectEntity.getOpen_house().equals(
				"0")
				|| SplashScreen.mLocaleRentFilterObjectEntity.getOpen_house()
						.equals("")) {
			boolOpenHHouses = false;
			mOpenHousesTog.setBackgroundResource(R.drawable.tick_off);
		} else {
			boolOpenHHouses = true;
			mOpenHousesTog.setBackgroundResource(R.drawable.tick_on);
		}
		if (SplashScreen.mLocaleRentFilterObjectEntity.getReduced_prices()
				.equals("0")
				|| SplashScreen.mLocaleRentFilterObjectEntity
						.getReduced_prices().equals("")) {
			boolReducedPrices = false;
			mReducedPricesTog.setBackgroundResource(R.drawable.tick_off);
		} else {
			boolReducedPrices = true;
			mReducedPricesTog.setBackgroundResource(R.drawable.tick_on);
		}

		if (SplashScreen.mLocaleRentFilterObjectEntity.getPrice_range_min()
				.equals("")
				&& SplashScreen.mLocaleRentFilterObjectEntity
						.getPrice_range_max().equals("")) {
			mPriceTxt.setText("Any");
		} else {
			mPriceTxt.setText(mPriceMin + " - " + mPriceMax);
		}
		if (SplashScreen.mLocaleRentFilterObjectEntity.getSquare_footage_min()
				.equals("")
				&& SplashScreen.mLocaleRentFilterObjectEntity
						.getSquare_footage_max().equals("")) {
			mSqauareTxt.setText("Any");
		} else {
			mSqauareTxt.setText(mSquareMin + " - " + mSquareMax);
		}
		if (SplashScreen.mLocaleRentFilterObjectEntity.getYear_build_min()
				.equals("")
				&& SplashScreen.mLocaleRentFilterObjectEntity
						.getYear_build_max().equals("")) {
			mYearTxt.setText("Any");
		} else {
			mYearTxt.setText(mYearMax + " - " + mYearMax);
		}
		mLotTxt.setText(mLot);
		mDaysonrbTxt.setText(mDaysonRb);
		mSoldewithTxt.setText(mSoldwithins);
		mPropertyTypeTxt.setText(mPropertyType);
		mMLSEdit.setText(SplashScreen.mLocaleRentFilterObjectEntity.getMLS());
	}

	private void setSellFilterDefaultDetails() {

		if (SplashScreen.mLocaleSellFilterObjectEntity.getPrice_range_min()
				.equals("")) {
			mPriceMinSale = "No Min";
		} else {
			mPriceMinSale = "$"
					+ SplashScreen.mLocaleSellFilterObjectEntity
							.getPrice_range_min();
			for (int i = 0; i < price_range_sale.size(); i++) {
				String price_txt = price_range_sale.get(i).toString().trim();
				String price_txt_db = "$"
						+ SplashScreen.mLocaleSellFilterObjectEntity
								.getPrice_range_min();
				if (price_txt.equalsIgnoreCase(price_txt_db)) {
					mPriceMinSpinSale.setSelection(i);
					break;
				}
			}
		}
		if (SplashScreen.mLocaleSellFilterObjectEntity.getPrice_range_max()
				.equals("")) {
			mPriceMaxSale = "No Max";
		} else {
			mPriceMaxSale = "$"
					+ SplashScreen.mLocaleSellFilterObjectEntity
							.getPrice_range_max();
			for (int i = 0; i < prices_range_sale.size(); i++) {
				String price_txt = prices_range_sale.get(i).toString().trim();
				String price_txt_db = "$"
						+ SplashScreen.mLocaleSellFilterObjectEntity
								.getPrice_range_max();
				if (price_txt.equalsIgnoreCase(price_txt_db)) {
					mPriceMaxSpinSale.setSelection(i);
					break;
				}
			}
		}

		if (SplashScreen.mLocaleSellFilterObjectEntity.getSquare_footage_min()
				.equals("")) {
			mSquareMin = "No Min";
		} else {
			mSquareMin = SplashScreen.mLocaleSellFilterObjectEntity
					.getSquare_footage_min() + "" + "sqft";
		}
		if (SplashScreen.mLocaleSellFilterObjectEntity.getSquare_footage_max()
				.equals("")) {
			mSquareMax = "No Max";
		} else {
			mSquareMax = SplashScreen.mLocaleSellFilterObjectEntity
					.getSquare_footage_max() + "" + "sqft";
		}
		if (SplashScreen.mLocaleSellFilterObjectEntity.getYear_build_min()
				.equals("")) {
			mYearMin = "No Min";
		} else {
			mYearMin = SplashScreen.mLocaleSellFilterObjectEntity
					.getYear_build_min();
		}

		if (SplashScreen.mLocaleSellFilterObjectEntity.getYear_build_max()
				.equals("")) {
			mYearMax = "No Min";
		} else {
			mYearMax = SplashScreen.mLocaleSellFilterObjectEntity
					.getYear_build_max();
		}
		if (SplashScreen.mLocaleSellFilterObjectEntity.getLot_size().equals("")) {
			mLot = "Any";
		} else {
			mLot = SplashScreen.mLocaleSellFilterObjectEntity.getLot_size()
					+ "" + " sqft";
		}

		if (SplashScreen.mLocaleSellFilterObjectEntity.getDays_on_RB().equals(
				"")) {
			mDaysonRb = "Any";
		} else {
			if (SplashScreen.mLocaleSellFilterObjectEntity.getDays_on_RB()
					.equals("1")) {
				mDaysonRb = SplashScreen.mLocaleSellFilterObjectEntity
						.getDays_on_RB() + "" + " day";
			} else {
				mDaysonRb = SplashScreen.mLocaleSellFilterObjectEntity
						.getDays_on_RB() + "" + " days";
			}
		}

		if (SplashScreen.mLocaleSellFilterObjectEntity.getProperty_type()
				.equals("")) {
			mPropertyType = "All Types";
		} else {
			mPropertyType = SplashScreen.mLocaleSellFilterObjectEntity
					.getProperty_type();
		}
		if (SplashScreen.mLocaleSellFilterObjectEntity.getBeds().equals("")) {
			setBedBackground(R.id.bed_any);
			setBedTextColor(R.id.bed_any);
		} else if (SplashScreen.mLocaleSellFilterObjectEntity.getBeds().equals(
				"0")) {
			setBedBackground(R.id.bed_num0);
			setBedTextColor(R.id.bed_num0);

		} else if (SplashScreen.mLocaleSellFilterObjectEntity.getBeds().equals(
				"1")) {
			setBedBackground(R.id.bed_num1);
			setBedTextColor(R.id.bed_num1);
		} else if (SplashScreen.mLocaleSellFilterObjectEntity.getBeds().equals(
				"2")) {
			setBedBackground(R.id.bed_num2);
			setBedTextColor(R.id.bed_num2);
		} else if (SplashScreen.mLocaleSellFilterObjectEntity.getBeds().equals(
				"3")) {
			setBedBackground(R.id.bed_num3);
			setBedTextColor(R.id.bed_num3);
		} else if (SplashScreen.mLocaleSellFilterObjectEntity.getBeds().equals(
				"4")) {
			setBedBackground(R.id.bed_num4);
			setBedTextColor(R.id.bed_num4);
		} else if (SplashScreen.mLocaleSellFilterObjectEntity.getBeds().equals(
				"5")) {
			setBedBackground(R.id.bed_num5);
			setBedTextColor(R.id.bed_num5);
		}
		if (SplashScreen.mLocaleSellFilterObjectEntity.getBaths().equals("")) {
			setBathBackground(R.id.baths_any);
			setBathTextColor(R.id.baths_any);
		} else if (SplashScreen.mLocaleSellFilterObjectEntity.getBaths()
				.equals("0")) {
			setBathBackground(R.id.baths_num0);
			setBathTextColor(R.id.baths_num0);

		} else if (SplashScreen.mLocaleSellFilterObjectEntity.getBaths()
				.equals("1")) {
			setBathBackground(R.id.baths_num1);
			setBathTextColor(R.id.baths_num1);
		} else if (SplashScreen.mLocaleSellFilterObjectEntity.getBaths()
				.equals("2")) {
			setBathBackground(R.id.baths_num2);
			setBathTextColor(R.id.baths_num2);
		} else if (SplashScreen.mLocaleSellFilterObjectEntity.getBaths()
				.equals("3")) {
			setBathBackground(R.id.baths_num3);
			setBathTextColor(R.id.baths_num3);
		} else if (SplashScreen.mLocaleSellFilterObjectEntity.getBaths()
				.equals("4")) {
			setBathBackground(R.id.baths_num4);
			setBathTextColor(R.id.baths_num4);
		} else if (SplashScreen.mLocaleSellFilterObjectEntity.getBaths()
				.equals("5")) {
			setBathBackground(R.id.baths_num5);
			setBathTextColor(R.id.baths_num5);
		}
		if (SplashScreen.mLocaleSellFilterObjectEntity.getNo_fee().equals("0")
				|| SplashScreen.mLocaleSellFilterObjectEntity.getNo_fee()
						.equals("")) {
			boolNoFee = false;
			mNoFeeTog.setBackgroundResource(R.drawable.toggle_off);
		} else {

			boolNoFee = true;
			mNoFeeTog.setBackgroundResource(R.drawable.toggle_on);
		}
		if (SplashScreen.mLocaleSellFilterObjectEntity.getResale().equals("0")
				|| SplashScreen.mLocaleSellFilterObjectEntity.getResale()
						.equals("")) {

			boolResale = false;
			mResaleTog.setBackgroundResource(R.drawable.tick_off);
		} else {
			boolResale = true;
			mResaleTog.setBackgroundResource(R.drawable.tick_on);
		}
		if (SplashScreen.mLocaleSellFilterObjectEntity.getNew_construction()
				.equals("0")
				|| SplashScreen.mLocaleSellFilterObjectEntity
						.getNew_construction().equals("")) {
			boolNewConstrction = false;
			mNewConstuctionTog.setBackgroundResource(R.drawable.tick_off);
		} else {
			boolNewConstrction = true;
			mNewConstuctionTog.setBackgroundResource(R.drawable.tick_on);
		}
		if (SplashScreen.mLocaleSellFilterObjectEntity.getFore_closure()
				.equals("0")
				|| SplashScreen.mLocaleSellFilterObjectEntity.getFore_closure()
						.equals("")) {

			boolForclousre = false;
			mForclousreTog.setBackgroundResource(R.drawable.tick_off);
		} else {
			boolForclousre = true;
			mForclousreTog.setBackgroundResource(R.drawable.tick_on);
		}
		if (SplashScreen.mLocaleSellFilterObjectEntity.getOpen_house().equals(
				"0")
				|| SplashScreen.mLocaleSellFilterObjectEntity.getOpen_house()
						.equals("")) {

			boolOpenHHouses = false;
			mOpenHousesTog.setBackgroundResource(R.drawable.tick_off);
		} else {
			boolOpenHHouses = true;
			mOpenHousesTog.setBackgroundResource(R.drawable.tick_on);
		}
		// if (SplashScreen.mLocaleFilterObjectEntity.getInclude_estimate()
		// .equals("0")
		// || SplashScreen.mLocaleFilterObjectEntity.getInclude_estimate()
		// .equals("")) {
		// boolIncludeEstimate = false;
		// mIncludeEstimateTog.setBackgroundResource(R.drawable.tick_off);
		// } else {
		// boolIncludeEstimate = true;
		// mIncludeEstimateTog.setBackgroundResource(R.drawable.tick_on);
		// }
		if (SplashScreen.mLocaleSellFilterObjectEntity.getReduced_prices()
				.equals("0")
				|| SplashScreen.mLocaleSellFilterObjectEntity
						.getReduced_prices().equals("")) {

			boolReducedPrices = false;
			mReducedPricesTog.setBackgroundResource(R.drawable.tick_off);
		} else {
			boolReducedPrices = true;
			mReducedPricesTog.setBackgroundResource(R.drawable.tick_on);
		}

		if (SplashScreen.mLocaleSellFilterObjectEntity.getPrice_range_min()
				.equals("")
				&& SplashScreen.mLocaleSellFilterObjectEntity
						.getPrice_range_max().equals("")) {
			mPriceTxtSale.setText("Any");
		} else {
			mPriceTxtSale.setText(mPriceMinSale + " - " + mPriceMaxSale);
		}
		if (SplashScreen.mLocaleSellFilterObjectEntity.getSquare_footage_min()
				.equals("")
				&& SplashScreen.mLocaleSellFilterObjectEntity
						.getSquare_footage_max().equals("")) {
			mSqauareTxt.setText("Any");
		} else {
			mSqauareTxt.setText(mSquareMin + " - " + mSquareMax);
		}
		if (SplashScreen.mLocaleSellFilterObjectEntity.getYear_build_min()
				.equals("")
				&& SplashScreen.mLocaleSellFilterObjectEntity
						.getYear_build_max().equals("")) {
			mYearTxt.setText("Any");
		} else {
			mYearTxt.setText(mYearMax + " - " + mYearMax);
		}
		mLotTxt.setText(mLot);
		mDaysonrbTxt.setText(mDaysonRb);
		mSoldewithTxt.setText(mSoldwithins);
		mPropertyTypeTxt.setText(mPropertyType);
		mMLSEdit.setText(SplashScreen.mLocaleSellFilterObjectEntity.getMLS());
	}

	private void setKeyViews(final HashMap<String, String> mMap) {

		mKeysAddedView.removeAllViews();
		int currentRowsWidth = 0;

		LayoutInflater mLayoutInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View convertView = null;

		row = new LinearLayout(this);
		row.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		row.setOrientation(LinearLayout.HORIZONTAL);
		mKeywordList.clear();

		for (Map.Entry<String, String> item : mMap.entrySet()) {

			if (mKeywordEdit.getText().toString() != null
					&& !mKeywordEdit.getText().toString().equals("")) {

				convertView = mLayoutInflater.inflate(R.layout.key_lay, null);
				TextView mTextView = (TextView) convertView
						.findViewById(R.id.key_txt);

				View view = (View) convertView.findViewById(R.id.view);
				convertView.setTag(item.getKey());
				convertView.setClickable(true);
				mTextView.setText(item.getValue());
				mKeywordList.add(item.getValue());

				for (int i = 0; i < mKeywordList.size(); i++) {
					if (type == 0) {
						SplashScreen.mLocaleSellFilterObjectEntity
								.setKeywords(mKeywordList.toString());
					} else if (type == 1) {
						SplashScreen.mLocaleRentFilterObjectEntity
								.setKeywords(mKeywordList.toString());
					} else if (type == 2) {
						SplashScreen.mLocaleSoldFilterObjectEntity
								.setKeywords(mKeywordList.toString());
					}
				}

				mTextView.measure(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				view.measure(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);

				int width = mTextView.getMeasuredWidth();
				int viewWidth = view.getMeasuredWidth();

				int TotalWidth = width + viewWidth + 35;

				if (currentRowsWidth + TotalWidth < windowWidth) {
					currentRowsWidth += TotalWidth;
					row.addView(convertView);
				} else {
					mKeysAddedView.addView(row);
					row = new LinearLayout(this);
					row.addView(convertView);
					currentRowsWidth = TotalWidth;

				}

			} else {

			}

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					mMap.remove(v.getTag());
					row.removeView(v);
					setKeyViews(mMap);
				}

			});
		}
		if (row.getChildCount() > 0)
			mKeysAddedView.addView(row);

		if (!mKeywordEdit.requestFocus()) {
			mKeywordEdit.requestFocus();
		}

		mKeywordEdit.setText("");
		if (mMap.size() == 0) {
			mClearText.setVisibility(View.INVISIBLE);
		} else {
			mClearText.setVisibility(View.VISIBLE);
		}

	}

	private void addSellPriceRangeList() {
		price_range_sale.clear();
		prices_range_sale.clear();
		price_range_sale.add("No Min");
		price_range_sale.add("$10,000");
		price_range_sale.add("$20,000");
		price_range_sale.add("$30,000");
		price_range_sale.add("$50,000");
		price_range_sale.add("$1,00,000");
		price_range_sale.add("$1,30,000");
		price_range_sale.add("$1,50,000");
		price_range_sale.add("$2,00,000");
		price_range_sale.add("$2,50,000");
		price_range_sale.add("$3,00,000");
		price_range_sale.add("$3,50,000");
		price_range_sale.add("$4,00,000");
		price_range_sale.add("$4,50,000");
		price_range_sale.add("$5,00,000");
		price_range_sale.add("$5,50,000");
		price_range_sale.add("$6,00,000");
		price_range_sale.add("$6,50,000");
		price_range_sale.add("$7,00,000");
		price_range_sale.add("$7,50,000");
		price_range_sale.add("$8,00,000");
		price_range_sale.add("$8,50,000");
		price_range_sale.add("$9,00,000");
		price_range_sale.add("$9,50,000");
		price_range_sale.add("$10,00,000");
		price_range_sale.add("$11,00,000");
		price_range_sale.add("$12,00,000");
		price_range_sale.add("$12,50,000");
		price_range_sale.add("$14,00,000");
		price_range_sale.add("$15,00,000");
		price_range_sale.add("$16,00,000");
		price_range_sale.add("$17,00,000");
		price_range_sale.add("$17,50,000");
		price_range_sale.add("$18,00,000");
		price_range_sale.add("$19,00,000");
		price_range_sale.add("$20,00,000");
		price_range_sale.add("$22,50,000");
		price_range_sale.add("$25,00,000");
		price_range_sale.add("$27,50,000");
		price_range_sale.add("$30,00,000");
		price_range_sale.add("$35,00,000");
		price_range_sale.add("$40,00,000");

		prices_range_sale.add("No Max");
		prices_range_sale.add("$10,000");
		prices_range_sale.add("$20,000");
		prices_range_sale.add("$30,000");
		prices_range_sale.add("$50,000");
		prices_range_sale.add("$1,00,000");
		prices_range_sale.add("$1,30,000");
		prices_range_sale.add("$1,50,000");
		prices_range_sale.add("$2,00,000");
		prices_range_sale.add("$2,50,000");
		prices_range_sale.add("$3,00,000");
		prices_range_sale.add("$3,50,000");
		prices_range_sale.add("$4,00,000");
		prices_range_sale.add("$4,50,000");
		prices_range_sale.add("$5,00,000");
		prices_range_sale.add("$5,50,000");
		prices_range_sale.add("$6,00,000");
		prices_range_sale.add("$6,50,000");
		prices_range_sale.add("$7,00,000");
		prices_range_sale.add("$7,50,000");
		prices_range_sale.add("$8,00,000");
		prices_range_sale.add("$8,50,000");
		prices_range_sale.add("$9,00,000");
		prices_range_sale.add("$9,50,000");
		prices_range_sale.add("$10,00,000");
		prices_range_sale.add("$11,00,000");
		prices_range_sale.add("$12,00,000");
		prices_range_sale.add("$12,50,000");
		prices_range_sale.add("$14,00,000");
		prices_range_sale.add("$15,00,000");
		prices_range_sale.add("$16,00,000");
		prices_range_sale.add("$17,00,000");
		prices_range_sale.add("$17,50,000");
		prices_range_sale.add("$18,00,000");
		prices_range_sale.add("$19,00,000");
		prices_range_sale.add("$20,00,000");
		prices_range_sale.add("$22,50,000");
		prices_range_sale.add("$25,00,000");
		prices_range_sale.add("$27,50,000");
		prices_range_sale.add("$30,00,000");
		prices_range_sale.add("$35,00,000");
		prices_range_sale.add("$40,00,000");

		mPriceMinListSale = price_range_sale;
		mPriceMaxListSale = prices_range_sale;
	}

	private void addRentPriceRangeList() {
		price_range.clear();
		prices_range.clear();

		for (int i = 3; i < 101; i++) {
			if (i == 3) {
				price_range.add("No Min");
				prices_range.add("No Max");
			} else {
				price_range.add("$" + String.valueOf(100 * i));
				prices_range.add("$" + String.valueOf(100 * i));
			}
		}

		mPriceMinList = price_range;
		mPriceMaxList = prices_range;
	}

	private void setSpinnerListValues() {
		addSellPriceRangeList();
		addRentPriceRangeList();
		// mPriceMinList = price_range;
		// mPriceMaxList = prices_range;

		for (int i = 0; i < 41; i++) {
			if (i == 0) {
				mSquareMinList.add("No Min");
				mSquareMaxList.add("No Max");
				// mLotList.add("Any");
			} else {
				mSquareMinList.add(String.valueOf(400 * i) + " " + "sqft");
				mSquareMaxList.add(String.valueOf(400 * i) + " " + "sqft");
				// mLotList.add(String.valueOf(400 * i) + " " + "sqft");
			}
		}

		for (int i = 0; i < 116; i++) {
			if (i == 0) {
				mYearMinList.add("No Min");
				mYearMaxList.add("No Max");
			} else {
				mYearMinList.add(String.valueOf(1900 + i));
				mYearMaxList.add(String.valueOf(1900 + i));
			}
		}

		mDaysonRbList.add("Any");
		mDaysonRbList.add("1 day");
		mDaysonRbList.add("7 days");
		mDaysonRbList.add("14 days");
		mDaysonRbList.add("30 days");
		mDaysonRbList.add("90 days");

		mSoldWithList.add("Any");
		mSoldWithList.add("3 months");
		mSoldWithList.add("6 months");
		mSoldWithList.add("9 months");

		mLotList.add("Any");
		mLotList.add("2,000+ sqft");
		mLotList.add("3,000+ sqft");
		mLotList.add("4,000+ sqft");
		mLotList.add("5,000+ sqft");
		mLotList.add("7,500+ sqft");
		mLotList.add("1/4+ acre");
		mLotList.add("1/2+ acre");
		mLotList.add("1+ acre");
		mLotList.add("2+ acre");
		mLotList.add("5+ acre");
		mLotList.add("10+ acre");

		setSpinnerAdapterValues();
	}

	private void setSpinnerAdapterValues() {

		mPriceMinAdapter = new ArrayAdapter<String>(this,
				R.layout.spinner_item_lay, R.id.spinner_item, mPriceMinList);
		mPriceMinSpin.setAdapter(mPriceMinAdapter);
		mPriceMinSpin.setSelection(0);

		mPriceMaxAdapter = new ArrayAdapter<String>(this,
				R.layout.spinner_item_lay, R.id.spinner_item, mPriceMaxList);
		mPriceMaxSpin.setAdapter(mPriceMaxAdapter);
		mPriceMaxSpin.setSelection(0);

		mPriceMinAdapterSale = new ArrayAdapter<String>(this,
				R.layout.spinner_item_lay, R.id.spinner_item, mPriceMinListSale);
		mPriceMinSpinSale.setAdapter(mPriceMinAdapterSale);
		mPriceMinSpinSale.setSelection(0);

		mPriceMaxAdapterSale = new ArrayAdapter<String>(this,
				R.layout.spinner_item_lay, R.id.spinner_item, mPriceMaxListSale);
		mPriceMaxSpinSale.setAdapter(mPriceMaxAdapterSale);
		mPriceMaxSpinSale.setSelection(0);

		mSquareMinAdapter = new ArrayAdapter<String>(this,
				R.layout.spinner_item_lay, R.id.spinner_item, mSquareMinList);
		mSquareMinSpin.setAdapter(mSquareMinAdapter);
		mSquareMinSpin.setSelection(0);

		mSquareMaxAdapter = new ArrayAdapter<String>(this,
				R.layout.spinner_item_lay, R.id.spinner_item, mSquareMaxList);
		mSquareMaxSpin.setAdapter(mSquareMaxAdapter);
		mSquareMaxSpin.setSelection(0);

		mYearMinAdapter = new ArrayAdapter<String>(this,
				R.layout.spinner_item_lay, R.id.spinner_item, mYearMinList);
		mYearMinSpin.setAdapter(mYearMinAdapter);
		mYearMinSpin.setSelection(115);

		mYearMaxAdapter = new ArrayAdapter<String>(this,
				R.layout.spinner_item_lay, R.id.spinner_item, mYearMaxList);
		mYearMaxSpin.setAdapter(mYearMaxAdapter);
		mYearMaxSpin.setSelection(0);

		mLotSize = new ArrayAdapter<String>(this, R.layout.spinner_item_lay,
				R.id.spinner_item, mLotList);
		mLotSpin.setAdapter(mLotSize);
		mLotSpin.setSelection(0);

		mDaysonRbSize = new ArrayAdapter<String>(this,
				R.layout.spinner_item_lay, R.id.spinner_item, mDaysonRbList);
		mDaysonRbSpin.setAdapter(mDaysonRbSize);
		mDaysonRbSpin.setSelection(0);

		mSoldWithinSize = new ArrayAdapter<String>(this,
				R.layout.spinner_item_lay, R.id.spinner_item, mSoldWithList);
		mSoldwithSpin.setAdapter(mSoldWithinSize);
		mSoldwithSpin.setSelection(0);
	}

	private void saleFilterChanges() {
		type = 0;
		mFilterType = "Sale";
		if (mShow == 0) {
			mForSaleLay.setVisibility(View.VISIBLE);
			mMlsLay.setVisibility(View.VISIBLE);
			mIncludeestlay.setVisibility(View.GONE);
			mEmptysoldAb.setVisibility(View.VISIBLE);
			mEmptysoldBe.setVisibility(View.GONE);

			mSoldwithinLay.setVisibility(View.GONE);
			mNoFeeLay.setVisibility(View.VISIBLE);
			mYearbuiltLay.setVisibility(View.VISIBLE);
			mEmptybackground.setVisibility(View.GONE);
			mShowLay.setVisibility(View.VISIBLE);
		} else {
			mShow = 1;
		}
		setHeaderViewBackground(R.id.for_sale_txt);
		addSellPriceRangeList();
		setSellFilterDefaultDetails();
	}

	private void rentFilterChanges() {
		type = 1;
		mFilterType = "Rent";
		if (mShow == 0) {
			mForSaleLay.setVisibility(View.GONE);
			mMlsLay.setVisibility(View.GONE);
			mIncludeestlay.setVisibility(View.GONE);
			mEmptysoldAb.setVisibility(View.VISIBLE);
			mEmptysoldBe.setVisibility(View.GONE);

			mSoldwithinLay.setVisibility(View.GONE);
			mNoFeeLay.setVisibility(View.GONE);
			mYearbuiltLay.setVisibility(View.GONE);
			mEmptybackground.setVisibility(View.GONE);
			mShowLay.setVisibility(View.GONE);
		} else {
			mShow = 2;
		}
		addRentPriceRangeList();
		setHeaderViewBackground(R.id.for_rent_txt);
		setRentFilterDefaultDetails();
	}

	private void soldFilterChanges() {
		type = 2;
		mFilterType = "Sold";
		if (mShow == 0) {
			mForSaleLay.setVisibility(View.GONE);
			mMlsLay.setVisibility(View.GONE);
			mIncludeestlay.setVisibility(View.GONE);
			mEmptysoldAb.setVisibility(View.VISIBLE);
			mEmptysoldBe.setVisibility(View.GONE);
			mSoldwithinLay.setVisibility(View.VISIBLE);
			mNoFeeLay.setVisibility(View.GONE);
			mYearbuiltLay.setVisibility(View.GONE);
			mEmptybackground.setVisibility(View.VISIBLE);
			mShowLay.setVisibility(View.VISIBLE);
		} else {
			mShow = 3;
		}
		addSellPriceRangeList();
		setHeaderViewBackground(R.id.sold_txt);
		setSoldFilterDefaultDetails();
	}

	private void updateRentfilterdb() {
		SQLiteDatabase db;
		db = DatabaseManager.getInstance().openDatabase();

		String query = "UPDATE local_rent_property SET price_range_min='"
				+ SplashScreen.mLocaleRentFilterObjectEntity
						.getPrice_range_min()
				+ "',price_range_max='"
				+ SplashScreen.mLocaleRentFilterObjectEntity
						.getPrice_range_max()
				+ "',property_type='"
				+ SplashScreen.mLocaleRentFilterObjectEntity.getProperty_type()
				+ "',beds='"
				+ SplashScreen.mLocaleRentFilterObjectEntity.getBeds()
				+ "',baths='"
				+ SplashScreen.mLocaleRentFilterObjectEntity.getBaths()
				+ "',no_fee='"
				+ SplashScreen.mLocaleRentFilterObjectEntity.getNo_fee()
				+ "',square_footage_min='"
				+ SplashScreen.mLocaleRentFilterObjectEntity
						.getSquare_footage_min()
				+ "',year_build_max='"
				+ SplashScreen.mLocaleRentFilterObjectEntity
						.getYear_build_max()
				+ "',lot_size='"
				+ SplashScreen.mLocaleRentFilterObjectEntity.getLot_size()
				+ "',days_on_RB='"
				+ SplashScreen.mLocaleRentFilterObjectEntity.getDays_on_RB()
				+ "',resale='"
				+ SplashScreen.mLocaleRentFilterObjectEntity.getResale()
				+ "',new_construction='"
				+ SplashScreen.mLocaleRentFilterObjectEntity
						.getNew_construction()
				+ "',fore_closure='"
				+ SplashScreen.mLocaleRentFilterObjectEntity.getFore_closure()
				+ "',open_house='"
				+ SplashScreen.mLocaleRentFilterObjectEntity.getOpen_house()
				+ "',reduced_prices='"
				+ SplashScreen.mLocaleRentFilterObjectEntity
						.getReduced_prices()
				+ "',keywords='"
				+ SplashScreen.mLocaleRentFilterObjectEntity.getKeywords()
				+ "',MLS='"
				+ SplashScreen.mLocaleRentFilterObjectEntity.getMLS()
				+ "',sold_within='"
				+ SplashScreen.mLocaleRentFilterObjectEntity.getSold_within()
				+ "',square_footage_max='"
				+ SplashScreen.mLocaleRentFilterObjectEntity
						.getSquare_footage_max()
				+ "',year_build_min='"
				+ SplashScreen.mLocaleRentFilterObjectEntity
						.getYear_build_min() + "' WHERE user_id=" + UserID;

		try {
			db.execSQL(query);
			System.out.println("Rent DB " + query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		DatabaseManager.getInstance().closeDatabase();

	}

	private void updateSalefilterdb() {

		SQLiteDatabase db;
		db = DatabaseManager.getInstance().openDatabase();

		String query = "UPDATE local_sell_property SET price_range_min='"
				+ SplashScreen.mLocaleSellFilterObjectEntity
						.getPrice_range_min()
				+ "',price_range_max='"
				+ SplashScreen.mLocaleSellFilterObjectEntity
						.getPrice_range_max()
				+ "',property_type='"
				+ SplashScreen.mLocaleSellFilterObjectEntity.getProperty_type()
				+ "',beds='"
				+ SplashScreen.mLocaleSellFilterObjectEntity.getBeds()
				+ "',baths='"
				+ SplashScreen.mLocaleSellFilterObjectEntity.getBaths()
				+ "',no_fee='"
				+ SplashScreen.mLocaleSellFilterObjectEntity.getNo_fee()
				+ "',square_footage_min='"
				+ SplashScreen.mLocaleSellFilterObjectEntity
						.getSquare_footage_min()
				+ "',year_build_max='"
				+ SplashScreen.mLocaleSellFilterObjectEntity
						.getYear_build_max()
				+ "',lot_size='"
				+ SplashScreen.mLocaleSellFilterObjectEntity.getLot_size()
				+ "',days_on_RB='"
				+ SplashScreen.mLocaleSellFilterObjectEntity.getDays_on_RB()
				+ "',resale='"
				+ SplashScreen.mLocaleSellFilterObjectEntity.getResale()
				+ "',new_construction='"
				+ SplashScreen.mLocaleSellFilterObjectEntity
						.getNew_construction()
				+ "',fore_closure='"
				+ SplashScreen.mLocaleSellFilterObjectEntity.getFore_closure()
				+ "',open_house='"
				+ SplashScreen.mLocaleSellFilterObjectEntity.getOpen_house()
				+ "',reduced_prices='"
				+ SplashScreen.mLocaleSellFilterObjectEntity
						.getReduced_prices()
				+ "',keywords='"
				+ SplashScreen.mLocaleSellFilterObjectEntity.getKeywords()
				+ "',MLS='"
				+ SplashScreen.mLocaleSellFilterObjectEntity.getMLS()
				+ "',sold_within='"
				+ SplashScreen.mLocaleSellFilterObjectEntity.getSold_within()
				+ "',square_footage_max='"
				+ SplashScreen.mLocaleSellFilterObjectEntity
						.getSquare_footage_max()
				+ "',year_build_min='"
				+ SplashScreen.mLocaleSellFilterObjectEntity
						.getYear_build_min() + "' WHERE user_id=" + UserID;

		try {
			db.execSQL(query);
			System.out.println("Sale DB " + query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		DatabaseManager.getInstance().closeDatabase();

	}

	private void updateSoldfilterdb() {

		SQLiteDatabase db;
		db = DatabaseManager.getInstance().openDatabase();

		String query = "UPDATE local_sold_property SET price_range_min='"
				+ SplashScreen.mLocaleSoldFilterObjectEntity
						.getPrice_range_min()
				+ "',price_range_max='"
				+ SplashScreen.mLocaleSoldFilterObjectEntity
						.getPrice_range_max()
				+ "',property_type='"
				+ SplashScreen.mLocaleSoldFilterObjectEntity.getProperty_type()
				+ "',beds='"
				+ SplashScreen.mLocaleSoldFilterObjectEntity.getBeds()
				+ "',baths='"
				+ SplashScreen.mLocaleSoldFilterObjectEntity.getBaths()
				+ "',no_fee='"
				+ SplashScreen.mLocaleSoldFilterObjectEntity.getNo_fee()
				+ "',square_footage_min='"
				+ SplashScreen.mLocaleSoldFilterObjectEntity
						.getSquare_footage_min()
				+ "',year_build_max='"
				+ SplashScreen.mLocaleSoldFilterObjectEntity
						.getYear_build_max()
				+ "',lot_size='"
				+ SplashScreen.mLocaleSoldFilterObjectEntity.getLot_size()
				+ "',days_on_RB='"
				+ SplashScreen.mLocaleSoldFilterObjectEntity.getDays_on_RB()
				+ "',resale='"
				+ SplashScreen.mLocaleSoldFilterObjectEntity.getResale()
				+ "',new_construction='"
				+ SplashScreen.mLocaleSoldFilterObjectEntity
						.getNew_construction()
				+ "',fore_closure='"
				+ SplashScreen.mLocaleSoldFilterObjectEntity.getFore_closure()
				+ "',open_house='"
				+ SplashScreen.mLocaleSoldFilterObjectEntity.getOpen_house()
				+ "',reduced_prices='"
				+ SplashScreen.mLocaleSoldFilterObjectEntity
						.getReduced_prices()
				+ "',keywords='"
				+ SplashScreen.mLocaleSoldFilterObjectEntity.getKeywords()
				+ "',MLS='"
				+ SplashScreen.mLocaleSoldFilterObjectEntity.getMLS()
				+ "',sold_within='"
				+ SplashScreen.mLocaleSoldFilterObjectEntity.getSold_within()
				+ "',square_footage_max='"
				+ SplashScreen.mLocaleSoldFilterObjectEntity
						.getSquare_footage_max()
				+ "',year_build_min='"
				+ SplashScreen.mLocaleSoldFilterObjectEntity
						.getYear_build_min() + "' WHERE user_id=" + UserID;

		try {
			db.execSQL(query);
			System.out.println("Sold DB " + query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		DatabaseManager.getInstance().closeDatabase();

	}

	private void resetRentFilters() {
		SplashScreen.mLocaleRentFilterObjectEntity.setPrice_range_min("");
		SplashScreen.mLocaleRentFilterObjectEntity.setPrice_range_max("");
		SplashScreen.mLocaleRentFilterObjectEntity.setProperty_type("");
		SplashScreen.mLocaleRentFilterObjectEntity.setBeds("");
		SplashScreen.mLocaleRentFilterObjectEntity.setBaths("");
		SplashScreen.mLocaleRentFilterObjectEntity.setNo_fee("0");
		SplashScreen.mLocaleRentFilterObjectEntity.setSquare_footage_min("");
		SplashScreen.mLocaleRentFilterObjectEntity.setSquare_footage_max("");
		SplashScreen.mLocaleRentFilterObjectEntity.setYear_build_min("");
		SplashScreen.mLocaleRentFilterObjectEntity.setYear_build_max("");
		SplashScreen.mLocaleRentFilterObjectEntity.setLot_size("");
		SplashScreen.mLocaleRentFilterObjectEntity.setDays_on_RB("");
		SplashScreen.mLocaleRentFilterObjectEntity.setResale("0");
		SplashScreen.mLocaleRentFilterObjectEntity.setNew_construction("0");
		SplashScreen.mLocaleRentFilterObjectEntity.setFore_closure("0");
		SplashScreen.mLocaleRentFilterObjectEntity.setOpen_house("0");
		SplashScreen.mLocaleRentFilterObjectEntity.setReduced_prices("0");
		SplashScreen.mLocaleRentFilterObjectEntity.setKeywords("");
		SplashScreen.mLocaleRentFilterObjectEntity.setMLS("");
		SplashScreen.mLocaleRentFilterObjectEntity.setSold_within("");

		mPriceTxt.setText("Any");
		mPropertyTypeTxt.setText("All Types");
		setBedBackground(R.id.bed_any);
		setBedTextColor(R.id.bed_any);
		setBathBackground(R.id.baths_any);
		setBathTextColor(R.id.baths_any);
		mSqauareTxt.setText("Any");
		mMap.clear();
		setKeyViews(mMap);
		mPriceMaxSpin.setSelection(0);
		mPriceMinSpin.setSelection(0);
		mSquareMaxSpin.setSelection(0);
		mSquareMinSpin.setSelection(0);

	}

	private void resetSaleFilters() {
		SplashScreen.mLocaleSellFilterObjectEntity.setPrice_range_min("");
		SplashScreen.mLocaleSellFilterObjectEntity.setPrice_range_max("");
		SplashScreen.mLocaleSellFilterObjectEntity.setProperty_type("");
		SplashScreen.mLocaleSellFilterObjectEntity.setBeds("");
		SplashScreen.mLocaleSellFilterObjectEntity.setBaths("");
		SplashScreen.mLocaleSellFilterObjectEntity.setNo_fee("0");
		SplashScreen.mLocaleSellFilterObjectEntity.setSquare_footage_min("");
		SplashScreen.mLocaleSellFilterObjectEntity.setSquare_footage_max("");
		SplashScreen.mLocaleSellFilterObjectEntity.setYear_build_min("");
		SplashScreen.mLocaleSellFilterObjectEntity.setYear_build_max("");
		SplashScreen.mLocaleSellFilterObjectEntity.setLot_size("");
		SplashScreen.mLocaleSellFilterObjectEntity.setDays_on_RB("");
		SplashScreen.mLocaleSellFilterObjectEntity.setResale("0");
		SplashScreen.mLocaleSellFilterObjectEntity.setNew_construction("0");
		SplashScreen.mLocaleSellFilterObjectEntity.setFore_closure("0");
		SplashScreen.mLocaleSellFilterObjectEntity.setOpen_house("0");
		SplashScreen.mLocaleSellFilterObjectEntity.setReduced_prices("0");
		SplashScreen.mLocaleSellFilterObjectEntity.setKeywords("");
		SplashScreen.mLocaleSellFilterObjectEntity.setMLS("");
		SplashScreen.mLocaleSellFilterObjectEntity.setSold_within("");

		mPriceTxtSale.setText("Any");
		mPropertyTypeTxt.setText("All Types");
		setBedBackground(R.id.bed_any);
		setBedTextColor(R.id.bed_any);
		setBathBackground(R.id.baths_any);
		setBathTextColor(R.id.baths_any);
		mSqauareTxt.setText("Any");
		mMap.clear();
		setKeyViews(mMap);
		mPriceMaxSpinSale.setSelection(0);
		mPriceMinSpinSale.setSelection(0);
		mSquareMaxSpin.setSelection(0);
		mSquareMinSpin.setSelection(0);
		mYearTxt.setText("Any");
		mLotTxt.setText("Any");
		mDaysonrbTxt.setText("Any");
		mYearMinSpin.setSelection(115);
		mYearMaxSpin.setSelection(0);
		mMLSEdit.setText("");
		mMLSEdit.setHint("ID");
		boolNoFee = false;
		mNoFeeTog.setBackgroundResource(R.drawable.toggle_off);
		boolResale = false;
		mResaleTog.setBackgroundResource(R.drawable.tick_off);
		boolNewConstrction = false;
		mNewConstuctionTog.setBackgroundResource(R.drawable.tick_off);
		boolForclousre = false;
		mForclousreTog.setBackgroundResource(R.drawable.tick_off);
		boolOpenHHouses = false;
		mOpenHousesTog.setBackgroundResource(R.drawable.tick_off);
		boolReducedPrices = false;
		mReducedPricesTog.setBackgroundResource(R.drawable.tick_off);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.add_icon:
			showAlertPopup();
			break;
		case R.id.reset_filters:
			if (AppConstants.type_of_property_filter.equalsIgnoreCase("rent")) {
				resetRentFilters();
			} else {
				resetSaleFilters();
			}
			break;
		case R.id.cancel:
			launchActivity(MapFragmentActivity.class);
			overridePendingTransition(R.anim.slide_out_right,
					R.anim.slide_in_left);
			break;
		case R.id.apply:
			updateRentfilterdb();
			updateSalefilterdb();
			updateSoldfilterdb();
			callFilterApply();
			break;
		case R.id.clear_text:

			mMap.clear();
			setKeyViews(mMap);

			break;
		case R.id.back_arrow:
			if (mCallFrom != null && mCallFrom.equalsIgnoreCase("Alerts")) {
				launchActivity(AlertsActivity.class);
				overridePendingTransition(R.anim.slide_out_right,
						R.anim.slide_in_left);
			} else {
				launchActivity(MapFragmentActivity.class);
				overridePendingTransition(R.anim.slide_out_right,
						R.anim.slide_in_left);
			}

			break;
		case R.id.for_sale_txt:
			AppConstants.type_of_property_filter = "sale";
			price_range_lay.setVisibility(View.GONE);
			price_range_lay_sale.setVisibility(View.VISIBLE);
			 mPriceRangeSpinRow.setVisibility(View.GONE);
			 mPriceRangeSpinRowSale.setVisibility(View.GONE);
			saleFilterChanges();
			// mShowmore.setVisibility(View.VISIBLE);
			break;
		case R.id.for_rent_txt:
			AppConstants.type_of_property_filter = "rent";
			price_range_lay.setVisibility(View.VISIBLE);
			price_range_lay_sale.setVisibility(View.GONE);
			 mPriceRangeSpinRow.setVisibility(View.GONE);
			 mPriceRangeSpinRowSale.setVisibility(View.GONE);
			rentFilterChanges();
			// mShowmore.setVisibility(View.GONE);
			break;
		case R.id.sold_txt:
			soldFilterChanges();
			// mShowmore.setVisibility(View.VISIBLE);
			break;
		case R.id.show_more:
			mKeyLay.setVisibility(View.VISIBLE);
			mKeyView.setVisibility(View.VISIBLE);
			mKeywordEdit.setVisibility(View.VISIBLE);
			mKeysAddedView.setVisibility(View.VISIBLE);
			mShowLay.setVisibility(View.VISIBLE);
			mShowmore.setVisibility(View.GONE);
			if (mShow == 1) {
				mForSaleLay.setVisibility(View.VISIBLE);
				mMlsLay.setVisibility(View.VISIBLE);
				mIncludeestlay.setVisibility(View.GONE);
				mEmptysoldAb.setVisibility(View.VISIBLE);
				mEmptysoldBe.setVisibility(View.GONE);
				mSoldwithinLay.setVisibility(View.GONE);
				mNoFeeLay.setVisibility(View.VISIBLE);
				mYearbuiltLay.setVisibility(View.VISIBLE);
				mEmptybackground.setVisibility(View.GONE);
				mShowLay.setVisibility(View.VISIBLE);
			} else if (mShow == 2) {
				mForSaleLay.setVisibility(View.GONE);
				mMlsLay.setVisibility(View.GONE);
				mIncludeestlay.setVisibility(View.GONE);
				mEmptysoldAb.setVisibility(View.VISIBLE);
				mEmptysoldBe.setVisibility(View.GONE);

				mSoldwithinLay.setVisibility(View.GONE);
				mNoFeeLay.setVisibility(View.GONE);
				mYearbuiltLay.setVisibility(View.GONE);
				mEmptybackground.setVisibility(View.GONE);
				mShowLay.setVisibility(View.GONE);
			} else if (mShow == 3) {
				mForSaleLay.setVisibility(View.GONE);
				mMlsLay.setVisibility(View.GONE);
				mIncludeestlay.setVisibility(View.GONE);
				mEmptysoldAb.setVisibility(View.VISIBLE);
				mEmptysoldBe.setVisibility(View.GONE);

				mSoldwithinLay.setVisibility(View.VISIBLE);
				mNoFeeLay.setVisibility(View.GONE);
				mYearbuiltLay.setVisibility(View.GONE);
				mEmptybackground.setVisibility(View.VISIBLE);
				mShowLay.setVisibility(View.VISIBLE);
			}
			mShow = 0;
			break;

		case R.id.price_range_txt:
			if (mPriceRangeSpinRow.getVisibility() == View.GONE) {
				mPriceRangeSpinRow.setVisibility(View.VISIBLE);
				mPriceRangeBtn.setBackgroundResource(R.drawable.up_arrow);
			} else {
				mPriceRangeSpinRow.setVisibility(View.GONE);
				mPriceRangeBtn.setBackgroundResource(R.drawable.down_arrow);
			}
			// if (mPriceTxt.getText().toString()
			// .equalsIgnoreCase("No Min - No Max")) {
			// mPriceTxt.setText("Any");
			// }
			break;

		case R.id.price_range_btn:

			if (mPriceRangeSpinRow.getVisibility() == View.GONE) {
				mPriceRangeSpinRow.setVisibility(View.VISIBLE);
				mPriceRangeBtn.setBackgroundResource(R.drawable.up_arrow);
			} else {
				mPriceRangeSpinRow.setVisibility(View.GONE);
				mPriceRangeBtn.setBackgroundResource(R.drawable.down_arrow);
			}
			// if (mPriceTxt.getText().toString()
			// .equalsIgnoreCase("No Min - No Max")) {
			// mPriceTxt.setText("Any");
			// }
			break;

		// price range sale
		case R.id.price_range_txt_sale:
			if (mPriceRangeSpinRowSale.getVisibility() == View.GONE) {
				mPriceRangeSpinRowSale.setVisibility(View.VISIBLE);
				mPriceRangeBtnSale.setBackgroundResource(R.drawable.up_arrow);
			} else {
				mPriceRangeSpinRowSale.setVisibility(View.GONE);
				mPriceRangeBtnSale.setBackgroundResource(R.drawable.down_arrow);
			}
			if (mPriceTxtSale.getText().toString()
					.equalsIgnoreCase("No Min - No Max")) {
				mPriceTxtSale.setText("Any");
			}
			break;

		case R.id.price_range_btn_sale:

			if (mPriceRangeSpinRowSale.getVisibility() == View.GONE) {
				mPriceRangeSpinRowSale.setVisibility(View.VISIBLE);
				mPriceRangeBtnSale.setBackgroundResource(R.drawable.up_arrow);
			} else {
				mPriceRangeSpinRowSale.setVisibility(View.GONE);
				mPriceRangeBtnSale.setBackgroundResource(R.drawable.down_arrow);
			}
			if (mPriceTxtSale.getText().toString()
					.equalsIgnoreCase("No Min - No Max")) {
				mPriceTxtSale.setText("Any");
			}
			break;

		case R.id.square_footage_txt:
			if (mSquareFootageSpinRow.getVisibility() == View.GONE) {
				mSquareFootageSpinRow.setVisibility(View.VISIBLE);
				mSquareFootageBtn.setBackgroundResource(R.drawable.up_arrow);
			} else {
				mSquareFootageSpinRow.setVisibility(View.GONE);
				mSquareFootageBtn.setBackgroundResource(R.drawable.down_arrow);
			}
			if (mSqauareTxt.getText().toString()
					.equalsIgnoreCase("No Min - No Max")) {
				mSqauareTxt.setText("Any");
			}
			break;

		case R.id.square_footage_btn:

			if (mSquareFootageSpinRow.getVisibility() == View.GONE) {
				mSquareFootageSpinRow.setVisibility(View.VISIBLE);
				mSquareFootageBtn.setBackgroundResource(R.drawable.up_arrow);
			} else {
				mSquareFootageSpinRow.setVisibility(View.GONE);
				mSquareFootageBtn.setBackgroundResource(R.drawable.down_arrow);
			}
			if (mSqauareTxt.getText().toString()
					.equalsIgnoreCase("No Min - No Max")) {
				mSqauareTxt.setText("Any");
			}
			break;
		case R.id.year_built_txt:
			if (mYearBuiltSpinRow.getVisibility() == View.GONE) {
				mYearBuiltSpinRow.setVisibility(View.VISIBLE);
				mYearBuiltBtn.setBackgroundResource(R.drawable.up_arrow);
			} else {
				mYearBuiltSpinRow.setVisibility(View.GONE);
				mYearBuiltBtn.setBackgroundResource(R.drawable.down_arrow);
			}
			if (mYearTxt.getText().toString()
					.equalsIgnoreCase("No Min - No Max")) {
				mYearTxt.setText("Any");
			}
			break;

		case R.id.year_built_btn:

			if (mYearBuiltSpinRow.getVisibility() == View.GONE) {
				mYearBuiltSpinRow.setVisibility(View.VISIBLE);
				mYearBuiltBtn.setBackgroundResource(R.drawable.up_arrow);
			} else {
				mYearBuiltSpinRow.setVisibility(View.GONE);
				mYearBuiltBtn.setBackgroundResource(R.drawable.down_arrow);
			}
			if (mYearTxt.getText().toString()
					.equalsIgnoreCase("No Min - No Max")) {
				mYearTxt.setText("Any");
			}
			break;

		case R.id.lot_size_txt:
			if (mLotSizeSpinRow.getVisibility() == View.GONE) {
				mLotSizeSpinRow.setVisibility(View.VISIBLE);
				mLotSizeBtn.setBackgroundResource(R.drawable.up_arrow);
			} else {
				mLotSizeSpinRow.setVisibility(View.GONE);
				mLotSizeBtn.setBackgroundResource(R.drawable.down_arrow);
			}
			break;
		case R.id.lot_size_btn:

			if (mLotSizeSpinRow.getVisibility() == View.GONE) {
				mLotSizeSpinRow.setVisibility(View.VISIBLE);
				mLotSizeBtn.setBackgroundResource(R.drawable.up_arrow);
			} else {
				mLotSizeSpinRow.setVisibility(View.GONE);
				mLotSizeBtn.setBackgroundResource(R.drawable.down_arrow);
			}
			break;

		case R.id.sold_within_txt:
			if (mSoldwithinSpinRow.getVisibility() == View.GONE) {
				mSoldwithinSpinRow.setVisibility(View.VISIBLE);
				mSoldwithinBtn.setBackgroundResource(R.drawable.up_arrow);
			} else {
				mSoldwithinSpinRow.setVisibility(View.GONE);
				mSoldwithinBtn.setBackgroundResource(R.drawable.down_arrow);
			}
			break;
		case R.id.sold_within_btn:
			if (mSoldwithinSpinRow.getVisibility() == View.GONE) {
				mSoldwithinSpinRow.setVisibility(View.VISIBLE);
				mSoldwithinBtn.setBackgroundResource(R.drawable.up_arrow);
			} else {
				mSoldwithinSpinRow.setVisibility(View.GONE);
				mSoldwithinBtn.setBackgroundResource(R.drawable.down_arrow);
			}
			break;

		case R.id.days_on_rb_txt:
			if (mDaysonRBSpinRow.getVisibility() == View.GONE) {
				mDaysonRBSpinRow.setVisibility(View.VISIBLE);
				mDaysonRBBtn.setBackgroundResource(R.drawable.up_arrow);
			} else {
				mDaysonRBSpinRow.setVisibility(View.GONE);
				mDaysonRBBtn.setBackgroundResource(R.drawable.down_arrow);
			}
			if (mDaysonrbTxt.getText().toString()
					.equalsIgnoreCase("No Min - No Max")) {
				mDaysonrbTxt.setText("Any");
			}

			break;
		case R.id.days_on_rb_btn:
			if (mDaysonRBSpinRow.getVisibility() == View.GONE) {
				mDaysonRBSpinRow.setVisibility(View.VISIBLE);
				mDaysonRBBtn.setBackgroundResource(R.drawable.up_arrow);
			} else {
				mDaysonRBSpinRow.setVisibility(View.GONE);
				mDaysonRBBtn.setBackgroundResource(R.drawable.down_arrow);
			}
			if (mDaysonrbTxt.getText().toString()
					.equalsIgnoreCase("No Min - No Max")) {
				mDaysonrbTxt.setText("Any");
			}
			break;

		case R.id.property_type_txt:
			showPropertyPopup(this);
			break;
		case R.id.property_type_btn:
			showPropertyPopup(this);
			break;

		case R.id.bed_any:
			if (type == 0) {
				SplashScreen.mLocaleSellFilterObjectEntity.setBeds("");
			} else if (type == 1) {
				SplashScreen.mLocaleRentFilterObjectEntity.setBeds("");
			} else if (type == 2) {
				SplashScreen.mLocaleSoldFilterObjectEntity.setBeds("");
			}
			setBedBackground(v.getId());
			setBedTextColor(v.getId());
			break;
		case R.id.bed_num0:
			if (type == 0) {
				SplashScreen.mLocaleSellFilterObjectEntity.setBeds("0");
			} else if (type == 1) {
				SplashScreen.mLocaleRentFilterObjectEntity.setBeds("0");
			} else if (type == 2) {
				SplashScreen.mLocaleSoldFilterObjectEntity.setBeds("0");
			}
			setBedBackground(v.getId());
			setBedTextColor(v.getId());
			break;
		case R.id.bed_num1:
			if (type == 0) {
				SplashScreen.mLocaleSellFilterObjectEntity.setBeds("1");
			} else if (type == 1) {
				SplashScreen.mLocaleRentFilterObjectEntity.setBeds("1");
			} else if (type == 2) {
				SplashScreen.mLocaleSoldFilterObjectEntity.setBeds("1");
			}
			setBedBackground(v.getId());
			setBedTextColor(v.getId());
			break;
		case R.id.bed_num2:
			if (type == 0) {
				SplashScreen.mLocaleSellFilterObjectEntity.setBeds("2");
			} else if (type == 1) {
				SplashScreen.mLocaleRentFilterObjectEntity.setBeds("2");
			} else if (type == 2) {
				SplashScreen.mLocaleSoldFilterObjectEntity.setBeds("2");
			}
			setBedBackground(v.getId());
			setBedTextColor(v.getId());
			break;
		case R.id.bed_num3:
			if (type == 0) {
				SplashScreen.mLocaleSellFilterObjectEntity.setBeds("3");
			} else if (type == 1) {
				SplashScreen.mLocaleRentFilterObjectEntity.setBeds("3");
			} else if (type == 2) {
				SplashScreen.mLocaleSoldFilterObjectEntity.setBeds("3");
			}
			setBedBackground(v.getId());
			setBedTextColor(v.getId());
			break;
		case R.id.bed_num4:
			if (type == 0) {
				SplashScreen.mLocaleSellFilterObjectEntity.setBeds("4");
			} else if (type == 1) {
				SplashScreen.mLocaleRentFilterObjectEntity.setBeds("4");
			} else if (type == 2) {
				SplashScreen.mLocaleSoldFilterObjectEntity.setBeds("4");
			}
			setBedBackground(v.getId());
			setBedTextColor(v.getId());
			break;
		case R.id.bed_num5:
			if (type == 0) {
				SplashScreen.mLocaleSellFilterObjectEntity.setBeds("5");
			} else if (type == 1) {
				SplashScreen.mLocaleRentFilterObjectEntity.setBeds("5");
			} else if (type == 2) {
				SplashScreen.mLocaleSoldFilterObjectEntity.setBeds("5");
			}
			setBedBackground(v.getId());
			setBedTextColor(v.getId());
			break;
		case R.id.baths_any:
			if (type == 0) {
				SplashScreen.mLocaleSellFilterObjectEntity.setBaths("");
			} else if (type == 1) {
				SplashScreen.mLocaleRentFilterObjectEntity.setBaths("");
			} else if (type == 2) {
				SplashScreen.mLocaleSoldFilterObjectEntity.setBaths("");
			}
			setBathBackground(v.getId());
			setBathTextColor(v.getId());
			break;
		case R.id.baths_num0:
			if (type == 0) {
				SplashScreen.mLocaleSellFilterObjectEntity.setBaths("0");
			} else if (type == 1) {
				SplashScreen.mLocaleRentFilterObjectEntity.setBaths("0");
			} else if (type == 2) {
				SplashScreen.mLocaleSoldFilterObjectEntity.setBaths("0");
			}
			setBathBackground(v.getId());
			setBathTextColor(v.getId());
			break;
		case R.id.baths_num1:
			if (type == 0) {
				SplashScreen.mLocaleSellFilterObjectEntity.setBaths("1");
			} else if (type == 1) {
				SplashScreen.mLocaleRentFilterObjectEntity.setBaths("1");
			} else if (type == 2) {
				SplashScreen.mLocaleSoldFilterObjectEntity.setBaths("1");
			}
			setBathBackground(v.getId());
			setBathTextColor(v.getId());
			break;
		case R.id.baths_num2:
			if (type == 0) {
				SplashScreen.mLocaleSellFilterObjectEntity.setBaths("2");
			} else if (type == 1) {
				SplashScreen.mLocaleRentFilterObjectEntity.setBaths("2");
			} else if (type == 2) {
				SplashScreen.mLocaleSoldFilterObjectEntity.setBaths("2");
			}
			setBathBackground(v.getId());
			setBathTextColor(v.getId());
			break;
		case R.id.baths_num3:
			if (type == 0) {
				SplashScreen.mLocaleSellFilterObjectEntity.setBaths("3");
			} else if (type == 1) {
				SplashScreen.mLocaleRentFilterObjectEntity.setBaths("3");
			} else if (type == 2) {
				SplashScreen.mLocaleSoldFilterObjectEntity.setBaths("3");
			}
			setBathBackground(v.getId());
			setBathTextColor(v.getId());
			break;
		case R.id.baths_num4:
			if (type == 0) {
				SplashScreen.mLocaleSellFilterObjectEntity.setBaths("4");
			} else if (type == 1) {
				SplashScreen.mLocaleRentFilterObjectEntity.setBaths("4");
			} else if (type == 2) {
				SplashScreen.mLocaleSoldFilterObjectEntity.setBaths("4");
			}
			setBathBackground(v.getId());
			setBathTextColor(v.getId());
			break;
		case R.id.baths_num5:
			if (type == 0) {
				SplashScreen.mLocaleSellFilterObjectEntity.setBaths("5");
			} else if (type == 1) {
				SplashScreen.mLocaleRentFilterObjectEntity.setBaths("5");
			} else if (type == 2) {
				SplashScreen.mLocaleSoldFilterObjectEntity.setBaths("5");
			}
			setBathBackground(v.getId());
			setBathTextColor(v.getId());
			break;

		case R.id.tog_resale:
			if (!boolResale) {
				boolResale = true;
				if (type == 0) {
					SplashScreen.mLocaleSellFilterObjectEntity.setResale("1");
				} else if (type == 1) {
					SplashScreen.mLocaleRentFilterObjectEntity.setResale("1");
				} else if (type == 2) {
					SplashScreen.mLocaleSoldFilterObjectEntity.setResale("1");
				}
				// SplashScreen.mLocaleSellFilterObjectEntity.setResale("1");
				mResaleTog.setBackgroundResource(R.drawable.tick_on);
			} else {
				boolResale = false;
				if (type == 0) {
					SplashScreen.mLocaleSellFilterObjectEntity.setResale("0");
				} else if (type == 1) {
					SplashScreen.mLocaleRentFilterObjectEntity.setResale("0");
				} else if (type == 2) {
					SplashScreen.mLocaleSoldFilterObjectEntity.setResale("0");
				}
				// SplashScreen.mLocaleSellFilterObjectEntity.setResale("0");
				mResaleTog.setBackgroundResource(R.drawable.tick_off);
			}
			break;
		case R.id.tog_new_construction:
			if (!boolNewConstrction) {
				boolNewConstrction = true;
				if (type == 0) {
					SplashScreen.mLocaleSellFilterObjectEntity
							.setNew_construction("1");
				} else if (type == 1) {
					SplashScreen.mLocaleRentFilterObjectEntity
							.setNew_construction("1");
				} else if (type == 2) {
					SplashScreen.mLocaleSoldFilterObjectEntity
							.setNew_construction("1");
				}
				// SplashScreen.mLocaleSellFilterObjectEntity
				// .setNew_construction("1");
				mNewConstuctionTog.setBackgroundResource(R.drawable.tick_on);
			} else {
				boolNewConstrction = false;
				if (type == 0) {
					SplashScreen.mLocaleSellFilterObjectEntity
							.setNew_construction("0");
				} else if (type == 1) {
					SplashScreen.mLocaleRentFilterObjectEntity
							.setNew_construction("0");
				} else if (type == 2) {
					SplashScreen.mLocaleSoldFilterObjectEntity
							.setNew_construction("0");
				}
				// SplashScreen.mLocaleSellFilterObjectEntity
				// .setNew_construction("0");
				mNewConstuctionTog.setBackgroundResource(R.drawable.tick_off);
			}
			break;
		case R.id.tog_fore_closure:
			if (!boolForclousre) {
				boolForclousre = true;
				if (type == 0) {
					SplashScreen.mLocaleSellFilterObjectEntity
							.setFore_closure("1");
				} else if (type == 1) {
					SplashScreen.mLocaleRentFilterObjectEntity
							.setFore_closure("1");
				} else if (type == 2) {
					SplashScreen.mLocaleSoldFilterObjectEntity
							.setFore_closure("1");
				}
				// SplashScreen.mLocaleSellFilterObjectEntity.setFore_closure("1");
				mForclousreTog.setBackgroundResource(R.drawable.tick_on);
			} else {
				boolForclousre = false;
				if (type == 0) {
					SplashScreen.mLocaleSellFilterObjectEntity
							.setFore_closure("0");
				} else if (type == 1) {
					SplashScreen.mLocaleRentFilterObjectEntity
							.setFore_closure("0");
				} else if (type == 2) {
					SplashScreen.mLocaleSoldFilterObjectEntity
							.setFore_closure("0");
				}
				// SplashScreen.mLocaleSellFilterObjectEntity.setFore_closure("0");
				mForclousreTog.setBackgroundResource(R.drawable.tick_off);
			}
			break;
		case R.id.tog_open_houses:
			if (!boolOpenHHouses) {
				boolOpenHHouses = true;
				if (type == 0) {
					SplashScreen.mLocaleSellFilterObjectEntity
							.setOpen_house("1");
				} else if (type == 1) {
					SplashScreen.mLocaleRentFilterObjectEntity
							.setOpen_house("1");
				} else if (type == 2) {
					SplashScreen.mLocaleSoldFilterObjectEntity
							.setOpen_house("1");
				}
				// SplashScreen.mLocaleSellFilterObjectEntity.setOpen_house("1");
				mOpenHousesTog.setBackgroundResource(R.drawable.tick_on);
			} else {
				boolOpenHHouses = false;
				if (type == 0) {
					SplashScreen.mLocaleSellFilterObjectEntity
							.setOpen_house("0");
				} else if (type == 1) {
					SplashScreen.mLocaleRentFilterObjectEntity
							.setOpen_house("0");
				} else if (type == 2) {
					SplashScreen.mLocaleSoldFilterObjectEntity
							.setOpen_house("0");
				}
				// SplashScreen.mLocaleSellFilterObjectEntity.setOpen_house("0");
				mOpenHousesTog.setBackgroundResource(R.drawable.tick_off);
			}
			break;
		case R.id.tog_include_estimates:
			// if (!boolIncludeEstimate) {
			// boolIncludeEstimate = true;
			// SplashScreen.mLocaleFilterObjectEntity.setInclude_estimate("1");
			// mIncludeEstimateTog.setBackgroundResource(R.drawable.tick_on);
			// } else {
			// boolIncludeEstimate = false;
			// SplashScreen.mLocaleFilterObjectEntity.setInclude_estimate("0");
			// mIncludeEstimateTog.setBackgroundResource(R.drawable.tick_off);
			// }
			break;
		case R.id.tog_reduced_prices:
			if (!boolReducedPrices) {
				boolReducedPrices = true;
				if (type == 0) {
					SplashScreen.mLocaleSellFilterObjectEntity
							.setReduced_prices("1");
				} else if (type == 1) {
					SplashScreen.mLocaleRentFilterObjectEntity
							.setReduced_prices("1");
				} else if (type == 2) {
					SplashScreen.mLocaleSoldFilterObjectEntity
							.setReduced_prices("1");
				}
				// SplashScreen.mLocaleSellFilterObjectEntity
				// .setReduced_prices("1");
				mReducedPricesTog.setBackgroundResource(R.drawable.tick_on);
			} else {
				boolReducedPrices = false;
				if (type == 0) {
					SplashScreen.mLocaleSellFilterObjectEntity
							.setReduced_prices("0");
				} else if (type == 1) {
					SplashScreen.mLocaleRentFilterObjectEntity
							.setReduced_prices("0");
				} else if (type == 2) {
					SplashScreen.mLocaleSoldFilterObjectEntity
							.setReduced_prices("0");
				}
				// SplashScreen.mLocaleSellFilterObjectEntity
				// .setReduced_prices("0");
				mReducedPricesTog.setBackgroundResource(R.drawable.tick_off);
			}
			break;
		case R.id.tog_no_fee:
			if (!boolNoFee) {
				boolNoFee = true;
				if (type == 0) {
					SplashScreen.mLocaleSellFilterObjectEntity.setNo_fee("1");
				} else if (type == 1) {
					SplashScreen.mLocaleRentFilterObjectEntity.setNo_fee("1");
				} else if (type == 2) {
					SplashScreen.mLocaleSoldFilterObjectEntity.setNo_fee("1");
				}
				// SplashScreen.mLocaleSellFilterObjectEntity.setNo_fee("1");
				mNoFeeTog.setBackgroundResource(R.drawable.toggle_on);
			} else {
				boolNoFee = false;
				if (type == 0) {
					SplashScreen.mLocaleSellFilterObjectEntity.setNo_fee("0");
				} else if (type == 1) {
					SplashScreen.mLocaleRentFilterObjectEntity.setNo_fee("0");
				} else if (type == 2) {
					SplashScreen.mLocaleSoldFilterObjectEntity.setNo_fee("0");
				}
				// SplashScreen.mLocaleSellFilterObjectEntity.setNo_fee("0");
				mNoFeeTog.setBackgroundResource(R.drawable.toggle_off);
			}
			break;
		}
	}

	private void showPropertyPopup(final Context context) {
		mDialog = new Dialog(context);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.popup_property_types);
		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
		wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

		TextView mHeaderTxt;
		LinearLayout mBackIcon;
		Button mCancel, mSet;

		mBackIcon = (LinearLayout) mDialog.findViewById(R.id.back_icon);
		mHeaderTxt = (TextView) mDialog.findViewById(R.id.header_txt);

		mHeaderTxt.setText("Property Type");
		mSet = (Button) mDialog.findViewById(R.id.set);
		mCancel = (Button) mDialog.findViewById(R.id.cancel);

		mAllTypes = (CheckBox) mDialog.findViewById(R.id.all_types);
		mSingleFamilyHome = (CheckBox) mDialog.findViewById(R.id.single_family);
		mCondo = (CheckBox) mDialog.findViewById(R.id.condo);
		mTownhouse = (CheckBox) mDialog.findViewById(R.id.town_house);
		mCoop = (CheckBox) mDialog.findViewById(R.id.coop);
		mApartment = (CheckBox) mDialog.findViewById(R.id.apartment);
		mLoft = (CheckBox) mDialog.findViewById(R.id.loft);
		mTIC = (CheckBox) mDialog.findViewById(R.id.tic);
		mApartmentCondo = (CheckBox) mDialog.findViewById(R.id.apartment_condo);
		mMobileManufactured = (CheckBox) mDialog
				.findViewById(R.id.mobile_manufactured);
		mFarmRanch = (CheckBox) mDialog.findViewById(R.id.farm_ranch);
		mLotLand = (CheckBox) mDialog.findViewById(R.id.lot_land);
		mMultiFamily = (CheckBox) mDialog.findViewById(R.id.multi_family);
		mIncomeInvestment = (CheckBox) mDialog
				.findViewById(R.id.income_investment);
		mHouseBoat = (CheckBox) mDialog.findViewById(R.id.houseboat);

		mAllTypes.setOnCheckedChangeListener(this);
		mSingleFamilyHome.setOnCheckedChangeListener(this);
		mCondo.setOnCheckedChangeListener(this);
		mTownhouse.setOnCheckedChangeListener(this);
		mCoop.setOnCheckedChangeListener(this);
		mApartment.setOnCheckedChangeListener(this);
		mLoft.setOnCheckedChangeListener(this);
		mTIC.setOnCheckedChangeListener(this);
		mApartmentCondo.setOnCheckedChangeListener(this);
		mMobileManufactured.setOnCheckedChangeListener(this);
		mFarmRanch.setOnCheckedChangeListener(this);
		mLotLand.setOnCheckedChangeListener(this);
		mMultiFamily.setOnCheckedChangeListener(this);
		mIncomeInvestment.setOnCheckedChangeListener(this);
		mHouseBoat.setOnCheckedChangeListener(this);

		setPropertyTypeValues();
		mBackIcon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();

			}
		});
		mSet.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();

				if (mPropertyTypeList.size() == 0) {
					if (type == 0) {
						SplashScreen.mLocaleSellFilterObjectEntity
								.setProperty_type("");
					} else if (type == 1) {
						SplashScreen.mLocaleRentFilterObjectEntity
								.setProperty_type("");
					} else if (type == 2) {
						SplashScreen.mLocaleSoldFilterObjectEntity
								.setProperty_type("");
					}
					// SplashScreen.mLocaleSellFilterObjectEntity
					// .setProperty_type("");
					mPropertyTypeTxt.setText("All Types");

				} else {
					String mString = mPropertyTypeList.toString();
					String propertyType = mString.replace("[", "");
					String propertyType1 = propertyType.replace("]", "");
					if (type == 0) {
						SplashScreen.mLocaleSellFilterObjectEntity
								.setProperty_type(propertyType1);
					} else if (type == 1) {
						SplashScreen.mLocaleRentFilterObjectEntity
								.setProperty_type(propertyType1);
					} else if (type == 2) {
						SplashScreen.mLocaleSoldFilterObjectEntity
								.setProperty_type(propertyType1);
					}
					// SplashScreen.mLocaleSellFilterObjectEntity
					// .setProperty_type(propertyType1);
					mPropertyTypeTxt.setText(propertyType1);
					if (mPropertyTypeList.size() > 1) {
						mPropertyTypeTxt.setText("Multi");
					} else if (mPropertyTypeList.size() == 1) {
						mPropertyTypeTxt.setText(mPropertyTypeList.get(0)
								.toString());
					}
				}

			}
		});
		mCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();

			}
		});

		mDialog.show();
	}

	private void setPropertyTypeValues() {
		if (SplashScreen.mLocaleSellFilterObjectEntity.getProperty_type()
				.equals("")) {
			mAllTypes.setButtonDrawable(R.drawable.tick_on);
			mSingleFamilyHome.setButtonDrawable(R.drawable.tick_off);
			mCondo.setButtonDrawable(R.drawable.tick_off);
			mTownhouse.setButtonDrawable(R.drawable.tick_off);
			mCoop.setButtonDrawable(R.drawable.tick_off);
			mApartment.setButtonDrawable(R.drawable.tick_off);
			mLoft.setButtonDrawable(R.drawable.tick_off);
			mTIC.setButtonDrawable(R.drawable.tick_off);
			mApartmentCondo.setButtonDrawable(R.drawable.tick_off);
			mMobileManufactured.setButtonDrawable(R.drawable.tick_off);
			mFarmRanch.setButtonDrawable(R.drawable.tick_off);
			mLotLand.setButtonDrawable(R.drawable.tick_off);
			mMultiFamily.setButtonDrawable(R.drawable.tick_off);
			mIncomeInvestment.setButtonDrawable(R.drawable.tick_off);
			mHouseBoat.setButtonDrawable(R.drawable.tick_off);

			boolAllTypes = false;
			boolSingleFamilyHome = true;
			boolCondo = true;
			boolTownhouse = true;
			boolCoop = true;
			boolApartment = true;
			boolLoft = true;
			boolTIC = true;
			boolApartmentCondo = true;
			boolMobileManufactured = true;
			boolFarmRanch = true;
			boolLotLand = true;
			boolMultiFamily = true;
			boolIncomeInvestment = true;
			boolHouseBoat = true;
		} else {
			mAllTypes.setButtonDrawable(R.drawable.tick_off);
			boolAllTypes = true;
		}
		if (SplashScreen.mLocaleSellFilterObjectEntity.getProperty_type()
				.contains("Single-Family Home")) {
			mSingleFamilyHome.setButtonDrawable(R.drawable.tick_on);
			boolSingleFamilyHome = false;
		} else {
			mSingleFamilyHome.setButtonDrawable(R.drawable.tick_off);
			boolSingleFamilyHome = true;
		}
		if (SplashScreen.mLocaleSellFilterObjectEntity.getProperty_type()
				.contains("Condo")) {
			mCondo.setButtonDrawable(R.drawable.tick_on);
			boolCondo = false;
		} else {
			mCondo.setButtonDrawable(R.drawable.tick_off);
			boolCondo = true;
		}
		if (SplashScreen.mLocaleSellFilterObjectEntity.getProperty_type()
				.contains("Townhouse")) {
			mTownhouse.setButtonDrawable(R.drawable.tick_on);
			boolTownhouse = false;
		} else {
			mTownhouse.setButtonDrawable(R.drawable.tick_off);
			boolTownhouse = true;
		}
		if (SplashScreen.mLocaleSellFilterObjectEntity.getProperty_type()
				.contains("Coop")) {
			mCoop.setButtonDrawable(R.drawable.tick_on);
			boolCoop = false;
		} else {
			mCoop.setButtonDrawable(R.drawable.tick_off);
			boolCoop = true;
		}
		if (SplashScreen.mLocaleSellFilterObjectEntity.getProperty_type()
				.contains("Apartment")) {
			mApartment.setButtonDrawable(R.drawable.tick_on);
			boolApartment = false;
		} else {
			mCoop.setButtonDrawable(R.drawable.tick_off);
			boolApartment = true;
		}
		if (SplashScreen.mLocaleSellFilterObjectEntity.getProperty_type()
				.contains("Loft")) {
			mLoft.setButtonDrawable(R.drawable.tick_on);
			boolLoft = false;
		} else {
			mLoft.setButtonDrawable(R.drawable.tick_off);
			boolLoft = true;
		}
		if (SplashScreen.mLocaleSellFilterObjectEntity.getProperty_type()
				.contains("TIC")) {
			mTIC.setButtonDrawable(R.drawable.tick_on);
			boolTIC = false;
		} else {
			mTIC.setButtonDrawable(R.drawable.tick_off);
			boolTIC = true;
		}
		if (SplashScreen.mLocaleSellFilterObjectEntity.getProperty_type()
				.contains("Apartment/Condo/Townhouse")) {
			mApartmentCondo.setButtonDrawable(R.drawable.tick_on);
			boolApartmentCondo = false;
		} else {
			mApartmentCondo.setButtonDrawable(R.drawable.tick_off);
			boolApartmentCondo = true;
		}
		if (SplashScreen.mLocaleSellFilterObjectEntity.getProperty_type()
				.contains("Mobile/Manufactured")) {
			mMobileManufactured.setButtonDrawable(R.drawable.tick_on);
			boolMobileManufactured = false;
		} else {
			mMobileManufactured.setButtonDrawable(R.drawable.tick_off);
			boolMobileManufactured = true;
		}
		if (SplashScreen.mLocaleSellFilterObjectEntity.getProperty_type()
				.contains("Farm/Ranch")) {
			mFarmRanch.setButtonDrawable(R.drawable.tick_on);
			boolFarmRanch = false;
		} else {
			mFarmRanch.setButtonDrawable(R.drawable.tick_off);
			boolFarmRanch = true;
		}
		if (SplashScreen.mLocaleSellFilterObjectEntity.getProperty_type()
				.contains("Lot/land")) {
			mLotLand.setButtonDrawable(R.drawable.tick_on);
			boolLotLand = false;
		} else {
			mLotLand.setButtonDrawable(R.drawable.tick_off);
			boolLotLand = true;
		}
		if (SplashScreen.mLocaleSellFilterObjectEntity.getProperty_type()
				.contains("Multi-Family")) {
			mMultiFamily.setButtonDrawable(R.drawable.tick_on);
			boolMultiFamily = false;
		} else {
			mMultiFamily.setButtonDrawable(R.drawable.tick_off);
			boolMultiFamily = true;
		}
		if (SplashScreen.mLocaleSellFilterObjectEntity.getProperty_type()
				.contains("Income/Investment")) {
			mIncomeInvestment.setButtonDrawable(R.drawable.tick_on);
			boolIncomeInvestment = false;
		} else {
			mIncomeInvestment.setButtonDrawable(R.drawable.tick_off);
			boolIncomeInvestment = true;
		}
		if (SplashScreen.mLocaleSellFilterObjectEntity.getProperty_type()
				.contains("Houseboat")) {
			mHouseBoat.setButtonDrawable(R.drawable.tick_on);
			boolHouseBoat = false;
		} else {
			mHouseBoat.setButtonDrawable(R.drawable.tick_off);
			boolHouseBoat = true;
		}
	}

	private void setHeaderViewBackground(int id) {

		mForSale.setBackgroundResource(id == R.id.for_sale_txt ? R.drawable.filter_text_bg
				: R.color.text_color_white);
		mForRent.setBackgroundResource(id == R.id.for_rent_txt ? R.drawable.filter_text_bg
				: R.color.text_color_white);
		mSold.setBackgroundResource(id == R.id.sold_txt ? R.drawable.filter_text_bg
				: R.color.text_color_white);

		mForSale.setTextColor(getResources().getColor(R.color.grey));
		mForRent.setTextColor(getResources().getColor(R.color.grey));
		mSold.setTextColor(getResources().getColor(R.color.grey));

		if (id == R.id.for_sale_txt) {
			mForSale.setTextColor(getResources().getColor(R.color.blue));
		} else if (id == R.id.for_rent_txt) {
			mForRent.setTextColor(getResources().getColor(R.color.blue));
		} else if (id == R.id.sold_txt) {
			mSold.setTextColor(getResources().getColor(R.color.blue));
		}
	}

	private void setBedBackground(int id) {

		mBedAny.setBackgroundResource(id == R.id.bed_any ? R.color.blue
				: R.drawable.btn_bg_left);
		mBedNum0.setBackgroundResource(id == R.id.bed_num0 ? R.color.blue
				: R.drawable.btn_bg_center);
		mBedNum1.setBackgroundResource(id == R.id.bed_num1 ? R.color.blue
				: R.drawable.btn_bg_center);
		mBedNum2.setBackgroundResource(id == R.id.bed_num2 ? R.color.blue
				: R.drawable.btn_bg_center);
		mBedNum3.setBackgroundResource(id == R.id.bed_num3 ? R.color.blue
				: R.drawable.btn_bg_center);
		mBedNum4.setBackgroundResource(id == R.id.bed_num4 ? R.color.blue
				: R.drawable.btn_bg_center);
		mBedNum5.setBackgroundResource(id == R.id.bed_num5 ? R.color.blue
				: R.drawable.btn_bg_right);
	}

	private void setBathBackground(int id) {
		mBathAny.setBackgroundResource(id == R.id.baths_any ? R.color.blue
				: R.drawable.btn_bg_left);
		mBathNum0.setBackgroundResource(id == R.id.baths_num0 ? R.color.blue
				: R.drawable.btn_bg_center);
		mBathNum1.setBackgroundResource(id == R.id.baths_num1 ? R.color.blue
				: R.drawable.btn_bg_center);
		mBathNum2.setBackgroundResource(id == R.id.baths_num2 ? R.color.blue
				: R.drawable.btn_bg_center);
		mBathNum3.setBackgroundResource(id == R.id.baths_num3 ? R.color.blue
				: R.drawable.btn_bg_center);
		mBathNum4.setBackgroundResource(id == R.id.baths_num4 ? R.color.blue
				: R.drawable.btn_bg_center);
		mBathNum5.setBackgroundResource(id == R.id.baths_num5 ? R.color.blue
				: R.drawable.btn_bg_right);

	}

	private void setBedTextColor(int id) {

		mBedAny.setTextColor(getResources().getColor(R.color.blue));
		mBedNum0.setTextColor(getResources().getColor(R.color.blue));
		mBedNum1.setTextColor(getResources().getColor(R.color.blue));
		mBedNum2.setTextColor(getResources().getColor(R.color.blue));
		mBedNum3.setTextColor(getResources().getColor(R.color.blue));
		mBedNum4.setTextColor(getResources().getColor(R.color.blue));
		mBedNum5.setTextColor(getResources().getColor(R.color.blue));

		if (id == R.id.bed_any) {
			mBedAny.setTextColor(getResources().getColor(
					R.color.text_color_white));
		} else if (id == R.id.bed_num0) {
			mBedNum0.setTextColor(getResources().getColor(
					R.color.text_color_white));
		} else if (id == R.id.bed_num1) {
			mBedNum1.setTextColor(getResources().getColor(
					R.color.text_color_white));
		} else if (id == R.id.bed_num2) {
			mBedNum2.setTextColor(getResources().getColor(
					R.color.text_color_white));
		} else if (id == R.id.bed_num3) {
			mBedNum3.setTextColor(getResources().getColor(
					R.color.text_color_white));
		} else if (id == R.id.bed_num4) {
			mBedNum4.setTextColor(getResources().getColor(
					R.color.text_color_white));
		} else if (id == R.id.bed_num5) {
			mBedNum5.setTextColor(getResources().getColor(
					R.color.text_color_white));
		}

	}

	private void setBathTextColor(int id) {

		mBathAny.setTextColor(getResources().getColor(R.color.blue));
		mBathNum0.setTextColor(getResources().getColor(R.color.blue));
		mBathNum1.setTextColor(getResources().getColor(R.color.blue));
		mBathNum2.setTextColor(getResources().getColor(R.color.blue));
		mBathNum3.setTextColor(getResources().getColor(R.color.blue));
		mBathNum4.setTextColor(getResources().getColor(R.color.blue));
		mBathNum5.setTextColor(getResources().getColor(R.color.blue));

		if (id == R.id.baths_any) {
			mBathAny.setTextColor(getResources().getColor(
					R.color.text_color_white));
		} else if (id == R.id.baths_num0) {
			mBathNum0.setTextColor(getResources().getColor(
					R.color.text_color_white));
		} else if (id == R.id.baths_num1) {
			mBathNum1.setTextColor(getResources().getColor(
					R.color.text_color_white));
		} else if (id == R.id.baths_num2) {
			mBathNum2.setTextColor(getResources().getColor(
					R.color.text_color_white));
		} else if (id == R.id.baths_num3) {
			mBathNum3.setTextColor(getResources().getColor(
					R.color.text_color_white));
		} else if (id == R.id.baths_num4) {
			mBathNum4.setTextColor(getResources().getColor(
					R.color.text_color_white));
		} else if (id == R.id.baths_num5) {
			mBathNum5.setTextColor(getResources().getColor(
					R.color.text_color_white));
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View arg1, int pos,
			long arg3) {
		if (parent.getId() == R.id.price_range_min_spin) {

			mTempListPrice.clear();
			mTempListPrice.addAll(mPriceMaxList);
			mPriceMin = (String) parent.getItemAtPosition(pos);
			mPriceTxt.setText(mPriceMin + " - No Max");
			if (mPriceMin.equals("No Min")) {
				// mPriceMin = "";
			}
			mSplitPriceMin = mPriceMin.replace("$", "");
			mSplitPriceMax = "";
			// if (type == 0) {
			// SplashScreen.mLocaleSellFilterObjectEntity
			// .setPrice_range_min(mSplitPriceMin);
			// SplashScreen.mLocaleSellFilterObjectEntity
			// .setPrice_range_max(mSplitPriceMax);
			// } else
			// if (type == 1) {
			SplashScreen.mLocaleRentFilterObjectEntity
					.setPrice_range_min(mSplitPriceMin);
			SplashScreen.mLocaleRentFilterObjectEntity
					.setPrice_range_max(mSplitPriceMax);
			// }

			if (mPriceMin.equalsIgnoreCase(mTempListPrice.get(pos))) {
				for (int j = 1; j < pos; j++) {
					mTempListPrice.remove(1);
				}
				mPriceMaxAdapter = new ArrayAdapter<String>(this,
						R.layout.spinner_item_lay, R.id.spinner_item,
						mTempListPrice);
				mPriceMaxSpin.setAdapter(mPriceMaxAdapter);
				mPriceMaxAdapter.notifyDataSetChanged();

				// }
			}
		} else if (parent.getId() == R.id.price_range_max_spin) {

			mPriceMax = (String) parent.getItemAtPosition(pos);
			mPriceTxt.setText(mPriceMin + " - " + mPriceMax);
			if (mPriceMax.equals("No Max")) {
				// mPriceMax = "";
			}
			mSplitPriceMax = mPriceMax.replace("$", "");
			// if (type == 0) {
			// SplashScreen.mLocaleSellFilterObjectEntity
			// .setPrice_range_max(mSplitPriceMax);
			// } else if (type == 1) {
			SplashScreen.mLocaleRentFilterObjectEntity
					.setPrice_range_max(mSplitPriceMax);
			// }

		} else if (parent.getId() == R.id.price_range_min_spin_sale) {

			mTempListPriceSale.clear();
			mTempListPriceSale.addAll(mPriceMaxListSale);
			mPriceMinSale = (String) parent.getItemAtPosition(pos);
			mPriceTxtSale.setText(mPriceMinSale + " - No Max");
			if (mPriceMinSale.equals("No Min")) {
			}

			mSplitPriceMinSale = mPriceMinSale.replace("$", "");
			mSplitPriceMaxSale = "";
			SplashScreen.mLocaleSellFilterObjectEntity
					.setPrice_range_min(mSplitPriceMinSale);
			SplashScreen.mLocaleSellFilterObjectEntity
					.setPrice_range_max(mSplitPriceMaxSale);
			if (mPriceMinSale.equalsIgnoreCase(mTempListPriceSale.get(pos))) {
				for (int j = 1; j < pos; j++) {
					mTempListPriceSale.remove(1);
				}
				mPriceMaxAdapterSale = new ArrayAdapter<String>(this,
						R.layout.spinner_item_lay, R.id.spinner_item,
						mTempListPriceSale);
				mPriceMaxSpinSale.setAdapter(mPriceMaxAdapterSale);
				mPriceMaxAdapterSale.notifyDataSetChanged();

			}

		} else if (parent.getId() == R.id.price_range_max_spin_sale) {

			mPriceMaxSale = (String) parent.getItemAtPosition(pos);
			mPriceTxtSale.setText(mPriceMinSale + " - " + mPriceMaxSale);
			if (mPriceMaxSale.equals("No Max")) {
			}
			mSplitPriceMaxSale = mPriceMaxSale.replace("$", "");
			SplashScreen.mLocaleSellFilterObjectEntity
					.setPrice_range_max(mSplitPriceMaxSale);

		} else if (parent.getId() == R.id.square_footage_min_spin) {

			mTempListSquare.clear();
			mTempListSquare.addAll(mSquareMaxList);
			mSquareMin = (String) parent.getItemAtPosition(pos);
			mSqauareTxt.setText(mSquareMin + " - No Max");
			if (mSquareMin.equals("No Min")) {
				// mSquareMin = "";
			}
			mSplitSquareMin = mSquareMin.replace(" sqft", "");
			mSplitSquareMax = "";
			if (type == 0) {
				SplashScreen.mLocaleSellFilterObjectEntity
						.setSquare_footage_min(mSplitSquareMin);
				SplashScreen.mLocaleSellFilterObjectEntity
						.setSquare_footage_max(mSplitSquareMax);
			} else if (type == 1) {
				SplashScreen.mLocaleRentFilterObjectEntity
						.setSquare_footage_min(mSplitSquareMin);
				SplashScreen.mLocaleRentFilterObjectEntity
						.setSquare_footage_max(mSplitSquareMax);
			} else if (type == 2) {
				SplashScreen.mLocaleSoldFilterObjectEntity
						.setSquare_footage_min(mSplitSquareMin);
				SplashScreen.mLocaleSoldFilterObjectEntity
						.setSquare_footage_max(mSplitSquareMax);
			}
			if (mSquareMin.equalsIgnoreCase(mTempListSquare.get(pos))) {
				for (int j = 1; j < pos; j++) {
					mTempListSquare.remove(1);
				}
				mSquareMaxAdapter = new ArrayAdapter<String>(this,
						R.layout.spinner_item_lay, R.id.spinner_item,
						mTempListSquare);
				mSquareMaxSpin.setAdapter(mSquareMaxAdapter);
				mSquareMaxAdapter.notifyDataSetChanged();

			}
		} else if (parent.getId() == R.id.square_footage_max_spin) {

			mSquareMax = (String) parent.getItemAtPosition(pos);
			mSqauareTxt.setText(mSquareMin + " - " + mSquareMax);
			if (mSquareMax.equals("No Max")) {
				// mSquareMax = "";
			}
			mSplitSquareMax = mSquareMax.replace(" sqft", "");
			if (type == 0) {
				SplashScreen.mLocaleSellFilterObjectEntity
						.setSquare_footage_max(mSplitSquareMax);
			} else if (type == 1) {
				SplashScreen.mLocaleRentFilterObjectEntity
						.setSquare_footage_max(mSplitSquareMax);
			} else if (type == 2) {
				SplashScreen.mLocaleSoldFilterObjectEntity
						.setSquare_footage_max(mSplitSquareMax);
			}

		} else if (parent.getId() == R.id.lot_size_spin) {

			mLot = (String) parent.getItemAtPosition(pos);
			mLotTxt.setText(mLot);
			if (mLot.equals("Any")) {
				mLot = "";
			} else if (mLot.equals("2,000+ sqft")) {
				mLot = "2000";
			} else if (mLot.equals("3,000+ sqft")) {
				mLot = "3000";
			} else if (mLot.equals("4,000+ sqft")) {
				mLot = "4000";
			} else if (mLot.equals("5,000+ sqft")) {
				mLot = "5000";
			} else if (mLot.equals("7,500+ sqft")) {
				mLot = "7500";
			} else if (mLot.equals("1/4+ sqft")) {
				mLot = "10890";
			} else if (mLot.equals("1/2+ sqft")) {
				mLot = "21780";
			} else if (mLot.equals("1+ sqft")) {
				mLot = "43560";
			} else if (mLot.equals("2+ sqft")) {
				mLot = "87120";
			} else if (mLot.equals("5+ sqft")) {
				mLot = "217800";
			} else if (mLot.equals("10+ sqft")) {
				mLot = "435600";
			}
			if (type == 0) {
				SplashScreen.mLocaleSellFilterObjectEntity.setLot_size(mLot);
			} else if (type == 1) {
				SplashScreen.mLocaleRentFilterObjectEntity.setLot_size(mLot);
			} else if (type == 2) {
				SplashScreen.mLocaleSoldFilterObjectEntity.setLot_size(mLot);
			}
			// SplashScreen.mLocaleSellFilterObjectEntity.setLot_size(mLot);
		} else if (parent.getId() == R.id.days_on_rb_spin) {
			mDaysonRb = (String) parent.getItemAtPosition(pos);
			mDaysonrbTxt.setText(mDaysonRb);
			if (mDaysonRb.equals("Any")) {
				mDaysonRb = "";
			} else if (mDaysonRb.equals("1 day")) {
				mDaysonRb = "1";
			} else if (mDaysonRb.equals("7 days")) {
				mDaysonRb = "7";
			} else if (mDaysonRb.equals("14 days")) {
				mDaysonRb = "14";
			} else if (mDaysonRb.equals("30 days")) {
				mDaysonRb = "30";
			} else if (mDaysonRb.equals("90 days")) {
				mDaysonRb = "90";
			}
			if (type == 0) {
				SplashScreen.mLocaleSellFilterObjectEntity
						.setDays_on_RB(mDaysonRb);
			} else if (type == 1) {
				SplashScreen.mLocaleRentFilterObjectEntity
						.setDays_on_RB(mDaysonRb);
			} else if (type == 2) {
				SplashScreen.mLocaleSoldFilterObjectEntity
						.setDays_on_RB(mDaysonRb);
			}
			// SplashScreen.mLocaleSellFilterObjectEntity.setDays_on_RB(mDaysonRb);
		} else if (parent.getId() == R.id.sold_within_spin) {
			// mSoldwithins = (String) parent.getItemAtPosition(pos);
			// mSoldewithTxt.setText(mSoldwithins);
			// if (mSoldwithins.equals("Any")) {
			// mSoldwithins = "";
			// } else if (mSoldwithins.equals("3 months")) {
			// mSoldwithins = "3";
			// } else if (mSoldwithins.equals("6 months")) {
			// mSoldwithins = "6";
			// } else if (mSoldwithins.equals("9 months")) {
			// mSoldwithins = "9";
			// }
			// SplashScreen.mLocaleFilterObjectEntity.setSold_within(mSoldwithins);
		} else if (parent.getId() == R.id.year_built_min_spin) {

			mTempListYear.clear();
			mTempListYear.addAll(mYearMaxList);
			mYearMin = (String) parent.getItemAtPosition(pos);
			if (mYearMin.equalsIgnoreCase("")) {
				mYearTxt.setText("2015" + " - No Max");
			} else {
				mYearTxt.setText(mYearMin + " - No Max");
			}

			if (mYearMin.equals("No Min")) {
				// mYearMin = "";
			}
			mYearMax = "";
			if (type == 0) {
				SplashScreen.mLocaleSellFilterObjectEntity
						.setYear_build_min(mYearMin);
				SplashScreen.mLocaleSellFilterObjectEntity
						.setYear_build_max(mYearMax);
			} else if (type == 1) {
				SplashScreen.mLocaleRentFilterObjectEntity
						.setYear_build_min(mYearMin);
				SplashScreen.mLocaleRentFilterObjectEntity
						.setYear_build_max(mYearMax);
			} else if (type == 2) {
				SplashScreen.mLocaleSoldFilterObjectEntity
						.setYear_build_min(mYearMin);
				SplashScreen.mLocaleSoldFilterObjectEntity
						.setYear_build_max(mYearMax);
			}
			if (mYearMin.equalsIgnoreCase(mTempListYear.get(pos))) {
				for (int j = 1; j < pos; j++) {
					mTempListYear.remove(1);
				}
				mYearMaxAdapter = new ArrayAdapter<String>(this,
						R.layout.spinner_item_lay, R.id.spinner_item,
						mTempListYear);
				mYearMaxSpin.setAdapter(mYearMaxAdapter);
				mYearMaxAdapter.notifyDataSetChanged();
			}

		} else if (parent.getId() == R.id.year_built_max_spin) {

			mYearMax = (String) parent.getItemAtPosition(pos);
			mYearTxt.setText(mYearMin + " - " + mYearMax);
			if (mYearMax.equals("No Max")) {
				// mYearMax = "";
			}
			if (type == 0) {
				SplashScreen.mLocaleSellFilterObjectEntity
						.setYear_build_max(mYearMax);
			} else if (type == 1) {
				SplashScreen.mLocaleRentFilterObjectEntity
						.setYear_build_max(mYearMax);
			} else if (type == 2) {
				SplashScreen.mLocaleSoldFilterObjectEntity
						.setYear_build_max(mYearMax);
			}
		}
	}

	private void callFilterApply() {
		if (mMLSEdit.getText().toString().length() >= 1) {
			if (type == 0) {
				SplashScreen.mLocaleSellFilterObjectEntity.setMLS(mMLSEdit
						.getText().toString());
			} else if (type == 1) {
				SplashScreen.mLocaleRentFilterObjectEntity.setMLS(mMLSEdit
						.getText().toString());
			} else if (type == 2) {
				SplashScreen.mLocaleSoldFilterObjectEntity.setMLS(mMLSEdit
						.getText().toString());
			}
		}
		Gson gson = new Gson();
		mSaleFilterObject = gson
				.toJson(SplashScreen.mLocaleSellFilterObjectEntity);
		mRentFilterObject = gson
				.toJson(SplashScreen.mLocaleRentFilterObjectEntity);
		mSoldFilterObject = gson
				.toJson(SplashScreen.mLocaleSoldFilterObjectEntity);
		mFilterObjects = "{\"Sale\": " + mSaleFilterObject + ",\"Rent\": "
				+ mRentFilterObject + ",\"Sold\": " + mSoldFilterObject + "}";

		String Url = AppConstants.BASE_URL + "filtersearch";

		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();

		params.put("user_id", UserID);
		params.put("filter_type", mFilterType);
		params.put("filter_object", mFilterObjects);

		if (mFilterType.equals("Rent")) {
			AppConstants.type_of_property_filter = "rent";
		} else if (mFilterType.equals("Sale")) {
			AppConstants.type_of_property_filter = "sale";
		} else if (mFilterType.equals("Sold")) {
			AppConstants.type_of_property_filter = "sold";
		}

		aq().transformer(t)
				.progress(DialogManager.getProgressDialog(this))
				.ajax(Url, params, JSONObject.class,
						new AjaxCallback<JSONObject>() {

							@Override
							public void callback(String url, JSONObject json,
									AjaxStatus status) {
								if (json != null) {
									System.out.println(json);
								} else {
									statusErrorCode(status);
								}
								launchActivity(MapFragmentActivity.class);
							}

						});

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(FilterActivity.this,
				MapFragmentActivity.class);
		AppConstants.CALL_MAP = "true";
		startActivity(intent);
		overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
	}

	@Override
	public void onCheckedChanged(CompoundButton button, boolean isCheck) {

		switch (button.getId()) {
		case R.id.all_types:
			if (boolAllTypes) {
				mPropertyTypeList.clear();
				mAllTypes.setButtonDrawable(R.drawable.tick_on);
				mSingleFamilyHome.setButtonDrawable(R.drawable.tick_off);
				mCondo.setButtonDrawable(R.drawable.tick_off);
				mTownhouse.setButtonDrawable(R.drawable.tick_off);
				mCoop.setButtonDrawable(R.drawable.tick_off);
				mApartment.setButtonDrawable(R.drawable.tick_off);
				mLoft.setButtonDrawable(R.drawable.tick_off);
				mTIC.setButtonDrawable(R.drawable.tick_off);
				mApartmentCondo.setButtonDrawable(R.drawable.tick_off);
				mMobileManufactured.setButtonDrawable(R.drawable.tick_off);
				mFarmRanch.setButtonDrawable(R.drawable.tick_off);
				mLotLand.setButtonDrawable(R.drawable.tick_off);
				mMultiFamily.setButtonDrawable(R.drawable.tick_off);
				mIncomeInvestment.setButtonDrawable(R.drawable.tick_off);
				mHouseBoat.setButtonDrawable(R.drawable.tick_off);

				boolAllTypes = false;
				boolSingleFamilyHome = true;
				boolCondo = true;
				boolTownhouse = true;
				boolCoop = true;
				boolApartment = true;
				boolLoft = true;
				boolTIC = true;
				boolApartmentCondo = true;
				boolMobileManufactured = true;
				boolFarmRanch = true;
				boolLotLand = true;
				boolMultiFamily = true;
				boolIncomeInvestment = true;
				boolHouseBoat = true;

			} else {
				mPropertyTypeList.clear();
				mAllTypes.setButtonDrawable(R.drawable.tick_on);
				boolAllTypes = true;
			}
			break;
		case R.id.single_family:
			if (boolSingleFamilyHome) {
				mPropertyTypeList.add("Single-Family Home");
				mSingleFamilyHome.setButtonDrawable(R.drawable.tick_on);
				boolSingleFamilyHome = false;
				mAllTypes.setButtonDrawable(R.drawable.tick_off);
				boolAllTypes = true;
			} else {
				mPropertyTypeList.remove("Single-Family Home");
				mSingleFamilyHome.setButtonDrawable(R.drawable.tick_off);
				boolSingleFamilyHome = true;
			}
			break;
		case R.id.condo:
			if (boolCondo) {
				mPropertyTypeList.add("Condo");
				mCondo.setButtonDrawable(R.drawable.tick_on);
				boolCondo = false;
				mAllTypes.setButtonDrawable(R.drawable.tick_off);
				boolAllTypes = true;
			} else {
				mPropertyTypeList.remove("Condo");
				mCondo.setButtonDrawable(R.drawable.tick_off);
				boolCondo = true;
			}
			break;
		case R.id.town_house:
			if (boolTownhouse) {
				mPropertyTypeList.add("Townhouse");
				mTownhouse.setButtonDrawable(R.drawable.tick_on);
				mAllTypes.setButtonDrawable(R.drawable.tick_off);
				boolAllTypes = true;
				boolTownhouse = false;
			} else {
				mPropertyTypeList.remove("Townhouse");
				mTownhouse.setButtonDrawable(R.drawable.tick_off);
				boolTownhouse = true;
			}
			break;
		case R.id.coop:
			if (boolCoop) {
				mPropertyTypeList.add("Coop");
				mCoop.setButtonDrawable(R.drawable.tick_on);
				mAllTypes.setButtonDrawable(R.drawable.tick_off);
				boolAllTypes = true;
				boolCoop = false;
			} else {
				mPropertyTypeList.remove("Coop");
				mCoop.setButtonDrawable(R.drawable.tick_off);
				boolCoop = true;
			}
			break;
		case R.id.apartment:
			if (boolApartment) {
				mPropertyTypeList.add("Apartment");
				mApartment.setButtonDrawable(R.drawable.tick_on);
				mAllTypes.setButtonDrawable(R.drawable.tick_off);
				boolAllTypes = true;
				boolApartment = false;
			} else {
				mPropertyTypeList.remove("Apartment");
				mApartment.setButtonDrawable(R.drawable.tick_off);
				boolApartment = true;
			}
			break;
		case R.id.loft:
			if (boolLoft) {
				mPropertyTypeList.add("Loft");
				mLoft.setButtonDrawable(R.drawable.tick_on);
				mAllTypes.setButtonDrawable(R.drawable.tick_off);
				boolAllTypes = true;
				boolLoft = false;
			} else {
				mPropertyTypeList.remove("Loft");
				mLoft.setButtonDrawable(R.drawable.tick_off);
				boolLoft = true;
			}
			break;
		case R.id.tic:
			if (boolTIC) {
				mPropertyTypeList.add("TIC");
				mTIC.setButtonDrawable(R.drawable.tick_on);
				mAllTypes.setButtonDrawable(R.drawable.tick_off);
				boolAllTypes = true;
				boolTIC = false;
			} else {
				mPropertyTypeList.remove("TIC");
				mTIC.setButtonDrawable(R.drawable.tick_off);
				boolTIC = true;
			}
			break;
		case R.id.apartment_condo:
			if (boolApartmentCondo) {
				mPropertyTypeList.add("Apartment/Condo/Townhouse");
				mApartmentCondo.setButtonDrawable(R.drawable.tick_on);
				mAllTypes.setButtonDrawable(R.drawable.tick_off);
				boolAllTypes = true;
				boolApartmentCondo = false;
			} else {
				mPropertyTypeList.remove("Apartment/Condo/Townhouse");
				mApartmentCondo.setButtonDrawable(R.drawable.tick_off);
				boolApartmentCondo = true;
			}
			break;
		case R.id.mobile_manufactured:
			if (boolMobileManufactured) {
				mPropertyTypeList.add("Mobile/Manufactured");
				mMobileManufactured.setButtonDrawable(R.drawable.tick_on);
				mAllTypes.setButtonDrawable(R.drawable.tick_off);
				boolAllTypes = true;
				boolMobileManufactured = false;
			} else {
				mPropertyTypeList.remove("Mobile/Manufactured");
				mMobileManufactured.setButtonDrawable(R.drawable.tick_off);
				boolMobileManufactured = true;
			}
			break;
		case R.id.farm_ranch:
			if (boolFarmRanch) {
				mPropertyTypeList.add("Farm/Ranch");
				mFarmRanch.setButtonDrawable(R.drawable.tick_on);
				mAllTypes.setButtonDrawable(R.drawable.tick_off);
				boolAllTypes = true;
				boolFarmRanch = false;
			} else {
				mPropertyTypeList.remove("Farm/Ranch");
				mFarmRanch.setButtonDrawable(R.drawable.tick_off);
				boolFarmRanch = true;
			}
			break;
		case R.id.lot_land:
			if (boolLotLand) {
				mPropertyTypeList.add("Lot/land");
				mLotLand.setButtonDrawable(R.drawable.tick_on);
				mAllTypes.setButtonDrawable(R.drawable.tick_off);
				boolAllTypes = true;
				boolLotLand = false;
			} else {
				mPropertyTypeList.remove("Lot/land");
				mLotLand.setButtonDrawable(R.drawable.tick_off);
				boolLotLand = true;
			}
			break;
		case R.id.multi_family:
			if (boolMultiFamily) {
				mPropertyTypeList.add("Multi-Family");
				mMultiFamily.setButtonDrawable(R.drawable.tick_on);
				mAllTypes.setButtonDrawable(R.drawable.tick_off);
				boolAllTypes = true;
				boolMultiFamily = false;
			} else {
				mPropertyTypeList.remove("Multi-Family");
				mMultiFamily.setButtonDrawable(R.drawable.tick_off);
				boolMultiFamily = true;
			}
			break;
		case R.id.income_investment:
			if (boolIncomeInvestment) {
				mPropertyTypeList.add("Income/Investment");
				mIncomeInvestment.setButtonDrawable(R.drawable.tick_on);
				mAllTypes.setButtonDrawable(R.drawable.tick_off);
				boolAllTypes = true;
				boolIncomeInvestment = false;
			} else {
				mPropertyTypeList.remove("Income/Investment");
				mIncomeInvestment.setButtonDrawable(R.drawable.tick_off);
				boolIncomeInvestment = true;
			}
			break;
		case R.id.houseboat:
			if (boolHouseBoat) {
				mPropertyTypeList.add("Houseboat");
				mHouseBoat.setButtonDrawable(R.drawable.tick_on);
				mAllTypes.setButtonDrawable(R.drawable.tick_off);
				boolAllTypes = true;
				boolHouseBoat = false;
			} else {
				mPropertyTypeList.remove("Houseboat");
				mHouseBoat.setButtonDrawable(R.drawable.tick_off);
				boolHouseBoat = true;
			}
			break;

		}
	}

	private void showAlertPopup() {
		mDialog = new Dialog(FilterActivity.this,
				android.R.style.Theme_Translucent_NoTitleBar);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.popup_alert);
		mDialog.setCancelable(false);
		Button cancel = (Button) mDialog.findViewById(R.id.cancel);
		Button save = (Button) mDialog.findViewById(R.id.save);
		final EditText mAlertEdit = (EditText) mDialog
				.findViewById(R.id.enter_search_name);

		mAlertEdit.setText(mAlerttxt);

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mDialog.cancel();

			}
		});
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mAlertTitile = mAlertEdit.getText().toString().trim();
				if (mAlertTitile.equalsIgnoreCase("")) {
					DialogManager.showCustomAlertDialog(FilterActivity.this,
							FilterActivity.this, "Please enter alret name");
				} else {

					mDialog.dismiss();
					updateRentfilterdb();
					updateSalefilterdb();
					updateSoldfilterdb();
					callAddAlert();
				}

			}
		});

		mDialog.show();
	}

	private void callAddAlert() {
		if (mMLSEdit.getText().toString().length() >= 1) {
			if (type == 0) {
				SplashScreen.mLocaleSellFilterObjectEntity.setMLS(mMLSEdit
						.getText().toString());
			} else if (type == 1) {
				SplashScreen.mLocaleRentFilterObjectEntity.setMLS(mMLSEdit
						.getText().toString());
			} else if (type == 2) {
				SplashScreen.mLocaleSoldFilterObjectEntity.setMLS(mMLSEdit
						.getText().toString());
			}
		}

		SplashScreen.mLocaleRentFilterObjectEntity.setLatitude(mLatitude);
		SplashScreen.mLocaleRentFilterObjectEntity.setLongitude(mLongitude);
		SplashScreen.mLocaleRentFilterObjectEntity.setLocation(mLocation);
		SplashScreen.mLocaleRentFilterObjectEntity.setDistance("1000");

		SplashScreen.mLocaleSellFilterObjectEntity.setLatitude(mLatitude);
		SplashScreen.mLocaleSellFilterObjectEntity.setLongitude(mLongitude);
		SplashScreen.mLocaleSellFilterObjectEntity.setLocation(mLocation);
		SplashScreen.mLocaleSellFilterObjectEntity.setDistance("1000");

		Gson gson = new Gson();
		mSaleFilterObject = gson
				.toJson(SplashScreen.mLocaleSellFilterObjectEntity);
		mRentFilterObject = gson
				.toJson(SplashScreen.mLocaleRentFilterObjectEntity);
		mSoldFilterObject = gson
				.toJson(SplashScreen.mLocaleSoldFilterObjectEntity);
		mFilterObjects = "{\"Sale\": " + mSaleFilterObject + ",\"Rent\": "
				+ mRentFilterObject + "}";

		String Url = AppConstants.BASE_URL + "alert";

		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();

		params.put("user_id", UserID);
		params.put("type", mFilterType);
		params.put("alert_object", mFilterObjects);
		params.put("alert_name", mAlertTitile);
		params.put("alert_id", mAlertID);

		if (mFilterType.equals("Rent")) {
			AppConstants.type_of_property_filter = "rent";
		} else if (mFilterType.equals("Sale")) {
			AppConstants.type_of_property_filter = "sale";
		} else if (mFilterType.equals("Sold")) {
			AppConstants.type_of_property_filter = "sold";
		}

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
										if (AppConstants.IS_EDIT.equals("true")) {
											DialogManager
													.showCustomAlertDialog(
															FilterActivity.this,
															FilterActivity.this,
															"Alert Updated Successfully.");
											AppConstants.IS_EDIT = "false";
										} else {
											DialogManager
													.showCustomAlertDialog(
															FilterActivity.this,
															FilterActivity.this,
															mResponse.getMsg());
										}
									}

								} else {
									statusErrorCode(status);
								}

							}

						});

	}

	@Override
	public void onOkclick() {
		if (mAlert.equalsIgnoreCase("CallApi")) {
			launchActivity(AlertsActivity.class);
			overridePendingTransition(R.anim.slide_out_right,
					R.anim.slide_in_left);
		} else {
			/**
			 * Close Dialog
			 */
		}
	}
}
