package com.smaat.renterblock.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smaat.renterblock.R;
import com.smaat.renterblock.adapters.ImageVideoDetailsAdapter;
import com.smaat.renterblock.entity.ChatInputEntity;
import com.smaat.renterblock.entity.GroupChatSendResponse;
import com.smaat.renterblock.entity.PropertyEntity;
import com.smaat.renterblock.entity.PropertyPictures;
import com.smaat.renterblock.entity.PropertyReview;
import com.smaat.renterblock.entity.PropertyReviewCommentEntity;
import com.smaat.renterblock.entity.ShareThisAppEntity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.AddToBoardsResponse;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.model.CreateGroupChatResponse;
import com.smaat.renterblock.model.FriendsRecentListResponse;
import com.smaat.renterblock.model.PropertyDetailsResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.ui.LoginScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.BlurTransformation;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.InterfaceBtnCallback;
import com.smaat.renterblock.utils.InterfaceTwoBtnCallback;
import com.smaat.renterblock.utils.NumberUtil;
import com.smaat.renterblock.utils.PreferenceUtil;
import com.smaat.renterblock.utils.TimeUtil;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.smaat.renterblock.R.id.photos_count_txt;


public class PropertyDetailsFragment extends BaseFragment implements OnMapReadyCallback {


    @BindView(R.id.property_image)
    ImageView mPropertyImage;

    @BindView(R.id.property_blur_image)
    ImageView mPropertyBlurImage;

    @BindView(R.id.last_updated_txt)
    TextView mLastUpdatedTxt;

    @BindView(R.id.amount_txt)
    TextView mAmountTxt;

    @BindView(photos_count_txt)
    TextView mPhotosCountTxt;

    @BindView(R.id.property_rating_bar)
    RatingBar mPropertyRatingBar;

    @BindView(R.id.rating_review_count_txt)
    TextView mRatingReviewCountTxt;

    @BindView(R.id.location_txt)
    TextView mLocationTxt;

    @BindView(R.id.rent_txt)
    TextView mRentTxt;

    @BindView(R.id.property_type_txt)
    TextView mPropertyTypeTxt;

    @BindView(R.id.square_footage_txt)
    TextView mSquareFootageTxt;

    @BindView(R.id.bed_count_txt)
    TextView mBedCountTxt;

    @BindView(R.id.bathroom_count_txt)
    TextView mBathroomCountTxt;

    @BindView(R.id.build_year_txt)
    TextView mBuildYearTxt;


    @BindView(R.id.detail_txt)
    TextView mDetailTxt;

    @BindView(R.id.detail_txt_full_view)
    TextView mDetailTxtFullView;

    @BindView(R.id.more_txt)
    TextView mMoreTxt;

    @BindView(R.id.adView)
    AdView mAdView;

    @BindView(R.id.rb_lay)
    RelativeLayout mRbLay;

    @BindView(R.id.add_to_my_board_big_btn)
    Button mAddToMyBoardBigBtn;

    @BindView(R.id.add_msg_board_btn)
    Button mAddMsgBoardBtn;

    @BindView(R.id.msg_view_lay)
    RelativeLayout mMsgViewLay;

    @BindView(R.id.msg_address_txt)
    TextView mMsgAddressTxt;

    @BindView(R.id.msg_img)
    ImageView mMsgImg;

    @BindView(R.id.msg_rating_bar)
    RatingBar mMsgRatingBar;

    @BindView(R.id.msg_reviews__count_txt)
    TextView mMsgReviewsCountTxt;

    @BindView(R.id.request_user_img)
    ImageView mRequestUserImg;

    @BindView(R.id.property_posted_user_name_txt)
    TextView mPropertyPostedUserNameTxt;

    @BindView(R.id.request_business_name_txt)
    TextView mRequestBusinessNameTxt;

    @BindView(R.id.detail_lay)
    RelativeLayout mDetailLay;

    @BindView(R.id.contact_info_lay)
    LinearLayout mContactInfoLay;

    @BindView(R.id.request_user_email_txt)
    TextView mRequestUserEmailTxt;

    @BindView(R.id.request_user_phone_txt)
    TextView mRequestUserPhoneTxt;

    @BindView(R.id.request_info_btn)
    Button mRequestInfoBtn;

    @BindView(R.id.request_send_btn)
    Button mRequestSendBtn;

    @BindView(R.id.report_fraud_btn)
    Button mReportFraudBtn;

    @BindView(R.id.call_icon_lay)
    LinearLayout mCallIconLay;

    @BindView(R.id.see_more_btn)
    Button mSeeMoreBtn;

    @BindView(R.id.scroll_view)
    ScrollView mScrollView;

    @BindView(R.id.scroll_parent_lay)
    LinearLayout mScrollParentLay;

    @BindView(R.id.review_lay)
    LinearLayout mReviewLay;

    @BindView(R.id.view_all)
    Button mViewAllBtn;

    @BindView(R.id.like_lay)
    RelativeLayout mLikeLay;

    @BindView(R.id.fav_img)
    ImageView mFavImg;

    @BindView(R.id.property_share_img)
    ImageView mPropertyShareImg;

    @BindView(R.id.photo_recycle_view)
    RecyclerView mPhotoRecyclerView;

    @BindView(R.id.message_board_lay)
    RelativeLayout mMessageBoardLay;

    @BindView(R.id.chat_icon_img)
    ImageView mChatIconImg;

    @BindView(R.id.friend_icon_img)
    ImageView mFriendIconImg;

    @BindView(R.id.image_click_lay)
    RelativeLayout mPropertyImagePopUpLay;

