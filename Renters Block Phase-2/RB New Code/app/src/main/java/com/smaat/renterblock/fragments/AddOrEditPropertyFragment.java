package com.smaat.renterblock.fragments;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.PropertyEntity;
import com.smaat.renterblock.entity.PropertyPictures;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.AddressResponse;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.model.PropertyDetailsResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.ImageUtil;
import com.smaat.renterblock.utils.InterfaceEdtWithBtnCallback;
import com.smaat.renterblock.utils.InterfaceTwoBtnCallback;
import com.smaat.renterblock.utils.InterfaceTwoBtnWithStringCallback;
import com.smaat.renterblock.utils.ListingProfileImageSelection;
import com.smaat.renterblock.utils.PathUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static com.smaat.renterblock.R.id.list_img4;
import static com.smaat.renterblock.R.id.play_btn1;
import static com.smaat.renterblock.R.id.play_btn2;
import static com.smaat.renterblock.R.id.play_btn3;
import static com.smaat.renterblock.R.id.play_btn4;
import static com.smaat.renterblock.R.id.play_btn5;
import static com.smaat.renterblock.R.id.swipe_img;

/**
 * Created by Smaat on 8/21/2017.
 */

public class AddOrEditPropertyFragment extends BaseFragment implements OnMapReadyCallback, OnMapClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.list_img1)
    ImageView mListImg1;
    @BindView(R.id.list_img2)
    ImageView mListImg2;
    @BindView(R.id.list_img3)
    ImageView mListImg3;
    @BindView(R.id.list_img4)
    ImageView mListImg4;
    @BindView(R.id.list_img5)
    ImageView mListImg5;

    @BindView(R.id.list_img_close1)
    Button mCloseBtn1;
    @BindView(R.id.list_img_close2)
    Button mCloseBtn2;
    @BindView(R.id.list_img_close3)
    Button mCloseBtn3;
    @BindView(R.id.list_img_close4)
    Button mCloseBtn4;
    @BindView(R.id.list_img_close5)
    Button mCloseBtn5;
    @BindView(play_btn1)
    Button mPlayBtn1;
    @BindView(play_btn2)
    Button mPlayBtn2;
    @BindView(play_btn3)
    Button mPlayBtn3;
    @BindView(play_btn4)
    Button mPlayBtn4;
    @BindView(play_btn5)
    Button mPlayBtn5;
    @BindView(R.id.left_btn)
    Button mLeftArrow;
    @BindView(R.id.right_btn)
    Button mRightArrow;
    @BindView(R.id.for_sale)
    Button mForSaleBtn;
    @BindView(R.id.for_rent)
    Button mForRentBtn;


    @BindView(R.id.bed_num0)
    TextView mBedNum0Txt;
    @BindView(R.id.bed_num1)
    TextView mBedNum1Txt;
    @BindView(R.id.bed_num2)
    TextView mBedNum2Txt;
    @BindView(R.id.bed_num3)
    TextView mBedNum3Txt;
    @BindView(R.id.bed_num4)
    TextView mBedNum4Txt;
    @BindView(R.id.bed_num5)
    TextView mBedNum5Txt;
    @BindView(R.id.bed_num6)
    TextView mBedNum6Txt;

    @BindView(R.id.baths_num0)
    TextView mBathNum0Txt;
    @BindView(R.id.baths_num1)
    TextView mBathNum1Txt;
    @BindView(R.id.baths_num2)
    TextView mBathNum2Txt;
    @BindView(R.id.baths_num3)
    TextView mBathNum3Txt;
    @BindView(R.id.baths_num4)
    TextView mBathNum4Txt;
    @BindView(R.id.baths_num5)
    TextView mBathNum5Txt;
    @BindView(R.id.baths_num6)
    TextView mBathNum6Txt;
    @BindView(R.id.property_type_txt)
    TextView mPropertyTxt;

    @BindView(R.id.tog_resale)
    ToggleButton mResaleTog;
    @BindView(R.id.tog_new_construction)
    ToggleButton mNewConstuctionTog;
    @BindView(R.id.tog_fore_closure)
    ToggleButton mForclousreTog;
    @BindView(R.id.tog_open_houses)
    ToggleButton mOpenHousesTog;
    @BindView(R.id.tog_reduced_prices)
    ToggleButton mReducedPricesTog;
    @BindView(R.id.tog_no_fee)
    ToggleButton mNoFeeTog;

    @BindView(R.id.address_edits)
    EditText mAddressEdt;
    @BindView(R.id.description_edit)
    EditText mDescriptionEdt;
    @BindView(R.id.price_edit)
    EditText mPriceEdt;
    @BindView(R.id.square_edit)
    EditText mSquareEdt;
    @BindView(R.id.year_edit)
    EditText mYearEdt;
    @BindView(R.id.lot_edit)
    EditText mLotEdit;
    @BindView(R.id.mls_edit)
    EditText mMLSEdit;

    @BindView(R.id.horizontal_scroll)
    HorizontalScrollView mHorizontalScrollView;
    private Marker mMarker;
    private boolean isEditBool = false;

    /*property values datas*/
    private String mAddressStr = "", mLatitudeStr = "", mLongtitudeStr = "",
            mTypeStr = "rent", mDescriptionStr = "", mPriceStr = "", mPropTypeStr = "",
            mBedsStr = "0", mBathsStr = "0", mSquareStr = "", mNoFeeStr = "0", mYearStr = "",
            mLotStr = "", mResaleStr = "0", mNewConstructionStr = "0",
            mForClosureStr = "0", mOpenHousesStr = "0", mReducedPricesStr = "0",
            mMLSStr = "", mPropertyId = "";


    private boolean mReSaleBool = false, mNewConstructBool = false, mForClosureBool = false, mOpenHHousesBool = false, mReducePriceBool = false, mNoFeeBool = false;
    private GoogleMap mGoogleMap;
    private final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private final int GALLERY_IMAGE_REQUEST_CODE = 200;
    private static final int CHOOSEVIDEO = 70;
    private static final int CAPTUREVIDEO = 30;
    private GoogleApiClient mGoogleApiClient;
    private final int REQUEST_CHECK_SETTINGS = 300;
    private int mViewIDInt;
    private Uri mPictureFileUri;

    private String mImageFileStr1 = "", mImageFileStr2 = "", mImageFileStr3 = "", mImageFileStr4 = "",
            mImageFileStr5 = "";
    private String mVideoFileStr1 = "", mVideoFileStr2 = "", mVideoFileStr3 = "", mVideoFileStr4 = "",
            mVideoFileStr5 = "";
    private PropertyPictures mPropertyPictures;
    private PropertyEntity mPropertyDetailsRes = new PropertyEntity();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_add_or_edit_property, container, false);
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

    @Override
    public void onFragmentResume() {
        super.onFragmentResume();

        if (getActivity() != null) {
              /*set header text and header right img*/
            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.edit_icon, 0);
            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.drawable.add_icon, 0,"");
            ((HomeScreen) getActivity()).setDrawerAction(false);

            ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.listing), 1);


            if (AppConstants.ADD_PROPERTY.equals(AppConstants.KEY_TRUE)) {
                ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.add_listing_header), 1);
            } else {
                ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.edit_listing), 1);

            }
            ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.listing), 0);

         /* Check for the permission to access location */
            if (permissionsAccessLocation()) {
                initView();
            }

        }
    }

    private void initView() {
        buildGoogleApiClient();
        SupportMapFragment fragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);

        /* Map synchronization */
        fragment.getMapAsync(this);


