package com.smaat.ipharma.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.vision.text.Text;
import com.smaat.ipharma.R;
import com.smaat.ipharma.adapter.MapListAdapter;
import com.smaat.ipharma.adapter.ReviewAdapter;
import com.smaat.ipharma.apiinterface.APIRequestHandler;
import com.smaat.ipharma.entity.MapPropertyEntity;
import com.smaat.ipharma.entity.RateResponseEntity;
import com.smaat.ipharma.entity.ShowReviewMessageEntity;
import com.smaat.ipharma.entity.ShowReviewResponse;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.utils.AppConstants;
import com.smaat.ipharma.utils.DialogManager;
import com.smaat.ipharma.utils.GlobalMethods;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.smaat.ipharma.utils.AppConstants.SHOP_ID_VAL;
import static com.smaat.ipharma.utils.GlobalMethods.CalculationByDistancedouble;

/**
 * Created by admin on 1/25/2017.
 */

public class ReviewFragment extends BaseFragment {

    @BindView(R.id.reviewlistview)
    ListView mReviewList;

    @BindView(R.id.pharmacy_image)
    ImageView mPharmacy_Image;
    @BindView(R.id.pharmacy_name)
    TextView mPharmacy_Name;
    @BindView(R.id.review_rating_bar)
    RatingBar mReview_Rating;
    @BindView(R.id.review_count)
    TextView mReview_Count;
    @BindView(R.id.distance_km)
    TextView mDistance;

    @BindView(R.id.address_txt)
    TextView mAddresstxt;


    MapPropertyEntity tempData;

    ReviewAdapter mReviewAdapter;


    @BindView(R.id.refresh_btn)
    ImageView refresh_btn;

    @BindView(R.id.internetconnection_layout)
    RelativeLayout internetconnection_layout;

    @BindView(R.id.pharmacy_rat)
    LinearLayout pharmacy_rat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.ui_review_screen, container,
                false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.bind(this, rootview);
        setupUI(rootview);
        tempData = AppConstants.PharmacyDetails;
        SetValues();
        APIRequestHandler.getInstance().showReview(tempData.getPharmacyID(),internetconnection_layout,mReviewList, ReviewFragment.this);
        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIRequestHandler.getInstance().showReview(tempData.getPharmacyID(),internetconnection_layout,mReviewList, ReviewFragment.this);
            }
        });
        pharmacy_rat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showRateDialog();
            }
        });
        return rootview;
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

            public void onRatingChanged(RatingBar ratingBar,
                                        float rating, boolean fromUser) {
                switch ((int) mFav_ratingbar.getRating()) {
                    case 1:
                        mRating_avg_txt.setText(getString(R.string.poor));
                        mRating_icons
                                .setBackgroundResource(R.drawable.poor);
                        break;
                    case 2:
                        mRating_avg_txt.setText(getString(R.string.bad));
                        mRating_icons.setBackgroundResource(R.drawable.bad);
                        break;
                    case 3:
                        mRating_avg_txt.setText(getString(R.string.average));
                        mRating_icons
                                .setBackgroundResource(R.drawable.average);
                        break;
                    case 4:
                        mRating_avg_txt.setText(getString(R.string.good));
                        mRating_icons
                                .setBackgroundResource(R.drawable.good);
                        break;
                    case 5:
                        mRating_avg_txt.setText(getString(R.string.excellent));
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

    private void callPharmacyRatingService(float rating_value) {

        APIRequestHandler.getInstance().rateShop(tempData.getPharmacyID(), GlobalMethods.getUserID(getActivity()), ""+rating_value,ReviewFragment.this);
    }
    private void SetValues() {

        String url = "";
        if (tempData.getShopIcon() != null && !tempData.getShopIcon().trim().isEmpty()) {
            url = AppConstants.ADMIN_BASE_URL + tempData.getShopIcon();
        } else {
            url = AppConstants.ADMIN_BASE_URL + tempData.getProfilePic();
        }

        Glide.with(getActivity())
                .load(url)
                .placeholder(R.drawable.popup_logo)
                .into(mPharmacy_Image);









        mPharmacy_Name.setText(GlobalMethods.capitalizeString(tempData.getShopName()));
        mReview_Rating.setRating(tempData.getAvgRating());
        mReview_Count.setText(tempData.getTotalReviews()+" "+getString(R.string.reviews));

        /*if (tempData.getDistance().contains(".")) {
            DecimalFormat distance_roundoff = new DecimalFormat("#.##");
            mDistance.setText(Double.valueOf(distance_roundoff
                    .format(Double.valueOf(tempData
                            .getDistance())))
                    + " " + getString(R.string.km_away));
        } else {
            mDistance.setText(tempData
                    .getDistance() + " " + getString(R.string.km_away));
        }*/

        mDistance.setText(CalculationByDistancedouble(tempData.getLatitude(),tempData.getLongitude())+ " " + getActivity().getString(R.string.km_away));

        String communication_address = tempData
                .getAddress()
                + ", "
                + tempData.getArea();

        mAddresstxt.setText(communication_address);
        /*if(tempData.getIsFav()!=null)
        {

            if(tempData.getIsFav().equalsIgnoreCase("1"))
            {
                mFavValue.setImageResource(R.drawable.thumps_up);
            }else{
                mFavValue.setImageResource(R.drawable.thumps_down);
            }
        }else{
            mFavValue.setImageResource(R.drawable.heart_red);
        }*/
    }


    @Override
    public void onRequestSuccess(Object responseObj) {

        if (responseObj instanceof ShowReviewResponse) {

            if(internetconnection_layout.getVisibility()==View.VISIBLE)
            {
                internetconnection_layout.setVisibility(View.GONE);
            }
            DialogManager.hideProgress();
            ShowReviewResponse mShowReviewResponse = (ShowReviewResponse) responseObj;
            if (mShowReviewResponse.getStatus().equalsIgnoreCase(
                    AppConstants.SUCCESS_CODE)) {
                DialogManager.hideProgress();
                setAdapter(mShowReviewResponse.getMsg());

            }

        }else if(responseObj instanceof RateResponseEntity)
        {
            RateResponseEntity rateresponse = (RateResponseEntity) responseObj;
            DialogManager.showMsgPopup(getActivity(),"",rateresponse.getMsg());
        }
        super.onRequestSuccess(responseObj);
    }


    @Override
    public void onResume() {
        super.onResume();
        /*if (!GlobalMethods.getStringValue(getActivity(), AppConstants.ShopName_OT).isEmpty()) {
            ((HomeScreen) getActivity()).setToolbarTitle(getString(R.string.reviews), getString(R.string.one_touch_updated)+" "+
                    GlobalMethods.getStringValue(getActivity(), AppConstants.ShopName_OT));
        } else {
            ((HomeScreen) getActivity()).setToolbarTitle(getString(R.string.reviews),getString(R.string.one_touch_order));
        }*/
        ((HomeScreen) getActivity()).setToolbarTitle(getString(R.string.reviews),getString(R.string.home));
    }

    private void setAdapter(ArrayList<ShowReviewMessageEntity> review_list) {
        if (review_list != null) {
            if(review_list.size()>0)
            {
                mReviewList.setVisibility(View.VISIBLE);
                mReviewAdapter = new ReviewAdapter(getActivity(),review_list);
                mReviewList.setAdapter(mReviewAdapter);
            }else{
                DialogManager.showMsgPopup(getActivity(),"","No reviews found");
            }
        }
    }
}
