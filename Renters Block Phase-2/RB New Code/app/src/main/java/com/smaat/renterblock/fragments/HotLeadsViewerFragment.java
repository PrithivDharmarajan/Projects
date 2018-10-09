package com.smaat.renterblock.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.smaat.renterblock.R;
import com.smaat.renterblock.adapters.HotLeadsPropertyViewAdapter;
import com.smaat.renterblock.entity.ChatInputEntity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.CreateGroupChatResponse;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.PreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;



public class HotLeadsViewerFragment extends BaseFragment {
    @BindView(R.id.hot_lead_recycler_view)
    RecyclerView mLeadRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_hotleads, container, false);
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

            AppConstants.TAG = this.getClass().getSimpleName();

             /*Slide menu action*/
            ((HomeScreen) getActivity()).setDrawerAction(false);

            /*set header first  img*/
            ((HomeScreen) getActivity()).setHeaderLeftFirstImgLayVisible(1);
            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.mipmap.app_icon, 2);

            /*set header second img*/
            ((HomeScreen) getActivity()).setHeaderLeftSecondImgLayVisible(R.mipmap.app_icon, 0);
            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.drawable.filter_icon, 0,"");

            /*set header third txt*/
            ((HomeScreen) getActivity()).setHeaderLeftThirdTxtLayVisible(getString(R.string.app_name), 0);
            ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.app_name), 0);

            /*set header txt*/
            ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.hot_leads), 1);
            ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.app_name), 0);



            initView();
        }
    }
    private void initView(){
        /*set adapter*/
        HotLeadsPropertyViewAdapter hotLeadsPropertyViewAdapter = new HotLeadsPropertyViewAdapter(HotLeadsViewerFragment.this,AppConstants.LEADS_VIEW_LIST);
        mLeadRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLeadRecyclerView.setAdapter(hotLeadsPropertyViewAdapter);
        mLeadRecyclerView.setNestedScrollingEnabled(false);

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