//        if (AppConstants.PROPERTY_DETAILS != null) {
//            setEditableValues();
//        }

        if (!AppConstants.ADD_PROPERTY.equals(AppConstants.KEY_TRUE)) {
            APIRequestHandler.getInstance().propertyDetails(AppConstants.DETAIL_PROPERTY_ID,
                    AddOrEditPropertyFragment.this);

        }


    }

    protected synchronized void buildGoogleApiClient() {
        if (getActivity() != null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(AddOrEditPropertyFragment.this)
                    .addOnConnectionFailedListener(AddOrEditPropertyFragment.this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
    }


    private void setEditableValues() {
        mPropertyId = AppConstants.DETAIL_PROPERTY_ID;
        mPropertyTxt.setText(mPropertyDetailsRes.getProperty_type().toString());

        ArrayList<PropertyPictures> propertyPictures = new ArrayList<PropertyPictures>();
        for (int i = 0; i < mPropertyDetailsRes.getProperty_pics().size(); i++) {
            mPropertyPictures = new PropertyPictures();
            mPropertyPictures.setFile(mPropertyDetailsRes.getProperty_pics().get(i)
                    .getFile());
            mPropertyPictures.setFile_type(mPropertyDetailsRes.getProperty_pics()
                    .get(i).getFile_type());
            mPropertyPictures.setPicture_id(mPropertyDetailsRes.getProperty_pics()
                    .get(i).getPicture_id());
            mPropertyPictures.setPro_id(mPropertyDetailsRes.getProperty_pics()
                    .get(i).getPro_id());
            mPropertyPictures.setProperty_id(mPropertyDetailsRes.getProperty_pics()
                    .get(i).getProperty_id());
            mPropertyPictures.setUser_id(mPropertyDetailsRes.getProperty_pics()
                    .get(i).getUser_id());
            mPropertyPictures.setDescription(mPropertyDetailsRes.getProperty_pics()
                    .get(i).getDescription());
            mPropertyPictures.setFile_order(mPropertyDetailsRes.getProperty_pics().get(i).getFile_order());
            propertyPictures.add(mPropertyPictures);
        }

        for (int j = 0; j < mPropertyDetailsRes.getProperty_video().size(); j++) {
            mPropertyPictures = new PropertyPictures();
            mPropertyPictures.setFile(mPropertyDetailsRes.getProperty_video().get(j)
                    .getFile());
            mPropertyPictures.setFile_type(mPropertyDetailsRes.getProperty_video()
                    .get(j).getFile_type());
            mPropertyPictures.setPicture_id(mPropertyDetailsRes.getProperty_video()
                    .get(j).getPicture_id());
            mPropertyPictures.setPro_id(mPropertyDetailsRes.getProperty_video()
                    .get(j).getPro_id());
            mPropertyPictures.setProperty_id(mPropertyDetailsRes.getProperty_video()
                    .get(j).getProperty_id());
            mPropertyPictures.setUser_id(mPropertyDetailsRes.getProperty_video()
                    .get(j).getUser_id());
            mPropertyPictures.setDescription(mPropertyDetailsRes.getProperty_video()
                    .get(j).getDescription());
            mPropertyPictures.setFile_order(mPropertyDetailsRes.getProperty_pics().get(j).getFile_order());
            propertyPictures.add(mPropertyPictures);

        }


        for (int pics = 0; pics < propertyPictures.size(); pics++) {


            switch (propertyPictures.get(pics).getFile_order()) {
                case "1":
                    if (propertyPictures.get(pics).getFile_type().equals("image")) {

                        try {
                            Glide.with(getActivity())
                                    .load(propertyPictures.get(pics).getFile()).error(R.drawable.default_profile_icon)
                                    .into(mListImg1);
                            mCloseBtn1.setVisibility(View.VISIBLE);
                            mImageFileStr1 = propertyPictures.get(pics).getFile();
                        } catch (Exception e) {
                            e.printStackTrace();
                            mListImg1.setImageResource(R.drawable.default_profile_icon);
                        }

                    } else {
                        mListImg1.setBackground(null);
                        mListImg1.setImageBitmap(null);
                        mListImg1.setBackgroundColor(Color
                                .parseColor("#000000"));
                        mPlayBtn1.setVisibility(View.VISIBLE);
                        mVideoFileStr1 = propertyPictures.get(0).getFile();

                    }
                    break;
                case "2":
                    if (propertyPictures.get(pics).getFile_type().equals("image")) {
                        try {
                            Glide.with(getActivity())
                                    .load(propertyPictures.get(pics).getFile()).error(R.drawable.default_profile_icon)
                                    .into(mListImg2);

                            mImageFileStr2 = propertyPictures.get(pics).getFile();
                            mCloseBtn2.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            e.printStackTrace();
                            mListImg2.setImageResource(R.drawable.default_profile_icon);
                        }
                    } else {
                        mListImg2.setBackground(null);
                        mListImg2.setBackgroundColor(Color
                                .parseColor("#000000"));
                        mListImg2.setImageBitmap(null);
                        mPlayBtn2.setVisibility(View.VISIBLE);
                        mVideoFileStr2 = propertyPictures.get(1).getFile();


                    }
                    break;
                case "3":
                    if (propertyPictures.get(pics).getFile_type().equals("image")) {
                        try {
                            Glide.with(getActivity())
                                    .load(propertyPictures.get(pics).getFile()).error(R.drawable.default_profile_icon)
                                    .into(mListImg3);
                            mImageFileStr3 = propertyPictures.get(pics).getFile();

                            mCloseBtn3.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            e.printStackTrace();
                            mListImg3.setImageResource(R.drawable.default_profile_icon);
                        }
                    } else {
                        mListImg3.setBackground(null);
                        mListImg3.setBackgroundColor(Color
                                .parseColor("#000000"));
                        mListImg3.setImageBitmap(null);
                        mPlayBtn3.setVisibility(View.VISIBLE);
                        mVideoFileStr3 = propertyPictures.get(pics).getFile();

                    }
                    break;
                case "4":
                    if (propertyPictures.get(3).getFile_type().equals("image")) {
                        mImageFileStr4 = propertyPictures.get(3).getFile();
                    } else {
                        mVideoFileStr4 = propertyPictures.get(3).getFile();
                    }
                    break;
                case "5":
                    if (propertyPictures.get(pics).getFile_type().equals("image")) {
                        mImageFileStr5 = propertyPictures.get(pics).getFile();
                    } else {
                        mVideoFileStr5 = propertyPictures.get(pics).getFile();
                    }
                    break;
            }


        }

        mTypeStr = mPropertyDetailsRes.getType().equals("sale") ? "sale" : "rent";
        mDescriptionEdt.setText(mPropertyDetailsRes.getDescription());
        if (mPropertyDetailsRes.getType().equals("sale")) {
            mForSaleBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    R.drawable.listing_radio_over, 0);
            mForRentBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    R.drawable.listing_radio_normal, 0);
        } else {
            mForRentBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    R.drawable.listing_radio_over, 0);
            mForSaleBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    R.drawable.listing_radio_normal, 0);
        }

        mPriceEdt.setText(mPropertyDetailsRes.getPrice_range().toString());
        mSquareEdt.setText(mPropertyDetailsRes.getSquare_footage().toString());
        mYearEdt.setText(mPropertyDetailsRes.getBuild_year().toString());
        mLotEdit.setText(mPropertyDetailsRes.getLot_size().toString());
        mMLSEdit.setText(mPropertyDetailsRes.getMLS());

        String beds = mPropertyDetailsRes.getBeds().toString();
        String baths = mPropertyDetailsRes.getBaths().toString();
        switch (baths) {
            case "0":
                mBathsStr = "0";
                setBathBackground(R.id.baths_num0);
                setBathTextColor(R.id.baths_num0);
                break;
            case "1":
                mBathsStr = "1";
                setBathBackground(R.id.baths_num1);
                setBathTextColor(R.id.baths_num1);
                break;
            case "2":
                mBathsStr = "2";
                setBathBackground(R.id.baths_num2);
                setBathTextColor(R.id.baths_num2);
                break;
            case "3":
                mBathsStr = "3";
                setBathBackground(R.id.baths_num3);
                setBathTextColor(R.id.baths_num3);
                break;
            case "4":
                mBathsStr = "4";
                setBathBackground(R.id.baths_num4);
                setBathTextColor(R.id.baths_num4);
                break;
            case "5":
                mBathsStr = "5";
                setBathBackground(R.id.baths_num5);
                setBathTextColor(R.id.baths_num5);
                break;
            default:
                mBathsStr = "6";
                setBathBackground(R.id.baths_num6);
                setBathTextColor(R.id.baths_num6);
                break;

        }


        switch (beds) {
            case "0":
                mBedsStr = "0";
                setBedBackground(R.id.bed_num0);
                setBedTextColor(R.id.bed_num0);
                break;
            case "1":
                mBedsStr = "1";
                setBedBackground(R.id.bed_num1);
                setBedTextColor(R.id.bed_num1);
                break;
            case "2":
                mBedsStr = "2";
                setBedBackground(R.id.bed_num2);
                setBedTextColor(R.id.bed_num2);
                break;
            case "3":
                mBedsStr = "3";
                setBedBackground(R.id.bed_num3);
                setBedTextColor(R.id.bed_num3);
                break;
            case "4":
                mBedsStr = "4";
                setBedBackground(R.id.bed_num4);
                setBedTextColor(R.id.bed_num4);
                break;
            case "5":
                mBedsStr = "5";
                setBedBackground(R.id.bed_num5);
                setBedTextColor(R.id.bed_num5);
                break;
            default:
                mBedsStr = "6";
                setBedBackground(R.id.bed_num6);
                setBedTextColor(R.id.bed_num6);
                break;

        }


        mResaleStr = mPropertyDetailsRes.getResale().equals("1") ? "1" : "0";
        mReSaleBool = mPropertyDetailsRes.getResale().equals("1") ? true : false;
        mResaleTog.setBackgroundResource(mPropertyDetailsRes.getResale().equals("1") ? R.drawable.tick_on :
                R.drawable.tick_off);

        mNoFeeStr = mPropertyDetailsRes.getFee().equals("1") ? "1" : "0";
        mNoFeeBool = mPropertyDetailsRes.getFee().equals("1") ? true : false;
        mNoFeeTog.setBackgroundResource(mPropertyDetailsRes.getFee().equals("1") ? R.drawable.toggle_on :
                R.drawable.toggle_off);

        mReducedPricesStr = mPropertyDetailsRes.getReduced_prices().equals("1") ? "1" : "0";
        mReducePriceBool = mPropertyDetailsRes.getReduced_prices().equals("1") ? true : false;
        mReducedPricesTog.setBackgroundResource(mPropertyDetailsRes.getReduced_prices().equals("1") ?
                R.drawable.tick_on : R.drawable.tick_off);


        mOpenHousesStr = mPropertyDetailsRes.getOpen_house().equals("1") ? "1" : "0";
        mOpenHHousesBool = mPropertyDetailsRes.getOpen_house().equals("1") ? true : false;
        mOpenHousesTog.setBackgroundResource(mPropertyDetailsRes.getOpen_house().equals("1") ?
                R.drawable.tick_on : R.drawable.tick_off);

        mForClosureStr = mPropertyDetailsRes.getForeclosure().equals("1") ? "1" : "0";
        mForClosureBool = mPropertyDetailsRes.getForeclosure().equals("1") ? true : false;
        mForclousreTog.setBackgroundResource(mPropertyDetailsRes.getForeclosure().equals("1") ?
                R.drawable.tick_on : R.drawable.tick_off);

        mNewConstructionStr = mPropertyDetailsRes.getNew_construction().equals("1") ? "1" : "0";
        mNewConstructBool = mPropertyDetailsRes.getNew_construction().equals("1") ? true : false;
        mNewConstuctionTog.setBackgroundResource(mPropertyDetailsRes.getNew_construction().equals("1") ?
                R.drawable.tick_on : R.drawable.tick_off);
        mAddressEdt.setText(mPropertyDetailsRes.getAddress());
        if (!mPropertyDetailsRes.getAddress().isEmpty())
            isEditBool = true;
        getLatLangApi(mAddressEdt.getText().toString().trim());


    }

    @OnClick({R.id.locate_my_address, R.id.save_bottom, R.id.left_arrow, R.id.right_arrow, R.id.list_img1,
            R.id.list_img2, R.id.list_img3, R.id.list_img4, R.id.list_img5, R.id.list_img_close1,
            R.id.list_img_close2, R.id.list_img_close3, R.id.list_img_close4, R.id.list_img_close5,
            R.id.bed_num0, R.id.bed_num1, R.id.bed_num2, R.id.bed_num3, R.id.bed_num4, R.id.bed_num5, R.id.bed_num6,
            R.id.baths_num0, R.id.baths_num1, R.id.baths_num2, R.id.baths_num3, R.id.baths_num4, R.id.baths_num5, R.id.baths_num6
            , R.id.resale, R.id.new_construction, R.id.for_closure, R.id.open_houses, R.id.reduced_prices, R.id.tog_no_fee, R.id.for_rent,
            R.id.for_sale, R.id.property_type})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.locate_my_address:
                if (!mAddressEdt.getText().toString().trim().isEmpty()) {
                    isEditBool = true;
                    getLatLangApi(mAddressEdt.getText().toString().trim());
                } else {
                    DialogManager.getInstance().showToast(getActivity(), getString(R.string.please_enter_location));
                }
                break;
            case R.id.save_bottom:
                validateAddProperty();
                break;
            case R.id.left_arrow:
                mHorizontalScrollView.fullScroll(View.FOCUS_LEFT);
                break;
            case R.id.right_arrow:
                mHorizontalScrollView.fullScroll(View.FOCUS_RIGHT);
                break;
            case R.id.list_img1:
                mViewIDInt = v.getId();
                showOptionDialog();
                break;
            case R.id.list_img2:
                mViewIDInt = v.getId();
                showOptionDialog();
                break;
            case R.id.list_img3:
                mViewIDInt = v.getId();
                showOptionDialog();
                break;
            case R.id.list_img4:
                mViewIDInt = v.getId();
                showOptionDialog();
                break;
            case R.id.list_img5:
                mViewIDInt = v.getId();
                showOptionDialog();
                break;

            case R.id.list_img_close1:
                mListImg1.setImageResource(R.drawable.listing_add_photo_normal);
                mCloseBtn1.setVisibility(View.GONE);
                mPlayBtn1.setVisibility(View.GONE);
                mImageFileStr1 = "";
                mVideoFileStr1 = "";
                break;
            case R.id.list_img_close2:
                mListImg2.setImageResource(R.drawable.listing_add_photo_normal);
                mCloseBtn2.setVisibility(View.GONE);
                mPlayBtn2.setVisibility(View.GONE);
                mImageFileStr2 = "";
                mVideoFileStr2 = "";
                break;
            case R.id.list_img_close3:
                mListImg3.setImageResource(R.drawable.listing_add_photo_normal);
                mCloseBtn3.setVisibility(View.GONE);
                mPlayBtn3.setVisibility(View.GONE);
                mImageFileStr3 = "";
                mVideoFileStr3 = "";
                break;
            case R.id.list_img_close4:
                mListImg4.setImageResource(R.drawable.listing_add_photo_normal);
                mCloseBtn4.setVisibility(View.GONE);
                mPlayBtn4.setVisibility(View.GONE);
                mImageFileStr4 = "";
                mVideoFileStr4 = "";
                break;
            case R.id.list_img_close5:
                mListImg5.setImageResource(R.drawable.listing_add_photo_normal);
                mCloseBtn5.setVisibility(View.GONE);
                mPlayBtn5.setVisibility(View.GONE);
                mImageFileStr5 = "";
                mVideoFileStr5 = "";
                break;
            case R.id.bed_num0:
                mBedsStr = "0";
                setBedBackground(v.getId());
                setBedTextColor(v.getId());
                //ListingActivity.photo_vid_entity.setBeds(mBeds);
                break;
            case R.id.bed_num1:
                mBedsStr = "1";
                setBedBackground(v.getId());
                setBedTextColor(v.getId());
                //ListingActivity.photo_vid_entity.setBeds(mBeds);
                break;
            case R.id.bed_num2:
                mBedsStr = "2";
                setBedBackground(v.getId());
                setBedTextColor(v.getId());
                //ListingActivity.photo_vid_entity.setBeds(mBeds);
                break;
            case R.id.bed_num3:
                mBedsStr = "3";
                setBedBackground(v.getId());
                setBedTextColor(v.getId());
                //ListingActivity.photo_vid_entity.setBeds(mBeds);
                break;
            case R.id.bed_num4:
                mBedsStr = "4";
                setBedBackground(v.getId());
                setBedTextColor(v.getId());
                //ListingActivity.photo_vid_entity.setBeds(mBeds);
                break;
            case R.id.bed_num5:
                mBedsStr = "5";
                setBedBackground(v.getId());
                setBedTextColor(v.getId());
                // ListingActivity.photo_vid_entity.setBeds(mBeds);
                break;
            case R.id.bed_num6:
                mBedsStr = "6";
                setBedBackground(v.getId());
                setBedTextColor(v.getId());
                // ListingActivity.photo_vid_entity.setBeds(mBeds);
                break;

            case R.id.baths_num0:
                mBathsStr = "0";
                setBathBackground(v.getId());
                setBathTextColor(v.getId());
                //  ListingActivity.photo_vid_entity.setBaths(mBaths);
                break;
            case R.id.baths_num1:
                mBathsStr = "1";
                setBathBackground(v.getId());
                setBathTextColor(v.getId());
                //ListingActivity.photo_vid_entity.setBaths(mBaths);
                break;
            case R.id.baths_num2:
                mBathsStr = "2";
                setBathBackground(v.getId());
                setBathTextColor(v.getId());
                // ListingActivity.photo_vid_entity.setBaths(mBaths);
                break;
            case R.id.baths_num3:
                mBathsStr = "3";
                setBathBackground(v.getId());
                setBathTextColor(v.getId());
                //ListingActivity.photo_vid_entity.setBaths(mBaths);
                break;
            case R.id.baths_num4:
                mBathsStr = "4";
                setBathBackground(v.getId());
                setBathTextColor(v.getId());
                // ListingActivity.photo_vid_entity.setBaths(mBaths);
                break;
            case R.id.baths_num5:
                mBathsStr = "5";
                setBathBackground(v.getId());
                setBathTextColor(v.getId());
                //ListingActivity.photo_vid_entity.setBaths(mBaths);
                break;
            case R.id.baths_num6:
                mBathsStr = "6";
                setBathBackground(v.getId());
                setBathTextColor(v.getId());
                //ListingActivity.photo_vid_entity.setBaths(mBaths);
                break;
            case R.id.resale:
                mResaleTog.setBackgroundResource(mReSaleBool ? R.drawable.tick_off : R.drawable.tick_on);
                mResaleStr = mReSaleBool ? "0" : "1";
                mReSaleBool = !mReSaleBool;
                break;
            case R.id.new_construction:
                mNewConstuctionTog.setBackgroundResource(mNewConstructBool ? R.drawable.tick_off : R.drawable.tick_on);
                mNewConstructionStr = mNewConstructBool ? "0" : "1";
                mNewConstructBool = !mNewConstructBool;
