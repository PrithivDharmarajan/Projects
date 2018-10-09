package com.smaat.spark.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.smaat.spark.R;
import com.smaat.spark.adapter.ChatFriendsAdapter;
import com.smaat.spark.adapter.DiscoverAdapter;
import com.smaat.spark.adapter.MessageAdapter;
import com.smaat.spark.database.ChatTable;
import com.smaat.spark.entity.inputEntity.ChatConnDisInputEntity;
import com.smaat.spark.entity.inputEntity.ChatSendReceiveInputEntity;
import com.smaat.spark.entity.inputEntity.NotificationInputEntity;
import com.smaat.spark.entity.outputEntity.ChatReceiveEntity;
import com.smaat.spark.entity.outputEntity.DiscoverEntity;
import com.smaat.spark.entity.outputEntity.TrendsEntity;
import com.smaat.spark.entity.outputEntity.UserDetailsEntity;
import com.smaat.spark.main.BaseFragment;
import com.smaat.spark.model.ChatReceiveResponse;
import com.smaat.spark.model.CommonResponse;
import com.smaat.spark.model.DiscoverResponse;
import com.smaat.spark.model.FriendsListResponse;
import com.smaat.spark.model.TrendsResponse;
import com.smaat.spark.services.APIRequestHandler;
import com.smaat.spark.swipemenu.SwipeMenu;
import com.smaat.spark.swipemenu.SwipeMenuCreator;
import com.smaat.spark.swipemenu.SwipeMenuItem;
import com.smaat.spark.swipemenu.SwipeMenuListView;
import com.smaat.spark.ui.HomeScreen;
import com.smaat.spark.utils.AppConstants;
import com.smaat.spark.utils.DialogManager;
import com.smaat.spark.utils.GlobalMethods;
import com.smaat.spark.utils.InterfaceBtnCallback;
import com.smaat.spark.utils.InterfaceTwoBtnCallback;

import org.apmem.tools.layouts.FlowLayout;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFragment extends BaseFragment implements InterfaceBtnCallback, AdapterView.OnItemClickListener {

    @BindView(R.id.topic_edt)
    EditText mTopicEdt;

    @BindView(R.id.topic_lay)
    LinearLayout mTopicLay;

    @BindView(R.id.scroll_view)
    HorizontalScrollView mScrollView;

    @BindView(R.id.search_dis_img)
    ImageView mSearchDisImg;

    @BindView(R.id.anonymous_mode_img)
    ImageView mAnonymousModeImg;

    @BindView(R.id.seek_bar_txt)
    TextView mSeekBarTxt;

    @BindView(R.id.seek_bar)
    SeekBar mSeekBar;

    @BindView(R.id.chat_parent_lay)
    ScrollView mChatParentLay;

    @BindView(R.id.message_parent_lay)
    LinearLayout mMessageParentLay;

    @BindView(R.id.discover_parent_lay)
    LinearLayout mDiscoverParentLay;

    @BindView(R.id.chat_img)
    ImageView mChatImg;

    @BindView(R.id.people_img)
    ImageView mPeopleImg;

    @BindView(R.id.web_img)
    ImageView mWebImg;

    @BindView(R.id.discover_list)
    RecyclerView mDiscoverRecyclerList;

    @BindView(R.id.trending_flow_lay)
    FlowLayout mTrendingFlowLay;

    @BindView(R.id.friends_lay)
    LinearLayout mFriendsLay;

    @BindView(R.id.message_swipe_list)
    SwipeMenuListView mMessageMenuList;

    @BindView(R.id.friend_list)
    RecyclerView mFriendsRecyclerList;

    @BindView(R.id.friends_search_edt)
    EditText mFriendsSearchEdt;

    @BindView(R.id.seek_lay)
    LinearLayout mSeekLay;

    @SuppressLint("StaticFieldLeak")
    public static TextView mMsgCountTxt;

    private ArrayList<String> mTopicStrArr;
    private ArrayList<TrendsEntity> mTrendsArrRes;
    private ArrayList<UserDetailsEntity> mFriendsArrList;
    private ArrayList<ChatReceiveEntity> mMessageArrList;
    private int mDeletedMessagePos = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_home_screen, container, false);
        ButterKnife.bind(this, rootView);
        setupUI(rootView);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        mMsgCountTxt = (TextView) rootView.findViewById(R.id.msg_count_txt);
        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeScreen) getActivity()).setDrawerAction(true);
        ((HomeScreen) getActivity()).setHeadRigImgVisible(false, R.drawable.app_icon);
        ((HomeScreen) getActivity()).setHeaderText(getString(R.string.connect));
        footerBtnVisible(Integer.valueOf(AppConstants.HOME_FRAG_POS));
        if (AppConstants.VIDEO_SCREEN.equals(AppConstants.SUCCESS_CODE)) {
            AppConstants.VIDEO_SCREEN = AppConstants.FAILURE_CODE;
            ((HomeScreen) getActivity()).addFragment(new ChatConnectFragment());
        }

