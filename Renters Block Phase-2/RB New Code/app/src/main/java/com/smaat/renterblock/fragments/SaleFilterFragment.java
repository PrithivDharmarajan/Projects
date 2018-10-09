package com.smaat.renterblock.fragments;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nex3z.flowlayout.FlowLayout;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.AlertFilterObjectEntity;
import com.smaat.renterblock.entity.FilterAPIEntity;
import com.smaat.renterblock.entity.FilterEntity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.SettingResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.InterfaceEdtWithBtnCallback;
import com.smaat.renterblock.utils.PreferenceUtil;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SaleFilterFragment extends BaseFragment implements AdapterView.OnItemSelectedListener {
    @BindView(R.id.parent_layout)
    RelativeLayout mParentLay;
    //    @BindView(R.id.for_rent_txt)
//    TextView mForRentTxt;
//    @BindView(R.id.for_sale_txt)
//    TextView mForSaleTxt;
    @BindView(R.id.price_range_txt)
    TextView mPriceRangeTxt;
    @BindView(R.id.price_range_img)
    ImageView mPriceRangeImg;
    private boolean mRentResetAllBool = false, mSaleResetAllBool = false;

    @BindView(R.id.price_range_lay)
    RelativeLayout mPriceRangeLay;
    @BindView(R.id.price_range_spin_lay)
    LinearLayout mPriceRangeSpinnerLay;
    @BindView(R.id.price_range_min_spin)
    Spinner mPriceRangeMinSpinner;
    @BindView(R.id.price_range_max_spin)
    Spinner mPriceRangeMaxSpinner;

    @BindView(R.id.property_type_txt)
    TextView mPropertyTypeTxt;
    @BindView(R.id.property_type_img)
    ImageView mPropertyTypeImg;
    @BindView(R.id.property_type_lay)
    RelativeLayout mPropertyTypeLay;


    @BindView(R.id.bed_any)
    TextView mBedAnyTxt;
    @BindView(R.id.bed_num0)
    TextView mBed0Txt;
    @BindView(R.id.bed_num1)
    TextView mBed1Txt;
    @BindView(R.id.bed_num2)
    TextView mBed2Txt;
    @BindView(R.id.bed_num3)
    TextView mBed3Txt;
    @BindView(R.id.bed_num4)
    TextView mBed4Txt;
    @BindView(R.id.bed_num5)
    TextView mBed5Txt;
    @BindView(R.id.baths_any)
    TextView mBathAnyTxt;
    @BindView(R.id.baths_num0)
    TextView mBath0Txt;
    @BindView(R.id.baths_num1)
    TextView mBath1Txt;
    @BindView(R.id.baths_num2)
    TextView mBath2Txt;
    @BindView(R.id.baths_num3)
    TextView mBath3Txt;
    @BindView(R.id.baths_num4)
    TextView mBath4Txt;
    @BindView(R.id.baths_num5)
    TextView mBath5Txt;

    @BindView(R.id.square_footage_txt)
    TextView mSquareTxt;
    @BindView(R.id.square_footage_img)
    ImageView mSquareFootageImg;
    @BindView(R.id.square_footage_min_spin)
    Spinner mSquareMinSpinner;
    @BindView(R.id.square_footage_max_spin)
    Spinner mSquareMaxSpinner;
    @BindView(R.id.square_footage_lay)
    RelativeLayout mSquareFootageLay;
    @BindView(R.id.square_footage_spin_lay)
    LinearLayout mSquareSpinnerLay;

    @BindView(R.id.days_on_rb_img)
    ImageView mDaysOnRbImg;
    @BindView(R.id.days_on_rb_txt)
    TextView mDaysOnRbTxt;
    @BindView(R.id.days_on_rb_spin)
    Spinner mDaysOnRbSpinner;
    @BindView(R.id.days_on_rb_lay)
    RelativeLayout mDaysOnRbLay;
    @BindView(R.id.days_on_rb_spin_lay)
    LinearLayout mDaysOnSpinLay;
    @BindView(R.id.lot_size_img)
    ImageView mLotSizeImg;
    @BindView(R.id.lot_size_txt)
    TextView mLotSizeTxt;
    @BindView(R.id.lot_size_spin)
    Spinner mLotSizeSpinner;
    @BindView(R.id.lot_size_spin_lay)
    LinearLayout mLotSizeSpinLay;
    @BindView(R.id.year_built_spin_lay)
    LinearLayout mYearBuiltSpinLay;
    @BindView(R.id.year_built_txt)
    TextView mYearBuiltTxt;
    @BindView(R.id.year_built_img)
    ImageView mYearBuiltImg;
    @BindView(R.id.year_built_max_spin)
    Spinner mYearBuiltMaxSpinner;
    @BindView(R.id.year_built_min_spin)
    Spinner mYearBuiltMinSpinner;
    @BindView(R.id.show_lay)
    LinearLayout mShowLay;
    @BindView(R.id.empty_bg_lay)
    RelativeLayout mEmptyBgLay;



    /* views in frag_filter_for_sale*/

    @BindView(R.id.filter_for_sale_lay)
    LinearLayout mSaleFilterLay;
    @BindView(R.id.resale_lay)
    RelativeLayout mResaleLay;
    @BindView(R.id.resale_img)
    ImageView mResaleImg;
    @BindView(R.id.new_construction_lay)
    RelativeLayout mNewConstructionLay;
    @BindView(R.id.new_construction_img)
    ImageView mNewConstructionImg;
    @BindView(R.id.for_closure_lay)
    RelativeLayout mForClosureLay;
    @BindView(R.id.fore_closure_img)
    ImageView mForClosureImg;
    @BindView(R.id.open_houses_lay)
    RelativeLayout mOpenHousesLay;
    @BindView(R.id.open_houses_img)
    ImageView mOpenHousesImg;
    @BindView(R.id.reduced_prices_lay)
    RelativeLayout mReducedPricesLay;
    @BindView(R.id.reduced_prices_img)
    ImageView mReducedPricesImg;

    /*views in frag_filter_main*/
    @BindView(R.id.keyword_edit)
    EditText mKeywordEdt;
    @BindView(R.id.mls_edit)
    EditText mMlsEdt;
    @BindView(R.id.show_more_txt)
    TextView mShowMoreTxt;
    @BindView(R.id.reset_filters_txt)
    TextView mResetFiltersTxt;
    @BindView(R.id.cancel_txt)
    TextView mCancelTxt;
    @BindView(R.id.apply_txt)
    TextView mApplyTxt;
    //    @BindView(R.id.for_sale_view_line)
//    View mForSaleViewLine;
//    @BindView(R.id.for_rent_view_line)
//    View mForRentViewLine;
    @BindView(R.id.no_fee_img)
    ImageView mNoFeeImg;
    @BindView(R.id.no_fee_lay)
    RelativeLayout mNoFeeLay;
    @BindView(R.id.keywords_flow_lay)
    FlowLayout mKeyFlowLay;
    @BindView(R.id.keyword_parent_lay)
    LinearLayout mKeyWordsLay;
    @BindView(R.id.mls_lay)
    RelativeLayout mMLSLay;

    @BindView(R.id.bottom_bar)
    LinearLayout mBottomBarLay;

    /*Array list used in Spinner*/
    private ArrayList<String> mPriceMinList, mPriceMaxList, mSquareMinList, mSquareMaxList, mLotList, mDaysOnRbList, mYearMinList, mYearMaxList, mPriceSaleMinList, mPriceSaleMaxList;
    private ArrayList<String> mTempListPrice = new ArrayList<>(), mTempListSquare = new ArrayList<>(), mTempListYear = new ArrayList<>();
    private ArrayAdapter<String> mPriceMinAdapter, mPriceMaxAdapter, mSquareMaxAdapter, mSquareMinAdapter, mYearMinAdapter, mYearMaxAdapter, mLotSizeAdapter, mDaysOnRbAdapter;
    private String mPriceMinStr = "", mPriceMaxStr = "", mSquareMinStr = "", mSquareMaxStr = "", mYearMinStr = "", mYearMaxStr = "", mPropertyTypeSale = "", mPropertyTypeRent = "", mDaysOnRbStr = "", mLotSizeStr = "";


    private boolean mResaleBool = false, mNewConstructionBool = false,
            mForClosureBool = false, mOpenHousesBool = false,
            mReducedPricesBool = false, mNoFeeBool = false;
    private int mFilterType = 1;/*1  for rent and 2 for sale */
    private ArrayList<String> mStoredKeywordsRent = new ArrayList<>(), mStoredKeywordsSale = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_filter_main, container, false);
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);
        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(rootView);
        initView();


        /*For focus current fragment*/
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    default:
                        v.performClick();
                }
                return true;
            }
        });

        return rootView;
    }


    /*Fragment manual onResume*/
    @Override
    public void onFragmentResume() {
        super.onFragmentResume();
        /*set header text and header right img*/

    }

    /*InitViews*/
    private void initView() {
        AppConstants.TAG = this.getClass().getSimpleName();


        setSpinnerListValues();
        /**/
        mPriceRangeMaxSpinner.setOnItemSelectedListener(this);
        mPriceRangeMinSpinner.setOnItemSelectedListener(this);
        mLotSizeSpinner.setOnItemSelectedListener(this);
        mDaysOnRbSpinner.setOnItemSelectedListener(this);
        mSquareMaxSpinner.setOnItemSelectedListener(this);
        mSquareMinSpinner.setOnItemSelectedListener(this);
        mYearBuiltMaxSpinner.setOnItemSelectedListener(this);
        mYearBuiltMinSpinner.setOnItemSelectedListener(this);
        mShowMoreTxt.setVisibility(View.VISIBLE);

        mKeywordEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                String mKey = v.getText().toString();
                if (!mKey.isEmpty()) {
                    if (mFilterType == 1) {
                        mStoredKeywordsRent.add(mKey);
                        keyAdapterFlowLay(mStoredKeywordsRent);
                        mKeywordEdt.setText("");
                        mKeywordEdt.setHint(getString(R.string.filter_key_hints));
                    } else if (mFilterType == 2) {
                        mStoredKeywordsSale.add(mKey);
                        keyAdapterFlowLay(mStoredKeywordsSale);
                        mKeywordEdt.setText("");
                        mKeywordEdt.setHint(getString(R.string.filter_key_hints));
                    }
                }
                hideSoftKeyboard();
                return true;
            }
        });
        mMlsEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (!textView.getText().toString().isEmpty()) {
                    AppConstants.SALE_FILTER_ENTITY.setMLS(textView.getText().toString());
                }
                return true;
            }
        });
        mBottomBarLay.setVisibility(AppConstants.CALLED_FROM_ALERT_FRAG ? View.GONE : View.VISIBLE);
        setSaleFilterLay();

    }



    /*Set values to all spinners and  set adapter */


    private void setSpinnerListValues() {

        /*Assign value for min and max spinner of Price Range Rent*/

        String[] mPriceRentList = getActivity().getResources().getStringArray(R.array.PriceListRent);
        mPriceMinList = new ArrayList<>();
        Collections.addAll(mPriceMinList, mPriceRentList);
        mPriceMinList.remove(mPriceMinList.get(1));

        mPriceMaxList = new ArrayList<>();
        Collections.addAll(mPriceMaxList, mPriceRentList);
        mPriceMaxList.remove(mPriceMaxList.get(0));

        /*Assign value to  min and max spinner of Square footage*/
        String[] mSquareFootageList = getActivity().getResources().getStringArray(R.array.SquareFootageList);

        mSquareMinList = new ArrayList<>();
        Collections.addAll(mSquareMinList, mSquareFootageList);
        mSquareMinList.remove(mSquareMinList.get(1));

        mSquareMaxList = new ArrayList<>();
        Collections.addAll(mSquareMaxList, mSquareFootageList);
        mSquareMaxList.remove(mSquareMaxList.get(0));

        /*Assign value to mina and max spinner of price range sale*/
        String[] mPriceSaleList = getActivity().getResources().getStringArray(R.array.PriceSaleList);

        mPriceSaleMinList = new ArrayList<>();
        Collections.addAll(mPriceSaleMinList, mPriceSaleList);
        mPriceSaleMinList.remove(mPriceSaleMinList.get(1));


        mPriceSaleMaxList = new ArrayList<>();
        Collections.addAll(mPriceSaleMaxList, mPriceSaleList);
        mPriceSaleMaxList.remove(mPriceSaleMaxList.get(0));

        /*Year built spinner  min and max */
        String[] mYearBuiltList = getActivity().getResources().getStringArray(R.array.YearBuiltList);

        mYearMinList = new ArrayList<>();
        Collections.addAll(mYearMinList, mYearBuiltList);
        mYearMinList.remove(mYearMinList.get(1));


        mYearMaxList = new ArrayList<>();
        Collections.addAll(mYearMaxList, mYearBuiltList);
        mYearMaxList.remove(mYearMaxList.get(0));

        /*Days on Rb Spinner value*/
        String[] mDaysOnRbListArray = getActivity().getResources().getStringArray(R.array.DaysOnRbList);

        mDaysOnRbList = new ArrayList<>();
        Collections.addAll(mDaysOnRbList, mDaysOnRbListArray);

        /*Lot spinner value*/

        String[] mLotSizeArray = getActivity().getResources().getStringArray(R.array.LotSizeList);

        mLotList = new ArrayList<>();
        Collections.addAll(mLotList, mLotSizeArray);

        setSpinnerAdapterValues();
    }

    private void setPriceRentSpinnerAdapterValues(ArrayList<String> mMinList, ArrayList<String> mMaxList) {
        mPriceMinAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_item_lay, R.id.spinner_item, mMinList);
        mPriceRangeMinSpinner.setAdapter(mPriceMinAdapter);
        mPriceRangeMinSpinner.setSelection(0);

        mPriceMaxAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_item_lay, R.id.spinner_item, mMaxList);
        mPriceRangeMaxSpinner.setAdapter(mPriceMaxAdapter);
        mPriceRangeMaxSpinner.setSelection(0);
    }

    private void setSpinnerAdapterValues() {
        setPriceRentSpinnerAdapterValues(mPriceMinList, mPriceMaxList);

        mSquareMinAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_item_lay, R.id.spinner_item, mSquareMinList);
        mSquareMinSpinner.setAdapter(mSquareMinAdapter);
        mSquareMinSpinner.setSelection(0);

        mSquareMaxAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_item_lay, R.id.spinner_item, mSquareMaxList);
        mSquareMaxSpinner.setAdapter(mSquareMaxAdapter);
        mSquareMaxSpinner.setSelection(0);

        mYearMinAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_item_lay, R.id.spinner_item, mYearMinList);
        mYearBuiltMinSpinner.setAdapter(mYearMinAdapter);
        mYearBuiltMinSpinner.setSelection(0);

        mYearMaxAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_item_lay, R.id.spinner_item, mYearMaxList);
        mYearBuiltMaxSpinner.setAdapter(mYearMaxAdapter);
        mYearBuiltMaxSpinner.setSelection(0);

        mLotSizeAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_lay,
                R.id.spinner_item, mLotList);
        mLotSizeSpinner.setAdapter(mLotSizeAdapter);
        mLotSizeSpinner.setSelection(0);

        mDaysOnRbAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_item_lay, R.id.spinner_item, mDaysOnRbList);
        mDaysOnRbSpinner.setAdapter(mDaysOnRbAdapter);
        mDaysOnRbSpinner.setSelection(0);

    }

    private void setSpinnerLayGone() {
        mPriceRangeSpinnerLay.setVisibility(View.GONE);
        mSquareSpinnerLay.setVisibility(View.GONE);
        mYearBuiltSpinLay.setVisibility(View.GONE);
        mLotSizeSpinLay.setVisibility(View.GONE);
        mDaysOnSpinLay.setVisibility(View.GONE);

    }


    public void setSaleFilterLay() {
        mFilterType = 2;
        mShowMoreTxt.setVisibility(View.VISIBLE);
        mEmptyBgLay.setVisibility(View.VISIBLE);
        mKeyWordsLay.setVisibility(View.GONE);
        setPriceRentSpinnerAdapterValues(mPriceSaleMinList, mPriceSaleMaxList);
        /*setting values according saved filter details*/
        if (AppConstants.CALLED_FROM_ALERT_FRAG) {
            if (AppConstants.ALERT_ENTITY.getType() != null && AppConstants.ALERT_ENTITY.getType().equals(AppConstants.SALE)) {

                String savedAlertFilterObj=AppConstants.ALERT_ENTITY.getAlert_object();
                AlertFilterObjectEntity entity = new Gson().fromJson(savedAlertFilterObj, AlertFilterObjectEntity.class);
                setFilterSavedDetails(entity.getSale(), true);
            }

        } else {
            setFilterSavedDetails(PreferenceUtil.getSaleFilterValues(getActivity()), true);
        }



                /*making spinner lay invisible */
        setSpinnerLayGone();
    }

    /*Click events*/
    @OnClick({R.id.property_type_lay, R.id.price_range_lay, R.id.square_footage_lay, R.id.show_more_txt, R.id.reset_filters_txt, R.id.no_fee_lay,
            R.id.baths_any, R.id.baths_num0, R.id.resale_lay, R.id.new_construction_lay, R.id.for_closure_lay, R.id.open_houses_lay, R.id.reduced_prices_lay, R.id.year_built_lay, R.id.lot_size_lay, R.id.days_on_rb_lay,
            R.id.baths_num1, R.id.baths_num2, R.id.baths_num3, R.id.baths_num4, R.id.baths_num5, R.id.bed_any, R.id.bed_num0, R.id.bed_num1, R.id.bed_num2, R.id.bed_num3, R.id.bed_num4, R.id.bed_num5, R.id.apply_txt, R.id.cancel_txt})
    public void onClick(View v) {
        if (getActivity() != null) {
            switch (v.getId()) {
                case R.id.apply_txt:
                    FilterAPIEntity mFilterAPIEntity = new FilterAPIEntity();
                    mFilterAPIEntity.setRent(new Gson().toJson(AppConstants.RENT_FILTER_ENTITY));
                    mFilterAPIEntity.setSale(new Gson().toJson(AppConstants.SALE_FILTER_ENTITY));
                    PreferenceUtil.storeFilterObject(getActivity(), AppConstants.RENT_FILTER_ENTITY, AppConstants.SALE_FILTER_ENTITY);

                    if (mFilterType == 1) {
                        AppConstants.TYPE_OF_FILTER = AppConstants.RENT;
                        APIRequestHandler.getInstance().filterSearchAPICall(this, AppConstants.RENT, new Gson().toJson(mFilterAPIEntity));

                    } else if (mFilterType == 2) {
                        AppConstants.TYPE_OF_FILTER = AppConstants.SALE;
                        APIRequestHandler.getInstance().filterSearchAPICall(this, AppConstants.SALE, new Gson().toJson(mFilterAPIEntity));

                    }


                    break;

                case R.id.cancel_txt:
                    ((HomeScreen) getActivity()).onBackPressed();
                    break;
                case R.id.property_type_lay:
                    if (mFilterType == 1) {
                        mPropertyTypeRent = mRentResetAllBool ? "" : mPropertyTypeRent;

                        DialogManager.getInstance().propertyTypeDialog(getActivity(), mPropertyTypeRent, new InterfaceEdtWithBtnCallback() {
                            @Override
                            public void onFirstEdtBtnClick(String firstEdtStr) {
                                mRentResetAllBool = false;
                                mPropertyTypeRent = firstEdtStr;
                                if(mPropertyTypeRent.isEmpty()){
                                    mPropertyTypeRent=getString(R.string.all_types);
                                }
                                mPropertyTypeTxt.setText(mPropertyTypeRent);
                                AppConstants.RENT_FILTER_ENTITY.setProperty_type(mPropertyTypeRent);

                            }
                        });
                    } else {
                        mPropertyTypeSale = mSaleResetAllBool ? "" : mPropertyTypeSale;

                        DialogManager.getInstance().propertyTypeDialog(getActivity(), mPropertyTypeSale, new InterfaceEdtWithBtnCallback() {
                            @Override
                            public void onFirstEdtBtnClick(String firstEdtStr) {
                                mSaleResetAllBool = false;
                                mPropertyTypeSale = firstEdtStr;
                                mPropertyTypeTxt.setText(mPropertyTypeSale);
                                AppConstants.SALE_FILTER_ENTITY.setProperty_type(mPropertyTypeSale);

                            }
                        });
                    }

                    break;

                case R.id.price_range_lay:
                 /*Showing price spinner*/
                    if (mPriceRangeSpinnerLay.getVisibility() == View.GONE) {
                        mPriceRangeSpinnerLay.setVisibility(View.VISIBLE);
                        mPriceRangeImg.setImageResource(R.drawable.up_arrow);
                    } else {
                        mPriceRangeSpinnerLay.setVisibility(View.GONE);
                        mPriceRangeImg.setImageResource(R.drawable.down_arrow);
                    }

                    break;
                case R.id.square_footage_lay:
                    if (mSquareSpinnerLay.getVisibility() == View.GONE) {
                        mSquareSpinnerLay.setVisibility(View.VISIBLE);
                        mSquareFootageImg.setImageResource(R.drawable.up_arrow);
                    } else {
                        mSquareSpinnerLay.setVisibility(View.GONE);
                        mSquareFootageImg.setImageResource(R.drawable.down_arrow);
                    }

                    break;


                case R.id.show_more_txt:
                    mShowMoreTxt.setVisibility(View.GONE);
                    mEmptyBgLay.setVisibility(View.GONE);
                    mShowLay.setVisibility(View.VISIBLE);
                    mSaleFilterLay.setVisibility(View.VISIBLE);
                    mKeyWordsLay.setVisibility(View.VISIBLE);
                    mMLSLay.setVisibility(View.VISIBLE);

                    break;
                case R.id.reset_filters_txt:
                    if (mFilterType == 1) {
                        resetRentFilters();
                    } else if (mFilterType == 2) {
                        resetSaleFilters();
                    }

                    break;
                case R.id.no_fee_lay:
                    if (mNoFeeBool) {
                        mNoFeeBool = false;
                        mNoFeeImg.setImageResource(R.drawable.toggle_off);
                        AppConstants.RENT_FILTER_ENTITY.setNo_fee(AppConstants.FAILURE_CODE);
                    } else {
                        mNoFeeBool = true;
                        mNoFeeImg.setImageResource(R.drawable.toggle_on);
                        AppConstants.SALE_FILTER_ENTITY.setNo_fee(AppConstants.SUCCESS_CODE);
                    }

                    break;

//            case R.id.for_rent_txt:
//                setRentDetailsOnclick();
//
//                break;
//            case R.id.for_sale_txt:
//                mFilterType = 2;
//                //mForSaleTxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_blue));
//                //mForSaleViewLine.setVisibility(View.VISIBLE);
//                //mForRentTxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
//                //mForRentViewLine.setVisibility(View.GONE);
//                mShowMoreTxt.setVisibility(View.VISIBLE);
//                mEmptyBgLay.setVisibility(View.VISIBLE);
//                mKeyWordsLay.setVisibility(View.GONE);
//                setPriceRentSpinnerAdapterValues(mPriceSaleMinList, mPriceSaleMaxList);
//                 /*setting values according saved filter details*/
//                setFilterSavedDetails(PreferenceUtil.getSaleFilterValues(getActivity()), true);
//
//                /*making spinner lay invisible */
//                setSpinnerLayGone();
//
//
//                break;
                case R.id.resale_lay:
                    if (mResaleBool) {
                        mResaleImg.setImageResource(R.drawable.tick_off);
                        mResaleBool = false;
                        AppConstants.RENT_FILTER_ENTITY.setResale(AppConstants.FAILURE_CODE);

                    } else {
                        mResaleImg.setImageResource(R.drawable.tick_on);
                        mResaleBool = true;
                        AppConstants.SALE_FILTER_ENTITY.setResale(AppConstants.SUCCESS_CODE);
                    }
                    break;
                case R.id.new_construction_lay:
                    if (mNewConstructionBool) {
                        mNewConstructionImg.setImageResource(R.drawable.tick_off);
                        mNewConstructionBool = false;
                        AppConstants.RENT_FILTER_ENTITY.setNew_construction(AppConstants.FAILURE_CODE);
                    } else {
                        mNewConstructionImg.setImageResource(R.drawable.tick_on);
                        mNewConstructionBool = true;
                        AppConstants.SALE_FILTER_ENTITY.setNew_construction(AppConstants.SUCCESS_CODE);
                    }
                    break;
                case R.id.for_closure_lay:
                    if (mForClosureBool) {
                        mForClosureImg.setImageResource(R.drawable.tick_off);
                        mForClosureBool = false;
                        AppConstants.RENT_FILTER_ENTITY.setFore_closure(AppConstants.FAILURE_CODE);

                    } else {
                        mForClosureImg.setImageResource(R.drawable.tick_on);
                        mForClosureBool = true;
                        AppConstants.SALE_FILTER_ENTITY.setFore_closure(AppConstants.SUCCESS_CODE);
                    }

                    break;
                case R.id.open_houses_lay:
                    if (mOpenHousesBool) {
                        mOpenHousesImg.setImageResource(R.drawable.tick_off);
                        mOpenHousesBool = false;
                        AppConstants.RENT_FILTER_ENTITY.setOpen_house(AppConstants.FAILURE_CODE);
                    } else {
                        mOpenHousesImg.setImageResource(R.drawable.tick_on);
                        mOpenHousesBool = true;
                        AppConstants.SALE_FILTER_ENTITY.setOpen_house(AppConstants.SUCCESS_CODE);
                    }
                    break;
                case R.id.reduced_prices_lay:
                    if (mReducedPricesBool) {
                        mReducedPricesImg.setImageResource(R.drawable.tick_off);
                        mReducedPricesBool = false;
                        AppConstants.RENT_FILTER_ENTITY.setReduced_prices(AppConstants.FAILURE_CODE);

                    } else {
                        mReducedPricesImg.setImageResource(R.drawable.tick_on);
                        mReducedPricesBool = true;
                        AppConstants.SALE_FILTER_ENTITY.setReduced_prices(AppConstants.SUCCESS_CODE);
                    }
                    break;
                case R.id.year_built_lay:
                    if (mYearBuiltSpinLay.getVisibility() == View.GONE) {
                        mYearBuiltSpinLay.setVisibility(View.VISIBLE);
                        mYearBuiltImg.setImageResource(R.drawable.up_arrow);
                    } else {
                        mYearBuiltSpinLay.setVisibility(View.GONE);
                        mYearBuiltImg.setImageResource(R.drawable.down_arrow);
                    }
                    break;
                case R.id.lot_size_lay:
                    if (mLotSizeSpinLay.getVisibility() == View.GONE) {
                        mLotSizeSpinLay.setVisibility(View.VISIBLE);
                        mLotSizeImg.setImageResource(R.drawable.up_arrow);
                    } else {
                        mLotSizeSpinLay.setVisibility(View.GONE);
                        mLotSizeImg.setImageResource(R.drawable.down_arrow);
                    }
                    break;
                case R.id.days_on_rb_lay:
                    if (mDaysOnSpinLay.getVisibility() == View.GONE) {
                        mDaysOnSpinLay.setVisibility(View.VISIBLE);
                        mDaysOnRbImg.setImageResource(R.drawable.up_arrow);
                    } else {
                        mDaysOnSpinLay.setVisibility(View.GONE);
                        mDaysOnRbImg.setImageResource(R.drawable.down_arrow);
                    }
                    break;
                case R.id.bed_any:
                case R.id.bed_num0:
                case R.id.bed_num1:
                case R.id.bed_num2:
                case R.id.bed_num3:
                case R.id.bed_num4:
                case R.id.bed_num5:
                    setBedTextBgColor(v.getId());
                    break;
                case R.id.baths_any:
                case R.id.baths_num0:
                case R.id.baths_num1:
                case R.id.baths_num2:
                case R.id.baths_num3:
                case R.id.baths_num4:
                case R.id.baths_num5:
                    setBathTextBgColor(v.getId());
                    break;

            }
        }

    }


    private void setBathTextBgColor(int id) {
        mBathAnyTxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_blue));
        mBath0Txt.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_blue));
        mBath1Txt.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_blue));
        mBath2Txt.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_blue));
        mBath3Txt.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_blue));
        mBath4Txt.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_blue));
        mBath5Txt.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_blue));

        mBathAnyTxt.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        mBath0Txt.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        mBath1Txt.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        mBath2Txt.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        mBath3Txt.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        mBath4Txt.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        mBath5Txt.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));

        switch (id) {
            case R.id.baths_any:
                if (mFilterType == 1) {
                    AppConstants.RENT_FILTER_ENTITY.setBaths("");
                } else if (mFilterType == 2) {
                    AppConstants.SALE_FILTER_ENTITY.setBaths("");
                }
                mBathAnyTxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                mBathAnyTxt.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.app_blue));
                break;
            case R.id.baths_num0:
                if (mFilterType == 1) {
                    AppConstants.RENT_FILTER_ENTITY.setBaths(AppConstants.FAILURE_CODE);
                } else if (mFilterType == 2) {
                    AppConstants.SALE_FILTER_ENTITY.setBaths(AppConstants.FAILURE_CODE);
                }
                mBath0Txt.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                mBath0Txt.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.app_blue));
                break;
            case R.id.baths_num1:
                if (mFilterType == 1) {
                    AppConstants.RENT_FILTER_ENTITY.setBaths(AppConstants.SUCCESS_CODE);
                } else if (mFilterType == 2) {
                    AppConstants.SALE_FILTER_ENTITY.setBaths(AppConstants.SUCCESS_CODE);
                }
                mBath1Txt.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                mBath1Txt.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.app_blue));
                break;
            case R.id.baths_num2:
                if (mFilterType == 1) {
                    AppConstants.RENT_FILTER_ENTITY.setBaths(getString(R.string.two));
                } else if (mFilterType == 2) {
                    AppConstants.SALE_FILTER_ENTITY.setBaths(getString(R.string.two));
                }
                mBath2Txt.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                mBath2Txt.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.app_blue));
                break;
            case R.id.baths_num3:
                if (mFilterType == 1) {
                    AppConstants.RENT_FILTER_ENTITY.setBaths(getString(R.string.three));
                } else if (mFilterType == 2) {
                    AppConstants.SALE_FILTER_ENTITY.setBaths(getString(R.string.three));
                }
                mBath3Txt.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                mBath3Txt.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.app_blue));
                break;
            case R.id.baths_num4:
                if (mFilterType == 1) {
                    AppConstants.RENT_FILTER_ENTITY.setBaths(getString(R.string.four));
                } else if (mFilterType == 2) {
                    AppConstants.SALE_FILTER_ENTITY.setBaths(getString(R.string.four));
                }
                mBath4Txt.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                mBath4Txt.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.app_blue));
                break;
            case R.id.baths_num5:
                if (mFilterType == 1) {
                    AppConstants.RENT_FILTER_ENTITY.setBaths(getString(R.string.five));
                } else if (mFilterType == 2) {
                    AppConstants.SALE_FILTER_ENTITY.setBaths(getString(R.string.five));
                }
                mBath5Txt.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                mBath5Txt.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.app_blue));
                break;

        }


    }

    private void setBedTextBgColor(int id) {

        mBedAnyTxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_blue));
        mBed0Txt.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_blue));
        mBed1Txt.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_blue));
        mBed2Txt.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_blue));
        mBed3Txt.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_blue));
        mBed4Txt.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_blue));
        mBed5Txt.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_blue));

        mBedAnyTxt.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        mBed0Txt.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        mBed1Txt.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        mBed2Txt.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        mBed3Txt.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        mBed4Txt.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        mBed5Txt.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));


        switch (id) {
            case R.id.bed_any:
                if (mFilterType == 1) {
                    AppConstants.RENT_FILTER_ENTITY.setBeds("");
                } else if (mFilterType == 2) {
                    AppConstants.SALE_FILTER_ENTITY.setBeds("");
                }
                mBedAnyTxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                mBedAnyTxt.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.app_blue));
                break;
            case R.id.bed_num0:
                if (mFilterType == 1) {
                    AppConstants.RENT_FILTER_ENTITY.setBeds(AppConstants.FAILURE_CODE);
                } else if (mFilterType == 2) {
                    AppConstants.SALE_FILTER_ENTITY.setBeds(AppConstants.FAILURE_CODE);
                }
                mBed0Txt.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                mBed0Txt.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.app_blue));
                break;
            case R.id.bed_num1:
                if (mFilterType == 1) {
                    AppConstants.RENT_FILTER_ENTITY.setBeds(AppConstants.SUCCESS_CODE);
                } else if (mFilterType == 2) {
                    AppConstants.SALE_FILTER_ENTITY.setBeds(AppConstants.SUCCESS_CODE);
                }
                mBed1Txt.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                mBed1Txt.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.app_blue));

                break;
            case R.id.bed_num2:
                if (mFilterType == 1) {
                    AppConstants.RENT_FILTER_ENTITY.setBeds(AppConstants.TWO);
                } else if (mFilterType == 2) {
                    AppConstants.SALE_FILTER_ENTITY.setBeds(AppConstants.TWO);
                }
                mBed2Txt.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                mBed2Txt.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.app_blue));
                break;
            case R.id.bed_num3:
                if (mFilterType == 1) {
                    AppConstants.RENT_FILTER_ENTITY.setBeds(AppConstants.THREE);
                } else if (mFilterType == 2) {
                    AppConstants.SALE_FILTER_ENTITY.setBeds(AppConstants.THREE);
                }
                mBed3Txt.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                mBed3Txt.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.app_blue));
                break;
            case R.id.bed_num4:
                if (mFilterType == 1) {
                    AppConstants.RENT_FILTER_ENTITY.setBeds(AppConstants.FOUR);

                } else if (mFilterType == 2) {
                    AppConstants.SALE_FILTER_ENTITY.setBeds(AppConstants.FOUR);

                }
                mBed4Txt.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                mBed4Txt.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.app_blue));
                break;
            case R.id.bed_num5:
                if (mFilterType == 1) {
                    AppConstants.RENT_FILTER_ENTITY.setBeds(AppConstants.FIVE);

                } else if (mFilterType == 2) {
                    AppConstants.SALE_FILTER_ENTITY.setBeds(AppConstants.FIVE);

                }
                mBed5Txt.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                mBed5Txt.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.app_blue));
                break;

        }


    }

    private void setBathBedTxtBg(FilterEntity mFilterEntity, boolean isBed) {

        String num = isBed ? mFilterEntity.getBeds() : mFilterEntity.getBaths();
        switch (num) {
            case "":
                if (isBed) {
                    setBedTextBgColor(R.id.bed_any);
                } else {
                    setBathTextBgColor(R.id.baths_any);
                }
                break;
            case AppConstants.FAILURE_CODE:
                if (isBed) {
                    setBedTextBgColor(R.id.bed_num0);
                } else {
                    setBathTextBgColor(R.id.baths_num0);
                }
                break;
            case AppConstants.SUCCESS_CODE:
                if (isBed) {
                    setBedTextBgColor(R.id.bed_num1);
                } else {
                    setBathTextBgColor(R.id.baths_num1);
                }
                break;
            case AppConstants.TWO:
                if (isBed) {
                    setBedTextBgColor(R.id.bed_num2);
                } else {
                    setBathTextBgColor(R.id.baths_num2);
                }
                break;
            case AppConstants.THREE:
                if (isBed) {
                    setBedTextBgColor(R.id.bed_num3);
                } else {
                    setBathTextBgColor(R.id.baths_num3);
                }
                break;
            case AppConstants.FOUR:
                if (isBed) {
                    setBedTextBgColor(R.id.bed_num4);
                } else {
                    setBathTextBgColor(R.id.baths_num4);
                }
                break;
            case AppConstants.FIVE:
                if (isBed) {
                    setBedTextBgColor(R.id.bed_num5);
                } else {
                    setBathTextBgColor(R.id.baths_num5);
                }
                break;

        }

    }


    private void resetRentFilters() {

        AppConstants.RENT_FILTER_ENTITY.setPrice_range_min("");
        AppConstants.RENT_FILTER_ENTITY.setPrice_range_max("");
        AppConstants.RENT_FILTER_ENTITY.setProperty_type("");
        AppConstants.RENT_FILTER_ENTITY.setBeds("");
        AppConstants.RENT_FILTER_ENTITY.setBaths("");
        AppConstants.RENT_FILTER_ENTITY.setSquare_footage_min("");
        AppConstants.RENT_FILTER_ENTITY.setSquare_footage_max("");
        AppConstants.RENT_FILTER_ENTITY.setKeywords("");
        mRentResetAllBool = true;

        mPriceRangeTxt.setText(getActivity().getString(R.string.any));
        mPropertyTypeTxt.setText(getActivity().getString(R.string.all_types));
        setBedTextBgColor(R.id.bed_any);
        setBathTextBgColor(R.id.baths_any);
        mSquareTxt.setText(getActivity().getString(R.string.any));

        mPriceRangeMinSpinner.setSelection(0);
        mPriceRangeMaxSpinner.setSelection(0);
        mSquareMinSpinner.setSelection(0);
        mSquareMaxSpinner.setSelection(0);
        PreferenceUtil.storeFilterObject(getActivity(), AppConstants.RENT_FILTER_ENTITY, AppConstants.SALE_FILTER_ENTITY);

    }


    private void resetSaleFilters() {

        AppConstants.SALE_FILTER_ENTITY.setPrice_range_min("");
        AppConstants.SALE_FILTER_ENTITY.setPrice_range_max("");
        AppConstants.SALE_FILTER_ENTITY.setProperty_type("");
        AppConstants.SALE_FILTER_ENTITY.setBeds("");
        AppConstants.SALE_FILTER_ENTITY.setBaths("");
        AppConstants.SALE_FILTER_ENTITY.setNo_fee(AppConstants.FAILURE_CODE);
        AppConstants.SALE_FILTER_ENTITY.setSquare_footage_min("");
        AppConstants.SALE_FILTER_ENTITY.setSquare_footage_max("");
        AppConstants.SALE_FILTER_ENTITY.setYear_build_min("");
        AppConstants.SALE_FILTER_ENTITY.setYear_build_max("");
        AppConstants.SALE_FILTER_ENTITY.setLot_size("");
        AppConstants.SALE_FILTER_ENTITY.setDays_on_RB("");
        AppConstants.SALE_FILTER_ENTITY.setResale(AppConstants.FAILURE_CODE);
        AppConstants.SALE_FILTER_ENTITY.setNew_construction(AppConstants.FAILURE_CODE);
        AppConstants.SALE_FILTER_ENTITY.setFore_closure(AppConstants.FAILURE_CODE);
        AppConstants.SALE_FILTER_ENTITY.setOpen_house(AppConstants.FAILURE_CODE);
        AppConstants.SALE_FILTER_ENTITY.setReduced_prices(AppConstants.FAILURE_CODE);
        AppConstants.SALE_FILTER_ENTITY.setKeywords("");
        AppConstants.SALE_FILTER_ENTITY.setMLS("");

        mSaleResetAllBool = true;

        mPriceRangeTxt.setText(getActivity().getString(R.string.any));
        mPropertyTypeTxt.setText(getActivity().getString(R.string.all_types));
        setBedTextBgColor(R.id.bed_any);
        setBathTextBgColor(R.id.baths_any);
        mSquareTxt.setText(getActivity().getString(R.string.any));
        mPriceRangeMinSpinner.setSelection(0);
        mPriceRangeMaxSpinner.setSelection(0);
        mSquareMinSpinner.setSelection(0);
        mSquareMaxSpinner.setSelection(0);
        mYearBuiltTxt.setText(getActivity().getString(R.string.any));
        mLotSizeTxt.setText(getActivity().getString(R.string.any));
        mDaysOnRbTxt.setText(getActivity().getString(R.string.any));
        mYearBuiltMinSpinner.setSelection(0);
        mYearBuiltMaxSpinner.setSelection(0);
        mMlsEdt.setText("");
        mMlsEdt.setHint(getActivity().getString(R.string.id));
        mNoFeeBool = false;
        mNoFeeImg.setImageResource(R.drawable.toggle_off);
        mResaleBool = false;
        mResaleImg.setImageResource(R.drawable.tick_off);
        mNewConstructionBool = false;
        mNewConstructionImg.setImageResource(R.drawable.tick_off);
        mForClosureBool = false;
        mForClosureImg.setImageResource(R.drawable.tick_off);
        mOpenHousesBool = false;
        mOpenHousesImg.setImageResource(R.drawable.tick_off);
        mReducedPricesBool = false;
        mReducedPricesImg.setImageResource(R.drawable.tick_off);
        PreferenceUtil.storeFilterObject(getActivity(), AppConstants.RENT_FILTER_ENTITY, AppConstants.SALE_FILTER_ENTITY);

    }

    private void keyAdapterFlowLay(final ArrayList<String> keyList) {
        String mKeywordStr = "";
        if (keyList.size() > 0) {
            mKeyFlowLay.setVisibility(View.VISIBLE);
            mKeyFlowLay.removeAllViews();
            mKeywordStr = TextUtils.join(",", keyList);
            RelativeLayout mParentLay;
            TextView keyTxt;
            for (int i = 0; i < keyList.size(); i++) {
                View mView = LayoutInflater.from(getActivity()).inflate(R.layout.adapter_flow_lay, null, false);
                mParentLay = mView.findViewById(R.id.parent_lay_key);
                keyTxt = mView.findViewById(R.id.keyword_txt);

                String text = keyList.get(i);
                keyTxt.setText(text);
                mParentLay.setTag(i);
                mParentLay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int selPos = (int) v.getTag();
                        keyList.remove(selPos);
                        keyAdapterFlowLay(keyList);


                    }
                });

                mKeyFlowLay.addView(mView);

            }
        } else {
            mKeyFlowLay.setVisibility(View.GONE);
        }
        if (mFilterType == 1) {
            AppConstants.RENT_FILTER_ENTITY.setKeywords(mKeywordStr);
        } else if (mFilterType == 2) {
            AppConstants.SALE_FILTER_ENTITY.setKeywords(mKeywordStr);
        }
    }

    /*Values assign to UI */
    private void setFilterSavedDetails(FilterEntity mEntity, boolean isSale) {


        if (mEntity.getPrice_range_min().isEmpty()) {
            mPriceMinStr = getString(R.string.no_min);
            mPriceRangeMinSpinner.setSelection(0);
        } else {
            mPriceMinStr = "$" + mEntity.getPrice_range_min();
            mPriceRangeMinSpinner.setSelection(mPriceMinAdapter.getPosition(mPriceMinStr));

        }
        if (mEntity.getPrice_range_max().isEmpty()) {
            mPriceMaxStr = getString(R.string.no_max);
            mPriceRangeMaxSpinner.setSelection(0);
        } else {
            mPriceMaxStr = "$" + mEntity.getPrice_range_max();
            mPriceRangeMaxSpinner.setSelection(mPriceMaxAdapter.getPosition(mPriceMaxStr));
        }
        mPriceRangeTxt.setText(mPriceMinStr + "-" + mPriceMaxStr);

        if (mEntity.getSquare_footage_min().isEmpty()) {
            mSquareMinStr = getString(R.string.no_min);
            mSquareMinSpinner.setSelection(0);
        } else {
            mSquareMinStr = mEntity
                    .getSquare_footage_min();
            mSquareMinSpinner.setSelection(mSquareMinAdapter.getPosition(mSquareMinStr));
        }
        if (mEntity.getSquare_footage_max().isEmpty()) {
            mSquareMaxStr = getString(R.string.no_max);
            mSquareMaxSpinner.setSelection(0);
        } else {
            mSquareMaxStr = mEntity
                    .getSquare_footage_max();
            mSquareMaxSpinner.setSelection(mSquareMaxAdapter.getPosition(mSquareMaxStr));
        }
        mSquareTxt.setText(mSquareMinStr + "-" + mSquareMaxStr);

        /*change bed bg */
        setBathBedTxtBg(mEntity, true);
        /*change bath bg*/
        setBathBedTxtBg(mEntity, false);

        if (!isSale) {

            if (mEntity.getProperty_type().isEmpty()) {
                mPropertyTypeRent = getString(R.string.all_types);
            } else {
                mPropertyTypeRent = mEntity.getProperty_type();
            }
            mPropertyTypeTxt.setText(mPropertyTypeRent);
        /*rent keywords*/
            mStoredKeywordsRent.clear();
            if (!mEntity.getKeywords().isEmpty()) {
                Collections.addAll(mStoredKeywordsRent, TextUtils.split(mEntity.getKeywords(), ","));
                keyAdapterFlowLay(mStoredKeywordsRent);
            }
        }
        if (isSale) {


            if (mEntity.getProperty_type().isEmpty()) {
                mPropertyTypeSale = getString(R.string.all_types);
            } else {
                mPropertyTypeSale = mEntity.getProperty_type();
            }
            mPropertyTypeTxt.setText(mPropertyTypeSale);
            /*Sale keywords*/
            mStoredKeywordsSale.clear();
            if (!mEntity.getKeywords().isEmpty()) {
                Collections.addAll(mStoredKeywordsSale, TextUtils.split(mEntity.getKeywords(), ","));
                keyAdapterFlowLay(mStoredKeywordsSale);
            }

            if (mEntity.getYear_build_min().isEmpty()) {
                mYearMinStr = getString(R.string.no_min);
                mYearBuiltMinSpinner.setSelection(0);
            } else {
                mYearMinStr = mEntity.getYear_build_min();
                mYearBuiltMinSpinner.setSelection(mYearMinAdapter.getPosition(mYearMinStr));
            }

            if (mEntity.getYear_build_max().isEmpty()) {
                mYearMaxStr = getString(R.string.no_max);
                mYearBuiltMaxSpinner.setSelection(0);
            } else {
                mYearMaxStr = mEntity.getYear_build_max();
                mYearBuiltMaxSpinner.setSelection(mYearMaxAdapter.getPosition(mYearMaxStr));
            }
            mYearBuiltTxt.setText(mYearMinStr + "-" + mYearMaxStr);
            if (mEntity.getLot_size().isEmpty()) {
                mLotSizeStr = getString(R.string.any);
                mLotSizeSpinner.setSelection(0);
            } else {
                mLotSizeStr = mEntity.getLot_size()
                        + "" + "sqft";
                mLotSizeSpinner.setSelection(mLotSizeAdapter.getPosition(mLotSizeStr));
            }
            mLotSizeTxt.setText(mLotSizeStr);

            if (mEntity.getDays_on_RB().isEmpty()) {
                mDaysOnRbStr = getString(R.string.any);
                mDaysOnRbSpinner.setSelection(0);
            } else {
                mDaysOnRbStr = mEntity.getDays_on_RB();
                mDaysOnRbSpinner.setSelection(mDaysOnRbAdapter.getPosition(mDaysOnRbStr));
            }
            mDaysOnRbTxt.setText(mDaysOnRbStr);


            mNoFeeBool = mEntity.getNo_fee().equals(AppConstants.SUCCESS_CODE);
            mNoFeeImg.setImageResource(mEntity.getNo_fee().equals(AppConstants.SUCCESS_CODE) ? R.drawable.toggle_on : R.drawable.toggle_off);


            mResaleBool = mEntity.getResale().equals(AppConstants.SUCCESS_CODE);
            mResaleImg.setImageResource(mEntity.getResale().equals(AppConstants.SUCCESS_CODE) ? R.drawable.tick_on : R.drawable.tick_off);

            mForClosureBool = mEntity.getFore_closure().equals(AppConstants.SUCCESS_CODE);
            mForClosureImg.setImageResource(mEntity.getFore_closure().equals(AppConstants.SUCCESS_CODE) ? R.drawable.tick_on : R.drawable.tick_off);

            mNewConstructionBool = mEntity.getNew_construction().equals(AppConstants.SUCCESS_CODE);
            mNewConstructionImg.setImageResource(mEntity.getNew_construction().equals(AppConstants.SUCCESS_CODE) ? R.drawable.tick_on : R.drawable.tick_off);

            mReducedPricesBool = mEntity.getReduced_prices().equals(AppConstants.SUCCESS_CODE);
            mReducedPricesImg.setImageResource(mEntity.getReduced_prices().equals(AppConstants.SUCCESS_CODE) ? R.drawable.tick_on : R.drawable.tick_off);

            mOpenHousesBool = mEntity.getOpen_house().equals(AppConstants.SUCCESS_CODE);
            mOpenHousesImg.setImageResource(mEntity.getOpen_house().equals(AppConstants.SUCCESS_CODE) ? R.drawable.tick_on : R.drawable.tick_off);

            if (!mEntity.getMLS().isEmpty()) {
                mMlsEdt.setText(mEntity.getMLS());
            }
        }

    }

    /*item selected listener for all spinner */
    @Override
    public void onItemSelected(AdapterView<?> parent, View arg1, int pos,
                               long arg3) {

        switch (parent.getId()) {

            case R.id.price_range_min_spin:
                mTempListPrice.clear();
                if (mFilterType == 1) {
                    mTempListPrice.addAll(mPriceMaxList);
                } else {
                    mTempListPrice.addAll(mPriceSaleMaxList);
                }
                mPriceMinStr = (String) parent.getItemAtPosition(pos);
                mPriceRangeTxt.setText(mPriceMinStr + " - " + getString(R.string.no_max));
                if (mFilterType == 1) {
                    AppConstants.RENT_FILTER_ENTITY.setPrice_range_min(mPriceMinStr.replace(getString(R.string.dollor), ""));
                    AppConstants.RENT_FILTER_ENTITY.setPrice_range_max("");
                } else if (mFilterType == 2) {
                    AppConstants.SALE_FILTER_ENTITY.setPrice_range_min(mPriceMinStr.replace(getString(R.string.dollor), ""));
                    AppConstants.SALE_FILTER_ENTITY.setPrice_range_max("");
                }

                if (mPriceMinStr.equalsIgnoreCase(mTempListPrice.get(pos))) {
                    for (int j = 1; j < pos; j++) {
                        mTempListPrice.remove(1);
                    }
                    mPriceMaxAdapter = new ArrayAdapter<>(getActivity(),
                            R.layout.spinner_item_lay, R.id.spinner_item,
                            mTempListPrice);
                    mPriceRangeMaxSpinner.setAdapter(mPriceMaxAdapter);
                    mPriceMaxAdapter.notifyDataSetChanged();

                }

                break;
            case R.id.price_range_max_spin:
                mPriceMaxStr = (String) parent.getItemAtPosition(pos);
                mPriceRangeTxt.setText(mPriceMinStr + " - " + mPriceMaxStr);
                if (mFilterType == 1) {
                    AppConstants.RENT_FILTER_ENTITY.setPrice_range_max(mPriceMaxStr.replace(getString(R.string.dollor), ""));

                } else if (mFilterType == 2) {
                    AppConstants.SALE_FILTER_ENTITY.setPrice_range_max(mPriceMaxStr.replace(getString(R.string.dollor), ""));
                }

                break;
            case R.id.year_built_min_spin:
                mTempListYear.clear();
                mTempListYear.addAll(mYearMaxList);
                mYearMinStr = (String) parent.getItemAtPosition(pos);
                mYearBuiltTxt.setText(mYearMinStr + " - " + getString(R.string.no_max));

                if (mYearMinStr.equalsIgnoreCase(mTempListYear.get(pos))) {
                    for (int j = 1; j < pos; j++) {
                        mTempListYear.remove(1);
                    }
                    mYearMaxAdapter = new ArrayAdapter<>(getActivity(),
                            R.layout.spinner_item_lay, R.id.spinner_item,
                            mTempListYear);
                    mYearBuiltMaxSpinner.setAdapter(mYearMaxAdapter);
                    mYearMaxAdapter.notifyDataSetChanged();
                }
                if (mFilterType == 1) {
                    AppConstants.RENT_FILTER_ENTITY.setYear_build_min(mYearMinStr);
                    AppConstants.RENT_FILTER_ENTITY.setYear_build_max("");
                } else if (mFilterType == 2) {
                    AppConstants.SALE_FILTER_ENTITY.setYear_build_min(mYearMinStr);
                    AppConstants.SALE_FILTER_ENTITY.setYear_build_max("");
                }

                break;
            case R.id.year_built_max_spin:
                mYearMaxStr = (String) parent.getItemAtPosition(pos);
                mYearBuiltTxt.setText(mYearMinStr + " - " + mYearMaxStr);
                if (mFilterType == 1) {
                    AppConstants.RENT_FILTER_ENTITY.setYear_build_max(mYearMaxStr);
                } else if (mFilterType == 2) {
                    AppConstants.SALE_FILTER_ENTITY.setYear_build_max(mYearMaxStr);
                }
                break;
            case R.id.lot_size_spin:
                mLotSizeStr = (String) parent.getItemAtPosition(pos);
                mLotSizeTxt.setText(mLotSizeStr);
                if (mLotSizeStr.equals(getString(R.string.any))) {
                    mLotSizeStr = "";
                } else if (mLotSizeStr.equals(getString(R.string.square_feet_2))) {
                    mLotSizeStr = "2000";
                } else if (mLotSizeStr.equals(getString(R.string.square_feet_3))) {
                    mLotSizeStr = "3000";
                } else if (mLotSizeStr.equals(getString(R.string.square_feet_4))) {
                    mLotSizeStr = "4000";
                } else if (mLotSizeStr.equals(getString(R.string.square_feet_5))) {
                    mLotSizeStr = "5000";
                } else if (mLotSizeStr.equals(getString(R.string.square_feet_75))) {
                    mLotSizeStr = "7500";
                } else if (mLotSizeStr.equals(getString(R.string.qrt_acre))) {
                    mLotSizeStr = "10890";
                } else if (mLotSizeStr.equals(getString(R.string.half_acre))) {
                    mLotSizeStr = "21780";
                } else if (mLotSizeStr.equals(getString(R.string.one_acre))) {
                    mLotSizeStr = "43560";
                } else if (mLotSizeStr.equals(getString(R.string.two_acre))) {
                    mLotSizeStr = "87120";
                } else if (mLotSizeStr.equals(getString(R.string.five_acre))) {
                    mLotSizeStr = "217800";
                } else if (mLotSizeStr.equals(getString(R.string.ten_acre))) {
                    mLotSizeStr = "435600";
                }
                if (mFilterType == 1) {
                    AppConstants.RENT_FILTER_ENTITY.setLot_size(mLotSizeStr);
                } else if (mFilterType == 2) {
                    AppConstants.SALE_FILTER_ENTITY.setLot_size(mLotSizeStr);
                }
                break;
            case R.id.days_on_rb_spin:
                mDaysOnRbStr = (String) parent.getItemAtPosition(pos);
                mDaysOnRbTxt.setText(mDaysOnRbStr);
                if (mDaysOnRbStr.equals(getString(R.string.any))) {
                    mDaysOnRbStr = "";
                }
                if (mFilterType == 1) {
                    AppConstants.RENT_FILTER_ENTITY.setDays_on_RB(mDaysOnRbStr);
                } else if (mFilterType == 2) {
                    AppConstants.SALE_FILTER_ENTITY.setDays_on_RB(mDaysOnRbStr);
                }
                break;
            case R.id.square_footage_min_spin:
                mTempListSquare.clear();
                mTempListSquare.addAll(mSquareMaxList);
                mSquareMinStr = (String) parent.getItemAtPosition(pos);
                mSquareTxt.setText(mSquareMinStr + " - " + getString(R.string.no_max));
                if (mFilterType == 1) {
                    AppConstants.RENT_FILTER_ENTITY.setSquare_footage_min(mSquareMinStr);
                    AppConstants.RENT_FILTER_ENTITY.setSquare_footage_max("");

                } else if (mFilterType == 2) {
                    AppConstants.SALE_FILTER_ENTITY.setSquare_footage_min(mSquareMinStr);
                    AppConstants.SALE_FILTER_ENTITY.setSquare_footage_max("");
                }

                if (mSquareMinStr.equalsIgnoreCase(mTempListSquare.get(pos))) {
                    for (int j = 1; j < pos; j++) {
                        mTempListSquare.remove(1);
                    }
                    mSquareMaxAdapter = new ArrayAdapter<>(getActivity(),
                            R.layout.spinner_item_lay, R.id.spinner_item,
                            mTempListSquare);
                    mSquareMaxSpinner.setAdapter(mSquareMaxAdapter);
                    mSquareMaxAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.square_footage_max_spin:
                mSquareMaxStr = (String) parent.getItemAtPosition(pos);
                mSquareTxt.setText(mSquareMinStr + " - " + mSquareMaxStr);
                if (mFilterType == 1) {
                    AppConstants.RENT_FILTER_ENTITY.setSquare_footage_max(mSquareMaxStr);

                } else if (mFilterType == 2) {
                    AppConstants.SALE_FILTER_ENTITY.setSquare_footage_max(mSquareMaxStr);
                }

                break;


        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof SettingResponse) {
            SettingResponse mResponse = (SettingResponse) resObj;
            if (mResponse.getError_code().equals(AppConstants.SUCCESS_CODE)) {
                ((HomeScreen) getActivity()).addFragment(new MapFragment());
            }
        }

    }

    @Override
    public void onRequestFailure(Throwable t) {
        super.onRequestFailure(t);
    }
}
