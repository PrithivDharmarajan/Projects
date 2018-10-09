package com.fautus.fautusapp.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.fautus.fautusapp.R;
import com.fautus.fautusapp.adapter.ChatAdapter;
import com.fautus.fautusapp.entity.ChatEntity;
import com.fautus.fautusapp.entity.MomentPhotoEntity;
import com.fautus.fautusapp.entity.PhotoEntity;
import com.fautus.fautusapp.main.BaseFragment;
import com.fautus.fautusapp.ui.HomeScreen;
import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.DialogManager;
import com.fautus.fautusapp.utils.InterfaceBtnCallback;
import com.fautus.fautusapp.utils.InterfaceTwoBtnCallback;
import com.fautus.fautusapp.utils.NetworkUtil;
import com.fautus.fautusapp.utils.ParseAPIConstants;
import com.fautus.fautusapp.utils.PreferenceUtil;
import com.google.firebase.iid.FirebaseInstanceId;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sendbird.android.BaseChannel;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.PreviousMessageListQuery;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.sendbird.android.UserMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.fautus.fautusapp.R.string.your_cus_loc;
import static com.fautus.fautusapp.R.string.your_pho_loc;
import static com.fautus.fautusapp.utils.AppConstants.MOMENT_PHOTO_ENTITY;
import static com.fautus.fautusapp.utils.ParseAPIConstants.user;


/**
 * This class implements UI and functions for Chat
 *
 * @author Smaat Apps
 * @version 1.0
 * @since 2017-04-20
 */

public class ChatFragment extends BaseFragment implements InterfaceBtnCallback {

    @BindView(R.id.chat_recycler_view)
    RecyclerView mChatRecyclerView;

    @BindView(R.id.chat_edt)
    EditText mChatEdt;

