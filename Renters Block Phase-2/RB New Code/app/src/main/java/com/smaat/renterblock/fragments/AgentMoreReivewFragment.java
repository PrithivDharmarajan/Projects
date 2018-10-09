package com.smaat.renterblock.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.smaat.renterblock.R;
import com.smaat.renterblock.adapters.AgentMoreReviewAdatper;
import com.smaat.renterblock.entity.FindAgentDetailReviewEntity;
import com.smaat.renterblock.entity.FindAgentDetailReviewResultEntity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AgentMoreReivewFragment extends BaseFragment {

    ArrayList<FindAgentDetailReviewResultEntity> mUserReviewResult = new ArrayList<>();
    AgentMoreReviewAdatper mAgentMoreReviewAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    @BindView(R.id.more_review_recycler_view)
    RecyclerView mReviewRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_activity_agent_more_reivew, container, false);
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);
        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(rootView);
        initView();
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

    @Override
    public void onFragmentResume() {
        super.onFragmentResume();
        ((HomeScreen)getActivity()).setHeaderTxt(getString(R.string.my_reviews),1);
    }

    private void initView() {
        getAgentMoreAPI();
    }

    private void getAgentMoreAPI() {
        if (AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY != null) {
            APIRequestHandler.getInstance().findAgentDetailsAPICall(AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY.getUser_id(), AgentMoreReivewFragment.this);

        }
    }

    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof FindAgentDetailReviewEntity) {
            final FindAgentDetailReviewEntity mReviewEntityRes = (FindAgentDetailReviewEntity) resObj;
            if (mReviewEntityRes.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                if (mReviewEntityRes.getResult().getReviewresult() != null) {
                    mUserReviewResult = mReviewEntityRes.getResult().getReviewresult();
                   setAdapter(mUserReviewResult);

                }
            }


        }
    }

    private void setAdapter(ArrayList<FindAgentDetailReviewResultEntity> mUserReviewResult) {
//        if (getActivity() != null) {
//            if (mAgentMoreReviewAdapter == null) {
        mAgentMoreReviewAdapter = new AgentMoreReviewAdatper(getContext(), mUserReviewResult);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mReviewRecyclerView.setLayoutManager(mLinearLayoutManager);
        mReviewRecyclerView.setAdapter(mAgentMoreReviewAdapter);
//            } else {
//                getActivity().runOnUiThread(new Runnable() {
//                    public void run() {
//                        mAgentMoreReviewAdapter.notifyDataSetChanged();
//                    }
//                });
//            }
//        }
    }


}