//                if (!boolNewConstrction) {
//                    mNewConstruction = "1";
//                    boolNewConstrction = true;
//                    mNewConstuctionTog.setBackgroundResource(R.drawable.tick_on);
//                    ListingActivity.photo_vid_entity
//                            .setNew_construction(mNewConstruction);
//                } else {
//                    mNewConstruction = "0";
//                    boolNewConstrction = false;
//                    mNewConstuctionTog.setBackgroundResource(R.drawable.tick_off);
//                    ListingActivity.photo_vid_entity
//                            .setNew_construction(mNewConstruction);
//                }
                break;
            case R.id.for_closure:
                mForclousreTog.setBackgroundResource(mForClosureBool ? R.drawable.tick_off : R.drawable.tick_on);
                mForClosureStr = mForClosureBool ? "0" : "1";
                mForClosureBool = !mForClosureBool;
                break;
            case R.id.open_houses:
                mOpenHousesTog.setBackgroundResource(mOpenHHousesBool ? R.drawable.tick_off : R.drawable.tick_on);
                mOpenHousesStr = mOpenHHousesBool ? "0" : "1";
                mOpenHHousesBool = !mOpenHHousesBool;
//                if (!boolOpenHHouses) {
//                    mOpenHouses = "1";
//                    boolOpenHHouses = true;
//                    mOpenHousesTog.setBackgroundResource(R.drawable.tick_on);
//                    ListingActivity.photo_vid_entity.setOpen_house(mOpenHouses);
//                } else {
//                    mOpenHouses = "0";
//                    boolOpenHHouses = false;
//                    mOpenHousesTog.setBackgroundResource(R.drawable.tick_off);
//                    ListingActivity.photo_vid_entity.setOpen_house(mOpenHouses);
//                }
                break;
            case R.id.reduced_prices:
                mReducedPricesTog.setBackgroundResource(mReducePriceBool ? R.drawable.tick_off : R.drawable.tick_on);
                mReducedPricesStr = mReducePriceBool ? "0" : "1";
                mReducePriceBool = !mReducePriceBool;
