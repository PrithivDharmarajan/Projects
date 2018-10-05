package com.bridgellc.bridge.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.adapter.NegotiationChatAdapter;
import com.bridgellc.bridge.apiinterface.APIRequestHandler;
import com.bridgellc.bridge.entity.HomeSingleItemEntity;
import com.bridgellc.bridge.entity.NegotiateResponseEntity;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.model.BuyItemResponse;
import com.bridgellc.bridge.model.CommonResponse;
import com.bridgellc.bridge.model.NegotiateResponse;
import com.bridgellc.bridge.model.PayForItemResponse;
import com.bridgellc.bridge.model.PaypalPayResponse;
import com.bridgellc.bridge.stickylistview.StickyListHeadersListView;
import com.bridgellc.bridge.ui.home.HomeScreenActivity;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogManagerTwoBtnCallback;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.GlobalMethods;

import java.util.ArrayList;
import java.util.Locale;

import retrofit.RetrofitError;

public class NegotiationChatRoom extends BaseActivity implements View.OnClickListener {


    private StickyListHeadersListView mChatListView;
    private NegotiationChatAdapter mChatListAdapter = null;
    private NegotiateResponseEntity chatResponseEntity = null;
    private boolean isFirstTime = true;

    private TextView titleTv, mDollTxt;
    private ImageView mHeaderLeftImage, mHeaderRightImage;
    private TextView mHeader_right_txt;
    private Button mSendBtn;
    private EditText mChatText;
    private String ItemQuantity = "", notification = "", neg_ID = "";
    private int requestType = 1;
    private boolean approveClick = false;
    private boolean isSend = false;
    private String mItemName = "", mItemQuantity = "", mItemPrice = "", mServicePrice = "", mTotalPrice = "";
    private int last_chat_count = 0;
    private int new_chat_count = 0;
    private int first = 0;
    private Dialog mNegScreenAlert;


    private boolean isMax = false;
    private String mUserId = "", mFriendId = "", mItemId = "", mBidId = "";
    ArrayList<NegotiateResponseEntity> mChatResponseList1;
    private ArrayList<NegotiateResponseEntity> mChatResponseList = new ArrayList<>();
    private ArrayList<NegotiateResponseEntity> tempChatList = new ArrayList<>();

    long timeInterval = 3000;
    private Handler chatHandler = new Handler();

    public static HomeSingleItemEntity mHomeSingleItemEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.negotiation_chat_screen);


        initComponents();
    }


    private void initComponents() {
        mViewGroup = (ViewGroup) findViewById(R.id.parent_layer);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);


        setHeader();
        mUserId = AppConstants.NEGO_USER_ID;
        mFriendId = AppConstants.NEGO_FRIEND_ID;
        mBidId = AppConstants.NEGO_BID_ID;
        mItemId = AppConstants.NEGO_ITEM_ID;
        ItemQuantity = AppConstants.NEGO_ITEM_QTY;
        notification = AppConstants.NEGO_ITEM_NOTI;


        mChatListView = (StickyListHeadersListView) findViewById(R.id.listview);
        mSendBtn = (Button) findViewById(R.id.send_btn);
        mChatText = (EditText) findViewById(R.id.chat_text);
        mChatText.setHint(getString(R.string.neg_amt));
