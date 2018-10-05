package com.bridgellc.bridge.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.adapter.NotificationAdapter;
import com.bridgellc.bridge.adapter.RecentActivityAdapter;
import com.bridgellc.bridge.apiinterface.APIRequestHandler;
import com.bridgellc.bridge.entity.HomeSingleItemEntity;
import com.bridgellc.bridge.entity.PaymentBuySellEntity;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.model.CommonModelResponse;
import com.bridgellc.bridge.model.CommonResponse;
import com.bridgellc.bridge.model.ItemDetailResponse;
import com.bridgellc.bridge.model.NotificationEntityResponse;
import com.bridgellc.bridge.model.PaymentHistoryResponse;
import com.bridgellc.bridge.model.PaypalPayResponse;
import com.bridgellc.bridge.swipemenu.SwipeMenu;
import com.bridgellc.bridge.swipemenu.SwipeMenuCreator;
import com.bridgellc.bridge.swipemenu.SwipeMenuItem;
import com.bridgellc.bridge.swipemenu.SwipeMenuListView;
import com.bridgellc.bridge.ui.home.HomeScreenActivity;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.GlobalMethods;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 * Created by admin on 4/30/2016.
 */
public class NotificationScreen extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private TextView noNotificationTxt, mAllTxt, mPaymentActTxt, mHeaderRightTxt;

    private ImageView mHeaderRightImage;
    private OnTouchListener mListener;
    private SwipeMenuListView mNotificationList;
    private NotificationAdapter mNotificationAdapter;
    private RecentActivityAdapter mPayActAdapter;
    private ArrayList<HomeSingleItemEntity> mNotifitResList;
    private HomeSingleItemEntity mItemDetailsRes;
    public static HomeSingleItemEntity mFromNotiItemDetailsRes = new HomeSingleItemEntity();
    private String type = "", notifyID = "";
    private String mItemID = "", mBidId = "", mItemName = "", mItemQuantity = "", mItemPrice = "", mServicePrice = "", mTotalPrice = "";

    private ListView mPaymentActList;
    public static int mNotifyVisible = 1;
    private ArrayList<PaymentBuySellEntity> mPaymenyDone;
    private ArrayList<PaymentBuySellEntity> mPaymenyEscrow;
    private ArrayList<PaymentBuySellEntity> mPaymenyReceived;
    private ArrayList<PaymentBuySellEntity> tempPaymenyDone;
    private ArrayList<PaymentBuySellEntity> tempPaymenyEscrow;
    private ArrayList<PaymentBuySellEntity> tempPaymenyReceived;
    private ArrayList<PaymentBuySellEntity> AllmPayment;
    private String mPay = "", mNotifitCount = "", mbuyerID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.notification_sreen);
        mNotifitResList = new ArrayList<HomeSingleItemEntity>();

        mPaymenyDone = new ArrayList<PaymentBuySellEntity>();
        mPaymenyEscrow = new ArrayList<PaymentBuySellEntity>();
        mPaymenyReceived = new ArrayList<PaymentBuySellEntity>();

        tempPaymenyDone = new ArrayList<PaymentBuySellEntity>();
        tempPaymenyEscrow = new ArrayList<PaymentBuySellEntity>();
        tempPaymenyReceived = new ArrayList<PaymentBuySellEntity>();

        AllmPayment = new ArrayList<PaymentBuySellEntity>();

//        Intent intent = new Intent(this, PayPalService.class);
//        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, AppConstants.CONFIG);
//        startService(intent);
        mActivity = this;

        initComponents();

    }


    private void initComponents() {
        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);

        ArrayList<String> m = new ArrayList<>();

        TouchableWrapper frameLayout = new TouchableWrapper(this);
        frameLayout.setBackgroundColor(getResources().getColor(
                android.R.color.transparent));

        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        mHeadeRightBtnLay = (RelativeLayout) findViewById(R.id.header_right_btn_lay2);
        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mHeaderTxt.setText(getString(R.string.notification).toUpperCase(Locale.ENGLISH));
        mHeaderRightTxt = (TextView) findViewById(R.id.header_right_green_txt);

