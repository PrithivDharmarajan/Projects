package com.bridgellc.bridge.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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
import com.bridgellc.bridge.model.BuyItemResponse;
import com.bridgellc.bridge.model.CommonModelResponse;
import com.bridgellc.bridge.model.CommonPhResponse;
import com.bridgellc.bridge.model.PayForItemResponse;
import com.bridgellc.bridge.model.PaypalPayResponse;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogManagerTwoBtnCallback;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.GlobalMethods;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProductDetailsBuyNeg extends BaseActivity implements View.OnClickListener {
    public static RelativeLayout mRatLay;
    public static String mHeadTxt, mFooterOneTxt, mFooterTwoTxt;
    public static int mFooterBtnCount;
    public static int mRatingVisible = 1;
    private ImageView mVerifyImg;
    private ImageView mSlidepointerOne, mSlidepointerTwo,
            mSlidepointerThree;
    private int page_count = 0;
    private ImageView mHeaderRightImage;
    public static HomeSingleItemEntity mHomeSingleItemEntity;
    private ViewPager mImagePager;
    private RatingBar mRatingBar;
    private String mBid = "", mPayMode = "";
    private SimpleDateFormat mTargetDateTime = new SimpleDateFormat("MMMM dd, yyyy hh:mm aa", Locale.US);
    private SimpleDateFormat mServerTimeForm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    private TextView mPersonNameTxt, mPersonUnivNameTxt, mItemNameTxt, mItemQuantityTxt, mItemPriceTxt, mItemServicesPriceTxt, mItemTotalPriceTxt, mItemCategoryTxt, mItemDeliveryTypeTxt, mItemConditionTxt, mItemDescTxt, mItemWebTxt, mItemPhTxt;
    private ArrayList<String> mImagesList = new ArrayList<>();
    private String mItemQuantity = "", mItemPrice = "", mServicePrice = "", mTotalPrice = "";
    private RelativeLayout mBottomLay;
    private LinearLayout mCertPartLay, mPartWebPhLay;
    private boolean isPh = true;
    private GradientDrawable bgShapeGreen, bgShapeWhite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details_screen);
        mActivity = this;

        initComponents();

    }

    private void initComponents() {
        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);

        mHeadTxt = getString(R.string.details).toUpperCase();
        mFooterOneTxt = getString(R.string.buy_it);
        mFooterTwoTxt = getString(R.string.nego);
        mFooterBtnCount = 2;
        mSlidepointerOne = (ImageView) findViewById(R.id.slidepointer_one);
        mSlidepointerTwo = (ImageView) findViewById(R.id.slidepointer_two);
        mSlidepointerThree = (ImageView) findViewById(R.id.slidepointer_three);

        mImagePager = (ViewPager) findViewById(R.id.uploadPager);
        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        mHeadeRightBtnLay = (RelativeLayout) findViewById(R.id.header_right_btn_lay);
        mHeaderRightImage = (ImageView) findViewById(R.id.header_right_img);
        mHeaderRightImage.setImageResource(R.drawable.block_icon);

        mFooterOneLay = (RelativeLayout) findViewById(R.id.footer_one_lay);
        mFooterTwoLay = (RelativeLayout) findViewById(R.id.footer_two_lay);
        mFooterThreeLay = (RelativeLayout) findViewById(R.id.footer_three_lay);

        mFooterOneBtn = (Button) findViewById(R.id.footer_one_btn);
        mFooterTwoBtn = (Button) findViewById(R.id.footer_two_btn);
        mFooterThreeBtn = (Button) findViewById(R.id.footer_three_btn);

        mBottomLay = (RelativeLayout) findViewById(R.id.bottom_lay);

        if (mHomeSingleItemEntity.getItem_mode().equalsIgnoreCase(getString(R.string.one))) {
            if (mHomeSingleItemEntity.getItem_mode().equalsIgnoreCase(getString(R.string.one)) && mHomeSingleItemEntity.getItem_type().equalsIgnoreCase(getString(R.string.one)) && mHomeSingleItemEntity.getDelivery_type().equalsIgnoreCase(getString(R.string.two)) && mHomeSingleItemEntity.getCategory_name().equalsIgnoreCase(getString(R.string.ticket))) {
                mFooterBtnCount = 1;
                mFooterOneLay.setBackgroundColor(getResources().getColor(R.color.blue_btn_bg));

            } else {
                mFooterOneLay.setBackgroundColor(getResources().getColor(R.color.blue_btn_bg));
                mFooterTwoLay.setBackgroundColor(getResources().getColor(R.color.green));
//                mFooterTwoBtn.setTextColor(getResources().getColor(R.color.blue_btn_bg));
            }
        } else {
            mFooterBtnCount = 1;
            mFooterOneLay.setBackgroundColor(getResources().getColor(R.color.blue_btn_bg));
            mFooterOneTxt = getString(R.string.make_off);
        }
        mFooterThreeBtn.setVisibility(View.GONE);

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
        mItemWebTxt = (TextView) findViewById(R.id.web_txt);
        mItemPhTxt = (TextView) findViewById(R.id.ph_num_txt);
        mPartWebPhLay = (LinearLayout) findViewById(R.id.part_web_ph_lay);

        mCertPartLay = (LinearLayout) findViewById(R.id.cert_part_lay);

        if (mFooterBtnCount == 1) {
            mFooterOneLay.setVisibility(View.VISIBLE);
            mFooterTwoLay.setVisibility(View.GONE);
            mFooterThreeLay.setVisibility(View.GONE);

            mFooterOneBtn.setText(mFooterOneTxt);
        } else if (mFooterBtnCount == 2) {
            mFooterOneLay.setVisibility(View.VISIBLE);
            mFooterTwoLay.setVisibility(View.VISIBLE);
            mFooterThreeLay.setVisibility(View.GONE);

            mFooterOneBtn.setText(mFooterOneTxt);
            mFooterTwoBtn.setText(mFooterTwoTxt);
        }

        if (mRatingVisible == 0) {
            mRatLay.setVisibility(View.GONE);
        }

        //Set Data
        mHeadeLeftBtnLay.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(mHeadTxt);

        if (GlobalMethods.isCertifiedPartner(this, mHomeSingleItemEntity.getItem_type(), mHomeSingleItemEntity.getPartner())) {
            mCertPartLay.setVisibility(View.VISIBLE);
            if (mHomeSingleItemEntity.getItem_type().equalsIgnoreCase(getString(R.string.one))) {
                mFooterTwoLay.setVisibility(View.GONE);
                mFooterThreeLay.setVisibility(View.GONE);

            } else if (mHomeSingleItemEntity.getItem_type().equalsIgnoreCase(getString(R.string.two))) {
                mBottomLay.setVisibility(View.GONE);
                mPartWebPhLay.setVisibility(View.VISIBLE);
            }
        }


        bgShapeGreen = (GradientDrawable) getResources().getDrawable(R.drawable.round);
        bgShapeWhite = (GradientDrawable) getResources().getDrawable(R.drawable.round);

        bgShapeGreen.setColor(getResources().getColor(R.color.green));
        bgShapeWhite.setColor(getResources().getColor(R.color.white));

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

    private void populateData() {
        String personName = "", personUnivName = "", otherpersonID = "", itemName = "", itemQuantity = "", itemPrice = "", itemSolidPrice = "", itemTotalPrice = "", itemCategory = "", itemDeliveryType = "", itemCondition = "", conditionType = "", itemDesc = "";
        float personRating;
        boolean ispersonVerified;

        itemName = GlobalMethods.isGoodsService(this, mHomeSingleItemEntity.getItem_type()) + " : " + mHomeSingleItemEntity.getItem_name();
//        itemName = getString(R.string.item_name) + " " + mHomeSingleItemEntity.getItem_name();
        itemCategory = getString(R.string.category) + " " + mHomeSingleItemEntity.getCategory_name();
        itemDeliveryType = getString(R.string.delivery_type) + " " + GlobalMethods.getDeliveryType(this, mHomeSingleItemEntity.getDelivery_type());
        conditionType = (mHomeSingleItemEntity.getItem_mode().equalsIgnoreCase(getString(R.string.one)) && mHomeSingleItemEntity.getItem_type().equalsIgnoreCase(getString(R.string.two))) ? getString(R.string.skill_level) : getString(R.string.condition);
        itemCondition = conditionType + " " + mHomeSingleItemEntity.getItem_condition();
        itemDesc = getString(R.string.desc) + " " + mHomeSingleItemEntity.getItem_description();


//        personName = mHomeSingleItemEntity.getUser_first_name();
//        personUnivName = getString(R.string.univ_name) + " " + mHomeSingleItemEntity.getOwner_university_name();
//
//        otherpersonID = mHomeSingleItemEntity.getUser_id();
//        personRating = GlobalMethods.isRating(mHomeSingleItemEntity.getOwner_rating());
//        ispersonVerified = GlobalMethods.isUserVerified(this, mHomeSingleItemEntity.getUser_verified());
//        itemQuantity = getString(R.string.no_qut) + " " + mHomeSingleItemEntity.getItem_quantity();
//        itemPrice = getString(R.string.price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getItem_cost());


//        if (mHomeSingleItemEntity.getCategory_name() != null && mHomeSingleItemEntity.getCategory_name().equalsIgnoreCase(getString(R.string.ticket))) {
//
//            itemName = getString(R.string.event_name) + " " + mHomeSingleItemEntity.getItem_name();
//            itemQuantity = getString(R.string.event_venue) + " " + mHomeSingleItemEntity.getVenue();
//            itemPrice = getString(R.string.event_date) + " " + GlobalMethods.gettwoDateFormate(mHomeSingleItemEntity.getEvent_date_time(), mServerTimeForm, mTargetDateTime);
//            itemSolidPrice = getString(R.string.price) + "  $ " + GlobalMethods.getPriValWithTwoPoint(mHomeSingleItemEntity.getItem_cost());
//            itemCategory = getString(R.string.no_tic) + " " + mHomeSingleItemEntity.getItem_quantity();
//            itemDeliveryType = getString(R.string.category) + " " + mHomeSingleItemEntity.getCategory_name();
//
//        }


        //Items in New and Neg
        // I am Seller
        personName = mHomeSingleItemEntity.getUser_first_name();
        personUnivName = getString(R.string.univ_name) + " " + mHomeSingleItemEntity.getOwner_university_name();
        otherpersonID = GlobalMethods.getUserID(this);
        personRating = GlobalMethods.isRating(mHomeSingleItemEntity.getOwner_rating());
        ispersonVerified = GlobalMethods.isUserVerified(this, mHomeSingleItemEntity.getOwner_verified());
//                    iscertPart = GlobalMethods.isCertifiedPartner(this, mHomeSingleItemEntity.getItem_type(), mHomeSingleItemEntity.getSeller_partner());

        if (mHomeSingleItemEntity.getItem_mode().equalsIgnoreCase(getString(R.string.one)) && mHomeSingleItemEntity.getItem_type().equalsIgnoreCase(getString(R.string.one)) && mHomeSingleItemEntity.getDelivery_type().equalsIgnoreCase(getString(R.string.two)) && mHomeSingleItemEntity.getCategory_name().equalsIgnoreCase(getString(R.string.ticket))) {
            //Tickets In NEW
            itemName = getString(R.string.event_name) + " " + mHomeSingleItemEntity.getItem_name();
            itemQuantity = getString(R.string.event_venue) + " " + mHomeSingleItemEntity.getVenue();
            itemPrice = getString(R.string.event_date) + " " + GlobalMethods.gettwoDateFormate(mHomeSingleItemEntity.getEvent_date_time(), mServerTimeForm, mTargetDateTime);
            itemSolidPrice = getString(R.string.price) + "  $ " + GlobalMethods.afterTwoPointVal(mHomeSingleItemEntity.getItem_cost());
            itemCategory = getString(R.string.no_tic) + " " + mHomeSingleItemEntity.getItem_quantity();
            itemDeliveryType = getString(R.string.category) + " " + mHomeSingleItemEntity.getCategory_name();

            mItemConditionTxt.setVisibility(View.GONE);
            mItemServicesPriceTxt.setVisibility(View.VISIBLE);
        } else {
            //Other Items In NEW
            itemQuantity = getString(R.string.no_qut) + " " + mHomeSingleItemEntity.getItem_quantity();
            itemPrice = getString(R.string.price) + "  $ " + GlobalMethods.afterTwoPointVal(mHomeSingleItemEntity.getItem_cost());
        }


        //Data Set
        mRatingBar.setRating(personRating);

        mPersonNameTxt.setText(personName);
        mPersonUnivNameTxt.setText(personUnivName);
        mItemNameTxt.setText(itemName);
        mItemQuantityTxt.setText(itemQuantity);
        mItemPriceTxt.setText(itemPrice);
        mItemServicesPriceTxt.setText(itemSolidPrice);
        mItemTotalPriceTxt.setText(itemTotalPrice);
        mItemCategoryTxt.setText(itemCategory);
        mItemDeliveryTypeTxt.setText(itemDeliveryType);
        mItemConditionTxt.setText(itemCondition);
        mItemDescTxt.setText(itemDesc);
        mItemPhTxt.setText(mHomeSingleItemEntity.getPhone_number());
        mItemWebTxt.setText(mHomeSingleItemEntity.getWebsite());

        Paint mBlue = new Paint(Color.BLUE);
        mItemPhTxt.setPaintFlags(mBlue.UNDERLINE_TEXT_FLAG);
        mItemPhTxt.setTextColor(Color.BLUE);
        mItemWebTxt.setPaintFlags(mBlue.UNDERLINE_TEXT_FLAG);
        mItemWebTxt.setTextColor(Color.BLUE);

        if (ispersonVerified) {
            mVerifyImg.setVisibility(View.VISIBLE);
        }

//        if (GlobalMethods.isSeller(ProductDetailsBuyNeg.this, mHomeSingleItemEntity.getUser_id())) {
//            mItemServicesPriceTxt.setVisibility(View.GONE);
//        }
//        if (mHomeSingleItemEntity.getItem_mode().equalsIgnoreCase(getString(R.string.one)) && mHomeSingleItemEntity.getItem_type().equalsIgnoreCase(getString(R.string.one)) && mHomeSingleItemEntity.getDelivery_type().equalsIgnoreCase(getString(R.string.two)) && mHomeSingleItemEntity.getCategory_name().equalsIgnoreCase(getString(R.string.ticket))) {
//
//            mItemConditionTxt.setVisibility(View.GONE);
//            mItemServicesPriceTxt.setVisibility(View.VISIBLE);
//            mItemTotalPriceTxt.setVisibility(View.VISIBLE);
//            mItemDeliveryTypeTxt.setVisibility(View.GONE);
//            mItemCategoryTxt.setVisibility(View.GONE);
//        }
        if (mHomeSingleItemEntity.getItem_mode().equalsIgnoreCase(getString(R.string.two))) {
            mItemConditionTxt.setVisibility(View.GONE);
        }

//        if (!mHomeSingleItemEntity.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID
//                (ProductDetailsBuyNeg.this)) && mHomeSingleItemEntity.getItem_mode().equalsIgnoreCase(getString(R.string.one)) && mHomeSingleItemEntity.getItem_type().equalsIgnoreCase(getString(R.string.one)) && mHomeSingleItemEntity.getDelivery_type().equalsIgnoreCase(getString(R.string.two)) && !mHomeSingleItemEntity.getCategory_name().equalsIgnoreCase(getString(R.string.ticket))) {

        mHeadeRightBtnLay.setVisibility(View.VISIBLE);
//        } else {
//            mHeadeRightBtnLay.setVisibility(View.GONE);
//        }
        OtherUserProfile.mOtherUSerID = otherpersonID;
        addImagePagerView();
    }

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


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.footer_one_btn:
                if (mHomeSingleItemEntity.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID
                        (ProductDetailsBuyNeg.this))) {
                    DialogManager.showBasicAlertDialog(ProductDetailsBuyNeg.this, getString(R
                            .string.your_item), new DialogMangerOkCallback() {
                        @Override
                        public void onOkClick() {

                        }
                    });
                } else {
                    if (mHomeSingleItemEntity.getItem_mode().equalsIgnoreCase(getString(R.string.one))) {

                        ProductBuyNegScreen.isBuy = true;
                        if (mHomeSingleItemEntity.getItem_type().equalsIgnoreCase(getString(R.string.two))) {
                            DialogManager.shownAlertDialogProductPrice(ProductDetailsBuyNeg.this,
                                    getString(R.string.app_name), getString(R.string.do_you_pur), mHomeSingleItemEntity.getItem_cost(), new DialogMangerOkCallback() {
                                        @Override
                                        public void onOkClick() {

//                                            if (GlobalMethods.getStringValue(ProductDetailsBuyNeg.this, AppConstants.CARD_DETAILS).equalsIgnoreCase(getString(R.string.one))) {
//                                                APIRequestHandler.getInstance().payForItemResponse(ProductDetailsBuyNeg
//                                                                .mHomeSingleItemEntity.getItem_id(),
//                                                        getString(R.string.one), ProductDetailsBuyNeg.this);
                                            String cost = mHomeSingleItemEntity.getItem_cost().replace("$", "").trim();
                                            String mServiceTxt = "", mTotalCostTxt = "";
                                            double pricecost = Float.parseFloat(cost);

                                            double service_cost = 0.60;
                                            if (pricecost >= 1 && pricecost < 10) {
                                                service_cost = 0.60;
                                            } else if (pricecost >= 10 && pricecost < 133) {
                                                service_cost = 7.50;
                                            } else if (pricecost >= 133 && pricecost < 200) {
                                                service_cost = 10;
                                            } else if (pricecost >= 200) {
                                                service_cost = 5.00;
                                            }
                                            if (service_cost == 0.60 || service_cost == 10) {
                                                mServiceTxt = GlobalMethods.afterTwoPointVal(String.valueOf(service_cost));
                                                mTotalCostTxt = GlobalMethods.afterTwoPointVal(String.valueOf(pricecost + service_cost));
                                            } else {

                                                double serviceTax = (double) ((pricecost * service_cost) / 100);
                                                mServiceTxt = GlobalMethods.afterTwoPointVal(String.valueOf(serviceTax));
                                                mTotalCostTxt = GlobalMethods.afterTwoPointVal(String.valueOf(pricecost + serviceTax));
                                            }

//                                            mItemName = mHomeSingleItemEntity.getItem_name();
                                            mItemQuantity = AppConstants.SUCCESS_CODE;
                                            mItemPrice = cost;
                                            mServicePrice = mServiceTxt;
                                            mTotalPrice = mTotalCostTxt;

                                            paymentScreenCall();


//                                            APIRequestHandler.getInstance().buyItemResponse(ProductDetailsBuyNeg
//                                                            .mHomeSingleItemEntity.getItem_id(),
//                                                    getString(R.string.one), AppConstants.FAILURE_CODE, ProductDetailsBuyNeg.this);
//                                            } else {
////                                                ProductBuyNegScreen.isBuy =true ;
//                                                AppConstants.CARD_DET_BACK_SCREEN = getString(R.string.three);
//                                                nextScreen(AddPayCard.class, false);
//                                            }
                                        }
                                    });

                        } else {
//                            if (GlobalMethods.getStringValue(ProductDetailsBuyNeg.this, AppConstants.CARD_DETAILS).equalsIgnoreCase(getString(R.string.one))) {
//                                Intent intent = new Intent(ProductDetailsBuyNeg.this, ProductBuyNegScreen.class);
//                                intent.putExtra("FriendId", ProductDetailsBuyNeg.mHomeSingleItemEntity.getUser_id());
//                                intent.putExtra("ItemId", ProductDetailsBuyNeg.mHomeSingleItemEntity.getItem_id());
//                                intent.putExtra("isNegotiate", false);
//                                startActivity(intent);
//                                overridePendingTransition(R.anim.slide_in_right,
//                                        R.anim.slide_out_left);


                            ProductBuyNegScreen.mHomeSingleItemEntity = mHomeSingleItemEntity;
                            nextScreen(ProductBuyNegScreen.class, true);

//                            } else {
//                                ProductBuyNegScreen.isBuy = true;
//                                ProductBuyNegScreen.mHomeSingleItemEntity = mHomeSingleItemEntity;
//                                AppConstants.CARD_DET_BACK_SCREEN = getString(R.string.three);
//                                nextScreen(AddPayCard.class, false);
//                            }
                        }
                    } else {


                        DialogManager.showEditBtnAlertDialog(this, getString(R.string.app_name), getString(R.string.enter_amt_bid), getString(R.string.offer), getString(R.string.cancel), new DialogManagerTwoBtnCallback() {
                            @Override
                            public void onBtnOkClick(String mOkStr) {
                                mBid = mOkStr;
                                mPayMode = GlobalMethods.getStringValue(ProductDetailsBuyNeg.this, AppConstants.PAYMENT_MODE);
                                APIRequestHandler.getInstance().getBidRequestResponse(mHomeSingleItemEntity.getItem_id(), mBid, mHomeSingleItemEntity.getUser_id(), mHomeSingleItemEntity.getItem_quantity(), mPayMode, ProductDetailsBuyNeg.this);

                            }

                            @Override
                            public void onBtnCancelClick(String mCancelStr) {

                            }
                        });

//                        showBIDAlertDialog(this, getString(R.string.offering), new DialogManagerTwoBtnCallback() {
//                            @Override
//                            public void onBtnOkClick(String mBidEdt) {
//                                mBid = mBidEdt;
//
////                                if (GlobalMethods.getStringValue(ProductDetailsBuyNeg.this, AppConstants.BANK_DETAILS).equalsIgnoreCase(getString(R.string.one))) {
////                                    APIRequestHandler.getInstance().getBidRequestResponse(mHomeSingleItemEntity.getItem_id(), mBid, mHomeSingleItemEntity.getUser_id(), mHomeSingleItemEntity.getItem_quantity(), ProductDetailsBuyNeg.this);
////                                } else {
////                                    AppConstants.BANK_ACC_DET_BACK_SCREEN = getString(R.string.two);
//////                                    isBidding = true;
////                                    nextScreen(BankAccDetails.class, false);
////                                }
//                                mPayMode = GlobalMethods.getStringValue(ProductDetailsBuyNeg.this, AppConstants.PAYMENT_MODE);
//
////                                if (GlobalMethods.getStringValue(ProductDetailsBuyNeg.this, AppConstants.PAYMENT_MODE).equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
////
////                                    DialogManager.showPayModeAlertDialog(ProductDetailsBuyNeg.this, true, new DialogManagerModeCallback() {
////                                        @Override
////                                        public void onContinueClick(String conti) {
////                                            mPayMode = conti;
////
////
////                                            GlobalMethods.storeValuetoPreference(ProductDetailsBuyNeg.this,
////                                                    GlobalMethods.STRING_PREFERENCE,
////                                                    AppConstants.PAYMENT_MODE, conti);
////                                            APIRequestHandler.getInstance().getBidRequestResponse(mHomeSingleItemEntity.getItem_id(), mBid, mHomeSingleItemEntity.getUser_id(), mHomeSingleItemEntity.getItem_quantity(), mPayMode, ProductDetailsBuyNeg.this);
////
////                                        }
////
////                                        @Override
////                                        public void onCancelClick(String cancel) {
////                                            mPayMode = cancel;
////                                            GlobalMethods.storeValuetoPreference(ProductDetailsBuyNeg.this,
////                                                    GlobalMethods.STRING_PREFERENCE,
////                                                    AppConstants.PAYMENT_MODE, cancel);
////                                            APIRequestHandler.getInstance().getBidRequestResponse(mHomeSingleItemEntity.getItem_id(), mBid, mHomeSingleItemEntity.getUser_id(), mHomeSingleItemEntity.getItem_quantity(), mPayMode, ProductDetailsBuyNeg.this);
////
////                                        }
////                                    });
////
////                                } else {
//                                APIRequestHandler.getInstance().getBidRequestResponse(mHomeSingleItemEntity.getItem_id(), mBid, mHomeSingleItemEntity.getUser_id(), mHomeSingleItemEntity.getItem_quantity(), mPayMode, ProductDetailsBuyNeg.this);
////                                }
//                            }

//                            @Override
//                            public void onBtnCancelClick(String mBidEdt) {
//
//                            }
//                        });
                    }
                }
                break;
            case R.id.footer_two_btn:
