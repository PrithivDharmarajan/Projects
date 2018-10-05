package com.bridgellc.bridge.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.adapter.NegotiationChatAdapter;
import com.bridgellc.bridge.adapter.RecentActivityAdapter;
import com.bridgellc.bridge.apiinterface.APIRequestHandler;
import com.bridgellc.bridge.entity.HomeSingleItemEntity;
import com.bridgellc.bridge.entity.NegotiateResponseEntity;
import com.bridgellc.bridge.entity.PaymentBuySellEntity;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.model.PaymentHistoryResponse;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 * Created by admin on 6/29/2016.
 */
public class RecentActivityScreen extends BaseActivity implements View.OnClickListener, DialogMangerOkCallback {


    private ListView recentPaymentListView;
    private NegotiationChatAdapter mChatListAdapter = null;
    private PaymentBuySellEntity paymentBuyEntity = null;
    private boolean isFirstTime = true;

    private TextView titleTv;
    private ImageView mHeaderLeftImage;
    private TextView mHeaderRightImage;
    private TextView mHeader_right_txt;
    private ImageView inviteImg;
    private TextView inviteText;
    private Button mSendBtn;
    private EditText mChatText;
    private String ItemQuantity = "", notification = "", neg_ID = "";
    private int requestType = 1;
    private boolean approveClick = false;
    private boolean isSend = false;
    public static boolean isNegotionWindow = false;
    private String mItemName = "", mItemQuantity = "", mItemPrice = "", mServicePrice = "", mTotalPrice = "";
    //    private ArrayList<String> chat_history = new ArrayList<String>();
    private int last_chat_count = 0;
    private int new_chat_count = 0;
    private int first = 0;
    private ArrayList<PaymentBuySellEntity> mPaymenyDone;
    private ArrayList<PaymentBuySellEntity> mPaymenyEscrow;
    private ArrayList<PaymentBuySellEntity> mPaymenyReceived;
    private ArrayList<PaymentBuySellEntity> tempPaymenyDone;
    private ArrayList<PaymentBuySellEntity> tempPaymenyEscrow;
    private ArrayList<PaymentBuySellEntity> tempPaymenyReceived;
    private boolean isMax = false;
    private String friendId = "1", itemId = "32";
    ArrayList<NegotiateResponseEntity> mChatResponseList1;
    private ArrayList<NegotiateResponseEntity> mChatResponseList = new ArrayList<NegotiateResponseEntity>();
    private ArrayList<NegotiateResponseEntity> tempChatList = new ArrayList<NegotiateResponseEntity>();
    private ArrayList<NegotiateResponseEntity> tempChatList1 = new ArrayList<NegotiateResponseEntity>();

    private ArrayList<PaymentBuySellEntity> AllmPayment = new ArrayList<PaymentBuySellEntity>();
    long timeInterval = 3000;
    private Handler chatHandler = new Handler();

