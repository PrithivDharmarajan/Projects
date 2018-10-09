package com.smaat.renterblock.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.smaat.renterblock.R;
import com.smaat.renterblock.adapters.FeedsAdapter;
import com.smaat.renterblock.entity.ProfileMyFeedsEntity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sys on 10/9/2017.
 */

public class FeedsPhotosAndVideosFragment extends BaseFragment {

    @BindView(R.id.feeds_recycler_view)
    RecyclerView mFeedsRecyclerView;

    private ArrayList<ProfileMyFeedsEntity> mPhotosVideoList = new ArrayList<>();
    private ProfileMyFeedsEntity mProfileMyFeedsEntity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_feed_photos_videos, container, false);
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);
        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(rootView);

        setAdapter();

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
         /* If the value of visibleInt is zero,  the view will set gone. Or if the value of visibleInt is one,  the view will set visible. Or else, the view will set gone*/
        if (getActivity() != null) {
             /*Slide menu action*/
            ((HomeScreen) getActivity()).setDrawerAction(false);

            /*set header first  img*/
            ((HomeScreen) getActivity()).setHeaderLeftFirstImgLayVisible(1);
            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.add_icon, 2);

            /*set header second img*/
            ((HomeScreen) getActivity()).setHeaderLeftSecondImgLayVisible(R.mipmap.app_icon, 0);
            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.mipmap.app_icon, 0,"");

            /*set header third txt*/
            ((HomeScreen) getActivity()).setHeaderLeftThirdTxtLayVisible(getString(R.string.app_name), 0);
            ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.view_listing), 0);

            /*set header txt*/
            ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.photo_video), 1);
            ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.app_name), 0);

            ((HomeScreen) getActivity()).mHeaderRightThirdTxtLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO redirect to map screen
                }
            });
        }
    }


    private void setAdapter() {
        mPhotosVideoList.clear();
        for (int i = 0; i < AppConstants.FEEDS_LIST.size(); i++) {
            if (AppConstants.FEEDS_LIST.get(i).getFile() != null) {
                mProfileMyFeedsEntity = new ProfileMyFeedsEntity();
                mProfileMyFeedsEntity.setUser_id(AppConstants.FEEDS_LIST.get(i).getUser_id());
                mProfileMyFeedsEntity.setProperty_id(AppConstants.FEEDS_LIST.get(i).getProperty_id());
                mProfileMyFeedsEntity.setFile(AppConstants.FEEDS_LIST.get(i).getFile());
                mProfileMyFeedsEntity.setFile_type(AppConstants.FEEDS_LIST.get(i).getFile_type());
                mProfileMyFeedsEntity.setPicture_id(AppConstants.FEEDS_LIST.get(i).getPicture_id());
                mProfileMyFeedsEntity.setDatetime(AppConstants.FEEDS_LIST.get(i).getDatetime());
                mProfileMyFeedsEntity.setProperty_name(AppConstants.FEEDS_LIST.get(i)
                        .getProperty_name());
                mProfileMyFeedsEntity.setFriends_count(AppConstants.FEEDS_LIST.get(i)
                        .getFriends_count());
                mProfileMyFeedsEntity.setReviews_count(AppConstants.FEEDS_LIST.get(i)
                        .getReviews_count());
                mProfileMyFeedsEntity
                        .setPhotos_Count(AppConstants.FEEDS_LIST.get(i).getPhotos_Count());
                mProfileMyFeedsEntity.setUser_name(AppConstants.FEEDS_LIST.get(i).getUser_name());
                mProfileMyFeedsEntity.setUser_profile_image(AppConstants.FEEDS_LIST.get(i)
                        .getUser_profile_image());
                mProfileMyFeedsEntity.setAddress(AppConstants.FEEDS_LIST.get(i).getAddress());
                mProfileMyFeedsEntity.setDescription(AppConstants.FEEDS_LIST.get(i).getDescription());
                mPhotosVideoList.add(mProfileMyFeedsEntity);
            }
        }


        FeedsAdapter feedsAdapter = new FeedsAdapter(getActivity(), mPhotosVideoList);
        mFeedsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFeedsRecyclerView.setAdapter(feedsAdapter);
    }


}