//                if (mFooterTwoBtn.getText().toString().trim().equalsIgnoreCase(getString(R.string.upload_txt))) {
//                    nextScreen(DropboxUpload.class, false);
//                } else if (mFooterTwoBtn.getText().toString().trim().equalsIgnoreCase(getString(R.string.finish_c))) {
//                    nextScreen(InspectModeScreen.class, false);
//                } else if (mFooterTwoBtn.getText().toString().trim().equalsIgnoreCase(getString(R.string.code_c))) {
//                    nextScreen(BuyerCodeScreen.class, false);
//                } else if (mFooterTwoBtn.getText().toString().trim().equalsIgnoreCase(getString(R.string.start_c))) {
//                    mFooterTwoBtn.setText(getString(R.string.started_c));
//                    mFooterTwoBtn.setClickable(false);
//                }
//                if (mHomeSingleItemEntity.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID
//                        (ProductDetailsBuyNeg.this))) {
//                    DialogManager.showValidationDialog(ProductDetailsBuyNeg.this, getString(R
//                            .string.your_item));
//                } else {
                NegotiationChatRoom.mHomeSingleItemEntity = mHomeSingleItemEntity;
                AppConstants.NEGO_FRIEND_ID = ProductDetailsBuyNeg.mHomeSingleItemEntity.getUser_id();
                AppConstants.NEGO_BID_ID = "";
                AppConstants.NEGO_USER_ID = GlobalMethods.getUserID(this);
                AppConstants.NEGO_ITEM_ID = ProductDetailsBuyNeg.mHomeSingleItemEntity.getItem_id();
                AppConstants.NEGO_ITEM_NOTI = "";
                AppConstants.NEGO_ITEM_QTY = getString(R.string.one);
                ProductBuyNegScreen.isBuy = false;
                if (mHomeSingleItemEntity.getItem_type().equalsIgnoreCase(getString(R.string.two))) {


                    DialogManager.showBaseTwoBtnDialog(ProductDetailsBuyNeg.this, getString(R
                                    .string.app_name), getString(R.string.do_you_neg),
                            getString(R
                                    .string
                                    .yes), getString(R.string.no), new DialogManagerTwoBtnCallback() {
                                @Override
                                public void onBtnOkClick(String mOkStr) {
                                    AppConstants.CHAT_BACK = AppConstants.HOME_SCREEN;
                                    nextScreen(NegotiationChatRoom.class, true);
                                }

                                @Override
                                public void onBtnCancelClick(String mCancelStr) {

                                }
                            });


//                    DialogManager.showNegAlertDialog(ProductDetailsBuyNeg.this, getString(R.string.nego), new DialogMangerOkCallback() {
//                        @Override
//                        public void onOkClick() {
//
//                            AppConstants.CHAT_BACK = AppConstants.HOME_SCREEN;
//
////                            if (mHomeSingleItemEntity.getPayment_mode().equalsIgnoreCase(getString(R.string.three)) || GlobalMethods.getStringValue(ProductDetailsBuyNeg.this, AppConstants.CARD_DETAILS).equalsIgnoreCase(getString(R.string.one))) {
//
//                            nextScreen(NegotiationChatRoom.class, true);
//
////                            } else {
////
////                                AppConstants.CARD_DET_BACK_SCREEN = getString(R.string.three);
////                                nextScreen(AddPayCard.class, false);
////
////                            }
//                        }
//                    });

                } else {
                    ProductBuyNegScreen.mHomeSingleItemEntity = mHomeSingleItemEntity;
                    nextScreen(ProductBuyNegScreen.class, true);
//                    nextScreen(NegotiationChatRoom.class, true);
                }