//        mHeaderRightImage = (ImageView) findViewById(R.id.header_right_img);
//        mHeaderRightImage.setImageResource(R.drawable.delete_icon);


        noNotificationTxt = (TextView) findViewById(R.id.no_notification_txt);
        mAllTxt = (TextView) findViewById(R.id.all_txt);
        mPaymentActTxt = (TextView) findViewById(R.id.payment_txt);

        mHeadeLeftBtnLay.setVisibility(View.VISIBLE);
        mHeadeRightBtnLay.setVisibility(View.VISIBLE);

        mNotificationList = (SwipeMenuListView)
                findViewById(R.id.notification_list);
        mPaymentActList = (ListView) findViewById(R.id.payment_activity_list);

        mNotificationList.setOnItemClickListener(this);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem sendmeReq = new SwipeMenuItem(NotificationScreen.this);
                sendmeReq.setBackground(new ColorDrawable(getResources()
                        .getColor(R.color.swipe_red)));
                sendmeReq.setWidth(250);
                sendmeReq.setTitle("Remove");
                sendmeReq.setTitleSize(14);
                sendmeReq.setTitleColor(getResources().getColor(R.color.white));
                menu.addMenuItem(sendmeReq);

            }
        };

        mNotificationList.setMenuCreator(creator);
        mNotificationList
                .setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(final int position,
                                                   SwipeMenu menu, int index) {

                        if (index == 0) {
//                            Delete All API Call
                            DialogManager.showBasicAlertDialog(NotificationScreen.this,
                                    getString(R.string.do_you_want_clr), new DialogMangerOkCallback() {
                                        @Override
                                        public void onOkClick() {
                                            APIRequestHandler.getInstance()
                                                    .deleteNotificationResponse(
                                                            mNotifitResList.get(position)
                                                                    .getNotification_id(), "", NotificationScreen.this);
                                        }
                                    });


                        }

                        return false;
                    }
                });
        mNotificationList.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        callNotificationAPI();

    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);
        if (responseObj instanceof NotificationEntityResponse) {
            NotificationEntityResponse mNeg = (NotificationEntityResponse) responseObj;
            if (mNeg.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                mNotifitResList.clear();

                if (!mNeg.getResult().getNotification().isEmpty()) {
                    mHeadeRightBtnLay.setVisibility(View.VISIBLE);
                    mNotificationList.setVisibility(View.VISIBLE);
                    noNotificationTxt.setVisibility(View.GONE);

                    mNotifitResList = mNeg.getResult().getNotification();
                    mNotifitCount = mNeg.getResult().getCount();
                    adpsetDate(true);

                    if (AppConstants.PAYPAL_NOTIFY.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                        AppConstants.PAYPAL_NOTIFY = AppConstants.FAILURE_CODE;
                        if (mFromNotiItemDetailsRes.getAmount_received() != null && !mFromNotiItemDetailsRes.getAmount_received().equalsIgnoreCase("")&& !mFromNotiItemDetailsRes.getAmount_received().equalsIgnoreCase(AppConstants.FAILURE_CODE)&&!mFromNotiItemDetailsRes.getAmount_received().equalsIgnoreCase(GlobalMethods.afterTwoPointVal(AppConstants.FAILURE_CODE))) {
                            callPaypalPayment(mFromNotiItemDetailsRes);
                        }
                    }

                    APIRequestHandler.getInstance().getMyPaymentHistoryResponse(this);

                } else {
                    mHeadeRightBtnLay.setVisibility(View.INVISIBLE);
                    mNotificationList.setVisibility(View.GONE);
                    noNotificationTxt.setVisibility(View.VISIBLE);
                }
            } else {
                DialogManager.showBasicAlertDialog(this, mNeg.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {

                    }
                });
            }
        }
        if (responseObj instanceof CommonModelResponse) {
            CommonModelResponse mDeleteRes = (CommonModelResponse) responseObj;
            if (mDeleteRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                callNotificationAPI();
            } else {
                DialogManager.showBasicAlertDialog(this, mDeleteRes.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {

                    }
                });
            }
        }
        if (responseObj instanceof ItemDetailResponse) {

            ItemDetailResponse itemRes = (ItemDetailResponse) responseObj;
            if (itemRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                mItemDetailsRes = itemRes.getResult();
//                movetoNextPage();
                APIRequestHandler.getInstance().getNotificationReadResponse(notifyID, NotificationScreen.this);
            } else {
                DialogManager.showBasicAlertDialog(this, itemRes.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {
                        nextScreen(HomeScreenActivity.class, true);

                    }
                });
            }

        }
        if (responseObj instanceof CommonResponse) {
            CommonResponse itemRes = (CommonResponse) responseObj;
            if (itemRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                movetoNextPage();
            } else {
                DialogManager.showBasicAlertDialog(this, itemRes.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {

                    }
                });
            }

        }
        if (responseObj instanceof PaypalPayResponse) {
            PaypalPayResponse mPaypalResponse = (PaypalPayResponse) responseObj;

            if (mPaypalResponse.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                DialogManager.showBasicAlertDialog(NotificationScreen.this, mPaypalResponse.getResult().getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {
//                        previousScreen(HomeScreenActivity.class, true);
                        previousScreen(DashboardScreen.class, true);

                    }
                });

            } else {
                DialogManager.showBasicAlertDialog(NotificationScreen.this, mPaypalResponse.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {

                    }
                });
            }

        }
        if (responseObj instanceof PaymentHistoryResponse) {
            PaymentHistoryResponse profileres = (PaymentHistoryResponse) responseObj;
            if (profileres.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                //IniData
                setPaymentActData(profileres);
            } else {
                DialogManager.showBasicAlertDialog(this,
                        profileres.getMessage(), new DialogMangerOkCallback() {
                            @Override
                            public void onOkClick() {

                            }
                        });
            }
        }
    }

    private void callNotificationAPI() {
        APIRequestHandler.getInstance().getNotificationResponse(this);
    }

    public void setListener(OnTouchListener listener) {
        mListener = listener;
    }


    public interface OnTouchListener {
        public abstract void onTouch();
    }

    public class TouchableWrapper extends FrameLayout {

        public TouchableWrapper(Context context) {
            super(context);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mListener.onTouch();
                    break;
                case MotionEvent.ACTION_UP:
                    mListener.onTouch();
                    break;
            }
            return super.dispatchTouchEvent(event);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.header_right_btn_lay:
//
//                DialogManager.showBasicBtnAlertDialog(this, getString(R.string.app_name), "Do you want to clear all the notifications ?", new DialogMangerOkCallback() {
//                    @Override
//                    public void onOkClick() {
//                        APIRequestHandler.getInstance()
//                                .deleteNotificationResponse("", getString(R.string.all).toLowerCase(), NotificationScreen.this);
//
//          }
//                });

                break;
            case R.id.all_txt:
                adpsetDate(true);
                break;
            case R.id.payment_txt:
                adpsetDate(false);
                break;
            case R.id.add_view:
                Intent referInt = new Intent(mActivity, ReferalScreen.class);
                mActivity.startActivity(referInt);
//                GlobalMethods.shareTxt(this);
                break;

        }
    }

    private void setPaymentActData(PaymentHistoryResponse mPayResponse) {
        mPaymenyDone.clear();
        mPaymenyEscrow.clear();
        mPaymenyReceived.clear();

        tempPaymenyDone.clear();
        tempPaymenyEscrow.clear();
        tempPaymenyReceived.clear();

        tempPaymenyDone = mPayResponse.getResult().getBuy();
        tempPaymenyEscrow = mPayResponse.getResult().getEscrow();
        tempPaymenyReceived = mPayResponse.getResult().getSell();


//        String received = mPayResponse.getResult().getPayment_received();
        String to_be_received = mPayResponse.getResult().getPayment_to_be_received();
//        String sent = mPayResponse.getResult().getPayment_sent();
        String to_be_send = mPayResponse.getResult().getPayment_to_be_sent();

        Double amt = Double.parseDouble(to_be_received) - Double.parseDouble(to_be_send);

        mPay = amt + "";
        for (int i = 0; i < tempPaymenyDone.size(); i++) {

            PaymentBuySellEntity obj = tempPaymenyDone.get(i);
            String str = getString(R.string.zero);
            obj.setEscrow(str);
            mPaymenyDone.add(obj);

        }


        for (int i = 0; i < tempPaymenyEscrow.size(); i++) {

            PaymentBuySellEntity obj = tempPaymenyEscrow.get(i);


            String str = getString(R.string.one);
            obj.setEscrow(str);
            mPaymenyEscrow.add(obj);

        }

        for (int i = 0; i < tempPaymenyReceived.size(); i++) {

            PaymentBuySellEntity obj = tempPaymenyReceived.get(i);


            String str = getString(R.string.zero);
            obj.setEscrow(str);
            mPaymenyReceived.add(obj);

        }

        AllmPayment.clear();
        AllmPayment.addAll(mPaymenyDone);
        AllmPayment.addAll(mPaymenyEscrow);
        AllmPayment.addAll(mPaymenyReceived);
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        Collections.sort(AllmPayment, new Comparator<PaymentBuySellEntity>() {
            @Override
            public int compare(PaymentBuySellEntity ent2, PaymentBuySellEntity ent1) {
                Date d = new Date();
                Date d1 = new Date();
                try {
                    d = simpleDateFormat.parse(ent2.getDate_time());
                    d1 = simpleDateFormat.parse(ent1.getDate_time());
                } catch (Exception e) {

                }
                return d.after(d1) ? -1 : 1;
            }
        });
    }


    private void adpsetDate(boolean notificationlist) {

        if (notificationlist) {
            mNotifyVisible = 1;

            mNotificationList.setVisibility(View.VISIBLE);
            mPaymentActList.setVisibility(View.GONE);

            mAllTxt.setBackground(getResources().getDrawable(R.drawable.green_with_rounded_pg));
            mPaymentActTxt.setBackground(null);

            mAllTxt.setTextColor(getResources().getColor(R.color.white));
            mPaymentActTxt.setTextColor(getResources().getColor(R.color.blue_gray));

//            mHeaderRightTxt.setText();

            mHeaderRightTxt.setText(mNotifitCount);
            mNotificationAdapter = new NotificationAdapter(this, mNotifitResList);
            mNotificationList.setAdapter(mNotificationAdapter);


        } else {
            mNotifyVisible = 2;

            mNotificationList.setVisibility(View.GONE);
            mPaymentActList.setVisibility(View.VISIBLE);

            mAllTxt.setBackground(null);
            mPaymentActTxt.setBackground(getResources().getDrawable(R.drawable.green_with_rounded_pg));

            mAllTxt.setTextColor(getResources().getColor(R.color.blue_gray));
            mPaymentActTxt.setTextColor(getResources().getColor(R.color.white));

//            mHeaderRightTxt.setText(getString(R.string.dollar_sym) + " " + GlobalMethods.getPriValWithTwoPoint(mPay, false));
            mHeaderRightTxt.setText(getString(R.string.dollar_sym) + " " + mPay);
            mPayActAdapter = new RecentActivityAdapter(this, AllmPayment);
            mPaymentActList.setAdapter(mPayActAdapter);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        type = mNotifitResList.get(position).getApifrom();
        notifyID = mNotifitResList.get(position).getNotification_id();
//        APIRequestHandler.getInstance().getItemDetailsResponse(mNotifitResList.get(position).getTypeid(), mNotifitResList.get(position).getUser_id(), mNotifitResList.get(position).getPayment_id(), mNotifitResList.get(position).getNotification_id(), this);
        APIRequestHandler.getInstance().getItemDetailsResponse(mNotifitResList.get(position).getTypeid(), mNotifitResList.get(position).getUser_id(), mNotifitResList.get(position).getPayment_id(), "", this);
    }

    @Override
    public void onBackPressed() {

//        finishScreen();
        previousScreen(HomeScreenActivity.class, true);

    }

    private void movetoNextPage() {

        if (mItemDetailsRes.getComplete().equalsIgnoreCase(mActivity.getString(R.string.zero)) || type.equalsIgnoreCase("Bank")) {
            switch (type) {
                case "Negotiation":
                    ProductDetailsScreen.mFooterBtnCount = 1;
                    ProductDetailsScreen.mFooterOneTxt = mActivity.getString(R.string.negotiation);

                    String otherUserId = "";

                    if (mItemDetailsRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller
                        otherUserId = mItemDetailsRes.getBuyer_id();
                    } else {
                        //I am Buyer
                        otherUserId = mItemDetailsRes.getUser_id();

                    }

//                ProductDetailsScreen.mHomeSingleItemEntity = mItemDetasRes;


                    NegotiationChatRoom.mHomeSingleItemEntity = mItemDetailsRes;
                    AppConstants.CHAT_BACK = AppConstants.FAILURE_CODE;
                    AppConstants.NEGO_FRIEND_ID = otherUserId;
                    AppConstants.NEGO_USER_ID = GlobalMethods.getUserID(this);
                    AppConstants.NEGO_ITEM_ID = mItemDetailsRes.getItem_id();
                    AppConstants.NEGO_ITEM_NOTI = mActivity.getString(R.string.one);
                    AppConstants.NEGO_ITEM_QTY = mItemDetailsRes.getPurchase_quantity();

                    if (mItemDetailsRes.getBid_id() != null && !mItemDetailsRes.getBid_id().equalsIgnoreCase("") && !mItemDetailsRes.getBid_id().equalsIgnoreCase(getString(R.string.zero))) {
                        AppConstants.NEGO_BID_ID = mItemDetailsRes.getBid_id();
                        NegotiationChatRoom.mHomeSingleItemEntity.setBuyer_id(mItemDetailsRes.getUser_id());

                    } else {
                        AppConstants.NEGO_BID_ID = "";
                    }

                    AppConstants.FROME_NOTIFICATION = getString(R.string.two);

                    nextScreen(NegotiationChatRoom.class, true);
                    break;

                case "Negotiation Approve":
                    if (mItemDetailsRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller
                        sellBuyApprovList(mItemDetailsRes);
                    } else {
                        //I am Buyer
                        buyerOrdList(mItemDetailsRes);

                    }
                    AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.two);

                    ProductDetailsScreen.mHomeSingleItemEntity = mItemDetailsRes;

                    nextScreen(ProductDetailsScreen.class, true);

                    break;
                case "Buy":
                    if (mItemDetailsRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller
                        sellBuyApprovList(mItemDetailsRes);
                    } else {
                        //I am Buyer
                        buyerOrdList(mItemDetailsRes);

                    }
                    AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.two);
                    ProductDetailsScreen.mHomeSingleItemEntity = mItemDetailsRes;

                    nextScreen(ProductDetailsScreen.class, true);


                    break;
                case "Receive Amount":
//                if (message.equalsIgnoreCase("You will receive amount shotly") || message.equalsIgnoreCase("Your transaction has been done. You will receive the amount shortly")) {
//                    if (mItemDetasRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
//                        //I am Seller
//                        sellBuyApprovList(mItemDetasRes);
//                    } else {
//                        //I am Buyer
//                        buyerOrdList(mItemDetasRes);
//
//                    }


                    ProductDetailsScreen.mFooterBtnCount = 2;
                    ProductDetailsScreen.mFooterOneTxt = mActivity.getString(R.string.chat);
                    ProductDetailsScreen.mFooterTwoTxt = mActivity.getString(R.string.rating);
                    if (mItemDetailsRes.getItem_type().equalsIgnoreCase(mActivity.getString(R.string.one)) && mItemDetailsRes.getDelivery_type().equalsIgnoreCase(mActivity.getString(R.string.two))) {
                        ProductDetailsScreen.mFooterBtnCount = 1;
                        ProductDetailsScreen.mFooterOneTxt = mActivity.getString(R.string.rating);
                    }
                    ProductDetailsScreen.mHomeSingleItemEntity = mItemDetailsRes;
                    AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.two);

                    nextScreen(ProductDetailsScreen.class, true);

//                }
                    break;


                case "Receive Amount Goods":
                    if (mItemDetailsRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller
                        sellBuyApprovList(mItemDetailsRes);
                    } else {
                        //I am Buyer
                        buyerOrdList(mItemDetailsRes);

                    }
                    ProductDetailsScreen.mHomeSingleItemEntity = mItemDetailsRes;
                    AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.two);

                    nextScreen(ProductDetailsScreen.class, true);

                    break;
                case "Receive Amount Service":
                    if (mItemDetailsRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller
                        sellBuyApprovList(mItemDetailsRes);
                    } else {
                        //I am Buyer
                        buyerOrdList(mItemDetailsRes);

                    }
                    ProductDetailsScreen.mHomeSingleItemEntity = mItemDetailsRes;
                    AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.two);

                    nextScreen(ProductDetailsScreen.class, true);

                    break;
                case "Approve Preview":
                    if (mItemDetailsRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller
                        sellBuyApprovList(mItemDetailsRes);
                    } else {
                        //I am Buyer
                        buyerOrdList(mItemDetailsRes);

                    }
                    ProductDetailsScreen.mHomeSingleItemEntity = mItemDetailsRes;
                    AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.two);

                    nextScreen(ProductDetailsScreen.class, true);

                    break;
                case "Upload Preview":
                    if (mItemDetailsRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller
                        sellBuyApprovList(mItemDetailsRes);
                    } else {
                        //I am Buyer
                        buyerOrdList(mItemDetailsRes);

                    }
                    ProductDetailsScreen.mHomeSingleItemEntity = mItemDetailsRes;
                    AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.two);

                    nextScreen(ProductDetailsScreen.class, true);

                    break;
                case "Upload File":

                    ProductDetailsScreen.mFooterBtnCount = 2;
                    ProductDetailsScreen.mFooterOneTxt = mActivity.getString(R.string.chat);
                    ProductDetailsScreen.mFooterTwoTxt = mActivity.getString(R.string.rating);


                    if (mItemDetailsRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller

                    } else {
                        //I am Buyer

                    }
                    if (mItemDetailsRes.getItem_type().equalsIgnoreCase(mActivity.getString(R.string.one)) && mItemDetailsRes.getDelivery_type().equalsIgnoreCase(mActivity.getString(R.string.two))) {
                        ProductDetailsScreen.mFooterBtnCount = 1;
                        ProductDetailsScreen.mFooterOneTxt = mActivity.getString(R.string.rating);
                    }

                    ProductDetailsScreen.mHomeSingleItemEntity = mItemDetailsRes;
                    AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.two);

                    nextScreen(ProductDetailsScreen.class, true);

                    break;
                case "Service Start":
                    if (mItemDetailsRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller
                        sellBuyApprovList(mItemDetailsRes);
                    } else {
                        //I am Buyer
                        buyerOrdList(mItemDetailsRes);

                    }

                    ProductDetailsScreen.mHomeSingleItemEntity = mItemDetailsRes;
                    AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.two);
                    nextScreen(ProductDetailsScreen.class, true);


                    break;
                case "Chat":
                    ProductDetailsScreen.mFooterBtnCount = 1;
                    ProductDetailsScreen.mFooterOneTxt = mActivity.getString(R.string.chat);
                    if (mItemDetailsRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller
                        sellBuyApprovList(mItemDetailsRes);
                        AppConstants.CHAT_FRIEND_ID = mItemDetailsRes.getBuyer_id();
                    } else {
//                    I am Buyer
                        buyerOrdList(mItemDetailsRes);
                        AppConstants.CHAT_FRIEND_ID = mItemDetailsRes.getUser_id();

                    }
                    ProductDetailsScreen.mHomeSingleItemEntity = mItemDetailsRes;

                    AppConstants.CHAT_ITEM_ID = mItemDetailsRes.getItem_id();

                    AppConstants.FROME_NOTIFICATION = getString(R.string.two);
                    ChatScreen.isSend = true;
                    nextScreen(ChatScreen.class, true);

                    break;
                case "Rating":
                    if (mItemDetailsRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller
                    } else {
                        //I am Buyer

                    }

                    ProductDetailsScreen.mHomeSingleItemEntity = mItemDetailsRes;
                    AppConstants.FROME_NOTIFICATION = getString(R.string.two);

                    ReviewScreen.mOtherUserId = GlobalMethods.getUserID(mActivity);

                    nextScreen(ReviewScreen.class, true);

                    break;

                case "Bid Approve":
                    if (mItemDetailsRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller
                        sellBuyApprovList(mItemDetailsRes);
                    } else {
                        //I am Buyer
                        buyerOrdList(mItemDetailsRes);

                    }

                    ProductDetailsScreen.mHomeSingleItemEntity = mItemDetailsRes;
