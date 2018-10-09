package com.smaat.renterblock.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.smaat.renterblock.R;
import com.smaat.renterblock.adapters.FriendPendingRequestAdapter;
import com.smaat.renterblock.entity.PendingDetailsEntity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.model.PendingRequestResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.InterfaceBtnCallback;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FriendPendingRequestFragment extends BaseFragment {

    @BindView(R.id.friends_list_recycler_view)
    RecyclerView mFriendsListRecyclerView;

    @BindView(R.id.start_chat_btn)
    Button mStartChatBtn;

    ArrayList<PendingDetailsEntity> mPendingDetailsArrayList = new ArrayList<>();
  //  FriendPendingRequestAdapter mFriendPendingRequestAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

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
            ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.friend_request), 1);
            ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.friends), 0);

            initView();
        }
    }

    private void initView() {
        callPendingRequestsList();
        mStartChatBtn.setVisibility(View.GONE);
    }

    private void callPendingRequestsList() {
        APIRequestHandler.getInstance().pendingFriendRequest(FriendPendingRequestFragment.this);
    }

    private void friendsPendingDetailsAdapter() {

        //if (mFriendPendingRequestAdapter == null) {
        FriendPendingRequestAdapter mFriendPendingRequestAdapter = new FriendPendingRequestAdapter(getActivity(), mPendingDetailsArrayList, this);
        mFriendsListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFriendsListRecyclerView.setAdapter(mFriendPendingRequestAdapter);
//        } else {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    mFriendPendingRequestAdapter.notifyDataSetChanged();
//                }
//            });
//        }
    }


    @Override
    public void onRequestSuccess(Object responseObj) {
        if (responseObj instanceof PendingRequestResponse) {
            PendingRequestResponse friendPendingResponse = (PendingRequestResponse) responseObj;
            if (friendPendingResponse.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                mPendingDetailsArrayList = friendPendingResponse.getResult();
                if(mPendingDetailsArrayList.size()==0){
                    DialogManager.getInstance().showAlertPopup(getActivity(),"No Friend Request",this);

                }
                friendsPendingDetailsAdapter();
            }
        } else if (responseObj instanceof CommonResponse) {
            CommonResponse mCommonResponse = (CommonResponse) responseObj;
            if (mCommonResponse.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                DialogManager.getInstance().showAlertPopup(getActivity(), mCommonResponse.getMsg(), new InterfaceBtnCallback() {
                    @Override
                    public void onPositiveClick() {
                        callPendingRequestsList();
                    }
                });
            }
        }
    }
}
