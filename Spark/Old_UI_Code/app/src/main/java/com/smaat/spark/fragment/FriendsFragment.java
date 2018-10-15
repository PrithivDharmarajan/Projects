package com.smaat.spark.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smaat.spark.R;
import com.smaat.spark.adapter.FriendsAdapter;
import com.smaat.spark.entity.inputEntity.ChatConnDisInputEntity;
import com.smaat.spark.entity.outputEntity.UserDetailsEntity;
import com.smaat.spark.main.BaseFragment;
import com.smaat.spark.model.FriendsListResponse;
import com.smaat.spark.services.APIRequestHandler;
import com.smaat.spark.ui.HomeScreen;
import com.smaat.spark.utils.AppConstants;
import com.smaat.spark.utils.DialogManager;
import com.smaat.spark.utils.GlobalMethods;
import com.smaat.spark.utils.InterfaceBtnCallback;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FriendsFragment extends BaseFragment implements InterfaceBtnCallback {


    @BindView(R.id.search_edt)
    EditText mSearchEdt;

    @BindView(R.id.friends_list)
    RecyclerView mFriendsRecyclerList;

    @BindView(R.id.friends_list_lay)
    LinearLayout mFriendsListLay;

    @BindView(R.id.invite_lay)
    LinearLayout mInviteLay;

    private ArrayList<UserDetailsEntity> mFriendsArrList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_friends_screen, container, false);
        ButterKnife.bind(this, rootView);
        setupUI(rootView);
        mFriendsArrList = new ArrayList<>();
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        ((HomeScreen) getActivity()).setDrawerAction(false);
        ((HomeScreen) getActivity()).setHeaderText(getString(R.string.friends));
        ((HomeScreen) getActivity()).setHeadRigImgVisible(true, R.drawable.friends_add_img);
        mSearchEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchFriends(mSearchEdt.getText().toString().trim());
                    hideSoftKeyboard();
                    return true;
                } else {
                    return false;
                }
            }
        });
        mSearchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchFriends(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ((HomeScreen) getActivity()).mHeaderRightBtnLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConstants.INVITE_ALL_USER = AppConstants.SUCCESS_CODE;
                ((HomeScreen) getActivity()).addFragment(new InviteFriendsFragment());
            }
        });
        ChatConnDisInputEntity friendListInputEntityRes = new ChatConnDisInputEntity(AppConstants.API_FRIEND_LIST, AppConstants.PARAMS_USER_ID, GlobalMethods.getUserID(getActivity()));
        APIRequestHandler.getInstance().callFriendsAndUserListAPI(friendListInputEntityRes, this);
    }


    @OnClick({R.id.find_contact_lay, R.id.share_user_name_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find_contact_lay:
                AppConstants.INVITE_ALL_USER = AppConstants.FAILURE_CODE;
                ((HomeScreen) getActivity()).addFragment(new InviteFriendsFragment());
                break;
            case R.id.share_user_name_lay:
                UserDetailsEntity userDetailsRes = GlobalMethods.getUserDetailsRes(getActivity());
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name) + " - " + getString(R.string.invite_friend));
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string
                        .share_txt) + "\n\n\t" + getString(R.string.username) + " " + userDetailsRes.getUsername()
                        + "\n\n\t" + getString(R.string.android_app) + " " + AppConstants.ANDROID_SPARK_APP
                        + "\n\n\t" + getString(R.string.ios_app) + " " + AppConstants.IOS_SPARK_APP);
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.invite_friend)));
                break;

        }


    }

    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);

        if (resObj instanceof FriendsListResponse) {

            FriendsListResponse friendsListRes = (FriendsListResponse) resObj;

            if (friendsListRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                mFriendsArrList = friendsListRes.getResult();

                if (mFriendsArrList.size() > 0) {
                    mInviteLay.setVisibility(View.GONE);
                    mFriendsListLay.setVisibility(View.VISIBLE);
                    mFriendsRecyclerList.setVisibility(View.VISIBLE);
                    setFriendListAdapter(mFriendsArrList);
                } else {
                    mInviteLay.setVisibility(View.VISIBLE);
                    mFriendsListLay.setVisibility(View.GONE);
                    mFriendsRecyclerList.setVisibility(View.GONE);
                }
                try {
                    mSearchEdt.setText("");
                } catch (Exception e) {
                    Log.d(AppConstants.TAG, e.toString());
                }
            } else {
                DialogManager.getInstance().showAlertPopup(getActivity(), friendsListRes.getMessage());
            }

        }
    }

    private void searchFriends(String searchStr) {

        if (searchStr.trim().isEmpty() && mFriendsArrList.size() > 0) {
            setFriendListAdapter(mFriendsArrList);
        } else {
            ArrayList<UserDetailsEntity> friendsLocArrList = new ArrayList<>();
            if (mFriendsArrList != null && mFriendsArrList.size() > 0) {
                for (int i = 0; i < mFriendsArrList.size(); i++) {
                    String searchName = mFriendsArrList.get(i).getUsername().toLowerCase(Locale.ENGLISH);
                    if (searchName.contains(searchStr.toLowerCase(Locale.ENGLISH))) {
                        friendsLocArrList.add(mFriendsArrList.get(i));
                    }
                }
                setFriendListAdapter(friendsLocArrList);
            }
        }

    }

    private void setFriendListAdapter(ArrayList<UserDetailsEntity> friendsList) {

        FriendsAdapter adapter = new FriendsAdapter(getActivity(), friendsList);
        mFriendsRecyclerList.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mFriendsRecyclerList.setAdapter(adapter);
        mFriendsRecyclerList.setNestedScrollingEnabled(false);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onYesClick() {

    }
}