package com.bridgellc.bridge.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.adapter.ChatAdapter;
import com.bridgellc.bridge.apiinterface.APIRequestHandler;
import com.bridgellc.bridge.entity.ChatResponseEntity;
import com.bridgellc.bridge.entity.HomeSingleItemEntity;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.model.ChatResponse;
import com.bridgellc.bridge.model.ItemDetailResponse;
import com.bridgellc.bridge.stickylistview.StickyListHeadersListView;
import com.bridgellc.bridge.ui.home.HomeScreenActivity;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.GlobalMethods;

import java.util.ArrayList;
import java.util.Locale;

import retrofit.RetrofitError;

public class ChatScreen extends BaseActivity implements View.OnClickListener {


    private StickyListHeadersListView mChatListView;
    private ChatAdapter mChatListAdapter;
    private ImageView mHeaderLeftImage, mHeaderRightImage;

    private HomeSingleItemEntity mItemDetlRes;
    private Button mSendBtn;
    private EditText mChatText;
    public static boolean isSend = false;

    public static String mPaymentId = "";
    private String friendId = "1", itemId = "32";
    private ArrayList<ChatResponseEntity> mChatResponseList1;
    private ArrayList<ChatResponseEntity> mChatResponseList = new ArrayList<ChatResponseEntity>();
    private ArrayList<ChatResponseEntity> tempChatList = new ArrayList<ChatResponseEntity>();
    long timeInterval = 3000;
    private Handler chatHandler = new Handler();
    private int last_chat_count = 0;
    private int new_chat_count = 0;
    private int first = 0;

//    public static ArrayList<String> mChatHistoryList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_screen);
//        if(getIntent().hasExtra("FriendId"))
//        {
//            friendId = getIntent().getStringExtra("FriendId");
//        }
//        if(getIntent().hasExtra("ItemId"))
//        {
//            itemId = getIntent().getStringExtra("ItemId");
//        }
        mItemDetlRes = new HomeSingleItemEntity();
        initComponents();
    }


    private void initComponents() {


        mViewGroup = (ViewGroup) findViewById(R.id.parent_layer);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);

        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mHeaderTxt.setText(getString(R.string.chat).toUpperCase(Locale.ENGLISH));

        mHeaderLeftImage = (ImageView) findViewById(R.id.header_left_img);
        mHeaderRightImage = (ImageView) findViewById(R.id.header_right_img);
        mHeaderLeftImage.setImageResource(R.drawable.back_img);
        mHeaderRightImage.setImageResource(R.drawable.detail_icon);

        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        mHeadeRightBtnLay = (RelativeLayout) findViewById(R.id.header_right_btn_lay);

        mHeadeLeftBtnLay.setVisibility(View.VISIBLE);
        mHeadeRightBtnLay.setVisibility(View.VISIBLE);

        friendId = AppConstants.CHAT_FRIEND_ID;
        itemId = AppConstants.CHAT_ITEM_ID;

        mChatListView = (StickyListHeadersListView) findViewById(R.id.listview);
        mSendBtn = (Button) findViewById(R.id.send_btn);
        mChatText = (EditText) findViewById(R.id.chat_text);