//                nextScreen(ProductDetailsScreen.class, true);

                    AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.two);

                    nextScreen(ProductDetailsScreen.class, true);

                    break;
                case "Bid":
                    if (mItemDetailsRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller
                        sellBuyApprovList(mItemDetailsRes);
                    } else {
                        //I am Buyer
                        buyerOrdList(mItemDetailsRes);

                    }

                    RequestBiddingScreen.mItemId = mItemDetailsRes.getItem_id();
                    AppConstants.FROME_NOTIFICATION = getString(R.string.two);
                    nextScreen(RequestBiddingScreen.class, true);

                    break;
                case "Bid Reject":
                case "Item Purchased":
                case "Reject Offer":
                    callNotificationAPI();
                    break;
                case "Bank":
//                    if (mItemDetasRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
//                        //I am Seller
//                        sellBuyerApprovList(mItemDetasRes);
//                    } else {
//                        //I am Buyer
//                        buyerOrderList(mItemDetasRes);
//
//                    }

//                    RequestBiddingScreen.mItemId = item_id;
                    AppConstants.BANK_ACC_DET_BACK_SCREEN = mActivity.getString(R.string.nine);
                    nextScreen(PaymentHomeScreen.class, true);
                    break;
                case "Offer Negotiation Approve Paypal":
                case "Negotiation Approve Paypal":
                        if (mItemDetailsRes.getAmount_received() != null && !mItemDetailsRes.getAmount_received().equalsIgnoreCase("")&& !mItemDetailsRes.getAmount_received().equalsIgnoreCase(AppConstants.FAILURE_CODE)&&!mItemDetailsRes.getAmount_received().equalsIgnoreCase(GlobalMethods.afterTwoPointVal(AppConstants.FAILURE_CODE))) {

//                        if (mItemDetailsRes.getSeller_partner() != null && mItemDetailsRes.getSeller_partner().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
//                            DialogManager.showEditBtnAlertDialog(NotificationScreen.this, getString(R.string.app_name), getString(R.string.enter_amt_tip), getString(R.string.cont), getString(R.string.ignore), new DialogManagerTwoBtnCallback() {
//                                @Override
//                                public void onBtnOkClick(String mOkStr) {
//                                    mTipsAmt = mOkStr;
//                                    callPaypalPayment(mItemDetailsRes);
//
//                                }
//
//                                @Override
//                                public void onBtnCancelClick(String mCancelStr) {
//                                    mTipsAmt = mCancelStr;
//                                    callPaypalPayment(mItemDetailsRes);
//
//                                }
//                            });
//                        } else {
                        callPaypalPayment(mItemDetailsRes);
//                        }
                    }
                    break;

