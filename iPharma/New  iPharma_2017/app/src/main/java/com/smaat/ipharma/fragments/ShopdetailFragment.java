package com.smaat.ipharma.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smaat.ipharma.R;
import com.smaat.ipharma.apiinterface.APIRequestHandler;
import com.smaat.ipharma.entity.MapPropertyEntity;
import com.smaat.ipharma.entity.RateResponseEntity;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.utils.AppConstants;
import com.smaat.ipharma.utils.DialogManager;
import com.smaat.ipharma.utils.GlobalMethods;
import com.smaat.ipharma.utils.ProfileImageSelectionUtil;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.smaat.ipharma.utils.AppConstants.FROMHISTORY;
import static com.smaat.ipharma.utils.GlobalMethods.CalculationByDistancedouble;

/**
 * Created by admin on 1/23/2017.
 */

public class ShopdetailFragment extends BaseFragment {

    @BindView(R.id.shop_image)
    ImageView m_shopimage;

    @BindView(R.id.pharmacy_name)
    TextView m_shopName;

    @BindView(R.id.address_txt)
    TextView m_shopAddress;

    @BindView(R.id.review_count)
    TextView m_reviewCount;

    @BindView(R.id.distance_km)
    TextView m_distance;


    /*@BindView(R.id.place_order)
    Button m_placeOrder;*/

    @BindView(R.id.review_rating_bar)
    RatingBar m_Rating_view;

    @BindView(R.id.pharmacy_image)
    ImageView m_PhamarcyImage;

    @BindView(R.id.openmail)
    ImageView m_openmail;

    @BindView(R.id.callbtn)
    ImageView m_call_btn;

    @BindView(R.id.clickwebsite)
    ImageView m_click_website;

    @BindView(R.id.clicklocation)
    ImageView m_click_location;

    @BindView(R.id.pharmacy_rat)
    LinearLayout m_Rating_Layout;

    /*@BindView(R.id.review_rating_bar)
    RatingBar m_Rating_view;*/

    @BindView(R.id.img_fav)
    ImageView m_fav_icon;

    MapPropertyEntity mpentity;


    private String mUserID = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.ui_shopdetail, container,
                false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.bind(this, rootview);
        setupUI(rootview);
        mUserID = ((String) GlobalMethods.getValueFromPreference(getActivity(),
                GlobalMethods.STRING_PREFERENCE, AppConstants.USER_ID));
        initcomponents();