//        mChatHistoryList.clear();
//        mChatHistoryList.add("");

        APIRequestHandler.getInstance().getChatResponse(friendId, itemId, ChatScreen.this);


        if (!isSend) {

            mChatText.setHint(getString(R.string.cont_send_msg));
            mChatText.setFocusable(false);
            mChatText.setFocusableInTouchMode(false);
            mChatText.setClickable(false);
            mSendBtn.setClickable(false);
            mSendBtn.setBackgroundColor(getResources().getColor(R.color.light_green));

        } else {

            mChatText.setHint(getString(R.string.type_here));
            mChatText.setFocusable(true);
            mChatText.setFocusableInTouchMode(true);
            mChatText.setClickable(true);
            mSendBtn.setClickable(true);
            mSendBtn.setBackgroundColor(getResources().getColor(R.color.green));
        }

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = GlobalMethods.encodeMessage(mChatText.getText().toString().trim());

                if (text.length() > 0) {

                    isSend = true;

                    ChatResponseEntity chatResponseEntity = new ChatResponseEntity();
                    chatResponseEntity.setSender_id(GlobalMethods.getUserID(ChatScreen.this));
                    chatResponseEntity.setChat_message(text);
                    chatResponseEntity.setSender_first_name(GlobalMethods.getStringValue(ChatScreen.this, AppConstants.FIRST_NAME));

                    String currentUTC = GlobalMethods.getUTCtime();
                    chatResponseEntity.setChat_date_time(currentUTC);

                    String str = GlobalMethods.checkBetweentime(ChatScreen.this, currentUTC);
                    chatResponseEntity.setHeaderakey(str);

                    mChatResponseList.add(chatResponseEntity);

                    mChatListAdapter = new ChatAdapter(ChatScreen.this, mChatResponseList);
                    mChatListView.setAdapter(mChatListAdapter);
                    mChatListAdapter.notifyDataSetChanged();

                    APIRequestHandler.getInstance().getChatSendResponse(friendId, itemId, text, mPaymentId, ChatScreen.this);

                }
                mChatText.setText("");
            }
        });
    }

    @Override
    public void onPause() {
        chatHandler.removeCallbacks(chatCheckingService);
        super.onPause();
    }

    @Override
    public void onResume() {

        chatHandler.removeCallbacks(chatCheckingService);
        chatHandler.postDelayed(chatCheckingService, 0);
        super.onResume();
    }

    private Runnable chatCheckingService = new Runnable() {
        @Override
        public void run() {
            if (friendId != null && !friendId.equalsIgnoreCase("") && itemId != null && !itemId.equalsIgnoreCase("")) {
                APIRequestHandler.getInstance().getChatResponse(friendId, itemId, ChatScreen.this);
            }
            chatHandler.postDelayed(this, timeInterval);

        }
    };


    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);
        if (responseObj instanceof ChatResponse) {
            ChatResponse mChatResponse = (ChatResponse) responseObj;

            mChatResponseList1 = new ArrayList<>();
            tempChatList = new ArrayList<>();

            for (ChatResponseEntity mChatResponseEntity :
                    mChatResponse.getResult()) {
                tempChatList.add(mChatResponseEntity);
            }

            for (int i = 0; i < tempChatList.size(); i++) {

                ChatResponseEntity obj = tempChatList.get(i);


                String str = GlobalMethods.checkBetweentime(ChatScreen.this, obj.getChat_date_time());
                obj.setHeaderakey(str);
                mChatResponseList1.add(obj);

            }

            setAdapter(mChatResponseList1);


            //   mChatListAdapter = new ChatAdapter(ChatScreen.this, mChatResponseList);
            //  mChatListView.setAdapter(mChatListAdapter);
            //   mChatListAdapter.notifyDataSetChanged();
        }
        if (responseObj instanceof ItemDetailResponse) {
            ItemDetailResponse itemRes = (ItemDetailResponse) responseObj;
            if (itemRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
//                AppConstants.mPUSH_ITEM_DETAILS = itemRes.getResult();
                mItemDetlRes = itemRes.getResult();

                if (mItemDetlRes.getComplete().equalsIgnoreCase(getString(R.string.one))) {
                    ProductDetailsScreen.mFooterOneTxt = getString(R.string.chat);
                    ProductDetailsScreen.mFooterTwoTxt = getString(R.string.rating);
                    ProductDetailsScreen.mFooterBtnCount = 2;
                    if (mItemDetlRes.getItem_type().equalsIgnoreCase(getString(R.string.one)) && mItemDetlRes.getDelivery_type().equalsIgnoreCase(getString(R.string.two))) {
                        ProductDetailsScreen.mFooterBtnCount = 1;
                        ProductDetailsScreen.mFooterOneTxt = getString(R.string.rating);
                    }
                } else {
                    if (mItemDetlRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller
                        sellBuyApprovList(mItemDetlRes);
                    } else {
                        //I am Buyer
                        buyerOrdList(mItemDetlRes);
                    }
                }

                ProductDetailsScreen.mHomeSingleItemEntity = mItemDetlRes;
                AppConstants.PRODUCT_DETAILS_BACK = getString(R.string.three);
                AppConstants.CODE_SCREEN = AppConstants.SUCCESS_CODE;
                AppConstants.FROME_NOTIFICATION = AppConstants.FAILURE_CODE;
                nextScreen(ProductDetailsScreen.class, true);
            } else {
                DialogManager.showBasicAlertDialog(this, itemRes.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {

                    }
                });
            }
        }
    }

    private void setAdapter(ArrayList<ChatResponseEntity> chatlist) {
        last_chat_count = mChatResponseList.size();
        new_chat_count = chatlist.size();
        first = mChatListView.getFirstVisiblePosition();


        if (mChatListAdapter == null) {

            mChatResponseList.addAll(chatlist);
            mChatListAdapter = new ChatAdapter(ChatScreen.this, mChatResponseList);
            mChatListView.setAdapter(mChatListAdapter);
        } else {
            if (new_chat_count > last_chat_count) {
                mChatResponseList.clear();
                mChatResponseList.addAll(chatlist);
                runOnUiThread(new Runnable() {
                    public void run() {

                        mChatListAdapter.notifyDataSetChanged();
                        if (new_chat_count > 3) {
                            int new_pos = first + 2;
                            mChatListView.setSelection(new_pos);
                            mChatListView.findFocus();
                        }

                    }
                });
            }
        }

    }


    @Override
    public void onRequestFailure(RetrofitError errorCode) {

    }

    @Override
    public void onBackPressed() {

        if (AppConstants.CODE_SCREEN.equalsIgnoreCase(AppConstants.HOME_SCREEN)) {
            AppConstants.CODE_SCREEN = AppConstants.SUCCESS_CODE;
            finishScreen();
        } else if (AppConstants.FROME_NOTIFICATION.equalsIgnoreCase(getString(R.string.one))) {
            AppConstants.FROME_NOTIFICATION = AppConstants.FAILURE_CODE;
            previousScreen(HomeScreenActivity.class, true);

        } else if (AppConstants.FROME_NOTIFICATION.equalsIgnoreCase(getString(R.string.two))) {
            AppConstants.FROME_NOTIFICATION = AppConstants.FAILURE_CODE;
            previousScreen(NotificationScreen.class, true);

        } else {
            finishScreen();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.header_right_btn_lay:
                if (AppConstants.CODE_SCREEN.equalsIgnoreCase(AppConstants.HOME_SCREEN)) {
                    APIRequestHandler.getInstance().getItemDetailsResponse(itemId, friendId, mPaymentId, "", this);

                } else if (AppConstants.FROME_NOTIFICATION.equalsIgnoreCase(getString(R.string.one)) || AppConstants.FROME_NOTIFICATION.equalsIgnoreCase(getString(R.string.two))) {
                    AppConstants.FROME_NOTIFICATION = AppConstants.FAILURE_CODE;
                    AppConstants.PRODUCT_DETAILS_BACK = getString(R.string.three);
                    nextScreen(ProductDetailsScreen.class, true);
//                } else {
//                    onBackPressed();
                } else {
                    finishScreen();
                }
                break;
        }
    }


    private void sellBuyApprovList(HomeSingleItemEntity homeSingleItemEntity) {
        ProductDetailsScreen.mFooterOneTxt = mActivity.getString(R.string.chat);
        ProductDetailsScreen.mFooterTwoTxt = mActivity.getString(R.string.finish);
        ProductDetailsScreen.mFooterBtnCount = 2;
        if (homeSingleItemEntity.getItem_type().equalsIgnoreCase(mActivity.getString(R.string.one)) && (homeSingleItemEntity.getDelivery_type().equalsIgnoreCase(mActivity.getString(R.string.two)))) {
            ProductDetailsScreen.mFooterBtnCount = 1;
            ProductDetailsScreen.mFooterOneTxt = mActivity.getString(R.string.rating);
        }
        if (homeSingleItemEntity.getItem_type().equalsIgnoreCase(mActivity.getString(R.string.two))) {
            if (homeSingleItemEntity.getDelivery_type().equalsIgnoreCase(mActivity.getString(R.string.one))) {

//                ProductDetailsScreen.mFooterTwoTxt = mActivity.getString(R.string.start_c);
            } else {
                ProductDetailsScreen.mFooterTwoTxt = mActivity.getString(R.string.upload_txt);
            }
        } else {
//            ProductDetailsScreen.mMode = mContext.getString(R.string.phy);
//            ProductDetailsScreen.mType = mContext.getString(R.string.serv);
        }
    }

    private void buyerOrdList(HomeSingleItemEntity buypost) {
        ProductDetailsScreen.mFooterOneTxt = mActivity.getString(R.string.chat);
        ProductDetailsScreen.mFooterTwoTxt = mActivity.getString(R.string.code);
        ProductDetailsScreen.mFooterThreeTxt = mActivity.getString(R.string.unsatis);
        ProductDetailsScreen.mFooterBtnCount = 3;


        if (buypost.getItem_type().equalsIgnoreCase(mActivity.getString(R.string.one)) && buypost.getDelivery_type().equalsIgnoreCase(mActivity.getString(R.string.two))) {

            ProductDetailsScreen.mFooterBtnCount = 1;
            ProductDetailsScreen.mFooterOneTxt = mActivity.getString(R.string.rating);

        }
        if (buypost.getItem_type().equalsIgnoreCase(mActivity.getString(R.string.two))) {
            if (buypost.getDelivery_type().equalsIgnoreCase(mActivity.getString(R.string.one))) {
                ProductDetailsScreen.mFooterBtnCount = 2;
//                ProductDetailsScreen.mFooterTwoTxt = mActivity.getString(R.string.finish_c);
                ProductDetailsScreen.mFooterTwoTxt = mActivity.getString(R.string.start);
            } else {

                ProductDetailsScreen.mFooterTwoTxt = mActivity.getString(R.string.preview);
                ProductDetailsScreen.mFooterThreeTxt = mActivity.getString(R.string.approve);
            }
        }
    }

}
