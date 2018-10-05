package com.bridgellc.bridge.ui.upload;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.apiinterface.APIRequestHandler;
import com.bridgellc.bridge.main.BaseFragment;
import com.bridgellc.bridge.main.BaseFragmentActivity;
import com.bridgellc.bridge.model.UploadPictureResponse;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.GlobalMethods;
import com.bridgellc.bridge.utils.ImageViewRounded;
import com.bridgellc.bridge.utils.ProfileImageSelectionUtil;
import com.bridgellc.bridge.utils.TypefaceSingleton;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by sys on 3/14/2016.
 */
public class UploadPhotosFragment extends BaseFragment implements DialogMangerOkCallback {

    private View view;
    private ImageViewRounded imageOne, imageTwo, imageThree;
    private ImageView mCloseOneImg, mCloseTwoImg, mCloseThreeImg;
    private RelativeLayout mCloseOneImgLay, mCloseTwoImgLay, mCloseThreeImgLay;

    private ImageViewRounded selectedImageView;

    private ViewPager imageViewPager;
    public static int index;

    private Button mNextBtn;
    public static Uri selectedImage;
    private ArrayList<Bitmap> mBitmapArray = new ArrayList<Bitmap>();

    private ArrayList<String> mImagesList = new ArrayList<String>();
    private  CustomPagerAdapter customPagerAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.upload_photos, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.image_one:
                    index = 1;
                    selectedImageView = imageOne;
                    break;
                case R.id.image_two:
                    if (mImagesList.size() > 0) {
                        selectedImageView = imageTwo;
                        index = 2;
                    } else {
                        selectedImageView = imageOne;
                        index = 1;
                    }
                    break;
                case R.id.image_three:
                    if (mImagesList.size() > 1) {
                        selectedImageView = imageThree;
                        index = 3;
                    } else if (mImagesList.size() > 0) {
                        selectedImageView = imageTwo;
                        index = 2;
                    } else {
                        selectedImageView = imageOne;
                        index = 1;
                    }
                    break;
            }
            ProfileImageSelectionUtil.showOption(getActivity());
        }
    };


    private void initComponents() {
        imageOne = (ImageViewRounded) view.findViewById(R.id.image_one);
        imageTwo = (ImageViewRounded) view.findViewById(R.id.image_two);
        imageThree = (ImageViewRounded) view.findViewById(R.id.image_three);

        mCloseOneImg = (ImageView) view.findViewById(R.id.image_cls_one);
        mCloseTwoImg = (ImageView) view.findViewById(R.id.image_cls_two);
        mCloseThreeImg = (ImageView) view.findViewById(R.id.image_cls_three);

        mCloseOneImgLay = (RelativeLayout) view.findViewById(R.id.image_cls_one_lay);
        mCloseTwoImgLay = (RelativeLayout) view.findViewById(R.id.image_cls_two_lay);
        mCloseThreeImgLay = (RelativeLayout) view.findViewById(R.id.image_cls_three_lay);

        imageViewPager = (ViewPager) view.findViewById(R.id.imagePager);
        TextView titleTv = (TextView) view.findViewById(R.id.title_tv);

//        if (!UploadScreen.mEntity.isGood) {
//            titleTv.setText(getString(R.string.upload_photo_serv));
//        } else if (UploadScreen.mEntity.isGood && UploadScreen.mEntity.isSelling && !UploadScreen.mEntity.isDeliveryInPerson && UploadScreen.mEntity.category.equalsIgnoreCase(getString(R.string.ticket))) {
//
//            titleTv.setText(getString(R.string.upload_photo_event));
//        }

        imageOne.setOnClickListener(onClickListener);
        imageTwo.setOnClickListener(onClickListener);
        imageThree.setOnClickListener(onClickListener);


        mCloseOneImgLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mImagesList.size()) {
                    case 1:
                        // pos 1 img removed
//                        Glide.with(getActivity())
//                                .load(R.drawable.upload_icon_holder)
//                                .asBitmap().into(imageOne);
                        imageOne.setImageResource(R.drawable.upload_icon_holder);
                        mCloseOneImgLay.setVisibility(View.GONE);
                        mImagesList.remove(0);

                        UploadScreen.mEntity.imagePath1="";
                        UploadScreen.mEntity.imagePath2="";
                        UploadScreen.mEntity.imagePath3="";
                        break;

                    case 2:
                        // pos 2 img removed
                        mImagesList.set(0, mImagesList.get(1));
                        Glide.with(getActivity())
                                .load(mImagesList.get(0))
                                .asBitmap().into(imageOne);
                        mImagesList.remove(1);


                        //set pos 2 default img
//                        Glide.with(getActivity())
//                                .load(getActivity().getResources().getDrawable(R.drawable.upload_icon_holder))
//                                .asBitmap().into(imageTwo);

                        imageTwo.setImageResource(R.drawable.upload_icon_holder);
                        mCloseTwoImgLay.setVisibility(View.GONE);


                        UploadScreen.mEntity.imagePath1=mImagesList.get(0);
                        UploadScreen.mEntity.imagePath2="";
                        UploadScreen.mEntity.imagePath3="";
                        break;

                    case 3:
                        // pos 2 img removed
                        mImagesList.set(0, mImagesList.get(1));
                        Glide.with(getActivity())
                                .load(mImagesList.get(0))
                                .asBitmap().into(imageOne);

                        //pos 3
                        mImagesList.set(1, mImagesList.get(2));
                        Glide.with(getActivity())
                                .load(mImagesList.get(1))
                                .asBitmap().into(imageTwo);
                        mImagesList.remove(2);

                        imageThree.setImageResource(R.drawable.upload_icon_holder);
                        mCloseThreeImgLay.setVisibility(View.GONE);


                        UploadScreen.mEntity.imagePath1=mImagesList.get(0);
                        UploadScreen.mEntity.imagePath2=mImagesList.get(1);
                        UploadScreen.mEntity.imagePath3="";
                        break;


                }
                customPagerAdapter = new CustomPagerAdapter(getActivity());
                imageViewPager.setAdapter(customPagerAdapter);
                imageViewPager.setCurrentItem(0);

            }
        });
        mCloseTwoImgLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (mImagesList.size()) {

                    case 1:
                       //pos 1 img removed
                       //No need
                        break;

                    case 2:
                        // pos 2 img removed
//                        mImagesList.set(0, mImagesList.get(1));
//                        Glide.with(getActivity())
//                                .load(mImagesList.get(0))
//                                .asBitmap().into(imageOne);
                        mImagesList.remove(1);


                        //set pos 2 default img
//                        Glide.with(getActivity())
//                                .load(getActivity().getResources().getDrawable(R.drawable.upload_icon_holder))
//                                .asBitmap().into(imageTwo);

                        imageTwo.setImageResource(R.drawable.upload_icon_holder);
                        mCloseTwoImgLay.setVisibility(View.GONE);


                        UploadScreen.mEntity.imagePath1=mImagesList.get(0);
                        UploadScreen.mEntity.imagePath2="";
                        UploadScreen.mEntity.imagePath3="";
                        break;
                    case 3:
                        // pos 2 img removed
//                        mImagesList.set(0, mImagesList.get(1));
//                        Glide.with(getActivity())
//                                .load(mImagesList.get(0))
//                                .asBitmap().into(imageOne);

                        //pos 3
                        mImagesList.set(1, mImagesList.get(2));
                        Glide.with(getActivity())
                                .load(mImagesList.get(1))
                                .asBitmap().into(imageTwo);
                        mImagesList.remove(2);

                        imageThree.setImageResource(R.drawable.upload_icon_holder);
                        mCloseThreeImgLay.setVisibility(View.GONE);


                        UploadScreen.mEntity.imagePath1=mImagesList.get(0);
                        UploadScreen.mEntity.imagePath2=mImagesList.get(1);
                        UploadScreen.mEntity.imagePath3="";
                        break;

                }
                customPagerAdapter = new CustomPagerAdapter(getActivity());
                imageViewPager.setAdapter(customPagerAdapter);
                imageViewPager.setCurrentItem(0);

            }
        });
        mCloseThreeImgLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                switch (mImagesList.size()) {

                    case 1:
                        // no need
                        break;



                    case 2:
                        // no need
                        break;

                    case 3:
                        // pos 2 img removed
//                        mImagesList.set(0, mImagesList.get(1));
//                        Glide.with(getActivity())
//                                .load(mImagesList.get(0))
//                                .asBitmap().into(imageOne);
//
//                        //pos 3
//                        mImagesList.set(1, mImagesList.get(2));
//                        Glide.with(getActivity())
//                                .load(mImagesList.get(1))
//                                .asBitmap().into(imageTwo);
                        mImagesList.remove(2);

                        imageThree.setImageResource(R.drawable.upload_icon_holder);
                        mCloseThreeImgLay.setVisibility(View.GONE);


                        UploadScreen.mEntity.imagePath1=mImagesList.get(0);
                        UploadScreen.mEntity.imagePath2=mImagesList.get(1);
                        UploadScreen.mEntity.imagePath3="";
                        break;

                }

                customPagerAdapter = new CustomPagerAdapter(getActivity());
                imageViewPager.setAdapter(customPagerAdapter);
                imageViewPager.setCurrentItem(0);
            }
        });

        mNextBtn = (Button) view.findViewById(R.id.footer_btn);
        mNextBtn.setText(getString(R.string.next));

        Typeface mLightFont = TypefaceSingleton.getTypeface().getLightFont(getActivity());
        mNextBtn.setTypeface(mLightFont);
        titleTv.setTypeface(mLightFont);

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (mImagesList.size() == 0) {

                    DialogManager.showBasicAlertDialog(getActivity(), getString(R.string.please_upload_pic), UploadPhotosFragment.this);

                } else {
                  /*  switch (mBitmapArray.size()) {
                        case 3:
                            UploadScreen.mEntity.bitmap3 = mBitmapArray.get(2);
                        case 2:
                            UploadScreen.mEntity.bitmap2 = mBitmapArray.get(1);
                        case 1:
                            UploadScreen.mEntity.bitmap1 = mBitmapArray.get(0);
                        default:

                            break;
                    }*/

                    boolean isServiceNeeded = false;
                    if (UploadScreen.mEntity.imagePath1 != null && UploadScreen.mEntity.imagePath1.length
                            () > 0 && !UploadScreen.mEntity.imagePath1.contains("http")) {
                        isServiceNeeded = true;

                    } else if (UploadScreen.mEntity.imagePath2 != null && UploadScreen.mEntity.imagePath2.length

                            () > 0 && !UploadScreen.mEntity.imagePath2.contains("http")) {
                        isServiceNeeded = true;

                    } else if (UploadScreen.mEntity.imagePath3 != null && UploadScreen.mEntity.imagePath3
                            .length
                                    () > 0 && !UploadScreen.mEntity.imagePath3.contains("http")) {
                        isServiceNeeded = true;
                    }

                    if (isServiceNeeded) {
                        APIRequestHandler.getInstance().uploadPhotosResponse(UploadScreen.mEntity,
                                UploadPhotosFragment.this);
                    } else {
                        if (UploadScreen.mEntity.isSelling && UploadScreen.mEntity.isGood) {

                            if (!UploadScreen.mEntity.isDeliveryInPerson) {
                                if (UploadScreen.mEntity.category != null && UploadScreen.mEntity.category.equalsIgnoreCase(getActivity().getString(R.string.ticket))) {
                                    UploadScreen.mEntity.dropBoxUrlPreview = "ABCD";
                                    UploadScreen.mEntity.dropBoxUrlOrginal = "ABCD";
//                                    ((BaseFragmentActivity) getActivity()).addFragment(new UploadPublishFragment(), 11);
                                    ((BaseFragmentActivity) getActivity()).addFragment(new UploadPublishFragment(), 10);
                                } else {

//                                    ((BaseFragmentActivity) getActivity()).addFragment(new UploadDropboxPreviewFragment(), 10);
                                    ((BaseFragmentActivity) getActivity()).addFragment(new UploadDropboxPreviewFragment(), 9);
                                }
                            } else {
//                                ((BaseFragmentActivity) getActivity()).addFragment(new UploadPublishFragment(), 10);
                                ((BaseFragmentActivity) getActivity()).addFragment(new UploadPublishFragment(), 9);
                            }
                        } else if (!UploadScreen.mEntity.isSelling && !UploadScreen.mEntity.isGood) {
//                            ((BaseFragmentActivity) getActivity()).addFragment(new UploadPublishFragment(), 8);
                            ((BaseFragmentActivity) getActivity()).addFragment(new UploadPublishFragment(), 7);
                        } else {
                            if (GlobalMethods.getStringValue(getActivity(), AppConstants.PARTNER).equalsIgnoreCase(getString(R.string.two))) {
//                                ((BaseFragmentActivity) getActivity()).addFragment(new UploadPublishFragment(), 11);
                                ((BaseFragmentActivity) getActivity()).addFragment(new UploadPublishFragment(), 10);

                            } else {
//                                ((BaseFragmentActivity) getActivity()).addFragment(new UploadPublishFragment(), 9);
                                ((BaseFragmentActivity) getActivity()).addFragment(new UploadPublishFragment(), 8);
                            }
                        }


                    }
                }
            }
        });
    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);


        UploadPictureResponse uploadPictureResponse = (UploadPictureResponse) responseObj;


        if (uploadPictureResponse.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
            if (uploadPictureResponse.getResult().getPicture1() != null && uploadPictureResponse
                    .getResult().getPicture1().length() > 0) {
                UploadScreen.mEntity.imagePath1 = uploadPictureResponse.getResult().getPicture1();
            }
            if (uploadPictureResponse.getResult().getPicture2() != null && uploadPictureResponse
                    .getResult().getPicture2().length() > 0) {
                UploadScreen.mEntity.imagePath2 = uploadPictureResponse.getResult().getPicture2();
            }
            if (uploadPictureResponse.getResult().getPicture3() != null && uploadPictureResponse
                    .getResult().getPicture3().length() > 0) {
                UploadScreen.mEntity.imagePath3 = uploadPictureResponse.getResult().getPicture3();
            }

            if (UploadScreen.mEntity.isSelling && UploadScreen.mEntity.isGood) {

                if (!UploadScreen.mEntity.isDeliveryInPerson) {
                    if (UploadScreen.mEntity.category != null && UploadScreen.mEntity.category.equalsIgnoreCase(getActivity().getString(R.string.ticket))) {
                        UploadScreen.mEntity.dropBoxUrlPreview = "ABCD";
                        UploadScreen.mEntity.dropBoxUrlOrginal = "ABCD";
                        ((BaseFragmentActivity) getActivity()).addFragment(new UploadPublishFragment(), 11);
                    } else {

                        ((BaseFragmentActivity) getActivity()).addFragment(new UploadDropboxPreviewFragment(), 10);
                    }
                } else {
                    ((BaseFragmentActivity) getActivity()).addFragment(new UploadPublishFragment(), 10);
                }
            } else if (!UploadScreen.mEntity.isSelling && !UploadScreen.mEntity.isGood) {
                ((BaseFragmentActivity) getActivity()).addFragment(new UploadPublishFragment(), 8);
            } else {
                if (GlobalMethods.getStringValue(getActivity(), AppConstants.PARTNER).equalsIgnoreCase(getString(R.string.two))) {
                    ((BaseFragmentActivity) getActivity()).addFragment(new UploadPublishFragment(), 11);
                } else {

                    ((BaseFragmentActivity) getActivity()).addFragment(new UploadPublishFragment(), 9);
                }
            }
        } else {
            DialogManager.showBasicAlertDialog(getActivity(), uploadPictureResponse.getMessage(), this);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        if (mImagesList.size() > 0) {
            customPagerAdapter = new CustomPagerAdapter(getActivity());
            imageViewPager.setAdapter(customPagerAdapter);
            imageViewPager.setCurrentItem(0);
            switch (mImagesList.size()) {
                case 3:
                    //     imageThree.setImageBitmap(mBitmapArray.get(2));
                    Glide.with(getActivity())
                            .load(mImagesList.get(2))
                            .asBitmap().into(imageThree);
                    mCloseThreeImgLay.setVisibility(View.VISIBLE);

                case 2:
                    //  imageTwo.setImageBitmap(mBitmapArray.get(1));
                    Glide.with(getActivity())
                            .load(mImagesList.get(1))
                            .asBitmap().into(imageTwo);
                    mCloseTwoImgLay.setVisibility(View.VISIBLE);
                case 1:
                    //     imageOne.setImageBitmap(mBitmapArray.get(0));
                    Glide.with(getActivity())
                            .load(mImagesList.get(0))
                            .asBitmap().into(imageOne);
                    mCloseOneImgLay.setVisibility(View.VISIBLE);
                default:
                    break;
            }
        } else {
            if (UploadScreen.mEntity.imagePath1 != null && UploadScreen.mEntity.imagePath1.length
                    () > 0 && UploadScreen.mEntity.imagePath1.contains("http")) {
                Glide.with(getActivity())
                        .load(UploadScreen.mEntity.imagePath1)
                        .asBitmap().into(imageOne);

                mCloseOneImgLay.setVisibility(View.VISIBLE);
                mImagesList.add(UploadScreen.mEntity.imagePath1);
                //  mBitmapArray.add(UploadScreen.mEntity.bitmap1);

            }
            if (UploadScreen.mEntity.imagePath2 != null && UploadScreen.mEntity.imagePath2.length

                    () > 0 && UploadScreen.mEntity.imagePath2.contains("http")) {
                Glide.with(getActivity())
                        .load(UploadScreen.mEntity.imagePath2)
                        .asBitmap().into(imageTwo);

                mCloseTwoImgLay.setVisibility(View.VISIBLE);
                //   mBitmapArray.add(UploadScreen.mEntity.bitmap2);
                mImagesList.add(UploadScreen.mEntity.imagePath2);

            }
            if (UploadScreen.mEntity.imagePath3 != null && UploadScreen.mEntity.imagePath3
                    .length
                            () > 0 && UploadScreen.mEntity.imagePath3.contains("http")) {
                Glide.with(getActivity())
                        .load(UploadScreen.mEntity.imagePath3)
                        .asBitmap().into(imageThree);

                mCloseThreeImgLay.setVisibility(View.VISIBLE);
                mImagesList.add(UploadScreen.mEntity.imagePath3);


                //   mBitmapArray.add(UploadScreen.mEntity.bitmap3);
            }

            CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(getActivity());
            imageViewPager.setAdapter(customPagerAdapter);
            imageViewPager.setCurrentItem(0);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String path = "";
        Bitmap image;
        try {


            if (resultCode == Activity.RESULT_OK) {

                if (requestCode == ProfileImageSelectionUtil.CAMERA) {
                    //  Bitmap thumbnail = (Bitmap) data.getExtras().get("data");


                    //           image = ProfileImageSelectionUtil.getCorrectOrientationImage
                    //                     (getActivity(), thumbnail);
//

                    //   path = storeImage(thumbnail, "profile"
                    //            + "-");
                    //    Uri tempUri = getImageUri(getActivity(), thumbnail);


                    //      Uri tempUri =saveMediaEntry(getActivity().getContentResolver(), thumbnail, "Title", null);
                    //   File finalFile = new File(getRealPathFromURI(tempUri));
                    BitmapFactory.Options options = new BitmapFactory.Options();

                    // downsizing image as it throws OutOfMemory Exception for larger
                    // images
                    options.inSampleSize = 8;

                    final Bitmap thumbnail = BitmapFactory.decodeFile(ProfileImageSelectionUtil.fileUri.getPath(),
                            options);


                    path = getPath(getActivity(), ProfileImageSelectionUtil.fileUri);
                    image = ProfileImageSelectionUtil.getCorrectOrientationImage
                            (getActivity(), thumbnail, path);
                    selectedImageView.setImageBitmap(image);

                    if (mImagesList.size() >= index) {
                        //    mBitmapArray.set(index - 1, image);
                        mImagesList.set(index - 1, path);
                    } else {
                        //         mBitmapArray.add(image);
                        mImagesList.add(path);

                    }

                    CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(getActivity());
                    imageViewPager.setAdapter(customPagerAdapter);
                    imageViewPager.setCurrentItem(index);

                } else if (requestCode == ProfileImageSelectionUtil.GALLERY) {
                    image = ProfileImageSelectionUtil.getImage(data, getActivity());
                    selectedImage = data.getData();

                    image = ProfileImageSelectionUtil.getCorrectOrientationImage
                            (getActivity(), selectedImage, image);
                    path = getPath(getActivity(), selectedImage);
                    selectedImageView.setImageBitmap(image);
                    if (mImagesList.size() >= index) {
                        //   mBitmapArray.set(index - 1, image);
                        mImagesList.set(index - 1, path);
                    } else {
                        //         mBitmapArray.add(image);
                        mImagesList.add(path);

                    }

                    CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(getActivity());
                    imageViewPager.setAdapter(customPagerAdapter);
                    imageViewPager.setCurrentItem(index);
                }

        /*              if (image != null) {
                            AppConstants.profile_img = image;
                            // mProfileImg.setImageBitmap(null);
                            // mProfileImg.setImageBitmap(image);
                            path = storeImage(image, "profile"
                                    + "-");
                        }*/


                //         path = Environment.getExternalStorageDirectory().getPath() + File.separator +
                //          "image" + index + ".jpg";

                switch (index) {
                    case 1:
                        UploadScreen.mEntity.imagePath1 = path;
                        break;
                    case 2:
                        UploadScreen.mEntity.imagePath2 = path;

                        break;
                    case 3:
                        UploadScreen.mEntity.imagePath3 = path;
                        break;
                }


                //     ProfileImageSelectionUtil.saveBitmap(path, image);


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Uri getPhotoFileUri(String fileName) {
        // Only continue if the SD Card is mounted

        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File fi = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                "camerascript" + UploadPhotosFragment.index + ".png");
        // Create the storage directory if it does not exist
//            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
//                Log.d(APP_TAG, "failed to create directory");
//            }

        // Return the file target for the photo based on filename
        return Uri.fromFile(new File(fi.getAbsolutePath()));

    }

    private String storeImage(Bitmap image, String fileName) {
        File pictureFile = getOutputMediaFile(fileName);
        if (pictureFile == null) {
            // Log.d(TAG,
            // "Error creating media file, check storage permissions: ");//
            // e.getMessage());
            return "";
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            // Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            // Log.d(TAG, "Error accessing file: " + e.getMessage());
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
                        + getActivity().getPackageName() + "/Files");

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
        String mImageName = "MeetIntro_" + fileName + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + mImageName);
        return mediaFile;
    }

    @Override
    public void onOkClick() {

    }

    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mImagesList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.image_layout, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            //        imageView.setImageBitmap(mBitmapArray.get(position));

            Glide.with(getActivity())
                    .load(mImagesList.get(position))
                    .asBitmap().into(imageView);


            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        //  ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = saveMediaEntry(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public void getImageUri1(Context inContext, Bitmap inImage) {


        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public String saveMediaEntry(ContentResolver cr, Bitmap source,
                                 String title, String description) {
        ContentValues values = new ContentValues();
        values.put(Images.Media.TITLE, title);
        values.put(Images.Media.DESCRIPTION, description);
        values.put(Images.Media.MIME_TYPE, "image/jpeg");

        Uri url = null;
        String stringUrl = null;    /* value to be returned */

        try {
            url = cr.insert(Images.Media.EXTERNAL_CONTENT_URI, values);

            if (source != null) {
                OutputStream imageOut = cr.openOutputStream(url);
                try {
                    //       int w = source.getWidth();
                    //       int h = source.getHeight();
//
                    //     Matrix matrix = new Matrix();

                    //     matrix.preRotate(0);
                    //     source = Bitmap.createBitmap(source, 0, 0, w, h, matrix, true);
                    //      source = source.copy(Bitmap.Config.ARGB_8888, true);
                    // ProfileImageSelectionUtil.getCorrectOrientationImage(getActivity(), source);
                    source.compress(Bitmap.CompressFormat.JPEG, 100, imageOut);
                } finally {
                    imageOut.close();
                }

                //       long id = ContentUris.parseId(url);
                // Wait until MINI_KIND thumbnail is generated.
                //         Bitmap miniThumb = Images.Thumbnails.getThumbnail(cr, id,
                //          Images.Thumbnails.MINI_KIND, null);
                // This is for backward compatibility.
                //    Bitmap microThumb = StoreThumbnail(cr, miniThumb, id, 50F, 50F,
                //             Images.Thumbnails.MICRO_KIND);
            } else {
                //  Log.e(TAG, "Failed to create thumbnail, removing original");
                cr.delete(url, null, null);
                url = null;
            }
        } catch (Exception e) {
            //  Log.e(TAG, "Failed to insert image", e);
            if (url != null) {
                cr.delete(url, null, null);
                url = null;
            }
        }

        if (url != null) {
            stringUrl = url.toString();
        }

        return stringUrl;


    }

}
