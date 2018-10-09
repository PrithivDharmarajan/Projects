package com.smaat.ipharma.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smaat.ipharma.R;
import com.smaat.ipharma.adapter.NavigationAdapter;
import com.smaat.ipharma.entity.MapPropertyEntity;
import com.smaat.ipharma.fragments.AddReminderScreen;
import com.smaat.ipharma.fragments.FavouritesListFragment;
import com.smaat.ipharma.fragments.HistoryFragment;
import com.smaat.ipharma.fragments.OfferFragment;
import com.smaat.ipharma.fragments.OrderDetailFragment;
import com.smaat.ipharma.fragments.PaymentFragment;
import com.smaat.ipharma.fragments.PaymentOptionsFragment;
import com.smaat.ipharma.fragments.PharmacyListFragment;
import com.smaat.ipharma.fragments.PillReminderListFragment;
import com.smaat.ipharma.fragments.ProfileFragment;
import com.smaat.ipharma.fragments.RecentOrderListFragment;
import com.smaat.ipharma.fragments.ReminderSettings;
import com.smaat.ipharma.fragments.SettingFragment;
import com.smaat.ipharma.fragments.ShopdetailFragment;
import com.smaat.ipharma.fragments.ViewShopMapFragment;
import com.smaat.ipharma.main.BaseActivity;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.utils.AppConstants;
import com.smaat.ipharma.utils.DialogManager;
import com.smaat.ipharma.utils.GlobalMethods;
import com.smaat.ipharma.utils.ImageCallback;
import com.smaat.ipharma.utils.ProfileImageSelectionUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import static com.smaat.ipharma.utils.AppConstants.Choosed_Image;
import static com.smaat.ipharma.utils.AppConstants.FROMHISTORY;
import static com.smaat.ipharma.utils.AppConstants.FROM_MY_ORDER;
import static com.smaat.ipharma.utils.AppConstants.SELECTED_POSITION;
import static com.smaat.ipharma.utils.AppConstants.Url;


/**
 * Created by user on 11/7/2016.
 */

