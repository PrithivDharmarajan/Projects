package com.smaat.spark.ui;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.smaat.spark.R;
import com.smaat.spark.entity.outputEntity.UserDetailsEntity;
import com.smaat.spark.main.BaseActivity;
import com.smaat.spark.model.CommonModel;
import com.smaat.spark.services.APIRequestHandler;
import com.smaat.spark.utils.AppConstants;
import com.smaat.spark.utils.GlobalMethods;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;


public class ChatImageScreenn extends BaseActivity {


    @BindView(R.id.parent_lay)
    ViewGroup mChatImgViewGroup;

    @BindView(R.id.preview_img)
    ImageView mPreviewImg;

    @BindView(R.id.chat_edt)
    EditText mChatEdt;

    private final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    private final String IMAGE_DIRECTORY_NAME = AppConstants.APP_NAME;
    private Uri fileUri;
    private String mImgNameStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ui_chat_image_screen);
        ButterKnife.bind(this);
        setupUI(mChatImgViewGroup);
        initComponents();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    private void initComponents() {
        captureImage();
    }

    @OnClick({R.id.send_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_btn:
                if (mPreviewImg.getVisibility() == View.VISIBLE) {
                    AppConstants.CHAT_PIC_TXT = mChatEdt.getText().toString().trim();
                    APIRequestHandler.getInstance().callChatImageUploadAPI(fileUri.getPath(), mImgNameStr, AppConstants.CHAT_PIC_FRIEND_ID, mChatEdt.getText().toString().trim(), AppConstants.CHAT_PIC_SUB, AppConstants.CHAT_PIC_MSG_FLOW, this);

                }
                break;
        }


    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
//                Locale.getDefault()).format(new Date());
        String timeStamp = new SimpleDateFormat("dd-MM-yyyy-HHmmss",
                Locale.getDefault()).format(new Date());

        UserDetailsEntity user = GlobalMethods.getUserDetailsRes(ChatImageScreenn.this);

        mImgNameStr = user.getUsername() + "-" + timeStamp;

        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {

//            mediaFile = new File(mediaStorageDir.getPath() + File.separator
//                    + "IMG_" + timeStamp + ".jpg");
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + user.getUsername() + "-" + timeStamp + ".jpg");
            sysOut("mediaFile--" + mediaFile.getAbsolutePath().toString());
            sysOut("mImgNameStr--" + mImgNameStr);
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                onBackPressed();
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                onBackPressed();
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        }
    }

    private void previewCapturedImage() {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
//            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
//                    options);
            AppConstants.CHAT_PIC_LOCAL_PATH = fileUri.toString();
//            mPreviewImg.setImageBitmap(bitmap);
            mPreviewImg.setVisibility(View.VISIBLE);
//            Glide.with(this).load(fileUri).into(mPreviewImg);

            sysOut("fileUri.toString()"+fileUri.toString());
            sysOut("fileUri.getPath()"+fileUri.getPath());

            Glide.with(this)
//                    .load(new File(AppConstants.CHAT_PIC_LOCAL_PATH))
//                    .load(fileUri)
                    .load(AppConstants.CHAT_PIC_LOCAL_PATH)
//                    .load(Uri.parse(CHAT_PIC_LOCAL_PATH))
                    .into(mPreviewImg);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        AppConstants.CHAT_PIC_BACK=AppConstants.FAILURE_CODE;
        finish();
    }

    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof CommonModel) {
            CommonModel chatConnDisRes = (CommonModel) resObj;
            if (chatConnDisRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                finish();

            }
        }
    }
}