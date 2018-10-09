package com.smaat.spark.fragment;

import android.content.ContentResolver;
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


public class InviteFriendsFragment extends BaseFragment implements InterfaceBtnCallback {


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
        ((HomeScreen) getActivity()).setDrawerAction(false);
        ((HomeScreen) getActivity()).setHeaderText(getString(R.string.invite_friend));
        ((HomeScreen) getActivity()).setHeadRigImgVisible(false, R.mipmap.app_icon);

        mUserDetailsEntityRes = GlobalMethods.getUserDetailsRes(getActivity());
        mFriendSearchEdt.setHint(getString(R.string.find_by_username));

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
                    if (AppConstants.INVITE_ALL_USER.equals(AppConstants.SUCCESS_CODE)) {
                        for (int i = 0; i < inviteAllList.size(); i++) {
                            if (inviteAllList.get(i).getFriend().equals(AppConstants.FAILURE_CODE)) {
                                //Not a friend in your list
                                inviteAllList.get(i).setInvite_btn_name(getString(R.string.add));
                                mInviteArrList.add(inviteAllList.get(i));
                            }
                        }
                    } else {
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
                    }
                    DialogManager.hideProgress();
                    if (mInviteArrList.size() > 0)
                        setInviteUserListAdapter(mInviteArrList);
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
        if (mInviteArrList.size() > 0) {
            if (searchStr.trim().isEmpty()) {
                setInviteUserListAdapter(mInviteArrList);
            } else {
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
    }

    private void setInviteUserListAdapter(ArrayList<UserDetailsEntity> friendsList) {
        InviteFriendsAdapter adapter = new InviteFriendsAdapter(getActivity(), this, friendsList);
        mInviteRecyclerList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mInviteRecyclerList.setAdapter(adapter);
        mInviteRecyclerList.setNestedScrollingEnabled(false);
        mInviteRecyclerList.setVisibility(View.VISIBLE);

    }

    @Override
    public void onYesClick() {

    }

    private static final String[] PROJECTION = new String[]{
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Email.DATA
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
