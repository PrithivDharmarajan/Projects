package com.smaat.renterblock.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.smaat.renterblock.R;
import com.smaat.renterblock.adapters.FriendsListAdapter;
import com.smaat.renterblock.entity.AcceptFriendEntity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.FriendsResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FriendsListFragment extends BaseFragment {

    @BindView(R.id.friends_list_recycler_view)
    RecyclerView mFriendsListRecyclerView;

    @BindView(R.id.start_chat_btn)
    Button mStartChatBtn;

    ArrayList<AcceptFriendEntity> mFriendsListArray = new ArrayList<>();
    FriendsListAdapter mFriendsListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_friends_list, container, false);
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

            /*Slide menu action*/
            ((HomeScreen) getActivity()).setDrawerAction(false);

            /*set header first  img*/
            ((HomeScreen) getActivity()).setHeaderLeftFirstImgLayVisible(1);
            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.add_button, 2);

            /*set header second img*/
            ((HomeScreen) getActivity()).setHeaderLeftSecondImgLayVisible(R.mipmap.app_icon, 2);
            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.drawable.friends_icon_white_color, 2,"");

            /*set header third txt*/
            ((HomeScreen) getActivity()).setHeaderLeftThirdTxtLayVisible(getString(R.string.app_name), 0);
            ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.send), 0);

            /*set header txt*/
            ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.friends_list), 1);
            ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.friends), 0);

            initView();
        }
    }

    private void initView() {
        callFriendsList();

        mStartChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogManager.getInstance().showToast(getActivity(), "Chat Started");
            }
        });
    }

    private void callFriendsList() {
        APIRequestHandler.getInstance().friendsList(FriendsListFragment.this);
    }

    private void friendsAdapter() {

        if (getActivity() != null) {
            if (mFriendsListAdapter == null) {
                mFriendsListAdapter = new FriendsListAdapter(getActivity(), mFriendsListArray);
                mFriendsListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mFriendsListRecyclerView.setAdapter(mFriendsListAdapter);
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mFriendsListAdapter.notifyDataSetChanged();
                    }
                });
            }
        }

    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        if (responseObj instanceof FriendsResponse) {
            FriendsResponse friendsResponse = (FriendsResponse) responseObj;
            if (friendsResponse.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                mFriendsListArray = friendsResponse.getResult().get(0).getAccept_friend();
                friendsAdapter();
            }
        }
    }
}
