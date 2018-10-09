package com.smaat.renterblock.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.GoogleApiEntity;
import com.smaat.renterblock.entity.PropertyEntity;
import com.smaat.renterblock.entity.PropertyPictures;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.DialogMangerCallback;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.ListingProfileImageSelection;
import com.smaat.renterblock.util.TypefaceSingleton;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class AddListingActivity extends BaseActivity implements OnMapReadyCallback,
        OnClickListener, OnMapClickListener, DialogMangerCallback,AdapterView.OnItemSelectedListener {
    private Bitmap mBitmapFromDevice;
    private ImageView mListImg1, mListImg2, mListImg3, mListImg4, mListImg5;
    private Button mListCloseBtn1, mListCloseBtn2, mListCloseBtn3,
            mListCloseBtn4, mListCloseBtn5;
    private int mImgID;
    private TextView mBedNum0, mBedNum1, mBedNum2, mBedNum3, mBedNum4,
            mBedNum5, mBedNum6, mBathNum0, mBathNum1, mBathNum2, mBathNum3,
            mBathNum4, mBathNum5, mBathNum6, mPropertyTypeTxt;
    private ToggleButton mResaleTog, mNewConstuctionTog, mForclousreTog,
            mOpenHousesTog, mReducedPricesTog, mNoFeeTog;

    private Button mForSale, mForRent;
    private boolean boolResale = false, boolNewConstrction = false,
            boolForclousre = false, boolOpenHHouses = false,
            boolReducedPrices = false, boolNoFee = false;

    private ArrayList<String> mPropertyTypeList;
    private LinearLayout mPropListLay;
    private ArrayList<Button> mPropItemBtn = new ArrayList<Button>();

    private HorizontalScrollView mHorizontalScrollView;

    private EditText mAddressEdit, mDescriptionEdit, mPriceEdit, mSquareEdit,
            mYearEdit, mLotEdit, mMLSEdit;
    byte[] mPropImg1, mPropImg2, mPropImg3, mPropImg4, mPropImg5;
    private String mAddress = "", mLatitude = "", mLongtitude = "",
            mType = "rent", mDescription = "", mPrice = "", mPropType = "",
            mBeds = "0", mBaths = "0", mSquare = "", mNoFee = "0", mYear = "",
            mLot = "", mResale = "0", mNewConstruction = "0",
            mForclosure = "0", mOpenHouses = "0", mReducedPrices = "0",
            mMLS = "";
    private String selectedType;
    private TextView mHeaderText;
    private boolean isFirst = false, isSecond = false, isThird = false,
            isFourth = false, isFifth = false;
    private String mCallAPI = "Ok";
    private Button mLeftArrow, mRightArrow;
    private GoogleMap mGoogleMap;
    protected boolean donzoom = false;
    private Marker marker;
    private String location_address_temp;
    private LinearLayout mMultiImageLay;
    private Spinner mSqftSpinner;
    File file1, file2, file3, file4, file5, file;
    byte[] bFile1, bFile2, bFile3, bFile4, bFile5;

    private Button play_btn1, play_btn2, play_btn3, play_btn4, play_btn5;

    Uri selectedVideo;
    private PropertyEntity prop_entity;

    private ArrayList<PropertyPictures> prop_pic_vid;
    PropertyPictures prop_vid_pic_ent;
    int pic_position = 0;
    private String property_id = "";

    FileInputStream fileInputStream = null;
    private String description1 = "", description2 = "", description3 = "",
            description4 = "", description5 = "";
    Typeface mTypeface;
    String[] mSqftStrAry = {"Sq. Ft.","Acre"};
    private ArrayAdapter mSqftAdapter;
    private LinearLayout map_linear_lay;
    private ScrollView main_scroll;

    private String mAcreStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_add_listing);
        ViewGroup root = (ViewGroup) findViewById(R.id.parent_layout_add_listing);
        mTypeface = TypefaceSingleton.getInstance().getHelvetica(this);
        setFont(root, mTypeface);
        setupUI(root);
        UserID = GlobalMethods.getUserID(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            prop_entity = new PropertyEntity();
            prop_entity = (PropertyEntity) extras
                    .getSerializable("property_entity");
        }
        initComponents();

        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment fragment = (SupportMapFragment) fm.findFragmentById(R.id.map1);
