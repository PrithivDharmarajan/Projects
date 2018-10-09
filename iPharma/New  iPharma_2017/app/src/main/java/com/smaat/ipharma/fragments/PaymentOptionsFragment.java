package com.smaat.ipharma.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.smaat.ipharma.R;
import com.smaat.ipharma.apiinterface.APIRequestHandler;
import com.smaat.ipharma.entity.MapResponse;
import com.smaat.ipharma.entity.OtpEntity;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.utils.AppConstants;
import com.smaat.ipharma.utils.DialogManager;
import com.smaat.ipharma.utils.GlobalMethods;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.smaat.ipharma.utils.AppConstants.DELIVERY_STATUS;
import static com.smaat.ipharma.utils.AppConstants.PHARMACY_ID;

/**
 * Created by admin on 2/10/2017.
 */

public class PaymentOptionsFragment extends BaseFragment {

    @BindView(R.id.cash_on_delivery_btn)
    Button mCashDelivery;

    @BindView(R.id.ipharma_money_btn)
    Button mIpharmaMoney;

    @BindView(R.id.debit_card_money)
    Button mDebitCard;

    @BindView(R.id.credit_card_btn)
    Button mCreditCard;

    @BindView(R.id.net_banking_btn)
    Button mNetBanking;




    @BindView(R.id.total_amount)
    TextView mTotalamt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.payment_options,
                container, false);
        ButterKnife.bind(this, rootview);
        setupUI(rootview);

        mTotalamt.setText(DELIVERY_STATUS.replace(getString(R.string.pay_rs),"").trim());

        return rootview;
    }


    @OnClick({R.id.cash_on_delivery_btn, R.id.ipharma_money_btn,R.id.debit_card_money,R.id.credit_card_btn,R.id.net_banking_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cash_on_delivery_btn:

                APIRequestHandler.getInstance().CashOnDelivery(
                        GlobalMethods.getUserID(getActivity()),
                        PHARMACY_ID,
                        AppConstants.SUCCESS_CODE, "Cash On Delivery", this);
                break;
            case R.id.ipharma_money_btn:
                DialogManager.showMsgPopup(getActivity(),"","Coming Soon");

                break;
            case R.id.debit_card_money:
                DialogManager.showMsgPopup(getActivity(),"","Coming Soon");

                break;
            case R.id.credit_card_btn:

                DialogManager.showMsgPopup(getActivity(),"","Coming Soon");
                break;
            case R.id.net_banking_btn:
                DialogManager.showMsgPopup(getActivity(),"","Coming Soon");

                break;
        }
    }


    @Override
    public void onRequestSuccess(Object responseObj) {

        OtpEntity response = (OtpEntity) responseObj;

        if (response.getStatus()
                .equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
            ((HomeScreen) getActivity()).replaceFragment(
                    new PharmacyListFragment());
            DialogManager.showToast(getActivity(),"Payment Added Sucessfully");

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeScreen) getActivity()).setToolbarTitle(getString(R.string.ipharma_money),getString(R.string.home));
    }
}
