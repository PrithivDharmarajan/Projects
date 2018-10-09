package com.fautus.fautusapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fautus.fautusapp.R;
import com.fautus.fautusapp.adapter.MomentDetailsAdapter;
import com.fautus.fautusapp.entity.MomentPhotoEntity;
import com.fautus.fautusapp.entity.PhotoEntity;
import com.fautus.fautusapp.main.BaseFragment;
import com.fautus.fautusapp.model.SendEmailResponse;
import com.fautus.fautusapp.services.APIRequestHandler;
import com.fautus.fautusapp.ui.HomeScreen;
import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.DateUtil;
import com.fautus.fautusapp.utils.DialogManager;
import com.fautus.fautusapp.utils.ImageUtil;
import com.fautus.fautusapp.utils.InterfaceBtnCallback;
import com.fautus.fautusapp.utils.InterfaceTwoEdtCallback;
import com.fautus.fautusapp.utils.NetworkUtil;
import com.fautus.fautusapp.utils.NumberUtil;
import com.fautus.fautusapp.utils.ParseAPIConstants;
import com.fautus.fautusapp.utils.PreferenceUtil;
import com.parse.ConfigCallback;
import com.parse.GetDataCallback;
import com.parse.ParseConfig;
import com.parse.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sys on 25-Apr-17.
 */

public class MomentDetailsFragment extends BaseFragment implements InterfaceBtnCallback {


    @BindView(R.id.address_txt)
    TextView mAddressTxt;

    @BindView(R.id.date_txt)
    TextView mDateTxt;

    @BindView(R.id.photo_selected_txt)
    public TextView mPhotoSelectedTxt;

    @BindView(R.id.amt_txt)
    TextView mAmtTxt;

    @BindView(R.id.per_photo_txt)
    TextView mPerPhotoTxt;

    @BindView(R.id.moment_img_recycler_view)
    RecyclerView mMomentImgRecyclerView;

    @BindView(R.id.buy_lay)
    RelativeLayout mBuyLay;

    @BindView(R.id.share_img)
    ImageView mShareImg;

    @BindView(R.id.amt_txt_lay)
    LinearLayout mAmtTxtLay;

    @BindView(R.id.bottom_txt)
    TextView mBottomTxt;

    @BindView(R.id.strip_img_lay)
    RelativeLayout mStripImgLay;

