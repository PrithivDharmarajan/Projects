package com.bridgellc.bridge.ui;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
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
import com.bridgellc.bridge.entity.HomeSingleItemEntity;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.GlobalMethods;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by USER on 4/4/2016.
 */
public class OtherUserProductDetails extends BaseActivity implements View.OnClickListener {
    public static RelativeLayout mRatLay, mBottomLay;
    public static int mFooterBtnCount;
    public static HomeSingleItemEntity mHomeSingleItemEntity;
    private RatingBar mRatingBar;
    private ImageView mVerifyImg;
    private ViewPager mImagePager;
    private TextView mPersonNameTxt, mPersonUnivNameTxt, mItemNameTxt, mItemQuantityTxt, mItemPriceTxt, mItemServicesPriceTxt, mItemTotalPriceTxt, mItemCategoryTxt, mItemDeliveryTypeTxt, mItemConditionTxt, mItemDescTxt ,mItemWebTxt, mItemPhTxt;
    private LinearLayout mCertPartLay,mPartWebPhLay;
    private ArrayList<String> mImagesList = new ArrayList<>();
    private int page_count = 0;

    private ImageView mSlidepointerOne, mSlidepointerTwo,
            mSlidepointerThree;
    private SimpleDateFormat mTargetDateTime = new SimpleDateFormat("MMMM dd, yyyy hh:mm aa", Locale.US);
    private SimpleDateFormat mServerTimeForm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    private GradientDrawable bgShapeGreen,bgShapeWhite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details_screen);
        initComponents();
    }

    private void initComponents() {
        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);

        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);

        mFooterOneLay = (RelativeLayout) findViewById(R.id.footer_one_lay);
        mFooterTwoLay = (RelativeLayout) findViewById(R.id.footer_two_lay);
        mFooterThreeLay = (RelativeLayout) findViewById(R.id.footer_three_lay);

        mImagePager = (ViewPager) findViewById(R.id.uploadPager);
        mImagePager.setAdapter(new MyPagerAdapter());

        mBottomLay = (RelativeLayout) findViewById(R.id.bottom_lay);

        mFooterOneBtn = (Button) findViewById(R.id.footer_one_btn);
        mFooterTwoBtn = (Button) findViewById(R.id.footer_two_btn);
        mFooterThreeBtn = (Button) findViewById(R.id.footer_three_btn);


        mSlidepointerOne = (ImageView) findViewById(R.id.slidepointer_one);
        mSlidepointerTwo = (ImageView) findViewById(R.id.slidepointer_two);
        mSlidepointerThree = (ImageView) findViewById(R.id.slidepointer_three);

        mRatLay = (RelativeLayout) findViewById(R.id.rat_lay);
        mFooterBtnCount = 0;
        mBottomLay.setVisibility(View.GONE);


        mRatingBar = (RatingBar) findViewById(R.id.fav_ratingbar);
        mVerifyImg = (ImageView) findViewById(R.id.verify_img);
        mPersonNameTxt = (TextView) findViewById(R.id.person_name_txt);
        mPersonUnivNameTxt = (TextView) findViewById(R.id.person_univ_name_txt);
        mSlidepointerOne = (ImageView) findViewById(R.id.slidepointer_one);
        mSlidepointerTwo = (ImageView) findViewById(R.id.slidepointer_two);
        mSlidepointerThree = (ImageView) findViewById(R.id.slidepointer_three);
        mCertPartLay = (LinearLayout) findViewById(R.id.cert_part_lay);


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

        bgShapeGreen=(GradientDrawable)getResources().getDrawable(R.drawable.round);
        bgShapeWhite=(GradientDrawable)getResources().getDrawable(R.drawable.round);

        bgShapeGreen.setColor(getResources().getColor(R.color.green));
        bgShapeWhite.setColor(getResources().getColor(R.color.white));


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

                mSlidepointerOne.setBackground(mCurrentPos==0?bgShapeGreen:bgShapeWhite);
                mSlidepointerTwo.setBackground(mCurrentPos==1?bgShapeGreen:bgShapeWhite);
                mSlidepointerThree.setBackground(mCurrentPos==2?bgShapeGreen:bgShapeWhite);

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
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

    private void populateData() {
        String personName = "", personUnivName = "", otherpersonID = "", itemName = "", itemQuantity = "", itemPrice = "", itemSolidPrice = "", itemTotalPrice = "", itemCategory = "", itemDeliveryType = "", itemCondition = "", conditionType = "", itemDesc = "",itemPh="",itemWeb="";
        float personRating;
        boolean ispersonVerified,iscertPart;

            itemName = GlobalMethods.isGoodsService(this, mHomeSingleItemEntity.getItem_type()) + " : " + mHomeSingleItemEntity.getItem_name();
//        itemName = getString(R.string.item_name) + " " + mHomeSingleItemEntity.getItem_name();
        itemCategory = getString(R.string.category) + " " + mHomeSingleItemEntity.getCategory_name();
        itemDeliveryType = getString(R.string.delivery_type) + " " + GlobalMethods.getDeliveryType(this, mHomeSingleItemEntity.getDelivery_type());
        conditionType = (mHomeSingleItemEntity.getItem_mode().equalsIgnoreCase(getString(R.string.one)) && mHomeSingleItemEntity.getItem_type().equalsIgnoreCase(getString(R.string.two))) ? getString(R.string.skill_level) : getString(R.string.condition);
        itemCondition = conditionType + " " + mHomeSingleItemEntity.getItem_condition();
        itemDesc = getString(R.string.desc) + " " + mHomeSingleItemEntity.getItem_description();
        iscertPart = GlobalMethods.isCertifiedPartner(this, mHomeSingleItemEntity.getItem_type(), mHomeSingleItemEntity.getSeller_partner());



        personName = mHomeSingleItemEntity.getOwner_first_name();
        personUnivName = getString(R.string.univ_name) + " " + mHomeSingleItemEntity.getOwner_university_name();
        otherpersonID = mHomeSingleItemEntity.getUser_id();
        personRating = GlobalMethods.isRating(mHomeSingleItemEntity.getUser_rating());
        ispersonVerified = GlobalMethods.isUserVerified(this, mHomeSingleItemEntity.getUser_verified());
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
//
//        }

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


        if (GlobalMethods.isSeller(this, mHomeSingleItemEntity.getUser_id())) {
            personName = getString(R.string.your_item);
        }


        if (GlobalMethods.isCertifiedPartner(this, mHomeSingleItemEntity.getItem_type(), mHomeSingleItemEntity.getSeller_partner())&&mHomeSingleItemEntity.getItem_type().equalsIgnoreCase(getString(R.string.two))) {
            mPartWebPhLay.setVisibility(View.VISIBLE);
            itemPh = mHomeSingleItemEntity.getPhone_number();
            itemWeb = mHomeSingleItemEntity.getWebsite();
        }




        //Data Set
        mRatingBar.setRating(personRating);

        mPersonUnivNameTxt.setText(personUnivName);
        mPersonNameTxt.setText(personName);
        mItemNameTxt.setText(itemName);
        mItemQuantityTxt.setText(itemQuantity);
        mItemPriceTxt.setText(itemPrice);
        mItemServicesPriceTxt.setText(itemSolidPrice);
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

        if (ispersonVerified) {
            mVerifyImg.setVisibility(View.VISIBLE);
        }

        if (iscertPart) {
            mCertPartLay.setVisibility(View.VISIBLE);
        }

//        if (GlobalMethods.isSeller(OtherUserProductDetails.this, mHomeSingleItemEntity.getUser_id())) {
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


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.footer_one_btn:

                break;
            case R.id.footer_two_btn:

                break;
            case R.id.footer_three_btn:

                break;

            case R.id.person_name_txt:

                break;
        }

    }

    @Override
    public void onBackPressed() {
        finishScreen();
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


            LayoutInflater inflater = LayoutInflater.from(OtherUserProductDetails.this);
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.image_layout_produt_details, container, false);

            final ImageView product_img = (ImageView) layout.findViewById(R.id.product_img);
//            Glide.with(OtherUserProductDetails.this)
//                    .load(mImagesList.get(position)).placeholder(R.drawable.product_bg).into(product_img);
//
//            product_img.setTag(mImagesList.get(position));


            runOnUiThread(new Runnable() {
                public void run() {
                    Glide.with(OtherUserProductDetails.this)
                            .load(mImagesList.get(position)).asBitmap().placeholder(R.drawable.product_bg).fitCenter().into(product_img);
                    product_img.setVisibility(View.VISIBLE);
                }
            });

            product_img.setTag(mImagesList.get(position));
            product_img.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    String tag = (String) view.getTag();
                    DialogManager.ImageViewer(OtherUserProductDetails.this,
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

}