    private List<String> mChatUserList;
    private GroupChannel mGroupChannel;
    private String mSenderIdStr = "", mReceiverIdStr = "";
    private ChatAdapter mChatAdapter;
    private ArrayList<ChatEntity> mChatTotalMsgList = new ArrayList<>();
    private ArrayList<ChatEntity> mChatAdapterMsgList = new ArrayList<>();
    private boolean isCurrentFocus = false;
    private Timer mMomentCancelledTimer, mMsgTypingTimer;
    private boolean isMomentCancelled = false;
    private Dialog mPhotographerDialog, mMomentCloseDialog, mMomentCanCelDialog;
    private boolean mIsTypingBool = false, mCalledOldMsgBool = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_chat_screen, container, false);
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);

        /*To focus on current fragment*/
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return rootView;
    }


    /*Frag manual onResume*/
    @Override
    public void onFragmentResume() {
        super.onFragmentResume();
        /*set header text and header images*/
        ((HomeScreen) getActivity()).setHeaderText(PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.USER_IS_CONSUMER) ? getResources().getString(R.string.your_moment) : getString(R.string.moment) + " " + getResources().getString(R.string.colon_sym) + " " + AppConstants.CHAT_MOMENT_DETAILS.getString(ParseAPIConstants.adHocCode));
        ((HomeScreen) getActivity()).setHeadLeftImg(PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.USER_IS_CONSUMER) ? R.drawable.refresh_img : R.drawable.menu_cls_img);
        ((HomeScreen) getActivity()).setHeadRightImgVisible(true, R.drawable.con_moment_img);
        isCurrentFocus = true;
        isMomentCancelled = false;
        mCalledOldMsgBool = false;

        initView();


    }


    private void initView() {
        ((HomeScreen) getActivity()).cancelNotification();

        if (getActivity() != null) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
        /*If the value of AppConstants.SHOW_CON_PHOTO_DIALOG is one , photographer's details in a dialog box will be visible*/
        if (AppConstants.SHOW_CON_PHOTO_DIALOG.equals(AppConstants.SUCCESS_CODE)) {
            AppConstants.SHOW_CON_PHOTO_DIALOG = AppConstants.FAILURE_CODE;

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mPhotographerDialog = DialogManager.getInstance().showPhotographerConnectedAlertPopup(getActivity(), AppConstants.CHAT_PHOTOGRAPHER_DETAILS);
                }
            });
        }
        /*Header left click */
        ((HomeScreen) getActivity()).mHeaderLeftLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFragmentBackPressed();


            }
        });
        /*Header right click */
        ((HomeScreen) getActivity()).mHeaderRightLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isMomentCancelled) {
                /* If the user is consumer, the following process will happen */
                    if (PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.USER_IS_CONSUMER)) {

                        AppConstants.HEADER_RIGHT_CLICK = AppConstants.FAILURE_CODE;
                        ((HomeScreen) getActivity()).addFragment(new ConsMomentCheckFragment());
                    } else {
                    /* if the user is photographer, then the following process will happen */
                        AppConstants.MOMENT_PHOTO_ENTITY = new MomentPhotoEntity();
                        AppConstants.MOMENT_PHOTO_ENTITY.setMoment(AppConstants.CHAT_MOMENT_DETAILS);
                    /*Set flag for right side image visible and it control from home screen - checkAndSetScreenStatus*/
                        AppConstants.MOMENT_UPLOAD_FROM_CHAT = AppConstants.SUCCESS_CODE;
                        AppConstants.HEADER_RIGHT_CLICK = AppConstants.FAILURE_CODE;
                        ((HomeScreen) getActivity()).addFragment(new MomentUploadFragment());
                    }
                }
            }
        });


        ((HomeScreen) getActivity()).mHeaderRightSecondBtnLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getActivity() != null) {
                    String plcNameStr = PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.USER_IS_CONSUMER) ? getResources().getString(your_pho_loc) : getResources().getString(your_cus_loc);
                    String uri = String.format(Locale.US, "geo:%f,%f?z=%d&q=%f,%f (%s)",
                            AppConstants.USER_MAP_LOC.getLatitude(), AppConstants.USER_MAP_LOC.getLongitude(), 15, AppConstants.USER_MAP_LOC.getLatitude(), AppConstants.USER_MAP_LOC.getLongitude(), plcNameStr);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    getActivity().startActivity(intent);
                }
            }
        });

         /*To Check internet connection*/
        if (AppConstants.SEND_BIRD_GROUP_CHANNEL == null && getActivity() != null && NetworkUtil.isNetworkAvailable(getActivity()) && !isMomentCancelled) {
            DialogManager.getInstance().showProgress(getActivity());
            mSenderIdStr = ParseUser.getCurrentUser().getUsername() != null ? ParseUser.getCurrentUser().getUsername() : getResources().getString(R.string.user_one);
            AppConstants.USER_MAP_LOC = new ParseGeoPoint();
            mChatUserList = new ArrayList<>();
            mChatUserList.add(mSenderIdStr);
                        /* If the user is consumer, the following process will happen */
            if (getActivity() != null && PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.USER_IS_CONSUMER)) {
                AppConstants.CHAT_PHOTOGRAPHER_DETAILS.getParseObject(user).fetchInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {

                        if (e == null && object != null) {
                            if (object.getParseGeoPoint(ParseAPIConstants.currentLocation) != null) {
                                AppConstants.USER_MAP_LOC.setLatitude(object.getParseGeoPoint(ParseAPIConstants.currentLocation).getLatitude());
                                AppConstants.USER_MAP_LOC.setLongitude(object.getParseGeoPoint(ParseAPIConstants.currentLocation).getLongitude());
                            }
                            mReceiverIdStr = object.getString(ParseAPIConstants.username) != null ? object.getString(ParseAPIConstants.username) : getResources().getString(R.string.user_two);

                            mChatUserList.add(mReceiverIdStr);
                            sendBirdSDKInit();
                        } else if (e != null) {
                            DialogManager.getInstance().hideProgress();
                            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), e.getMessage(), ChatFragment.this);
                        }
                    }
                });
            } else if (getActivity() != null) {
                            /*If the user is Photographer, the following process will happen */
                if (AppConstants.CHAT_CONSUMER_DETAILS != null) {
                    AppConstants.CHAT_CONSUMER_DETAILS.fetchInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            if (e == null && object != null) {
                                if (object.getParseGeoPoint(ParseAPIConstants.currentLocation) != null) {
                                    AppConstants.USER_MAP_LOC.setLatitude(object.getParseGeoPoint(ParseAPIConstants.currentLocation).getLatitude());
                                    AppConstants.USER_MAP_LOC.setLongitude(object.getParseGeoPoint(ParseAPIConstants.currentLocation).getLongitude());
                                }
                                AppConstants.CHAT_CONSUMER_DETAILS = object;
                                mReceiverIdStr = AppConstants.CHAT_CONSUMER_DETAILS.getString(ParseAPIConstants.username) != null ? AppConstants.CHAT_CONSUMER_DETAILS.getString(ParseAPIConstants.username) : getResources().getString(R.string.user_two);
                                mChatUserList.add(mReceiverIdStr);
                                sendBirdSDKInit();
                            } else if (e != null) {
                                DialogManager.getInstance().hideProgress();
                                DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), e.getMessage(), ChatFragment.this);
                            }
                        }
                    });
                }
            }
        } else if (AppConstants.SEND_BIRD_GROUP_CHANNEL == null && getActivity() != null) {
                /*Alert message will be appeared if there is no internet connection*/
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), getString(R.string.no_internet), this);

        }

        mChatEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cancelTypingStatusTimer();
                mMsgTypingTimer = new Timer();
                /*Set typing status is true*/
                setTypingStatus(s.length() > 0);
            }


            @Override
            public void afterTextChanged(final Editable s) {
                cancelTypingStatusTimer();
                mMsgTypingTimer = new Timer();
                mMsgTypingTimer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                if (mIsTypingBool) {
                                    /*Set typing status is false*/
                                    setTypingStatus(false);
                                }
                            }
                        }, 1000);

            }
        });
        mChatEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mChatTotalMsgList != null && mChatTotalMsgList.size() > 0 && hasFocus) {
                    mChatRecyclerView.getLayoutManager().smoothScrollToPosition(mChatRecyclerView, null, mChatTotalMsgList.size() - 1);
                }
            }
        });

        mChatRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard();
                return false;
            }
        });

    }


    /* initialization for SendBird chat SDK */
    private void sendBirdSDKInit() {
        SendBird.connect(mSenderIdStr, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                if (getActivity() != null) {
                    if (e == null) {
                    /* Channel Creation */
                        createChatChannel();

                        if (FirebaseInstanceId.getInstance().getToken() == null) {
                            DialogManager.getInstance().showToast(getActivity(), "Token Null");
                        }
                        if (FirebaseInstanceId.getInstance().getToken() == null) return;

                        SendBird.registerPushTokenForCurrentUser(FirebaseInstanceId.getInstance().getToken(),
                                new SendBird.RegisterPushTokenWithStatusHandler() {
                                    @Override
                                    public void onRegistered(SendBird.PushTokenRegistrationStatus status, SendBirdException e) {
                                        if (e != null) {
                                            DialogManager.getInstance().showToast(getActivity(), e.getMessage());
                                        }
                                    }
                                });

//                        SendBird.registerPushTokenForCurrentUser(PreferenceUtil.getStringValue(getActivity(), AppConstants.SEND_BIRD_DEVICE_ID), new SendBird.RegisterPushTokenWithStatusHandler() {
//                            @Override
//                            public void onRegistered(SendBird.PushTokenRegistrationStatus pushTokenRegistrationStatus, SendBirdException e) {
//
//                            }
//                        });

                    } else {
                        DialogManager.getInstance().hideProgress();
                        DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), e.getMessage(), ChatFragment.this);
                    }
                }
            }
        });


    }

    /* Default photographer's message - send manually */
    private void sendDefaultMsg(String senderStr) {
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setMsg(String.format(getResources().getString(R.string.photo_shoot_msg), AppConstants.CHAT_PHOTOGRAPHER_DETAILS.getString(ParseAPIConstants.fullName)));
        chatEntity.setSender(senderStr);
        chatEntity.setUserTyping(false);
        if (mChatTotalMsgList == null)
            mChatTotalMsgList = new ArrayList<>();

        try {
            if (mChatTotalMsgList.size() > 0 && mChatTotalMsgList.get(0).getMsg().equals(String.format(getResources().getString(R.string.photo_shoot_msg), AppConstants.CHAT_PHOTOGRAPHER_DETAILS.getString(ParseAPIConstants.fullName)))) {
                mChatTotalMsgList.remove(0);
            }
        } catch (Exception e) {
            Log.e(AppConstants.TAG, e.toString());
        }

        mChatTotalMsgList.add(chatEntity);

        setChatListAdapter(mChatTotalMsgList);
    }

    /* Channel Creation */
    private void createChatChannel() {

        GroupChannel.createChannelWithUserIds(mChatUserList, true, AppConstants.CHAT_MOMENT_DETAILS.getObjectId(), "", null, new GroupChannel.GroupChannelCreateHandler() {
            @Override
            public void onResult(GroupChannel groupChannel, SendBirdException e) {
                DialogManager.getInstance().hideProgress();
                if (e == null) {
                    AppConstants.SEND_BIRD_GROUP_CHANNEL = groupChannel;
                    mGroupChannel = groupChannel;
                    loadPreviousMessages(200);

                } else {
                    DialogManager.getInstance().hideProgress();
                    DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), e.getMessage(), ChatFragment.this);
                }

            }
        });


    }

    /*View OnClick*/
    @OnClick({R.id.send_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_btn:
                if (mChatEdt.getText().toString().trim().length() > 0) {
                    sendMsg(mChatEdt.getText().toString().trim());
                    mChatEdt.setText("");
                }
                break;
        }
    }

    /* Send message */
    private void sendMsg(final String msgStr) {
        if (mGroupChannel != null) {
            mGroupChannel.sendUserMessage(msgStr, null, new BaseChannel.SendUserMessageHandler() {
                @Override
                public void onSent(UserMessage userMessage, SendBirdException e) {
                    ChatEntity chatEntity = new ChatEntity();
                    chatEntity.setMsg(msgStr);
                    chatEntity.setTimeStamp(userMessage.getCreatedAt());
                    chatEntity.setSender(AppConstants.SUCCESS_CODE);
                    chatEntity.setUserTyping(false);

                    if (mChatTotalMsgList == null)
                        mChatTotalMsgList = new ArrayList<>();

                    mChatTotalMsgList.add(chatEntity);
                    setChatListAdapter(mChatTotalMsgList);
                    mChatRecyclerView.getLayoutManager().smoothScrollToPosition(mChatRecyclerView, null, mChatTotalMsgList.size() - 1);

                }
            });
        }

    }

    /* chat adapter*/
    private void setChatListAdapter(ArrayList<ChatEntity> chatList) {
        if (getActivity() != null) {

            mChatAdapterMsgList.clear();
            mChatAdapterMsgList.addAll(chatList);

            if (mChatAdapter == null) {
                mChatAdapter = new ChatAdapter(getActivity(), mChatAdapterMsgList);
                mChatRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mChatRecyclerView.setAdapter(mChatAdapter);
                mChatRecyclerView.setNestedScrollingEnabled(true);
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        mChatAdapter.notifyDataSetChanged();
                    }
                });
            }
        /* to focus the last message in the list */
            LinearLayoutManager layoutManager = ((LinearLayoutManager) mChatRecyclerView.getLayoutManager());

            if (layoutManager != null) {
                int firstVisiblePosition = layoutManager.findLastVisibleItemPosition();

                boolean isMovBool = firstVisiblePosition == mChatAdapter.getItemCount() - 1 || firstVisiblePosition == mChatAdapter.getItemCount() - 2;

                View v = layoutManager.getChildAt(0);
                if (firstVisiblePosition > 0 && v != null) {
                    if (isMovBool && firstVisiblePosition + 1 >= 0 && mChatAdapter.getItemCount() > 0 && mChatAdapterMsgList.size() > 0 && firstVisiblePosition + 1 <= mChatAdapter.getItemCount()) {
                        mChatRecyclerView.getLayoutManager().smoothScrollToPosition(mChatRecyclerView, null, firstVisiblePosition + 1);
//                        layoutManager.scrollToPositionWithOffset(firstVisiblePosition + 1, offsetBottom);
//                    } else {
//                        mChatRecyclerView.getLayoutManager().smoothScrollToPosition(mChatRecyclerView, null, mChatAdapter.getItemCount() - 1);
                    }
                }
            }
        }
    }

    @Override
    public void onResume() {
        isCurrentFocus = true;
        isMomentCancelled = false;
        mCalledOldMsgBool = false;
        AppConstants.PHOTOGRAPHER_AVA = true;

        cancelCheckMomentCancelledByUserTimer();
        checkMomentCancelledByUser();

        if (AppConstants.SEND_BIRD_GROUP_CHANNEL != null) {
            mGroupChannel = AppConstants.SEND_BIRD_GROUP_CHANNEL;
            loadPreviousMessages(200);
        }

        if (PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.USER_IS_CONSUMER)) {
            checkMomentClosedStatus();
        }

        SendBird.addChannelHandler(AppConstants.CHAT_MOMENT_DETAILS.getObjectId(), new SendBird.ChannelHandler() {
            @Override
            public void onMessageReceived(BaseChannel baseChannel, BaseMessage baseMessage) {

                if (!mCalledOldMsgBool) {
                    DialogManager.getInstance().hideProgress();
                             /* Receive messages from SendBird */
                    ChatEntity chatEntity = new ChatEntity();
                    chatEntity.setMsg(((UserMessage) baseMessage).getMessage());
                    chatEntity.setTimeStamp(baseMessage.getCreatedAt());
                    chatEntity.setSender(AppConstants.FAILURE_CODE);
                    chatEntity.setUserTyping(false);
                    if (mChatTotalMsgList == null)
                        mChatTotalMsgList = new ArrayList<>();

                    mChatTotalMsgList.add(chatEntity);
                    setChatListAdapter(mChatTotalMsgList);
                }

            }

            @Override
            public void onTypingStatusUpdated(GroupChannel groupChannel) {
                            /* When typing status has been updated.*/
                List<User> typingUsers = groupChannel.getTypingMembers();

                if (mChatTotalMsgList.size() > 0) {
                    ChatEntity chatRes = new ChatEntity();
                    if (typingUsers.size() > 0 && !mChatTotalMsgList.get(mChatTotalMsgList.size() - 1).isUserTyping()) {
                        chatRes.setMsg(mChatTotalMsgList.get(mChatTotalMsgList.size() - 1).getMsg());
                        chatRes.setSender(mChatTotalMsgList.get(mChatTotalMsgList.size() - 1).getSender());
                        chatRes.setUserTyping(true);
                        mChatTotalMsgList.set(mChatTotalMsgList.size() - 1, chatRes);
                        setChatListAdapter(mChatTotalMsgList);

                    } else if (typingUsers.size() == 0) {
                        chatRes.setMsg(mChatTotalMsgList.get(mChatTotalMsgList.size() - 1).getMsg());
                        chatRes.setSender(mChatTotalMsgList.get(mChatTotalMsgList.size() - 1).getSender());
                        chatRes.setUserTyping(false);
                        mChatTotalMsgList.set(mChatTotalMsgList.size() - 1, chatRes);
                        setChatListAdapter(mChatTotalMsgList);
                    }

                }
            }
        });