        return rootview;
    }

    private void initcomponents() {
        mpentity =AppConstants.PharmacyDetails;

        Log.e("getProfilePic","getProfilePic"+mpentity.getProfilePic());
        Glide.with(getActivity())
                .load(AppConstants.ADMIN_BASE_URL+mpentity.getProfilePic())
                .placeholder(R.drawable.shopdetail_placeholder)
                .into(m_shopimage);


        String url = "";
        if (mpentity.getShopIcon() != null && !mpentity.getShopIcon().trim().isEmpty()) {
            url = AppConstants.ADMIN_BASE_URL + mpentity.getShopIcon();
        } else {
            url = AppConstants.ADMIN_BASE_URL + mpentity.getProfilePic();
        }

        Glide.with(getActivity())
                .load(url)
                .placeholder(R.drawable.popup_logo)
                .into(m_PhamarcyImage);

        m_shopName.setText(GlobalMethods.capitalizeString(mpentity.getShopName()));
        m_shopAddress.setText(GlobalMethods.capitalizeString(mpentity.getAddress()));
        if(mpentity.getTotalReviews()!=null)
        {
            if(Integer.parseInt(mpentity.getTotalReviews())==0||Integer.parseInt(mpentity.getTotalReviews())==1)
            {
                m_reviewCount.setText(mpentity
                        .getTotalReviews()+" "+"Review");
            }else{
                m_reviewCount.setText(mpentity
                        .getTotalReviews()+" "+getString(R.string.reviews));
            }
        }else{
            m_reviewCount.setText(mpentity
                    .getTotalReviews()+" "+"Review");
        }


        /*m_reviewCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        String communication_address = mpentity
                .getAddress()
                + ", "
                + mpentity.getArea()
                ;
        m_shopAddress.setText(communication_address);

        m_Rating_view.setRating(mpentity.getAvgRating());

        /*if (mpentity.getDistance().contains(".")) {
            DecimalFormat distance_roundoff = new DecimalFormat("#.##");
            m_distance.setText(Double.valueOf(distance_roundoff
                    .format(Double.valueOf(mpentity
                            .getDistance())))
                    + " " + getString(R.string.km_away));
        } else {
            m_distance.setText(mpentity
                    .getDistance() + " " + getString(R.string.km_away));
        }*/
        m_distance.setText(CalculationByDistancedouble(mpentity.getLatitude(),mpentity.getLongitude())+ " " + getActivity().getString(R.string.km_away));


        if(mpentity.getIsFav()!=null)
        {
            if(mpentity.getIsFav().equalsIgnoreCase("1"))
            {
                m_fav_icon.setTag("1");
                m_fav_icon.setImageResource(R.drawable.heart_red);
            }else{
                m_fav_icon.setTag("2");
                m_fav_icon.setImageResource(R.drawable.heart_grey);
            }
        }else{
            m_fav_icon.setTag("1");
            m_fav_icon.setImageResource(R.drawable.heart_red);
            m_fav_icon.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.openmail, R.id.callbtn, R.id.clickwebsite, R.id.clicklocation,R.id.img_fav,R.id.review_count,R.id.pharmacy_rat/*,R.id.shop_image*/})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.openmail:
                if (mpentity.getEmail().equalsIgnoreCase(null)
                        || mpentity.getEmail().equalsIgnoreCase("")) {
                    DialogManager.showMsgPopup(getActivity(),"",
                            getString(R.string.email_not_available));
                } else {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
                            Uri.fromParts("mailto", mpentity.getEmail(),
                                    null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_name));
                    getActivity().startActivity(
                            Intent.createChooser(emailIntent, "Send email..."));
                }



                break;
            /*case R.id.shop_image:
                AppConstants.SHOP_LATITUDE = mpentity.getLatitude();
                AppConstants.SHOP_LONGITUDE = mpentity.getLongitude();
                AppConstants.SHOP_NAME = mpentity.getShopName();
                Intent i = new Intent(getActivity(),ViewShopMapFragment.class);
                i.putExtra("ShowImage",true);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_to_bottom);
                break;*/
            case R.id.callbtn:
                if (mpentity.getMobile().toString().equalsIgnoreCase(null)
                        || mpentity.getMobile().equalsIgnoreCase("")) {
                    DialogManager.showMsgPopup(getActivity(),"",getString(R.string.ph_not_available));
                } else {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" +mpentity.getMobile()));
                    startActivity(callIntent);
                }

                break;
            case R.id.clickwebsite:
                if (mpentity.getWebsite().equalsIgnoreCase(null)
                        || mpentity.getWebsite().equalsIgnoreCase("")) {
                    DialogManager.showMsgPopup(getActivity(),"",
                            getString(R.string.website_not_available));
                } else {
                    String url = mpentity.getWebsite().toString();
                    if (!url.startsWith("http://") && !url.startsWith("https://"))
                        url = "http://" + url;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }


                break;
            case R.id.clicklocation:

                AppConstants.SHOP_LATITUDE = mpentity.getLatitude();
                AppConstants.SHOP_LONGITUDE = mpentity.getLongitude();
                AppConstants.SHOP_NAME = mpentity.getShopName();
                Intent i = new Intent(getActivity(),ViewShopMapFragment.class);
                i.putExtra("ShowImage",false);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_to_bottom);
                break;
            case R.id.review_count:
                AppConstants.PharmacyDetails = mpentity;
                ((HomeScreen) getActivity()).pushFragment(new ReviewFragment());
                break;
            case R.id.pharmacy_rat:
                AppConstants.PharmacyDetails = mpentity;
                ((HomeScreen) getActivity()).pushFragment(new ReviewFragment());
                break;
            case R.id.img_fav:
                if(!GlobalMethods.movetologinscreen(getActivity()))
                {
                    if(v.getTag().toString().equalsIgnoreCase("1"))
                    {
                        APIRequestHandler.getInstance().addFavourite(mpentity.getPharmacyID(), mUserID, "2",
                                ShopdetailFragment.this);
                        //DialogManager.showToast(getActivity(),getString(R.string.rem_fav));
                        m_fav_icon.setImageResource(R.drawable.heart_grey);
                        m_fav_icon.setTag("2");
                        mpentity.setIsFav("2");
                    }else{
                        APIRequestHandler.getInstance().addFavourite(mpentity.getPharmacyID(), mUserID, "1",
                                ShopdetailFragment.this);
                        //DialogManager.showToast(getActivity(),getString(R.string.add_fav));
                        m_fav_icon.setImageResource(R.drawable.heart_red);
                        m_fav_icon.setTag("1");
                        mpentity.setIsFav("1");
                    }
                }

                break;

        }
    }
    @Override
    public void onResume() {
        super.onResume();
        ((HomeScreen) getActivity()).setToolbarTitle(getString(R.string.or_now),getString(R.string.plce_order));
    }

    private void callPharmacyRatingService(float rating_value) {

        APIRequestHandler.getInstance().rateShop(mpentity.getPharmacyID(), mUserID, ""+rating_value,ShopdetailFragment.this);
    }


    public void showRateDialog() {

        mDialog = new Dialog(getActivity());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_rate);

        mDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
        wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

        mDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ImageView rate = (ImageView) mDialog.findViewById(R.id.rate_it);
        ImageView cancel = (ImageView) mDialog.findViewById(R.id.cancel);
        final TextView mRating_avg_txt = (TextView) mDialog
                .findViewById(R.id.rating_avg_txt);
        final RatingBar mFav_ratingbar = (RatingBar) mDialog
                .findViewById(R.id.fav_ratingbar);
        mFav_ratingbar.setRating(5);
        final Button mRating_icons = (Button) mDialog
                .findViewById(R.id.rating_icons);

        mFav_ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            public void onRatingChanged(RatingBar ratingBar,float rating, boolean fromUser) {
                switch ((int) mFav_ratingbar.getRating()) {
                    case 1:
                        mRating_avg_txt.setText(getActivity()
                                .getString(R.string.poor));
                        mRating_icons
                                .setBackgroundResource(R.drawable.poor);
                        break;
                    case 2:
                        mRating_avg_txt.setText(getActivity()
                                .getString(R.string.bad));
                        mRating_icons.setBackgroundResource(R.drawable.bad);
                        break;
                    case 3:
                        mRating_avg_txt.setText(getActivity()
                                .getString(R.string.average));
                        mRating_icons
                                .setBackgroundResource(R.drawable.average);
                        break;
                    case 4:
                        mRating_avg_txt.setText(getActivity()
                                .getString(R.string.good));
                        mRating_icons
                                .setBackgroundResource(R.drawable.good);
                        break;
                    case 5:
                        mRating_avg_txt.setText(getActivity()
                                .getString(R.string.excellent));
                        mRating_icons
                                .setBackgroundResource(R.drawable.excellent);
                        break;
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mDialog.dismiss();
            }
        });
        rate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                float rating_value = mFav_ratingbar.getRating();
                callPharmacyRatingService(rating_value);
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }


    @Override
    public void onRequestSuccess(Object responseObj) {
        // TODO Auto-generated method stub
        super.onRequestSuccess(responseObj);
        if(responseObj instanceof RateResponseEntity)
        {
            RateResponseEntity rateresponse = (RateResponseEntity) responseObj;
            DialogManager.showMsgPopup(getActivity(),"",getString(R.string.sucess_rate));
        }
    }
}
