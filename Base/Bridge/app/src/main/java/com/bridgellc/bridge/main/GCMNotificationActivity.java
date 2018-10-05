package com.bridgellc.bridge.main;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.apiinterface.APIRequestHandler;
import com.bridgellc.bridge.entity.HomeSingleItemEntity;
import com.bridgellc.bridge.model.CommonResponse;
import com.bridgellc.bridge.model.ItemDetailResponse;
import com.bridgellc.bridge.ui.ChatScreen;
import com.bridgellc.bridge.ui.NegotiationChatRoom;
import com.bridgellc.bridge.ui.NotificationScreen;
import com.bridgellc.bridge.ui.PaymentHomeScreen;
import com.bridgellc.bridge.ui.ProductDetailsScreen;
import com.bridgellc.bridge.ui.RequestBiddingScreen;
import com.bridgellc.bridge.ui.ReviewScreen;
import com.bridgellc.bridge.ui.SignInScreen;
import com.bridgellc.bridge.ui.home.HomeScreenActivity;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.GlobalMethods;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.RetrofitError;

public class GCMNotificationActivity extends BaseActivity {


    private HomeSingleItemEntity mItemDetailsRes;
    private Bundle mBundle;
    private String mNotifyRes = "", notifyID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen_fragment);

        ViewGroup root = (ViewGroup) findViewById(R.id.parent_lay);
        mItemDetailsRes = new HomeSingleItemEntity();

        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            mNotifyRes = mBundle.getString("pushNotification");
        }


        JSONObject json;
        String item_id = "";
        String buyer_id = "";
        String payment_id = "";
        String notification_id = "";
        mActivity = this;
        try {
            json = new JSONObject(mNotifyRes);
            item_id = json.getString("typeId");
            buyer_id = json.getString("notification_from");
            payment_id = json.getString("payment_id");
            notification_id = json.getString("notification_id");
            notifyID = notification_id;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (GlobalMethods.isLoggedIn(GCMNotificationActivity.this)) {

//            APIRequestHandler.getInstance().getItemDetailsResponse(item_id, buyer_id, payment_id, notification_id, this);
            APIRequestHandler.getInstance().getItemDetailsResponse(item_id, buyer_id, payment_id, "", this);
        } else {
            Toast.makeText(mActivity, "Please Signin...", Toast.LENGTH_LONG).show();
            previousScreen(SignInScreen.class, true);
        }
    }


    @Override
    public void onRequestSuccess(Object responseObj) {
        if (responseObj instanceof ItemDetailResponse) {
            ItemDetailResponse itemRes = (ItemDetailResponse) responseObj;
            if (itemRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                mItemDetailsRes = itemRes.getResult();
                // movetoNextPage();
                APIRequestHandler.getInstance().getNotificationReadResponse(notifyID, GCMNotificationActivity.this);
            } else {
                DialogManager.showBasicAlertDialog(this, itemRes.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {
                        ((BaseActivity) mActivity).nextScreen(HomeScreenActivity.class, true);
                    }
                });
            }
        } else if (responseObj instanceof CommonResponse) {
            CommonResponse itemRes = (CommonResponse) responseObj;
            if (itemRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                movetoNextPage();
            } else {
                DialogManager.showBasicAlertDialog(this, itemRes.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {

                        ((BaseActivity) mActivity).nextScreen(HomeScreenActivity.class, true);
                    }
                });
            }

        }
    }


    @Override
    public void onRequestFailure(RetrofitError errorCode) {

        if (errorCode.getCause() instanceof java.net.ConnectException || errorCode.getCause() instanceof java.net.UnknownHostException) {
            DialogManager.showBasicAlertDialog(mActivity, getString(R.string.no_internet), new DialogMangerOkCallback() {
                @Override
                public void onOkClick() {

                    ((BaseActivity) mActivity).nextScreen(HomeScreenActivity.class, true);
                }
            });
        } else if (errorCode.getCause() instanceof java.net.SocketTimeoutException) {
            DialogManager.showBasicAlertDialog(mActivity,
                    getString(R.string.connect_time_out), new DialogMangerOkCallback() {
                        @Override
                        public void onOkClick() {

                            ((BaseActivity) mActivity).nextScreen(HomeScreenActivity.class, true);
                        }
                    });

        } else if (errorCode.getCause() instanceof retrofit.converter.ConversionException) {
            DialogManager.showBasicAlertDialog(mActivity, getString(R.string.serv_con_exce), new DialogMangerOkCallback() {
                @Override
                public void onOkClick() {

                    ((BaseActivity) mActivity).nextScreen(HomeScreenActivity.class, true);
                }
            });
        } else {

            DialogManager.showBasicAlertDialog(mActivity,
                    getString(R.string.serv_not_res), new DialogMangerOkCallback() {
                        @Override
                        public void onOkClick() {

                            ((BaseActivity) mActivity).nextScreen(HomeScreenActivity.class, true);
                        }
                    });

        }
    }

    private void movetoNextPage() {
        JSONObject jsonObj;
        String type = "", item_id = "";
        try {
            jsonObj = new JSONObject(mNotifyRes);
            type = jsonObj.getString("type_of_notification");
            item_id = jsonObj.getString("typeId");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mActivity, e.toString(), Toast.LENGTH_LONG).show();
        }

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

                    NegotiationChatRoom.mHomeSingleItemEntity = mItemDetailsRes;
                    AppConstants.CHAT_BACK = AppConstants.FAILURE_CODE;
                    AppConstants.NEGO_FRIEND_ID = otherUserId;
                    AppConstants.NEGO_USER_ID = GlobalMethods.getUserID(mActivity);
                    AppConstants.NEGO_ITEM_NOTI = mActivity.getString(R.string.one);
                    AppConstants.NEGO_ITEM_ID = mItemDetailsRes.getItem_id();
                    AppConstants.NEGO_ITEM_QTY = mItemDetailsRes.getPurchase_quantity();

                    if (mItemDetailsRes.getBid_id() != null && !mItemDetasRes.getBid_id().equalsIgnoreCase("") && !mItemDetailsRes.getBid_id().equalsIgnoreCase(mActivity.getString(R.string.zero))) {
                        AppConstants.NEGO_BID_ID = mItemDetailsRes.getBid_id();
                        NegotiationChatRoom.mHomeSingleItemEntity.setBuyer_id(mItemDetailsRes.getUser_id());

                    } else {
                        AppConstants.NEGO_BID_ID = "";
                    }


                    AppConstants.FROME_NOTIFICATION = mActivity.getString(R.string.one);

                    ((BaseActivity) mActivity).nextScreen(NegotiationChatRoom.class, true);
                    break;
                case "Negotiation Approve":
                    if (mItemDetailsRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller
                        sellBuyApprovList(mItemDetailsRes);
                    } else {
                        //I am Buyer
                        buyerOrdList(mItemDetailsRes);

                    }
                    AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.three);
                    ProductDetailsScreen.mHomeSingleItemEntity = mItemDetailsRes;


                    ((BaseActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);

                    break;
                case "Buy":
                    if (mItemDetailsRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller
                        sellBuyApprovList(mItemDetailsRes);
                    } else {
                        //I am Buyer
                        buyerOrdList(mItemDetailsRes);

                    }
                    AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.three);
                    ProductDetailsScreen.mHomeSingleItemEntity = mItemDetailsRes;
                    ((BaseActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);


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
                    AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.three);

                    ((BaseActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);

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
                    AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.three);

                    ((BaseActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);

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
                    AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.three);

                    ((BaseActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);

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
                    AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.three);

                    ((BaseActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);

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
                    AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.three);

                    ((BaseActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);

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

                    ProductDetailsScreen.mHomeSingleItemEntity = mItemDetailsRes;
                    if (mItemDetailsRes.getItem_type().equalsIgnoreCase(mActivity.getString(R.string.one)) && mItemDetailsRes.getDelivery_type().equalsIgnoreCase(mActivity.getString(R.string.two))) {
                        ProductDetailsScreen.mFooterBtnCount = 1;
                        ProductDetailsScreen.mFooterOneTxt = mActivity.getString(R.string.rating);
                    }
                    AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.three);

                    ((BaseActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);

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
                    AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.three);
                    ((BaseActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);


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

                    AppConstants.FROME_NOTIFICATION = mActivity.getString(R.string.one);
                    ChatScreen.isSend = true;
                    ((BaseActivity) mActivity).nextScreen(ChatScreen.class, true);

                    break;
                case "Rating":
                    if (mItemDetailsRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller
                    } else {
                        //I am Buyer

                    }

                    ProductDetailsScreen.mHomeSingleItemEntity = mItemDetailsRes;
                    AppConstants.FROME_NOTIFICATION = mActivity.getString(R.string.one);

                    ReviewScreen.mOtherUserId = GlobalMethods.getUserID(mActivity);

                    ((BaseActivity) mActivity).nextScreen(ReviewScreen.class, true);

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

                    AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.three);

                    ((BaseActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);

                    break;
                case "Bid":
                    if (mItemDetailsRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller
                        sellBuyApprovList(mItemDetailsRes);
                    } else {
                        //I am Buyer
                        buyerOrdList(mItemDetailsRes);

                    }

                    RequestBiddingScreen.mItemId = item_id;
                    AppConstants.FROME_NOTIFICATION = mActivity.getString(R.string.one);
                    ((BaseActivity) mActivity).nextScreen(RequestBiddingScreen.class, true);

                    break;
                case "Bid Reject":
                case "Item Purchased":
                case "Reject Offer":
                    ((BaseActivity) mActivity).nextScreen(HomeScreenActivity.class, true);
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
                    AppConstants.BANK_ACC_DET_BACK_SCREEN = mActivity.getString(R.string.eight);
                    ((BaseActivity) mActivity).nextScreen(PaymentHomeScreen.class, true);
                    break;

                case "Offer Negotiation Approve Paypal":
                case "Negotiation Approve Paypal":
                    AppConstants.PAYPAL_NOTIFY = AppConstants.SUCCESS_CODE;
                    NotificationScreen.mFromNotiItemDetailsRes = mItemDetasRes;
                    ((BaseActivity) mActivity).nextScreen(NotificationScreen.class, true);

                    break;
                case "Refund":
                    if (mItemDetailsRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller
                        sellBuyApprovList(mItemDetailsRes);
                    } else {
                        //I am Buyer
                        buyerOrdList(mItemDetailsRes);

                    }

                    ProductDetailsScreen.mHomeSingleItemEntity = mItemDetailsRes;
                    AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.three);
                    ((BaseActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);

                    break;
                default:
                    break;
            }
        } else {
            ProductDetailsScreen.mHomeSingleItemEntity = mItemDetailsRes;


            ProductDetailsScreen.mFooterBtnCount = 2;
            ProductDetailsScreen.mFooterOneTxt = mActivity.getString(R.string.chat);
            ProductDetailsScreen.mFooterTwoTxt = mActivity.getString(R.string.rating);

            if (mItemDetailsRes.getItem_type().equalsIgnoreCase(mActivity.getString(R.string.one)) && mItemDetailsRes.getDelivery_type().equalsIgnoreCase(mActivity.getString(R.string.two))) {
                ProductDetailsScreen.mFooterBtnCount = 1;
                ProductDetailsScreen.mFooterOneTxt = mActivity.getString(R.string.rating);
            }

            AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.three);

            ((BaseActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);


        }
    }


    private void sellBuyApprovList(HomeSingleItemEntity homeSingleItemEntity) {
        ProductDetailsScreen.mFooterOneTxt = mActivity.getString(R.string.chat);
        ProductDetailsScreen.mFooterTwoTxt = mActivity.getString(R.string.finish);
        ProductDetailsScreen.mFooterBtnCount = 2;
        if (homeSingleItemEntity.getItem_type().equalsIgnoreCase(mActivity.getString(R.string.one)) && (homeSingleItemEntity.getDelivery_type().equalsIgnoreCase(mActivity.getString(R.string.two)))) {

            ProductDetailsScreen.mFooterTwoTxt = mActivity.getString(R.string.rating);
            if (mItemDetailsRes.getItem_type().equalsIgnoreCase(mActivity.getString(R.string.one)) && mItemDetailsRes.getDelivery_type().equalsIgnoreCase(mActivity.getString(R.string.two))) {
                ProductDetailsScreen.mFooterBtnCount = 1;
                ProductDetailsScreen.mFooterOneTxt = mActivity.getString(R.string.rating);
            }

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
            ProductDetailsScreen.mFooterOneTxt = mActivity.getString(R.string.chat);
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

}