//        SendBird.addConnectionHandler(AppConstants.CHAT_MOMENT_DETAILS.getObjectId(), new SendBird.ConnectionHandler() {
//            @Override
//            public void onReconnectStarted() {
//            }
//
//            @Override
//            public void onReconnectSucceeded() {
//                refresh();
//
//            }
//
//            @Override
//            public void onReconnectFailed() {
//            }
//        });
        ((HomeScreen) getActivity()).cancelNotification();
        super.onResume();
    }

    @Override
    public void onPause() {
        isCurrentFocus = false;
        isMomentCancelled = true;
        setTypingStatus(false);
        cancelCheckMomentCancelledByUserTimer();

//        SendBird.removeChannelHandler(AppConstants.CHAT_MOMENT_DETAILS.getObjectId());
        SendBird.removeAllChannelHandlers();
        super.onPause();

    }

    /*to check the status on Moment, whether it is closed or not*/

    private void checkMomentClosedStatus() {
        if (getActivity() != null && isCurrentFocus) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (getActivity() != null && isCurrentFocus) {
                        ParseQuery<ParseObject> momentQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Moment);
                        momentQuery.whereEqualTo(ParseAPIConstants.objectId, AppConstants.CHAT_MOMENT_DETAILS.getObjectId());
                        momentQuery.whereEqualTo(ParseAPIConstants.closed, true);
                        momentQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(final ParseObject object, ParseException e) {
                                if (e == null && object != null) {
                                    ParseQuery<ParseObject> photoQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Photo);
                                    photoQuery.whereEqualTo(ParseAPIConstants.moment, object);
                                    photoQuery.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> momentPhotosObjList, ParseException e) {
                                        /* Check moment photo counts */
                                            if (e == null && momentPhotosObjList != null && momentPhotosObjList.size() > 0) {
                                                ArrayList<PhotoEntity> ParseFileObj = new ArrayList<>();
                                                int purchasedPhotosCountInt = 0;
                                                for (int j = 0; j < momentPhotosObjList.size(); j++) {
                                                    PhotoEntity photoFile = new PhotoEntity();
                                                    photoFile.setPhotoObj(momentPhotosObjList.get(j));
                                                    photoFile.setPhoto(momentPhotosObjList.get(j).getParseFile(ParseAPIConstants.photo));
                                                    photoFile.setPhoto_purchased(momentPhotosObjList.get(j).getBoolean(ParseAPIConstants.purchased) ? AppConstants.SUCCESS_CODE : AppConstants.FAILURE_CODE);
                                                    if (momentPhotosObjList.get(j).getBoolean(ParseAPIConstants.purchased)) {
                                                        purchasedPhotosCountInt += 1;
                                                    }
                                                    ParseFileObj.add(photoFile);
                                                }
                                            /* Direct to MomentDetailsFragment */
                                                MOMENT_PHOTO_ENTITY = new MomentPhotoEntity();
                                                MOMENT_PHOTO_ENTITY.setPurchasedPhotosCount(String.valueOf(purchasedPhotosCountInt));
                                                AppConstants.MOMENT_ALREADY_BOUGHT = purchasedPhotosCountInt > 0 ? AppConstants.SUCCESS_CODE : AppConstants.FAILURE_CODE;
                                                MOMENT_PHOTO_ENTITY.setMoment(object);
                                                MOMENT_PHOTO_ENTITY.setPhoto(ParseFileObj);
                                                AppConstants.MOMENT_FROM_LIST = AppConstants.FAILURE_CODE;
                                                if (getActivity() != null) {
                                                    baseFragmentAlertDismiss(mPhotographerDialog);
                                                    PreferenceUtil.storeConsumerChatDetails(getActivity(), null);
                                                    ((HomeScreen) getActivity()).addFragment(new MomentDetailsFragment());
                                                }
                                            } else {
                                                checkMomentClosedStatus();
                                            }
                                        }
                                    });
                                } else {
                                    checkMomentClosedStatus();
                                }
                            }
                        });
                    }
                }
            });
        }
    }


    /* Disconnect SendBird Chat SDK */
    private void disconnectChat() {
        if (AppConstants.SEND_BIRD_GROUP_CHANNEL != null) {
            AppConstants.SEND_BIRD_GROUP_CHANNEL.leave(new GroupChannel.GroupChannelLeaveHandler() {
                @Override
                public void onResult(SendBirdException e) {
                    SendBird.disconnect(new SendBird.DisconnectHandler() {
                        @Override
                        public void onDisconnected() {
                            moveToHomeScreen();
                        }
                    });
                }
            });
        } else {
            moveToHomeScreen();
        }
    }

    /*Interface default ok button click*/
    @Override
    public void onOkClick() {

    }


    /*close moment*/
    private void checkAndCloseMoment() {

        if (!isMomentCancelled) {

            DialogManager.getInstance().showProgress(getActivity());
            ParseQuery<ParseObject> momentQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Moment);
            momentQuery.whereEqualTo(ParseAPIConstants.objectId, AppConstants.CHAT_MOMENT_DETAILS.getObjectId());
            momentQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(final ParseObject object, ParseException e) {
                /*Check internet connection*/
                    if (NetworkUtil.isNetworkAvailable(getActivity())) {
                        if (!isMomentCancelled) {
                            ParseQuery<ParseObject> photoQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Photo);
                            photoQuery.whereEqualTo(ParseAPIConstants.moment, object);
                            photoQuery.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> momentPhotosObjList, ParseException e) {

                                    if (!isMomentCancelled) {
                                        if (e == null && momentPhotosObjList != null && momentPhotosObjList.size() > 0) {
                                            object.put(ParseAPIConstants.closed, true);
                                            object.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if (e != null) {
                                                        object.saveEventually();
                                                    }
                                                    disconnectChat();
                                                }
                                            });

                                        } else if (e == null) {
                                            isMomentCancelled = true;
                                            cancelCheckMomentCancelledByUserTimer();

                                            /*If there were no photos uploaded, hide this moment*/
                                            object.put(ParseAPIConstants.enabled, false);
                                            object.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if (e != null) {
                                                        object.saveEventually();
                                                    }
                                                    disconnectChat();
                                                }
                                            });
                                        } else {
                                            disconnectChat();
                                        }
                                    }
                                }
                            });
                        }
                    } else {
                    /*Alert message will be appeared if there is no internet connection*/
                        DialogManager.getInstance().hideProgress();
                        DialogManager.getInstance().showAlertPopup(getContext(), getString(R.string.app_name), getString(R.string.no_internet), ChatFragment.this);
                    }
                }
            });
        }
    }

    private void moveToHomeScreen() {
        if (getActivity() != null) {
            if (PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.USER_IS_CONSUMER)) {
                baseFragmentAlertDismiss(mPhotographerDialog);
                DialogManager.getInstance().hideProgress();
                ((HomeScreen) getActivity()).addFragment(new ConsHomeFragment());

            } else {
                final ParseQuery<ParseObject> photographerQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Photographer);

                photographerQuery.whereEqualTo(user, ParseUser.getCurrentUser());
                photographerQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (getActivity() != null) {
                            if (e == null && object != null) {
                                object.put(ParseAPIConstants.isAvailable, true);
                                object.saveEventually();
                            }
                            DialogManager.getInstance().hideProgress();
                            baseFragmentAlertDismiss(mPhotographerDialog);
                            ((HomeScreen) getActivity()).addFragment(new PhotoHomeFragment());

                        }
                    }
                });
            }
        }
    }


    /* To check if the moment is cancelled or not */
    private void checkMomentCancelledByUser() {

        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cancelCheckMomentCancelledByUserTimer();
                    mMomentCancelledTimer = new Timer();
                    mMomentCancelledTimer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            if (getActivity() != null && NetworkUtil.isNetworkAvailable(getActivity())) {
                                AppConstants.CHAT_MOMENT_DETAILS.fetchInBackground(new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject object, ParseException e) {
                                        if (isMomentCancelled || !isCurrentFocus) {
                                            if (!isCurrentFocus) {
                                                baseFragmentAlertDismiss(mPhotographerDialog);
                                                baseFragmentAlertDismiss(mMomentCloseDialog);
                                                baseFragmentAlertDismiss(mMomentCanCelDialog);
                                            }
                                            cancelCheckMomentCancelledByUserTimer();
                                        } else if (object != null && object.get(ParseAPIConstants.enabled) != null && !object.getBoolean(ParseAPIConstants.enabled) && !isMomentCancelled && isCurrentFocus) {
                                            isMomentCancelled = true;
                                            cancelCheckMomentCancelledByUserTimer();
                                            baseFragmentAlertDismiss(mPhotographerDialog);
                                            baseFragmentAlertDismiss(mMomentCloseDialog);
                                            baseFragmentAlertDismiss(mMomentCanCelDialog);

                                            PreferenceUtil.storeConsumerChatDetails(getActivity(), null);
                                            mMomentCanCelDialog = DialogManager.getInstance().showAlertPopup(getActivity(), getResources().getString(R.string.moment_cancelled_topic), getResources().getString(R.string.moment_cancelled_msg), new InterfaceBtnCallback() {
                                                @Override
                                                public void onOkClick() {

                                                    if (getActivity() != null) {
                                                        DialogManager.getInstance().showProgress(getActivity());
                                                        disconnectChat();
                                                    }
                                                }
                                            });

                                        }

                                    }
                                });
                            } else {
                                baseFragmentAlertDismiss(mMomentCanCelDialog);
                                cancelCheckMomentCancelledByUserTimer();
                            }

                        }
                    }, 3000, 3000);
                }
            });

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isCurrentFocus = false;
        isMomentCancelled = true;
        cancelCheckMomentCancelledByUserTimer();
        baseFragmentAlertDismiss(mMomentCloseDialog);
        baseFragmentAlertDismiss(mMomentCanCelDialog);

    }


    /*Cancelling the check moment cancelled timer */
    private void cancelCheckMomentCancelledByUserTimer() {
        if (mMomentCancelledTimer != null) {
            mMomentCancelledTimer.cancel();
            mMomentCancelledTimer.purge();
        }

    }

    /*Cancelling the typing status timer*/
    private void cancelTypingStatusTimer() {
        if (mMsgTypingTimer != null) {
            mMsgTypingTimer.cancel();
            mMsgTypingTimer.purge();
        }
    }

    @Override
    public void onFragmentBackPressed() {
     /* If the user is consumer, the following process will happen */

        if (getActivity() != null && AppConstants.SEND_BIRD_GROUP_CHANNEL != null) {
            if (PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.USER_IS_CONSUMER)) {
                refresh();
            } else {
                    /*If the user is photographer, then the following process will happen */
                baseFragmentAlertDismiss(mMomentCloseDialog);
                mMomentCloseDialog = DialogManager.getInstance().showOptionAlertPopup(getActivity(), getResources().getString(R.string.close_moment_topic).toUpperCase(Locale.US), getResources().getString(R.string.close_moment_msg), getResources().getString(R.string.cancel).toUpperCase(Locale.US), getResources().getString(R.string.ok), new InterfaceTwoBtnCallback() {
                    @Override
                    public void onYesClick() {

                        if (!isMomentCancelled) {
                            checkAndCloseMoment();
                        }
                    }

                    @Override
                    public void onNoClick() {

                    }
                });
            }
        }
    }

    private void setTypingStatus(boolean typingBool) {
        mIsTypingBool = typingBool;
        if (mGroupChannel == null)
            return;
        if (typingBool)
            mGroupChannel.startTyping();
        else
            mGroupChannel.endTyping();
    }


    private void loadPreviousMessages(int limit) {
        if (mGroupChannel != null) {
            PreviousMessageListQuery prevMessageListQuery = mGroupChannel.createPreviousMessageListQuery();
            prevMessageListQuery.load(limit, false, new PreviousMessageListQuery.MessageListQueryResult() {
                @Override
                public void onResult(List<BaseMessage> messages, SendBirdException e) {
                    if (messages != null && messages.size() > 0 && !mCalledOldMsgBool) {
                        mCalledOldMsgBool = true;
                        mChatTotalMsgList = new ArrayList<>();
                        /*first default message */
                        if (getActivity() != null) {
                            sendDefaultMsg(PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.USER_IS_CONSUMER) ? AppConstants.FAILURE_CODE : AppConstants.SUCCESS_CODE);
                        }
                        if (mSenderIdStr.isEmpty()) {
                            mSenderIdStr = ParseUser.getCurrentUser().getUsername() != null ? ParseUser.getCurrentUser().getUsername() : getResources().getString(R.string.user_one);
                        }

                        for (BaseMessage message : messages) {
                            ChatEntity chatEntity = new ChatEntity();
                            chatEntity.setMsg(((UserMessage) message).getMessage());
                            chatEntity.setTimeStamp(message.getCreatedAt());
                            chatEntity.setSender(((UserMessage) message).getSender().getUserId().equals(mSenderIdStr) ? AppConstants.SUCCESS_CODE : AppConstants.FAILURE_CODE);
                            chatEntity.setUserTyping(false);

                            mChatTotalMsgList.add(chatEntity);
                        }
                        mCalledOldMsgBool = false;
                        setChatListAdapter(mChatTotalMsgList);
                        mChatRecyclerView.getLayoutManager().smoothScrollToPosition(mChatRecyclerView, null, mChatTotalMsgList.size() - 1);
                    } else if (getActivity() != null) {
                        mCalledOldMsgBool = false;
                        sendDefaultMsg(PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.USER_IS_CONSUMER) ? AppConstants.FAILURE_CODE : AppConstants.SUCCESS_CODE);
                    }

                }
            });
        } else {
            mCalledOldMsgBool = false;
        }
    }

    private void refresh() {
        if (AppConstants.SEND_BIRD_GROUP_CHANNEL != null) {

            AppConstants.SEND_BIRD_GROUP_CHANNEL.refresh(new GroupChannel.GroupChannelRefreshHandler() {
                @Override
                public void onResult(SendBirdException e) {
                    if (e != null) {
                        DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), e.getMessage(), ChatFragment.this);
                    }
                }
            });
        }
    }

}