//		if (fragment == null) {
//			fragment = SupportMapFragment.newInstance();
//			fm.beginTransaction().replace(R.id.map, fragment).commit();
//		}
        fragment.getMapAsync(AddListingActivity.this);


    }

    private void initComponents() {

        map_linear_lay = (LinearLayout) findViewById(R.id.map_linear_lay);
        main_scroll = (ScrollView) findViewById(R.id.scrollView1);

//		((WorkaroundMapFragment) getFragmentManager()
//				.findFragmentById(R.id.map))
//				.setListener(new WorkaroundMapFragment.OnTouchListener() {
//					@Override
//					public void onTouch() {
//						main_scroll.requestDisallowInterceptTouchEvent(true);
//					}
//				});

        selectedType = (String) GlobalMethods.getValueFromPreference(this,
                GlobalMethods.STRING_PREFERENCE, AppConstants.pref_type);

        mMultiImageLay = (LinearLayout) findViewById(R.id.multi_img_lay);
        mMultiImageLay.setVisibility(View.VISIBLE);


        mListImg1 = (ImageView) findViewById(R.id.list_img1);
        mListImg2 = (ImageView) findViewById(R.id.list_img2);
        mListImg3 = (ImageView) findViewById(R.id.list_img3);
        mListImg4 = (ImageView) findViewById(R.id.list_img4);
        mListImg5 = (ImageView) findViewById(R.id.list_img5);

        mListCloseBtn1 = (Button) findViewById(R.id.list_img_close1);
        mListCloseBtn2 = (Button) findViewById(R.id.list_img_close2);
        mListCloseBtn3 = (Button) findViewById(R.id.list_img_close3);
        mListCloseBtn4 = (Button) findViewById(R.id.list_img_close4);
        mListCloseBtn5 = (Button) findViewById(R.id.list_img_close5);

        LinearLayout save_top = (LinearLayout) findViewById(R.id.save_top);
        save_top.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                validateAddProperty();
            }
        });

        mLeftArrow = (Button) findViewById(R.id.left_btn);
        mRightArrow = (Button) findViewById(R.id.right_btn);
        mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontal_scroll);
        if (mEnhancedProfile.equals("0")) {
            mRightArrow.setVisibility(View.INVISIBLE);
            mLeftArrow.setVisibility(View.INVISIBLE);
            mHorizontalScrollView.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // TODO Auto-generated method stub
                    return true;
                }
            });
        }

        mHeaderText = (TextView) findViewById(R.id.header_txt);
        if (prop_entity != null) {
            mHeaderText.setText("Edit Listing");
        } else {
            if (selectedType.equalsIgnoreCase(AppConstants.SELLER)) {
                mHeaderText.setText(getString(R.string.add_property_header));
            } else if (selectedType.equalsIgnoreCase(AppConstants.AGENT)
                    || selectedType.equalsIgnoreCase(AppConstants.BROKER)) {
                mHeaderText.setText(getString(R.string.add_listing_header));
            }
        }

        mBedNum0 = (TextView) findViewById(R.id.bed_num0);
        mBedNum1 = (TextView) findViewById(R.id.bed_num1);
        mBedNum2 = (TextView) findViewById(R.id.bed_num2);
        mBedNum3 = (TextView) findViewById(R.id.bed_num3);
        mBedNum4 = (TextView) findViewById(R.id.bed_num4);
        mBedNum5 = (TextView) findViewById(R.id.bed_num5);
        mBedNum6 = (TextView) findViewById(R.id.bed_num6);

        mBathNum0 = (TextView) findViewById(R.id.baths_num0);
        mBathNum1 = (TextView) findViewById(R.id.baths_num1);
        mBathNum2 = (TextView) findViewById(R.id.baths_num2);
        mBathNum3 = (TextView) findViewById(R.id.baths_num3);
        mBathNum4 = (TextView) findViewById(R.id.baths_num4);
        mBathNum5 = (TextView) findViewById(R.id.baths_num5);
        mBathNum6 = (TextView) findViewById(R.id.baths_num6);

        play_btn1 = (Button) findViewById(R.id.play_btn1);
        play_btn2 = (Button) findViewById(R.id.play_btn2);
        play_btn3 = (Button) findViewById(R.id.play_btn3);
        play_btn4 = (Button) findViewById(R.id.play_btn4);
        play_btn5 = (Button) findViewById(R.id.play_btn5);

        mResaleTog = (ToggleButton) findViewById(R.id.tog_resale);
        mNewConstuctionTog = (ToggleButton) findViewById(R.id.tog_new_construction);
        mForclousreTog = (ToggleButton) findViewById(R.id.tog_fore_closure);
        mOpenHousesTog = (ToggleButton) findViewById(R.id.tog_open_houses);
        mReducedPricesTog = (ToggleButton) findViewById(R.id.tog_reduced_prices);
        mNoFeeTog = (ToggleButton) findViewById(R.id.tog_no_fee);

        mForSale = (Button) findViewById(R.id.for_sale);
        mForRent = (Button) findViewById(R.id.for_rent);

        mAddressEdit = (EditText) findViewById(R.id.address_edits);
        mAddressEdit.setTypeface(mTypeface);
        // mAddressEdit.setImeOptions(EditorInfo.IME_ACTION_GO);
        mDescriptionEdit = (EditText) findViewById(R.id.description_edit);
        mPriceEdit = (EditText) findViewById(R.id.price_edit);
        mSquareEdit = (EditText) findViewById(R.id.square_edit);
        mYearEdit = (EditText) findViewById(R.id.year_edit);
        mLotEdit = (EditText) findViewById(R.id.lot_edit);
        mMLSEdit = (EditText) findViewById(R.id.mls_edit);


        mSqftSpinner = (Spinner)findViewById(R.id.sqft_spinner);
        mSqftSpinner.setOnItemSelectedListener(this);

        mSqftAdapter = new ArrayAdapter(getApplicationContext(),R.layout.customize_spinner_txtview,mSqftStrAry);
        mSqftAdapter.setDropDownViewResource(R.layout.spinner_drop_down_lay);
        mSqftSpinner.setAdapter(mSqftAdapter);

        mAddressEdit.setMovementMethod(new ScrollingMovementMethod());
        mDescriptionEdit.setMovementMethod(new ScrollingMovementMethod());

        mAddressEdit.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    new getPositionInfo().execute((mAddressEdit.getText()
                            .toString()));
                    hideSoftKeyboard(AddListingActivity.this);
                    return true;
                }
                return false;
            }
        });

        mDescriptionEdit.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (view.getId() == R.id.description_edit) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            view.getParent().requestDisallowInterceptTouchEvent(
                                    false);
                            break;
                    }
                }
                return false;
            }
        });

        mPropertyTypeTxt = (TextView) findViewById(R.id.property_type_txt);
        mPropertyTypeTxt.setText(AppConstants.SELECTED_PROPERTY_TYPE);
        mPropertyTypeList = new ArrayList<String>();
        setPropertyListValues();

        if (prop_entity != null) {

            AppConstants.SELECTED_PROPERTY_TYPE = prop_entity
                    .getProperty_type().toString();
            property_id = prop_entity.getProperty_id().toString();
            mPropertyTypeTxt.setText(prop_entity.getProperty_type().toString());

            prop_pic_vid = new ArrayList<PropertyPictures>();
            for (int i = 0; i < prop_entity.getProperty_pics().size(); i++) {
                prop_vid_pic_ent = new PropertyPictures();
                prop_vid_pic_ent.setFile(prop_entity.getProperty_pics().get(i)
                        .getFile());
                prop_vid_pic_ent.setFile_type(prop_entity.getProperty_pics()
                        .get(i).getFile_type());
                prop_vid_pic_ent.setPicture_id(prop_entity.getProperty_pics()
                        .get(i).getPicture_id());
                prop_vid_pic_ent.setPro_id(prop_entity.getProperty_pics()
                        .get(i).getPro_id());
                prop_vid_pic_ent.setProperty_id(prop_entity.getProperty_pics()
                        .get(i).getProperty_id());
                prop_vid_pic_ent.setUser_id(prop_entity.getProperty_pics()
                        .get(i).getUser_id());
                prop_vid_pic_ent.setDescription(prop_entity.getProperty_pics()
                        .get(i).getDescription());
                prop_pic_vid.add(prop_vid_pic_ent);

            }

            for (int j = 0; j < prop_entity.getProperty_video().size(); j++) {
                prop_vid_pic_ent = new PropertyPictures();
                prop_vid_pic_ent.setFile(prop_entity.getProperty_video().get(j)
                        .getFile());
                prop_vid_pic_ent.setFile_type(prop_entity.getProperty_video()
                        .get(j).getFile_type());
                prop_vid_pic_ent.setPicture_id(prop_entity.getProperty_video()
                        .get(j).getPicture_id());
                prop_vid_pic_ent.setPro_id(prop_entity.getProperty_video()
                        .get(j).getPro_id());
                prop_vid_pic_ent.setProperty_id(prop_entity.getProperty_video()
                        .get(j).getProperty_id());
                prop_vid_pic_ent.setUser_id(prop_entity.getProperty_video()
                        .get(j).getUser_id());
                prop_vid_pic_ent.setDescription(prop_entity.getProperty_video()
                        .get(j).getDescription());
                prop_pic_vid.add(prop_vid_pic_ent);

            }

            for (int pics = 0; pics < prop_pic_vid.size(); pics++) {
                pic_position = pics;
                if (pics == 0) {
                    if (prop_pic_vid.get(pics).getDescription() != null) {
                        description1 = prop_pic_vid.get(pics).getDescription()
                                .toString();
                    }
                    if (prop_pic_vid.get(0).getFile_type().equals("image")) {
                        aq().id(R.id.list_img1)
                                .progress(R.id.img_progress_1)
                                .image(prop_pic_vid.get(pics).getFile(), true,
                                        true, 0, 0, null, 0, 1.0f / 1.0f);
                        mListCloseBtn1.setVisibility(View.VISIBLE);
                        new convertUrltoBitmap().execute(
                                prop_pic_vid.get(pic_position).getFile(), "0");

                    } else {
                        mListImg1.setBackground(null);
                        mListImg1.setImageBitmap(null);
                        mListImg1.setBackgroundColor(Color
                                .parseColor("#000000"));
                        play_btn1.setVisibility(View.VISIBLE);
                        new downloadImageUrl().execute(
                                prop_pic_vid.get(pic_position).getFile(), "0");
                    }
                } else if (pics == 1) {
                    if (prop_pic_vid.get(pics).getDescription() != null) {
                        description2 = prop_pic_vid.get(pics).getDescription()
                                .toString();
                    }
                    if (prop_pic_vid.get(1).getFile_type().equals("image")) {
                        aq().id(R.id.list_img2)
                                .progress(R.id.img_progress_2)
                                .image(prop_pic_vid.get(pics).getFile(), true,
                                        true, 0, 0, null, 0, 1.0f / 1.0f);
                        mListCloseBtn2.setVisibility(View.VISIBLE);
                        new convertUrltoBitmap().execute(
                                prop_pic_vid.get(pic_position).getFile(), "1");
                    } else {
                        mListImg2.setBackground(null);
                        mListImg2.setBackgroundColor(Color
                                .parseColor("#000000"));
                        mListImg2.setImageBitmap(null);
                        play_btn2.setVisibility(View.VISIBLE);
                        new downloadImageUrl().execute(
                                prop_pic_vid.get(pic_position).getFile(), "1");
                    }
                } else if (pics == 2) {
                    if (prop_pic_vid.get(pics).getDescription() != null) {
                        description3 = prop_pic_vid.get(pics).getDescription()
                                .toString();
                    }
                    if (prop_pic_vid.get(2).getFile_type().equals("image")) {
                        aq().id(R.id.list_img3)
                                .progress(R.id.img_progress_3)
                                .image(prop_pic_vid.get(pics).getFile(), true,
                                        true, 0, 0, null, 0, 1.0f / 1.0f);
                        mListCloseBtn3.setVisibility(View.VISIBLE);
                        new convertUrltoBitmap().execute(
                                prop_pic_vid.get(pic_position).getFile(), "2");
                    } else {
                        mListImg3.setBackground(null);
                        mListImg3.setBackgroundColor(Color
                                .parseColor("#000000"));
                        mListImg3.setImageBitmap(null);
                        play_btn3.setVisibility(View.VISIBLE);
                        new downloadImageUrl().execute(
                                prop_pic_vid.get(pic_position).getFile(), "2");
                    }
                } else if (pics == 3) {
                    if (prop_pic_vid.get(pics).getDescription() != null) {
                        description4 = prop_pic_vid.get(pics).getDescription()
                                .toString();
                    }
                    if (prop_pic_vid.get(3).getFile_type().equals("image")) {
                        aq().id(R.id.list_img4)
                                .progress(R.id.img_progress_4)
                                .image(prop_pic_vid.get(pics).getFile(), true,
                                        true, 0, 0, null, 0, 1.0f / 1.0f);
                        mListCloseBtn4.setVisibility(View.VISIBLE);
                        new convertUrltoBitmap().execute(
                                prop_pic_vid.get(pic_position).getFile(), "3");
                    } else {
                        mListImg4.setBackground(null);
                        mListImg4.setBackgroundColor(Color
                                .parseColor("#000000"));
                        mListImg4.setImageBitmap(null);
                        play_btn4.setVisibility(View.VISIBLE);
                        new downloadImageUrl().execute(
                                prop_pic_vid.get(pic_position).getFile(), "3");
                    }
                } else if (pics == 4) {
                    if (prop_pic_vid.get(pics).getDescription() != null) {
                        description5 = prop_pic_vid.get(pics).getDescription()
                                .toString();
                    }
                    if (prop_pic_vid.get(4).getFile_type().equals("image")) {
                        aq().id(R.id.list_img5)
                                .progress(R.id.img_progress_5)
                                .image(prop_pic_vid.get(pics).getFile(), true,
                                        true, 0, 0, null, 0, 1.0f / 1.0f);
                        mListCloseBtn5.setVisibility(View.VISIBLE);
                        new convertUrltoBitmap().execute(
                                prop_pic_vid.get(pic_position).getFile(), "4");
                    } else {
                        mListImg5.setBackground(null);
                        mListImg5.setBackgroundColor(Color
                                .parseColor("#000000"));
                        mListImg5.setImageBitmap(null);
                        play_btn5.setVisibility(View.VISIBLE);
                        new downloadImageUrl().execute(
                                prop_pic_vid.get(pic_position).getFile(), "4");
                    }
                }
            }

            String txt_arr = prop_entity.getAddress().toString();
            int i = txt_arr.indexOf(',',
                    1 + txt_arr.indexOf(',', 1 + txt_arr.indexOf(',')));
            try {
                String firstPart = txt_arr.substring(0, i);
                String secondPart = txt_arr.substring(i + 1);
                mAddressEdit.setText(firstPart + "\n" + secondPart);
            } catch (Exception e) {
                // TODO: handle exception
                mAddressEdit.setText(txt_arr);
            }

            new getPositionInfo().execute((mAddressEdit.getText().toString()));
            mDescriptionEdit.setText(prop_entity.getDescription());
            if (prop_entity.getType().equals("sale")) {
                mType = "sale";
                mForSale.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.listing_radio_over, 0);
                mForRent.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.listing_radio_normal, 0);
            } else {
                mType = "rent";
                mForRent.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.listing_radio_over, 0);
                mForSale.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.listing_radio_normal, 0);
            }
            mPriceEdit.setText(prop_entity.getPrice_range().toString());
            mSquareEdit.setText(prop_entity.getSquare_footage().toString());
            mYearEdit.setText(prop_entity.getBuild_year().toString());
            mLotEdit.setText(prop_entity.getLot_size().toString());
            mMLSEdit.setText(prop_entity.getMLS());

            String beds = prop_entity.getBeds().toString();
            String baths = prop_entity.getBaths().toString();

            if (baths.equalsIgnoreCase("0")) {
                mBaths = "0";
                setBathBackground(R.id.baths_num0);
                setBathTextColor(R.id.baths_num0);
            } else if (baths.equalsIgnoreCase("1")) {
                mBaths = "1";
                setBathBackground(R.id.baths_num1);
                setBathTextColor(R.id.baths_num1);
            } else if (baths.equalsIgnoreCase("2")) {
                mBaths = "2";
                setBathBackground(R.id.baths_num2);
                setBathTextColor(R.id.baths_num2);
            } else if (baths.equalsIgnoreCase("3")) {
                mBaths = "3";
                setBathBackground(R.id.baths_num3);
                setBathTextColor(R.id.baths_num3);
            } else if (baths.equalsIgnoreCase("4")) {
                mBaths = "4";
                setBathBackground(R.id.baths_num4);
                setBathTextColor(R.id.baths_num4);
            } else if (baths.equalsIgnoreCase("5")) {
                mBaths = "5";
                setBathBackground(R.id.baths_num5);
                setBathTextColor(R.id.baths_num5);
            } else {
                mBaths = "6";
                setBathBackground(R.id.baths_num6);
                setBathTextColor(R.id.baths_num6);
            }

            if (beds.equalsIgnoreCase("0")) {
                mBeds = "0";
                setBedBackground(R.id.bed_num0);
                setBedTextColor(R.id.bed_num0);
            } else if (beds.equalsIgnoreCase("1")) {
                mBeds = "1";
                setBedBackground(R.id.bed_num1);
                setBedTextColor(R.id.bed_num1);
            } else if (beds.equalsIgnoreCase("2")) {
                mBeds = "2";
                setBedBackground(R.id.bed_num2);
                setBedTextColor(R.id.bed_num2);
            } else if (beds.equalsIgnoreCase("3")) {
                mBeds = "3";
                setBedBackground(R.id.bed_num3);
                setBedTextColor(R.id.bed_num3);
            } else if (beds.equalsIgnoreCase("4")) {
                mBeds = "4";
                setBedBackground(R.id.bed_num4);
                setBedTextColor(R.id.bed_num4);
            } else if (beds.equalsIgnoreCase("5")) {
                mBeds = "5";
                setBedBackground(R.id.bed_num5);
                setBedTextColor(R.id.bed_num5);
            } else {
                mBeds = "6";
                setBedBackground(R.id.bed_num6);
                setBedTextColor(R.id.bed_num6);
            }

            if (prop_entity.getResale().equals("1")) {
                mResale = "1";
                boolResale = true;
                mResaleTog.setBackgroundResource(R.drawable.tick_on);
            } else {
                mResale = "0";
                boolResale = false;
                mResaleTog.setBackgroundResource(R.drawable.tick_off);
            }

            if (prop_entity.getFee().equals("1")) {
                mNoFee = "1";
                boolNoFee = true;
                mNoFeeTog.setBackgroundResource(R.drawable.toggle_on);
            } else {
                mNoFee = "0";
                boolNoFee = false;
                mNoFeeTog.setBackgroundResource(R.drawable.toggle_off);
            }
            if (prop_entity.getReduced_prices().equals("1")) {
                mReducedPrices = "1";
                boolReducedPrices = true;
                mReducedPricesTog.setBackgroundResource(R.drawable.tick_on);
            } else {
                mReducedPrices = "0";
                boolReducedPrices = false;
                mReducedPricesTog.setBackgroundResource(R.drawable.tick_off);
            }
            if (prop_entity.getOpen_house().equals("1")) {
                mOpenHouses = "1";
                boolOpenHHouses = true;
                mOpenHousesTog.setBackgroundResource(R.drawable.tick_on);
            } else {
                mOpenHouses = "0";
                boolOpenHHouses = false;
                mOpenHousesTog.setBackgroundResource(R.drawable.tick_off);
            }
            if (prop_entity.getForeclosure().equals("1")) {
                mForclosure = "1";
                boolForclousre = true;
                mForclousreTog.setBackgroundResource(R.drawable.tick_on);
            } else {
                mForclosure = "0";
                boolForclousre = false;
                mForclousreTog.setBackgroundResource(R.drawable.tick_off);
            }

            if (prop_entity.getNew_construction().equals("1")) {
                mNewConstruction = "1";
                boolNewConstrction = true;
                mNewConstuctionTog.setBackgroundResource(R.drawable.tick_on);
            } else {
                mNewConstruction = "0";
                boolNewConstrction = false;
                mNewConstuctionTog.setBackgroundResource(R.drawable.tick_off);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        initializeMap();


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (position == 1){
            AppConstants.CheckingPosition = AppConstants.one;
            if (!mLotEdit.getText().toString().isEmpty()){
                mAcreStr  = String.valueOf(Integer.parseInt(mLotEdit.getText().toString())*43560);

            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    class convertUrltoBitmap extends AsyncTask<String, Void, Bitmap> {

        String position = "";

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap image = null;
            position = params[1];
            try {
                URL url = new URL(params[0]);
                image = BitmapFactory.decodeStream(url.openConnection()
                        .getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (position.equals("0")) {
                isFirst = true;
                mBitmapFromDevice = result;
                ByteArrayOutputStream oututStream = new ByteArrayOutputStream();
                mBitmapFromDevice.compress(Bitmap.CompressFormat.PNG, 100,
                        oututStream);
                byte[] mainImageData = (oututStream).toByteArray();
                mPropImg1 = mainImageData;
            } else if (position.equals("1")) {
                isSecond = true;
                mBitmapFromDevice = result;
                ByteArrayOutputStream oututStream = new ByteArrayOutputStream();
                mBitmapFromDevice.compress(Bitmap.CompressFormat.PNG, 100,
                        oututStream);
                byte[] mainImageData = (oututStream).toByteArray();
                mPropImg2 = mainImageData;
            } else if (position.equals("2")) {
                isThird = true;
                mBitmapFromDevice = result;
                ByteArrayOutputStream oututStream = new ByteArrayOutputStream();
                mBitmapFromDevice.compress(Bitmap.CompressFormat.PNG, 100,
                        oututStream);
                byte[] mainImageData = (oututStream).toByteArray();
                mPropImg3 = mainImageData;
            } else if (position.equals("3")) {
                isFourth = true;
                mBitmapFromDevice = result;
                ByteArrayOutputStream oututStream = new ByteArrayOutputStream();
                mBitmapFromDevice.compress(Bitmap.CompressFormat.PNG, 100,
                        oututStream);
                byte[] mainImageData = (oututStream).toByteArray();
                mPropImg4 = mainImageData;
            } else if (position.equals("4")) {
                isFifth = true;
                mBitmapFromDevice = result;
                ByteArrayOutputStream oututStream = new ByteArrayOutputStream();
                mBitmapFromDevice.compress(Bitmap.CompressFormat.PNG, 100,
                        oututStream);
                byte[] mainImageData = (oututStream).toByteArray();
                mPropImg5 = mainImageData;
            }

        }
    }

    private void initializeMap() {
            /*mGoogleMap = ((WorkaroundMapFragment) getFragmentManager()
					.findFragmentById(R.id.map)).getMap();*/

        if (mGoogleMap == null) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mGoogleMap.setOnMapClickListener(this);
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
            mGoogleMap.getUiSettings().setCompassEnabled(true);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
            mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);

            mGoogleMap
                    .setOnMyLocationChangeListener(new OnMyLocationChangeListener() {

                        @Override
                        public void onMyLocationChange(Location arg0) {
                            CameraPosition cameraPosition;
                            if (!donzoom) {
                                double mLatit = Double.valueOf(arg0
                                        .getLatitude());
                                double mLongit = Double.valueOf(arg0
                                        .getLongitude());
                                if (prop_entity != null) {
                                    if (prop_entity
                                            .getLatitude().equalsIgnoreCase("")) {
                                        mLatit = Double.valueOf("40.702974");
                                        mLongit = Double.valueOf("-74.2598716");
                                    } else {
                                        mLatit = Double.valueOf(prop_entity
                                                .getLatitude());
                                        mLongit = Double.valueOf(prop_entity
                                                .getLongitude());
                                    }
                                }
                                marker = mGoogleMap
                                        .addMarker(new MarkerOptions()
                                                .position(
                                                        new LatLng(mLatit,
                                                                mLongit))
                                                .title("Current Location")
                                                .icon(BitmapDescriptorFactory
                                                        .fromResource(R.drawable.listing_map_marker)));
                                cameraPosition = new CameraPosition.Builder()
                                        .target(new LatLng(mLatit, mLongit))
                                        .zoom((float) 13).build();
                                mGoogleMap.animateCamera(CameraUpdateFactory
                                        .newCameraPosition(cameraPosition));
                                donzoom = true;
                                if (prop_entity != null) {
                                    mLatitude = prop_entity.getLatitude();
                                    mLongtitude = prop_entity
                                            .getLongitude();
                                } else {

                                    mLatitude = String.valueOf(arg0
                                            .getLatitude());
                                    mLongtitude = String.valueOf(arg0
                                            .getLongitude());
                                }
                                callGoogleApiService(
                                        Double.valueOf(mLatitude),
                                        Double.valueOf(mLongtitude));
                            }
                        }
                    });
        }

    }

    @Override
    public void onMapClick(LatLng point) {
        mLatitude = String.valueOf(point.latitude);
        mLongtitude = String.valueOf(point.longitude);
        mAddressEdit.setText("");
        if (!mAddressEdit.getText().toString().trim().equalsIgnoreCase("")
                && !location_address_temp.equalsIgnoreCase(mAddressEdit
                .getText().toString().trim())) {
            new getPositionInfo().execute((mAddressEdit.getText().toString()));
        } else {
            if (marker != null) {
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(point));
                marker.remove();
            }
            marker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(point)
                    .title("Current Location")
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.listing_map_marker)));
            callGoogleApiService(point.latitude, point.longitude);
        }
    }

    class getPositionInfo extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {

            String address = params[0];
            return GlobalMethods.getLatlong(address, AddListingActivity.this);
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            if (result != null) {
                String[] position_points = result.get(1).split(",");
                if (marker != null) {
                    mGoogleMap.animateCamera(CameraUpdateFactory
                            .newLatLng(new LatLng(Double
                                    .valueOf(position_points[0]), Double
                                    .valueOf(position_points[1]))));
                    marker.remove();
                }
                marker = mGoogleMap.addMarker(new MarkerOptions()
                        .position(
                                new LatLng(Double.valueOf(position_points[0]),
                                        Double.valueOf(position_points[1])))
                        .title("Current Location")
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.drawable.listing_map_marker)));
                callGoogleApiService(Double.valueOf(position_points[0]),
                        Double.valueOf(position_points[1]));
                System.out.println("found result" + " " + result);
            }
            super.onPostExecute(result);
        }
    }

    private void callGoogleApiService(double latitude, double longitude) {

        mLatitude = String.valueOf(latitude);
        mLongtitude = String.valueOf(longitude);

        String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="
                + latitude + "," + longitude + "&sensor=true";
        aq().ajax(url, JSONObject.class, this, "addresslocation");
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
                String txt_arr = response.getResults().get(0)
                        .getFormatted_address().toString();
                int i = txt_arr.indexOf(',',
                        1 + txt_arr.indexOf(',', 1 + txt_arr.indexOf(',')));

                String firstPart = txt_arr.substring(0, i);
                String secondPart = txt_arr.substring(i + 1);
                mAddressEdit.setText(firstPart + "\n" + secondPart);

                location_address_temp = response.getResults().get(0)
                        .getFormatted_address().toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPropertyListValues() {
        mPropertyTypeList.add("Single-Family Home");
        mPropertyTypeList.add("Condo");
        mPropertyTypeList.add("Townhouse");
        mPropertyTypeList.add("Coop");
        mPropertyTypeList.add("Apartment");
        mPropertyTypeList.add("Loft");
        mPropertyTypeList.add("TIC");
        mPropertyTypeList.add("Apartment/Condo/Townhouse");
        mPropertyTypeList.add("Mobile/Manufactured");
        mPropertyTypeList.add("Farm/Ranch");
        mPropertyTypeList.add("Lot/land");
        mPropertyTypeList.add("Multi-Family");
        mPropertyTypeList.add("Income/Investment");
        mPropertyTypeList.add("Houseboat");
    }

    private void showOptionDialog(Context context) {
        mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.image_selection);
        mDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
        wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
        Button mPhoto, mVideo, mCancel;
        mPhoto = (Button) mDialog.findViewById(R.id.photo);
        mVideo = (Button) mDialog.findViewById(R.id.video);
        mCancel = (Button) mDialog.findViewById(R.id.cancel);
        mPhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                cameraOption("pic");
            }
        });
        mVideo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                cameraOption("video");
            }
        });
        mCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.show();

    }

    private void cameraOption(String option) {
        ListingProfileImageSelection.showOptionNew(this, option);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_icon:
                Intent intent = new Intent(AddListingActivity.this,
                        ListingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                break;
            case R.id.locate_my_address:
                new getPositionInfo().execute((mAddressEdit.getText().toString()));
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
                ListingActivity.file_position = "1";
                mImgID = v.getId();
                if (mEnhancedProfile.equals("0")) {
                    ListingProfileImageSelection.showOptionNew(this, "pic");
                } else {
                    showOptionDialog(this);
                }
                // ListingProfileImageSelection.showOptionNew(this, "pic");
                break;
            case R.id.list_img2:
                ListingActivity.file_position = "2";
                mImgID = v.getId();
                if (mEnhancedProfile.equals("0")) {
                    ListingProfileImageSelection.showOptionNew(this, "pic");
                } else {
                    showOptionDialog(this);
                }
                // ListingProfileImageSelection.showOptionNew(this, "pic");
                break;
            case R.id.list_img3:
                ListingActivity.file_position = "3";
                mImgID = v.getId();
                if (mEnhancedProfile.equals("0")) {
                    ListingProfileImageSelection.showOptionNew(this, "pic");
                } else {
                    showOptionDialog(this);
                }
                // ListingProfileImageSelection.showOptionNew(this, "pic");
                break;
            case R.id.list_img4:
                ListingActivity.file_position = "4";
                mImgID = v.getId();
                showOptionDialog(this);
                // ListingProfileImageSelection.showOptionNew(this, "pic");
                break;
            case R.id.list_img5:
                ListingActivity.file_position = "5";
                mImgID = v.getId();
                showOptionDialog(this);
                // ListingProfileImageSelection.showOptionNew(this, "pic");
                break;

            case R.id.list_img_close1:
                isFirst = false;
                mListImg1.setImageResource(R.drawable.listing_add_photo_normal);
                mListCloseBtn1.setVisibility(View.GONE);
                play_btn1.setVisibility(View.GONE);
                if (!isFirst && !isSecond && !isThird) {
                    mRightArrow.setVisibility(View.INVISIBLE);
                    mLeftArrow.setVisibility(View.INVISIBLE);
                }
                if (prop_entity != null) {
                    mPropImg1 = null;
                }
                ListingActivity.photo_vid_entity.setImg_bitmap1(null);
                ListingActivity.photo_vid_entity.setVid_bitmap1(null);
                break;
            case R.id.list_img_close2:
                isSecond = false;
                mListImg2.setImageResource(R.drawable.listing_add_photo_normal);
                play_btn2.setVisibility(View.GONE);
                mListCloseBtn2.setVisibility(View.GONE);
                if (prop_entity != null) {
                    mPropImg2 = null;
                }
                ListingActivity.photo_vid_entity.setImg_bitmap2(null);
                ListingActivity.photo_vid_entity.setVid_bitmap2(null);
                break;
            case R.id.list_img_close3:
                isThird = false;
                mListCloseBtn3.setVisibility(View.GONE);
                play_btn3.setVisibility(View.GONE);
                mListImg3.setImageResource(R.drawable.listing_add_photo_normal);
                if (mEnhancedProfile.equals("1")) {
                    mHorizontalScrollView.fullScroll(View.FOCUS_LEFT);
                }
                mRightArrow.setVisibility(View.VISIBLE);
                mLeftArrow.setVisibility(View.INVISIBLE);
                if (prop_entity != null) {
                    mPropImg3 = null;
                }
                ListingActivity.photo_vid_entity.setImg_bitmap3(null);
                ListingActivity.photo_vid_entity.setVid_bitmap3(null);
                break;
            case R.id.list_img_close4:
                isFourth = false;
                mListImg4.setImageResource(R.drawable.listing_add_photo_normal);
                play_btn4.setVisibility(View.GONE);
                mListCloseBtn4.setVisibility(View.GONE);
                if (prop_entity != null) {
                    mPropImg4 = null;
                }
                ListingActivity.photo_vid_entity.setImg_bitmap4(null);
                ListingActivity.photo_vid_entity.setVid_bitmap4(null);
                break;
            case R.id.list_img_close5:
                isFifth = false;
                mListImg5.setImageResource(R.drawable.listing_add_photo_normal);
                play_btn5.setVisibility(View.GONE);
                mListCloseBtn5.setVisibility(View.GONE);
                if (prop_entity != null) {
                    mPropImg5 = null;
                }
                ListingActivity.photo_vid_entity.setImg_bitmap5(null);
                ListingActivity.photo_vid_entity.setVid_bitmap5(null);
                break;
            case R.id.bed_num0:
                mBeds = "0";
                setBedBackground(v.getId());
                setBedTextColor(v.getId());
                ListingActivity.photo_vid_entity.setBeds(mBeds);
                break;
            case R.id.bed_num1:
                mBeds = "1";
                setBedBackground(v.getId());
                setBedTextColor(v.getId());
                ListingActivity.photo_vid_entity.setBeds(mBeds);
                break;
            case R.id.bed_num2:
                mBeds = "2";
                setBedBackground(v.getId());
                setBedTextColor(v.getId());
                ListingActivity.photo_vid_entity.setBeds(mBeds);
                break;
            case R.id.bed_num3:
                mBeds = "3";
                setBedBackground(v.getId());
                setBedTextColor(v.getId());
                ListingActivity.photo_vid_entity.setBeds(mBeds);
                break;
            case R.id.bed_num4:
                mBeds = "4";
                setBedBackground(v.getId());
                setBedTextColor(v.getId());
                ListingActivity.photo_vid_entity.setBeds(mBeds);
                break;
            case R.id.bed_num5:
                mBeds = "5";
                setBedBackground(v.getId());
                setBedTextColor(v.getId());
                ListingActivity.photo_vid_entity.setBeds(mBeds);
                break;
            case R.id.bed_num6:
                mBeds = "6";
                setBedBackground(v.getId());
                setBedTextColor(v.getId());
                ListingActivity.photo_vid_entity.setBeds(mBeds);
                break;

            case R.id.baths_num0:
                mBaths = "0";
                setBathBackground(v.getId());
                setBathTextColor(v.getId());
                ListingActivity.photo_vid_entity.setBaths(mBaths);
                break;
            case R.id.baths_num1:
                mBaths = "1";
                setBathBackground(v.getId());
                setBathTextColor(v.getId());
                ListingActivity.photo_vid_entity.setBaths(mBaths);
                break;
            case R.id.baths_num2:
                mBaths = "2";
                setBathBackground(v.getId());
                setBathTextColor(v.getId());
                ListingActivity.photo_vid_entity.setBaths(mBaths);
                break;
            case R.id.baths_num3:
                mBaths = "3";
                setBathBackground(v.getId());
                setBathTextColor(v.getId());
                ListingActivity.photo_vid_entity.setBaths(mBaths);
                break;
            case R.id.baths_num4:
                mBaths = "4";
                setBathBackground(v.getId());
                setBathTextColor(v.getId());
                ListingActivity.photo_vid_entity.setBaths(mBaths);
                break;
            case R.id.baths_num5:
                mBaths = "5";
                setBathBackground(v.getId());
                setBathTextColor(v.getId());
                ListingActivity.photo_vid_entity.setBaths(mBaths);
                break;
            case R.id.baths_num6:
                mBaths = "6";
                setBathBackground(v.getId());
                setBathTextColor(v.getId());
                ListingActivity.photo_vid_entity.setBaths(mBaths);
                break;
            case R.id.resale:
                if (!boolResale) {
                    mResale = "1";
                    boolResale = true;
                    mResaleTog.setBackgroundResource(R.drawable.tick_on);
                    ListingActivity.photo_vid_entity.setResale(mResale);
                } else {
                    mResale = "0";
                    boolResale = false;
                    mResaleTog.setBackgroundResource(R.drawable.tick_off);
                    ListingActivity.photo_vid_entity.setResale(mResale);
                }
                break;
            case R.id.new_construction:
                if (!boolNewConstrction) {
                    mNewConstruction = "1";
                    boolNewConstrction = true;
                    mNewConstuctionTog.setBackgroundResource(R.drawable.tick_on);
                    ListingActivity.photo_vid_entity
                            .setNew_construction(mNewConstruction);
                } else {
                    mNewConstruction = "0";
                    boolNewConstrction = false;
                    mNewConstuctionTog.setBackgroundResource(R.drawable.tick_off);
                    ListingActivity.photo_vid_entity
                            .setNew_construction(mNewConstruction);
                }
                break;
            case R.id.for_closure:
                if (!boolForclousre) {
                    mForclosure = "1";
                    boolForclousre = true;
                    mForclousreTog.setBackgroundResource(R.drawable.tick_on);
                    ListingActivity.photo_vid_entity.setFor_Closure(mForclosure);
                } else {
                    mForclosure = "0";
                    boolForclousre = false;
                    mForclousreTog.setBackgroundResource(R.drawable.tick_off);
                    ListingActivity.photo_vid_entity.setFor_Closure(mForclosure);
                }

                break;
            case R.id.open_houses:
                if (!boolOpenHHouses) {
                    mOpenHouses = "1";
                    boolOpenHHouses = true;
                    mOpenHousesTog.setBackgroundResource(R.drawable.tick_on);
                    ListingActivity.photo_vid_entity.setOpen_house(mOpenHouses);
                } else {
                    mOpenHouses = "0";
                    boolOpenHHouses = false;
                    mOpenHousesTog.setBackgroundResource(R.drawable.tick_off);
                    ListingActivity.photo_vid_entity.setOpen_house(mOpenHouses);
                }
                break;
            case R.id.reduced_prices:
                if (!boolReducedPrices) {
                    mReducedPrices = "1";
                    boolReducedPrices = true;
                    mReducedPricesTog.setBackgroundResource(R.drawable.tick_on);
                    ListingActivity.photo_vid_entity
                            .setReduced_prices(mReducedPrices);
                } else {
                    mReducedPrices = "0";
                    boolReducedPrices = false;
                    mReducedPricesTog.setBackgroundResource(R.drawable.tick_off);
                    ListingActivity.photo_vid_entity
                            .setReduced_prices(mReducedPrices);
                }

                break;
            case R.id.tog_no_fee:
                if (!boolNoFee) {
                    boolNoFee = true;
                    mNoFee = "1";
                    mNoFeeTog.setBackgroundResource(R.drawable.toggle_on);
                    ListingActivity.photo_vid_entity.setNo_fee("1");
                } else {
                    boolNoFee = false;
                    mNoFee = "0";
                    mNoFeeTog.setBackgroundResource(R.drawable.toggle_off);
                    ListingActivity.photo_vid_entity.setNo_fee("0");
                }
                break;
            case R.id.for_rent:
                mType = "rent";
                mForRent.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.listing_radio_over, 0);
                mForSale.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.listing_radio_normal, 0);
                ListingActivity.photo_vid_entity.setFor_rent("1");
                ListingActivity.photo_vid_entity.setFor_sale("0");
                break;
            case R.id.for_sale:
                mType = "sale";
                mForSale.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.listing_radio_over, 0);
                mForRent.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.listing_radio_normal, 0);
                ListingActivity.photo_vid_entity.setFor_rent("0");
                ListingActivity.photo_vid_entity.setFor_sale("1");
                break;
            case R.id.property_type:
                showPropertyPopup(this);
                break;

        }

    }

    private void showPropertyPopup(final Context context) {
        mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.popup_property_type_new);
        mDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
        wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
        TextView mHeaderTxt;
        LinearLayout mBackIcon;
        Button mCancel, mSet;

        mBackIcon = (LinearLayout) mDialog.findViewById(R.id.back_icon);
        mSet = (Button) mDialog.findViewById(R.id.set);

        mCancel = (Button) mDialog.findViewById(R.id.cancel);
        mHeaderTxt = (TextView) mDialog.findViewById(R.id.header_txt);
        mHeaderTxt.setText("Property Type");
        mPropListLay = (LinearLayout) mDialog.findViewById(R.id.prop_list_lay);

        setPropListAdapter(mPropertyTypeList);
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
                mPropertyTypeTxt.setText(AppConstants.SELECTED_PROPERTY_TYPE);
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

    private void setPropListAdapter(final ArrayList<String> mPropertyTypeList) {

        mPropListLay.removeAllViews();

        if (mPropertyTypeList != null && mPropertyTypeList.size() > 0) {
            for (int i = 0; i < mPropertyTypeList.size(); i++) {

                final String mPropType = mPropertyTypeList.get(i);

                LayoutInflater inflater = (LayoutInflater) this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View convertView = inflater.inflate(
                        R.layout.adapter_prop_type_list, null);
                TextView mPropTypeText = (TextView) convertView
                        .findViewById(R.id.prop_type_text);
                final Button mPropTypeButton = (Button) convertView
                        .findViewById(R.id.prop_type_btn);
                RelativeLayout mPropTypeLay = (RelativeLayout) convertView
                        .findViewById(R.id.prop_type_lay);

                mPropItemBtn.add(mPropTypeButton);
                String type = AppConstants.SELECTED_PROPERTY_TYPE;
                if (mPropertyTypeList.get(i).toString().equalsIgnoreCase(type)) {
                    mPropTypeButton.setBackgroundResource(R.drawable.tick_on);
                }
                mPropTypeLay.setTag(i);
                mPropTypeLay.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        int pos = Integer.parseInt(String.valueOf(v.getTag()));
                        for (Button btn : mPropItemBtn) {
                            btn.setBackgroundResource(R.drawable.tick_off);
                        }
                        mPropTypeButton
                                .setBackgroundResource(R.drawable.tick_on);
                        AppConstants.SELECTED_PROPERTY_TYPE = mPropertyTypeList
                                .get(pos);
                    }
                });
                mPropTypeText.setText(mPropType);

                mPropListLay.addView(convertView);

            }
        }
    }

    private void setBedBackground(int id) {

        mBedNum0.setBackgroundResource(id == R.id.bed_num0 ? R.color.blue
                : R.drawable.btn_bg_left);
        mBedNum1.setBackgroundResource(id == R.id.bed_num1 ? R.color.blue
                : R.drawable.btn_bg_center);
        mBedNum2.setBackgroundResource(id == R.id.bed_num2 ? R.color.blue
                : R.drawable.btn_bg_center);
        mBedNum3.setBackgroundResource(id == R.id.bed_num3 ? R.color.blue
                : R.drawable.btn_bg_center);
        mBedNum4.setBackgroundResource(id == R.id.bed_num4 ? R.color.blue
                : R.drawable.btn_bg_center);
        mBedNum5.setBackgroundResource(id == R.id.bed_num5 ? R.color.blue
                : R.drawable.btn_bg_center);
        mBedNum6.setBackgroundResource(id == R.id.bed_num6 ? R.color.blue
                : R.drawable.btn_bg_right);
    }

    private void setBathBackground(int id) {

        mBathNum0.setBackgroundResource(id == R.id.baths_num0 ? R.color.blue
                : R.drawable.btn_bg_left);
        mBathNum1.setBackgroundResource(id == R.id.baths_num1 ? R.color.blue
                : R.drawable.btn_bg_center);
        mBathNum2.setBackgroundResource(id == R.id.baths_num2 ? R.color.blue
                : R.drawable.btn_bg_center);
        mBathNum3.setBackgroundResource(id == R.id.baths_num3 ? R.color.blue
                : R.drawable.btn_bg_center);
        mBathNum4.setBackgroundResource(id == R.id.baths_num4 ? R.color.blue
                : R.drawable.btn_bg_center);
        mBathNum5.setBackgroundResource(id == R.id.baths_num5 ? R.color.blue
                : R.drawable.btn_bg_center);
        mBathNum6.setBackgroundResource(id == R.id.baths_num6 ? R.color.blue
                : R.drawable.btn_bg_right);

    }

    private void setBedTextColor(int id) {

        mBedNum6.setTextColor(getResources().getColor(R.color.blue));
        mBedNum0.setTextColor(getResources().getColor(R.color.blue));
        mBedNum1.setTextColor(getResources().getColor(R.color.blue));
        mBedNum2.setTextColor(getResources().getColor(R.color.blue));
        mBedNum3.setTextColor(getResources().getColor(R.color.blue));
        mBedNum4.setTextColor(getResources().getColor(R.color.blue));
        mBedNum5.setTextColor(getResources().getColor(R.color.blue));

        if (id == R.id.bed_num6) {
            mBedNum6.setTextColor(getResources().getColor(
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

        mBathNum6.setTextColor(getResources().getColor(R.color.blue));
        mBathNum0.setTextColor(getResources().getColor(R.color.blue));
        mBathNum1.setTextColor(getResources().getColor(R.color.blue));
        mBathNum2.setTextColor(getResources().getColor(R.color.blue));
        mBathNum3.setTextColor(getResources().getColor(R.color.blue));
        mBathNum4.setTextColor(getResources().getColor(R.color.blue));
        mBathNum5.setTextColor(getResources().getColor(R.color.blue));

        if (id == R.id.baths_num6) {
            mBathNum6.setTextColor(getResources().getColor(
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {

            if (resultCode == RESULT_OK) {

                if (requestCode == ListingProfileImageSelection.CAMERA
                        || requestCode == ListingProfileImageSelection.GALLERY) {

                    Bitmap image = ListingProfileImageSelection.getImage(data,
                            this);

                    if (image != null) {
                        if (requestCode == ListingProfileImageSelection.CAMERA) {
                            if (ListingProfileImageSelection.isUriTrue) {
                                image = ListingProfileImageSelection
                                        .getCorrectOrientationImage(this,
                                                data.getData(), image);
                            } else {
                                image = ListingProfileImageSelection
                                        .getCorrectOrientationImage(this, image);
                            }
                        } else {

                            Uri selectedImage = data.getData();

                            image = ListingProfileImageSelection
                                    .getCorrectOrientationImage(this,
                                            selectedImage, image);
                        }

                        mBitmapFromDevice = image;

                        setImage(mImgID, mBitmapFromDevice);
                    } else {
                        DialogManager.showCustomAlertDialog(this, this,
                                "Unsupported file format");
                    }

                } else if (requestCode == ListingProfileImageSelection.CAMERA_VIDEO
                        || requestCode == ListingProfileImageSelection.VIDEO_GALLERY) {
                    Bitmap thumb = null;
                    if (requestCode == ListingProfileImageSelection.CAMERA_VIDEO) {
                        thumb = ThumbnailUtils.createVideoThumbnail(
                                getFilename().getAbsolutePath(), 70);

                    } else {
                        selectedVideo = data.getData();
                        if (mImgID == R.id.list_img1) {
                            file1 = new File(
                                    convertMediaUriToPath(selectedVideo));
                        } else if (mImgID == R.id.list_img2) {
                            file2 = new File(
                                    convertMediaUriToPath(selectedVideo));
                        } else if (mImgID == R.id.list_img3) {
                            file3 = new File(
                                    convertMediaUriToPath(selectedVideo));
                        } else if (mImgID == R.id.list_img4) {
                            file4 = new File(
                                    convertMediaUriToPath(selectedVideo));
                        } else if (mImgID == R.id.list_img5) {
                            file5 = new File(
                                    convertMediaUriToPath(selectedVideo));
                        }
                        thumb = ThumbnailUtils.createVideoThumbnail(
                                convertMediaUriToPath(selectedVideo), 70);
                        // mListImg1.setImageBitmap(thumb);
                        // play_btn1.setVisibility(View.VISIBLE);
                    }

                    setVideoImg(mImgID, thumb);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setVideoImg(int mImgID, Bitmap thumb) {
        String photo_order = "";
        if (mImgID == R.id.list_img1) {
            photo_order = "1";
            ListingActivity.photo_vid_entity.setVid_bitmap1(thumb);
            isFirst = true;
            mListImg1.setImageBitmap(thumb);
            play_btn1.setVisibility(View.VISIBLE);
            mListCloseBtn1.setVisibility(View.VISIBLE);
            if (isSecond && isThird) {
                mRightArrow.setVisibility(View.INVISIBLE);
                mLeftArrow.setVisibility(View.VISIBLE);
                if (mEnhancedProfile.equals("1")) {
                    mHorizontalScrollView.fullScroll(View.FOCUS_RIGHT);
                }
            }
        } else if (mImgID == R.id.list_img2) {
            photo_order = "2";
            ListingActivity.photo_vid_entity.setVid_bitmap2(thumb);
            isSecond = true;
            mListImg2.setImageBitmap(thumb);
            play_btn2.setVisibility(View.VISIBLE);
            mListCloseBtn2.setVisibility(View.VISIBLE);
            if (isFirst && isThird) {
                mRightArrow.setVisibility(View.INVISIBLE);
                mLeftArrow.setVisibility(View.VISIBLE);
                if (mEnhancedProfile.equals("1")) {
                    mHorizontalScrollView.fullScroll(View.FOCUS_RIGHT);
                }
            }
        } else if (mImgID == R.id.list_img3) {
            photo_order = "3";
            ListingActivity.photo_vid_entity.setVid_bitmap3(thumb);
            isThird = true;
            mListImg3.setImageBitmap(thumb);
            play_btn3.setVisibility(View.VISIBLE);
            mListCloseBtn3.setVisibility(View.VISIBLE);
            if (isFirst && isSecond) {
                mRightArrow.setVisibility(View.INVISIBLE);
                mLeftArrow.setVisibility(View.VISIBLE);
                if (mEnhancedProfile.equals("1")) {
                    mHorizontalScrollView.fullScroll(View.FOCUS_RIGHT);
                }
            }
        } else if (mImgID == R.id.list_img4) {
            photo_order = "4";
            ListingActivity.photo_vid_entity.setVid_bitmap4(thumb);
            isFourth = true;
            play_btn4.setVisibility(View.VISIBLE);
            mListImg4.setImageBitmap(thumb);
            mListCloseBtn4.setVisibility(View.VISIBLE);
        } else if (mImgID == R.id.list_img5) {
            photo_order = "5";
            ListingActivity.photo_vid_entity.setVid_bitmap5(thumb);
            isFifth = true;
            mListImg5.setImageBitmap(thumb);
            play_btn5.setVisibility(View.VISIBLE);
            mListCloseBtn5.setVisibility(View.VISIBLE);
        }

        showDescriptionAlert(photo_order, thumb);
    }

    protected String convertMediaUriToPath(Uri uri) {
        String[] proj = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }

    private File getFilename() {
        if (ListingActivity.file_position.equals("1")) {
            file1 = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                    "cameravideo1.mp4");

            System.out.println("audio " + file1);
            return file1;
        } else if (ListingActivity.file_position.equals("2")) {
            file2 = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                    "cameravideo2.mp4");

            System.out.println("audio " + file2);
            return file2;
        } else if (ListingActivity.file_position.equals("3")) {
            file3 = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                    "cameravideo3.mp4");

            System.out.println("audio " + file3);
            return file3;
        } else if (ListingActivity.file_position.equals("4")) {
            file4 = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                    "cameravideo4.mp4");

            System.out.println("audio " + file4);
            return file4;
        } else if (ListingActivity.file_position.equals("5")) {
            file5 = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                    "cameravideo5.mp4");

            System.out.println("audio " + file5);
            return file5;
        }
        return null;
    }

    private void setImage(int mImgID, Bitmap mBitmap) {
        String photo_order = "";
        mBitmapFromDevice = mBitmap;
        ByteArrayOutputStream oututStream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, oututStream);
        byte[] mainImageData = (oututStream).toByteArray();

        if (mImgID == R.id.list_img1) {
            photo_order = "1";
            ListingActivity.photo_vid_entity.setImg_bitmap1(mBitmap);
            isFirst = true;
            mPropImg1 = mainImageData;
            mListImg1.setImageBitmap(mBitmap);
            mListCloseBtn1.setVisibility(View.VISIBLE);
            if (isSecond && isThird) {
                mRightArrow.setVisibility(View.INVISIBLE);
                mLeftArrow.setVisibility(View.VISIBLE);
                if (mEnhancedProfile.equals("1")) {
                    mHorizontalScrollView.fullScroll(View.FOCUS_RIGHT);
                }
            }
        } else if (mImgID == R.id.list_img2) {
            photo_order = "2";
            ListingActivity.photo_vid_entity.setImg_bitmap2(mBitmap);
            isSecond = true;
            mPropImg2 = mainImageData;
            mListImg2.setImageBitmap(mBitmap);
            mListCloseBtn2.setVisibility(View.VISIBLE);
            if (isFirst && isThird) {
                mRightArrow.setVisibility(View.INVISIBLE);
                mLeftArrow.setVisibility(View.VISIBLE);
                if (mEnhancedProfile.equals("1")) {
                    mHorizontalScrollView.fullScroll(View.FOCUS_RIGHT);
                }
            }
        } else if (mImgID == R.id.list_img3) {
            photo_order = "3";
            ListingActivity.photo_vid_entity.setImg_bitmap3(mBitmap);
            isThird = true;
            mPropImg3 = mainImageData;
            mListImg3.setImageBitmap(mBitmap);
            mListCloseBtn3.setVisibility(View.VISIBLE);
            if (isFirst && isSecond) {
                mRightArrow.setVisibility(View.INVISIBLE);
                mLeftArrow.setVisibility(View.VISIBLE);
                if (mEnhancedProfile.equals("1")) {
                    mHorizontalScrollView.fullScroll(View.FOCUS_RIGHT);
                }
            }
        } else if (mImgID == R.id.list_img4) {
            photo_order = "4";
            ListingActivity.photo_vid_entity.setImg_bitmap4(mBitmap);
            isFourth = true;
            mPropImg4 = mainImageData;
            mListImg4.setImageBitmap(mBitmap);
            mListCloseBtn4.setVisibility(View.VISIBLE);
        } else if (mImgID == R.id.list_img5) {
            photo_order = "5";
            ListingActivity.photo_vid_entity.setImg_bitmap5(mBitmap);
            isFifth = true;
            mPropImg5 = mainImageData;
            mListImg5.setImageBitmap(mBitmap);
            mListCloseBtn5.setVisibility(View.VISIBLE);
        }

        showDescriptionAlert(photo_order, mBitmap);
    }

    private void showDescriptionAlert(final String order, Bitmap thumb) {
        final Dialog d3 = new Dialog(AddListingActivity.this,
                android.R.style.Theme_Translucent_NoTitleBar);
        d3.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d3.setContentView(R.layout.post_photo_video_view);
        d3.setCancelable(false);
        TextView post_desc = (TextView) d3.findViewById(R.id.post_desc);
        LinearLayout cancel = (LinearLayout) d3.findViewById(R.id.back_icon);
        final EditText mDescription = (EditText) d3
                .findViewById(R.id.description);
        ImageView capture_image = (ImageView) d3
                .findViewById(R.id.capture_image);

        capture_image.setImageBitmap(thumb);

        post_desc.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (order.equals("1")) {
                    description1 = mDescription.getText().toString();
                } else if (order.equals("2")) {
                    description2 = mDescription.getText().toString();
                } else if (order.equals("3")) {
                    description3 = mDescription.getText().toString();
                } else if (order.equals("4")) {
                    description4 = mDescription.getText().toString();
                } else if (order.equals("5")) {
                    description5 = mDescription.getText().toString();
                }
                d3.dismiss();
            }
        });
        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                d3.dismiss();
            }
        });

        d3.show();
    }

    private void validateAddProperty() {

        mAddress = mAddressEdit.getText().toString().trim();
        mDescription = mDescriptionEdit.getText().toString().trim();
        mPrice = mPriceEdit.getText().toString().trim();
        mSquare = mSquareEdit.getText().toString().trim();
        mYear = mYearEdit.getText().toString().trim();
        if (AppConstants.CheckingPosition.equalsIgnoreCase(AppConstants.one)){
            if (!mLotEdit.getText().toString().isEmpty()){
                mAcreStr  = String.valueOf(Integer.parseInt(mLotEdit.getText().toString())*43560);
                mLot = mAcreStr;
            }
        }else {
            mLot = mLotEdit.getText().toString().trim();
        }

        mMLS = mMLSEdit.getText().toString().trim();
        if (mMLS == null) {
            mMLS = "";
        }
        mPropType = mPropertyTypeTxt.getText().toString().trim();

        if (mPropType.equalsIgnoreCase("Select")) {
            mPropType = "";
        }

        if (!isFirst && !isSecond && !isThird && !isFourth && !isFifth) {
            mBitmapFromDevice = null;
        }

        if (mBitmapFromDevice == null) {
            DialogManager.showCustomAlertDialog(this, this,
                    getString(R.string.please_upload_image));

        } else if (mAddress.equals("") && mAddress.length() <= 0) {
            DialogManager.showCustomAlertDialog(this, this,
                    getString(R.string.please_enter_address));
        } else if (mDescription.equals("") && mDescription.length() <= 0) {
            DialogManager.showCustomAlertDialog(this, this,
                    getString(R.string.please_enter_description));
        } else if (mPrice.equals("") && mPrice.length() <= 0) {
            DialogManager.showCustomAlertDialog(this, this,
                    getString(R.string.please_enter_price));
        } else {
            callAddProperty();
        }
    }

    private void callAddProperty() {
        long length1 = 0, length2 = 0, length3 = 0, length4 = 0, length5 = 0;
        String Url = AppConstants.BASE_URL + "addnewproperty";

        GsonTransformer t = new GsonTransformer();
        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("user_id", UserID);
        params.put("property_name", "");
        params.put("address", mAddress);
        params.put("type", mType);
        params.put("latitude", mLatitude);
        params.put("longitude", mLongtitude);
        params.put("description", mDescription);
        params.put("property_type", mPropType);
        params.put("price_range", mPrice);
        params.put("beds", mBeds);
        params.put("baths", mBaths);
        params.put("fee", mNoFee);
        params.put("square_footage", mSquare);
        params.put("build_year", mYear);
        params.put("lot_size", mLot);
        params.put("new_construction", mNewConstruction);
        params.put("foreclosure", mForclosure);
        params.put("open_house", mOpenHouses);
        params.put("reduced_prices", mReducedPrices);
        params.put("MLS", mMLS);
        params.put("resale", mResale);
        params.put("property_image1", mPropImg1);
        params.put("property_image2", mPropImg2);
        params.put("property_image3", mPropImg3);
        params.put("property_image4", mPropImg4);
        params.put("property_image5", mPropImg5);
        params.put("description1", description1);
        params.put("description2", description2);
        params.put("description3", description3);
        params.put("description4", description4);
        params.put("description5", description5);
        params.put("property_id", property_id);
        if (file1 != null && !file1.equals("")) {

            try {
                bFile1 = new byte[(int) file1.length()];
                fileInputStream = new FileInputStream(file1);
                fileInputStream.read(bFile1);
                fileInputStream.close();
                length1 = file1.length();
                length1 = length1 / 1024;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }

        if (file2 != null && !file2.equals("")) {

            try {
                bFile2 = new byte[(int) file2.length()];
                fileInputStream = new FileInputStream(file2);
                fileInputStream.read(bFile2);
                fileInputStream.close();
                length2 = file2.length();
                length2 = length2 / 1024;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }

        if (file3 != null && !file3.equals("")) {

            try {
                bFile3 = new byte[(int) file3.length()];
                fileInputStream = new FileInputStream(file3);
                fileInputStream.read(bFile3);
                fileInputStream.close();
                length3 = file3.length();
                length3 = length3 / 1024;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }

        if (file4 != null && !file4.equals("")) {

            try {
                bFile4 = new byte[(int) file4.length()];
                fileInputStream = new FileInputStream(file4);
                fileInputStream.read(bFile4);
                fileInputStream.close();
                length4 = file4.length();
                length4 = length4 / 1024;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }

        if (file5 != null && !file5.equals("")) {

            try {
                bFile5 = new byte[(int) file5.length()];
                fileInputStream = new FileInputStream(file5);
                fileInputStream.read(bFile5);
                fileInputStream.close();
                length5 = file5.length();
                length5 = length5 / 1024;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        params.put("property_video1", bFile1);
        params.put("property_video2", bFile2);
        params.put("property_video3", bFile3);
        params.put("property_video4", bFile4);
        params.put("property_video5", bFile5);

        if (length1 > 25360 || length2 > 25360 || length3 > 25360
                || length4 > 25360 || length5 > 25360) {
            mDialog = DialogManager.showDialog(this,
                    "Video Limit Exceeded. Max size is 25 MB.",
                    getString(R.string.ok), null, 0, 0);
        } else {

            aq().transformer(t)
                    .progress(DialogManager.getProgressDialog(this))
                    .ajax(Url, params, JSONObject.class,
                            new AjaxCallback<JSONObject>() {

                                @Override
                                public void callback(String url,
                                                     JSONObject json, AjaxStatus status) {
//									if (json != null) {
                                    // CommonResponse mResponse = new Gson()
                                    // .fromJson(json.toString(),
                                    // CommonResponse.class);
                                    // onSuccessResponse(mResponse);
                                    onSuccessResponse();
//									} else {
//										statusErrorCode(status);
//									}
                                }

                            });
        }
    }

    // protected void onSuccessResponse(CommonResponse mResponse) {
    protected void onSuccessResponse() {

        mCallAPI = "CallAPI";
        if (prop_entity != null) {
            DialogManager.showCustomAlertDialog(this, this,
                    "Successfully Updated.");
        } else {
            DialogManager.showCustomAlertDialog(this, this,
                    "Successfully Created.");
        }
    }

    @Override
    public void onItemclick(String SelctedItem, int pos) {

    }

    @Override
    public void onOkclick() {
        if (mCallAPI.equalsIgnoreCase("CallAPI")) {
            launchActivity(ListingActivity.class);
            overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_left);
        } else {
            /**
             * Close Popup
             */
        }
    }

    class downloadImageUrl extends AsyncTask<String, Void, File> {

        String position = "";

        @Override
        protected File doInBackground(String... params) {
            position = params[1];
            return Downloadvideo(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(File result) {
            super.onPostExecute(result);
            if (position.equals("0")) {
                file1 = result;
            } else if (position.equals("1")) {
                file2 = result;
            } else if (position.equals("2")) {
                file3 = result;
            } else if (position.equals("3")) {
                file4 = result;
            } else if (position.equals("4")) {
                file5 = result;
            }

        }

    }

    public File Downloadvideo(String Url, String position) {

        String filepath = null;
        try {
            URL url = new URL(Url);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            File SDCardRoot = Environment.getExternalStorageDirectory();
            String filename = "";
            if (position.equals("0")) {
                filename = "downloadedFile1.mp4";
                // file = file1;
            } else if (position.equals("1")) {
                filename = "downloadedFile2.mp4";
                // file = file2;
            } else if (position.equals("2")) {
                filename = "downloadedFile3.mp4";
                // file = file3;
            } else if (position.equals("3")) {
                filename = "downloadedFile4.mp4";
                // file = file4;
            } else if (position.equals("4")) {
                filename = "downloadedFile5.mp4";
                // file = file5;
            }
            Log.i("Local filename:", "" + filename);
            file = new File(SDCardRoot, filename);
            if (file.createNewFile()) {
                file.createNewFile();
            }
            FileOutputStream fileOutput = new FileOutputStream(file);
            InputStream inputStream = urlConnection.getInputStream();
            int totalSize = urlConnection.getContentLength();
            int downloadedSize = 0;
            byte[] buffer = new byte[1024];
            int bufferLength = 0;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                Log.i("Progress:", "downloadedSize:" + downloadedSize
                        + "totalSize:" + totalSize);
            }
            fileOutput.close();
            if (downloadedSize == totalSize)
                filepath = file.getPath();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            filepath = null;
            e.printStackTrace();
        }
        return file;

    }
}
