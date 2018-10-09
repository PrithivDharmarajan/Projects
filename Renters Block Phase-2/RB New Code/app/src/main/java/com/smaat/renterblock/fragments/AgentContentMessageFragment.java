package com.smaat.renterblock.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.AgentReviewEntity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.InterfaceBtnCallback;
import com.smaat.renterblock.utils.NumberUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AgentContentMessageFragment extends BaseFragment {


    @BindView(R.id.agent_contact_message_edit)
    EditText mAgentContactMessageEdt;

    @BindView(R.id.agent_contact_user_name_txt)
    TextView mAgentContactUserNameTxt;

    @BindView(R.id.agent_contact_friends_count_txt)
    TextView mAgentContactFriendsCountTxt;

    @BindView(R.id.agent_contact_reviews_count_txt)
    TextView mAgentContactReviewsCountTxt;

    @BindView(R.id.agent_contact_photos_count_txt)
    TextView mAgentContactPhotosCountTxt;

    @BindView(R.id.agent_contact_user_rating_bar)
    RatingBar mAgentContactUserRatingBar;

    @BindView(R.id.agent_contact_agent_with_txt)
    TextView mAgentContaxtAgentWithTxt;

    @BindView(R.id.agent_contact_user_image)
    ImageView mAgentContactUserImages;

    @BindView(R.id.agent_contact_license_txt)
    TextView mAgentContactLicenseTxt;

    @BindView(R.id.agent_contact_chat_img)
    ImageView mAgentContactChatImg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.frag_agent_content_message_screen, container, false);
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
            ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.contact_the_user), 1);
            ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.app_name), 0);

        }
    }

    private void initView() {
        if (AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY != null) {
            if (AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY.getUser_pic().isEmpty()) {
                mAgentContactUserImages.setImageResource(R.drawable.default_prop_icon);
            } else {
                try {
                    Glide.with(getContext())
                            .load(AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY.getUser_pic()).placeholder(R.drawable.profile_pic).into(mAgentContactUserImages);
                } catch (Exception e) {
                    mAgentContactUserImages.setImageResource(R.drawable.profile_pic);
                }
            }

            mAgentContactFriendsCountTxt.setText(AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY.getFriends_count());
            mAgentContactPhotosCountTxt.setText(AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY.getPhotos_count());
            mAgentContactReviewsCountTxt.setText(AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY.getReviews_count());
            mAgentContactUserRatingBar.setRating(NumberUtil.getRatingVal(AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY.getUser_avg_rating()));

            mAgentContactUserNameTxt.setText(AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY.getUser_name());
            mAgentContaxtAgentWithTxt.setText(AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY.getUser_type() + " " + getString(R.string.with) + " " + AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY.getBusiness_name());
            mAgentContactLicenseTxt.setText(AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY.getLicence());


            mAgentContactChatImg.setVisibility(AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY.getRb_user().equalsIgnoreCase(AppConstants.SUCCESS_CODE) ? View.VISIBLE : View.GONE);
        }
    }

    @OnClick({R.id.agent_contact_send_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.agent_contact_send_btn:
                if (mAgentContactMessageEdt.getText().toString().isEmpty()) {
                    DialogManager.getInstance().showAlertPopup(getContext(), "Please Fill The message", new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {

                        }
                    });
                } else {
                    getSendEmailCallAPI();
                }
                break;
        }
    }

    private void getSendEmailCallAPI() {
        APIRequestHandler.getInstance().contactAgentMailAPICall(AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY.getUser_id(), mAgentContactMessageEdt.getText().toString(), AgentContentMessageFragment.this);
    }

    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        AgentReviewEntity mAgentReviewEntity = (AgentReviewEntity) resObj;
        if (mAgentReviewEntity.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
            DialogManager.getInstance().showAlertPopup(getContext(), mAgentReviewEntity.getMsg(), new InterfaceBtnCallback() {
                @Override
                public void onPositiveClick() {

                }
            });
            ((HomeScreen) getActivity()).onBackPressed();
        }
    }
}
