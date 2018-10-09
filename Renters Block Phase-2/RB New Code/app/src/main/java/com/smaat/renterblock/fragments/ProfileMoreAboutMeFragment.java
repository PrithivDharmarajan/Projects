package com.smaat.renterblock.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.UpdateViewEntity;
import com.smaat.renterblock.entity.UpdateViewResponse;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.ImageUtil;
import com.smaat.renterblock.utils.InterfaceTwoBtnCallback;
import com.smaat.renterblock.utils.InterfaceTwoBtnWithStringCallback;
import com.smaat.renterblock.utils.PathUtils;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static com.smaat.renterblock.R.id.user_image4;

public class ProfileMoreAboutMeFragment extends BaseFragment {

    @BindView(R.id.about_me_user_name_txt)
    TextView mAboutMeUserTxt;

    @BindView(R.id.about_me_friends_count_txt)
    TextView mAboutMeFriendCountTxt;

    @BindView(R.id.about_me_photos_count_txt)
    TextView mAboutMePhotoCountTxt;

    @BindView(R.id.about_me_reviews_count_txt)
    TextView mAboutMeReviewCountTxt;

    @BindView(R.id.about_me_user_image)
    ImageView mAboutMeUserImg;

    @BindView(R.id.about_me_license)
    TextView mAboutMeLicenseTxt;

    @BindView(R.id.about_me_user_rating_bar)
    RatingBar mAboutMeUserRatingBar;

    @BindView(R.id.user_image1)
    ImageView mUserImg1;

    @BindView(R.id.user_image2)
    ImageView mUserImg2;

    @BindView(R.id.user_image3)
    ImageView mUserImg3;

    @BindView(R.id.user_image4)
    ImageView mUserImg4;

    @BindView(R.id.user_image5)
    ImageView mUserImg5;

    @BindView(R.id.user_video1_img)
    ImageView mUserVideo1;

    @BindView(R.id.user_video2_img)
    ImageView mUserVideo2;

    @BindView(R.id.user_video3_img)
    ImageView mUserVideo3;

    @BindView(R.id.user_video4_img)
    ImageView mUserVideo4;

    @BindView(R.id.user_video5_img)
    ImageView mUserVideo5;

    @BindView(R.id.about_me_edt_txt)
    EditText mAboutMeEdtTxt;


    private int mViewIDInt;
    private Uri mPictureFileUri;
    private final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private final int GALLERY_IMAGE_REQUEST_CODE = 200;
    private static final int CHOOSEVIDEO = 70;
    private static final int CAPTUREVIDEO = 30;


