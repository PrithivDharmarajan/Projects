package com.smaat.ipharma.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.smaat.ipharma.R;
import com.smaat.ipharma.fragment.AboutFragment;
import com.smaat.ipharma.fragment.FavoriteFragment;
import com.smaat.ipharma.fragment.HistoryFragment;
import com.smaat.ipharma.fragment.IpharmaMoneyFragment;
import com.smaat.ipharma.fragment.MapScreenFragment;
import com.smaat.ipharma.fragment.MessagesFragment;
import com.smaat.ipharma.fragment.MoreFragment;
import com.smaat.ipharma.fragment.MyOrderFragment;
import com.smaat.ipharma.fragment.OffersFragment;
import com.smaat.ipharma.fragment.OneTouchFragment;
import com.smaat.ipharma.fragment.OneTouchListSelectFragment;
import com.smaat.ipharma.fragment.OrderDetailsFragment;
import com.smaat.ipharma.fragment.OrderNowFragment;
import com.smaat.ipharma.fragment.PaymentOptionsFragment;
import com.smaat.ipharma.fragment.PaymentOptionsTypeFragment;
import com.smaat.ipharma.fragment.PillReminderFragment;
import com.smaat.ipharma.fragment.ProfileFragment;
import com.smaat.ipharma.fragment.ReviewsFragment;
import com.smaat.ipharma.fragment.WriteReviewFragment;
import com.smaat.ipharma.main.BaseActivity;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.util.AppConstants;
import com.smaat.ipharma.util.DialogManager;
import com.smaat.ipharma.util.DialogMangerCallback;
import com.smaat.ipharma.util.GlobalMethods;
import com.smaat.ipharma.util.ImageCallback;
import com.smaat.ipharma.util.ProfileImageSelectionUtil;
import com.smaat.ipharma.util.TypefaceSingleton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@SuppressWarnings("deprecation")
public class HomeScreen extends BaseActivity implements DialogMangerCallback {
    public static BaseFragment mFragment;
    public static BaseFragment mBackMove = null;
    static ScrollView mSlideMenuBar;
    // public static SlideHolder mSlideHolder;
    public static TextView mHeaderText, mUsername, mEmailId;
    public static ImageView mHeaderMapListView;
    public static Button mHeaderLeft, mHeaderRight;
    public static TextView mFooterText;

