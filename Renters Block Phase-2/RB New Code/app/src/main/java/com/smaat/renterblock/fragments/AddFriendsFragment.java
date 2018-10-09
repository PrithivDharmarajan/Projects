package com.smaat.renterblock.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.smaat.renterblock.R;
import com.smaat.renterblock.adapters.FriendsSearchAdapter;
import com.smaat.renterblock.entity.FriendsSearchResponseArray;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.FriendsRecentListResponse;
import com.smaat.renterblock.model.FriendsSearchResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.InterfaceBtnCallback;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 10/12/2017.
 */

public class AddFriendsFragment extends BaseFragment {

    @BindView(R.id.start_chat_btn)
    Button mStartChatBtn;

    @BindView(R.id.friends_list_recycler_view)
    RecyclerView mFriendsSearchRecyclerView;

    String searchKeyStr;
    ArrayList<FriendsSearchResponseArray> mFriendsSearchArray = new ArrayList<>();
    FriendsSearchAdapter mFriendsSearchAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_friends_list, container, false);
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(rootView);

        mStartChatBtn.setVisibility(View.GONE);

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

//        callFriendsList();

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
            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.search_icon, 1);

            /*set header second img*/
            ((HomeScreen) getActivity()).setHeaderLeftSecondImgLayVisible(R.mipmap.app_icon, 2);
            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.drawable.friends_icon_white_color, 2,"");

            /*set header third txt*/
            ((HomeScreen) getActivity()).setHeaderLeftThirdTxtLayVisible(getString(R.string.app_name), 0);
            ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.send), 0);

            /*set header txt*/
            ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.friends), 0);
            ((HomeScreen) getActivity()).mHeaderEdt.setText("");
            ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.search_for_peoples), 1);

            initView();

        }
    }

    private void initView() {
        searchKeyStr = ((HomeScreen) getActivity()).mHeaderEdt.getText().toString().trim();

        ((HomeScreen) getActivity()).mHeaderRightFirstImgLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callFriendsSearchAPI();
            }
        });

        ((HomeScreen) getActivity()).mHeaderEdt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == keyEvent.KEYCODE_ENTER) {
                    if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN)) {
                        searchKeyStr = ((HomeScreen) getActivity()).mHeaderEdt.getText().toString().trim();
                        callFriendsSearchAPI();
                        return true;

                    }
                }
                return false;
            }
        });

        ((HomeScreen) getActivity()).mHeaderEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ((HomeScreen) getActivity()).mHeaderEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ((HomeScreen) getActivity()).mHeaderEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return false;
            }
        });
    }

    private void callFriendsSearchAPI() {
        APIRequestHandler.getInstance().friendSearchList(searchKeyStr, AddFriendsFragment.this);
    }

    private void friendsSearchAdapter() {
        if (getActivity() != null) {
            if (mFriendsSearchAdapter == null) {
                mFriendsSearchAdapter = new FriendsSearchAdapter(getActivity(), mFriendsSearchArray,this);
                mFriendsSearchRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mFriendsSearchRecyclerView.setAdapter(mFriendsSearchAdapter);
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mFriendsSearchAdapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        if (responseObj instanceof FriendsSearchResponse) {
            FriendsSearchResponse friendsSearchResponse = (FriendsSearchResponse) responseObj;
            if (friendsSearchResponse.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                mFriendsSearchArray = friendsSearchResponse.getResult();

                if (mFriendsSearchArray.size() > 0) {
                    friendsSearchAdapter();
                } else {
                    DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.no_results_found), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {

                        }
                    });
                }
            }
        }else if (responseObj instanceof FriendsRecentListResponse) {
            FriendsRecentListResponse mResponse = (FriendsRecentListResponse) responseObj;
            DialogManager.getInstance().showAlertPopup(getActivity(), mResponse.getMsg(),
                    new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {

                        }
                    });
        }
    }
}
