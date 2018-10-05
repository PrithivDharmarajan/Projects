package com.bridgellc.bridge.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.entity.PaypalCardEntity;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.ui.BankAccountDetails;
import com.bridgellc.bridge.ui.PaymentCardListScreen;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;

import java.util.ArrayList;

/**
 * Created by admin on 6/30/2016.
 */
public class PaymentCardListAdapter extends BaseAdapter {

    private Context mContext;
    private Holder viewHolder;
    private ArrayList<PaypalCardEntity> payCardList;
    private ArrayList<PaypalCardEntity> tempPayCardList = new ArrayList<PaypalCardEntity>();

    private ImageView mSelectImg[];
    private boolean oldUniversityId = true;
    private String flagminusIcon = "0";

    public PaymentCardListAdapter(Context mContext,
                                  ArrayList<PaypalCardEntity> payCardList1, String flagminusIcon1) {
        this.mContext = mContext;
        this.payCardList = payCardList1;
        this.flagminusIcon = flagminusIcon1;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

//        if (convertView == null) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.payment_card_child, parent, false);
        viewHolder = new Holder(convertView);

        convertView.setTag(viewHolder);
//        } else {
        viewHolder = (Holder) convertView.getTag();
//        }


        if (payCardList.get(position).getPaypalCardFlag().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
            viewHolder.mCardNameTxt.setText(payCardList.get(position).getUser_name());

            if (payCardList.get(position).getCard_brand().equals("MasterCard")) {
                viewHolder.mCard_img.setImageResource(R.drawable.bridge_payment_edit_master);

            } else {
                viewHolder.mCard_img.setImageResource(R.drawable.bridge_payment_edit_visa);

            }
        } else {
            viewHolder.mCardNameTxt.setText(payCardList.get(position).getFirst_name());
            viewHolder.mCard_img.setBackgroundResource(R.drawable.bridge_payment_edit_paypal_card);
        }

        if (flagminusIcon.equals(AppConstants.SUCCESS_CODE)) {
            viewHolder.mIcon_img.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mIcon_img.setVisibility(View.INVISIBLE);
        }
        //   mSelectImg[position] = viewHolder.mCard_img;

        TagClass tagClass = new TagClass();
        tagClass.mtag = payCardList.get(position);
        viewHolder.mPayParentLay.setTag(tagClass);

        viewHolder.mPayParentLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TagClass paypalCardData = (TagClass) v.getTag();
                if (flagminusIcon.equals(AppConstants.SUCCESS_CODE)) {

                    DialogManager.showBasicAlertDialog(mContext, mContext.getString(R.string.are_primary_card), new DialogMangerOkCallback() {
                        @Override
                        public void onOkClick() {
                            PaypalCardEntity selItem = getItem(position);


                            PaymentCardListScreen.paypalCardEntity.remove(selItem);

                            tempPayCardList.add(selItem);
                            tempPayCardList.addAll(PaymentCardListScreen.paypalCardEntity);

                            PaymentCardListScreen.paypalCardEntity.clear();
                            PaymentCardListScreen.paypalCardEntity.addAll(tempPayCardList);
                            //  PaymentCardListScreen.card_list_adapter.notifyDataSetChanged();
                            PaymentCardListScreen.card_list_adapter = new PaymentCardListAdapter(mContext, PaymentCardListScreen.paypalCardEntity, AppConstants.SUCCESS_CODE);
                            PaymentCardListScreen.cardList.setAdapter(PaymentCardListScreen.card_list_adapter);

                        }
                    });


                } else {


                    if (paypalCardData.mtag.getPaypalCardFlag().equalsIgnoreCase(AppConstants.FAILURE_CODE)) {

                        AppConstants.PAYMENT_TYPE = mContext.getString(R.string.three);
                        AppConstants.PAYPAL_EMAIL = paypalCardData.mtag.getPaypal_email();
                        AppConstants.PAYPAL_FIRSTNAME = paypalCardData.mtag.getFirst_name();
                        AppConstants.PAYPAL_LASTNAME = paypalCardData.mtag.getLast_name();
                        AppConstants.PAYPAL_ID = paypalCardData.mtag.getPaypal_id();

                        AppConstants.CARD_NAME = "";
                        AppConstants.EXPIRE_DATE = "";
                        AppConstants.SECURITY_CODE = "";
                        AppConstants.CARD_NUMBER = "";
                        AppConstants.CARD_NUMBER_WITHX = "";
                        AppConstants.CAED_ID = "";
                    } else {

                        AppConstants.PAYMENT_TYPE = mContext.getString(R.string.one);
                        AppConstants.CARD_NAME = paypalCardData.mtag.getUser_name();
                        AppConstants.EXPIRE_DATE = paypalCardData.mtag.getExp_month() + "/" + paypalCardData.mtag.getExp_year();
                        AppConstants.CARD_NUMBER_WITHX = paypalCardData.mtag.getCard_number();
                        AppConstants.CARD_NUMBER = paypalCardData.mtag.getOriginal_card_number();
                        AppConstants.SECURITY_CODE = paypalCardData.mtag.getCvc();
                        AppConstants.CAED_ID = paypalCardData.mtag.getCard_id();

                        AppConstants.PAYPAL_EMAIL = "";
                        AppConstants.PAYPAL_FIRSTNAME = "";
                        AppConstants.PAYPAL_LASTNAME = "";
                        AppConstants.PAYPAL_ID = "";
                    }
                    AppConstants.BANK_ACC_DET_BACK_SCREEN = mContext.getString(R.string.app_name);
                    ((BaseActivity) mContext).nextScreen(BankAccountDetails.class, true);

                }
            }
        });

        return convertView;

    }

    class TagClass {
        private PaypalCardEntity mtag;
    }

    class Holder {
        ImageView mCard_img, mIcon_img;

        TextView mCardNameTxt;
        RelativeLayout mPayParentLay;

        public Holder(View mView) {
            mIcon_img = (ImageView) mView.findViewById(R.id.icon_img);
            mCardNameTxt = (TextView) mView.findViewById(R.id.card_name);
            mCard_img = (ImageView) mView.findViewById(R.id.card_img);
            mPayParentLay = (RelativeLayout) mView.findViewById(R.id.pay_parent_lay);
        }
    }

    @Override
    public int getCount() {
        return payCardList.size();
    }

    @Override
    public PaypalCardEntity getItem(int position) {
        return payCardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}