//                case "Item Purchased":
//                    callNotificationAPI();
//                    break;
                case "Refund":
                    if (mItemDetailsRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller
                        sellBuyApprovList(mItemDetailsRes);
                    } else {
                        //I am Buyer
                        buyerOrdList(mItemDetailsRes);

                    }

                    ProductDetailsScreen.mHomeSingleItemEntity = mItemDetailsRes;
                    AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.two);
                    nextScreen(ProductDetailsScreen.class, true);

                    break;
            }
        } else {
            ProductDetailsScreen.mHomeSingleItemEntity = mItemDetailsRes;

            ProductDetailsScreen.mFooterBtnCount = 2;
            ProductDetailsScreen.mFooterOneTxt = mActivity.getString(R.string.chat);
            ProductDetailsScreen.mFooterTwoTxt = mActivity.getString(R.string.rating);
            AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.two);
            if (mItemDetailsRes.getItem_type().equalsIgnoreCase(mActivity.getString(R.string.one)) && mItemDetailsRes.getDelivery_type().equalsIgnoreCase(mActivity.getString(R.string.two))) {
                ProductDetailsScreen.mFooterBtnCount = 1;
                ProductDetailsScreen.mFooterOneTxt = mActivity.getString(R.string.rating);
            }
            nextScreen(ProductDetailsScreen.class, true);


        }
    }

    private void sellBuyApprovList(HomeSingleItemEntity homeSingleItemEntity) {
        ProductDetailsScreen.mFooterOneTxt = mActivity.getString(R.string.chat);
        ProductDetailsScreen.mFooterTwoTxt = mActivity.getString(R.string.finish);
        ProductDetailsScreen.mFooterBtnCount = 2;
        if (homeSingleItemEntity.getItem_type().equalsIgnoreCase(mActivity.getString(R.string.one)) && (homeSingleItemEntity.getDelivery_type().equalsIgnoreCase(mActivity.getString(R.string.two)))) {

            ProductDetailsScreen.mFooterTwoTxt = mActivity.getString(R.string.rating);
        }
        if (homeSingleItemEntity.getItem_type().equalsIgnoreCase(mActivity.getString(R.string.two))) {
            if (homeSingleItemEntity.getDelivery_type().equalsIgnoreCase(mActivity.getString(R.string.one))) {

            } else {
                ProductDetailsScreen.mFooterTwoTxt = mActivity.getString(R.string.upload_txt);
            }
        }
    }


    private void buyerOrdList(HomeSingleItemEntity buypost) {
        ProductDetailsScreen.mFooterOneTxt = mActivity.getString(R.string.chat);
        ProductDetailsScreen.mFooterTwoTxt = mActivity.getString(R.string.code);
        ProductDetailsScreen.mFooterThreeTxt = mActivity.getString(R.string.unsatis);
        ProductDetailsScreen.mFooterBtnCount = 3;
        if (buypost.getItem_type().equalsIgnoreCase(mActivity.getString(R.string.one)) && buypost.getDelivery_type().equalsIgnoreCase(mActivity.getString(R.string.two))) {

            ProductDetailsScreen.mFooterBtnCount = 2;
            ProductDetailsScreen.mFooterTwoTxt = mActivity.getString(R.string.rating);
            if (buypost.getItem_type().equalsIgnoreCase(mActivity.getString(R.string.one)) && buypost.getDelivery_type().equalsIgnoreCase(mActivity.getString(R.string.two))) {
                ProductDetailsScreen.mFooterBtnCount = 1;
                ProductDetailsScreen.mFooterOneTxt = mActivity.getString(R.string.rating);
            }

        }

        if (buypost.getItem_type().equalsIgnoreCase(mActivity.getString(R.string.two))) {
            if (buypost.getDelivery_type().equalsIgnoreCase(mActivity.getString(R.string.one))) {
                ProductDetailsScreen.mFooterBtnCount = 2;
                ProductDetailsScreen.mFooterTwoTxt = mActivity.getString(R.string.start);
            } else {

                ProductDetailsScreen.mFooterTwoTxt = mActivity.getString(R.string.preview);
                ProductDetailsScreen.mFooterThreeTxt = mActivity.getString(R.string.approve);
            }
        }
    }


    private void callPaypalPayment(HomeSingleItemEntity paypalRes) {
        mItemID = paypalRes.getItem_id();
        mItemName = paypalRes.getItem_name();
        mItemQuantity = paypalRes.getPurchase_quantity();
        mItemPrice = paypalRes.getItem_cost();
        mServicePrice = paypalRes.getProcessing_fee();
//        mTotalPrice = paypalRes.getTotal_cost();
        mTotalPrice = paypalRes.getAmount_received();

        if (paypalRes.getBid_id() != null && !paypalRes.getBid_id().equalsIgnoreCase("") && !paypalRes.getBid_id().equalsIgnoreCase(getString(R.string.zero))) {

            mBidId = paypalRes.getBid_id();

        } else {
            mBidId = "";

        }

        mbuyerID = GlobalMethods.getUserID(this);

//        if (paypalRes.getUser_id() != null
//                &&
//                !paypalRes.getUser_id().equalsIgnoreCase(getString(R.string.zero))
//                && !paypalRes.getUser_id().equalsIgnoreCase("")) {
//            mbuyerID = paypalRes.getUser_id();
//
//            System.out.println("Before Buyer ID---" + mbuyerID);
//        } else {
//            mbuyerID = GlobalMethods.getUserID(this);
//
//            System.out.println("After Buyer ID---" + mbuyerID);
//        }


//        String ="";


//        String cost = mTotalPrice.replace("$", "").trim();
//        double pricecost = Float.parseFloat(cost);
//
//        double service_cost = 0.60;
//        if (pricecost >= 1.0 && pricecost < 10.0) {
//            service_cost = 0.60;
//        } else if (pricecost >= 10 && pricecost < 133) {
//            service_cost = 7.50;
//        } else if (pricecost >= 133) {
//            service_cost = 10;
//        }
//        if (service_cost == 0.60 || service_cost == 10) {
//            mTot=GlobalMethods.getPriValWithTwoPoint(String.format("%.02f", (pricecost + service_cost)));
//        } else {
//
//            double serviceTax = (double) (pricecost * (service_cost / 100.0f));
//            mTot=GlobalMethods.getPriValWithTwoPoint(String.format("%.2f", (pricecost + serviceTax)));
//
//        }
        String mTot = GlobalMethods.afterTwoPointVal(String.valueOf(Double.valueOf(mServicePrice) + Double.valueOf(mTotalPrice)));
        AppConstants.PAYPAL_BUY_ID = mbuyerID;
        AppConstants.PAYPAL_BID_ID = mBidId;

        AppConstants.PAYPAL_ITEM_ID = mItemID;

        AppConstants.PAYPAL_ITEM_QTY = mItemQuantity;
        AppConstants.PAYPAL_ITEM_COST = mItemPrice;
        AppConstants.PAYPAL_SER_FEES = mServicePrice;
        AppConstants.PAYPAL_TOT_COST = mTot;
        AppConstants.PAYPAL_NEG = AppConstants.FAILURE_CODE;
        AppConstants.PAYPAL_NEG_ID = "";
        AppConstants.PAYPAL_TIPS = AppConstants.FAILURE_CODE;
        AppConstants.PAYPAL_ITEM_DELV_TYPE = paypalRes.getDelivery_type();
        AppConstants.PAYPAL_USER_ID = paypalRes.getUser_id();
        AppConstants.PAYPAL_USER_NAME = paypalRes.getUser_first_name();
        AppConstants.PAYPAL_ITEM_TYPE = paypalRes.getItem_type();
        AppConstants.PAYPAL_ITEM_NAME = paypalRes.getItem_name();

        nextScreen(PaymentPaypalStripScreen.class, false);


//        PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(mTot), "USD", GlobalMethods.isGoodsService(this, paypalRes.getItem_type()) + " : " + paypalRes.getItem_name(),
//                PayPalPayment.PAYMENT_INTENT_SALE);
//        Intent paypalIntent = new Intent(NotificationScreen.this, PaymentActivity.class);
//        paypalIntent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
//        startActivityForResult(paypalIntent, AppConstants.REQUEST_PAYPAL_PAYMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppConstants.REQUEST_PAYPAL_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data
                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {

                        JSONObject jsonObj = new JSONObject(confirm.toJSONObject().toString());
                        String paymentId = jsonObj.getJSONObject("response").getString("id");

//                        GlobalMethods.storeValuetoPreference(ProductDetailsBuyNeg.this, GlobalMethods.STRING_PREFERENCE, AppConstants.CARD_DETAILS, getString(R.string.one));
//                        if (mBidId.equalsIgnoreCase("")) {
//                            APIRequestHandler.getInstance().getPaypalPaymentResponse(mItemID, mItemQuantity, mItemPrice, paymentId, mServicePrice, mTotalPrice, AppConstants.SUCCESS_CODE, "", AppConstants.FAILURE_CODE, this);
//
//                        } else {
//
//                            APIRequestHandler.getInstance().getBidPaypalPaymentResponse(mItemID, mTotalPrice, mBidId, mbuyerID, paymentId, "", this);
//
//                        }

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment was submitted. Please see the docs.");
            }
        }


    }
}