//        mChatText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
//        mChatText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
//        chat_history.clear();
//        chat_history.add("");


        APIRequestHandler.getInstance().getNegotiationsResponse(mUserId, mFriendId, mItemId, notification,
                NegotiationChatRoom.this);

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = mChatText.getText().toString().trim();

                if (text.length() > 0&&editTxtValidation()) {
                    requestType = 1;
                    mHeader_right_txt.setVisibility(View.INVISIBLE);
                    mHeadeRightBtnLay.setVisibility(View.INVISIBLE);
                    mSendBtn.setBackgroundColor(getResources().getColor(R.color.light_green));
                    mSendBtn.setEnabled(false);
                    mChatText.setEnabled(false);

                    isSend = true;


                    chatResponseEntity = new NegotiateResponseEntity();
                    chatResponseEntity.setUser_id(GlobalMethods.getUserID(NegotiationChatRoom.this));

                    chatResponseEntity.setNegitation_amount(text);
                    chatResponseEntity.setUser_name(GlobalMethods.getStringValue
                            (NegotiationChatRoom.this, AppConstants.FIRST_NAME));
                    String currentUTC = GlobalMethods.getUTCtime();


                    String str = GlobalMethods.checkBetweentime(NegotiationChatRoom.this, currentUTC);
                    chatResponseEntity.setDate_sent(currentUTC);
                    chatResponseEntity.setHeaderakey(str);

                    mChatResponseList.add(chatResponseEntity);

                    mChatListAdapter = new NegotiationChatAdapter(NegotiationChatRoom.this, mChatResponseList);
                    mChatListView.setAdapter(mChatListAdapter);
                    mChatListAdapter.notifyDataSetChanged();

                    if (mNegScreenAlert != null && mNegScreenAlert.isShowing()) {
                        mNegScreenAlert.dismiss();
                    }
                    mNegScreenAlert = DialogManager.showBasicAlertDialog(NegotiationChatRoom.this,
                            getString(R.string.you_cannot_msg), new DialogMangerOkCallback() {
                                @Override
                                public void onOkClick() {

                                }
                            });


                    APIRequestHandler.getInstance().getNegotiateSendResponse(mFriendId, mItemId,
                            text, ItemQuantity, mBidId, NegotiationChatRoom.this);
                }
                mChatText.setText("");
            }
        });
        InputFilter filter = new InputFilter() {
            final int maxDigitsBeforeDecimalPoint = 6;
            final int maxDigitsAfterDecimalPoint = 2;

            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                StringBuilder builder = new StringBuilder(dest);
                builder.replace(dstart, dend, source
                        .subSequence(start, end).toString());
                if (!builder.toString().matches(
                        "(([1-9]{1})([0-9]{0," + (maxDigitsBeforeDecimalPoint - 1) + "})?)?(\\.[0-9]{0," + maxDigitsAfterDecimalPoint + "})?"

                )) {
                    if (source.length() == 0)
                        return dest.subSequence(dstart, dend);
                    return "";
                }

                return null;

            }
        };

        mChatText.setFilters(new InputFilter[]{filter});
        mChatText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String mt = mChatText.getText().toString().trim();
                if (mt.length() > 0) {
                    mDollTxt.setVisibility(View.VISIBLE);
                } else {
                    mDollTxt.setVisibility(View.GONE);
                }

            }
        });
    }
    private boolean editTxtValidation() {

        boolean tickVal;

            String mval = mChatText.getText().toString().trim();
            if (mval.contains(".")) {
                String mV[] = mval.split("\\.");
                if (mV.length == 2) {
                    mval = GlobalMethods.afterTwoPointVal(mval);
                } else {
                    mval = mval + ".00";
                }
            } else {
                mval = mval + ".00";
            }
            tickVal = (!mval.equalsIgnoreCase(AppConstants.FAILURE_CODE) && !mval.equalsIgnoreCase(GlobalMethods.afterTwoPointVal(AppConstants.FAILURE_CODE)));
        mChatText.setText(mval);
        mChatText.setSelection(mval.length());


        return tickVal;
    }

    @Override
    public void onPause() {
        super.onPause();
        chatHandler.removeCallbacks(chatCheckingService);
        if (mNegScreenAlert != null && mNegScreenAlert.isShowing()) {
            mNegScreenAlert.dismiss();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mNegScreenAlert != null && mNegScreenAlert.isShowing()) {
            mNegScreenAlert.dismiss();
        }
    }

    @Override
    public void onRequestFailure(RetrofitError errorCode) {

        if (isSend == true) {

            super.onRequestFailure(errorCode);
            mChatResponseList.remove(chatResponseEntity);

            isSend = false;
            int msgListSize = mChatResponseList.size();
            if (msgListSize < 11 && msgListSize > 0) {
                if (!(mChatResponseList.get(msgListSize - 1).getUser_id().equalsIgnoreCase
                        (GlobalMethods.getUserID(this)))) {
                    mSendBtn.setBackgroundColor(getResources().getColor(R.color.green));
                    mSendBtn.setEnabled(true);
                    mChatText.setEnabled(true);


                } else {
                    mSendBtn.setBackgroundColor(getResources().getColor(R.color.light_green));
                    mSendBtn.setEnabled(false);
                    mChatText.setEnabled(false);
                }

            }
            chatResponseEntity = null;
        }
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
            if (mFriendId != null && !mFriendId.equalsIgnoreCase("") && mItemId != null && !mItemId
                    .equalsIgnoreCase("")) {
                APIRequestHandler.getInstance().getNegotiationsResponse(mUserId, mFriendId, mItemId, notification,
                        NegotiationChatRoom.this);
            }
            chatHandler.postDelayed(this, timeInterval);

        }
    };


    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);
        if (responseObj instanceof NegotiateResponse && requestType != 2) {
            NegotiateResponse mChatResponse = (NegotiateResponse) responseObj;

            if (mChatResponse.getResponse_code().equalsIgnoreCase(getString(R.string.two))) {

                String alertMsg = mChatResponse.getMessage();

                if (mHomeSingleItemEntity.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(this))) {
                    alertMsg = getString(R.string.sell_approve);
                }
                if (mNegScreenAlert != null && mNegScreenAlert.isShowing()) {
                    mNegScreenAlert.dismiss();
                }
                mNegScreenAlert = DialogManager.showBasicAlertDialog(NegotiationChatRoom.this, alertMsg, new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {
                        previousScreen(HomeScreenActivity.class, true);
                    }
                });
            } else {
                mChatResponseList1 = new ArrayList<NegotiateResponseEntity>();
                tempChatList = new ArrayList<NegotiateResponseEntity>();
                for (NegotiateResponseEntity mChatResponseEntity :
                        mChatResponse.getResult()) {
                    tempChatList.add(mChatResponseEntity);

                }

                for (int i = 0; i < tempChatList.size(); i++) {

                    NegotiateResponseEntity obj = tempChatList.get(i);


                    String str = GlobalMethods.checkBetweentime(NegotiationChatRoom.this, obj.getDate_sent());
                    obj.setHeaderakey(str);
                    mChatResponseList1.add(obj);

                }

                if (mChatResponseList1.size() == 0 || !(mChatResponseList1.get(mChatResponseList1.size() - 1)
                        .getUser_id
                                ().equalsIgnoreCase
                                (GlobalMethods.getUserID(this)))) {
                    if (mChatResponseList1.size() == 0) {
                        mHeader_right_txt.setVisibility(View.INVISIBLE);
                        mHeadeRightBtnLay.setVisibility(View.INVISIBLE);
                    } else {
                        mHeader_right_txt.setVisibility(View.VISIBLE);
                        mHeadeRightBtnLay.setVisibility(View.VISIBLE);
                    }
                    mSendBtn.setBackgroundColor(getResources().getColor(R.color.green));
                    mSendBtn.setEnabled(true);
                    mChatText.setEnabled(true);
                } else {
                    if (isFirstTime) {
                        isFirstTime = false;
                        if (mNegScreenAlert != null && mNegScreenAlert.isShowing()) {
                            mNegScreenAlert.dismiss();
                        }
                        mNegScreenAlert = DialogManager.showBasicAlertDialog(NegotiationChatRoom.this, getString(R.string.you_cannot_msg), new DialogMangerOkCallback() {
                            @Override
                            public void onOkClick() {

                            }
                        });
                    }
                    mHeader_right_txt.setVisibility(View.INVISIBLE);
                    mHeadeRightBtnLay.setVisibility(View.INVISIBLE);
                    mSendBtn.setBackgroundColor(getResources().getColor(R.color.light_green));
                    mSendBtn.setEnabled(false);
                    mChatText.setEnabled(false);
                }

                if (mChatResponseList1.size() == 10) {

                    if (isMax == false) {
                        isMax = true;
                        if (mNegScreenAlert != null && mNegScreenAlert.isShowing()) {
                            mNegScreenAlert.dismiss();
                        }
                        mNegScreenAlert = DialogManager.showBasicAlertDialog(this, getString(R.string.max_msg), new DialogMangerOkCallback() {
                            @Override
                            public void onOkClick() {
                                isMax = false;

                                mSendBtn.setBackgroundColor(getResources().getColor(R.color.light_green));
                                mSendBtn.setEnabled(false);
                                mChatText.setEnabled(false);
//                                if (AppConstants.CHAT_BACK.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
//                                    AppConstants.CHAT_BACK = AppConstants.FAILURE_CODE;
//                                } else if (AppConstants.FROME_NOTIFICATION.equalsIgnoreCase(getString(R.string.one))) {
//                                    AppConstants.FROME_NOTIFICATION = AppConstants.FAILURE_CODE;
//
//                                } else if (AppConstants.FROME_NOTIFICATION.equalsIgnoreCase(getString(R.string.two))) {
//                                    AppConstants.FROME_NOTIFICATION = AppConstants.FAILURE_CODE;
//                                } else if (AppConstants.CHAT_BACK.equalsIgnoreCase(AppConstants.HOME_SCREEN)) {
//                                    AppConstants.CHAT_BACK = AppConstants.FAILURE_CODE;
//                                }
//
//                                previousScreen(HomeScreenActivity.class, true);

                            }
                        });
                    }
                    mSendBtn.setBackgroundColor(getResources().getColor(R.color.light_green));
                    mSendBtn.setEnabled(false);
                    mChatText.setEnabled(false);
                }

                if (mChatResponseList1.size() == 1) {

                    if (approveClick == false && mChatResponseList1.get(0).getApprove().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                        String msg = getString(R.string.your_item_app);
                        approveClick = true;
                        if (mHomeSingleItemEntity.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(this))) {
                            msg = getString(R.string.sell_approve);
                        }

                        if (mNegScreenAlert != null && mNegScreenAlert.isShowing()) {
                            mNegScreenAlert.dismiss();
                        }
                        mNegScreenAlert = DialogManager.showBasicAlertDialog(this, msg, new DialogMangerOkCallback() {
                            @Override
                            public void onOkClick() {

                                if (AppConstants.CHAT_BACK.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                                    AppConstants.CHAT_BACK = AppConstants.FAILURE_CODE;
                                } else if (AppConstants.FROME_NOTIFICATION.equalsIgnoreCase(getString(R.string.one))) {
                                    AppConstants.FROME_NOTIFICATION = AppConstants.FAILURE_CODE;

                                } else if (AppConstants.FROME_NOTIFICATION.equalsIgnoreCase(getString(R.string.two))) {
                                    AppConstants.FROME_NOTIFICATION = AppConstants.FAILURE_CODE;
                                } else if (AppConstants.CHAT_BACK.equalsIgnoreCase(AppConstants.HOME_SCREEN)) {
                                    AppConstants.CHAT_BACK = AppConstants.FAILURE_CODE;
                                }
                                nextScreen(HomeScreenActivity.class, true);
                            }
                        });
                    }


                }
                int msgListSize = mChatResponseList1.size();
                if (chatResponseEntity != null && msgListSize < 11 && msgListSize > 0) {
                    if (!(mChatResponseList1.get(msgListSize - 1).getUser_id().equals(chatResponseEntity
                            .getUser_id()))) {
                        mChatResponseList1.add(chatResponseEntity);
                    } else {
                        chatResponseEntity = null;
                    }
                }
                isFirstTime = false;
                Log.d("size of chat", mChatResponseList1.size() + "");
                //      mChatListAdapter = new NegotiationChatAdapter(NegotiationChatRoom.this,
                //          mChatResponseList);
                //  mChatListView.setAdapter(mChatListAdapter);
                //    mChatListAdapter.notifyDataSetChanged();
                setAdapter(mChatResponseList1);

            }
        } else if (responseObj instanceof PayForItemResponse && requestType != 2) {
            PayForItemResponse payForItemEntity = (PayForItemResponse) responseObj;
            if (payForItemEntity.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
//                BuyerCodeScreen.mBuyerCode = payForItemEntity.getResult().getUnique_code();
//                nextScreen(BuyerCodeScreen.class, false);
            } else {
                if (mNegScreenAlert != null && mNegScreenAlert.isShowing()) {
                    mNegScreenAlert.dismiss();
                }
                mNegScreenAlert = DialogManager.showBasicAlertDialog(this, payForItemEntity.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {

                    }
                });
            }
        } else if (responseObj instanceof CommonResponse && requestType == 2) {
            CommonResponse commonResponse = (CommonResponse) responseObj;

            String msg = commonResponse
                    .getMessage();

            if (msg == null) {
                msg = getString(R.string.serv_res_success);
            }

//            DialogManager.showBasicAlertDialog(this, getString(R.string.approve_c), msg, new
//                    DialogMangerOkCallback() {
//                        @Override
//                        public void onOkClick() {

//                            if (mHomeSingleItemEntity.getUser_id().equalsIgnoreCase
//                                    (GlobalMethods.getUserID(NegotiationChatRoom.this))&&!mHomeSingleItemEntity.getItem_type().equalsIgnoreCase(getString(R.string.two))) {
//                                nextScreen(HomeScreenActivity.class, true);
//                            } else if (mHomeSingleItemEntity.getItem_type().equalsIgnoreCase(getString(R.string.two))) {
//                            String mBuyerID = (GlobalMethods.getUserID(NegotiationChatRoom.this));
//                            if (mHomeSingleItemEntity.getUser_id().equalsIgnoreCase
//                                    (GlobalMethods.getUserID(NegotiationChatRoom.this))) {
//                                mBuyerID = mHomeSingleItemEntity.getBuyer_id();
//                            }

            String mBuyerID = "";
            if (mHomeSingleItemEntity.getBuyer_id() != null && !mHomeSingleItemEntity.getBuyer_id().equalsIgnoreCase(AppConstants.FAILURE_CODE) && !mHomeSingleItemEntity.getBuyer_id().equalsIgnoreCase("")) {
                mBuyerID = mHomeSingleItemEntity.getBuyer_id();
            } else {
                mBuyerID = GlobalMethods.getUserID(NegotiationChatRoom.this);
            }

//                            System.out.println("Neg Screen--" + mBuyerID);
//                            APIRequestHandler.getInstance().payForItemResponse(mBuyerID, mHomeSingleItemEntity.getItem_id(),
//                                    ItemQuantity, NegotiationChatRoom.this);

//            APIRequestHandler.getInstance().buyItemResponse(mBuyerID,
//                    mHomeSingleItemEntity.getItem_id(),
//                    ItemQuantity, neg_ID, AppConstants.FAILURE_CODE, NegotiationChatRoom.this);
//                            } else {
//
//                                ProductDetailsBuyNeg.mHomeSingleItemEntity = mHomeSingleItemEntity;
//
//                                if (mChatResponseList.size() > 0) {
//                                    String amount = mChatResponseList.get(mChatResponseList.size() - 1)
//                                            .getNegitation_amount();
//                                    ProductDetailsBuyNeg.priceAmount = "$" + String.format("%.0f",
//                                            Double.parseDouble(amount));
//
//
//                                } else {
//                                    ProductDetailsBuyNeg.priceAmount = "$" + String.format("%.0f",
//                                            mHomeSingleItemEntity.getItem_cost());
//                                }
//                                ProductDetailsBuyNeg.productImg = mHomeSingleItemEntity.getPicture1();
//
//                                Intent intent = new Intent(NegotiationChatRoom.this, ProductBuyNegScreen.class);
//                                intent.putExtra("FriendId", friendId);
//                                intent.putExtra("ItemId", ProductDetailsBuyNeg.mHomeSingleItemEntity.getItem_id());
//                                intent.putExtra("isNegotiate", true);
//                                startActivity(intent);
//                                overridePendingTransition(R.anim.slide_in_right,
//                                        R.anim.slide_out_left);
//                                finish();
//                        }

//                    APIRequestHandler.getInstance().payForItemResponse(itemId,
//                            "1" , NegotiationChatRoom.this);

//                        }
//                    });

        }
        if (responseObj instanceof CommonResponse && isSend == true) {
            CommonResponse mChatRes = (CommonResponse) responseObj;
            if (mChatRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                isSend = false;
                mSendBtn.setBackgroundColor(getResources().getColor(R.color.light_green));
                mSendBtn.setEnabled(false);
                mChatText.setEnabled(false);
            } else {
                if (isSend == true) {
                    mChatResponseList.remove(chatResponseEntity);
                    isSend = false;
                    int msgListSize = mChatResponseList.size();
                    if (msgListSize < 11 && msgListSize > 0) {
                        if (!(mChatResponseList.get(msgListSize - 1).getUser_id().equalsIgnoreCase
                                (GlobalMethods.getUserID(this)))) {
                            mSendBtn.setBackgroundColor(getResources().getColor(R.color.green));
                            mSendBtn.setEnabled(true);
                            mChatText.setEnabled(true);


                        } else {
                            mSendBtn.setBackgroundColor(getResources().getColor(R.color.light_green));
                            mSendBtn.setEnabled(false);
                            mChatText.setEnabled(false);
                        }

                    }
                    chatResponseEntity = null;
                }
            }

        }
        if (responseObj instanceof PayForItemResponse) {
            PayForItemResponse mPayForItemResponse = (PayForItemResponse) responseObj;
            if (mPayForItemResponse.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                if (AppConstants.CHAT_BACK.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                    AppConstants.CHAT_BACK = AppConstants.FAILURE_CODE;
                } else if (AppConstants.FROME_NOTIFICATION.equalsIgnoreCase(getString(R.string.one))) {
                    AppConstants.FROME_NOTIFICATION = AppConstants.FAILURE_CODE;

                } else if (AppConstants.FROME_NOTIFICATION.equalsIgnoreCase(getString(R.string.two))) {
                    AppConstants.FROME_NOTIFICATION = AppConstants.FAILURE_CODE;
                } else if (AppConstants.CHAT_BACK.equalsIgnoreCase(AppConstants.HOME_SCREEN)) {
                    AppConstants.CHAT_BACK = AppConstants.FAILURE_CODE;
                }
                nextScreen(HomeScreenActivity.class, true);
            } else {
                if (mNegScreenAlert != null && mNegScreenAlert.isShowing()) {
                    mNegScreenAlert.dismiss();
                }
                mNegScreenAlert = DialogManager.showBasicAlertDialog(this, mPayForItemResponse.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {

                    }
                });
            }
        }
        if (responseObj instanceof BuyItemResponse) {
            BuyItemResponse mCommonResponse = (BuyItemResponse) responseObj;

            if (mCommonResponse.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {


//                if (mHomeSingleItemEntity.getPayment_mode().equalsIgnoreCase(getString(R.string.three))) {
//                    PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(1), "USD", "BRIDGE PAYPAL",
//                            PayPalPayment.PAYMENT_INTENT_SALE);
//                    Intent paypalIntent = new Intent(ProductDetailsBuyNeg.this, PaymentActivity.class);
//                    paypalIntent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
//                    startActivityForResult(paypalIntent, AppConstants.REQUEST_PAYPAL_PAYMENT);

                mItemName = mCommonResponse.getResult().getItem_name();
                mItemQuantity = mCommonResponse.getResult().getItem_quantity();
                mItemPrice = mCommonResponse.getResult().getItem_cost();
                mServicePrice = mCommonResponse.getResult().getProcess_fee();
                mTotalPrice = mCommonResponse.getResult().getTotal_cost();


//                    if (mHomeSingleItemEntity.getBuyer_id() != null && !mHomeSingleItemEntity.getBuyer_id().equalsIgnoreCase("")) {
//
//                        callPaypalPayment(mCommonResponse.getResult());
//                    } else {
//                        String mBuyerID = "";
//                        if (mHomeSingleItemEntity.getBuyer_id() != null && !mHomeSingleItemEntity.getBuyer_id().equalsIgnoreCase("")) {
//                            mBuyerID = mHomeSingleItemEntity.getBuyer_id();
//                        } else {
//                            mBuyerID = GlobalMethods.getUserID(NegotiationChatRoom.this);
//                        }
//                        APIRequestHandler.getInstance().getPaypalPaymentResponse(mBuyerID, mHomeSingleItemEntity.getItem_id(), mItemQuantity, mItemPrice, mTotalPrice, "", mServicePrice, AppConstants.SUCCESS_CODE, neg_ID, this);
//                    }

                String buyerID = "";
                if (mHomeSingleItemEntity.getBuyer_id() != null && !mHomeSingleItemEntity.getBuyer_id().equalsIgnoreCase(AppConstants.FAILURE_CODE) && !mHomeSingleItemEntity.getBuyer_id().equalsIgnoreCase("")) {
                    buyerID = mHomeSingleItemEntity.getBuyer_id();
                } else {
                    buyerID = GlobalMethods.getUserID(NegotiationChatRoom.this);
                }


                if (buyerID.equalsIgnoreCase(GlobalMethods.getUserID(NegotiationChatRoom.this))) {
//                    callPaypalPayment(mCommonResponse.getResult());
                    nextScreen(PaymentHomeScreen.class, false);

                } else {
                    APIRequestHandler.getInstance().getPaypalPaymentResponse(buyerID, mHomeSingleItemEntity.getItem_id(), mItemQuantity, mItemPrice, "", mServicePrice, mTotalPrice, AppConstants.SUCCESS_CODE, neg_ID, AppConstants.FAILURE_CODE, "", NegotiationChatRoom.this);


                }

            } else if (mCommonResponse.getResponse_code().equalsIgnoreCase(getString(R.string.two))) {
                mItemName = mCommonResponse.getResult().getItem_name();
                mItemQuantity = mCommonResponse.getResult().getItem_quantity();
                mItemPrice = mCommonResponse.getResult().getItem_cost();
                mServicePrice = mCommonResponse.getResult().getProcess_fee();
                mTotalPrice = mCommonResponse.getResult().getTotal_cost();


                String buyerID = "";
                if (mHomeSingleItemEntity.getBuyer_id() != null && !mHomeSingleItemEntity.getBuyer_id().equalsIgnoreCase(AppConstants.FAILURE_CODE) && !mHomeSingleItemEntity.getBuyer_id().equalsIgnoreCase("")) {
                    buyerID = mHomeSingleItemEntity.getBuyer_id();
                } else {
                    buyerID = GlobalMethods.getUserID(NegotiationChatRoom.this);
                }


                if (buyerID.equalsIgnoreCase(GlobalMethods.getUserID(NegotiationChatRoom.this))) {
//                    callPaypalPayment(mCommonResponse.getResult());

                } else {
                    APIRequestHandler.getInstance().getPaypalPaymentResponse(buyerID, mHomeSingleItemEntity.getItem_id(), mItemQuantity, mItemPrice, "", mServicePrice, mTotalPrice, AppConstants.SUCCESS_CODE, neg_ID, AppConstants.FAILURE_CODE, "", NegotiationChatRoom.this);

                }
            } else {
                if (mNegScreenAlert != null && mNegScreenAlert.isShowing()) {
                    mNegScreenAlert.dismiss();
                }
                mNegScreenAlert = DialogManager.showBasicAlertDialog(this, mCommonResponse.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {

                    }
                });
            }

        }
        if (responseObj instanceof PaypalPayResponse) {
            PaypalPayResponse mPaypalResponse = (PaypalPayResponse) responseObj;

            if (mPaypalResponse.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {


                String buyerID = "";
                if (mHomeSingleItemEntity.getBuyer_id() != null && !mHomeSingleItemEntity.getBuyer_id().equalsIgnoreCase(AppConstants.FAILURE_CODE) && !mHomeSingleItemEntity.getBuyer_id().equalsIgnoreCase("")) {
                    buyerID = mHomeSingleItemEntity.getBuyer_id();
                } else {
                    buyerID = GlobalMethods.getUserID(NegotiationChatRoom.this);
                }

                if (buyerID.equalsIgnoreCase(GlobalMethods.getUserID(NegotiationChatRoom.this)) && mHomeSingleItemEntity.getItem_type()
                        .equalsIgnoreCase(getString(R.string.one))) {

                    //Goods
                    if (mHomeSingleItemEntity.getDelivery_type()
                            .equalsIgnoreCase(getString(R.string.one))) {

                        BuyerCodeScreen.mPayID = mPaypalResponse.getResult().getPayment_id();
                        BuyerCodeScreen.mItemID = AppConstants.PAYPAL_ITEM_ID;

                        AppConstants.CODE_SCREEN = AppConstants.HOME_SCREEN;
                        nextScreen(BuyerCodeScreen.class, true);
                    } else {
                        if (mNegScreenAlert != null && mNegScreenAlert.isShowing()) {
                            mNegScreenAlert.dismiss();
                        }
                        mNegScreenAlert = DialogManager.showBasicAlertDialog(NegotiationChatRoom.this, getString(R.string.electronic_goods_delivery_text), new DialogMangerOkCallback() {

                            @Override
                            public void onOkClick() {


                                RateBuySellScreen.mHeader = getString(R.string.rateseller);
                                AppConstants.RATING_ITEM_ID = AppConstants.PAYPAL_ITEM_ID;
                                AppConstants.RATING_USER_ID = AppConstants.PAYPAL_USER_ID;
                                AppConstants.RATING_USER_NAME = AppConstants.PAYPAL_USER_NAME;

                                AppConstants.RATING_RATE = "";
                                AppConstants.RATING_CMD = "";
                                nextScreen(RateBuySellScreen.class, true);


                            }
                        });
                    }


                } else {
                    //You are seller

                    if (AppConstants.CHAT_BACK.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                        AppConstants.CHAT_BACK = AppConstants.FAILURE_CODE;
                    } else if (AppConstants.FROME_NOTIFICATION.equalsIgnoreCase(getString(R.string.one))) {
                        AppConstants.FROME_NOTIFICATION = AppConstants.FAILURE_CODE;
                    } else if (AppConstants.FROME_NOTIFICATION.equalsIgnoreCase(getString(R.string.two))) {
                        AppConstants.FROME_NOTIFICATION = AppConstants.FAILURE_CODE;
                    } else if (AppConstants.CHAT_BACK.equalsIgnoreCase(AppConstants.HOME_SCREEN)) {
                        AppConstants.CHAT_BACK = AppConstants.FAILURE_CODE;
                    }

                    previousScreen(DashboardScreen.class, true);

                }

            } else {
                if (mNegScreenAlert != null && mNegScreenAlert.isShowing()) {
                    mNegScreenAlert.dismiss();
                }
                mNegScreenAlert = DialogManager.showBasicAlertDialog(NegotiationChatRoom.this, mPaypalResponse.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {

                    }
                });
            }

        }
    }

    private void setHeader() {
        titleTv = (TextView) findViewById(R.id.header_txt);
        titleTv.setText(getString(R.string.negotiation).toUpperCase(Locale.ENGLISH));
        mHeaderLeftImage = (ImageView) findViewById(R.id.header_left_img);
        mHeaderRightImage = (ImageView) findViewById(R.id.header_right_img);
        mHeaderLeftImage.setImageResource(R.drawable.back_img);

        mHeader_right_txt = (TextView) findViewById(R.id.header_right_txt);
        mHeader_right_txt.setVisibility(View.INVISIBLE);
        mHeader_right_txt.setText(getString(R.string.approve));

        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        mHeadeRightBtnLay = (RelativeLayout) findViewById(R.id.header_right_btn_lay);
        mHeaderRightImage.setVisibility(View.GONE);

        mHeadeLeftBtnLay.setVisibility(View.VISIBLE);
        mHeadeRightBtnLay.setVisibility(View.INVISIBLE);
        mDollTxt = (TextView) findViewById(R.id.doll_sym_txt);

        mHeader_right_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String buyerID = "";
                if (mHomeSingleItemEntity.getBuyer_id() != null && !mHomeSingleItemEntity.getBuyer_id().equalsIgnoreCase(AppConstants.FAILURE_CODE) && !mHomeSingleItemEntity.getBuyer_id().equalsIgnoreCase("")) {
                    buyerID = mHomeSingleItemEntity.getBuyer_id();
                } else {
                    buyerID = GlobalMethods.getUserID(NegotiationChatRoom.this);
                }

                neg_ID = mChatResponseList.get(mChatResponseList.size() - 1).getNegotiate_id();
                final String buyID = buyerID;
                String cost = mChatResponseList.get(mChatResponseList.size() - 1).getNegitation_amount().replace("$", "").trim();
                String mServiceTxt = "", mTotalCostTxt = "";
                double pricecost = Float.parseFloat(cost);

                double service_cost = 0.60;
                if (pricecost >= 1 && pricecost < 10) {
                    service_cost = 0.60;
                } else if (pricecost >= 10 && pricecost < 133) {
                    service_cost = 7.50;
                } else if (pricecost >= 133 && pricecost < 200) {
                    service_cost = 10;
                } else if (pricecost >= 200) {
                    service_cost = 5.00;
                }
                if (service_cost == 0.60 || service_cost == 10) {
                    mServiceTxt = GlobalMethods.afterTwoPointVal(String.valueOf(service_cost));
                    mTotalCostTxt = GlobalMethods.afterTwoPointVal(String.valueOf(pricecost + service_cost));
                } else {

                    double serviceTax = (double) ((pricecost * service_cost) / 100);
                    mServiceTxt = GlobalMethods.afterTwoPointVal(String.valueOf(serviceTax));
                    mTotalCostTxt = GlobalMethods.afterTwoPointVal(String.valueOf(pricecost + serviceTax));
                }

                mItemName = mHomeSingleItemEntity.getItem_name();
                mItemQuantity = ItemQuantity;
                mItemPrice = mChatResponseList.get(mChatResponseList.size() - 1).getNegitation_amount();
                mServicePrice = mServiceTxt;
                mTotalPrice = mTotalCostTxt;
                if (buyerID.equalsIgnoreCase(GlobalMethods.getUserID(NegotiationChatRoom.this))) {
                    if (mNegScreenAlert != null && mNegScreenAlert.isShowing()) {
                        mNegScreenAlert.dismiss();
                    }
                    mNegScreenAlert = DialogManager.shownAlertDialogProductPrice(NegotiationChatRoom.this, getString(R.string.app_name), getString(R.string.do_you_want), mChatResponseList.get(mChatResponseList.size() - 1).getNegitation_amount(), new DialogMangerOkCallback() {
                        @Override
                        public void onOkClick() {
//                            approveCall();
//                            APIRequestHandler.getInstance().buyItemResponse(buyID,
//                                    mHomeSingleItemEntity.getItem_id(),
//                                    ItemQuantity, neg_ID, AppConstants.FAILURE_CODE, NegotiationChatRoom.this);


                            if (mBidId.equalsIgnoreCase("")) {

                                AppConstants.PAYPAL_BUY_ID = GlobalMethods.getUserID(NegotiationChatRoom.this);
                                AppConstants.PAYPAL_BID_ID = AppConstants.FAILURE_CODE;

                            } else {
                                if (mHomeSingleItemEntity.getBuyer_id() != null && !mHomeSingleItemEntity.getBuyer_id().equalsIgnoreCase(AppConstants.FAILURE_CODE) && !mHomeSingleItemEntity.getBuyer_id().equalsIgnoreCase("")) {
                                    AppConstants.PAYPAL_BUY_ID = mHomeSingleItemEntity.getBuyer_id();
                                } else {
                                    AppConstants.PAYPAL_BUY_ID = GlobalMethods.getUserID(NegotiationChatRoom.this);
                                }


                                AppConstants.PAYPAL_BID_ID = mBidId;
//                                mHomeSingleItemEntity.setItem_mode(getString(R.string.two));
//                                APIRequestHandler.getInstance().getBidPaypalPaymentResponse(mHomeSingleItemEntity.getItem_id(), mTotalPrice, mBidId, buyerID, paymentId, neg_ID, this);

                            }


                            AppConstants.PAYPAL_ITEM_ID = mHomeSingleItemEntity.getItem_id();

                            AppConstants.PAYPAL_ITEM_QTY = mItemQuantity;
                            AppConstants.PAYPAL_ITEM_COST = mItemPrice;
                            AppConstants.PAYPAL_SER_FEES = mServicePrice;
                            AppConstants.PAYPAL_TOT_COST = mTotalPrice;
                            AppConstants.PAYPAL_NEG = AppConstants.SUCCESS_CODE;
                            AppConstants.PAYPAL_NEG_ID = neg_ID;
                            AppConstants.PAYPAL_TIPS = AppConstants.FAILURE_CODE;
                            AppConstants.PAYPAL_ITEM_DELV_TYPE = mHomeSingleItemEntity.getDelivery_type();
                            AppConstants.PAYPAL_USER_ID = mHomeSingleItemEntity.getUser_id();
                            AppConstants.PAYPAL_USER_NAME = mHomeSingleItemEntity.getUser_first_name();
                            AppConstants.PAYPAL_ITEM_TYPE = mHomeSingleItemEntity.getItem_type();
                            AppConstants.PAYPAL_ITEM_NAME = mHomeSingleItemEntity.getItem_name();

                            nextScreen(PaymentPaypalStripScreen.class, false);


                        }
                    });
                } else {
                    if (mNegScreenAlert != null && mNegScreenAlert.isShowing()) {
                        mNegScreenAlert.dismiss();
                    }

                    mNegScreenAlert = DialogManager.showBaseTwoBtnDialog(NegotiationChatRoom
                            .this, getString(R
                            .string.app_name), getString(R.string.do_you_want), getString(R.string
                            .yes), getString(R.string.no), new DialogManagerTwoBtnCallback() {
                        @Override
                        public void onBtnOkClick(String mOkStr) {

                            if (mBidId.equalsIgnoreCase("")) {
                                APIRequestHandler.getInstance().getPaypalPaymentResponse(buyID, mHomeSingleItemEntity.getItem_id(), mItemQuantity, mItemPrice, "", mServicePrice, mTotalPrice, AppConstants.SUCCESS_CODE, neg_ID, AppConstants.FAILURE_CODE, "", NegotiationChatRoom.this);
                            } else {

                                mHomeSingleItemEntity.setItem_mode(getString(R.string.two));
                                APIRequestHandler.getInstance().getBidPaypalPaymentResponse(mHomeSingleItemEntity.getItem_id(), mTotalPrice, mBidId, buyID, "", neg_ID, "", NegotiationChatRoom.this);

                            }
                        }

                        @Override
                        public void onBtnCancelClick(String mCancelStr) {

                        }
                    });


//                    mNegScreenAlert = DialogManager.showBasicBtnAlertDialog(NegotiationChatRoom.this, getString(R
//                            .string.app_name), getString(R.string.do_you_want), new
//                            DialogMangerOkCallback() {
//                                @Override
//                                public void onOkClick() {
////                                    approveCall();
////                                    APIRequestHandler.getInstance().buyItemResponse(buyID,
////                                            mHomeSingleItemEntity.getItem_id(),
////                                            ItemQuantity, neg_ID, AppConstants.FAILURE_CODE, NegotiationChatRoom.this);
////                                    String cost = mChatResponseList.get(mChatResponseList.size() - 1).getNegitation_amount().replace("$", "").trim();
////                                    String mServiceTxt="",mTotalCostTxt="";
////                                    double pricecost = Float.parseFloat(cost);
////
////                                    double service_cost = 0.60;
////                                    if (pricecost >= 1 && pricecost < 10) {
////                                        service_cost = 0.60;
////                                    } else if (pricecost >= 10 && pricecost < 133) {
////                                        service_cost = 7.50;
////                                    } else if (pricecost >= 133 && pricecost < 200) {
////                                        service_cost = 10;
////                                    } else if (pricecost >= 200) {
////                                        service_cost = 5.00;
////                                    }
////                                    if (service_cost == 0.60 || service_cost == 10) {
////                                        mServiceTxt=GlobalMethods.afterTwoPointVal(String.valueOf(service_cost));
////                                        mTotalCostTxt= GlobalMethods.afterTwoPointVal(String.valueOf(pricecost + service_cost));
////                                    } else {
////
////                                        double serviceTax = (double) ((pricecost * service_cost) / 100);
////                                        mServiceTxt=GlobalMethods.afterTwoPointVal(String.valueOf(serviceTax));
////                                        mTotalCostTxt=GlobalMethods.afterTwoPointVal(String.valueOf(pricecost + serviceTax));
////                                    }
////
////
////                                    mItemName = mHomeSingleItemEntity.getItem_name();
////                                    mItemQuantity = ItemQuantity;
////                                    mItemPrice = mChatResponseList.get(mChatResponseList.size() - 1).getNegitation_amount();
////                                    mServicePrice = mServiceTxt;
////                                    mTotalPrice = mTotalCostTxt;
////                                    APIRequestHandler.getInstance().getPaypalPaymentResponse(buyID, mHomeSingleItemEntity.getItem_id(), mItemQuantity, mItemPrice, mTotalPrice, "", mServicePrice, AppConstants.SUCCESS_CODE, neg_ID,"", NegotiationChatRoom.this);
//
//
//                                    System.out.println("mBidId---" + mBidId);
//                                    System.out.println("mHomeSingleItemEntity.getItem_id()---" + mHomeSingleItemEntity.getItem_id());
//                                    System.out.println("mItemQuantity---" + mItemQuantity);
//                                    System.out.println("mItemPrice---" + mItemPrice);
//                                    System.out.println("mServicePrice---" + mServicePrice);
//                                    System.out.println("mTotalPrice---" + mTotalPrice);
//                                    System.out.println("neg_ID---" + neg_ID);
//                                    System.out.println("buyID---" + buyID);
//                                    if (mBidId.equalsIgnoreCase("")) {
//                                        APIRequestHandler.getInstance().getPaypalPaymentResponse(buyID, mHomeSingleItemEntity.getItem_id(), mItemQuantity, mItemPrice, "", mServicePrice, mTotalPrice, AppConstants.SUCCESS_CODE, neg_ID, AppConstants.FAILURE_CODE, "", NegotiationChatRoom.this);
//                                    } else {
//
//                                        mHomeSingleItemEntity.setItem_mode(getString(R.string.two));
//                                        APIRequestHandler.getInstance().getBidPaypalPaymentResponse(mHomeSingleItemEntity.getItem_id(), mTotalPrice, mBidId, buyID, "", neg_ID, "", NegotiationChatRoom.this);
//
//                                    }
//                                }
//
//
//                            });
                }


            }
        });
    }

    private void approveCall() {
        requestType = 2;
        approveClick = true;
        neg_ID = mChatResponseList.get(mChatResponseList.size() - 1).getNegotiate_id();
        APIRequestHandler.getInstance().approveNegotiateResponse(mChatResponseList.get(mChatResponseList.size() - 1).getNegotiate_id(), mItemId,
                " " + mChatResponseList.get(mChatResponseList.size() - 1).getNegitation_amount(), NegotiationChatRoom.this);


    }

    private void setAdapter(final ArrayList<NegotiateResponseEntity> FinalchatList) {

        last_chat_count = mChatResponseList.size();
        new_chat_count = FinalchatList.size();
        first = mChatListView.getFirstVisiblePosition();


        if (mChatListAdapter == null) {
            mChatResponseList.addAll(FinalchatList);
            mChatListAdapter = new NegotiationChatAdapter(NegotiationChatRoom.this,
                    mChatResponseList);
            mChatListView.setAdapter(mChatListAdapter);

        } else {

            //   final int last= mChatListView.getLastVisiblePosition();
            if (new_chat_count > last_chat_count) {
                runOnUiThread(new Runnable() {
                    public void run() {

                        mChatResponseList.clear();
                        mChatResponseList.addAll(FinalchatList);
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
    public void onBackPressed() {

        if (AppConstants.CHAT_BACK.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
            AppConstants.CHAT_BACK = AppConstants.FAILURE_CODE;
            finishScreen();
        } else if (AppConstants.FROME_NOTIFICATION.equalsIgnoreCase(getString(R.string.one))) {
            AppConstants.FROME_NOTIFICATION = AppConstants.FAILURE_CODE;
            previousScreen(HomeScreenActivity.class, true);

        } else if (AppConstants.FROME_NOTIFICATION.equalsIgnoreCase(getString(R.string.two))) {
            AppConstants.FROME_NOTIFICATION = AppConstants.FAILURE_CODE;
            previousScreen(NotificationScreen.class, true);
        } else if (AppConstants.CHAT_BACK.equalsIgnoreCase(AppConstants.HOME_SCREEN)) {
            AppConstants.CHAT_BACK = AppConstants.FAILURE_CODE;
            previousScreen(HomeScreenActivity.class, true);
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

        }
    }

//    private void callPaypalPayment(BuyItemEntityResponse paypalRes) {
//
//        mItemName = paypalRes.getItem_name();
//        mItemQuantity = paypalRes.getItem_quantity();
//        mItemPrice = paypalRes.getItem_cost();
//        mServicePrice = paypalRes.getProcess_fee();
//        mTotalPrice = paypalRes.getTotal_cost();
//
//        PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(mTotalPrice), "USD", GlobalMethods.isGoodsService(this, mHomeSingleItemEntity.getItem_type()) + " : " + mHomeSingleItemEntity.getItem_name(),
//                PayPalPayment.PAYMENT_INTENT_SALE);
//        Intent paypalIntent = new Intent(NegotiationChatRoom.this, PaymentActivity.class);
//        paypalIntent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
//        startActivityForResult(paypalIntent, AppConstants.REQUEST_PAYPAL_PAYMENT);
//    }
//
//    private void callPaypalPayment() {
//
//
//        PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(mTotalPrice), "USD", getString(R.string.item_name) + " " + mHomeSingleItemEntity.getItem_name(),
//                PayPalPayment.PAYMENT_INTENT_SALE);
//        Intent paypalIntent = new Intent(NegotiationChatRoom.this, PaymentActivity.class);
//        paypalIntent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
//        startActivityForResult(paypalIntent, AppConstants.REQUEST_PAYPAL_PAYMENT);
//    }


}
