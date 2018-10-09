package com.fautus.fautusapp.fragment;

import android.Manifest;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fautus.fautusapp.R;
import com.fautus.fautusapp.entity.ParsePhotographerEntity;
import com.fautus.fautusapp.main.BaseFragment;
import com.fautus.fautusapp.services.APIRequestHandler;
import com.fautus.fautusapp.ui.HomeScreen;
import com.fautus.fautusapp.ui.SelectEquipmentScreen;
import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.DateUtil;
import com.fautus.fautusapp.utils.DialogManager;
import com.fautus.fautusapp.utils.InterfaceBtnCallback;
import com.fautus.fautusapp.utils.InterfaceTwoBtnCallback;
import com.fautus.fautusapp.utils.NetworkUtil;
import com.fautus.fautusapp.utils.ParseAPIConstants;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static com.fautus.fautusapp.utils.ParseAPIConstants.user;

/**
 * Created by sys on 06-May-17.
 */

public class PhotographerProfileFragment extends BaseFragment implements InterfaceBtnCallback {

    /*Variable initialization using bind view*/

    @BindView(R.id.header_lay)
    RelativeLayout mHeaderLay;

    @BindView(R.id.parent_lay)
    ViewGroup mProfileViewGroup;

    @BindView(R.id.header_left_btn_lay)
    RelativeLayout mHeaderLeftBtnLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.full_name_edt)
    EditText mFullNameEdt;

    @BindView(R.id.city_edt)
    EditText mCityEdt;

    @BindView(R.id.state_edt)
    EditText mStateEdt;

    @BindView(R.id.language_spoken_edt)
    EditText mLanguageSpokenEdt;

    @BindView(R.id.dob_txt)
    TextView mDobTxt;

    @BindView(R.id.ssn_edt)
    EditText mSsnEdt;

    @BindView(R.id.upload_selfie_img)
    ImageView mUploadSelfieImg;

    @BindView(R.id.upload_selfie_holder_img)
    ImageView mUploadSelfieHolderImg;

    @BindView(R.id.rounded_selfie_img_lay)
    RelativeLayout mRoundedSelfieImgLay;

    @BindView(R.id.upload_selfie_txt)
    TextView mUploadSelfieTxt;

    @BindView(R.id.upload_selfie_close_img)
    ImageView mUploadSelfieCloseImg;

    @BindView(R.id.upload_photo_id_img)
    ImageView mUploadPhotoIdImg;

    @BindView(R.id.upload_photo_id_txt)
    TextView mUploadPhotoIdTxt;

    @BindView(R.id.upload_photo_close_img)
    ImageView mUploadPhotoCloseImg;

    private final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private final int GALLERY_IMAGE_REQUEST_CODE = 200;
    private Uri mPictureFileUri;
    private String mUploadSelfieImgPathStr = AppConstants.FAILURE_CODE, mUploadPhotoImgPathStr = AppConstants.FAILURE_CODE;
    private boolean mIsUploadSelfieImgBool = false;
    private int mDate, mMonth, mYear;

    private SimpleDateFormat mServerDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.US);
    private SimpleDateFormat mLocalDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
    private ParseObject mParseProfileObject;
    private ParseFile mSelfiePhotoParseFile, mIdPhotoParseFile;
    private boolean mSelfiePhotoChangedBool = false, mIdPhotoChangedBool = false;
    private boolean isPhotographerAvaBool = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.ui_create_profile_screen, container, false);
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
        /*Set header data*/
        mHeaderLay.setVisibility(View.GONE);
        /*Check internet connection*/
        if (NetworkUtil.isNetworkAvailable(getActivity())) {
            /*Get photographer details from Parse DB*/
            mSelfiePhotoChangedBool = false;
            mIdPhotoChangedBool = false;
            getPhotographerDetails();
        } else {
             /*Alert message will be appeared if there is no internet connection*/
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), getString(R.string.no_internet), this);
        }


    }


    /*Set photographer details*/
    private void setData(ParsePhotographerEntity details) {
        mFullNameEdt.setText(details.getFullName());
        mCityEdt.setText(details.getCity());
        mStateEdt.setText(details.getState());
        mLanguageSpokenEdt.setText(details.getLangugages());
        mDobTxt.setText(details.getDateOfBirth().isEmpty() ? getResources().getString(R.string.dob) : details.getDateOfBirth());
        mSsnEdt.setText(details.getLast4SSN());

    }

    /*View OnClick*/
    @OnClick({R.id.dob_txt, R.id.upload_selfie_img, R.id.upload_selfie_holder_img, R.id.upload_selfie_close_img, R.id.upload_photo_id_img, R.id.upload_photo_close_img, R.id.continue_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dob_txt:
                /*Show date picker*/
                showDatePickerDialog();
                break;
            case R.id.upload_selfie_holder_img:
            case R.id.upload_selfie_img:
                mIsUploadSelfieImgBool = true;
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
                mIsUploadSelfieImgBool = true;
                setPictureData();
                break;
            case R.id.upload_photo_id_img:
                mIsUploadSelfieImgBool = false;
                if (permissionsCameraAndStorage()) {
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

                }
                break;
            case R.id.upload_photo_close_img:
                mIsUploadSelfieImgBool = false;
                setPictureData();
                break;
            case R.id.continue_lay:
                validateFields();
                break;
        }

    }


    /*Validate the edit text fields and API call*/
    private void validateFields() {
        /*hiding keypad for user interaction*/
        hideSoftKeyboard();

        String fullNameStr, cityStr, stateStr, languageSpokenStr, dobStr, ssnStr;
        fullNameStr = mFullNameEdt.getText().toString().trim();
        cityStr = mCityEdt.getText().toString().trim();
        stateStr = mStateEdt.getText().toString().trim();
        languageSpokenStr = mLanguageSpokenEdt.getText().toString().trim();
        dobStr = mDobTxt.getText().toString().trim();
        ssnStr = mSsnEdt.getText().toString().trim();

        if (fullNameStr.isEmpty()) {
            mFullNameEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.req_name_topic), getString(R.string.req_name_msg), this);
        } else if (cityStr.isEmpty()) {
            mCityEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.req_city_topic), getString(R.string.req_city_msg), this);
        } else if (stateStr.isEmpty()) {
            mStateEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.req_state_topic), getString(R.string.req_state_msg), this);
        } else if (languageSpokenStr.isEmpty()) {
            mLanguageSpokenEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.req_language_topic), getString(R.string.req_language_msg), this);
        } else if (dobStr.isEmpty() || dobStr.equals(getString(R.string.dob))) {
            mDobTxt.requestFocus();
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.req_dob_topic), getString(R.string.req_dob_msg), this);
        } else if (ssnStr.isEmpty()) {
            mSsnEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.req_ssn_topic), getString(R.string.req_ssn_msg), this);
        } else if (ssnStr.length() != 4) {
            mSsnEdt.requestFocus();
            mSsnEdt.setSelection(ssnStr.length());
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.req_ssn_topic), getString(R.string.req_ssn_4_digits_msg), this);
        } else if (!mSelfiePhotoChangedBool && mParseProfileObject.getParseFile(ParseAPIConstants.profilePhoto) == null || mSelfiePhotoChangedBool && mUploadSelfieImgPathStr.equals(AppConstants.FAILURE_CODE)) {
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.req_selfie_topic), getString(R.string.req_selfie_msg), this);
        } else if (!mIdPhotoChangedBool && mParseProfileObject.getParseFile(ParseAPIConstants.photoIDPhoto) == null || mIdPhotoChangedBool && mUploadPhotoImgPathStr.equals(AppConstants.FAILURE_CODE)) {
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.req_photo_id_topic), getString(R.string.req_photo_id_msg), this);
        } else {
            /*Parse API call*/
            if (mParseProfileObject == null) {
                mParseProfileObject = new ParseObject(ParseAPIConstants.Parse_Photographer);
            }
            mParseProfileObject.put(user, ParseUser.getCurrentUser());
            mParseProfileObject.put(ParseAPIConstants.fullName, fullNameStr);
            mParseProfileObject.put(ParseAPIConstants.city, cityStr);
            mParseProfileObject.put(ParseAPIConstants.state, stateStr);
            mParseProfileObject.put(ParseAPIConstants.languages, languageSpokenStr);
            mParseProfileObject.put(ParseAPIConstants.dateOfBirth, DateUtil.getDateFromString(DateUtil.getCustomDateFormat(dobStr, mLocalDateFormat, mServerDateFormat), mServerDateFormat));
            mParseProfileObject.put(ParseAPIConstants.last4SSN, ssnStr);
            mParseProfileObject.put(ParseAPIConstants.isAvailable, isPhotographerAvaBool);
            mParseProfileObject.put(ParseAPIConstants.emailVerified, true);

            //selfie photo
            if (mSelfiePhotoChangedBool) {
                if (!mUploadSelfieImgPathStr.equals(AppConstants.FAILURE_CODE)) {
                    mParseProfileObject.put(ParseAPIConstants.profilePhoto, mSelfiePhotoParseFile);
                } else if (mParseProfileObject.getParseFile(ParseAPIConstants.profilePhoto) != null) {
                    mParseProfileObject.remove(ParseAPIConstants.profilePhoto);
                }
            }

            //Id proof photo

            if (mIdPhotoChangedBool) {
                if (!mUploadPhotoImgPathStr.equals(AppConstants.FAILURE_CODE)) {
                    mParseProfileObject.put(ParseAPIConstants.photoIDPhoto, mIdPhotoParseFile);
                } else if (mParseProfileObject.getParseFile(ParseAPIConstants.photoIDPhoto) != null) {
                    mParseProfileObject.remove(ParseAPIConstants.photoIDPhoto);
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
    }


    /*Photographer parse API callback will be resulted*/
    @Override
    public void onParseRequestSuccess() {
        super.onParseRequestSuccess();
        AppConstants.PROFILE_FROM_MENU = AppConstants.SUCCESS_CODE;
        nextScreen(SelectEquipmentScreen.class, false);
    }

    /*Show data picker*/
    private void showDatePickerDialog() {
        String dateOfBirthStr = mDobTxt.getText().toString().trim();

        final Calendar cal = Calendar.getInstance();
        mDate = cal.get(Calendar.DAY_OF_MONTH);
        mMonth = cal.get(Calendar.MONTH);
        mYear = cal.get(Calendar.YEAR);
        cal.add(Calendar.DAY_OF_MONTH, -1);

        DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), mDateSetListener,
                mYear, mMonth, mDate);

        if (!dateOfBirthStr.isEmpty() && !dateOfBirthStr.equalsIgnoreCase(getString(R.string.dob))) {
            String[] dateOfBirthStrArr = dateOfBirthStr.split(getString(R.string.slash_sym));
            mDatePicker = new DatePickerDialog(getActivity(), mDateSetListener,
                    Integer.valueOf(dateOfBirthStrArr[2]), (Integer.valueOf
                    (dateOfBirthStrArr[0]) - 1), Integer.valueOf(dateOfBirthStrArr[1]));
        }

        mDatePicker.getDatePicker().setMaxDate(cal.getTimeInMillis());
        mDatePicker.show();


    }

    /*Date picker listener */
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog
            .OnDateSetListener() {

        /* date picker dialog box*/
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            mYear = year;
            mMonth = monthOfYear;
            mDate = dayOfMonth;

            final Calendar cal = Calendar.getInstance();

            if (mYear >= cal.get(Calendar.YEAR) && mMonth >= cal.get(Calendar.MONTH) && mDate >=
                    cal.get(Calendar.DAY_OF_MONTH)) {
                DialogManager.getInstance().showToast(getActivity(), getString(R.string.select_past_date));
            } else {

                String dateStr = String.valueOf(mMonth + 1) + getString(R.string.slash_sym) + mDate + getString(R.string.slash_sym) + mYear;
                Date selectedDate;
                String mDateStr;

                try {
                    selectedDate = mLocalDateFormat.parse(dateStr);
                    mDateStr = mLocalDateFormat.format(selectedDate);
                    mDobTxt.setText(mDateStr);
                } catch (Exception e) {
                    Log.e(AppConstants.TAG, e.getMessage());
                }
            }


        }

    };

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
                if (mIsUploadSelfieImgBool) {
                    mUploadSelfieImgPathStr = mPictureFileUri.getPath();
                } else {
                    mUploadPhotoImgPathStr = mPictureFileUri.getPath();
                }
                previewCapturedImage();
            } else {
                if (mIsUploadSelfieImgBool) {
                    mUploadSelfieImgPathStr = AppConstants.FAILURE_CODE;
                } else {
                    mUploadPhotoImgPathStr = AppConstants.FAILURE_CODE;
                }

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
                Cursor cursor = getActivity().getContentResolver().query(mPictureFileUri, null, null, null, null);

                if (cursor == null) { // Source is Dropbox or other similar local file path
                    if (mIsUploadSelfieImgBool) {
                        mUploadSelfieImgPathStr = mPictureFileUri.getPath();
                    } else {
                        mUploadPhotoImgPathStr = mPictureFileUri.getPath();
                    }
                } else {
                    cursor.moveToFirst();
                    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (mIsUploadSelfieImgBool) {
                        mUploadSelfieImgPathStr = cursor.getString(idx);
                    } else {
                        mUploadPhotoImgPathStr = cursor.getString(idx);
                    }

                    cursor.close();
                }

                previewCapturedImage();

            } else {
                if (mIsUploadSelfieImgBool) {
                    mUploadSelfieImgPathStr = AppConstants.FAILURE_CODE;
                } else {
                    mUploadPhotoImgPathStr = AppConstants.FAILURE_CODE;
                }

                if (resultCode == RESULT_CANCELED) {
                /*Cancelling the image capture process by the user*/
                    DialogManager.getInstance().showToast(getActivity(), getString(R.string.user_cancelled));

                } else {
               /*image capture getting failed due to certail technical issues*/
                    DialogManager.getInstance().showToast(getActivity(), getString(R.string.sorry_failed_img));
                }
            }

        }
    }


    /*Byte conversion from image uri*/
    public byte[] getBytes(Uri uri) {
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

            if ((mIsUploadSelfieImgBool && !mUploadSelfieImgPathStr.equals(AppConstants.FAILURE_CODE)) || !(mIsUploadSelfieImgBool && mUploadPhotoImgPathStr.equals(AppConstants.FAILURE_CODE))) {

                Glide.with(this)
                        .load(mPictureFileUri)
                        .into(mIsUploadSelfieImgBool ? mUploadSelfieImg : mUploadPhotoIdImg);


                if (mIsUploadSelfieImgBool) {
                    mUploadSelfieHolderImg.setVisibility(View.GONE);
                    mRoundedSelfieImgLay.setVisibility(View.VISIBLE);
                    mUploadSelfieImgPathStr = mPictureFileUri.getPath();
                    byte[] temp = getBytes(mPictureFileUri);
                    mSelfiePhotoParseFile = new ParseFile(getString(R.string.user_profile_img), temp);
                    // Upload the image into Parse Cloud
                    mSelfiePhotoParseFile.saveInBackground();
                    mSelfiePhotoChangedBool = true;

                } else {
                    mUploadPhotoImgPathStr = mPictureFileUri.getPath();
                    byte[] imageInByte1 = getBytes(mPictureFileUri);
                    mIdPhotoParseFile = new ParseFile(getString(R.string.user_id_img), imageInByte1);
                    // Upload the image into Parse Cloud
                    mIdPhotoParseFile.saveInBackground();
                    mIdPhotoChangedBool = true;
                }

            } else {
                setPictureData();
            }

            if (mIsUploadSelfieImgBool) {
                mUploadSelfieTxt.setVisibility(View.INVISIBLE);
                mUploadSelfieCloseImg.setVisibility(View.VISIBLE);
            } else {
                mUploadPhotoIdTxt.setVisibility(View.INVISIBLE);
                mUploadPhotoCloseImg.setVisibility(View.VISIBLE);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /*set picture default data*/
    private void setPictureData() {
        if (mIsUploadSelfieImgBool) {
            mUploadSelfieImgPathStr = AppConstants.FAILURE_CODE;
            mRoundedSelfieImgLay.setVisibility(View.GONE);
            mUploadSelfieHolderImg.setVisibility(View.VISIBLE);
            mUploadSelfieImg.setImageResource(R.drawable.selfie_img);
            mUploadSelfieTxt.setVisibility(View.VISIBLE);
            mUploadSelfieCloseImg.setVisibility(View.INVISIBLE);
            mSelfiePhotoChangedBool = true;
        } else {
            mUploadPhotoImgPathStr = AppConstants.FAILURE_CODE;
            mUploadPhotoIdImg.setImageResource(R.drawable.photo_id_img);
            mUploadPhotoIdTxt.setVisibility(View.VISIBLE);
            mUploadPhotoCloseImg.setVisibility(View.INVISIBLE);
            mIdPhotoChangedBool = true;
        }
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


    /*Get photographer details form parse DB*/
    private void getPhotographerDetails() {

        /*Appearing progress bar*/
        DialogManager.getInstance().showProgress(getActivity());

        /*Get current user */
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> photographerQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Photographer);
        photographerQuery.whereEqualTo(ParseAPIConstants.user, currentUser);

        /*get first value from parse DB*/
        photographerQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject photographerObjectRes, ParseException e) {

                if (photographerObjectRes == null) {
                    mParseProfileObject = new ParseObject(ParseAPIConstants.Parse_Photographer);
                } else {
                    mParseProfileObject = photographerObjectRes;
                }

                if (photographerObjectRes != null) {
                    /*Get response from parse DB and set photographer details to local entity*/
                    ParsePhotographerEntity parsePhotographerEntity = new ParsePhotographerEntity();
                    parsePhotographerEntity.setFullName(mParseProfileObject.getString(ParseAPIConstants.fullName));
                    parsePhotographerEntity.setCity(mParseProfileObject.getString(ParseAPIConstants.city));
                    parsePhotographerEntity.setState(mParseProfileObject.getString(ParseAPIConstants.state));
                    parsePhotographerEntity.setLangugages(mParseProfileObject.getString(ParseAPIConstants.languages));
                    parsePhotographerEntity.setDateOfBirth(mParseProfileObject.getDate(ParseAPIConstants.dateOfBirth) != null ? DateUtil.getCustomDateFormat(DateUtil.getStringFromDate(mParseProfileObject.getDate(ParseAPIConstants.dateOfBirth), mServerDateFormat), mServerDateFormat, mLocalDateFormat) : "");
                    parsePhotographerEntity.setLast4SSN(mParseProfileObject.getString(ParseAPIConstants.last4SSN));

                    isPhotographerAvaBool = mParseProfileObject.getBoolean(ParseAPIConstants.isAvailable);

                    /*Load photographer ID picture*/
                    if (mParseProfileObject.getParseFile(ParseAPIConstants.photoIDPhoto) != null) {
                        mParseProfileObject.getParseFile(ParseAPIConstants.photoIDPhoto).getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {

                                try {
                                    /*If the ID picture exists in the DB, this term will make it invisible*/
                                    mUploadPhotoIdTxt.setVisibility(View.INVISIBLE);
                                    mUploadPhotoCloseImg.setVisibility(View.VISIBLE);
                                    Glide.with(getActivity())
                                            .load(data)
                                            .into(mUploadPhotoIdImg);
                                } catch (Exception ex) {
                                    Log.e(this.getClass().getSimpleName(), ex.getMessage());
                                }

                            }
                        });
                    }

                    /*Load photographer profile picture*/
                    if (mParseProfileObject.getParseFile(ParseAPIConstants.profilePhoto) != null) {
                        mParseProfileObject.getParseFile(ParseAPIConstants.profilePhoto).getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                try {
                                    /*If the Profile picture exists in the DB, this term will make it invisible*/
                                    mUploadSelfieTxt.setVisibility(View.INVISIBLE);
                                    mUploadSelfieCloseImg.setVisibility(View.VISIBLE);
                                    mUploadSelfieHolderImg.setVisibility(View.GONE);
                                    mRoundedSelfieImgLay.setVisibility(View.VISIBLE);

                                    Glide.with(getActivity())
                                            .load(data)
                                            .into(mUploadSelfieImg);
                                } catch (Exception ex) {
                                    Log.e(this.getClass().getSimpleName(), ex.getMessage());
                                }
                            }
                        });
                    }

                    /* To check the get values are empty or not*/
                    if (!parsePhotographerEntity.getFullName().isEmpty() || !parsePhotographerEntity.getCity().isEmpty()
                            || !parsePhotographerEntity.getState().isEmpty() || !parsePhotographerEntity.getLangugages().isEmpty()
                            || !parsePhotographerEntity.getLast4SSN().isEmpty() || !parsePhotographerEntity.getDateOfBirth().isEmpty()) {
                        /*set photographer details*/
                        setData(parsePhotographerEntity);
                    }
                }
                /*To hide the progress bar*/
                DialogManager.getInstance().hideProgress();
            }
        });

    }

    /*Update photographer details to parse DB*/
    private void updatePhotographerDetails() {
        DialogManager.getInstance().showProgress(getActivity());
        mParseProfileObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                DialogManager.getInstance().hideProgress();
                if (e == null) {
                    if (NetworkUtil.isNetworkAvailable(getActivity())) {
                        ParseUser photographerUser = ParseUser.getCurrentUser();
                        photographerUser.put(ParseAPIConstants.photographerObject, mParseProfileObject);
                        APIRequestHandler.getInstance().paresSaveInBackground(photographerUser, PhotographerProfileFragment.this);
                    } else {
                        DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), getString(R.string.no_internet), PhotographerProfileFragment.this);
                    }
                } else {
                    DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), e.getMessage(), PhotographerProfileFragment.this);
                }
            }
        });
    }

    /*Interface default ok click*/
    @Override
    public void onOkClick() {

    }

}
