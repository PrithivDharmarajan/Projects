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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smaat.renterblock.R;
import com.smaat.renterblock.adapters.FriendsAdapter;
import com.smaat.renterblock.adapters.RecentFriendsListAdapter;
import com.smaat.renterblock.entity.AcceptFriendEntity;
import com.smaat.renterblock.entity.ChatInputEntity;
import com.smaat.renterblock.entity.FriendsDetailsEntity;
import com.smaat.renterblock.entity.FriendsRecentListArray;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.CreateGroupChatResponse;
import com.smaat.renterblock.model.FriendsRecentListResponse;
import com.smaat.renterblock.model.FriendsResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.PreferenceUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FriendsFragment extends BaseFragment {

    @BindView(R.id.friends_recycler_view)
    RecyclerView mFriendsRecyclerView;

    @BindView(R.id.recent_lay)
    LinearLayout mRecentLay;

    @BindView(R.id.friends_lay)
    LinearLayout mFriendsLay;

    @BindView(R.id.friends_txt)
    TextView mFriendsTxt;

    @BindView(R.id.recents_view_line)
    View mRecentsViewLine;

    @BindView(R.id.friends_view_line)
    View mFriendsViewLine;

    @BindView(R.id.recent_friends_recycler_view)
    RecyclerView mRecentFriendsRecylerView;

    @BindView(R.id.user_name_search_edt)
    EditText mUserNameSearchEdt;

    String mSearchStr;
    private String listView_type = "friends";
    RecentFriendsListAdapter mRecentFriendsListAdapter;
    FriendsAdapter mFriendsAdapter;

    ArrayList<FriendsRecentListArray> mRecentListArray = new ArrayList<>();
    ArrayList<FriendsRecentListArray> mSortedRecentList = new ArrayList<>();
    ArrayList<FriendsDetailsEntity> mFriendsListArray = new ArrayList<>();
    ArrayList<AcceptFriendEntity> mAcceptedFriendsList = new ArrayList<>();
    ArrayList<AcceptFriendEntity> mSortedFriendsList = new ArrayList<>();
    private String mPendingCountStr = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_friends_screen, container, false);
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
            ((HomeScreen) getActivity()).setDrawerAction(true);

            /*set header first  img*/
            ((HomeScreen) getActivity()).setHeaderLeftFirstImgLayVisible(1);
            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.add_button, 1);

            /*set header second img*/
            ((HomeScreen) getActivity()).setHeaderLeftSecondImgLayVisible(R.mipmap.app_icon, 2);
            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.drawable.friends_icon_white_color, 1,mPendingCountStr);

            /*set header third txt*/
            ((HomeScreen) getActivity()).setHeaderLeftThirdTxtLayVisible(getString(R.string.app_name), 0);
            ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.send), 0);

            /*set header txt*/
            ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.friends), 1);
            ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.friends), 0);
            ((HomeScreen) getActivity()).mHeaderRightSecondImgLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((HomeScreen) getActivity()).addFragment(new FriendPendingRequestFragment());
                }
            });

            ((HomeScreen) getActivity()).mHeaderRightFirstImgLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((HomeScreen) getActivity()).addFragment(new AddFriendsFragment());
                }
            });

            mRecentFriendsRecylerView.setVisibility(View.GONE);
            mFriendsRecyclerView.setVisibility(View.VISIBLE);
            mRecentsViewLine.setVisibility(View.GONE);
            mFriendsViewLine.setVisibility(View.VISIBLE);


        }
    }


    @Override
    public void onResume() {
        super.onResume();
        callFriendsList();

        initView();
    }

    private void initView() {


//        mUserNameSearchEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                if (listView_type.equalsIgnoreCase("friends")) {
//                    reloadFriendsView(mSearchStr);
//                } else {
//
//                }
//                return false;
//            }
//        });

        mUserNameSearchEdt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == keyEvent.KEYCODE_ENTER) {
                    if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN)) {

                        mSearchStr = mUserNameSearchEdt.getText().toString().toLowerCase();

                        if (listView_type.equalsIgnoreCase("friends")) {
                            reloadFriendsView(mSearchStr);
                        } else {
                            reloadRecentFriendsView(mSearchStr);
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        mUserNameSearchEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return false;
            }
        });

        mUserNameSearchEdt.addTextChangedListener(new TextWatcher() {
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

    }

    private void reloadFriendsView(String name) {
        mSortedFriendsList = new ArrayList<>();
        for (int i = 0; i < mAcceptedFriendsList.size(); i++) {
            if (mAcceptedFriendsList.get(i).getFriends_details().size() > 0 && mAcceptedFriendsList.get(i).getFriends_details().get(0).getUser_name().toLowerCase().contains(name)) {
                mSortedFriendsList.add(mAcceptedFriendsList.get(i));
            }
        }

        friendsAdapter(mSortedFriendsList);
        mUserNameSearchEdt.setText("");
    }

    private void reloadRecentFriendsView(String name) {

        for (int i = 0; i < mRecentListArray.size(); i++) {
            if (mRecentListArray.get(i).getUser_name().toLowerCase().contains(name)) {
                mSortedRecentList.add(mRecentListArray.get(i));
            }

        }
        friendsRecentListAdapter(mSortedRecentList);
        mUserNameSearchEdt.setText("");
    }

    /*View OnClick*/
    @OnClick({R.id.recent_lay, R.id.friends_lay})
    public void onClick(View v) {
        if (getActivity() != null) {
            switch (v.getId()) {
                case R.id.recent_lay:
                    mRecentFriendsRecylerView.setVisibility(View.VISIBLE);
                    mFriendsRecyclerView.setVisibility(View.GONE);
                    mRecentsViewLine.setVisibility(View.VISIBLE);
                    mFriendsViewLine.setVisibility(View.GONE);
                    ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.drawable.chat_icons_white_color, 1, "");
                    ((HomeScreen) getActivity()).mHeaderRightSecondImgLay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ((HomeScreen) getActivity()).addFragment(new FriendsListFragment());
                        }
                    });

                    ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.call_icon_white_color, 1);
                    ((HomeScreen) getActivity()).mHeaderRightFirstImgLay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DialogManager.getInstance().callSelection(getActivity());
                        }
                    });
                    callRecentFriendList();
                    break;
                case R.id.friends_lay:
                    mRecentFriendsRecylerView.setVisibility(View.GONE);
                    mFriendsRecyclerView.setVisibility(View.VISIBLE);
                    mRecentsViewLine.setVisibility(View.GONE);
                    mFriendsViewLine.setVisibility(View.VISIBLE);
                    ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.drawable.friends_icon_white_color, 1, mPendingCountStr);
                    ((HomeScreen) getActivity()).mHeaderRightSecondImgLay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ((HomeScreen) getActivity()).addFragment(new FriendPendingRequestFragment());
                        }
                    });
                    ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.add_button, 1);
                    ((HomeScreen) getActivity()).mHeaderRightFirstImgLay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ((HomeScreen) getActivity()).addFragment(new AddFriendsFragment());
                        }
                    });

                    callFriendsList();
                    break;
            }
        }
    }

    private void callRecentFriendList() {
        APIRequestHandler.getInstance().friendsRecentList(FriendsFragment.this);
    }

    private void callFriendsList() {
        APIRequestHandler.getInstance().friendsList(FriendsFragment.this);
    }

    private void friendsRecentListAdapter(ArrayList<FriendsRecentListArray> mRecentListArray) {

        if (getActivity() != null) {
            mRecentFriendsListAdapter = new RecentFriendsListAdapter(getActivity(), mRecentListArray, this);
            mRecentFriendsRecylerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecentFriendsRecylerView.setAdapter(mRecentFriendsListAdapter);
        }

    }

    private void friendsAdapter(ArrayList<AcceptFriendEntity> mFriendsListEntity) {
        if (getActivity() != null) {

            if (mFriendsListArray.size() != 0) {
                String getFriendsCount = mFriendsListArray.get(0).getGetfriends();
//            img_in.setText(mFriendsListArray.get(0).getPending_count());
                if (getFriendsCount != null) {
                    mFriendsTxt.setText(getString(R.string.friends) + "(" + getFriendsCount + ")");
                } else {
                    mFriendsTxt.setText(getString(R.string.friends_empty_count_txt));
                }
            }

            mFriendsAdapter = new FriendsAdapter(getActivity(), mFriendsListEntity, this);
            mFriendsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mFriendsRecyclerView.setAdapter(mFriendsAdapter);

        }
    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        if (getActivity() != null) {
            if (responseObj instanceof FriendsRecentListResponse) {
                FriendsRecentListResponse friendsRecentListResponse = (FriendsRecentListResponse) responseObj;
                if (friendsRecentListResponse.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                    mRecentListArray = friendsRecentListResponse.getResult();
                    friendsRecentListAdapter(mRecentListArray);
                }
            } else if (responseObj instanceof FriendsResponse) {
                FriendsResponse friendsResponse = (FriendsResponse) responseObj;


                if (friendsResponse.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                    mFriendsListArray = friendsResponse.getResult();

                    mAcceptedFriendsList = new ArrayList<>();
                    mAcceptedFriendsList.addAll(mFriendsListArray.get(0).getAccept_friend());
                    mAcceptedFriendsList.addAll(mFriendsListArray.get(0).getSent_pending_details());
                    mPendingCountStr = friendsResponse.getResult().get(0).getPending_count().toString();
                    ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.drawable.friends_icon_white_color, 1, mPendingCountStr);
//

                    friendsAdapter(mAcceptedFriendsList);
                }
            } else if (responseObj instanceof CreateGroupChatResponse) {
                CreateGroupChatResponse chatIdRes = (CreateGroupChatResponse) responseObj;
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
}
