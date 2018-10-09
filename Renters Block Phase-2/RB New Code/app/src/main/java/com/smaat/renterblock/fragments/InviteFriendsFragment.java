package com.smaat.renterblock.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.AcceptFriendEntity;
import com.smaat.renterblock.entity.FriendDetailsEntity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.FriendsResponse;
import com.smaat.renterblock.sectionrecyclerview.HeaderRecyclerViewSection;
import com.smaat.renterblock.sectionrecyclerview.ItemObject;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class InviteFriendsFragment extends BaseFragment {
    @BindView(R.id.invite_recycler_view)
    RecyclerView mInviteRecyclerView;


    ArrayList<AcceptFriendEntity> mAcceptFriendEntity = new ArrayList<>();

    private SectionedRecyclerViewAdapter mSectionAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_invite_friends, container, false);

        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);

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
        /*set header text and header right img*/

        initView();
        ((HomeScreen) getActivity()).mHeaderFullLay.setVisibility(View.VISIBLE);
  /*set header txt*/
        ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.invite_friend), 1);
        ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.invite_friend), 0);


        ((HomeScreen) getActivity()).setDrawerAction(false);

        ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.drawable.add_img, 2,"");
        ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.add_img, 0);
        AppConstants.INVITE_FRIENDS_SCHEDULE = true;


    }

    private void initView() {
  /*To create initial View*/

        APIRequestHandler.getInstance().friendsList(InviteFriendsFragment.this);

    }

    private void setAdapter(ArrayList<AcceptFriendEntity> mAcceptFriendEntity) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mInviteRecyclerView.setLayoutManager(linearLayoutManager);
        mInviteRecyclerView.setHasFixedSize(true);
        mSectionAdapter = new SectionedRecyclerViewAdapter();
        ArrayList<AcceptFriendEntity> selectList = selectedFriends();


        if (selectList.size() > 0) {
            ArrayList mAcceptFriendEntity1 = new ArrayList<>();

            for (int i = 0; i < mAcceptFriendEntity.size(); i++) {

                boolean isThereBool = false;

                for (int j = 0; j < selectList.size(); j++) {
                    if (mAcceptFriendEntity.get(i).getFriends_details().get(0).getUser_friend_id().equals(selectList.get(j).getFriends_details().get(0).getUser_friend_id())) {
                        isThereBool = true;
                        break;
                    }
                }

                if (!isThereBool) {
                    mAcceptFriendEntity1.add(mAcceptFriendEntity.get(i));
                }
            }

            mAcceptFriendEntity.clear();
            mAcceptFriendEntity.addAll(mAcceptFriendEntity1);
        }


        HeaderRecyclerViewSection selectedSection = new HeaderRecyclerViewSection("User In Schedule", selectedFriends(), mSectionAdapter);
        HeaderRecyclerViewSection inviteSection = new HeaderRecyclerViewSection("Invite Friends", mAcceptFriendEntity, mSectionAdapter);


        mSectionAdapter.addSection(selectedSection);
        mSectionAdapter.addSection(inviteSection);
        mInviteRecyclerView.setAdapter(mSectionAdapter);

    }

    private ArrayList<AcceptFriendEntity> selectedFriends() {
        ArrayList<FriendDetailsEntity> acceptFriendEntityList = new ArrayList<>();
        ArrayList<AcceptFriendEntity> mAcceptFriendEntity = new ArrayList<>();
        if (AppConstants.INVITE_FRIENDS_HASH_MAP.size() > 0) {
            Iterator it = AppConstants.INVITE_FRIENDS_HASH_MAP.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                System.out.println(pair.getKey() + " = " + pair.getValue());
                acceptFriendEntityList = (ArrayList<FriendDetailsEntity>) pair.getValue();

                AcceptFriendEntity acceptFriendEntity = new AcceptFriendEntity();
                acceptFriendEntity.setFriends_details(acceptFriendEntityList);

                mAcceptFriendEntity.add(acceptFriendEntity);

                //  it.remove(); // avoids a ConcurrentModificationException

            }
        }
        return mAcceptFriendEntity;
    }


    private ArrayList<ItemObject> getDataSource() {
        ArrayList<ItemObject> data = new ArrayList<ItemObject>();

        data.add(new ItemObject("This is the item content in the second position"));
        return data;
    }

    @Override
    public void onRequestSuccess(Object resObj) {
        if (resObj instanceof FriendsResponse) {
            FriendsResponse friendsResponse = (FriendsResponse) resObj;
            if (friendsResponse.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                mAcceptFriendEntity = friendsResponse.getResult().get(0).getAccept_friend();
                setAdapter(mAcceptFriendEntity);
            }
        }
    }

    @OnClick({R.id.invite_update_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invite_update_btn:
                if (getActivity() != null)
                    getActivity().onBackPressed();
                break;


        }


    }
}