//                    if (mHomeSingleItemEntity.getItem_type().equalsIgnoreCase(getString(R.string.one))) {
//
//                        if (GlobalMethods.getStringValue(ProductDetailsBuyNeg.this, AppConstants.CARD_DETAILS).equalsIgnoreCase(getString(R.string.one))) {
//
////                            Intent intent = new Intent(ProductDetailsBuyNeg.this, ProductBuyNegScreen.class);
////                            intent.putExtra("FriendId", ProductDetailsBuyNeg.mHomeSingleItemEntity.getUser_id());
////                            intent.putExtra("ItemId", ProductDetailsBuyNeg.mHomeSingleItemEntity.getItem_id());
////                            intent.putExtra("isNegotiate", true);
////                            startActivity(intent);
////                            overridePendingTransition(R.anim.slide_in_right,
////                                    R.anim.slide_out_left);
//
//
//                            nextScreen(ProductBuyNegScreen.class, true);
//                        } else {
////                            isBuyNeg = getString(R.string.three);
//
//                            AppConstants.CARD_DET_BACK_SCREEN = getString(R.string.three);
//                            nextScreen(AddPayCard.class, false);
//                        }
//                    } else {
//                        DialogManager.showNegAlertDialog(ProductDetailsBuyNeg.this, getString(R.string.nego_c), new DialogMangerOkCallback() {
//                            @Override
//                            public void onOkClick() {
//                                if (mHomeSingleItemEntity.getItem_type().equalsIgnoreCase(getString(R.string.two))) {
//                                    NegotiationChatRoom.mHomeSingleItemEntity = mHomeSingleItemEntity;
//                                    AppConstants.NEGO_FRIEND_ID = ProductDetailsBuyNeg.mHomeSingleItemEntity.getUser_id();
//                                    AppConstants.NEGO_ITEM_ID = ProductDetailsBuyNeg.mHomeSingleItemEntity.getItem_id();
//                                    AppConstants.NEGO_ITEM_NOTI = "";
//                                    AppConstants.NEGO_ITEM_QTY = getString(R.string.one);
//                                    if (GlobalMethods.getStringValue(ProductDetailsBuyNeg.this, AppConstants.CARD_DETAILS).equalsIgnoreCase(getString(R.string.one))) {
//
//                                        ProductBuyNegScreen.isBuy = false;
//                                        AppConstants.CHAT_BACK = AppConstants.HOME_SCREEN;
//                                        nextScreen(NegotiationChatRoom.class, true);
//                                    } else {
//
//                                        ProductBuyNegScreen.isBuy = false;
//                                        AppConstants.CARD_DET_BACK_SCREEN = getString(R.string.three);
//                                        nextScreen(AddPayCard.class, false);
//                                    }
//                                }
//
//                            }
//                        });
//                    }
//                }
                break;

            case R.id.person_name_txt:

                OtherUserProfile.mOtherUSerID = mHomeSingleItemEntity.getUser_id();
                nextScreen(OtherUserProfile.class, false);
                break;

            case R.id.header_right_btn_lay:
//                if (mHomeSingleItemEntity.getPreview_url() != null && !mHomeSingleItemEntity.getPreview_url().equalsIgnoreCase("")) {
//                    Uri uri;
//                    Intent url;
//                    uri = Uri.parse(getString(R.string.http) + mHomeSingleItemEntity.getPreview_url());
//                    url = new Intent(android.content.Intent.ACTION_VIEW, uri);
//                    startActivity(url);
//                }


                BlockUserScreen.mReportUserTxt = "\n\n\tName : " + mHomeSingleItemEntity
                        .getOwner_first_name() + "\n\tID : " +
                        mHomeSingleItemEntity.getOwner_email();
                BlockUserScreen.mReportItemTxt = "\n\n\t" + getString(R.string.univ_name) + " " +
                        mHomeSingleItemEntity.getOwner_university_name() + "\n\t" + GlobalMethods
                        .isGoodsService(this, mHomeSingleItemEntity.getItem_type()) + " : " +
                        mHomeSingleItemEntity.getItem_name() + "\n\t" + getString(R.string
                        .price_cost) + "  $ " + GlobalMethods.afterTwoPointVal
                        (mHomeSingleItemEntity.getItem_cost()) + "\n\t" + getString(R.string.item) + " "
                        + getString(R.string.no_qut) + " " + mHomeSingleItemEntity
                        .getItem_quantity() + "\n\t" + getString(R.string.item) + " " + getString(R.string
                        .desc) + " " + mHomeSingleItemEntity.getItem_description();

                nextScreen(BlockUserScreen.class, false);
                break;
            case R.id.ph_num_txt:

                isPh = true;

                APIRequestHandler.getInstance().getPartnerCountResponse(mHomeSingleItemEntity.getUser_id(), mHomeSingleItemEntity.getItem_id(), "", mHomeSingleItemEntity.getPhone_number(), this);
                break;
            case R.id.web_txt:
                isPh = false;
                APIRequestHandler.getInstance().getPartnerCountResponse(mHomeSingleItemEntity.getUser_id(), mHomeSingleItemEntity.getItem_id(), mHomeSingleItemEntity.getWebsite(), "", this);

                break;
        }

    }


    @Override
    public void onBackPressed() {
        finishScreen();
    }

    private void paymentScreenCall() {
        AppConstants.PAYPAL_BUY_ID = GlobalMethods.getUserID(ProductDetailsBuyNeg.this);
        AppConstants.PAYPAL_BID_ID = AppConstants.FAILURE_CODE;

        AppConstants.PAYPAL_ITEM_ID = mHomeSingleItemEntity.getItem_id();

        AppConstants.PAYPAL_ITEM_QTY = mItemQuantity;
        AppConstants.PAYPAL_ITEM_COST = mItemPrice;
        AppConstants.PAYPAL_SER_FEES = mServicePrice;
        AppConstants.PAYPAL_TOT_COST = mTotalPrice;
        AppConstants.PAYPAL_NEG = AppConstants.FAILURE_CODE;
        AppConstants.PAYPAL_NEG_ID = "";
        AppConstants.PAYPAL_TIPS = AppConstants.FAILURE_CODE;
        AppConstants.PAYPAL_ITEM_DELV_TYPE = mHomeSingleItemEntity.getDelivery_type();
        AppConstants.PAYPAL_USER_ID = mHomeSingleItemEntity.getUser_id();
        AppConstants.PAYPAL_USER_NAME = mHomeSingleItemEntity.getUser_first_name();
        AppConstants.PAYPAL_ITEM_TYPE = mHomeSingleItemEntity.getItem_type();
        AppConstants.PAYPAL_ITEM_NAME = mHomeSingleItemEntity.getItem_name();

        nextScreen(PaymentPaypalStripScreen.class, false);
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


            LayoutInflater inflater = LayoutInflater.from(ProductDetailsBuyNeg.this);
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.image_layout_produt_details, container, false);

            final ImageView product_img = (ImageView) layout.findViewById(R.id.product_img);