    public static HomeSingleItemEntity mHomeSingleItemEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_recent_activity);

        mPaymenyDone = new ArrayList<PaymentBuySellEntity>();
        mPaymenyEscrow = new ArrayList<PaymentBuySellEntity>();
        mPaymenyReceived = new ArrayList<PaymentBuySellEntity>();

        tempPaymenyDone = new ArrayList<PaymentBuySellEntity>();
        tempPaymenyEscrow = new ArrayList<PaymentBuySellEntity>();
        tempPaymenyReceived = new ArrayList<PaymentBuySellEntity>();

        setHeader();
        initComponents();

    }

    private void initComponents() {


        recentPaymentListView = (ListView) findViewById(R.id.recent_payment_listview);

        inviteText = (TextView) findViewById(R.id.invite_txt);
        inviteImg = (ImageView) findViewById(R.id.add_view);
        inviteText.setText(getString(R.string.invite_friends_get_paid));
        APIRequestHandler.getInstance().getMyPaymentHistoryResponse(this);
    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);

        if (responseObj instanceof PaymentHistoryResponse) {
            PaymentHistoryResponse profileres = (PaymentHistoryResponse) responseObj;
            if (profileres.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                //IniData
                setData(profileres);
            } else {
                DialogManager.showBasicAlertDialog(this,
                        profileres.getMessage(), this);
            }
        }
    }

    private void setHeader() {

        mViewGroup = (ViewGroup) findViewById(R.id.parent_layer);
        setupUI(mViewGroup);
        setFont(mViewGroup,mLightFont);

        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        mHeadeRightBtnLay = (RelativeLayout) findViewById(R.id.header_right_btn_lay2);

        mHeaderTxt.setText(getString(R.string.activity).toUpperCase(Locale.ENGLISH));
        //     mHeaderLeftImage.setImageResource(R.drawable.back_img);

        mHeader_right_txt = (TextView) findViewById(R.id.header_right_green_txt);
        mHeadeLeftBtnLay.setVisibility(View.VISIBLE);
        mHeadeRightBtnLay.setVisibility(View.VISIBLE);
/*

        titleTv = (TextView) findViewById(R.id.header_txt);
        titleTv.setText(getString(R.string.recent_activity_payment_title));
        mHeaderLeftImage = (ImageView) findViewById(R.id.header_left_img);
      //  mHeaderRightImage = (TextView) findViewById(R.id.header_right_img);
        mHeaderLeftImage.setImageResource(R.drawable.back_img);

        mHeader_right_txt = (TextView) findViewById(R.id.header_right_txt1);
        //mHeader_right_txt.setVisibility(View.INVISIBLE);
       // mHeader_right_txt.setText(getString(R.string.approve_c));

        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        mHeadeRightBtnLay = (RelativeLayout) findViewById(R.id.header_right_btn_lay2);
      //  mHeaderRightImage.setVisibility(View.GONE);

        mHeadeLeftBtnLay.setVisibility(View.VISIBLE);
        mHeadeRightBtnLay.setVisibility(View.VISIBLE);

*/
    }

    private void setData(PaymentHistoryResponse mPayResponse) {
        mPaymenyDone.clear();
        mPaymenyEscrow.clear();
        mPaymenyReceived.clear();
        tempPaymenyDone.clear();
        tempPaymenyEscrow.clear();
        tempPaymenyReceived.clear();

        tempPaymenyDone = mPayResponse.getResult().getBuy();
        tempPaymenyEscrow = mPayResponse.getResult().getEscrow();
        tempPaymenyReceived = mPayResponse.getResult().getSell();


        String received = mPayResponse.getResult().getPayment_received();
        String to_be_received = mPayResponse.getResult().getPayment_to_be_received();
        String sent = mPayResponse.getResult().getPayment_sent();
        String to_be_send = mPayResponse.getResult().getPayment_to_be_sent();

        Double amt = Double.parseDouble(received) - Double.parseDouble(sent);
        mHeader_right_txt.setText("$" + amt);
        for (int i = 0; i < tempPaymenyDone.size(); i++) {

            PaymentBuySellEntity obj = tempPaymenyDone.get(i);


            String str = "0";
            obj.setEscrow(str);
            mPaymenyDone.add(obj);

        }

        for (int i = 0; i < tempPaymenyEscrow.size(); i++) {

            PaymentBuySellEntity obj = tempPaymenyEscrow.get(i);


            String str = "1";
            obj.setEscrow(str);
            mPaymenyEscrow.add(obj);

        }

        for (int i = 0; i < tempPaymenyReceived.size(); i++) {

            PaymentBuySellEntity obj = tempPaymenyReceived.get(i);


            String str = "0";
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
        RecentActivityAdapter recentListAdapter = new RecentActivityAdapter(this, AllmPayment);
        recentPaymentListView.setAdapter(recentListAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.add_view:
                Share_Text();
                break;
       /*     case R.id.payescrow_btn:
                setAdapterData(2);
                break;
            case R.id.payreceive_btn:
                setAdapterData(3);
                break;
            case R.id.header_right_btn_lay:

                DialogManager.showBasicBtnAlertDialog(this, getString(R.string.app_name), "Do you want to clear all the payment history ?", new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {
                        APIRequestHandler.getInstance()
                                .getPaymentClearResponse(PaymentScreen.this);

                    }
                });

                break;
        */
        }

    }

    private void Share_Text() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Join Base! Come to one place to get everything you need & make money!";
        //  sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));

    }

    @Override
    public void onBackPressed() {
        previousScreen(DashboardScreen.class, true);
    }

    @Override
    public void onOkClick() {

    }
}