package com.smaat.spark.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.smaat.spark.R;
import com.smaat.spark.adapter.ChatAdapter;
import com.smaat.spark.database.ChatTable;
import com.smaat.spark.entity.inputEntity.ChatSendReceiveInputEntity;
import com.smaat.spark.entity.outputEntity.ChatReceiveEntity;
import com.smaat.spark.entity.outputEntity.UserDetailsEntity;
import com.smaat.spark.main.BaseFragment;
import com.smaat.spark.model.ChatReceiveResponse;
import com.smaat.spark.services.APIRequestHandler;
import com.smaat.spark.ui.HomeScreen;
import com.smaat.spark.utils.AppConstants;
import com.smaat.spark.utils.DialogManager;
import com.smaat.spark.utils.GlobalMethods;
import com.smaat.spark.utils.InterfaceBtnCallback;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ChatFriendFragment extends BaseFragment implements InterfaceBtnCallback {


    @BindView(R.id.chat_list)
    RecyclerView mChatRecyclerView;

    @BindView(R.id.chat_edt)
    EditText mChatEdt;

    private Handler mChatHandler;
    private boolean isCheckDBDataBool = false, isRunnableCallBool = false, isFragFocusBool = false, isSendPressedBool = false;
    private UserDetailsEntity mUserDetailsRes;
    private ChatAdapter mChatListAdapter;
    private ArrayList<ChatReceiveEntity> mChatReceiveList;

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
        mChatReceiveList = new ArrayList<>();
        AppConstants.CHAT_ID = AppConstants.FAILURE_CODE;
        AppConstants.CHAT_SCREEN_TITLE = getString(R.string.chat_with) + " " + AppConstants.CHAT_FRIEND_NAME;
    }


    private void initView() {
        mUserDetailsRes = GlobalMethods.getUserDetailsRes(getActivity());
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


    @Override
    public void onPause() {
        super.onPause();
        if (mChatHandler != null) {
            mChatHandler.removeCallbacks(chatReceiveAPICall);
        }
        isFragFocusBool = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        isFragFocusBool = true;
        initView();
        AppConstants.CHAT_SUB = AppConstants.CHAT_SUB_FRIEND;

        ((HomeScreen) getActivity()).setDrawerAction(false);
        ((HomeScreen) getActivity()).setHeadRigImgVisible(false, R.mipmap.app_icon);

        if (!isCheckDBDataBool) {
            getDataFromDB(mUserDetailsRes.getUser_id(), AppConstants.CHAT_FRIEND_ID);
        } else if (mChatHandler != null) {
            isRunnableCallBool = true;
            mChatHandler.removeCallbacks(chatReceiveAPICall);
            mChatHandler.postDelayed(chatReceiveAPICall, 0);
        }

        ((HomeScreen) getActivity()).setHeaderLay(1);
        setHeaderTxt();


    }

    private void setHeaderTxt() {
        try {
            ((HomeScreen) getActivity()).setHeaderText(AppConstants.CHAT_SCREEN_TITLE);
        } catch (Exception e) {
            Log.d(AppConstants.TAG, e.toString());
        }
    }

    private Runnable chatReceiveAPICall = new Runnable() {
        @Override
        public void run() {

            ChatSendReceiveInputEntity chatReceiveInputEntity = new ChatSendReceiveInputEntity(AppConstants.API_CHAT_RECEIVE, AppConstants.PARAMS_CHAT_RECEIVE, mUserDetailsRes.getUser_id(), AppConstants.CHAT_FRIEND_ID, AppConstants.CHAT_SUB, AppConstants.CHAT_ID, "");
            APIRequestHandler.getInstance().callReceiveAPI(chatReceiveInputEntity, ChatFriendFragment.this);
            mChatHandler.postDelayed(this, 1000);


        }
    };


    private void setChatListAdapter(ArrayList<ChatReceiveEntity> chatList) {
        if (chatList.size() > 0) {
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
    public void onRequestSuccess(Object resObj) {

        if (isFragFocusBool && resObj instanceof ChatReceiveResponse) {
            //Receive Chat API Res
            ChatReceiveResponse chatChatReceiveRes = (ChatReceiveResponse) resObj;
            if (chatChatReceiveRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                if (chatChatReceiveRes.getResult().size() > 0 && !(AppConstants.CHAT_ID.equals(chatChatReceiveRes.getResult().get(chatChatReceiveRes.getResult().size() - 1).getChat_id()))) {
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

                ChatTable.updateChatTable(model);
                ChatTable.updateMaxChatIDTable(model);
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
    public void onRequestFailure(Throwable t) {
        if (isSendPressedBool) {
            isSendPressedBool = false;
        }
    }

    @Override
    public void onYesClick() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isFragFocusBool = false;
        if (mChatHandler != null) {
            mChatHandler.removeCallbacks(chatReceiveAPICall);
        }
    }


}
