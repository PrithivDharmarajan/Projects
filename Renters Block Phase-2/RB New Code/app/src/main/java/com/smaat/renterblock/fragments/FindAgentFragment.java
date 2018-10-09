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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.smaat.renterblock.R;
import com.smaat.renterblock.adapters.FindAnAgentAdapter;
import com.smaat.renterblock.entity.AgentFilterLocalEntity;
import com.smaat.renterblock.entity.ChatInputEntity;
import com.smaat.renterblock.entity.FindAgentFilterEntity;
import com.smaat.renterblock.entity.FindAgentFilterResultEntity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.AddressResponse;
import com.smaat.renterblock.model.CreateGroupChatResponse;
import com.smaat.renterblock.model.FriendsRecentListResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.InterfaceBtnCallback;
import com.smaat.renterblock.utils.InterfaceTwoBtnCallback;
import com.smaat.renterblock.utils.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FindAgentFragment extends BaseFragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, SwipeRefreshLayout.OnRefreshListener {

    private FindAnAgentAdapter mFindAgentAdapter;

    @BindView(R.id.find_an_agent_recycler_view)
    RecyclerView mFindAnAgentRecyclerView;

    @BindView(R.id.refresh_view)
    SwipeRefreshLayout mRefreshView;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.place_txt)
    TextView mAddressTxt;

    @BindView(R.id.result_count_txt)
    TextView mResultCountTxt;

    private GoogleApiClient mGoogleApiClient;
    private final int REQUEST_CHECK_SETTINGS = 300;
    private int mLimitInt = 0, mPastVisibleItemCountInt, mVisibleItemCountInt, mTotalItemCountInt;
    private LinearLayoutManager mLinearLayoutManager;
    private boolean mLoadingHiddenBool = true;
    private String mAddressStr = "";


    ArrayList<FindAgentFilterResultEntity> mFindAgentFilterListRes = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_find_agent, container, false);
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);
        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(rootView);
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

            AppConstants.TAG = this.getClass().getSimpleName();

             /*Slide menu action*/
            ((HomeScreen) getActivity()).setDrawerAction(true);

            /*set header first  img*/
            ((HomeScreen) getActivity()).setHeaderLeftFirstImgLayVisible(1);
            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.mipmap.app_icon, 0);

            /*set header second img*/
            ((HomeScreen) getActivity()).setHeaderLeftSecondImgLayVisible(R.mipmap.app_icon, 0);
            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.drawable.filter_icon, 1,"");

            /*set header third txt*/
            ((HomeScreen) getActivity()).setHeaderLeftThirdTxtLayVisible(getString(R.string.app_name), 0);
            ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.app_name), 0);

            /*set header txt*/
            ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.find_an_agent), 1);
            ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.app_name), 0);

            /*header right click*/
            ((HomeScreen) getActivity()).mHeaderRightSecondImgLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((HomeScreen) getActivity()).addFragment(new AgentFilterFragment());
                }
            });
            mFindAgentAdapter = null;

            initView();
        }
    }

    private void initView() {
        initGoogleAPIClient();


        mFindAnAgentRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && (mLinearLayoutManager != null)) //check for scroll down
                {
                    mVisibleItemCountInt = mLinearLayoutManager.getChildCount();
                    mTotalItemCountInt = mLinearLayoutManager.getItemCount();
                    mPastVisibleItemCountInt = mLinearLayoutManager.findFirstVisibleItemPosition();
                    if (mLoadingHiddenBool && (mVisibleItemCountInt + mPastVisibleItemCountInt) >= mTotalItemCountInt) {
                        //Do pagination.. i.e. fetch new data
                        mLoadingHiddenBool = false;
                        callLoadMoreList();
                    }
                }
            }
        });

              /*set refresh properties*/
        mRefreshView.setRefreshing(false);
        mRefreshView.setOnRefreshListener(this);
        mRefreshView.setColorSchemeResources(R.color.app_blue,
                R.color.app_blue,
                R.color.black,
                R.color.black_transparent);
    }

    /*Init Google API clients*/
    private void initGoogleAPIClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(FindAgentFragment.this)
                .addOnConnectionFailedListener(FindAgentFragment.this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    /* chat adapter*/
    private void setAdapter(ArrayList<FindAgentFilterResultEntity> findAgentFilterList) {
        if (getActivity() != null) {
            if (mFindAgentAdapter == null) {
                mFindAgentAdapter = new FindAnAgentAdapter(getActivity(), findAgentFilterList, this);
                mLinearLayoutManager = new LinearLayoutManager(getActivity());
                mFindAnAgentRecyclerView.setLayoutManager(mLinearLayoutManager);
                mFindAnAgentRecyclerView.setAdapter(mFindAgentAdapter);
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        mFindAgentAdapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (getActivity() != null) {

            LocationManager mLocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (mLocManager!=null&&mLocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
                    initGoogleAPIClient();
                } else {
                    callProductListAPI(mLimitInt);
                }
            } else {
                LocationSettingsRequest.Builder locSettingsReqBuilder = new LocationSettingsRequest.Builder().addLocationRequest(createLocationRequest());
                PendingResult<LocationSettingsResult> pendingResult = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, locSettingsReqBuilder.build());
                pendingResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                    @Override
                    public void onResult(@NonNull LocationSettingsResult result) {
                        final Status status = result.getStatus();
                        switch (status.getStatusCode()) {
                            case LocationSettingsStatusCodes.SUCCESS:
                                // API call.
                                if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
                                    initGoogleAPIClient();
                                } else {
                                    callProductListAPI(mLimitInt);
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

    protected LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);              // milli sec
        locationRequest.setFastestInterval(1000);      // milli sec
        locationRequest.setSmallestDisplacement(25f);  // in fet
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }


    /* Get current location */
    private Location getCurrentLatLong() {

        Location mLocation = new Location("");
        if (getActivity() != null) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            /*Ask for permission on locatio access
            * Set flag for call back to continue this process*/
                permissionsAccessLocation();
            }

            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {
                mLocation.setLatitude(mLastLocation.getLatitude());
                mLocation.setLongitude(mLastLocation.getLongitude());

            }
        }

        return mLocation;
    }


    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        mProgressBar.setVisibility(View.GONE);
        if (getActivity() != null) {

            if (resObj instanceof AddressResponse) {
                AddressResponse userAddressRes = (AddressResponse) resObj;
                if (userAddressRes.getResults().size() > 0) {
                    mAddressStr = userAddressRes.getResults().get(0).getFormatted_address();
                }

                callProductListAPI(10);
                mAddressTxt.setText(mAddressStr);
            } else if (resObj instanceof FindAgentFilterEntity) {
                FindAgentFilterEntity findAgentRes = (FindAgentFilterEntity) resObj;
                if (findAgentRes.getError_code().equals(AppConstants.SUCCESS_CODE)) {
                    if (mFindAgentFilterListRes == null || mFindAgentFilterListRes.size() == 0 || mLimitInt == 0) {
                        mFindAgentFilterListRes = new ArrayList<>();
                        mFindAgentAdapter = null;
                    }

                    mFindAgentFilterListRes.addAll(findAgentRes.getResult());
                    mResultCountTxt.setText(mFindAgentFilterListRes.size() + " " + getString(R.string.result));
                    mLoadingHiddenBool = true;
                    setAdapter(mFindAgentFilterListRes);
                }

            } else if (resObj instanceof FriendsRecentListResponse) {
                FriendsRecentListResponse mResponse = (FriendsRecentListResponse) resObj;
                DialogManager.getInstance().showAlertPopup(getActivity(), mResponse.getMsg(),
                        new InterfaceBtnCallback() {
                            @Override
                            public void onPositiveClick() {

                            }
                        });
            } else if (resObj instanceof CreateGroupChatResponse) {
                CreateGroupChatResponse chatIdRes = (CreateGroupChatResponse) resObj;
                if (chatIdRes.getError_code().equals(AppConstants.SUCCESS_CODE)) {
                    AppConstants.CHAT_INPUT_ENTITY = new ChatInputEntity();
                    AppConstants.CHAT_INPUT_ENTITY.setUser_id(PreferenceUtil.getUserID(getActivity()));
                    AppConstants.CHAT_INPUT_ENTITY.setFriend_id(chatIdRes.getFriend_id());
                    AppConstants.CHAT_INPUT_ENTITY.setSchedule_id(chatIdRes.getResult());

                    ((HomeScreen) getActivity()).addFragment(new ChatFragment());
                }
            }
        }

    }

    /* Ask for permission on Location access*/
    private void permissionsAccessLocation() {
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
            askAccessPermission(listPermissionsNeeded, 1, new InterfaceTwoBtnCallback() {
                @Override
                public void onPositiveClick() {
                    getCurrentLatLong();
                }

                @Override
                public void onNegativeClick() {
                    permissionsAccessLocation();
                }


            });
        }

    }

    private void callProductListAPI(int startLimitInt) {

        Location loc = getCurrentLatLong();
        String latLngStr = loc.getLatitude() + "," + loc.getLongitude();
        if (mAddressStr.isEmpty()) {
            String addressURLStr = String.format(AppConstants.GET_ADDRESS_URL, latLngStr);
            APIRequestHandler.getInstance().callGetUserAddressAPI(addressURLStr, this);
        } else {
            mLimitInt = startLimitInt;

            AgentFilterLocalEntity savedAgentFilter=PreferenceUtil.getAgentFilterDetail(getActivity());


           // if (AppConstants.AGENT_FILTER_ENTITY.isUpdated()) {
            if(savedAgentFilter.isUpdated()){
                mAddressTxt.setText(savedAgentFilter.getLocation());
            }

                APIRequestHandler.getInstance().findAnAgentAPICall(savedAgentFilter.getName(), savedAgentFilter.getLocation(), savedAgentFilter.getPrice_range_min(), savedAgentFilter.getPrice_range_max(), savedAgentFilter.getProperty_experties(), 10 + "", startLimitInt + "", savedAgentFilter.getType(), savedAgentFilter.getLatitude(), savedAgentFilter.getLongitude(), startLimitInt == 1, this);

//            } else {
//                APIRequestHandler.getInstance().findAnAgentAPICall("", mAddressStr, "", "", "", 10 + "", startLimitInt + "", "", loc.getLatitude() + "", loc.getLongitude() + "", startLimitInt == 1, this);
//
//            }
        }
    }

    /*Load more list call*/
    private void callLoadMoreList() {
        //add loading progress view
        mProgressBar.setVisibility(View.VISIBLE);
        if (mFindAgentFilterListRes != null && mFindAgentFilterListRes.size() > 1) {
            mFindAnAgentRecyclerView.getLayoutManager().smoothScrollToPosition(mFindAnAgentRecyclerView, null, mFindAgentFilterListRes.size() - 1);
        }
        callProductListAPI(mLimitInt += 10);
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRefresh() {
        if (mRefreshView.isRefreshing()) {
            mProgressBar.setVisibility(View.GONE);
            mRefreshView.setRefreshing(false);
            callProductListAPI(0);
        }
    }
}
