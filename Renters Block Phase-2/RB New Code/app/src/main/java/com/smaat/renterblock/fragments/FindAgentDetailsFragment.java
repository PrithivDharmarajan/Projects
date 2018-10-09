package com.smaat.renterblock.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.ChatInputEntity;
import com.smaat.renterblock.entity.FindAgentDetailReviewEntity;
import com.smaat.renterblock.entity.FindAgentDetailReviewResultEntity;
import com.smaat.renterblock.entity.FindAgentDetailUserResult;
import com.smaat.renterblock.entity.ShareThisAppEntity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.CreateGroupChatResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.InterfaceBtnCallback;
import com.smaat.renterblock.utils.NumberUtil;
import com.smaat.renterblock.utils.PreferenceUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class FindAgentDetailsFragment extends BaseFragment {

    @BindView(R.id.user_image)
    ImageView mUserImageView;

    @BindView(R.id.user_name_txt)
    TextView mUserNameTxt;

    @BindView(R.id.friends_count_txt)
    TextView mFriendsCountTxt;

    @BindView(R.id.reviews_count_txt)
    TextView mReviewsCountTxt;

    @BindView(R.id.photos_count_txt)
    TextView mPhototsCountTxt;

    @BindView(R.id.user_rating_bar)
    RatingBar mUserRatingBar;

    @BindView(R.id.chat_img)
    ImageView mChatImg;

    @BindView(R.id.listing_count_txt)
    TextView mListingCountTxt;

    @BindView(R.id.agent_with_txt)
    TextView mAgentWithText;

    @BindView(R.id.review_user_lay)
    RelativeLayout mReivewUserLay;

    @BindView(R.id.latest_review)
    TextView mLatestReviewTxt;

    @BindView(R.id.review_user_image)
    ImageView mReviewUserImg;

    @BindView(R.id.review_user_name_txt)
    TextView mReviewUserNameTxt;

    @BindView(R.id.review_friends_count_txt)
    TextView mReviewFriendCountTxt;

    @BindView(R.id.review_reviews_count_txt)
    TextView mReviewReviewCountTxt;

    @BindView(R.id.review_photos_count_txt)
    TextView mReviewPhotoCountTxt;

    @BindView(R.id.review_user_rating_bar)
    RatingBar mReviewRatingBar;

    @BindView(R.id.review_comments_full_txt)
    TextView mReviewCommentsFullTxt;

    @BindView(R.id.review_comments_txt)
    TextView mReviewCommentsTxt;

    @BindView(R.id.read_more_txt)
    TextView mReadMoreTxt;

    @BindView(R.id.more_reviews_lay)
    RelativeLayout mMoreReviewLay;

    @BindView(R.id.listings_lay)
    RelativeLayout mListingLay;

    FindAgentDetailUserResult mUserDetailsResult;
    ArrayList<FindAgentDetailReviewResultEntity> mUserReviewResult = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_find_agent_details, container, false);
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);
        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(rootView);

        /*For focus current fragment*/

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

          /* If the value of visibleInt is zero,  the view will set gone. Or if the value of visibleInt is one,  the view will set visible. Or else, the view will set gone*/
        if (getActivity() != null) {
             /*Slide menu action*/
            ((HomeScreen) getActivity()).setDrawerAction(false);

            /*set header first  img*/
            ((HomeScreen) getActivity()).setHeaderLeftFirstImgLayVisible(1);
            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.mipmap.app_icon, 2);

            /*set header second img*/
            ((HomeScreen) getActivity()).setHeaderLeftSecondImgLayVisible(R.mipmap.app_icon, 0);
            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.mipmap.app_icon, 0,"");

            /*set header third txt*/
            ((HomeScreen) getActivity()).setHeaderLeftThirdTxtLayVisible(getString(R.string.app_name), 0);
            ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.app_name), 0);

            /*set header txt*/
            ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.find_an_agent), 1);
            ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.app_name), 0);

            initView();
        }

    }

    /*InitViews*/
    private void initView() {
        AppConstants.TAG = this.getClass().getSimpleName();
        ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.drawable.filter_icon, 0,"");
        callAPI();
    }

    private void callAPI() {
        if (AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY != null) {
            APIRequestHandler.getInstance().findAgentDetailsAPICall(AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY.getUser_id(), FindAgentDetailsFragment.this);

        }
    }

    /*set OnClick*/
    @OnClick({R.id.request_btn, R.id.review_btn, R.id.more_reviews_lay, R.id.listings_lay, R.id.about_lay, R.id.share_profile_lay})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.request_btn:
                if (AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY.getRb_user().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                    ((HomeScreen) getActivity()).addFragment(new AgentContentMessageFragment());
                } else {
                    String body = "Hi, I saw your available listings "
                            + "on Renter's Block and would like to learn more about "
                            + "a specific listing. Please LOGIN! "
                            + "and feel free to message me through the app or website. Looking forward to connecting with you!";
//                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
//                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY.getEmail_id()});
//                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "New Hot Lead from Renter's Block");
//                    emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body));
//                    emailIntent.setType("text/plain");
//                    startActivity(emailIntent);
                    final Intent emailInt = new Intent(Intent.ACTION_SENDTO);
                    emailInt.setData(Uri.parse("mailto:" +mUserDetailsResult.getEmail_id()));
                    emailInt.putExtra(Intent.EXTRA_SUBJECT, "New Hot Lead from Renter's Block  ");
                    emailInt.putExtra(Intent.EXTRA_TEXT,body );
                    startActivity(emailInt);
                }
                break;
            case R.id.review_btn:
                /*Need to redirect List*/
                if (getActivity() != null) {
                    ((HomeScreen) getActivity()).addFragment(new AgentReviewPostFragment());
                }
                break;
            case R.id.more_reviews_lay:
                break;
            case R.id.listings_lay:
                break;
            case R.id.about_lay:
                ((HomeScreen) getActivity()).addFragment(new AboutAgentUserFragment());

                break;
            case R.id.share_profile_lay:
                AppConstants.SHARE_DETAILS = new ShareThisAppEntity();
                AppConstants.SHARE_DETAILS.setSms_share(getString(R.string.msg_txt) + AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY.getUser_id()+ "\n" + getString(R.string.address) + " - " + AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY.getAddress1()+AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY.getAddress2());
                AppConstants.SHARE_DETAILS.setEmail_share_subject(getString(R.string.msg_txt) + AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY.getUser_id());
                AppConstants.SHARE_DETAILS.setEmail_share_text(getString(R.string.address) + " - " + AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY.getAddress1()+AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY.getAddress2());
                AppConstants.SHARE_DETAILS.setFacebook_share_title(getString(R.string.msg_txt) + AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY.getUser_id());
                AppConstants.SHARE_DETAILS.setFacebook_share_description(getString(R.string.address) + " - " + AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY.getAddress1()+AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY.getAddress2());
                AppConstants.SHARE_DETAILS.setProperty_id(mUserDetailsResult.getUser_id());
                AppConstants.SHARE_DETAILS.setFacebook_share_link(AppConstants.APP_LINK);

                AppConstants.SHARE_THIS_PROFILE=true;

                ((HomeScreen) getActivity()).addFragment(new ShareThisAppFragment());
                break;
        }
    }


    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof FindAgentDetailReviewEntity) {
            final FindAgentDetailReviewEntity mReviewEntityRes = (FindAgentDetailReviewEntity) resObj;
            if (mReviewEntityRes.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                if (mReviewEntityRes.getResult().getUserresult() != null) {
                    mUserDetailsResult = mReviewEntityRes.getResult().getUserresult();
                    AppConstants.FindAgent_DetailUser_Result = mUserDetailsResult;
                    setUserData();
                }
                if (mReviewEntityRes.getResult().getReviewresult() != null) {
                    mUserReviewResult = mReviewEntityRes.getResult().getReviewresult();
                    if (mReviewEntityRes.getResult().getReviewresult().size() != 0) {
                        mReivewUserLay.setVisibility(View.VISIBLE);
                        mLatestReviewTxt.setVisibility(View.VISIBLE);
                    }
                    setUserReviewData();
                }
            }
            mMoreReviewLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mReviewEntityRes.getResult().getReviewresult().size() == 0) {
                        DialogManager.getInstance().showAlertPopup(getContext(), getString(R.string.no_review_to_show), new InterfaceBtnCallback() {
                            @Override
                            public void onPositiveClick() {

                            }
                        });
                    } else if (mReviewEntityRes.getResult().getReviewresult().size() != 0) {
                        ((HomeScreen) getActivity()).addFragment(new AgentMoreReivewFragment());
                    }
                }
            });
        }
        else if (resObj instanceof CreateGroupChatResponse) {
            CreateGroupChatResponse chatIdRes = (CreateGroupChatResponse) resObj;
            if (chatIdRes.getError_code().equals(AppConstants.SUCCESS_CODE)) {
                AppConstants.CHAT_INPUT_ENTITY = new ChatInputEntity();
                AppConstants.CHAT_INPUT_ENTITY.setUser_id(PreferenceUtil.getUserID(getActivity()));
                AppConstants.CHAT_INPUT_ENTITY.setFriend_id(chatIdRes.getFriend_id());
                AppConstants.CHAT_INPUT_ENTITY.setSchedule_id(chatIdRes.getResult());

                ((HomeScreen) getActivity()).addFragment(new ChatFragment());
            }
        }
    }


    private void setUserData() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mUserDetailsResult.getUser_profileImage().isEmpty()) {
                        mUserImageView.setImageResource(R.drawable.default_prop_icon);
                    } else {
                        try {
                            Glide.with(getContext())
                                    .load(mUserDetailsResult.getUser_profileImage()).placeholder(R.drawable.profile_pic).into(mUserImageView);
                        } catch (Exception e) {
                            mUserImageView.setImageResource(R.drawable.profile_pic);
                        }
                    }
                    mUserNameTxt.setText(mUserDetailsResult.getName() + " " + mUserDetailsResult.getLast_name());
                    mFriendsCountTxt.setText(mUserDetailsResult.getFriends_count());
                    mReviewsCountTxt.setText(mUserDetailsResult.getReviews_count());
                    mPhototsCountTxt.setText(mUserDetailsResult.getPhotos_count());
                    mChatImg.setVisibility(AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY.getRb_user().equalsIgnoreCase(AppConstants.SUCCESS_CODE) ? View.VISIBLE : View.GONE);
                    mUserRatingBar.setRating(NumberUtil.getRatingVal(mUserDetailsResult.getUser_avg_rating()));
                    mAgentWithText.setText(mUserDetailsResult.getUser_type() + " " + getString(R.string.with) + " " + mUserDetailsResult.getBusiness_name());
                    mListingCountTxt.setText("(" + mUserDetailsResult.getProperty_listing() + ")");

                    mChatImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            APIRequestHandler.getInstance().getChatID(mUserDetailsResult.getUser_id(),mUserDetailsResult.getUser_name(),FindAgentDetailsFragment.this);
                        }
                    });

                    mListingLay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mUserDetailsResult.getProperty_listing().equalsIgnoreCase("0") || mUserDetailsResult.getProperty_listing().isEmpty() || mUserDetailsResult.getProperty_listing() == null) {
                                DialogManager.getInstance().showAlertPopup(getContext(), getString(R.string.no_listing_to_show), new InterfaceBtnCallback() {
                                    @Override
                                    public void onPositiveClick() {

                                    }
                                });
                            } else {
                                AppConstants.PROPERTY_LIST_CURRENT_BACK_FRAGMENT = new FindAgentDetailsFragment();
                                AppConstants.PROPERTY_LIST_USER_ID = AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY.getUser_id();
                                ((HomeScreen) getActivity()).addFragment(new PropertyListingFragment());
                            }

                        }
                    });
                }
            });
        }

    }

    private void setUserReviewData() {
//        mUserReviewResult = new ArrayList<>();
        if (mUserReviewResult.size() != 0) {
            if (mUserReviewResult.get(0).getUser_profileImage().isEmpty()) {
                mReviewUserImg.setImageResource(R.drawable.default_prop_icon);
            } else {
                try {
                    Glide.with(getContext())
                            .load(mUserReviewResult.get(0).getUser_profileImage()).placeholder(R.drawable.profile_pic).into(mReviewUserImg);
                } catch (Exception e) {
                    mReviewUserImg.setImageResource(R.drawable.profile_pic);
                }
            }
            mReviewUserNameTxt.setText(mUserReviewResult.get(0).getName());
            mReviewFriendCountTxt.setText(mUserReviewResult.get(0).getFriends_count());
            mReviewReviewCountTxt.setText(mUserReviewResult.get(0).getReviews_count());
            mReviewPhotoCountTxt.setText(mUserReviewResult.get(0).getPhotos_count());
            mReviewRatingBar.setRating(NumberUtil.getRatingVal(mUserReviewResult.get(0).getUser_avg_rating()));
            mReviewCommentsFullTxt.setText(mUserReviewResult.get(0).getComments());
            mReviewCommentsTxt.setText(mUserReviewResult.get(0).getComments());
            if (mReviewCommentsFullTxt.getText().toString().length() > 70) {
                mReadMoreTxt.setVisibility(View.VISIBLE);
            } else {
                mReadMoreTxt.setVisibility(View.GONE);
            }

            mReadMoreTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mReviewCommentsFullTxt.setVisibility(View.VISIBLE);
                    mReviewCommentsTxt.setVisibility(View.GONE);
                    mReadMoreTxt.setVisibility(View.GONE);
                }
            });
        }

    }

}
