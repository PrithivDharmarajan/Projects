package com.bridgellc.bridge.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.apiinterface.APIRequestHandler;
import com.bridgellc.bridge.entity.HomeSingleItemEntity;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.model.CommonResponse;
import com.bridgellc.bridge.model.FinishServicesResponse;
import com.bridgellc.bridge.model.ItemDetailResponse;
import com.bridgellc.bridge.model.ItemEditableResponse;
import com.bridgellc.bridge.model.PayForItemResponse;
import com.bridgellc.bridge.ui.home.HomeScreenActivity;
import com.bridgellc.bridge.ui.upload.UpdateItemScreen;
import com.bridgellc.bridge.ui.upload.UploadEntityClass;
import com.bridgellc.bridge.ui.upload.UploadScreen;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogManagerTwoBtnCallback;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.GlobalMethods;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProductDetailsScreen extends BaseActivity implements View.OnClickListener {
    public static RelativeLayout mRatLay, mBottomLay;
    public static String mFooterOneTxt = "", mFooterTwoTxt = "", mFooterThreeTxt = "";
    public static int mFooterBtnCount;
    public static HomeSingleItemEntity mHomeSingleItemEntity;
    private RatingBar mRatingBar;
    private ImageView mVerifyImg;
    private ViewPager mImagePager;
    private int page_count = 0;
    private ArrayList<String> mImagesList = new ArrayList<>();
    private ImageView mSlidepointerOne, mSlidepointerTwo, mSlidepointerThree;
    private RelativeLayout mFtrOneLay, mFtrTwoLay, mFtrThreeLay;
    private TextView mFooterOneBtn, mFooterTwoBtn, mFooterThreeBtn;
    private Button mFtrOneBtn;
    private LinearLayout mCertPartLay, mPartWebPhLay;
    private ImageView mFooterOneImg, mFooterTwoImg, mFooterThreeImg, mHeaderRightImage;
    private TextView mPersonNameTxt, mPersonUnivNameTxt, mItemNameTxt, mItemQuantityTxt, mItemPriceTxt, mItemServicesPriceTxt, mItemTotalPriceTxt, mItemCategoryTxt, mItemDeliveryTypeTxt, mItemConditionTxt, mTipsTxt, mItemDescTxt, mItemWebTxt, mItemPhTxt;
    //    public static String priceAmount;
    private RelativeLayout mBottomOneLay, mBottomTwoLay;
    private String otherUserId, UserName;
    public static boolean isFromHome;
    private SimpleDateFormat mTargetDateTime = new SimpleDateFormat("MMMM dd, yyyy hh:mm aa", Locale.US);
    private SimpleDateFormat mServerTimeForm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    private GradientDrawable bgShapeGreen, bgShapeWhite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details_screen);

    }

    private void initComponents() {
        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);

        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        mHeadeRightBtnLay = (RelativeLayout) findViewById(R.id.header_right_btn_lay);
        mHeaderRightImage = (ImageView) findViewById(R.id.header_right_img);
        mHeaderRightImage.setImageResource(R.drawable.preview_icon);


        mFtrOneLay = (RelativeLayout) findViewById(R.id.footer_one_lay);
        mFtrTwoLay = (RelativeLayout) findViewById(R.id.footer_two_lay);
        mFtrThreeLay = (RelativeLayout) findViewById(R.id.footer_three_lay);

        mFooterOneLay = (RelativeLayout) findViewById(R.id.footer_one_ly);
        mFooterTwoLay = (RelativeLayout) findViewById(R.id.footer_two_ly);
        mFooterThreeLay = (RelativeLayout) findViewById(R.id.footer_three_ly);

        //Bottom Custm Lay
        mBottomOneLay = (RelativeLayout) findViewById(R.id.bottom_one_lay);
        mBottomTwoLay = (RelativeLayout) findViewById(R.id.bottom_two_lay);


        mImagePager = (ViewPager) findViewById(R.id.uploadPager);
        mImagePager.setAdapter(new MyPagerAdapter());

        mBottomLay = (RelativeLayout) findViewById(R.id.bottom_lay);

        mFtrOneBtn = (Button) findViewById(R.id.footer_one_btn);

        mFooterOneBtn = (TextView) findViewById(R.id.footer_one_txt);
        mFooterTwoBtn = (TextView) findViewById(R.id.footer_two_txt);
        mFooterThreeBtn = (TextView) findViewById(R.id.footer_three_txt);


        //Image
        mFooterOneImg = (ImageView) findViewById(R.id.footer_one_img);
        mFooterTwoImg = (ImageView) findViewById(R.id.footer_two_img);
        mFooterThreeImg = (ImageView) findViewById(R.id.footer_three_img);


        mRatLay = (RelativeLayout) findViewById(R.id.rat_lay);
        if (mFooterBtnCount == 0) {
            mBottomLay.setVisibility(View.GONE);

        } else if (mFooterBtnCount == 1) {
            mBottomOneLay.setVisibility(View.VISIBLE);
            mBottomTwoLay.setVisibility(View.GONE);

            mFooterOneLay.setVisibility(View.VISIBLE);
            mFooterTwoLay.setVisibility(View.GONE);
            mFooterThreeLay.setVisibility(View.GONE);


            mFtrOneLay.setVisibility(View.VISIBLE);
            mFtrOneLay.setBackgroundColor(getResources().getColor(R.color.blue_btn_bg));
            mFtrTwoLay.setVisibility(View.GONE);
            mFtrThreeLay.setVisibility(View.GONE);

            mFooterOneBtn.setText(mFooterOneTxt);
            mFtrOneBtn.setText(mFooterOneTxt);

            if (mFooterOneTxt.equalsIgnoreCase(getString(R.string.edit))) {
                TextView mHeaderRightText = (TextView) findViewById(R.id.header_right_txt);
                mHeaderRightText.setText(getString(R.string.delete));
                mHeaderRightText.setVisibility(View.VISIBLE);
                mHeaderRightText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        DialogManager.showBaseTwoBtnDialog(ProductDetailsScreen.this, getString(R
                                .string.app_name), getString(R.string.delete_conf), getString(R.string
                                .yes), getString(R.string.no), new DialogManagerTwoBtnCallback() {
                            @Override
                            public void onBtnOkClick(String mOkStr) {
                                APIRequestHandler.getInstance().getDeleteItemResponse
                                        (mHomeSingleItemEntity.getItem_id(), ProductDetailsScreen
                                                .this);
                            }

                            @Override
                            public void onBtnCancelClick(String mCancelStr) {

                            }
                        });


//                        DialogManager.showBasicBtnAlertDialog(ProductDetailsScreen.this, getString(R
//                                .string.delete), getString(R.string.delete_conf), new
//                                DialogMangerOkCallback() {
//
//
//                                    @Override
//                                    public void onOkClick() {
//                                        APIRequestHandler.getInstance().getDeleteItemResponse
//                                                (mHomeSingleItemEntity.getItem_id(), ProductDetailsScreen
//                                                        .this);
//                                    }
//                                });

                    }
                });
            }

        } else if (mFooterBtnCount == 2) {


            mBottomOneLay.setVisibility(View.GONE);
            mBottomTwoLay.setVisibility(View.VISIBLE);


            mFooterOneLay.setVisibility(View.VISIBLE);
            mFooterTwoLay.setVisibility(View.VISIBLE);
            mFooterThreeLay.setVisibility(View.GONE);

            mFooterOneBtn.setText(mFooterOneTxt);
            mFooterTwoBtn.setText(mFooterTwoTxt);

            //Button One Bg
            if (mFooterOneTxt.equalsIgnoreCase(getString(R.string.chat))) {
                mFooterOneImg.setImageResource(R.drawable.chat_btn);
            }

            //Button Two Bg
            if (mFooterTwoTxt.equalsIgnoreCase(getString(R.string.finish))) {
                mFooterTwoImg.setImageResource(R.drawable.finish_btn);
            } else if (mFooterTwoTxt.equalsIgnoreCase(getString(R.string.start))) {

                mFooterTwoImg.setImageResource(R.drawable.start_btn);
            } else if (mFooterTwoTxt.equalsIgnoreCase(getString(R.string.upload_txt))) {

                mFooterTwoImg.setImageResource(R.drawable.upload_btn);
            } else if (mFooterTwoTxt.equalsIgnoreCase(getString(R.string.code))) {

                mFooterTwoImg.setImageResource(R.drawable.code_btn);
            } else if (mFooterTwoTxt.equalsIgnoreCase(getString(R.string.preview))) {

                mFooterTwoImg.setImageResource(R.drawable.preview_btn);
            } else if (mFooterTwoTxt.equalsIgnoreCase(getString(R.string.rating))) {

                mFooterTwoImg.setImageResource(R.drawable.rate_now_btn);
            }

        } else {

            mBottomOneLay.setVisibility(View.GONE);
            mBottomTwoLay.setVisibility(View.VISIBLE);

            mFooterOneLay.setVisibility(View.VISIBLE);
            mFooterTwoLay.setVisibility(View.VISIBLE);
            mFooterThreeLay.setVisibility(View.VISIBLE);

            mFooterOneBtn.setText(mFooterOneTxt);
            mFooterTwoBtn.setText(mFooterTwoTxt);
            mFooterThreeBtn.setText(mFooterThreeTxt);

            if (mFooterOneTxt.equalsIgnoreCase(getString(R.string.chat))) {
                mFooterOneImg.setImageResource(R.drawable.chat_btn);
            }

            //Button Two Bg
            if (mFooterTwoTxt.equalsIgnoreCase(getString(R.string.finish))) {
                mFooterTwoImg.setImageResource(R.drawable.finish_btn);
            } else if (mFooterTwoTxt.equalsIgnoreCase(getString(R.string.start)) || mFooterThreeTxt.equalsIgnoreCase(getString(R.string.started))) {

                mFooterTwoImg.setImageResource(R.drawable.start_btn);
            } else if (mFooterTwoTxt.equalsIgnoreCase(getString(R.string.upload_txt))) {

                mFooterTwoImg.setImageResource(R.drawable.upload_btn);
            } else if (mFooterTwoTxt.equalsIgnoreCase(getString(R.string.code))) {

                mFooterTwoImg.setImageResource(R.drawable.code_btn);
            } else if (mFooterTwoTxt.equalsIgnoreCase(getString(R.string.preview))) {

                mFooterTwoImg.setImageResource(R.drawable.preview_btn);
            }

            //Button Three Bg
            if (mFooterThreeTxt.equalsIgnoreCase(getString(R.string.unsatis))) {
                mFooterThreeImg.setImageResource(R.drawable.unsatis_btn);
            } else if (mFooterThreeTxt.equalsIgnoreCase(getString(R.string.start)) || mFooterThreeTxt.equalsIgnoreCase(getString(R.string.started))) {

                mFooterThreeImg.setImageResource(R.drawable.start_btn);
            } else if (mFooterThreeTxt.equalsIgnoreCase(getString(R.string.approve))) {

                mFooterThreeImg.setImageResource(R.drawable.approve_btn);
            }
        }
        if (mFooterTwoBtn.getText().toString().trim().equalsIgnoreCase(getString(R
                .string.start))) {

            if (mHomeSingleItemEntity.getFinish_service() != null && mHomeSingleItemEntity.getFinish_service().equalsIgnoreCase(getString(R.string.one))) {
                mFooterTwoBtn.setText(getString(R
                        .string.started));
                mFooterTwoBtn.setClickable(false);
            }
        }
        if (mFooterThreeBtn.getText().toString().trim().equalsIgnoreCase(getString(R
                .string.approve))) {
            if (mHomeSingleItemEntity.getIs_approve() != null && mHomeSingleItemEntity.getIs_approve().equalsIgnoreCase(getString(R.string.one))) {
                mFooterThreeBtn.setText(getString(R.string.approved));
            }
        }

        mRatingBar = (RatingBar) findViewById(R.id.fav_ratingbar);
        mVerifyImg = (ImageView) findViewById(R.id.verify_img);
        mPersonNameTxt = (TextView) findViewById(R.id.person_name_txt);
        mPersonUnivNameTxt = (TextView) findViewById(R.id.person_univ_name_txt);
        mSlidepointerOne = (ImageView) findViewById(R.id.slidepointer_one);
        mSlidepointerTwo = (ImageView) findViewById(R.id.slidepointer_two);
        mSlidepointerThree = (ImageView) findViewById(R.id.slidepointer_three);


        mItemNameTxt = (TextView) findViewById(R.id.item_name_txt);
        mItemQuantityTxt = (TextView) findViewById(R.id.item_quantity_txt);
        mItemPriceTxt = (TextView) findViewById(R.id.item_price_txt);
        mItemServicesPriceTxt = (TextView) findViewById(R.id.item_ser_price_txt);
        mItemTotalPriceTxt = (TextView) findViewById(R.id.item_tot_price_txt);
        mItemCategoryTxt = (TextView) findViewById(R.id.item_category_txt);
        mItemDeliveryTypeTxt = (TextView) findViewById(R.id.item_delv_txt);
        mItemConditionTxt = (TextView) findViewById(R.id.item_condition_txt);
        mItemDescTxt = (TextView) findViewById(R.id.item_desc_txt);
        mTipsTxt = (TextView) findViewById(R.id.tips_txt);
        mItemWebTxt = (TextView) findViewById(R.id.web_txt);
        mItemPhTxt = (TextView) findViewById(R.id.ph_num_txt);
        mPartWebPhLay = (LinearLayout) findViewById(R.id.part_web_ph_lay);

        bgShapeGreen = (GradientDrawable) getResources().getDrawable(R.drawable.round);
        bgShapeWhite = (GradientDrawable) getResources().getDrawable(R.drawable.round);

        bgShapeGreen.setColor(getResources().getColor(R.color.green));
        bgShapeWhite.setColor(getResources().getColor(R.color.white));

        mCertPartLay = (LinearLayout) findViewById(R.id.cert_part_lay);
        //Set Data
        mHeadeLeftBtnLay.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.details).toUpperCase(Locale.ENGLISH));
        populateData();

        mImagePager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {

            }

            @Override
            public void onPageScrolled(int mCurrentPos, float arg1, int arg2) {

                mSlidepointerOne.setBackground(mCurrentPos == 0 ? bgShapeGreen : bgShapeWhite);
                mSlidepointerTwo.setBackground(mCurrentPos == 1 ? bgShapeGreen : bgShapeWhite);
                mSlidepointerThree.setBackground(mCurrentPos == 2 ? bgShapeGreen : bgShapeWhite);
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (mFooterBtnCount > 1 && !mFooterTwoTxt.equalsIgnoreCase(getString(R.string.rating_c))) {

        initComponents();
        if (mFooterBtnCount > 1 || (mFooterBtnCount == 1 && mFooterOneTxt.equalsIgnoreCase(getString(R.string.rating)))) {
            APIRequestHandler.getInstance().getItemDetailsResponse(mHomeSingleItemEntity.getItem_id(), mHomeSingleItemEntity.getBuyer_id(), mHomeSingleItemEntity.getPayment_id(), "", this);
        }

    }

    private void setViewPageIndicater(int pageCount) {
        if (pageCount > 0) {
            if (pageCount == 1) {

                mSlidepointerOne.setVisibility(View.VISIBLE);
                mSlidepointerTwo.setVisibility(View.GONE);
                mSlidepointerThree.setVisibility(View.GONE);

            } else if (pageCount == 2) {

                mSlidepointerOne.setVisibility(View.VISIBLE);
                mSlidepointerTwo.setVisibility(View.VISIBLE);
                mSlidepointerThree.setVisibility(View.GONE);
            } else {

                mSlidepointerOne.setVisibility(View.VISIBLE);
                mSlidepointerTwo.setVisibility(View.VISIBLE);
                mSlidepointerThree.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);


        if (responseObj instanceof PayForItemResponse) {
            PayForItemResponse logoutres = (PayForItemResponse) responseObj;

            if (logoutres.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                BuyerCodeScreen.mBuyerName =
                        mHomeSingleItemEntity.getOwner_first_name();

                BuyerCodeScreen.mPayID = mHomeSingleItemEntity.getPayment_id();
                BuyerCodeScreen.mItemID = mHomeSingleItemEntity.getItem_id();


                ChatScreen.mPaymentId = mHomeSingleItemEntity.getPayment_id();

                AppConstants.CODE_SCREEN = AppConstants.HOME_SCREEN;
                AppConstants.CHAT_FRIEND_ID = mHomeSingleItemEntity.getUser_id();
                AppConstants.CHAT_ITEM_ID = mHomeSingleItemEntity.getItem_id();

                nextScreen(BuyerCodeScreen.class, true);


                AppConstants.RATING_BTN = AppConstants.FAILURE_CODE;
                nextScreen(BuyerCodeScreen.class, false);

            } else {
                DialogManager.showBasicAlertDialog(this,
                        logoutres.getMessage(), new DialogMangerOkCallback() {
                            @Override
                            public void onOkClick() {

                            }
                        });
            }
        } else if (responseObj instanceof CommonResponse) {
            CommonResponse commonResponse = (CommonResponse) responseObj;


            if (commonResponse.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                if (mFooterTwoBtn.getText().toString().trim().equalsIgnoreCase(getString(R
                        .string.start))) {
                    mFooterTwoBtn.setText(getString(R.string.started));
                    mFooterTwoBtn.setClickable(false);
                }
                DialogManager.showBasicAlertDialog(ProductDetailsScreen.this,
                        commonResponse.getMessage(), new DialogMangerOkCallback() {
                            @Override
                            public void onOkClick() {
                                if (!mFooterTwoBtn.getText().toString().trim().equalsIgnoreCase(getString(R
                                        .string.started))) {
                                    nextScreen(HomeScreenActivity.class, true);
                                }

                            }
                        });
            } else if (commonResponse.getResponse_code().equalsIgnoreCase(getString(R.string.two))) {
                DialogManager.showBasicAlertDialog(this,
                        commonResponse.getMessage(), new DialogMangerOkCallback() {
                            @Override
                            public void onOkClick() {
                                previousScreen(HomeScreenActivity.class, true);
                            }
                        });
            } else {
                DialogManager.showBasicAlertDialog(this,
                        commonResponse.getMessage(), new DialogMangerOkCallback() {
                            @Override
                            public void onOkClick() {

                            }
                        });
            }
        } else if (responseObj instanceof FinishServicesResponse) {
            FinishServicesResponse commonResponse = (FinishServicesResponse) responseObj;

            if (commonResponse.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                AppConstants.BANK_ACC_DET_BACK_SCREEN = AppConstants.FAILURE_CODE;

                if (mFooterThreeBtn.getText().toString().trim().equalsIgnoreCase(getString(R
                        .string.approve))) {

                    mFooterThreeBtn.setText(getString(R.string.approved));
//                mFooterThreeBtn.setClickable(false);
                }
                DialogManager.showBasicAlertDialog(ProductDetailsScreen.this,
                        commonResponse.getMessage(), new DialogMangerOkCallback() {
                            @Override
                            public void onOkClick() {


                                if (mFooterThreeBtn.getText().toString().trim().equalsIgnoreCase(getString(R
                                        .string.approved))) {
//                                nextScreen(HomeScreenActivity.class, true);
                                } else {
                                    UserName = mHomeSingleItemEntity.getBuyer_first_name();
                                    RateBuySellScreen.mHeader = getString(R.string.ratebuyer);
                                    AppConstants.RATING_ITEM_ID = mHomeSingleItemEntity.getItem_id();
                                    AppConstants.RATING_USER_ID = mHomeSingleItemEntity.getBuyer_id();
                                    AppConstants.RATING_USER_NAME = UserName;

                                    AppConstants.RATING_RATE = mHomeSingleItemEntity.getItem_buyer_rating();
                                    AppConstants.RATING_CMD = mHomeSingleItemEntity.getItem_buyer_comments();
                                    nextScreen(RateBuySellScreen.class, false);

                                }
                            }
                        });
            } else if (commonResponse.getResponse_code().equalsIgnoreCase(getString(R.string.two))) {
                AppConstants.BANK_ACC_DET_BACK_SCREEN = getString(R.string.three);
                nextScreen(PaymentHomeScreen.class, false);
            } else if (commonResponse.getResponse_code().equalsIgnoreCase(getString(R.string.three))) {
                DialogManager.showBasicAlertDialog(this,
                        commonResponse.getMessage(), new DialogMangerOkCallback() {
                            @Override
                            public void onOkClick() {
                                previousScreen(HomeScreenActivity.class, true);
                            }
                        });
            } else {
                DialogManager.showBasicAlertDialog(this,
                        commonResponse.getMessage(), new DialogMangerOkCallback() {
                            @Override
                            public void onOkClick() {

                            }
                        });
            }

        } else if (responseObj instanceof ItemEditableResponse) {
            ItemEditableResponse itemres = (ItemEditableResponse) responseObj;

            if (itemres.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                if (itemres.getResult().getEditable().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                    DialogManager.showBasicAlertDialog(this,
                            getString(R.string.you_del), new DialogMangerOkCallback() {
                                @Override
                                public void onOkClick() {

                                }
                            });
                } else {
//                    APIRequestHandler.getInstance().getItemEditableResponse(mHomeSingleItemEntity.getItem_id(), this);

                    UploadEntityClass mUploadEntityClass = new UploadEntityClass();

                    mUploadEntityClass.isGood = getBooleanFromString(mHomeSingleItemEntity
                            .getItem_type());
                    mUploadEntityClass.isSelling = getBooleanFromString(mHomeSingleItemEntity
                            .getItem_mode());
                    mUploadEntityClass.isDeliveryInPerson = getBooleanFromString
                            (mHomeSingleItemEntity.getDelivery_type());

                    mUploadEntityClass.category = mHomeSingleItemEntity.getCategory_name();
                    mUploadEntityClass.name = mHomeSingleItemEntity.getItem_name();
                    mUploadEntityClass.price = mHomeSingleItemEntity.getItem_cost();
                    mUploadEntityClass.quantity = mHomeSingleItemEntity.getItem_quantity();
                    mUploadEntityClass.condition = mHomeSingleItemEntity.getItem_condition();
                    mUploadEntityClass.desc = mHomeSingleItemEntity.getItem_description();
                    mUploadEntityClass.imagePath1 = mHomeSingleItemEntity.getPicture1();
                    mUploadEntityClass.imagePath2 = mHomeSingleItemEntity.getPicture2();
                    mUploadEntityClass.imagePath3 = mHomeSingleItemEntity.getPicture3();
                    mUploadEntityClass.dropBoxUrlPreview = mHomeSingleItemEntity.getPreview_url();
                    mUploadEntityClass.dropBoxUrlOrginal = mHomeSingleItemEntity.getOriginal_url();
                    mUploadEntityClass.isDropBoxUrl = getBooleanFromString
                            (mHomeSingleItemEntity.getFile_url_type());
                    mUploadEntityClass.itemId = mHomeSingleItemEntity.getItem_id();
                    mUploadEntityClass.event_date_time = mHomeSingleItemEntity.getEvent_date_time();
                    mUploadEntityClass.venue = mHomeSingleItemEntity.getVenue();
                    mUploadEntityClass.phone_number = mHomeSingleItemEntity.getPhone_number();
                    mUploadEntityClass.website = mHomeSingleItemEntity.getWebsite();

                    AppConstants.UPLOAD_DATA = new Gson().toJson(mUploadEntityClass);
                    UploadScreen.mEntity = mUploadEntityClass;
                    nextScreen(UpdateItemScreen.class, false);
                }


            } else {
                DialogManager.showBasicAlertDialog(this,
                        itemres.getMessage(), new DialogMangerOkCallback() {
                            @Override
                            public void onOkClick() {

                            }
                        });
            }
        }
        if (responseObj instanceof ItemDetailResponse) {
            ItemDetailResponse itemRes = (ItemDetailResponse) responseObj;
            if (itemRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

//                if (itemRes.getResult().getComplete().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
//                    mFooterOneTxt = getString(R.string.chat_c);
//                    mFooterTwoTxt = getString(R.string.rating_c);
//                    mFooterBtnCount = 2;
//                    mFooterOneBtn.setText(mFooterOneTxt);
//                    mFooterTwoBtn.setText(mFooterTwoTxt);
//                }
                if (itemRes.getResult().getComplete().equalsIgnoreCase(getString(R.string.zero))) {
                    if (itemRes.getResult().getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller
                        sellBuyApprovList(itemRes.getResult());
                    } else {
                        //I am Buyer
                        buyOrderList(itemRes.getResult());

                    }
                } else {
                    mFooterBtnCount = 2;
                    mFooterOneTxt = getString(R.string.chat);
                    mFooterTwoTxt = getString(R.string.rating);

                    if (itemRes.getResult().getItem_type().equalsIgnoreCase(getString(R.string.one)) && itemRes.getResult().getDelivery_type().equalsIgnoreCase(getString(R.string.two))) {
                        mFooterBtnCount = 1;
                        mFooterOneTxt = getString(R.string.rating);
                    }

                }

                mHomeSingleItemEntity = itemRes.getResult();
                initComponents();
//                mItemDetailsRes = itemRes.getResult();
//                movetoNextPage();
            }
        }
    }


    private void populateData() {
        String personName = "", personUnivName = "", otherpersonID = "", itemName = "", itemQuantity = "", itemPrice = "", itemServiceCharge = "", itemTotalPrice = "", itemCategory = "", itemDeliveryType = "", itemCondition = "", conditionType = "", itemDesc = "", itemPh = "", itemWeb = "";
        float personRating = 0.0f;
        boolean ispersonVerified = false, iscertPart = false;

        itemName = GlobalMethods.isGoodsService(this, mHomeSingleItemEntity.getItem_type()) + " : " + mHomeSingleItemEntity.getItem_name();
//        itemName = getString(R.string.item_name) + " " + mHomeSingleItemEntity.getItem_name();
        itemCategory = getString(R.string.category) + " " + mHomeSingleItemEntity.getCategory_name();
        itemDeliveryType = getString(R.string.delivery_type) + " " + GlobalMethods.getDeliveryType(this, mHomeSingleItemEntity.getDelivery_type());
        conditionType = (mHomeSingleItemEntity.getItem_mode().equalsIgnoreCase(getString(R.string.one)) && mHomeSingleItemEntity.getItem_type().equalsIgnoreCase(getString(R.string.two))) ? getString(R.string.skill_level) : getString(R.string.condition);
        itemCondition = conditionType + " " + mHomeSingleItemEntity.getItem_condition();
        itemDesc = getString(R.string.desc) + " " + mHomeSingleItemEntity.getItem_description();


        if (mHomeSingleItemEntity.getComplete() != null && mHomeSingleItemEntity.getComplete().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

            //Ticket History
            if (mHomeSingleItemEntity.getItem_mode().equalsIgnoreCase(getString(R.string.one)) && mHomeSingleItemEntity.getItem_type().equalsIgnoreCase(getString(R.string.one)) && mHomeSingleItemEntity.getDelivery_type().equalsIgnoreCase(getString(R.string.two)) && mHomeSingleItemEntity.getCategory_name().equalsIgnoreCase(getString(R.string.ticket))) {

                itemName = getString(R.string.event_name) + " " + mHomeSingleItemEntity.getItem_name();
                itemQuantity = getString(R.string.event_date) + " " + GlobalMethods.gettwoDateFormate(mHomeSingleItemEntity.getEvent_date_time(), mServerTimeForm, mTargetDateTime);
                itemPrice = getString(R.string.event_venue) + " " + mHomeSingleItemEntity.getVenue();

                if (GlobalMethods.isSeller(this, mHomeSingleItemEntity.getUser_id())) {
                    //I am Seller
                    personName = mHomeSingleItemEntity.getBuyer_first_name();
                    personUnivName = getString(R.string.univ_name) + " " + mHomeSingleItemEntity.getBuyer_university_name();
                    otherpersonID = mHomeSingleItemEntity.getBuyer_id();
                    personRating = GlobalMethods.isRating(mHomeSingleItemEntity.getBuyer_rating());
                    ispersonVerified = GlobalMethods.isUserVerified(this, mHomeSingleItemEntity.getBuyer_verified());
//                    iscertPart = GlobalMethods.isCertifiedPartner(this, mHomeSingleItemEntity.getItem_type(), mHomeSingleItemEntity.getBuyer_partner());
                    iscertPart = false;
                    itemServiceCharge = getString(R.string.tickets_sold) + " " + mHomeSingleItemEntity.getPurchase_quantity();

                    itemTotalPrice = getString(R.string.price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getAmount_received(), true);
                    itemCategory = getString(R.string.service_price) + "  $ " + GlobalMethods.afterTwoPointVal(mHomeSingleItemEntity.getProcessing_fee());
                    itemDeliveryType = getString(R.string.sold_price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getAmount_received(), true);

                } else {
                    // I am Buyer
                    personName = mHomeSingleItemEntity.getOwner_first_name();
                    personUnivName = getString(R.string.univ_name) + " " + mHomeSingleItemEntity.getOwner_university_name();
                    otherpersonID = mHomeSingleItemEntity.getUser_id();
                    personRating = GlobalMethods.isRating(mHomeSingleItemEntity.getOwner_rating());
                    ispersonVerified = GlobalMethods.isUserVerified(this, mHomeSingleItemEntity.getOwner_verified());

                    iscertPart = GlobalMethods.isCertifiedPartner(this, mHomeSingleItemEntity.getItem_type(), mHomeSingleItemEntity.getSeller_partner());
                    itemServiceCharge = getString(R.string.no_tic) + " " + mHomeSingleItemEntity.getPurchase_quantity();

                    if (mHomeSingleItemEntity.getTips() != null && !mHomeSingleItemEntity.getTips().equalsIgnoreCase("") && !GlobalMethods.afterTwoPointVal(mHomeSingleItemEntity.getTips()).equalsIgnoreCase(getString(R.string.zero))) {
                        double itmpric = (Double.valueOf(mHomeSingleItemEntity.getProcessing_fee()) + Double.valueOf(mHomeSingleItemEntity.getTips()));
                        itemTotalPrice = getString(R.string.price) + "  $ " + GlobalMethods.afterTwoPointVal(String.valueOf(Double.valueOf(mHomeSingleItemEntity.getAmount_received()) - itmpric));
                    } else {
                        itemTotalPrice = getString(R.string.price) + "  $ " + GlobalMethods.afterTwoPointVal(String.valueOf(Double.valueOf(mHomeSingleItemEntity.getAmount_received()) - Double.valueOf(mHomeSingleItemEntity.getProcessing_fee())));
                    }


//                    itemTotalPrice = getString(R.string.price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(String.valueOf(Double.valueOf(mHomeSingleItemEntity.getAmount_received()) - (Double.valueOf(mHomeSingleItemEntity.getProcessing_fee()))));
                    itemCategory = getString(R.string.commission) + "  $ " + GlobalMethods.afterTwoPointVal(mHomeSingleItemEntity.getProcessing_fee());
                    itemDeliveryType = getString(R.string.amt_paid) + "  $ " + GlobalMethods.afterTwoPointVal(mHomeSingleItemEntity.getAmount_received());
                }

            } else {
                //Others History
                if (GlobalMethods.isSeller(this, mHomeSingleItemEntity.getUser_id())) {
                    //I am Seller
                    personName = mHomeSingleItemEntity.getBuyer_first_name();
                    personUnivName = getString(R.string.univ_name) + " " + mHomeSingleItemEntity.getBuyer_university_name();
                    personRating = GlobalMethods.isRating(mHomeSingleItemEntity.getBuyer_rating());
                    otherpersonID = mHomeSingleItemEntity.getBuyer_id();
                    itemQuantity = getString(R.string.no_qut) + " " + mHomeSingleItemEntity.getPurchase_quantity();
                    ispersonVerified = GlobalMethods.isUserVerified(this, mHomeSingleItemEntity.getBuyer_verified());
//                    iscertPart = GlobalMethods.isCertifiedPartner(this, mHomeSingleItemEntity.getItem_type(), mHomeSingleItemEntity.getBuyer_partner());
                    iscertPart = false;
//                    if(!GlobalMethods.afterTwoPointVal(mHomeSingleItemEntity.getAmount_received()).equalsIgnoreCase(getString(R.string.zero))){
//
//                        itemPrice = getString(R.string.price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getAmount_received());
//                        itemServiceCharge = getString(R.string.service_price) + "  $ " + GlobalMethods.afterTwoPointVal(mHomeSingleItemEntity.getAmount_received());
//                        itemTotalPrice = getString(R.string.sold_price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getAmount_received());
//                    }else{
                    itemPrice = getString(R.string.price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getAmount_received(), true);
                    itemServiceCharge = getString(R.string.service_price) + "  $ " + GlobalMethods.afterTwoPointVal(mHomeSingleItemEntity.getAmount_received());
                    itemTotalPrice = getString(R.string.sold_price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getAmount_received(), true);
//                    }


                } else {
                    //I am Buyer
                    personName = mHomeSingleItemEntity.getOwner_first_name();
                    personUnivName = getString(R.string.univ_name) + " " + mHomeSingleItemEntity.getOwner_university_name();
                    personRating = GlobalMethods.isRating(mHomeSingleItemEntity.getOwner_rating());
                    otherpersonID = mHomeSingleItemEntity.getUser_id();
                    itemQuantity = getString(R.string.no_qut) + " " + mHomeSingleItemEntity.getPurchase_quantity();
                    ispersonVerified = GlobalMethods.isUserVerified(this, mHomeSingleItemEntity.getOwner_verified());
                    iscertPart = GlobalMethods.isCertifiedPartner(this, mHomeSingleItemEntity.getItem_type(), mHomeSingleItemEntity.getBuyer_partner());


                    if (!GlobalMethods.afterTwoPointVal(mHomeSingleItemEntity.getAmount_received()).equalsIgnoreCase(GlobalMethods.afterTwoPointVal(getString(R.string.zero)))) {
                        if (mHomeSingleItemEntity.getTips() != null && !mHomeSingleItemEntity.getTips().equalsIgnoreCase("") && !GlobalMethods.afterTwoPointVal(mHomeSingleItemEntity.getTips()).equalsIgnoreCase(GlobalMethods.afterTwoPointVal(getString(R.string.zero)))) {
                            double itmpric = (Double.valueOf(mHomeSingleItemEntity.getProcessing_fee()) + Double.valueOf(mHomeSingleItemEntity.getTips()));
                            itemPrice = getString(R.string.price) + "  $ " + GlobalMethods.afterTwoPointVal(String.valueOf(Double.valueOf(mHomeSingleItemEntity.getAmount_received()) - itmpric));
                        } else {
                            itemPrice = getString(R.string.price) + "  $ " + GlobalMethods.afterTwoPointVal(String.valueOf(Double.valueOf(mHomeSingleItemEntity.getAmount_received()) - Double.valueOf(mHomeSingleItemEntity.getProcessing_fee())));
                        }


//                        itemPrice = getString(R.string.price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(String.valueOf(Double.valueOf(mHomeSingleItemEntity.getAmount_received()) - (Double.valueOf(mHomeSingleItemEntity.getProcessing_fee()))));
                        itemServiceCharge = getString(R.string.service_price) + "  $ " + GlobalMethods.afterTwoPointVal(mHomeSingleItemEntity.getProcessing_fee());
                        itemTotalPrice = getString(R.string.amt_paid) + "  $ " + GlobalMethods.afterTwoPointVal(mHomeSingleItemEntity.getAmount_received());
                    } else {

//                        if (mHomeSingleItemEntity.getTips() != null && !mHomeSingleItemEntity.getTips().equalsIgnoreCase("") && !GlobalMethods.afterTwoPointVal(mHomeSingleItemEntity.getTips()).equalsIgnoreCase(getString(R.string.zero))) {
//
//
//                            double itmpric=(Double.valueOf(mHomeSingleItemEntity.getProcessing_fee()) + Double.valueOf(mHomeSingleItemEntity.getTips()));
//                            itemPrice = getString(R.string.price) + "  $ " + GlobalMethods.afterTwoPointVal(String.valueOf(Double.valueOf(mHomeSingleItemEntity.getAmount_received()) - itmpric));
//                        } else {
//                            itemPrice = getString(R.string.price) + "  $ " + GlobalMethods.afterTwoPointVal(String.valueOf(Double.valueOf(mHomeSingleItemEntity.getAmount_received()) - Double.valueOf(mHomeSingleItemEntity.getProcessing_fee())));
//                        }


                        itemPrice = getString(R.string.price) + "  $ " + GlobalMethods.afterTwoPointVal(mHomeSingleItemEntity.getAmount_received());
                        itemServiceCharge = getString(R.string.service_price) + "  $ " + GlobalMethods.afterTwoPointVal(mHomeSingleItemEntity.getAmount_received());
                        itemTotalPrice = getString(R.string.amt_paid) + "  $ " + GlobalMethods.afterTwoPointVal(mHomeSingleItemEntity.getAmount_received());

                    }
                }
            }


            //Others item text Visible
            mItemTotalPriceTxt.setVisibility(View.VISIBLE);
            mItemServicesPriceTxt.setVisibility(View.VISIBLE);
            if (GlobalMethods.isSeller(this, mHomeSingleItemEntity.getUser_id())) {
                //I am Seller so no need Service price
                mItemServicesPriceTxt.setVisibility(View.GONE);
            }

            //Ticket text Visible
            if (mHomeSingleItemEntity.getItem_mode().equalsIgnoreCase(getString(R.string.one)) && mHomeSingleItemEntity.getItem_type().equalsIgnoreCase(getString(R.string.one)) && mHomeSingleItemEntity.getDelivery_type().equalsIgnoreCase(getString(R.string.two)) && mHomeSingleItemEntity.getCategory_name().equalsIgnoreCase(getString(R.string.ticket))) {

                if (GlobalMethods.isSeller(ProductDetailsScreen.this, mHomeSingleItemEntity.getUser_id())) {

                    mItemCategoryTxt.setVisibility(View.GONE);
                } else {
                    mItemCategoryTxt.setVisibility(View.VISIBLE);
                }
                mItemConditionTxt.setVisibility(View.GONE);
                mItemDeliveryTypeTxt.setVisibility(View.VISIBLE);
                mItemServicesPriceTxt.setVisibility(View.VISIBLE);
            }
        } else {

            if (mFooterBtnCount < 2) {
                //Items in New and Neg
                if (GlobalMethods.isSeller(this, mHomeSingleItemEntity.getUser_id())) {
                    // I am Seller
                    personName = getString(R.string.your_item);
                    personUnivName = getString(R.string.univ_name) + " " + GlobalMethods.getStringValue(this, AppConstants.USER_UNIVERSITY_NAME);
                    otherpersonID = GlobalMethods.getUserID(this);
                    personRating = GlobalMethods.isRating(mHomeSingleItemEntity.getOwner_rating());
                    ispersonVerified = GlobalMethods.isUserVerified(this, mHomeSingleItemEntity.getOwner_verified());
//                    iscertPart = GlobalMethods.isCertifiedPartner(this, mHomeSingleItemEntity.getItem_type(), mHomeSingleItemEntity.getSeller_partner());
                    iscertPart = false;
                } else {
                    // I am Buyer
                    personName = mHomeSingleItemEntity.getBuyer_first_name();
                    personUnivName = getString(R.string.univ_name) + " " + mHomeSingleItemEntity.getBuyer_university_name();
                    otherpersonID = mHomeSingleItemEntity.getBuyer_id();
                    personRating = GlobalMethods.isRating(mHomeSingleItemEntity.getBuyer_rating());
                    ispersonVerified = GlobalMethods.isUserVerified(this, mHomeSingleItemEntity.getBuyer_verified());

                    iscertPart = GlobalMethods.isCertifiedPartner(this, mHomeSingleItemEntity.getItem_type(), mHomeSingleItemEntity.getBuyer_partner());
                }

                if (mFooterOneTxt.equalsIgnoreCase(getString(R.string.negotiation)) && mFooterBtnCount == 1) {
                    //Item in Neg
                    if (GlobalMethods.isSeller(this, mHomeSingleItemEntity.getUser_id())) {
                        //I am Seller
                        personName = mHomeSingleItemEntity.getBuyer_first_name();
                        personUnivName = getString(R.string.univ_name) + " " + mHomeSingleItemEntity.getBuyer_university_name();
                        otherpersonID = mHomeSingleItemEntity.getBuyer_id();
                        personRating = GlobalMethods.isRating(mHomeSingleItemEntity.getBuyer_rating());
                        ispersonVerified = GlobalMethods.isUserVerified(this, mHomeSingleItemEntity.getBuyer_verified());

//                        iscertPart = GlobalMethods.isCertifiedPartner(this, mHomeSingleItemEntity.getItem_type(), mHomeSingleItemEntity.getBuyer_partner());
                        iscertPart = false;
                    } else {
                        //I am Buyer
                        personName = mHomeSingleItemEntity.getOwner_first_name();
                        personUnivName = getString(R.string.univ_name) + " " + mHomeSingleItemEntity.getOwner_university_name();
                        otherpersonID = mHomeSingleItemEntity.getUser_id();
                        personRating = GlobalMethods.isRating(mHomeSingleItemEntity.getOwner_rating());
                        ispersonVerified = GlobalMethods.isUserVerified(this, mHomeSingleItemEntity.getOwner_verified());

                        iscertPart = GlobalMethods.isCertifiedPartner(this, mHomeSingleItemEntity.getItem_type(), mHomeSingleItemEntity.getSeller_partner());
                    }
                    itemServiceCharge = getString(R.string.neg_price) + "  $ " + GlobalMethods.afterTwoPointVal(mHomeSingleItemEntity.getNegotiate_cost());
                    mItemServicesPriceTxt.setVisibility(View.VISIBLE);
                }
                if (mHomeSingleItemEntity.getItem_mode().equalsIgnoreCase(getString(R.string.one)) && mHomeSingleItemEntity.getItem_type().equalsIgnoreCase(getString(R.string.one)) && mHomeSingleItemEntity.getDelivery_type().equalsIgnoreCase(getString(R.string.two)) && mHomeSingleItemEntity.getCategory_name().equalsIgnoreCase(getString(R.string.ticket))) {
                    //Tickets In NEW
                    itemName = getString(R.string.event_name) + " " + mHomeSingleItemEntity.getItem_name();
                    itemQuantity = getString(R.string.event_venue) + " " + mHomeSingleItemEntity.getVenue();
                    itemPrice = getString(R.string.event_date) + " " + GlobalMethods.gettwoDateFormate(mHomeSingleItemEntity.getEvent_date_time(), mServerTimeForm, mTargetDateTime);
                    itemServiceCharge = getString(R.string.tick_price) + " :  $ " + GlobalMethods
                            .getPriValWithTwoPoint(mHomeSingleItemEntity.getItem_cost(), true);
                    itemCategory = getString(R.string.no_tic) + " " + mHomeSingleItemEntity.getItem_quantity();
                    itemDeliveryType = getString(R.string.category) + " " + mHomeSingleItemEntity.getCategory_name();
                    iscertPart = false;

                    mItemConditionTxt.setVisibility(View.GONE);
                    mItemServicesPriceTxt.setVisibility(View.VISIBLE);
                } else {
                    //Other Items In NEW
                    itemQuantity = getString(R.string.no_qut) + " " + mHomeSingleItemEntity.getItem_quantity();
                    itemPrice = getString(R.string.price) + "  $ " + GlobalMethods.afterTwoPointVal(mHomeSingleItemEntity.getItem_cost());
                }


                if (GlobalMethods.isCertifiedPartner(this, mHomeSingleItemEntity.getItem_type(), mHomeSingleItemEntity.getSeller_partner()) && mHomeSingleItemEntity.getItem_type().equalsIgnoreCase(getString(R.string.two))) {
                    mPartWebPhLay.setVisibility(View.VISIBLE);
                    itemPh = mHomeSingleItemEntity.getPhone_number();
                    itemWeb = mHomeSingleItemEntity.getWebsite();
                }


            } else {
                //Items in Buyer Approved List and My Buying List (Sell and Buyer Side)
                if (GlobalMethods.isSeller(this, mHomeSingleItemEntity.getUser_id())) {
                    //I am Seller
                    personName = mHomeSingleItemEntity.getBuyer_first_name();
                    personUnivName = getString(R.string.univ_name) + " " + mHomeSingleItemEntity.getBuyer_university_name();
                    personRating = GlobalMethods.isRating(mHomeSingleItemEntity.getBuyer_rating());
                    otherpersonID = mHomeSingleItemEntity.getBuyer_id();
                    itemQuantity = getString(R.string.no_qut) + " " + mHomeSingleItemEntity.getPurchase_quantity();
                    ispersonVerified = GlobalMethods.isUserVerified(this, mHomeSingleItemEntity.getBuyer_verified());

                    iscertPart = GlobalMethods.isCertifiedPartner(this, mHomeSingleItemEntity.getItem_type(), mHomeSingleItemEntity.getBuyer_partner());

                    itemPrice = getString(R.string.sold_price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getAmount_received(), true);
                    itemServiceCharge = getString(R.string.price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getAmount_received(), true);
                    itemTotalPrice = getString(R.string.sold_price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getAmount_received(), true);

                    mItemTotalPriceTxt.setVisibility(View.GONE);
                    mItemServicesPriceTxt.setVisibility(View.GONE);
                } else {
                    //I am Buyer
                    personName = mHomeSingleItemEntity.getOwner_first_name();
                    personUnivName = getString(R.string.univ_name) + " " + mHomeSingleItemEntity.getOwner_university_name();
                    personRating = GlobalMethods.isRating(mHomeSingleItemEntity.getOwner_rating());
                    otherpersonID = mHomeSingleItemEntity.getUser_id();
                    itemQuantity = getString(R.string.no_qut) + " " + mHomeSingleItemEntity.getPurchase_quantity();
                    ispersonVerified = GlobalMethods.isUserVerified(this, mHomeSingleItemEntity.getOwner_verified());

                    iscertPart = GlobalMethods.isCertifiedPartner(this, mHomeSingleItemEntity.getItem_type(), mHomeSingleItemEntity.getSeller_partner());

//                    itemPrice = getString(R.string.amt_paid) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getAmount_received());
//                    itemServiceCharge = getString(R.string.service_price) + "  $ " + GlobalMethods.afterTwoPointVal(mHomeSingleItemEntity.getProcessing_fee());
//                    itemTotalPrice = getString(R.string.amt_paid) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getAmount_received());


                    if (mHomeSingleItemEntity.getTips() != null && !mHomeSingleItemEntity.getTips().equalsIgnoreCase("") && !GlobalMethods.afterTwoPointVal(mHomeSingleItemEntity.getTips()).equalsIgnoreCase(getString(R.string.zero))) {


                        double itmpric = (Double.valueOf(mHomeSingleItemEntity.getProcessing_fee()) + Double.valueOf(mHomeSingleItemEntity.getTips()));
                        itemPrice = getString(R.string.price) + "  $ " + GlobalMethods.afterTwoPointVal(String.valueOf(Double.valueOf(mHomeSingleItemEntity.getAmount_received()) - itmpric));
                    } else {
                        itemPrice = getString(R.string.price) + "  $ " + GlobalMethods.afterTwoPointVal(String.valueOf(Double.valueOf(mHomeSingleItemEntity.getAmount_received()) - Double.valueOf(mHomeSingleItemEntity.getProcessing_fee())));
                    }


                    itemServiceCharge = getString(R.string.service_price) + "  $ " + GlobalMethods.afterTwoPointVal(mHomeSingleItemEntity.getProcessing_fee());
                    itemTotalPrice = getString(R.string.amt_paid) + "  $ " + GlobalMethods.afterTwoPointVal(mHomeSingleItemEntity.getAmount_received());

                    mItemTotalPriceTxt.setVisibility(View.VISIBLE);
                    mItemServicesPriceTxt.setVisibility(View.VISIBLE);

                }

                mItemPriceTxt.setVisibility(View.VISIBLE);
            }

        }


        //Data Set
        mRatingBar.setRating(personRating);

        mPersonNameTxt.setText(personName);
        mPersonUnivNameTxt.setText(personUnivName);
        mItemNameTxt.setText(itemName);
        mItemQuantityTxt.setText(itemQuantity);
        mItemPriceTxt.setText(itemPrice);
        mItemServicesPriceTxt.setText(itemServiceCharge);
        mItemTotalPriceTxt.setText(itemTotalPrice);
        mItemCategoryTxt.setText(itemCategory);
        mItemDeliveryTypeTxt.setText(itemDeliveryType);
        mItemConditionTxt.setText(itemCondition);
        mItemDescTxt.setText(itemDesc);

        mItemPhTxt.setText(itemPh);
        mItemWebTxt.setText(itemWeb);

        Paint mBlue = new Paint(Color.BLUE);
        mItemPhTxt.setPaintFlags(mBlue.UNDERLINE_TEXT_FLAG);
        mItemPhTxt.setTextColor(Color.BLUE);
        mItemWebTxt.setPaintFlags(mBlue.UNDERLINE_TEXT_FLAG);
        mItemWebTxt.setTextColor(Color.BLUE);


        if (mHomeSingleItemEntity.getTips() != null && !mHomeSingleItemEntity.getTips().equalsIgnoreCase("") && !GlobalMethods.afterTwoPointVal(mHomeSingleItemEntity.getTips()).equalsIgnoreCase(GlobalMethods.afterTwoPointVal(getString(R.string.zero)))) {
            mTipsTxt.setText(getString(R.string.tips) + " $ " + GlobalMethods.afterTwoPointVal(mHomeSingleItemEntity.getTips()));
            mTipsTxt.setVisibility(View.VISIBLE);
        }

        if (ispersonVerified) {
            mVerifyImg.setVisibility(View.VISIBLE);
        }
        if (iscertPart) {
            mCertPartLay.setVisibility(View.VISIBLE);
        }
        if (mHomeSingleItemEntity.getItem_mode().equalsIgnoreCase(getString(R.string.two))) {
            mItemConditionTxt.setVisibility(View.GONE);
        }

        if (!mHomeSingleItemEntity.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID
                (ProductDetailsScreen.this)) && mHomeSingleItemEntity.getItem_mode().equalsIgnoreCase(getString(R.string.one)) && mHomeSingleItemEntity.getItem_type().equalsIgnoreCase(getString(R.string.one)) && mHomeSingleItemEntity.getDelivery_type().equalsIgnoreCase(getString(R.string.two)) && !mHomeSingleItemEntity.getCategory_name().equalsIgnoreCase(getString(R.string.ticket))) {

            mHeadeRightBtnLay.setVisibility(View.VISIBLE);
        } else {
            mHeadeRightBtnLay.setVisibility(View.GONE);
        }
        OtherUserProfile.mOtherUSerID = otherpersonID;
        addImagePagerView();
    }


//    private void populateData() {
//        String personName = "", personUnivName = "", otherpersonID = "", itemName = "", itemQuantity = "", itemPrice = "", itemSolidPrice = "", itemTotalPrice = "", itemCategory = "", itemDeliveryType = "", itemCondition = "", conditionType = "", itemDesc = "";
//        float personRating = 0.0f;
//        boolean ispersonVerified = false, ishistory = false;
//
//        itemName = GlobalMethods.isGoodsService(this, mHomeSingleItemEntity.getItem_type()) + " : " + mHomeSingleItemEntity.getItem_name();
//        itemCategory = getString(R.string.category) + " " + mHomeSingleItemEntity.getCategory_name();
//        itemDeliveryType = getString(R.string.delivery_type) + " " + GlobalMethods.getDeliveryType(this, mHomeSingleItemEntity.getDelivery_type());
//        conditionType = (mHomeSingleItemEntity.getItem_mode().equalsIgnoreCase(getString(R.string.one)) && mHomeSingleItemEntity.getItem_type().equalsIgnoreCase(getString(R.string.two))) ? getString(R.string.skill_level) : getString(R.string.condition);
//        itemCondition = conditionType + " " + mHomeSingleItemEntity.getItem_condition();
//        itemDesc = getString(R.string.desc) + " " + mHomeSingleItemEntity.getItem_description();
//
//        System.out.println("mFooterBtnCount---" + mFooterBtnCount);
//        if (mFooterBtnCount < 2) {
//            if (GlobalMethods.isSeller(this, mHomeSingleItemEntity.getUser_id())) {
//                personName = getString(R.string.your_item);
//                personUnivName = getString(R.string.univ_name) + " " + GlobalMethods.getStringValue(this, AppConstants.USER_UNIVERSITY_NAME);
//                otherpersonID = GlobalMethods.getUserID(this);
//                personRating = GlobalMethods.isRating(mHomeSingleItemEntity.getOwner_rating());
//                ispersonVerified = GlobalMethods.isUserVerified(this, mHomeSingleItemEntity.getOwner_verified());
//            } else {
//                personName = mHomeSingleItemEntity.getBuyer_first_name();
//                personUnivName = getString(R.string.univ_name) + " " + mHomeSingleItemEntity.getBuyer_university_name();
//                otherpersonID = mHomeSingleItemEntity.getBuyer_id();
//                personRating = GlobalMethods.isRating(mHomeSingleItemEntity.getBuyer_rating());
//                ispersonVerified = GlobalMethods.isUserVerified(this, mHomeSingleItemEntity.getBuyer_verified());
//            }
//
//            if (mFooterOneTxt.equalsIgnoreCase(getString(R.string.negotiation)) || !mFooterOneTxt.equalsIgnoreCase(getString(R.string.edit_c)) && mHomeSingleItemEntity.getCategory_name().equalsIgnoreCase(getString(R.string.ticket))) {
//                if (!GlobalMethods.isSeller(this, mHomeSingleItemEntity.getUser_id())) {
//
//                    //Buyer
//                    personName = mHomeSingleItemEntity.getOwner_first_name();
//                    personUnivName = getString(R.string.univ_name) + " " + mHomeSingleItemEntity.getOwner_university_name();
//                    otherpersonID = mHomeSingleItemEntity.getUser_id();
//                    personRating = GlobalMethods.isRating(mHomeSingleItemEntity.getOwner_rating());
//                    ispersonVerified = GlobalMethods.isUserVerified(this, mHomeSingleItemEntity.getOwner_verified());
//                } else {
//                    //Seller
//                    personName = mHomeSingleItemEntity.getBuyer_first_name();
//                    personUnivName = getString(R.string.univ_name) + " " + mHomeSingleItemEntity.getBuyer_university_name();
//                    otherpersonID = mHomeSingleItemEntity.getBuyer_id();
//                    personRating = GlobalMethods.isRating(mHomeSingleItemEntity.getBuyer_rating());
//                    ispersonVerified = GlobalMethods.isUserVerified(this, mHomeSingleItemEntity.getBuyer_verified());
//                }
//                itemSolidPrice = getString(R.string.neg_price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getNegotiate_cost());
//
//            }
//            if (mHomeSingleItemEntity.getItem_mode().equalsIgnoreCase(getString(R.string.one)) && mHomeSingleItemEntity.getItem_type().equalsIgnoreCase(getString(R.string.one)) && mHomeSingleItemEntity.getDelivery_type().equalsIgnoreCase(getString(R.string.two)) && mHomeSingleItemEntity.getCategory_name().equalsIgnoreCase(getString(R.string.ticket))) {
//
//                ishistory = false;
//
//
//                itemName = getString(R.string.event_name) + " " + mHomeSingleItemEntity.getItem_name();
//                itemQuantity = getString(R.string.event_date) + " " + GlobalMethods.getCustomDateFormate(mHomeSingleItemEntity.getEvent_date_time(), mTargetDateTime);
//                itemPrice = getString(R.string.venue) + " " + mHomeSingleItemEntity.getVenue();
//                itemSolidPrice = getString(R.string.no_tic) + " " + mHomeSingleItemEntity.getItem_quantity();
//                itemTotalPrice = getString(R.string.tic_price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getItem_cost());
////                itemCategory = getString(R.string.sell_price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(String.valueOf(Double.valueOf(mHomeSingleItemEntity.getPurchase_quantity()) * Double.valueOf(mHomeSingleItemEntity.getItem_cost())));
//
//
//                itemCategory = getString(R.string.sell_price) + "  $ " + mHomeSingleItemEntity.getItem_cost();
//                itemDeliveryType = getString(R.string.category) + " " + mHomeSingleItemEntity.getCategory_name();
//                if (mFooterBtnCount == 1 && mFooterOneTxt.equalsIgnoreCase(getString(R.string.rating_c))) {
//
//                    itemSolidPrice = getString(R.string.no_tic) + " " + mHomeSingleItemEntity.getPurchase_quantity();
//                    if (GlobalMethods.isSeller(this, mHomeSingleItemEntity.getUser_id())) {
//
//
//                        personName = mHomeSingleItemEntity.getBuyer_first_name();
//
//                        personUnivName = getString(R.string.univ_name) + " " + mHomeSingleItemEntity.getBuyer_university_name();
//                        otherpersonID = mHomeSingleItemEntity.getBuyer_id();
//                        personRating = GlobalMethods.isRating(mHomeSingleItemEntity.getBuyer_rating());
//                        ispersonVerified = GlobalMethods.isUserVerified(this, mHomeSingleItemEntity.getBuyer_verified());
//
//                        itemTotalPrice = getString(R.string.tic_price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getItem_cost());
//                        itemCategory = getString(R.string.price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getItem_cost());
////                        itemCategory = getString(R.string.sell_price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(String.valueOf(Double.valueOf(mHomeSingleItemEntity.getPurchase_quantity()) * Double.valueOf(mHomeSingleItemEntity.getItem_cost())));
//                        itemDeliveryType = getString(R.string.sold_price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getAmount_received());
//
//                    } else {
//
//                        personName = mHomeSingleItemEntity.getOwner_first_name();
//
//                        personUnivName = getString(R.string.univ_name) + " " + mHomeSingleItemEntity.getOwner_university_name();
//                        otherpersonID = mHomeSingleItemEntity.getUser_id();
//                        personRating = GlobalMethods.isRating(mHomeSingleItemEntity.getOwner_rating());
//                        ispersonVerified = GlobalMethods.isUserVerified(this, mHomeSingleItemEntity.getOwner_verified());
//
//                        itemTotalPrice = getString(R.string.pur_price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getItem_cost());
//                        itemCategory = getString(R.string.service_price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getProcessing_fee());
//                        itemDeliveryType = getString(R.string.amt_paid) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getAmount_received());
//
//                    }
//
//                    ishistory = true;
//
//                }
//            } else {
//
//                if (mFooterOneTxt.equalsIgnoreCase(getString(R.string.rating_c)) || mFooterTwoTxt.equalsIgnoreCase(getString(R.string.rating_c))) {
//                    if (GlobalMethods.isSeller(this, mHomeSingleItemEntity.getUser_id())) {
//                        //Seller
//                        personName = mHomeSingleItemEntity.getBuyer_first_name();
//
//                        personUnivName = getString(R.string.univ_name) + " " + mHomeSingleItemEntity.getBuyer_university_name();
//                        otherpersonID = mHomeSingleItemEntity.getBuyer_id();
//                        personRating = GlobalMethods.isRating(mHomeSingleItemEntity.getBuyer_rating());
//                        ispersonVerified = GlobalMethods.isUserVerified(this, mHomeSingleItemEntity.getBuyer_verified());
//
//                        itemQuantity = getString(R.string.sell_qut) + " " + mHomeSingleItemEntity.getPurchase_quantity();
//                        itemPrice = getString(R.string.item_price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getItem_cost());
//                        itemSolidPrice = getString(R.string.sell_price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getItem_cost());
////                        itemSolidPrice = getString(R.string.sell_price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(String.valueOf(Double.valueOf(mHomeSingleItemEntity.getPurchase_quantity()) * Double.valueOf(mHomeSingleItemEntity.getItem_cost())));
//                        //itemSolidPrice = getString(R.string.service_price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getProcessing_fee());
//                        itemTotalPrice = getString(R.string.total_price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getAmount_received());
//
//                    } else {
//                        //Buyer
//                        personName = mHomeSingleItemEntity.getOwner_first_name();
//                        personUnivName = getString(R.string.univ_name) + " " + mHomeSingleItemEntity.getOwner_university_name();
//                        otherpersonID = mHomeSingleItemEntity.getUser_id();
//                        personRating = GlobalMethods.isRating(mHomeSingleItemEntity.getOwner_rating());
//                        ispersonVerified = GlobalMethods.isUserVerified(this, mHomeSingleItemEntity.getOwner_verified());
//
//                        itemQuantity = getString(R.string.pur_qut) + " " + mHomeSingleItemEntity.getPurchase_quantity();
//                        itemPrice = getString(R.string.pur_price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getItem_cost());
//                        itemSolidPrice = getString(R.string.service_price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getProcessing_fee());
//                        itemTotalPrice = getString(R.string.amt_paid) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getAmount_received());
//
//                    }
//                    ishistory = true;
//                } else {
//                    itemQuantity = getString(R.string.no_qut) + " " + mHomeSingleItemEntity.getItem_quantity();
//                    itemPrice = getString(R.string.price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getItem_cost());
//                    ishistory = false;
//                }
//
//
//            }
//        } else {
//            ishistory = true;
//            if (GlobalMethods.isSeller(this, mHomeSingleItemEntity.getUser_id())) {
//                //I am Seller
//                personName = mHomeSingleItemEntity.getBuyer_first_name();
//                personUnivName = getString(R.string.univ_name) + " " + mHomeSingleItemEntity.getBuyer_university_name();
//                personRating = GlobalMethods.isRating(mHomeSingleItemEntity.getBuyer_rating());
//                otherpersonID = mHomeSingleItemEntity.getBuyer_id();
//                itemQuantity = getString(R.string.sell_qut) + " " + mHomeSingleItemEntity.getPurchase_quantity();
//                ispersonVerified = GlobalMethods.isUserVerified(this, mHomeSingleItemEntity.getBuyer_verified());
//
////                itemPrice = getString(R.string.sell_price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getItem_cost());
////                itemSolidPrice = getString(R.string.service_price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getProcessing_fee());
//                itemPrice = getString(R.string.item_price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getItem_cost());
//                itemSolidPrice = getString(R.string.sell_price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getAmount_received());
////                itemSolidPrice = getString(R.string.sell_price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(String.valueOf(Double.valueOf(mHomeSingleItemEntity.getPurchase_quantity()) * Double.valueOf(mHomeSingleItemEntity.getItem_cost())));
//                itemTotalPrice = getString(R.string.total_price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getAmount_received());
//            } else {
//                //I am Buyer
//                personName = mHomeSingleItemEntity.getOwner_first_name();
//
//                personUnivName = getString(R.string.univ_name) + " " + mHomeSingleItemEntity.getOwner_university_name();
//                personRating = GlobalMethods.isRating(mHomeSingleItemEntity.getOwner_rating());
//                otherpersonID = mHomeSingleItemEntity.getUser_id();
//                itemQuantity = getString(R.string.pur_qut) + " " + mHomeSingleItemEntity.getPurchase_quantity();
//                ispersonVerified = GlobalMethods.isUserVerified(this, mHomeSingleItemEntity.getOwner_verified());
//
//                itemPrice = getString(R.string.pur_price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getItem_cost());
//                itemSolidPrice = getString(R.string.service_price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getProcessing_fee());
//                itemTotalPrice = getString(R.string.amt_paid) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getAmount_received());
//
//            }
//        }
//
//
//        //Data Set
//        mRatingBar.setRating(personRating);
//
//        mPersonNameTxt.setText(personName);
//        mPersonUnivNameTxt.setText(personUnivName);
//        mItemNameTxt.setText(itemName);
//        mItemQuantityTxt.setText(itemQuantity);
//        mItemPriceTxt.setText(itemPrice);
//        mItemServicesPriceTxt.setText(itemSolidPrice);
//        mItemTotalPriceTxt.setText(itemTotalPrice);
//        mItemCategoryTxt.setText(itemCategory);
//        mItemDeliveryTypeTxt.setText(itemDeliveryType);
//        mItemConditionTxt.setText(itemCondition);
//        mItemDescTxt.setText(itemDesc);
//
//        if (ishistory == true) {
//            mItemServicesPriceTxt.setVisibility(View.VISIBLE);
//            mItemTotalPriceTxt.setVisibility(View.VISIBLE);
//        }
//
//
//        if (ispersonVerified) {
//            mVerifyImg.setVisibility(View.VISIBLE);
//        }
//
//        if (GlobalMethods.isSeller(ProductDetailsScreen.this, mHomeSingleItemEntity.getUser_id())) {
////            mItemServicesPriceTxt.setVisibility(View.GONE);
//            mItemTotalPriceTxt.setVisibility(View.GONE);
//        }
//        if (mHomeSingleItemEntity.getItem_mode().equalsIgnoreCase(getString(R.string.one)) && mHomeSingleItemEntity.getItem_type().equalsIgnoreCase(getString(R.string.one)) && mHomeSingleItemEntity.getDelivery_type().equalsIgnoreCase(getString(R.string.two)) && mHomeSingleItemEntity.getCategory_name().equalsIgnoreCase(getString(R.string.ticket))) {
//
//            if (GlobalMethods.isSeller(ProductDetailsScreen.this, mHomeSingleItemEntity.getUser_id())) {
//                mItemDeliveryTypeTxt.setVisibility(View.GONE);
////                mItemCategoryTxt.setVisibility(View.GONE);
//            } else {
//                mItemDeliveryTypeTxt.setVisibility(View.VISIBLE);
//            }
//            mItemConditionTxt.setVisibility(View.GONE);
//            mItemServicesPriceTxt.setVisibility(View.VISIBLE);
//            mItemTotalPriceTxt.setVisibility(View.VISIBLE);
//
//        }
//        if (mHomeSingleItemEntity.getItem_mode().equalsIgnoreCase(getString(R.string.two))) {
//            mItemConditionTxt.setVisibility(View.GONE);
//        }
//        if (mFooterOneTxt.equalsIgnoreCase(getString(R.string.negotiation))) {
//            mItemServicesPriceTxt.setVisibility(View.VISIBLE);
//        }
//        OtherUserProfile.mOtherUSerID = otherpersonID;
//        addImagePagerView();
//    }

    private void addImagePagerView() {

        mImagesList = new ArrayList<>();
        if (mHomeSingleItemEntity.getPicture1() != null && mHomeSingleItemEntity.getPicture1()
                .length() > 0) {
            page_count = 1;
            mImagesList.add(mHomeSingleItemEntity.getPicture1());
        }

        if (mHomeSingleItemEntity.getPicture2() != null && mHomeSingleItemEntity.getPicture2()
                .length() > 0) {
            page_count = 2;
            mImagesList.add(mHomeSingleItemEntity.getPicture2());
        }

        if (mHomeSingleItemEntity.getPicture3() != null && mHomeSingleItemEntity.getPicture3()
                .length() > 0) {
            page_count = 3;
            mImagesList.add(mHomeSingleItemEntity.getPicture3());
        }
        setViewPageIndicater(page_count);
        if (mImagesList.size() > 0) {
            mImagePager.setAdapter(new MyPagerAdapter());
            mImagePager.setCurrentItem(0);
        } else {
            mImagePager.setBackgroundResource(R.drawable.product_bg);
        }


//        if (mHomeSingleItemEntity.getComplete() != null && mHomeSingleItemEntity.getComplete().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
//            mItemServicesPriceTxt.setVisibility(View.VISIBLE);
//            mItemTotalPriceTxt.setVisibility(View.VISIBLE);
//        }else if(mFooterBtnCount >1){
//            mItemServicesPriceTxt.setVisibility(View.GONE);
//            mItemTotalPriceTxt.setVisibility(View.VISIBLE);
//
//            mItemPriceTxt.setVisibility(View.GONE);
//        }
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.footer_one_btn_ly:
                if (mFooterOneBtn.getText().toString().trim().equalsIgnoreCase(getString(R.string
                        .chat))) {


                    if (!(mHomeSingleItemEntity.getUser_id().equalsIgnoreCase(GlobalMethods
                            .getUserID(ProductDetailsScreen.this)))) {
//                        otherUserId = mHomeSingleItemEntity.getUser_id();

                        AppConstants.CHAT_FRIEND_ID = mHomeSingleItemEntity.getUser_id();
//                        UserName = mHomeSingleItemEntity.getOwner_first_name() + "" + mHomeSingleItemEntity.getOwner_last_name();
                    } else {
//                        otherUserId = mHomeSingleItemEntity.getBuyer_id();
                        AppConstants.CHAT_FRIEND_ID = mHomeSingleItemEntity.getBuyer_id();
//                        UserName = mHomeSingleItemEntity.getBuyer_first_name() + "" + mHomeSingleItemEntity.getBuyer_last_name();
                    }
//                    Intent intent = new Intent(ProductDetailsScreen.this, ChatScreen.class);
//                    intent.putExtra("FriendId", otherUserId);
//                    intent.putExtra("ItemId", mHomeSingleItemEntity.getItem_id());
//                    intent.putExtra("UserName", UserName);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.slide_in_right,
//                            R.anim.slide_out_left);
//                    nextScreen(ChatScreen.class, false);

                    AppConstants.CHAT_ITEM_ID = mHomeSingleItemEntity.getItem_id();
                    if (mFooterTwoBtn.getText().toString().trim().equalsIgnoreCase(getString(R
                            .string.rating))) {
                        ChatScreen.isSend = false;
                    } else {
                        ChatScreen.isSend = true;
                    }
                    ChatScreen.mPaymentId = mHomeSingleItemEntity.getPayment_id();

                    nextScreen(ChatScreen.class, false);
                } else if (mFooterOneBtn.getText().toString().trim().equalsIgnoreCase(getString(R
                        .string.rating))) {

                    if (!(mHomeSingleItemEntity.getUser_id().equalsIgnoreCase(GlobalMethods
                            .getUserID(ProductDetailsScreen.this)))) {
                        otherUserId = mHomeSingleItemEntity.getUser_id();
                        UserName = mHomeSingleItemEntity.getOwner_first_name();
                        RateBuySellScreen.mHeader = getString(R.string.rateseller);

                        AppConstants.RATING_RATE = mHomeSingleItemEntity.getItem_seller_rating();
                        AppConstants.RATING_CMD = mHomeSingleItemEntity.getItem_seller_comments();
                    } else {

                        RateBuySellScreen.mHeader = getString(R.string.ratebuyer);
                        otherUserId = mHomeSingleItemEntity.getBuyer_id();
                        UserName = mHomeSingleItemEntity.getBuyer_first_name();
                        AppConstants.RATING_RATE = mHomeSingleItemEntity.getItem_buyer_rating();
                        AppConstants.RATING_CMD = mHomeSingleItemEntity.getItem_buyer_comments();
                    }

                    AppConstants.RATING_ITEM_ID = mHomeSingleItemEntity.getItem_id();
                    AppConstants.RATING_USER_ID = otherUserId;
                    AppConstants.RATING_USER_NAME = UserName;

                    AppConstants.RATING_BACK = AppConstants.SUCCESS_CODE;
                    nextScreen(RateBuySellScreen.class, false);

                } else if (mFooterOneBtn.getText().toString().trim().equalsIgnoreCase(getString(R
                        .string.negotiation))) {

                    if (!(mHomeSingleItemEntity.getUser_id().equalsIgnoreCase(GlobalMethods
                            .getUserID(ProductDetailsScreen.this)))) {
                        otherUserId = mHomeSingleItemEntity.getUser_id();
                        UserName = mHomeSingleItemEntity.getOwner_first_name();
                    } else {
                        otherUserId = mHomeSingleItemEntity.getBuyer_id();

                        UserName = mHomeSingleItemEntity.getBuyer_first_name();
                    }


                    NegotiationChatRoom.mHomeSingleItemEntity = mHomeSingleItemEntity;
                    AppConstants.CHAT_BACK = AppConstants.SUCCESS_CODE;
                    AppConstants.NEGO_FRIEND_ID = otherUserId;
                    AppConstants.NEGO_BID_ID = "";
                    AppConstants.NEGO_USER_ID = GlobalMethods.getUserID(this);
                    AppConstants.NEGO_ITEM_NOTI = "";
                    AppConstants.NEGO_ITEM_ID = mHomeSingleItemEntity.getItem_id();
                    AppConstants.NEGO_ITEM_QTY = mHomeSingleItemEntity.getPurchase_quantity();
                    nextScreen(NegotiationChatRoom.class, false);

                } else if (mFooterOneBtn.getText().toString().trim().equalsIgnoreCase(getString(R
                        .string.edit))) {

                    APIRequestHandler.getInstance().getItemEditableResponse(mHomeSingleItemEntity.getItem_id(), this);


                }

                break;
            case R.id.footer_two_btn_ly:
                if (mFooterTwoBtn.getText().toString().trim().equalsIgnoreCase(getString(R.string
                        .upload_txt))) {
                    DropboxUpload.item_ID = mHomeSingleItemEntity.getItem_id();
                    DropboxUpload.buyer_ID = mHomeSingleItemEntity.getBuyer_id();
                    DropboxUpload.buyer_Rating = mHomeSingleItemEntity.getItem_buyer_rating();
                    DropboxUpload.buyer_Cmd = mHomeSingleItemEntity.getItem_buyer_comments();
                    DropboxUpload.buyer_Name = mHomeSingleItemEntity.getBuyer_first_name() + " " + mHomeSingleItemEntity.getBuyer_last_name();
                    DropboxUpload.is_Approved = mHomeSingleItemEntity.getIs_approve();
                    DropboxUpload.payID = mHomeSingleItemEntity.getPayment_id();
                    nextScreen(DropboxUpload.class, false);
                } else if (mFooterTwoBtn.getText().toString().trim().equalsIgnoreCase(getString(R
                        .string.finish))) {
//                    if(mHomeSingleItemEntity.getUser_id().equalsIgnoreCase(GlobalMethods
//                            .getUserID(ProductDetailsScreen.this))){
                    if (mHomeSingleItemEntity.getItem_type().equalsIgnoreCase(getString(R.string.two))) {

                        if (mHomeSingleItemEntity.getFinish_service() != null && mHomeSingleItemEntity.getFinish_service().equalsIgnoreCase(getString(R.string.one))) {

                            DialogManager.showBaseTwoBtnDialog(this, getString(R
                                    .string.app_name), getString(R.string.finish_service), getString(R.string
                                    .yes), getString(R.string.no), new DialogManagerTwoBtnCallback() {
                                @Override
                                public void onBtnOkClick(String mOkStr) {
                                    APIRequestHandler.getInstance().getFinishServicesBuyResponse(mHomeSingleItemEntity.getBuyer_id(), mHomeSingleItemEntity.getItem_id(), ProductDetailsScreen.this);

                                }

                                @Override
                                public void onBtnCancelClick(String mCancelStr) {

                                }
                            });


//                            DialogManager.showBasicBtnAlertDialog(this, getString(R.string.app_name), getString(R.string.finish_service), new DialogMangerOkCallback() {
//                                @Override
//                                public void onOkClick() {
//                                    APIRequestHandler.getInstance().getFinishServicesBuyResponse(mHomeSingleItemEntity.getBuyer_id(), mHomeSingleItemEntity.getItem_id(), ProductDetailsScreen.this);
//
//                                }
//                            });
                        } else {
                            DialogManager.showBasicAlertDialog(this, getString(R.string.services_not), new DialogMangerOkCallback() {
                                @Override
                                public void onOkClick() {

                                }
                            });
                        }

                    } else {
                        AppConstants.RATING_BTN = AppConstants.FAILURE_CODE;
                        Intent intent = new Intent(ProductDetailsScreen.this, InspectModeScreen
                                .class);
                        intent.putExtra("FriendId", mHomeSingleItemEntity.getUser_id());
                        intent.putExtra("ItemId", mHomeSingleItemEntity.getItem_id());
                        intent.putExtra("UserName", mHomeSingleItemEntity.getUser_first_name());
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,
                                R.anim.slide_out_left);

                        //finish();
                        AppConstants.RATING_ITEM_ID = mHomeSingleItemEntity.getItem_id();
                        AppConstants.RATING_USER_ID = mHomeSingleItemEntity.getBuyer_id();


                        AppConstants.RATING_RATE = mHomeSingleItemEntity.getItem_buyer_rating();
                        AppConstants.RATING_CMD = mHomeSingleItemEntity.getItem_buyer_comments();
//                        AppConstants.RATING_BACK = AppConstants.SUCCESS_CODE;
                        AppConstants.RATING_USER_NAME = mHomeSingleItemEntity.getBuyer_first_name();
                        finish();
//                        nextScreen(RateBuySellScreen.class,false);
//                        nextScreen(InspectModeScreen.class, true);
//                    }
//                    else{
//
//                        APIRequestHandler.getInstance().finishBuyResponse(mHomeSingleItemEntity
//                                .getItem_id(),ProductDetailsScreen.this);
//
                    }
                } else if (mFooterTwoBtn.getText().toString().trim().equalsIgnoreCase(getString(R
                        .string.code))) {

//                    APIRequestHandler.getInstance().finishBuyResponse(mHomeSingleItemEntity
//                            .getItem_id(), ProductDetailsScreen.this);

                    BuyerCodeScreen.mBuyerName =
                            mHomeSingleItemEntity.getOwner_first_name();


                    BuyerCodeScreen.mPayID = mHomeSingleItemEntity.getPayment_id();
                    BuyerCodeScreen.mItemID = mHomeSingleItemEntity.getItem_id();


                    ChatScreen.mPaymentId = mHomeSingleItemEntity.getPayment_id();

//                    AppConstants.CODE_SCREEN = AppConstants.HOME_SCREEN;
                    AppConstants.CHAT_FRIEND_ID = mHomeSingleItemEntity.getUser_id();
                    AppConstants.CHAT_ITEM_ID = mHomeSingleItemEntity.getItem_id();

//                    nextScreen(BuyerCodeScreen.class, true);


                    AppConstants.RATING_BTN = AppConstants.FAILURE_CODE;
                    nextScreen(BuyerCodeScreen.class, false);


//                    BuyerCodeScreen.mBuyerName =
//                            mHomeSingleItemEntity.getUser_first_name();
//                    BuyerCodeScreen.mPayID=mHomeSingleItemEntity.getPayment_id();
//                    BuyerCodeScreen.mItemID=mHomeSingleItemEntity.getItem_id();
//
//                    ChatScreen.mPaymentId = mHomeSingleItemEntity.getPayment_id();
//
//                    AppConstants.CODE_SCREEN = AppConstants.HOME_SCREEN;
//
//                    AppConstants.CHAT_FRIEND_ID = mHomeSingleItemEntity.getUser_id();
//                    AppConstants.CHAT_ITEM_ID = mHomeSingleItemEntity.getItem_id();
//                    nextScreen(BuyerCodeScreen.class, true);

                } else if (mFooterTwoBtn.getText().toString().trim().equalsIgnoreCase(getString(R
                        .string.start))) {
                    //API changed

                    DialogManager.showBaseTwoBtnDialog(this, getString(R
                            .string.app_name), getString(R.string.start_ser_conf), getString(R.string
                            .yes), getString(R.string.no), new DialogManagerTwoBtnCallback() {
                        @Override
                        public void onBtnOkClick(String mOkStr) {
                            APIRequestHandler.getInstance().getStartedServicesResponse(mHomeSingleItemEntity
                                    .getItem_id(), mHomeSingleItemEntity
                                    .getUser_id(), ProductDetailsScreen.this);
                        }

                        @Override
                        public void onBtnCancelClick(String mCancelStr) {

                        }
                    });


//                    DialogManager.showBasicBtnAlertDialog(this, getString(R.string.app_name), getString(R.string.start_ser_conf), new DialogMangerOkCallback() {
//                        @Override
//                        public void onOkClick() {
//                            APIRequestHandler.getInstance().getStartedServicesResponse(mHomeSingleItemEntity
//                                    .getItem_id(), mHomeSingleItemEntity
//                                    .getUser_id(), ProductDetailsScreen.this);
//                        }
//                    });

                } else if (mFooterTwoBtn.getText().toString().trim().equalsIgnoreCase(getString(R
                        .string.preview))) {
                    if (mHomeSingleItemEntity.getFile_url() != null && !mHomeSingleItemEntity.getFile_url().equalsIgnoreCase("")) {
                        Uri uri;
                        Intent url;
                        uri = Uri.parse(getString(R.string.http) + mHomeSingleItemEntity.getFile_url().toString().trim());
                        url = new Intent(android.content.Intent.ACTION_VIEW, uri);
                        startActivity(url);
                    } else {
                        DialogManager.showBasicAlertDialog(ProductDetailsScreen.this, getString(R.string.no_preview), new
                                DialogMangerOkCallback() {

                                    @Override
                                    public void onOkClick() {

                                    }
                                });
                    }

                } else if (mFooterTwoBtn.getText().toString().trim().equalsIgnoreCase(getString(R
                        .string.rating))) {

                    if (!(mHomeSingleItemEntity.getUser_id().equalsIgnoreCase(GlobalMethods
                            .getUserID(ProductDetailsScreen.this)))) {
                        otherUserId = mHomeSingleItemEntity.getUser_id();
                        UserName = mHomeSingleItemEntity.getOwner_first_name();
                        RateBuySellScreen.mHeader = getString(R.string.rateseller);

                        AppConstants.RATING_RATE = mHomeSingleItemEntity.getItem_seller_rating();
                        AppConstants.RATING_CMD = mHomeSingleItemEntity.getItem_seller_comments();
                    } else {

                        RateBuySellScreen.mHeader = getString(R.string.ratebuyer);
                        otherUserId = mHomeSingleItemEntity.getBuyer_id();
                        UserName = mHomeSingleItemEntity.getBuyer_first_name();

                        AppConstants.RATING_RATE = mHomeSingleItemEntity.getItem_buyer_rating();
                        AppConstants.RATING_CMD = mHomeSingleItemEntity.getItem_buyer_comments();
                    }

                    AppConstants.RATING_ITEM_ID = mHomeSingleItemEntity.getItem_id();
                    AppConstants.RATING_USER_ID = otherUserId;
                    AppConstants.RATING_USER_NAME = UserName;

                    AppConstants.RATING_BACK = AppConstants.SUCCESS_CODE;
                    nextScreen(RateBuySellScreen.class, false);

                }
                break;
            case R.id.footer_three_btn_ly:

                if (mFooterThreeBtn.getText().toString().trim().equalsIgnoreCase(getString(R
                        .string.unsatis))) {

                    if (mHomeSingleItemEntity != null && mHomeSingleItemEntity.getPayment_id()
                            !=
                            null &&
                            !mHomeSingleItemEntity.getPayment_id().equalsIgnoreCase("")) {


                        DialogManager.showBaseTwoBtnDialog(this, getString(R
                                .string.app_name), getString(R.string.unsatiesfy_conf), getString(R.string
                                .yes), getString(R.string.no), new DialogManagerTwoBtnCallback() {
                            @Override
                            public void onBtnOkClick(String mOkStr) {
                                APIRequestHandler.getInstance().getUnsatiesFactoryResponse
                                        (mHomeSingleItemEntity.getItem_id(),
                                                mHomeSingleItemEntity.getPayment_id(),
                                                ProductDetailsScreen.this);
                            }

                            @Override
                            public void onBtnCancelClick(String mCancelStr) {

                            }
                        });


//                        DialogManager.showBasicBtnAlertDialog(ProductDetailsScreen.this, getString(R
//                                .string.unsatis), getString(R.string.unsatiesfy_conf), new
//                                DialogMangerOkCallback() {
//
//
//                                    @Override
//                                    public void onOkClick() {
//                                        APIRequestHandler.getInstance().getUnsatiesFactoryResponse
//                                                (mHomeSingleItemEntity.getItem_id(),
//                                                        mHomeSingleItemEntity.getPayment_id(),
//                                                        ProductDetailsScreen.this);
//                                    }
//                                });


                    }

                } else if (mFooterThreeBtn.getText().toString().trim().equalsIgnoreCase(getString(R
                        .string.approve))) {
                    if (mHomeSingleItemEntity.getFile_url() != null && !mHomeSingleItemEntity.getFile_url().equalsIgnoreCase("")) {
                        APIRequestHandler.getInstance().getApprovePreviewSerResponse(mHomeSingleItemEntity.getItem_id(), this);
                    } else {
                        DialogManager.showBasicAlertDialog(ProductDetailsScreen.this, getString(R.string.preview_not_fund), new
                                DialogMangerOkCallback() {

                                    @Override
                                    public void onOkClick() {

                                    }
                                });
                    }

                }
                break;
            case R.id.footer_one_btn:
                if (mFooterOneBtn.getText().toString().trim().equalsIgnoreCase(getString(R.string
                        .chat))) {


                    if (!(mHomeSingleItemEntity.getUser_id().equalsIgnoreCase(GlobalMethods
                            .getUserID(ProductDetailsScreen.this)))) {
                        AppConstants.CHAT_FRIEND_ID = mHomeSingleItemEntity.getUser_id();

                        UserName = mHomeSingleItemEntity.getOwner_first_name();
                    } else {
                        AppConstants.CHAT_FRIEND_ID = mHomeSingleItemEntity.getBuyer_id();

                        UserName = mHomeSingleItemEntity.getBuyer_first_name();
                    }
                    if (mHomeSingleItemEntity.getItem_type().equalsIgnoreCase(getString(R.string.two)) && mHomeSingleItemEntity.getDelivery_type().equalsIgnoreCase(getString(R.string.one))) {


                        if (mFooterTwoBtn.getText().toString().trim().equalsIgnoreCase(getString(R.string.start)) || mFooterTwoBtn.getText().toString().trim().equalsIgnoreCase(getString(R.string.started))) {
                            if (!(mHomeSingleItemEntity.getUser_id().equalsIgnoreCase(GlobalMethods
                                    .getUserID(ProductDetailsScreen.this)))) {
                                AppConstants.CHAT_FRIEND_ID = mHomeSingleItemEntity.getBuyer_id();

                                UserName = mHomeSingleItemEntity.getOwner_first_name();
                            } else {
                                AppConstants.CHAT_FRIEND_ID = mHomeSingleItemEntity.getUser_id();

                                UserName = mHomeSingleItemEntity.getBuyer_first_name();
                            }
                        }
                        if (mFooterTwoBtn.getText().toString().trim().equalsIgnoreCase(getString(R.string.finish))) {
                            if (!(mHomeSingleItemEntity.getUser_id().equalsIgnoreCase(GlobalMethods
                                    .getUserID(ProductDetailsScreen.this)))) {
                                AppConstants.CHAT_FRIEND_ID = mHomeSingleItemEntity.getBuyer_id();

                                UserName = mHomeSingleItemEntity.getOwner_first_name();
                            } else {
                                AppConstants.CHAT_FRIEND_ID = mHomeSingleItemEntity.getUser_id();

                                UserName = mHomeSingleItemEntity.getBuyer_first_name();
                            }
                        }
                    }
                    AppConstants.CHAT_ITEM_ID = mHomeSingleItemEntity.getItem_id();
                    if (mFooterTwoBtn.getText().toString().trim().equalsIgnoreCase(getString(R
                            .string.rating))) {
                        ChatScreen.isSend = false;
                    } else {
                        ChatScreen.isSend = true;
                    }

                    ChatScreen.mPaymentId = mHomeSingleItemEntity.getPayment_id();
                    nextScreen(ChatScreen.class, false);
                } else if (mFooterOneBtn.getText().toString().trim().equalsIgnoreCase(getString(R
                        .string.rating))) {


                    if (!(mHomeSingleItemEntity.getUser_id().equalsIgnoreCase(GlobalMethods
                            .getUserID(ProductDetailsScreen.this)))) {
                        otherUserId = mHomeSingleItemEntity.getUser_id();
                        UserName = mHomeSingleItemEntity.getOwner_first_name();
                        RateBuySellScreen.mHeader = getString(R.string.rateseller);

                        AppConstants.RATING_RATE = mHomeSingleItemEntity.getItem_seller_rating();
                        AppConstants.RATING_CMD = mHomeSingleItemEntity.getItem_seller_comments();
                    } else {

                        RateBuySellScreen.mHeader = getString(R.string.ratebuyer);
                        otherUserId = mHomeSingleItemEntity.getBuyer_id();
                        UserName = mHomeSingleItemEntity.getBuyer_first_name();


                        AppConstants.RATING_RATE = mHomeSingleItemEntity.getItem_buyer_rating();
                        AppConstants.RATING_CMD = mHomeSingleItemEntity.getItem_buyer_comments();
                    }

                    AppConstants.RATING_ITEM_ID = mHomeSingleItemEntity.getItem_id();
                    AppConstants.RATING_USER_ID = otherUserId;
                    AppConstants.RATING_USER_NAME = UserName;

                    AppConstants.RATING_BACK = AppConstants.SUCCESS_CODE;
                    nextScreen(RateBuySellScreen.class, false);

                } else if (mFooterOneBtn.getText().toString().trim().equalsIgnoreCase(getString(R
                        .string.negotiation))) {

                    if (!(mHomeSingleItemEntity.getUser_id().equalsIgnoreCase(GlobalMethods
                            .getUserID(ProductDetailsScreen.this)))) {
                        otherUserId = mHomeSingleItemEntity.getUser_id();
                        UserName = mHomeSingleItemEntity.getOwner_first_name();
                    } else {
                        otherUserId = mHomeSingleItemEntity.getBuyer_id();
                        UserName = mHomeSingleItemEntity.getBuyer_first_name();
                    }


                    AppConstants.CHAT_BACK = AppConstants.SUCCESS_CODE;
                    AppConstants.NEGO_FRIEND_ID = otherUserId;
                    AppConstants.NEGO_BID_ID = "";
                    AppConstants.NEGO_USER_ID = GlobalMethods.getUserID(this);
                    AppConstants.NEGO_ITEM_NOTI = "";
                    AppConstants.NEGO_ITEM_ID = mHomeSingleItemEntity.getItem_id();
                    AppConstants.NEGO_ITEM_QTY = mHomeSingleItemEntity.getPurchase_quantity();

                    NegotiationChatRoom.mHomeSingleItemEntity = mHomeSingleItemEntity;
                    if (mHomeSingleItemEntity.getNegotiate_offers() != null && mHomeSingleItemEntity.getNegotiate_offers().equalsIgnoreCase(getString(R.string.one))) {
                        NegotiationChatRoom.mHomeSingleItemEntity.setBuyer_id(mHomeSingleItemEntity.getUser_id());
                        AppConstants.NEGO_ITEM_QTY = mHomeSingleItemEntity.getItem_quantity();
                        AppConstants.NEGO_BID_ID = mHomeSingleItemEntity.getBid_id();

//                        otherUserId = mHomeSingleItemEntity.getUser_id();
//                        UserName = mHomeSingleItemEntity.getOwner_first_name();
//                    }else if (mHomeSingleItemEntity.getNegotiate_offers() != null &&mHomeSingleItemEntity.getNegotiate_offers().equalsIgnoreCase(getString(R.string.zero))) {
//
//                        otherUserId = mHomeSingleItemEntity.getBuyer_id();
//                        UserName = mHomeSingleItemEntity.getBuyer_first_name();
//                        otherUserId = mHomeSingleItemEntity.getUser_id();
//                        UserName = mHomeSingleItemEntity.getOwner_first_name();
                    }

                    nextScreen(NegotiationChatRoom.class, false);

                } else if (mFooterOneBtn.getText().toString().trim().equalsIgnoreCase(getString(R
                        .string.edit))) {

                    APIRequestHandler.getInstance().getItemEditableResponse(mHomeSingleItemEntity.getItem_id(), this);

                }

                break;


            case R.id.person_name_txt:
                nextScreen(OtherUserProfile.class, false);
                break;

            case R.id.header_right_btn_lay:
                if (mHomeSingleItemEntity.getPreview_url() != null && !mHomeSingleItemEntity.getPreview_url().equalsIgnoreCase("")) {
                    Uri uri;
                    Intent url;
                    uri = Uri.parse(getString(R.string.http) + mHomeSingleItemEntity.getPreview_url());
                    url = new Intent(android.content.Intent.ACTION_VIEW, uri);
                    startActivity(url);
                }
                break;
        }

    }

    private boolean getBooleanFromString(String str) {
        return str.equalsIgnoreCase("1");
    }

    @Override
    public void onBackPressed() {

        if (isFromHome) {
            isFromHome = false;
            finishScreen();
//        } else if (AppConstants.FROME_NOTIFICATION.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
//            AppConstants.FROME_NOTIFICATION = AppConstants.FAILURE_CODE;
//            previousScreen(HomeScreenActivity.class, true);
        } else if (AppConstants.PRODUCT_DETAILS_BACK.equalsIgnoreCase(getString(R.string.one))) {
            AppConstants.PRODUCT_DETAILS_BACK = AppConstants.FAILURE_CODE;
            previousScreen(ProfileListScreen.class, true);
        } else if (AppConstants.PRODUCT_DETAILS_BACK.equalsIgnoreCase(getString(R.string.two))) {
            AppConstants.PRODUCT_DETAILS_BACK = AppConstants.FAILURE_CODE;
            previousScreen(NotificationScreen.class, true);
        } else if (AppConstants.PRODUCT_DETAILS_BACK.equalsIgnoreCase(getString(R.string.three))) {
            AppConstants.PRODUCT_DETAILS_BACK = AppConstants.FAILURE_CODE;
            previousScreen(HomeScreenActivity.class, true);
//        } else if (AppConstants.PRODUCT_DETAILS_BACK.equalsIgnoreCase(getString(R.string.four))) {
//            AppConstants.PRODUCT_DETAILS_BACK = AppConstants.FAILURE_CODE;
//            finishScreen();
        } else {
            AppConstants.PRODUCT_DETAILS_BACK = AppConstants.FAILURE_CODE;
            previousScreen(HomeScreenActivity.class, true);
        }
    }


    private class MyPagerAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return mImagesList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {


            LayoutInflater inflater = LayoutInflater.from(ProductDetailsScreen.this);
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.image_layout_produt_details,
                    container, false);

            final ImageView product_img = (ImageView) layout.findViewById(R.id.product_img);
            runOnUiThread(new Runnable() {
                public void run() {

                    Glide.with(ProductDetailsScreen.this)
                            .load(mImagesList.get(position)).asBitmap().placeholder(R.drawable.product_bg).fitCenter().into(product_img);
                    product_img.setVisibility(View.VISIBLE);
                }
            });


            product_img.setTag(mImagesList.get(position));

            product_img.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    String tag = (String) view.getTag();
                    DialogManager.ImageViewer(ProductDetailsScreen.this,
                            tag);

                    return true;
                }
            });


            container.addView(layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }

    }

    private void sellBuyApprovList(HomeSingleItemEntity homeSingleItemEntity) {
        mFooterOneTxt = mActivity.getString(R.string.chat);
        mFooterTwoTxt = mActivity.getString(R.string.finish);
        mFooterBtnCount = 2;
        if (homeSingleItemEntity.getItem_type().equalsIgnoreCase(mActivity.getString(R.string.one)) && (homeSingleItemEntity.getDelivery_type().equalsIgnoreCase(mActivity.getString(R.string.two)))) {
            mFooterTwoTxt = getString(R.string.rating);
        }
        if (homeSingleItemEntity.getItem_type().equalsIgnoreCase(mActivity.getString(R.string.two))) {
            if (homeSingleItemEntity.getDelivery_type().equalsIgnoreCase(mActivity.getString(R.string.one))) {

            } else {
                mFooterTwoTxt = mActivity.getString(R.string.upload_txt);
            }
        }
    }

    private void buyOrderList(HomeSingleItemEntity buypost) {
        mFooterOneTxt = getString(R.string.chat);
        mFooterTwoTxt = mActivity.getString(R.string.code);
        mFooterThreeTxt = mActivity.getString(R.string.unsatis);
        mFooterBtnCount = 3;


        if (buypost.getItem_type().equalsIgnoreCase(mActivity.getString(R.string.one)) && buypost.getDelivery_type().equalsIgnoreCase(mActivity.getString(R.string.two))) {

            mFooterBtnCount = 2;
            mFooterOneTxt = getString(R.string.chat);
            mFooterTwoTxt = getString(R.string.rating);
            if (buypost.getItem_type().equalsIgnoreCase(mActivity.getString(R.string.one)) && buypost.getDelivery_type().equalsIgnoreCase(mActivity.getString(R.string.two))) {
                mFooterBtnCount = 1;
                mFooterOneTxt = mActivity.getString(R.string.rating);
            }
        }
        if (buypost.getItem_type().equalsIgnoreCase(mActivity.getString(R.string.two))) {
            if (buypost.getDelivery_type().equalsIgnoreCase(mActivity.getString(R.string.one))) {
                mFooterBtnCount = 2;
                mFooterTwoTxt = mActivity.getString(R.string.start);
            } else {

                mFooterTwoTxt = mActivity.getString(R.string.preview);
                mFooterThreeTxt = mActivity.getString(R.string.approve);
            }
        }
    }
}
