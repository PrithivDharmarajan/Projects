package com.bridgellc.bridge.ui.upload;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.main.BaseFragmentActivity;
import com.bridgellc.bridge.ui.home.HomeScreenActivity;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogManagerTwoBtnCallback;
import com.bridgellc.bridge.utils.TypefaceSingleton;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class UpdateItemScreen extends BaseFragmentActivity {

    public ArrayList<Fragment> mFragmentsList = new ArrayList<Fragment>();
    private ViewPager mUploadScreenPager;

    private TextView titleTv;
    private ImageView mHeaderLeftImage, mHeaderRightImage;
    public UploadPhotosFragment mUploadPhotosFragment;
    public UploadPublishFragment mUploadPublishFragment;

    public UploadDropboxPreviewFragment mUploadDropboxPreviewFragment;
    public UploadDropboxOrginalFragment mUploadDropboxOrginalFragment;

    public static boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_screen);
        isEdit = true;
        initComponents();

        mLightFont = TypefaceSingleton.getTypeface().getLightFont(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isEdit = false;
    }

    private void initComponents() {
        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);

        titleTv = (TextView) findViewById(R.id.header_txt);
        titleTv.setText(getString(R.string.update_txt).toUpperCase(Locale.ENGLISH));
        mHeaderLeftImage = (ImageView) findViewById(R.id.header_left_img);
        mHeaderRightImage = (ImageView) findViewById(R.id.header_right_img);
        mHeaderLeftImage.setImageResource(R.drawable.back_img);
        mHeaderRightImage.setImageResource(R.drawable.refresh_icon);

        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        mHeadeRightBtnLay = (RelativeLayout) findViewById(R.id.header_right_btn_lay);


        mHeadeLeftBtnLay.setVisibility(View.VISIBLE);
        mHeadeRightBtnLay.setVisibility(View.VISIBLE);

        getBitmapsFromImagePath();

        mUploadScreenPager = (ViewPager) findViewById(R.id.uploadPager);

        mHeadeLeftBtnLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPrevFragment();
            }
        });

        mHeadeRightBtnLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogManager.showBaseTwoBtnDialog(UpdateItemScreen.this, getString(R.string.app_name), getString(R.string.are_want_start), getString(R.string.yes), getString(R.string.no), new DialogManagerTwoBtnCallback() {
                    @Override
                    public void onBtnOkClick(String mOkStr) {

                        UploadScreen.mEntity = new UploadEntityClass();
                        if (AppConstants.UPLOAD_DATA != null && !AppConstants.UPLOAD_DATA.isEmpty()) {
                            UploadScreen.mEntity = new Gson().fromJson(AppConstants.UPLOAD_DATA, UploadEntityClass.class);
                        }
                        addFragment(new UploadItemServiceNameFragment(), 0);
                    }

                    @Override
                    public void onBtnCancelClick(String mCancelStr) {

                    }
                });

            }
        });

        addFragment(new UploadItemServiceNameFragment(), 0);

        mUploadScreenPager
                .setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position,
                                               float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        Fragment mFragment = mFragmentsList.get(position);

                        if (mFragment instanceof UploadDropboxPreviewFragment) {
                            titleTv.setText(getString(R.string.upload_preview).toUpperCase(Locale.ENGLISH));
                        } else if (mFragment instanceof UploadDropboxOrginalFragment) {
                            titleTv.setText(getString(R.string.upload_orginal).toUpperCase(Locale.ENGLISH));
                        } else {
                            titleTv.setText(getString(R.string.update_txt).toUpperCase(Locale.ENGLISH));
                        }

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });


    }


    public void showPrevFragment() {
        if (slidingDrawer.isOpened()) {
            slidingDrawer.animateClose();
        }

        int position = mUploadScreenPager.getCurrentItem();
        if (position > 0) {
            mUploadScreenPager.setCurrentItem((position - 1));
        } else {

            finishScreen();
        }

    }

    @Override
    public void addFragment(Fragment fragment, int position) {

        position = position - 3;

        if (mFragmentsList.size() > position) {
            ArrayList<Fragment> mNewList = new ArrayList<>();
            for (int i = 0; i < position; i++) {
                mNewList.add(mFragmentsList.get(i));
            }
            mFragmentsList = mNewList;
        }
        if (fragment instanceof UploadPhotosFragment) {
            mUploadPhotosFragment = (UploadPhotosFragment) fragment;
        }

        if (fragment instanceof UploadPublishFragment) {
            mUploadPublishFragment = (UploadPublishFragment) fragment;
        }
        if (fragment instanceof UploadDropboxPreviewFragment) {
            mUploadDropboxPreviewFragment = (UploadDropboxPreviewFragment) fragment;
        }

        mFragmentsList.add(fragment);
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mUploadScreenPager.setAdapter(myPagerAdapter);
        mUploadScreenPager.setOffscreenPageLimit(1);
        mUploadScreenPager.setCurrentItem(mFragmentsList.size());
    }


    ArrayList<Bitmap> mBitmapArray = new ArrayList<>();


    public void getBitmapsFromImagePath() {

        if (UploadScreen.mEntity.imagePath1 != null && 0 < UploadScreen.mEntity.imagePath1.length
                () && UploadScreen.mEntity.imagePath1.contains("http")) {

            new AsyncTask<Void, Void, Void>() {

                private Bitmap theBitmap = null;

                @Override
                protected Void doInBackground(Void... params) {
//                    Looper.prepare();
                    try {
                        theBitmap = Glide.
                                with(UpdateItemScreen.this).
                                load(UploadScreen.mEntity.imagePath1).
                                asBitmap().
                                into(-1, -1).
                                get();
                    } catch (final ExecutionException e) {

                    } catch (final InterruptedException e) {

                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void dummy) {
                    if (null != theBitmap) {
                        // The full bitmap should be available here
                        mBitmapArray.add(theBitmap);
                        switch (mBitmapArray.size()) {
                            case 3:
                                UploadScreen.mEntity.bitmap3 = mBitmapArray.get(2);
                            case 2:
                                UploadScreen.mEntity.bitmap2 = mBitmapArray.get(1);
                            case 1:
                                UploadScreen.mEntity.bitmap1 = mBitmapArray.get(0);
                                break;
                        }

                    }
                    ;
                }
            }.execute();


        }
        if (UploadScreen.mEntity.imagePath2 != null && UploadScreen.mEntity.imagePath2.length

                () > 0 && UploadScreen.mEntity.imagePath2.contains("http")) {
            new AsyncTask<Void, Void, Void>() {

                private Bitmap theBitmap = null;

                @Override
                protected Void doInBackground(Void... params) {
                    Looper.prepare();
                    try {
                        theBitmap = Glide.
                                with(UpdateItemScreen.this).
                                load(UploadScreen.mEntity.imagePath2).
                                asBitmap().
                                into(-1, -1).
                                get();
                    } catch (final ExecutionException e) {

                    } catch (final InterruptedException e) {

                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void dummy) {
                    if (null != theBitmap) {
                        // The full bitmap should be available here
                        mBitmapArray.add(theBitmap);
                        switch (mBitmapArray.size()) {
                            case 3:
                                UploadScreen.mEntity.bitmap3 = mBitmapArray.get(2);
                            case 2:
                                UploadScreen.mEntity.bitmap2 = mBitmapArray.get(1);
                            case 1:
                                UploadScreen.mEntity.bitmap1 = mBitmapArray.get(0);
                                break;
                        }

                    }
                }
            }.execute();
        }
        if (UploadScreen.mEntity.imagePath3 != null && UploadScreen.mEntity.imagePath3
                .length
                        () > 0 && UploadScreen.mEntity.imagePath3.contains("http")) {
            new AsyncTask<Void, Void, Void>() {

                private Bitmap theBitmap = null;

                @Override
                protected Void doInBackground(Void... params) {
                    Looper.prepare();
                    try {
                        theBitmap = Glide.
                                with(UpdateItemScreen.this).
                                load(UploadScreen.mEntity.imagePath3).
                                asBitmap().
                                into(-1, -1).
                                get();
                    } catch (final ExecutionException e) {

                    } catch (final InterruptedException e) {

                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void dummy) {
                    if (null != theBitmap) {
                        // The full bitmap should be available here
                        mBitmapArray.add(theBitmap);
                        switch (mBitmapArray.size()) {
                            case 3:
                                UploadScreen.mEntity.bitmap3 = mBitmapArray.get(2);
                            case 2:
                                UploadScreen.mEntity.bitmap2 = mBitmapArray.get(1);
                            case 1:
                                UploadScreen.mEntity.bitmap1 = mBitmapArray.get(0);
                                break;
                        }

                    }
                    ;
                }
            }.execute();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            if (requestCode == UploadDropboxPreviewFragment.DBX_CHOOSER_REQUEST) {
                mUploadDropboxPreviewFragment.onActivityResult(requestCode, resultCode, data);
            } else if (requestCode == UploadDropboxOrginalFragment.DX_CHOOSER_REQUEST) {
                mUploadDropboxOrginalFragment.onActivityResult(requestCode, resultCode, data);
            } else {
                mUploadPhotosFragment.onActivityResult(requestCode, resultCode, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            previousScreen(HomeScreenActivity.class, true);
        }

//        if (requestCode == UploadDropboxPreviewFragment.DBX_CHOOSER_REQUEST) {
//            mUploadDropboxPreviewFragment.onActivityResult(requestCode, resultCode, data);
//        } else {
//            mUploadPhotosFragment.onActivityResult(requestCode, resultCode, data);
//        }
    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);
        mUploadPublishFragment.onRequestSuccess(responseObj);
    }

    class MyPagerAdapter extends FragmentStatePagerAdapter {


        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return mFragmentsList.size();
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            return mFragmentsList.get(position);
        }


    }

    @Override
    public void onBackPressed() {

        showPrevFragment();
    }

}