public class HomeScreen extends BaseActivity {
    private String[] mNavigationDrawerItemTitles;
    private TypedArray mNavigationDrawerItemImages;
    public DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    private ViewGroup header;
    private TextView mTitle;
    private RelativeLayout mMenuicon;
    public Toolbar mToolbar;
    public static int SELECT_FILE = 0;
    public static int REQUEST_CAMERA = 1;
    public static int CROP_PIC = 2;
    private ImageView prf_image;
    private RelativeLayout side_menu;
    private TextView user_name;
    private TextView edit_profile;
    private String ImageName = "";
    private boolean isAddFrag=false;
    public static  boolean mIsInForegroundMode;
    private int backStackEntryCount = 0;
    private NavigationAdapter adapter;
    public static  ImageView right_side_menu_icon,settings_reminder;
    LinearLayout back_button;
    public static TextView address_title;
    public static Bitmap mSelectedImgBitmap = null;
    TextView footer_text;
    Fragment fragment;
    LinearLayout footer_layout;
    public static Fragment homeFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_home_fragment);
        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
        mNavigationDrawerItemImages = getResources().obtainTypedArray(R.array.navigation_drawer_images_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        side_menu = (RelativeLayout)findViewById(R.id.main_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) findViewById(R.id.toolbar_title);
        mMenuicon = (RelativeLayout) findViewById(R.id.menu_click);
        footer_text = (TextView)findViewById(R.id.footer_text);
        footer_layout = (LinearLayout)findViewById(R.id.footer_layout);
        View view = (View) findViewById(R.id.drawer_layout);
        setupUI(view);

        setupToolbar();
        setupDrawerToggle();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportFragmentManager().addOnBackStackChangedListener(getListener());


        adapter = new NavigationAdapter(HomeScreen.this, mNavigationDrawerItemTitles, mNavigationDrawerItemImages);


        LayoutInflater inflater = getLayoutInflater();
        header = (ViewGroup) inflater.inflate(R.layout.ui_header_item, mDrawerList, false);
       /* if(!GlobalMethods.getUserID(getApplicationContext()).equalsIgnoreCase(""))
        {*/
         mDrawerList.addHeaderView(header, null, false);
        /*}else{
            mDrawerList.removeHeaderView(header);
        }*/

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!GlobalMethods.movetologinscreen(HomeScreen.this))
                {
                    fragment = new ProfileFragment();
                    pushFragment(fragment);
                    mDrawerLayout.closeDrawer(side_menu);
                }

            }
        });
        mDrawerList.setAdapter(adapter);
        right_side_menu_icon = (ImageView)mToolbar.findViewById(R.id.right_side_menu);
        address_title= (TextView) mToolbar.findViewById(R.id.address_title);
        settings_reminder = (ImageView)findViewById(R.id.pill_add_btn);
        back_button = (LinearLayout) mToolbar.findViewById(R.id.back_button);
        user_name = (TextView)header.findViewById(R.id.user_name);
        prf_image = (ImageView)header.findViewById(R.id.app_logo);
        edit_profile = (TextView)header.findViewById(R.id.edit_profile);
        if(!GlobalMethods.getUserID(getApplicationContext()).equalsIgnoreCase(""))
        {
            user_name.setText(GlobalMethods.getStringValue(getApplicationContext(),AppConstants.USER_NAME));
        }else{
            user_name.setText(getApplicationContext().getString(R.string.guest_user));
        }


        /*edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new ProfileFragment();
                pushFragment(fragment);
                mDrawerLayout.closeDrawer(side_menu);
            }
        });*/
        settings_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pushFragment(new ReminderSettings());

            }
        });


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);


        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            String mType = bundle.getString("mTypeId");
            if(!mType.equalsIgnoreCase(""))
            {
                fragment = new RecentOrderListFragment();
                replaceFragment(fragment);
            }else{
                fragment = new PharmacyListFragment();
                replaceFragment(fragment);
                setToolbarTitle(getString(R.string.home),getString(R.string.one_touch_order));
            }
        } else {
            fragment = new PharmacyListFragment();
            replaceFragment(fragment);
            setToolbarTitle(getString(R.string.home),getString(R.string.one_touch_order));
        }



    }



    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (position > 0) {
                selectItem(position - 1);
            } else {
                selectItem(position);
            }
        }

    }

    private void selectItem(int position) {

        switch (position) {
            case 0:
                if(!GlobalMethods.movetologinscreen(HomeScreen.this))
                {

                    fragment = new RecentOrderListFragment();
                }else{
                    fragment = null;
                }
                break;
            case 1:
                if(!GlobalMethods.movetologinscreen(HomeScreen.this))
                {
                    fragment = new FavouritesListFragment();
                }else{
                    fragment = null;
                }

                break;
            /*case 2:
                fragment = new OfferFragment();
                break;*/
            case 2:
                Url = AppConstants.DISCLAIMER_LINK;
                fragment = new AboutFragment();
                break;
            case 3:
                if(!GlobalMethods.movetologinscreen(HomeScreen.this))
                {
                    fragment = new PaymentFragment();
                }else{
                    fragment = null;
                }
                break;
            case 4:
                fragment = new PillReminderListFragment();
                break;
            case 5:
                if(!GlobalMethods.movetologinscreen(HomeScreen.this))
                {
                    fragment = new SettingFragment();
                }else{
                    fragment = null;
                }

                break;
        }

        pushFragment(fragment);
        mDrawerList.setItemChecked(position, true);
        mDrawerList.setSelection(position);
        setToolbarTitle(getString(R.string.home),
                getString(R.string.one_touch_order));
        mDrawerLayout.closeDrawer(side_menu);
    }

    public void pushFragment(Fragment fmt) {
        if (fmt != null) {
            if (!fmt.isAdded()) {
                isAddFrag=true;
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.content_frame, fmt)
                        .addToBackStack(fmt.getClass().getSimpleName())
                        .commit();
            }
        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }

    }


    public void replaceFragment(Fragment fmt) {
        if (fmt != null) {
            if (!fmt.isAdded()) {

                isAddFrag=true;
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fmt)
                        .commit();
            }
        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }

    }

    public void setupDrawerToggle() {
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerToggle.syncState();
        mMenuicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawer(side_menu);
                } else {
                    mDrawerLayout.openDrawer(side_menu);
                }
            }
        });

    }

    void setupToolbar() {
        setSupportActionBar(mToolbar);
    }



    private FragmentManager.OnBackStackChangedListener getListener() {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = getSupportFragmentManager();


                if (manager != null) {
                    backStackEntryCount = manager.getBackStackEntryCount();
                    //Log.e("backStackEntryCount","backStackEntryCount"+backStackEntryCount);
                    if (backStackEntryCount == -1) {
                        finish();
                    } else {
                        if(backStackEntryCount==0)
                        {
                            backStackEntryCount=-1;
                        }
                        BaseFragment currFrag = (BaseFragment) manager.findFragmentById(R.id.content_frame);
                        currFrag.onResume();
                    }

                }
                // }

            }
        };

        return result;
    }

    @Override
    public void onBackPressed() {


        backButtonClick();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mIsInForegroundMode = false;
        Log.e("ONPAUSETEST","ONPAUSE");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsInForegroundMode = true;
        Log.e("ONPAUSETEST","ONRESUMEONRESUME");
    }

    // Some function.
    public boolean isInForeground() {
        return mIsInForegroundMode;
    }


    public void setToolbarTitle(final String sTitle,final String footertitle) {
        address_title.setVisibility(View.GONE);
        if(sTitle.equalsIgnoreCase(getString(R.string.home)))
        {
            mTitle.setText("");
            address_title.setVisibility(View.VISIBLE);
            right_side_menu_icon.setVisibility(View.VISIBLE);
            right_side_menu_icon.setImageResource(R.drawable.map_view);
            back_button.setVisibility(View.INVISIBLE);
        }else if(sTitle.equalsIgnoreCase(getString(R.string.profile)))
        {
            mTitle.setText(sTitle);
            right_side_menu_icon.setVisibility(View.VISIBLE);
            right_side_menu_icon.setImageResource(R.drawable.pf_edit);
            back_button.setVisibility(View.VISIBLE);
        }else{
            mTitle.setText(sTitle);
            right_side_menu_icon.setVisibility(View.INVISIBLE);
            back_button.setVisibility(View.VISIBLE);
        }

        if(sTitle.equalsIgnoreCase(getString(R.string.pill_reminder)))
        {
            settings_reminder.setVisibility(View.VISIBLE);
        }else{
            settings_reminder.setVisibility(View.GONE);
        }

        right_side_menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sTitle.equalsIgnoreCase(getString(R.string.home)))
                {
                    PharmacyListFragment fragment = (PharmacyListFragment)
                            getSupportFragmentManager().findFragmentById(R.id.content_frame);
                    fragment.changeView(right_side_menu_icon);
                }else if(sTitle.equalsIgnoreCase(getString(R.string.profile))){
                    ProfileFragment fragment = (ProfileFragment)
                            getSupportFragmentManager().findFragmentById(R.id.content_frame);
                    fragment.UpdateProfile();
                }

            }
        });


        footer_text.setText(footertitle);

       /* footer_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(sTitle.equalsIgnoreCase(getString(R.string.pill_reminder)))
            {
                pushFragment(new AddReminderScreen());
            }else if(sTitle.equalsIgnoreCase(getString(R.string.add_reminder))){
                AddReminderScreen fragment = (AddReminderScreen)
                        getSupportFragmentManager().findFragmentById(R.id.content_frame);
                fragment.addreminder();
                pushFragment(new PillReminderListFragment());
            }
            }
        });*/


        footer_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sTitle.equalsIgnoreCase(getString(R.string.pill_reminder)))
                {
                    pushFragment(new AddReminderScreen());
                    AppConstants.EDITLOCALTIMEENTITIY = null;
                }else if(footertitle.equalsIgnoreCase(getString(R.string.save_reminder))||footertitle.equalsIgnoreCase(getString(R.string.update_reminder))){
                    AddReminderScreen fragment = (AddReminderScreen)
                            getSupportFragmentManager().findFragmentById(R.id.content_frame);
                    fragment.addreminder();

                }else if(sTitle.equalsIgnoreCase(getString(R.string.order_now))&&footertitle.equalsIgnoreCase(getString(R.string.plce_order)))
                {
                    if(!GlobalMethods.movetologinscreen(HomeScreen.this))
                    {
                        FROMHISTORY = false;
                        ProfileImageSelectionUtil.showscanPopUp(HomeScreen.this);
                    }


                }else if(sTitle.equalsIgnoreCase(getString(R.string.or_det))&&footertitle.equalsIgnoreCase(getString(R.string.plce_order)))
                {
                    if(IsConnecttointernet())
                    {
                        if(!GlobalMethods.movetologinscreen(HomeScreen.this))
                        {
                            OrderDetailFragment fragment = (OrderDetailFragment)
                                    getSupportFragmentManager().findFragmentById(R.id.content_frame);
                            fragment.LoadPlaceorder();
                        }

                    }else{
                        DialogManager.showMsgPopup(HomeScreen.this,"",getString(R.string.no_internet));
                    }

                }else if(footertitle.contains(getString(R.string.pay_rs)))
                {
                    pushFragment( new PaymentOptionsFragment());
                }else if(footertitle.equalsIgnoreCase(getString(R.string.one_touch_order))){

                    if(!GlobalMethods.movetologinscreen(HomeScreen.this))
                    {
                        if(GlobalMethods.getStringValue(getApplicationContext(),AppConstants.PharmacyID_OT).isEmpty())
                        {
                            DialogManager.enableTouch(HomeScreen.this);
                        }else{
                            FROMHISTORY = false;
                            MapPropertyEntity ppty = new MapPropertyEntity();
                            ppty.setShopIcon(GlobalMethods.getStringValue(getApplicationContext(),AppConstants.ShopIcon_OT));
                            ppty.setShopName(GlobalMethods.getStringValue(getApplicationContext(),AppConstants.ShopName_OT));
                            ppty.setAddress(GlobalMethods.getStringValue(getApplicationContext(),AppConstants.Address_OT));
                            ppty.setEmail(GlobalMethods.getStringValue(getApplicationContext(),AppConstants.Email_OT));
                            ppty.setWebsite(GlobalMethods.getStringValue(getApplicationContext(),AppConstants.Website_OT));
                            ppty.setPhone(GlobalMethods.getStringValue(getApplicationContext(),AppConstants.Phone_OT));
                            ppty.setPharmacyID(GlobalMethods.getStringValue(getApplicationContext(),AppConstants.PharmacyID_OT));
                            ppty.setTotalReviews("0");
                            AppConstants.PharmacyDetails = ppty;
                            ProfileImageSelectionUtil.showscanPopUp(HomeScreen.this);
                        }
                    }


                }else if(footertitle.equalsIgnoreCase(getString(R.string.select_pre))){

                    if(HistoryFragment.history_list.size()>0)
                    {
                        FROMHISTORY = true;
                        /*MapPropertyEntity ppty = new MapPropertyEntity();
                        ppty.setShopIcon(HistoryFragment.history_list.get(SELECTED_POSITION).getShopIcon());
                        ppty.setShopName(HistoryFragment.history_list.get(SELECTED_POSITION).getShopName());
                        ppty.setAddress(HistoryFragment.history_list.get(SELECTED_POSITION).getAddress());
                        ppty.setEmail(HistoryFragment.history_list.get(SELECTED_POSITION).getEmail());
                        ppty.setWebsite(HistoryFragment.history_list.get(SELECTED_POSITION).getWebsite());
                        ppty.setPhone(HistoryFragment.history_list.get(SELECTED_POSITION).getPhone());
                        ppty.setPharmacyID(HistoryFragment.history_list.get(SELECTED_POSITION).getShopID());
                        ppty.setAvgRating(0);
                        ppty.setTotalReviews("0");*/
                        Choosed_Image = HistoryFragment.history_list.get(SELECTED_POSITION).getPrescriptionURL();
                        //AppConstants.PharmacyDetails = ppty;
                        FROM_MY_ORDER = false;
                        pushFragment(new OrderDetailFragment());
                    }else{
                        DialogManager.showMsgPopup(HomeScreen.this,"",getString(R.string.no_history));
                    }


                }else if(footertitle.equalsIgnoreCase(getString(R.string.home)))
                {
                    //replaceFragment(new PharmacyListFragment());
                    movetoHomeScreen();

                }else if(footertitle.equalsIgnoreCase(getString(R.string.wr_review)))
                {
                    //replaceFragment(new PharmacyListFragment());
                    OpenReviewPopup();
                }

            }
        });
    }


    public void movetoHomeScreen()
    {
        if(HomeScreen.homeFragment!=null)
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, HomeScreen.homeFragment)
                    .commit();
            HomeScreen.homeFragment.onResume();
        }else{
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new PharmacyListFragment())
                    .commit();
        }
    }
    public void OpenReviewPopup()
    {
        final Dialog mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.review_popup);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        ImageView m_btnYes = (ImageView) mDialog.findViewById(R.id.yes_btn);
        ImageView m_btnNo = (ImageView) mDialog.findViewById(R.id.close_button);
        final RatingBar fav_ratingbar = (RatingBar) mDialog.findViewById(R.id.fav_ratingbar);
        fav_ratingbar.setRating(5);
        final EditText review_text = (EditText) mDialog.findViewById(R.id.review_text);

        final TextView mRating_avg_txt = (TextView) mDialog
                .findViewById(R.id.rating_avg_txt);
        final Button mRating_icons = (Button) mDialog
                .findViewById(R.id.rating_icons);
        fav_ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            public void onRatingChanged(RatingBar ratingBar,
                                        float rating, boolean fromUser) {
                switch ((int) fav_ratingbar.getRating()) {
                    case 1:
                        mRating_avg_txt.setText(getApplicationContext()
                                .getString(R.string.poor));
                        mRating_icons
                                .setBackgroundResource(R.drawable.poor);
                        break;
                    case 2:
                        mRating_avg_txt.setText(getApplicationContext()
                                .getString(R.string.bad));
                        mRating_icons.setBackgroundResource(R.drawable.bad);
                        break;
                    case 3:
                        mRating_avg_txt.setText(getApplicationContext()
                                .getString(R.string.average));
                        mRating_icons
                                .setBackgroundResource(R.drawable.average);
                        break;
                    case 4:
                        mRating_avg_txt.setText(getApplicationContext()
                                .getString(R.string.good));
                        mRating_icons
                                .setBackgroundResource(R.drawable.good);
                        break;
                    case 5:
                        mRating_avg_txt.setText(getApplicationContext()
                                .getString(R.string.excellent));
                        mRating_icons
                                .setBackgroundResource(R.drawable.excellent);
                        break;
                }
            }
        });

        m_btnYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mDialog.dismiss();
                final String rating = String.valueOf(fav_ratingbar.getRating());
                if(review_text.getText().toString().trim().isEmpty())
                {
                    DialogManager.showMsgPopup(HomeScreen.this,"",getString(R.string.enter_review));
                }else if(rating.equalsIgnoreCase("0.0"))
                {
                    DialogManager.showMsgPopup(HomeScreen.this,"",getString(R.string.enter_rating));
                }else{
                    OrderDetailFragment fragment = (OrderDetailFragment)
                            getSupportFragmentManager().findFragmentById(R.id.content_frame);
                    fragment.WriteReview(review_text.getText().toString().trim(),""+fav_ratingbar.getRating());
                }

            }
        });
        m_btnNo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();


            }
        });

        mDialog.show();
    }

    public void backButtonClick() {

        if(mDrawerLayout.isDrawerOpen(Gravity.LEFT))
        {
            mDrawerLayout.closeDrawer(side_menu);
        }
        int count = getFragmentManager().getBackStackEntryCount();
        if (getFragmentManager() != null) {
            if (count == 0) {
                FragmentManager manager = getSupportFragmentManager();
                BaseFragment currFrag = (BaseFragment) manager.findFragmentById(R.id.content_frame);
                if(currFrag instanceof RecentOrderListFragment)
                {
                    movetoHomeScreen();
                }else if(currFrag instanceof FavouritesListFragment)
                {
                    movetoHomeScreen();
                }else if(currFrag instanceof AboutFragment)
                {
                    movetoHomeScreen();
                }else if(currFrag instanceof PaymentFragment)
                {
                    movetoHomeScreen();
                }else if(currFrag instanceof PillReminderListFragment)
                {
                    movetoHomeScreen();
                }else if(currFrag instanceof SettingFragment)
                {
                    movetoHomeScreen();
                }else{
                    super.onBackPressed();
                }

            } else {
                getFragmentManager().popBackStack();
            }
        }


    }

    public ImageCallback mPhotoUpload = new ImageCallback() {

        @Override
        public void imageDetails(Bitmap data) {

            Bitmap image = data;

            if (image != null) {

                mSelectedImgBitmap = image;

                /*mSelectedImgBitmap = image;

                AppConstants.FRAG = AppConstants.ORDERNOW_SCREEN;
                mSelectedImgLocalPath = storeImage(image, "OrderImg" + "-");

                AppConstants.prescription_image = mSelectedImgLocalPath;

                HomeScreen.mFragment = new OrderDetailsFragment();
                HomeScreen.mHeaderRightLay.setVisibility(View.INVISIBLE);
                HomeScreen.mBottombar.setVisibility(View.GONE);
                HomeScreen.mFooterText.setText(R.string.place_order);
                HomeScreen.mHeaderLeft .setBackgroundResource(R.drawable.back_butto);

                AppConstants.photo = AppConstants.CAMERA;
                // AppConstants.popup = AppConstants.CAMERA;
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConstants.PHARMCAY_DETAILS,
                        OrderNowFragment.mMapDetailEntity);
                ((HomeScreen) HomeScreen.this).mFragment.setArguments(bundle);
                AppConstants.from_my_order = AppConstants.FAILURE_CODE;
                ((HomeScreen) HomeScreen.this).replaceFragment(
                        HomeScreen.mFragment, true);*/
                pushFragment(new OrderDetailFragment());
                FROM_MY_ORDER = false;

            } else {
                DialogManager.showToast(HomeScreen.this,
                        getString(R.string.image_issue));
            }
        }

    };



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if (mFragment instanceof OrderNowFragment) {
            if (resultCode == Activity.RESULT_OK) {
                // Bitmap mBitmap = null;
                if (requestCode == ProfileImageSelectionUtil.CAMERA) {

                    File file = new File(Environment.getExternalStorageDirectory()+File.separator + ProfileImageSelectionUtil.ImageName);

                    //Crop the captured image using an other intent
                    try {
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), getImageContentUri(getApplicationContext(),file));
                            Bitmap orient_bitmap = ProfileImageSelectionUtil.getCorrectOrientationImage(getApplicationContext(),getImageContentUri(getApplicationContext(),file),bitmap);
                            Uri selectedImage = getImageUri(getApplicationContext(), orient_bitmap);
                            cropCapturedImage(selectedImage);
                            //mPhotoUpload.imageDetails(orient_bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    catch(Exception e){
                        //display an error message if user device doesn't support
                        e.printStackTrace();
                    }
                } else if (requestCode == ProfileImageSelectionUtil.GALLERY) {
                    Bitmap choosed_img = ProfileImageSelectionUtil.getImage(data, HomeScreen.this);
                    choosed_img = ProfileImageSelectionUtil.getCorrectOrientationImage
                            (getApplicationContext(), data.getData(), choosed_img);
                    Uri selectedImage = getImageUri(getApplicationContext(), choosed_img);
                    cropCapturedImage(selectedImage);

                    //mPhotoUpload.imageDetails(choosed_img);
                }else if(requestCode==CROP_PIC)
                {
                   /* if(imageReturnedIntent!=null)
                    {*/
                        try {

                            String filePath = Environment.getExternalStorageDirectory()
                                    + "/temporary_holder.png";


                            Bitmap m_bitmapChoosedImg=null;
                            m_bitmapChoosedImg = BitmapFactory.decodeFile(filePath);
                            mPhotoUpload.imageDetails(m_bitmapChoosedImg);

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                //}
            } else {
                DialogManager.showToast(HomeScreen.this,
                        getString(R.string.prescription_not));
            }

        //}
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    private void onSelectFromResult(Intent data,int selectmode) {
        Bitmap choosed_img=null;


        if (selectmode == SELECT_FILE)
        {
            if (data != null) {
                try {
                    choosed_img = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    String  path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), choosed_img, "Title", null);
                    Uri uri = Uri.parse(path);
                    uri = getImageUri(getApplicationContext(),
                            getCorrectOrientationImage(getApplicationContext(),uri,choosed_img));
                    cropCapturedImage(uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if (selectmode == REQUEST_CAMERA){


            File file = new File(Environment.getExternalStorageDirectory()+File.separator + ImageName);
            //Crop the captured image using an other intent
            try {
				/*the user's device may not support cropping*/
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), getImageContentUri(getApplicationContext(),file));
                    Uri crop_uri = getImageUri(getApplicationContext(),
                            getCorrectOrientationImage(getApplicationContext(),getImageContentUri(getApplicationContext(),file),bitmap));
                    cropCapturedImage(crop_uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            catch(ActivityNotFoundException aNFE){
                //display an error message if user device doesn't support
                String errorMessage = "Sorry - your device doesn't support the crop action!";
                Toast toast = Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        /*String  path = MediaStore.Images.Media.insertImage(getContentResolver(), choosed_img, "Title", null);
        Uri uri = Uri.parse(path);
        performCrop(uri);*/
    }


    public static Bitmap getCorrectOrientationImage(Context context,Uri selectedImage, Bitmap image) {

        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        int rotate = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(filePath);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 1);

            Matrix matrix = new Matrix();

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                rotate = 90;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                rotate = 180;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                rotate = 270;
            }
            if (rotate != 0) {
                int w = image.getWidth();
                int h = image.getHeight();

                matrix.preRotate(rotate);
// Rotate the bitmap
                image = Bitmap.createBitmap(image, 0, 0, w, h, matrix, true);
                image = image.copy(Bitmap.Config.ARGB_8888, true);
            }
        } catch (Exception exception) {
            Log.d("check", "Could not rotate the image");
        }
        return image;
    }

    public void cropCapturedImage(Uri picUri){
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        //indicate image type and Uri
        cropIntent.setDataAndType(picUri, "image/*");
        //set crop properties
        cropIntent.putExtra("crop", "true");
        //indicate aspect of desired crop
        /*cropIntent.putExtra("aspectX", 8);
        cropIntent.putExtra("aspectY", 6);
        cropIntent.putExtra("outputX", 800);
        cropIntent.putExtra("outputY", 600);*/
        cropIntent.putExtra("return-data", false);

        File f = new File(Environment.getExternalStorageDirectory(),
                "/temporary_holder.png");
        try {
            f.createNewFile();
        } catch (IOException ex) {
            Log.e("io", ex.getMessage());
        }

        Uri uriOutput = Uri.fromFile(f);

        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriOutput);

        startActivityForResult(cropIntent, CROP_PIC);
    }


    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public void setProfileName()
    {
        user_name.setText(GlobalMethods.getStringValue(getApplicationContext(), AppConstants.USER_NAME));
    }
}