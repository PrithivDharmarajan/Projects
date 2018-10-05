package com.bridgellc.bridge.ui;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.apiinterface.APIRequestHandler;
import com.bridgellc.bridge.entity.HomeSingleItemEntity;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.model.BuyItemResponse;
import com.bridgellc.bridge.model.PayForItemResponse;
import com.bridgellc.bridge.model.PaypalPayResponse;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogManagerTwoBtnCallback;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.GlobalMethods;

import java.util.Locale;

public class ProductBuyNegScreen extends BaseActivity implements DialogMangerOkCallback {
    private TextView mSelectQtyTxt;
    private Button mOkBtn;
    private double totalAmount;
    String priceAmtText = "0";
    private String mItemName = "", mItemQuantity = "", mItemPrice = "", mServicePrice = "", mTotalPrice = "";

    public static HomeSingleItemEntity mHomeSingleItemEntity;
    public static boolean isBuy = false;
    private int mQuantity = 0;


    private TextView mItemQuantityTxt, mTotCostTxt;
    private int mReqType = 1;
    private String itemValue = AppConstants.SUCCESS_CODE;
    private ListView mQtyList;
    private String mTipsAmt = AppConstants.FAILURE_CODE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_buy_neg);

        initComponents();
    }

    private void initComponents() {

        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);

        TextView mItemNameTxt = (TextView) findViewById(R.id.item_name_txt);
        TextView mPriceTxt = (TextView) findViewById(R.id.price_txt);
        TextView mCondition = (TextView) findViewById(R.id.item_condition_txt);

        mSelectQtyTxt = (TextView) findViewById(R.id.selcted_qty_txt);
        mQtyList = (ListView) findViewById(R.id.qty_listView);
        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mItemQuantityTxt = (TextView) findViewById(R.id.no_of_item_txt);
        mTotCostTxt = (TextView) findViewById(R.id.tot_cost_txt);

        final ScrollView outerLayout = (ScrollView) findViewById(R.id.scrollView);
        outerLayout.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                mQtyList.setVisibility(View.INVISIBLE);
                return true;
            }
        });
        mQtyList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                outerLayout.requestDisallowInterceptTouchEvent(true);

                int action = event.getActionMasked();

                switch (action) {
                    case MotionEvent.ACTION_UP:
                        outerLayout.requestDisallowInterceptTouchEvent(false);
                        break;
                }

                return false;
            }
        });


        mItemQuantityTxt.setText(getString(R.string.no_of_item) + " " + mHomeSingleItemEntity
                .getItem_quantity());
        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        mOkBtn = (Button) findViewById(R.id.footer_btn);
        mOkBtn.setText(getString(R.string.cont));
        mHeadeLeftBtnLay.setVisibility(View.VISIBLE);


        mTotCostTxt.setText(getString(R.string.total_cost) + "$0");

        mSelectQtyTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mQtyList.getVisibility() == View.GONE) {
                    mQtyList.setVisibility(View.VISIBLE);
                } else {
                    mQtyList.setVisibility(View.GONE);
                }

            }
        });


        if (mHomeSingleItemEntity.getItem_quantity() != null &&
                mHomeSingleItemEntity.getItem_quantity().length() > 0) {

            int count = Integer.parseInt(mHomeSingleItemEntity
                    .getItem_quantity());

            String[] spinnerArray = new String[count];

            for (int i = 0; i < count; i++) {
                spinnerArray[i] = "" + (i + 1);
            }


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, spinnerArray);

            mQtyList.setAdapter(adapter);
            mQtyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    // ListView Clicked item index
                    int itemPosition = position;


                    mQuantity = position + 1;
                    totalAmount = (double) mQuantity * Double.parseDouble(mHomeSingleItemEntity.getItem_cost());
                    mTotCostTxt.setText(getString(R.string.total_cost) + " " + getString(R.string.dollar_sym) + " " + String.format(Locale.ENGLISH,"%.02f", totalAmount));
                    priceAmtText = String.format(Locale.ENGLISH,"%.02f", totalAmount);

                    itemValue = (String) mQtyList.getItemAtPosition(position);
                    mSelectQtyTxt.setText(getString(R.string.qty) + " " + itemValue);
                    mQtyList.setVisibility(View.INVISIBLE);

                }

            });


            mQuantity = 1;
            totalAmount = (double) 1 * Double.parseDouble(mHomeSingleItemEntity.getItem_cost());
            mTotCostTxt.setText(getString(R.string.total_cost) + " " + getString(R.string.dollar_sym) + " " + String.format(Locale.ENGLISH,"%.02f", totalAmount));
            priceAmtText = String.format(Locale.ENGLISH,"%.02f", totalAmount);
            mSelectQtyTxt.setText(getString(R.string.qty) + " "+AppConstants.SUCCESS_CODE);


            //Set Data

            mHeaderTxt.setText(getString(R.string.shop).toUpperCase(Locale.ENGLISH));

            mItemNameTxt.setText(GlobalMethods.isGoodsService(ProductBuyNegScreen.this, mHomeSingleItemEntity.getItem_type()) + " : " + mHomeSingleItemEntity.getItem_name());
            mPriceTxt.setText(getString(R.string.price) + " " + getString(R.string.dollar_sym) + " " + mHomeSingleItemEntity.getItem_cost());

            String cond = mHomeSingleItemEntity.getItem_type().equalsIgnoreCase(getString(R.string.one))
                    ? getString(R.string.condition) : getString(R.string.skill_level);
            mCondition.setText(cond + " " + mHomeSingleItemEntity.getItem_condition());
            mCondition.setVisibility(mHomeSingleItemEntity.getCategory_name().equalsIgnoreCase
                    (getString(R.string.ticket)) ? View.GONE : View.VISIBLE);
        }


    }

    @Override
    public void onBackPressed() {
//        finishScreen();
        previousScreen(ProductDetailsBuyNeg.class, true);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.footer_btn:
                if (isBuy) {
                    if (priceAmtText.replace("$", "").trim().equalsIgnoreCase(GlobalMethods
                            .afterTwoPointVal(getString(R.string
                                    .zero)))) {


                        DialogManager.showBaseTwoBtnDialog(ProductBuyNegScreen.this, getString(R
                                .string.app_name), getString(R.string.do_you_pur), getString(R.string
                                .yes), getString(R.string.no), new DialogManagerTwoBtnCallback() {
                            @Override
                            public void onBtnOkClick(String mOkStr) {

                                APIRequestHandler.getInstance().getPaypalPaymentResponse(GlobalMethods
                                                .getUserID(ProductBuyNegScreen.this),
                                        mHomeSingleItemEntity.getItem_id()
                                        , "" + mQuantity, AppConstants.FAILURE_CODE, "free", AppConstants.FAILURE_CODE, AppConstants.FAILURE_CODE,
                                        AppConstants.FAILURE_CODE, AppConstants.FAILURE_CODE,
                                        AppConstants.FAILURE_CODE, "", ProductBuyNegScreen.this);

                            }

                            @Override
                            public void onBtnCancelClick(String mCancelStr) {

                            }
                        });

                    } else {
                        DialogManager.shownAlertDialogProductPrice(ProductBuyNegScreen.this,
                                getString(R.string.app_name), getString(R.string.do_you_pur), priceAmtText, new DialogMangerOkCallback() {


                                    @Override
                                    public void onOkClick() {
                                        mReqType = 1;

                                        String cost = priceAmtText.replace("$", "").trim();
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
                                        mItemQuantity = "" + mQuantity;
                                        mItemPrice = cost;
                                        mServicePrice = mServiceTxt;
                                        mTotalPrice = mTotalCostTxt;


                                        if (mHomeSingleItemEntity.getPartner().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                                            DialogManager.showEditBtnAlertDialog(ProductBuyNegScreen.this, getString(R.string.app_name), getString(R.string.enter_amt_tip), getString(R.string.cont), getString(R.string.ignore), new DialogManagerTwoBtnCallback() {
                                                @Override
                                                public void onBtnOkClick(String mOkStr) {
                                                    mTipsAmt = mOkStr;
                                                    paymentScreenCall();
                                                }

                                                @Override
                                                public void onBtnCancelClick(String mCancelStr) {
                                                    mTipsAmt = getString(R.string.zero);
                                                    paymentScreenCall();
                                                }
                                            });

                                        } else {
                                            paymentScreenCall();
                                        }

                                    }
                                });
                    }
                } else {

                    DialogManager.showBaseTwoBtnDialog(ProductBuyNegScreen.this, getString(R
                            .string.app_name), getString(R.string.do_you_neg), getString(R.string
                            .yes), getString(R.string.no), new DialogManagerTwoBtnCallback() {
                        @Override
                        public void onBtnOkClick(String mOkStr) {
                            NegotiationChatRoom.mHomeSingleItemEntity = mHomeSingleItemEntity;
                            AppConstants.NEGO_FRIEND_ID = mHomeSingleItemEntity.getUser_id();
                            AppConstants.NEGO_BID_ID = "";
                            AppConstants.NEGO_USER_ID = GlobalMethods.getUserID(ProductBuyNegScreen.this);
                            AppConstants.NEGO_ITEM_ID = mHomeSingleItemEntity.getItem_id();
                            AppConstants.NEGO_ITEM_QTY = itemValue;
                            AppConstants.CHAT_BACK = AppConstants.HOME_SCREEN;
                            AppConstants.NEGO_ITEM_NOTI = "";
                            nextScreen(NegotiationChatRoom.class, true);
                        }

                        @Override
                        public void onBtnCancelClick(String mCancelStr) {

                        }
                    });

//                    DialogManager.showNegAlertDialog(ProductBuyNegScreen.this, getString(R.string.nego), new DialogMangerOkCallback() {
//                        @Override
//                        public void onOkClick() {
//                            NegotiationChatRoom.mHomeSingleItemEntity = mHomeSingleItemEntity;
//                            AppConstants.NEGO_FRIEND_ID = mHomeSingleItemEntity.getUser_id();
//                            AppConstants.NEGO_BID_ID = "";
//                            AppConstants.NEGO_USER_ID = GlobalMethods.getUserID(ProductBuyNegScreen.this);
//                            AppConstants.NEGO_ITEM_ID = mHomeSingleItemEntity.getItem_id();
//                            AppConstants.NEGO_ITEM_QTY = itemValue;
//                            AppConstants.CHAT_BACK = AppConstants.HOME_SCREEN;
//                            AppConstants.NEGO_ITEM_NOTI = "";
//
////                            if (mHomeSingleItemEntity.getPayment_mode().equalsIgnoreCase(getString(R.string.three)) || GlobalMethods.getStringValue(ProductBuyNegScreen.this, AppConstants.CARD_DETAILS).equalsIgnoreCase(getString(R.string.one))) {
//
//
//                            nextScreen(NegotiationChatRoom.class, true);
//
////                            } else {
////                                AppConstants.CARD_DET_BACK_SCREEN = getString(R.string.one);
////                                nextScreen(AddPayCard.class, false);
////                            }
//                        }
//                    });
//                }

                    break;
                }
        }
    }

    private void paymentScreenCall() {
        AppConstants.PAYPAL_BUY_ID = GlobalMethods.getUserID(ProductBuyNegScreen.this);
        AppConstants.PAYPAL_BID_ID = AppConstants.FAILURE_CODE;

        AppConstants.PAYPAL_ITEM_ID = mHomeSingleItemEntity.getItem_id();

        AppConstants.PAYPAL_ITEM_QTY = mItemQuantity;
        AppConstants.PAYPAL_ITEM_COST = mItemPrice;
        AppConstants.PAYPAL_SER_FEES = mServicePrice;
        AppConstants.PAYPAL_TOT_COST = mTotalPrice;
        AppConstants.PAYPAL_NEG = AppConstants.FAILURE_CODE;
        AppConstants.PAYPAL_NEG_ID = "";
        AppConstants.PAYPAL_TIPS = mTipsAmt;
        AppConstants.PAYPAL_ITEM_DELV_TYPE = mHomeSingleItemEntity.getDelivery_type();
        AppConstants.PAYPAL_USER_ID = mHomeSingleItemEntity.getUser_id();
        AppConstants.PAYPAL_USER_NAME = mHomeSingleItemEntity.getUser_first_name();
        AppConstants.PAYPAL_ITEM_TYPE = mHomeSingleItemEntity.getItem_type();
        AppConstants.PAYPAL_ITEM_NAME = mHomeSingleItemEntity.getItem_name();

        nextScreen(PaymentPaypalStripScreen.class, false);
    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);

        if (responseObj instanceof PayForItemResponse) {

            final PayForItemResponse payForItemEntity = (PayForItemResponse) responseObj;
            if (payForItemEntity.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                if (mHomeSingleItemEntity.getItem_type().equalsIgnoreCase
                        (getString(R.string.one)) && mHomeSingleItemEntity.getDelivery_type()
                        .equalsIgnoreCase("2")) {
                    DialogManager.showBasicAlertDialog(ProductBuyNegScreen.this, getString(R.string.electronic_goods_delivery_text), new DialogMangerOkCallback() {

                        @Override
                        public void onOkClick() {


//                            String UserName = mHomeSingleItemEntity.getUser_first_name();
                            RateBuySellScreen.mHeader = getString(R.string.rateseller);
                            AppConstants.RATING_ITEM_ID = mHomeSingleItemEntity.getItem_id();
                            AppConstants.RATING_USER_ID = mHomeSingleItemEntity.getUser_id();
                            AppConstants.RATING_USER_NAME = mHomeSingleItemEntity.getUser_first_name();
//                            ChatScreen.isSend = true;


                            AppConstants.RATING_RATE = mHomeSingleItemEntity.getItem_seller_rating();
                            AppConstants.RATING_CMD = mHomeSingleItemEntity.getItem_seller_comments();
                            nextScreen(RateBuySellScreen.class, true);


//                            finish();
                        }
                    });
                } else {
//                    BuyerCodeScreen.mBuyerCode = payForItemEntity.getResult().getUnique_code();
                    BuyerCodeScreen.mBuyerName =
                            mHomeSingleItemEntity.getUser_first_name();
                    BuyerCodeScreen.mPayID = payForItemEntity.getResult().getPayment_id();
                    BuyerCodeScreen.mItemID = mHomeSingleItemEntity.getItem_id();

                    ChatScreen.mPaymentId = payForItemEntity.getResult().getPayment_id();

                    AppConstants.RATING_BTN = getString(R.string.one);
                    AppConstants.CODE_SCREEN = AppConstants.HOME_SCREEN;
                    AppConstants.CHAT_FRIEND_ID = mHomeSingleItemEntity.getUser_id();
                    AppConstants.CHAT_ITEM_ID = mHomeSingleItemEntity.getItem_id();

                    nextScreen(BuyerCodeScreen.class, true);


                }
            } else {
                DialogManager.showBasicAlertDialog(this, payForItemEntity.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {

                    }
                });
            }
        } else if (responseObj instanceof BuyItemResponse) {
            mReqType = 2;
            BuyItemResponse mCommonResponse = (BuyItemResponse) responseObj;
//            if (mCommonResponse.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
//                APIRequestHandler.getInstance().payForItemResponse(ProductDetailsBuyNeg
//                                .mHomeSingleItemEntity.getItem_id(),
//                        "" + mQuantity, ProductBuyNegScreen.this);
//            } else if (mCommonResponse.getResponse_code().equalsIgnoreCase(getString(R.string.two))) {
//                AppConstants.CARD_DET_BACK_SCREEN = getString(R.string.one);
//                nextScreen(AddPayCard.class, false);
//            } else {
//                DialogManager.showValidationDialog(this, mCommonResponse.getMessage());
//            }


            if (mCommonResponse.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {


//                if (mHomeSingleItemEntity.getPayment_mode().equalsIgnoreCase(getString(R.string.three))) {
//                    PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(1), "USD", "BRIDGE PAYPAL",
//                            PayPalPayment.PAYMENT_INTENT_SALE);
//                    Intent paypalIntent = new Intent(ProductDetailsBuyNeg.this, PaymentActivity.class);
//                    paypalIntent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
//                    startActivityForResult(paypalIntent, AppConstants.REQUEST_PAYPAL_PAYMENT);

//                callPaypalPayment(mCommonResponse.getResult());

//                } else {
//
//                    APIRequestHandler.getInstance().payForItemResponse(ProductDetailsBuyNeg
//                                    .mHomeSingleItemEntity.getItem_id(),
//                            "" + mQuantity, ProductBuyNegScreen.this);
//                }
            } else if (mCommonResponse.getResponse_code().equalsIgnoreCase(getString(R.string.two))) {

//                if (mHomeSingleItemEntity.getPayment_mode().equalsIgnoreCase(getString(R.string.three))) {

//                callPaypalPayment(mCommonResponse.getResult());

//                } else {
//                    AppConstants.CARD_DET_BACK_SCREEN = getString(R.string.one);
////                AppConstants.CARD_DET_BACK_SCREEN = getString(R.string.three);
//                    nextScreen(AddPayCard.class, false);
//                }
            } else {

                DialogManager.showBasicAlertDialog(ProductBuyNegScreen.this, mCommonResponse.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {

                    }
                });
            }

        } else if (responseObj instanceof PaypalPayResponse) {
            final PaypalPayResponse mPaypalResponse = (PaypalPayResponse) responseObj;

            if (mPaypalResponse.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                DialogManager.showBasicAlertDialog(ProductBuyNegScreen.this, mPaypalResponse.getResult().getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {


                        if (mHomeSingleItemEntity.getItem_type()
                                .equalsIgnoreCase(getString(R.string.one))) {

                            //Goods
                            if (mHomeSingleItemEntity.getDelivery_type()
                                    .equalsIgnoreCase(getString(R.string.one))) {

                                BuyerCodeScreen.mBuyerName =
                                        mHomeSingleItemEntity.getUser_first_name();
                                BuyerCodeScreen.mPayID = mPaypalResponse.getResult().getPayment_id();
                                BuyerCodeScreen.mItemID = mHomeSingleItemEntity.getItem_id();


                                ChatScreen.mPaymentId = mPaypalResponse.getResult().getPayment_id();

                                AppConstants.CODE_SCREEN = AppConstants.HOME_SCREEN;
                                AppConstants.CHAT_FRIEND_ID = mHomeSingleItemEntity.getUser_id();
                                AppConstants.CHAT_ITEM_ID = mHomeSingleItemEntity.getItem_id();

                                nextScreen(BuyerCodeScreen.class, true);
                            } else {
//                                DialogManager.showBasicAlertDialog(ProductBuyNegScreen.this, getString(R
//                                        .string.approve), getString(R.string.electronic_goods_delivery_text), new DialogMangerOkCallback() {
//
//                                    @Override
//                                    public void onOkClick() {

                                String UserName = mHomeSingleItemEntity.getUser_first_name();
                                RateBuySellScreen.mHeader = getString(R.string.rateseller);
                                AppConstants.RATING_ITEM_ID = mHomeSingleItemEntity.getItem_id();
                                AppConstants.RATING_USER_ID = mHomeSingleItemEntity.getUser_id();
                                AppConstants.RATING_USER_NAME = UserName;
                                ChatScreen.isSend = true;

                                AppConstants.RATING_RATE = mHomeSingleItemEntity.getItem_seller_rating();
                                AppConstants.RATING_CMD = mHomeSingleItemEntity.getItem_seller_comments();

                                nextScreen(RateBuySellScreen.class, true);


                            }
//                                });
//                            }
                        } else {
                            //Services
                            previousScreen(DashboardScreen.class, true);

                        }


//                        if (mHomeSingleItemEntity.getItem_type().equalsIgnoreCase
//                                (getString(R.string.one)) && mHomeSingleItemEntity.getDelivery_type()
//                                .equalsIgnoreCase("2")) {
//                            DialogManager.showBasicAlertDialog(ProductBuyNegScreen.this, getString(R
//                                    .string.approve), getString(R.string.electronic_goods_delivery_text), new DialogMangerOkCallback() {
//
//                                @Override
//                                public void onOkClick() {
//
//
//                                    String UserName = mHomeSingleItemEntity.getUser_first_name();
//                                    RateBuySellScreen.mHeader = getString(R.string.rateseller);
//                                    AppConstants.RATING_ITEM_ID = mHomeSingleItemEntity.getItem_id();
//                                    AppConstants.RATING_USER_ID = mHomeSingleItemEntity.getUser_id();
//                                    AppConstants.RATING_USER_NAME = UserName;
//                                    ChatScreen.isSend = true;
//
//                                    AppConstants.RATING_RATE = mHomeSingleItemEntity.getItem_seller_rating();
//                                    AppConstants.RATING_CMD = mHomeSingleItemEntity.getItem_seller_comments();
//
//                                    nextScreen(RateBuySellScreen.class, true);
//
//
////                            finish();
//                                }
//                            });
//                        } else {
////                            BuyerCodeScreen.mBuyerCode = mPaypalResponse.getResult().getUnique_code();
//                            BuyerCodeScreen.mBuyerName =
//                                    mHomeSingleItemEntity.getUser_first_name();
//                            BuyerCodeScreen.mPayID = mPaypalResponse.getResult().getPayment_id();
//                            BuyerCodeScreen.mItemID = mHomeSingleItemEntity.getItem_id();
//
//
//                            ChatScreen.mPaymentId = mPaypalResponse.getResult().getPayment_id();
//
//                            AppConstants.CODE_SCREEN = AppConstants.HOME_SCREEN;
//                            AppConstants.CHAT_FRIEND_ID = mHomeSingleItemEntity.getUser_id();
//                            AppConstants.CHAT_ITEM_ID = mHomeSingleItemEntity.getItem_id();
//
//                            nextScreen(BuyerCodeScreen.class, true);
//                        }


                    }
                });

            } else {
                DialogManager.showBasicAlertDialog(ProductBuyNegScreen.this, mPaypalResponse.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {

                    }
                });
            }

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
//        Intent paypalIntent = new Intent(ProductBuyNegScreen.this, PaymentActivity.class);
//        paypalIntent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
//        startActivityForResult(paypalIntent, AppConstants.REQUEST_PAYPAL_PAYMENT);
//    }

    @Override
    public void onOkClick() {

    }


}