//        mTopicEdt.setText("");
//        mTopicStrArr.clear();
//        mTopicLay.removeAllViews();
//        setTrendsAdapter();
//        mScrollView.setVisibility(View.GONE);
    }

    private void initView() {
        mTopicStrArr = new ArrayList<>();
        mTrendsArrRes = new ArrayList<>();
        mFriendsArrList = new ArrayList<>();
        mMessageArrList = new ArrayList<>();

        mSearchDisImg.setTag(1);
        mAnonymousModeImg.setTag(1);
        footerBtnVisible(Integer.valueOf(AppConstants.HOME_FRAG_POS));

        //set UserDetails
        ((HomeScreen) getActivity()).setUserInfo();

        ((HomeScreen) getActivity()).mMsgRightBtnLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFriendsArrList.size() > 0) {
                    if (mFriendsLay.getVisibility() == View.VISIBLE) {
                        mFriendsLay.setVisibility(View.GONE);
                        mMessageMenuList.setVisibility(View.VISIBLE);
                        ((HomeScreen) getActivity()).setHeaderLay(3);
                    } else {
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
        //Set Default value to seek bar
        mSeekBar.setProgress(10);
        mSeekBarTxt.setText(String.valueOf(10) + " " + getString(R.string.miles));
        mSeekBar.setEnabled(false);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSeekBarTxt.setText(String.valueOf(progress) + " " + getString(R.string.miles));
                AppConstants.CHAT_DISTANCE = String.valueOf(progress);

            }
        });

        mTopicEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    edtFindText();
                }
                return true;
            }
        });

        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_BUTTON_PRESS:
                        mTopicEdt.setVisibility(View.VISIBLE);
                        mScrollView.setVisibility(View.GONE);
                        setTrendsAdapter();
                        break;
                }
                return true;
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


        mMessageMenuList.setOnItemClickListener(HomeFragment.this);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem swipeMenuItem = new SwipeMenuItem(getActivity());
//                swipeMenuItem.setBackground(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.red)));
                swipeMenuItem.setBackground(R.drawable.msg_delete_bg);

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                swipeMenuItem.setWidth(displayMetrics.widthPixels / 3);
//                swipeMenuItem.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.app_icon));
                swipeMenuItem.setTitle(getString(R.string.x));
                swipeMenuItem.setTitleColor(ContextCompat.getColor(getActivity(), R.color.white));
                swipeMenuItem.setTitleSize(30);
                menu.addMenuItem(swipeMenuItem);
            }
        };
