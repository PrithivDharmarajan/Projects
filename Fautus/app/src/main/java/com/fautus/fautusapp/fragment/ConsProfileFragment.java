package com.fautus.fautusapp.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fautus.fautusapp.R;
import com.fautus.fautusapp.main.BaseFragment;
import com.fautus.fautusapp.ui.HomeScreen;
import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.DialogManager;
import com.fautus.fautusapp.utils.InterfaceBtnCallback;
import com.fautus.fautusapp.utils.InterfaceTwoBtnCallback;
import com.fautus.fautusapp.utils.NetworkUtil;
import com.fautus.fautusapp.utils.ParseAPIConstants;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
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

/**
 * This class implements UI and functions for photographers
 * to fill their profile strap details
 *
 * @author Smaat Apps
 * @since 2017-03-31
 */
public class ConsProfileFragment extends BaseFragment implements InterfaceBtnCallback {


    @BindView(R.id.full_name_optional_edt)
    EditText mFullNameOptionalEdt;

    @BindView(R.id.upload_optional_selfie_img)
    ImageView mUploadOptionalSelfieImg;

    @BindView(R.id.upload_optional_selfie_holder_img)
    ImageView mUploadOptionalSelfieHolderImg;

    @BindView(R.id.rounded_upload_optional_selfie_lay)
    RelativeLayout mRoundedUploadOptionalSelfieLay;

    @BindView(R.id.upload_optional_selfie_txt)
    TextView mUploadOptionalSelfieTxt;

    @BindView(R.id.upload_selfie_close_img)
    ImageView mUploadSelfieCloseImg;

