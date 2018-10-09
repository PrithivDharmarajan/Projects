package com.smaat.ipharma.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.smaat.ipharma.R;
import com.smaat.ipharma.apiinterface.APICommonInterface;
import com.smaat.ipharma.entity.CommonResponse;
import com.smaat.ipharma.entity.GoogleApiEntity;
import com.smaat.ipharma.entity.MapPropertyEntity;
import com.smaat.ipharma.entity.NewOrderEntity;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.util.AppConstants;
import com.smaat.ipharma.util.DialogManager;
import com.smaat.ipharma.util.DialogMangerSucessCallback;
import com.smaat.ipharma.util.GPSTracker;
import com.smaat.ipharma.util.GlobalMethods;
import com.smaat.ipharma.util.TypefaceSingleton;

import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class OrderDetailsFragment extends BaseFragment implements
        OnClickListener, DialogMangerSucessCallback {
    private ImageView mPrescriptionImg, mLocationImg;
    @SuppressWarnings("unused")
    private TextView mStatus, mPharmacyName, mPharmacyAddress, mDistance,
            mUserame, mUserAddress, mFootettxt;

    private EditText mDeliveryEdit, mNoteHere;
    @SuppressWarnings("unused")
    private Button mSave, mLocatioCheck, mRight;
    private boolean isCheck = false;
    private LinearLayout mPlaceorder;
    private double latitude, longitude;
    private Bundle extras;
    private MapPropertyEntity mMapDetailEntity;
    private String mShopId, mUser_name, mCommunicationAddress;
    private String UserID;
    private String mPharmacyId = "";
    private TextView mPhonenum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_order_details,
                container, false);
        setupUI(rootview);
        return rootview;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup mViewGroup = (ViewGroup) view
                .findViewById(R.id.parent_layout);
        Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(
                getActivity());
        setFont(mViewGroup, mTypeface);
        setupUI(mViewGroup);
        hideSoftKeyboard(getActivity());
        UserID = GlobalMethods.getUserID(getActivity());
        mUser_name = GlobalMethods.getUserName(getActivity());
        mCommunicationAddress = (String) GlobalMethods.getValueFromPreference(
                getActivity(), GlobalMethods.STRING_PREFERENCE,
                AppConstants.communication_address);
        extras = this.getArguments();
        if (extras != null) {
            mMapDetailEntity = (MapPropertyEntity) extras
                    .getSerializable(AppConstants.PHARMCAY_DETAILS);
            AppConstants.mMapDetailEntity = mMapDetailEntity;
            mShopId = mMapDetailEntity.getPharmacyID();
            AppConstants.Pharmacy_id = mShopId;

        }
        initComponents(view);
        setValuesFrompharmacy();
        HomeScreen.mHeaderLeftLay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (AppConstants.FRAG
                        .equalsIgnoreCase(AppConstants.HISTORY_SCREEN)) {

                    ((HomeScreen) getActivity()).replaceFragment(
                            new HistoryFragment(), true);
                    HomeScreen.mHeaderLeft
                            .setBackgroundResource(R.drawable.back_butto);
                    HomeScreen.mBottombar.setVisibility(View.GONE);
                    HomeScreen.mFooterText.setText(R.string.select_pres);
                } else if (AppConstants.popup.equalsIgnoreCase(AppConstants.CAMERA)) {
                    AppConstants.photo = AppConstants.CAMERA;
                    ((HomeScreen) getActivity()).replaceFragment(
                            new MapScreenFragment(), false);
                    AppConstants.FROMINTLOADMAP = AppConstants.FROMMAP;
                    HomeScreen.mHeaderRight
                            .setBackgroundResource(R.drawable.maplist_normal);
                    HomeScreen.mHeaderLeft
                            .setBackgroundResource(R.drawable.slide);

                } else if (HomeScreen.mBackMove instanceof OneTouchFragment) {
                    ((HomeScreen) getActivity()).replaceFragment(
                            HomeScreen.mBackMove, false);
                    AppConstants.FROMINTLOADMAP = AppConstants.FROMMAP;
                    HomeScreen.mHeaderRight
                            .setBackgroundResource(R.drawable.maplist_normal);
                    HomeScreen.mHeaderLeft
                            .setBackgroundResource(R.drawable.back_butto);
                } else {
                    HomeScreen.onBackMove(getActivity());
                }

            }
        });

    }

    private void setValuesFrompharmacy() {
        if (mMapDetailEntity != null) {
            mPharmacyName.setText(mMapDetailEntity.getShopName());
            mPharmacyAddress.setText(mMapDetailEntity.getAddress());
            DecimalFormat distance_roundoff = new DecimalFormat("#.##");
            mDistance.setText(Double.valueOf(distance_roundoff.format(Double
                    .valueOf(mMapDetailEntity.getDistance())))
                    + " "
                    + getString(R.string.km_away));
            mPhonenum.setText(mMapDetailEntity.getPhone());
        } else {
            mPharmacyName.setText(AppConstants.history_mPharmacyName);
            mPharmacyAddress.setText(AppConstants.history_mPharmacyAddress);
            mPhonenum.setText(AppConstants.history_mPhonenum);
            DecimalFormat distance_roundoff = new DecimalFormat("#.##");
            if (!AppConstants.history_mDistance.equalsIgnoreCase("")) {
                try {
                    mDistance.setText(Double.valueOf(distance_roundoff
                            .format(Double
                                    .valueOf(AppConstants.history_mDistance)))
                            + " " + getString(R.string.km_away));
                } catch (NumberFormatException e) {
                    mDistance.setText("");
                }
            }
            mShopId = AppConstants.history_mShopId;
            mPharmacyId = AppConstants.history_mPrescriptionId;
            mNoteHere.setText(AppConstants.history_mNotehere);
        }
        mUserame.setText(mUser_name);
        mDeliveryEdit.setText(mCommunicationAddress);
    }


    private void initComponents(View view) {
        mPrescriptionImg = (ImageView) view
                .findViewById(R.id.prescription_image);
        mStatus = (TextView) view.findViewById(R.id.status);
        mPharmacyName = (TextView) view.findViewById(R.id.pharmacy_name);
        mPharmacyName.setTypeface(HomeScreen.mHelveticaBold);
        mPharmacyAddress = (TextView) view.findViewById(R.id.pharmacy_address);
        mDistance = (TextView) view.findViewById(R.id.distance);
        mDeliveryEdit = (EditText) view.findViewById(R.id.address_edit);
        mFootettxt = (TextView) view.findViewById(R.id.footer_text);
        mFootettxt.setTypeface(HomeScreen.mHelveticaBold);

        mPhonenum = (TextView) view.findViewById(R.id.phone_num);

        mLocationImg = (ImageView) view.findViewById(R.id.current_location);
        mLocationImg.setOnClickListener(this);

        mNoteHere = (EditText) view.findViewById(R.id.note_edit);

        mSave = (Button) view.findViewById(R.id.save);
        mSave.setOnClickListener(this);

        mPlaceorder = (LinearLayout) view.findViewById(R.id.place_order);
        mPlaceorder.setOnClickListener(this);

        mLocatioCheck = (Button) view.findViewById(R.id.locaiton_check);
        mLocatioCheck.setOnClickListener(this);

        mUserame = (TextView) view.findViewById(R.id.user_name);
        mUserAddress = (TextView) view.findViewById(R.id.user_address);
        mRight = (Button) view.findViewById(R.id.right_arrow);
        if (AppConstants.photo.equalsIgnoreCase(AppConstants.PHOTO)) {

            Glide.with(this).load(AppConstants.Base_Url + "/"
                    +
                    AppConstants.history_prec_img)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mPrescriptionImg);


        }
        if (AppConstants.photo.equalsIgnoreCase(AppConstants.CAMERA)) {
            mPrescriptionImg.setImageBitmap(HomeScreen.mSelectedImgBitmap);
            AppConstants.FRAG = AppConstants.ORDERNOW_SCREEN;
        }

        if (AppConstants.from_my_order
                .equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

            AppConstants.FRAG = AppConstants.MYORDER_SCREEN;
            mNoteHere.setFocusable(false);
            mNoteHere.setFocusableInTouchMode(false);
            mDeliveryEdit.setFocusable(false);
            mDeliveryEdit.setFocusableInTouchMode(false);
            mFootettxt.setText(AppConstants.delivery_status);
        } else {

            mNoteHere.setFocusable(true);
            mNoteHere.setFocusableInTouchMode(true);
            mDeliveryEdit.setFocusable(true);
            mDeliveryEdit.setFocusableInTouchMode(true);
        }

    }

    private void showCurrentLocation() {
        GPSTracker tracker = new GPSTracker(getActivity());
        if (tracker.canGetLocation() == false) {
            tracker.showSettingsAlert();
        } else {
            latitude = tracker.getLatitude();
            longitude = tracker.getLongitude();
            callGoogleApiService(latitude, longitude);
        }
    }

    private void callGoogleApiService(double latitude, double longitude) {

        String url = AppConstants.LATLNG_LINK + latitude + "," + longitude
                + "&sensor=true";
        aq().ajax(url, JSONObject.class, this,
                getString(R.string.addresslocation));
    }

    public void addresslocation(String url, JSONObject json, AjaxStatus status) {
        if (json != null) {
            try {
                GoogleApiEntity obj = new Gson().fromJson(json.toString(),
                        GoogleApiEntity.class);
                onRequest(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void onRequest(GoogleApiEntity response) {
        try {
            if (response != null) {
                if (response.getResults().size() > 0) {
                    String[] addresses = response.getResults().get(0)
                            .getFormatted_address().toString().split(",");
                    if (addresses.length >= 4) {
                        String add_txt = response.getResults().get(0)
                                .getFormatted_address();
                        mDeliveryEdit.setText(add_txt);
                    }
                } else {
                    DialogManager.showCustomAlertDialog(getActivity(),
                            OrderDetailsFragment.this,
                            getString(R.string.unable_to_find_location));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onRequestCallBack(CommonResponse response) {
        if (response != null) {
            if (response.getError_code().equalsIgnoreCase(
                    AppConstants.SUCCESS_CODE)) {

            }
        }

    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);
        CommonResponse resultObj = (CommonResponse) responseObj;


        DialogManager.showPopUpDialog(getActivity(), this, resultObj.getMsg());

    }


    public void onItemclick(String SelctedItem, int pos) {

    }

    public void onOkclick() {
        AppConstants.from_map_list = AppConstants.TRUE;
        ((HomeScreen) getActivity()).replaceFragment(new MapScreenFragment(),
                false);
        MapScreenFragment.mMapListView.setVisibility(View.VISIBLE);
        HomeScreen.mHeaderRight
                .setBackgroundResource(R.drawable.map_view_normal);
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.current_location:
                showCurrentLocation();
                break;
            case R.id.locaiton_check:
                if (!isCheck) {
                    isCheck = true;
                    mLocatioCheck
                            .setBackgroundResource(R.drawable.register_till_fill);

                } else {
                    mLocatioCheck
                            .setBackgroundResource(R.drawable.regsiter_tick_empty);
                    isCheck = false;
                }
                break;
            case R.id.place_order:
                if (AppConstants.from_my_order
                        .equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                    if (AppConstants.delivery_status
                            .contains(getString(R.string.pay_rs))) {
                        HomeScreen.mHeaderRightLay.setVisibility(View.INVISIBLE);
                        HomeScreen.mHeaderLeft
                                .setBackgroundResource(R.drawable.back_butto);
                        ((HomeScreen) getActivity()).replaceFragment(
                                new PaymentOptionsFragment(), true);
                    }
                } else {

                    if (mDeliveryEdit.getText().toString().equalsIgnoreCase("")) {
                        DialogManager.showCustomAlertDialog(getActivity(),
                                OrderDetailsFragment.this,
                                getString(R.string.please_enter_address));
                    } else {
                        if (mMapDetailEntity != null) {
                            callPlaceOrderService();
                        } else {
                            callReOrderService();
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    private void callReOrderService() {
        DialogManager.showProgress(getActivity());
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(
                AppConstants.Base_Url).build();
        APICommonInterface interfaces = restAdapter
                .create(APICommonInterface.class);

        String new_address = mUserAddress.getText().toString();
        String order_note = mNoteHere.getText().toString();

        interfaces.reOrder(mShopId, UserID, mCommunicationAddress, new_address,
                mPharmacyId, order_note, new Callback<NewOrderEntity>() {

                    public void failure(RetrofitError arg0) {
                        DialogManager.hideProgress(getActivity());
                        DialogManager.showCustomAlertDialog(getActivity(),
                                OrderDetailsFragment.this,
                                getString(R.string.no_network));
                    }

                    public void success(NewOrderEntity response, Response obj) {

                        DialogManager.hideProgress(getActivity());
                        if (response.getStatus().equalsIgnoreCase(
                                AppConstants.SUCCESS_CODE)) {

                            DialogManager.showSuccessDialog(getActivity(),
                                    OrderDetailsFragment.this,
                                    getString(R.string.order_dialog_text));

                        } else {
                            DialogManager.showCustomAlertDialog(getActivity(),
                                    OrderDetailsFragment.this,
                                    response.getMsg());
                        }

                    }

                });
    }

    private void callPlaceOrderService() {
        DialogManager.showProgress(getActivity());
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(
                AppConstants.Base_Url).build();
        APICommonInterface interfaces = restAdapter
                .create(APICommonInterface.class);

        String new_address = mUserAddress.getText().toString();
        String order_note = mNoteHere.getText().toString();

        TypedFile perscr_img = new TypedFile("multipart/form-data", new File(
                AppConstants.prescription_image));
        interfaces.newOrder(mShopId, UserID, mCommunicationAddress,
                new_address, order_note, perscr_img,
                new Callback<NewOrderEntity>() {

                    public void failure(RetrofitError arg0) {
                        DialogManager.hideProgress(getActivity());
                        DialogManager.showCustomAlertDialog(getActivity(),
                                OrderDetailsFragment.this,
                                getString(R.string.no_network));
                    }

                    public void success(NewOrderEntity response, Response obj) {
                        DialogManager.hideProgress(getActivity());
                        if (response.getStatus().equalsIgnoreCase(
                                AppConstants.SUCCESS_CODE)) {
                            DialogManager.showSuccessDialog(getActivity(),
                                    OrderDetailsFragment.this,
                                    getString(R.string.order_dialog_text));

                        } else {
                            DialogManager.showCustomAlertDialog(getActivity(),
                                    OrderDetailsFragment.this,
                                    response.getMsg());
                        }

                    }

                });
    }

}
