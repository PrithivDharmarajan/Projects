package com.smaat.renterblock.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.ChatInputEntity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.CreateGroupChatResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.NumberUtil;
import com.smaat.renterblock.utils.PreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.smaat.renterblock.R.id.about_agent_friends_count_txt;

public class AboutAgentUserFragment extends BaseFragment {

    @BindView(R.id.about_agent_user_image)
    ImageView mAboutAgentUserImg;

    @BindView(R.id.about_agent_user_name_txt)
    TextView mAboutAgentUserNameTxt;

    @BindView(R.id.about_agent_agent_with_txt)
    TextView mAboutAgentWithTxt;

    @BindView(R.id.about_agent__chat_img)
    ImageView mAboutAgentChatImg;

    @BindView(R.id.about_agent_description)
    TextView mDescription;

    @BindView(about_agent_friends_count_txt)
    TextView mAboutAgentFriendCountTxt;

    @BindView(R.id.about_agent_license_txt)
    TextView mAboutAgentLicenseTxt;

    @BindView(R.id.about_agent_photos_count_txt)
    TextView mAboutAgentPhotosCountTxt;

    @BindView(R.id.about_agent_reviews_count_txt)
    TextView mAboutAgentReviewCountTxt;

    @BindView(R.id.about_agent_user_rating_bar)
    RatingBar mAboutAgentRatingBar;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_about_agent_user_screen, container, false);
        ButterKnife.bind(this, rootView);
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

        initView();
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
            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.mipmap.app_icon, 2);

            /*set header second img*/
            ((HomeScreen) getActivity()).setHeaderLeftSecondImgLayVisible(R.mipmap.app_icon, 0);
            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.mipmap.app_icon, 0,"");

            /*set header third txt*/
            ((HomeScreen) getActivity()).setHeaderLeftThirdTxtLayVisible(getString(R.string.app_name), 0);
            ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.app_name), 0);

            /*set header txt*/
            ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.about_renters_blocks), 1);
            ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.app_name), 0);
        }
    }

    private void initView() {
        if (AppConstants.FindAgent_DetailUser_Result != null) {
            if (AppConstants.FindAgent_DetailUser_Result.getUser_profileImage().isEmpty()) {
                mAboutAgentUserImg.setImageResource(R.drawable.default_prop_icon);
            } else {
                try {
                    Glide.with(getContext())
                            .load(AppConstants.FindAgent_DetailUser_Result.getUser_profileImage()).placeholder(R.drawable.profile_pic).into(mAboutAgentUserImg);
                } catch (Exception e) {
                    mAboutAgentUserImg.setImageResource(R.drawable.profile_pic);
                }
            }

            mAboutAgentFriendCountTxt.setText(AppConstants.FindAgent_DetailUser_Result.getFriends_count());
            mAboutAgentPhotosCountTxt.setText(AppConstants.FindAgent_DetailUser_Result.getPhotos_count());
            mAboutAgentReviewCountTxt.setText(AppConstants.FindAgent_DetailUser_Result.getReviews_count());
            mAboutAgentRatingBar.setRating(NumberUtil.getRatingVal(AppConstants.FindAgent_DetailUser_Result.getUser_avg_rating()));

            mAboutAgentUserNameTxt.setText(AppConstants.FindAgent_DetailUser_Result.getUser_name());
            mAboutAgentWithTxt.setText(AppConstants.FindAgent_DetailUser_Result.getUser_type() + " " + getString(R.string.with) + " " + AppConstants.FindAgent_DetailUser_Result.getBusiness_name());
            mAboutAgentLicenseTxt.setText(AppConstants.FindAgent_DetailUser_Result.getLicence());
            mDescription.setText(AppConstants.FindAgent_DetailUser_Result.getDescription());

            mAboutAgentChatImg.setVisibility(AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY.getRb_user().equalsIgnoreCase(AppConstants.SUCCESS_CODE) ? View.VISIBLE : View.GONE);

            mAboutAgentChatImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    APIRequestHandler.getInstance().getChatID(AppConstants.FindAgent_DetailUser_Result.getUser_id(),AppConstants.FindAgent_DetailUser_Result.getUser_name(),AboutAgentUserFragment.this);
                }
            });
        }
    }

    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof CreateGroupChatResponse) {
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
}
