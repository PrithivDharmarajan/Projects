package com.fautus.fautusapp.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fautus.fautusapp.R;
import com.fautus.fautusapp.main.BaseFragment;
import com.fautus.fautusapp.model.RetrieveStripeCustomerResponse;
import com.fautus.fautusapp.services.APIRequestHandler;
import com.fautus.fautusapp.ui.HomeScreen;
import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.DialogManager;
import com.fautus.fautusapp.utils.ImageUtil;
import com.fautus.fautusapp.utils.InterfaceBtnCallback;
import com.fautus.fautusapp.utils.InterfaceTwoBtnCallback;
import com.fautus.fautusapp.utils.InterfaceTwoEdtCallback;
import com.fautus.fautusapp.utils.NetworkUtil;
import com.fautus.fautusapp.utils.NumberUtil;
import com.fautus.fautusapp.utils.ParseAPIConstants;
import com.fautus.fautusapp.utils.PreferenceUtil;
import com.parse.ConfigCallback;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseCloud;
import com.parse.ParseConfig;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.fautus.fautusapp.utils.AppConstants.MOMENT_PHOTO_ENTITY;
import static com.fautus.fautusapp.utils.AppConstants.MOMENT_SELECTED_PHOTO_FILE;
import static com.fautus.fautusapp.utils.ParseAPIConstants.amountToPhotographer;

/**
 * Created by sys on 26-Apr-17.
 */

public class MomentPayFragment extends BaseFragment implements InterfaceBtnCallback {


    @BindView(R.id.friendliness_rating_bar)
    RatingBar mFriendlinessRatingBar;

    @BindView(R.id.quality_photo_rating_bar)
    RatingBar mQualityPhotoRatingBar;

    @BindView(R.id.photographer_img)
    ImageView mPhotographerImg;

    @BindView(R.id.no_tips_txt)
    TextView mNoTipsTxt;

    @BindView(R.id.tips_one_txt)
    TextView mTipsOneTxt;

    @BindView(R.id.tips_two_txt)
    TextView mTipsTwoTxt;

    @BindView(R.id.tips_five_txt)
    TextView mTipsFiveTxt;

    @BindView(R.id.tips_ten_txt)
    TextView mTipsTenTxt;

    @BindView(R.id.photo_selected_txt)
    public TextView mPhotoSelectedTxt;

    @BindView(R.id.amt_txt)
    TextView mAmtTxt;

    @BindView(R.id.per_photo_txt)
    TextView mPerPhotoTxt;

    @BindView(R.id.strip_img_lay)
    RelativeLayout mStripImgLay;


    private String mTipsAmtStr = AppConstants.FAILURE_CODE,
            mPhotographerAccountIdStr = AppConstants.FAILURE_CODE, mAmountStr = AppConstants.FAILURE_CODE, mAmountToPhotographerStr = AppConstants.FAILURE_CODE, mIssueDescription = AppConstants.FAILURE_CODE;


