package com.smaat.renterblock.fragments;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.adapters.FilterFindAgentAdapter;
import com.smaat.renterblock.entity.AgentFilterLocalEntity;
import com.smaat.renterblock.entity.FilterEntity;
import com.smaat.renterblock.entity.FindAgentFilterEntity;
import com.smaat.renterblock.entity.LocalSavedSearchEntity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.AddressResponse;
import com.smaat.renterblock.model.PlaceDescription;
import com.smaat.renterblock.model.PlacePredictionResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.InterfaceAPICommonCallback;
import com.smaat.renterblock.utils.InterfaceBtnCallback;
import com.smaat.renterblock.utils.InterfaceEdtWithBtnCallback;
import com.smaat.renterblock.utils.InterfaceLocalSearchEntityCallback;
import com.smaat.renterblock.utils.InterfaceTwoBtnCallback;
import com.smaat.renterblock.utils.NetworkUtil;
import com.smaat.renterblock.utils.PreferenceUtil;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.smaat.renterblock.R.id.price_range_lay;


public class AgentFilterFragment extends BaseFragment implements OnItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    @BindView(R.id.price_range_min_spin)
    Spinner mPriceMinSpin;

    @BindView(R.id.price_range_max_spin)
    Spinner mPriceMaxSpin;

    @BindView(R.id.price_range_txt)
    TextView mPriceRangeTxt;

    @BindView(R.id.user_type_txt)
    TextView mUser_Type;

    @BindView(R.id.price_range_spin_row)
    LinearLayout mPriceRangeSpinLay;

    @BindView(R.id.user_type_lay)
    LinearLayout mUserTypeLay;

    @BindView(R.id.agent_tog_btn)
    ToggleButton mAgentTogBtn;

    @BindView(R.id.broker_tog_btn)
    ToggleButton mBrokerTogBtn;

    @BindView(R.id.seller_tog_btn)
    ToggleButton mSellerTogBtn;

    @BindView(R.id.property_type_txt)
    TextView mPropertyTypeTxt;


    @BindView(R.id.saved_search_list)
    RecyclerView mSavedSearchRecView;

    @BindView(R.id.location_edit)
    EditText mLocationEdtTxt;

    @BindView(R.id.keywords_edit)
    EditText mKeywordsEdtTxt;
    private ArrayList<String> mPriceMinList = new ArrayList<>();
    private ArrayList<String> mPriceMaxList = new ArrayList<>();
    private ArrayList<String> mTempListPrice = new ArrayList<>();
    private ArrayList<String> price_range = new ArrayList<String>();
    private ArrayList<String> prices_range = new ArrayList<String>();
    private ArrayAdapter<String> mPriceMinAdapter;
    private ArrayAdapter<String> mPriceMaxAdapter;
    private LocationSettingsRequest.Builder mLocSettingsReqBuilder;
    private PendingResult<LocationSettingsResult> mPendingResult;
    private final int REQUEST_CHECK_SETTINGS = 300;
    private GoogleApiClient mGoogleApiClient;
    private String mPriceMin = "", mPriceMax = "", mSplitPriceMin = "",
            mSplitPriceMax = "", mAgentStr = "", mBrokerStr = "", mSellerStr = "", mUserTyepStr = "", mPropertyTypeStr = "";
    private String mLocationStr = "", mKeywordsStr = "", mSelectTypeStr = "", mPriceRangeMinStr = "", mPriceRangeMinNum = "", mPriceRangeMaxStr = "", mPriceRangeMaxNum = "", mPropertyType = "",
            mLimitStr = "10", mStartStr = "0", mLatitudeStr = "", mLongitudeStr = "";
    private FilterFindAgentAdapter mFilterFindAgentAdapter;
    private boolean mBoolAgent = false, mBoolBroker = false, mBoolSeller = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_agents_filter, container, false);
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);
        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(rootView);
        initView();
        /*For focus current fragment*/

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

        /* If the value of visibleInt is zero,  the view will set gone. Or if the value of visibleInt is one,  the view will set visible. Or else, the view will set gone*/
        if (getActivity() != null) {
             /*Slide menu action*/
            ((HomeScreen) getActivity()).setDrawerAction(false);

            /*set header first  img*/
            ((HomeScreen) getActivity()).setHeaderLeftFirstImgLayVisible(1);
            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.mipmap.app_icon, 2);

            /*set header second img*/
            ((HomeScreen) getActivity()).setHeaderLeftSecondImgLayVisible(R.mipmap.app_icon, 0);
            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.mipmap.app_icon, 0,"");

            /*set header third txt*/
            ((HomeScreen) getActivity()).setHeaderLeftThirdTxtLayVisible(getString(R.string.app_name), 0);
            ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.app_name), 0);

            /*set header txt*/
            ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.update_filter), 1);
            ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.app_name), 0);

        }
        /*set header text and header right img*/

    }

    private void initView() {

        AppConstants.TAG = this.getClass().getSimpleName();
        setSpinnerListValues();
        mPropertyTypeTxt.setText(getString(R.string.all_types));
        initGoogleAPIClient();
        mLocationEdtTxt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mUserTypeLay.setVisibility(View.GONE);
                ArrayList<LocalSavedSearchEntity> SavedSearchList=PreferenceUtil.getUserDetailsRes(getActivity()).getSavedsearch();
                if(SavedSearchList.size()>0){
                setPlaceSearchAdapter(SavedSearchList, true);}
                return false;
            }
        });
        setFilterDetail();
        mSavedSearchRecView.setVisibility(View.GONE);

        mLocationEdtTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    APIRequestHandler.getInstance().callPlaceSuggestionAPI(charSequence.toString(), AgentFilterFragment.this);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private void setSpinnerListValues() {
        addSellPriceRangeList();
        mPriceMinList = price_range;
        mPriceMaxList = prices_range;
        setSpinnerAdapterValues();
    }

    private void setSpinnerAdapterValues() {

        mPriceMinAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_item_lay, R.id.spinner_item, mPriceMinList);
        mPriceMinSpin.setAdapter(mPriceMinAdapter);


        mPriceMaxAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_item_lay, R.id.spinner_item, mPriceMaxList);
        mPriceMaxSpin.setAdapter(mPriceMaxAdapter);

        mPriceMinSpin.setOnItemSelectedListener(this);
        mPriceMaxSpin.setOnItemSelectedListener(this);

    }
    private void setFilterDetail(){
        AgentFilterLocalEntity entity=PreferenceUtil.getAgentFilterDetail(getActivity());
        mLocationEdtTxt.setText(entity.getLocation());
        mKeywordsEdtTxt.setText(entity.getName());
        mUser_Type.setText(entity.getType());
        mPropertyTypeTxt.setText(entity.getProperty_experties());
        String  mPriceMinStr, mPriceMaxStr;
        if (entity.getPrice_range_min().isEmpty()) {
            mPriceMinStr = getString(R.string.no_min);
            mPriceMinSpin.setSelection(0);
        } else {
            mPriceMinStr = "$" + entity.getPrice_range_min();
            mPriceMinSpin.setSelection(mPriceMinAdapter.getPosition(mPriceMinStr));

        }
        if (entity.getPrice_range_max().isEmpty()) {
            mPriceMaxStr = getString(R.string.no_max);
            mPriceMaxSpin.setSelection(0);
        } else {
            mPriceMaxStr = "$" + entity.getPrice_range_max();
            mPriceMaxSpin.setSelection(mPriceMaxAdapter.getPosition(mPriceMaxStr));
        }
        mPriceRangeTxt.setText(mPriceMinStr + "-" + mPriceMaxStr);
        if(entity.getType().contains("Agents")){
            mBoolAgent=true;
            mAgentStr = "Agents";
        }
        if(entity.getType().contains("Brokers")){
            mBoolBroker=true;
            mBrokerStr="Brokers";
        }
        if(entity.getType().contains("Sellers")){
            mBoolSeller=true;
            mSellerStr="Sellers";
        }
        mBrokerTogBtn.setBackgroundResource(mBoolBroker?R.drawable.tick_on:R.drawable.tick_off);
        mAgentTogBtn.setBackgroundResource(mBoolAgent?R.drawable.tick_on:R.drawable.tick_off);
        mSellerTogBtn.setBackgroundResource(mBoolSeller?R.drawable.tick_on:R.drawable.tick_off);

    }

    private void addSellPriceRangeList() {
        price_range.clear();
        prices_range.clear();
        price_range.add("No Min");
        price_range.add("$10,000");
        price_range.add("$20,000");
        price_range.add("$30,000");
        price_range.add("$50,000");
        price_range.add("$100,000");
        price_range.add("$130,000");
        price_range.add("$150,000");
        price_range.add("$200,000");
        price_range.add("$250,000");
        price_range.add("$300,000");
        price_range.add("$350,000");
        price_range.add("$400,000");
        price_range.add("$450,000");
        price_range.add("$500,000");
        price_range.add("$550,000");
        price_range.add("$600,000");
        price_range.add("$650,000");
        price_range.add("$700,000");
        price_range.add("$750,000");
        price_range.add("$800,000");
        price_range.add("$850,000");
        price_range.add("$900,000");
        price_range.add("$950,000");
        price_range.add("$1M");
        price_range.add("$1.1M");
        price_range.add("$1.2M");
        price_range.add("$1.25M");
        price_range.add("$1.4M");
        price_range.add("$1.5M");
        price_range.add("$1.6M");
        price_range.add("$1.7M");
        price_range.add("$1.75M");
        price_range.add("$1.8M");
        price_range.add("$1.9M");
        price_range.add("$2M");
        price_range.add("$2.25M");
        price_range.add("$2.5M");
        price_range.add("$2.75M");
        price_range.add("$3M");
        price_range.add("$3.5M");
        price_range.add("$4M");
        price_range.add("$5M");
        price_range.add("$10M");
        price_range.add("$20M");

        prices_range.add("No Max");
        prices_range.add("$10,000");
        prices_range.add("$20,000");
        prices_range.add("$30,000");
        prices_range.add("$50,000");
        prices_range.add("$100,000");
        prices_range.add("$130,000");
        prices_range.add("$150,000");
        prices_range.add("$200,000");
        prices_range.add("$250,000");
        prices_range.add("$300,000");
        prices_range.add("$350,000");
        prices_range.add("$400,000");
        prices_range.add("$450,000");
        prices_range.add("$500,000");
        prices_range.add("$550,000");
        prices_range.add("$600,000");
        prices_range.add("$650,000");
        prices_range.add("$700,000");
        prices_range.add("$750,000");
        prices_range.add("$800,000");
        prices_range.add("$850,000");
        prices_range.add("$900,000");
        prices_range.add("$950,000");
        prices_range.add("$1M");
        prices_range.add("$1.1M");
        prices_range.add("$1.2M");
        prices_range.add("$1.25M");
        prices_range.add("$1.4M");
        prices_range.add("$1.5M");
        prices_range.add("$1.6M");
        prices_range.add("$1.7M");
        prices_range.add("$1.75M");
        prices_range.add("$1.8M");
        prices_range.add("$1.9M");
        prices_range.add("$2M");
        prices_range.add("$2.25M");
        prices_range.add("$2.5M");
        prices_range.add("$2.75M");
        prices_range.add("$3M");
        prices_range.add("$3.5M");
        prices_range.add("$4M");
        prices_range.add("$5M");
        prices_range.add("$10M");
        prices_range.add("$20M");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3) {

        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        if (parent.getId() == R.id.price_range_min_spin) {
            mTempListPrice = new ArrayList<>();
            mTempListPrice.addAll(mPriceMaxList);
            mPriceMin = (String) parent.getItemAtPosition(pos);
            mPriceRangeMinNum = (String) parent.getItemAtPosition(pos);
            try {
                Number priceMin = numberFormat.parse(mPriceRangeMinNum);
                mPriceRangeMinStr = priceMin.toString();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mPriceRangeTxt.setText(mPriceMin + " - " + getString(R.string.no_max));
          /*  if (mPriceMin.equals("No Min")) {
                if (mPriceMax.equals("No Max")) {
                    mPriceRangeTxt.setText(getString(R.string.any));
                } else {
                    mPriceRangeTxt.setText("No Min" + " - " + mPriceMax);
                }
            }
            mSplitPriceMin = mPriceMin.replace("$", "");
            mSplitPriceMax = "";*/

            if (mPriceMin.equalsIgnoreCase(mTempListPrice.get(pos))) {
                for (int j = 1; j < pos; j++) {
                    mTempListPrice.remove(1);
                }
                mPriceMaxAdapter = new ArrayAdapter<>(getContext(),
                        R.layout.spinner_item_lay, R.id.spinner_item,
                        mTempListPrice);
                mPriceMaxSpin.setAdapter(mPriceMaxAdapter);
                mPriceMaxAdapter.notifyDataSetChanged();
            }
        } else if (parent.getId() == R.id.price_range_max_spin) {
            mPriceRangeMaxNum = (String) parent.getItemAtPosition(pos);
            try {
                Number priceMax = numberFormat.parse(mPriceRangeMaxNum);
                mPriceRangeMaxStr = priceMax.toString();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mPriceMax = (String) parent.getItemAtPosition(pos);
            mPriceRangeTxt.setText(mPriceMin + " - " + mPriceMax);
            if (mPriceMax.equals("No Max")) {
                if (mPriceMin.equals("No Min")) {
                    mPriceRangeTxt.setText(getString(R.string.any));
                } else {
                    mPriceRangeTxt.setText(mPriceMin + " - " + "No Max");
                }
                mPriceRangeTxt.setText(getString(R.string.any));
            }
            mSplitPriceMax = mPriceMax.replace("$", "");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    @OnClick({price_range_lay, R.id.user_type_txt, R.id.agent_lay, R.id.broker_lay, R.id.seller_lay, R.id.main_lay, R.id.property_type_lay, R.id.location_edit, R.id.reset_btn, R.id.apply_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case price_range_lay:
                if (mPriceRangeSpinLay.getVisibility() == View.GONE) {
                    mPriceRangeSpinLay.setVisibility(View.VISIBLE);
                    mUserTypeLay.setVisibility(View.GONE);
                    mSavedSearchRecView.setVisibility(View.GONE);
                } else {
                    mPriceRangeSpinLay.setVisibility(View.GONE);
                }
                break;
            case R.id.user_type_txt:
                if (mUserTypeLay.getVisibility() == View.GONE) {
                    mUserTypeLay.setVisibility(View.VISIBLE);
                    mSavedSearchRecView.setVisibility(View.GONE);
                } else {
                    mUserTypeLay.setVisibility(View.GONE);
                }
                break;
            case R.id.agent_lay:
                if (!mBoolAgent) {
                    mAgentTogBtn.setBackgroundResource(R.drawable.tick_on);
                    mAgentStr = "Agents";
                    mBoolAgent = true;
                } else {
                    mAgentTogBtn.setBackgroundResource(R.drawable.tick_off);
                    mAgentStr = "";
                    mBoolAgent = false;
                }
                setUserTextType();
                break;
            case R.id.broker_lay:
                if (!mBoolBroker) {
                    mBrokerTogBtn.setBackgroundResource(R.drawable.tick_on);
                    mBrokerStr = "Brokers";
                    mBoolBroker = true;
                } else {
                    mBrokerTogBtn.setBackgroundResource(R.drawable.tick_off);
                    mBrokerStr = "";
                    mBoolBroker = false;
                }
                setUserTextType();
                break;
            case R.id.seller_lay:
                if (!mBoolSeller) {
                    mSellerTogBtn.setBackgroundResource(R.drawable.tick_on);
                    mSellerStr = "Sellers";
                    mBoolSeller = true;
                } else {
                    mSellerTogBtn.setBackgroundResource(R.drawable.tick_off);
                    mSellerStr = "";
                    mBoolSeller = false;
                }
                setUserTextType();
                break;
            case R.id.main_lay:
                mUserTypeLay.setVisibility(View.GONE);
                mSavedSearchRecView.setVisibility(View.GONE);
                break;
            case R.id.property_type_lay:
                DialogManager.getInstance().propertyTypeDialog(getContext(), mPropertyTypeStr, new InterfaceEdtWithBtnCallback() {
                    @Override
                    public void onFirstEdtBtnClick(String firstEdtStr) {
                        mPropertyTypeStr = firstEdtStr;
                        mPropertyTypeTxt.setText(mPropertyTypeStr);
                    }
                });
                mUserTypeLay.setVisibility(View.GONE);
                mSavedSearchRecView.setVisibility(View.GONE);
                break;


            case R.id.reset_btn:
                mUser_Type.setText("");
                mLocationEdtTxt.setText("");
                mPropertyTypeTxt.setText(getString(R.string.all_types));
                mPriceRangeTxt.setText(getString(R.string.any));
                mKeywordsEdtTxt.setText("");
                mUserTypeLay.setVisibility(View.GONE);
                mSavedSearchRecView.setVisibility(View.GONE);
                break;
            case R.id.apply_btn:
                onApplyValidation();
                mUserTypeLay.setVisibility(View.GONE);
                mSavedSearchRecView.setVisibility(View.GONE);
                break;
        }
    }

    private void onApplyValidation() {

        if (getActivity() != null) {
            mLocationStr = mLocationEdtTxt.getText().toString().trim();
            mKeywordsStr = mKeywordsEdtTxt.getText().toString().trim();
            mSelectTypeStr = mUser_Type.getText().toString().trim();
            mPropertyType = mPropertyTypeTxt.getText().toString().trim();

            AgentFilterLocalEntity filterLocalEntity = new AgentFilterLocalEntity();
            filterLocalEntity.setLatitude(mLatitudeStr);
            filterLocalEntity.setLongitude(mLongitudeStr);
            filterLocalEntity.setLocation(mLocationStr);
            filterLocalEntity.setName(mKeywordsStr);
            filterLocalEntity.setType(mSelectTypeStr);
            filterLocalEntity.setProperty_experties(mPropertyType);
            filterLocalEntity.setPrice_range_min(mPriceRangeMinStr);
            filterLocalEntity.setPrice_range_max(mPriceRangeMaxStr);
            filterLocalEntity.setUpdated(true);
            PreferenceUtil.storeAgentFilterDetail(getActivity(),filterLocalEntity);
            ((HomeScreen) getActivity()).onBackPressed();
        }

//        APIRequestHandler.getInstance().findAnAgentAPICall(mKeywordsStr, mLocationStr, mPriceRangeMinStr, mPriceRangeMaxStr, mPropertyType, mLimitStr, mStartStr, mSelectTypeStr, mLatitudeStr, mLongitudeStr, false, AgentFilterFragment.this);
    }

    private void setUserTextType() {
        if (!mAgentStr.equalsIgnoreCase("") && mBrokerStr.equalsIgnoreCase("") && mSellerStr.equalsIgnoreCase("")) {
            mUserTyepStr = mAgentStr;
        } else if (mAgentStr.equalsIgnoreCase("") && !mBrokerStr.equalsIgnoreCase("") && mSellerStr.equalsIgnoreCase("")) {
            mUserTyepStr = mBrokerStr;
        } else if (mAgentStr.equalsIgnoreCase("") && mBrokerStr.equalsIgnoreCase("") && !mSellerStr.equalsIgnoreCase("")) {
            mUserTyepStr = mSellerStr;
        } else if (!mAgentStr.equalsIgnoreCase("") && !mBrokerStr.equalsIgnoreCase("") && mSellerStr.equalsIgnoreCase("")) {
            mUserTyepStr = mAgentStr + "," + mBrokerStr;
        } else if (mAgentStr.equalsIgnoreCase("") && !mBrokerStr.equalsIgnoreCase("") && !mSellerStr.equalsIgnoreCase("")) {
            mUserTyepStr = mBrokerStr + "," + mSellerStr;
        } else if (!mAgentStr.equalsIgnoreCase("") && mBrokerStr.equalsIgnoreCase("") && !mSellerStr.equalsIgnoreCase("")) {
            mUserTyepStr = mAgentStr + "," + mSellerStr;
        } else if (!mAgentStr.equalsIgnoreCase("") && !mBrokerStr.equalsIgnoreCase("") && !mSellerStr.equalsIgnoreCase("")) {
            mUserTyepStr = mAgentStr + "," + mBrokerStr + "," + mSellerStr;
        } else if (mAgentStr.equalsIgnoreCase("") && mBrokerStr.equalsIgnoreCase("") && mSellerStr.equalsIgnoreCase("")) {
            mUserTyepStr = mAgentStr + mBrokerStr + mSellerStr;
        }
        mUser_Type.setText(mUserTyepStr);
    }


    private void setCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsAccessLocation(4);
            return;
        }

        Location currentLoc = getCurrentLatLong();
        callGoogleApiService(currentLoc.getLatitude(), currentLoc.getLongitude());
       /* mLatitudeStr = String.valueOf(currentLoc.getLatitude());
        mLongitudeStr = String.valueOf(currentLoc.getLongitude());*/
    }

    private void callGoogleApiService(double latitude, double longitude) {

//        mLocationEdtTxt.setText(AddressUtil.getAddressFromLatLng(latitude, longitude, getActivity()));

    }

    private Location getCurrentLatLong() {

        Location mLocation = new Location("");
        if (getActivity() != null) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            /*Ask for permission on locatio access
            * Set flag for call back to continue this process*/
                permissionsAccessLocation(3);
            }

            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);


            if (mLastLocation != null) {
                mLocation.setLatitude(mLastLocation.getLatitude());
                mLocation.setLongitude(mLastLocation.getLongitude());

            }
        }

        return mLocation;
    }

    private boolean permissionsAccessLocation(final int askPermissionFromIntFlag) {
        boolean addPermission = true;
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            int permissionLocation = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
            int permissionCoarseLocation = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);

            if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (permissionCoarseLocation != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            addPermission = askAccessPermission(listPermissionsNeeded, 1, new InterfaceTwoBtnCallback() {
                @Override
                public void onNegativeClick() {
                    if (askPermissionFromIntFlag == 0) {
                        initView();
                    } else if (askPermissionFromIntFlag == 1) {


                    } else if (askPermissionFromIntFlag == 2 || askPermissionFromIntFlag == 4) {
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        if (askPermissionFromIntFlag == 4) {
                            setCurrentLocation();
                        }

                    }
                }

                @Override
                public void onPositiveClick() {

                }


            });
        }

        return addPermission;
    }

    /*Init Google API clients*/
    private void initGoogleAPIClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(AgentFilterFragment.this)
                    .addOnConnectionFailedListener(AgentFilterFragment.this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (getActivity() != null) {

            LocationManager mLocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (mLocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
                    initGoogleAPIClient();
                } else {
                    screenAPICall();
                }
            } else {
                mLocSettingsReqBuilder = new LocationSettingsRequest.Builder().addLocationRequest(createLocationRequest());
                mPendingResult = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, mLocSettingsReqBuilder.build());
                mPendingResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                    @Override
                    public void onResult(@NonNull LocationSettingsResult result) {
                        final Status status = result.getStatus();
                        switch (status.getStatusCode()) {
                            case LocationSettingsStatusCodes.SUCCESS:
                                // API call.
                                if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
                                    initGoogleAPIClient();
                                } else {
                                    screenAPICall();
                                }
                                break;
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied, but this can be fixed
                                // by showing the user a dialog.
                                try {
                                    // and check the result in onActivityResult().
                                    status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException e) {
                                    // Ignore the error.
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // Location settings are not satisfied. However, we have no way
                                // to fix the settings so we won't show the dialog.
                                break;
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        DialogManager.getInstance().showToast(getActivity(), "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        DialogManager.getInstance().showToast(getActivity(), connectionResult.getErrorMessage());
    }

    private void screenAPICall() {
           /*Check for internet connection*/
        if (getActivity() != null) {
            if (NetworkUtil.isNetworkAvailable(getActivity())) {
                setCurrentLocation();
//                startLocationUpdate();
            } else {
            /*Alert message will be appeared if there is no internet connection*/
                DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.no_internet), new InterfaceBtnCallback() {
                    @Override
                    public void onPositiveClick() {

                    }
                });

            }
        }
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);              // milli sec
        locationRequest.setFastestInterval(1000);      // milli sec
        locationRequest.setSmallestDisplacement(25f);  // in fet
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof FindAgentFilterEntity) {
            FindAgentFilterEntity mFindAgentFilterResponse = (FindAgentFilterEntity) resObj;
            if (mFindAgentFilterResponse.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                DialogManager.getInstance().showAlertPopup(getContext(), "Success", new InterfaceBtnCallback() {
                    @Override
                    public void onPositiveClick() {

                    }
                });
            } else {
                DialogManager.getInstance().showAlertPopup(getContext(), "Denied", new InterfaceBtnCallback() {
                    @Override
                    public void onPositiveClick() {

                    }
                });
            }
        } else if (resObj instanceof PlacePredictionResponse) {

            PlacePredictionResponse mResponse = (PlacePredictionResponse) resObj;

            ArrayList<PlaceDescription> mPlaceList = mResponse.getPredictions();

            ArrayList<LocalSavedSearchEntity> mSuggestionList = new ArrayList<>();
            if (mPlaceList.size() > 0) {
                for (int i = 0; i < mPlaceList.size(); i++) {
                    LocalSavedSearchEntity entity = new LocalSavedSearchEntity();
                    entity.setLocation(mPlaceList.get(i).getDescription());
                    entity.setLatitude(mPlaceList.get(i).getLatitude());
                    entity.setLatitude(mPlaceList.get(i).getLongitude());
                    entity.setPlaceID(mPlaceList.get(i).getPlace_id());
                    mSuggestionList.add(entity);
                }
                setPlaceSearchAdapter(mSuggestionList, false);
            }
        } else if (resObj instanceof AddressResponse) {
            AddressResponse userAddressRes = (AddressResponse) resObj;
            if (userAddressRes.getResults().size() > 0) {
                mLocationEdtTxt.setText(userAddressRes.getResults().get(0).getFormatted_address());
            }
        }
    }

    private void setPlaceSearchAdapter(final ArrayList<LocalSavedSearchEntity> mList, boolean isSavedLocation) {
        if (getActivity() != null) {
            if (mList.size() > 0) {
                mSavedSearchRecView.setVisibility(View.VISIBLE);
                if (isSavedLocation) {
                    Location location = getCurrentLatLong();
                    FilterEntity filterEntity = new FilterEntity();
                    filterEntity.setLatitude(String.valueOf(location.getLatitude()));
                    filterEntity.setLongitude(String.valueOf(location.getLongitude()));
                    filterEntity.setFilter_name("Current Location");
                    LocalSavedSearchEntity m = new LocalSavedSearchEntity();
                    m.setFilter_object(new Gson().toJson(filterEntity));
                    ArrayList<LocalSavedSearchEntity> currentLocList = new ArrayList<>();
                    currentLocList.add(m);
                    currentLocList.addAll(mList);

                    mFilterFindAgentAdapter = new FilterFindAgentAdapter(getActivity(), currentLocList, true, new InterfaceLocalSearchEntityCallback() {
                        @Override
                        public void getLocalSearchEntity(LocalSavedSearchEntity entity) {
                            mSavedSearchRecView.setVisibility(View.GONE);
                            if (entity.getFilter_object1().getFilter_name().equalsIgnoreCase(getString(R.string.current_location))) {
                                String latLngStr = entity.getFilter_object1().getLatitude() + "," + entity.getFilter_object1().getLongitude();
                                String addressURLStr = String.format(AppConstants.GET_ADDRESS_URL, latLngStr);
                                mLatitudeStr = entity.getFilter_object1().getLatitude();
                                mLongitudeStr = entity.getFilter_object1().getLongitude();
                                APIRequestHandler.getInstance().callGetUserAddressAPI(addressURLStr, AgentFilterFragment.this);
                            } else {
                                mLocationEdtTxt.setText(entity.getFilter_object1().getFilter_name());
                            }


                        }
                    });
                    GridLayoutManager manager = new GridLayoutManager(getActivity(), 1);
                    mSavedSearchRecView.setLayoutManager(manager);
                    mSavedSearchRecView.setNestedScrollingEnabled(false);
                    mSavedSearchRecView.setFocusable(false);
                    mSavedSearchRecView.setAdapter(mFilterFindAgentAdapter);


                } else {
                    mFilterFindAgentAdapter = new FilterFindAgentAdapter(getActivity(), mList, false, new InterfaceLocalSearchEntityCallback() {
                        @Override
                        public void getLocalSearchEntity(LocalSavedSearchEntity entity) {
                            mSavedSearchRecView.setVisibility(View.GONE);
                            mLocationEdtTxt.setText(entity.getLocation());
                            String addressURLStr = String.format(AppConstants.GET_DETAILS_ADDRESS_URL, mLocationEdtTxt.getText().toString());

                            APIRequestHandler.getInstance().callGetUserAddressAPICallback(getActivity(), addressURLStr, new InterfaceAPICommonCallback() {
                                @Override
                                public void onRequestSuccess(Object obj) {
                                    AddressResponse userAddressRes = (AddressResponse) obj;
                                    mLatitudeStr = userAddressRes.getResults().get(0).getGeometry().getLocation().getLat();
                                    mLongitudeStr = userAddressRes.getResults().get(0).getGeometry().getLocation().getLng();
                                }

                                @Override
                                public void onRequestFailure(Throwable r) {

                                }
                            });


                        }
                    });
                    GridLayoutManager manager = new GridLayoutManager(getActivity(), 1);
                    mSavedSearchRecView.setLayoutManager(manager);
                    mSavedSearchRecView.setNestedScrollingEnabled(false);
                    mSavedSearchRecView.setFocusable(false);
                    mSavedSearchRecView.setAdapter(mFilterFindAgentAdapter);
                }

            }

        }

    }


}