//                if (!boolReducedPrices) {
//                    mReducedPrices = "1";
//                    boolReducedPrices = true;
//                    mReducedPricesTog.setBackgroundResource(R.drawable.tick_on);
//                    ListingActivity.photo_vid_entity
//                            .setReduced_prices(mReducedPrices);
//                } else {
//                    mReducedPrices = "0";
//                    boolReducedPrices = false;
//                    mReducedPricesTog.setBackgroundResource(R.drawable.tick_off);
//                    ListingActivity.photo_vid_entity
//                            .setReduced_prices(mReducedPrices);
//                }
//
                break;
            case R.id.tog_no_fee:
                mNoFeeTog.setBackgroundResource(mNoFeeBool ? R.drawable.toggle_off : R.drawable.toggle_on);
                mNoFeeStr = mNoFeeBool ? "0" : "1";
                mNoFeeBool = !mNoFeeBool;
//                if (!boolNoFee) {
//                    boolNoFee = true;
//                    mNoFee = "1";
//                    mNoFeeTog.setBackgroundResource(R.drawable.toggle_on);
//                    ListingActivity.photo_vid_entity.setNo_fee("1");
//                } else {
//                    boolNoFee = false;
//                    mNoFee = "0";
//                    mNoFeeTog.setBackgroundResource(R.drawable.toggle_off);
//                    ListingActivity.photo_vid_entity.setNo_fee("0");
//                }
                break;
            case R.id.for_rent:
                mTypeStr = "rent";
                mForRentBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.listing_radio_over, 0);
                mForSaleBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.listing_radio_normal, 0);