//            if (mImagesList.get(position) != null && mImagesList.get(position).length() > 0) {
//                Glide.with(ProductDetailsBuyNeg.this)
//                        .load(mImagesList.get(position)).placeholder(R.drawable.product_bg).into
//                        (product_img);
//
//                product_img.setTag(mImagesList.get(position));
//            }


            runOnUiThread(new Runnable() {
                public void run() {
                    Glide.with(ProductDetailsBuyNeg.this)
                            .load(mImagesList.get(position)).asBitmap().placeholder(R.drawable.product_bg).fitCenter().into(product_img);
                    product_img.setVisibility(View.VISIBLE);
                }
            });

            product_img.setTag(mImagesList.get(position));
            product_img.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    String tag = (String) view.getTag();
                    if (tag != null) {
                        DialogManager.ImageViewer(ProductDetailsBuyNeg.this,
                                tag);
                    }

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

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);

        if (responseObj instanceof PayForItemResponse) {

            PayForItemResponse payForItemEntity = (PayForItemResponse) responseObj;
            if (payForItemEntity.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                DialogManager.showBasicAlertDialog(ProductDetailsBuyNeg.this, payForItemEntity.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {
//                        previousScreen(HomeScreenActivity.class, true);
                        previousScreen(DashboardScreen.class, true);
                    }
                });

            } else {
                DialogManager.showBasicAlertDialog(ProductDetailsBuyNeg.this, payForItemEntity.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {

                    }
                });
            }
        } else if (responseObj instanceof CommonModelResponse) {

            CommonModelResponse bidItemEntity = (CommonModelResponse) responseObj;
            if (bidItemEntity.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                {
                    DialogManager.showBasicAlertDialog(ProductDetailsBuyNeg.this, bidItemEntity.getMessage(), new DialogMangerOkCallback() {
                        @Override
                        public void onOkClick() {
//                            previousScreen(HomeScreenActivity.class, true);
                            previousScreen(DashboardScreen.class, true);
                        }
                    });
                }

            } else {
                DialogManager.showBasicAlertDialog(ProductDetailsBuyNeg.this, bidItemEntity.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {

                    }
                });
            }
        } else if (responseObj instanceof BuyItemResponse) {
            BuyItemResponse mCommonResponse = (BuyItemResponse) responseObj;

            if (mCommonResponse.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

//                if (mHomeSingleItemEntity.getPayment_mode().equalsIgnoreCase(getString(R.string.three))) {

//                callPaypalPayment(mCommonResponse.getResult());

//                } else {
//                    APIRequestHandler.getInstance().payForItemResponse(ProductDetailsBuyNeg
//                                    .mHomeSingleItemEntity.getItem_id(),
//                            getString(R.string.one), ProductDetailsBuyNeg.this);
//
//                }
            } else if (mCommonResponse.getResponse_code().equalsIgnoreCase(getString(R.string.two))) {

//                if (mHomeSingleItemEntity.getPayment_mode().equalsIgnoreCase(getString(R.string.three))) {

//                callPaypalPayment(mCommonResponse.getResult());

//                } else {
//                    AppConstants.CARD_DET_BACK_SCREEN = getString(R.string.three);
//                    nextScreen(AddPayCard.class, false);
//                }
            } else {

                DialogManager.showBasicAlertDialog(ProductDetailsBuyNeg.this, mCommonResponse.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {

                    }
                });
            }

        } else if (responseObj instanceof PaypalPayResponse) {
            PaypalPayResponse mPaypalResponse = (PaypalPayResponse) responseObj;

            if (mPaypalResponse.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                DialogManager.showBasicAlertDialog(ProductDetailsBuyNeg.this, mPaypalResponse.getResult().getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {
//                        previousScreen(HomeScreenActivity.class, true);
                        previousScreen(DashboardScreen.class, true);

                    }
                });

            } else {
                DialogManager.showBasicAlertDialog(ProductDetailsBuyNeg.this, mPaypalResponse.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {

                    }
                });
            }

        } else if (responseObj instanceof CommonPhResponse) {
            CommonPhResponse mPhWebResponse = (CommonPhResponse) responseObj;

            if (mPhWebResponse.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                if (isPh == true) {
                    Uri call;
                    call = Uri.parse(getString(R.string.tel) + mHomeSingleItemEntity.getPhone_number());
                    Intent mCall = new Intent(Intent.ACTION_CALL, call);

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(mCall);
                } else {

                    Uri uri;
                    Intent url;
                    uri = Uri.parse(getString(R.string.http) + mHomeSingleItemEntity.getWebsite());
                    url = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(url);

                }
            } else {
                DialogManager.showBasicAlertDialog(ProductDetailsBuyNeg.this, mPhWebResponse.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {

                    }
                });
            }

        }

    }