    private PropertyEntity mPropertyDetailsRes = new PropertyEntity();
    private boolean isClickedBool = false;
    private String mUserIDStr = AppConstants.FAILURE_CODE, mFriendUserID = "";
    private double mLat = 0.0, mLong = 0.0;

    private Rect mReact = new Rect();
    private ViewTreeObserver mObserver;
    private Dialog mRecommendedReviewDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_property_details, container, false);

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


    /*Fragment manual onResume*/
    @Override
    public void onFragmentResume() {
        super.onFragmentResume();

        mAdView.loadAd(new AdRequest.Builder().build());

        /*To create Property detail View*/
        callPropertyAPI();
    }

    private void initView() {
        if (getActivity() != null) {
              /*Map Showing*/
            SupportMapFragment fragment = (SupportMapFragment) this.getChildFragmentManager()
                    .findFragmentById(R.id.property_deatils_map);
            fragment.getMapAsync(this);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    mScrollParentLay.getHitRect(mReact);
                    final ViewTreeObserver.OnScrollChangedListener onScrollChangedListener = new
                            ViewTreeObserver.OnScrollChangedListener() {

                                @Override
                                public void onScrollChanged() {
                                    //do stuff here
                                    if (getActivity() != null) {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (mReportFraudBtn.getLocalVisibleRect(mReact)) {
                                                    mLikeLay.setVisibility(View.GONE);
                                                } else if (mRbLay.getLocalVisibleRect(mReact) || mPhotoRecyclerView.getLocalVisibleRect(mReact) || mMessageBoardLay.getLocalVisibleRect(mReact)) {
                                                    mLikeLay.setVisibility(View.VISIBLE);
                                                    mLikeLay.setBackground(ContextCompat.getDrawable(getActivity(), R.color.white));
                                                    mFavImg.setImageResource(mPropertyDetailsRes.getIsfavourite().equals(AppConstants.FAILURE_CODE) ?
                                                            R.drawable.fav_icon_black : R.drawable.fav_enable_icon_black);
                                                    mPropertyShareImg.setImageResource(R.drawable.share_icon_black);
                                                } else {
                                                    mLikeLay.setVisibility(View.VISIBLE);
                                                    mLikeLay.setBackground(ContextCompat.getDrawable(getActivity(), R.color.black_transparent));
                                                    mFavImg.setImageResource(mPropertyDetailsRes.getIsfavourite().equals(AppConstants.FAILURE_CODE) ?
                                                            R.drawable.like_icon_normal : R.drawable.like_icon_over);
                                                    mPropertyShareImg.setImageResource(R.drawable.share_icon);
                                                }
                                            }
                                        });


                                    }
                                }
                            };
                    mScrollParentLay.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (mObserver == null) {
                                mObserver = mScrollParentLay.getViewTreeObserver();
                                mObserver.addOnScrollChangedListener(onScrollChangedListener);
                            } else if (!mObserver.isAlive()) {
                                mObserver.removeOnScrollChangedListener(onScrollChangedListener);
                                mObserver = mScrollParentLay.getViewTreeObserver();
                                mObserver.addOnScrollChangedListener(onScrollChangedListener);
                            }
                            return false;
                        }
                    });

                    if (mPropertyDetailsRes.getProperty_datetime() != null) {
                        mLastUpdatedTxt.setText(getResources().getString(R.string.last_updated) + " " + TimeUtil.getTimeDifference(mPropertyDetailsRes.getProperty_datetime()));
                    }

                    mPhotosCountTxt.setVisibility(mPropertyDetailsRes.getProperty_pics().size() > 0 ? View.VISIBLE : View.INVISIBLE);
                    if (mPropertyDetailsRes.getProperty_pics().size() > 0) {
                        mPhotosCountTxt.setText(getString(R.string.one_per) + "" + mPropertyDetailsRes.getProperty_pics().size());
                    }
                    mRatingReviewCountTxt.setText(getString(R.string.open_parenthesis) + " " + mPropertyDetailsRes.getReview_count() + " " + getString(R.string.close_parenthesis));


                    String propertyAmtStr = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(mPropertyDetailsRes.getPrice_range()));
                    Integer validate = Integer.parseInt(mPropertyDetailsRes.getPrice_range());

                    mAmountTxt.setText(getString(R.string.dollar) + (validate <= 2 ? mPropertyDetailsRes.getPrice_range() : propertyAmtStr) + " " + getString(R.string.per_month));
                    mPropertyRatingBar.setRating(NumberUtil.getRatingVal(mPropertyDetailsRes.getProperty_rating()));

                    mLocationTxt.setText(mPropertyDetailsRes.getAddress());
                    mRentTxt.setText(getString(R.string.property_for) + " " + mPropertyDetailsRes.getType());
                    mPropertyTypeTxt.setText(mPropertyDetailsRes.getProperty_type());
                    mSquareFootageTxt.setText(mPropertyDetailsRes.getSquare_footage() + " " + getString(R.string.property_sqft));
                    mBedCountTxt.setText(mPropertyDetailsRes.getBeds() + " " + getString(R.string.bed_rooms));
                    mBathroomCountTxt.setText(mPropertyDetailsRes.getBaths() + " " + getString(R.string.bath_rooms));
                    mBuildYearTxt.setText(mPropertyDetailsRes.getBuild_year() + " " + getString(R.string.build_year));
                    mDetailTxt.setText(mPropertyDetailsRes.getDescription());
                    mDetailTxtFullView.setText(mPropertyDetailsRes.getDescription());

                    mDetailLay.setVisibility(mPropertyDetailsRes.getDescription().isEmpty() ? View.GONE : View.VISIBLE);

                    try {
                        if (mPropertyDetailsRes.getMy_message_board().get(0).getProperty_pics().size() > 0 &&
                                mPropertyDetailsRes.getMy_message_board().get(0).getProperty_pics().size() > 0
                                && !mPropertyDetailsRes.getMy_message_board().get(0).getProperty_pics().get(0).getFile().isEmpty()) {
                            Glide.with(PropertyDetailsFragment.this)
                                    .load(mPropertyDetailsRes.getMy_message_board().get(0).getProperty_pics().get(0).getFile())
                                    .placeholder(R.drawable.profile_pic)
                                    .into(mMsgImg);
                        } else {
                            mMsgImg.setImageResource(R.drawable.profile_pic);
                        }
                    } catch (Exception e) {
                        mMsgImg.setImageResource(R.drawable.profile_pic);
                    }
                    mMsgRatingBar.setRating(NumberUtil.getRatingVal(mPropertyDetailsRes.getMy_message_board().size() > 0 ?
                            mPropertyDetailsRes.getMy_message_board().get(0).getProperty_rating() : AppConstants.FAILURE_CODE));

                    mAddToMyBoardBigBtn.setVisibility(View.GONE);
                    mAddMsgBoardBtn.setVisibility(View.VISIBLE);
                    mMsgViewLay.setVisibility(View.VISIBLE);
                    mAddMsgBoardBtn.setBackgroundResource(R.drawable.minus_icon);
                    mMsgAddressTxt.setText(mPropertyDetailsRes.getAddress());
                    mMsgReviewsCountTxt.setText(getString(R.string.open_parenthesis) + " " +
                            (mPropertyDetailsRes.getMy_message_board().size() > 0 ?
                                    mPropertyDetailsRes.getMy_message_board().get(0).getReview_count() : AppConstants.FAILURE_CODE) + " " + getString(R.string.close_parenthesis));

                    if(mPropertyDetailsRes.getUser_id().equalsIgnoreCase(PreferenceUtil.getUserID(getActivity()))){
                        mChatIconImg.setVisibility(View.GONE);
                        mFriendIconImg.setVisibility(View.GONE);
                    }else {
                        mChatIconImg.setVisibility(View.VISIBLE);
                        mFriendIconImg.setVisibility(View.VISIBLE);
                    }

                    if (mPropertyDetailsRes.getIsmymessageboard().equals(AppConstants.FAILURE_CODE)) {
                        mAddMsgBoardBtn.setVisibility(mPropertyDetailsRes.getMy_message_board().size() > 0 ? View.GONE : View.VISIBLE);
                        mAddToMyBoardBigBtn.setVisibility(mPropertyDetailsRes.getMy_message_board().size() > 0 ? View.VISIBLE : View.GONE);
                        mMsgViewLay.setVisibility(mPropertyDetailsRes.getMy_message_board().size() > 0 ? View.GONE : View.VISIBLE);
                        mAddMsgBoardBtn.setBackgroundResource(R.drawable.add_button);
                    }


                    try {
                        if (mPropertyDetailsRes.getProperty_posted_user_details().size() > 0
                                && !mPropertyDetailsRes.getProperty_posted_user_details().get(0).getUser_pic().isEmpty()) {
                            Glide.with(PropertyDetailsFragment.this)
                                    .load(mPropertyDetailsRes.getProperty_posted_user_details().get(0).getUser_pic())
                                    .into(mRequestUserImg);
                            mFriendUserID = mPropertyDetailsRes.getProperty_posted_user_details().get(0).getUser_id();
                        } else {
                            mRequestUserImg.setImageResource(R.drawable.profile_pic);
                        }
                    } catch (Exception e) {
                        mRequestUserImg.setImageResource(R.drawable.profile_pic);
                    }

                    try {
                        mPropertyImage.setVisibility(mPropertyDetailsRes.getProperty_pics().size() > 0
                                && !mPropertyDetailsRes.getProperty_pics().get(0).getFile().isEmpty() ? View.VISIBLE : View.GONE);
                        mPropertyBlurImage.setVisibility(mPropertyDetailsRes.getProperty_pics().size() > 0
                                && !mPropertyDetailsRes.getProperty_pics().get(0).getFile().isEmpty() ? View.VISIBLE : View.GONE);

                        if (mPropertyDetailsRes.getProperty_pics().size() > 0
                                && !mPropertyDetailsRes.getProperty_pics().get(0).getFile().isEmpty()) {
                            Glide.with(PropertyDetailsFragment.this)
                                    .load(mPropertyDetailsRes.getProperty_pics().get(0).getFile())
                                    .into(mPropertyImage);
                            Glide.with(PropertyDetailsFragment.this)
                                    .load(mPropertyDetailsRes.getProperty_pics().get(0).getFile())
                                    .transform(new BlurTransformation(getActivity()))
                                    .into(mPropertyBlurImage);
                        }


                    } catch (Exception e) {
                        mPropertyImage.setVisibility(View.GONE);
                        mPropertyBlurImage.setVisibility(View.GONE);
                    }
                    mPropertyImagePopUpLay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(mPropertyDetailsRes.getProperty_pics().size() > 0) {
                                DialogManager.getInstance().showPropertyImage(getActivity(), mPropertyDetailsRes.getProperty_pics().get(0).getFile(), mPropertyDetailsRes.getAddress(), mPropertyDetailsRes.getPrice_range(), mPropertyDetailsRes.getProperty_datetime(), new InterfaceTwoBtnCallback() {
                                    @Override
                                    public void onNegativeClick() {
                                        mScrollView.fullScroll(View.FOCUS_DOWN);
                                    }

                                    @Override
                                    public void onPositiveClick() {
                                        callReviewFragment();

                                    }
                                });
                            }

                        }
                    });

                    mPropertyPostedUserNameTxt.setText(mPropertyDetailsRes.getProperty_posted_user_details().size() > 0 ?
                            mPropertyDetailsRes.getProperty_posted_user_details().get(0).getFirst_name() + " " +
                                    mPropertyDetailsRes.getProperty_posted_user_details().get(0).getLast_name() : "");

                    mRequestBusinessNameTxt.setText(mPropertyDetailsRes.getProperty_posted_user_details().size() > 0
                            && !mPropertyDetailsRes.getProperty_posted_user_details().get(0).getBusiness_name().isEmpty() ?
                            mPropertyDetailsRes.getProperty_posted_user_details().get(0).getBusiness_name() :
                            getString(R.string.not_applicable));


                    if (mPropertyDetailsRes.getProperty_posted_user_details().get(0).getRb_user() != null
                            && mPropertyDetailsRes.getProperty_posted_user_details().get(0).getRb_user().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                        mRequestUserEmailTxt.setText(mPropertyDetailsRes.getProperty_posted_user_details().get(0).getEmail_id());
                        mRequestUserEmailTxt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                        "mailto", mPropertyDetailsRes.getProperty_posted_user_details().get(0).getEmail_id(), null));
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                            }
                        });
                    } else {
                        mRequestUserEmailTxt.setText(getString(R.string.not_applicable));
                    }

                    if (mPropertyDetailsRes.getProperty_posted_user_details().get(0).getPhone().isEmpty()) {
                        mRequestUserPhoneTxt.setText(getString(R.string.not_applicable));
                    } else {
                        mRequestUserPhoneTxt.setText(mPropertyDetailsRes.getProperty_posted_user_details().get(0).getPhone());
                        mRequestUserPhoneTxt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:" + mPropertyDetailsRes.getProperty_posted_user_details().get(0).getPhone()));
                                startActivity(intent);
                            }
                        });
                    }

                    if (mPropertyDetailsRes.getProperty_posted_user_details().get(0).getPhone().isEmpty()) {
                        mCallIconLay.setVisibility(View.GONE);
                    } else {
                        mCallIconLay.setVisibility(View.VISIBLE);
                    }

                    if (mPropertyDetailsRes.getProperty_posted_user_details().get(0).getRb_user() != null
                            && mPropertyDetailsRes.getProperty_posted_user_details().get(0).getRb_user().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                        mCallIconLay.setVisibility(View.VISIBLE);
                    } else {
                        mCallIconLay.setVisibility(View.GONE);
                    }


                    if (mPropertyDetailsRes.getProperty_posted_user_details().get(0).getIs_request_already_sent().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                        mContactInfoLay.setVisibility(View.VISIBLE);
                        mRequestSendBtn.setVisibility(View.VISIBLE);
                        mRequestInfoBtn.setVisibility(View.INVISIBLE);
                    } else {
                        mContactInfoLay.setVisibility(View.GONE);
                        mRequestSendBtn.setVisibility(View.INVISIBLE);
                        mRequestInfoBtn.setVisibility(View.VISIBLE);
                    }

                    if (mPropertyDetailsRes.getUserpropertypic().size() == 0) {
                        mSeeMoreBtn.setText(getString(R.string.add));
                    } else {
                        mSeeMoreBtn.setText(getString(R.string.see_more));
                    }

                    if (mPropertyDetailsRes.getProperty_review().size() > 0) {
                        showRecommendedReviews(mPropertyDetailsRes.getProperty_review());
                    }

                    if (mPropertyDetailsRes.getProperty_pics().size() != 0) {
                        showPhotosVideos(mPropertyDetailsRes.getUserpropertypic());
                    }

                    if (!mPropertyDetailsRes.getLatitude().isEmpty() && !mPropertyDetailsRes.getLongitude().isEmpty()) {
                        mLat = Double.parseDouble(mPropertyDetailsRes.getLatitude());
                        mLong = Double.parseDouble(mPropertyDetailsRes.getLongitude());
//            setLocation(mLat, mLong);
                    } else {
                        DialogManager.getInstance().showToast(getActivity(), "Location Empty");
                    }
                }
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Add a marker in CLRI and move the camera
        LatLng rb = new LatLng(mLat, mLong);
        googleMap.addMarker(new MarkerOptions().position(rb).title(mPropertyDetailsRes.getProperty_name()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rb, 19));
    }


    private void showRecommendedReviews(final ArrayList<PropertyReview> mPropertyReviewResponse) {
        mReviewLay.removeAllViews();
        RelativeLayout mRecommend_lay;
        TextView review_user_name, recommand_review_comment, recommand_review_time;
        RatingBar recommand_revie_rating;
        ImageView mReviewImage;
        Button mReviewChatBtn;

        if (mPropertyReviewResponse.size() < 5) {
            mViewAllBtn.setVisibility(View.GONE);
            for (int i = 0; i < mPropertyReviewResponse.size(); i++) {

                final PropertyReview mPropertyReview = mPropertyReviewResponse.get(i);
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View convertView = inflater.inflate(R.layout.adapter_recommended_reviews, null, false);

                review_user_name = convertView.findViewById(R.id.review_user_name);
                recommand_review_comment = convertView.findViewById(R.id.recommand_review_comment);
                recommand_review_time = convertView.findViewById(R.id.recommand_review_time);
                recommand_revie_rating = convertView.findViewById(R.id.recommand_revie_rating);
                mReviewImage = convertView.findViewById(R.id.recommand_review_image);
                mReviewChatBtn = convertView.findViewById(R.id.chat_icon_adapter);


                try {
                    if (mPropertyReviewResponse.size() > 0 && mPropertyReviewResponse.get(0).getReview_user_details().size() > 0 && !mPropertyReviewResponse.get(0).getReview_user_details().get(0).getUser_pic().isEmpty()) {
                        Glide.with(PropertyDetailsFragment.this)
                                .load(mPropertyReviewResponse.get(0).getReview_user_details().get(0).getUser_pic())
                                .placeholder(R.drawable.profile_pic)
                                .into(mReviewImage);
                    } else {
                        mReviewImage.setImageResource(R.drawable.profile_pic);
                    }
                } catch (Exception e) {
                    mReviewImage.setImageResource(R.drawable.profile_pic);
                }
                mRecommend_lay = convertView.findViewById(R.id.recommend_lay1);
                mRecommend_lay.setTag(i);
                mRecommend_lay.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        final int pos = Integer.parseInt(String.valueOf(v.getTag()));

                        DialogManager.getInstance().showReviewDialog(getActivity(), mPropertyReviewResponse.get(pos), pos, new InterfaceBtnCallback() {
                            @Override
                            public void onPositiveClick() {
                               // callGroupIdService(mPropertyReviewResponse.get(pos).getReview_user_id(), pos);
                                APIRequestHandler.getInstance().getChatID(mPropertyReviewResponse.get(pos).getReview_user_id(), mPropertyReviewResponse.get(pos).getReview_user_details().get(0).getUser_name(), PropertyDetailsFragment.this);

                            }
                        });
                    }
                });

                float rating = Float.parseFloat(mPropertyReview.getRating());
                recommand_revie_rating.setRating(rating);
                review_user_name.setText(mPropertyReview.getReview_user_details().size() > 0 ?
                        mPropertyReview.getReview_user_details().get(0).getUser_name() : "");
                recommand_review_comment.setText(mPropertyReview.getComments());
                recommand_review_time.setText(TimeUtil.getTimeDifference(mPropertyReview.getDate_time()));

                Button chat_icon = (Button) convertView.findViewById(R.id.chat_icon_adapter);
                chat_icon.setTag(i);
                mReviewImage.setTag(i);

                if (mPropertyReviewResponse.get(i).getReview_user_id().equalsIgnoreCase(PreferenceUtil.getUserID(getActivity()))) {
                    chat_icon.setVisibility(View.GONE);
                } else {
                    chat_icon.setVisibility(View.VISIBLE);
                }

                mReviewImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = Integer.parseInt(String.valueOf(view.getTag()));
                        AppConstants.PROFILE_CURRENT_BACK_FRAGMENT = new PropertyDetailsFragment();
                        AppConstants.PROFILE_ID = mPropertyReviewResponse.get(pos).getReview_user_id();
                        ((HomeScreen) getActivity()).addFragment(new ProfileFragment());
                    }
                });

                chat_icon.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        int pos = Integer.parseInt(String.valueOf(v.getTag()));
                        if (PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.LOGIN_STATUS)) {
//                            callGroupIdService(mPropertyReviewResponse.get(pos).getReview_user_details().get(0).getUser_id(), pos);
                            callChatAPI(mPropertyReviewResponse.get(pos).getReview_user_id(), mPropertyReview.getReview_user_details().get(0).getUser_name());

                        } else {
                            nextScreen(LoginScreen.class, false);
                        }

                    }
                });

                mReviewLay.addView(convertView);
            }
        } else {
            mViewAllBtn.setVisibility(View.VISIBLE);
            for (int i = 0; i < 5; i++) {

                final PropertyReview mPropertyReview = mPropertyReviewResponse.get(i);
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View convertView = inflater.inflate(R.layout.adapter_recommended_reviews, null, false);

                review_user_name = (TextView) convertView.findViewById(R.id.review_user_name);
                recommand_review_comment = (TextView) convertView.findViewById(R.id.recommand_review_comment);
                recommand_review_time = (TextView) convertView.findViewById(R.id.recommand_review_time);
                recommand_revie_rating = (RatingBar) convertView.findViewById(R.id.recommand_revie_rating);
                mRecommend_lay = (RelativeLayout) convertView.findViewById(R.id.recommend_lay1);
                mReviewImage = (ImageView) convertView.findViewById(R.id.recommand_review_image);
                mRecommend_lay.setTag(i);

                mRecommend_lay.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        final int pos = Integer.parseInt(String.valueOf(v.getTag()));
                        DialogManager.getInstance().showReviewDialog(getActivity(), mPropertyReviewResponse.get(pos), pos, new InterfaceBtnCallback() {
                            @Override
                            public void onPositiveClick() {
                            //    callGroupIdService(mPropertyReviewResponse.get(pos).getReview_user_id(), pos);
                                APIRequestHandler.getInstance().getChatID(mPropertyReviewResponse.get(pos).getReview_user_id(), mPropertyReviewResponse.get(pos).getReview_user_details().get(0).getUser_name(), PropertyDetailsFragment.this);

                            }
                        });
                    }
                });


                Glide.with(PropertyDetailsFragment.this)
                        .load(mPropertyReviewResponse.get(0).getReview_user_details().get(0).getUser_pic())
                        .placeholder(R.drawable.profile_pic)
                        .into(mReviewImage);

                float rating = Float.parseFloat(mPropertyReview.getRating());
                recommand_revie_rating.setRating(rating);
                review_user_name.setText(mPropertyReview.getReview_user_details().get(0).getUser_name());
                recommand_review_comment.setText(mPropertyReview.getComments());
                recommand_review_time.setText(TimeUtil.getTimeDifference(mPropertyReview.getDate_time()));

                Button chat_icon = (Button) convertView.findViewById(R.id.chat_icon_adapter);
                chat_icon.setTag(i);
                mReviewImage.setTag(i);

                if (mPropertyReviewResponse.get(i).getReview_user_id().equalsIgnoreCase(PreferenceUtil.getUserID(getActivity()))) {
                    chat_icon.setVisibility(View.GONE);
                } else {
                    chat_icon.setVisibility(View.VISIBLE);
                }

                mReviewImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = Integer.parseInt(String.valueOf(view.getTag()));
                        AppConstants.PROFILE_CURRENT_BACK_FRAGMENT = new PropertyDetailsFragment();
                        AppConstants.PROFILE_ID = mPropertyReviewResponse.get(pos).getReview_user_id();
                        ((HomeScreen) getActivity()).addFragment(new ProfileFragment());
                    }
                });
                chat_icon.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        int pos = Integer.parseInt(String.valueOf(v.getTag()));
                        if (PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.LOGIN_STATUS)) {
//                            callGroupIdService(mPropertyReviewResponse.get(pos).getReview_user_details().get(0).getUser_id(), pos);
                            callChatAPI(mPropertyReviewResponse.get(pos).getReview_user_id(), mPropertyReview.getReview_user_details().get(0).getUser_name());

                        } else {
                            nextScreen(LoginScreen.class, false);
                        }
                    }
                });

                mReviewLay.addView(convertView);
            }
        }

    }

    private void showPhotosVideos(final ArrayList<PropertyPictures> mUserPropertyPicsList) {

        ImageVideoDetailsAdapter mImageDetailsAdapter = new ImageVideoDetailsAdapter(getActivity(), mUserPropertyPicsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mPhotoRecyclerView.setLayoutManager(layoutManager);
        mPhotoRecyclerView.setFocusable(false);
        mPhotoRecyclerView.setAdapter(mImageDetailsAdapter);
        mPhotoRecyclerView.setNestedScrollingEnabled(false);

    }
    private void callReviewFragment(){
        AppConstants.REVIEW_LIST_CURRENT_BACK_FRAGMENT = new PropertyDetailsFragment();
        AppConstants.CURRENT_REVIEW_DETAILS = new PropertyReviewCommentEntity();
        AppConstants.CURRENT_REVIEW_DETAILS.setProperty_id(mPropertyDetailsRes.getProperty_id());
        AppConstants.CURRENT_REVIEW_DETAILS.setReview_header_txt(getString(R.string.post_review));
                    /*this comment id assignment to check whether we have to call Update API or PostReview API...in postREview it assigned postreview str and
                    * edit review it would have  particular comment id and for Update it was empty str*/
        AppConstants.CURRENT_REVIEW_DETAILS.setProperty_review_comment_id(getString(R.string.post_review));
        ((HomeScreen) getActivity()).addFragment(new ReviewsWriteFragment());
    }


    /*View OnClick*/
    @OnClick({R.id.request_info_details_btn, R.id.review_btn, R.id.property_detail_back_img, R.id.more_txt, R.id.add_to_my_board_big_btn,
            R.id.add_msg_board_btn, R.id.friend_icon_img, R.id.report_fraud_btn, R.id.request_info_btn,
            R.id.see_more_btn, R.id.view_all, R.id.loc_img, R.id.fav_img, R.id.property_share_img, R.id.request_user_img, R.id.property_posted_user_name_txt, R.id.chat_icon_img})
    public void onClick(View v) {

        if (getActivity() != null) {
            switch (v.getId()) {
                case R.id.request_info_details_btn:
                    mScrollView.fullScroll(View.FOCUS_DOWN);

                    break;
                case R.id.review_btn:
                   callReviewFragment();

                    break;
                case R.id.property_detail_back_img:
                    if (getActivity() != null) {
                        getActivity().onBackPressed();
                    }
                    break;
                case R.id.more_txt:
                    if (!isClickedBool) {
                        mMoreTxt.setText(getResources().getString(R.string.less));
                        mDetailTxtFullView.setVisibility(View.VISIBLE);
                        mDetailTxt.setVisibility(View.GONE);
                        isClickedBool = true;
                    } else {
                        mMoreTxt.setText(getResources().getString(R.string.more_));
                        mDetailTxtFullView.setVisibility(View.GONE);
                        mDetailTxt.setVisibility(View.VISIBLE);
                        isClickedBool = false;
                    }
                    break;
                case R.id.add_to_my_board_big_btn:
                    callAddMyBoard();
                    break;
                case R.id.add_msg_board_btn:
                    callAddMyBoard();
                    break;
                case R.id.friend_icon_img:
                    sendFriendRequest();
                    break;
                case R.id.report_fraud_btn:
                    if (!mPropertyDetailsRes.getUser_id().equalsIgnoreCase(PreferenceUtil.getUserID(getActivity()))) {
                        DialogManager.getInstance().showOptionPopup(getActivity(), getString(R.string.report_fraud_content), getString(R.string.yes), getString(R.string.no), new InterfaceTwoBtnCallback() {
                            @Override
                            public void onNegativeClick() {

                            }

                            @Override
                            public void onPositiveClick() {
                                sendReportFraud();
                            }
                        });
                    }else {
                        DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.report_fraud_cant), new InterfaceBtnCallback() {
                            @Override
                            public void onPositiveClick() {

                            }
                        });
                    }
                    break;
                case R.id.request_info_btn:
                    if (PreferenceUtil.getUserID(getActivity()).equalsIgnoreCase(mPropertyDetailsRes.getUser_id())) {
                        DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.you_cannot_req),
                                new InterfaceBtnCallback() {
                                    @Override
                                    public void onPositiveClick() {

                                    }
                                });
                    } else if (PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.LOGIN_STATUS)) {
                        if (mPropertyDetailsRes.getProperty_posted_user_details().size() > 0 &&
                                mPropertyDetailsRes.getProperty_posted_user_details().get(0).getRb_user()
                                        .equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                            callRequestInfoService();
                        } else {

                            String body = "Hi, I saw your available "
                                    + "listings on Renter's Block and would like to learn more about "
                                    + "a specific listing. Please LOGIN! "
                                    + "and feel free to message me through the app or website. "
                                    + "Looking forward to connecting with you!";


                            final Intent emailInt = new Intent(Intent.ACTION_SENDTO);
                            emailInt.setData(Uri.parse("mailto:" + mPropertyDetailsRes.getProperty_posted_user_details().get(0).getEmail_id()));
                            emailInt.putExtra(Intent.EXTRA_SUBJECT, "New Hot Lead from Renter's Block for -" + mPropertyDetailsRes.getProperty_id());
                            emailInt.putExtra(Intent.EXTRA_TEXT, body);
                            startActivity(emailInt);
                        }
                    } else {
                        nextScreen(LoginScreen.class, false);
                    }

                    break;
                case R.id.see_more_btn:
                    if (mPropertyDetailsRes.getUserpropertypic().size() > 0) {
                        AppConstants.mPropertyPics = mPropertyDetailsRes.getUserpropertypic();
                        ((HomeScreen) getActivity()).addFragment(new ImageUploadFragment());
                    } else {
                        ((HomeScreen) getActivity()).addFragment(new ImageUploadFragment());
                    }


                    break;
                case R.id.view_all:
                    mRecommendedReviewDialog= DialogManager.getInstance().showAllReviewsDialog(getActivity(), mPropertyDetailsRes.getProperty_review(),PropertyDetailsFragment.this);
                    break;
                case R.id.loc_img:
                    PreferenceUtil.storeStringValue(getActivity(), AppConstants.PROPERTY_DETAILS_LATITUDE, mPropertyDetailsRes.getLatitude());
                    PreferenceUtil.storeStringValue(getActivity(), AppConstants.PROPERTY_DETAILS_LONGITUDE, mPropertyDetailsRes.getLongitude());
                    ((HomeScreen) getActivity()).addFragment(new PropertyDetailsMapViewFragment());
                    break;
                case R.id.fav_img:
                    if (mPropertyDetailsRes.getIsfavourite().equals("0")) {
                        mFavImg.setImageResource(R.drawable.like_icon_normal);
                    } else {
                        mFavImg.setImageResource(R.drawable.like_icon_over);
                    }
                    break;
                case R.id.property_share_img:

                    AppConstants.SHARE_DETAILS = new ShareThisAppEntity();
                    AppConstants.SHARE_DETAILS.setSms_share(getString(R.string.msg_txt) + mPropertyDetailsRes.getProperty_id() + "\n" + getString(R.string.address) + " - " + mPropertyDetailsRes.getAddress());
                    AppConstants.SHARE_DETAILS.setEmail_share_subject(getString(R.string.msg_txt) + mPropertyDetailsRes.getProperty_id());
                    AppConstants.SHARE_DETAILS.setEmail_share_text(getString(R.string.address) + " - " + mPropertyDetailsRes.getAddress());
                    AppConstants.SHARE_DETAILS.setFacebook_share_title(getString(R.string.msg_txt) + mPropertyDetailsRes.getProperty_id());
                    AppConstants.SHARE_DETAILS.setFacebook_share_description(getString(R.string.address) + " - " + mPropertyDetailsRes.getAddress());
                    AppConstants.SHARE_DETAILS.setProperty_id(mPropertyDetailsRes.getProperty_id());
                    AppConstants.SHARE_DETAILS.setFacebook_share_link(AppConstants.APP_LINK);

                    AppConstants.SHARE_THIS_PROFILE=true;

                    ((HomeScreen) getActivity()).addFragment(new ShareThisAppFragment());

                    break;
                case R.id.property_posted_user_name_txt:
                    AppConstants.PROFILE_ID = mPropertyDetailsRes.getProperty_posted_user_details().get(0).getUser_id();
                    AppConstants.PROFILE_CURRENT_BACK_FRAGMENT = new PropertyDetailsFragment();
                    ((HomeScreen) getActivity()).addFragment(new ProfileFragment());
                    break;
                case R.id.chat_icon_img:
                    if (PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.LOGIN_STATUS)) {
                        callChatAPI(mPropertyDetailsRes.getProperty_posted_user_details().get(0).getUser_id(), mPropertyDetailsRes.getProperty_posted_user_details().get(0).getUser_name());

                    } else {
                        nextScreen(LoginScreen.class, false);
                    }
                    break;
                case R.id.request_user_img:
                    AppConstants.PROFILE_CURRENT_BACK_FRAGMENT = new PropertyDetailsFragment();
                    AppConstants.PROFILE_ID = mPropertyDetailsRes.getProperty_posted_user_details().get(0).getUser_id();
                    ((HomeScreen) getActivity()).addFragment(new ProfileFragment());
                    break;
            }
        }
    }

    private void sendReportFraud() {

        String property_id = "Property Id:" + mPropertyDetailsRes.getProperty_id() + "\n\n";
        String property_add = "Property Address:" + mPropertyDetailsRes.getAddress();

        final Intent emailInt = new Intent(Intent.ACTION_SENDTO);
        emailInt.setData(Uri.parse("mailto:" + "admin@rentersblock.com"));
        emailInt.putExtra(Intent.EXTRA_SUBJECT, "Report Fraud about the Property");
        emailInt.putExtra(Intent.EXTRA_TEXT, property_id + property_add);
        startActivity(emailInt);

    }

    public void callChatAPI(String mFriedUserId, String mFriendName) {
        APIRequestHandler.getInstance().getChatID(mFriedUserId, mFriendName, PropertyDetailsFragment.this);

    }

    public void callPropertyAPI() {

        APIRequestHandler.getInstance().propertyDetails(AppConstants.DETAIL_PROPERTY_ID, PropertyDetailsFragment.this);
    }

    private void callAddMyBoard() {

        APIRequestHandler.getInstance().addToMsgBoard(AppConstants.DETAIL_PROPERTY_ID, PropertyDetailsFragment.this);

    }

    private void sendFriendRequest() {
        APIRequestHandler.getInstance().sendFriendRequest(PropertyDetailsFragment.this, mFriendUserID);
    }


    private void callRequestInfoService() {
        APIRequestHandler.getInstance().requestInfoService(AppConstants.DETAIL_PROPERTY_ID, PropertyDetailsFragment.this);
    }

    public void callGroupIdService(String ReviewUserID, final int pos) {
//        GroupChatSendResponse mResponse = new GroupChatSendResponse();
//        mResponse.setPos(pos);
        APIRequestHandler.getInstance().groupIdServices(ReviewUserID, "", PropertyDetailsFragment.this);
    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);

        if (responseObj instanceof PropertyDetailsResponse) {
            final PropertyDetailsResponse propertyEntity = (PropertyDetailsResponse) responseObj;

            if (propertyEntity.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                if (propertyEntity.getResult().size() > 0) {
                    mPropertyDetailsRes = propertyEntity.getResult().get(0);
                    initView();
                }
            }
        } else if (responseObj instanceof AddToBoardsResponse) {
            final AddToBoardsResponse mResponse = (AddToBoardsResponse) responseObj;
            if (mResponse.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                DialogManager.getInstance().showToast(getActivity(), mResponse.getResult());

                callPropertyAPI();
            }
        } else if (responseObj instanceof CommonResponse) {
            final CommonResponse mResponse = (CommonResponse) responseObj;
            if (mResponse.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                callPropertyAPI();
                DialogManager.getInstance().showAlertPopup(getActivity(), mResponse.getMsg(), new InterfaceBtnCallback() {
                    @Override
                    public void onPositiveClick() {

                    }
                });
            }
        } else if (responseObj instanceof GroupChatSendResponse) {
            final GroupChatSendResponse mResponse = (GroupChatSendResponse) responseObj;
            if (mResponse.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                String is_friend = "0";
                if (mResponse.getMsg().equalsIgnoreCase("A group with these members already exists")) {
                    is_friend = "1";
//                    callChatService(mResponse.getResult(), mResponse.getPos(), is_friend);
                }
            }
        } else if (responseObj instanceof FriendsRecentListResponse) {
            FriendsRecentListResponse mResponse = (FriendsRecentListResponse) responseObj;
            DialogManager.getInstance().showAlertPopup(getActivity(), mResponse.getMsg(), new InterfaceBtnCallback() {
                @Override
                public void onPositiveClick() {

                }
            });
        } else if (responseObj instanceof CreateGroupChatResponse) {
            CreateGroupChatResponse mResponse = (CreateGroupChatResponse) responseObj;
            if (mResponse.getError_code().equals(AppConstants.SUCCESS_CODE)) {
                if(mRecommendedReviewDialog!=null&&mRecommendedReviewDialog.isShowing()){
                    mRecommendedReviewDialog.dismiss();
                }
                AppConstants.CHAT_INPUT_ENTITY = new ChatInputEntity();
                AppConstants.CHAT_INPUT_ENTITY.setUser_id(PreferenceUtil.getUserID(getActivity()));
                AppConstants.CHAT_INPUT_ENTITY.setFriend_id(mResponse.getFriend_id());
                AppConstants.CHAT_INPUT_ENTITY.setSchedule_id(mResponse.getResult());
                ((HomeScreen) getActivity()).addFragment(new ChatFragment());
            }
        }
    }

//    private void callChatService(String group_id, int pos, final String is_friend) {
//        Intent intent = new Intent(getActivity(), FriendChatScreen.class);
//        intent.putExtra("ids", mPropertyDetailsRes.getProperty_review().get(pos).getReview_user_details().get(0).getUser_id());
//        intent.putExtra("names", mPropertyDetailsRes.getProperty_review().get(pos).getReview_user_details().get(0).getUser_name());
//        intent.putExtra("groupId", group_id);
//        intent.putExtra("type", "group");
//        intent.putExtra("enhanced_profile_ids",
//                mPropertyDetailsRes.getProperty_review().get(pos).getReview_user_details().get(0).getEnhanced_profile());
//        if (is_friend.equalsIgnoreCase("1")) {
//            intent.putExtra("from_call", "");
//        } else {
//            intent.putExtra("from_call", "hotleads");
//        }
//        startActivity(intent);
//    }
}