//                ListingActivity.photo_vid_entity.setFor_rent("1");
//                ListingActivity.photo_vid_entity.setFor_sale("0");
                break;
            case R.id.for_sale:
                mTypeStr = "sale";
                mForSaleBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.listing_radio_over, 0);
                mForRentBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.listing_radio_normal, 0);
//                ListingActivity.photo_vid_entity.setFor_rent("0");
//                ListingActivity.photo_vid_entity.setFor_sale("1");
                break;
            case R.id.property_type:
                DialogManager.getInstance().selectPropertyTypePopup(getContext(),
                        new InterfaceEdtWithBtnCallback() {
                            @Override
                            public void onFirstEdtBtnClick(String firstEdtStr) {
                                mPropertyTxt.setText(firstEdtStr);
                            }
                        });
                break;

        }

    }

    private void validateAddProperty() {

        mAddressStr = mAddressEdt.getText().toString().trim();
        mDescriptionStr = mDescriptionEdt.getText().toString().trim();
        mPriceStr = mPriceEdt.getText().toString().trim();
        mSquareStr = mSquareEdt.getText().toString().trim();
        mYearStr = mYearEdt.getText().toString().trim();
        mLotStr = mLotEdit.getText().toString().trim();
        mMLSStr = mMLSEdit.getText().toString().trim();

        mPropTypeStr = mPropertyTxt.getText().toString().trim();

        if (mPropTypeStr.equalsIgnoreCase("Select")) {
            mPropTypeStr = "";
        }

        if (mImageFileStr1.isEmpty()
                && mImageFileStr2.isEmpty() && mImageFileStr3.isEmpty() && mImageFileStr4.isEmpty() && mImageFileStr5.isEmpty()
                && mVideoFileStr1.isEmpty() && mVideoFileStr2.isEmpty() && mVideoFileStr3.isEmpty() &&
                mVideoFileStr4.isEmpty() && mVideoFileStr5.isEmpty()) {
            DialogManager.getInstance().showAlertPopup(getActivity(),
                    getString(R.string.please_upload_image), this);

        } else if (mAddressStr.isEmpty() && mAddressStr.length() <= 0) {
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.please_enter_address), this);
        } else if (mDescriptionStr.isEmpty() && mDescriptionStr.length() <= 0) {
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.please_enter_description), this);
        } else if (mPriceStr.isEmpty() && mPriceStr.length() <= 0) {
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.please_enter_price), this);
        } else {
            callAddProperty();
        }
    }


    private void callAddProperty() {
        // long length1 = 0, length2 = 0, length3 = 0, length4 = 0, length5 = 0;
        APIRequestHandler.getInstance().addProperty("", mAddressStr,
                mTypeStr, mLatitudeStr, mLongtitudeStr, mDescriptionStr, mPropTypeStr, mPriceStr, mBedsStr, mBathsStr,
                mNoFeeStr, mSquareStr, mYearStr, mLotStr, mNewConstructionStr, mForClosureStr, mOpenHousesStr,
                mReducedPricesStr, mMLSStr, mResaleStr,
                mImageFileStr1, mImageFileStr2, mImageFileStr3, mImageFileStr4, mImageFileStr5,
                "", "", "", "", "", mPropertyId,
                mVideoFileStr1, mVideoFileStr2, mVideoFileStr3, mVideoFileStr4, mVideoFileStr5, this);

    }


    private void showOptionDialog() {
        DialogManager.getInstance().showImageUploadPopup(getActivity(),
                getString(R.string.choose), getString(R.string.photo), getString(R.string.video), new InterfaceTwoBtnCallback() {
                    @Override
                    public void onNegativeClick() {

                        OptionDialog("photo");
                    }

                    @Override
                    public void onPositiveClick() {

                        OptionDialog("video");
                    }
                });

    }


    private void setBedBackground(int id) {

        mBedNum0Txt.setBackgroundColor(id == R.id.bed_num0 ? ContextCompat.getColor(getContext(), R.color.app_blue)
                : ContextCompat.getColor(getContext(), R.color.white));
        mBedNum1Txt.setBackgroundColor(id == R.id.bed_num1 ? ContextCompat.getColor(getContext(), R.color.app_blue)
                : ContextCompat.getColor(getContext(), R.color.white));
        mBedNum2Txt.setBackgroundColor(id == R.id.bed_num2 ? ContextCompat.getColor(getContext(), R.color.app_blue)
                : ContextCompat.getColor(getContext(), R.color.white));
        mBedNum3Txt.setBackgroundColor(id == R.id.bed_num3 ? ContextCompat.getColor(getContext(), R.color.app_blue)
                : ContextCompat.getColor(getContext(), R.color.white));
        mBedNum4Txt.setBackgroundColor(id == R.id.bed_num4 ? ContextCompat.getColor(getContext(), R.color.app_blue)
                : ContextCompat.getColor(getContext(), R.color.white));
        mBedNum5Txt.setBackgroundColor(id == R.id.bed_num5 ? ContextCompat.getColor(getContext(), R.color.app_blue)
                : ContextCompat.getColor(getContext(), R.color.white));
        mBedNum6Txt.setBackgroundColor(id == R.id.bed_num6 ? ContextCompat.getColor(getContext(), R.color.app_blue)
                : ContextCompat.getColor(getContext(), R.color.white));
    }

    private void setBathBackground(int id) {

        mBathNum0Txt.setBackgroundColor(id == R.id.baths_num0 ? ContextCompat.getColor(getContext(), R.color.app_blue)
                : ContextCompat.getColor(getContext(), R.color.white));
        mBathNum1Txt.setBackgroundColor(id == R.id.baths_num1 ? ContextCompat.getColor(getContext(), R.color.app_blue)
                : ContextCompat.getColor(getContext(), R.color.white));
        mBathNum2Txt.setBackgroundColor(id == R.id.baths_num2 ? ContextCompat.getColor(getContext(), R.color.app_blue)
                : ContextCompat.getColor(getContext(), R.color.white));
        mBathNum3Txt.setBackgroundColor(id == R.id.baths_num3 ? ContextCompat.getColor(getContext(), R.color.app_blue)
                : ContextCompat.getColor(getContext(), R.color.white));
        mBathNum4Txt.setBackgroundColor(id == R.id.baths_num4 ? ContextCompat.getColor(getContext(), R.color.app_blue)
                : ContextCompat.getColor(getContext(), R.color.white));
        mBathNum5Txt.setBackgroundColor(id == R.id.baths_num5 ? ContextCompat.getColor(getContext(), R.color.app_blue)
                : ContextCompat.getColor(getContext(), R.color.white));
        mBathNum6Txt.setBackgroundColor(id == R.id.baths_num6 ? ContextCompat.getColor(getContext(), R.color.app_blue)
                : ContextCompat.getColor(getContext(), R.color.white));

    }

    private void setBedTextColor(int id) {

        mBedNum0Txt.setTextColor(ContextCompat.getColor(getContext(), R.color.app_blue));
        mBedNum1Txt.setTextColor(ContextCompat.getColor(getContext(), R.color.app_blue));
        mBedNum2Txt.setTextColor(ContextCompat.getColor(getContext(), R.color.app_blue));
        mBedNum3Txt.setTextColor(ContextCompat.getColor(getContext(), R.color.app_blue));
        mBedNum4Txt.setTextColor(ContextCompat.getColor(getContext(), R.color.app_blue));
        mBedNum5Txt.setTextColor(ContextCompat.getColor(getContext(), R.color.app_blue));
        mBedNum6Txt.setTextColor(ContextCompat.getColor(getContext(), R.color.app_blue));

        switch (id) {
            case R.id.bed_num0:
                mBedNum0Txt.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                break;
            case R.id.bed_num1:
                mBedNum1Txt.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                break;
            case R.id.bed_num2:
                mBedNum2Txt.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                break;
            case R.id.bed_num3:
                mBedNum3Txt.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                break;
            case R.id.bed_num4:
                mBedNum4Txt.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                break;
            case R.id.bed_num5:
                mBedNum5Txt.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                break;
            case R.id.bed_num6:
                mBedNum6Txt.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                break;
        }
    }


    private void setBathTextColor(int id) {

        mBathNum0Txt.setTextColor(ContextCompat.getColor(getContext(), R.color.app_blue));
        mBathNum1Txt.setTextColor(ContextCompat.getColor(getContext(), R.color.app_blue));
        mBathNum2Txt.setTextColor(ContextCompat.getColor(getContext(), R.color.app_blue));
        mBathNum3Txt.setTextColor(ContextCompat.getColor(getContext(), R.color.app_blue));
        mBathNum4Txt.setTextColor(ContextCompat.getColor(getContext(), R.color.app_blue));
        mBathNum5Txt.setTextColor(ContextCompat.getColor(getContext(), R.color.app_blue));
        mBathNum6Txt.setTextColor(ContextCompat.getColor(getContext(), R.color.app_blue));

        switch (id) {
            case R.id.baths_num0:
                mBathNum0Txt.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                break;
            case R.id.baths_num1:
                mBathNum1Txt.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                break;
            case R.id.baths_num2:
                mBathNum2Txt.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                break;
            case R.id.baths_num3:
                mBathNum3Txt.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                break;
            case R.id.baths_num4:
                mBathNum4Txt.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                break;
            case R.id.baths_num5:
                mBathNum5Txt.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                break;
            case R.id.baths_num6:
                mBathNum6Txt.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                break;
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                final String selectedImageCapturePath = mPictureFileUri.getPath();

                setImage(selectedImageCapturePath);

                try {
                    Bitmap bm = BitmapFactory.decodeStream(
                            getActivity().getContentResolver().openInputStream(mPictureFileUri));

                    DialogManager.getInstance().showDescriptionAlert(getActivity(), bm, new InterfaceTwoBtnWithStringCallback() {
                        @Override
                        public void onPositiveStringCallback(String str) {

                            uploadImageAndVideo("image", selectedImageCapturePath, str);
                        }

                        @Override
                        public void onNegativeCallback() {

                        }
                    });

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            } else {
                if (resultCode == RESULT_CANCELED) {
                /*Cancelling the image capture process by the user*/
                    DialogManager.getInstance().showToast(getActivity(), getString(R.string.user_cancelled));

                } else {
               /*image capture getting failed due to certail technical issues*/
                    DialogManager.getInstance().showToast(getActivity(), getString(R.string.sorry_failed_img));
                }

            }

        } else if (requestCode == GALLERY_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                mPictureFileUri = data.getData();
                final String selectedImageGalleryPath = PathUtils.getPath(getActivity(), mPictureFileUri);
                setImage(selectedImageGalleryPath);

                try {
                    Bitmap bm = BitmapFactory.decodeStream(
                            getActivity().getContentResolver().openInputStream(mPictureFileUri));

                    DialogManager.getInstance().showDescriptionAlert(getActivity(), bm, new InterfaceTwoBtnWithStringCallback() {
                        @Override
                        public void onPositiveStringCallback(String str) {

                            uploadImageAndVideo("image", selectedImageGalleryPath, str);
                        }

                        @Override
                        public void onNegativeCallback() {

                        }
                    });

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            } else {
                if (resultCode == RESULT_CANCELED) {
                /*Cancelling the image capture process by the user*/
                    DialogManager.getInstance().showToast(getActivity(), getString(R.string.user_cancelled));

                } else {
               /*image capture getting failed due to certail technical issues*/
                    DialogManager.getInstance().showToast(getActivity(), getString(R.string.sorry_failed_img));
                }

            }

        } else if (requestCode == CAPTUREVIDEO) {

            if (resultCode == getActivity().RESULT_OK) {

                final String selectedVideoCapturePath = PathUtils.getPath(getActivity(), mPictureFileUri);
                if (selectedVideoCapturePath != null) {
                    Bitmap thumb = ThumbnailUtils.createVideoThumbnail(selectedVideoCapturePath,
                            MediaStore.Images.Thumbnails.MINI_KIND);
                    setImage(thumb, selectedVideoCapturePath);


                    DialogManager.getInstance().showDescriptionAlert(getActivity(), thumb, new InterfaceTwoBtnWithStringCallback() {
                        @Override
                        public void onPositiveStringCallback(String str) {

                            uploadImageAndVideo("video", selectedVideoCapturePath, str);
                        }

                        @Override
                        public void onNegativeCallback() {

                        }
                    });


                }

            } else {
               /*image capture getting failed due to certail technical issues*/
                DialogManager.getInstance().showToast(getActivity(), "Video capture failed.");

            }

        } else if (requestCode == CHOOSEVIDEO) {

            if (resultCode == getActivity().RESULT_OK) {

                final String selectedVideoGalleryPath = PathUtils.getPath(getActivity(), data.getData());
                if (selectedVideoGalleryPath != null) {

                    Bitmap thumb = ThumbnailUtils.createVideoThumbnail(selectedVideoGalleryPath,
                            MediaStore.Images.Thumbnails.MINI_KIND);
                    setImage(thumb, selectedVideoGalleryPath);


                    DialogManager.getInstance().showDescriptionAlert(getActivity(), thumb, new InterfaceTwoBtnWithStringCallback() {
                        @Override
                        public void onPositiveStringCallback(String str) {

                            uploadImageAndVideo("video", selectedVideoGalleryPath, str);
                        }

                        @Override
                        public void onNegativeCallback() {

                        }
                    });

                }


            } else {
               /*image capture getting failed due to certail technical issues*/
                DialogManager.getInstance().showToast(getActivity(), "Video capture failed.");

            }
        }

    }

    private void uploadImageAndVideo(String type, String filepath, String description) {
        APIRequestHandler.getInstance().ImageVideoUpload(AppConstants.DETAIL_PROPERTY_ID,
                "0", type, description, filepath, AddOrEditPropertyFragment.this);

    }

    private void setImage(String uri) {
        switch (mViewIDInt) {
            case R.id.list_img1:
                try {
                    mImageFileStr1 = uri;
                    Glide.with(getActivity())
                            .load(uri).error(R.drawable.default_profile_icon)
                            .into(mListImg1);

                } catch (Exception e) {
                    e.printStackTrace();
                    mListImg1.setImageResource(R.drawable.default_profile_icon);
                }
                mCloseBtn1.setVisibility(View.VISIBLE);

                break;
            case R.id.list_img2:
                try {
                    mImageFileStr2 = uri;
                    Glide.with(getActivity())
                            .load(uri).error(R.drawable.default_profile_icon)
                            .into(mListImg2);

                } catch (Exception e) {
                    e.printStackTrace();
                    mListImg2.setImageResource(R.drawable.default_profile_icon);
                }
                mCloseBtn2.setVisibility(View.VISIBLE);
                break;
            case R.id.list_img3:
                try {
                    mImageFileStr3 = uri;
                    Glide.with(getActivity())
                            .load(uri).error(R.drawable.default_profile_icon)
                            .into(mListImg3);

                } catch (Exception e) {
                    e.printStackTrace();
                    mListImg3.setImageResource(R.drawable.default_profile_icon);
                }
                mCloseBtn3.setVisibility(View.VISIBLE);
                break;
            case list_img4:
                try {
                    mImageFileStr4 = uri;
                    Glide.with(getActivity())
                            .load(uri).error(R.drawable.default_profile_icon)
                            .into(mListImg4);

                } catch (Exception e) {
                    e.printStackTrace();
                    mListImg4.setImageResource(R.drawable.default_profile_icon);
                }
                mCloseBtn4.setVisibility(View.VISIBLE);
                break;
            case R.id.list_img5:
                try {
                    mImageFileStr5 = uri;
                    Glide.with(getActivity())
                            .load(uri).error(R.drawable.default_profile_icon)
                            .into(mListImg5);

                } catch (Exception e) {
                    e.printStackTrace();
                    mListImg5.setImageResource(R.drawable.default_profile_icon);
                }
                mCloseBtn5.setVisibility(View.VISIBLE);
                break;

        }
    }

    private void setImage(Bitmap bitmap, String uri) {
        switch (mViewIDInt) {
            case R.id.list_img1:
                mVideoFileStr1 = uri;
                mListImg1.setImageBitmap(bitmap);
                mCloseBtn1.setVisibility(View.VISIBLE);
                mPlayBtn1.setVisibility(View.VISIBLE);
                break;
            case R.id.list_img2:
                mVideoFileStr2 = uri;
                mListImg2.setImageBitmap(bitmap);
                mCloseBtn2.setVisibility(View.VISIBLE);
                mPlayBtn2.setVisibility(View.VISIBLE);
                break;
            case R.id.list_img3:
                mVideoFileStr3 = uri;
                mListImg3.setImageBitmap(bitmap);
                mCloseBtn3.setVisibility(View.VISIBLE);
                mPlayBtn3.setVisibility(View.VISIBLE);
                break;
            case list_img4:
                mVideoFileStr4 = uri;
                mListImg4.setImageBitmap(bitmap);
                mCloseBtn4.setVisibility(View.VISIBLE);
                mPlayBtn4.setVisibility(View.VISIBLE);
                break;
            case R.id.list_img5:
                mVideoFileStr5 = uri;
                mListImg5.setImageBitmap(bitmap);
                mCloseBtn5.setVisibility(View.VISIBLE);
                mPlayBtn5.setVisibility(View.VISIBLE);
                break;

        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsAccessLocation();
            return;
        }
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        mGoogleMap.setOnMapClickListener(this);
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);
        setCurrentLocation();


    }

    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof AddressResponse) {
            AddressResponse userAddressRes = (AddressResponse) resObj;
            if (userAddressRes.getResults().size() > 0) {
                if (isEditBool) {
                    isEditBool = false;
                    if (mGoogleMap != null && mMarker != null) {
                        mMarker.remove();
                        LatLng coordinate = new LatLng(Double.parseDouble(userAddressRes.getResults().get(0).getGeometry().getLocation().getLat()),
                                Double.parseDouble(userAddressRes.getResults().get(0).getGeometry().getLocation().getLng()));
                        mMarker = mGoogleMap.addMarker(new MarkerOptions().position(coordinate).
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.listing_map_marker)));
                        mLatitudeStr = userAddressRes.getResults().get(0).getGeometry().getLocation().getLat();
                        mLongtitudeStr = userAddressRes.getResults().get(0).getGeometry().getLocation().getLng();
                        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 17));
                    }

                } else {
                    mAddressEdt.setText("");
                    mAddressEdt.setText(userAddressRes.getResults().get(0).getFormatted_address());
                }


            }
        }

        if (resObj instanceof CommonResponse) {
            CommonResponse commonResponse = (CommonResponse) resObj;
            if (commonResponse.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                if (getActivity() != null)
                    getActivity().onBackPressed();
            }
        }

        if (resObj instanceof PropertyDetailsResponse) {
            final PropertyDetailsResponse propertyEntity = (PropertyDetailsResponse) resObj;

            if (propertyEntity.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                if (propertyEntity.getResult().size() > 0) {
                    mPropertyDetailsRes = propertyEntity.getResult().get(0);
                    setEditableValues();
                }
            }
        }

    }

    @Override
    public void onMapClick(LatLng point) {
        mLatitudeStr = String.valueOf(point.latitude);
        mLongtitudeStr = String.valueOf(point.longitude);
        if (mGoogleMap != null && mMarker != null) {
            mMarker.remove();
            mMarker = mGoogleMap.addMarker(new MarkerOptions().position(point).
                    icon(BitmapDescriptorFactory.fromResource(R.drawable.listing_map_marker)));
            moveMapCamera(mLatitudeStr, mLongtitudeStr);
            getAddressApi(mLatitudeStr + "," + mLongtitudeStr);
        }


    }


    private void getLatLangApi(String addressStr) {
        String addressURLStr = String.format(AppConstants.GET_DETAILS_ADDRESS_URL, addressStr);
        APIRequestHandler.getInstance().callGetUserAddressAPI(addressURLStr, this);
    }

    private void getAddressApi(String mLatLongStr) {
        String addressURLStr = String.format(AppConstants.GET_ADDRESS_URL, mLatLongStr);
        APIRequestHandler.getInstance().callGetUserAddressAPI(addressURLStr, this);
    }


    /*Set current location to map view*/
    private void setCurrentLocation() {
        if (getActivity() != null) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissionsAccessLocation();
                return;
            }
            if (mGoogleMap != null) {
      /* Enable current location */
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
                Location currentLoc = getCurrentLatLong();
                LatLng coordinate = new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude());

                mMarker = mGoogleMap.addMarker(new MarkerOptions().position(mPropertyDetailsRes != null &&
                        !mPropertyDetailsRes.getLatitude().startsWith("0") &&
                        !mPropertyDetailsRes.getLongitude().startsWith("0") ? new LatLng(Double.parseDouble(mPropertyDetailsRes.getLatitude()),
                        Double.parseDouble(mPropertyDetailsRes.getLongitude())) : coordinate).
                        icon(BitmapDescriptorFactory.fromResource(R.drawable.listing_map_marker)));
                mLatitudeStr = String.valueOf(currentLoc.getLatitude());
                mLongtitudeStr = String.valueOf(currentLoc.getLongitude());
                if (AppConstants.ADD_PROPERTY.equals(AppConstants.KEY_TRUE)) {
                    moveMapCamera(String.valueOf(currentLoc.getLatitude()), String.valueOf(currentLoc.getLongitude()));
                }


            }
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (getActivity() != null) {

            LocationManager mLocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (mLocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
                    buildGoogleApiClient();
                } else {
                    setCurrentLocation();
                }
            } else {
                LocationSettingsRequest.Builder locSettingsReqBuilder = new LocationSettingsRequest.Builder().addLocationRequest(createLocationRequest());
                PendingResult<LocationSettingsResult> pendingResult = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, locSettingsReqBuilder.build());
                pendingResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                    @Override
                    public void onResult(@NonNull LocationSettingsResult result) {
                        final Status status = result.getStatus();
                        if (getActivity() != null) {
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // API call.

                                    if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
                                        buildGoogleApiClient();
                                    } else {
                                        setCurrentLocation();
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
                    }
                });
            }
        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("Connection failed:", "Connection failed: ConnectionResult.getErrorCode() = " +
                connectionResult.getErrorCode());

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

        Location location = new Location("");
        if (getActivity() != null) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            /*Ask for permission on locatio access
            * Set flag for call back to continue this process*/
                permissionsAccessLocation();
            }

            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {
                location.setLatitude(mLastLocation.getLatitude());
                location.setLongitude(mLastLocation.getLongitude());

            }
        }

        return location;
    }


    /* Ask for permission on Location access*/
    private boolean permissionsAccessLocation() {
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
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mGoogleMap.setMyLocationEnabled(true);
                    setCurrentLocation();
                }

                @Override
                public void onPositiveClick() {

                }
            });
        }

        return addPermission;
    }


    private void OptionDialog(String option) {

        if (option.equalsIgnoreCase("photo")) {
            DialogManager.getInstance().showImageUploadPopup(getActivity(), getString(R.string.select_photo), getString(R.string.take_photo), getString(R.string.choose_exisiting_photo), new InterfaceTwoBtnCallback() {
                @Override
                public void onNegativeClick() {
                    captureImage();
                }

                @Override
                public void onPositiveClick() {
                    galleryImage();
                }
            });
        } else {

            DialogManager.getInstance().showImageUploadPopup(getActivity(), getString(R.string.select_video), getString(R.string.take_video), getString(R.string.choose_exisiting_video), new InterfaceTwoBtnCallback() {
                @Override
                public void onNegativeClick() {
                    captureVideo();
                }

                @Override
                public void onPositiveClick() {
                    galleryVideo();
                }
            });
        }

    }


    /*open camera Image*/
    private void captureImage() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mPictureFileUri = (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) ? FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", ImageUtil.getOutputMediaFile(MEDIA_TYPE_IMAGE)) : Uri.fromFile(ImageUtil.getOutputMediaFile(MEDIA_TYPE_IMAGE));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPictureFileUri);


        // start the image capture Intent
        getActivity().startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

    }

    /*open gallery Image*/
    private void galleryImage() {
        Intent j = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        getActivity().startActivityForResult(j, GALLERY_IMAGE_REQUEST_CODE);
    }

    /*open Camera Video*/
    private void captureVideo() {

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        mPictureFileUri = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) ? FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", ImageUtil.getOutputMediaFile(MEDIA_TYPE_IMAGE)) : Uri.fromFile(ImageUtil.getOutputMediaFile(MEDIA_TYPE_IMAGE));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPictureFileUri);


        // start the image capture Intent
        getActivity().startActivityForResult(intent, CAPTUREVIDEO);

    }

    /*open Gallery Video*/
    private void galleryVideo() {
        Intent videointent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        getActivity().startActivityForResult(Intent.createChooser(videointent, "Select Video"), CHOOSEVIDEO);
    }


    public void moveMapCamera(final String lat, final String lang) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LatLng moveLoc = new LatLng(Double.parseDouble(!lat.isEmpty() ?
                            lat : "0.0"),
                            Double.parseDouble(!lang.isEmpty() ? lang : "0.0"));
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(moveLoc, 17));
                    getAddressApi(lat + "," + lang);

                }
            });
        }

    }


}