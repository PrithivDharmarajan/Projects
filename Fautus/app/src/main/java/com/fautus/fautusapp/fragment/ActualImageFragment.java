package com.fautus.fautusapp.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.fautus.fautusapp.R;
import com.fautus.fautusapp.main.BaseFragment;
import com.fautus.fautusapp.ui.HomeScreen;
import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.DialogManager;
import com.fautus.fautusapp.utils.ImageUtil;
import com.fautus.fautusapp.utils.InterfaceBtnCallback;
import com.fautus.fautusapp.utils.InterfaceTwoBtnCallback;
import com.fautus.fautusapp.utils.ParseAPIConstants;
import com.fautus.fautusapp.utils.PreferenceUtil;
import com.fautus.fautusapp.utils.ZoomLayout;
import com.parse.GetDataCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This class implements UI and Functions to view the original image of moment
 *
 * @author Smaat Apps
 * @version 1.0
 * @since 2017-04-26
 */

public class ActualImageFragment extends BaseFragment implements InterfaceBtnCallback {


    @BindView(R.id.zoom_parent_lay)
    ZoomLayout mZoomParentLay;

    @BindView(R.id.actual_with_sky_img)
    ImageView mActualWithSkyImg;

    @BindView(R.id.actual_with_out_sky_img)
    ImageView mActualWithOutSkyImg;

    @BindView(R.id.actual_holder_img)
    ImageView mActualHolderImg;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_actual_image_screen, container, false);
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


    @Override
    public void onDetach() {
        super.onDetach();
    }

    /*Fragment manual onResume*/
    @Override
    public void onFragmentResume() {
        super.onFragmentResume();
        /*set header text and header imges*/
        ((HomeScreen) getActivity()).setDrawerAction(false);
        ((HomeScreen) getActivity()).setHeaderText(getString(R.string.moment));
        ((HomeScreen) getActivity()).setHeadRightImgVisible(true, AppConstants.MOMENT_PHOTO_FILE.getPhoto_purchased().equals(AppConstants.FAILURE_CODE) ? R.drawable.actual_select_img : R.drawable.download_img);
        initView();

    }

    private void initView() {
/* To check the STRIP button is ON or OFF */

        final boolean isSkyBlueVisible = PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.SETTINGS_STRIP_ON);
    /* If STRIP button is ON, mActualWithSkyImg can be visible */
        mActualWithSkyImg.setVisibility(isSkyBlueVisible ? View.VISIBLE : View.GONE);
    /* If STRIP button is OFF, mActualWithOutSkyImg can be visible */
        mActualWithOutSkyImg.setVisibility(isSkyBlueVisible ? View.GONE : View.VISIBLE);
        if (AppConstants.MOMENT_PHOTO_ENTITY != null) {
            ((HomeScreen) getActivity()).setHeaderText(getString(R.string.moment) + " " + getResources().getString(R.string.colon_sym) + " " + AppConstants.MOMENT_PHOTO_ENTITY.getMoment().getString(ParseAPIConstants.adHocCode));
        }

        if (AppConstants.MOMENT_PHOTO_FILE != null) {
        /* if AppConstants.MOMENT_ALREADY_BOUGHT is one, then the moment image is already bought */
            mActualHolderImg.setVisibility(AppConstants.MOMENT_PHOTO_FILE.getPhoto_purchased().equals(AppConstants.FAILURE_CODE) ? View.VISIBLE : View.GONE);
            AppConstants.MOMENT_PHOTO_FILE.getPhoto().getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    try {
                        Glide.with(getActivity())
                                .load(data).signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                                .into(isSkyBlueVisible ? mActualWithSkyImg : mActualWithOutSkyImg);
                    } catch (Exception ex) {
                        (isSkyBlueVisible ? mActualWithSkyImg : mActualWithOutSkyImg).setImageResource(R.drawable.app_transparent_img);
                        Log.e(AppConstants.TAG, ex.getMessage());
                    }
                }
            });
        } else {
            (isSkyBlueVisible ? mActualWithSkyImg : mActualWithOutSkyImg).setImageResource(R.mipmap.app_icon);
        }

        ((HomeScreen) getActivity()).mHeaderRightLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppConstants.MOMENT_PHOTO_FILE.getPhoto_purchased().equals(AppConstants.SUCCESS_CODE)) {
                    DialogManager.getInstance().showOptionAlertPopup(getActivity(), getResources().getString(R.string.app_name), getString(R.string.down_photo), getResources().getString(R.string.no), getResources().getString(R.string.yes), new InterfaceTwoBtnCallback() {
                        @Override
                        public void onYesClick() {
                            if (permissionsReadWrite()) {
                                /* to download actual image */
                                if (mActualWithSkyImg.getVisibility() == View.VISIBLE) {
                                /* to download with strip image */
                                    ArrayList<View> viewList = new ArrayList<>();
                                    viewList.add(mActualWithSkyImg);
                                    ImageUtil.saveToInternalStorage(getActivity(),viewList, 0);
                                } else {
                                    /* to download without strip image */
                                    ArrayList<String> urlList = new ArrayList<>();
                                    urlList.add(AppConstants.MOMENT_PHOTO_FILE.getPhoto().getUrl());
                                    ImageUtil.downloadImage(getActivity(),urlList, 0);
                                }
                            }
                        }

                        @Override
                        public void onNoClick() {

                        }
                    });

                }
            }
        });

        mZoomParentLay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mZoomParentLay.init(getActivity());
                return false;
            }
        });

    }

    /* To get Permission on access storage*/
    private boolean permissionsReadWrite() {
        boolean addPermission = true;
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            int permissionRead = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
            int permissionWrite = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permissionRead != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (permissionWrite != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

        }
        if (!listPermissionsNeeded.isEmpty()) {
            addPermission = isPermission(listPermissionsNeeded, new InterfaceTwoBtnCallback() {
                @Override
                public void onYesClick() {
                    if (mActualWithSkyImg.getVisibility() == View.VISIBLE) {
                        ArrayList<View> viewList = new ArrayList<>();
                        viewList.add(mActualWithSkyImg);
                        ImageUtil.saveToInternalStorage(getActivity(),viewList, 0);
                    } else {
                        ArrayList<String> urlList = new ArrayList<>();
                        urlList.add(AppConstants.MOMENT_PHOTO_FILE.getPhoto().getUrl());
                        ImageUtil.downloadImage(getActivity(),urlList, 0);
                    }
                }

                public void onNoClick() {
                }
            });
        }
        return addPermission;
    }

    @Override
    public void onOkClick() {

    }
}
