package com.smaat.renterblock.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.smaat.renterblock.R;
import com.smaat.renterblock.adapters.ImageVideoUploadAdapter;
import com.smaat.renterblock.entity.UserPropertyPics;
import com.smaat.renterblock.entity.UserPropertyPicsEntity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.ImageVideoUploadResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.ImageUtil;
import com.smaat.renterblock.utils.InterfaceTwoBtnCallback;
import com.smaat.renterblock.utils.InterfaceTwoBtnWithStringCallback;
import com.smaat.renterblock.utils.PathUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class ImageUploadFragment extends BaseFragment {

    @BindView(R.id.photo_recycle_view)
    RecyclerView mPhotoRecyclerView;

    private final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private final int GALLERY_IMAGE_REQUEST_CODE = 200;
    private static final int CHOOSEVIDEO = 70;
    private static final int CAPTUREVIDEO = 30;
    private Uri mPictureFileUri;
    Bitmap bm;
    String selectedVideoGalleryPath = "", selectedVideoCapturePath = "", selectedImageCapturePath = "", selectedImageGalleryPath = "";
    ImageVideoUploadAdapter mImageVideoUploadAdapter;
    private ArrayList<UserPropertyPics> mEntity = new ArrayList<>();
    ArrayList<ImageVideoUploadResponse> mResponse = new ArrayList<>();
    private boolean mImageUploadAPIBool = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_image_upload, container, false);

        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);

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
        /*set header text and header right img*/

        initView();
        ((HomeScreen) getActivity()).mHeaderFullLay.setVisibility(View.VISIBLE);
  /*set header txt*/
        ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.photos_videos), 1);
        ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.photos_videos), 0);


        ((HomeScreen) getActivity()).setDrawerAction(false);

        ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.drawable.add_img, 1,"");
        ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.add_img, 0);

    }

    private void initView() {
  /*To create initial View*/
        uploadImageAndVideo("", "", "");

        ((HomeScreen) getActivity()).mHeaderRightSecondImgLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogManager.getInstance().showImageUploadPopup(getActivity(), getString(R.string.choose), getString(R.string.photo), getString(R.string.video), new InterfaceTwoBtnCallback() {
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
        });

    }

    private void imageAdapter(ArrayList<UserPropertyPicsEntity> mEntity) {

        if (AppConstants.mPropertyPics.size() > 0) {
            mImageVideoUploadAdapter = new ImageVideoUploadAdapter(getActivity(), mEntity);
            GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
            mPhotoRecyclerView.setLayoutManager(manager);
            mPhotoRecyclerView.setNestedScrollingEnabled(false);
            mPhotoRecyclerView.setFocusable(false);
            mPhotoRecyclerView.setAdapter(mImageVideoUploadAdapter);
        }
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

    /*onActivityResult for image capture*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                selectedImageCapturePath = mPictureFileUri.getPath();
                try {
                    bm = BitmapFactory.decodeStream(
                            getActivity().getContentResolver().openInputStream(mPictureFileUri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


                DialogManager.getInstance().showDescriptionAlert(getActivity(), bm, new InterfaceTwoBtnWithStringCallback() {
                    @Override
                    public void onPositiveStringCallback(String str) {

                        uploadImageAndVideo("image", selectedImageCapturePath, str);
                    }

                    @Override
                    public void onNegativeCallback() {

                    }
                });


//                ImageUploadEntity mEntity = new ImageUploadEntity();
//                mEntity.setType("image");
//                mEntity.setBitmap(bm);
//                mEntity.setFilepath(selectedImageCapturePath);
//                AppConstants.IMAGE_UPLOAD_ENTITY = mEntity;
//
//                CallFragment();

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
                // successfully captured the image
                // display it in image view
                mPictureFileUri = data.getData();

                selectedImageGalleryPath = PathUtils.getPath(getActivity(), mPictureFileUri);

                Cursor cursor = getActivity().getContentResolver().query(mPictureFileUri, null, null, null, null);

                try {
                    bm = BitmapFactory.decodeStream(
                            getActivity().getContentResolver().openInputStream(mPictureFileUri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


                DialogManager.getInstance().showDescriptionAlert(getActivity(), bm, new InterfaceTwoBtnWithStringCallback() {
                    @Override
                    public void onPositiveStringCallback(String str) {
                        uploadImageAndVideo("image", selectedImageGalleryPath, str);
                    }

                    @Override
                    public void onNegativeCallback() {

                    }
                });


//                ImageUploadEntity mEntity = new ImageUploadEntity();
//                mEntity.setType("image");
//                mEntity.setBitmap(bm);
//                mEntity.setFilepath(selectedImageGalleryPath);
//                AppConstants.IMAGE_UPLOAD_ENTITY = mEntity;

//                nextScreen(TutorialScreen.class,true);


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
//                uriVideo = data.getData();

                selectedVideoCapturePath = PathUtils.getPath(getActivity(), mPictureFileUri);

                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(selectedVideoCapturePath,
                        MediaStore.Images.Thumbnails.MINI_KIND);


                DialogManager.getInstance().showDescriptionAlert(getActivity(), thumb, new InterfaceTwoBtnWithStringCallback() {
                    @Override
                    public void onPositiveStringCallback(String str) {
                        uploadImageAndVideo("video", selectedVideoCapturePath, str);
                    }

                    @Override
                    public void onNegativeCallback() {

                    }
                });

//                ImageUploadEntity mEntity = new ImageUploadEntity();
//                mEntity.setType("video");
//                mEntity.setBitmap(thumb);
//                mEntity.setFilepath(selectedVideoCapturePath);
//                AppConstants.IMAGE_UPLOAD_ENTITY = mEntity;
//                CallFragment();

            } else {
                // Video capture failed, advise user
                Toast.makeText(getActivity(), "Video capture failed.",
                        Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == CHOOSEVIDEO) {

            if (resultCode == getActivity().RESULT_OK) {
                // MEDIA GALLERY
                selectedVideoGalleryPath = PathUtils.getPath(getActivity(), data.getData());

                if (selectedVideoGalleryPath != null) {

                    Bitmap thumb = ThumbnailUtils.createVideoThumbnail(selectedVideoGalleryPath,
                            MediaStore.Images.Thumbnails.MINI_KIND);


                    DialogManager.getInstance().showDescriptionAlert(getActivity(), thumb, new InterfaceTwoBtnWithStringCallback() {
                        @Override
                        public void onPositiveStringCallback(String str) {
                            uploadImageAndVideo("video", selectedVideoGalleryPath, str);
                        }

                        @Override
                        public void onNegativeCallback() {

                        }
                    });

//                ImageUploadEntity mEntity = new ImageUploadEntity();
//                mEntity.setType("video");
//                mEntity.setBitmap(thumb);
//                mEntity.setFilepath(selectedVideoGalleryPath);
//                AppConstants.IMAGE_UPLOAD_ENTITY = mEntity;
//                CallFragment();
                }

                if (resultCode == RESULT_CANCELED) {
                /*Cancelling the image capture process by the user*/
                    DialogManager.getInstance().showToast(getActivity(), getString(R.string.user_cancelled));

                } else {
               /*image capture getting failed due to certail technical issues*/
                    DialogManager.getInstance().showToast(getActivity(), getString(R.string.sorry_failed_video));
                }

            }else {
               /*image capture getting failed due to certail technical issues*/
                DialogManager.getInstance().showToast(getActivity(), getString(R.string.sorry_failed_video));
            }
        }

    }


    private void CallFragment() {
        ((HomeScreen) getActivity()).addFragment(new ImageDescriptionFragment());
    }


    private void uploadImageAndVideo(String type, String filepath, String description) {
        mImageUploadAPIBool = !type.isEmpty();
        APIRequestHandler.getInstance().ImageVideoUpload(AppConstants.DETAIL_PROPERTY_ID, "0", type, description, filepath, ImageUploadFragment.this);

    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);

        if (responseObj instanceof ImageVideoUploadResponse) {
            final ImageVideoUploadResponse imageUploadEntity = (ImageVideoUploadResponse) responseObj;
            if (imageUploadEntity.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                imageAdapter(imageUploadEntity.getUserpropertypic());
                if (mImageUploadAPIBool) {
                    mImageUploadAPIBool = false;
                    uploadImageAndVideo("", "", "");
                }
//                mEntity.add(imageUploadEntity.getUserpropertypic().get(0).getFile());
//                AppConstants.mPropertyPics.add();
            }
        }

    }


    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

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


    public static Bitmap getCorrectOrientationImage(Context context, Uri selectedImage, Bitmap image) {

        Log.e("selectedImage", "selectedImage" + selectedImage);
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
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

}