//    private void callPaypalPayment(BuyItemEntityResponse paypalRes) {
//
////        mItemName = paypalRes.getItem_name();
//        mItemQuantity = paypalRes.getItem_quantity();
//        mItemPrice = paypalRes.getItem_cost();
//        mServicePrice = paypalRes.getProcess_fee();
//        mTotalPrice = paypalRes.getTotal_cost();
//
//        PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(mTotalPrice), "USD", GlobalMethods.isGoodsService(this, mHomeSingleItemEntity.getItem_type()) + " : " + mHomeSingleItemEntity.getItem_name(),
//                PayPalPayment.PAYMENT_INTENT_SALE);
//        Intent paypalIntent = new Intent(ProductDetailsBuyNeg.this, PaymentActivity.class);
//        paypalIntent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
//        startActivityForResult(paypalIntent, AppConstants.REQUEST_PAYPAL_PAYMENT);
//    }

//    void showBIDAlertDialog(final Context mContext, String mTitle, final DialogManagerTwoBtnCallback mDiainterface) {
//
//
//        final Dialog mDialog = DialogManager.getDialog(mContext, R.layout.popup_alert_bid);
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        Window window = mDialog.getWindow();
//        mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//        lp.copyFrom(window.getAttributes());
//        ViewGroup mPopup;
//        mPopup = (ViewGroup) mDialog.findViewById(R.id.popup_parent_lay);
//        setupUI(mPopup);
////This makes the dialog take up the full width
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        window.setAttributes(lp);
//        mDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
//        RelativeLayout mCloseLay = (RelativeLayout) mDialog.findViewById(R.id.close_img_lay);
//        TextView mTitleTxt = (TextView) mDialog.findViewById(R.id.header_txt);
//        final EditText mBidEdt = (EditText) mDialog
//                .findViewById(R.id.bid_edt);
//        mBidEdt.requestFocus();
//        Button mYesBtn = (Button) mDialog.findViewById(R.id.footer_one_btn);
//        Button mNoBtn = (Button) mDialog.findViewById(R.id.footer_two_btn);
//
//        RelativeLayout mFooterOneLay = (RelativeLayout) mDialog.findViewById(R.id.footer_one_lay);
//        RelativeLayout mFooterTwoLay = (RelativeLayout) mDialog.findViewById(R.id.footer_two_lay);
//
//        mFooterOneLay.setVisibility(View.VISIBLE);
//        mFooterTwoLay.setVisibility(View.VISIBLE);
//
//        mYesBtn.setBackgroundColor(mContext.getResources().getColor(R.color.blue_btn_bg));
//        mNoBtn.setBackgroundColor(mContext.getResources().getColor(R.color.white));
//        mNoBtn.setTextColor(mContext.getResources().getColor(R.color.blue_btn_bg));
//
//        mTitleTxt.setText(mTitle);
//        mYesBtn.setText(mContext.getString(R.string.offer_nw));
//        mNoBtn.setText(mContext.getString(R.string.cancel));
//        mYesBtn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                String mBidd = mBidEdt.getText().toString();
////                if (!mBidd.equalsIgnoreCase("")) {
//                if (mBidd.length() > 0 && Integer.valueOf(mBidd) > 0) {
//                    mDiainterface.onBtnOkClick(mBidd);
//                    hideSoftKeyboard(mBidEdt);
//                    mDialog.dismiss();
//                } else {
//                    DialogManager.showBasicAlertDialog(mContext, mContext.getString(R.string.app_name), mContext.getString(R.string.enter_bidd_amt), new DialogMangerOkCallback() {
//                        @Override
//                        public void onOkClick() {
//
//                        }
//                    });
//                }
//            }
//        });
//        mNoBtn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                hideSoftKeyboard(mBidEdt);
//                mDialog.dismiss();
//            }
//        });
//        mCloseLay.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                hideSoftKeyboard(mBidEdt);
//                mDialog.dismiss();
//
//            }
//        });
//        mDialog.show();
//    }


}