//
        mMessageMenuList.setMenuCreator(creator);
        mMessageMenuList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final int position,
                                           SwipeMenu menu, int index) {

                if (index == 0) {
//                            Delete All API Call
                    DialogManager.getInstance().showOptionPopup(getActivity(), getString(R.string.do_you_want_del), getString(R.string.yes), getString(R.string.no), new InterfaceTwoBtnCallback() {
                        @Override
                        public void onYesClick() {

                            NotificationInputEntity deleteInput = new NotificationInputEntity(AppConstants.API_DELETE_CHAT, AppConstants.PARAMS_DELETE_CHAT, mMessageArrList.get(position).getUser_id(), mMessageArrList.get(position).getFriend_id(), mMessageArrList.get(position).getChat_id());
                            APIRequestHandler.getInstance()
                                    .callDeleteMsgAPI(deleteInput, HomeFragment.this);
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

        trendsApiCall();

    }

    private void edtFindText() {
        String edtStr = mTopicEdt.getText().toString().trim();
        if (!edtStr.isEmpty()) {
            hideSoftKeyboard();
            String ede[] = edtStr.split("\\s+");

            List<String> trEdtStrList = new ArrayList<>(Arrays.asList(ede));
            LinkedHashSet<String> hs = new LinkedHashSet<>();
            hs.addAll(trEdtStrList);
            trEdtStrList.clear();
            trEdtStrList.addAll(hs);

            //Remove
            for (int j = 0; j < mTopicStrArr.size(); j++) {
                boolean isTopStrInBool = false;
                for (int i = 0; i < trEdtStrList.size(); i++) {
                    if (mTopicStrArr.get(j).trim().equalsIgnoreCase(trEdtStrList.get(i).trim())) {
                        isTopStrInBool = true;
                        break;
                    }
                }
                if (!isTopStrInBool) {
                    for (int i = 0; i < mTrendsArrRes.size(); i++) {
                        if (mTopicStrArr.get(j).trim().equalsIgnoreCase(mTrendsArrRes.get(i).getName())) {
                            mTrendsArrRes.get(i).setTrend_selected(getString(R.string.no));
                            mTopicStrArr.remove(j);
                            break;
                        }

                    }
                }
            }

            //Add
            for (int k = 0; k < trEdtStrList.size(); k++) {
                boolean isStrInBool = false;
                for (int j = 0; j < mTopicStrArr.size(); j++) {
                    if (trEdtStrList.get(k).trim().equalsIgnoreCase(mTopicStrArr.get(j).trim())) {
                        isStrInBool = true;
                        break;
                    }
                }
                if (!isStrInBool && !trEdtStrList.get(k).trim().isEmpty()) {
                    mTopicStrArr.add(trEdtStrList.get(k).trim());
                    for (int i = 0; i < mTrendsArrRes.size(); i++) {
                        for (int j = 0; j < mTopicStrArr.size(); j++) {
                            if (mTrendsArrRes.get(i).getName().trim().equalsIgnoreCase(mTopicStrArr.get(j).trim())) {
                                mTrendsArrRes.get(i).setTrend_selected(getString(R.string.yes));
                                break;
                            }
                        }
                    }
                }

            }

            mTopicEdt.setVisibility(View.GONE);
            mScrollView.setVisibility(View.VISIBLE);
        } else {
            for (int i = 0; i < mTrendsArrRes.size(); i++) {
                mTrendsArrRes.get(i).setTrend_selected(getString(R.string.no));
            }

            mTopicStrArr.clear();
        }
        setTrendsAdapter();
        setSelectedTrendsAdapter();
    }

    @OnClick({R.id.search_dis_img, R.id.anonymous_mode_img, R.id.scroll_view, R.id.topic_lay, R.id.connect_btn, R.id.chat_lay, R.id.people_lay, R.id.web_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_dis_img:
                if (mSearchDisImg.getTag().equals(1)) {
                    mSearchDisImg.setTag(2);
                    mSeekBar.setEnabled(true);
                    mSeekLay.setVisibility(View.VISIBLE);
                    mSearchDisImg.setImageResource(R.drawable.distance_enable_img);
                } else {
                    mSearchDisImg.setTag(1);
                    mSeekBar.setProgress(0);
                    mSeekBar.setEnabled(false);
                    mSeekLay.setVisibility(View.INVISIBLE);
                    mSearchDisImg.setImageResource(R.drawable.distance_disable_img);
                }
                break;
            case R.id.anonymous_mode_img:
                if (mAnonymousModeImg.getTag().equals(1)) {
                    mAnonymousModeImg.setTag(2);
                    mAnonymousModeImg.setImageResource(R.drawable.distance_enable_img);
                } else {
                    mAnonymousModeImg.setTag(1);
                    mAnonymousModeImg.setImageResource(R.drawable.distance_disable_img);
                    mAnonymousModeImg.setImageResource(R.drawable.distance_disable_img);
                }
                break;
            case R.id.topic_lay:
            case R.id.scroll_view:
                if (mTopicEdt.getVisibility() == View.VISIBLE) {
                    mTopicEdt.setVisibility(View.GONE);
                    mScrollView.setVisibility(View.VISIBLE);
                    setSelectedTrendsAdapter();
                } else {
                    mTopicEdt.setVisibility(View.VISIBLE);
                    mScrollView.setVisibility(View.GONE);
                    setTrendsAdapter();
                }
                break;
            case R.id.connect_btn:
                String topicStr = mTopicEdt.getText().toString().trim();
                if (topicStr.isEmpty()) {
                    shakeAnimEdt(mTopicEdt);
                    DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.select_topic));
                } else {
                    AppConstants.CHAT_SUB = topicStr;
                    for (int i = 0; i < mTrendsArrRes.size(); i++) {
                        mTrendsArrRes.get(i).setTrend_selected(getString(R.string.no));
                    }
                    mTopicEdt.setText("");
                    mTopicStrArr.clear();
                    mTopicLay.removeAllViews();
                    setTrendsAdapter();
                    mScrollView.setVisibility(View.GONE);
                    ((HomeScreen) getActivity()).addFragment(new ChatConnectFragment());
                }
                break;
            case R.id.chat_lay:
                if (!AppConstants.HOME_FRAG_POS.equals(AppConstants.FAILURE_CODE)) {
                    footerBtnVisible(0);
                }
                break;
            case R.id.people_lay:
                if (!AppConstants.HOME_FRAG_POS.equals(AppConstants.SUCCESS_CODE)) {
                    footerBtnVisible(1);

                }
                break;
            case R.id.web_lay:
                if (!AppConstants.HOME_FRAG_POS.equals(getString(R.string.two))) {
                    footerBtnVisible(2);
                }
                break;
        }

    }


    private void footerBtnVisible(int i) {
        AppConstants.HOME_FRAG_POS = i + "";
        mChatParentLay.setVisibility(i == 0 ? View.VISIBLE : View.GONE);
        mChatImg.setImageResource(i == 0 ? R.drawable.chat_enable_btn : R.drawable.chat_disable_btn);
        mMessageParentLay.setVisibility(i == 1 ? View.VISIBLE : View.GONE);
        mPeopleImg.setImageResource(i == 1 ? R.drawable.people_enable_btn : R.drawable.people_disable_btn);
        mDiscoverParentLay.setVisibility(i == 2 ? View.VISIBLE : View.GONE);
        mWebImg.setImageResource(i == 2 ? R.drawable.discover_enable_btn : R.drawable.discover_disable_btn);

        //set header Text
        String headerStr = getString(R.string.discover);
        if (i == 0) {
            headerStr = getString(R.string.connect);
        } else if (i == 1) {
            headerStr = getString(R.string.messages);
            getMessageApiCall();
        }

        //set message View Visibility
        if (i != 1) {
            mFriendsLay.setVisibility(View.GONE);
            mMessageMenuList.setVisibility(View.VISIBLE);
            ((HomeScreen) getActivity()).setHeaderLay(3);
        }

        //Home Screen Header Control
        ((HomeScreen) getActivity()).setHeaderLay((i == 1) ? 3 : 1);

        //Home Screen Header Text set Here
        ((HomeScreen) getActivity()).setHeaderText(headerStr);
    }

    @Override
    public void onYesClick() {

    }

    private void trendsApiCall() {
        //Trends API Call
        ChatConnDisInputEntity trendsInputEntityRes = new ChatConnDisInputEntity(AppConstants.API_TRENDS, AppConstants.PARAMS_USER_ID, GlobalMethods.getUserID(getActivity()));
        APIRequestHandler.getInstance().callTrendsAPI(trendsInputEntityRes, this);
    }

    private void getMessageApiCall() {
        ChatSendReceiveInputEntity chatReceiveInputEntity = new ChatSendReceiveInputEntity(AppConstants.API_GET_INBOX, AppConstants.PARAMS_USER_ID, GlobalMethods.getUserID(getActivity()), "");
        APIRequestHandler.getInstance().callMsgListAPI(chatReceiveInputEntity, HomeFragment.this);
    }

    private void getFriendsApiCall() {
        ChatConnDisInputEntity friendListInputEntityRes = new ChatConnDisInputEntity(AppConstants.API_FRIEND_LIST, AppConstants.PARAMS_USER_ID, GlobalMethods.getUserID(getActivity()));
        APIRequestHandler.getInstance().callFriendsAndUserListAPI(friendListInputEntityRes, this);
    }

    private void getDiscoverApiCall() {
        ChatConnDisInputEntity discoverInputEntityRes = new ChatConnDisInputEntity(AppConstants.API_DISCOVER, AppConstants.PARAMS_USER_ID, GlobalMethods.getUserID(getActivity()));
        APIRequestHandler.getInstance().callDiscoverAPI(discoverInputEntityRes, this);
    }

    @Override
    public void onRequestSuccess(Object resObj) {
        if (resObj instanceof TrendsResponse) {
            getMessageApiCall();
            TrendsResponse trendsRes = (TrendsResponse) resObj;
            if (trendsRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                mTrendsArrRes = new ArrayList<>();
                mTrendsArrRes = trendsRes.getResult();
                setTrendsAdapter();
            } else {
                DialogManager.getInstance().showAlertPopup(getActivity(), trendsRes.getMessage());
            }

        } else if (resObj instanceof ChatReceiveResponse) {
            getFriendsApiCall();
            ChatReceiveResponse messageRes = (ChatReceiveResponse) resObj;
            if (messageRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                mMessageArrList = new ArrayList<>();
                mMessageArrList = messageRes.getResult();

//                ArrayList<ChatReceiveEntity> inboxMsgList = messageRes.getResult();
//
//                for (int i = 0; i < inboxMsgList.size(); i++) {
//                    if (ChatTable.getDeletedChat(inboxMsgList.get(i)).isEmpty()) {
//                        mMessageArrList.add(inboxMsgList.get(i));
//                    }
//                }


                setMessageAdapter();
            } else {
                DialogManager.getInstance().showAlertPopup(getActivity(), messageRes.getMessage());
            }

        } else if (resObj instanceof FriendsListResponse) {

            FriendsListResponse friendsListRes = (FriendsListResponse) resObj;
            getDiscoverApiCall();
            if (friendsListRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                mFriendsArrList = friendsListRes.getResult();
                if (mFriendsArrList.size() > 0) {
                    setFriendListAdapter(mFriendsArrList);
                }
            } else {
                DialogManager.getInstance().showAlertPopup(getActivity(), friendsListRes.getMessage());
            }

        } else if (resObj instanceof DiscoverResponse) {
            DiscoverResponse discoverRes = (DiscoverResponse) resObj;
            if (discoverRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                setDiscoverAdapter(discoverRes.getResult());
            } else {
                DialogManager.getInstance().showAlertPopup(getActivity(), discoverRes.getMessage());
            }
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

    @Override
    public void onRequestFailure(Throwable t) {
//        baseAlertDismiss();
        if (t instanceof IOException || t.getCause() instanceof ConnectException || t.getCause() instanceof java.net.UnknownHostException
                || t.getMessage() == null) {

            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.no_internet));
            trendsApiCall();

        } else if (t.getCause() instanceof java.net.SocketTimeoutException) {

            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.connect_time_out));
            trendsApiCall();

        } else {
            DialogManager.getInstance().showAlertPopup(getActivity(), t.getMessage());
            trendsApiCall();

        }
    }

    private void setTrendsAdapter() {
        mTrendingFlowLay.removeAllViews();
        if (mTrendsArrRes.size() > 0) {
            for (int i = 0; i < mTrendsArrRes.size(); i++) {
                View trendsView = LayoutInflater.from(getActivity()).inflate(R.layout.adap_trends_view, null, false);
                final TextView trendsTxt = (TextView) trendsView.findViewById(R.id.trends_txt);
                trendsTxt.setText(mTrendsArrRes.get(i).getName());
                String tagStr = mTrendsArrRes.get(i).getTrend_selected();
                if (tagStr.equals(getString(R.string.yes))) {
                    trendsTxt.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.trending_select_bg));
                } else {
                    tagStr = getString(R.string.no);
                    mTrendsArrRes.get(i).setTrend_selected(tagStr);
                    trendsTxt.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.trending_un_select_bg));
                }
                trendsTxt.setTag(i);
                trendsTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int posInt = (Integer) view.getTag();
