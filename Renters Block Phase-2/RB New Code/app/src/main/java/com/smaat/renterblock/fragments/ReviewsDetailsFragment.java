package com.smaat.renterblock.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smaat.renterblock.R;
import com.smaat.renterblock.adapters.MyReviewDetailsAdapter;
import com.smaat.renterblock.entity.PropertyReviewCommentEntity;
import com.smaat.renterblock.entity.UserDetailsEntity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DateUtil;
import com.smaat.renterblock.utils.NumberUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sys on 29-Aug-17.
 */

public class ReviewsDetailsFragment extends BaseFragment {


    @BindView(R.id.user_img)
    ImageView mUserImg;

    @BindView(R.id.user_name_txt)
    TextView mUserNameTxt;

    @BindView(R.id.friends_count_txt)
    TextView mFriendsCountTxt;

    @BindView(R.id.reviews_count_txt)
    TextView mReviewsCountTxt;

    @BindView(R.id.photos_count_txt)
    TextView mPhotosCountTxt;

    @BindView(R.id.user_rating_bar)
    RatingBar mUserRatingBar;

    @BindView(R.id.minutes_txt)
    TextView mMinutesTxt;

    @BindView(R.id.main_comment_txt)
    TextView mMainCommentTxt;

    @BindView(R.id.comment_recycler_view)
    RecyclerView mCommentRecyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstances) {
        View rootView = inflater.inflate(R.layout.frag_review_details_screen, container, false);
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);
        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(rootView);

        /*For focus current fragment*/
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    default:
                        v.performClick();
                }
                return true;
            }
        });
        return rootView;
    }

    @Override
    public void onFragmentResume() {
        super.onFragmentResume();
         /* If the value of visibleInt is zero,  the view will set gone. Or if the value of visibleInt is one,  the view will set visible. Or else, the view will set gone*/
        if (getActivity() != null) {
             /*Slide menu action*/
            ((HomeScreen) getActivity()).setDrawerAction(false);

            /*set header first  img*/
            ((HomeScreen) getActivity()).setHeaderLeftFirstImgLayVisible(1);
            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.add_icon, 0);

            /*set header second img*/
            ((HomeScreen) getActivity()).setHeaderLeftSecondImgLayVisible(R.mipmap.app_icon, 0);
            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.mipmap.app_icon, 0,"");

            /*set header third txt*/
            ((HomeScreen) getActivity()).setHeaderLeftThirdTxtLayVisible(getString(R.string.app_name), 0);
            ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.view_listing), 1);

            /*set header txt*/
            ((HomeScreen) getActivity()).setHeaderTxt(AppConstants.REVIEW_DETAILS_RES.getReview().get(0).getAddress(), 1);
            ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.app_name), 0);

            ((HomeScreen) getActivity()).mHeaderRightThirdTxtLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO redirect to  property detail screen
                    AppConstants.DETAIL_PROPERTY_ID = AppConstants.REVIEW_DETAILS_RES.getReview().get(0).getProperty_id();
                    ((HomeScreen) getActivity()).addFragment(new PropertyDetailsFragment());
                }
            });
            initView();
        }
    }


    private void initView() {
        UserDetailsEntity userDetailsRes = AppConstants.REVIEW_DETAILS_RES.getUser_details();

        if (userDetailsRes.getUser_pic().isEmpty()) {
            mUserImg.setImageResource(R.drawable.default_profile_icon);

        } else {
            try {
                Glide.with(this)
                        .load(userDetailsRes.getUser_pic())
                        .into(mUserImg);
            } catch (Exception ex) {
                mUserImg.setImageResource(R.drawable.default_profile_icon);
                Log.d(AppConstants.TAG, ex.getMessage());
            }
        }

        mUserNameTxt.setText(userDetailsRes.getUser_name());
        mFriendsCountTxt.setText(userDetailsRes.getGetfriends());

        mReviewsCountTxt.setText(userDetailsRes.getGetreviewcount());
        mPhotosCountTxt.setText(userDetailsRes.getGetproimagecount());
        mUserRatingBar.setRating(NumberUtil.getRatingVal(AppConstants.REVIEW_DETAILS_RES.getReview().get(0).getRating()));
        mMinutesTxt.setText(DateUtil.getTimeDifference(AppConstants.REVIEW_DETAILS_RES.getReview().get(0).getDate_time()));
        mMainCommentTxt.setText(AppConstants.REVIEW_DETAILS_RES.getReview().get(0).getComments());
        setAdapter(AppConstants.REVIEW_DETAILS_RES.getReview().get(0).getProperty_review_comment());
    }


    /*View OnClick*/
    @OnClick({R.id.edit_btn, R.id.update_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_btn:
            case R.id.update_btn:

                AppConstants.CURRENT_REVIEW_DETAILS = v.getId() == R.id.edit_btn ?
                        AppConstants.REVIEW_DETAILS_RES.getReview().get(0).getProperty_review_comment().get(AppConstants.REVIEW_DETAILS_RES.getReview().get(0).getProperty_review_comment().size() - 1)
                        : new PropertyReviewCommentEntity();
                AppConstants.CURRENT_REVIEW_DETAILS.setProperty_review_id(AppConstants.REVIEW_DETAILS_RES.getReview().get(0).getProperty_review_id());
                AppConstants.CURRENT_REVIEW_DETAILS.setProperty_id(AppConstants.REVIEW_DETAILS_RES.getReview().get(0).getProperty_id());
                AppConstants.CURRENT_REVIEW_DETAILS.setReview_header_txt(getString(v.getId() == R.id.edit_btn ? R.string.edit_review : R.string.update_review));
                ((HomeScreen) getActivity()).addFragment(new ReviewsWriteFragment());
                break;
        }
    }


    private void setAdapter(ArrayList<PropertyReviewCommentEntity> momentArrList) {
        MyReviewDetailsAdapter galleryMomentAdapter = new MyReviewDetailsAdapter(getActivity(), momentArrList);
        mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCommentRecyclerView.setAdapter(galleryMomentAdapter);
        mCommentRecyclerView.setNestedScrollingEnabled(false);
    }
}