    private ParseObject mCustomerParseObj, mPhotographerParseObj;
    private List<ParseUser> mParseUserArrList;
    private ArrayList<View> mImagesWithStripLineArrList;
    private int mListPos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_moment_pay_screen, container, false);
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
        if (AppConstants.READY_TO_PAY_SOURCE.equals(AppConstants.FAILURE_CODE)) {
            initView();
        } else {

            checkCardAvailable();

//            mAmountStr = String.valueOf(Double.valueOf(mAmountStr) * 100);
//            momentPay(AppConstants.READY_TO_PAY_SOURCE, AppConstants.STRIP_CURRENCY_CODE, mPhotographerAccountIdStr, mAmountStr, mAmountToPhotographerStr);
        }

    }

    private void initView() {
        setTipsData(0);
        if (MOMENT_PHOTO_ENTITY != null) {
              /*Check internet connection*/
            if (NetworkUtil.isNetworkAvailable(getActivity())) {

                /*get consumer details*/
                getConsumerDetails();

                /*get photographer details*/
                ParseObject photographerObj = MOMENT_PHOTO_ENTITY.getMoment().getParseObject(ParseAPIConstants.photographer);
                if (photographerObj != null) {
                    DialogManager.getInstance().showProgress(getActivity());
                    photographerObj.fetchInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            if (e == null && object != null) {
                                mPhotographerParseObj = object;
                                getPhotographerPic();
                            } else if (e != null) {
                                DialogManager.getInstance().hideProgress();
                                DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), e.getMessage(), MomentPayFragment.this);
                            }
                        }
                    });
                } else {
                    mParseUserArrList = AppConstants.MOMENT_PHOTO_ENTITY.getMoment().getList(ParseAPIConstants.authorizedUsers);
                    mListPos = 0;
                    DialogManager.getInstance().showProgress(getActivity());
                    getUserDetails(mListPos);
                }
            } else {
                /*Alert message will be appeared if there is no internet connection*/
                DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), getString(R.string.no_internet), this);

            }
        }
    }

    private void getConsumerDetails() {
         /*Check internet connection*/
        if (NetworkUtil.isNetworkAvailable(getActivity())) {
            ParseUser.getCurrentUser().fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (e == null && object != null) {
                        mCustomerParseObj = object;
                    } else {
                        if (e != null)
                            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), e.toString(), MomentPayFragment.this);
                    }
                }
            });
        } else {
            /*Alert message will be appeared if there is no internet connection*/
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), getString(R.string.no_internet), this);
        }
    }

    private void getPhotographerPic() {
         /*Load photographer profile picture*/
        if (mPhotographerParseObj != null) {
            if (mPhotographerParseObj.getParseFile(ParseAPIConstants.profilePhoto) != null) {
                mPhotographerParseObj.getParseFile(ParseAPIConstants.profilePhoto).getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] data, ParseException e) {
                        try {
                                    /*If the Profile picture exists in the DB, this term will make it invisible*/
                            Glide.with(getActivity())
                                    .load(data)
                                    .into(mPhotographerImg);
                        } catch (Exception ex) {
                            Log.e(this.getClass().getSimpleName(), ex.getMessage());
                        }
                    }
                });
            }

            mPhotographerParseObj.getParseUser(ParseAPIConstants.user).fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (e == null && object != null) {
                        mPhotographerAccountIdStr = object.getString(ParseAPIConstants.stripePhotographerAccountId);
                        getParseConfigFromParseDB();
                    } else {
                        DialogManager.getInstance().hideProgress();
                        if (e != null) {
                            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), e.getMessage(), MomentPayFragment.this);
                        }
                    }

                }
            });

        }

    }

    private void getUserDetails(int pos) {
        if (mParseUserArrList.size() > pos) {
            final ParseUser user = ParseUser.getCurrentUser();
            mParseUserArrList.get(pos).fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (e == null && object != null) {
                        if (!user.getObjectId().equals(object.getObjectId())) {

                            final ParseQuery<ParseObject> userQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_User);
                            userQuery.whereEqualTo(ParseAPIConstants.objectId, object.getObjectId());
                            userQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject object, ParseException ex) {
                                    if (ex == null && object != null) {

                                        ParseQuery<ParseObject> photographerQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Photographer);
                                        photographerQuery.whereEqualTo(ParseAPIConstants.user, object);
                                        photographerQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                                            @Override
                                            public void done(ParseObject object, ParseException e) {
                                                if (e == null && object != null) {
                                                    mPhotographerParseObj = object;
                                                    getPhotographerPic();
                                                } else if (mParseUserArrList.size()-1 <= mListPos) {
                                                    DialogManager.getInstance().hideProgress();

                                                }
                                            }
                                        });

                                    } else {
                                        DialogManager.getInstance().hideProgress();
                                        if (ex != null)
                                            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), ex.getMessage(), MomentPayFragment.this);
                                    }
                                }
                            });

                        }
                    } else {
                        DialogManager.getInstance().hideProgress();
                        if (e != null)
                            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), e.toString(), MomentPayFragment.this);
                    }
                    mListPos += 1;
                    if (mParseUserArrList.size() >= mListPos) {
                        getUserDetails(mListPos);
                    }
                }
            });
        }
    }

    /*View OnClick*/
    @OnClick({R.id.no_tips_txt, R.id.tips_one_txt, R.id.tips_two_txt, R.id.tips_five_txt, R.id.tips_ten_txt, R.id.pay_download_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.no_tips_txt:
                setTipsData(0);
                break;
            case R.id.tips_one_txt:
                setTipsData(1);
                break;
            case R.id.tips_two_txt:
                setTipsData(2);
                break;
            case R.id.tips_five_txt:
                setTipsData(5);
                break;
            case R.id.tips_ten_txt:
                setTipsData(10);
                break;
            case R.id.pay_download_lay:
                validateFields();
                break;
        }
    }

    private void validateFields() {

        if (mFriendlinessRatingBar.getRating() < 3) {
            DialogManager.getInstance().showLowRatingAlertPopup(getActivity(), new InterfaceTwoEdtCallback() {
                @Override
                public void onEdtOneClick(String firstEdtStr, String secondEdtStr) {
                    mIssueDescription = firstEdtStr;
                    checkCardAvailable();
                }
            });
        } else {
            mIssueDescription = AppConstants.FAILURE_CODE;
            checkCardAvailable();
        }

    }

    private void setTipsData(int tipsPosInt) {
        mTipsAmtStr = String.valueOf(tipsPosInt);

        /*Set tips view background*/
        mNoTipsTxt.setBackground(ContextCompat.getDrawable(getActivity(), tipsPosInt == 0 ? R.drawable.corner_left_rounded_sky_bg : R.color.white));
        mTipsOneTxt.setBackground(ContextCompat.getDrawable(getActivity(), tipsPosInt == 1 ? R.color.sky_blue : R.color.white));
        mTipsTwoTxt.setBackground(ContextCompat.getDrawable(getActivity(), tipsPosInt == 2 ? R.color.sky_blue : R.color.white));
        mTipsFiveTxt.setBackground(ContextCompat.getDrawable(getActivity(), tipsPosInt == 5 ? R.color.sky_blue : R.color.white));
        mTipsTenTxt.setBackground(ContextCompat.getDrawable(getActivity(), tipsPosInt == 10 ? R.drawable.corner_right_rounded_sky_bg : R.color.white));

        /*Set tips view text color*/
        mNoTipsTxt.setTextColor(ContextCompat.getColor(getActivity(), tipsPosInt == 0 ? R.color.white : R.color.sky_blue));
        mTipsOneTxt.setTextColor(ContextCompat.getColor(getActivity(), tipsPosInt == 1 ? R.color.white : R.color.sky_blue));
        mTipsTwoTxt.setTextColor(ContextCompat.getColor(getActivity(), tipsPosInt == 2 ? R.color.white : R.color.sky_blue));
        mTipsFiveTxt.setTextColor(ContextCompat.getColor(getActivity(), tipsPosInt == 5 ? R.color.white : R.color.sky_blue));
        mTipsTenTxt.setTextColor(ContextCompat.getColor(getActivity(), tipsPosInt == 10 ? R.color.white : R.color.sky_blue));

        setData();
    }

    public void setData() {

        mPhotoSelectedTxt.setText(MOMENT_SELECTED_PHOTO_FILE.size() + " " + (MOMENT_SELECTED_PHOTO_FILE.size() > 1 ? getResources().getString(R.string.photos) : getResources().getString(R.string.photo)) + " " + getResources().getString(R.string.selected));
        Double totalAmtDouble = Double.valueOf(AppConstants.MOMENT_PAY_AMT) + (Double.valueOf(mTipsAmtStr));
        mAmountStr = String.valueOf(totalAmtDouble);
        mAmtTxt.setText(getResources().getString(R.string.dollar_sym) + " " + NumberUtil.afterTwoPointVal(String.valueOf(totalAmtDouble)));
        Double perCost = Double.valueOf(AppConstants.MOMENT_PAY_AMT) / Double.valueOf(NumberUtil.afterTwoPointVal(String.valueOf(MOMENT_SELECTED_PHOTO_FILE.size())));
        mPerPhotoTxt.setText(getResources().getString(R.string.open_bracket) + " " + getResources().getString(R.string.dollar_sym) + " " + NumberUtil.afterTwoPointVal(String.valueOf(perCost)) + " " + getResources().getString(R.string.per_photo) + " " + getResources().getString(R.string.close_bracket));

    }

    /*Interface default ok button click*/
    @Override
    public void onOkClick() {

    }

    private void momentPay(String sourceStr, String currencyStr, String photographerAccountIdStr, String amountStr, String amountToPhotographerStr) {

        if (!sourceStr.equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
            Double photographerWithTips = Double.valueOf(mTipsAmtStr) * (0.75) * 100;
            amountToPhotographerStr = NumberUtil.withOutPointVal(String.valueOf(Double.valueOf(amountToPhotographerStr) + photographerWithTips));

            DialogManager.getInstance().showProgress(getActivity());
            LinkedHashMap<String, Object> params = new LinkedHashMap<>();
            params.put(ParseAPIConstants.source, sourceStr);
            params.put(ParseAPIConstants.currency, currencyStr);
            params.put(ParseAPIConstants.photographerAccountId, photographerAccountIdStr);
            params.put(ParseAPIConstants.amount, amountStr);
            params.put(amountToPhotographer, amountToPhotographerStr);

            ParseCloud.callFunctionInBackground(ParseAPIConstants.PC_chargeStripeCustomerPayment, params, new FunctionCallback<Object>() {
                @Override
                public void done(Object object, ParseException e) {

                    if (e == null) {
                        AppConstants.READY_TO_PAY_SOURCE = AppConstants.FAILURE_CODE;
                        rateToPhotographer();
                    } else {
                        DialogManager.getInstance().hideProgress();
                        try {
                            JSONObject errorJsonObj = new JSONObject(e.getMessage());
                            String messageStr = errorJsonObj.getString(getString(R.string.message));
                            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), (messageStr != null && !messageStr.isEmpty() ? messageStr : e.getMessage()), MomentPayFragment.this);

                        } catch (Exception ex) {
                            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), e.getMessage(), MomentPayFragment.this);
                            Log.e(AppConstants.TAG, ex.getMessage());
                        }
                    }
                }
            });
        }

    }

    private void checkCardAvailable() {

        if (mCustomerParseObj != null && mCustomerParseObj.getString(ParseAPIConstants.stripeCustomerId) != null) {
            APIRequestHandler.getInstance().stripCardAPICall(mCustomerParseObj.getString(ParseAPIConstants.stripeCustomerId), MomentPayFragment.this);
        } else {
            ParseUser.getCurrentUser().fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (e == null && object != null) {
                        mCustomerParseObj = object;
                        APIRequestHandler.getInstance().stripCardAPICall(object.getString(ParseAPIConstants.stripeCustomerId), MomentPayFragment.this);
                    } else if (e != null) {
                        DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), e.getMessage(), MomentPayFragment.this);
                    }
                }
            });
        }
    }

    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof RetrieveStripeCustomerResponse) {
            RetrieveStripeCustomerResponse res = (RetrieveStripeCustomerResponse) resObj;
            if (res.getDefault_source() != null) {
                AppConstants.READY_TO_PAY_SOURCE = res.getDefault_source();
                mAmountStr = NumberUtil.withOutPointVal(String.valueOf(Double.valueOf(mAmountStr) * 100));
                momentPay(AppConstants.READY_TO_PAY_SOURCE, AppConstants.STRIP_CURRENCY_CODE, mPhotographerAccountIdStr, mAmountStr, mAmountToPhotographerStr);
            } else {
                AppConstants.READY_TO_PAY_SOURCE = AppConstants.FAILURE_CODE;
                ((HomeScreen) getActivity()).addFragment(new StripConsPaymentFragment());
            }
        }
    }


    private void rateToPhotographer() {
        ParseObject parseRatingObj = new ParseObject(ParseAPIConstants.Parse_Rating);
        parseRatingObj.put(ParseAPIConstants.moment, MOMENT_PHOTO_ENTITY.getMoment());
        parseRatingObj.put(ParseAPIConstants.photographer, mPhotographerParseObj);
        parseRatingObj.put(ParseAPIConstants.user, ParseUser.getCurrentUser());
        parseRatingObj.put(ParseAPIConstants.friendlinessRating, mFriendlinessRatingBar.getRating());
        parseRatingObj.put(ParseAPIConstants.qualityRating, mQualityPhotoRatingBar.getRating());

        if (!mIssueDescription.equalsIgnoreCase(AppConstants.FAILURE_CODE) && !mIssueDescription.isEmpty()) {
            parseRatingObj.put(ParseAPIConstants.issueDescription, mIssueDescription);
        }
        parseRatingObj.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    storePhotoPurchasedStatus();
                } else {
                    DialogManager.getInstance().hideProgress();
                    DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), e.getMessage(), MomentPayFragment.this);
                }
            }
        });
    }

    private void storePhotoPurchasedStatus() {
        if (MOMENT_SELECTED_PHOTO_FILE != null && MOMENT_SELECTED_PHOTO_FILE.size() > 0) {
            for (int momentFilePos = 0; momentFilePos < MOMENT_SELECTED_PHOTO_FILE.size(); momentFilePos++) {
                ParseObject photoParseObj = AppConstants.MOMENT_SELECTED_PHOTO_FILE.get(momentFilePos).getPhotoObj();
                photoParseObj.put(ParseAPIConstants.purchased, true);
                photoParseObj.saveInBackground();
            }
            DialogManager.getInstance().hideProgress();


            DialogManager.getInstance().showOptionAlertPopup(getActivity(), getString(R.string.payment_success_topic), getString(R.string.payment_success_msg), getString(R.string.no), getString(R.string.yes), new InterfaceTwoBtnCallback() {
                @Override
                public void onYesClick() {
                    if (getActivity() != null && PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.SETTINGS_STRIP_ON) && mImagesWithStripLineArrList != null && mImagesWithStripLineArrList.size() > 0) {
                        ImageUtil.saveToInternalStorage(getActivity(),mImagesWithStripLineArrList, 0);

                    } else {
                        ArrayList<String> urlList = new ArrayList<>();
                        for (int filePosInt = 0; filePosInt < MOMENT_SELECTED_PHOTO_FILE.size(); filePosInt++) {
                            urlList.add(AppConstants.MOMENT_SELECTED_PHOTO_FILE.get(filePosInt).getPhoto().getUrl());
                        }

                        ImageUtil.downloadImage(getActivity(),urlList, 0);
                    }

                    ((HomeScreen) getActivity()).addFragment(new ConsHomeFragment());
                }

                @Override
                public void onNoClick() {
                        /*Dialog box second btn click action*/
                    ((HomeScreen) getActivity()).addFragment(new ConsHomeFragment());
                }
            });


        }
    }


    /*Get amount Configuration*/
    private void getParseConfigFromParseDB() {
        ParseConfig.getInBackground(new ConfigCallback() {
            @Override
            public void done(ParseConfig config, ParseException e) {
                if (e == null && config != null) {
                    if (getActivity() != null && PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.SETTINGS_STRIP_ON)) {
                        getImagesWithStripLine();
                    } else {
                        DialogManager.getInstance().hideProgress();
                    }
                    setParseConfig(config);
                } else if (e != null) {
                    DialogManager.getInstance().hideProgress();
                    DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), e.getMessage(), MomentPayFragment.this);
                }
            }
        });

    }

    private void setParseConfig(ParseConfig parseConfig) {
        try {
            String mMomentCostStr = parseConfig.getNumber(ParseAPIConstants.AvidMoment) + "";
            String mPhotoCostStr = parseConfig.getNumber(ParseAPIConstants.AvidPhoto) + "";
            String mServiceCostStr = AppConstants.PHOTO_ALREADY_PURCHASED_BOOL ? AppConstants.FAILURE_CODE : parseConfig.getNumber(getResources().getString(R.string.tran_fee)) + "";

            String mPhotoSplit = parseConfig.getNumber(ParseAPIConstants.PhotoSplit) + "";
            String mMomentSplit = parseConfig.getNumber(ParseAPIConstants.MomentSplit) + "";

            String mReqSkillLevelStr = AppConstants.MOMENT_PHOTO_ENTITY.getMoment().getNumber(ParseAPIConstants.skillLevelRequested) != null ? AppConstants.MOMENT_PHOTO_ENTITY.getMoment().getNumber(ParseAPIConstants.skillLevelRequested) + "" : AppConstants.FAILURE_CODE;

            if (!(mReqSkillLevelStr.equalsIgnoreCase(AppConstants.FAILURE_CODE))) {

                mMomentCostStr = mReqSkillLevelStr.equalsIgnoreCase(AppConstants.SUCCESS_CODE) ? parseConfig.getNumber(ParseAPIConstants.SkillMoment) + "" : parseConfig.getNumber(ParseAPIConstants.ProMoment) + "";
                mPhotoCostStr = mReqSkillLevelStr.equalsIgnoreCase(AppConstants.SUCCESS_CODE) ? parseConfig.getNumber(ParseAPIConstants.SkillPhoto) + "" : parseConfig.getNumber(ParseAPIConstants.ProPhoto) + "";
            }


            mMomentCostStr = NumberUtil.afterTwoPointVal(AppConstants.PHOTO_ALREADY_PURCHASED_BOOL ? AppConstants.FAILURE_CODE : mMomentCostStr);
            mMomentSplit = NumberUtil.afterTwoPointVal(AppConstants.PHOTO_ALREADY_PURCHASED_BOOL ? AppConstants.FAILURE_CODE : mMomentSplit);
            mPhotoCostStr = NumberUtil.afterTwoPointVal(mPhotoCostStr);
            mPhotoSplit = NumberUtil.afterTwoPointVal(mPhotoSplit);


            double tempTotalPhotoPrice = Double.valueOf(NumberUtil.afterTwoPointVal(AppConstants.MOMENT_SELECTED_PHOTO_FILE.size() + "")) * Double.valueOf(mPhotoCostStr);


            double photoCalc = tempTotalPhotoPrice - (tempTotalPhotoPrice * Double.valueOf(mPhotoSplit));


            double momentCalc = Double.valueOf(mMomentCostStr) - (Double.valueOf(mMomentCostStr) * Double.valueOf(mMomentSplit));
            mAmountToPhotographerStr = NumberUtil.afterTwoPointVal(String.valueOf(photoCalc + momentCalc));

            mAmountToPhotographerStr = NumberUtil.withOutPointVal(String.valueOf(Double.valueOf(mAmountToPhotographerStr) * 100));
        } catch (Exception ex) {
            Log.e(AppConstants.TAG, ex.getMessage());
        }
    }

    private void getImagesWithStripLine() {
        mStripImgLay.removeAllViews();
        mImagesWithStripLineArrList = new ArrayList<>();
        for (int pos = 0; pos < AppConstants.MOMENT_SELECTED_PHOTO_FILE.size(); pos++) {
            final ViewGroup nullParent = null;
            View selectedView = LayoutInflater.from(getActivity()).inflate(R.layout.adap_strip_img_view, nullParent);
            final ImageView stripImg = selectedView.findViewById(R.id.actual_with_sky_img);
            if (AppConstants.MOMENT_SELECTED_PHOTO_FILE.get(pos) != null) {
                AppConstants.MOMENT_SELECTED_PHOTO_FILE.get(pos).getPhoto().getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] data, ParseException e) {
                        try {
                            Glide.with(MomentPayFragment.this)
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
        DialogManager.getInstance().hideProgress();
    }

}

