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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.smaat.spark.fragment.SettingsFragment;
import com.smaat.spark.fragment.UpdateProfileFragment;
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
    LinearLayout mConnectLay;

    @BindView(R.id.discover_lay)
    LinearLayout mDiscoverLay;

    @BindView(R.id.messages_lay)
    LinearLayout mMessagesLay;

    @BindView(R.id.profile_lay)
    LinearLayout mProfileLay;
    @BindView(R.id.header_left_btn_lay)
    public RelativeLayout mHeaderLeftBtnLay;

    @BindView(R.id.header_right_btn_lay)
    public RelativeLayout mHeaderRightBtnLay;

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


    @BindView(R.id.next_txt)
    public TextView mNextTxt;

    private Handler notifyHandler = new Handler();
    APICommonInterface mServiceInterface;
    private boolean mAddFrgBool = false;


    //Footer Img
    @BindView(R.id.connect_img)
    ImageView mConnectImg;

    @BindView(R.id.discover_img)
    ImageView mDiscoverImg;

    @BindView(R.id.messages_img)
    ImageView mMessagesImg;

    @BindView(R.id.msg_count_txt)
    TextView mMsgCountTxt;

    @BindView(R.id.update_profile_img)
    ImageView mUpdateProfileImg;

    @BindView(R.id.bottom_footer_lay)
    RelativeLayout mBottomFooterLay;

    private int mBottomViewInt = 0;

    private BaseFragment mCurrentFragment;

    FragmentManager fragmentManagerConnect, fragmentManagerDiscover, fragmentManagerMessages, fragmentManagerProfile;
    List<BaseFragment> mConnectFragmentList, mDiscoverFragmentList, mMessagesFragmentList, mProfileFragmentList;

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
        setUserInfo();
        getSupportFragmentManager().addOnBackStackChangedListener(getListener());
        setDrawerAction(true);
        AppConstants.HOME_FRAG_POS = AppConstants.FAILURE_CODE;

        mConnectFragmentList = new ArrayList<>();
        mDiscoverFragmentList = new ArrayList<>();
        mMessagesFragmentList = new ArrayList<>();
        mProfileFragmentList = new ArrayList<>();

        mConnectFragmentList.add(new HomeFragment());
        mDiscoverFragmentList.add(new DiscoverFragment());
        mMessagesFragmentList.add(new MessagesFragment());
        mProfileFragmentList.add(new UpdateProfileFragment());
