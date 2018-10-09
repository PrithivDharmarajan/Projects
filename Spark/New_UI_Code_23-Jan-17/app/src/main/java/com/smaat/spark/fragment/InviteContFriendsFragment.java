package com.smaat.spark.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.TextView;

import com.smaat.spark.R;
import com.smaat.spark.adapter.InviteFriendsAdapter;
import com.smaat.spark.entity.inputEntity.ChatConnDisInputEntity;
import com.smaat.spark.entity.outputEntity.UserDetailsEntity;
import com.smaat.spark.main.BaseFragment;
import com.smaat.spark.model.CommonResponse;
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


public class InviteContFriendsFragment extends BaseFragment implements InterfaceBtnCallback {


    @BindView(R.id.friend_search_edt)
    EditText mFriendSearchEdt;

    @BindView(R.id.friends_list)
    RecyclerView mInviteRecyclerList;

    private ArrayList<UserDetailsEntity> mInviteArrList;
    private UserDetailsEntity mUserDetailsEntityRes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_invite_friends_screen, container, false);
        ButterKnife.bind(this, rootView);
        setupUI(rootView);
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
        ((HomeScreen) getActivity()).setHeaderLeftClick(false);
        ((HomeScreen) getActivity()).setHeaderText(getString(R.string.invite_friend));
        ((HomeScreen) getActivity()).setHeadRigImgVisible(false, R.mipmap.app_icon);
        
        mFriendSearchEdt.setHint(getResources().getString(R.string.search_by_name));
        mUserDetailsEntityRes = GlobalMethods.getUserDetailsRes(getActivity());

        mFriendSearchEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchInviteFriends(mFriendSearchEdt.getText().toString().trim());
                    hideSoftKeyboard();
                    return true;
                } else {
                    return false;
                }
            }
        });

        mFriendSearchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchInviteFriends(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        callFriendListAPI();

    }

    @OnClick({R.id.share_user_name_lay})
    public void onClick(View v) {
        switch (v.getId()) {
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

    private void callFriendListAPI() {
        ChatConnDisInputEntity friendListInputEntityRes = new ChatConnDisInputEntity(AppConstants.API_ALL_USER_LIST, AppConstants.PARAMS_USER_ID, GlobalMethods.getUserID(getActivity()));
        APIRequestHandler.getInstance().callFriendsAndUserListAPI(friendListInputEntityRes, this);
    }

    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);

        if (resObj instanceof FriendsListResponse) {

            FriendsListResponse friendsListRes = (FriendsListResponse) resObj;

            if (friendsListRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                try {
                    DialogManager.showProgress(getActivity());
                    mInviteArrList = new ArrayList<>();
                    mFriendSearchEdt.setText("");
                    ArrayList<UserDetailsEntity> inviteAllList = friendsListRes.getResult();
                    ArrayList<UserDetailsEntity> contactsUserArrList = getUserContactsDetails();
                    for (int i = 0; i < contactsUserArrList.size(); i++) {
                        boolean isContactsBool = true;
                        for (int j = 0; j < inviteAllList.size(); j++) {
                            if (contactsUserArrList.get(i).getEmail_id().equals(inviteAllList.get(j).getEmail_id())) {
                                if (inviteAllList.get(j).getFriend().equals(AppConstants.FAILURE_CODE)) {
                                    inviteAllList.get(j).setInvite_btn_name(getString(R.string.add));
                                    mInviteArrList.add(inviteAllList.get(j));
                                }
                                isContactsBool = false;
                                break;
                            }
                            if (contactsUserArrList.get(i).getEmail_id().equals(mUserDetailsEntityRes.getEmail_id())) {
                                isContactsBool = false;
                                break;
                            }
                        }
                        if (isContactsBool) {
                            contactsUserArrList.get(i).setInvite_btn_name(getString(R.string.invite));
                            mInviteArrList.add(contactsUserArrList.get(i));
                        }
                    }

                    DialogManager.hideProgress();
                    if (mInviteArrList.size() > 0) {
                        searchInviteFriends("");
                    } else {
                        DialogManager.showToast(getActivity(), getResources().getString(R.string.no_contacts));
                        getActivity().onBackPressed();
                    }
                } catch (Exception e) {
                    Log.d(AppConstants.TAG, e.toString());
                }
            } else {
                DialogManager.getInstance().showAlertPopup(getActivity(), friendsListRes.getMessage());
            }

        } else if (resObj instanceof CommonResponse) {
            CommonResponse addFriendRes = (CommonResponse) resObj;
            if (addFriendRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.friend_req_sent));
                callFriendListAPI();

            } else {
                DialogManager.getInstance().showAlertPopup(getActivity(), addFriendRes.getMessage());
            }

        }
    }

    private void searchInviteFriends(String searchStr) {

        if (searchStr.isEmpty() || !(mInviteArrList.size() > 0)) {
            setInviteUserListAdapter(mInviteArrList);
        } else if (mInviteArrList.size() > 0) {
            ArrayList<UserDetailsEntity> friendsLocArrList = new ArrayList<>();
            if (mInviteArrList != null) {
                for (int i = 0; i < mInviteArrList.size(); i++) {
                    String searchName = mInviteArrList.get(i).getUsername().toLowerCase(Locale.ENGLISH);
                    if (searchName.contains(searchStr.toLowerCase(Locale.ENGLISH))) {
                        friendsLocArrList.add(mInviteArrList.get(i));
                    }
                }
                setInviteUserListAdapter(friendsLocArrList);
            }
        }


    }

    private void setInviteUserListAdapter(final ArrayList<UserDetailsEntity> friendsList) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (friendsList.size() > 0) {
                    InviteFriendsAdapter adapter = new InviteFriendsAdapter(getActivity(), InviteContFriendsFragment.this, friendsList);
                    mInviteRecyclerList.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mInviteRecyclerList.setAdapter(adapter);
                    mInviteRecyclerList.setNestedScrollingEnabled(false);
                    mInviteRecyclerList.setVisibility(View.VISIBLE);
                } else {
                    mInviteRecyclerList.setVisibility(View.GONE);

                }
            }
        });


    }

    @Override
    public void onYesClick() {

    }

    private static final String[] PROJECTION = new String[]{
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Email.DATA,
            ContactsContract.CommonDataKinds.Phone.DATA
    };

    public ArrayList<UserDetailsEntity> getUserContactsDetails() {
        ArrayList<UserDetailsEntity> locContactsList = new ArrayList<>();
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, PROJECTION, null, null, null);
        if (cursor != null) {
            try {
                final int displayNameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                final int emailIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);

                String displayNameStr, emailAddStr;
                while (cursor.moveToNext()) {
                    displayNameStr = cursor.getString(displayNameIndex);
                    emailAddStr = cursor.getString(emailIndex);
                    if (emailAddStr != null && displayNameStr != null) {
                        UserDetailsEntity contactsList = new UserDetailsEntity();
                        contactsList.setUsername(displayNameStr);
                        contactsList.setEmail_id(emailAddStr);
                        locContactsList.add(contactsList);
                    }

                }
            } finally {
                cursor.close();
            }
        }
        return locContactsList;
    }
}
