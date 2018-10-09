package com.smaat.spark.ui;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.smaat.spark.R;
import com.smaat.spark.entity.inputEntity.ChatConnDisInputEntity;
import com.smaat.spark.entity.inputEntity.SettingsInputEntity;
import com.smaat.spark.entity.outputEntity.BackgroundEntity;
import com.smaat.spark.entity.outputEntity.UserDetailsEntity;
import com.smaat.spark.fragment.ChatConnectFragment;
import com.smaat.spark.fragment.ChatFriendFragment;
import com.smaat.spark.fragment.DiscoverFragment;
import com.smaat.spark.fragment.FriendsFragment;
import com.smaat.spark.fragment.HomeFragment;
import com.smaat.spark.fragment.InviteFriendsFragment;
import com.smaat.spark.fragment.MessagesFragment;
import com.smaat.spark.fragment.NotificationFragment;
import com.smaat.spark.fragment.ResetPwdFragment;
import com.smaat.spark.fragment.SettingsFragment;
import com.smaat.spark.fragment.UpdateProfileFragment;
import com.smaat.spark.fragment.UserDetailsFragment;
import com.smaat.spark.main.BaseActivity;
import com.smaat.spark.main.BaseFragment;
import com.smaat.spark.model.BackgroundResponse;
import com.smaat.spark.model.CommonResponse;
import com.smaat.spark.model.UserDetailsResponse;
import com.smaat.spark.services.APICommonInterface;
import com.smaat.spark.services.APIRequestHandler;
import com.smaat.spark.utils.AppConstants;
import com.smaat.spark.utils.DialogManager;
import com.smaat.spark.utils.GlobalMethods;
import com.smaat.spark.utils.InterfaceTwoBtnCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeScreen extends BaseActivity {


    @BindView(R.id.coordinator_lay)
    CoordinatorLayout mCoordinatorLay;

    @BindView(R.id.connect_lay)
    RelativeLayout mConnectLay;

    @BindView(R.id.discover_lay)
    RelativeLayout mDiscoverLay;

    @BindView(R.id.messages_lay)
    RelativeLayout mMessagesLay;

    @BindView(R.id.profile_lay)
    RelativeLayout mProfileLay;

    @BindView(R.id.header_left_btn_lay)
    public RelativeLayout mHeaderLeftBtnLay;

    @BindView(R.id.header_right_btn_lay)
    public RelativeLayout mHeaderRightBtnLay;

    @BindView(R.id.header_visible_lay)
    public RelativeLayout mHeaderVisibleLay;

    @BindView(R.id.header_left_img)
    ImageView mHeaderLeftImg;

    @BindView(R.id.header_right_img)
    ImageView mHeaderRightImg;

    @BindView(R.id.header_txt)
    public TextView mHeaderTxt;


    @BindView(R.id.notification_count_txt)
    TextView mNotifyCountTxt;

    @BindView(R.id.chat_header_lay)
    RelativeLayout mChatHeaderLay;

    @BindView(R.id.msg_header_lay)
    RelativeLayout mMsgHeaderLay;

    @BindView(R.id.genera_lay)
    RelativeLayout mGeneraHeaderLay;

    @BindView(R.id.header_end_lay)
    public RelativeLayout mHeaderEndLay;

    @BindView(R.id.msg_right_btn_lay)
    public RelativeLayout mMsgRightBtnLay;

    @BindView(R.id.chat_header_txt)
    public TextView mChatHeaderTxt;

    @BindView(R.id.header_invite_lay)
    public RelativeLayout mHeaderInviteLay;

    @BindView(R.id.header_next_lay)
    public RelativeLayout mHeaderNextLay;

    @BindView(R.id.friend_add_img)
    public ImageView mFriendAddImg;

    @BindView(R.id.next_txt)
    public TextView mNextTxt;

    private Handler notifyHandler = new Handler();
    APICommonInterface mServiceInterface;


    //Footer Img
    @BindView(R.id.connect_img)
    ImageView mConnectImg;

    @BindView(R.id.discover_img)
    ImageView mDiscoverImg;

    @BindView(R.id.messages_img)
    ImageView mMessagesImg;

    @BindView(R.id.msg_count_txt)
    TextView mMsgCountTxt;

    @BindView(R.id.connect_count_txt)
    TextView mConnectCountTxt;

    @BindView(R.id.update_profile_img)
    ImageView mUpdateProfileImg;

    @BindView(R.id.bottom_footer_lay)
    RelativeLayout mBottomFooterLay;

    private BaseFragment mCurrentFragment;
    private List<BaseFragment> mConnectFragmentList, mDiscoverFragmentList, mMessagesFragmentList, mProfileFragmentList;
    private String mNotificationCountStr = AppConstants.FAILURE_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_home_screen);
        ButterKnife.bind(this);
        setupUI(mCoordinatorLay);
        initView();
    }


    private void initView() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).build();
        mServiceInterface = retrofit.create(APICommonInterface.class);

        mHeaderRightImg.setImageResource(R.drawable.blue_back_img);
        mHeaderLeftBtnLay.setVisibility(View.VISIBLE);
        mHeaderRightBtnLay.setVisibility(View.INVISIBLE);

        setHeaderLeftClick(true);

        mConnectFragmentList = new ArrayList<>();
        mDiscoverFragmentList = new ArrayList<>();
        mMessagesFragmentList = new ArrayList<>();
        mProfileFragmentList = new ArrayList<>();

        setHeaderLay(1);
        setFooterImg(0);

        replaceFragment(new HomeFragment(), 1);
        mDiscoverFragmentList.add(new DiscoverFragment());
        mMessagesFragmentList.add(new MessagesFragment());
        mProfileFragmentList.add(new UpdateProfileFragment());

        if (GlobalMethods.getStringValue(this, AppConstants.CHAT_CONNECTED_STATUS).equals(getString(R.string.two))) {
            replaceFragment(new ChatConnectFragment(), 1);
        }

        checkNotifyData();

        UserDetailsEntity userDetailsEntity = GlobalMethods.getUserDetailsRes(this);
        sysOut("User ID ---" + GlobalMethods.getUserID(this));
        sysOut("User Name ---" + userDetailsEntity.getUsername());


    }


    private void checkNotifyData() {
        Bundle notificationBundle = getIntent().getExtras();
        if (notificationBundle != null) {

            JSONObject json;
            String typeNotificationStr = AppConstants.FAILURE_CODE, senderIdStr = AppConstants.FAILURE_CODE, senderNameStr = "", senderImagesStr = "", senderAddressStr = "", senderInterestStr = "", senderLocStr = "", notifyResStr = notificationBundle.getString(AppConstants.NOTIFICATION_DATA);

            try {
                json = new JSONObject(notifyResStr);
                typeNotificationStr = json.getString("type_of_notification");
                senderIdStr = json.getString("notification_from");
                senderNameStr = json.getString("sender_name");
                senderImagesStr = json.getString("sender_image");
                senderInterestStr = json.getString("sender_interests");
                senderAddressStr = json.getString("sender_address");
                senderLocStr = json.getString("sender_location status");

            } catch (JSONException e) {
                Log.d(AppConstants.TAG, e.toString());
            }

            if (typeNotificationStr.equals(AppConstants.SUCCESS_CODE) || typeNotificationStr.equals(getString(R.string.two)) || typeNotificationStr.equals(getString(R.string.three))) {

                setFooterImg(0);
                setHeaderLeftClick(false);
                mHeaderRightBtnLay.setVisibility(View.INVISIBLE);
                replaceFragment(new NotificationFragment(), 1);
            } else if (typeNotificationStr.equals(getString(R.string.four))) {
                setFooterImg(2);
                AppConstants.CHAT_FRIEND_ID = senderIdStr;
                AppConstants.CHAT_FRIEND_NAME = senderNameStr;


                UserDetailsEntity friendDetailsRes = new UserDetailsEntity();
                friendDetailsRes.setUsername(senderNameStr);
                friendDetailsRes.setUser_id(senderIdStr);
                friendDetailsRes.setSubject(AppConstants.CHAT_SUB_FRIEND);
                friendDetailsRes.setMain_picture(senderImagesStr);
                friendDetailsRes.setInterests(senderInterestStr);
                friendDetailsRes.setAddress(senderAddressStr);
                friendDetailsRes.setHide_location(senderLocStr);

                AppConstants.OTHER_USER_DETAILS = new Gson().toJson(friendDetailsRes, UserDetailsEntity.class);

                AppConstants.CHAT_FRIEND_FROM_CONNECT = AppConstants.FAILURE_CODE;
                replaceFragment(new ChatFriendFragment(), 1);
            }
        }
    }


    @OnClick({R.id.connect_lay, R.id.discover_lay,
            R.id.messages_lay, R.id.profile_lay, R.id.msg_left_btn_lay, R.id.invite_right_btn_lay})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.connect_lay:
                if (AppConstants.BOTTOM_BUTTON_POS != 0) {
                    checkNotificationFragmentStack(AppConstants.BOTTOM_BUTTON_POS);
                    setFooterImg(0);
                    replaceFragment(mConnectFragmentList.get(mConnectFragmentList.size() - 1), 0);
                } else if (mConnectFragmentList.size() > 1 && !(mConnectFragmentList.get(mConnectFragmentList.size() - 1) instanceof ChatConnectFragment)) {
                    resetFragmentStack(AppConstants.BOTTOM_BUTTON_POS);
                    replaceFragment(mConnectFragmentList.get(mConnectFragmentList.size() - 1), 0);
                }
                break;

            case R.id.discover_lay:
                if (AppConstants.BOTTOM_BUTTON_POS != 1) {
                    checkNotificationFragmentStack(AppConstants.BOTTOM_BUTTON_POS);
                    setFooterImg(1);
                    replaceFragment(mDiscoverFragmentList.get(mDiscoverFragmentList.size() - 1), 0);
                } else if (mDiscoverFragmentList.size() > 1) {
                    resetFragmentStack(AppConstants.BOTTOM_BUTTON_POS);
                    replaceFragment(mDiscoverFragmentList.get(mDiscoverFragmentList.size() - 1), 0);
                }
                break;

            case R.id.messages_lay:
                if (AppConstants.BOTTOM_BUTTON_POS != 2) {
                    checkNotificationFragmentStack(AppConstants.BOTTOM_BUTTON_POS);
                    setFooterImg(2);
                    replaceFragment(mMessagesFragmentList.get(mMessagesFragmentList.size() - 1), 0);
                } else if (mMessagesFragmentList.size() > 1) {
                    resetFragmentStack(AppConstants.BOTTOM_BUTTON_POS);
                    replaceFragment(mMessagesFragmentList.get(mMessagesFragmentList.size() - 1), 0);
                }
                break;

            case R.id.profile_lay:
                checkNotificationFragmentStack(AppConstants.BOTTOM_BUTTON_POS);

                if (AppConstants.BOTTOM_BUTTON_POS != 3) {
                    setFooterImg(3);
                    replaceFragment(mProfileFragmentList.get(mProfileFragmentList.size() - 1), 0);
                } else if (mProfileFragmentList.size() > 1) {
                    resetFragmentStack(AppConstants.BOTTOM_BUTTON_POS);
                    replaceFragment(mProfileFragmentList.get(mProfileFragmentList.size() - 1), 0);
                }
                break;

            case R.id.msg_left_btn_lay:
                setHeaderLeftClick(false);
                replaceFragment(new FriendsFragment(), 1);
                break;
            case R.id.invite_right_btn_lay:
                if (!(mCurrentFragment instanceof InviteFriendsFragment)) {
                    replaceFragment(new InviteFriendsFragment(), 1);
                }
                break;
        }

    }

    public void setFooterImg(int pos) {
        AppConstants.BOTTOM_BUTTON_POS = pos;
        mConnectImg.setImageResource(pos == 0 ? R.drawable.connect_enable_btn : R.drawable.connect_disable_btn);
        mDiscoverImg.setImageResource(pos == 1 ? R.drawable.discover_enable_btn : R.drawable.discover_disable_btn);
        mMessagesImg.setImageResource(pos == 2 ? R.drawable.chat_enable_btn : R.drawable.chat_disable_btn);
        mUpdateProfileImg.setImageResource(pos == 3 ? R.drawable.profile_enable_btn : R.drawable.profile_disable_btn);

    }

    public void setHeaderText(String headerStr) {
        mHeaderTxt.setText(headerStr);
    }

    public void setHeadRigImgVisible(boolean isVisible, int imgName) {
        mHeaderRightBtnLay.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
        if (isVisible) {
            mHeaderRightImg.setImageResource(imgName);
        }

    }

    public void setHeaderLay(int visiblePos) {
        mGeneraHeaderLay.setVisibility(visiblePos == 1 ? View.VISIBLE : View.GONE);
        mChatHeaderLay.setVisibility(visiblePos == 2 ? View.VISIBLE : View.GONE);
        mMsgHeaderLay.setVisibility(visiblePos == 3 ? View.VISIBLE : View.GONE);
    }

    public void setFooterLay(boolean isVisibleBool) {
        mBottomFooterLay.setVisibility(isVisibleBool ? View.VISIBLE : View.GONE);
    }

    private void callNotificationAPI() {
        ChatConnDisInputEntity notificationInputEntityRes = new ChatConnDisInputEntity(AppConstants.API_BACKGROUND, AppConstants.PARAMS_USER_ID, GlobalMethods.getUserID(this));

        mServiceInterface.getBackgroundAPICall(notificationInputEntityRes).enqueue(new Callback<BackgroundResponse>() {
            @Override
            public void onResponse(Call<BackgroundResponse> call, Response<BackgroundResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().getResponse_code().equals(AppConstants.SUCCESS_CODE)) {
                        if (response.body().getResult().size() > 0) {
                            try {
                                BackgroundEntity backgroundRes = response.body().getResult().get(0);

                                mNotificationCountStr = backgroundRes.getUnread_notifications_count();
//                                String blockCountStr = backgroundRes.getBlocks_count();
                                String unReadFriendMsgCountStr = backgroundRes.getUnread_messages();
                                String unReadConnectMsgCountStr = backgroundRes.getConnect_unread_messages();
                                String connectChatSubStr = backgroundRes.getSubject();
                                checkNotifyCount();
//                                if (Integer.valueOf(blockCountStr) > 9) {
//                                    DialogManager.showToast(HomeScreen.this, getString(R.string.block_msg));
//                                    GlobalMethods.storeUserDetails(HomeScreen.this, null);
//                                    previousScreen(LoginScreen.class, true);
//                                }
                                if (!backgroundRes.getEmail_id().isEmpty()) {
                                    UserDetailsEntity userDetailsRes = GlobalMethods.getUserDetailsRes(HomeScreen.this);
                                    userDetailsRes.setEmail_id(backgroundRes.getEmail_id());
                                    GlobalMethods.storeUserDetails(HomeScreen.this, userDetailsRes);
                                }
                                mMsgCountTxt.setText(Integer.valueOf(unReadFriendMsgCountStr) > 0 ? unReadFriendMsgCountStr : "");
                                mMsgCountTxt.setVisibility(Integer.valueOf(unReadFriendMsgCountStr) > 0 ? View.VISIBLE : View.GONE);

                                if (mCurrentFragment instanceof ChatConnectFragment) {
                                    mConnectCountTxt.setVisibility(View.GONE);
                                } else {
                                    mConnectCountTxt.setText(Integer.valueOf(unReadConnectMsgCountStr) > 0 ? unReadConnectMsgCountStr : "");
                                    mConnectCountTxt.setVisibility(Integer.valueOf(unReadConnectMsgCountStr) > 0 ? View.VISIBLE : View.GONE);
                                }

                                if (GlobalMethods.getStringValue(HomeScreen.this, AppConstants.CHAT_CONNECTED_STATUS).equals(AppConstants.SUCCESS_CODE) && !connectChatSubStr.isEmpty()) {
                                    connectChatSubStr = connectChatSubStr.replaceAll(",", " ");
                                }

                                if (AppConstants.BOTTOM_BUTTON_POS == 0 && !backgroundRes.getConnect_friend_id().equals(AppConstants.FAILURE_CODE) && !(mCurrentFragment instanceof ChatConnectFragment) && !GlobalMethods.getStringValue(HomeScreen.this, AppConstants.CHAT_CONNECTED_STATUS).equals(getString(R.string.two)) && !connectChatSubStr.isEmpty()) {
                                    GlobalMethods.storeStringValue(HomeScreen.this, AppConstants.CHAT_CONNECT_SUB, GlobalMethods.setMsgEncoder(connectChatSubStr));
                                    GlobalMethods.storeStringValue(HomeScreen.this, AppConstants.CHAT_CONNECT_ORIGINAL_SUB, GlobalMethods.setMsgEncoder(connectChatSubStr));
                                    GlobalMethods.storeStringValue(HomeScreen.this, AppConstants.CHAT_CONNECT_FRIEND_ID, backgroundRes.getConnect_friend_id());
                                    GlobalMethods.storeStringValue(HomeScreen.this, AppConstants.CHAT_CONNECT_FRIEND_NAME, "");

                                    resetFragmentStack(0);
                                    setFooterImg(0);
                                    replaceFragment(new ChatConnectFragment(), 1);
                                } else if (AppConstants.BOTTOM_BUTTON_POS == 0 && GlobalMethods.getStringValue(HomeScreen.this, AppConstants.CHAT_CONNECTED_STATUS).equals(AppConstants.SUCCESS_CODE) && !(mCurrentFragment instanceof ChatConnectFragment) && !GlobalMethods.getStringValue(HomeScreen.this, AppConstants.CHAT_CONNECTED_STATUS).equals(getString(R.string.two))) {

                                    GlobalMethods.storeStringValue(HomeScreen.this, AppConstants.CHAT_CONNECT_FRIEND_ID, AppConstants.FAILURE_CODE);
                                    GlobalMethods.storeStringValue(HomeScreen.this, AppConstants.CHAT_CONNECT_FRIEND_NAME, AppConstants.FAILURE_CODE);

                                    resetFragmentStack(0);
                                    setFooterImg(0);
                                    replaceFragment(new ChatConnectFragment(), 1);
                                }


                            } catch (Exception e) {
                                Log.d(AppConstants.TAG, e.toString());
                            }
                        }
                    } else {
                        if (response.body().getMessage().equals(getString(R.string.user_not_valid))) {

                            try {
                                DialogManager.showToast(HomeScreen.this, getString(R.string.acc_del_msg));
                                GlobalMethods.storeUserDetails(HomeScreen.this, null);
                                previousScreen(LoginScreen.class, true);

                            } catch (Exception e) {
                                Log.d(AppConstants.TAG, e.toString());
                            }
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<BackgroundResponse> call, Throwable t) {
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (notifyHandler != null) {
            notifyHandler.removeCallbacks(notifyCheckingService);
            notifyHandler.postDelayed(notifyCheckingService, 0);
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (notifyHandler != null) {
            notifyHandler.removeCallbacks(notifyCheckingService);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (notifyHandler != null) {
            notifyHandler.removeCallbacks(notifyCheckingService);
        }
    }

    private Runnable notifyCheckingService = new Runnable() {
        @Override
        public void run() {

            if (GlobalMethods.isLoggedIn(HomeScreen.this)) {
                callNotificationAPI();
                notifyHandler.postDelayed(this, 1000);
            }
        }
    };

    //    public void setHeaderLeftClick(boolean isMenuScreen) {
//        setHeaderLeftBtnLayClick(isMenuScreen);
//        setHeaderLeftImg(isMenuScreen ? R.drawable.notification_btn : R.drawable.blue_back_img);
//    }
    public void setHeaderLeftClick(boolean isMenuScreen) {

        if (isMenuScreen) {
            mHeaderLeftImg.setImageResource(R.drawable.notification_btn);
            mHeaderLeftBtnLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setHeaderLeftClick(false);
                    replaceFragment(new NotificationFragment(), 1);
                }
            });
        } else {
            mHeaderLeftImg.setImageResource(R.drawable.blue_back_img);
            mHeaderLeftBtnLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }


    public void replaceFragment(BaseFragment fragment, int showInt) {
        //showInt is 0 means just show given frag, showInt is 1 means add and showInt is 2 means remove
        if (fragment != null) {
            if (AppConstants.BOTTOM_BUTTON_POS == 0) {
                if (showInt == 1) {
                    mConnectFragmentList.add(fragment);
                } else if (showInt == 2) {
                    mConnectFragmentList.remove(mConnectFragmentList.size() - 1);
                }
                mCurrentFragment = mConnectFragmentList.get(mConnectFragmentList.size() - 1);

            } else if (AppConstants.BOTTOM_BUTTON_POS == 1) {

                if (showInt == 1) {
                    mDiscoverFragmentList.add(fragment);
                } else if (showInt == 2) {
                    mDiscoverFragmentList.remove(mDiscoverFragmentList.size() - 1);
                }
                mCurrentFragment = mDiscoverFragmentList.get(mDiscoverFragmentList.size() - 1);

            } else if (AppConstants.BOTTOM_BUTTON_POS == 2) {
                if (showInt == 1) {
                    mMessagesFragmentList.add(fragment);
                } else if (showInt == 2) {
                    mMessagesFragmentList.remove(mMessagesFragmentList.size() - 1);
                }
                mCurrentFragment = mMessagesFragmentList.get(mMessagesFragmentList.size() - 1);
            } else {
                if (showInt == 1) {
                    mProfileFragmentList.add(fragment);
                } else if (showInt == 2) {
                    mProfileFragmentList.remove(mProfileFragmentList.size() - 1);
                }
                mCurrentFragment = mProfileFragmentList.get(mProfileFragmentList.size() - 1);
            }
            checkFrag(mCurrentFragment);
            checkNotifyCount();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame_lay, mCurrentFragment, mCurrentFragment.getClass().getSimpleName())
                    .addToBackStack(null)
                    .commit();


        } else {
            Log.e(AppConstants.TAG, getResources().getString(R.string.err_create_frag));
        }
    }


    public void resetFragmentStack(int BottomPosInt) {
        if (BottomPosInt == 0) {
            mConnectFragmentList.clear();
            mConnectFragmentList.add(new HomeFragment());
        } else if (BottomPosInt == 1) {
            mDiscoverFragmentList.clear();
            mDiscoverFragmentList.add(new DiscoverFragment());
        } else if (BottomPosInt == 2) {
            mMessagesFragmentList.clear();
            mMessagesFragmentList.add(new MessagesFragment());
        } else {
            mProfileFragmentList.clear();
            mProfileFragmentList.add(new UpdateProfileFragment());
        }
    }

    public void checkNotificationFragmentStack(int BottomPosInt) {
        if (BottomPosInt == 0 && mConnectFragmentList.size() > 1 && mConnectFragmentList.get(mConnectFragmentList.size() - 1) instanceof NotificationFragment) {
            mConnectFragmentList.remove(mConnectFragmentList.size() - 1);
        } else if (BottomPosInt == 1 && mDiscoverFragmentList.size() > 1 && mDiscoverFragmentList.get(mDiscoverFragmentList.size() - 1) instanceof NotificationFragment) {
            mDiscoverFragmentList.remove(mDiscoverFragmentList.size() - 1);
        } else if (BottomPosInt == 2 && mMessagesFragmentList.size() > 1 && mMessagesFragmentList.get(mMessagesFragmentList.size() - 1) instanceof NotificationFragment) {
            mMessagesFragmentList.remove(mMessagesFragmentList.size() - 1);
        } else if (BottomPosInt == 3 && mProfileFragmentList.size() > 1 && mProfileFragmentList.get(mProfileFragmentList.size() - 1) instanceof NotificationFragment) {
            mProfileFragmentList.remove(mProfileFragmentList.size() - 1);
        }
    }

    private void checkFrag(Fragment fragName) {
        setHeaderLay(fragName instanceof ChatConnectFragment ? 2 : 1);
        boolean isFooterBool = true;
        if (fragName instanceof UserDetailsFragment || fragName instanceof SettingsFragment || fragName instanceof ResetPwdFragment) {
            isFooterBool = false;
        }
        setFooterLay(isFooterBool);
        AppConstants.CHAT_BACK_PRESSED = (fragName instanceof ChatConnectFragment) ? AppConstants.SUCCESS_CODE : AppConstants.FAILURE_CODE;
        mHeaderLeftBtnLay.setVisibility(fragName instanceof UpdateProfileFragment ? View.INVISIBLE : View.VISIBLE);


        if (fragName instanceof ChatConnectFragment) {
            mHeaderEndLay.setVisibility(View.VISIBLE);
            mChatHeaderTxt.setText("");
            mNextTxt.setText(getString(R.string.next));
            mNextTxt.setTextColor(ContextCompat.getColor(HomeScreen.this, R.color.screen_bg));
        } else if (fragName instanceof MessagesFragment) {

            mHeaderEndLay.setVisibility(View.INVISIBLE);
            mChatHeaderTxt.setText(getString(R.string.new_message));
            mNextTxt.setText(getString(R.string.cancel));
            mNextTxt.setTextColor(ContextCompat.getColor(HomeScreen.this, R.color.red));
        }

        if (!(fragName instanceof ChatConnectFragment) && !(fragName instanceof ChatFriendFragment)) {
            mHeaderTxt.setOnClickListener(null);
            mChatHeaderTxt.setOnClickListener(null);
        }
        hideSoftKeyboard(HomeScreen.this);

    }


    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof UserDetailsResponse) {
            UserDetailsResponse userRes = (UserDetailsResponse) resObj;
            if (userRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                UserDetailsEntity userDetailsRes = GlobalMethods.getUserDetailsRes(HomeScreen.this);
                userDetailsRes.setAnonymous(userRes.getResult().get(0).getAnonymous());
                userDetailsRes.setPush_notifications(userRes.getResult().get(0).getPush_notifications());
                userDetailsRes.setHide_location(userRes.getResult().get(0).getHide_location());
                GlobalMethods.storeUserDetails(this, userDetailsRes);
                AppConstants.SETTINGS_BACK = AppConstants.FAILURE_CODE;
                onBackPressed();

            } else {
                DialogManager.getInstance().showAlertPopup(this, userRes.getMessage());
            }
        } else if (resObj instanceof CommonResponse) {
            CommonResponse chatDisConnectRes = (CommonResponse) resObj;
            if (chatDisConnectRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                GlobalMethods.storeStringValue(this, AppConstants.CHAT_CONNECTED_STATUS, AppConstants.FAILURE_CODE);
                mHeaderInviteLay.setVisibility(View.GONE);

                if (AppConstants.CHAT_BACK_PRESSED.equals(AppConstants.SUCCESS_CODE)) {

//                    ChatReceiveEntity deleteEntityRes = new ChatReceiveEntity();
//                    deleteEntityRes.setUser_id(GlobalMethods.getUserID(this));
//                    deleteEntityRes.setFriend_id(GlobalMethods.getStringValue(this, AppConstants.CHAT_CONNECT_FRIEND_ID));
//                    deleteEntityRes.setSubject(AppConstants.CHAT_SUB_FRIEND);
//                    ChatTable.deleteChatMessageList(deleteEntityRes);

                    if (AppConstants.CHAT_DISCOVER.equals(AppConstants.SUCCESS_CODE)) {
                        mHeaderInviteLay.setVisibility(View.GONE);
                        resetFragmentStack(0);
                        setFooterImg(1);
                        AppConstants.CHAT_DISCOVER = AppConstants.FAILURE_CODE;
                        replaceFragment(new DiscoverFragment(), 0);

                    } else {
                        backButtonClick();
                    }

//                } else if (AppConstants.CHAT_BACK_PRESSED.equals(getString(R.string.two))) {
//                    mConnectFragmentList.remove(mConnectFragmentList.size() - 1);
//                    AppConstants.CHAT_FRIEND_ID = GlobalMethods.getStringValue(this, AppConstants.CHAT_CONNECT_FRIEND_ID);
//                    AppConstants.CHAT_FRIEND_NAME = GlobalMethods.getStringValue(this, AppConstants.CHAT_CONNECT_FRIEND_NAME);
//
//
//                    resetFragmentStack(AppConstants.BOTTOM_BUTTON_POS);
//                    setFooterImg(2);
//                    resetFragmentStack(AppConstants.BOTTOM_BUTTON_POS);
//                    AppConstants.CHAT_FRIEND_FROM_CONNECT=AppConstants.SUCCESS_CODE;
//                    replaceFragment(new ChatFriendFragment(), 1);
//
//
                }
                AppConstants.CHAT_BACK_PRESSED = AppConstants.FAILURE_CODE;

            } else {
                DialogManager.getInstance().showAlertPopup(this, chatDisConnectRes.getMessage());
            }
        }
    }

    private void checkNotifyCount() {
        if ((mCurrentFragment instanceof HomeFragment) || (mCurrentFragment instanceof DiscoverFragment) || (mCurrentFragment instanceof UpdateProfileFragment)) {
            mNotifyCountTxt.setText(Integer.valueOf(mNotificationCountStr) > 0 ? mNotificationCountStr : "");
            mNotifyCountTxt.setVisibility(Integer.valueOf(mNotificationCountStr) > 0 ? View.VISIBLE : View.GONE);

        } else {
            mNotifyCountTxt.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        hideSoftKeyboard(HomeScreen.this);

        if (AppConstants.SETTINGS_BACK.equals(AppConstants.SUCCESS_CODE)) {
            callSettingsUpdateAPI();
        } else if (AppConstants.CHAT_BACK_PRESSED.equals(AppConstants.SUCCESS_CODE) || AppConstants.CHAT_BACK_PRESSED.equals(getString(R.string.two))) {
            callChatDisConnectAPI();

            if (AppConstants.CHAT_BACK_PRESSED.equals(getString(R.string.two))) {
                mConnectFragmentList.remove(mConnectFragmentList.size() - 1);
                AppConstants.CHAT_FRIEND_ID = GlobalMethods.getStringValue(this, AppConstants.CHAT_CONNECT_FRIEND_ID);
                AppConstants.CHAT_FRIEND_NAME = GlobalMethods.getStringValue(this, AppConstants.CHAT_CONNECT_FRIEND_NAME);
                resetFragmentStack(AppConstants.BOTTOM_BUTTON_POS);
                setFooterImg(2);
                resetFragmentStack(AppConstants.BOTTOM_BUTTON_POS);
                AppConstants.CHAT_FRIEND_FROM_CONNECT = AppConstants.SUCCESS_CODE;
                replaceFragment(new ChatFriendFragment(), 1);
            }
        } else {
            backButtonClick();
        }
    }

    private void callSettingsUpdateAPI() {
        SettingsInputEntity settingsInputEntity = new SettingsInputEntity(AppConstants.API_UPDATE_SETTINGS, AppConstants.PARAMS_UPDATE_SETTINGS, GlobalMethods.getUserID(this), AppConstants.FAILURE_CODE, AppConstants.SETTINGS_PUSH, AppConstants.SETTINGS_HIDE_LOC, AppConstants.SETTINGS_ANONYMOUS);
        APIRequestHandler.getInstance().callSettingsAPI(settingsInputEntity, this);
    }

    private void callChatDisConnectAPI() {
        ChatConnDisInputEntity chatDisConInputEntityRes = new ChatConnDisInputEntity(AppConstants.API_CHAT_DISCONNECT, AppConstants.PARAMS_USER_ID, GlobalMethods.getUserID(this), AppConstants.FAILURE_CODE);
        APIRequestHandler.getInstance().callDisConnectAPI(chatDisConInputEntityRes, this);
    }

    public void backButtonClick() {
        if (AppConstants.BOTTOM_BUTTON_POS == 0) {
            if (mConnectFragmentList.size() > 1) {
                replaceFragment(mConnectFragmentList.get(mConnectFragmentList.size() - 1), 2);
            } else {
                exitFromApp();
            }

        } else if (AppConstants.BOTTOM_BUTTON_POS == 1) {
            if (mDiscoverFragmentList.size() > 1) {
                replaceFragment(mDiscoverFragmentList.get(mDiscoverFragmentList.size() - 1), 2);
            } else {
                exitFromApp();
            }

        } else if (AppConstants.BOTTOM_BUTTON_POS == 2) {
            if (mMessagesFragmentList.size() > 1) {
                replaceFragment(mMessagesFragmentList.get(mMessagesFragmentList.size() - 1), 2);
            } else {
                exitFromApp();
            }
        } else {
            if (mProfileFragmentList.size() > 1) {
                replaceFragment(mProfileFragmentList.get(mProfileFragmentList.size() - 1), 2);
            } else {
                exitFromApp();
            }

        }

    }

    private void exitFromApp() {
        DialogManager.getInstance().showOptionPopup(this, getString(R.string.exit_msg), getString(R.string.yes), getString(R.string.no), new InterfaceTwoBtnCallback() {
            @Override
            public void onYesClick() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask();
                } else {
                    finish();
                }
            }

            public void onNoClick() {

            }
        });
    }

}
