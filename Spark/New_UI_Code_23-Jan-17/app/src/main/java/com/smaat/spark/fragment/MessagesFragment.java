package com.smaat.spark.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.smaat.spark.R;
import com.smaat.spark.adapter.ChatFriendsAdapter;
import com.smaat.spark.adapter.MessageAdapter;
import com.smaat.spark.database.ChatTable;
import com.smaat.spark.entity.inputEntity.ChatConnDisInputEntity;
import com.smaat.spark.entity.inputEntity.ChatSendReceiveInputEntity;
import com.smaat.spark.entity.inputEntity.NotificationInputEntity;
import com.smaat.spark.entity.outputEntity.ChatReceiveEntity;
import com.smaat.spark.entity.outputEntity.UserDetailsEntity;
import com.smaat.spark.main.BaseFragment;
import com.smaat.spark.model.ChatReceiveResponse;
import com.smaat.spark.model.CommonResponse;
import com.smaat.spark.model.FriendsListResponse;
import com.smaat.spark.services.APIRequestHandler;
import com.smaat.spark.swipemenu.SwipeMenu;
import com.smaat.spark.swipemenu.SwipeMenuCreator;
import com.smaat.spark.swipemenu.SwipeMenuItem;
import com.smaat.spark.swipemenu.SwipeMenuListView;
import com.smaat.spark.ui.HomeScreen;
import com.smaat.spark.utils.AppConstants;
import com.smaat.spark.utils.DialogManager;
import com.smaat.spark.utils.GlobalMethods;
import com.smaat.spark.utils.InterfaceTwoBtnCallback;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MessagesFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @BindView(R.id.message_swipe_list)
    SwipeMenuListView mMessageMenuList;

    @BindView(R.id.friends_lay)
    LinearLayout mFriendsLay;

    @BindView(R.id.friends_search_edt)
    EditText mFriendsSearchEdt;

    @BindView(R.id.friend_list)
    RecyclerView mFriendsRecyclerList;

    @BindView(R.id.no_msg_txt)
    TextView mNoMsgTxt;

    private int mDeletedMessagePos = -1;
    private ArrayList<ChatReceiveEntity> mMessageArrList ;
    private ArrayList<UserDetailsEntity> mFriendsArrList ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_messages_screen, container, false);
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeScreen) getActivity()).setHeaderLeftClick(true);
        ((HomeScreen) getActivity()).setHeadRigImgVisible(false, R.mipmap.app_icon);
        ((HomeScreen) getActivity()).mHeaderVisibleLay.setVisibility(View.GONE);
        ((HomeScreen) getActivity()).mHeaderInviteLay.setVisibility(View.GONE);
        ((HomeScreen) getActivity()).mHeaderTxt.setText(getResources().getString(R.string.messages));
        ((HomeScreen) getActivity()).mChatHeaderTxt.setText(getResources().getString(R.string.new_message));
        ((HomeScreen) getActivity()).setHeaderLay(3);
        initView();
        ((HomeScreen) getActivity()).mMsgRightBtnLay.setVisibility(mFriendsArrList.size() > 0 ? View.VISIBLE : View.INVISIBLE);

    }

    private void initView() {
        mMessageArrList = new ArrayList<>();
        mFriendsArrList = new ArrayList<>();


        ((HomeScreen) getActivity()).mMsgRightBtnLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFriendsArrList.size() > 0) {
                    if (mFriendsLay.getVisibility() == View.VISIBLE) {
                        mFriendsLay.setVisibility(View.GONE);
                        mMessageMenuList.setVisibility(View.VISIBLE);
                        ((HomeScreen) getActivity()).setHeaderLay(3);
                    } else if (mFriendsLay.getVisibility() == View.GONE) {
                        mMessageMenuList.setVisibility(View.GONE);
                        mFriendsLay.setVisibility(View.VISIBLE);
                        ((HomeScreen) getActivity()).setHeaderLay(2);
                    }
                } else {
                    DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.you_have_no_friends));
                }

            }
        });


        ((HomeScreen) getActivity()).mHeaderNextLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFriendsLay.setVisibility(View.GONE);
                mMessageMenuList.setVisibility(View.VISIBLE);
                ((HomeScreen) getActivity()).setHeaderLay(3);
            }
        });

        mFriendsSearchEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchFriends(mFriendsSearchEdt.getText().toString().trim());
                    hideSoftKeyboard();
                    return true;
                } else {
                    return false;
                }
            }
        });
        mFriendsSearchEdt.addTextChangedListener(new TextWatcher() {
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


        mMessageMenuList.setOnItemClickListener(MessagesFragment.this);
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem swipeMenuItem = new SwipeMenuItem(getActivity());
                swipeMenuItem.setBackground(R.drawable.msg_delete_bg);

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                swipeMenuItem.setWidth(displayMetrics.widthPixels / 3);
                swipeMenuItem.setTitle(getString(R.string.x));
                swipeMenuItem.setTitleColor(ContextCompat.getColor(getActivity(), R.color.white));
                swipeMenuItem.setTitleSize(30);
                menu.addMenuItem(swipeMenuItem);
            }
        };
        mMessageMenuList.setMenuCreator(creator);
        mMessageMenuList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final int position,
                                           SwipeMenu menu, int index) {

                if (index == 0) {
                    //Delete All API Call
                    DialogManager.getInstance().showOptionPopup(getActivity(), getString(R.string.do_you_want_del), getString(R.string.yes), getString(R.string.no), new InterfaceTwoBtnCallback() {
                        @Override
                        public void onYesClick() {

                            NotificationInputEntity deleteInput = new NotificationInputEntity(AppConstants.API_DELETE_CHAT, AppConstants.PARAMS_DELETE_CHAT, mMessageArrList.get(position).getUser_id(), mMessageArrList.get(position).getFriend_id(), mMessageArrList.get(position).getChat_id());
                            APIRequestHandler.getInstance()
                                    .callDeleteMsgAPI(deleteInput, MessagesFragment.this);
                            mDeletedMessagePos = position;

                        }

                        @Override
                        public void onNoClick() {

                        }
                    });

                }

                return false;
            }
        });
        mMessageMenuList.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        getMessageApiCall();
    }

    private void getMessageApiCall() {
        ChatSendReceiveInputEntity chatReceiveInputEntity = new ChatSendReceiveInputEntity(AppConstants.API_GET_INBOX, AppConstants.PARAMS_USER_ID, GlobalMethods.getUserID(getActivity()), "");
        APIRequestHandler.getInstance().callMsgListAPI(chatReceiveInputEntity, this);
    }

    private void getFriendsApiCall() {
        ChatConnDisInputEntity friendListInputEntityRes = new ChatConnDisInputEntity(AppConstants.API_FRIEND_LIST, AppConstants.PARAMS_USER_ID, GlobalMethods.getUserID(getActivity()));
        APIRequestHandler.getInstance().callFriendsAndUserListAPI(friendListInputEntityRes, this);
    }

    @Override
    public void onRequestSuccess(Object resObj) {
        if (resObj instanceof ChatReceiveResponse) {
            getFriendsApiCall();
            ChatReceiveResponse messageRes = (ChatReceiveResponse) resObj;
            if (messageRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                mMessageArrList = new ArrayList<>();
                mMessageArrList = messageRes.getResult();
                setMessageAdapter();
            } else {
                DialogManager.getInstance().showAlertPopup(getActivity(), messageRes.getMessage());
            }

        } else if (resObj instanceof FriendsListResponse) {

            FriendsListResponse friendsListRes = (FriendsListResponse) resObj;

            if (friendsListRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                mFriendsArrList = friendsListRes.getResult();
                if (mFriendsArrList.size() > 0) {
                    ((HomeScreen) getActivity()).mMsgRightBtnLay.setVisibility(View.VISIBLE);
                    setFriendListAdapter(mFriendsArrList);
                } else {
                    mFriendsArrList = new ArrayList<>();
                    setFriendListAdapter(mFriendsArrList);
                }
            } else {
                DialogManager.getInstance().showAlertPopup(getActivity(), friendsListRes.getMessage());
            }

            ((HomeScreen) getActivity()).mMsgRightBtnLay.setVisibility(mFriendsArrList.size() > 0 ? View.VISIBLE : View.INVISIBLE);

        }
        if (resObj instanceof CommonResponse) {
            CommonResponse deletedMsgRes = (CommonResponse) resObj;
            if (deletedMsgRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                ChatTable.deleteChatMessageList(mMessageArrList.get(mDeletedMessagePos));
                deleteChat(mMessageArrList.get(mDeletedMessagePos));
                mMessageArrList.remove(mDeletedMessagePos);
            } else {
                DialogManager.getInstance().showAlertPopup(getActivity(), deletedMsgRes.getMessage());
            }
        }
    }

    private void deleteChat(ChatReceiveEntity deleteMsgRes) {

        UserDetailsEntity userDetailsRes = GlobalMethods.getUserDetailsRes(getActivity());

        ChatReceiveEntity model = new ChatReceiveEntity();

        boolean isUserBool = userDetailsRes.getUser_id().equals(deleteMsgRes.getUser_id());
        model.setUser_id(userDetailsRes.getUser_id());
        model.setUsername(userDetailsRes.getUsername());
        model.setUserimage(userDetailsRes.getMain_picture());

        model.setFriend_id(isUserBool ? deleteMsgRes.getFriend_id() : deleteMsgRes.getUser_id());
        model.setFriendname(isUserBool ? deleteMsgRes.getFriendname() : deleteMsgRes.getUsername());
        model.setFriendimage(isUserBool ? deleteMsgRes.getFriendimage() : deleteMsgRes.getUserimage());
        model.setMsg_sent_user(deleteMsgRes.getUser_id());

        model.setSubject(deleteMsgRes.getSubject());
        model.setChat_id(deleteMsgRes.getChat_id());
        model.setMessage(deleteMsgRes.getMessage());
        model.setDatetime(deleteMsgRes.getDatetime());


        ChatTable.updateMaxChatIDTable(model);
        setMessageAdapter();
    }

    private void setMessageAdapter() {
        mNoMsgTxt.setVisibility(mMessageArrList.size() > 0 ? View.GONE : View.VISIBLE);
        MessageAdapter messageAdapter = new MessageAdapter(getActivity(), mMessageArrList);
        mMessageMenuList.setAdapter(messageAdapter);
    }

    private void setFriendListAdapter(ArrayList<UserDetailsEntity> friendsList) {
        ChatFriendsAdapter chatFriendsAdapter = new ChatFriendsAdapter(getActivity(), friendsList);
        mFriendsRecyclerList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFriendsRecyclerList.setAdapter(chatFriendsAdapter);
        mFriendsRecyclerList.setNestedScrollingEnabled(false);
        chatFriendsAdapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        AppConstants.CHAT_FRIEND_ID = mMessageArrList.get(i).getFriend_id();
        AppConstants.CHAT_FRIEND_NAME = mMessageArrList.get(i).getFriendname();

        UserDetailsEntity friendDetailsRes = new UserDetailsEntity();
        friendDetailsRes.setUsername(mMessageArrList.get(i).getFriendname());
        friendDetailsRes.setUser_id(mMessageArrList.get(i).getFriend_id());
        friendDetailsRes.setSubject(mMessageArrList.get(i).getSubject());
        friendDetailsRes.setMain_picture(mMessageArrList.get(i).getFriendimage());
        friendDetailsRes.setAddress(mMessageArrList.get(i).getFriend_address());
        friendDetailsRes.setHide_location(mMessageArrList.get(i).getFriend_location());

        AppConstants.OTHER_USER_DETAILS = new Gson().toJson(friendDetailsRes, UserDetailsEntity.class);
        AppConstants.CHAT_FRIEND_FROM_CONNECT=AppConstants.FAILURE_CODE;
        ((HomeScreen) getActivity()).replaceFragment(new ChatFriendFragment(), 1);
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
}
