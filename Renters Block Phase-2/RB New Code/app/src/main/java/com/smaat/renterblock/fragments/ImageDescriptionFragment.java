package com.smaat.renterblock.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.ImageUploadEntity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.ImageVideoUploadResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.smaat.renterblock.R.id.capture_image;
import static com.smaat.renterblock.R.id.post_desc;


public class ImageDescriptionFragment extends BaseFragment {

    @BindView(post_desc)
    TextView postDescTxt;
    @BindView(R.id.back_icon)
    LinearLayout cancelLay;
    @BindView(R.id.description)
    EditText mDescription;
    @BindView(capture_image)
    ImageView mCaptureImage;

    String mDescriptionStr;
    ImageUploadEntity mEntityDetails;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View imageDescriptionView = inflater.inflate(R.layout.post_photo_video_view, container, false);

        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, imageDescriptionView);

        initView();

        return imageDescriptionView;
    }

    private void initView() {

        mEntityDetails = AppConstants.IMAGE_UPLOAD_ENTITY;
        if (mEntityDetails != null) {

            mCaptureImage.setImageBitmap(mEntityDetails.getBitmap());

            mDescriptionStr = mDescription.getText().toString().trim();

            postDescTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uploadImageAndVideo(mEntityDetails.getType(), mEntityDetails.getFilepath(), mDescriptionStr);
                }
            });

            cancelLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uploadImageAndVideo(mEntityDetails.getType(), mEntityDetails.getFilepath(), mDescriptionStr);
                }
            });
        }
    }


    /*Fragment manual onResume*/
    @Override
    public void onFragmentResume() {

    }


    private void uploadImageAndVideo(String type, String filepath, String description) {

        APIRequestHandler.getInstance().ImageVideoUpload(AppConstants.mPropertyPics.get(0).getProperty_id(), "0", type, description, filepath, ImageDescriptionFragment.this);

    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);

        if (responseObj instanceof ImageVideoUploadResponse) {
            final ImageVideoUploadResponse imageUploadEntity = (ImageVideoUploadResponse) responseObj;
            if (imageUploadEntity.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                DialogManager.getInstance().showToast(getActivity(), imageUploadEntity.getMsg());
//                AppConstants.mPropertyPics.add(imageUploadEntity.getUserpropertypic().get(0).getFile());
            }
        }

    }

}
