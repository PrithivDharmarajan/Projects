package com.bridgellc.bridge.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.adapter.PaymentCardListAdapter;
import com.bridgellc.bridge.apiinterface.APIRequestHandler;
import com.bridgellc.bridge.entity.HomeSingleItemEntity;
import com.bridgellc.bridge.entity.PaypalCardEntity;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.model.BankAccDetailsResponse;
import com.bridgellc.bridge.model.BankAddAccountResponse;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

/**
 * Created by admin on 6/30/2016.
 */
public class PaymentCardListScreen extends BaseActivity implements View.OnClickListener, DialogMangerOkCallback {
    public static RelativeLayout mRatLay, mBottomLay;
    public static String mFooterOneTxt = "", mFooterTwoTxt = "", mFooterThreeTxt = "";
    public static int mFooterBtnCount;
    public static HomeSingleItemEntity mHomeSingleItemEntity;

    private RatingBar mRatingBar;
    private ImageView mVerifyImg;
    private ViewPager mImagePager;
    private int page_count = 0;
    private ArrayList<String> mImagesList = new ArrayList<String>();
    private ImageView mSlidepointerOne, mSlidepointerTwo,
            mSlidepointerThree;
    private RelativeLayout mFtrOneLay, mFtrTwoLay, mFtrThreeLay;
    private TextView mFooterOneBtn, mFooterTwoBtn, mFooterThreeBtn;
    private TextView mHeaderRightText;
    private RelativeLayout mBottomOneLay, mBottomTwoLay;
    private ArrayList<PaypalCardEntity> tempAccDetailsRes = new ArrayList<PaypalCardEntity>();
    private ArrayList<PaypalCardEntity> tempCardDetailsRes = new ArrayList<PaypalCardEntity>();
    private ArrayList<PaypalCardEntity> tempPaypalDetailsRes = new ArrayList<PaypalCardEntity>();
    private ArrayList<PaypalCardEntity> mAccDetailsRes = new ArrayList<PaypalCardEntity>();
    private ArrayList<PaypalCardEntity> mCardDetailsRes = new ArrayList<PaypalCardEntity>();
    private ArrayList<PaypalCardEntity> mPaypalDetailsRes = new ArrayList<PaypalCardEntity>();
    public static PaymentCardListAdapter card_list_adapter;
    public static ListView cardList;
    public static ArrayList<PaypalCardEntity> paypalCardEntity = new ArrayList<PaypalCardEntity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_list_screen);
        initComponents();

    }

    private void initComponents() {
        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);

        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        mHeadeRightBtnLay = (RelativeLayout) findViewById(R.id.header_right_btn_lay);


        mFooterOneLay = (RelativeLayout) findViewById(R.id.footer_one_lay);
        mFooterTwoLay = (RelativeLayout) findViewById(R.id.footer_two_lay);
        mFooterThreeLay = (RelativeLayout) findViewById(R.id.footer_three_lay);
        mFooterOneBtn = (Button) findViewById(R.id.footer_one_btn);
        mFooterTwoBtn = (Button) findViewById(R.id.footer_two_btn);


        mFooterOneLay.setVisibility(View.VISIBLE);
        mFooterTwoLay.setVisibility(View.GONE);
        mFooterThreeLay.setVisibility(View.GONE);


        mFooterOneBtn.setText(getString(R.string.add_new_debit));

        mHeaderRightText = (TextView) findViewById(R.id.header_right_txt);
        mHeaderRightText.setText(getString(R.string.choose_pref));
        mHeaderRightText.setTextSize(14);
        mHeaderRightText.setVisibility(View.VISIBLE);
        mHeaderRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((mHeaderRightText.getText().toString()).equals(getString(R.string.choose_pref))) {
                    mHeaderRightText.setText(getString(R.string.done));
                    card_list_adapter = new PaymentCardListAdapter(PaymentCardListScreen.this, paypalCardEntity, AppConstants.SUCCESS_CODE);

                    cardList.setAdapter(card_list_adapter);


                } else {
                    mHeaderRightText.setText(getString(R.string.choose_pref));
                    card_list_adapter = new PaymentCardListAdapter(PaymentCardListScreen.this, paypalCardEntity, AppConstants.FAILURE_CODE);
                    cardList.setAdapter(card_list_adapter);
                    PaypalCardEntity mFirstPosDetail = card_list_adapter.getItem(0);
                    if (mFirstPosDetail.getPaypalCardFlag().equalsIgnoreCase(AppConstants.FAILURE_CODE)) {

                        APIRequestHandler.getInstance().getAddBankAccResponse(getString(R.string.three), AppConstants.SUCCESS_CODE, "", "", "", "", "", "", "", "", "", mFirstPosDetail.getFirst_name(), mFirstPosDetail.getLast_name(), mFirstPosDetail.getPaypal_email(), mFirstPosDetail.getPaypal_id(), PaymentCardListScreen.this);

                    } else {

                        APIRequestHandler.getInstance().getAddBankAccResponse(getString(R.string.one), AppConstants.SUCCESS_CODE, mFirstPosDetail.getUser_name(), "", "", "", mFirstPosDetail.getOriginal_card_number(), mFirstPosDetail.getExp_month(), mFirstPosDetail.getExp_year(), mFirstPosDetail.getCvc(), mFirstPosDetail.getCard_id(), "", "", "", "", PaymentCardListScreen.this);

                    }
                }

            }
        });


        mHeadeLeftBtnLay.setVisibility(View.VISIBLE);
