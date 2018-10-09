package com.smaat.renterblock.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.smaat.renterblock.R;
import com.smaat.renterblock.main.BaseActivity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.InterfaceBtnCallback;
import com.smaat.renterblock.utils.NumberUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sys on 29-Aug-17.
 */

public class ReviewsWriteFragment extends BaseFragment {

    @BindView(R.id.post_review_txt)
    TextView mPostReviewTxt;

    @BindView(R.id.rating_bar)
    RatingBar mRatingBar;

    @BindView(R.id.review_edt)
    EditText mReviewEdt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstances) {
        View rootView = inflater.inflate(R.layout.frag_review_write_screen, container, false);
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
            ((HomeScreen) getActivity()).setHeaderLeftThirdTxtLayVisible(getString(R.string.close), 1);
            ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.con), 1);

            /*set header txt*/
            ((HomeScreen) getActivity()).setHeaderTxt( AppConstants.CURRENT_REVIEW_DETAILS.getReview_header_txt(), 1);
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

                        AppConstants.CURRENT_REVIEW_DETAILS.setReview_rating(mRatingBar.getRating() + "");
                        AppConstants.CURRENT_REVIEW_DETAILS.setReview_comments(mReviewEdt.getText().toString().trim());
                        AppConstants.CURRENT_REVIEW_DETAILS.setReview_header_txt(getString(R.string.post_review));
                        ((HomeScreen) getActivity()).addFragment(new ReviewsPostFragment());
                    }else{
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
        mPostReviewTxt.setVisibility(AppConstants.CURRENT_REVIEW_DETAILS.getReview_header_txt().equalsIgnoreCase(getString(R.string.update_review)) ? View.VISIBLE : View.GONE);
        mRatingBar.setRating(NumberUtil.getRatingVal(AppConstants.CURRENT_REVIEW_DETAILS.getReview_rating()));
        mReviewEdt.setText(AppConstants.CURRENT_REVIEW_DETAILS.getReview_comments());
        mReviewEdt.setSelection(mReviewEdt.getText().toString().length());
    }

}
