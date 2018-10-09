package com.smaat.renterblock.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.smaat.renterblock.R;
import com.smaat.renterblock.adapters.MyReviewListAdapter;
import com.smaat.renterblock.entity.ReviewPropertyEntity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.ReviewPropertyResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sys on 29-Aug-17.
 */

public class ReviewsListFragment extends BaseFragment {

    @BindView(R.id.my_reviews_recycler_view)
    RecyclerView mMyReviewsRecyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstances) {
        View rootView = inflater.inflate(R.layout.frag_review_list_screen, container, false);
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
            ((HomeScreen) getActivity()).setDrawerAction(AppConstants.REVIEW_LIST_CURRENT_BACK_FRAGMENT instanceof ReviewsListFragment);

            /*set header first  img*/
            ((HomeScreen) getActivity()).setHeaderLeftFirstImgLayVisible(1);
            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.add_icon, 1);

            /*set header second img*/
            ((HomeScreen) getActivity()).setHeaderLeftSecondImgLayVisible(R.mipmap.app_icon, 0);
            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.mipmap.app_icon, 0,"");

            /*set header third txt*/
            ((HomeScreen) getActivity()).setHeaderLeftThirdTxtLayVisible(getString(R.string.app_name), 0);
            ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.app_name), 0);

            /*set header txt*/
            ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.my_reviews), 1);
            ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.app_name), 0);

            ((HomeScreen) getActivity()).mHeaderRightFirstImgLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getActivity() != null) {
                        AppConstants.MAP_CURRENT_BACK_FRAGMENT = new ReviewsListFragment();
                        AppConstants.TYPE_OF_PROPERTY = AppConstants.RENT;
                        ((HomeScreen) getActivity()).addFragment(new MapFragment());
                    }
                }
            });


            APIRequestHandler.getInstance().getReviewDetailsAPICall( AppConstants.PROFILE_ID,this);
        }
    }


    @Override
    public void onRequestSuccess(Object resObj) {
        if (resObj instanceof ReviewPropertyResponse) {
            ReviewPropertyResponse reviewPropertyResponse = (ReviewPropertyResponse) resObj;
            if (reviewPropertyResponse.getError_code().equals(AppConstants.SUCCESS_CODE)) {
                setAdapter(reviewPropertyResponse.getResult());
            } else {
                DialogManager.getInstance().showAlertPopup(getActivity(), reviewPropertyResponse.getMsg(), this);
            }
        }
    }

    private void setAdapter(ReviewPropertyEntity momentArrList) {
        MyReviewListAdapter galleryMomentAdapter = new MyReviewListAdapter(getActivity(), momentArrList);
        mMyReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMyReviewsRecyclerView.setAdapter(galleryMomentAdapter);
    }
}