    private Uri mPictureFileUri;
    private final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private final int GALLERY_IMAGE_REQUEST_CODE = 200;
    private String mUploadSelfieImgPathStr = AppConstants.FAILURE_CODE;
    private ParseFile mSelfiePhotoParseFile;
    private ParseUser mParseUser;
    private boolean mSelfiePhotoChangedBool = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_consumer_profile_screen, container, false);
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);
        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(rootView);
        /*For focus current fragment*/
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
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
        ((HomeScreen) getActivity()).setDrawerAction(true);
        ((HomeScreen) getActivity()).setHeaderText(getString(R.string.profile));
        ((HomeScreen) getActivity()).setHeadRightImgVisible(false, R.mipmap.app_icon);
        initView();
    }

    private void initView() {
        /*Check internet connection*/
        if (NetworkUtil.isNetworkAvailable(getActivity())) {
            /*Get user details from Parse DB*/
            mSelfiePhotoChangedBool = false;
            gerUserProfileDetails();
        } else {
             /*Alert message will be appeared if there is no internet connection*/
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), getString(R.string.no_internet), this);
        }


    }

    /*View OnClick*/
    @OnClick({R.id.upload_optional_selfie_img, R.id.upload_optional_selfie_holder_img, R.id.upload_selfie_close_img, R.id.update_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload_optional_selfie_holder_img:
            case R.id.upload_optional_selfie_img:
                if (permissionsCameraAndStorage()) {
                    DialogManager.getInstance().showPictureUploadPopup(getActivity(), new InterfaceTwoBtnCallback() {
                        @Override
                        public void onYesClick() {
                            captureImage();
                        }

                        @Override
                        public void onNoClick() {
                            galleryImage();
                        }
                    });
                }
                break;
            case R.id.upload_selfie_close_img:
                setPictureData();
                break;
            case R.id.update_lay:
                validateFields();
                break;
        }

    }

    /*Validate the edit text fields and API call*/
    private void validateFields() {
        /*hiding keypad for user interaction*/
        hideSoftKeyboard();

        String fullNameStr;
        fullNameStr = mFullNameOptionalEdt.getText().toString().trim();


        if (mParseUser != null) {
            mParseUser = ParseUser.getCurrentUser();
        }
        if (!fullNameStr.isEmpty()) {
            mParseUser.put(ParseAPIConstants.fullName, fullNameStr);
        }
        //selfie photo
        if (mSelfiePhotoChangedBool) {
            if (!mUploadSelfieImgPathStr.equals(AppConstants.FAILURE_CODE)) {
                mParseUser.put(ParseAPIConstants.profilePhoto, mSelfiePhotoParseFile);
            } else if (mParseUser.getParseFile(ParseAPIConstants.profilePhoto) != null) {
                mParseUser.remove(ParseAPIConstants.profilePhoto);
            }
        }

            /*Check internet connection*/
        if (NetworkUtil.isNetworkAvailable(getActivity())) {
                /*Photographer API call*/
            updatePhotographerDetails();
        } else {
                /*Alert message will be appreared if there is no internet connection*/
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), getString(R.string.no_internet), this);

        }

    }

    /*Get user details from parse API*/
    private void gerUserProfileDetails() {

        mParseUser = ParseUser.getCurrentUser();
        if (mParseUser.getString(ParseAPIConstants.fullName) != null) {
            mFullNameOptionalEdt.setText(mParseUser.getString(ParseAPIConstants.fullName));
        }
        /*Load photographer profile picture*/
        if (mParseUser.getParseFile(ParseAPIConstants.profilePhoto) != null) {
            mParseUser.getParseFile(ParseAPIConstants.profilePhoto).getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null && data != null) {
                        try {
                            /*If the Profile picture exists in the DB, this term will make it invisible*/
                            mUploadOptionalSelfieTxt.setVisibility(View.INVISIBLE);
                            mUploadSelfieCloseImg.setVisibility(View.VISIBLE);
                            mUploadOptionalSelfieHolderImg.setVisibility(View.GONE);
                            mRoundedUploadOptionalSelfieLay.setVisibility(View.VISIBLE);
                            mUploadSelfieImgPathStr = mParseUser.getParseFile(ParseAPIConstants.profilePhoto).getUrl();
                            Glide.with(getActivity())
                                    .load(data)
                                    .into(mUploadOptionalSelfieImg);
                        } catch (Exception ex) {
                            Log.e(this.getClass().getSimpleName(), ex.getMessage());
                        }
                    } else if (e != null) {
                        DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), e.getMessage(), ConsProfileFragment.this);
                    }
                }
            });
        }

    }

    @Override
    public void onOkClick() {

    }

    /*open camera*/
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mPictureFileUri = (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) ? FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", getOutputMediaFile(MEDIA_TYPE_IMAGE)) : Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPictureFileUri);

        // start the image capture Intent
        getActivity().startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    /*open gallery*/
    private void galleryImage() {
        Intent j = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        getActivity().startActivityForResult(j, GALLERY_IMAGE_REQUEST_CODE);
    }

    private File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), getString(R.string.app_name));

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e(this.getClass().getSimpleName(), getString(R.string.failed_directory));
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + getString(R.string.app_name) + "-" + timeStamp + ".png");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("mPictureFileUri", mPictureFileUri);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mPictureFileUri = savedInstanceState.getParcelable("mPictureFileUri");
        }
    }

    /*onActivityResult for image capture*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                /*Image captured successfully and displayed in the image view*/

                mUploadSelfieImgPathStr = mPictureFileUri.getPath();
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                /*Cancelling the image capture process by the user*/
                DialogManager.getInstance().showToast(getActivity(), getString(R.string.user_cancelled));
            } else {
               /*image capture getting failed due to certail technical issues*/
                DialogManager.getInstance().showToast(getActivity(), getString(R.string.sorry_failed_img));
            }

        } else if (requestCode == GALLERY_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                mPictureFileUri = data.getData();
                Cursor cursor = getActivity().getContentResolver().query(mPictureFileUri, null, null, null, null);

                if (cursor == null) { // Source is Dropbox or other similar local file path
                    mUploadSelfieImgPathStr = mPictureFileUri.getPath();
                } else {
                    cursor.moveToFirst();
                    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    mUploadSelfieImgPathStr = cursor.getString(idx);
                    cursor.close();
                }

                previewCapturedImage();

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getActivity(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getActivity(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        }
    }


    /*Byte conversion from image uri*/
    private byte[] getBytes(Uri uri) {
        try {
            InputStream iStream = getActivity().getContentResolver().openInputStream(uri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len;
            if (iStream != null) {
                while ((len = iStream.read(buffer)) != -1) {
                    byteBuffer.write(buffer, 0, len);
                }
            }
            return byteBuffer.toByteArray();
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        }
        return null;
    }


    /*set captured image*/
    private void previewCapturedImage() {
        try {

            if (!mUploadSelfieImgPathStr.equals(AppConstants.FAILURE_CODE)) {
                Glide.with(this)
                        .load(mPictureFileUri)
                        .into(mUploadOptionalSelfieImg);

                mUploadOptionalSelfieHolderImg.setVisibility(View.GONE);
                mRoundedUploadOptionalSelfieLay.setVisibility(View.VISIBLE);

                byte[] temp = getBytes(mPictureFileUri);
                mSelfiePhotoParseFile = new ParseFile(getString(R.string.user_profile_img), temp);
                // Upload the image into Parse Cloud
                mSelfiePhotoParseFile.saveInBackground();
                mSelfiePhotoChangedBool = true;

            } else {
                setPictureData();
            }

            mUploadOptionalSelfieTxt.setVisibility(View.INVISIBLE);
            mUploadSelfieCloseImg.setVisibility(View.VISIBLE);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /*set picture default data*/
    private void setPictureData() {
        mUploadSelfieImgPathStr = AppConstants.FAILURE_CODE;

        mUploadOptionalSelfieHolderImg.setVisibility(View.VISIBLE);
        mRoundedUploadOptionalSelfieLay.setVisibility(View.GONE);

        mUploadOptionalSelfieImg.setImageResource(R.drawable.selfie_img);
        mUploadOptionalSelfieTxt.setVisibility(View.VISIBLE);
        mUploadSelfieCloseImg.setVisibility(View.INVISIBLE);
        mSelfiePhotoChangedBool = true;
    }

    /*Permission for access location*/
    private boolean permissionsCameraAndStorage() {
        boolean addPermission = true;
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            int permissionCamera = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
            int readStoragePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
            int storagePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);


            if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CAMERA);
            }
            if (readStoragePermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (storagePermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            addPermission = isPermission(listPermissionsNeeded, new InterfaceTwoBtnCallback() {
                @Override
                public void onYesClick() {

                    DialogManager.getInstance().showPictureUploadPopup(getActivity(), new InterfaceTwoBtnCallback() {
                        @Override
                        public void onYesClick() {
                            captureImage();
                        }

                        @Override
                        public void onNoClick() {
                            galleryImage();

                        }
                    });

                }

                public void onNoClick() {

                }
            });
        }

        return addPermission;
    }

    /*Update user details to parse DB*/
    private void updatePhotographerDetails() {
        DialogManager.getInstance().showProgress(getActivity());
        mParseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                DialogManager.getInstance().hideProgress();
                String alertTopicStr = getString(R.string.profile_store_topic);
                String alertMsgStr = getString(R.string.profile_store_msg);

                if (e == null) {
                    alertTopicStr = getString(R.string.profile_update_topic);
                    alertMsgStr = getString(R.string.profile_update_msg);
                }

                DialogManager.getInstance().showAlertPopup(getActivity(), alertTopicStr, alertMsgStr, ConsProfileFragment.this);

            }
        });
    }

}
