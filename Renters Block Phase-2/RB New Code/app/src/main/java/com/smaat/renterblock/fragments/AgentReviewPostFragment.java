package com.smaat.renterblock.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;

import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.AgentReviewEntity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.InterfaceBtnCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AgentReviewPostFragment extends BaseFragment {

    @BindView(R.id.review_ratingbar)
    RatingBar mReviewRatingBar;
    @BindView(R.id.review_edit)
    EditText mReviewEdt;
    private String mRatingStr = "0.0", mCommentStr= "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View rootView = inflater.inflate(R.layout.frag_agent_review_post_screen,container,false);
        ButterKnife.bind(this,rootView);
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
        ((HomeScreen)getActivity()).setDrawerAction(false);
        ((HomeScreen) getActivity()).setHeaderLeftThirdTxtLayVisible(getString(R.string.post_review),1);
        ((HomeScreen)getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.post),1);
        ((HomeScreen)getActivity()).setHeaderLeftThirdTxtLayVisible(getString(R.string.close),1);
    }

    private void initView() {

        mReviewEdt.setCursorVisible(true);

        mReviewEdt.post(new Runnable() {
            @Override
            public void run() {
                mReviewEdt.setSelection(mReviewEdt.getText().toString()
                        .length());
            }
        });
        mReviewRatingBar
                .setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

                    @Override
                    public void onRatingChanged(RatingBar ratingBar,
                                                float rating, boolean fromUser) {

                        mRatingStr = (Float.toString(mReviewRatingBar.getRating()));



                    }
                });
        ((HomeScreen)getActivity()).mHeaderRightThirdTxtLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mReviewEdt.getText().toString().trim().isEmpty()){
                    DialogManager.getInstance().showAlertPopup(getContext(), getString(R.string.please_give_comments), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {

                        }
                    });
                }else if (mRatingStr.equalsIgnoreCase("0.0")){
                    DialogManager.getInstance().showAlertPopup(getContext(),getString(R.string.please_select_rating), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {

                        }
                    });
                }else {
                    /*CallAPI*/
                    mCommentStr = mReviewEdt.getText().toString();
                    APIRequestHandler.getInstance().agentReviewPostAPICall(AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY.getUser_id(),mCommentStr,mRatingStr,AgentReviewPostFragment.this);
                }
            }
        });
    }

    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        AgentReviewEntity agentReviewEntityRes = (AgentReviewEntity)resObj;
        if (agentReviewEntityRes.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)){
            DialogManager.getInstance().showAlertPopup(getContext(), agentReviewEntityRes.getMsg(), new InterfaceBtnCallback() {
                @Override
                public void onPositiveClick() {

                }
            });
//            ((HomeScreen)getActivity()).addFragment(new FindAgentDetailsFragment());
            ((HomeScreen)getActivity()).onBackPressed();
        }

    }
}