    private SimpleDateFormat mServerDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.US);
    private SimpleDateFormat mLocalDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
    private String mMomentCostStr = AppConstants.FAILURE_CODE, mPhotoCostStr = AppConstants.FAILURE_CODE, mReqSkillLevelStr = AppConstants.FAILURE_CODE, mServiceCostStr = AppConstants.FAILURE_CODE;
    private int mPhotoSelectedInt = 0, mUnPurchasedPhotoCountInt = 0;
    private Double mTotalAmtDouble;
    private ArrayList<View> mImagesWithStripLineArrList=new ArrayList<>();
    private ArrayList<String> mPurchasedPhotoURLArrList=new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_moment_buy_screen, container, false);
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
        ((HomeScreen) getActivity()).setDrawerAction(false);
        ((HomeScreen) getActivity()).setHeaderText(getString(R.string.moment));
        ((HomeScreen) getActivity()).setHeadRightImgVisible(false, R.mipmap.app_icon);
        initView();

    }

    @Override
    public void onResume() {
        super.onResume();
        hideSoftKeyboard();
    }

    public void setConsumerDetails(int photoSelectedInt) {
        mPhotoSelectedInt = photoSelectedInt;
        mPhotoSelectedTxt.setText(photoSelectedInt + " " + (photoSelectedInt > 1 ? getResources().getString(R.string.photos) : getResources().getString(R.string.photo)) + " " + getResources().getString(R.string.selected));


        mTotalAmtDouble = Double.valueOf(mMomentCostStr) + (Double.valueOf(mPhotoCostStr) * Double.valueOf(NumberUtil.afterTwoPointVal(photoSelectedInt + ""))) + Double.valueOf(mServiceCostStr);

        mAmtTxt.setText(getResources().getString(R.string.dollar_sym) + " " + (mPhotoSelectedInt > 0 ? NumberUtil.afterTwoPointVal(String.valueOf(mTotalAmtDouble)) : AppConstants.FAILURE_CODE));
        Double perCost = mTotalAmtDouble / Double.valueOf(NumberUtil.afterTwoPointVal(photoSelectedInt + ""));
        mPerPhotoTxt.setText(getResources().getString(R.string.open_bracket) + " " + getResources().getString(R.string.dollar_sym) + " " + (mPhotoSelectedInt > 0 ? NumberUtil.afterTwoPointVal(String.valueOf(perCost)) : AppConstants.FAILURE_CODE) + " " + getResources().getString(R.string.per_photo) + " " + getResources().getString(R.string.close_bracket));


        if (mPurchasedPhotoURLArrList.size() > 0 && AppConstants.MOMENT_PHOTO_ENTITY.getPhoto().size() != mPurchasedPhotoURLArrList.size()) {
            mPhotoSelectedTxt.setText(getResources().getString(R.string.purchase_remaining) + " " + mUnPurchasedPhotoCountInt + " " + (photoSelectedInt > 1 ? getResources().getString(R.string.photos) : getResources().getString(R.string.photo)));
            mPerPhotoTxt.setText(getResources().getString(R.string.approx) + " " + mPerPhotoTxt.getText().toString().trim());
        }

    }


    private void initView() {
        if (PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.USER_IS_CONSUMER)) {
            getPhotoPurchasedDetails();
            getParseConfigFromParseDB();
            getImagesWithStripLine();
        } else {
            mPhotoSelectedTxt.setText(AppConstants.MOMENT_PHOTO_ENTITY.getPhoto().size() + " " + (AppConstants.MOMENT_PHOTO_ENTITY.getPhoto().size() > 1 ? getResources().getString(R.string.photos) : getResources().getString(R.string.photo)) + " " + getResources().getString(R.string.in_moment).toUpperCase(Locale.US));
            mPerPhotoTxt.setText(AppConstants.MOMENT_PHOTO_ENTITY.getMoment().getString(ParseAPIConstants.adHocCode));
            mAmtTxtLay.setVisibility(View.GONE);
        }
        MomentPhotoEntity momentRes = AppConstants.MOMENT_PHOTO_ENTITY;
        if (momentRes != null) {
            ((HomeScreen) getActivity()).setHeaderText(getString(R.string.moment) + " " + getResources().getString(R.string.colon_sym) + " " + momentRes.getMoment().getString(ParseAPIConstants.adHocCode));
            mAddressTxt.setText(momentRes.getMoment().getString(ParseAPIConstants.locationCity));
            mDateTxt.setText(DateUtil.getCustomDateFormat(DateUtil.getStringFromDate(momentRes.getMoment().getDate(ParseAPIConstants.momentDate), mServerDateFormat), mServerDateFormat, mLocalDateFormat));

            MomentDetailsAdapter galleryMomentAdapter = new MomentDetailsAdapter(getActivity(), mUnPurchasedPhotoCountInt, momentRes.getPhoto(), MomentDetailsFragment.this);
            mMomentImgRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            mMomentImgRecyclerView.setAdapter(galleryMomentAdapter);
            mMomentImgRecyclerView.setNestedScrollingEnabled(true);

            mBuyLay.setVisibility(AppConstants.MOMENT_ALREADY_BOUGHT.equalsIgnoreCase(AppConstants.SUCCESS_CODE) && PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.USER_IS_CONSUMER) ? View.GONE : View.VISIBLE);
            mBottomTxt.setText(PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.USER_IS_CONSUMER) ? getResources().getString(R.string.but_moment) : getResources().getString(R.string.email_invite));

            if (PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.USER_IS_CONSUMER) && mPurchasedPhotoURLArrList.size() > 0 && AppConstants.MOMENT_PHOTO_ENTITY.getPhoto().size() != mPurchasedPhotoURLArrList.size()) {
                mBottomTxt.setText(getResources().getString(R.string.complete_moment));
            }

        }

        ((HomeScreen) getActivity()).mHeaderLeftLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppConstants.MOMENT_FROM_LIST.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                    getActivity().onBackPressed();
                } else {
                    ((HomeScreen) getActivity()).addFragment(PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.USER_IS_CONSUMER) ? new ConsHomeFragment() : new PhotoHomeFragment());
                }

            }
        });
    }


    private void getPhotoPurchasedDetails() {
        ArrayList<PhotoEntity> photoList = AppConstants.MOMENT_PHOTO_ENTITY.getPhoto();
        mUnPurchasedPhotoCountInt = 0;
        mPurchasedPhotoURLArrList = new ArrayList<>();
        for (int i = 0; i < photoList.size(); i++) {
            if (photoList.get(i).getPhoto_purchased().equalsIgnoreCase(AppConstants.FAILURE_CODE))
                mUnPurchasedPhotoCountInt += 1;
            else
                mPurchasedPhotoURLArrList.add(photoList.get(i).getPhoto().getUrl());

        }

        if (mPurchasedPhotoURLArrList.size() > 0) {
            ((HomeScreen) getActivity()).setHeadRightImgVisible(true, R.drawable.download_img);
            ((HomeScreen) getActivity()).mHeaderRightLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadPurchasedImages();
                }
            });
        }
    }


    private void downloadPurchasedImages() {
        if (PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.SETTINGS_STRIP_ON) && mImagesWithStripLineArrList != null && mImagesWithStripLineArrList.size() > 0) {
               ImageUtil.saveToInternalStorage( getActivity(),mImagesWithStripLineArrList,0);

        } else {
                ImageUtil.downloadImage(getActivity(),mPurchasedPhotoURLArrList,0);
        }
    }


    /*View OnClick*/
    @OnClick({R.id.share_img, R.id.ques_mark_img_img, R.id.buy_moment_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_img:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                //  sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string
                        .share_txt) + " " + AppConstants.MOMENT_PHOTO_ENTITY.getMoment().getString(ParseAPIConstants.adHocCode));
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_moment)));

                break;
            case R.id.ques_mark_img_img:
                String photosCountStr = mPhotoSelectedInt + " " + (mPhotoSelectedInt > 1 ? getResources().getString(R.string.photos) : getResources().getString(R.string.photo));
                String photoCostStr = NumberUtil.afterTwoPointVal(String.valueOf(Double.valueOf(mPhotoCostStr) * Double.valueOf(NumberUtil.afterTwoPointVal(mPhotoSelectedInt + ""))));

                DialogManager.getInstance().showMomentAmtDetailsPopup(getActivity(), mReqSkillLevelStr, mMomentCostStr, photosCountStr, photoCostStr, mServiceCostStr, NumberUtil.afterTwoPointVal(mTotalAmtDouble + ""));
                break;
            case R.id.buy_moment_lay:
                if (PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.USER_IS_CONSUMER)) {
                    if (mPhotoSelectedInt > 0) {
                        AppConstants.PHOTO_ALREADY_PURCHASED_BOOL = mPurchasedPhotoURLArrList.size() > 0 && AppConstants.MOMENT_PHOTO_ENTITY.getPhoto().size() != mPurchasedPhotoURLArrList.size();
                        AppConstants.MOMENT_PAY_AMT = String.valueOf(mTotalAmtDouble);
                        AppConstants.READY_TO_PAY_SOURCE = AppConstants.FAILURE_CODE;
                        ((HomeScreen) getActivity()).addFragment(new MomentPayFragment());
                    }
                } else {
                    DialogManager.getInstance().showEmailInviteAlertPopup(getActivity(), new InterfaceTwoEdtCallback() {
                        @Override
                        public void onEdtOneClick(String firstEdtStr, String secondEdtStr) {
                            APIRequestHandler.getInstance().callSendEmailAPI(firstEdtStr, getResources().getString(R.string.your_moment), String.format(getResources().getString(R.string.your_fautus_snap_code), AppConstants.MOMENT_PHOTO_ENTITY.getMoment().getString(ParseAPIConstants.adHocCode)), MomentDetailsFragment.this);

                        }
                    });
                }


        }

    }


    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof SendEmailResponse) {
            ((HomeScreen) getActivity()).addFragment(PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.USER_IS_CONSUMER) ? new ConsHomeFragment() : new PhotoHomeFragment());
        }
    }

    private void getParseConfigFromParseDB() {
        /*Check internet connection*/
        if (NetworkUtil.isNetworkAvailable(getActivity())) {
            DialogManager.getInstance().showProgress(getActivity());
            ParseConfig.getInBackground(new ConfigCallback() {
                @Override
                public void done(ParseConfig config, ParseException e) {
                    DialogManager.getInstance().hideProgress();
                    if (e == null && config != null) {
                        setParseConfig(config);
                    }
                }
            });
        } else {
             /*Alert message will be appeared if there is no internet connection*/
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), getString(R.string.no_internet), this);
        }
    }

    private void setParseConfig(ParseConfig parseConfig) {
        try {

            mMomentCostStr = parseConfig.getNumber(ParseAPIConstants.AvidMoment) + "";
            mPhotoCostStr = parseConfig.getNumber(ParseAPIConstants.AvidPhoto) + "";
            mServiceCostStr = parseConfig.getNumber(getResources().getString(R.string.tran_fee)) + "";
            mReqSkillLevelStr = AppConstants.MOMENT_PHOTO_ENTITY.getMoment().getNumber(ParseAPIConstants.skillLevelRequested) != null ? AppConstants.MOMENT_PHOTO_ENTITY.getMoment().getNumber(ParseAPIConstants.skillLevelRequested) + "" : AppConstants.FAILURE_CODE;

            if (!mReqSkillLevelStr.isEmpty() && !(mReqSkillLevelStr.equalsIgnoreCase(AppConstants.FAILURE_CODE))) {

                mMomentCostStr = mReqSkillLevelStr.equalsIgnoreCase(AppConstants.SUCCESS_CODE) ? parseConfig.getNumber(ParseAPIConstants.SkillMoment) + "" : parseConfig.getNumber(ParseAPIConstants.ProMoment) + "";
                mPhotoCostStr = mReqSkillLevelStr.equalsIgnoreCase(AppConstants.SUCCESS_CODE) ? parseConfig.getNumber(ParseAPIConstants.SkillPhoto) + "" : parseConfig.getNumber(ParseAPIConstants.ProPhoto) + "";
            }

            if (mPurchasedPhotoURLArrList.size() > 0 && AppConstants.MOMENT_PHOTO_ENTITY.getPhoto().size() != mPurchasedPhotoURLArrList.size()) {
                mMomentCostStr = AppConstants.FAILURE_CODE;
            }

            mMomentCostStr = NumberUtil.afterTwoPointVal(mMomentCostStr);
            mPhotoCostStr = NumberUtil.afterTwoPointVal(mPhotoCostStr);
            mServiceCostStr = NumberUtil.afterTwoPointVal(mServiceCostStr);

            mPhotoSelectedInt = mUnPurchasedPhotoCountInt;
            setConsumerDetails(mPhotoSelectedInt);
        } catch (Exception ex) {

            Log.e(AppConstants.TAG, ex.getMessage());
        }
    }

    @Override
    public void onOkClick() {

    }

    private void getImagesWithStripLine() {
        mStripImgLay.removeAllViews();
        mImagesWithStripLineArrList = new ArrayList<>();
        for (int pos = 0; pos < AppConstants.MOMENT_PHOTO_ENTITY.getPhoto().size(); pos++) {
            if (AppConstants.MOMENT_PHOTO_ENTITY.getPhoto().get(pos).getPhoto_purchased().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                final ViewGroup nullParent = null;
                View selectedView = LayoutInflater.from(getActivity()).inflate(R.layout.adap_strip_img_view, nullParent);
                final ImageView stripImg = selectedView.findViewById(R.id.actual_with_sky_img);
                if (AppConstants.MOMENT_PHOTO_ENTITY.getPhoto().get(pos) != null) {
                    AppConstants.MOMENT_PHOTO_ENTITY.getPhoto().get(pos).getPhoto().getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            try {
                                Glide.with(MomentDetailsFragment.this)
                                        .load(data)
                                        .into(stripImg);
                            } catch (Exception ex) {
                                stripImg.setImageResource(R.drawable.app_transparent_img);
                                Log.e(AppConstants.TAG, ex.getMessage());
                            }
                        }
                    });
                } else {
                    stripImg.setImageResource(R.drawable.app_transparent_img);
                }
                mImagesWithStripLineArrList.add(selectedView);
                mStripImgLay.addView(selectedView);
            }
        }
        DialogManager.getInstance().hideProgress();
    }
}