//        mHeadeRightBtnLay.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.add_payment).toUpperCase(Locale.ENGLISH));

        cardList = (ListView) findViewById(R.id.card_list);
        // cardList.setOnItemClickListener(this);

        //API call
        APIRequestHandler.getInstance().getBankAccCardDetailsResponse(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;

            case R.id.footer_one_btn:
                AppConstants.BANK_ACC_DET_BACK_SCREEN = getString(R.string.app_name);
                AppConstants.BANK_FROMLIST_BACK = AppConstants.SUCCESS_CODE;
                nextScreen(PaymentHomeScreen.class, true);
                break;
        }
    }


    @Override
    public void onRequestSuccess(Object responseObj) {
        if (responseObj instanceof BankAccDetailsResponse) {
            BankAccDetailsResponse mRes = (BankAccDetailsResponse) responseObj;
            if (mRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                tempCardDetailsRes.clear();
                tempAccDetailsRes.clear();
                tempPaypalDetailsRes.clear();

                tempCardDetailsRes = mRes.getResult().getDebit();
                tempAccDetailsRes = mRes.getResult().getBank();
                tempPaypalDetailsRes = mRes.getResult().getPaypal();
                // mCardDetailsRes.removeAll(mCardDetailsRes);
                //mPaypalDetailsRes.removeAll(mPaypalDetailsRes);
                //  mPaymentType = mRes.getResult().getPaymenttype();

                mCardDetailsRes.clear();
                mPaypalDetailsRes.clear();
                for (int i = 0; i < tempCardDetailsRes.size(); i++) {

                    PaypalCardEntity obj = tempCardDetailsRes.get(i);


                    String str = "1";
                    obj.setPaypalCardFlag(str);
                    mCardDetailsRes.add(obj);

                }
                for (int i = 0; i < tempPaypalDetailsRes.size(); i++) {

                    PaypalCardEntity obj = tempPaypalDetailsRes.get(i);


                    String str = "0";
                    obj.setPaypalCardFlag(str);
                    mPaypalDetailsRes.add(obj);

                }
                // paypalCardEntity.sort()
                paypalCardEntity.clear();
                paypalCardEntity.addAll(mCardDetailsRes);
                paypalCardEntity.addAll(mPaypalDetailsRes);
                Collections.sort(paypalCardEntity, new Comparator<PaypalCardEntity>() {
                    public int compare(PaypalCardEntity mPriorityEnOne, PaypalCardEntity mPriorityEnTwo) {
                        return mPriorityEnOne.getPriority().compareTo(mPriorityEnTwo.getPriority());
                    }
                });

                Collections.reverse(paypalCardEntity);

                PaymentCardListAdapter card_list_adapter = new PaymentCardListAdapter(PaymentCardListScreen.this, paypalCardEntity, "0");
                cardList.setAdapter(card_list_adapter);
// Sorting
               /* Collections.sort(paypalCardEntity, new Comparator<PaypalCardEntity>() {
                    @Override
                    public int compare(PaypalCardEntity entity2, PaypalCardEntity entity1)
                    {
                        if(Integer.parseInt(entity2.getPriority())>(Integer.parseInt(entity1.getPriority()))){
                            return 1;
                        } else {
                            return -1;
                        }

                    }
                });

*/

                //  setData();

            } else {
                if (mRes.getMessage().equalsIgnoreCase(getString(R.string.bank_not_added))) {

                } else {
                    DialogManager.showBasicAlertDialog(this,
                            mRes.getMessage(), PaymentCardListScreen.this);
                }
            }

        } else if (responseObj instanceof BankAddAccountResponse) {
            BankAddAccountResponse mRes = (BankAddAccountResponse) responseObj;
            if (mRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

            } else {
                DialogManager.showBasicAlertDialog(this,
                        mRes.getMessage(), PaymentCardListScreen.this);
            }
        }


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        previousScreen(SettingsScreen.class, true);
    }

    @Override
    public void onOkClick() {

    }
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        if (flagminusIcon.equals(AppConstants.SUCCESS_CODE)) {
//
//            PaypalCardEntity selItem=  getItem(position);
//
//
//            PaymentCardListScreen.paypalCardEntity.remove(selItem);
//
//            tempPayCardList.add(selItem);
//            tempPayCardList.addAll(PaymentCardListScreen.paypalCardEntity);
//
//
//            PaymentCardListScreen.card_list_adapter = new PaymentCardListAdapter(mContext, tempPayCardList, AppConstants.SUCCESS_CODE);
//
//            PaymentCardListScreen.cardList.setAdapter(PaymentCardListScreen.card_list_adapter);
//
//
//
//        } else {
//
//
//            if (paypalCardData.mtag.getPaypalCardFlag().equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
//
//                AppConstants.PAYMENT_TYPE = mContext.getString(R.string.three);
//                AppConstants.PAYPAL_EMAIL = paypalCardData.mtag.getPaypal_email();
//                AppConstants.PAYPAL_FIRSTNAME = paypalCardData.mtag.getFirst_name();
//                AppConstants.PAYPAL_LASTNAME = paypalCardData.mtag.getLast_name();
//                AppConstants.PAYPAL_ID = paypalCardData.mtag.getPaypal_id();
//
//                AppConstants.CARD_NAME = "";
//                AppConstants.EXPIRE_DATE = "";
//                AppConstants.SECURITY_CODE = "";
//                AppConstants.CARD_NUMBER = "";
//                AppConstants.CARD_NUMBER_WITHX = "";
//                AppConstants.CAED_ID = "";
//            } else {
//
//                AppConstants.PAYMENT_TYPE = mContext.getString(R.string.one);
//                AppConstants.CARD_NAME = paypalCardData.mtag.getUser_name();
//                AppConstants.EXPIRE_DATE = paypalCardData.mtag.getExp_month() + "/" + paypalCardData.mtag.getExp_year();
//                AppConstants.CARD_NUMBER_WITHX = paypalCardData.mtag.getCard_number();
//                AppConstants.CARD_NUMBER = paypalCardData.mtag.getOriginal_card_number();
//                AppConstants.SECURITY_CODE = paypalCardData.mtag.getCvc();
//                AppConstants.CAED_ID = paypalCardData.mtag.getCard_id();
//
//                AppConstants.PAYPAL_EMAIL = "";
//                AppConstants.PAYPAL_FIRSTNAME = "";
//                AppConstants.PAYPAL_LASTNAME = "";
//                AppConstants.PAYPAL_ID = "";
//            }
//            AppConstants.BANK_ACC_DET_BACK_SCREEN = mContext.getString(R.string.app_name);
//            ((BaseActivity) mContext).nextScreen(BankAccountDetails.class, true);
//
//        }
//    }
//    }
}