    private String mImageFileStr1 = "", mImageFileStr2 = "", mImageFileStr3 = "", mImageFileStr4 = "",
            mImageFileStr5 = "";
    private String mVideoFileStr1 = "", mVideoFileStr2 = "", mVideoFileStr3 = "", mVideoFileStr4 = "",
            mVideoFileStr5 = "";
    String mAboutTxtStr;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_profile_more_about, container, false);

        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(rootView);


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
            ((HomeScreen) getActivity()).setDrawerAction(false);

            /*set header first  img*/
            ((HomeScreen) getActivity()).setHeaderLeftFirstImgLayVisible(1);
            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.mipmap.app_icon, 2);

            /*set header second img*/
            ((HomeScreen) getActivity()).setHeaderLeftSecondImgLayVisible(R.mipmap.app_icon, 0);
            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.mipmap.app_icon, 0, "");

            /*set header third txt*/
            ((HomeScreen) getActivity()).setHeaderLeftThirdTxtLayVisible(getString(R.string.app_name), 0);
            ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.send), 0);

            /*set header txt*/
            ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.about_me), 1);
            ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.feedback), 0);

            initView();

        }
    }

    private void initView() {
        mAboutMeUserTxt.setText(AppConstants.UserProfileResponse.getResult().getUser().get(0).getUser_name());
        mAboutMeFriendCountTxt.setText(AppConstants.UserProfileResponse.getResult().getFriendscount());
        mAboutMePhotoCountTxt.setText(AppConstants.UserProfileResponse.getResult().getPhotocount());
        mAboutMeReviewCountTxt.setText(AppConstants.UserProfileResponse.getResult().getReviewscount());
        mAboutMeLicenseTxt.setText(AppConstants.UserProfileResponse.getResult().getUser().get(0).getLicence());

        if (AppConstants.UserProfileResponse.getResult().getRating() != null) {
            mAboutMeUserRatingBar.setRating(Float.parseFloat(AppConstants.UserProfileResponse.getResult().getRating()));
        }

        try {
            String ImagePicStr = AppConstants.UserProfileResponse.getResult().getUser().get(0).getUser_pic();
            Glide.with(getActivity())
                    .load(ImagePicStr).error(R.drawable.profile_pic)
                    .into(mAboutMeUserImg);
        } catch (Exception e) {
            e.printStackTrace();
            mAboutMeUserImg.setImageResource(R.drawable.profile_pic);
        }

        mAboutTxtStr  = mAboutMeEdtTxt.getText().toString();
        callUpdateViewAPI();

    }

    @OnClick({R.id.user_image1,R.id.user_image2,R.id.user_image3,R.id.user_image4,R.id.user_image5,R.id.user_video1_img,
    R.id.user_video2_img,R.id.user_video3_img,R.id.user_video4_img,R.id.user_video5_img,R.id.profile_update_btn})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.user_image1:
                mViewIDInt = v.getId();
                OptionDialog("photo");

                break;
            case R.id.user_image2:
                mViewIDInt = v.getId();
                OptionDialog("photo");
                break;
            case R.id.user_image3:
                mViewIDInt = v.getId();
                OptionDialog("photo");
                break;
            case R.id.user_image4:
                mViewIDInt = v.getId();
                OptionDialog("photo");
                break;
            case R.id.user_image5:
                mViewIDInt = v.getId();
                OptionDialog("photo");
                break;

            case R.id.user_video1_img:
                mViewIDInt = v.getId();
                OptionDialog("video");

                break;
            case R.id.user_video2_img:
                mViewIDInt = v.getId();
                OptionDialog("video");
                break;
            case R.id.user_video3_img:
                mViewIDInt = v.getId();
                OptionDialog("video");
                break;
            case R.id.user_video4_img:
                mViewIDInt = v.getId();
                OptionDialog("video");
                break;
            case R.id.user_video5_img:
                mViewIDInt = v.getId();
                OptionDialog("video");
                break;

            case R.id.profile_update_btn:
                callUpdateMeAPI();

                break;
        }

    }

    private void callUpdateViewAPI(){
        APIRequestHandler.getInstance().updateViewAboutMe(ProfileMoreAboutMeFragment.this);
    }

    private void callUpdateMeAPI(){
        mAboutTxtStr  = mAboutMeEdtTxt.getText().toString();
        APIRequestHandler.getInstance().updateAboutMe(mAboutTxtStr,"","","","",ProfileMoreAboutMeFragment.this);
        ((HomeScreen)getActivity()).onBackPressed();
    }

    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);

        if (resObj instanceof UpdateViewEntity){
            UpdateViewEntity mRes = (UpdateViewEntity) resObj;
            if (mRes.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)){
                setData(mRes,mRes.getResult());
            }
         }
    }

    private void setData(UpdateViewEntity mRes, ArrayList<UpdateViewResponse> result) {
        mAboutMeEdtTxt.setText(mRes.getAbout());
        for (int i=0; i < result.size();i++){

            if (mRes.getResult().get(i).getFile_order().equalsIgnoreCase("1")&&mRes.getResult().get(i).getFile_type().equalsIgnoreCase("image")){
                try {
                    mImageFileStr1 = mRes.getResult().get(i).getFile();
                    Glide.with(getActivity())
                            .load(mImageFileStr1).error(R.drawable.listing_add_photo_normal)
                            .into(mUserImg1);
                } catch (Exception e) {
                    e.printStackTrace();
                    mUserImg1.setImageResource(R.drawable.listing_add_photo_normal);
                }
            }
            else if (mRes.getResult().get(i).getFile_order().equalsIgnoreCase("2")&&mRes.getResult().get(i).getFile_type().equalsIgnoreCase("image")){
                try {
                    mImageFileStr2 = mRes.getResult().get(i).getFile();
                    Glide.with(getActivity())
                            .load(mImageFileStr2).error(R.drawable.listing_add_photo_normal)
                            .into(mUserImg2);
                } catch (Exception e) {
                    e.printStackTrace();
                    mUserImg2.setImageResource(R.drawable.listing_add_photo_normal);
                }
            }
            else if (mRes.getResult().get(i).getFile_order().equalsIgnoreCase("3")&&mRes.getResult().get(i).getFile_type().equalsIgnoreCase("image")){
                try {
                    mImageFileStr3 = mRes.getResult().get(i).getFile();
                    Glide.with(getActivity())
                            .load(mImageFileStr3).error(R.drawable.listing_add_photo_normal)
                            .into(mUserImg3);
                } catch (Exception e) {
                    e.printStackTrace();
                    mUserImg3.setImageResource(R.drawable.listing_add_photo_normal);
                }
            }
            else if (mRes.getResult().get(i).getFile_order().equalsIgnoreCase("4")&&mRes.getResult().get(i).getFile_type().equalsIgnoreCase("image")){
                try {
                    mImageFileStr4 = mRes.getResult().get(i).getFile();
                    Glide.with(getActivity())
                            .load(mImageFileStr4).error(R.drawable.listing_add_photo_normal)
                            .into(mUserImg4);
                } catch (Exception e) {
                    e.printStackTrace();
                    mUserImg4.setImageResource(R.drawable.listing_add_photo_normal);
                }
            }
            else if (mRes.getResult().get(i).getFile_order().equalsIgnoreCase("5")&&mRes.getResult().get(i).getFile_type().equalsIgnoreCase("image")){
                try {
                    mImageFileStr5 = mRes.getResult().get(i).getFile();
                    Glide.with(getActivity())
                            .load(mImageFileStr5).error(R.drawable.listing_add_photo_normal)
                            .into(mUserImg5);
                } catch (Exception e) {
                    e.printStackTrace();
                    mUserImg5.setImageResource(R.drawable.listing_add_photo_normal);
                }
            }
            else if (mRes.getResult().get(i).getFile_order().equalsIgnoreCase("1")&&mRes.getResult().get(i).getFile_type().equalsIgnoreCase("video")){
                try {
                    mImageFileStr1 = mRes.getResult().get(i).getFile();
                    Glide.with(getActivity())
                            .load(mImageFileStr1).error(R.drawable.profile_about_me_add_video)
                            .into(mUserVideo1);
                } catch (Exception e) {
                    e.printStackTrace();
                    mUserVideo1.setImageResource(R.drawable.profile_about_me_add_video);
                }
            }
            else if (mRes.getResult().get(i).getFile_order().equalsIgnoreCase("2")&&mRes.getResult().get(i).getFile_type().equalsIgnoreCase("video")){
                try {
                    mImageFileStr2 = mRes.getResult().get(i).getFile();
                    Glide.with(getActivity())
                            .load(mImageFileStr2).error(R.drawable.profile_about_me_add_video)
                            .into(mUserVideo2);
                } catch (Exception e) {
                    e.printStackTrace();
                    mUserVideo2.setImageResource(R.drawable.profile_about_me_add_video);
                }
            }
            else if (mRes.getResult().get(i).getFile_order().equalsIgnoreCase("3")&&mRes.getResult().get(i).getFile_type().equalsIgnoreCase("video")){
                try {
                    mImageFileStr3 = mRes.getResult().get(i).getFile();
                    Glide.with(getActivity())
                            .load(mImageFileStr3).error(R.drawable.profile_about_me_add_video)
                            .into(mUserVideo3);
                } catch (Exception e) {
                    e.printStackTrace();
                    mUserVideo3.setImageResource(R.drawable.profile_about_me_add_video);
                }
            }
            else if (mRes.getResult().get(i).getFile_order().equalsIgnoreCase("4")&&mRes.getResult().get(i).getFile_type().equalsIgnoreCase("video")){
                try {
                    mImageFileStr4 = mRes.getResult().get(i).getFile();
                    Glide.with(getActivity())
                            .load(mImageFileStr4).error(R.drawable.profile_about_me_add_video)
                            .into(mUserVideo4);
                } catch (Exception e) {
                    e.printStackTrace();
                    mUserVideo4.setImageResource(R.drawable.profile_about_me_add_video);
                }
            }
            else if (mRes.getResult().get(i).getFile_order().equalsIgnoreCase("5")&&mRes.getResult().get(i).getFile_type().equalsIgnoreCase("video")){
                try {
                    mImageFileStr5 = mRes.getResult().get(i).getFile();
                    Glide.with(getActivity())
                            .load(mImageFileStr5).error(R.drawable.profile_about_me_add_video)
                            .into(mUserVideo5);
                } catch (Exception e) {
                    e.printStackTrace();
                    mUserVideo5.setImageResource(R.drawable.profile_about_me_add_video);
                }
            }
        }

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

//                            uploadImageAndVideo("image", selectedImageCapturePath, str);
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

//                            uploadImageAndVideo("image", selectedImageGalleryPath, str);
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

//                            uploadImageAndVideo("video", selectedVideoCapturePath, str);
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

//                            uploadImageAndVideo("video", selectedVideoGalleryPath, str);
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

    private void setImage(String uri) {
        switch (mViewIDInt) {
            case R.id.user_image1:
                try {
                    mImageFileStr1 = uri;
                    Glide.with(getActivity())
                            .load(uri).error(R.drawable.default_profile_icon)
                            .into(mUserImg1);
                    APIRequestHandler.getInstance().updateAboutMe(mAboutMeEdtTxt.getText().toString(),"image","1","",mImageFileStr1,ProfileMoreAboutMeFragment.this);
                } catch (Exception e) {
                    e.printStackTrace();
                    mUserImg1.setImageResource(R.drawable.default_profile_icon);
                }
//                mCloseBtn1.setVisibility(View.VISIBLE);

                break;
            case R.id.user_image2:
                try {
                    mImageFileStr2 = uri;
                    Glide.with(getActivity())
                            .load(uri).error(R.drawable.default_profile_icon)
                            .into(mUserImg2);
                    APIRequestHandler.getInstance().updateAboutMe(mAboutMeEdtTxt.getText().toString(),"image","2","",mImageFileStr2,ProfileMoreAboutMeFragment.this);

                } catch (Exception e) {
                    e.printStackTrace();
                    mUserImg2.setImageResource(R.drawable.default_profile_icon);
                }
//                mCloseBtn2.setVisibility(View.VISIBLE);
                break;
            case R.id.user_image3:
                try {
                    mImageFileStr3 = uri;
                    Glide.with(getActivity())
                            .load(uri).error(R.drawable.default_profile_icon)
                            .into(mUserImg3);
                    APIRequestHandler.getInstance().updateAboutMe(mAboutMeEdtTxt.getText().toString(),"image","3","",mImageFileStr3,ProfileMoreAboutMeFragment.this);

                } catch (Exception e) {
                    e.printStackTrace();
                    mUserImg3.setImageResource(R.drawable.default_profile_icon);
                }
//                mCloseBtn3.setVisibility(View.VISIBLE);
                break;
            case user_image4:
                try {
                    mImageFileStr4 = uri;
                    Glide.with(getActivity())
                            .load(uri).error(R.drawable.default_profile_icon)
                            .into(mUserImg4);
                    APIRequestHandler.getInstance().updateAboutMe(mAboutMeEdtTxt.getText().toString(),"image","4","",mImageFileStr4,ProfileMoreAboutMeFragment.this);

                } catch (Exception e) {
                    e.printStackTrace();
                    mUserImg4.setImageResource(R.drawable.default_profile_icon);
                }
//                mCloseBtn4.setVisibility(View.VISIBLE);
                break;
            case R.id.user_image5:
                try {
                    mImageFileStr5 = uri;
                    Glide.with(getActivity())
                            .load(uri).error(R.drawable.default_profile_icon)
                            .into(mUserImg5);
                    APIRequestHandler.getInstance().updateAboutMe(mAboutMeEdtTxt.getText().toString(),"image","5","",mImageFileStr5,ProfileMoreAboutMeFragment.this);

                } catch (Exception e) {
                    e.printStackTrace();
                    mUserImg5.setImageResource(R.drawable.default_profile_icon);
                }
//                mCloseBtn5.setVisibility(View.VISIBLE);
                break;

        }
    }
    private void setImage(Bitmap bitmap, String uri) {
        switch (mViewIDInt) {
            case R.id.user_video1_img:
                mVideoFileStr1 = uri;
                mUserVideo1.setImageBitmap(bitmap);
//                mCloseBtn1.setVisibility(View.VISIBLE);
//                mPlayBtn1.setVisibility(View.VISIBLE);
                APIRequestHandler.getInstance().updateAboutMe(mAboutMeEdtTxt.getText().toString(),"video","1","",mVideoFileStr1,ProfileMoreAboutMeFragment.this);

                break;
            case R.id.user_video2_img:
                mVideoFileStr2 = uri;
                mUserVideo2.setImageBitmap(bitmap);
//                mCloseBtn2.setVisibility(View.VISIBLE);
//                mPlayBtn2.setVisibility(View.VISIBLE);
                APIRequestHandler.getInstance().updateAboutMe(mAboutMeEdtTxt.getText().toString(),"video","2","",mVideoFileStr2,ProfileMoreAboutMeFragment.this);

                break;
            case R.id.user_video3_img:
                mVideoFileStr3 = uri;
                mUserVideo3.setImageBitmap(bitmap);
//                mCloseBtn3.setVisibility(View.VISIBLE);
//                mPlayBtn3.setVisibility(View.VISIBLE);
                APIRequestHandler.getInstance().updateAboutMe(mAboutMeEdtTxt.getText().toString(),"video","3","",mVideoFileStr3,ProfileMoreAboutMeFragment.this);

                break;
            case R.id.user_video4_img:
                mVideoFileStr4 = uri;
                mUserVideo4.setImageBitmap(bitmap);
//                mCloseBtn4.setVisibility(View.VISIBLE);
//                mPlayBtn4.setVisibility(View.VISIBLE);
                APIRequestHandler.getInstance().updateAboutMe(mAboutMeEdtTxt.getText().toString(),"video","4","",mVideoFileStr4,ProfileMoreAboutMeFragment.this);

                break;
            case R.id.user_video5_img:
                mVideoFileStr5 = uri;
                mUserVideo5.setImageBitmap(bitmap);
//                mCloseBtn5.setVisibility(View.VISIBLE);
//                mPlayBtn5.setVisibility(View.VISIBLE);
                APIRequestHandler.getInstance().updateAboutMe(mAboutMeEdtTxt.getText().toString(),"video","5","",mVideoFileStr5,ProfileMoreAboutMeFragment.this);

                break;

        }
    }

}
