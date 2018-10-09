package com.smaat.spark.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.smaat.spark.R;
import com.smaat.spark.adapter.ChatAdapter;
import com.smaat.spark.database.ChatTable;
import com.smaat.spark.entity.inputEntity.ChatConnDisInputEntity;
import com.smaat.spark.entity.inputEntity.ChatSendReceiveInputEntity;
import com.smaat.spark.entity.outputEntity.ChatReceiveEntity;
import com.smaat.spark.entity.outputEntity.UserDetailsEntity;
import com.smaat.spark.main.BaseFragment;
import com.smaat.spark.model.ChatConnectResponse;
import com.smaat.spark.model.ChatReceiveResponse;
import com.smaat.spark.model.CommonResponse;
import com.smaat.spark.services.APIRequestHandler;
import com.smaat.spark.ui.HomeScreen;
import com.smaat.spark.utils.AppConstants;
import com.smaat.spark.utils.DialogManager;
import com.smaat.spark.utils.GlobalMethods;
import com.smaat.spark.utils.InterfaceBtnCallback;
import com.smaat.spark.utils.InterfaceTwoBtnCallback;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ChatConnectFragment extends BaseFragment implements InterfaceBtnCallback {


    @BindView(R.id.chat_list)
    RecyclerView mChatRecyclerView;

    @BindView(R.id.chat_edt)
    EditText mChatEdt;

    @BindView(R.id.send_btn)
    Button mSendBtn;

    private Handler mConnectHandler, mChatHandler;
    private int mConnectAPICountInt = 1;
    private boolean isConnectedBool = false, isBackPressedBool = false, isSendPressedBool = false,
            isAddAPIPressedBool = false, isNextPressedBool = false, isCheckDBDataBool = false, isRunnableCallBool = false, isFragFocusBool = false;
    private UserDetailsEntity mUserDetailsRes;
    private ChatAdapter mChatListAdapter;
    private ArrayList<ChatReceiveEntity> mChatReceiveList;
    private UserDetailsEntity mFriendDetailsRes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_chat_screen, container, false);
        ButterKnife.bind(this, rootView);
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

        mChatHandler = new Handler();
        mConnectHandler = new Handler();
        mChatReceiveList = new ArrayList<>();
        AppConstants.CHAT_ID = AppConstants.FAILURE_CODE;
        AppConstants.CHAT_SCREEN_TITLE = getString(R.string.search_user);
    }


    private void initView() {
        mUserDetailsRes = GlobalMethods.getUserDetailsRes(getActivity());

        ((HomeScreen) getActivity()).mChatHeaderTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnectedBool && !isNextPressedBool) {
                    AppConstants.OTHER_USER_DETAILS = new Gson().toJson(mFriendDetailsRes, UserDetailsEntity.class);
                    ((HomeScreen) getActivity()).addFragment(new UserDetailsFragment());
                }

            }
        });
        ((HomeScreen) getActivity()).mHeaderTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnectedBool && !isNextPressedBool) {
                    AppConstants.OTHER_USER_DETAILS = new Gson().toJson(mFriendDetailsRes, UserDetailsEntity.class);
                    ((HomeScreen) getActivity()).addFragment(new UserDetailsFragment());
                }
            }
        });
        ((HomeScreen) getActivity()).mHeaderLeftBtnLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNextPressedBool = false;
                backPressed();
            }
        });
        ((HomeScreen) getActivity()).mHeaderEndLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNextPressedBool = false;
                backPressed();
            }
        });

        ((HomeScreen) getActivity()).mHeaderNextLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnectedBool) {
                    isNextPressedBool = true;
                    backPressed();
                }
            }
        });
        ((HomeScreen) getActivity()).mHeaderInviteLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnectedBool && !isNextPressedBool) {
                    isAddAPIPressedBool = true;
                    ChatSendReceiveInputEntity friendListInputEntityRes = new ChatSendReceiveInputEntity(AppConstants.API_ADD_FRIEND, AppConstants.PARAMS_ADD_FRIEND, GlobalMethods.getUserID(getActivity()), AppConstants.CHAT_FRIEND_ID);
                    APIRequestHandler.getInstance().callAddFriendAPI(friendListInputEntityRes, ChatConnectFragment.this);
                }
            }
        });
    }

    @OnClick({R.id.send_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_btn:
                if (GlobalMethods.isNetworkAvailable(getActivity())) {
                    String chatStr = mChatEdt.getText().toString().trim();
                    if (!chatStr.isEmpty()) {
                        isSendPressedBool = true;
                        ChatSendReceiveInputEntity chatInputEntity = new ChatSendReceiveInputEntity(AppConstants.API_CHAT_SEND, AppConstants.PARAMS_CHAT_SEND, mUserDetailsRes.getUser_id(), AppConstants.CHAT_FRIEND_ID,
                                GlobalMethods.setMsgEncoder(chatStr), AppConstants.CHAT_SUB);
                        APIRequestHandler.getInstance().callSendAPI(chatInputEntity, this);

                        ArrayList<ChatReceiveEntity> chatSendRes = new ArrayList<>();
                        ChatReceiveEntity chatEntitySendRes = new ChatReceiveEntity();

                        chatEntitySendRes.setFriend_id(AppConstants.CHAT_FRIEND_ID);
                        chatEntitySendRes.setUser_id(mUserDetailsRes.getUser_id());
                        chatEntitySendRes.setSubject(AppConstants.CHAT_SUB);
                        chatEntitySendRes.setChat_id(getString(R.string.nag_val));
                        chatEntitySendRes.setMessage(chatStr);
                        chatEntitySendRes.setFriendname(AppConstants.CHAT_FRIEND_NAME);
                        chatEntitySendRes.setUsername(mUserDetailsRes.getUsername());
                        chatEntitySendRes.setUserimage("");
                        chatEntitySendRes.setFriendimage("");
                        chatEntitySendRes.setDatetime(GlobalMethods.getCurrentDate());
                        chatEntitySendRes.setMsg_sent_user(AppConstants.SUCCESS_CODE);

                        chatSendRes.add(chatEntitySendRes);
                        setDataToDB(chatSendRes);
                        mChatEdt.setText("");

                    }
                } else {
                    DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.no_internet));
                }
                break;

        }
    }

    private void setBtnBackground() {
        if (!isBackPressedBool) {
            if (!isConnectedBool) {
                mChatEdt.setHint(getString(R.string.cont_send_msg));
                mChatEdt.setFocusable(false);
                mChatEdt.setFocusableInTouchMode(false);
                mChatEdt.setClickable(false);
                mSendBtn.setClickable(false);
                mSendBtn.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.rounded_grey_bg));

            } else {
                mChatEdt.setHint(getString(R.string.type_here));
                mChatEdt.setFocusable(true);
                mChatEdt.setFocusableInTouchMode(true);
                mChatEdt.setClickable(true);
                mSendBtn.setClickable(true);
                mSendBtn.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.sky_blue_btn));
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isFragFocusBool = false;
        if (mChatHandler != null) {
            mChatHandler.removeCallbacks(chatReceiveAPICall);
        }
        if (mConnectHandler != null) {
            mConnectHandler.removeCallbacks(connectAPICall);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        isFragFocusBool = true;
        initView();
        ((HomeScreen) getActivity()).setDrawerAction(false);
        ((HomeScreen) getActivity()).setHeadRigImgVisible(false, R.mipmap.app_icon);


        if (isConnectedBool && !isBackPressedBool) {

            if (AppConstants.CHAT_SUB.equals(AppConstants.CHAT_SUB_FRIEND) && !isCheckDBDataBool) {
                getDataFromDB(mUserDetailsRes.getUser_id(), AppConstants.CHAT_FRIEND_ID);
            } else if (mChatHandler != null) {
                isRunnableCallBool = true;
                mChatHandler.removeCallbacks(chatReceiveAPICall);
                mChatHandler.postDelayed(chatReceiveAPICall, 0);

            }
        } else if (!isConnectedBool) {
            setBtnBackground();
            if (mConnectHandler != null) {
                mConnectHandler.removeCallbacks(connectAPICall);
                if (mConnectAPICountInt == 7) {
                    if (mConnectHandler != null) {
                        mConnectHandler.removeCallbacks(connectAPICall);
                    }
                    mBaseFragDialog = DialogManager.getInstance().showOptionPopup(getActivity(), getString(R.string.no_users_avail), getString(R.string.try_again), getString(R.string.go_back), new InterfaceTwoBtnCallback() {
                        @Override
                        public void onYesClick() {
                            mConnectAPICountInt = 1;
                            mConnectHandler.postDelayed(connectAPICall, 0);
                        }

                        @Override
                        public void onNoClick() {
                            backPressed();
                        }
                    });
                } else {
                    mConnectHandler.postDelayed(connectAPICall, 0);
                }
            }
        }

        ((HomeScreen) getActivity()).setHeaderLay(2);
        ((HomeScreen) getActivity()).mHeaderEndLay.setVisibility(View.VISIBLE);
        setHeaderTxt();
        ((HomeScreen) getActivity()).mNextTxt.setText(getString(R.string.next));
        ((HomeScreen) getActivity()).mNextTxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.screen_bg));

    }

    private Runnable connectAPICall = new Runnable() {
        @Override
        public void run() {

            if (!isConnectedBool && !isBackPressedBool) {
                String randomStr = "";
                if (mConnectAPICountInt < 7) {
                    mConnectAPICountInt++;
                    AppConstants.CHAT_SCREEN_TITLE = getString(R.string.search_user);
                    randomStr = AppConstants.FAILURE_CODE;
                } else if (mConnectAPICountInt == 7) {
                    AppConstants.CHAT_SCREEN_TITLE = getString(R.string.search_random_user);
                    randomStr = AppConstants.SUCCESS_CODE;
                }
                if (isNextPressedBool) {
                    mConnectAPICountInt = 1;
                    randomStr = AppConstants.SUCCESS_CODE;
                }
                if (mConnectAPICountInt <= 7) {
                    ChatConnDisInputEntity chatConnDisInputEntityRes = new ChatConnDisInputEntity(AppConstants.API_CHAT_CONNECT, AppConstants.PARAMS_CHAT_CONNECT, mUserDetailsRes.getUser_id(),
                            AppConstants.FAILURE_CODE, AppConstants.CHAT_SUB, AppConstants.CHAT_DISTANCE, mUserDetailsRes.getLat(), mUserDetailsRes.getLon(), randomStr, AppConstants.FAILURE_CODE);
                    APIRequestHandler.getInstance().callConnectAPI(chatConnDisInputEntityRes, ChatConnectFragment.this);

                }
            }
            setHeaderTxt();

            mChatHandler.postDelayed(this, 3000);
        }
    };

    private void setHeaderTxt() {
        try {
            ((HomeScreen) getActivity()).mChatHeaderTxt.setText(AppConstants.CHAT_SCREEN_TITLE);
            ((HomeScreen) getActivity()).setHeaderText(AppConstants.CHAT_SCREEN_TITLE);
        } catch (Exception e) {
            Log.d(AppConstants.TAG, e.toString());
        }
    }

    private Runnable chatReceiveAPICall = new Runnable() {
        @Override
        public void run() {
            if (isConnectedBool && !isBackPressedBool) {
                ChatSendReceiveInputEntity chatReceiveInputEntity = new ChatSendReceiveInputEntity(AppConstants.API_CHAT_RECEIVE, AppConstants.PARAMS_CHAT_RECEIVE, mUserDetailsRes.getUser_id(), AppConstants.CHAT_FRIEND_ID, AppConstants.CHAT_SUB, AppConstants.CHAT_ID, "");
                APIRequestHandler.getInstance().callReceiveAPI(chatReceiveInputEntity, ChatConnectFragment.this);
                mChatHandler.postDelayed(this, 1000);
            }

        }
    };


    private void setChatListAdapter(ArrayList<ChatReceiveEntity> chatList) {
        if (chatList.size() > 0 && !isBackPressedBool) {
            if (mChatListAdapter == null) {
                mChatReceiveList.addAll(chatList);
                mChatListAdapter = new ChatAdapter(getActivity(), mChatReceiveList);
                mChatRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mChatRecyclerView.setAdapter(mChatListAdapter);
                mChatRecyclerView.smoothScrollToPosition(mChatReceiveList.size() - 1);
            } else {
                mChatReceiveList.addAll(chatList);
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        mChatListAdapter.notifyDataSetChanged();

                    }
                });
            }
            LinearLayoutManager layoutManager = ((LinearLayoutManager) mChatRecyclerView.getLayoutManager());
            int firstVisiblePosition = layoutManager.findLastVisibleItemPosition();

            View v = layoutManager.getChildAt(0);
            if (firstVisiblePosition > 0 && v != null) {
                int offsetBottom = v.getBottom();
                if (firstVisiblePosition + 1 >= 0 && mChatListAdapter.getItemCount() > 0) {
                    layoutManager.scrollToPositionWithOffset(firstVisiblePosition + 1, offsetBottom);
                } else {
                    mChatRecyclerView.smoothScrollToPosition(offsetBottom);
                }
            }
        }
    }


    @Override
    public void onYesClick() {

    }

    private void backPressed() {
        isBackPressedBool = true;
        hideSoftKeyboard();
        ChatConnDisInputEntity chatDisConInputEntityRes = new ChatConnDisInputEntity(AppConstants.API_CHAT_DISCONNECT, AppConstants.PARAMS_USER_ID, mUserDetailsRes.getUser_id(), AppConstants.FAILURE_CODE);
        APIRequestHandler.getInstance().callDisConnectAPI(chatDisConInputEntityRes, this);
    }

    @Override
    public void onRequestSuccess(Object resObj) {
        if (isFragFocusBool) {
            if (resObj instanceof ChatConnectResponse) {
                ChatConnectResponse chatConnDisRes = (ChatConnectResponse) resObj;

                if (chatConnDisRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                    baseAlertDismiss();
                    if (chatConnDisRes.getResult().size() > 0) {
                        isConnectedBool = true;
                        isNextPressedBool = false;
                        UserDetailsEntity chatConnectRes = chatConnDisRes.getResult().get(0);
                        mFriendDetailsRes = chatConnectRes;
                        AppConstants.CHAT_SUB = chatConnectRes.getSubject();
                        AppConstants.CHAT_FRIEND_ID = chatConnectRes.getUser_id();
                        AppConstants.CHAT_SCREEN_TITLE = getString(R.string.chat_with) + " " + chatConnectRes.getUsername();
                        setBtnBackground();
                        sysOut("chatConnectRes.getFriend()" + chatConnectRes.getFriend());
                        AppConstants.CHAT_SUB = chatConnectRes.getFriend().equals(getString(R.string.three)) ? AppConstants.CHAT_SUB_FRIEND : AppConstants.CHAT_SUB;

                        ((HomeScreen) getActivity()).mHeaderInviteLay.setVisibility(chatConnectRes.getFriend().equals(getString(R.string.three)) ? View.VISIBLE : View.GONE);
//                    ((HomeScreen) getActivity()).setHeaderLay(AppConstants.CHAT_IS_FRIEND.equals(AppConstants.SUCCESS_CODE) ? 1 : 2);
                        ((HomeScreen) getActivity()).mHeaderInviteLay.setVisibility(chatConnectRes.getFriend().equalsIgnoreCase(AppConstants.FAILURE_CODE) ? View.VISIBLE : View.GONE);


                        if (AppConstants.CHAT_SUB.equals(AppConstants.CHAT_SUB_FRIEND) && !isCheckDBDataBool) {
                            getDataFromDB(mUserDetailsRes.getUser_id(), AppConstants.CHAT_FRIEND_ID);
                        } else if (mChatHandler != null) {
                            isRunnableCallBool = true;
                            mChatHandler.removeCallbacks(chatReceiveAPICall);
                            mChatHandler.postDelayed(chatReceiveAPICall, 0);
                        }

                    } else {
                        if (mConnectAPICountInt == 7) {
                            if (mConnectHandler != null) {
                                mConnectHandler.removeCallbacks(connectAPICall);
                            }
                            mBaseFragDialog = DialogManager.getInstance().showOptionPopup(getActivity(), getString(R.string.no_users_avail), getString(R.string.try_again), getString(R.string.go_back), new InterfaceTwoBtnCallback() {
                                @Override
                                public void onYesClick() {
                                    mConnectAPICountInt = 1;
                                    mConnectHandler.postDelayed(connectAPICall, 0);
                                }

                                @Override
                                public void onNoClick() {
                                    backPressed();
                                }
                            });
                        }

                    }

                } else {
                    if (mConnectAPICountInt == 7) {
                        if (mConnectHandler != null) {
                            mConnectHandler.removeCallbacks(connectAPICall);
                        }
                        mBaseFragDialog = DialogManager.getInstance().showOptionPopup(getActivity(), getString(R.string.no_users_avail), getString(R.string.yes), getString(R.string.no), new InterfaceTwoBtnCallback() {
                            @Override
                            public void onYesClick() {
                                mConnectAPICountInt = 1;
                                mConnectHandler.postDelayed(connectAPICall, 0);
                            }

                            @Override
                            public void onNoClick() {
                                backPressed();
                            }
                        });
                    }
                }
            }
            if (resObj instanceof ChatReceiveResponse) {
                //Receive Chat API Res
                ChatReceiveResponse chatChatReceiveRes = (ChatReceiveResponse) resObj;
                if (chatChatReceiveRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                    if (chatChatReceiveRes.getExtra_response().getStatus().equalsIgnoreCase(AppConstants.FAILURE_CODE) || (chatChatReceiveRes.getExtra_response().getStatus().equalsIgnoreCase(AppConstants.SUCCESS_CODE) && chatChatReceiveRes.getExtra_response().getConnected_user_id().equals(AppConstants.CHAT_FRIEND_ID))) {
                        //Chat button disable
                        isBackPressedBool = true;
                        hideSoftKeyboard();
                        mChatEdt.setText("");
                        AppConstants.CHAT_SCREEN_TITLE = getString(R.string.user_disconnect);

                        mChatEdt.setHint(getString(R.string.cont_send_msg));
                        mChatEdt.setFocusable(false);
                        mChatEdt.setFocusableInTouchMode(false);
                        mChatEdt.setClickable(false);
                        mSendBtn.setClickable(false);
                        ((HomeScreen) getActivity()).mHeaderInviteLay.setVisibility(View.GONE);
                        mSendBtn.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.rounded_grey_bg));

                    } else if (chatChatReceiveRes.getResult().size() > 0 && !(AppConstants.CHAT_ID.equals(chatChatReceiveRes.getResult().get(chatChatReceiveRes.getResult().size() - 1).getChat_id()))) {
                        isSendPressedBool = false;
                        AppConstants.CHAT_ID = chatChatReceiveRes.getResult().get(chatChatReceiveRes.getResult().size() - 1).getChat_id();

                        setDataToDB(chatChatReceiveRes.getResult());

                    }
                    if (!chatChatReceiveRes.getExtra_response().getFriend().equals(getActivity().getResources().getString(R.string.three)) && AppConstants.CHAT_SUB.equals(AppConstants.CHAT_SUB_FRIEND)) {

                        DialogManager.getInstance().showAlertPopup(getActivity(), getActivity().getResources().getString(R.string.user_del_msg));
                        getActivity().onBackPressed();

                    }

                }

            }
            if (resObj instanceof CommonResponse) {
                CommonResponse chatDisConnectRes = (CommonResponse) resObj;
                if (chatDisConnectRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                    if (isAddAPIPressedBool) {
                        isAddAPIPressedBool = false;
                        DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.friend_req_sent));

                    } else {
                        if (isNextPressedBool) {
                            isConnectedBool = false;
                            isSendPressedBool = false;
                            isBackPressedBool = false;
                            isAddAPIPressedBool = false;
                            isCheckDBDataBool = false;
                            isRunnableCallBool = false;
                            AppConstants.CHAT_ID = AppConstants.FAILURE_CODE;
                            AppConstants.CHAT_SCREEN_TITLE = getString(R.string.search_user);
                        } else {
                            AppConstants.CHAT_BACK_PRESSED = AppConstants.FAILURE_CODE;
                            getActivity().onBackPressed();
                        }
                    }
                } else {
                   DialogManager.getInstance().showAlertPopup(getActivity(), chatDisConnectRes.getMessage());
                }
            }
            setHeaderTxt();
        }
    }

    @Override
    public void onRequestFailure(Throwable t) {
        if (isAddAPIPressedBool) {
            isAddAPIPressedBool = false;
        }
        baseAlertDismiss();
        if (!isConnectedBool) {
            if (mConnectAPICountInt == 7) {

                if (mConnectHandler != null) {
                    mConnectHandler.removeCallbacks(connectAPICall);
                }
                mBaseFragDialog = DialogManager.getInstance().showOptionPopup(getActivity(), getString(R.string.no_users_avail), getString(R.string.try_again), getString(R.string.go_back), new InterfaceTwoBtnCallback() {
                    @Override
                    public void onYesClick() {
                        mConnectAPICountInt = 1;
                        mConnectHandler.postDelayed(connectAPICall, 0);
                    }

                    @Override
                    public void onNoClick() {
                        backPressed();
                    }
                });
            }
        } else if (isSendPressedBool) {
            isSendPressedBool = false;
        }


    }

    private void setDataToDB(ArrayList<ChatReceiveEntity> chatModel) {

        if (chatModel.size() > 0) {

            UserDetailsEntity userDetailsRes = GlobalMethods.getUserDetailsRes(getActivity());
            ArrayList<ChatReceiveEntity> chatArrList = new ArrayList<>();

            for (int i = 0; i < chatModel.size(); i++) {
                ChatReceiveEntity model = new ChatReceiveEntity();
                boolean isUserBool = userDetailsRes.getUser_id().equals(chatModel.get(i).getUser_id());

                model.setUser_id(userDetailsRes.getUser_id());
                model.setUsername(userDetailsRes.getUsername());
                model.setUserimage(userDetailsRes.getMain_picture());

                model.setFriend_id(isUserBool ? chatModel.get(i).getFriend_id() : chatModel.get(i).getUser_id());
                model.setFriendname(isUserBool ? chatModel.get(i).getFriendname() : chatModel.get(i).getUsername());
                model.setFriendimage(isUserBool ? chatModel.get(i).getFriendimage() : chatModel.get(i).getUserimage());
//                model.setMsg_sent_user(chatModel.get(i).getMsg_sent_user().equals(AppConstants.SUCCESS_CODE) ? chatModel.get(i).getUser_id() : chatModel.get(i).getFriend_id());
                model.setMsg_sent_user(chatModel.get(i).getUser_id());

                model.setSubject(chatModel.get(i).getSubject());
                model.setChat_id(chatModel.get(i).getChat_id());
                model.setMessage(chatModel.get(i).getMessage());
                model.setDatetime(chatModel.get(i).getDatetime());

                sysOut("setMsg_sent_user " + model.getMsg_sent_user());
                sysOut("setMsg " + model.getMessage());
                sysOut("User ID " + model.getUser_id());
                sysOut("Friend ID " + model.getFriend_id());

                if (AppConstants.CHAT_SUB.equals(AppConstants.CHAT_SUB_FRIEND)) {
                    ChatTable.updateChatTable(model);
                    ChatTable.updateMaxChatIDTable(model);
                }
                chatArrList.add(model);
            }

            setChatListAdapter(chatArrList);
        }

    }

    private void getDataFromDB(String userIdStr, String friendIdStr) {

        isCheckDBDataBool = true;
        ChatReceiveEntity model = new ChatReceiveEntity();
        model.setUser_id(userIdStr);
        model.setFriend_id(friendIdStr);
        model.setSubject(AppConstants.CHAT_SUB);

        ArrayList<ChatReceiveEntity> mMsgList = ChatTable.getChatMessageList(model);

        String chatIDStr = ChatTable.getChatMaxID(model);

        sysOut("chatIDStr  " + chatIDStr);
        if (!chatIDStr.isEmpty() && Integer.valueOf(chatIDStr) > 0) {
            AppConstants.CHAT_ID = chatIDStr;
        } else {
            AppConstants.CHAT_ID = AppConstants.FAILURE_CODE;
        }
        setChatListAdapter(mMsgList);

        if (!isRunnableCallBool && mChatHandler != null) {
            isRunnableCallBool = true;
            mChatHandler.removeCallbacks(chatReceiveAPICall);
            mChatHandler.postDelayed(chatReceiveAPICall, 0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isFragFocusBool = false;
        if (mChatHandler != null) {
            mChatHandler.removeCallbacks(chatReceiveAPICall);
        }
        if (mConnectHandler != null) {
            mConnectHandler.removeCallbacks(connectAPICall);
        }
    }
}