//                    if (mScrollView.getVisibility() == View.VISIBLE) {
//                        mTopicEdt.setVisibility(View.VISIBLE);
//                        mScrollView.setVisibility(View.GONE);
//                    }
                        if (mTopicStrArr.size() > 0 && mTrendsArrRes.get(posInt).getTrend_selected().equals(getString(R.string.yes))) {
                            for (int i = 0; i < mTopicStrArr.size(); i++) {

                                if (mTopicStrArr.get(i).equals(mTrendsArrRes.get(posInt).getName())) {
                                    mTopicStrArr.remove(i);
                                    mTrendsArrRes.get(posInt).setTrend_selected(getString(R.string.no));
                                    trendsTxt.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.trending_un_select_bg));
                                }
                            }
                        } else {
                            mTopicStrArr.add(mTrendsArrRes.get(posInt).getName());
                            mTrendsArrRes.get(posInt).setTrend_selected(getString(R.string.yes));
                            trendsTxt.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.trending_select_bg));
                        }

                        mTopicEdt.setVisibility(View.GONE);
                        mScrollView.setVisibility(View.VISIBLE);
//                        setSelectedTrendsAdapter();
                        setEdtValues(mTopicStrArr);
                        edtFindText();

                    }
                });

                mTrendingFlowLay.addView(trendsView);
            }
        } else {
            mTopicEdt.setVisibility(View.VISIBLE);
            mScrollView.setVisibility(View.GONE);
        }
    }


    private void setSelectedTrendsAdapter() {
        mTopicLay.removeAllViews();
        for (int pos = 0; pos < mTopicStrArr.size(); pos++) {
            final ViewGroup nullParent = null;
            View selectedView = LayoutInflater.from(getActivity()).inflate(R.layout.adap_selected_trends_view, nullParent);
            final TextView trendsTxt = (TextView) selectedView.findViewById(R.id.trends_txt);
            final TextView cancelTxt = (TextView) selectedView.findViewById(R.id.cancel_txt);
            trendsTxt.setText(mTopicStrArr.get(pos));
            cancelTxt.setTag(pos);

            cancelTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int posInt = (Integer) view.getTag();
                    boolean topIsInBool = false;

                    for (int i = 0; i < mTrendsArrRes.size(); i++) {
                        if (mTopicStrArr.get(posInt).equals(mTrendsArrRes.get(i).getName())) {
                            mTrendsArrRes.get(i).setTrend_selected(getString(R.string.no));
                            mTopicStrArr.remove(posInt);
                            topIsInBool = true;
                            if (mTopicStrArr.size() > 0) {
                                setSelectedTrendsAdapter();
                            } else {
                                mTopicLay.removeAllViews();
                                mTopicEdt.setVisibility(View.VISIBLE);
                                mScrollView.setVisibility(View.GONE);
                            }
                            setTrendsAdapter();
                            setEdtValues(mTopicStrArr);
                            break;
                        }

                    }
                    if (!topIsInBool) {
                        mTopicStrArr.remove(posInt);
                        if (mTopicStrArr.size() > 0) {
                            setSelectedTrendsAdapter();
                        } else {
                            mTopicLay.removeAllViews();
                            mTopicEdt.setVisibility(View.VISIBLE);
                            mScrollView.setVisibility(View.GONE);
                        }
                        setTrendsAdapter();
                        setEdtValues(mTopicStrArr);
                    }

                }
            });

            mTopicLay.addView(selectedView);
        }
    }


    private void setEdtValues(ArrayList<String> topicStrArr) {
        String topicStr = "";
        for (int i = 0; i < topicStrArr.size(); i++) {

            if (i == 0) {
                topicStr = topicStrArr.get(i);
            } else {
                topicStr += " " + topicStrArr.get(i);
            }
        }
        mTopicEdt.setText(topicStr);
    }


    private void setDiscoverAdapter(ArrayList<DiscoverEntity> discoverList) {

        final DiscoverAdapter discoverAdapter = new DiscoverAdapter(getActivity(), discoverList);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int mod = position % 6;

                if (position == 0)
                    return 3;
                else if (position < 5)
                    return 1;
                else if (position == 5)
                    return 2;
                else if (mod == 0)
                    return 3;
                else if (mod < 5)
                    return 1;
                else
                    return 2;
            }
        });
        mDiscoverRecyclerList.setItemAnimator(new DefaultItemAnimator());
        mDiscoverRecyclerList.setLayoutManager(layoutManager);
        mDiscoverRecyclerList.setAdapter(discoverAdapter);
        mDiscoverRecyclerList.setNestedScrollingEnabled(false);
        discoverAdapter.notifyDataSetChanged();
    }


    private void setMessageAdapter() {

        MessageAdapter messageAdapter = new MessageAdapter(getActivity(), mMessageArrList);
        mMessageMenuList.setAdapter(messageAdapter);
//        GlobalMethods.setListViewHeightBasedOnChildren(mMessageMenuList);
    }


    private void setFriendListAdapter(ArrayList<UserDetailsEntity> friendsList) {
        ChatFriendsAdapter chatFriendsAdapter = new ChatFriendsAdapter(getActivity(), friendsList);
        mFriendsRecyclerList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFriendsRecyclerList.setAdapter(chatFriendsAdapter);
        mFriendsRecyclerList.setNestedScrollingEnabled(false);
        chatFriendsAdapter.notifyDataSetChanged();
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


        AppConstants.CHAT_SUB = AppConstants.CHAT_SUB_FRIEND;
        AppConstants.CHAT_FRIEND_ID = mMessageArrList.get(i).getFriend_id();
        AppConstants.CHAT_FRIEND_NAME = mMessageArrList.get(i).getFriendname();
//                    nextScreen(ChatNormalScreen.class, false);
        ((HomeScreen) getActivity()).addFragment(new ChatFriendFragment());
//        type = mNotifitResList.get(position).getApifrom();
//        notifyID = mNotifitResList.get(position).getNotification_id();
////        APIRequestHandler.getInstance().getItemDetailsResponse(mNotifitResList.get(position).getTypeid(), mNotifitResList.get(position).getUser_id(), mNotifitResList.get(position).getPayment_id(), mNotifitResList.get(position).getNotification_id(), this);
//        APIRequestHandler.getInstance().getItemDetailsResponse(mNotifitResList.get(position).getTypeid(), mNotifitResList.get(position).getUser_id(), mNotifitResList.get(position).getPayment_id(), "", this);

    }
}
