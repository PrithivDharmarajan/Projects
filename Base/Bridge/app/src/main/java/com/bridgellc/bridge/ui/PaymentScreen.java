package com.bridgellc.bridge.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.adapter.PaymentsAdapter;
import com.bridgellc.bridge.apiinterface.APIRequestHandler;
import com.bridgellc.bridge.entity.PaymentBuySellEntity;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.model.PaymentHistoryResponse;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogManagerTwoBtnCallback;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.GlobalMethods;

import java.util.ArrayList;
import java.util.Locale;

public class PaymentScreen extends BaseActivity implements View.OnClickListener, DialogMangerOkCallback {

    private ListView mPaymentlist;
    private Button mPayDoneBtn, mPayReceivedBtn, mPayEscrowBtn;
    private PaymentsAdapter mAdapter;
    private ImageView mHeaderRightImage;
    private TextView mPaymentSentTxt, mPaymentRecTxt, mPaymentToSentTxt, mPaymentToRecTxt;
    private ArrayList<PaymentBuySellEntity> mPaymenyDone;
    private ArrayList<PaymentBuySellEntity> mPaymenyEscrow;
    private ArrayList<PaymentBuySellEntity> mPaymenyReceived;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_payment_screen);

        mPaymenyDone = new ArrayList<>();
        mPaymenyEscrow = new ArrayList<>();
        mPaymenyReceived = new ArrayList<>();
        initComponents();
    }

    private void initComponents() {
        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);

        //Header and Footer Int
        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        mHeadeRightBtnLay = (RelativeLayout) findViewById(R.id.header_right_btn_lay);
        mHeaderRightImage = (ImageView) findViewById(R.id.header_right_img);
        mHeaderRightImage.setImageResource(R.drawable.delete_icon);

        //Inner Int
        mPaymentlist = (ListView) findViewById(R.id.payment_list);

        mPaymentSentTxt = (TextView) findViewById(R.id.payment_sent_txt);
        mPaymentRecTxt = (TextView) findViewById(R.id.payment_received_txt);
        mPaymentToSentTxt = (TextView) findViewById(R.id.payment_to_sent_txt);
        mPaymentToRecTxt = (TextView) findViewById(R.id.payment_to_received_txt);


        mPayDoneBtn = (Button) findViewById(R.id.paydone_btn);
        mPayEscrowBtn = (Button) findViewById(R.id.payescrow_btn);
        mPayReceivedBtn = (Button) findViewById(R.id.payreceive_btn);

        //data Set
        mHeaderTxt.setText(getString(R.string.payment).toUpperCase(Locale.ENGLISH));
        mHeadeLeftBtnLay.setVisibility(View.VISIBLE);

        //API Call
        APIRequestHandler.getInstance().getMyPaymentHistoryResponse(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.paydone_btn:
                setAdapterData(1);
                break;
            case R.id.payescrow_btn:
                setAdapterData(2);
                break;
            case R.id.payreceive_btn:
                setAdapterData(3);
                break;
            case R.id.header_right_btn_lay:

                DialogManager.showBaseTwoBtnDialog(this, getString(R
                        .string.app_name), getString(R.string.del_payment_his), getString(R.string
                        .yes), getString(R.string.no), new DialogManagerTwoBtnCallback() {
                    @Override
                    public void onBtnOkClick(String mOkStr) {

                        APIRequestHandler.getInstance()
                                .getPaymentClearResponse(PaymentScreen.this);
                    }

                    @Override
                    public void onBtnCancelClick(String mCancelStr) {

                    }
                });

                break;
        }

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

    private void setData(PaymentHistoryResponse mPayResponse) {
        mPaymenyDone.clear();
        mPaymenyEscrow.clear();
        mPaymenyReceived.clear();
        mPaymenyDone = mPayResponse.getResult().getBuy();
        mPaymenyEscrow = mPayResponse.getResult().getEscrow();
        mPaymenyReceived = mPayResponse.getResult().getSell();

        mPaymentSentTxt.setText("$ " + GlobalMethods.getPriValWithTwoPoint(mPayResponse.getResult().getPayment_sent(), false));
        mPaymentRecTxt.setText("$ " + GlobalMethods.getPriValWithTwoPoint(mPayResponse.getResult().getPayment_received(), false));
        mPaymentToSentTxt.setText("$ " + GlobalMethods.getPriValWithTwoPoint(mPayResponse.getResult().getPayment_to_be_sent(), false));
        mPaymentToRecTxt.setText("$ " + GlobalMethods.getPriValWithTwoPoint(mPayResponse.getResult().getPayment_to_be_received(), false));

        //set Adapter
        setAdapterData(1);

    }

    private void setAdapterData(int buttonVisible) {
        ArrayList<PaymentBuySellEntity> mPayment = new ArrayList<PaymentBuySellEntity>();
        if (buttonVisible == 1) {
            mPayDoneBtn.setBackgroundResource(R.color.footer_green_two);
            mPayReceivedBtn.setBackgroundResource(R.color.green);
            mPayEscrowBtn.setBackgroundResource(R.color.green);
            mPayment = mPaymenyDone;
        } else if (buttonVisible == 2) {
            mPayDoneBtn.setBackgroundResource(R.color.green);
            mPayEscrowBtn.setBackgroundResource(R.color.footer_green_two);
            mPayReceivedBtn.setBackgroundResource(R.color.green);
            mPayment = mPaymenyEscrow;
        } else {
            mPayDoneBtn.setBackgroundResource(R.color.green);
            mPayEscrowBtn.setBackgroundResource(R.color.green);
            mPayReceivedBtn.setBackgroundResource(R.color.footer_green_two);
            mPayment = mPaymenyReceived;
        }
        if (mPayment != null && !mPayment.isEmpty()) {
//            mHeadeRightBtnLay.setVisibility(View.VISIBLE);
            mPaymentlist.setVisibility(View.VISIBLE);

            mAdapter = new PaymentsAdapter(this, mPayment, buttonVisible);
            mPaymentlist.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        } else {
            mHeadeRightBtnLay.setVisibility(View.INVISIBLE);
            mPaymentlist.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBackPressed() {
        previousScreen(DashboardScreen.class, true);
    }

    @Override
    public void onOkClick() {

    }
}
