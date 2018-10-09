package com.smaat.renterblock.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.smaat.renterblock.R;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.ContentURLResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.InterfaceBtnCallback;
import com.smaat.renterblock.utils.NumberUtil;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ReviewsPostFragment extends BaseFragment {

    @BindView(R.id.rating_bar)
    RatingBar mRatingBar;

    @BindView(R.id.review_txt)
    TextView mReviewTxt;

    @BindView(R.id.twitter_share_toggle_btn)
    ToggleButton mTwitterShareToggleBtn;

    @BindView(R.id.fb_share_toggle_btn)
    ToggleButton mFbShareToggleBtn;


    private TwitterAuthClient mAuthClientTwitter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstances) {
        View rootView = inflater.inflate(R.layout.frag_review_post_screen, container, false);
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
            ((HomeScreen) getActivity()).setHeaderLeftFirstImgLayVisible(0);
            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.add_icon, 0);

            /*set header second img*/
            ((HomeScreen) getActivity()).setHeaderLeftSecondImgLayVisible(R.mipmap.app_icon, 0);
            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.mipmap.app_icon, 0,"");

            /*set header third txt*/
            ((HomeScreen) getActivity()).setHeaderLeftThirdTxtLayVisible(getString(R.string.close), 1);
            ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.post), 1);

            /*set header txt*/
            ((HomeScreen) getActivity()).setHeaderTxt(AppConstants.CURRENT_REVIEW_DETAILS.getReview_header_txt(), 1);
            ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.app_name), 0);

            ((HomeScreen) getActivity()).mHeaderLeftThirdTxtLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getActivity() != null) {
                        ((HomeScreen) getActivity()).onBackPressed();
                    }

                }
            });
            ((HomeScreen) getActivity()).mHeaderRightThirdTxtLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getActivity() != null && mRatingBar.getRating() != 0.0) {

                        if (AppConstants.CURRENT_REVIEW_DETAILS.getProperty_review_comment_id().equals(getString(R.string.post_review))) {
                            APIRequestHandler.getInstance().postNewReview(ReviewsPostFragment.this, AppConstants.CURRENT_REVIEW_DETAILS.getProperty_id(), AppConstants.CURRENT_REVIEW_DETAILS.getReview_comments(), AppConstants.CURRENT_REVIEW_DETAILS.getReview_rating());
                        } else {
                            APIRequestHandler.getInstance().postAndEditReviewAPICall((AppConstants.CURRENT_REVIEW_DETAILS.getProperty_review_comment_id().isEmpty() ? AppConstants.REVIEW_POST_URL : AppConstants.REVIEW_EDIT_URL),
                                    AppConstants.CURRENT_REVIEW_DETAILS.getProperty_review_id(), AppConstants.CURRENT_REVIEW_DETAILS.getProperty_review_comment_id(), AppConstants.CURRENT_REVIEW_DETAILS.getReview_comments(), AppConstants.CURRENT_REVIEW_DETAILS.getReview_rating(), AppConstants.CURRENT_REVIEW_DETAILS.getProperty_id(), ReviewsPostFragment.this);
                        }

                    } else {
                        DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.alert_rating), new InterfaceBtnCallback() {
                            @Override
                            public void onPositiveClick() {

                            }
                        });
                    }
                }
            });

            initView();
        }
    }

    private void initView() {
        mRatingBar.setRating(NumberUtil.getRatingVal(AppConstants.CURRENT_REVIEW_DETAILS.getReview_rating()));
        mReviewTxt.setText(AppConstants.CURRENT_REVIEW_DETAILS.getReview_comments());
    }

    /*View OnClick*/
    @OnClick({R.id.twitter_share_toggle_btn, R.id.fb_share_toggle_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.twitter_share_toggle_btn:
                onTwitterSend();
                mTwitterShareToggleBtn.setBackgroundResource(R.drawable.twit_share_enable);
                break;
            case R.id.fb_share_toggle_btn:
                mFbShareToggleBtn.setBackgroundResource(R.drawable.fb_share_enable);
                onFacebookSend();
                break;
        }
    }

    private void onTwitterSend() {

        if (getActivity() != null) {

            mAuthClientTwitter = new TwitterAuthClient();

            //Check Twitter Auth User
            mAuthClientTwitter.authorize(getActivity(), new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> twitterSessionResult) {
                    //Twitter Authorized User
                    final TwitterSession session = TwitterCore.getInstance().getSessionManager()
                            .getActiveSession();
                    final Intent intent = new ComposerActivity.Builder(getActivity())
                            .session(session)
                            .text(getString(R.string.app_content) + "\n" + AppConstants.APP_LINK)
                            .createIntent();
                    startActivity(intent);

                }

                @Override
                public void failure(TwitterException e) {
                    //Twitter Authorization  Failed
                    TwitterCore.getInstance().getSessionManager().clearActiveSession();
                }
            });

        }
    }

    private void onFacebookSend() {
        if (getActivity() != null) {
            FacebookSdk.sdkInitialize(getActivity());
            ShareDialog shareDialog = new ShareDialog(this);
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setQuote(getString(R.string.app_name))
                    .setContentTitle(getString(R.string.app_content))
                    .setContentDescription(getString(R.string.app_content))
                    .setContentUrl(Uri.parse(AppConstants.APP_LINK))
                    .build();
            shareDialog.show(content);
        }
    }

    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (getActivity() != null && resObj instanceof ContentURLResponse) {
            ContentURLResponse reviewUpdateEditResponse = (ContentURLResponse) resObj;
            if (reviewUpdateEditResponse.getError_code().equals(AppConstants.SUCCESS_CODE)) {
                DialogManager.getInstance().showAlertPopup(getActivity(), reviewUpdateEditResponse.getResult(), new InterfaceBtnCallback() {
                    @Override
                    public void onPositiveClick() {
                        if (AppConstants.REVIEW_LIST_CURRENT_BACK_FRAGMENT instanceof PropertyDetailsFragment) {
                            ((HomeScreen) getActivity()).addFragment(new MapFragment());
                        }

                        ((HomeScreen) getActivity()).addFragment(AppConstants.REVIEW_LIST_CURRENT_BACK_FRAGMENT instanceof PropertyDetailsFragment ? new PropertyDetailsFragment() : new ReviewsListFragment());
                    }
                });

            } else {
                DialogManager.getInstance().showAlertPopup(getActivity(), reviewUpdateEditResponse.getResult(), this);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mAuthClientTwitter != null)
            mAuthClientTwitter.onActivityResult(requestCode, resultCode, data);
    }
}