    ImageView mMapListBtn;
    public static MapScreenFragment mMapScreen;
    public static Typeface mHelveticaNormal, mHelveticaBold, mHelveticaLight,
            mHighTower;
    public static RelativeLayout mHeaderLeftLay, mHeaderRightLay, mBottombar;
    static DrawerLayout mDrawerLayout;
    static ActionBarDrawerToggle mDrawerToggle;
    static String mSelectedImgLocalPath = "";
    public static Bitmap mSelectedImgBitmap;
    String mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_screen);
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewGroup mViewGroup = (ViewGroup) findViewById(R.id.parent_layout);
        mViewGroup.requestTransparentRegion(mViewGroup);
        Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(
                HomeScreen.this);
        setFont(mViewGroup, mTypeface);
        setupUI(mViewGroup);
        initview();

        mMapScreen = new MapScreenFragment();

        mHelveticaNormal = TypefaceSingleton.getInstance().getHelvetica(this);
        mHelveticaBold = TypefaceSingleton.getInstance().getHelveticaBold(this);
        mHelveticaLight = TypefaceSingleton.getInstance().getHelveticaLight(
                this);
        mHighTower = TypefaceSingleton.getInstance().getHighTower(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            mType = bundle.getString("mTypeId");
            mFragment = new MyOrderFragment();
            AppConstants.FROMINTLOADMAP = AppConstants.SUCCESS_CODE;
            mHeaderRightLay.setVisibility(View.INVISIBLE);
        } else {
            mFragment = new MapScreenFragment();
            mHeaderRight.setBackgroundResource(R.drawable.map_view_normal);
            mHeaderRightLay.setVisibility(View.VISIBLE);
        }

        mHeaderLeft.setBackgroundResource(R.drawable.slide);
        replaceFragment(mFragment, true);

    }

    private void initview() {
        mSlideMenuBar = (ScrollView) findViewById(R.id.slide_menu_bar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.home_drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.slide, R.string.app_name, R.string.app_name) {

            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();

            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mHeaderText = (TextView) findViewById(R.id.address_text);
        mHeaderText.setTypeface(mHelveticaBold);
        mHeaderLeftLay = (RelativeLayout) findViewById(R.id.header_left_lay);
        mHeaderRightLay = (RelativeLayout) findViewById(R.id.header_right_lay);
        mHeaderLeft = (Button) findViewById(R.id.header_left);
        mHeaderRight = (Button) findViewById(R.id.header_right);
        mBottombar = (RelativeLayout) findViewById(R.id.bottom_bar);
        mFooterText = (TextView) findViewById(R.id.footer_text);

        mUsername = (TextView) findViewById(R.id.profile);
        mUsername.setText((String) GlobalMethods.getValueFromPreference(this,
                GlobalMethods.STRING_PREFERENCE, AppConstants.USER_NAME));

        mEmailId = (TextView) findViewById(R.id.user_email);
        mEmailId.setText((String) GlobalMethods.getValueFromPreference(this,
                GlobalMethods.STRING_PREFERENCE, AppConstants.USER_EMAIL_ID));

    }

    public static void slideMenu() {
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if (mDrawerLayout.isDrawerOpen(mSlideMenuBar)) {
            mDrawerLayout.closeDrawer(mSlideMenuBar);
        } else {
            mDrawerLayout.openDrawer(mSlideMenuBar);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_layout:
                mFragment = new ProfileFragment();
                mHeaderRightLay.setVisibility(View.INVISIBLE);
                mHeaderLeft.setBackgroundResource(R.drawable.back_butto);
                replaceFragment(mFragment, true);
                break;
            case R.id.one_touch_order:
                AppConstants.from_map_list = AppConstants.TRUE;
                mFragment = new OneTouchFragment();
                mHeaderRightLay.setVisibility(View.VISIBLE);
                mHeaderRight.setBackgroundResource(R.drawable.one_touch_white);
                mHeaderLeft.setBackgroundResource(R.drawable.back_butto);
                replaceFragment(mFragment, true);
                break;
            case R.id.pill_reminder_txt:
                mFragment = new PillReminderFragment();
                mHeaderRightLay.setVisibility(View.INVISIBLE);
                mHeaderLeft.setBackgroundResource(R.drawable.back_butto);
                replaceFragment(mFragment, true);
                break;

            case R.id.order_medicine:
                AppConstants.from_map_list = AppConstants.TRUE;
                MapScreenFragment.mMapListView.setVisibility(View.VISIBLE);
                HomeScreen.mHeaderRight
                        .setBackgroundResource(R.drawable.map_view_normal);
                replaceFragment(new MapScreenFragment(), true);
                break;

            case R.id.my_order:
                mFragment = new MyOrderFragment();
                mHeaderRightLay.setVisibility(View.INVISIBLE);
                mHeaderLeft.setBackgroundResource(R.drawable.back_butto);
                replaceFragment(mFragment, true);
                break;
            case R.id.my_fav_phar:
                mFragment = new FavoriteFragment();
                mHeaderRightLay.setVisibility(View.INVISIBLE);
                mHeaderLeft.setBackgroundResource(R.drawable.back_butto);
                replaceFragment(mFragment, true);
                break;
            case R.id.offers:
                mFragment = new OffersFragment();
                mHeaderRightLay.setVisibility(View.VISIBLE);
                mHeaderLeft.setBackgroundResource(R.drawable.back_butto);
                replaceFragment(mFragment, true);
                break;
            case R.id.ipharma_money:
                mFragment = new IpharmaMoneyFragment();
                mHeaderRightLay.setVisibility(View.INVISIBLE);
                mHeaderLeft.setBackgroundResource(R.drawable.back_butto);
                replaceFragment(mFragment, true);
                break;

            case R.id.messgaes:
                mFragment = new MessagesFragment();
                mHeaderRightLay.setVisibility(View.VISIBLE);
                mHeaderRight.setBackgroundResource(R.drawable.message_refresh);
                mHeaderText.setText(R.string.messages);
                mHeaderLeft.setBackgroundResource(R.drawable.back_butto);
                replaceFragment(mFragment, true);
                break;

            case R.id.disclaimer:
                GlobalMethods.storeValuetoPreference(this,
                        GlobalMethods.STRING_PREFERENCE, AppConstants.WEB_SCREEN,
                        AppConstants.HOME_SCREEN);
                mFragment = new AboutFragment();
                mHeaderRightLay.setVisibility(View.INVISIBLE);
                mHeaderLeft.setBackgroundResource(R.drawable.back_butto);
                replaceFragment(mFragment, true);
                break;
            case R.id.more:
                mFragment = new MoreFragment();
                mHeaderRightLay.setVisibility(View.INVISIBLE);
                mHeaderLeft.setBackgroundResource(R.drawable.back_butto);
                replaceFragment(mFragment, true);
                break;
            case R.id.signout:
                // finish();
                DialogManager.showLogoutDialog(this,
                        getString(R.string.logout_alert));
                break;
        }
        slideMenu();
    }


    public void replaceFragment(Fragment fragment, boolean isForwardAnimation) {
        if (fragment instanceof ProfileFragment) {
            mHeaderText.setText(R.string.profile);
        } else if (fragment instanceof MyOrderFragment) {
            mHeaderText.setText(R.string.my_order);
        } else if (fragment instanceof OneTouchListSelectFragment) {
            mHeaderText.setText(R.string.one_touch_order_list);
        } else if (fragment instanceof OneTouchFragment) {
            mHeaderText.setText(R.string.one_touch_order);
        } else if (fragment instanceof FavoriteFragment) {
            mHeaderText.setText(R.string.favorite);
        } else if (fragment instanceof OffersFragment) {
            mHeaderText.setText(R.string.offers);
        } else if (fragment instanceof IpharmaMoneyFragment) {
            mHeaderText.setText(R.string.ipharma_money);
        } else if (fragment instanceof MessagesFragment) {
            mHeaderText.setText(R.string.messages);
        } else if (fragment instanceof AboutFragment) {

            if (((String) GlobalMethods.getValueFromPreference(this,
                    GlobalMethods.STRING_PREFERENCE, AppConstants.WEB_SCREEN))
                    .equalsIgnoreCase(AppConstants.HOME_SCREEN)) {
                mHeaderText.setText(R.string.disclaimer);
            } else {
                mHeaderText.setText(R.string.about_us);
            }
        } else if (fragment instanceof MoreFragment) {
            mHeaderText.setText(R.string.more);
        } else if (fragment instanceof MapScreenFragment) {

            mHeaderText.setText(MapScreenFragment.add_txt);
        } else if (fragment instanceof OrderNowFragment) {
            mHeaderText.setText(R.string.order_now);
        } else if (fragment instanceof ReviewsFragment) {
            mHeaderText.setText(R.string.review);
        } else if (fragment instanceof OrderDetailsFragment) {
            mHeaderText.setText(R.string.order_details);
        } else if (fragment instanceof PaymentOptionsFragment) {
            mHeaderText.setText(R.string.payment_options);
        } else if (fragment instanceof HistoryFragment) {
            mHeaderText.setText(R.string.history);
        } else if (fragment instanceof WriteReviewFragment) {
            mHeaderText.setText(R.string.write_review);
        } else if (fragment instanceof PaymentOptionsTypeFragment) {
            mHeaderText.setText(R.string.payment_options);
        }
        mHeaderText.setTypeface(mHelveticaBold);
        UpdateDisplayFragment(fragment, isForwardAnimation);
    }

    public void UpdateDisplayFragment(Fragment mFragment,
                                      boolean isForwardAnimation) {
        if (mFragment instanceof MapScreenFragment) {
            mFragment = mMapScreen;
            mHeaderRightLay.setVisibility(View.VISIBLE);
            mHeaderLeft.setBackgroundResource(R.drawable.slide);
            mHeaderText.setText(MapScreenFragment.add_txt);
        }
        if (mFragment instanceof OrderDetailsFragment) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.home_screen_main_view, mFragment)
                    .addToBackStack(null).commitAllowingStateLoss();
        }

        if (mFragment != null && !mFragment.isDetached()) {

            if (isForwardAnimation) {
                showForwardAnimation(mFragment);
            } else {
                showBackWordAnimation(mFragment);
            }

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

    }

    public ImageCallback mPhotoUpload = new ImageCallback() {

        @Override
        public void imageDetails(Bitmap data) {

            Bitmap image = data;

            if (image != null) {


                mSelectedImgBitmap = image;

                AppConstants.FRAG = AppConstants.ORDERNOW_SCREEN;
                mSelectedImgLocalPath = storeImage(image, "OrderImg" + "-");

                AppConstants.prescription_image = mSelectedImgLocalPath;

                HomeScreen.mFragment = new OrderDetailsFragment();
                HomeScreen.mHeaderRightLay.setVisibility(View.INVISIBLE);
                HomeScreen.mBottombar.setVisibility(View.GONE);
                HomeScreen.mFooterText.setText(R.string.place_order);
                HomeScreen.mHeaderLeft
                        .setBackgroundResource(R.drawable.back_butto);

                AppConstants.photo = AppConstants.CAMERA;
                // AppConstants.popup = AppConstants.CAMERA;
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConstants.PHARMCAY_DETAILS,
                        OrderNowFragment.mMapDetailEntity);
                ((HomeScreen) HomeScreen.this).mFragment.setArguments(bundle);
                AppConstants.from_my_order = AppConstants.FAILURE_CODE;
                ((HomeScreen) HomeScreen.this).replaceFragment(
                        HomeScreen.mFragment, true);

            } else {
                DialogManager.showToast(HomeScreen.this,
                        getString(R.string.image_issue));
            }
        }

    };


    public ImageCallback mPhotoUploadOneTouch = new ImageCallback() {

        @Override
        public void imageDetails(Bitmap data) {

            Bitmap image = data;

            if (image != null) {


                mSelectedImgBitmap = image;

                mSelectedImgLocalPath = storeImage(image, "OrderImg" + "-");

                AppConstants.prescription_image = mSelectedImgLocalPath;

                AppConstants.FRAG = AppConstants.ONE_TOUCH_ORDER;
                HomeScreen.mFragment = new OrderDetailsFragment();
                HomeScreen.mHeaderRightLay.setVisibility(View.INVISIBLE);
                HomeScreen.mBottombar.setVisibility(View.GONE);
                HomeScreen.mFooterText.setText(R.string.place_order);
                HomeScreen.mHeaderLeft
                        .setBackgroundResource(R.drawable.back_butto);

                AppConstants.photo = AppConstants.CAMERA;
                // AppConstants.popup = AppConstants.CAMERA;
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConstants.PHARMCAY_DETAILS,
                        OneTouchFragment.mMapDetailEntity);
                ((HomeScreen) HomeScreen.this).mFragment.setArguments(bundle);
                AppConstants.from_my_order = AppConstants.FAILURE_CODE;
                ((HomeScreen) HomeScreen.this).replaceFragment(
                        HomeScreen.mFragment, true);

            } else {
                DialogManager.showToast(HomeScreen.this,
                        getString(R.string.image_issue));
            }
        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mFragment instanceof OrderNowFragment) {
            if (resultCode == Activity.RESULT_OK) {
                // Bitmap mBitmap = null;
                if (requestCode == ProfileImageSelectionUtil.CAMERA) {

                    mPhotoUpload.imageDetails(ProfileImageSelectionUtil
                            .getInstance().getBitmapFromCamera(HomeScreen.this,
                                    data));
                } else if (requestCode == ProfileImageSelectionUtil.GALLERY) {

                    mPhotoUpload.imageDetails(ProfileImageSelectionUtil
                            .getInstance().getBitmapFromGallery(
                                    HomeScreen.this, data));
                }
            } else {
                DialogManager.showToast(HomeScreen.this,
                        getString(R.string.prescription_not));
            }

        } else if (mFragment instanceof OneTouchFragment) {
            if (resultCode == Activity.RESULT_OK) {
                // Bitmap mBitmap = null;
                if (requestCode == ProfileImageSelectionUtil.CAMERA) {

                    mPhotoUploadOneTouch.imageDetails(ProfileImageSelectionUtil
                            .getInstance().getBitmapFromCamera(HomeScreen.this,
                                    data));
                } else if (requestCode == ProfileImageSelectionUtil.GALLERY) {

                    mPhotoUploadOneTouch.imageDetails(ProfileImageSelectionUtil
                            .getInstance().getBitmapFromGallery(
                                    HomeScreen.this, data));
                }
            } else {
                DialogManager.showToast(HomeScreen.this,
                        getString(R.string.prescription_not));
            }

        }
    }

    private String TAG = "IPharma";

    private String storeImage(Bitmap image, String fileName) {
        File pictureFile = getOutputMediaFile(fileName);
        if (pictureFile == null) {
            Log.d(TAG, "Error creating media file, check storage permissions: ");// e.getMessage());
            return "";
        }
        try {
            Options options = new BitmapFactory.Options();
            options.inScaled = false;
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }

        return pictureFile.getAbsolutePath();
    }

    /**
     * Create a File for saving an image or video
     */
    private File getOutputMediaFile(String fileName) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(
                Environment.getExternalStorageDirectory() + "/Android/data/"
                        + this.getPackageName() + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name

        File mediaFile;
        String mImageName = "IPharma_" + fileName + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + mImageName);
        return mediaFile;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();


    }

    @Override
    public void onBackPressed() {
        if (AppConstants.FRAG.equalsIgnoreCase(AppConstants.EXIT)) {

            finish();
        } else {
            if (!onBackMove(this)) {

                finish();
            }
        }
        // super.onBackPressed();
    }

    public static boolean onBackMove(Context mContext) {


        if (AppConstants.FRAG.equalsIgnoreCase(AppConstants.MAP_SCREEN)) {
            mBackMove = new MapScreenFragment();
            mBottombar.setVisibility(View.GONE);
        } else if (AppConstants.FRAG.equalsIgnoreCase(AppConstants.ONE_TOUCH_ORDER_SELECT)) {
            mBackMove = new OneTouchFragment();
            mBottombar.setVisibility(View.GONE);
        } else if (AppConstants.FRAG
                .equalsIgnoreCase(AppConstants.MYORDER_SCREEN)) {
            mBackMove = new MyOrderFragment();
        } else if (AppConstants.FRAG
                .equalsIgnoreCase(AppConstants.ORDERNOW_SCREEN)) {
            mBackMove = new OrderNowFragment();
        } else if (AppConstants.FRAG.equalsIgnoreCase(AppConstants.REVIEW)) {

            mBackMove = new ReviewsFragment();
        } else if (AppConstants.FRAG
                .equalsIgnoreCase(AppConstants.ORDER_DETAILS)) {
            mBackMove = new OrderDetailsFragment();
        } else if (AppConstants.FRAG
                .equalsIgnoreCase(AppConstants.ONE_TOUCH_ORDER)) {
            mBackMove = new OneTouchFragment();
        } else if (AppConstants.FRAG
                .equalsIgnoreCase(AppConstants.HISTORY_SCREEN)) {
            mBackMove = new HistoryFragment();
            HomeScreen.mBottombar.setVisibility(View.GONE);
            HomeScreen.mFooterText.setText(R.string.select_pres);
        } else if (AppConstants.FRAG
                .equalsIgnoreCase(AppConstants.PAYMENT_OPTION_SCREEN)) {
            mBackMove = new PaymentOptionsFragment();
            // AppConstants.from_my_order = AppConstants.FAILURE_CODE;
        } else if (AppConstants.FRAG
                .equalsIgnoreCase(AppConstants.MONEY_SCREEN)) {
            mBackMove = new IpharmaMoneyFragment();
        } else if (AppConstants.FRAG
                .equalsIgnoreCase(AppConstants.MORESCREEN)) {
            mBackMove = new MoreFragment();
        } else if (AppConstants.FRAG
                .equalsIgnoreCase(AppConstants.OFFERS_SCREEN)) {
            mBackMove = new OffersFragment();
        } else if (AppConstants.FRAG
                .equalsIgnoreCase(AppConstants.FAVORITE_SCREEN)) {
            mBackMove = new FavoriteFragment();
        } else if (AppConstants.FRAG
                .equalsIgnoreCase(AppConstants.PILL_REM_SCREEEN)) {
            mBackMove = new PillReminderFragment();
            mBottombar.setVisibility(View.GONE);
        }


        if (mBackMove != null) {
            if (mBackMove instanceof MapScreenFragment) {
                mHeaderRightLay.setVisibility(View.VISIBLE);
                mHeaderRight.setBackgroundResource(R.drawable.map_view_normal);
                mHeaderLeft.setBackgroundResource(R.drawable.slide);
            } else {

                mHeaderRightLay.setVisibility(View.INVISIBLE);
                mHeaderLeft.setBackgroundResource(R.drawable.back_butto);
            }

            ((HomeScreen) mContext).replaceFragment(mBackMove, false);

            return true;
        }
        return false;
    }

    public void onItemclick(String SelctedItem, int pos) {
        // TODO Auto-generated method stub

    }

    public void onOkclick() {
        // TODO Auto-generated method stub

    }

    private void showForwardAnimation(Fragment mFragment) {

        getSupportFragmentManager()
                .addOnBackStackChangedListener(getListener());
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right,
                        R.anim.slide_out_left, R.anim.slide_in_left,
                        R.anim.slide_out_right)
                .replace(R.id.home_screen_main_view, mFragment)
                .addToBackStack(null).commit();

    }

    private void showBackWordAnimation(Fragment mFragment) {

        getSupportFragmentManager()
                .addOnBackStackChangedListener(getListener());
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_out_right,
                        R.anim.slide_in_left, R.anim.slide_out_left,
                        R.anim.slide_in_right)
                .replace(R.id.home_screen_main_view, mFragment)
                .addToBackStack(null).commit();

    }

    private OnBackStackChangedListener getListener() {
        OnBackStackChangedListener result = new OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = getSupportFragmentManager();

                if (manager != null) {
                    Fragment currFrag = manager
                            .findFragmentById(R.id.home_screen_main_view);

                    currFrag.onResume();
                }
            }
        };

        return result;
    }

}