//        addFragment(new HomeFragment());
//        addFragment(new DiscoverFragment());
//        addFragment(new MessagesFragment());
//        addFragment(new UpdateProfileFragment());

        setHeaderLay(1);
        checkNotifyData();



    }


    private void checkNotifyData() {
        Bundle notificationBundle = getIntent().getExtras();
        if (notificationBundle != null) {

            JSONObject json;
            String typeNotificationStr = AppConstants.FAILURE_CODE, senderIdStr = AppConstants.FAILURE_CODE, senderNameStr = "", notifyResStr = notificationBundle.getString(AppConstants.NOTIFICATION_DATA);

            try {
                json = new JSONObject(notifyResStr);
                typeNotificationStr = json.getString("type_of_notification");
                senderIdStr = json.getString("notification_from");
                senderNameStr = json.getString("sender_name");


            } catch (JSONException e) {
                Log.d(AppConstants.TAG, e.toString());
            }

            if (typeNotificationStr.equals(AppConstants.SUCCESS_CODE) || typeNotificationStr.equals(getString(R.string.two)) || typeNotificationStr.equals(getString(R.string.three))) {
                AppConstants.HOME_FRAG_POS = AppConstants.FAILURE_CODE;
                mDrawerLayout.closeDrawer(GravityCompat.START);
                setDrawerAction(false);
                mHeaderRightBtnLay.setVisibility(View.INVISIBLE);
                addFragment(new NotificationFragment());
            } else if (typeNotificationStr.equals(getString(R.string.four))) {

                AppConstants.CHAT_SUB = AppConstants.CHAT_SUB_FRIEND;
                AppConstants.CHAT_FRIEND_ID = senderIdStr;
                AppConstants.CHAT_FRIEND_NAME = senderNameStr;
                addFragment(new ChatFriendFragment());
            }
        }
    }

    public void setUserInfo() {
        UserDetailsEntity userDetailsRes = GlobalMethods.getUserDetailsRes(this);
        sysOut("User Name---" + userDetailsRes.getUsername());
        sysOut("User ID---" + userDetailsRes.getUser_id());
    }

    @OnClick({R.id.connect_lay, R.id.discover_lay,
            R.id.messages_lay, R.id.update_profile_lay,R.id.msg_left_btn_lay, R.id.invite_right_btn_lay, R.id.slide_right_img, R.id.my_profile_txt, R.id.friends_txt, R.id.notification_lay, R.id.settings_txt, R.id.logout_btn})
    public void onClick(View v) {

        DialogManager.showToast(this, "Click");
        switch (v.getId()) {
  case R.id.connect_lay:
                if (mBottomViewInt != 0) {
                    setFooterImg(0);
                    replaceFragment(mConnectFragmentList.get(mConnectFragmentList.size()-1));
//                    replaceFragment(new HomeFragment(), 1);
                }
                break;
            case R.id.discover_lay:
                if (mBottomViewInt != 1) {
                    setFooterImg(1);
                    replaceFragment(mConnectFragmentList.get(mDiscoverFragmentList.size()-1));
//                    replaceFragment(new DiscoverFragment(), 1);
                }
                break;
            case R.id.messages_lay:
                if (mBottomViewInt != 2) {
                    setFooterImg(2);
                    replaceFragment(mConnectFragmentList.get(mMessagesFragmentList.size()-1));
//                    replaceFragment(new MessagesFragment(), 1);
                }

                break;
            case R.id.update_profile_lay:
                if ((mBottomViewInt != 3)) {
                    setFooterImg(3);
                    replaceFragment(mConnectFragmentList.get(mProfileFragmentList.size()-1));
//                    replaceFragment(new UpdateProfileFragment(), 1);
                }
				break
            case R.id.msg_left_btn_lay:
                setHeadetLeftClick(false);
                setHeaderRightBtnLayVisible(View.VISIBLE);
                replaceFragment(new FriendsFragment());
                break;
            case R.id.invite_right_btn_lay:
               f (!(mCurrentFragment instanceof InviteFriendsFragment)) {
                    AppConstants.INVITE_ALL_USER = AppConstants.SUCCESS_CODE;
                    replaceFragment(new InviteFriendsFragment());
                }                break;
            case R.id.slide_right_img:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.my_profile_txt:

                AppConstants.HOME_FRAG_POS = AppConstants.FAILURE_CODE;
                mDrawerLayout.closeDrawer(GravityCompat.START);
                setDrawerAction(false);
                mHeaderRightBtnLay.setVisibility(View.INVISIBLE);
                addFragment(new UpdateProfileFragment());
                break;
            case R.id.friends_txt:

                AppConstants.HOME_FRAG_POS = AppConstants.FAILURE_CODE;
                mDrawerLayout.closeDrawer(GravityCompat.START);
                setDrawerAction(false);
                mHeaderRightBtnLay.setVisibility(View.VISIBLE);
                addFragment(new FriendsFragment());
                break;

            case R.id.notification_lay:

                AppConstants.HOME_FRAG_POS = AppConstants.FAILURE_CODE;
                mDrawerLayout.closeDrawer(GravityCompat.START);
                setDrawerAction(false);
                mHeaderRightBtnLay.setVisibility(View.INVISIBLE);
                addFragment(new NotificationFragment());
                break;
//            case R.id.slide_right_img:
//                mDrawerLayout.closeDrawer(GravityCompat.START);
//                break;
//            case R.id.my_profile_txt:
//
//                AppConstants.HOME_FRAG_POS = AppConstants.FAILURE_CODE;
//                mDrawerLayout.closeDrawer(GravityCompat.START);
//                setHeadetLeftClick(false);
//                mHeaderRightBtnLay.setVisibility(View.INVISIBLE);
//                addFragment(new UpdateProfileFragment());
//                break;
//            case R.id.friends_txt:
//
//                AppConstants.HOME_FRAG_POS = AppConstants.FAILURE_CODE;
//                mDrawerLayout.closeDrawer(GravityCompat.START);
//                setHeadetLeftClick(false);
//                mHeaderRightBtnLay.setVisibility(View.VISIBLE);
//                addFragment(new FriendsFragment());
//                break;
//
//            case R.id.notification_lay:
//
//                AppConstants.HOME_FRAG_POS = AppConstants.FAILURE_CODE;
//                mDrawerLayout.closeDrawer(GravityCompat.START);
//                setHeadetLeftClick(false);
//                mHeaderRightBtnLay.setVisibility(View.INVISIBLE);
//                addFragment(new NotificationFragment());
//                break;
//            case R.id.settings_txt:
//
//                AppConstants.HOME_FRAG_POS = AppConstants.FAILURE_CODE;
//                mDrawerLayout.closeDrawer(GravityCompat.START);
//                setHeadetLeftClick(false);
//                mHeaderRightBtnLay.setVisibility(View.INVISIBLE);
//                addFragment(new SettingsFragment());
//                break;
//            case R.id.logout_btn:
//                mHeaderRightBtnLay.setVisibility(View.INVISIBLE);
//                mDrawerLayout.closeDrawer(GravityCompat.START);
//                DialogManager.getInstance().showOptionPopup(mActivity, getString(R.string.logout_msg), getString(R.string.yes), getString(R.string.no), new InterfaceTwoBtnCallback() {
//                    @Override
//                    public void onYesClick() {
//                        GlobalMethods.storeUserDetails(mActivity, null);
//                        previousScreen(LoginScreen.class, true);
//                    }
//
//                    public void onNoClick() {
//                    }
//                });
//                break;
        }

    }

    private void setFooterImg(int pos) {
        mBottomViewInt = pos;
        AppConstants.BOTTOM_BUTTON_POS = pos;
        mConnectImg.setImageResource(pos == 0 ? R.drawable.connect_enable_btn : R.drawable.connect_disable_btn);
        mConnectLay.setVisibility(pos == 0 ? View.VISIBLE : View.GONE);

        mDiscoverImg.setImageResource(pos == 1 ? R.drawable.discover_enable_btn : R.drawable.discover_disable_btn);
        mDiscoverLay.setVisibility(pos == 1 ? View.VISIBLE : View.GONE);

        mMessagesImg.setImageResource(pos == 2 ? R.drawable.chat_enable_btn : R.drawable.chat_disable_btn);
        mMessagesLay.setVisibility(pos == 2 ? View.VISIBLE : View.GONE);

        mUpdateProfileImg.setImageResource(pos == 3 ? R.drawable.profile_enable_btn : R.drawable.profile_disable_btn);
        mProfileLay.setVisibility(pos == 3 ? View.VISIBLE : View.GONE);

    }

    public void setHeaderText(String headerStr) {
        mHeaderTxt.setText(headerStr);
    }
 public void setHeaderText(String headerStr) {
        if (mBottomViewInt == 0) {
            mConnectHeaderTxt.setText(headerStr);
        } else if (mBottomViewInt == 1) {
            mDiscoverHeaderTxt.setText(headerStr);
        } else if (mBottomViewInt == 2) {
            mMessagesHeaderTxt.setText(headerStr);
        } else if (mBottomViewInt == 3) {
            mProfileHeaderTxt.setText(headerStr);
        }
    }
	
	public void setHeadRigImgVisible(boolean isVisible, int imgName) {

        if (mBottomViewInt == 0) {
            mConnectHeaderRightBtnLay.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
            if (isVisible) {
                mConnectHeaderRightImg.setImageResource(imgName);
            }
        } else if (mBottomViewInt == 1) {
            mDiscoverHeaderRightBtnLay.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
            if (isVisible) {
                mDiscoverHeaderRightImg.setImageResource(imgName);
            }
        } else if (mBottomViewInt == 2) {
            mMessagesHeaderRightBtnLay.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
            if (isVisible) {
                mMessagesHeaderRightImg.setImageResource(imgName);
            }
        } else if (mBottomViewInt == 3) {
            mProfileHeaderRightBtnLay.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
            if (isVisible) {
                mProfileHeaderRightImg.setImageResource(imgName);
            }
        }

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

                                String notificationCountStr = backgroundRes.getUnread_notifications_count();
                                String blockCountStr = backgroundRes.getBlocks_count();
                                String unReadMsgCountStr = backgroundRes.getUnread_messages();
                               if ((mCurrentFragment instanceof HomeFragment) || (mCurrentFragment instanceof DiscoverFragment) || (mCurrentFragment instanceof UpdateProfileFragment)) {
                                    mNotifyCountTxt.setText(Integer.valueOf(notificationCountStr) > 0 ? notificationCountStr : "");
                                mNotifyCountTxt.setVisibility(Integer.valueOf(notificationCountStr) > 0 ? View.VISIBLE : View.GONE);

                                } else {
                                mNotifyCountTxt.setVisibility(View.GONE);

                                }


                                if (Integer.valueOf(blockCountStr) > 9) {
                                    DialogManager.showToast(HomeScreen.this, getString(R.string.block_msg));
                                    GlobalMethods.storeUserDetails(HomeScreen.this, null);
                                    previousScreen(LoginScreen.class, true);
                                }
                                if (!backgroundRes.getEmail_id().isEmpty()) {
                                    UserDetailsEntity userDetailsRes = GlobalMethods.getUserDetailsRes(HomeScreen.this);
                                    userDetailsRes.setEmail_id(backgroundRes.getEmail_id());
                                    GlobalMethods.storeUserDetails(HomeScreen.this, userDetailsRes);
                                }
                                mMsgCountTxt.setText(Integer.valueOf(unReadMsgCountStr) > 0 ? unReadMsgCountStr : "");
                                mMsgCountTxt.setVisibility(Integer.valueOf(unReadMsgCountStr) > 0 ? View.VISIBLE : View.GONE);


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

    public void setDrawerAction(boolean isMenuScreen) {
        if (isMenuScreen) {
            mHeaderLeftImg.setImageResource(R.drawable.blue_menu_img);
            mHeaderLeftBtnLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                    } else {
                        mDrawerLayout.openDrawer(GravityCompat.START);
                    }
                }
            });
        } else {
            mHeaderLeftImg.setImageResource(R.drawable.blue_back_img);
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
            mHeaderLeftBtnLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    public void addFragment(Fragment fmt) {
        if (fmt != null) {
            mAddFrgBool = true;
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fmt instanceof HomeFragment) {
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            fragmentManager.beginTransaction()
                    .addToBackStack(fmt.getClass().getSimpleName())
                    .add(R.id.content_frame_lay, fmt)
                    .commit();
        } else {
            Log.e(AppConstants.TAG, getString(R.string.err_create_frag));
        }
    }


    private FragmentManager.OnBackStackChangedListener getListener() {
        return new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = getSupportFragmentManager();
                if (manager != null) {
                    if (mAddFrgBool) {
                        mAddFrgBool = false;
                    } else {
                        int backStackEntryCount = manager.getBackStackEntryCount();
                        if (backStackEntryCount > 0) {
                            BaseFragment currFrag = (BaseFragment) manager.findFragmentById(R.id.content_frame_lay);
                            if (currFrag != null) {
                                checkFrag(currFrag);
                                currFrag.onResume();
                            } else {
                                Log.d(AppConstants.TAG, getString(R.string.err_create_frag));
                            }
                        }
                    }
                }
            }

        };

    }

    private void checkFrag(Fragment fragName) {
        setHeaderLay(fragName instanceof ChatConnectFragment ? 2 : 1);
        AppConstants.CHAT_BACK_PRESSED = (fragName instanceof ChatConnectFragment) ? AppConstants.SUCCESS_CODE : AppConstants.FAILURE_CODE;
        mDrawerLayout.setDrawerLockMode(fragName instanceof HomeFragment ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        if (fragName instanceof ChatConnectFragment) {
            mHeaderEndLay.setVisibility(View.VISIBLE);
            mChatHeaderTxt.setText("");
            mNextTxt.setText(getString(R.string.next));
            mNextTxt.setTextColor(ContextCompat.getColor(HomeScreen.this, R.color.screen_bg));
        } else if (fragName instanceof HomeFragment) {

            mHeaderEndLay.setVisibility(View.INVISIBLE);
            mChatHeaderTxt.setText(getString(R.string.new_message));
            mNextTxt.setText(getString(R.string.cancel));
            mNextTxt.setTextColor(ContextCompat.getColor(HomeScreen.this, R.color.red));
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
                userDetailsRes.setPrivate_account(userRes.getResult().get(0).getPrivate_account());
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
                AppConstants.CHAT_BACK_PRESSED = AppConstants.FAILURE_CODE;
                mHeaderInviteLay.setVisibility(View.GONE);
                onBackPressed();
            } else {
                DialogManager.getInstance().showAlertPopup(this, chatDisConnectRes.getMessage());
            }
        }
    }

    @Override
    public void onBackPressed() {
        hideSoftKeyboard(HomeScreen.this);
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (AppConstants.SETTINGS_BACK.equals(AppConstants.SUCCESS_CODE)) {
            callSettingsUpdateAPI();
        } else if (AppConstants.CHAT_BACK_PRESSED.equals(AppConstants.SUCCESS_CODE)) {
            callChatDisConnectAPI();
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

        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (getSupportFragmentManager() != null) {
            if (count == 1) {
                exitFromApp();
            } else {
                getSupportFragmentManager().popBackStack();
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
